package io.renren.modules.generator.service;

import io.renren.modules.generator.entity.SignTableEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public interface SignTableService {
	
	SignTableEntity queryObject(Integer id);
	
	List<SignTableEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SignTableEntity signTable);
	
	void update(SignTableEntity signTable);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
