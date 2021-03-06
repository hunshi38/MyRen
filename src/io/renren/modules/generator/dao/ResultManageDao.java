package io.renren.modules.generator.dao;

import io.renren.modules.generator.entity.ResultManageEntity;
import io.renren.modules.sys.dao.BaseDao;
/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public interface ResultManageDao extends BaseDao<ResultManageEntity> {
	ResultManageEntity queryObjectByCompetitionId(Integer competitionId);
}
