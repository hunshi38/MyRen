package io.renren.modules.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.modules.generator.dao.NoticeBoardDao;
import io.renren.modules.generator.entity.NoticeBoardEntity;
import io.renren.modules.generator.service.NoticeBoardService;



@Service("noticeBoardService")
public class NoticeBoardServiceImpl implements NoticeBoardService {
	@Autowired
	private NoticeBoardDao noticeBoardDao;
	
	@Override
	public NoticeBoardEntity queryObject(Integer id){
		return noticeBoardDao.queryObject(id);
	}
	
	@Override
	public List<NoticeBoardEntity> queryList(Map<String, Object> map){
		return noticeBoardDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return noticeBoardDao.queryTotal(map);
	}
	
	@Override
	public void save(NoticeBoardEntity noticeBoard){
		noticeBoardDao.save(noticeBoard);
	}
	
	@Override
	public void update(NoticeBoardEntity noticeBoard){
		noticeBoardDao.update(noticeBoard);
	}
	
	@Override
	public void delete(Integer id){
		noticeBoardDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		noticeBoardDao.deleteBatch(ids);
	}

	@Override
	public List<NoticeBoardEntity> queryObjectByCid(Integer cid) {
		
		
		return noticeBoardDao.queryObjectByCid(cid);
	}
	
}
