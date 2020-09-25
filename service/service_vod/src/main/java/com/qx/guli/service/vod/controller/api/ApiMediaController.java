package com.qx.guli.service.vod.controller.api;

import com.qx.guli.common.base.result.R;
import com.qx.guli.common.base.result.ResultCodeEnum;
import com.qx.guli.common.base.util.ExceptionUtils;
import com.qx.guli.service.vod.service.VideoService;
import com.qx.guli.service.base.exception.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname ApiMediaController
 * @Description TODO
 * @Date 2020/6/21 10:19
 * @Created by 卿星
 */
//@CrossOrigin
@RestController
@RequestMapping("/api/vod/media")
@Api(value = "阿里云前台视频控制")
@Slf4j
public class ApiMediaController {

    @Autowired
    private VideoService videoService;

    @GetMapping("get-play-auth/{videoSourceId}")
    public R getPlayAuth(
            @ApiParam(value = "阿里云视频文件的id", required = true)
            @PathVariable String videoSourceId){
        try{
            String playAuth = videoService.getPlayAuth(videoSourceId);
            return  R.ok().message("获取播放凭证成功").data("playAuth", playAuth);
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.FETCH_PLAYAUTH_ERROR);
        }

    }

}
