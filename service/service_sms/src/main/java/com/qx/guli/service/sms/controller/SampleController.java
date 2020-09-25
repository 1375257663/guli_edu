package com.qx.guli.service.sms.controller;

import com.qx.guli.common.base.result.R;
import com.qx.guli.service.sms.util.SmsProperties;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname SampleController
 * @Description nacos测试类
 * @Date 2020/6/30 23:11
 * @Created by 卿星
 */
@Api(description = "测试nacos配置")
@RestController
@RequestMapping("/sms/sample")
@RefreshScope   // 使@Value注解配置的属性支持nacos配置后动态修改
public class SampleController {

    @Value("${aliyun.sms.signName}")
    private String signName;

    @Autowired
    private SmsProperties smsProperties;

    @GetMapping("/test/get/signName")
    public R getValueBy(){
        return R.ok().data("signName",signName);
    }

    @GetMapping("/test/get/smsProperties")
    public R getValue(){
        return R.ok().data("smsProperties",smsProperties);
    }



}
