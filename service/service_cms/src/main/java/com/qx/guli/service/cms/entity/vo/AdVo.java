package com.qx.guli.service.cms.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname AdVo
 * @Description TODO
 * @Date 2020/6/21 17:52
 * @Created by 卿星
 */
@Data
public class AdVo implements Serializable {

    private static final long serialVersionUID=1L;
    private String id;
    private String title;
    private Integer sort;
    private String type;
}