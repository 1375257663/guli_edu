package com.qx.guli.service.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qx.guli.common.base.result.ResultCodeEnum;
import com.qx.guli.common.base.util.FormUtils;
import com.qx.guli.common.base.util.JwtInfo;
import com.qx.guli.common.base.util.JwtUtils;
import com.qx.guli.common.base.util.MD5;
import com.qx.guli.service.base.dto.MemberDto;
import com.qx.guli.service.base.exception.GuliException;
import com.qx.guli.service.ucenter.entity.Member;
import com.qx.guli.service.ucenter.entity.vo.LoginVo;
import com.qx.guli.service.ucenter.entity.vo.RegisterVo;
import com.qx.guli.service.ucenter.mapper.MemberMapper;
import com.qx.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author qx
 * @since 2020-06-23
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void regist(RegisterVo registerVo) {
        // 获取注册元素
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();
        // 判断注册元素是否符合条件
        if(
           StringUtils.isEmpty(code) ||
           !FormUtils.isMobile(mobile) ||
           StringUtils.isEmpty(nickname) ||
           StringUtils.isEmpty(password)
           ){
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        // 判断验证码是否正确
        String codeRes = (String) redisTemplate.opsForValue().get(mobile);
        if(!code.equals(codeRes)){
            throw new GuliException(ResultCodeEnum.CODE_ERROR);
        }
        // 判断是否被注册
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count > 0){
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }

        Member member = new Member();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        // 设置默认属性
        member.setDisabled(false);
        member.setAvatar("https://guli-file-helen.oss-cn-beijing.aliyuncs.com/avatar/default.jpg");
        // 存库
        baseMapper.insert(member);
    }

    @Override
    public String login(LoginVo loginVo) {

        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        // 效验参数
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) || !FormUtils.isMobile(mobile)){
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        // 效验手机号
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Member memberRes = baseMapper.selectOne(wrapper);
        // 判断
        if(memberRes == null){
            throw new GuliException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }
        // 效验密码
        // 加密原密码
        String encryptPwd = MD5.encrypt(password);
        // 判断
        if(!encryptPwd.equals(memberRes.getPassword())){
            throw new GuliException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        // 判断用户是否被禁用
        if(memberRes.getDisabled()){
            throw new GuliException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        // 创建JWT有效载荷信息对象封装数据
        JwtInfo jwtInfo = new JwtInfo(memberRes.getId(),memberRes.getNickname(),memberRes.getAvatar());
        // 生成token
        String jwtToken = JwtUtils.getJwtToken(jwtInfo, 3600);

        return jwtToken;

    }

    @Override
    public Member getByOpenId(String openidRes) {

        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openidRes);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public MemberDto getMemberDtoByMemberId(String memberId) {

        // 查询MemberDto
        MemberDto memberDto = baseMapper.selectMemberDtoById(memberId);
        return memberDto;
    }

    @Override
    public Integer countRegisterNum(String day) {
        return baseMapper.selectRegisterNumByDay(day);
    }
}
