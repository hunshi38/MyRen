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

import io.renren.modules.generator.entity.CompetitionInfoEntity;
import io.renren.modules.generator.service.CompetitionInfoService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;


/**
 * 赛事信息
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:17
 */
@RestController
@RequestMapping("competitioninfo")
public class CompetitionInfoController {
	@Autowired
	private CompetitionInfoService competitionInfoService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("competitioninfo:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<CompetitionInfoEntity> competitionInfoList = competitionInfoService.queryList(query);
		int total = competitionInfoService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(competitionInfoList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("competitioninfo:info")
	public R info(@PathVariable("id") Integer id){
		CompetitionInfoEntity competitionInfo = competitionInfoService.queryObject(id);
		
		return R.ok().put("competitionInfo", competitionInfo);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("competitioninfo:save")
	public R save(@RequestBody CompetitionInfoEntity competitionInfo){
		competitionInfoService.save(competitionInfo);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("competitioninfo:update")
	public R update(@RequestBody CompetitionInfoEntity competitionInfo){
		competitionInfoService.update(competitionInfo);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("competitioninfo:delete")
	public R delete(@RequestBody Integer[] ids){
		competitionInfoService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
