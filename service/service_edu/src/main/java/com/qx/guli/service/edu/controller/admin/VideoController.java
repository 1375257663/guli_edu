package com.qx.guli.service.edu.controller.admin;


import com.qx.guli.common.base.result.R;
import com.qx.guli.service.edu.entity.Chapter;
import com.qx.guli.service.edu.entity.Video;
import com.qx.guli.service.edu.feign.VodMediaService;
import com.qx.guli.service.edu.service.ChapterService;
import com.qx.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author qx
 * @since 2020-06-04
 */
//@CrossOrigin
@Api(value="视频管理")
@RestController
@RequestMapping("/admin/edu/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @ApiOperation("新增课时")
    @PostMapping("save")
    public R save(
            @ApiParam(value="课时对象", required = true)
            @RequestBody Video video){
        boolean flag = videoService.save(video);
        if (flag) {
            return R.ok().message("保存成功");
        } else {
            return R.error().message("保存失败");
        }
    }


    @ApiOperation("根据id删除课时")
    @DeleteMapping("remove/{videoId}")
    public R remove(
            @ApiParam(value="课时id", required = true)
            @PathVariable String videoId){

        // 删除视频
        videoService.removeMediaVideoById(videoId);

        boolean flag = videoService.removeById(videoId);
        if (flag) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("批量删除")
    @DeleteMapping("remove")
    public R removeVideoByIdList(
            @ApiParam(value="课时id集合", required = true)
            @RequestBody List<String> idList){

        // 删除视频
        videoService.removeMediaVideoByIdList(idList);

        return R.ok();
    }

    @ApiOperation("根据id查询课时")
    @GetMapping("get/{id}")
    public R getById(
            @ApiParam(value="课时id", required = true)
            @PathVariable String id){

        Video video = videoService.getById(id);
        if (video != null) {
            return R.ok().data("item",video);
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id修改课时")
    @PutMapping("update")
    public R updateById(
            @ApiParam(value = "课时对象",required = true)
            @RequestBody Video video){

        boolean flag = videoService.updateById(video);
        if(flag){
            return R.ok().message("更新课时成功！");
        }else{
            return R.error().message("数据不存在！");
        }
    }

    @ApiOperation("增加播放数量")
    @PutMapping("plus/view-count/{vid}")
    public R updateById(@ApiParam(name = "vid", value = "视频ID")
                        @PathVariable String vid){
        videoService.plusViewCount(vid);
        return R.ok();
    }

    @ApiOperation(value = "根据日期统计播放数")
    @GetMapping(value = "count-video-view-num/{day}")
    public R countVideoeNum(
            @ApiParam(name = "day", value = "统计日期")
            @PathVariable String day){

        Integer count = videoService.countVideoViewNum(day);

        return R.ok().data("playCount",count);
    }

}

