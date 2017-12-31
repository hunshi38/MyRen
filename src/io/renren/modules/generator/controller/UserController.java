package io.renren.modules.generator.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.generator.entity.SignTableEntity;
import io.renren.modules.generator.entity.UserEntity;
import io.renren.modules.generator.service.SignInfoService;
import io.renren.modules.generator.service.SignTableService;
import io.renren.modules.generator.service.UserService;
import io.renren.modules.sys.shiro.ShiroUtils;
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
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private SignTableService signTableService;
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("user:list")
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);

		List<UserEntity> userList = userService.queryList(query);
		int total = userService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(),
				query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("user:info")
	public R info(@PathVariable("id") Integer id) {
		UserEntity user = userService.queryObject(id);

		return R.ok().put("user", user);
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public R login(String userName,String password) {

		if (userName.equals("") || password.equals("")) {
			return R.error(-1, "用户名或者密码为空!");
		} else {
			UserEntity user = new UserEntity();
			user.setUserName(userName);
			user.setMd5(password);
			UserEntity account = userService.auth(user);
			if (account != null) {
				
				return R.ok().put("user", account);
			} else {
				return R.error(-2, "用户或密码错误!");
			}
		}

	}
	@RequestMapping("/checkstatus")
	public R checkStatus( Integer competitionId,Integer userId){
		SignTableEntity signTable = signTableService.queryObjectByfrontUserId2(userId, competitionId);
		if(signTable!=null){
			int checkStatus = signTable.getCheckStatus();
			switch(checkStatus){
			case 0:{ return R.error(checkStatus, "未审核");}
			case 1:{return R.error(checkStatus, "审核通过");}
			case 2:{return R.error(checkStatus,"审核不通过");}
			}
		}
		else{
			return R.error(-1,"没有报名成功");
		}
		
		return R.ok();
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("user:save")
	public R save(@RequestBody UserEntity user) {
		userService.save(user);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("user:update")
	public R update(@RequestBody UserEntity user) {
		System.out.println("md5"+user.getMd5());
		userService.update(user);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("user:delete")
	public R delete(@RequestBody Integer[] ids) {
		userService.deleteBatch(ids);

		return R.ok();
	}

}
