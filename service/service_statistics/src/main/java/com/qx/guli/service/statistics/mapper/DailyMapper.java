package com.qx.guli.service.statistics.mapper;

import com.qx.guli.service.statistics.entity.Daily;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 网站统计日数据 Mapper 接口
 * </p>
 *
 * @author qx
 * @since 2020-06-29
 */
public interface DailyMapper extends BaseMapper<Daily> {

    Daily selectByDay(String day);
}
