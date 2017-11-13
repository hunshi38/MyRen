package io.renren.modules.generator.service;

import io.renren.modules.generator.entity.ResultManageEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public interface ResultManageService {
	
	ResultManageEntity queryObject(Integer id);
	
	List<ResultManageEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ResultManageEntity resultManage);
	
	void update(ResultManageEntity resultManage);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
