package io.renren.modules.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.modules.generator.dao.EventNameDao;
import io.renren.modules.generator.entity.EventNameEntity;
import io.renren.modules.generator.service.EventNameService;



@Service("eventNameService")
public class EventNameServiceImpl implements EventNameService {
	@Autowired
	private EventNameDao eventNameDao;
	
	@Override
	public EventNameEntity queryObject(Integer id){
		return eventNameDao.queryObject(id);
	}
	
	@Override
	public List<EventNameEntity> queryList(Map<String, Object> map){
		return eventNameDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return eventNameDao.queryTotal(map);
	}
	
	@Override
	public void save(EventNameEntity eventName){
		eventNameDao.save(eventName);
	}
	
	@Override
	public void update(EventNameEntity eventName){
		eventNameDao.update(eventName);
	}
	
	@Override
	public void delete(Integer id){
		eventNameDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		eventNameDao.deleteBatch(ids);
	}

	@Override
	public List<EventNameEntity> queryListByCompetitionId(Integer competitionId) {
		// TODO Auto-generated method stub
		return eventNameDao.queryListByCompetitionId(competitionId);
	}
	
}
