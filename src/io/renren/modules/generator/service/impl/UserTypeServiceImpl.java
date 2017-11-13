package io.renren.modules.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.modules.generator.dao.UserTypeDao;
import io.renren.modules.generator.entity.UserTypeEntity;
import io.renren.modules.generator.service.UserTypeService;



@Service("userTypeService")
public class UserTypeServiceImpl implements UserTypeService {
	@Autowired
	private UserTypeDao userTypeDao;
	
	@Override
	public UserTypeEntity queryObject(Integer id){
		return userTypeDao.queryObject(id);
	}
	
	@Override
	public List<UserTypeEntity> queryList(Map<String, Object> map){
		return userTypeDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return userTypeDao.queryTotal(map);
	}
	
	@Override
	public void save(UserTypeEntity userType){
		userTypeDao.save(userType);
	}
	
	@Override
	public void update(UserTypeEntity userType){
		userTypeDao.update(userType);
	}
	
	@Override
	public void delete(Integer id){
		userTypeDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		userTypeDao.deleteBatch(ids);
	}
	
}
