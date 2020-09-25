package com.qx.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qx.guli.service.edu.entity.Chapter;
import com.qx.guli.service.edu.entity.Video;
import com.qx.guli.service.edu.entity.vo.ChapterVo;
import com.qx.guli.service.edu.entity.vo.VideoVo;
import com.qx.guli.service.edu.mapper.ChapterMapper;
import com.qx.guli.service.edu.mapper.CourseMapper;
import com.qx.guli.service.edu.mapper.VideoMapper;
import com.qx.guli.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private CourseMapper courseMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeChapterById(String id) {

        // 删除课时信息：video
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",id);
        videoMapper.delete(wrapper);
        // 删除章节：chapter
        return baseMapper.deleteById(id) == 1;
    }

    @Override
    public List<ChapterVo> getnestedListByCourseId(String id) {

        // 创建课程集合
        List<ChapterVo> chapterVoList = new ArrayList<>();

        // 1、通过课程id获取该所有章节
        // ①：创建wrapper对象用于封装查询条件
//        QueryWrapper wrapper = new QueryWrapper<>();
        // ②：（这里不能强转，强转后下面查询其他表也会使用该条件）
        QueryWrapper<Chapter> wrapperChapter = new QueryWrapper<Chapter>();
        // ③：插入条件
        wrapperChapter.eq("course_id",id);
        // 设置排序方式:通过id和sort排序
        wrapperChapter.orderByAsc("sort","id");
        // ④：查询所有章节
        List<Chapter> chapterList = baseMapper.selectList(wrapperChapter);
        // 2、通过章节id查询本章节所有课程
        // ①：遍历得到每个章节，这种方法需要遍历每个章节，通过章节id查询，需要多次创建sql连接查询，故不使用这种查询
        /*for (Chapter chapter :
                chapterList) {
            // ②：得到章节ID
            String chapterId = chapter.getId();
            // ③：
            QueryWrapper<Video> wrapperVideo = new QueryWrapper<>();
            // ④：章节id
            wrapperVideo.eq("chapter_id",chapterId);
            // ⑤：调用mapper查询得到Video集合
            List<VideoVo> videoList = videoMapper.selectVideoVoList(wrapperVideo);
            // ⑥：创建ChapterVo接收数据
            ChapterVo chapterVo = new ChapterVo();
            // ⑦：封装数据
            chapterVo.setId(chapter.getId());
            chapterVo.setTitle(chapter.getTitle());
            chapterVo.setChildren(videoList);
            chapterVo.setSort(chapter.getSort());
            // ⑧：将封装好的数据：chapterVoList->放入集合
            chapterVoList.add(chapterVo);
        }*/
        // ②1：通过课程id查询所有章节
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        // ②2：设置条件
        videoQueryWrapper.eq("course_id",id);
        videoQueryWrapper.orderByAsc("sort","id");
        // ②3：查询
        List<Video> videoList = videoMapper.selectList(videoQueryWrapper);
        // ②3：遍历chapterList并赋值
        for (Chapter chapter: chapterList) {

            // 创建VideoVoList接收数据
            List<VideoVo> videoVoList = new ArrayList<>();
            // 创建ChapterVo接收数据
            ChapterVo chapterVo = new ChapterVo();
            // 设置数据
            BeanUtils.copyProperties(chapter,chapterVo);
            // 遍历videoList，当章节id相同时赋值
            for (Video video: videoList) {
               if(video.getChapterId().equals(chapter.getId())){
                   // 创建VideoVo接收数据
                   VideoVo videoVo = new VideoVo();
                   // 复制属性
                   BeanUtils.copyProperties(video,videoVo);
                   // 为videoVo设置是否免费
                   videoVo.setFree(video.getIsFree());
                   videoVoList.add(videoVo);
               }
            }
            // 设置videoVoList
            chapterVo.setChildren(videoVoList);

            // 添加到ChapterList
            chapterVoList.add(chapterVo);

        }


        return chapterVoList;
    }
}
