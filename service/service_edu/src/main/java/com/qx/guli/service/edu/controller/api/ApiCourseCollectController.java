package com.qx.guli.service.edu.controller.api;


import com.qx.guli.common.base.result.R;
import com.qx.guli.common.base.util.JwtInfo;
import com.qx.guli.common.base.util.JwtUtils;
import com.qx.guli.service.edu.entity.CourseCollect;
import com.qx.guli.service.edu.entity.vo.CourseCollectVo;
import com.qx.guli.service.edu.service.CourseCollectService;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 课程收藏 前端控制器
 * </p>
 *
 * @author qx
 * @since 2020-06-04
 */
@Api("课程收藏")
//@CrossOrigin
@RestController
@RequestMapping("/api/edu/course-collect")
public class ApiCourseCollectController {

    @Autowired
    private CourseCollectService courseCollectService;

    @ApiOperation("判断课程是否收藏")
    @GetMapping("auth/is-collect/{courseId}")
    public R list(@ApiParam(value = "课程ID",required = true) @PathVariable String courseId,HttpServletRequest request){

        // 获取用户信息
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        if (jwtInfo == null){
            return R.error().message("未登录");
        }
        boolean flag = courseCollectService.isCollectByCourseId(courseId,jwtInfo.getId());
        return R.ok().message("该课程您已收藏").data("flag",flag);
    }

    @ApiOperation("获取课程收藏")
    @GetMapping("auth/list")
    public R list(HttpServletRequest request){

        // 获取用户信息
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        List<CourseCollectVo> courseCollectList = courseCollectService.listByMemberId(jwtInfo.getId());

        return R.ok().data("items",courseCollectList);
    }

    @ApiOperation("根据课程收藏ID删除课程")
    @DeleteMapping("auth/remove/{id}")
    public R remove(@ApiParam(value = "课程收藏ID",required = true) @PathVariable String id){

        boolean flag = courseCollectService.removeById(id);
        if(flag){
            return R.ok().message("已取消收藏");
        }

        return R.error().message("取消失败");
    }

    @ApiOperation("根据课程ID删除课程")
    @DeleteMapping("auth/removeByCourseId/{courseId}")
    public R removeByCourseId(@ApiParam(value = "课程ID",required = true) @PathVariable String courseId,HttpServletRequest request){

        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        boolean flag = courseCollectService.removeByCourseId(courseId,jwtInfo.getId());
        return R.ok().message("已取消该收藏");
    }

    @ApiOperation("收藏课程")
    @PostMapping("auth/save/{courseId}")
    public R collect(@ApiParam(value = "课程ID",required = true) @PathVariable String courseId, HttpServletRequest request){
        // 获取用户信息
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);

        boolean flag = courseCollectService.saveByCourseId(courseId,jwtInfo.getId());
        if(flag){
            return R.ok().message("已为您收藏该课程");
        }

        return R.error().message("收藏课程失败");
    }

}

