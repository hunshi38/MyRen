package io.renren.modules.generator.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import io.renren.modules.generator.entity.TeamInfoEntity;
import io.renren.modules.sys.dao.BaseDao;
/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public interface TeamInfoDao extends BaseDao<TeamInfoEntity> {
	List<TeamInfoEntity> queryByCondition(Map<String, Object> map);
	List<String>getCompanyList(@Param("teamIdList")List<Integer> teamIdList);
	List<TeamInfoEntity>fuzzyQuery(@Param("name")String name,@Param("company")String company,@Param("teamIdList")List<Integer> teamIdList);
	List<TeamInfoEntity>getTeamList(@Param("teamIdList")List<Integer> teamIdList);
}
