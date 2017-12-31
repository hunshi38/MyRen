package io.renren.modules.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.modules.generator.dao.GroupNameDao;
import io.renren.modules.generator.entity.GroupNameEntity;
import io.renren.modules.generator.service.GroupNameService;



@Service("groupNameService")
public class GroupNameServiceImpl implements GroupNameService {
	@Autowired
	private GroupNameDao groupNameDao;
	
	@Override
	public GroupNameEntity queryObject(Integer id){
		return groupNameDao.queryObject(id);
	}
	
	@Override
	public List<GroupNameEntity> queryList(Map<String, Object> map){
		return groupNameDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return groupNameDao.queryTotal(map);
	}
	
	@Override
	public void save(GroupNameEntity groupName){
		groupNameDao.save(groupName);
	}
	
	@Override
	public void update(GroupNameEntity groupName){
		groupNameDao.update(groupName);
	}
	
	@Override
	public void delete(Integer id){
		groupNameDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		groupNameDao.deleteBatch(ids);
	}

	@Override
	public List<GroupNameEntity> queryListByCompetitionIdAndEventId(
			Integer competitonId, Integer eventId) {
		
		return groupNameDao.queryListByCompetitionIdAndEventId(competitonId, eventId);
	}

	@Override
	public List<GroupNameEntity> queryListByCompetitionId(Integer competitonId) {
		// TODO Auto-generated method stub
		return groupNameDao.queryListByCompetitionId(competitonId);
	}
	
	
}
