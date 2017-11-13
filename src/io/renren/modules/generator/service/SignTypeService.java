package io.renren.modules.generator.service;

import io.renren.modules.generator.entity.SignTypeEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public interface SignTypeService {
	
	SignTypeEntity queryObject(Integer id);
	
	List<SignTypeEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SignTypeEntity signType);
	
	void update(SignTypeEntity signType);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
