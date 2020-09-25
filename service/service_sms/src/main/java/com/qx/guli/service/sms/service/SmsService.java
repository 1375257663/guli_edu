package com.qx.guli.service.sms.service;

import com.aliyuncs.exceptions.ClientException;

/**
 * @Classname SmsService
 * @Description TODO
 * @Date 2020/6/23 15:26
 * @Created by 卿星
 */
public interface SmsService {
    void send(String mobile, String code) throws ClientException;
}
