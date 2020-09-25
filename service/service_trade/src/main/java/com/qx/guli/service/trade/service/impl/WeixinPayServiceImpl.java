package com.qx.guli.service.trade.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.qx.guli.common.base.result.ResultCodeEnum;
import com.qx.guli.common.base.util.ExceptionUtils;
import com.qx.guli.common.base.util.HttpClientUtils;
import com.qx.guli.service.base.exception.GuliException;
import com.qx.guli.service.trade.entity.Order;
import com.qx.guli.service.trade.service.OrderService;
import com.qx.guli.service.trade.service.WeixinPayService;
import com.qx.guli.service.trade.util.WeixinPayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname WeixinPayServiceImpl
 * @Description 微信调用service层
 * @Date 2020/6/27 21:55
 * @Created by 卿星
 */
@Service
@Slf4j
public class WeixinPayServiceImpl implements WeixinPayService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private WeixinPayProperties weixinPayProperties;


    @Override
    public Map<String, Object> createNative(String orderNo, String remoteAddr) {
        Map<String, String> paramsMap = null;

        try {
            // 查询订单
            Order order = orderService.getOrderByOrderNo(orderNo);
            // 查询订单是否下单
            if(order.getStatus() == 1){
                throw new GuliException(ResultCodeEnum.ORDER_EXIST_ERROR);
            }

            // 组装微信需要的参数
            paramsMap = new HashMap<>();
            paramsMap.put("appid",weixinPayProperties.getAppId());// 公众账号ID
            paramsMap.put("mch_id",weixinPayProperties.getPartner());// 商户号
            paramsMap.put("nonce_str", WXPayUtil.generateNonceStr());// 随机字符串
            paramsMap.put("body", order.getCourseTitle());// 随机字符串
            paramsMap.put("out_trade_no", orderNo);// 随机字符串:商户系统内部订单号，要求32个字符内
            //注意，这里必须使用字符串类型的参数（总金额：分）
            paramsMap.put("total_fee", order.getTotalFee().intValue() + "");// 标价金额 分
            paramsMap.put("spbill_create_ip", remoteAddr);// 终端IP
            paramsMap.put("notify_url", weixinPayProperties.getNotifyUrl());// 通知地址:支付结果通知的回调地址
            paramsMap.put("trade_type", "NATIVE ");// 交易类型
            paramsMap.put("product_id", order.getId());// 商品ID

            // 将参数map转为xml形式，生成带有签名的xml格式字符串
            String xmlParams = WXPayUtil.generateSignedXml(paramsMap,weixinPayProperties.getPartnerKey());
            log.info("\n xmlParams：\n" + xmlParams);

            // 调用微信接口，预支付，设置请求参数，并发送POST请求
            String preUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            HttpClientUtils clientUtils = new HttpClientUtils(preUrl);
            clientUtils.setXmlParam(xmlParams);
            clientUtils.post();

            String resultXml = clientUtils.getContent();
            log.info("\n resultXml：\n" + resultXml);
            // 将xml转为map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);

            // 错误处理
            if("FAIL".equals(resultMap.get("return_code")) || "FAIL".equals(resultMap.get("result_code"))){
                log.error("微信支付统一下单错误 - "
                        + "return_code: " + resultMap.get("return_code")
                        + "return_msg: " + resultMap.get("return_msg")
                        + "result_code: " + resultMap.get("result_code")
                        + "err_code: " + resultMap.get("err_code")
                        + "err_code_des: " + resultMap.get("err_code_des"));
                throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
            }

            // 封装返回数据
            Map<String, Object> endMap = new HashMap<>();
            endMap.put("result_code",resultMap.get("result_code"));// 响应码
            endMap.put("code_url",resultMap.get("code_url"));// 生成二维码的URL
            endMap.put("course_id",order.getCourseId());// 课程ID
            endMap.put("total_fee",order.getTotalFee());// 总金额，分
            endMap.put("out_trade_no", orderNo);// 订单号

            return endMap;
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }
    }

}
