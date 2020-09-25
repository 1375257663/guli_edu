package com.qx.guli.service.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qx.guli.common.base.result.R;
import com.qx.guli.service.statistics.entity.Daily;
import com.qx.guli.service.statistics.feign.EduService;
import com.qx.guli.service.statistics.feign.UcenterMemberService;
import com.qx.guli.service.statistics.mapper.DailyMapper;
import com.qx.guli.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author qx
 * @since 2020-06-29
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    private UcenterMemberService ucenterMemberService;
    @Autowired
    private EduService eduService;

    @Override
    public Daily createStatisticsByDay(String day) {

        // 注册数
        R r = ucenterMemberService.countRegisterNum(day);
        Map<String, Object> data = r.getData();
        int registerCount = (Integer)data.get("registerCount");
        // 课程
        R r1 = eduService.countCourseNum(day);
        int courserCount = (Integer) r1.getData().get("courserCount");
        // 视频
        int playCount = (Integer) eduService.countVideoeNum(day).getData().get("playCount");

        // 判断当天是否有统计分析
        Daily daily = baseMapper.selectByDay(day);
        if(daily == null){
            daily = new Daily();
            daily.setRegisterNum(registerCount);
            daily.setCourseNum(courserCount);
            daily.setVideoViewNum(playCount);
            daily.setLoginNum(0);
            daily.setDateCalculated(day);
            // 保存
            baseMapper.insert(daily);
        }else {
            // 更新
            daily.setRegisterNum(registerCount);
            daily.setCourseNum(courserCount);
            daily.setVideoViewNum(playCount);
            daily.setDateCalculated(day);
            // 如果daily存在则使用默认LoginNum
//            daily.setLoginNum();
            // 执行更新
            baseMapper.updateById(daily);
        }

        return daily;
    }

    @Override
    public void plusLoginNum() {

        // 获取当前日期
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        // 查询当天是否有统计分析
        QueryWrapper<Daily> wrapper = new QueryWrapper<Daily>().eq("date_calculated", date);
        Daily daily = baseMapper.selectOne(wrapper);
        // 如果没有,则新建
        if(null == daily){
            daily = this.createStatisticsByDay(date);
            // 加登录数，并保存
            Integer loginNum = daily.getLoginNum();
            daily.setLoginNum(++loginNum);
            daily.setDateCalculated(date);
            baseMapper.updateById(daily);
        }else {
            Integer loginNum = daily.getLoginNum();
            daily.setLoginNum(++loginNum);
            // 更新
            baseMapper.updateById(daily);
        }

    }

    @Override
    public Map<String, Map<String, Object>> getChartData(String begin, String end) {

        // 注册人数
        Map<String, Object> registerNum = this.getChartDataByType(begin, end, "register_num");

        // 登录人数
        Map<String, Object> loginNum = this.getChartDataByType(begin, end, "login_num");

        // 每日播放视频数
        Map<String, Object> videoViewNum = this.getChartDataByType(begin, end, "video_view_num");

        // 每日新增课程数
        Map<String, Object> courseNum = this.getChartDataByType(begin, end, "course_num");
        // 组装数据
        Map<String, Map<String, Object>> resultMap = new HashMap<>();
        resultMap.put("register_num",registerNum);
        resultMap.put("login_num",loginNum);
        resultMap.put("video_view_num",videoViewNum);
        resultMap.put("course_num",courseNum);
        return resultMap;
    }

    private Map<String,Object> getChartDataByType(String begin,String end,String type){

        QueryWrapper<Daily> wrapper = new QueryWrapper<>();
        // 查询条件
        wrapper.select("date_calculated", type);
        wrapper.between("date_calculated",begin,end);

        // 查询
        List<Map<String, Object>> mapList = baseMapper.selectMaps(wrapper);
        
        // x轴日期存储对象
        List<String> xAxisList = new ArrayList<String>();
        // y轴整数型数据
        List<Integer> yAxisList = new ArrayList<Integer>();

        // 遍历
        for (Map<String, Object> map : mapList) {
            // 组装x轴时间
            String dateCalculated = (String)map.get("date_calculated");
            xAxisList.add(dateCalculated);
            // 组装y轴数据
            Integer typeDate = (Integer)map.get(type);
            yAxisList.add(typeDate);
        }
        // 将x轴数据，y轴数据存入map
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("xList",xAxisList);
        resultMap.put("yList",yAxisList);

        return resultMap;
    }
}
