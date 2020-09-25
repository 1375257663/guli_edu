package com.qx.guli.service.edu.entity.vo;

import com.qx.guli.service.base.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Classname OrderCollectVo
 * @Description 我的收藏
 * @Date 2020/6/27 15:16
 * @Created by 卿星
 */
@Data
public class CourseCollectVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String courseId;
//    private String memberId;
    private String cover;
    private String title;
    private BigDecimal price;
    private String teacherName;
    private String gmtCreate;
//    private String gmtModified;
}
