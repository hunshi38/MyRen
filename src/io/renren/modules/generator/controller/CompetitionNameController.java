package io.renren.modules.generator.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.renren.modules.generator.entity.CompetitionNameEntity;
import io.renren.modules.generator.entity.ResultManageEntity;
import io.renren.modules.generator.service.CompetitionNameService;
import io.renren.common.Config;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:17
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("competitionname")
public class CompetitionNameController {
	@Autowired
	private CompetitionNameService competitionNameService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("competitionname:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<CompetitionNameEntity> competitionNameList = competitionNameService.queryList(query);
		int total = competitionNameService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(competitionNameList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	@RequestMapping(value="/listall")
	public R list(){
		Map<String, Object> map = new HashMap<String,Object>();
		List<CompetitionNameEntity> competitionNameList = competitionNameService.queryList(map);
		
		return R.ok().put("list",competitionNameList );
	}
	@RequestMapping(value="/listType/{type}")
	public R gonggaolist(@PathVariable("type") Integer type){
		List<CompetitionNameEntity> competitionNameList = competitionNameService.queryListByType(type);
	
		return R.ok().put("list",competitionNameList );
	}

	/**
	 * 信息
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/download/model")
	public void downTable(HttpServletResponse response,
			HttpServletRequest request) throws UnsupportedEncodingException {

//		String path = request.getSession().getServletContext()
//				.getRealPath("/download/signModel/");
		String path = Config.FILE_UPLOAD_PATH;
	    String filename;
		String source = request.getParameter("source");
		
		if(source!=null){
			filename =request.getParameter("filename");
		}
		else {
			filename = new String(request.getParameter("filename").getBytes(
				"iso8859-1"), "UTF-8");
		}
		
		filename = path + "/" + filename;
		System.out.println("path" + path);
		InputStream fis = null;
		try {
			File file = new File(filename);
			fis = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = null;
			buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(
							filename.replaceAll(" ", "").getBytes("utf-8"),
							"utf-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream os = null;
			os = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			os.write(buffer);
			os.flush();
			os.close();
			System.out.println("over-");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/info/{id}")
	@RequiresPermissions("competitionname:info")
	public R info(@PathVariable("id") Integer id){
		CompetitionNameEntity competitionName = competitionNameService.queryObject(id);
		
		return R.ok().put("competitionName", competitionName);
	}
	
	/**
	 * 保存
	 */
	// @RequestMapping("/save")
	// @RequiresPermissions("competitionname:save")
	// public R save(@RequestBody CompetitionNameEntity competitionName){
	// competitionNameService.save(competitionName);
	//
	// return R.ok();
	// }
	
	

	@RequestMapping("/save")
	@RequiresPermissions("competitionname:save")
	public R save(
			@RequestParam("name") String name,
			@RequestParam("type") String type,
			@RequestParam("start_date") String start_date,
			@RequestParam("end_date") String end_date,
			@RequestParam("limit_account") String limit_account,
			@RequestParam("note") String note,
			@RequestParam(value = "files", required = false) MultipartFile file,
			ModelMap model, HttpServletRequest request,
			HttpServletResponse response) throws Exception, IOException {
//		String path = request.getSession().getServletContext()
//				.getRealPath("/download/signModel");
		String path = Config.FILE_UPLOAD_PATH;
		String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
		File targetFile = new File(path, fileName);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		
		file.transferTo(targetFile);
		CompetitionNameEntity competitionName = new CompetitionNameEntity();
		competitionName.setName(name);
		competitionName.setType(Integer.parseInt(type));
		competitionName.setNote(note);
		competitionName.setLimitAccount(limit_account);
		competitionName.setSignStartDate(start_date);
		competitionName.setSignEndDate(end_date);
		competitionName.setSignTablePath(fileName);
		competitionNameService.save(competitionName);
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("competitionname:update")
	public R update(@RequestBody CompetitionNameEntity competitionName){
		competitionNameService.update(competitionName);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("competitionname:delete")
	public R delete(@RequestBody Integer[] ids){
		competitionNameService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
