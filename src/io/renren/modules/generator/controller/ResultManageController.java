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

import io.renren.modules.generator.entity.ResultManageEntity;
import io.renren.modules.generator.service.ResultManageService;
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
@RequestMapping("resultmanage")
public class ResultManageController {
	@Autowired
	private ResultManageService resultManageService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("resultmanage:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<ResultManageEntity> resultManageList = resultManageService.queryList(query);
		int total = resultManageService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(resultManageList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("resultmanage:info")
	public R info(@PathVariable("id") Integer id){
		ResultManageEntity resultManage = resultManageService.queryObject(id);
		
		return R.ok().put("resultManage", resultManage);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("resultmanage:save")
	public R save(@RequestBody ResultManageEntity resultManage){
		resultManageService.save(resultManage);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("resultmanage:update")
	public R update(@RequestBody ResultManageEntity resultManage){
		resultManageService.update(resultManage);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("resultmanage:delete")
	public R delete(@RequestBody Integer[] ids){
		resultManageService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
