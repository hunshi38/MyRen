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

import io.renren.modules.generator.entity.CheckStatusEntity;
import io.renren.modules.generator.service.CheckStatusService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;


/**
 * 审查状态
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:17
 */
@RestController
@RequestMapping("checkstatus")
public class CheckStatusController {
	@Autowired
	private CheckStatusService checkStatusService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("checkstatus:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<CheckStatusEntity> checkStatusList = checkStatusService.queryList(query);
		int total = checkStatusService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(checkStatusList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("checkstatus:info")
	public R info(@PathVariable("id") Integer id){
		CheckStatusEntity checkStatus = checkStatusService.queryObject(id);
		
		return R.ok().put("checkStatus", checkStatus);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("checkstatus:save")
	public R save(@RequestBody CheckStatusEntity checkStatus){
		checkStatusService.save(checkStatus);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("checkstatus:update")
	public R update(@RequestBody CheckStatusEntity checkStatus){
		checkStatusService.update(checkStatus);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("checkstatus:delete")
	public R delete(@RequestBody Integer[] ids){
		checkStatusService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
