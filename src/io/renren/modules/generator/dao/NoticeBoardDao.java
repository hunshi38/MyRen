package io.renren.modules.generator.dao;

import java.util.List;

import io.renren.modules.generator.entity.NoticeBoardEntity;
import io.renren.modules.sys.dao.BaseDao;
/**
 * 公告管理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public interface NoticeBoardDao extends BaseDao<NoticeBoardEntity> {



	public List<NoticeBoardEntity> queryObjectByCid(Integer cid);
	
}
