package com.qx.guli.service.statistics.feign;

import com.qx.guli.common.base.result.R;
import com.qx.guli.service.statistics.feign.fallback.UcenterMemberServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Classname UcenterMemberService
 * @Description TODO
 * @Date 2020/6/29 16:42
 * @Created by 卿星
 */
@Service
@FeignClient(value = "service-ucenter",fallback = UcenterMemberServiceFallBack.class)
public interface UcenterMemberService {

    @GetMapping(value = "/admin/ucenter/member/count-register-num/{day}")
    public R countRegisterNum(@PathVariable("day") String day);

}
