package io.renren.modules.generator.service;

import io.renren.modules.generator.entity.EventNameEntity;

import java.util.List;
import java.util.Map;

/**
 * 项目名称
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:16
 */
public interface EventNameService {
	
	EventNameEntity queryObject(Integer id);
	
	List<EventNameEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(EventNameEntity eventName);
	
	void update(EventNameEntity eventName);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	List<EventNameEntity>queryListByCompetitionId(Integer competitionId);

	
}
