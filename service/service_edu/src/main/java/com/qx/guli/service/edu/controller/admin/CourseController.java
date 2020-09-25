package com.qx.guli.service.edu.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qx.guli.common.base.result.R;
import com.qx.guli.common.base.result.ResultCodeEnum;
import com.qx.guli.service.edu.entity.Course;
import com.qx.guli.service.edu.entity.EduTeacher;
import com.qx.guli.service.edu.entity.form.CourseInfoForm;
import com.qx.guli.service.edu.entity.vo.CoursePublishVo;
import com.qx.guli.service.edu.entity.vo.CourseQueryVo;
import com.qx.guli.service.edu.entity.vo.CourseVo;
import com.qx.guli.service.edu.entity.vo.TeacherQueryVo;
import com.qx.guli.service.edu.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
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
@Api(value="课程管理")
@RestController
@RequestMapping("/admin/edu/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @ApiOperation("上传课程")
    @PostMapping("save-course-info")
    public R saveCourseInfo(
            @ApiParam(value = "课程信息",required = true)
            @RequestBody CourseInfoForm courseInfoForm){

        String courseId = courseService.saveCourseInform(courseInfoForm);

        return R.ok().message("课程基本信息上传成功！").data("courseId",courseId);
    }

    @ApiOperation("更新课程")
    @PutMapping ("update-course-info")
    public R updateCourseInfo(
            @ApiParam(value = "课程信息",required = true)
            @RequestBody CourseInfoForm courseInfoForm){

        courseService.updateCourseInform(courseInfoForm);

        return R.ok().message("课程信息更新成功！");
    }

    @ApiOperation("通过课程id查询课程")
    @GetMapping("course-info/{courseId}")
    public R getCourseInfo(
            @ApiParam(value = "课程id",required = true)
            @PathVariable(value = "courseId") String courseId){

        CourseInfoForm courseInfoForm = courseService.getCourseInfo(courseId);

        if(courseInfoForm != null){
            return R.ok().data("courseInfoForm",courseInfoForm);
        }else {
            return R.error().message(ResultCodeEnum.COURSE_LOSE.getMessage());
        }
    }

    @ApiOperation("课程分页")
    @GetMapping("list/{current}/{size}")
    public R getCourseInfo(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable("current") Long current,
            @ApiParam(name = "size", value = "每页记录数", required = true)
            @PathVariable("size")Long size,
            @ApiParam(name = "pageTeacherCondition", value = "查询对象", required = false) CourseQueryVo courseVo){

        // 调用service查询
        IPage<CourseVo> pageResult = courseService.pageSelect(current, size, courseVo);
        // 获取总记录数
        long total = pageResult.getTotal();
        // 获取数据
        List<CourseVo> records = pageResult.getRecords();

        return R.ok().data("total",total).data("items",records);
    }

    @ApiOperation("删除课程")
    @DeleteMapping("remove/{id}")
    public R removeCourse(
            @ApiParam(name = "id", value = "课程id", required = true)
            @PathVariable String id,
            @RequestParam("0") String cover){

        // 调用service执行删除
        courseService.removeCourseAndCoverById(id,cover);

        return R.ok().message("删除课程成功！");
    }

    @ApiOperation("根据id获取课程发布信息")
    @GetMapping("course-publish/{id}")
    public R getCoursePublishVoById(
            @ApiParam(name = "id", value = "课程id", required = true)
            @PathVariable String id){

        // 调用service返回结果
        CoursePublishVo coursePublishVo = courseService.getCoursePublishVoById(id);

        return R.ok().message("课程发布信息获取成功！").data("item",coursePublishVo);
    }


    @ApiOperation("根据id发布课程信息")
    @PutMapping("publish-course/{id}")
    public R setCoursePublishVoById(
            @ApiParam(name = "id", value = "课程id", required = true)
            @PathVariable String id){

        // 调用service返回结果
        boolean flag = courseService.PublishCourseById(id);

        if(flag){
            return R.ok().message("课程发布成功！");
        }else {
            return R.error().message("发布课程不存在！");
        }
    }

    @ApiOperation(value = "根据日期统计课程新增数")
    @GetMapping(value = "count-course-num/{day}")
    public R countCourseNum(
            @ApiParam(name = "day", value = "统计日期")
            @PathVariable String day){

        Integer count = courseService.countCourseNum(day);

        return R.ok().data("courserCount",count);
    }

}

