package io.renren.modules.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.modules.generator.dao.SignInfoDao;
import io.renren.modules.generator.entity.SignInfoEntity;
import io.renren.modules.generator.service.SignInfoService;



@Service("signInfoService")
public class SignInfoServiceImpl implements SignInfoService {
	@Autowired
	private SignInfoDao signInfoDao;
	
	@Override
	public SignInfoEntity queryObject(Integer id){
		return signInfoDao.queryObject(id);
	}
	
	@Override
	public List<SignInfoEntity> queryList(Map<String, Object> map){
		return signInfoDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return signInfoDao.queryTotal(map);
	}
	
	@Override
	public void save(SignInfoEntity signInfo){
		signInfoDao.save(signInfo);
	}
	
	@Override
	public void update(SignInfoEntity signInfo){
		signInfoDao.update(signInfo);
	}
	
	@Override
	public void delete(Integer id){
		signInfoDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		signInfoDao.deleteBatch(ids);
	}

	@Override
	public SignInfoEntity queryObjectByIdCardAndCompetitionId(
			Integer competitionId, String idCard) {
		// TODO Auto-generated method stub
		return signInfoDao.queryObjectByIdCardAndCompetitionId(competitionId, idCard);
	}

	@Override
	public List<Integer> getTeamListByCompetitonId(Integer competitionId) {
		// TODO Auto-generated method stub
		return signInfoDao.getTeamListByCompetitonId(competitionId);
	}

	@Override
	public List<Integer> getTeamListByGroupId(Integer groupId) {
		// TODO Auto-generated method stub
		return signInfoDao.getTeamListByGroupId(groupId);
	}
	
}
