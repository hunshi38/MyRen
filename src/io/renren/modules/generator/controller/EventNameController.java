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

import io.renren.modules.generator.entity.EventNameEntity;
import io.renren.modules.generator.entity.GroupNameEntity;
import io.renren.modules.generator.entity.NoticeBoardEntity;
import io.renren.modules.generator.service.EventNameService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;


/**
 * 项目名称
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:16
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("eventname")
public class EventNameController {
	@Autowired
	private EventNameService eventNameService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("eventname:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<EventNameEntity> eventNameList = eventNameService.queryList(query);
		int total = eventNameService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(eventNameList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	@RequestMapping(value="/listall")
	public R list(){
		Map<String, Object> map = new HashMap<String,Object>();
		List<EventNameEntity> eventNameList = eventNameService.queryList(map);
		
		return R.ok().put("list",eventNameList);
	}
	
	@RequestMapping("/user/getEventListByCompetitionId")
	
	public R getEventListByCompetitonId(Integer competitionId) {
		
	List<EventNameEntity>eventList = eventNameService.queryListByCompetitionId(competitionId);
		return R.ok().put("eventlist", eventList);
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("eventname:info")
	public R info(@PathVariable("id") Integer id){
		EventNameEntity eventName = eventNameService.queryObject(id);
		
		return R.ok().put("eventName", eventName);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("eventname:save")
	public R save(@RequestBody EventNameEntity eventName){
		eventNameService.save(eventName);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("eventname:update")
	public R update(@RequestBody EventNameEntity eventName){
		eventNameService.update(eventName);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("eventname:delete")
	public R delete(@RequestBody Integer[] ids){
		eventNameService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
