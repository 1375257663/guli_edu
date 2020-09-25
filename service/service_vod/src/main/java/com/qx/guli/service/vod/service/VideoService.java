package com.qx.guli.service.vod.service;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Classname VideoService
 * @Description TODO
 * @Date 2020/6/16 23:21
 * @Created by 卿星
 */
public interface VideoService {

    String uploadVideo(MultipartFile file);

    boolean removeVideoById(String id);

    void removeMediaVideoByIdList(List<String> videoIdList) throws ClientException;

    String getPlayAuth(String videoSourceId) throws ClientException;
}
