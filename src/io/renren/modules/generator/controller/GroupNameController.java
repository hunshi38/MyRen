package io.renren.modules.generator.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.generator.entity.CompetitionNameEntity;
import io.renren.modules.generator.entity.EventNameEntity;
import io.renren.modules.generator.entity.GroupNameEntity;
import io.renren.modules.generator.service.GroupNameService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:16
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController 
@RequestMapping("groupname")
public class GroupNameController {
	@Autowired
	private GroupNameService groupNameService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("groupname:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<GroupNameEntity> groupNameList = groupNameService.queryList(query);
		int total = groupNameService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(groupNameList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	@RequestMapping(value="/listall")
	public R list(){
		Map<String, Object> map = new HashMap<String,Object>();
		List<GroupNameEntity> groupNameList = groupNameService.queryList(map);
		
		return R.ok().put("list",groupNameList );
	}
	
  @RequestMapping("/user/getGroupListById")
	
	public R getEventListByCompetitonId(Integer competitionId,Integer eventId) {
	List<GroupNameEntity>groupList =  groupNameService.queryListByCompetitionIdAndEventId(competitionId, eventId);
	
		return R.ok().put("groupList", groupList);
	}
  @RequestMapping("/user/getGroupListById2")
	
	public R getEventListByCompetitonId2(Integer competitionId) {
	List<GroupNameEntity>groupList =  groupNameService.queryListByCompetitionId(competitionId);
	
		return R.ok().put("groupList", groupList);
	}
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("groupname:info")
	public R info(@PathVariable("id") Integer id){
		GroupNameEntity groupName = groupNameService.queryObject(id);
		
		return R.ok().put("groupName", groupName);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("groupname:save")
	public R save(@RequestBody GroupNameEntity groupName){
		groupNameService.save(groupName);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("groupname:update")
	public R update(@RequestBody GroupNameEntity groupName){
		groupNameService.update(groupName);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("groupname:delete")
	public R delete(@RequestBody Integer[] ids){
		groupNameService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
