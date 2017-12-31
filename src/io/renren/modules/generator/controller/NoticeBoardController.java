package io.renren.modules.generator.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
public class NoticeBoardController {
	@Autowired
	private NoticeBoardService noticeBoardService;

	/**
	 * 列表
	 */
	@RequestMapping("noticeboard/list")
	@RequiresPermissions("noticeboard:list")
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);

		List<NoticeBoardEntity> noticeBoardList = noticeBoardService
				.queryList(query);
		int total = noticeBoardService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(noticeBoardList, total,
				query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("noticeboard/info/{id}")
	@RequiresPermissions("noticeboard:info")
	public R info(@PathVariable("id") Integer id) {
		NoticeBoardEntity noticeBoard = noticeBoardService.queryObject(id);

		return R.ok().put("noticeBoard", noticeBoard);
	}

	@RequestMapping(value = "noticeboard/infoByCid/{id}")
	public R getInfoByCid(@PathVariable("id") Integer id) {
		NoticeBoardEntity noticeBoard = null;
		List<NoticeBoardEntity> noticeBoardList = noticeBoardService
				.queryObjectByCid(id);
		if (noticeBoardList != null && noticeBoardList.size() > 0) {
			noticeBoard = noticeBoardList.get(0);
			Integer readNum = noticeBoard.getReadNumber() + 1; // 阅读人数加一
			noticeBoard.setReadNumber(readNum);
			noticeBoardService.update(noticeBoard);//保存到数据库
			String content = noticeBoard.getContent();
			String fileReg = "/MyRen/ueditor/upload/file";
			String replaceStr = Constant.SERVER_IP + fileReg;
			content = content.replaceAll(fileReg, replaceStr);
			String contentUrl = noticeBoard.getContenUrl();
			noticeBoard.setContenUrl(Constant.SERVER_IP+contentUrl);
			noticeBoard.setContent(content);
			
		}
		return R.ok().put("noticeBoard", noticeBoard);
	}

	/**
	 * 保存
	 * 
	 * @throws ParseException
	 */
	@RequestMapping("noticeboard/save")
	@RequiresPermissions("noticeboard:save")
	public R save(HttpServletRequest request, HttpServletResponse response)
			throws ParseException {
		NoticeBoardEntity noticeBoard = new NoticeBoardEntity();
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String cid = request.getParameter("cid");
		noticeBoard.setCId(Integer.parseInt(cid));
		noticeBoard.setTitle(title);
		noticeBoard.setContent(content);
		System.out.println("add content"+content);
		noticeBoard.setReadNumber(0);
		String imgRegx = "/MyRen/ueditor/upload/image/\\d{8}/\\d+.\\w+";
		String fileRegx = "/MyRen/ueditor/upload/file/\\d{8}/\\d+.\\w+";
		Pattern p = Pattern.compile(imgRegx);
		Matcher m = p.matcher(content);
		if (m.find()) {
			noticeBoard.setPicUrl(m.group());
		}
		Pattern p1 = Pattern.compile(fileRegx);
		Matcher m1 = p1.matcher(content);
		if (m1.find()) {
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
	@RequestMapping("noticeboard/update")
	@RequiresPermissions("noticeboard:update")
	public R update(HttpServletRequest request, HttpServletResponse response)
			throws ParseException {
		String id = request.getParameter("id");
		System.out.println("id"+id);
		NoticeBoardEntity noticeBoard = noticeBoardService.queryObject(Integer
				.parseInt(id));
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String cid = request.getParameter("cid");
		if (noticeBoard != null) {
			noticeBoard.setCId(Integer.parseInt(cid));
			noticeBoard.setTitle(title);
			noticeBoard.setContent(content);
		System.out.println("update"+content);
			noticeBoard.setReadNumber(0);
			String imgRegx = "/MyRen/ueditor/upload/image/\\d{8}/\\d+.\\w+";
			String fileRegx = "/MyRen/ueditor/upload/file/\\d{8}/\\d+.\\w+";
			Pattern p = Pattern.compile(imgRegx);
			Matcher m = p.matcher(content);
			if (m.find()) {
				noticeBoard.setPicUrl(m.group());
			}
			Pattern p1 = Pattern.compile(fileRegx);
			Matcher m1 = p1.matcher(content);
			if (m1.find()) {
				noticeBoard.setContenUrl(m1.group());
			}
			Date createTime = new Date();
			noticeBoard.setCreateTime(createTime);

			noticeBoardService.update(noticeBoard);
		}

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("noticeboard/delete")
	@RequiresPermissions("noticeboard:delete")
	public R delete(@RequestBody Integer[] ids) {
		noticeBoardService.deleteBatch(ids);

		return R.ok();
	}

}
