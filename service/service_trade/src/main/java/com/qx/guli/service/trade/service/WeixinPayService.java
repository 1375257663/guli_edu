package com.qx.guli.service.trade.service;

import java.util.Map;

/**
 * @Classname WeixinPayService
 * @Description TODO
 * @Date 2020/6/27 21:55
 * @Created by 卿星
 */
public interface WeixinPayService {

    Map<String, Object> createNative(String orderNo, String remoteAddr);

}
