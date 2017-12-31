package io.renren.modules.generator.test;


import io.renren.modules.generator.dao.StudentMajorMapper;
import io.renren.modules.generator.dao.StudentMapper;
import io.renren.modules.generator.entity.Student;
import io.renren.modules.generator.entity.StudentMajor;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class JJJTest {
    private SqlSession ss;
    private StudentMapper sm;
    private StudentMajorMapper smm;

    @Before
    public void setUp() throws Exception {
    	String resource = "mybatis.xml";
    	InputStream inputStream = Resources.getResourceAsStream(resource);
    	SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        ss=sqlSessionFactory.openSession();
        sm=ss.getMapper(StudentMapper.class);
        smm=ss.getMapper(StudentMajorMapper.class);
        
    }
    @After
    public void tearDown() throws Exception {
        ss.commit();
        ss.close();
    }
    //一对多查询
    public void test() {
        List<StudentMajor> list=smm.selectAll();
        for(StudentMajor a:list){
            System.out.println(a);
        }
    }
    //根据专业查人员，给一对多用
    public void selectz(){
        List<Student> l=sm.selectz(3);
        for(Student a:l){
            System.out.println(a);
        }
    }


    //多对一查询
   
    public void selectall() {
        List<Student> st=sm.selectall();
        for(Student tt:st){
            System.out.println(tt);
        }
    }
    @Test
    public void selectone() {
        Student st=sm.queryObject(1);
        
            System.out.println(st);
       
    }
    //根据主键查询，给多对一用
    public void select(){
        StudentMajor a=smm.select(1);
        System.out.println(a);
    }

}