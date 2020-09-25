package com.qx.guli.service.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qx.guli.common.base.result.R;
import com.qx.guli.service.cms.entity.Ad;
import com.qx.guli.service.cms.entity.vo.AdVo;
import com.qx.guli.service.cms.feign.OssFeignService;
import com.qx.guli.service.cms.mapper.AdMapper;
import com.qx.guli.service.cms.mapper.AdTypeMapper;
import com.qx.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.xml.crypto.Data;
import java.util.List;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author qx
 * @since 2020-06-21
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {

    @Autowired
    private OssFeignService ossFeignService;

    @Override
    public IPage<AdVo> selectPage(Long current, Long size) {
        // 设置分页条件
        Page<AdVo> page = new Page<>(current,size);
        // 设置查询条件
        QueryWrapper<AdVo> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("ca.type_id","ca.sort");
        List<AdVo> records = baseMapper.selectPageByQueryWrapper(page,wrapper);
        page.setRecords(records);
        return page;
    }

    @Override
    public boolean removeAdImageById(String id) {
        // 获取当前Image
        Ad ad = baseMapper.selectById(id);
        if(ad != null){
            String imageUrl = ad.getImageUrl();
            if(!StringUtils.isEmpty(imageUrl)){
                // 远程调用删除
                R r = ossFeignService.removeFile(imageUrl);
                return r.getSuccess();
            }
        }
        return false;
    }

    @Cacheable(value = "index",key = "'listByAdTypeId'")
    @Override
    public List<Ad> listByAdTypeId(String adTypeId) {

        QueryWrapper<Ad> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort","id");
        wrapper.eq("type_id", adTypeId);

        return baseMapper.selectList(wrapper);
    }
}
