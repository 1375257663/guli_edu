package com.qx.guli.service.trade.feign;

import com.qx.guli.service.base.dto.MemberDto;
import com.qx.guli.service.trade.feign.fallback.UcenterMemberServiceFallback;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Classname UcenterMemberService
 * @Description TODO
 * @Date 2020/6/26 14:06
 * @Created by 卿星
 */
@FeignClient(value = "service-ucenter",fallback = UcenterMemberServiceFallback.class)
public interface UcenterMemberService {

    @ApiOperation("根据会员id查询会员信息")
    @GetMapping("api/ucenter/member/inner/get-member-dto/{memberId}")
    public MemberDto getMemberDtoByMemberId(@PathVariable String memberId);
}
