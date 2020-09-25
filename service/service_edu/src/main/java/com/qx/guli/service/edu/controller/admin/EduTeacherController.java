package com.qx.guli.service.edu.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qx.guli.common.base.result.R;
import com.qx.guli.service.edu.entity.EduTeacher;
import com.qx.guli.service.edu.entity.vo.TeacherQueryVo;
import com.qx.guli.service.edu.feign.OssFileService;
import com.qx.guli.service.edu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author qx
 * @since 2020-06-02
 */
// 设置允许跨域访问
//@CrossOrigin
@Api(value="讲师管理")
@RestController
@RequestMapping("/admin/edu/teacher")
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @Autowired
    private OssFileService ossfileService;

    @GetMapping("list")
    @ApiOperation(value = "所有讲师列表")
    public R getAllTeacher(){
        R r = R.ok().data("items",eduTeacherService.list(null));
//        if(1 == 1){
//            throw new GuliException(500,"谷粒出错了……");
//        }
        return r;
    }

    @DeleteMapping("remove/{id}")
    @ApiOperation(value = "根据ID删除讲师", notes = "根据ID删除讲师，逻辑删除")
    public R deletedById(@ApiParam(value = "讲师ID", required = true) @PathVariable("id") String id){
        // 删除图片
        eduTeacherService.removeAvatarById(id);
        // 删除讲师
        boolean flag = eduTeacherService.removeById(id);
        if(flag){
            // 删除头像
            return R.ok().message("删除成功");
        }else {
            return R.error().message("数据不存在");
        }
    }

    /*@GetMapping("list/{current}/{size}")
    public R pageTeacher(@PathVariable("current") int current,@PathVariable("size")int size){
        Page<EduTeacher> page = new Page<>(current, size);
        IPage<EduTeacher> pageResult = eduTeacherService.page(page, null);
        return R.ok().data("items",pageResult.getRecords()).data("total",pageResult.getTotal());
    }*/

    @GetMapping("list/{current}/{size}")
    @ApiOperation(value = "分页讲师列表")
    public R pageTeacherByCondition(@ApiParam(name = "current", value = "当前页码", required = true) @PathVariable("current") int current
                                    ,@ApiParam(name = "size", value = "每页记录数", required = true) @PathVariable("size")int size
                                    ,@ApiParam(name = "pageTeacherCondition", value = "查询对象", required = false) TeacherQueryVo teacherQueryVo){

        // 调用service查询
        IPage<EduTeacher> pageResult = eduTeacherService.pageSelect(current, size, teacherQueryVo);
        // 获取总记录数
        long total = pageResult.getTotal();
        // 获取数据
        List<EduTeacher> records = pageResult.getRecords();
        // 转载数据
        /*Map<String,Object> resMap = new HashMap<String, Object>();
        resMap.put("total",total);
        resMap.put("records",records);*/

        return R.ok().data("total",total).data("items",records);

    }

    @ApiOperation(value = "新增讲师")
    @PostMapping("save")
    public R addTeacher(
            @ApiParam(name = "teacher", value = "讲师对象", required = true) @RequestBody EduTeacher eduTeacher){

        boolean flag = eduTeacherService.save(eduTeacher);

        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("get/{id}")
    public R searchTeacherById(@ApiParam(name = "id", value = "讲师ID", required = true)@PathVariable("id")String id){

        EduTeacher teacher = eduTeacherService.getById(id);
        return R.ok().data("items",teacher);
    }

    @ApiOperation(value = "根据ID修改讲师")
    @PutMapping("update")
    public R updateTeacher(
            /*@ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id,*/

            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody EduTeacher eduTeacher){

        boolean flag = eduTeacherService.updateById(eduTeacher);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

    @ApiOperation("根据id列表删除讲师")
    @DeleteMapping("batch-remove")
    public R batchRemove(
            @ApiParam(value = "讲师id列表", required = true)
            @RequestBody List<String> ids){

        boolean flag = eduTeacherService.removeByIds(ids);
        if(flag){
            return R.ok().message("批量删除成功！");
        }
        return R.error().message("批量删除失败");
    }

    @ApiOperation("根据左关键字查询讲师名列表")
    @GetMapping("select/{keyword}")
    public R selectByKey(
            @ApiParam(value = "查询关键字", required = true)
            @PathVariable("keyword")String keyword){


        List<Map<String, Object>> mapList = eduTeacherService.getByKeyWord(keyword);

        return R.ok().data("nameList",mapList);
    }

    @ApiOperation("测试")
    @RequestMapping("test")
    public R test(){
        ossfileService.test();
        return R.ok();
    }

    @ApiOperation("测试")
    @RequestMapping("test/concurrent")
    public R testConcurrent(){
        return R.ok();
    }

    @ApiOperation("测试")
    @GetMapping("/message1")
    public String message1() {
        return "message1";
    }

    @ApiOperation("测试")
    @GetMapping("/message2")
    public String message2() {
        return "message2";
    }


}

