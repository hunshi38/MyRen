package io.renren.modules.generator.service;

import io.renren.modules.generator.entity.NoticeBoardEntity;

import java.util.List;
import java.util.Map;

/**
 * 公告管理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public interface NoticeBoardService {
	
	NoticeBoardEntity queryObject(Integer id);
	
	List<NoticeBoardEntity> queryObjectByCid(Integer cid);
	
	List<NoticeBoardEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(NoticeBoardEntity noticeBoard);
	
	void update(NoticeBoardEntity noticeBoard);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
