package com.qx.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qx.guli.common.base.result.ResultCodeEnum;
import com.qx.guli.service.base.dto.CourseDto;
import com.qx.guli.service.edu.entity.*;
import com.qx.guli.service.edu.entity.form.CourseInfoForm;
import com.qx.guli.service.edu.entity.vo.*;
import com.qx.guli.service.edu.feign.OssFileService;
import com.qx.guli.service.edu.mapper.*;
import com.qx.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qx.guli.service.edu.service.VideoService;
import com.qx.guli.service.base.exception.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author qx
 * @since 2020-06-04
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;
    @Autowired
    private OssFileService ossFileService;
    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private ChapterMapper chapterMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CourseCollectMapper courseCollectMapper;



    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    @Override
    public String saveCourseInform(CourseInfoForm courseInfoForm) {

        // 创建课程类接收数据
        Course course = new Course();

        // 复制课程属性
        BeanUtils.copyProperties(courseInfoForm,course);
        // 设置课程发布状态
        course.setStatus(Course.COURSE_DRAFT);
        // 设置课程是否删除
        course.setIsDeleted(0);
        // 调用数据库插入数据
        baseMapper.insert(course);
        // 课程和课程详情属于同一个课程表中内容，但由于课程详情内容太多，所以使用分库分表，课程id和课程详情id相同
        // 获取课程id
        String courseId = course.getId();
        // 获取课程详情
        String description = courseInfoForm.getDescription();
        // 插入课程详情
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(description);
        courseDescription.setId(courseId);
        courseDescriptionMapper.insert(courseDescription);

        return courseId;
    }

    @Override
    public CourseInfoForm getCourseInfo(String courseId) {

        // 通过id查询课程
        Course course = baseMapper.selectById(courseId);
        if(course == null){
            throw new GuliException(ResultCodeEnum.COURSE_LOSE);
        }
        // 查询课程详情
        CourseDescription courseDescription = courseDescriptionMapper.selectById(courseId);
        // 创建课程视图对象
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        // 设置课程详情
        BeanUtils.copyProperties(course,courseInfoForm);
        courseInfoForm.setDescription(courseDescription.getDescription());
        return courseInfoForm;
    }

    @Override
    public void updateCourseInform(CourseInfoForm courseInfoForm) {

        // 创建课程类接收数据
        Course course = new Course();

        // 复制课程属性
        BeanUtils.copyProperties(courseInfoForm,course);
        // 调用数据库插入数据
        baseMapper.updateById(course);
        // 课程和课程详情属于同一个课程表中内容，但由于课程详情内容太多，所以使用分库分表，课程id和课程详情id相同
        // 获取课程id
        String courseId = course.getId();
        // 获取课程详情
        String description = courseInfoForm.getDescription();
        // 插入课程详情
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(description);
        courseDescription.setId(courseId);
        courseDescriptionMapper.updateById(courseDescription);

    }

    @Override
    public IPage<CourseVo> pageSelect(Long current, Long size, CourseQueryVo courseVo) {

        // 创建包装类对象
        QueryWrapper<CourseVo> courseVoQueryWrapper = new QueryWrapper<>();
        // 降序排列
        courseVoQueryWrapper.orderByDesc("c.gmt_create");
        // 封装条件
        String subjectId = courseVo.getSubjectId();
        String subjectParentId = courseVo.getSubjectParentId();
        String teacherId = courseVo.getTeacherId();
        String title = courseVo.getTitle();
        if(!StringUtils.isEmpty(subjectId)){
            courseVoQueryWrapper.eq("s1.id",subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)){
            courseVoQueryWrapper.eq("s2.id",subjectParentId);
        }
        if(!StringUtils.isEmpty(title)){
            courseVoQueryWrapper.like("c.title",title);
        }
        if(!StringUtils.isEmpty(teacherId)){
            courseVoQueryWrapper.eq("t.id",teacherId);
        }
        // 过滤删除的课程
        courseVoQueryWrapper.eq("c.is_deleted",0);
        // 创建page对象
        Page<CourseVo> courseVoPage = new Page<>(current,size);

        return baseMapper.selectPageByCourseVo(courseVoPage,courseVoQueryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeCourseAndCoverById(String id, String cover) {

            // 调用ossFileService远程删除图片
            ossFileService.removeFile(cover);
            // 删除课程详情
            courseDescriptionMapper.deleteById(id);

            // 创建包装类构造条件
            QueryWrapper queryWrapper = new QueryWrapper<>();
            // 删除视频
            // 调用oss远程删除，先删除这个，不然后面查询不到数据
            videoService.removeMediaVideoByCourseId(id);
            QueryWrapper<Video> videoWrapper = (QueryWrapper<Video>) queryWrapper;
            queryWrapper.eq("course_id",id);
            videoMapper.delete(videoWrapper);

            // 删除章节
            QueryWrapper<Chapter> chapterWrapper = (QueryWrapper<Chapter>) queryWrapper;
            chapterMapper.delete(chapterWrapper);
            // 删除评论
            QueryWrapper<Comment> commentWrapper = (QueryWrapper<Comment>) queryWrapper;
            commentMapper.delete(commentWrapper);
            // 删除收藏信息
            QueryWrapper<CourseCollect> courseCollectWrapper = (QueryWrapper<CourseCollect>) queryWrapper;
            courseCollectMapper.delete(courseCollectWrapper);

            // 删除课程
            return this.removeById(id);

    }

    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {

        return baseMapper.selectCoursePublishVoById(id);

    }

    @Override
    public boolean PublishCourseById(String id) {

        // 设置课程属性
        Course course = new Course();
        course.setId(id);
        course.setStatus(Course.COURSE_NORMAL);
        return baseMapper.updateById(course) == 1;
    }

    @Override
    public List<Course> webSelectList(WebCourseQueryVo webCourseQueryVo) {

        // 创建包装类封装条件
        QueryWrapper<Course> wrapper = new QueryWrapper<>();

        // 取出查询条件

        String buyCountSort = webCourseQueryVo.getBuyCountSort();
        String gmtCreateSort = webCourseQueryVo.getGmtCreateSort();
        String priceSort = webCourseQueryVo.getPriceSort();
        String subjectId = webCourseQueryVo.getSubjectId();
        String subjectParentId = webCourseQueryVo.getSubjectParentId();

        // 封装条件

        if(!StringUtils.isEmpty(subjectId)){
            wrapper.eq("subject_id",subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)){
            wrapper.eq("subject_parent_id",subjectParentId);
        }

        if(!StringUtils.isEmpty(buyCountSort)){
            if(buyCountSort.equals("2")){
                wrapper.orderByAsc("buy_count");
            }else {
                wrapper.orderByDesc("buy_count");
            }
        }

        if(!StringUtils.isEmpty(gmtCreateSort)){
            wrapper.orderByDesc("gmt_create");
        }

        if(!StringUtils.isEmpty(priceSort)){
            wrapper.orderByDesc("price");
        }

        // 设置查询已发布课程
        wrapper.eq("status",Course.COURSE_NORMAL);
        // 查询
        List<Course> courseList = baseMapper.selectList(wrapper);
        return courseList;
    }

    /**
     * 获取课程展示内容
     * @param courseId
     * @return
     */
//    @Transactional(rollbackFor = Exception.class)
    @Override
    public WebCourseVo getWebCourseVoByCourseId(String courseId) {
        // 更新课程浏览数
        Course course = baseMapper.selectById(courseId);
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);
        // 查询数据并返回
        return baseMapper.selectWebCourseVoById(courseId);
    }

    // key必须要加上单引号
    @Cacheable(value = "index" ,key = "'listHotCourses'")
    @Override
    public List<Course> listHotCourses() {
        // 包装条件
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("view_count");
        // 只查8条数据
        wrapper.last("limit 8");
        return baseMapper.selectList(wrapper);

    }

    @Override
    public CourseDto getCourseDtoById(String id) {
        CourseDto courseDto = baseMapper.selectCourseDtoById(id);
        return courseDto;
    }

    @Override
    public void updateCourseBuyCount(String courseId) {
        Course course = baseMapper.selectById(courseId);
        if(null!=course){
            Long buyCount = course.getBuyCount();
            buyCount ++;
            course.setBuyCount(buyCount);
            baseMapper.updateById(course);
        }
    }

    @Override
    public Integer countCourseNum(String day) {
        return baseMapper.selectCountByDay(day);
    }

}
