package com.qx.guli.service.statistics.feign.fallback;

import com.qx.guli.common.base.result.R;
import com.qx.guli.service.statistics.feign.EduService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Classname EduCourseServiceFallBack
 * @Description TODO
 * @Date 2020/6/29 22:55
 * @Created by 卿星
 */
@Service
@Slf4j
public class EduServiceFallBack implements EduService {

    @Override
    public R countCourseNum(String day) {
        log.error("countCourseNum熔断");
        return R.ok().data("courserCount",0);
    }

    @Override
    public R countVideoeNum(String day) {
        log.error("countVideoeNum熔断");
        return R.ok().data("playCount",0);
    }
}
