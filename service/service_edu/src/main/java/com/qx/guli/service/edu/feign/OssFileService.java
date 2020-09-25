package com.qx.guli.service.edu.feign;

import com.qx.guli.common.base.result.R;
import com.qx.guli.service.edu.feign.fallback.OssFileServiceFallBack;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname OssfileService
 * @Description TODO
 * @Date 2020/6/8 19:40
 * @Created by 卿星
 */
@Service
// value：发生异常要服务降级处理的微服务，fallback：降级处理的类
@FeignClient(value = "service-oss",fallback = OssFileServiceFallBack.class)
public interface OssFileService {

    @ApiOperation("测试")
    @GetMapping("/admin/oss/file/test")
    R test();

    @DeleteMapping("/admin/oss/file/remove")
    R removeFile(@RequestParam String url);

}
