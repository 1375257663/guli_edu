package com.qx.guli.service.ucenter.service;

import com.qx.guli.service.base.dto.MemberDto;
import com.qx.guli.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qx.guli.service.ucenter.entity.vo.LoginVo;
import com.qx.guli.service.ucenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author qx
 * @since 2020-06-23
 */
public interface MemberService extends IService<Member> {

    void regist(RegisterVo registerVo);

    String login(LoginVo loginVo);

    Member getByOpenId(String openidRes);

    MemberDto getMemberDtoByMemberId(String memberId);

    Integer countRegisterNum(String day);
}
