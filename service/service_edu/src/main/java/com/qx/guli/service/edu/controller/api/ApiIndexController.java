package com.qx.guli.service.edu.controller.api;

import com.qx.guli.common.base.result.R;
import com.qx.guli.service.edu.entity.Chapter;
import com.qx.guli.service.edu.entity.Course;
import com.qx.guli.service.edu.entity.EduTeacher;
import com.qx.guli.service.edu.entity.vo.WebCourseQueryVo;
import com.qx.guli.service.edu.service.CourseService;
import com.qx.guli.service.edu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.Cacheable;

import java.util.List;

/**
 * @Classname ApiIndexController
 * @Description TODO
 * @Date 2020/6/22 15:54
 * @Created by 卿星
 */
//@CrossOrigin
@Api(value="课程")
@RestController
@RequestMapping("/api/edu/index")
public class ApiIndexController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private EduTeacherService teacherService;
    @Autowired
    private RedisTemplate redisTemplate;

//    @Cacheable("apiEduIndex")
    @ApiOperation("课程列表")
    @GetMapping
    public R index(){

        // 获取热门课程(前8条)
        List<Course> courseList = courseService.listHotCourses();
        // 获取名师（前4条）
        List<EduTeacher> teacherList = teacherService.listFamousTeachers();

        return R.ok().data("courseList",courseList).data("teacherList",teacherList);
    }

    @PostMapping("test-save")
    public R testSet(@RequestBody Chapter chapter){
        redisTemplate.opsForValue().set("chapter",chapter);
        return R.ok();
    }

    @GetMapping("get")
    public R testGet(){
        Chapter chapter = (Chapter)redisTemplate.opsForValue().get("chapter");
        return R.ok().data("chapter",chapter);
    }

    @DeleteMapping("delete")
    public R testDelete(){
        redisTemplate.delete("chapter");
        return R.ok();
    }

    @PutMapping("update")
    public R testupdate(@RequestBody Chapter chapter){
        redisTemplate.opsForValue().set("chapter",chapter);
        return R.ok().data("chapter",chapter);
    }


}
