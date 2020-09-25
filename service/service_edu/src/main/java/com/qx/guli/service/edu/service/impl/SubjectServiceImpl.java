package com.qx.guli.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.qx.guli.service.edu.entity.Subject;
import com.qx.guli.service.edu.entity.excel.ExcelSubjectData;
import com.qx.guli.service.edu.entity.vo.SubjectVo;
import com.qx.guli.service.edu.listener.ExcelSubjectDataListener;
import com.qx.guli.service.edu.mapper.SubjectMapper;
import com.qx.guli.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author qx
 * @since 2020-06-04
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void uploadSubjectByExcel(InputStream inputStream) {
        // 指定读的类，监听器，读取类型，默认读取第一个sheet 文件流inputStream会自动关闭，不用手动关闭
        EasyExcel
                .read(inputStream, ExcelSubjectData.class,new ExcelSubjectDataListener(baseMapper))
                .excelType(ExcelTypeEnum.XLS)
                .sheet()
                .doRead();

    }

    @Override
    public List<SubjectVo> nestedList() {

        return baseMapper.selectNestedListByParentId("0");
    }
}
