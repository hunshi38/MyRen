package io.renren.modules.generator.dao;

import java.util.List;

import io.renren.modules.generator.entity.CompetitionNameEntity;
import io.renren.modules.sys.dao.BaseDao;
/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:17
 */
public interface CompetitionNameDao extends BaseDao<CompetitionNameEntity> {
	public List<CompetitionNameEntity> queryListByType(Integer cid);
}
