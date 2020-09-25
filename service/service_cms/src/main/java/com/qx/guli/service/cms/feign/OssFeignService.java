package com.qx.guli.service.cms.feign;

import com.qx.guli.common.base.result.R;
import com.qx.guli.service.cms.feign.fallback.OssFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Classname OssFeignService
 * @Description TODO
 * @Date 2020/6/21 18:05
 * @Created by 卿星
 */
//@Service // OssFileServiceFallBack实现了该类，故可以不写
@FeignClient(value = "oss-service",fallback = OssFileServiceFallBack.class)
public interface OssFeignService {

    @DeleteMapping("/admin/oss/file/remove")
    R removeFile(@RequestBody String url);
}
