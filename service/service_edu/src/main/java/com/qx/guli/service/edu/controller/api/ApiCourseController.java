package com.qx.guli.service.edu.controller.api;

import com.qx.guli.common.base.result.R;
import com.qx.guli.service.base.dto.CourseDto;
import com.qx.guli.service.edu.entity.Course;
import com.qx.guli.service.edu.entity.vo.ChapterVo;
import com.qx.guli.service.edu.entity.vo.WebCourseQueryVo;
import com.qx.guli.service.edu.entity.vo.WebCourseVo;
import com.qx.guli.service.edu.service.ChapterService;
import com.qx.guli.service.edu.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname ApiCourseController
 * @Description 课程
 * @Date 2020/6/19 16:21
 * @Created by 卿星
 */

//@CrossOrigin
@Api(value="课程")
@RestController
@RequestMapping("/api/edu/course")
public class ApiCourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private ChapterService chapterService;

    @ApiOperation("课程列表")
    @GetMapping("list")
    public R list(
            @ApiParam(value = "查询对象", required = false)
                    WebCourseQueryVo webCourseQueryVo){
        List<Course> courseList = courseService.webSelectList(webCourseQueryVo);
        return  R.ok().data("courseList", courseList);
    }

    @ApiOperation("根据课程ID查询该课程在页面显示元素")
    @GetMapping("/get/{id}")
    public R getCourseVoById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String id){
        // 查询课程信息和章节信息
        WebCourseVo webCourseVo = courseService.getWebCourseVoByCourseId(id);
        // 查询章节信息
        List<ChapterVo> chapterVoList = chapterService.getnestedListByCourseId(id);
        return  R.ok().data("courseVo", webCourseVo).data("chapterVoList",chapterVoList);
    }

    @ApiOperation("根据课程ID查询该课程在页面显示元素")
    @GetMapping("inner/get-course-dto/{courseId}")
    public CourseDto getCourseDtoById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String courseId){

        // 查询CourseDto
        CourseDto courseDto = courseService.getCourseDtoById(courseId);
        return courseDto;
    }


    @ApiOperation("更新销量")
    @PutMapping ("update-course-buyCount/{courseId}")
    public R updateCourseBuyCount(@ApiParam(value = "课程id",required = true)
                                  @PathVariable(value = "courseId") String courseId){

        courseService.updateCourseBuyCount(courseId);

        return R.ok().message("课程销量更新成功！");
    }

}
