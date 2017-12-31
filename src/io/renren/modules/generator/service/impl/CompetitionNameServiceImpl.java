package io.renren.modules.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.modules.generator.dao.CompetitionNameDao;
import io.renren.modules.generator.entity.CompetitionNameEntity;
import io.renren.modules.generator.service.CompetitionNameService;



@Service("competitionNameService")
public class CompetitionNameServiceImpl implements CompetitionNameService {
	@Autowired
	private CompetitionNameDao competitionNameDao;
	
	@Override
	public CompetitionNameEntity queryObject(Integer id){
		System.out.println("-----------");
		return competitionNameDao.queryObject(id);
	}
	
	@Override
	public List<CompetitionNameEntity> queryList(Map<String, Object> map){
		return competitionNameDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return competitionNameDao.queryTotal(map);
	}
	
	@Override
	public void save(CompetitionNameEntity competitionName){
		competitionNameDao.save(competitionName);
	}
	
	@Override
	public void update(CompetitionNameEntity competitionName){
		competitionNameDao.update(competitionName);
	}
	
	@Override
	public void delete(Integer id){
		competitionNameDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		competitionNameDao.deleteBatch(ids);
	}

	@Override
	public List<CompetitionNameEntity> queryListByType(Integer type){
		return competitionNameDao.queryListByType(type);
	}
	
}
