package com.qx.guli.service.vod.controller.admin;

import com.qx.guli.common.base.result.R;
import com.qx.guli.common.base.result.ResultCodeEnum;
import com.qx.guli.common.base.util.ExceptionUtils;
import com.qx.guli.service.vod.service.VideoService;
import com.qx.guli.service.base.exception.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Classname MediaController
 * @Description TODO
 * @Date 2020/6/17 0:09
 * @Created by 卿星
 */
//@CrossOrigin
@RestController
@RequestMapping("/admin/vod/media")
@Api(value = "阿里云视频管理")
@Slf4j
public class MediaController {

    @Autowired
    private VideoService videoService;

    @ApiOperation("视频文件上传")
    @PostMapping("upload")
    public R upload(
            @ApiParam(value = "上传视频文件",required = true)
            @RequestParam(value = "file",required = true)
            MultipartFile multipartFile){
        String videoId = videoService.uploadVideo(multipartFile);
        return R.ok().message("文件上传成功！").data("videoId",videoId);
    }

    @ApiOperation("删除视频文件")
    @DeleteMapping("remove/{id}")
    public R removeByVideoSourceId(
            @ApiParam(value = "视频源id",required = true)
            @PathVariable(value = "id",required = true)
            String id){
        boolean flag = videoService.removeVideoById(id);
        return R.ok().message("视频删除成功！");
    }

    @DeleteMapping("remove")
    public R removeVideoByIdList(
            @ApiParam(value = "阿里云视频id列表", required = true)
            @RequestBody List<String> videoIdList){

        try {
            videoService.removeMediaVideoByIdList(videoIdList);
            return  R.ok().message("视频删除成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }
}
