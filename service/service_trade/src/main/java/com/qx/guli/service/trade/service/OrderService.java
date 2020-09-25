package com.qx.guli.service.trade.service;

import com.qx.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author qx
 * @since 2020-06-26
 */
public interface OrderService extends IService<Order> {

    String saveOrder(String courseId, String memberId);

    Order getByOrderIdAndMemberId(String orderId, String memberId);

    boolean isBuyByCourseId(String courseId, String memberId);

    List<Order> listByMemberId(String memberId);

    boolean removeByMemberAndOrderId(String orderId, String memberId);

    Order getOrderByOrderNo(String orderNo);

    void updateOrder(Order order, Map<String, String> resultMap);

    boolean queryPayStatus(String orderNo);
}
