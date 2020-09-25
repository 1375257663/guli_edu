package com.qx.guli.service.edu.controller;

import com.qx.guli.common.base.result.R;
import com.qx.guli.service.edu.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname LoginController
 * @Description TODO
 * @Date 2020/6/5 22:06
 * @Created by 卿星
 */
//@CrossOrigin // 允许跨域
@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    EduTeacherService eduTeacherService;

    @PostMapping("/login")
    public R login(/*@RequestParam("username")String username,@RequestParam("password")String password*/){
        /*QueryWrapper<EduTeacher> eduTeacherQueryWrapper = new QueryWrapper<>();
        eduTeacherQueryWrapper.eq("")
        eduTeacherService.getOne();*/
        return R.ok().data("token","admin");
    }

    @PostMapping("/logout")
    public R logout(){
        return R.ok();
    }

    @GetMapping("/info")
    public R getInfo(@RequestParam("token")String token){
        return R.ok()
                .data("roles","[admin]")
                .data("name","admin")
                .data("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
    }
}
