package io.renren.modules.generator.service;

import io.renren.modules.generator.entity.AthleteEntity;

import java.util.List;
import java.util.Map;

/**
 * 运动员
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:17
 */
public interface AthleteService {
	
	AthleteEntity queryObject(Integer id);
	
	List<AthleteEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AthleteEntity athlete);
	
	void update(AthleteEntity athlete);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
