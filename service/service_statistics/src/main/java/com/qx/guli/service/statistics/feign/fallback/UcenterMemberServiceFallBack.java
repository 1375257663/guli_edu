package com.qx.guli.service.statistics.feign.fallback;

import com.qx.guli.common.base.result.R;
import com.qx.guli.service.statistics.feign.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Classname UcenterMemberServiceImpl
 * @Description TODO
 * @Date 2020/6/29 16:46
 * @Created by 卿星
 */
@Service
@Slf4j
public class UcenterMemberServiceFallBack implements UcenterMemberService {
    @Override
    public R countRegisterNum(String day) {
        log.error("熔断");
        return R.ok().data("registerCount",0);
    }
}
