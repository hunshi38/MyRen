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

import io.renren.modules.generator.entity.SignInfoEntity;
import io.renren.modules.generator.service.SignInfoService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;


/**
 * 远动员报名信息
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
@RestController
@RequestMapping("signinfo")
public class SignInfoController {
	@Autowired
	private SignInfoService signInfoService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("signinfo:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<SignInfoEntity> signInfoList = signInfoService.queryList(query);
		int total = signInfoService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(signInfoList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("signinfo:info")
	public R info(@PathVariable("id") Integer id){
		SignInfoEntity signInfo = signInfoService.queryObject(id);
		
		return R.ok().put("signInfo", signInfo);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("signinfo:save")
	public R save(@RequestBody SignInfoEntity signInfo){
		signInfoService.save(signInfo);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("signinfo:update")
	public R update(@RequestBody SignInfoEntity signInfo){
		signInfoService.update(signInfo);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("signinfo:delete")
	public R delete(@RequestBody Integer[] ids){
		signInfoService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
