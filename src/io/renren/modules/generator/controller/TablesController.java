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

import io.renren.modules.generator.entity.TablesEntity;
import io.renren.modules.generator.service.TablesService;
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
@RequestMapping("tables")
public class TablesController {
	@Autowired
	private TablesService tablesService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("tables:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<TablesEntity> tablesList = tablesService.queryList(query);
		int total = tablesService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(tablesList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("tables:info")
	public R info(@PathVariable("id") Integer id){
		TablesEntity tables = tablesService.queryObject(id);
		
		return R.ok().put("tables", tables);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("tables:save")
	public R save(@RequestBody TablesEntity tables){
		tablesService.save(tables);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("tables:update")
	public R update(@RequestBody TablesEntity tables){
		tablesService.update(tables);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("tables:delete")
	public R delete(@RequestBody Integer[] ids){
		tablesService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
