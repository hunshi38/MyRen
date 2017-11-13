package io.renren.modules.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.modules.generator.dao.RegisterAthleteDao;
import io.renren.modules.generator.entity.RegisterAthleteEntity;
import io.renren.modules.generator.service.RegisterAthleteService;



@Service("registerAthleteService")
public class RegisterAthleteServiceImpl implements RegisterAthleteService {
	@Autowired
	private RegisterAthleteDao registerAthleteDao;
	
	@Override
	public RegisterAthleteEntity queryObject(String idCard){
		return registerAthleteDao.queryObject(idCard);
	}
	
	@Override
	public List<RegisterAthleteEntity> queryList(Map<String, Object> map){
		return registerAthleteDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return registerAthleteDao.queryTotal(map);
	}
	
	@Override
	public void save(RegisterAthleteEntity registerAthlete){
		registerAthleteDao.save(registerAthlete);
	}
	
	@Override
	public void update(RegisterAthleteEntity registerAthlete){
		registerAthleteDao.update(registerAthlete);
	}
	
	@Override
	public void delete(String idCard){
		registerAthleteDao.delete(idCard);
	}
	
	@Override
	public void deleteBatch(String[] idCards){
		registerAthleteDao.deleteBatch(idCards);
	}
	
}
