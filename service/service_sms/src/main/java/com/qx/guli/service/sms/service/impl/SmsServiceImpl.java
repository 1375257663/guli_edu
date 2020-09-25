package com.qx.guli.service.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.qx.guli.common.base.result.ResultCodeEnum;
import com.qx.guli.service.base.exception.GuliException;
import com.qx.guli.service.sms.service.SmsService;
import com.qx.guli.service.sms.util.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @Classname SmsServiceImpl
 * @Description TODO
 * @Date 2020/6/23 15:26
 * @Created by 卿星
 */
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsProperties smsProperties;

    @Override
    public void send(String mobile, String code) throws ClientException {
        sendSms(mobile,code);
    }

    private void sendSms(String phoneNum,String code) throws ClientException {
        // 创建client对象
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou",
                smsProperties.getKeyId(),
                smsProperties.getKeySecret());
        // 创建client对象
        IAcsClient client = new DefaultAcsClient(profile);
        // 设置请求参数
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", smsProperties.getRegionId());
        request.putQueryParameter("PhoneNumbers", phoneNum);
        request.putQueryParameter("SignName", smsProperties.getSignName());
        request.putQueryParameter("TemplateCode", smsProperties.getTemplateCode());
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");

        // 发送响应并返回结果
        CommonResponse response = client.getCommonResponse(request);
        // 得到响应数据
        String resultData = response.getData();
        // 创建gson对象
        Gson gson = new Gson();
        // 将Json响应转为map
        HashMap hashMap = gson.fromJson(resultData, HashMap.class);
        // 获取响应结果
        String resultCode = (String) hashMap.get("Code");
        String message = (String) hashMap.get("Message");
        // 控制所有短信流向限制（同一手机号：一分钟一条、一个小时五条、一天十条）
        if(resultCode.equals("isv.BUSINESS_LIMIT_CONTROL")){
            log.error("短信发送过于频繁 " + "【code】" + code + ", 【message】" + message);
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
        }

        if(!resultCode.equals("OK")){
            log.error("短信发送失败 " + " - code: " + code + ", message: " + message);
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
        }

    }
}
