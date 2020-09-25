package com.qx.guli.service.statistics.controller.admin;


import com.qx.guli.common.base.result.R;
import com.qx.guli.service.statistics.entity.Daily;
import com.qx.guli.service.statistics.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author qx
 * @since 2020-06-29
 */
@Api(description="统计分析管理")
@RestController
@RequestMapping("/admin/statistics/daily")
@EnableSwagger2
public class DailyController {

    @Autowired
    private DailyService dailyService;

    @ApiOperation("增加登录数")
    @PutMapping("plus/login-num")
    public R plusLoginNum(){

        dailyService.plusLoginNum();
        return R.ok();
    }

    @ApiOperation("生成统计记录")
    @PostMapping("create/{day}")
    public R createStatisticsByDay(
            @ApiParam("统计日期")
            @PathVariable String day) {

        dailyService.createStatisticsByDay(day);
        return R.ok().message("数据统计生成成功");
    }

    @ApiOperation("显示统计数据")
    @GetMapping("show-chart/{begin}/{end}")
    public R showChart(
            @ApiParam("开始时间") @PathVariable String begin,
            @ApiParam("结束时间") @PathVariable String end){

        Map<String,Map<String,Object>> chartData = dailyService.getChartData(begin,end);
        return R.ok().data("chartData",chartData);
    }

}

