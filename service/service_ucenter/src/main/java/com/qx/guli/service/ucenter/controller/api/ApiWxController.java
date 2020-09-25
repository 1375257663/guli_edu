package com.qx.guli.service.ucenter.controller.api;

import com.google.gson.Gson;
import com.qx.guli.common.base.result.ResultCodeEnum;
import com.qx.guli.common.base.util.ExceptionUtils;
import com.qx.guli.common.base.util.HttpClientUtils;
import com.qx.guli.common.base.util.JwtInfo;
import com.qx.guli.common.base.util.JwtUtils;
import com.qx.guli.service.base.exception.GuliException;
import com.qx.guli.service.ucenter.entity.Member;
import com.qx.guli.service.ucenter.service.MemberService;
import com.qx.guli.service.ucenter.util.UcenterProperties;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Classname ApiWxController
 * @Description 微信操作
 * @Date 2020/6/25 11:06
 * @Created by 卿星
 */
//@CrossOrigin
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
@Slf4j
public class ApiWxController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private UcenterProperties ucenterProperties;

    @ApiOperation("微信登录")
    @GetMapping("login")
    public String login(HttpSession session){

        // https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect?" +
                "appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 编码url
        String redirectUrl = null;
        try {
            redirectUrl = URLEncoder.encode(ucenterProperties.getRedirectUri(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.URL_ENCODE_ERROR);
        }
        // 用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击）
        String state = UUID.randomUUID().toString().replace("-", "");
        log.info("生成 state: " + state);
        // 存入session
        session.setAttribute("wx_open_state",state);

        // 拼接参数
        String url = String.format(
                baseUrl,
                ucenterProperties.getAppId(),
                redirectUrl,
                state);

        return "redirect:" + url;
    }

    @ApiOperation("微信登录回调")
    @GetMapping("callback")
    public String login(String code,String state,HttpSession session){

        // 判断参数是否为空
        if(StringUtils.isEmpty(code) || StringUtils.isEmpty(state)){
            log.error("非法回调请求");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }
        // 判断状态码
        String wx_open_state = (String) session.getAttribute("wx_open_state");
        if(!state.equals(wx_open_state)){
            log.error("微信状态码不正确");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }
        // https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        String startUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
        // 设置请求参数
        HttpClientUtils clientUtils = new HttpClientUtils(startUrl);
        Map<String, String> paramterMap = new HashMap<>();
        paramterMap.put("appid",ucenterProperties.getAppId());
        paramterMap.put("secret",ucenterProperties.getAppSecret());
        paramterMap.put("code",code);
        paramterMap.put("grant_type","authorization_code");
        clientUtils.setParameter(paramterMap);

        // 发送请求，获取参数
        String result = null;
        try {
            clientUtils.get();
            result = clientUtils.getContent();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            log.error("获取access_token失败");
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        // 解析
        Gson gson = new Gson();
        Map resultMap = gson.fromJson(result, HashMap.class);
        if(resultMap.get("errcode") != null){
            log.error("获取access_token失败" + ",errmsg:" + resultMap.get("errmsg"));
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        // {"access_token":"34_LBGtUsxhsga7LorvFBKJ_TMClMT7iWbXwJBPpc68k1BgmrjoUTDriYNOljyA4nOApYFMiLgPpdEbBxv6lHItLxNOzc3g4NO6FmfGYreh6XE",
        // "expires_in":7200,
        // "refresh_token":"34_JjxzWgOLqElO2IJz9ULPTImM-Q78rLLudVf92JuY36wAhu3qxk8_PPHsKfpJtVWUsFCxc8EVxFwIEk3i7_QCfa8An7hvcJZ1hcRiBgIDQ4Q",
        // "openid":"o3_SC57SR3BvkkpA_JxBDr5oh-_s",
        // "scope":"snsapi_login",
        // "unionid":"oWgGz1K8ufr1DIZfuobkEpBkO5SU"}
        String access_token = (String) resultMap.get("access_token");
        String openid = (String) resultMap.get("openid");
        Member member = memberService.getByOpenId(openid);
        if(member == null){
            // 通过code获取access_token的接口。
            String getInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
            Map<String, String> infopMapReqest = new HashMap<>();
            // access_token=ACCESS_TOKEN&openid=OPENID
            infopMapReqest.put("access_token",access_token);
            infopMapReqest.put("openid",openid);
            HttpClientUtils getInfoClientUtil = new HttpClientUtils(getInfoUrl, infopMapReqest);
            String userInfo = null;
            try {
                // 发送请求
                getInfoClientUtil.get();
                userInfo = getInfoClientUtil.getContent();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("获取UserInfo失败");
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            Map infoMap = gson.fromJson(userInfo, Map.class);
            if(infoMap.get("errcode") != null){
                log.error("获取UserInfo失败" + ",errmsg:" + resultMap.get("errmsg"));
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            // 判断当前账号是否注册
            String nickname = (String) infoMap.get("nickname");
            String avatar = (String) infoMap.get("headimgurl");
            Double sex = (Double) infoMap.get("sex");

            // 获取数据并存入数据库
            member = new Member();
            member.setNickname(nickname);
            member.setAvatar(avatar);
            member.setDisabled(false);
            member.setOpenid(openid);
            member.setSex(sex.intValue());

            memberService.save(member);
        }

        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setId(member.getId());
        jwtInfo.setAvatar(member.getAvatar());
        jwtInfo.setNickname(member.getNickname());
        // 转换为jwtToken
        String jwtToken = JwtUtils.getJwtToken(jwtInfo, 3600);
        // 携带token跳转
        return "redirect:http://localhost:3000?token=" + jwtToken;
    }


}
