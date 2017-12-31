package io.renren.modules.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.modules.generator.dao.CompetitionInfoDao;
import io.renren.modules.generator.entity.CompetitionInfoEntity;
import io.renren.modules.generator.service.CompetitionInfoService;



@Service("competitionInfoService")
public class CompetitionInfoServiceImpl implements CompetitionInfoService {
	@Autowired
	private CompetitionInfoDao competitionInfoDao;
	
	@Override
	public CompetitionInfoEntity queryObject(Integer id){
		return competitionInfoDao.queryObject(id);
	}
	
	@Override
	public List<CompetitionInfoEntity> queryList(Map<String, Object> map){
		return competitionInfoDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return competitionInfoDao.queryTotal(map);
	}
	
	@Override
	public void save(CompetitionInfoEntity competitionInfo){
		competitionInfoDao.save(competitionInfo);
	}
	
	@Override
	public void update(CompetitionInfoEntity competitionInfo){
		competitionInfoDao.update(competitionInfo);
	}
	
	@Override
	public void delete(Integer id){
		competitionInfoDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		competitionInfoDao.deleteBatch(ids);
	}

	@Override
	public List<CompetitionInfoEntity> queryListByCompetitionId(Integer id) {
		// TODO Auto-generated method stub
		return competitionInfoDao.queryListByCompetitionId(id);
	}


	
}
