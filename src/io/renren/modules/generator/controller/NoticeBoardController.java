package io.renren.modules.generator.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.generator.entity.NoticeBoardEntity;
import io.renren.modules.generator.service.NoticeBoardService;
import io.renren.common.utils.Constant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;


/**
 * 公告管理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
@RestController
@RequestMapping("noticeboard")
public class NoticeBoardController {
	@Autowired
	private NoticeBoardService noticeBoardService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("noticeboard:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<NoticeBoardEntity> noticeBoardList = noticeBoardService.queryList(query);
		int total = noticeBoardService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(noticeBoardList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("noticeboard:info")
	public R info(@PathVariable("id") Integer id){
		NoticeBoardEntity noticeBoard = noticeBoardService.queryObject(id);
		
		return R.ok().put("noticeBoard", noticeBoard);
	}
	
	@ResponseBody
	@RequestMapping("/infoByCid/{id}")
	public R getInfoByCid(@PathVariable("id") Integer id){
		NoticeBoardEntity noticeBoard  = null;
		List<NoticeBoardEntity> noticeBoardList = noticeBoardService.queryObjectByCid(id);
		if(noticeBoardList!=null && noticeBoardList.size()>0){
		 noticeBoard = noticeBoardList.get(0);
		String content = noticeBoard.getContent();
		String imgOrFileReg = "/MyRen/ueditor/upload/";
		String replaceStr = Constant.SERVER_IP+imgOrFileReg;
		content = content.replaceAll(imgOrFileReg, replaceStr);
		noticeBoard.setContent(content);
		Integer readNum = noticeBoard.getReadNumber()+1; //阅读人数加一
		noticeBoard.setReadNumber(readNum);
		}
		
		return R.ok().put("noticeBoard", noticeBoard);
	}
	
	/**
	 * 保存
	 * @throws ParseException 
	 */
	@RequestMapping("/save")
	@RequiresPermissions("noticeboard:save")
	public R save(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		 NoticeBoardEntity noticeBoard = new NoticeBoardEntity();
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String cid = request.getParameter("cid");
		noticeBoard.setCId(Integer.parseInt(cid));
		noticeBoard.setTitle(title);
		noticeBoard.setContent(content);
		noticeBoard.setReadNumber(0);
		String imgRegx = "/MyRen/ueditor/upload/image/\\d{8}/\\d+.\\w+";
		String fileRegx = "/MyRen/ueditor/upload/file/\\d{8}/\\d+.\\w+";
		Pattern p = Pattern.compile(imgRegx);
		Matcher m = p.matcher(content);
		if(m.find()){
			noticeBoard.setPicUrl(m.group());
		}
		Pattern p1 = Pattern.compile(fileRegx);
		Matcher m1 = p1.matcher(content);
		if(m1.find()){
			noticeBoard.setContenUrl(m1.group());
		}
		Date createTime = new Date();
		noticeBoard.setCreateTime(createTime);
		noticeBoardService.save(noticeBoard);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("noticeboard:update")
	public R update(@RequestBody NoticeBoardEntity noticeBoard){
		noticeBoardService.update(noticeBoard);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("noticeboard:delete")
	public R delete(@RequestBody Integer[] ids){
		noticeBoardService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
