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

import io.renren.modules.generator.entity.RegisterAthleteEntity;
import io.renren.modules.generator.service.RegisterAthleteService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;


/**
 * 注册运动员
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
@RestController
@RequestMapping("registerathlete")
public class RegisterAthleteController {
	@Autowired
	private RegisterAthleteService registerAthleteService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("registerathlete:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<RegisterAthleteEntity> registerAthleteList = registerAthleteService.queryList(query);
		int total = registerAthleteService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(registerAthleteList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{idCard}")
	@RequiresPermissions("registerathlete:info")
	public R info(@PathVariable("idCard") String idCard){
		RegisterAthleteEntity registerAthlete = registerAthleteService.queryObject(idCard);
		
		return R.ok().put("registerAthlete", registerAthlete);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("registerathlete:save")
	public R save(@RequestBody RegisterAthleteEntity registerAthlete){
		registerAthleteService.save(registerAthlete);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("registerathlete:update")
	public R update(@RequestBody RegisterAthleteEntity registerAthlete){
		registerAthleteService.update(registerAthlete);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("registerathlete:delete")
	public R delete(@RequestBody String[] idCards){
		registerAthleteService.deleteBatch(idCards);
		
		return R.ok();
	}
	
}
