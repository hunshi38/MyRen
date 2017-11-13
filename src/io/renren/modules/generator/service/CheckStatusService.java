package io.renren.modules.generator.service;

import io.renren.modules.generator.entity.CheckStatusEntity;

import java.util.List;
import java.util.Map;

/**
 * 审查状态
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:17
 */
public interface CheckStatusService {
	
	CheckStatusEntity queryObject(Integer id);
	
	List<CheckStatusEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CheckStatusEntity checkStatus);
	
	void update(CheckStatusEntity checkStatus);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
