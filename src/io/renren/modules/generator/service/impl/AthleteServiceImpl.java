package io.renren.modules.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.modules.generator.dao.AthleteDao;
import io.renren.modules.generator.entity.AthleteEntity;
import io.renren.modules.generator.service.AthleteService;



@Service("athleteService")
public class AthleteServiceImpl implements AthleteService {
	@Autowired
	private AthleteDao athleteDao;
	
	@Override
	public AthleteEntity queryObject(Integer id){
		return athleteDao.queryObject(id);
	}
	
	@Override
	public List<AthleteEntity> queryList(Map<String, Object> map){
		return athleteDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return athleteDao.queryTotal(map);
	}
	
	@Override
	public void save(AthleteEntity athlete){
		athleteDao.save(athlete);
	}
	
	@Override
	public void update(AthleteEntity athlete){
		athleteDao.update(athlete);
	}
	
	@Override
	public void delete(Integer id){
		athleteDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		athleteDao.deleteBatch(ids);
	}

	@Override
	public AthleteEntity queryObjectByCardId(String idCard) {
		
		return athleteDao.queryObjectByCardId(idCard);
	}
	
	
}
