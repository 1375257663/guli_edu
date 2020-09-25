package com.qx.guli.service.ucenter.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname RegisterVo
 * @Description 注册VO
 * @Date 2020/6/23 19:19
 * @Created by 卿星
 */
@Data
public class RegisterVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nickname;
    private String mobile;
    private String password;
    private String code;

}
