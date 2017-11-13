package io.renren.modules.generator.service;

import io.renren.modules.generator.entity.TablesEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public interface TablesService {
	
	TablesEntity queryObject(Integer id);
	
	List<TablesEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TablesEntity tables);
	
	void update(TablesEntity tables);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
