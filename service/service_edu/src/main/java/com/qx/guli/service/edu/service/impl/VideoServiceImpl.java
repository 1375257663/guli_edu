package com.qx.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qx.guli.service.edu.entity.Video;
import com.qx.guli.service.edu.feign.VodMediaService;
import com.qx.guli.service.edu.mapper.VideoMapper;
import com.qx.guli.service.edu.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author qx
 * @since 2020-06-04
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private VodMediaService vodMediaService;

    @Override
    public Video getByChapterId(String chapterId) {

        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        Video video = baseMapper.selectOne(wrapper);
        return  video;
    }

    @Override
    public void removeMediaVideoById(String id) {
        // 获取课时
        log.warn("VideoServiceImpl：video id = " + id);
        //根据videoid找到视频id
        Video video = baseMapper.selectById(id);
        String videoSourceId = video.getVideoSourceId();
        log.warn("VideoServiceImpl：videoSourceId= " + videoSourceId);
        // 通过课时章节删除
        vodMediaService.removeByVideoSourceId(videoSourceId);
    }

    @Override
    public void removeMediaVideoByIdList(List<String> idList) {

        // 查询
        List<Video> videoList = baseMapper.selectBatchIds(idList);
        // 创建StringBuilder
        StringBuilder sb = new StringBuilder();
        // 遍历取出sourceId
        // 遍历到第几个数
        int count = 0;
        for (Video video : videoList) {
            count ++;
            String videoSourceId = video.getVideoSourceId();
            if(count <videoList.size()){
                sb.append(videoSourceId + ",");
            }else {
                sb.append(videoSourceId);
            }

        }
        // 远程调用删除
        vodMediaService.removeByVideoSourceId(sb.toString());

        // 删除数据库中数据
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public void removeVideoByChapterId(String id) {

        List<String> videoSourceList = getVideoSourceIdList("chapter_id", id);
        vodMediaService.removeVideoByIdList(videoSourceList);
    }

    @Override
    public void removeMediaVideoByCourseId(String id) {
        List<String> videoSourceIdList = getVideoSourceIdList("course_id", id);
        System.out.println(videoSourceIdList);
        vodMediaService.removeVideoByIdList(videoSourceIdList);
    }

    @Override
    public Integer countVideoViewNum(String day) {

        QueryWrapper<Video> wrapper = new QueryWrapper<Video>().eq("gmt_modified", day).select("playCount");

        List<Object> list = baseMapper.selectObjs(wrapper);
        Integer playCount = Integer.parseInt(list.get(0).toString());

        return baseMapper.selectVideoViewNum(day);
    }

    /**
     * 增加课时
     * @param vid
     */
    @Override
    public void plusViewCount(String vid) {
        QueryWrapper<Video> wrapper = new QueryWrapper<Video>().eq("video_source_id", vid);
        List<Video> videos = baseMapper.selectList(wrapper);
        if(!CollectionUtils.isEmpty(videos)){
            Video video = videos.get(0);
            Long playCount = video.getPlayCount();
            // 将播放数量增加后放入video
            video.setPlayCount(++playCount);
            // 更新
            baseMapper.updateById(video);
        }
    }

    private List<String> getVideoSourceIdList(String column,String id){
        // 包装类：设置查询条件
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq(column,id);
        wrapper.select("video_source_id");
        // 查询
//        List<Object> videoSourceListObj = baseMapper.selectObjs(wrapper);
        List<Map<String, Object>> videoSourcemapList = baseMapper.selectMaps(wrapper);
        // 创建集合封装数据
        List<String> videoSourceList = new ArrayList<>();
        // 遍历
        for (Map<String, Object> map : videoSourcemapList) {
            String videoSourceId = (String) map.get("video_source_id");
            videoSourceList.add(videoSourceId);
        }
        return videoSourceList;
    }

    /*@Test
    public void test() throws Exception {
        String decode = URLDecoder.decode("%B5%E7%D7%D3%D0%C5%CF%A2%B9%A4%B3%CC%CA%C7%D2%BB%C3%C5%D3%A6%D3%C3%BC%C6%CB%E3%BB%FA%B5%C8%CF%D6%B4%FA%BB%AF%BC%BC%CA%F5%BD%F8%D0%D0%B5%E7%D7%D3%D0%C5%CF%A2%BF%D8%D6%C6%BA%CD%D0%C5%CF%A2%B4%A6%C0%ED%B5%C4%D1%A7%BF%C6%A3%AC%D6%F7%D2%AA%D1%D0%BE%BF%D0%C5%CF%A2%B5%C4%BB%F1%C8%A1%D3%EB%B4%A6%C0%ED%A3%AC%B5%E7%D7%D3%C9%E8%B1%B8%D3%EB%D0%C5%CF%A2%CF%B5%CD%B3%B5%C4%C9%E8%BC%C6%A1%A2%BF%AA%B7%A2%A1%A2%D3%A6%D3%C3%BA%CD%BC%AF%B3%C9%A1%A3%CF%D6%D4%DA%A3%AC%B5%E7%D7%D3%D0%C5%CF%A2%B9%A4%B3%CC%D2%D1%BE%AD%BA%AD%B8%C7%C1%CB%C9%E7%BB%E1%B5%C4%D6%EE%B6%E0%B7%BD%C3%E6%A3%AC%CF%F1%B5%E7%BB%B0%BD%BB%BB%BB%BE%D6%C0%EF%D4%F5%C3%B4%B4%A6%C0%ED%B8%F7%D6%D6%B5%E7%BB%B0%D0%C5%BA%C5%A3%AC%CA%D6%BB%FA%CA%C7%D4%F5%D1%F9%B4%AB%B5%DD%CE%D2%C3%C7%B5%C4%C9%F9%D2%F4%C9%F5%D6%C1%CD%BC%CF%F1%B5%C4%A3%AC%CE%D2%C3%C7%D6%DC%CE%A7%B5%C4%CD%F8%C2%E7%D4%F5%D1%F9%B4%AB%B5%DD%CA%FD%BE%DD%A3%AC%C9%F5%D6%C1%D0%C5%CF%A2%BB%AF%CA%B1%B4%FA%BE%FC%B6%D3%B5%C4%D0%C5%CF%A2%B4%AB%B5%DD%D6%D0%C8%E7%BA%CE%B1%A3%C3%DC%B5%C8%B6%BC%D2%AA%C9%E6%BC%B0%B5%E7%D7%D3%D0%C5%CF%A2%B9%A4%B3%CC%B5%C4%D3%A6%D3%C3%BC%BC%CA%F5%A1%A3%CE%D2%C3%C7%BF%C9%D2%D4%CD%A8%B9%FD%D2%BB%D0%A9%BB%F9%B4%A1%D6%AA%CA%B6%B5%C4%D1%A7%CF%B0%C8%CF%CA%B6%D5%E2%D0%A9%B6%AB%CE%F7%A3%AC%B2%A2%C4%DC%B9%BB%D3%A6%D3%C3%B8%FC%CF%C8%BD%F8%B5%C4%BC%BC%CA%F5%BD%F8%D0%D0%D0%C2%B2%FA%C6%B7%B5%C4%D1%D0%BE%BF%A1%A3%26%238194%3B%B5%E7%D7%D3%D0%C5%CF%A2%B9%A4%B3%CC%D7%A8%D2%B5%CA%C7%BC%AF%CF%D6%B4%FA%B5%E7%D7%D3%BC%BC%CA%F5%A1%A2%D0%C5%CF%A2%BC%BC%CA%F5%A1%A2%CD%A8%D0%C5%BC%BC%CA%F5%D3%DA%D2%BB%CC%E5%B5%C4%D7%A8%D2%B5%A1%A3%26%238194%3B%D1%A7%CF%B0%BB%F9%B1%BE%B5%E7%C2%B7%D6%AA%CA%B6%A3%AC%B2%A2%D5%C6%CE%D5%D3%C3%BC%C6%CB%E3%BB%FA%B5%C8%B4%A6%C0%ED%D0%C5%CF%A2%B5%C4%B7%BD%B7%A8%A1%A3%CA%D7%CF%C8%D2%AA%D3%D0%D4%FA%CA%B5%B5%C4%CA%FD%D1%A7%D6%AA%CA%B6%A3%AC%B6%D4%CE%EF%C0%ED%D1%A7%B5%C4%D2%AA%C7%F3%D2%B2%BA%DC%B8%DF%A3%AC%B2%A2%C7%D2%D6%F7%D2%AA%CA%C7%B5%E7%D1%A7%B7%BD%C3%E6%A3%BB%D2%AA%D1%A7%CF%B0%D0%ED%B6%E0%B5%E7%C2%B7%D6%AA%CA%B6%A1%A2%B5%E7%D7%D3%BC%BC%CA%F5%A1%A2%D0%C5%BA%C5%D3%EB%CF%B5%CD%B3%A1%A2%BC%C6%CB%E3%BB%FA%BF%D8%D6%C6%D4%AD%C0%ED%A1%A2%CD%A8%D0%C5%D4%AD%C0%ED%B5%C8%BB%F9%B1%BE%BF%CE%B3%CC%A1%A3%D1%A7%CF%B0%B5%E7%D7%D3%D0%C5%CF%A2%B9%A4%B3%CC%D7%D4%BC%BA%BB%B9%D2%AA%B6%AF%CA%D6%C9%E8%BC%C6%A1%A2%C1%AC%BD%D3%D2%BB%D0%A9%B5%E7%C2%B7%B2%A2%BD%E1%BA%CF%BC%C6%CB%E3%BB%FA%BD%F8%D0%D0%CA%B5%D1%E9%A3%AC%B6%D4%B6%AF%CA%D6%B2%D9%D7%F7%BA%CD%CA%B9%D3%C3%B9%A4%BE%DF%B5%C4%D2%AA%C7%F3%D2%B2%CA%C7%B1%C8%BD%CF%B8%DF%B5%C4%A1%A3%C6%A9%C8%E7%D7%D4%BC%BA%C1%AC%BD%D3%B4%AB%B8%D0%C6%F7%B5%C4%B5%E7%C2%B7%A3%AC%D3%C3%BC%C6%CB%E3%BB%FA%C9%E8%D6%C3%D0%A1%B5%C4%CD%A8%D0%C5%CF%B5%CD%B3%A3%AC%BB%B9%BB%E1%B2%CE%B9%DB%D2%BB%D0%A9%B4%F3%B9%AB%CB%BE%B5%C4%B5%E7%D7%D3%BA%CD%D0%C5%CF%A2%B4%A6%C0%ED%C9%E8%B1%B8%A3%AC%C0%ED%BD%E2%CA%D6%BB%FA%D0%C5%BA%C5%A1%A2%D3%D0%CF%DF%B5%E7%CA%D3%CA%C7%C8%E7%BA%CE%B4%AB%CA%E4%B5%C4%B5%C8%A3%AC%B2%A2%C4%DC%D3%D0%BB%FA%BB%E1%D4%DA%C0%CF%CA%A6%D6%B8%B5%BC%CF%C2%B2%CE%D3%EB%B4%F3%B5%C4%B9%A4%B3%CC%C9%E8%BC%C6%A1%A3%D6%D0%B9%FAIT%D0%D0%D2%B5%C6%F0%B2%BD%D6%C1%BD%F1%D3%D0%CA%AE%C4%EA%A3%AC%BA%DC%C4%EA%C7%E1%A1%A3%D0%C2%CF%CA%B5%C4%CA%C2%CE%EF%A1%A2%B3%AF%D1%F4%B5%C4%B2%FA%D2%B5%D7%DC%CA%C7%B1%B8%CA%DC%D7%A2%C4%BF%A1%A3%D5%FD%CA%C7%D5%E2%B8%F6%D4%AD%D2%F2%A3%AC%BC%C6%CB%E3%BB%FA%D7%A8%D2%B5%D1%B8%CB%D9%B3%C9%CE%AA%B8%DF%D0%A3%B5%C4%C8%C8%C3%C5%D7%A8%D2%B5%A3%AC%B2%BB%C9%D9%CD%AC%D1%A7%CF%F7%BC%E2%D3%D6%D4%D9%CF%F7%BC%E2%C1%CB%C4%D4%B4%FC%CD%F9%D5%E2%B8%F6%CF%F3%D1%C0%CB%FE%C0%EF%B5%C4%CF%F3%D1%C0%B6%A5%D7%EA%A3%AC%BB%F2%CE%AA%D0%CB%C8%A4%A3%AC%BB%F2%CE%AA%C4%B1%C9%FA%D5%C6%CE%D5%D2%BB%C3%C5%BC%BC%C4%DC%A3%AC%BB%F2%CE%AA%C7%B0%CD%BE%B8%FC%BA%C3%B8%FC%BF%EC%B5%D8%B7%A2%D5%B9%A1%A3%CF%E0%B1%C8%C7%B0%BC%B8%C4%EA%B5%C4%BC%C6%CB%E3%BB%FA%D7%A8%D2%B5%B5%C4%BB%F0%B1%AC%A3%AC%BD%FC%C4%EA%C0%B4%B6%D4%D5%E2%B8%F6%D7%A8%D2%B5%B5%C4%D1%A1%D4%F1%BD%A5%C7%F7%D3%DA%C1%CB%C0%ED%D0%D4%BA%CD%BF%CD%B9%DB%A1%A3%26%238194%3B%0A%26%238194%3B%26%238194%3B%26%238194%3B%B5%E7%D7%D3%D0%C5%CF%A2%B2%FA%D2%B5%CA%C7%D2%BB%CF%EE%D0%C2%D0%CB%B5%C4%B8%DF%BF%C6%BC%BC%B2%FA%D2%B5%A3%AC%B1%BB%B3%C6%CE%AA%B3%AF%D1%F4%B2%FA%D2%B5%A1%A3%B8%F9%BE%DD%D0%C5%CF%A2%B2%FA%D2%B5%B2%BF%B7%D6%CE%F6%A3%AC%A1%B0%CA%AE%CE%E5%A1%B1%C6%DA%BC%E4%CA%C7%CE%D2%B9%FA%B5%E7%D7%D3%D0%C5%CF%A2%B2%FA%D2%B5%B7%A2%D5%B9%B5%C4%B9%D8%BC%FC%CA%B1%C6%DA%A3%AC%D4%A4%BC%C6%B5%E7%D7%D3%D0%C5%CF%A2%B2%FA%D2%B5%C8%D4%BD%AB%D2%D4%B8%DF%D3%DA%BE%AD%BC%C3%D4%F6%CB%D9%C1%BD%B1%B6%D7%F3%D3%D2%B5%C4%CB%D9%B6%C8%BF%EC%CB%D9%B7%A2%D5%B9%A3%AC%B2%FA%D2%B5%C7%B0%BE%B0%CA%AE%B7%D6%B9%E3%C0%AB%A1%A3%26%238194%3B%CE%B4%C0%B4%B5%C4%B7%A2%D5%B9%D6%D8%B5%E3%CA%C7%B5%E7%D7%D3%D0%C5%CF%A2%B2%FA%C6%B7%D6%C6%D4%EC%D2%B5%A1%A2%C8%ED%BC%FE%B2%FA%D2%B5%BA%CD%BC%AF%B3%C9%B5%E7%C2%B7%B5%C8%B2%FA%D2%B5%A3%BB%D0%C2%D0%CB%CD%A8%D0%C5%D2%B5%CE%F1%C8%E7%CA%FD%BE%DD%CD%A8%D0%C5%A1%A2%B6%E0%C3%BD%CC%E5%A1%A2%BB%A5%C1%AA%CD%F8%A1%A2%B5%E7%BB%B0%D0%C5%CF%A2%B7%FE%CE%F1%A1%A2%CA%D6%BB%FA%B6%CC%D0%C5%B5%C8%D2%B5%CE%F1%D2%B2%BD%AB%D1%B8%CB%D9%C0%A9%D5%B9%A3%BB%D6%B5%B5%C3%B9%D8%D7%A2%B5%C4%BB%B9%D3%D0%CE%C4%BB%AF%BF%C6%BC%BC%B2%FA%D2%B5%A3%AC%C8%E7%CD%F8%C2%E7%D3%CE%CF%B7%B5%C8%A1%A3%C4%BF%C7%B0%A3%AC%D0%C5%CF%A2%BC%BC%CA%F5%D6%A7%B3%D6%C8%CB%B2%C5%D0%E8%C7%F3%D6%D0%C5%C5%B3%FD%BC%BC%CA%F5%B9%CA%D5%CF%A1%A2%C9%E8%B1%B8%BA%CD%B9%CB%BF%CD%B7%FE%CE%F1%A1%A2%D3%B2%BC%FE%BA%CD%C8%ED%BC%FE%B0%B2%D7%B0%D2%D4%BC%B0%C5%E4%D6%C3%B8%FC%D0%C2%BA%CD%CF%B5%CD%B3%B2%D9%D7%F7%A1%A2%BC%E0%CA%D3%D3%EB%CE%AC%D0%DE%B5%C8%CB%C4%C0%E0%C8%CB%B2%C5%D7%EE%CE%AA%B6%CC%C8%B1%A1%A3%B4%CB%CD%E2%A3%AC%B5%E7%D7%D3%C9%CC%CE%F1%BA%CD%BB%A5%B6%AF%C3%BD%CC%E5%A1%A2%CA%FD%BE%DD%BF%E2%BF%AA%B7%A2%BA%CD%C8%ED%BC%FE%B9%A4%B3%CC%B7%BD%C3%E6%B5%C4%D0%E8%C7%F3%C1%BF%D2%B2%B7%C7%B3%A3%B4%F3%A1%A3%26%238194%3B%CE%B4%C0%B4%D5%B9%CD%FB%26%238194%3B%B5%E7%D7%D3%D0%C5%CF%A2%B9%A4%B3%CC%CA%C7%D2%BB%C3%C5%D3%A6%D3%C3%BC%C6%CB%E3%BB%FA%B5%C8%CF%D6%B4%FA%BB%AF%BC%BC%CA%F5%BD%F8%D0%D0%B5%E7%D7%D3%D0%C5%CF%A2%BF%D8%D6%C6%BA%CD%D0%C5%CF%A2%B4%A6%C0%ED%B5%C4%D1%A7%BF%C6%A3%AC%D6%F7%D2%AA%D1%D0%BE%BF%D0%C5%CF%A2%B5%C4%BB%F1%C8%A1%D3%EB%B4%A6%C0%ED%A3%AC%B5%E7%D7%D3%C9%E8%B1%B8%D3%EB%D0%C5%CF%A2%CF%B5%CD%B3%B5%C4%C9%E8%BC%C6%A1%A2%BF%AA%B7%A2%A1%A2%D3%A6%D3%C3%BA%CD%BC%AF%B3%C9%A1%A3%CF%D6%D4%DA%A3%AC%B5%E7%D7%D3%D0%C5%CF%A2%B9%A4%B3%CC%D2%D1%BE%AD%BA%AD%B8%C7%C1%CB%C9%E7%BB%E1%B5%C4%D6%EE%B6%E0%B7%BD%C3%E6%A3%AC%CF%F1%B5%E7%BB%B0%BD%BB%BB%BB%BE%D6%C0%EF%D4%F5%C3%B4%B4%A6%C0%ED%B8%F7%D6%D6%B5%E7%BB%B0%D0%C5%BA%C5%A3%AC%CA%D6%BB%FA%CA%C7%D4%F5%D1%F9%B4%AB%B5%DD%CE%D2%C3%C7%B5%C4%C9%F9%D2%F4%C9%F5%D6%C1%CD%BC%CF%F1%B5%C4%A3%AC%CE%D2%C3%C7%D6%DC%CE%A7%B5%C4%CD%F8%C2%E7%D4%F5%D1%F9%B4%AB%B5%DD%CA%FD%BE%DD%A3%AC%C9%F5%D6%C1%D0%C5%CF%A2%BB%AF%CA%B1%B4%FA%BE%FC%B6%D3%B5%C4%D0%C5%CF%A2%B4%AB%B5%DD%D6%D0%C8%E7%BA%CE%B1%A3%C3%DC%B5%C8%B6%BC%D2%AA%C9%E6%BC%B0%B5%E7%D7%D3%D0%C5%CF%A2%B9%A4%B3%CC%B5%C4%D3%A6%D3%C3%BC%BC%CA%F5%A1%A3%CE%D2%C3%C7%BF%C9%D2%D4%CD%A8%B9%FD%D2%BB%D0%A9%BB%F9%B4%A1%D6%AA%CA%B6%B5%C4%D1%A7%CF%B0%C8%CF%CA%B6%D5%E2%D0%A9%B6%AB%CE%F7%A3%AC%B2%A2%C4%DC%B9%BB%D3%A6%D3%C3%B8%FC%CF%C8%BD%F8%B5%C4%BC%BC%CA%F5%BD%F8%D0%D0%D0%C2%B2%FA%C6%B7%B5%C4%D1%D0%BE%BF%BA%CD%BF%AA%B7%A2%A1%A3%26%238194%3B%26%238194%3B%BC%F2%B5%A5%B5%C4%B9%E6%BB%AE%A3%AC%CA%D7%CF%C8%B1%A3%D6%A4%B8%FA%C9%CF%C0%CF%CA%A6%B5%C4%BD%C5%B2%BD%A3%AC%B0%D1%BF%CE%CC%C3%C4%DA%C8%DD%B8%E3%BA%C3%A3%AC%D2%F2%CE%AA%CC%FA%B5%C0%D3%D0%D2%BB%B8%F6%D6%C7%C4%DC%BB%FA%C6%F7%C8%CB%B5%C4%C6%BD%CC%A8%A3%AC%CB%F9%D2%D4%BF%CE%D3%E0%CA%B1%BC%E4%C8%A5%B2%CE%BC%D3%D2%BB%CF%C2%A3%AC%D5%E2%B8%F6%C5%E0%D1%B5%A3%AC%BF%C9%CC%E1%B8%DF%D7%D4%BC%BA%D5%E6%D5%FD%B5%D8%B6%AF%CA%D6%C4%DC%C1%A6%26%238194%3B%A3%AC%CA%FD%BE%DD%BD%E1%B9%B9%B5%C8%BF%CE%B3%CC%B6%BC%D0%E8%D2%AA%D3%D0%BD%CF%BA%C3%B5%C4c%D3%EF%D1%D4%C4%DC%C1%A6%A3%AC%CB%F9%D2%D4%BF%CE%D3%E0%CA%B1%BC%E4%B8%E3%D2%BB%CF%C2%D4%AD%C0%B4%B5%C4c%D3%EF%D1%D4%B5%C4%BF%CE%B3%CC%C9%E8%BC%C6%A3%AC%C1%ED%CD%E2%CE%D2%C4%DA%D0%C4%D7%DC%D3%D0%D2%BB%D6%D6%BE%AD%C9%CC%B5%C4%B3%E5%B6%AF%A3%AC%CB%F9%D2%D4%D4%DA%D3%D0%D7%E3%B9%BB%BF%CE%D3%E0%CA%B1%BC%E4%B5%C4%C7%E9%BF%F6%CF%C2%A3%AC%BC%E6%D0%DE%D2%BB%CF%C2%BE%AD%BC%C3%D1%A7%A3%AC%B9%FA%BC%CA%BB%E1%BC%C6%CA%A6%D2%BB%D6%B1%CA%C7%CE%D2%BC%D2%C8%CB%BA%CD%CE%D2%B5%C4%C3%CE%CF%EB%A1%AD%A1%AD",
                "GBK");
    System.out.println("decode" + decode);

    }*/

}
