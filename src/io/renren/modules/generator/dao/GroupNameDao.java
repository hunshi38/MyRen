package io.renren.modules.generator.dao;

import java.util.List;

import io.renren.modules.generator.entity.GroupNameEntity;
import io.renren.modules.sys.dao.BaseDao;
/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:16
 */
public interface GroupNameDao extends BaseDao<GroupNameEntity> {
	List<GroupNameEntity>queryListByCompetitionIdAndEventId(Integer competitonId,Integer eventId);
	List<GroupNameEntity>queryListByCompetitionId(Integer competitonId);

}
