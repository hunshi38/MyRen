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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.renren.modules.generator.entity.CompetitionNameEntity;
import io.renren.modules.generator.entity.ResultManageEntity;
import io.renren.modules.generator.service.CompetitionNameService;
import io.renren.modules.generator.service.ResultManageService;
import io.renren.modules.generator.util.ExcelUtil;
import io.renren.modules.generator.util.OfficeUtil;
import io.renren.common.Config;
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
@RequestMapping("resultmanage")
public class ResultManageController {
	@Autowired
	private ResultManageService resultManageService;
	@Autowired
	private CompetitionNameService competitionNameService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("resultmanage:list")
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);

		List<ResultManageEntity> resultManageList = resultManageService
				.queryList(query);
		int total = resultManageService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(resultManageList, total,
				query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("resultmanage:info")
	public R info(@PathVariable("id") Integer id) {
		ResultManageEntity resultManage = resultManageService.queryObject(id);

		return R.ok().put("resultManage", resultManage);
	}

	@RequestMapping("/queryGrade")
	public R queryGrade(Integer competitionId) {
		ResultManageEntity resultManage = resultManageService
				.queryObjectByCompetitionId(competitionId);
		if (resultManage != null) {
			String uuid = resultManage.getUuid();
			if (uuid != null) {
				uuid = Config.OFFICE_PREVIEW_URL + uuid;
                 String qrCode_url = uuid+Config.OFFICE_QR+"300";
                 resultManage.setUuid(uuid);
                 resultManage.setAdditionalWord(qrCode_url);
			}
			return R.ok().put("result", resultManage);
		} else {
			return R.error(-1, "还没有成绩");
		}

	}
	
	@RequestMapping("/download")
	public void downTable(HttpServletResponse response,
			HttpServletRequest request) throws UnsupportedEncodingException {

//		String path = request.getSession().getServletContext()
//				.getRealPath("/download/signModel/");
		String path = Config.FILE_RESULT_UPLOAD_PATH;
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

	// @RequestMapping("/querySubGrade")
	// public R querySubGrade( HttpServletRequest request, HttpServletResponse
	// response){
	//
	// Integer competitionId =
	// Integer.parseInt(request.getParameter("competitionId"));
	// Integer selectValue =
	// Integer.parseInt(request.getParameter("selectValue"));
	// System.out.println("cid"+competitionId+"sel"+selectValue);
	// ResultManageEntity resultManage =
	// resultManageService.queryObjectByCompetitionId(competitionId);
	// String path =
	// request.getSession().getServletContext().getRealPath("/temp/");
	// File dir = new File(path);
	// if(!dir.exists()){
	// dir.mkdirs();
	// }
	//
	//
	// String resultName = resultManage.getResultFilePath();
	// String fileType = resultName.substring(resultName.lastIndexOf(".") + 1,
	// resultName.length());
	// String fileName = UUID.randomUUID()+".xls";
	// File tempFile = new File(path+"/"+fileName);
	// if(!tempFile.exists()){
	// try {
	// tempFile.createNewFile();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// if(selectValue == -1){
	//
	// }else if (fileType.equals("xls")|| fileType.equals("xlsx")){
	// System.out.println("temp path"+tempFile.getAbsolutePath());
	// ExcelUtil excelUtil = new ExcelUtil();
	// excelUtil.getSubExcel(Config.FILE_RESULT_UPLOAD_PATH+"/"+resultName,selectValue,tempFile.getAbsolutePath());
	// System.out.println("---over   ");
	// }
	// return R.ok();
	//
	// }
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("resultmanage:save")
	public R save(@RequestBody ResultManageEntity resultManage) {
		resultManageService.save(resultManage);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("resultmanage:update")
	public R update(@RequestBody ResultManageEntity resultManage) {
		resultManageService.update(resultManage);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("resultmanage:delete")
	public R delete(@RequestBody Integer[] ids) {
		resultManageService.deleteBatch(ids);

		return R.ok();
	}

	@RequestMapping("/uploadexcel")
	public void uploadExcel(
			@RequestParam("result_id") String result_id,
			@RequestParam("competition_id") String competition_id,
			@RequestParam("additional_word") String additional_word,
			@RequestParam(value = "files", required = false) MultipartFile file,
			ModelMap model, HttpServletRequest request,
			HttpServletResponse response) throws Exception, IOException {
		// String path =
		// request.getSession().getServletContext().getRealPath("/download/GradeDownLoad");
		String path = Config.FILE_RESULT_UPLOAD_PATH;
		String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
		File targetFile = new File(path, fileName);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		System.out.println(path + "--" + fileName);
		// 本地测试
		file.transferTo(targetFile);

		ResultManageEntity resultManage = new ResultManageEntity();
		CompetitionNameEntity result_name = competitionNameService
				.queryObject(new Integer(result_id));
		resultManage.setResultName(result_name.getName());
		resultManage.setCompetitionId(Integer.parseInt(result_id));
		resultManage.setAdditionalWord(additional_word);
		resultManage.setResultFilePath(fileName);
		String filePath = Config.FILE_RESULT_UPLOAD_PATH + "/" + fileName;
		System.out.println("office web--");
		String uuid = OfficeUtil.uploadFile(filePath);
		System.out.println("office web over --");
		if (uuid != null) {
			System.out.println("uuid" + uuid);
			resultManage.setUuid(uuid);
		}
		resultManageService.save(resultManage);
	}

}
