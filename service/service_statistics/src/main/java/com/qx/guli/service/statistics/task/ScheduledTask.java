package com.qx.guli.service.statistics.task;

import com.qx.guli.service.statistics.service.DailyService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Classname ScheduledTask
 * @Description TODO
 * @Date 2020/6/30 9:54
 * @Created by 卿星
 */
@Component
@Slf4j
public class ScheduledTask {

    @Autowired
    private DailyService dailyService;

    // 0 0 1 1/1 * ? *
   /* @Scheduled(cron = "0 8,9 * * * ?")
    public void task1(){
        System.out.println("执行任务中。。。");
    }*/

    /**
     * 每天凌晨1点执行任务
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void taskGenarateStatisticsData(){
        // 获取当前日期并减一天
        String day = new DateTime().minusDays(1).toString("yyyy-MM-dd");
        dailyService.createStatisticsByDay(day);
        log.info("taskGenarateStatisticsData 统计完毕");
    }

}
