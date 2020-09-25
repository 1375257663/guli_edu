package com.qx.guli.service.trade.feign.fallback;

import com.qx.guli.common.base.result.R;
import com.qx.guli.service.base.dto.CourseDto;
import com.qx.guli.service.trade.feign.EduCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

/**
 * @Classname EduCourseServiceImpl
 * @Description TODO
 * @Date 2020/6/26 13:53
 * @Created by 卿星
 */
@Service
@Slf4j
public class EduCourseServiceFallback implements EduCourseService {

    @Override
    public CourseDto getCourseDtoById(String courseId) {
        log.info("熔断保护");
        return null;
    }

    @Override
    public R updateCourseBuyCount(String courseId) {
        log.info("熔断保护");
        return null;
    }
}
