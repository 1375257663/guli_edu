package com.qx.guli.service.edu.service;

import com.qx.guli.service.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author qx
 * @since 2020-06-04
 */
public interface VideoService extends IService<Video> {

    Video getByChapterId(String chapterId);

    void removeMediaVideoById(String id);

    void removeMediaVideoByIdList(List<String> idList);

    void removeVideoByChapterId(String id);

    void removeMediaVideoByCourseId(String id);

    Integer countVideoViewNum(String day);

    void plusViewCount(String vid);
}
