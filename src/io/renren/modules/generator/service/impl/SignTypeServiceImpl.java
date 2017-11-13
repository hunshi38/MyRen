package io.renren.modules.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.modules.generator.dao.SignTypeDao;
import io.renren.modules.generator.entity.SignTypeEntity;
import io.renren.modules.generator.service.SignTypeService;



@Service("signTypeService")
public class SignTypeServiceImpl implements SignTypeService {
	@Autowired
	private SignTypeDao signTypeDao;
	
	@Override
	public SignTypeEntity queryObject(Integer id){
		return signTypeDao.queryObject(id);
	}
	
	@Override
	public List<SignTypeEntity> queryList(Map<String, Object> map){
		return signTypeDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return signTypeDao.queryTotal(map);
	}
	
	@Override
	public void save(SignTypeEntity signType){
		signTypeDao.save(signType);
	}
	
	@Override
	public void update(SignTypeEntity signType){
		signTypeDao.update(signType);
	}
	
	@Override
	public void delete(Integer id){
		signTypeDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		signTypeDao.deleteBatch(ids);
	}
	
}
