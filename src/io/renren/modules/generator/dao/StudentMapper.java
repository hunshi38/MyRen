package io.renren.modules.generator.dao;

import io.renren.modules.generator.entity.Student;

import java.util.List;
import java.util.Map;



public interface StudentMapper {
    /**
     * 全表查询
     */
    public List<Student> selectall();
    public Student queryObject(Integer id);
    public List<Student> selectz(Integer major);
}