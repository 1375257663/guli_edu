package com.qx.guli.service.edu.feign.fallback;

import com.qx.guli.common.base.result.R;
import com.qx.guli.service.edu.feign.VodMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Classname VodMediaServiceFallBack
 * @Description TODO
 * @Date 2020/6/17 16:37
 * @Created by 卿星
 */
@Service
@Slf4j
public class VodMediaServiceFallBack implements VodMediaService {

    @Override
    public R removeByVideoSourceId(String id) {
        log.info("熔断保护");
        return R.error();
    }

    @Override
    public R removeVideoByIdList(List<String> videoIdList) {
        log.info("熔断保护");
        return R.error();
    }

    @Override
    public R upload(MultipartFile multipartFile) {
        log.info("熔断保护");
        return R.error();
    }
}
