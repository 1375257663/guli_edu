package com.qx.guli.service.edu.controller.admin;


import com.qx.guli.common.base.result.R;
import com.qx.guli.common.base.result.ResultCodeEnum;
import com.qx.guli.service.edu.entity.vo.SubjectVo;
import com.qx.guli.service.edu.service.SubjectService;
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
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author qx
 * @since 2020-06-04
 */
@RestController
@RequestMapping("/admin/edu/subject")
//@CrossOrigin
@Api(value="课程管理")
@Slf4j
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @ApiOperation(value = "Excel批量导入课程类别数据")
    @PostMapping("/import")
    public R importByExcel(
            @ApiParam(value = "Excel文件", required = true)
            @RequestParam("file")MultipartFile multipartFile){

        try {
            subjectService.uploadSubjectByExcel(multipartFile.getInputStream());
            return R.ok().message("文件批量上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("文件上传失败，错误日志："+e.getMessage());
            throw new GuliException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }

    }

    @GetMapping("nested-list")
    public R nestedList(){

        List<SubjectVo> subjectVoList = subjectService.nestedList();
        return R.ok().data("items",subjectVoList);
    }

}

