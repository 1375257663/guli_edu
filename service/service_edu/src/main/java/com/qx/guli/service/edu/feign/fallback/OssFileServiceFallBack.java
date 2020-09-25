package com.qx.guli.service.edu.feign.fallback;

import com.qx.guli.common.base.result.R;
import com.qx.guli.service.edu.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Classname OssFileServiceFallBack
 * @Description TODO
 * @Date 2020/6/9 17:49
 * @Created by 卿星
 */
@Slf4j
@Service
public class OssFileServiceFallBack implements OssFileService {


    @Override
    public R test() {
        log.info("服务熔断。。。经过降级处理。。。");
        return R.ok();
    }

    @Override
    public R removeFile(String url) {
        log.info("服务熔断。。。经过降级处理。。。");
        return R.error();
    }
}
