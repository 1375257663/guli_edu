package com.qx.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qx.guli.service.edu.entity.CourseCollect;
import com.qx.guli.service.edu.entity.vo.CourseCollectVo;
import com.qx.guli.service.edu.mapper.CourseCollectMapper;
import com.qx.guli.service.edu.service.CourseCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程收藏 服务实现类
 * </p>
 *
 * @author qx
 * @since 2020-06-04
 */
@Service
public class CourseCollectServiceImpl extends ServiceImpl<CourseCollectMapper, CourseCollect> implements CourseCollectService {

    @Override
    public List<CourseCollectVo> listByMemberId(String memberId) {

        QueryWrapper<CourseCollectVo> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id",memberId).orderByDesc("cc.gmt_create").eq("cc.is_deleted",0);
        return baseMapper.selectCollectVoList(wrapper);
    }

    @Override
    public boolean saveByCourseId(String courseId, String memberId) {
        CourseCollect courseCollect = new CourseCollect();
        courseCollect.setCourseId(courseId);
        courseCollect.setMemberId(memberId);
        return this.save(courseCollect);
    }

    @Override
    public boolean isCollectByCourseId(String courseId, String memberId) {

        QueryWrapper<CourseCollect> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id",memberId).eq("course_id",courseId);
        return baseMapper.selectCount(wrapper) == 1;
    }

    @Override
    public boolean removeByCourseId(String courseId, String memberId) {
        // 如果为手长课程，则删除
        if(this.isCollectByCourseId(courseId,memberId)){
            QueryWrapper<CourseCollect> wrapper = new QueryWrapper<>();
            wrapper.eq("member_id",memberId).eq("course_id",courseId);
            return baseMapper.delete(wrapper) == 1;
        }
        return false;
    }

}
