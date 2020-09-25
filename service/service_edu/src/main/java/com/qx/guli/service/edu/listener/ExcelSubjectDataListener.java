package com.qx.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qx.guli.common.base.result.ResultCodeEnum;
import com.qx.guli.service.edu.entity.Subject;
import com.qx.guli.service.edu.entity.excel.ExcelSubjectData;
import com.qx.guli.service.edu.mapper.SubjectMapper;
import com.qx.guli.service.base.exception.GuliException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Classname ExcelSubjectDataListener
 * @Description TODO
 * @Date 2020/6/10 10:24
 * @Created by 卿星
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {

    private SubjectMapper subjectMapper;

    /**
     * 每读取一条数据都会调用该方法
     * @param excelSubjectData
     * @param analysisContext
     */
    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        log.info("文件上传中，当前文件：{}"+ excelSubjectData);

        if(excelSubjectData == null){
            throw new GuliException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }
        // 获取当前文件的以及分类
        String levelOneTitle = excelSubjectData.getLevelOneTitle();
        // 获取当前文件的以及分类
        String levelTwoTitle = excelSubjectData.getLevelTwoTitle();
        // 判断当前一级分类是否存在
        Subject subject = getByTitle(levelOneTitle);
        String LevelOneId = null;
        if(subject == null){
            // 如果未接收到数据，则Subject对象不会创建
            subject = new Subject();
            // 为当前subject设置属性
            subject.setTitle(levelOneTitle);
            subject.setParentId("0");
            // 插入数据库
            subjectMapper.insert(subject);
            LevelOneId = subject.getId();
        }else{
            // 如果当前分类存在，直接赋值
            LevelOneId = subject.getId();
        }
        // 插入二级标题
        // 判断二级标题是否存在
        // 获取二级标题副标题

        Subject subject1 = getByParentIdAndTitle(LevelOneId,levelTwoTitle);
        if(subject1 == null){
            // 如果未接收到数据，则Subject对象不会创建
            subject1 = new Subject();
            subject1.setTitle(levelTwoTitle);
            subject1.setParentId(LevelOneId);
           // 插入数据库
            subjectMapper.insert(subject1);
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("文件上传完成。。");
    }

    /**
     *  判断当前一级分类是否存在
     * @param levelTitle
     * @return
     */
    private Subject getByTitle(String levelTitle){
        // 创建包装类
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        // 设置条件
        queryWrapper.eq("title",levelTitle);
        queryWrapper.eq("parent_id",0);
        Subject subject = subjectMapper.selectOne(queryWrapper);
        return subject;
    }

    /**
     * 根据分类名称和父id查询这个二级分类是否存在
     * @param id
     * @return
     */
    private Subject getByParentIdAndTitle(String id,String title){
        // 创建包装类
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        // 设置条件
        queryWrapper.eq("parent_id",id);
        queryWrapper.eq("title",title);
        return subjectMapper.selectOne(queryWrapper);
    }
}
