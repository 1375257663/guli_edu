package com.qx.guli.service.ucenter.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname LoginVo
 * @Description TODO
 * @Date 2020/6/24 15:35
 * @Created by 卿星
 */
@Data
public class LoginVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String mobile;
    private String password;

}
