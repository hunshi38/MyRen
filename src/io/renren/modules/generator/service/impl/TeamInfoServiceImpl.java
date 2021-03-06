package io.renren.modules.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.modules.generator.dao.TeamInfoDao;
import io.renren.modules.generator.entity.TeamInfoEntity;
import io.renren.modules.generator.service.TeamInfoService;



@Service("teamInfoService")
public class TeamInfoServiceImpl implements TeamInfoService {
	@Autowired
	private TeamInfoDao teamInfoDao;
	
	@Override
	public TeamInfoEntity queryObject(Integer id){
		return teamInfoDao.queryObject(id);
	}
	
	@Override
	public List<TeamInfoEntity> queryList(Map<String, Object> map){
		return teamInfoDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return teamInfoDao.queryTotal(map);
	}
	
	@Override
	public void save(TeamInfoEntity teamInfo){
		teamInfoDao.save(teamInfo);
	}
	
	@Override
	public void update(TeamInfoEntity teamInfo){
		teamInfoDao.update(teamInfo);
	}
	
	@Override
	public void delete(Integer id){
		teamInfoDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		teamInfoDao.deleteBatch(ids);
	}

	@Override
	public List<TeamInfoEntity> queryByCondition(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return teamInfoDao.queryByCondition(map);
	}

	@Override
	public List<String> getCompanyList(List<Integer> teamIdList) {
		// TODO Auto-generated method stub
		return teamInfoDao.getCompanyList(teamIdList);
	}

	@Override
	public List<TeamInfoEntity> fuzzyQuery(String name, String company,
			List<Integer> teamIdList) {
		// TODO Auto-generated method stub
		return teamInfoDao.fuzzyQuery(name, company, teamIdList);
	}

	@Override
	public List<TeamInfoEntity> getTeamList(List<Integer> teamIdList) {
		// TODO Auto-generated method stub
		return teamInfoDao.getTeamList(teamIdList);
	}
	
}
