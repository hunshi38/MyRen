package io.renren.modules.generator.service;

import io.renren.modules.generator.entity.CompetitionNameEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:17
 */
public interface CompetitionNameService {
	
	CompetitionNameEntity queryObject(Integer id);
	
	List<CompetitionNameEntity> queryList(Map<String, Object> map);
	int queryTotal(Map<String, Object> map);
	
	void save(CompetitionNameEntity competitionName);
	
	void update(CompetitionNameEntity competitionName);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

	public List<CompetitionNameEntity> queryListByType(Integer cid);
}
