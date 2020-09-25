package com.qx.guli.service.trade.controller.api;

import com.github.wxpay.sdk.WXPayUtil;
import com.qx.guli.common.base.result.R;
import com.qx.guli.common.base.util.JwtInfo;
import com.qx.guli.common.base.util.JwtUtils;
import com.qx.guli.common.base.util.StreamUtils;
import com.qx.guli.service.trade.entity.Order;
import com.qx.guli.service.trade.service.OrderService;
import com.qx.guli.service.trade.service.WeixinPayService;
import com.qx.guli.service.trade.util.WeixinPayProperties;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname ApiWeixinPayController
 * @Description 微信支付
 * @Date 2020/6/27 22:34
 * @Created by 卿星
 */
@RestController
@RequestMapping("/api/trade/weixin-pay")
@Api(description = "网站微信支付")
//@CrossOrigin //跨域
@Slf4j
public class ApiWeixinPayController {

    @Autowired
    private WeixinPayService weixinPayService;
    @Autowired
    private WeixinPayProperties weixinPayProperties;
    @Autowired
    private OrderService orderService;

    @PostMapping("create-native/{orderNo}")
    public R createNative(@PathVariable String orderNo, HttpServletRequest request) {

        Map<String, Object> resultMap = weixinPayService.createNative(orderNo, request.getRemoteAddr());

        return R.ok().data(resultMap);
    }

    /**
     * 支付回调
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("callback/notify")
    public String callbackNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("callback-notify被调用");
        // 解析数据
        String resultXml = StreamUtils.inputStream2String(request.getInputStream(),"UTF-8");
        System.out.println("resultXml = /n" + resultXml);
        // 转为map
        Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);

        // 返回map
        Map<String, Object> returnMap = new HashMap<>();
        // 效验结果,验签
        if("SUCCESS".equals(resultMap.get("return_code")) && "SUCCESS".equals(resultMap.get("result_code"))){

            // 签名验证
            if(WXPayUtil.isSignatureValid(resultXml,weixinPayProperties.getPartnerKey())){
                // 并校验返回的订单金额是否与商户侧的订单金额一致
                // 通过订单号获取订单
                Order order = orderService.getOrderByOrderNo(resultMap.get("out_trade_no"));

                // 将数据库取出订单金额和微信返回金额比较
                if(order != null && order.getTotalFee().intValue() == Integer.parseInt(resultMap.get("total_fee"))){
                    // 设置响应返回格式
                    response.setContentType("text/xml");
                    // 商户处理后同步返回给微信参数
                    returnMap.put("return_code","SUCCESS");
                    returnMap.put("return_msg","OK");
                    String returnXml = WXPayUtil.mapToXml(resultMap);
                    // 判断是否已支付
                    // 幂等性：无论调用多少次请求，结果都不会改变
                    if(order.getStatus() == 0){
                        // 更新支付状态
                        orderService.updateOrder(order,resultMap);
                        log.warn("支付成功，通知已处理！");
                    }else {
                        log.warn("通知已处理");
                    }
                    return returnXml;
                }
            }

        }

        returnMap.put("return_code","FAIL");
        returnMap.put("return_msg","");
        String returnXml = WXPayUtil.mapToXml(resultMap);
        response.setContentType("text/xml");
        log.warn("校验失败");
        return returnXml;
    }


}
