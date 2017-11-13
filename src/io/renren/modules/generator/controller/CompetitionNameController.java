package io.renren.modules.generator.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.generator.entity.CompetitionNameEntity;
import io.renren.modules.generator.service.CompetitionNameService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:17
 */
@RestController
@RequestMapping("competitionname")
public class CompetitionNameController {
	@Autowired
	private CompetitionNameService competitionNameService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("competitionname:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<CompetitionNameEntity> competitionNameList = competitionNameService.queryList(query);
		int total = competitionNameService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(competitionNameList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	@RequestMapping(value="/listall")
	public R list(){
		Map<String, Object> map = new HashMap<String,Object>();
		List<CompetitionNameEntity> competitionNameList = competitionNameService.queryList(map);
		
		return R.ok().put("list",competitionNameList );
	}
	@RequestMapping(value="/listType/{type}")
	public R gonggaolist(@PathVariable("type") Integer type){
		List<CompetitionNameEntity> competitionNameList = competitionNameService.queryListByType(type);
		
		return R.ok().put("list",competitionNameList );
	}
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("competitionname:info")
	public R info(@PathVariable("id") Integer id){
		CompetitionNameEntity competitionName = competitionNameService.queryObject(id);
		
		return R.ok().put("competitionName", competitionName);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("competitionname:save")
	public R save(@RequestBody CompetitionNameEntity competitionName){
		competitionNameService.save(competitionName);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("competitionname:update")
	public R update(@RequestBody CompetitionNameEntity competitionName){
		competitionNameService.update(competitionName);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("competitionname:delete")
	public R delete(@RequestBody Integer[] ids){
		competitionNameService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
