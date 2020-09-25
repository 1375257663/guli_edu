package com.qx.guli.service.cms.feign.fallback;

import com.qx.guli.common.base.result.R;
import com.qx.guli.service.cms.feign.OssFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Classname OssFileServiceFallBack
 * @Description TODO
 * @Date 2020/6/21 21:14
 * @Created by 卿星
 */
@Slf4j
@Service
public class OssFileServiceFallBack implements OssFeignService {
    @Override
    public R removeFile(String url) {
        log.info("熔断保护");
        return R.error().message("调用超时");
    }
}
