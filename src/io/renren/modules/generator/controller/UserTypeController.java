package io.renren.modules.generator.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.generator.entity.UserTypeEntity;
import io.renren.modules.generator.service.UserTypeService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;


/**
 * 账户类型
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
@RestController
@RequestMapping("usertype")
public class UserTypeController {
	@Autowired
	private UserTypeService userTypeService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	
	public R list( ){
		//查询列表数据
      
        Map<String, Object> map = new HashMap<String,Object>();
		List<UserTypeEntity> userTypeList = userTypeService.queryList(map);
		return R.ok().put("list", userTypeList);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("usertype:info")
	public R info(@PathVariable("id") Integer id){
		UserTypeEntity userType = userTypeService.queryObject(id);
		
		return R.ok().put("userType", userType);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("usertype:save")
	public R save(@RequestBody UserTypeEntity userType){
		userTypeService.save(userType);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("usertype:update")
	public R update(@RequestBody UserTypeEntity userType){
		userTypeService.update(userType);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("usertype:delete")
	public R delete(@RequestBody Integer[] ids){
		userTypeService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
