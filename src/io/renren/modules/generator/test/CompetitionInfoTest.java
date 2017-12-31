package io.renren.modules.generator.test;


import io.renren.modules.generator.dao.CompetitionInfoDao;
import io.renren.modules.generator.dao.CompetitionNameDao;
import io.renren.modules.generator.dao.StudentMajorMapper;
import io.renren.modules.generator.dao.StudentMapper;
import io.renren.modules.generator.entity.CompetitionInfoEntity;
import io.renren.modules.generator.entity.Student;
import io.renren.modules.generator.entity.StudentMajor;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CompetitionInfoTest {
    private SqlSession ss;
    private CompetitionInfoDao sm;
    private CompetitionNameDao smm;

    @Before
    public void setUp() throws Exception {
    	String resource = "mybatis.xml";
    	InputStream inputStream = Resources.getResourceAsStream(resource);
    	SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        ss=sqlSessionFactory.openSession();
        sm=ss.getMapper(CompetitionInfoDao.class);
        smm=ss.getMapper(CompetitionNameDao.class);
        
    }
    @After
    public void tearDown() throws Exception {
        ss.commit();
        ss.close();
    }


    //多对一查询
    @Test
    public void selectall() {
    	Map<String,Object>map = new HashMap<String,Object>();
//        List<CompetitionInfoEntity> st=sm.queryList(map);
//        for(CompetitionInfoEntity tt:st){
//            System.out.println(tt);
//        }
    	CompetitionInfoEntity et = sm.queryObject(new Integer(1));
    	System.out.println(et);
    }


}