package com.qx.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname SubjectVo
 * @Description TODO
 * @Date 2020/6/10 16:04
 * @Created by 卿星
 */
@Data
public class SubjectVo implements Serializable {

    private static long serialVersionUID = 1L;

    private String id;

    private String title;

    private Integer sort;

    private List<SubjectVo> children = new ArrayList<>();

}
