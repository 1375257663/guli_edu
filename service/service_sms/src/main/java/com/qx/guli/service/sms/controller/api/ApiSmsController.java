package com.qx.guli.service.sms.controller.api;

import com.aliyuncs.exceptions.ClientException;
import com.qx.guli.common.base.result.R;
import com.qx.guli.common.base.result.ResultCodeEnum;
import com.qx.guli.common.base.util.FormUtils;
import com.qx.guli.common.base.util.RandomUtils;
import com.qx.guli.service.base.exception.GuliException;
import com.qx.guli.service.sms.service.SmsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.concurrent.TimeUnit;

/**
 * @Classname ApiSmsController
 * @Description TODO
 * @Date 2020/6/23 15:24
 * @Created by 卿星
 */
@Api(description = "短信管理")
//@CrossOrigin
@Slf4j
@RequestMapping("/api/sms")
@RestController
public class ApiSmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("send/{mobile}")
    public R getCode(@PathVariable String mobile) throws ClientException {

        // 效验手机号是否合法
        if(StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)){
            log.error("手机号不合法，手机号：" + mobile);
            throw new GuliException(ResultCodeEnum.LOGIN_PHONE_ERROR);
//            return R.error().message(ResultCodeEnum.LOGIN_PHONE_ERROR.getMessage());
        }
        // 生成随机验证码
        String code = RandomUtils.getSixBitRandom();
        // 发送验证码
        smsService.send(mobile,code);
        // 将手机号和验证码保存到redis中
        redisTemplate.opsForValue().set(mobile, code, 5, TimeUnit.MINUTES);

        return R.ok().message("短信发送成功!");
    }

}
