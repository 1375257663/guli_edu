package com.qx.guli.service.statistics.feign;

import com.qx.guli.common.base.result.R;
import com.qx.guli.service.statistics.feign.fallback.EduServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Classname EduCourseService
 * @Description TODO
 * @Date 2020/6/29 22:53
 * @Created by 卿星
 */
@Service
@FeignClient(value = "edu-service",fallback = EduServiceFallBack.class)
public interface EduService {

    @GetMapping(value = "/admin/edu/course/count-course-num/{day}")
    public R countCourseNum(@PathVariable("day") String day);

    @GetMapping(value = "/admin/edu/video/count-video-view-num/{day}")
    public R countVideoeNum(@PathVariable("day") String day);
}
