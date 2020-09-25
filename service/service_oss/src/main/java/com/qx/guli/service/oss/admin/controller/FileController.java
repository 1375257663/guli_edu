package com.qx.guli.service.oss.admin.controller;

import com.qx.guli.common.base.result.R;
import com.qx.guli.common.base.result.ResultCodeEnum;
import com.qx.guli.service.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

/**
 * @Classname FileController
 * @Description TODO
 * @Date 2020/6/7 17:56
 * @Created by 卿星
 */
//@CrossOrigin    // 跨域
@Api(value = "阿里云文件管理")
@RestController
@RequestMapping(("/admin/oss/file"))
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    @RequestMapping("upload")
    @ApiOperation("上传图片")
    public R upload(
            @ApiParam(value = "文件",required = true)
            @RequestParam("file") MultipartFile file,
            @ApiParam(value = "模块",required = true)
            @RequestParam("module") String module) {

        String uploadUrl = null;
        try {
            uploadUrl = fileService.upload(file.getInputStream(), module, file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
//            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
            return R.error().message(ResultCodeEnum.FILE_UPLOAD_ERROR.getMessage());
        }

        return R.ok().data("url",uploadUrl).message("文件上传成功");

    }

    @ApiOperation("测试")
    @GetMapping("test")
    public R test(){
        System.out.println("oss test被调用了。。。");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return R.ok();
    }

    @ApiOperation("测试")
    @DeleteMapping("remove")
    public R removeFile(@ApiParam(value = "头像地址",required = true)@RequestParam String url){

        fileService.removeFile(url);

        return R.ok().message("删除成功!");
    }

}
