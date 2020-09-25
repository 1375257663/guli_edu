package com.qx.guli.service.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qx.guli.service.edu.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qx.guli.service.edu.entity.vo.TeacherQueryVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author qx
 * @since 2020-06-02
 */
public interface EduTeacherService extends IService<EduTeacher> {

    IPage<EduTeacher> pageSelect(int current, int size, TeacherQueryVo teacherQueryVo);

    List<Map<String, Object>> getByKeyWord(String keyword);

    boolean removeAvatarById(String id);

    /**
     * 根据讲师id获取讲师详情页数据
     * @param id
     * @return
     */
    Map<String,Object> selectTeacherInfoById(String id);

    List<EduTeacher> listFamousTeachers();
}
