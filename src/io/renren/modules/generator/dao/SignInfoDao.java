package io.renren.modules.generator.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.renren.modules.generator.entity.SignInfoEntity;
import io.renren.modules.sys.dao.BaseDao;
/**
 * 远动员报名信息
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public interface SignInfoDao extends BaseDao<SignInfoEntity> {

	void save(SignInfoEntity signInfo);
	SignInfoEntity queryObjectByIdCardAndCompetitionId(@Param("competitionId")Integer competitionId,@Param("idCard")String idCard);
	List<Integer>getTeamListByCompetitonId(Integer competitionId);
	List<Integer>getTeamListByGroupId(Integer groupId);
	
}
