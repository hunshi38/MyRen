package io.renren.modules.generator.dao;

import io.renren.modules.generator.entity.UserEntity;
import io.renren.modules.sys.dao.BaseDao;
/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public interface UserDao extends BaseDao<UserEntity> {

	UserEntity auth(UserEntity user);
}
