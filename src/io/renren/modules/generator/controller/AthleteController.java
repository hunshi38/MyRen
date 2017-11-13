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

import io.renren.modules.generator.entity.AthleteEntity;
import io.renren.modules.generator.service.AthleteService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;


/**
 * 运动员
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:17
 */
@RestController
@RequestMapping("athlete")
public class AthleteController {
	@Autowired
	private AthleteService athleteService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("athlete:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<AthleteEntity> athleteList = athleteService.queryList(query);
		int total = athleteService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(athleteList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("athlete:info")
	public R info(@PathVariable("id") Integer id){
		AthleteEntity athlete = athleteService.queryObject(id);
		
		return R.ok().put("athlete", athlete);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("athlete:save")
	public R save(@RequestBody AthleteEntity athlete){
		athleteService.save(athlete);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("athlete:update")
	public R update(@RequestBody AthleteEntity athlete){
		athleteService.update(athlete);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("athlete:delete")
	public R delete(@RequestBody Integer[] ids){
		athleteService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
