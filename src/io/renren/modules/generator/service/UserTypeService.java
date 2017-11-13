package io.renren.modules.generator.service;

import io.renren.modules.generator.entity.UserTypeEntity;

import java.util.List;
import java.util.Map;

/**
 * 账户类型
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public interface UserTypeService {
	
	UserTypeEntity queryObject(Integer id);
	
	List<UserTypeEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(UserTypeEntity userType);
	
	void update(UserTypeEntity userType);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
