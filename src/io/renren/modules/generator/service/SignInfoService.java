package io.renren.modules.generator.service;

import io.renren.modules.generator.entity.SignInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 远动员报名信息
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public interface SignInfoService {
	
	SignInfoEntity queryObject(Integer id);
	
	List<SignInfoEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SignInfoEntity signInfo);
	
	void update(SignInfoEntity signInfo);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	SignInfoEntity queryObjectByIdCardAndCompetitionId(Integer competitionId,String idCard);
	List<Integer>getTeamListByCompetitonId(Integer competitionId);
	List<Integer>getTeamListByGroupId(Integer groupId);
}
