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

import io.renren.modules.generator.entity.SignTableEntity;
import io.renren.modules.generator.service.SignTableService;
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
@RequestMapping("signtable")
public class SignTableController {
	@Autowired
	private SignTableService signTableService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("signtable:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<SignTableEntity> signTableList = signTableService.queryList(query);
		int total = signTableService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(signTableList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("signtable:info")
	public R info(@PathVariable("id") Integer id){
		SignTableEntity signTable = signTableService.queryObject(id);
		
		return R.ok().put("signTable", signTable);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("signtable:save")
	public R save(@RequestBody SignTableEntity signTable){
		signTableService.save(signTable);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("signtable:update")
	public R update(@RequestBody SignTableEntity signTable){
		signTableService.update(signTable);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("signtable:delete")
	public R delete(@RequestBody Integer[] ids){
		signTableService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
