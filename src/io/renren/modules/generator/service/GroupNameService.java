package io.renren.modules.generator.service;

import io.renren.modules.generator.entity.GroupNameEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:16
 */
public interface GroupNameService {
	
	GroupNameEntity queryObject(Integer id);
	
	List<GroupNameEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(GroupNameEntity groupName);
	
	void update(GroupNameEntity groupName);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	List<GroupNameEntity>queryListByCompetitionIdAndEventId(Integer competitonId,Integer eventId);
	List<GroupNameEntity>queryListByCompetitionId(Integer competitonId);
}
