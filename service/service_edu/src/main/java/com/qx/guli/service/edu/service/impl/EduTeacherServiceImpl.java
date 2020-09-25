package com.qx.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qx.guli.common.base.result.R;
import com.qx.guli.service.edu.entity.Course;
import com.qx.guli.service.edu.entity.EduTeacher;
import com.qx.guli.service.edu.entity.vo.TeacherQueryVo;
import com.qx.guli.service.edu.feign.OssFileService;
import com.qx.guli.service.edu.mapper.EduTeacherMapper;
import com.qx.guli.service.edu.service.CourseService;
import com.qx.guli.service.edu.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author qx
 * @since 2020-06-02
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Autowired
    private OssFileService ossFileService;
    @Autowired
    private CourseService courseService;

    @Override
    public IPage<EduTeacher> pageSelect(int current, int size, TeacherQueryVo teacherQueryVo) {
        // 创建分页对象
        Page<EduTeacher> eduTeacherPage = new Page<>(current, size);
        // 创建mp包装类
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<EduTeacher>();
        // 排序
        wrapper.orderByAsc("sort");
        // 如果条件为空，直接查询返回所有
        if(teacherQueryVo == null){
            return baseMapper.selectPage(eduTeacherPage, wrapper);
        }
        // 取出数据
        String name = teacherQueryVo.getName();
        Integer level = teacherQueryVo.getLevel();
        String start = teacherQueryVo.getStart();
        String end = teacherQueryVo.getEnd();
        // 判断数据是否有效
        if(!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(start)){
            wrapper.ge("gmt_create",start);
        }
        if(!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create",end);
        }

        IPage<EduTeacher> pageResult = baseMapper.selectPage(eduTeacherPage, wrapper);

        return pageResult;
    }

    @Override
    public List<Map<String, Object>> getByKeyWord(String keyword) {
        // 创建包装类
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        // 设置要查询的列
        wrapper.select("name");
        // 设置条件
        wrapper.like("name",keyword);
        List<Map<String, Object>> selectMaps = baseMapper.selectMaps(wrapper);
        return selectMaps;
    }

    @Override
    public boolean removeAvatarById(String id) {

        // 通过id查询到url
        EduTeacher eduTeacher = baseMapper.selectById(id);
        // 判断teacher是否为空
        if(eduTeacher != null){
            // 获取头像地址
            String url = eduTeacher.getAvatar();
            if(!StringUtils.isEmpty(url)){
                // 远程调用删除头像
                R r = ossFileService.removeFile(url);
                return r.getSuccess();
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> selectTeacherInfoById(String id) {

        // 查询教师
        EduTeacher teacher = baseMapper.selectById(id);

        // 查询老师所教课程
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",id);
        List<Course> courseList = courseService.list(wrapper);
        // 封装数据
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("teacher",teacher);
        dataMap.put("courseList",courseList);
        return dataMap;
    }

    // 存入数据库后变为 "index::listFamousTeachers"，同一value下，key必须唯一
    @Cacheable(value = "index",key = "'listFamousTeachers'")
    @Override
    public List<EduTeacher> listFamousTeachers() {
        // 包装条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        // 查4条数据
        wrapper.last("limit 4");
        return baseMapper.selectList(wrapper);
    }
}
