package com.qx.guli.service.trade.feign;

import com.qx.guli.common.base.result.R;
import com.qx.guli.service.base.dto.CourseDto;
import com.qx.guli.service.trade.feign.fallback.EduCourseServiceFallback;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @Classname EduCourseService
 * @Description feign远程调用
 * @Date 2020/6/26 13:53
 * @Created by 卿星
 */
@FeignClient(value = "service-edu",fallback = EduCourseServiceFallback.class)
public interface EduCourseService {

    @GetMapping(value = "/api/edu/course/inner/get-course-dto/{courseId}")
    CourseDto getCourseDtoById(@PathVariable(value = "courseId") String courseId);

    @PutMapping("/api/edu/course/update-course-buyCount/{courseId}")
    R updateCourseBuyCount(@PathVariable(value = "courseId") String courseId);

}
