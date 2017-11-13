package io.renren.modules.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.modules.generator.dao.SignTableDao;
import io.renren.modules.generator.entity.SignTableEntity;
import io.renren.modules.generator.service.SignTableService;



@Service("signTableService")
public class SignTableServiceImpl implements SignTableService {
	@Autowired
	private SignTableDao signTableDao;
	
	@Override
	public SignTableEntity queryObject(Integer id){
		return signTableDao.queryObject(id);
	}
	
	@Override
	public List<SignTableEntity> queryList(Map<String, Object> map){
		return signTableDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return signTableDao.queryTotal(map);
	}
	
	@Override
	public void save(SignTableEntity signTable){
		signTableDao.save(signTable);
	}
	
	@Override
	public void update(SignTableEntity signTable){
		signTableDao.update(signTable);
	}
	
	@Override
	public void delete(Integer id){
		signTableDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		signTableDao.deleteBatch(ids);
	}
	
}
