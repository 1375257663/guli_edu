package com.qx.guli.service.edu.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qx.guli.service.base.dto.CourseDto;
import com.qx.guli.service.edu.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qx.guli.service.edu.entity.vo.CoursePublishVo;
import com.qx.guli.service.edu.entity.vo.CourseVo;
import com.qx.guli.service.edu.entity.vo.WebCourseVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author qx
 * @since 2020-06-04
 */
public interface CourseMapper extends BaseMapper<Course> {

    /**
     *
     * @param courseVoPage  MybatisPlus会自动组装分页参数
     * @param courseVoQueryWrapper  如果配置了@Param(Constants.WRAPPER)参数，
     *                              MybatisPlus会自动解析并组装查询条件，
     *                              但在xml配置文件中要配置${ew.customSqlSegment}
     * @return
     */
    IPage<CourseVo> selectPageByCourseVo(Page<CourseVo> courseVoPage,@Param(Constants.WRAPPER) QueryWrapper<CourseVo> courseVoQueryWrapper);

    CoursePublishVo selectCoursePublishVoById(String id);

    WebCourseVo selectWebCourseVoById(String courseId);

    CourseDto selectCourseDtoById(String id);

    Integer selectCountByDay(String day);
}
