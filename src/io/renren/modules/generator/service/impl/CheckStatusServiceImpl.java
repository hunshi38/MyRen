package io.renren.modules.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.modules.generator.dao.CheckStatusDao;
import io.renren.modules.generator.entity.CheckStatusEntity;
import io.renren.modules.generator.service.CheckStatusService;



@Service("checkStatusService")
public class CheckStatusServiceImpl implements CheckStatusService {
	@Autowired
	private CheckStatusDao checkStatusDao;
	
	@Override
	public CheckStatusEntity queryObject(Integer id){
		return checkStatusDao.queryObject(id);
	}
	
	@Override
	public List<CheckStatusEntity> queryList(Map<String, Object> map){
		return checkStatusDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return checkStatusDao.queryTotal(map);
	}
	
	@Override
	public void save(CheckStatusEntity checkStatus){
		checkStatusDao.save(checkStatus);
	}
	
	@Override
	public void update(CheckStatusEntity checkStatus){
		checkStatusDao.update(checkStatus);
	}
	
	@Override
	public void delete(Integer id){
		checkStatusDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		checkStatusDao.deleteBatch(ids);
	}
	
}
