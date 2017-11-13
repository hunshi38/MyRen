package io.renren.modules.generator.service;

import io.renren.modules.generator.entity.CompetitionInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 赛事信息
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:17
 */
public interface CompetitionInfoService {
	
	CompetitionInfoEntity queryObject(Integer id);
	
	List<CompetitionInfoEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CompetitionInfoEntity competitionInfo);
	
	void update(CompetitionInfoEntity competitionInfo);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
