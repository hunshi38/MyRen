package io.renren.modules.generator.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.renren.modules.generator.entity.SignTableEntity;
import io.renren.modules.sys.dao.BaseDao;
/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public interface SignTableDao extends BaseDao<SignTableEntity> {
	SignTableEntity queryObjectByfrontUserId2(@Param("userId")Integer userId,@Param("competitionId")Integer competitionId);
	SignTableEntity queryObjectByFilePath(String path);
	List<Integer>getSignYears();
}
