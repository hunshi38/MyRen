package io.renren.modules.generator.service;

import io.renren.modules.generator.entity.RegisterAthleteEntity;

import java.util.List;
import java.util.Map;

/**
 * 注册运动员
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public interface RegisterAthleteService {
	
	RegisterAthleteEntity queryObject(String idCard);
	
	List<RegisterAthleteEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(RegisterAthleteEntity registerAthlete);
	
	void update(RegisterAthleteEntity registerAthlete);
	
	void delete(String idCard);
	
	void deleteBatch(String[] idCards);
}
