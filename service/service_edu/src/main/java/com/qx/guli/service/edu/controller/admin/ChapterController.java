package com.qx.guli.service.edu.controller.admin;


import com.qx.guli.common.base.result.R;
import com.qx.guli.service.edu.entity.Chapter;
import com.qx.guli.service.edu.entity.Video;
import com.qx.guli.service.edu.entity.vo.ChapterVo;
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
 * 课程 前端控制器
 * </p>
 *
 * @author qx
 * @since 2020-06-04
 */
//@CrossOrigin
@Api(value="章节管理")
@RestController
@RequestMapping("/admin/edu/chapter")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;
    @Autowired
    private VideoService videoService;

    @ApiOperation("新增章节")
    @PostMapping("save")
    public R save(
            @ApiParam(value="章节对象", required = true)
            @RequestBody Chapter chapter){
        boolean flag = chapterService.save(chapter);
        if (flag) {
            return R.ok().message("保存成功");
        } else {
            return R.error().message("保存失败");
        }
    }

    @ApiOperation("根据id删除章节")
    @DeleteMapping("remove/{id}")
    public R remove(
            @ApiParam(value="章节id", required = true)
            @PathVariable String id){

        // 删除视频
        // 远程调用删除
        videoService.removeVideoByChapterId(id);

        boolean flag = chapterService.removeChapterById(id);
        if (flag) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id查询章节")
    @GetMapping("get/{id}")
    public R getById(
            @ApiParam(value="章节id", required = true)
            @PathVariable String id){

        Chapter chapter = chapterService.getById(id);
        if (chapter != null) {
            return R.ok().data("item",chapter);
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id修改章节")
    @PutMapping("update")
    public R updateById(
            @ApiParam(value = "章节对象",required = true)
            @RequestBody Chapter chapter){

        boolean flag = chapterService.updateById(chapter);
        if(flag){
            return R.ok().message("更新章节成功！");
        }else{
            return R.error().message("数据不存在！");
        }
    }

    @ApiOperation("根据课程id查询所有章节及其嵌套的video")
    @GetMapping("nested-list/{id}")
    public R nestedListByCourseId(
            @ApiParam(value = "章节id",required = true)
            @PathVariable String id){
        List<ChapterVo> chapterVoList = chapterService.getnestedListByCourseId(id);
        return R.ok().data("items",chapterVoList);
    }

}

