package com.qx.guli.service.ucenter.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Classname UcenterProperties
 * @Description TODO
 * @Date 2020/6/25 11:09
 * @Created by 卿星
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx.open")
public class UcenterProperties {

    private String appId;
    private String appSecret;
    private String redirectUri;

}
