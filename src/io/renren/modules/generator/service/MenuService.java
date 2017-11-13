package io.renren.modules.generator.service;

import io.renren.modules.generator.entity.MenuEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:16
 */
public interface MenuService {
	
	MenuEntity queryObject(Integer id);
	
	List<MenuEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MenuEntity menu);
	
	void update(MenuEntity menu);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
