package com.qx.guli.service.base.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname MemberDto
 * @Description TODO
 * @Date 2020/6/26 12:00
 * @Created by 卿星
 */
@Data
public class MemberDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;//会员id
    private String mobile;//手机号
    private String nickname;//昵称

}
