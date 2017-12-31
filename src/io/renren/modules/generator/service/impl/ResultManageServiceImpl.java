package io.renren.modules.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.modules.generator.dao.ResultManageDao;
import io.renren.modules.generator.entity.ResultManageEntity;
import io.renren.modules.generator.service.ResultManageService;



@Service("resultManageService")
public class ResultManageServiceImpl implements ResultManageService {
	@Autowired
	private ResultManageDao resultManageDao;
	
	@Override
	public ResultManageEntity queryObject(Integer id){
		return resultManageDao.queryObject(id);
	}
	
	@Override
	public List<ResultManageEntity> queryList(Map<String, Object> map){
		return resultManageDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return resultManageDao.queryTotal(map);
	}
	
	@Override
	public void save(ResultManageEntity resultManage){
		resultManageDao.save(resultManage);
	}
	
	@Override
	public void update(ResultManageEntity resultManage){
		resultManageDao.update(resultManage);
	}
	
	@Override
	public void delete(Integer id){
		resultManageDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		resultManageDao.deleteBatch(ids);
	}

	@Override
	public ResultManageEntity queryObjectByCompetitionId(Integer competitionId) {
		// TODO Auto-generated method stub
		return resultManageDao.queryObjectByCompetitionId(competitionId);
	}
	
}
