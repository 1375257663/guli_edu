package com.qx.guli.service.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qx.guli.service.base.dto.CourseDto;
import com.qx.guli.service.edu.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qx.guli.service.edu.entity.form.CourseInfoForm;
import com.qx.guli.service.edu.entity.vo.*;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author qx
 * @since 2020-06-04
 */
public interface CourseService extends IService<Course> {

    String saveCourseInform(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseInfo(String courseId);

    void updateCourseInform(CourseInfoForm courseInfoForm);

    IPage<CourseVo> pageSelect(Long current, Long size, CourseQueryVo courseVo);

    boolean removeCourseAndCoverById(String id, String cover);

    CoursePublishVo getCoursePublishVoById(String id);

    boolean PublishCourseById(String id);

    List<Course> webSelectList(WebCourseQueryVo webCourseQueryVo);

    WebCourseVo getWebCourseVoByCourseId(String courseId);

//    @Cacheable(key = "'index'" ,value = "listHotCourses")
    List<Course> listHotCourses();

    CourseDto getCourseDtoById(String id);

    void updateCourseBuyCount(String courseId);

    Integer countCourseNum(String day);
}
