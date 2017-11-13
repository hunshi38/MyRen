package io.renren.modules.generator.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.generator.entity.TeamInfoEntity;
import io.renren.modules.generator.service.TeamInfoService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
@RestController
@RequestMapping("teaminfo")
public class TeamInfoController {
	@Autowired
	private TeamInfoService teamInfoService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("teaminfo:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<TeamInfoEntity> teamInfoList = teamInfoService.queryList(query);
		int total = teamInfoService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(teamInfoList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("teaminfo:info")
	public R info(@PathVariable("id") Integer id){
		TeamInfoEntity teamInfo = teamInfoService.queryObject(id);
		
		return R.ok().put("teamInfo", teamInfo);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("teaminfo:save")
	public R save(@RequestBody TeamInfoEntity teamInfo){
		teamInfoService.save(teamInfo);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("teaminfo:update")
	public R update(@RequestBody TeamInfoEntity teamInfo){
		teamInfoService.update(teamInfo);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("teaminfo:delete")
	public R delete(@RequestBody Integer[] ids){
		teamInfoService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
