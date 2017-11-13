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

import io.renren.modules.generator.entity.SignTypeEntity;
import io.renren.modules.generator.service.SignTypeService;
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
@RequestMapping("signtype")
public class SignTypeController {
	@Autowired
	private SignTypeService signTypeService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("signtype:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<SignTypeEntity> signTypeList = signTypeService.queryList(query);
		int total = signTypeService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(signTypeList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("signtype:info")
	public R info(@PathVariable("id") Integer id){
		SignTypeEntity signType = signTypeService.queryObject(id);
		
		return R.ok().put("signType", signType);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("signtype:save")
	public R save(@RequestBody SignTypeEntity signType){
		signTypeService.save(signType);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("signtype:update")
	public R update(@RequestBody SignTypeEntity signType){
		signTypeService.update(signType);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("signtype:delete")
	public R delete(@RequestBody Integer[] ids){
		signTypeService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
