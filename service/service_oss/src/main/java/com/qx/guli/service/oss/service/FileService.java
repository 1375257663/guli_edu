package com.qx.guli.service.oss.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @Classname FileService
 * @Description TODO
 * @Date 2020/6/7 16:03
 * @Created by 卿星
 */
public interface FileService {
    String upload(InputStream inputStream,String module,String originalFileName);

    void removeFile(String url);
}
