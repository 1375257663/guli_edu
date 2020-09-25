package com.qx.guli.service.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qx.guli.service.cms.entity.Ad;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qx.guli.service.cms.entity.vo.AdVo;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务类
 * </p>
 *
 * @author qx
 * @since 2020-06-21
 */
public interface AdService extends IService<Ad> {

    IPage<AdVo> selectPage(Long current, Long size);

    boolean removeAdImageById(String id);

    List<Ad> listByAdTypeId(String adTypeId);
}
