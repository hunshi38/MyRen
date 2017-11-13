package io.renren.modules.generator.service;

import io.renren.modules.generator.entity.TeamInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public interface TeamInfoService {
	
	TeamInfoEntity queryObject(Integer id);
	
	List<TeamInfoEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TeamInfoEntity teamInfo);
	
	void update(TeamInfoEntity teamInfo);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
