package com.qx.guli.service.trade.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Classname WeixinPayProperties
 * @Description TODO
 * @Date 2020/6/27 21:45
 * @Created by 卿星
 */
@ConfigurationProperties(prefix = "weixin.pay")
@Component
@Data
public class WeixinPayProperties {

    private String appId;
    private String partner;
    private String partnerKey;
    private String notifyUrl;

}
