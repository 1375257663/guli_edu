package com.qx.guli.service.edu.service;

import com.qx.guli.service.edu.entity.CourseCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qx.guli.service.edu.entity.vo.CourseCollectVo;

import java.util.List;

/**
 * <p>
 * 课程收藏 服务类
 * </p>
 *
 * @author qx
 * @since 2020-06-04
 */
public interface CourseCollectService extends IService<CourseCollect> {

    List<CourseCollectVo> listByMemberId(String memberId);

    boolean saveByCourseId(String courseId, String memberId);

    boolean isCollectByCourseId(String courseId, String memberId);

    boolean removeByCourseId(String courseId, String memberId);
}
