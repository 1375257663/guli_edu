package com.qx.guli.service.ucenter.mapper;

import com.qx.guli.service.base.dto.MemberDto;
import com.qx.guli.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author qx
 * @since 2020-06-23
 */
public interface MemberMapper extends BaseMapper<Member> {

    MemberDto selectMemberDtoById(String memberId);

    Integer selectRegisterNumByDay(String day);
}
