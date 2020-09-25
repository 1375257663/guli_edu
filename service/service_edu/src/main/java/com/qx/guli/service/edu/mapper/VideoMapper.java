package com.qx.guli.service.edu.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.qx.guli.service.edu.entity.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qx.guli.service.edu.entity.vo.VideoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程视频 Mapper 接口
 * </p>
 *
 * @author qx
 * @since 2020-06-04
 */
public interface VideoMapper extends BaseMapper<Video> {

    List<VideoVo> selectVideoVoList(@Param(Constants.WRAPPER) QueryWrapper<Video> wrapperVideo);

    Integer selectVideoViewNum(String day);
}
