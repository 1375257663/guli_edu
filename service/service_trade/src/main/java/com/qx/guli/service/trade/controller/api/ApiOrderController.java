package com.qx.guli.service.trade.controller.api;


import com.qx.guli.common.base.result.R;
import com.qx.guli.common.base.result.ResultCodeEnum;
import com.qx.guli.common.base.util.JwtInfo;
import com.qx.guli.common.base.util.JwtUtils;
import com.qx.guli.service.base.dto.CourseDto;
import com.qx.guli.service.base.dto.MemberDto;
import com.qx.guli.service.trade.entity.Order;
import com.qx.guli.service.trade.feign.EduCourseService;
import com.qx.guli.service.trade.feign.UcenterMemberService;
import com.qx.guli.service.trade.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author qx
 * @since 2020-06-26
 */
@RestController
@RequestMapping("/api/trade/order")
@Api(description = "网站订单管理")
//@CrossOrigin //跨域
@Slf4j
public class ApiOrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("新增订单")
    @PostMapping("auth/save/{courseId}")
    public R save(@PathVariable String courseId, HttpServletRequest request) {
        // 组装Order数据
        //
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        String orderId = orderService.saveOrder(courseId,jwtInfo.getId());
        return R.ok().data("orderId",orderId);
    }

    @ApiOperation( "判断课程是否购买")
    @GetMapping("auth/is-buy/{courseId}")
    public R isBuyByCourseId(@PathVariable String courseId, HttpServletRequest request) {
        // 必须是自己的订单才能查询
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        boolean isBuy = orderService.isBuyByCourseId(courseId,jwtInfo.getId());
        return R.ok().data("flag",isBuy);
    }

    @ApiOperation("通过订单ID查询订单")
    @GetMapping("auth/get/{orderId}")
    public R get(@PathVariable String orderId, HttpServletRequest request) {
        // 必须是自己的订单才能查询
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        Order order = orderService.getByOrderIdAndMemberId(orderId,jwtInfo.getId());
        return R.ok().data("item",order);
    }

    @ApiOperation(value = "获取当前用户订单列表")
    @GetMapping("auth/list")
    public R list(HttpServletRequest request) {
        // 必须是自己的订单才能查询
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        List<Order> orderList = orderService.listByMemberId(jwtInfo.getId());
        return R.ok().data("items",orderList);
    }

    @ApiOperation(value = "删除订单")
    @DeleteMapping("auth/remove/{orderId}")
    public R remove(@PathVariable String orderId, HttpServletRequest request) {
        // 必须是自己的订单才能删除
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        boolean flag = orderService.removeByMemberAndOrderId(orderId,jwtInfo.getId());
        if(flag){
            return R.ok().message("删除订单成功");
        }
        return R.error().message("删除订单失败");
    }

    @GetMapping("/query-pay-status/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {

        boolean flag = orderService.queryPayStatus(orderNo);
        if(flag){
            return R.ok().message("支付成功");
        }
        // 正在支付中
        return R.setResult(ResultCodeEnum.PAY_RUN);
    }
}

