package com.qx.guli.service.edu.controller.api;

import com.qx.guli.common.base.result.R;
import com.qx.guli.service.edu.entity.EduTeacher;
import com.qx.guli.service.edu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Classname ApiTeacherController
 * @Description TODO
 * @Date 2020/6/18 23:38
 * @Created by 卿星
 */

//@CrossOrigin
@Api(value="讲师")
@RestController
@RequestMapping("/api/edu/teacher")
public class ApiTeacherController {

    @Autowired
    private EduTeacherService teacherService;

    @ApiOperation(value="所有讲师列表")
    @GetMapping("list")
    public R listAll(){
        List<EduTeacher> list = teacherService.list();
        return R.ok().data("items", list).message("获取讲师列表成功");
    }

    @ApiOperation(value="根据id查询讲师")
    @GetMapping("get/{id}")
    public R getById(@PathVariable String id){
        Map<String, Object> map = teacherService.selectTeacherInfoById(id);
        return R.ok().data(map).message("获取讲师列表成功");
    }

}
