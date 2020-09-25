package com.qx.guli.service.statistics.service;

import com.qx.guli.service.statistics.entity.Daily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author qx
 * @since 2020-06-29
 */
public interface DailyService extends IService<Daily> {

    Daily createStatisticsByDay(String day);

    void plusLoginNum();

    Map<String, Map<String, Object>> getChartData(String begin, String end);
}
