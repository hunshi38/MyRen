package io.renren.modules.generator.dao;

import io.renren.modules.generator.entity.StudentMajor;

import java.util.List;


public interface StudentMajorMapper {
    /**
     * 全表查询
     * @return
     */
    public List<StudentMajor> selectAll();
    /**
     * 根据主键查数据,给多对一用
     * @param id
     * @return
     */
    public StudentMajor select(Integer id);
}