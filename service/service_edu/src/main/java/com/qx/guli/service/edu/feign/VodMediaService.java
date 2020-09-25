package com.qx.guli.service.edu.feign;

import com.qx.guli.common.base.result.R;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Classname VodMediaService
 * @Description TODO
 * @Date 2020/6/17 16:21
 * @Created by 卿星
 */
@Service
@FeignClient(value = "service-vod")
public interface VodMediaService {

    @DeleteMapping("/admin/vod/media/remove/{id}")
    R removeByVideoSourceId(@PathVariable(value = "id",required = true) String id);

    @DeleteMapping("/admin/vod/media/remove")
    R removeVideoByIdList(@RequestBody List<String> videoIdList);

    @PostMapping("/admin/vod/media/upload")
    R upload(@RequestParam(value = "file",required = true) MultipartFile multipartFile);
}
