package com.qx.guli.service.trade.feign.fallback;

import com.qx.guli.service.base.dto.MemberDto;
import com.qx.guli.service.trade.feign.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Classname UcenterMemberServiceFallback
 * @Description TODO
 * @Date 2020/6/26 14:08
 * @Created by 卿星
 */
@Service
@Slf4j
public class UcenterMemberServiceFallback implements UcenterMemberService {

    @Override
    public MemberDto getMemberDtoByMemberId(String memberId) {
        log.info("熔断保护");
        return null;
    }
}
