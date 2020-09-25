package com.qx.guli.service.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.qx.guli.common.base.result.ResultCodeEnum;
import com.qx.guli.service.base.dto.CourseDto;
import com.qx.guli.service.base.dto.MemberDto;
import com.qx.guli.service.base.exception.GuliException;
import com.qx.guli.service.trade.entity.Order;
import com.qx.guli.service.trade.entity.PayLog;
import com.qx.guli.service.trade.feign.EduCourseService;
import com.qx.guli.service.trade.feign.UcenterMemberService;
import com.qx.guli.service.trade.mapper.OrderMapper;
import com.qx.guli.service.trade.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qx.guli.service.trade.service.PayLogService;
import com.qx.guli.service.trade.util.OrderNoUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author qx
 * @since 2020-06-26
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduCourseService courseService;
    @Autowired
    private UcenterMemberService ucenterMemberService;
    @Autowired
    private PayLogService payLogService;

    @Override
    public String saveOrder(String courseId, String memberId) {

        // 查询是否已经下过订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId).eq("member_id",memberId);
        Order order = baseMapper.selectOne(queryWrapper);
        // 已下单，直接返回订单号
        if(order != null){
            return order.getId();
        }
        // 查询数据
        CourseDto courseDto = courseService.getCourseDtoById(courseId);
        MemberDto memberDto = ucenterMemberService.getMemberDtoByMemberId(memberId);
        // 判断
        if(courseDto == null){
            log.error("该课程不存在，课程ID：" + courseId);
            throw new GuliException(ResultCodeEnum.COURSE_LOSE);
        }
        if(memberDto == null){
            log.error("该用户不存在，用户ID ：" + memberId);
            throw new GuliException(ResultCodeEnum.MEMBER_NOT_EXIST);
        }

        // 组装数据
        order = new Order();

        // 订单号
        order.setOrderNo(OrderNoUtils.getOrderNo());
        // 订单金额（分）
        order.setTotalFee(courseDto.getPrice().multiply(new BigDecimal(100)));

        order.setCourseCover(courseDto.getCover());
        order.setCourseId(courseDto.getId());
        order.setCourseTitle(courseDto.getTitle());
        order.setTeacherName(courseDto.getTeacherName());

        order.setMemberId(memberId);
        order.setMobile(memberDto.getMobile());
        order.setNickname(memberDto.getNickname());
        // 1:微信支付 2：支付宝支付
        order.setPayType(1);
        // 0:未支付 1：已支付
        order.setStatus(0);

        baseMapper.insert(order);
        return order.getId();
    }

    @Override
    public Order getByOrderIdAndMemberId(String orderId, String memberId) {
        // 组装条件
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId).eq("member_id",memberId);
        Order order = baseMapper.selectOne(queryWrapper);
        return order;
    }

    @Override
    public boolean isBuyByCourseId(String courseId, String memberId) {
        // 组装条件
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        // 通过课程ID，会员ID，支付状态判断是否购买
        queryWrapper.eq("course_id",courseId).eq("member_id",memberId).eq("status", 1);
        Integer count = baseMapper.selectCount(queryWrapper);
        // Integer需要转为int类型执行数字比较
        return count.intValue() == 1;

    }

    @Override
    public List<Order> listByMemberId(String memberId) {
        // 组装条件
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        // 按时间倒序
        queryWrapper.eq("member_id",memberId).orderByDesc("gmt_create");
        List<Order> orderList = baseMapper.selectList(queryWrapper);
        return orderList;
    }

    @Override
    public boolean removeByMemberAndOrderId(String orderId, String memberId) {
        // 组装条件
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        // 按时间倒序
        queryWrapper.eq("member_id",memberId).eq("id",orderId);

        return this.remove(queryWrapper);
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        // 组装条件
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>().eq("order_no",orderNo);

        return baseMapper.selectOne(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOrder(Order order, Map<String, String> resultMap) {
        // 设置订单支付状态
        order.setStatus(1);
        // 数据库更新
        baseMapper.updateById(order);

        // 更新支付日志
        PayLog payLog = new PayLog();
        // 复制相同属性
        BeanUtils.copyProperties(order,payLog);

        payLog.setPayTime(new Date());
        payLog.setTradeState("已支付");
        payLog.setTransactionId(resultMap.get("transaction_id"));
        payLog.setTotalFee(Long.parseLong(order.getTotalFee().intValue() + ""));
        // 设置其他属性
        Gson gson = new Gson();
        String resultXml = gson.toJson(resultMap);
        payLog.setAttr(resultXml);
        // 插入数据
        payLogService.save(payLog);

        // 更新课程销量
        courseService.updateCourseBuyCount(order.getCourseId());
    }

    @Override
    public boolean queryPayStatus(String orderNo) {

        QueryWrapper<Order> wrapper = new QueryWrapper<Order>().eq("order_no",orderNo);
        Order order = this.getOne(wrapper);
        // 查询订单是否支付
        if(order != null && order.getStatus() == 1){
            return true;
        }
        return false;
    }

}
