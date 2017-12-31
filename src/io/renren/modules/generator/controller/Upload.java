package io.renren.modules.generator.controller;

import io.renren.modules.generator.entity.AthleteEntity;
import io.renren.modules.generator.entity.CompetitionInfoEntity;
import io.renren.modules.generator.entity.CompetitionNameEntity;
import io.renren.modules.generator.entity.RegisterAthleteEntity;
import io.renren.modules.generator.entity.SignInfoEntity;
import io.renren.modules.generator.entity.SignTableEntity;
import io.renren.modules.generator.entity.TablesEntity;
import io.renren.modules.generator.entity.TeamInfoEntity;
import io.renren.modules.generator.entity.UserEntity;
import io.renren.modules.generator.service.AthleteService;
import io.renren.modules.generator.service.CompetitionInfoService;
import io.renren.modules.generator.service.CompetitionNameService;
import io.renren.modules.generator.service.RegisterAthleteService;
import io.renren.modules.generator.service.SignInfoService;
import io.renren.modules.generator.service.SignTableService;
import io.renren.modules.generator.service.TablesService;
import io.renren.modules.generator.service.TeamInfoService;
import io.renren.modules.generator.service.UserService;
import io.renren.modules.generator.util.ExcelUtil;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;



@Controller
@RequestMapping("/user")
public class Upload {
	@Autowired
	private UserService userService;
	@Autowired
	private SignTableService signTableService;
	@Autowired
	private CompetitionInfoService competitionInfoService;
	@Autowired
	private CompetitionNameService competitionNameService;
	@Autowired
	private TeamInfoService teamInfoService;
	@Autowired
	private RegisterAthleteService registerAthleteService;
	@Autowired
	private AthleteService athleteService;
	@Autowired
	private SignInfoService signInfoService;
	@RequestMapping("/receiveFile")
	public void receiveExcel(@RequestParam(value = "enterFile") MultipartFile multipartFile, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		// 上传
		String competition_id1 = request.getParameter("competition_id");
		String front_user_id = request.getParameter("front_user_id");
		String competition_type ="0";
		System.out.println("competition_id"+competition_id1+" frontUser"+front_user_id);
		
		UserEntity frontUser = userService.queryObject(Integer.parseInt(front_user_id));
		String company = frontUser.getNote();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		String today = formatter.format(new Date());
		try {
			date = formatter.parse(today);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String path = request.getSession().getServletContext().getRealPath("upload");
		String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
		System.out.println(competition_id1 + "  " + front_user_id + "   " + competition_type);
		System.out.println(fileName);
		System.out.println(path);
		File targetFile = new File(path, fileName);
		if(!targetFile.exists()){
            targetFile.mkdirs();
        }

		// 保存
		try {
			multipartFile.transferTo(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("fileUrl", request.getContextPath() + "/upload/" + fileName);

		SignTableEntity signTable = new SignTableEntity();
		signTable.setCheckStatus(0);
		signTable.setCompetitionId(Integer.parseInt(competition_id1));
		signTable.setFileName(multipartFile.getOriginalFilename());
		signTable.setFilePath(fileName);
		signTable.setFrontUserId(Integer.parseInt(front_user_id));
		signTable.setNote("空");
		signTable.setType(Integer.parseInt(competition_type));
		signTable.setTime(Calendar.getInstance().get(Calendar.YEAR));
		int UserId = Integer.parseInt(front_user_id);
		System.out.println("-----userId"+UserId+" ------comid"+competition_id1);
		
		SignTableEntity searchSt = signTableService.queryObjectByfrontUserId2(UserId,Integer.parseInt(competition_id1));
		if (searchSt==null) {
			signTableService.save(signTable);
		} else if (searchSt.getCheckStatus() != 1) {
			searchSt.setCheckStatus(0);
			searchSt.setCompetitionId(Integer.parseInt(competition_id1));
			searchSt.setFileName(multipartFile.getOriginalFilename());
			searchSt.setFilePath(fileName);
			searchSt.setFrontUserId(Integer.parseInt(front_user_id));
			searchSt.setNote("空");
			searchSt.setType(Integer.parseInt(competition_type));
			searchSt.setTime(Calendar.getInstance().get(Calendar.YEAR));
			signTableService.update(searchSt);
			
		} else {
			response.getWriter().write("<script>alert('审核已通过，上传无效')</script>");
			return;
		}

		Object[] s = {  };
		List<CompetitionInfoEntity> competitionInfoList = competitionInfoService.queryListByCompetitionId(Integer.parseInt(competition_id1));		
		String competitionName = competitionInfoList.get(0).getCompetitionNameEntity().getName();
		System.out.println(competitionName);
	    if(competitionName.indexOf("特色项目学校排球") != -1){
	    	receiveExcelForVolleyball(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
	    }else if (competitionName.indexOf("特色项目学校篮球") != -1) {
			receiveExcelForTeseBasketBall(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
	    } else if (competitionName.indexOf("羽毛球") != -1||competitionName.indexOf("特色项目学校短式网球") != -1) {
			receiveExcelForCurrency(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
	    }else if (competitionName.indexOf("少年儿童乒乓球") != -1) {
			receiveExcelForPingpong(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
	    }else if (competitionName.indexOf("特色项目学校游泳") != -1) {
			receiveExcelForTeseSwimming(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
	    }else if (competitionName.indexOf("篮球") != -1 || competitionName.indexOf("足球") != -1
				|| competitionName.indexOf("排球") != -1) {
			receiveExcelForBasketBall(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
		} else if (competitionName.indexOf("体操") != -1) {
			receiveExcelForGymnastics(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
		} else if (competitionName.indexOf("武术") != -1) {
			receiveExcelForKungfu(request, response, competition_id1, front_user_id, competition_type, date, fileName,
					company);
		} else if (competitionName.indexOf("游泳") != -1) {
			receiveExcelForSwim(request, response, competition_id1, front_user_id, competition_type, date, fileName,
					company);
		} else if (competitionName.indexOf("跆拳道") != -1) {
			receiveExcelForTaekwondo(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
		} else if (competitionName.indexOf("皮划艇") != -1||competitionName.indexOf("赛艇") != -1) {
			receiveExcelForPihuating(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
		} else if (competitionName.indexOf("特色项目学校网球") != -1||competitionName.indexOf("特色项目学校短式网球") != -1) {
			receiveExcelForCurrency(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
		} else if (competitionName.indexOf("网球") != -1) {
			receiveExcelForWangqiu(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
		}else if (competitionName.indexOf("田径") != -1) {
			receiveExcelForTianjing(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
		} else if (competitionName.indexOf("举重") != -1) {
			receiveExcelForWeightlifting(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
		} else if (competitionName.indexOf("柔道") != -1) {
			receiveExcelForJudo(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
		} else if (competitionName.indexOf("摔跤") != -1) {
			receiveExcelForWrestling(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
		}else if (competitionName.indexOf("特色项目学校羽毛球") != -1) {
			receiveExcelForTeseBadminton(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
		}else if (competitionName.indexOf("特色项目学校乒乓球") != -1) {
			receiveExcelForPingpongball(request, response, competition_id1, front_user_id, competition_type, date,
					fileName, company);
		}
	}

	// 解析三大球
	public void receiveExcelForBasketBall(HttpServletRequest request, HttpServletResponse response,
			String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
			String company) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");

		try {
			String filePath = null;
			String filename = fileName;
			Date upload = today;
			int competition_id = Integer.parseInt(competition_id1);
			String frontUserId = request.getParameter("frontUserif");
			filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
			System.out.println(filePath);
			List<String> log = new ArrayList<>();
			ExcelUtil excelutil = new ExcelUtil();

			// 遍历excel的两张工作表
			int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
			List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
			for (int number = 0; number < iSheetQuanitity; number++) {
				// 得到运动员数据
				List<String[]> list = excelutil.readByPoi(filePath, number, 6, 5, 9, false);
				System.out.println(list.size());
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < list.get(i).length; j++) {
						System.out.println(list.get(i)[j]);
					}
				}
				// 得到所以合并单元格的数据
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				// 判断报名日期是否正确
				if (isApplyTime(response, competition_id, formatter1, upload)) {
					return;
				}
				// 判断人数是否符合
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				if (isAtheteNum(response, competition_id, filePath, number, list, strCurrentSheetName)) {
					return;
				}
				for (int i = 0; i < list.size(); i++) {
					//判断运动员年龄是否符合
					log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
					//判断性别是否符合
					log = isSex(list, i, log, strCurrentSheetName);
					//判断是否是注册运动员
					log = isAthlete(list, i, log);
				}
				//判断同一张报名表中参赛人员是否重复
				String[] s = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					log = Check(list, i, log, s);
				}
			}

			for (int number = 0; number < iSheetQuanitity; number++) {
				List<String[]> list = excelutil.readByPoi(filePath, number, 6, 5, 9, false);
				System.out.println(list.size());
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < list.get(i).length; j++) {
						System.out.println(list.get(i)[j]);
					}
				}
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				String phone = excelutil.onecell(filePath, number, 3, 7, false);
				System.out.println(phone);

				String capain = null;
				if (list1.get(7).trim().equals("") ? false : true) {
					capain = list1.get(7).trim();
					if (list1.get(8).trim().equals("") ? false : true) {
						capain = capain + ";" + list1.get(8).trim();
						if (list1.get(9).trim().equals("") ? false : true) {
							capain = capain + ";" + list1.get(9).trim();
						}
					}
				}

				if (log.isEmpty()) {
					System.out.println("log为空");
					for (int i = 0; i < list.size(); i++) {
						addAthlete(i, list, list1, formatter2, company, phone);
					}

					String str = "";
					for (int i = 0; i < list.size(); i++) {

						int jerseyNumber = list.get(i)[6].trim().equals("") || list.get(i)[6].trim().equals("略")
								|| list.get(i)[6] == null ? i + 4 : Integer.parseInt(list.get(i)[6]);

						str = str + list.get(i)[2] + "-" + list.get(i)[3] + ";";
					}

					// 添加运动员队伍信息到数据库
					CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
					String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
					addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
							competition_type, filename,competitionInfo);

				}
			}
			SignTableEntity st = signTableService.queryObjectByFilePath(filename);
			if (log.isEmpty()) {
				st.setCheckStatus(1);
				signTableService.update(st);
				response.getWriter().write("<script>alert('审核通过')</script>");
				return;
			} else {
				st.setCheckStatus(0);
				signTableService.update(st);
					String s = "";
				for (int i = 0; i < log.size(); i++) {
					s = s + log.get(i) + ";    ";
				}
				response.getWriter().write("<script>alert('" + s + "')</script>");
				return;
			}
		} catch (Exception e) {
			response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
			e.printStackTrace();
		}
	}

	// 解析特色篮球
	public void receiveExcelForTeseBasketBall(HttpServletRequest request, HttpServletResponse response,
			String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
			String company) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");

		try {
			String filePath = null;
			String filename = fileName;
			Date upload = today;
			int competition_id = Integer.parseInt(competition_id1);
			String frontUserId = request.getParameter("frontUserif");
			filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
			System.out.println(filePath);
			List<String> log = new ArrayList<>();
			ExcelUtil excelutil = new ExcelUtil();

			// 遍历excel的两张工作表
			int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
			List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
			for (int number = 0; number < iSheetQuanitity; number++) {
				// 得到运动员数据
				List<String[]> list = excelutil.readByPoi(filePath, number, 6, 5, 9, false);
				System.out.println(list.size());
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < list.get(i).length; j++) {
						System.out.println(list.get(i)[j]);
					}
				}
				// 得到所以合并单元格的数据
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				// 判断报名日期是否正确
				if (isApplyTime(response, competition_id, formatter1, upload)) {
					return;
				}
				// 判断人数是否符合
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				if (isAtheteNum(response, competition_id, filePath, number, list, strCurrentSheetName)) {
					return;
				}
				
				for (int i = 0; i < list.size(); i++) {
					//判断运动员年龄是否符合
					log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
					//判断性别是否符合
					log = isSex(list, i, log, strCurrentSheetName);
				}
				//判断同一张报名表中参赛人员是否重复
				String[] s = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					log = Check(list, i, log, s);
				}
			}

			for (int number = 0; number < iSheetQuanitity; number++) {
				List<String[]> list = excelutil.readByPoi(filePath, number, 6, 5, 9, false);
				System.out.println(list.size());
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < list.get(i).length; j++) {
						System.out.println(list.get(i)[j]);
					}
				}
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				String phone = excelutil.onecell(filePath, number, 3, 7, false);
				System.out.println(phone);

				String capain = null;
				if (list1.get(7).trim().equals("") ? false : true) {
					capain = list1.get(7).trim();
					if (list1.get(8).trim().equals("") ? false : true) {
						capain = capain + ";" + list1.get(8).trim();
						if (list1.get(9).trim().equals("") ? false : true) {
							capain = capain + ";" + list1.get(9).trim();
						}
					}
				}

				if (log.isEmpty()) {
					System.out.println("log为空");
					for (int i = 0; i < list.size(); i++) {
						addAthlete(i, list, list1, formatter2, company, phone);
					}

					String str = "";
					for (int i = 0; i < list.size(); i++) {

						int jerseyNumber = list.get(i)[6].trim().equals("") || list.get(i)[6].trim().equals("略")
								|| list.get(i)[6] == null ? i + 4 : Integer.parseInt(list.get(i)[6]);

						str = str + list.get(i)[2] + "-" + list.get(i)[3] + "-" + jerseyNumber + ";";
					}

					// 添加运动员队伍信息到数据库
					CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
					String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
					addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
							competition_type, filename,competitionInfo);

				}
			}
			
			SignTableEntity st = signTableService.queryObjectByFilePath(filename);
			if (log.isEmpty()) {
				st.setCheckStatus(1);
				signTableService.update(st);
				response.getWriter().write("<script>alert('审核通过')</script>");
				return;
			} else {
				st.setCheckStatus(0);
				signTableService.update(st);
				String s = "";
				for (int i = 0; i < log.size(); i++) {
					s = s + log.get(i) + ";    ";
				}
				response.getWriter().write("<script>alert('" + s + "')</script>");
				return;
			}
		} catch (Exception e) {
			response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
			e.printStackTrace();
		}
	}
		
	//解析特色项目学校排球
	public void receiveExcelForVolleyball(HttpServletRequest request, HttpServletResponse response,
			String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
			String company) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
		try {
			String filePath = null;
			String filename = fileName;
			Date upload = today;
			int competition_id = Integer.parseInt(competition_id1);
			String frontUserId = request.getParameter("frontUserif");
			filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
			System.out.println(filePath);
			List<String> log = new ArrayList<>();
			ExcelUtil excelutil = new ExcelUtil();

			// 遍历excel的两张工作表
			int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
			List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);		
	
		for (int number = 0; number < iSheetQuanitity; number++) {
				// 得到运动员数据
				List<String[]> list = excelutil.readByPoi(filePath, number, 6, 5, 9, false);
				System.out.println(list.size());
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < list.get(i).length; j++) {
						System.out.println(list.get(i)[j]);
					}
				}
				// 得到所以合并单元格的数据
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				// 判断报名日期是否正确
				if (isApplyTime(response, competition_id, formatter1, upload)) {
					return;
				}
				// 判断人数是否符合
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				if (isAtheteNum(response, competition_id, filePath, number, list, strCurrentSheetName)) {
					return;
				}
				//判断运动员年龄是否符合
				for (int i = 0; i < list.size(); i++) {
					log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
					//判断性别是否符合
					log = isSex(list, i, log, strCurrentSheetName);
				}
				//判断同一张报名表中参赛人员是否重复
				String[] s = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					log = Check(list, i, log, s);
				}
			}

			for (int number = 0; number < iSheetQuanitity; number++) {
				List<String[]> list = excelutil.readByPoi(filePath, number, 6, 5, 9, false);
				System.out.println(list.size());
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < list.get(i).length; j++) {
						System.out.println(list.get(i)[j]);
					}
				}
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				String phone = excelutil.onecell(filePath, number, 3, 7, false);
				System.out.println(phone);

				String capain = null;
				if (list1.get(7).trim().equals("") ? false : true) {
					capain = list1.get(7).trim();
					if (list1.get(8).trim().equals("") ? false : true) {
						capain = capain + ";" + list1.get(8).trim();
						if (list1.get(9).trim().equals("") ? false : true) {
							capain = capain + ";" + list1.get(9).trim();
						}
					}
				}

				if (log.isEmpty()) {
					System.out.println("log为空");
					for (int i = 0; i < list.size(); i++) {
						addAthlete(i, list, list1, formatter2, company, phone);
					}

					String str = "";
					for (int i = 0; i < list.size(); i++) {

						int jerseyNumber = list.get(i)[6].trim().equals("") || list.get(i)[6].trim().equals("略")
								|| list.get(i)[6] == null ? i + 4 : Integer.parseInt(list.get(i)[6]);

						str = str + list.get(i)[2] + "-" + list.get(i)[3] + "-" + jerseyNumber + ";";
					}

					// 添加运动员队伍信息到数据库
					CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
					String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
					addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
							competition_type, filename,competitionInfo);

				}
			}
			SignTableEntity st = signTableService.queryObjectByFilePath(filename);
			if (log.isEmpty()) {
				st.setCheckStatus(1);
				signTableService.update(st);
				response.getWriter().write("<script>alert('审核通过')</script>");
				return;
			} else {
				st.setCheckStatus(0);
				signTableService.update(st);
				String s = "";
				for (int i = 0; i < log.size(); i++) {
					s = s + log.get(i) + ";    ";
				}
				response.getWriter().write("<script>alert('" + s + "')</script>");
				return;
			}
		} catch (Exception e) {
			response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
			e.printStackTrace();
		}
	}

	
	// 解析体操
	public void receiveExcelForGymnastics(HttpServletRequest request, HttpServletResponse response,
			String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
			String company) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");

		try {
			// String s= shenhe.add(request, response,
			// Integer.parseInt(competition_id), date,fileName);
			String filePath = null;
			String filename = fileName;
			Date upload = today;
			int competition_id = Integer.parseInt(competition_id1);
			String frontUserId = front_user_id;
			// String filename=new
			// String(request.getParameter("filepath").getBytes("ISO-8859-1"),"UTF-8");
			// String filename=request.getParameter("filepath");
			filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
			System.out.println(filePath);
			List<String> log = new ArrayList<>();
			ExcelUtil excelutil = new ExcelUtil();
			List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
			int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
			for (int number = 0; number < iSheetQuanitity; number++) {
				// 得到运动员数据
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				List<String[]> list = new ArrayList<String[]>();
				if (strCurrentSheetName.indexOf("甲组男") != -1 || strCurrentSheetName.indexOf("乙组男") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
				}
				if (strCurrentSheetName.indexOf("丙组男") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 16, true);
				}
				if (strCurrentSheetName.indexOf("丁组男") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
				}
				if (strCurrentSheetName.indexOf("幼儿组男") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
				}
				if (strCurrentSheetName.indexOf("甲组女") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
				}
				if (strCurrentSheetName.indexOf("乙组女") != -1 || strCurrentSheetName.indexOf("丙组女") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 14, true);
				}
				if (strCurrentSheetName.indexOf("丁组女") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
				}
				if (strCurrentSheetName.indexOf("幼儿组女") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
				}
				System.out.println(list.size());
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < list.get(i).length; j++) {
						System.out.println(list.get(i)[j]);
					}
				}
				// 得到所以合并单元格的数据
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				// 判断报名日期是否正确
				if (isApplyTime(response, competition_id, formatter1, upload)) {
					return;
				}

				// 判断人数是否符合
				if (isAtheteNum(response, competition_id, filePath, number, list, strCurrentSheetName)) {
					return;
				}
				//判断运动员年龄是否符合
				for (int i = 0; i < list.size(); i++) {
					log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
					//判断性别是否符合
					log = isSex(list, i, log, strCurrentSheetName);
					//判断是否是注册运动员
					log = isAthlete(list, i, log);
				}
				//判断同一张报名表中参赛人员是否重复
				String[] s = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					log = Check(list, i, log, s);
				}
//				// 判断人数是否正确
//				for (int iCompetitionIndex = 0; iCompetitionIndex < competitionInfo.size(); iCompetitionIndex++) {
//					if (strCurrentSheetName.trim()
//							.equals(competitionInfo.get(iCompetitionIndex).getGroupName().getGroupName())) {
//						if (strCurrentSheetName.indexOf("男") != -1) {
//							if (list.size() > competitionInfo.get(iCompetitionIndex).getGroupName().getMaxMen()) {
//								response.getWriter()
//										.write("<script>alert('" + strCurrentSheetName + "不得超过" + list.size() + "aaa"
//												+ competitionInfo.get(iCompetitionIndex).getGroupName().getMaxMen()
//												+ "人')</script>");
//								return;
//							}
//						} else {
//							if (list.size() > competitionInfo.get(iCompetitionIndex).getGroupName().getMaxWomen()) {
//								response.getWriter()
//										.write("<script>alert('" + strCurrentSheetName + "不得超过"
//												+ competitionInfo.get(iCompetitionIndex).getGroupName().getMaxWomen()
//												+ "人')</script>");
//								return;
//							}
//						}
//
//					}
//				}
//
//				// 判断运动员信息是否正确
//				if (strCurrentSheetName.indexOf("幼儿") == -1) {
//					for (int i = 0; i < list.size(); i++) {
//						Boolean b1 = true;
//						RegisterAthlete registerAthlete = competition.getCNByIdCard(list.get(i)[2]);
//
//						// 根据身份证号获得注册运动员对象
//						if (registerAthlete != null) {
//							System.out.println(registerAthlete.getName());
//							// 判断运动员信息与注册运动员信息是否一致
//							b1 = registerAthlete.getName().equals(list.get(i)[3]);
//							if (!b1) {
//								log.add(list.get(i)[3] + "姓名与注册信息不符");
//							}
//
//							if (b1) {
//								if (strCurrentSheetName.indexOf("男") != -1) {
//									if (registerAthlete.getGender() == 1) {
//										log.add(list.get(i)[3] + "男队不能有女运动员");
//									}
//								} else {
//									if (registerAthlete.getGender() == 0) {
//										log.add(list.get(i)[3] + "女队不能有男运动员");
//									}
//								}
//							}
//
//							// 判断运动员出生日期是否符合规则
//							String birthday = list.get(i)[2].substring(6, 14);
//							Date dtBirthday = formatter2.parse(birthday);
//							for (int iCompetitionIndex = 0; iCompetitionIndex < competitionInfo
//									.size(); iCompetitionIndex++) {
//								if (competitionInfo.get(iCompetitionIndex).getGroupName().getGroupName().trim()
//										.equals(strCurrentSheetName.trim())) {
//									Date dtMaxDate = competitionInfo.get(iCompetitionIndex).getGroupName()
//											.getBirthdayEarly();
//									Date dtMinDate = competitionInfo.get(iCompetitionIndex).getGroupName()
//											.getBirthdayAfter();
//									if (dtBirthday.after(dtMaxDate)) {
//										log.add(list.get(i)[3] + "运动员未到规定年龄");
//									}
//									if (dtBirthday.before(dtMinDate)) {
//										log.add(list.get(i)[3] + "运动员超出规定年龄");
//									}
//								}
//							}
//						} else {
//							// 如果年龄小于多少可以直接过，标准在哪里？？？
//							String birthday = list.get(i)[2].substring(6, 14);
//							if (Integer.parseInt(birthday) <= 20101231) {
//								log.add(list.get(i)[3] + "运动员不存在");
//							}
//						}
//					}
//				} else {
//					for (int i = 0; i < list.size(); i++) {
//						Calendar calendar = Calendar.getInstance();
//						int now_y = calendar.get(Calendar.YEAR);// 得到年份
//						int isYoung = now_y - Integer.parseInt(list.get(i)[2].substring(6, 10));
//						if (isYoung > 7) {
//							log.add(list.get(i)[3] + "不是幼年组");
//						}
//					}
//				}
			}

			for (int number = 0; number < iSheetQuanitity; number++) {
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				String str = "";
				String competitionArray = null;
				List<String[]> list = new ArrayList<String[]>();
				if (strCurrentSheetName.indexOf("甲组男") != -1 || strCurrentSheetName.indexOf("乙组男") != -1) {
					competitionArray = "全能,自由操+山羊,单杠+跳马,双杠+吊环,蹦床";
					list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
				}
				if (strCurrentSheetName.indexOf("丙组男") != -1) {
					competitionArray = "全能,自由操,山羊,单杠,跳马,双杠,吊环,蹦床";
					list = excelutil.readByPoi(filePath, number, 6, 5, 16, true);
				}
				if (strCurrentSheetName.indexOf("丁组男") != -1) {
					competitionArray = "全能,自由操,山羊,单杠,蹦床";
					list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
				}
				if (strCurrentSheetName.indexOf("幼儿组男") != -1) {
					competitionArray = "团体,自由操,跳马,山羊,单杠,集体舞操";
					list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
				}
				if (strCurrentSheetName.indexOf("甲组女") != -1) {
					competitionArray = "全能,自由操+高低杠,跳马+平衡木,蹦床";
					list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
				}
				if (strCurrentSheetName.indexOf("乙组女") != -1 || strCurrentSheetName.indexOf("丙组女") != -1) {
					competitionArray = "全能,自由操,高低杠,跳马,平衡木,蹦床";
					list = excelutil.readByPoi(filePath, number, 6, 5, 14, true);
				}
				if (strCurrentSheetName.indexOf("丁组女") != -1) {
					competitionArray = "全能,自由操,跳马,蹦床";
					list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
				}
				if (strCurrentSheetName.indexOf("幼儿组女") != -1) {
					competitionArray = "团体,自由操,跳马,山羊,平衡木,集体舞操";
					list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
				}

				System.out.println(list.size());
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < list.get(i).length; j++) {
						System.out.println("aaaaaaa" + list.get(i)[j]);
					}
				}
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				String phone = excelutil.onecell(filePath, number, 3, 7, false);
				System.out.println(phone);

				// 得到教练
				String capain = null;
				capain = capainSet(capain, list1);

				if (log.isEmpty()) {
					System.out.println("log为空");
					if (!list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							addAthlete(i, list, list1, formatter2, company, phone);
						}

						for (int i = 0; i < list.size(); i++) {
							// "单杠","双杠","吊环","山羊","自由操","跳马","蹦床","平衡木","高低杠","全能","集体舞操","团体"
							String[] competitionArray1 = competitionArray.split(",");
							str = str + list.get(i)[2];
							for (int j = 8; j < list.get(i).length; j++) {
								if (!(list.get(i)[j].trim().equals("") || list.get(i)[j] == null)) {
									if (list.get(i)[j].equals("√")) {
										str = str + "-" + competitionArray1[j - 8];
									} else {
										response.getWriter().write("<script>alert('选择项目用这种“√”。')</script>");
										return;
									}
								}
							}
							str = str + ";";
						}

						// 添加运动员队伍信息到数据库
						CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
						addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
								competition_type, filename,competitionInfo);
					}

				}
			}

			SignTableEntity st = signTableService.queryObjectByFilePath(filename);
			if (log.isEmpty()) {
				st.setCheckStatus(1);
				signTableService.update(st);
				response.getWriter().write("<script>alert('审核通过')</script>");
				return;
			} else {
				st.setCheckStatus(0);
				signTableService.update(st);
				String s = "";
				for (int i = 0; i < log.size(); i++) {
					s = s + log.get(i) + ";    ";
				}
				response.getWriter().write("<script>alert('" + s + "')</script>");
				return;
			}

		} catch (Exception e) {
			response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
			e.printStackTrace();
		}

	}

	// 解析武术
	public void receiveExcelForKungfu(HttpServletRequest request, HttpServletResponse response, String competition_id1,
			String front_user_id, String competition_type, Date today, String fileName, String company)
					throws IOException {
		response.setContentType("text/html;charset=utf-8");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");

		try {
			// String s= shenhe.add(request, response,
			// Integer.parseInt(competition_id), date,fileName);
			String filePath = null;
			String filename = fileName;
			Date upload = today;
			int competition_id = Integer.parseInt(competition_id1);
			String frontUserId = front_user_id;
			// String filename=new
			// String(request.getParameter("filepath").getBytes("ISO-8859-1"),"UTF-8");
			// String filename=request.getParameter("filepath");
			filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
			System.out.println(filePath);
			List<String> log = new ArrayList<>();
			ExcelUtil excelutil = new ExcelUtil();
			Map<String, Integer> projectNumberMap = new HashMap<String, Integer>();
			int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
			String competitionArray = "";
			int[] quanshuNumber = new int[11];// "长拳0、南拳1、太极拳2、自选太极拳3、地躺拳4、第一套长拳5、第一套南拳6、乙42太极拳7、3路长拳8、规定拳9、24太极拳10"
			List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
			for (int number = 0; number < iSheetQuanitity; number++) {
				// 得到运动员数据
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				List<String[]> list = new ArrayList<String[]>();
				if (strCurrentSheetName.indexOf("甲组男") != -1) {
					competitionArray = "第三套国际竞赛套路,自选套路,地躺拳,对练,测试";
					list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
				}
				if (strCurrentSheetName.indexOf("乙组男") != -1 || strCurrentSheetName.indexOf("乙组女") != -1) {
					competitionArray = "第一套国际竞赛套路,42式太极拳,42式太极剑,测试";
					list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
				}
				if (strCurrentSheetName.indexOf("丙组男") != -1 || strCurrentSheetName.indexOf("丙组女") != -1) {
					competitionArray = "初级第3路,初级套路,少年规定拳,24式太极拳,基本功,测试";
					list = excelutil.readByPoi(filePath, number, 6, 5, 14, true);
				}
				if (strCurrentSheetName.indexOf("甲组女") != -1) {
					competitionArray = "第三套国际竞赛套路,自选套路,对练,测试";
					list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
				}
				if (strCurrentSheetName.indexOf("集体项目") != -1) {
					competitionArray = "集体项目,测试";
					list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
				}
				/*
				 * for (int i = 0; i < list.size(); i++) { for (int j = 0; j <
				 * list.get(i).length; j++) {
				 * System.out.println(list.get(i)[j]); } }
				 */
				// 得到所以合并单元格的数据
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				// 判断报名日期是否正确
				if (isApplyTime(response, competition_id, formatter1, upload)) {
					return;
				}

				// 判断人数是否正确
				List<String[]> list3 = new ArrayList<String[]>();
				for (int k = 0; k < list.size(); k++) {
					if (list.get(k)[list.get(k).length - 1] == "") {
						list3.add(list.get(k));
					}
				}
				if (isAtheteNum(response, competition_id, filePath, number, list3, strCurrentSheetName)) {
					return;
				}

				for (int i = 0; i < list.size(); i++) {
					int temp = 0;
					for (int j = 8; j < list.get(i).length; j++) {
						if (!(list.get(i)[j].trim().equals("") || list.get(i)[j] == null)) {
							if (list.get(i)[j].equals("√")) {
								temp++;
							} else {
								int count = 0;
								String regEx = "[\\u4e00-\\u9fa5]";
								Pattern p = Pattern.compile(regEx);
								Matcher m = p.matcher(list.get(i)[j]);
								while (m.find()) {
									for (int k = 0; k <= m.groupCount(); k++) {
										count = count + 1;
									}
								}
								if (count == 0) {
									response.getWriter().write("<script>alert('选择项目用这种“√”。')</script>");
									return;
								} else {
									String[] s = list.get(i)[j].split("、");
									temp = temp + s.length;
								}
							}
						}
					}
					if(list.get(i)[list.get(i).length-1]==null||list.get(i)[list.get(i).length-1].equals("")){
						if (projectNumberMap.get(list.get(i)[3]) == null) {
							projectNumberMap.put(list.get(i)[3], temp);
						} else {
							temp = projectNumberMap.get(list.get(i)[3]) + temp;
							projectNumberMap.put(list.get(i)[3], temp);
						}
					}
					
				}
				
				for (int i = 0; i < list.size(); i++) {
					//判断运动员年龄是否符合
					log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
					//判断性别是否符合
					log = isSex(list, i, log, strCurrentSheetName);
					//判断是否是注册运动员
					log = isAthlete(list, i, log);
				}
				//判断同一张报名表中参赛人员是否重复
				String[] s = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					log = Check(list, i, log, s);
				}

				
				
				for (int i = 0; i < list.size(); i++) {
					if(list.get(i)[list.get(i).length-1]==null||list.get(i)[list.get(i).length-1].equals("")){
						String[] competitionArray1 = competitionArray.split(",");
						for (int j = 8; j < list.get(i).length; j++) {
							if (!(list.get(i)[j].trim().equals("") || list.get(i)[j] == null)) {
								if (list.get(i)[j].equals("√")) {
									if(competitionArray1[j - 8].equals("第三套国际竞赛套路")||competitionArray1[j - 8].equals("自选套路")||competitionArray1[j - 8].equals("第一套国际竞赛套路")||competitionArray1[j - 8].equals("初级套路")){
										response.getWriter().write("<script>alert('该"+competitionArray1[j - 8]+"项目请用汉字写想报的比赛并用“、”分割。')</script>");
										return;
									}
									if (competitionArray1[j - 8].equals("地躺拳")) {
										quanshuNumber[4]++;
									}
									if (competitionArray1[j - 8].equals("42式太极拳")) {
										quanshuNumber[7]++;
									}
									if (competitionArray1[j - 8].equals("初级第3路")) {
										quanshuNumber[8]++;
									}
									if (competitionArray1[j - 8].equals("少年规定拳")) {
										quanshuNumber[9]++;
									}
									if (competitionArray1[j - 8].equals("24式太极拳")) {
										quanshuNumber[10]++;
									}
								} else {
									int count = 0;
									String regEx = "[\\u4e00-\\u9fa5]";
									Pattern p = Pattern.compile(regEx);
									Matcher m = p.matcher(list.get(i)[j]);
									while (m.find()) {
										for (int k = 0; k <= m.groupCount(); k++) {
											count = count + 1;
										}
									}
									if (count == 0) {
										response.getWriter().write("<script>alert('选择项目用这种“√”。')</script>");
										return;
									} else {
										String s2;
										if (competitionArray1[j - 8].equals("第三套国际竞赛套路")) {
											s2 = "长拳、南拳、太极拳";
										} else if (competitionArray1[j - 8].equals("自选套路")) {
											s2 = "刀术、剑术、枪术、棍术、南刀、南棍、太极剑、太极拳";
										} else if (competitionArray1[j - 8].equals("第一套国际竞赛套路")) {
											s2 = "长拳、刀术、剑术、枪术、棍术、南刀、南棍、南拳";
										} else if (competitionArray1[j - 8].equals("初级套路")) {
											s2 = "刀术、剑术、枪术、棍术";
										} else if (competitionArray1[j - 8].equals("比赛内容")) {
											s2 = list.get(i)[j];
										}else {
											response.getWriter().write(
													"<script>alert('除了第三套国际竞赛套路、自选套路、第一套国际竞赛套路、初级套路写汉字，其他项目用这种“√”。')</script>");
											return;
										}
										String[] s1 = list.get(i)[j].split("、");
										for (int k = 0; k < s1.length; k++) {
											if (s2.indexOf(s1[k]) == -1) {
												response.getWriter().write("<script>alert('项目里面写" + s2 + "。')</script>");
												return;
											} else {
												if (competitionArray1[j - 8].equals("第三套国际竞赛套路") && s1[k].equals("长拳")) {
													quanshuNumber[0]++;
												}
												if (competitionArray1[j - 8].equals("第三套国际竞赛套路") && s1[k].equals("南拳")) {
													quanshuNumber[1]++;
												}
												if (competitionArray1[j - 8].equals("第三套国际竞赛套路") && s1[k].equals("太极拳")) {
													quanshuNumber[2]++;
												}
												if (competitionArray1[j - 8].equals("自选套路") && s1[k].equals("太极拳")) {
													quanshuNumber[3]++;
												}
												if (competitionArray1[j - 8].equals("第一套国际竞赛套路") && s1[k].equals("长拳")) {
													quanshuNumber[5]++;
												}
												if (competitionArray1[j - 8].equals("第一套国际竞赛套路") && s1[k].equals("南拳")) {
													quanshuNumber[6]++;
												}
											}
										}
									}
								}
							}
						}
					}
					
				}
				
			}
			//判别拳术人员是否满足
			for(int i=0;i<quanshuNumber.length;i++){
				if(quanshuNumber[i]>2){
					response.getWriter().write("<script>alert('每种拳术机械各组限报2人。')</script>");
					return;
				}
			}
			//判断每个人是否报3项项目
			String error = "";
			for (Map.Entry<String, Integer> entry : projectNumberMap.entrySet()) {
				if (entry.getValue() < 3) {
					error = error + entry.getKey() + "   ";
				}
			}
			if (error != "") {
				response.getWriter().write("<script>alert('" + error + "必须要参加三个项目，如果项目里面有多个项目请用“、”分割。')</script>");
				return;
			}

			for (int number = 0; number < iSheetQuanitity; number++) {

				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				String str = "";
				competitionArray = null;
				List<String[]> list = new ArrayList<String[]>();
				if (strCurrentSheetName.indexOf("甲组男") != -1) {
					competitionArray = "第三套国际竞赛套路,自选套路,地躺拳,对练,测试";
					list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
				}
				if (strCurrentSheetName.indexOf("乙组男") != -1 || strCurrentSheetName.indexOf("乙组女") != -1) {
					competitionArray = "第一套国际竞赛套路,42式太极拳,42式太极剑,测试";
					list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
				}
				if (strCurrentSheetName.indexOf("丙组男") != -1 || strCurrentSheetName.indexOf("丙组女") != -1) {
					competitionArray = "初级第3路,初级套路,少年规定拳,24式太极拳,基本功,测试";
					list = excelutil.readByPoi(filePath, number, 6, 5, 14, true);
				}
				if (strCurrentSheetName.indexOf("甲组女") != -1) {
					competitionArray = "第三套国际竞赛套路,自选套路,对练,测试";
					list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
				}
				if (strCurrentSheetName.indexOf("集体项目") != -1) {
					competitionArray = "集体项目,测试";
					list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
				}

				List<String> list1 = excelutil.execelpoi(filePath, number, 0);

				String phone = excelutil.onecell(filePath, number, 3, 7, false);
				System.out.println(phone);

				// 得到教练
				String capain = null;
				capain = capainSet(capain, list1);

				if (log.isEmpty()) {
					System.out.println("log为空");
					if (!list.isEmpty()) {

						for (int i = 0; i < list.size(); i++) {
							String[] competitionArray1 = competitionArray.split(",");
							str = str + list.get(i)[2] + "-" + list.get(i)[3];
							for (int j = 8; j < list.get(i).length; j++) {
								if (!(list.get(i)[j].trim().equals("") || list.get(i)[j] == null)) {
									if (list.get(i)[j].equals("√")) {
										str = str + "-" + competitionArray1[j - 8];
									} else {
										String s;
										if (competitionArray1[j - 8].equals("第三套国际竞赛套路")) {
											s = "长拳、南拳、太极拳";
										} else if (competitionArray1[j - 8].equals("自选套路")) {
											s = "刀术、剑术、枪术、棍术、南刀、南棍、太极剑、太极拳";
										} else if (competitionArray1[j - 8].equals("第一套国际竞赛套路")) {
											s = "长拳、刀术、剑术、枪术、棍术、南刀、南棍、南拳";
										} else if (competitionArray1[j - 8].equals("初级套路")) {
											s = "刀术、剑术、枪术、棍术";
										} else {
											response.getWriter().write(
													"<script>alert('除了第三套国际竞赛套路、自选套路、第一套国际竞赛套路、初级套路写汉字，其他项目用这种“√”。')</script>");
											return;
										}
										String[] s1 = list.get(i)[j].split("、");
										for (int k = 0; k < s1.length; k++) {
											if (s.indexOf(s1[k]) == -1) {
												response.getWriter().write("<script>alert('项目里面写" + s + "。')</script>");
												return;
											}
											str = str + "-" + competitionArray1[j - 8] + "(" + list.get(i)[j] + ")";
										}
									}
								}
							}
							str = str + ";";
						}

						for (int i = 0; i < list.size(); i++) {
							addAthlete(i, list, list1, formatter2, company, phone);
						}

						// 添加运动员队伍信息到数据库
						CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
						addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
								competition_type, filename,competitionInfo);
					}
				}
			}

			SignTableEntity st = signTableService.queryObjectByFilePath(filename);
			if (log.isEmpty()) {
				st.setCheckStatus(1);
				signTableService.update(st);
				response.getWriter().write("<script>alert('审核通过')</script>");
				return;
			} else {
				st.setCheckStatus(0);
				signTableService.update(st);
				String s = "";
				for (int i = 0; i < log.size(); i++) {
					s = s + log.get(i) + ";    ";
				}
				response.getWriter().write("<script>alert('" + s + "')</script>");
				return;
			}
		} catch (Exception e) {
			response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
			e.printStackTrace();
		}

	}

	// 解析游泳
	public void receiveExcelForSwim(HttpServletRequest request, HttpServletResponse response, String competition_id1,
			String front_user_id, String competition_type, Date today, String fileName, String company)
					throws IOException {
		response.setContentType("text/html;charset=utf-8");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");

		try {
			String filePath = null;
			String filename = fileName;
			Date upload = today;
			int competition_id = Integer.parseInt(competition_id1);
			String frontUserId = front_user_id;
			filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
			System.out.println(filePath);
			List<String> log = new ArrayList<>();
			ExcelUtil excelutil = new ExcelUtil();
			HashSet<String> athleteSet = new HashSet<String>();
			int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
			List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
			for (int number = 0; number < iSheetQuanitity; number++) {
				// 得到运动员数据
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				List<String[]> list = new ArrayList<String[]>();
				if (strCurrentSheetName.indexOf("高中组男") != -1 || strCurrentSheetName.indexOf("高中组女") != -1
						|| strCurrentSheetName.indexOf("初中组女") != -1 || strCurrentSheetName.indexOf("初中组男") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
				}
				if (strCurrentSheetName.indexOf("男子12岁组") != -1 || strCurrentSheetName.indexOf("男子13岁组") != -1
						|| strCurrentSheetName.indexOf("女子12岁组") != -1 || strCurrentSheetName.indexOf("女子11岁组") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
				}
				if (strCurrentSheetName.indexOf("男子12-13岁组") != -1 || strCurrentSheetName.indexOf("女子11-12岁组") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
				}
				if (strCurrentSheetName.indexOf("男子11岁组") != -1 || strCurrentSheetName.indexOf("男子10岁组") != -1
						|| strCurrentSheetName.indexOf("女子10岁组") != -1 || strCurrentSheetName.indexOf("女子9岁组") != -1) {
					try {
						list = excelutil.readByPoi(filePath, number, 6, 5, 17, true);
					} catch (Exception e) {
					}
					if (strCurrentSheetName.indexOf("女子10岁组") != -1) {

						list = excelutil.readByPoi(filePath, number, 6, 5, 17, true);
					}

				}
				if (strCurrentSheetName.indexOf("男子9岁组") != -1 || strCurrentSheetName.indexOf("女子8岁组") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 14, true);
				}
				if (strCurrentSheetName.indexOf("男子8岁及以下组") != -1 || strCurrentSheetName.indexOf("女子7岁及以下组") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 14, true);
				}

				// 得到所以合并单元格的数据
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);

				// 判断报名日期是否正确
				if (isApplyTime(response, competition_id, formatter1, upload)) {
					return;
				}
				if (!(strCurrentSheetName.trim().equals("高中组男") || strCurrentSheetName.trim().equals("高中组女")
						|| strCurrentSheetName.trim().equals("初中组女") || strCurrentSheetName.trim().equals("初中组男"))) {
					// 判断人数是否正确
					if (isAtheteNum(response, competition_id, filePath, number, list, strCurrentSheetName)) {
						return;
					}
					for (String[] str : list) {
						athleteSet.add(str[2]);
					}
				}

				if (athleteSet.size() > 65) {
					response.getWriter().write("<script>alert('除了高中组，初中组其他组可报共65人。')</script>");
					return;
				}

				// 验证运动员信息
				int jieliNumber = 0;
				List<String> athleteList1 = new ArrayList<String>();
				List<String> athleteList2 = new ArrayList<String>();
				List<String> athleteList3 = new ArrayList<String>();
				
				for (int i = 0; i < list.size(); i++) {

					log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
					log = isSex(list, i, log, strCurrentSheetName);
					log = isAthlete(list, i, log);
					if (!(strCurrentSheetName.trim().equals("高中组男") || strCurrentSheetName.trim().equals("高中组女")
							|| strCurrentSheetName.trim().equals("初中组女") || strCurrentSheetName.trim().equals("初中组男")
							|| strCurrentSheetName.trim().equals("男子13岁组")
							|| strCurrentSheetName.trim().equals("男子12岁组")
							|| strCurrentSheetName.trim().equals("女子12岁组")
							|| strCurrentSheetName.trim().equals("女子11岁组"))) {
						if (strCurrentSheetName.trim().equals("男子8岁及以下组")
								|| strCurrentSheetName.trim().equals("女子7岁及以下组")) {
							if (athleteList1.size() != 4) {
								if (list.get(i)[8].trim().equals("")) {
									if (athleteList1.size() != 0) {
										String s1 = "";
										for (int j = 0; j < athleteList1.size(); j++) {
											s1 = s1 + athleteList1.get(j);
										}
										response.getWriter().write("<script>alert('接力需要4人" + s1 + "只有"
												+ athleteList1.size() + "。')</script>");
										return;
									}
								} else {
									if (list.get(i)[8].trim().equals("√")) {
										athleteList1.add(list.get(i)[3]);
									} else {
										response.getWriter().write("<script>alert('选择项目用这种“√”。')</script>");
										return;
									}
								}
							} else {
								jieliNumber++;
								athleteList1.clear();
							}
						} else {
							if (athleteList2.size() != 4) {
								if (list.get(i)[8].trim().equals("")) {
									if (athleteList2.size() != 0) {
										String s1 = "";
										for (int j = 0; j < athleteList2.size(); j++) {
											s1 = s1 + athleteList2.get(j);
										}
										response.getWriter().write("<script>alert('接力需要4人" + s1 + "只有"
												+ athleteList2.size() + "。')</script>");
										return;
									}
								} else {
									if (list.get(i)[8].trim().equals("√")) {
										athleteList2.add(list.get(i)[3]);
									} else {
										response.getWriter().write("<script>alert('选择项目用这种“√”。')</script>");
										return;
									}
								}
							} else {
								jieliNumber++;
								athleteList3.clear();
							}

							if (athleteList3.size() != 4) {
								if (list.get(i)[9].trim().equals("")) {
									if (athleteList3.size() != 0) {
										String s1 = "";
										for (int j = 0; j < athleteList3.size(); j++) {
											s1 = s1 + athleteList3.get(j);
										}
										response.getWriter().write("<script>alert('接力需要4人" + s1 + "只有"
												+ athleteList3.size() + "。')</script>");
										return;
									}
								} else {
									if (list.get(i)[9].trim().equals("√")) {
										athleteList3.add(list.get(i)[3]);
									} else {
										response.getWriter().write("<script>alert('选择项目用这种“√”。')</script>");
										return;
									}
								}
							} else {
								jieliNumber++;
								athleteList3.clear();
							}
						}

						if (jieliNumber > 2) {
							response.getWriter().write("<script>alert('每组接力最多2对。')</script>");
							return;
						}
					}
				}
			}

			for (int number = 0; number < iSheetQuanitity; number++) {
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				String str = "";
				String competitionArray = null;
				List<String[]> list = new ArrayList<String[]>();
				if (strCurrentSheetName.indexOf("高中组男") != -1 || strCurrentSheetName.indexOf("高中组女") != -1
						|| strCurrentSheetName.indexOf("初中组女") != -1 || strCurrentSheetName.indexOf("初中组男") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
					competitionArray = "100m蝶,100m仰,100m蛙,100m自";
				}
				if (strCurrentSheetName.indexOf("男子12岁组") != -1 || strCurrentSheetName.indexOf("男子13岁组") != -1
						|| strCurrentSheetName.indexOf("女子12岁组") != -1 || strCurrentSheetName.indexOf("女子11岁组") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
					competitionArray = "全能,100m自,200m个混,800m自";
				}
				if (strCurrentSheetName.indexOf("男子12-13岁组") != -1 || strCurrentSheetName.indexOf("女子11-12岁组") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
					competitionArray = "4*100m自接,4*100m混接";
				}
				if (strCurrentSheetName.indexOf("男子11岁组") != -1 || strCurrentSheetName.indexOf("男子10岁组") != -1
						|| strCurrentSheetName.indexOf("女子10岁组") != -1 || strCurrentSheetName.indexOf("女子9岁组") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 17, true);
					competitionArray = "4*100m自接,4*100m混接,全能,50m自,200m个混,400m自,100m蝶+400m自,100m仰+400m自,100m蛙+200m混";
				}
				if (strCurrentSheetName.indexOf("男子9岁组") != -1 || strCurrentSheetName.indexOf("女子8岁组") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 14, true);
					competitionArray = "4*50m自接,4*50m混接,全能,50m自,200m个混,400m自";
				}
				if (strCurrentSheetName.indexOf("男子8岁及以下组") != -1 || strCurrentSheetName.indexOf("女子7岁及以下组") != -1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 14, true);
					competitionArray = "400m自,4*50m自接,50m蝶,50m仰,50m蛙,50m自,200m个混";
				}

				List<String> list1 = excelutil.execelpoi(filePath, number, 0);

				String phone = excelutil.onecell(filePath, number, 3, 7, false);
				System.out.println(phone);

				// 得到教练
				String capain = null;
				capain = capainSet(capain, list1);

				if (log.isEmpty()) {
					System.out.println("log为空");
					if (!list.isEmpty()) {

						for (int i = 0; i < list.size(); i++) {
							String[] competitionArray1 = competitionArray.split(",");
							str = str + list.get(i)[2] + "-" + list.get(i)[3];
							for (int j = 8; j < list.get(i).length; j++) {
								if (!(list.get(i)[j].trim().equals("") || list.get(i)[j] == null)) {
									if (list.get(i)[j].equals("√")) {
										str = str + "-" + competitionArray1[j - 8];
									} else {
										int count = 0;
										String regEx = "[\\u4e00-\\u9fa5]";
										Pattern p = Pattern.compile(regEx);
										Matcher m = p.matcher(list.get(i)[j]);
										while (m.find()) {
											for (int k = 0; k <= m.groupCount(); k++) {
												count = count + 1;
											}
										}
										if (count == 0) {
											response.getWriter().write("<script>alert('选择项目用这种“√”。')</script>");
											return;
										} else {
											str = str + "-" + list.get(i)[j];
										}
									}
								}
							}
							str = str + ";";
						}

						for (int i = 0; i < list.size(); i++) {
							addAthlete(i, list, list1, formatter2, company, phone);
						}
						// 添加运动员队伍信息到数据库
						CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
						addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
								competition_type, filename, competitionInfo);
					}
				}
			}

			SignTableEntity st = signTableService.queryObjectByFilePath(filename);
			if (log.isEmpty()) {
				st.setCheckStatus(1);
				signTableService.update(st);
				response.getWriter().write("<script>alert('审核通过')</script>");
				return;
			} else {
				st.setCheckStatus(0);
				signTableService.update(st);
				String s = "";
				for (int i = 0; i < log.size(); i++) {
					s = s + log.get(i) + ";    ";
				}
				response.getWriter().write("<script>alert('" + s + "')</script>");
				return;
			}
		} catch (Exception e) {
			response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
			e.printStackTrace();
		}
	}

	// 解析特色项目学校游泳
	public void receiveExcelForTeseSwimming(HttpServletRequest request, HttpServletResponse response,
			String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
			String company) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
		try{
			String filePath = null;
			String filename = fileName;
			Date upload = today;
			int competition_id = Integer.parseInt(competition_id1);
			String frontUserId = front_user_id;
			filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
			System.out.println(filePath);
			List<String> log = new ArrayList<>();
			ExcelUtil excelutil = new ExcelUtil();

			int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
			List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
			for (int number = 0; number < iSheetQuanitity; number++) {
				// 得到运动员数据
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				List<String[]> list = new ArrayList<String[]>();
				int xiangmuNum=0;
				if(strCurrentSheetName.indexOf("游泳初中组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					xiangmuNum=15;
				}
				if(strCurrentSheetName.indexOf("游泳小学A组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					xiangmuNum=15;
				}
				if(strCurrentSheetName.indexOf("游泳小学B组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					xiangmuNum=15;
				}
				if(strCurrentSheetName.indexOf("游泳小学C组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					xiangmuNum=15;
				}
				if(strCurrentSheetName.indexOf("游泳小学D组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					xiangmuNum=15;
				}
				if(strCurrentSheetName.indexOf("游泳小学E组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					xiangmuNum=15;
				}
				if(strCurrentSheetName.indexOf("游泳小学F组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					xiangmuNum=15;
				}
				if(strCurrentSheetName.indexOf("游泳幼儿园组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					xiangmuNum=15;
				}
				
				int[] athletexiangmus = new int[list.size()]; 	   //运动员报的项目数
				int[] athletexiangmus2 = new int[list.size()];     //运动员报的项目数
				for (int i = 0; i < list.size(); i++) {
					athletexiangmus[i] = 0;
					athletexiangmus2[i] = 0;
					for (int j = 8; j < list.get(i).length; j++) {
						if (!list.get(i)[j].equals("")) {
							if (!list.get(i)[j].trim().equals("√")) {
								response.getWriter().write("<script>alert('请勾选项目时用“√”。')</script>");
								return;
							}else if(strCurrentSheetName.indexOf("初中")!=-1){
								if(list.get(i)[j].trim().equals("√")) {
									athletexiangmus[i]++;
								}
							}else if(strCurrentSheetName.indexOf("小学F组")!=-1){
								if(j>7&&j<12){
									if(list.get(i)[j].trim().equals("√")) {
										athletexiangmus[i]++;
									}
								}else{
									if(list.get(i)[j].trim().equals("√")) {
										athletexiangmus2[i]++;
									}
								}
							}else if(strCurrentSheetName.indexOf("小学A组")!=-1||
									 strCurrentSheetName.indexOf("小学B组")!=-1||
									 strCurrentSheetName.indexOf("小学C组")!=-1||
									 strCurrentSheetName.indexOf("小学D组")!=-1||
									 strCurrentSheetName.indexOf("小学E组")!=-1){
								if(list.get(i)[j].trim().equals("√")&&j>7&&j<12) {
									athletexiangmus[i]++;
								}
							}else if(strCurrentSheetName.indexOf("幼儿园")!=-1){
								if(list.get(i)[j].trim().equals("√")) {
									athletexiangmus[i]++;
								}
							}
						}
					}
				}
				//判断运动员报的项目是否符合
				for (int i = 0; i < list.size(); i++) {
					if(strCurrentSheetName.indexOf("初中组")!=-1){
						if(athletexiangmus[i]>2){
						    response.getWriter().write("<script>alert('初中组运动员限报两种泳式的单项。')</script>");
						    return;
					    }
					}else if(strCurrentSheetName.indexOf("小学A组")!=-1){
						if(athletexiangmus[i]>2){
						    response.getWriter().write("<script>alert('小学A组运动员限报两种泳式的单项和200m个人混合泳。')</script>");
						    return;
					    }
					}else if(strCurrentSheetName.indexOf("小学B组")!=-1){
						if(athletexiangmus[i]>2){
						    response.getWriter().write("<script>alert('小学B组运动员限报两种泳式的单项和200m个人混合泳。')</script>");
						    return;
					    }
					}else if(strCurrentSheetName.indexOf("小学C组")!=-1){
						if(athletexiangmus[i]>2){
						    response.getWriter().write("<script>alert('小学C组运动员限报两种泳式的单项和200m个人混合泳。')</script>");
						    return;
					    }
					}else if(strCurrentSheetName.indexOf("小学D组")!=-1){
						if(athletexiangmus[i]>2){
						    response.getWriter().write("<script>alert('小学D组运动员限报两种泳式的单项和200m个人混合泳。')</script>");
						    return;
					    }
					}else if(strCurrentSheetName.indexOf("小学E组")!=-1){
						if(athletexiangmus[i]>2){
						    response.getWriter().write("<script>alert('小学E组运动员限报两种泳式的单项和200m个人混合泳。')</script>");
						    return;
					    }
					}else if(strCurrentSheetName.indexOf("小学F组")!=-1){
						if(athletexiangmus[i]>2||athletexiangmus2[i]>1){
							response.getWriter().write("<script>alert('小学F组运动员限报两种泳式的打腿和一个单项。')</script>");
						    return;
						}
					}else if(strCurrentSheetName.indexOf("幼儿园")!=-1){
						if(athletexiangmus[i]>2){
						    response.getWriter().write("<script>alert('幼儿园组运动员限报两种泳式的打腿。')</script>");
						    return;
					    }
					}
				}
				
				// 得到所以合并单元格的数据
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				// 判断报名日期是否正确
				if (isApplyTime(response, competition_id, formatter1, upload)) {
					return;
				}

				// 判断人数是否正确
				if(strCurrentSheetName.equals("游泳小学A组")||strCurrentSheetName.equals("游泳小学B组")){
					if(list.size()>6){
						response.getWriter().write("<script>alert('" + strCurrentSheetName + "不得多于6人')</script>");
					}
				}else if(strCurrentSheetName.equals("游泳小学C组")||strCurrentSheetName.equals("游泳小学D组")){
					if(list.size()>8){
						response.getWriter().write("<script>alert('" + strCurrentSheetName + "不得多于8人')</script>");
					}
				}else if(strCurrentSheetName.equals("游泳小学E组")||strCurrentSheetName.equals("游泳小学F组")){
					if(list.size()>10){
						response.getWriter().write("<script>alert('" + strCurrentSheetName + "不得多于10人')</script>");
					}
				}else if(strCurrentSheetName.equals("游泳幼儿园组")){
					if(list.size()>12){
						response.getWriter().write("<script>alert('" + strCurrentSheetName + "不得多于12人')</script>");
					}
				}
				
				//判断运动员所报项目与性别是否符合和年龄是否符合
//				for (int i = 0; i < list.size(); i++) {
//					log = isError3(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
//				}
				//判断运动员年龄是否符合
				for (int i = 0; i < list.size(); i++) {
					log = isSex(list, i, log, strCurrentSheetName);
					log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
				}
				//判断同一张报名表中参赛人员是否重复
				String[] s = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					log = Check(list, i, log, s);
				}
			}
			
			for (int number = 0; number < iSheetQuanitity; number++) {
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				String str = "";
				String competitionArray = null;
				List<String[]> list = new ArrayList<String[]>();
			    if(strCurrentSheetName.indexOf("游泳初中组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					competitionArray="100m自,100m仰,100m蛙,100m蝶";
				}
				if(strCurrentSheetName.indexOf("游泳小学A组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					competitionArray="50m自,50m仰,50m蛙,50m蝶,200m个混";
				}
				if(strCurrentSheetName.indexOf("游泳小学B组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					competitionArray="50m自,50m仰,50m蛙,50m蝶,200m个混";
				}
				if(strCurrentSheetName.indexOf("游泳小学C组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					competitionArray="50m自,50m仰,50m蛙,50m蝶,200m个混";
				}
				if(strCurrentSheetName.indexOf("游泳小学D组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					competitionArray="50m自,50m仰,50m蛙,50m蝶,200m个混";
				}
				if(strCurrentSheetName.indexOf("游泳小学E组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					competitionArray="50m自,50m仰,50m蛙,50m蝶,200m个混";
				}
				if(strCurrentSheetName.indexOf("游泳小学F组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 28, true);
					competitionArray="50m自打腿,50m仰打腿,50m蛙打腿,50m蝶打腿,50m自,50m仰";
				}
				if(strCurrentSheetName.indexOf("游泳幼儿园组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					competitionArray="50m自打腿,50m仰打腿,50m蛙打腿,50m蝶打腿";
				}
				String[] competitionArray1 = competitionArray.split(",");
				if(strCurrentSheetName.indexOf("幼儿园组")!=-1){
					for(int j = 8; j < 12; j++){
					 if(!(competitionArray1[j-8].equals("")||competitionArray1[j-8].equals(null))){
						for (int i = 0; i < list.size(); i++) {
							if (!(list.get(i)[j].trim().equals("") || list.get(i)[j].trim().equals(null))) {
								strCurrentSheetName=strCurrentSheetName + competitionArray1[j-8];
								log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
								log = isSex(list, i, log, strCurrentSheetName);
								strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
							}
						}
					 }
				   }
				}else if(strCurrentSheetName.indexOf("小学A组")!=-1||
						 strCurrentSheetName.indexOf("小学B组")!=-1||
						 strCurrentSheetName.indexOf("小学C组")!=-1||
						 strCurrentSheetName.indexOf("小学D组")!=-1||
						 strCurrentSheetName.indexOf("小学E组")!=-1){
					for(int j = 8; j < 13; j++){
					 if(!(competitionArray1[j-8].equals("")||competitionArray1[j-8].equals(null))){
						for (int i = 0; i < list.size(); i++) {
							if (!(list.get(i)[j].trim().equals("") || list.get(i)[j].trim().equals(null))) {
								strCurrentSheetName=strCurrentSheetName + competitionArray1[j-8];
								log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
								log = isSex(list, i, log, strCurrentSheetName);
								strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
							}
						}
					 }
				   }
				}else if(strCurrentSheetName.indexOf("小学F组")!=-1){
					for(int j = 8; j < 14; j++){
					 if(!(competitionArray1[j-8].equals("")||competitionArray1[j-8].equals(null))){
						for (int i = 0; i < list.size(); i++) {
							if (!(list.get(i)[j].trim().equals("") || list.get(i)[j].trim().equals(null))) {
								strCurrentSheetName=strCurrentSheetName + competitionArray1[j-8];
								log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
								log = isSex(list, i, log, strCurrentSheetName);
								strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
							}
						}
					 }
				   }
				}
			}
			
			for (int number = 0; number < iSheetQuanitity; number++) {
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				String str = "";
				String competitionArray = null;
				List<String[]> list = new ArrayList<String[]>();
			    if(strCurrentSheetName.indexOf("游泳初中组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					competitionArray="100m自,100m仰,100m蛙,100m蝶";
				}
				if(strCurrentSheetName.indexOf("游泳小学A组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					competitionArray="50m自,50m仰,50m蛙,50m蝶,200m个混";
				}
				if(strCurrentSheetName.indexOf("游泳小学B组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					competitionArray="50m自,50m仰,50m蛙,50m蝶,200m个混";
				}
				if(strCurrentSheetName.indexOf("游泳小学C组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					competitionArray="50m自,50m仰,50m蛙,50m蝶,200m个混";
				}
				if(strCurrentSheetName.indexOf("游泳小学D组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					competitionArray="50m自,50m仰,50m蛙,50m蝶,200m个混";
				}
				if(strCurrentSheetName.indexOf("游泳小学E组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					competitionArray="50m自,50m仰,50m蛙,50m蝶,200m个混";
				}
				if(strCurrentSheetName.indexOf("游泳小学F组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 28, true);
					competitionArray="50m自打腿,50m仰打腿,50m蛙打腿,50m蝶打腿,50m自,50m仰";
				}
				if(strCurrentSheetName.indexOf("游泳幼儿园组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					competitionArray="50m自打腿,50m仰打腿,50m蛙打腿,50m蝶打腿";
				}
				
					System.out.println(list.size());
					for (int i = 0; i < list.size(); i++) {
						for (int j = 0; j < list.get(i).length; j++) {
							System.out.println("aaaaaaa" + list.get(i)[j]);
						}
					}
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
					for (int i = 0; i < list1.size(); i++) {
						System.out.println(list1.get(i));
					}
				String phone = excelutil.onecell(filePath, number, 3, 7, false);
				System.out.println(phone);

				// 得到教练
				String capain = null;
				capain = capainSet(capain, list1);

				if (log.isEmpty()) {
					System.out.println("log为空");
					if (!list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							addAthlete(i, list, list1, formatter2, company, phone);
						}

						if(strCurrentSheetName.indexOf("初中组")!=-1||strCurrentSheetName.indexOf("幼儿园组")!=-1){
							for(int j = 8; j < 12; j++){
								String[] competitionArray1 = competitionArray.split(",");
							 if(!(competitionArray1[j-8].equals("")||competitionArray1[j-8].equals(null))){
								for (int i = 0; i < list.size(); i++) {
									if (!(list.get(i)[j].trim().equals("") || list.get(i)[j].trim().equals(null))) {
										str = list.get(i)[2] + "-" + list.get(i)[3]+"-"+ competitionArray1[j-8]+";";
										strCurrentSheetName=strCurrentSheetName + competitionArray1[j-8];
										CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
										addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
												competition_type, filename, competitionInfo);
										strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
									}
								}
							 }
						   }
						}else if(strCurrentSheetName.indexOf("小学A组")!=-1||
								 strCurrentSheetName.indexOf("小学B组")!=-1||
								 strCurrentSheetName.indexOf("小学C组")!=-1||
								 strCurrentSheetName.indexOf("小学D组")!=-1||
								 strCurrentSheetName.indexOf("小学E组")!=-1){
							for(int j = 8; j < 13; j++){
								String[] competitionArray1 = competitionArray.split(",");
							 if(!(competitionArray1[j-8].equals("")||competitionArray1[j-8].equals(null))){
								for (int i = 0; i < list.size(); i++) {
									if (!(list.get(i)[j].trim().equals("") || list.get(i)[j].trim().equals(null))) {
										str = list.get(i)[2] + "-" + list.get(i)[3]+"-"+ competitionArray1[j-8]+";";
										strCurrentSheetName=strCurrentSheetName + competitionArray1[j-8];
										CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
										addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
												competition_type, filename, competitionInfo);
										strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
									}
								}
							 }
						   }
						}else if(strCurrentSheetName.indexOf("小学F组")!=-1){
							for(int j = 8; j < 14; j++){
								String[] competitionArray1 = competitionArray.split(",");
							 if(!(competitionArray1[j-8].equals("")||competitionArray1[j-8].equals(null))){
								for (int i = 0; i < list.size(); i++) {
									if (!(list.get(i)[j].trim().equals("") || list.get(i)[j].trim().equals(null))) {
										str = list.get(i)[2] + "-" + list.get(i)[3]+"-"+ competitionArray1[j-8]+";";
										strCurrentSheetName=strCurrentSheetName + competitionArray1[j-8];
										CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
										addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
												competition_type, filename, competitionInfo);
										strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
									}
								}
							 }
						   }
						}
					}
				}
			}
//
//						// 添加运动员队伍信息到数据库
//						CompetitionName competitonName = (CompetitionName) competition.findById(CompetitionName.class,
//								competition_id);
//						addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
//								competition_type, filename, competitionInfo);
//						}
//					}
//							
//				}
		

			SignTableEntity st = signTableService.queryObjectByFilePath(filename);
			if (log.isEmpty()) {
				st.setCheckStatus(1);
				signTableService.update(st);
				response.getWriter().write("<script>alert('审核通过')</script>");
				return;
			} else {
				st.setCheckStatus(0);
				signTableService.update(st);
				String s = "";
				for (int i = 0; i < log.size(); i++) {
					s = s + log.get(i) + ";    ";
				}
				response.getWriter().write("<script>alert('" + s + "')</script>");
				return;
			}
		}catch (Exception e) {
			response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
			e.printStackTrace();
		}
	}
		
	// 解析跆拳道
	public void receiveExcelForTaekwondo(HttpServletRequest request, HttpServletResponse response,
			String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
			String company) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");

		try {
			String filePath = null;
			String filename = fileName;
			Date upload = today;
			int competition_id = Integer.parseInt(competition_id1);
			String frontUserId = front_user_id;
			filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
			System.out.println(filePath);
			List<String> log = new ArrayList<>();
			ExcelUtil excelutil = new ExcelUtil();

			int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
			List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
			for (int number = 0; number < iSheetQuanitity; number++) {
				// 得到运动员数据
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				int[] numberArray = new int[6];
				List<String[]> list = new ArrayList<String[]>();
				list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
				for (int i = 0; i < list.size(); i++) {
					for (int j = 8; j < list.get(i).length; j++) {
						System.out.println("llllllllllll"+list.get(i)[j]);
						if (!list.get(i)[j].equals("")) {
							if (list.get(i)[j].trim().equals("√")) {
								numberArray[j - 8]++;
							}else{
								response.getWriter().write("<script>alert('请勾选项目时用“√”。')</script>");
								return;
							}
						}
					}
				}
				for (int i = 0; i < numberArray.length; i++) {
					if (numberArray[i] > 2) {
						response.getWriter().write("<script>alert('各个级别的运动运限报2名。')</script>");
						return;
					}
				}
				// 得到所以合并单元格的数据
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println("kkkkkkkkkkkk"+list1.get(i));
				}
				// 判断报名日期是否正确
				if (isApplyTime(response, competition_id, formatter1, upload)) {
					return;
				}

				// 判断人数是否正确
				if (isAtheteNum(response, competition_id, filePath, number, list, strCurrentSheetName)) {
					return;
				}

				for (int i = 0; i < list.size(); i++) {
					//判断性别是否符合
					log = isSex(list, i, log, strCurrentSheetName);
					//判断是否是注册运动员
					log = isAthlete(list, i, log);
					//判断运动员年龄是否符合
					log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
				}
				//判断同一张报名表中参赛人员是否重复
				String[] s = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					log = Check(list, i, log, s);
				}
			}

			for (int number = 0; number < iSheetQuanitity; number++) {
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				String str = "";
				String competitionArray = null;
				List<String[]> list = new ArrayList<String[]>();
				list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
				if (strCurrentSheetName.indexOf("甲组男") != -1) {
					competitionArray = "44kg,48kg,52kg,56kg,56kg+";
				}
				if (strCurrentSheetName.indexOf("甲组女") != -1) {
					competitionArray = "40kg,44kg,48kg,52kg,52kg+";
				}
				if (strCurrentSheetName.indexOf("乙组男") != -1) {
					competitionArray = "36kg,40kg,44kg,50kg,50kg+";
				}
				if (strCurrentSheetName.indexOf("乙组女") != -1) {
					competitionArray = "34kg,38kg,42kg,47kg,46kg+";
				}
				if (strCurrentSheetName.indexOf("丙组男") != -1) {
					competitionArray = "26kg,30kg,34kg,38kg,38kg+";
				}
				if (strCurrentSheetName.indexOf("丙组女") != -1) {
					competitionArray = "24kg,28kg,32kg,36kg,36kg+";
				}

				System.out.println(list.size());
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < list.get(i).length; j++) {
						System.out.println("aaaaaaa" + list.get(i)[j]);
					}
				}
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				String phone = excelutil.onecell(filePath, number, 3, 7, false);
				System.out.println(phone);

				// 得到教练
				String capain = null;
				capain = capainSet(capain, list1);

				if (log.isEmpty()) {
					System.out.println("log为空");
					if (!list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							addAthlete(i, list, list1, formatter2, company, phone);
						}

						for (int i = 0; i < list.size(); i++) {
							String[] competitionArray1 = competitionArray.split(",");
							str = str + list.get(i)[2] + "-" + list.get(i)[3];
							for (int j = 8; j < list.get(i).length; j++) {
								if (!(list.get(i)[j].trim().equals("") || list.get(i)[j] == null)) {
									str = str + "-" + competitionArray1[j - 8];
								}
							}
							str = str + ";";
						}

						// 添加运动员队伍信息到数据库
						CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
						addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
								competition_type, filename, competitionInfo);
						
					}

				}
			}

			SignTableEntity st = signTableService.queryObjectByFilePath(filename);
			if (log.isEmpty()) {
				st.setCheckStatus(1);
				signTableService.update(st);
				response.getWriter().write("<script>alert('审核通过')</script>");
				return;
			} else {
				st.setCheckStatus(0);
				signTableService.update(st);
				String s = "";
				for (int i = 0; i < log.size(); i++) {
					s = s + log.get(i) + ";    ";
				}
				response.getWriter().write("<script>alert('" + s + "')</script>");
				return;
			}

		} catch (Exception e) {
			response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
			e.printStackTrace();
		}

	}
	
	
	// 解析皮划艇、赛艇
	public void receiveExcelForPihuating(HttpServletRequest request, HttpServletResponse response,
			String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
			String company) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
		try {
			String filePath = null;
			String filename = fileName;
			Date upload = today;
			int competition_id = Integer.parseInt(competition_id1);
			String frontUserId = front_user_id;
			filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
			System.out.println(filePath);
			List<String> log = new ArrayList<>();
			ExcelUtil excelutil = new ExcelUtil();

			int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
			List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
			for (int number = 0; number < iSheetQuanitity; number++) {
				// 得到运动员数据
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				int[] numberArray = new int[6];
				List<String[]> list = new ArrayList<String[]>();
				if (strCurrentSheetName.indexOf("皮划艇甲组男")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 20, true);
				}
				if (strCurrentSheetName.indexOf("皮划艇甲组女")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 14, true);
				}
				if (strCurrentSheetName.indexOf("皮划艇乙组男")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 18, true);
				}
				if (strCurrentSheetName.indexOf("皮划艇乙组女")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
				}
				if (strCurrentSheetName.indexOf("皮划艇丙组男")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 11, true);
				}
				if (strCurrentSheetName.indexOf("皮划艇丙组女")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
				}
				if (strCurrentSheetName.indexOf("赛艇甲组男")!=-1||strCurrentSheetName.indexOf("赛艇甲组女")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 17, true);
				}
				if (strCurrentSheetName.indexOf("赛艇乙组男")!=-1||strCurrentSheetName.indexOf("赛艇乙组女")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 14, true);
				}
				if (strCurrentSheetName.indexOf("赛艇丙组男")!=-1||strCurrentSheetName.indexOf("赛艇丙组女")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
				}
				for (int i = 0; i < list.size(); i++) {
					for (int j = 8; j < list.get(i).length; j++) {
						System.out.println(list.get(i)[j]);
						if (!list.get(i)[j].equals("")) {
							if (!list.get(i)[j].trim().equals("√")) {
								response.getWriter().write("<script>alert('请勾选项目时用“√”。')</script>");
								return;
							}
						}
					}
				}
				// 得到所以合并单元格的数据
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				// 判断报名日期是否正确
				if (isApplyTime(response, competition_id, formatter1, upload)) {
					return;
				}

				// 判断人数是否正确
				if (isAtheteNum(response, competition_id, filePath, number, list, strCurrentSheetName)) {
					return;
				}

				for (int i = 0; i < list.size(); i++) {
					//判断性别是否符合
					log = isSex(list, i, log, strCurrentSheetName);
					//判断是否是注册运动员
					log = isAthlete(list, i, log);
					//判断运动员年龄是否符合
					log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
				}
				//判断同一张报名表中参赛人员是否重复
				String[] s = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					log = Check(list, i, log, s);
				}
			}
			
			for (int number = 0; number < iSheetQuanitity; number++) {
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				String str = "";
				String competitionArray = null;
				List<String[]> list = new ArrayList<String[]>();
				list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
				if (strCurrentSheetName.indexOf("皮划艇甲组男")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 20, true);
					competitionArray = "200mK1,1000mK1,2000K1,200mK2,1000mK2,2000K2,200mC1,1000mC1,2000C1,200mC2,1000mC2,2000C2";
				}
				if (strCurrentSheetName.indexOf("皮划艇甲组女")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 14, true);
					competitionArray = "200mK1,500mK1,2000K1,200mK2,500mK2,2000K2";
				}
				if (strCurrentSheetName.indexOf("皮划艇乙组男")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 18, true);
					competitionArray = "200mK1,200mC1,1000mK1,2000K1,1000mK2,2000K2,1000mC1,2000C1,1000mC2,2000C2";
				}
				if (strCurrentSheetName.indexOf("皮划艇乙组女")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
					competitionArray = "500mK1,2000K1,500mK2,2000K2";
				}
				if (strCurrentSheetName.indexOf("皮划艇丙组男")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 11, true);
					competitionArray = "2000K1,2000K2,2000C1";
				}
				if (strCurrentSheetName.indexOf("皮划艇丙组女")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
					competitionArray = "2000K1,2000K2";
				}
				if (strCurrentSheetName.indexOf("赛艇甲组男")!=-1||strCurrentSheetName.indexOf("赛艇甲组女")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 17, true);
					competitionArray = "1000m1X,2000m1X,6000m1X,1000m2X,2000m2X,6000m2X,1000m2-,2000m2-,6000m2-";
				}
				if (strCurrentSheetName.indexOf("赛艇乙组男")!=-1||strCurrentSheetName.indexOf("赛艇乙组女")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 14, true);
					competitionArray = "2000m1X,6000m1X,2000m2X,6000m2X,2000m2-,6000m2-";
				}
				if (strCurrentSheetName.indexOf("赛艇丙组男")!=-1||strCurrentSheetName.indexOf("赛艇丙组女")!=-1) {
					list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
					competitionArray = "2000m1X,4000m1X,2000m2X,4000m2X";
				}
				System.out.println(list.size());
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < list.get(i).length; j++) {
						System.out.println("aaaaaaa" + list.get(i)[j]);
					}
				}
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				String phone = excelutil.onecell(filePath, number, 3, 7, false);
				System.out.println(phone);

				// 得到教练
				String capain = null;
				capain = capainSet(capain, list1);

				if (log.isEmpty()) {
					System.out.println("log为空");
					if (!list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							addAthlete(i, list, list1, formatter2, company, phone);
						}

						for (int i = 0; i < list.size(); i++) {
							String[] competitionArray1 = competitionArray.split(",");
							str = str + list.get(i)[2] + "-" + list.get(i)[3];
							for (int j = 8; j < list.get(i).length; j++) {
								if (!(list.get(i)[j].trim().equals("") || list.get(i)[j] == null)) {
									str = str + "-" + competitionArray1[j - 8];
								}
							}
							str = str + ";";
						}

						// 添加运动员队伍信息到数据库
						CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
						addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
								competition_type, filename, competitionInfo);
						
					}

				}
			}

			SignTableEntity st = signTableService.queryObjectByFilePath(filename);
			if (log.isEmpty()) {
				st.setCheckStatus(1);
				signTableService.update(st);
				response.getWriter().write("<script>alert('审核通过')</script>");
				return;
			} else {
				st.setCheckStatus(0);
				signTableService.update(st);
				String s = "";
				for (int i = 0; i < log.size(); i++) {
					s = s + log.get(i) + ";    ";
				}
				response.getWriter().write("<script>alert('" + s + "')</script>");
				return;
			}
		}catch (Exception e) {
			response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
			e.printStackTrace();
		}
	}
	
	
	// 解析网球
	public void receiveExcelForWangqiu(HttpServletRequest request, HttpServletResponse response,
			String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
			String company) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
		try {
			String filePath = null;
			String filename = fileName;
			Date upload = today;
			int competition_id = Integer.parseInt(competition_id1);
			String frontUserId = front_user_id;
			filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
			System.out.println(filePath);
			List<String> log = new ArrayList<>();
			ExcelUtil excelutil = new ExcelUtil();

			int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
			List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
			for (int number = 0; number < iSheetQuanitity; number++) {
				// 得到运动员数据
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				int[] numberArray = new int[6];
				List<String[]> list = new ArrayList<String[]>();
				list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
				int tuantixiangmu=0;
				for (int i = 0; i < list.size(); i++) {
					for (int j = 8; j < list.get(i).length; j++) {
						System.out.println(list.get(i)[j]);
						if (!list.get(i)[j].equals("")) {
							if (!list.get(i)[j].trim().equals("√")) {
								response.getWriter().write("<script>alert('请勾选项目时用“√”。')</script>");
								return;
							}else{
								if(j==8){
									tuantixiangmu++;
								}else{
									if(strCurrentSheetName.indexOf("网球")!=-1){
										response.getWriter().write("<script>alert('网球只能选团体。')</script>");
										return;
									}
								}
							}
						}
					}
				}
				
				// 得到所以合并单元格的数据
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				// 判断报名日期是否正确
				if (isApplyTime(response, competition_id, formatter1, upload)) {
					return;
				}
				// 判断人数是否正确
				if (isAtheteNum(response, competition_id, filePath, number, list, strCurrentSheetName)) {
					return;
				}
				
				for (int i = 0; i < list.size(); i++) {
					//判断性别是否符合
					log = isSex(list, i, log, strCurrentSheetName);
					//判断运动员年龄是否符合
					log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
				}
				//判断同一张报名表中参赛人员是否重复
				String[] s = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					log = Check(list, i, log, s);
				}
				
				if(!list.isEmpty() && strCurrentSheetName.indexOf("团体")!=-1){
					if(tuantixiangmu<3){
						response.getWriter().write("<script>alert('团体赛至少3人报名。')</script>");
						return;
					}
				}
			}
			for (int number = 0; number < iSheetQuanitity; number++) {
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				String str = "";
				String competitionArray = null;
				List<String[]> list = new ArrayList<String[]>();
				list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
			    competitionArray = "团体,双打,单打";
				System.out.println(list.size());
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < list.get(i).length; j++) {
						System.out.println("aaaaaaa" + list.get(i)[j]);
					}
				}
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				String phone = excelutil.onecell(filePath, number, 3, 7, false);
				System.out.println(phone);

				// 得到教练
				String capain = null;
				capain = capainSet(capain, list1);

				if (log.isEmpty()) {
					System.out.println("log为空");
					if (!list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							addAthlete(i, list, list1, formatter2, company, phone);
						}

						for (int i = 0; i < list.size(); i++) {
							String[] competitionArray1 = competitionArray.split(",");
							str = str + list.get(i)[2] + "-" + list.get(i)[3];
							for (int j = 8; j < list.get(i).length; j++) {
								if (!(list.get(i)[j].trim().equals("") || list.get(i)[j] == null)) {
									str = str + "-" + competitionArray1[j - 8];
								}
							}
							str = str + ";";
						}

						// 添加运动员队伍信息到数据库
						CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
						addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
								competition_type, filename, competitionInfo);
						
					}

				}
			}
			
			SignTableEntity st = signTableService.queryObjectByFilePath(filename);
			if (log.isEmpty()) {
				st.setCheckStatus(1);
				signTableService.update(st);
				response.getWriter().write("<script>alert('审核通过')</script>");
				return;
			} else {
				st.setCheckStatus(0);
				signTableService.update(st);
				String s = "";
				for (int i = 0; i < log.size(); i++) {
					s = s + log.get(i) + ";    ";
				}
				response.getWriter().write("<script>alert('" + s + "')</script>");
				return;
			}
			
		}catch (Exception e) {
			response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
			e.printStackTrace();
		}
		
	}
	//解析特色项目网球
	public void receiveExcelForTesewangqiu(HttpServletRequest request, HttpServletResponse response,
			String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
			String company) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
		try {
			String filePath = null;
			String filename = fileName;
			Date upload = today;
			int competition_id = Integer.parseInt(competition_id1);
			String frontUserId = front_user_id;
			filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
			System.out.println(filePath);
			List<String> log = new ArrayList<>();
			List<String> logs = new ArrayList<>();
			ExcelUtil excelutil = new ExcelUtil();

			int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
			List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
			for (int number = 0; number < iSheetQuanitity; number++) {
				// 得到运动员数据
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				int[] numberArray = new int[6];
				List<String[]> list = new ArrayList<String[]>();
				list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
				int tuantixiangmu=0;
				for (int i = 0; i < list.size(); i++) {
					for (int j = 8; j < list.get(i).length; j++) {
						System.out.println(list.get(i)[j]);
						if (!list.get(i)[j].equals("")) {
							if (!list.get(i)[j].trim().equals("√")) {
								response.getWriter().write("<script>alert('请勾选项目时用“√”。')</script>");
								return;
							}else{
								if(j==8){
									tuantixiangmu++;
								}else{
									if(strCurrentSheetName.indexOf("特色项目网球")!=-1){
										response.getWriter().write("<script>alert('网球只能选团体。')</script>");
										return;
									}
								}
							}
						}
					}
				}
				
				// 得到所以合并单元格的数据
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				// 判断报名日期是否正确
				if (isApplyTime(response, competition_id, formatter1, upload)) {
					return;
				}
				// 判断人数是否正确
				if (isAtheteNum(response, competition_id, filePath, number, list, strCurrentSheetName)) {
					return;
				}
				
				for (int i = 0; i < list.size(); i++) {
					//判断性别是否符合
					log = isSex(list, i, log, strCurrentSheetName);
					//判断运动员年龄是否符合
					log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
				}

				//判断同一张报名表中参赛人员是否重复
				String[] s = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					log = Check(list, i, log, s);
				}
				
				if(!list.isEmpty()){
					if(tuantixiangmu<3){
						response.getWriter().write("<script>alert('团体赛至少3人报名。')</script>");
						return;
					}
				}
			}
			for (int number = 0; number < iSheetQuanitity; number++) {
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				String str = "";
				String competitionArray = null;
				List<String[]> list = new ArrayList<String[]>();
				list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
			    competitionArray = "团体,单打";
				System.out.println(list.size());
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < list.get(i).length; j++) {
						System.out.println("bbbbbbb" + list.get(i)[j]);
					}
				}
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				String phone = excelutil.onecell(filePath, number, 3, 7, false);
				System.out.println(phone);

				// 得到教练
				String capain = null;
				capain = capainSet(capain, list1);

				if (log.isEmpty()) {
					System.out.println("log为空");
					if (!list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							addAthlete(i, list, list1, formatter2, company, phone);
						}

						for (int i = 0; i < list.size(); i++) {
							String[] competitionArray1 = competitionArray.split(",");
							str = str + list.get(i)[2] + "-" + list.get(i)[3];
							for (int j = 8; j < list.get(i).length; j++) {
								if (!(list.get(i)[j].trim().equals("") || list.get(i)[j] == null)) {
									str = str + "-" + competitionArray1[j - 8];
								}
							}
							str = str + ";";
						}

						// 添加运动员队伍信息到数据库
						CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
						addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
								competition_type, filename, competitionInfo);
						
					}

				}
			}

			SignTableEntity st = signTableService.queryObjectByFilePath(filename);
			if (log.isEmpty()) {
				st.setCheckStatus(1);
				signTableService.update(st);
				response.getWriter().write("<script>alert('审核通过')</script>");
				return;
			} else {
				st.setCheckStatus(0);
				signTableService.update(st);
				String s = "";
				for (int i = 0; i < log.size(); i++) {
					s = s + log.get(i) + ";    ";
				}
				response.getWriter().write("<script>alert('" + s + "')</script>");
				return;
			}
			
		}catch (Exception e) {
			response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
			e.printStackTrace();
		}
		
		
	}
	
	
	// 解析田径
	public void receiveExcelForTianjing(HttpServletRequest request, HttpServletResponse response,
			String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
			String company) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
		try{
			String filePath = null;
			String filename = fileName;
			Date upload = today;
			int competition_id = Integer.parseInt(competition_id1);
			String frontUserId = front_user_id;
			filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
			System.out.println(filePath);
			List<String> log = new ArrayList<>();
			ExcelUtil excelutil = new ExcelUtil();

			int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
			List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
			for (int number = 0; number < iSheetQuanitity; number++) {
				// 得到运动员数据
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				List<String[]> list = new ArrayList<String[]>();
				int xiangmuNum=0;
				if(strCurrentSheetName.indexOf("田径高中组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 32, true);
					xiangmuNum=24;
				}
				if(strCurrentSheetName.indexOf("田径初中组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 32, true);
					xiangmuNum=24;
				}
				if(strCurrentSheetName.indexOf("田径小学甲组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 28, true);
					xiangmuNum=20;
				}
				if(strCurrentSheetName.indexOf("田径小学乙组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					xiangmuNum=7;
				}
				
				int[] athletexiangmus = new int[list.size()]; 	   //运动员报的项目
				int[] xiangmu=new int[xiangmuNum];	               //工作表所有运动员的项目
				int yizunan=0;						               //乙组男子接力人数
				int yizunv=0;						               //乙组女子接力人数
				int jielinannum=0;						           //接力男人数
				int jielinvnum=0;						           //接力女人数
				for (int i = 0; i < list.size(); i++) {
					athletexiangmus[i]=0;
					for (int j = 8; j < list.get(i).length; j++) {
						String sex = list.get(i)[2].substring(16,17);
						if (!list.get(i)[j].equals("")) {
							if (!list.get(i)[j].trim().equals("√")) {
								response.getWriter().write("<script>alert('请勾选项目时用“√”。')</script>");
								return;
							}else if(strCurrentSheetName.indexOf("田径小学乙组")!=-1){
								if(j>8){
									athletexiangmus[i]++;
									xiangmu[j-8]++;
								}else{
										if(("1").equals(sex)||("3").equals(sex)||("5").equals(sex)||("7").equals(sex)||("9").equals(sex)){
											yizunan++;
										}
										if(("0").equals(sex)||("2").equals(sex)||("4").equals(sex)||("6").equals(sex)||("8").equals(sex)){
											yizunv++;
										}
								}
							}else{
								if(j>9){
									athletexiangmus[i]++;
									xiangmu[j-8]++;
								}else{
									if(("1").equals(sex)||("3").equals(sex)||("5").equals(sex)||("7").equals(sex)||("9").equals(sex)){
										jielinannum++;
									}
									if(("0").equals(sex)||("2").equals(sex)||("4").equals(sex)||("6").equals(sex)||("8").equals(sex)){
									     jielinvnum++;
									}
								}
							}
						}
					}
				}
				
				// 得到所以合并单元格的数据
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				// 判断报名日期是否正确
				if (isApplyTime(response, competition_id, formatter1, upload)) {
					return;
				}

				// 判断人数是否正确
				if(list.size()>12){
					response.getWriter().write("<script>alert('" + strCurrentSheetName + "不得多于12人')</script>");
				}
				
				//判断同一张报名表中参赛人员是否重复
				String[] s = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					log = Check(list, i, log, s);
				}
				
				//判断运动员报的项目是否符合
				for (int i = 0; i < list.size(); i++) {
				    if(athletexiangmus[i]>2){
					    response.getWriter().write("<script>alert('运动员限报两项单项比赛和接力比赛。')</script>");
					    return;
				    }
				}
				
				//判断各单项比赛是否超过3人
				for(int i=0;i<xiangmuNum;i++){
					if(xiangmu[i]>3){
						response.getWriter().write("<script>alert('单项比赛不能超过3人。')</script>");
						return;
					}
				}
				
				//判断乙组接力人数
				System.out.println(strCurrentSheetName);
				if(strCurrentSheetName.indexOf("田径小学乙组")!=-1){
					if(!(yizunan%5==0&&yizunv%5==0)){
						response.getWriter().write("<script>alert('小学乙组接力要男女各5人。')</script>");
						return;
					}
				}
				
				//判断乙组以外其他组接力人数
				if(jielinannum%4!=0){
					response.getWriter().write("<script>alert('"+strCurrentSheetName+"男子接力要4人。')</script>");
					return;
				}
				if(jielinvnum%4!=0){
					response.getWriter().write("<script>alert('"+strCurrentSheetName+"女子接力要4人。')</script>");
					return;
				}
				
			}
			//判断年龄是否符合
			for (int number = 0; number < iSheetQuanitity; number++) {
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				String str = "";
				String competitionArray = null;
				List<String[]> list = new ArrayList<String[]>();
			    if(strCurrentSheetName.indexOf("田径高中组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 32, true);
					competitionArray="4*100接力(女),4*100接力(男),100m(女),100m(男),200m(女),200m(男),400m(女),400m(男),800m(女),1500m(男),100m栏(女),110m栏(男),跳高(女),跳高(男),跳远(女),跳远(男),三级跳远(女),三级跳远(男),铅球(女),铅球(男),铁饼(女),铁饼(男),标枪(女),标枪(男)";
				}
				if(strCurrentSheetName.indexOf("田径初中组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 32, true);
					competitionArray="4*100接力(女),4*100接力(男),100m(女),100m(男),200m(女),200m(男),400m(女),400m(男),800m(女),1500m(男),100m栏(女),110m栏(男),跳高(女),跳高(男),跳远(女),跳远(男),三级跳远(女),三级跳远(男),铅球(女),铅球(男),铁饼(女),铁饼(男),标枪(女),标枪(男)";
				}
				if(strCurrentSheetName.indexOf("田径小学甲组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 28, true);
					competitionArray="4*100接力(女),4*100接力(男),60m栏(女),60m栏(男),100m(女),100m(男),200m(女),200m(男),400m(女),400m(男),800m(女),800m(男),跳高(女),跳高(男),跳远(女),跳远(男),铅球(女),铅球(男),垒球(女),垒球(男)";
				}
				if(strCurrentSheetName.indexOf("田径小学乙组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					competitionArray="男女混合50m绕杆接力,立定三级跳(女),立定三级跳(男),双飞跳绳(女),双飞跳绳(男),后抛实心球(女),后抛实心球(男)";
				}
				
				String[] competitionArray1 = competitionArray.split(",");
				if(strCurrentSheetName.indexOf("高中组")!=-1||strCurrentSheetName.indexOf("初中组")!=-1){
					for(int j = 8; j < 32; j++){
						if(!(competitionArray1[j-8].equals("")||competitionArray1[j-8].equals(null))){
							for (int i = 0; i < list.size(); i++) {
								if (!(list.get(i)[j].trim().equals("") || list.get(i)[j].trim().equals(null))) {
										strCurrentSheetName=strCurrentSheetName + competitionArray1[j-8];
										log = isError4(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
										strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
								}
							}
						}
					}
				}else if(strCurrentSheetName.indexOf("小学甲组")!=-1){
					for(int j = 8; j < 28; j++){
						if(!(competitionArray1[j-8].equals("")||competitionArray1[j-8].equals(null))){
							for (int i = 0; i < list.size(); i++) {
								if (!(list.get(i)[j].trim().equals("") || list.get(i)[j].trim().equals(null))) {
										strCurrentSheetName=strCurrentSheetName + competitionArray1[j-8];
										log = isError4(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
										strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
								}
							}
						}
					}
				}else if(strCurrentSheetName.indexOf("小学乙组")!=-1){
					for(int j = 8; j < 15; j++){
						if(!(competitionArray1[j-8].equals("")||competitionArray1[j-8].equals(null))){
							for (int i = 0; i < list.size(); i++) {
								if (!(list.get(i)[j].trim().equals("") || list.get(i)[j].trim().equals(null))) {
										strCurrentSheetName=strCurrentSheetName + competitionArray1[j-8];
										log = isError4(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
										strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
									}
							}
						}
					}
				}
			}
			
			for (int number = 0; number < iSheetQuanitity; number++) {
				String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
				String str = "";
				String competitionArray = null;
				List<String[]> list = new ArrayList<String[]>();
			    if(strCurrentSheetName.indexOf("田径高中组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 32, true);
					competitionArray="4*100接力(女),4*100接力(男),100m(女),100m(男),200m(女),200m(男),400m(女),400m(男),800m(女),1500m(男),100m栏(女),110m栏(男),跳高(女),跳高(男),跳远(女),跳远(男),三级跳远(女),三级跳远(男),铅球(女),铅球(男),铁饼(女),铁饼(男),标枪(女),标枪(男)";
				}
				if(strCurrentSheetName.indexOf("田径初中组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 32, true);
					competitionArray="4*100接力(女),4*100接力(男),100m(女),100m(男),200m(女),200m(男),400m(女),400m(男),800m(女),1500m(男),100m栏(女),110m栏(男),跳高(女),跳高(男),跳远(女),跳远(男),三级跳远(女),三级跳远(男),铅球(女),铅球(男),铁饼(女),铁饼(男),标枪(女),标枪(男)";
				}
				if(strCurrentSheetName.indexOf("田径小学甲组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 28, true);
					competitionArray="4*100接力(女),4*100接力(男),60m栏(女),60m栏(男),100m(女),100m(男),200m(女),200m(男),400m(女),400m(男),800m(女),800m(男),跳高(女),跳高(男),跳远(女),跳远(男),铅球(女),铅球(男),垒球(女),垒球(男)";
				}
				if(strCurrentSheetName.indexOf("田径小学乙组")!=-1){
					list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
					competitionArray="男女混合50m绕杆接力,立定三级跳(女),立定三级跳(男),双飞跳绳(女),双飞跳绳(男),后抛实心球(女),后抛实心球(男)";
				}
				
				System.out.println(list.size());
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < list.get(i).length; j++) {
						System.out.println("aaaaaaa" + list.get(i)[j]);
					}
				}
				List<String> list1 = excelutil.execelpoi(filePath, number, 0);
				for (int i = 0; i < list1.size(); i++) {
					System.out.println(list1.get(i));
				}
				String phone = excelutil.onecell(filePath, number, 3, 7, false);
				System.out.println(phone);

				// 得到教练
				String capain = null;
				capain = capainSet(capain, list1);

				if (log.isEmpty()) {
					System.out.println("log为空");
					if (!list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							addAthlete(i, list, list1, formatter2, company, phone);
						}

					//添加运动员队伍信息到数据库
					int count=0; 
					if(strCurrentSheetName.indexOf("高中组")!=-1||strCurrentSheetName.indexOf("初中组")!=-1){
						for(int j = 8; j < 32; j++){
							String[] competitionArray1 = competitionArray.split(",");
							if(competitionArray1[j-8].indexOf("接力")!=-1){
								for (int i = 0; i < list.size(); i++) {
									if (!(list.get(i)[j].trim().equals("") || list.get(i)[j].trim().equals(null))) {
										str = str + list.get(i)[2] + "-" + list.get(i)[3]+"-"+ competitionArray1[j-8]+";";
										count++;
										if(count==4){
											strCurrentSheetName=strCurrentSheetName + competitionArray1[j-8];
											CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
											addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
													competition_type, filename, competitionInfo);
											str="";
											strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
											count=0;
										}
									}
								}
							}else if(!(competitionArray1[j-8].equals("")||competitionArray1[j-8].equals(null))){
								for (int i = 0; i < list.size(); i++) {
									if (!(list.get(i)[j].trim().equals("") || list.get(i)[j].trim().equals(null))) {
										str = list.get(i)[2] + "-" + list.get(i)[3]+"-"+ competitionArray1[j-8]+";";
										strCurrentSheetName=strCurrentSheetName + competitionArray1[j-8];
										CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
										addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
												competition_type, filename, competitionInfo);
										strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
										}
								}
							}
						}
					}else if(strCurrentSheetName.indexOf("小学甲组")!=-1){
						for(int j = 8; j < 28; j++){
							String[] competitionArray1 = competitionArray.split(",");
							if(competitionArray1[j-8].indexOf("接力")!=-1){
								for (int i = 0; i < list.size(); i++) {
									if (!(list.get(i)[j].trim().equals("") || list.get(i)[j].trim().equals(null))) {
										str = str + list.get(i)[2] + "-" + list.get(i)[3]+"-"+ competitionArray1[j-8]+";";
										count++;
										if(count==4){
											strCurrentSheetName=strCurrentSheetName + competitionArray1[j-8];
											CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
											addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
													competition_type, filename, competitionInfo);
											str="";
											strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
											count=0;
										}
									}
								}
							}else if(!(competitionArray1[j-8].equals("")||competitionArray1[j-8].equals(null))){
								for (int i = 0; i < list.size(); i++) {
									if (!(list.get(i)[j].trim().equals("") || list.get(i)[j].trim().equals(null))) {
										str = list.get(i)[2] + "-" + list.get(i)[3]+"-"+ competitionArray1[j-8]+";";
										strCurrentSheetName=strCurrentSheetName + competitionArray1[j-8];
										CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
										addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
												competition_type, filename, competitionInfo);
										strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
										}
								}
							}
						}
					}else if(strCurrentSheetName.indexOf("小学乙组")!=-1){
						for(int j = 8; j < 15; j++){
							String[] competitionArray1 = competitionArray.split(",");
							if(competitionArray1[j-8].indexOf("接力")!=-1){
								for (int i = 0; i < list.size(); i++) {
									if (!(list.get(i)[j].trim().equals("") || list.get(i)[j].trim().equals(null))) {
										str = str + list.get(i)[2] + "-" + list.get(i)[3]+"-"+ competitionArray1[j-8]+";";
										count++;
										if(count==10){
											strCurrentSheetName=strCurrentSheetName + competitionArray1[j-8];
											CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
											addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
													competition_type, filename, competitionInfo);
											str="";
											strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
											count=0;
										}
									}
								}
							}else if(!(competitionArray1[j-8].equals("")||competitionArray1[j-8].equals(null))){
								for (int i = 0; i < list.size(); i++) {
									if (!(list.get(i)[j].trim().equals("") || list.get(i)[j].trim().equals(null))) {
										str = list.get(i)[2] + "-" + list.get(i)[3]+"-"+ competitionArray1[j-8]+";";
										strCurrentSheetName=strCurrentSheetName + competitionArray1[j-8];
										CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
										addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
												competition_type, filename, competitionInfo);
										strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
										}
								}
							}
						}
					}
							
				}
			}
		}

			SignTableEntity st = signTableService.queryObjectByFilePath(filename);
			if (log.isEmpty()) {
				st.setCheckStatus(1);
				signTableService.update(st);
				response.getWriter().write("<script>alert('审核通过')</script>");
				return;
			} else {
				st.setCheckStatus(0);
				signTableService.update(st);
				String s = "";
				for (int i = 0; i < log.size(); i++) {
					s = s + log.get(i) + ";    ";
				}
				response.getWriter().write("<script>alert('" + s + "')</script>");
				return;
			}
		}catch (Exception e) {
			response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
			e.printStackTrace();
		}
	}
	
	
		//解析举重
		public void receiveExcelForWeightlifting(HttpServletRequest request, HttpServletResponse response,
				String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
				String company) throws IOException {
				response.setContentType("text/html;charset=utf-8");
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
				SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
				try{
					String filePath = null;
					String filename = fileName;
					Date upload = today;
					int competition_id = Integer.parseInt(competition_id1);
					String frontUserId = front_user_id;
					filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
					System.out.println(filePath);
					List<String> log = new ArrayList<>();
					ExcelUtil excelutil = new ExcelUtil();

					int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
					int[] numberArray = new int[6];
					List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
					for (int number = 0; number < iSheetQuanitity; number++) {
						// 得到运动员数据
						String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
						List<String[]> list = new ArrayList<String[]>();
						if(strCurrentSheetName.indexOf("举重男子甲组") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 14, true);
						}
						if(strCurrentSheetName.indexOf("举重男子乙组") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 15, true);
						}
						if(strCurrentSheetName.indexOf("举重男子丙组") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 14, true);
						}
						if(strCurrentSheetName.indexOf("举重女子甲组") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
						}
						if(strCurrentSheetName.indexOf("举重女子乙组") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 14, true);
						}
						if(strCurrentSheetName.indexOf("举重女子丙组") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
						}
						for (int i = 0; i < list.size(); i++) {
							for (int j = 8; j < list.get(i).length; j++) {
								if (!list.get(i)[j].equals("")) {
									if (list.get(i)[j].trim().equals("√")) {
										numberArray[j - 8]++;
									}else{
										response.getWriter().write("<script>alert('请勾选项目时用“√”。')</script>");
										return;
									}
								}
							}
						}
						for (int i = 0; i < numberArray.length; i++) {
							if (numberArray[i] > 2) {
								response.getWriter().write("<script>alert('各个级别的运动员限报2名。')</script>");
								return;
							}
						}
						// 得到所有合并单元格的数据
						List<String> list1 = excelutil.execelpoi(filePath, number, 0);
						for (int i = 0; i < list1.size(); i++) {
							System.out.println("kkkkkkkkkkkk"+list1.get(i));
						}
						// 判断报名日期是否正确
						if (isApplyTime(response, competition_id, formatter1, upload)) {
							return;
						}

						// 判断人数是否正确
						if (isAtheteNum(response, competition_id, filePath, number, list, strCurrentSheetName)) {
							return;
						}

						
						for (int i = 0; i < list.size(); i++) {
							//判断性别是否符合
							log = isSex(list, i, log, strCurrentSheetName);
							//判断是否是注册运动员
							log = isAthlete(list, i, log);
							//判断运动员年龄是否符合
							log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
						}
						//判断同一张报名表中参赛人员是否重复
						String[] s = new String[list.size()];
						for (int i = 0; i < list.size(); i++) {
							log = Check(list, i, log, s);
						}
					}

					for (int number = 0; number < iSheetQuanitity; number++) {
						String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
						String str = "";
						String competitionArray = null;
						List<String[]> list = new ArrayList<String[]>();
						list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
						if (strCurrentSheetName.indexOf("男子甲组") != -1) {
							competitionArray = "52kg,56kg,62kg,69kg,77kg,85kg,85kg+";
						}
						if (strCurrentSheetName.indexOf("女子甲组") != -1) {
							competitionArray = "48kg,53kg,58kg,63kg,69kg,69kg+";
						}
						if (strCurrentSheetName.indexOf("男子乙组") != -1) {
							competitionArray = "46kg,49kg,52kg,56kg,62kg,69kg,77kg,77kg+";
						}
						if (strCurrentSheetName.indexOf("女子乙组") != -1) {
							competitionArray = "42kg,45kg,48kg,53kg,58kg,63kg,63kg+";
						}
						if (strCurrentSheetName.indexOf("男子丙组") != -1) {
							competitionArray = "40kg,44kg,48kg,52kg,56kg,62kg,62kg+";
						}
						if (strCurrentSheetName.indexOf("女子丙组") != -1) {
							competitionArray = "36kg,40kg,44kg,48kg,53kg,58kg";
						}

						System.out.println(list.size());
						for (int i = 0; i < list.size(); i++) {
							for (int j = 0; j < list.get(i).length; j++) {
								System.out.println("aaaaaaa" + list.get(i)[j]);
							}
						}
						List<String> list1 = excelutil.execelpoi(filePath, number, 0);
						for (int i = 0; i < list1.size(); i++) {
							System.out.println(list1.get(i));
						}
						String phone = excelutil.onecell(filePath, number, 3, 7, false);
						System.out.println(phone);

						// 得到教练
						String capain = null;
						capain = capainSet(capain, list1);

						if (log.isEmpty()) {
							System.out.println("log为空");
							if (!list.isEmpty()) {
								for (int i = 0; i < list.size(); i++) {
									addAthlete(i, list, list1, formatter2, company, phone);
								}
								for (int i = 0; i < list.size(); i++) {
									String[] competitionArray1 = competitionArray.split(",");
									str = str + list.get(i)[2] + "-" + list.get(i)[3];
									for (int j = 8; j < list.get(i).length; j++) {
										if (!(list.get(i)[j].trim().equals("") || list.get(i)[j] == null)) {
											str = str + "-" + competitionArray1[j - 8] + ";";
											// 添加运动员队伍信息到数据库
											strCurrentSheetName=strCurrentSheetName + competitionArray1[j-8];
											CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
											addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
													competition_type, filename, competitionInfo);
											strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
											str="";
										}
									}
								}
							}
						}
					}

					SignTableEntity st = signTableService.queryObjectByFilePath(filename);
					if (log.isEmpty()) {
						st.setCheckStatus(1);
						signTableService.update(st);
						response.getWriter().write("<script>alert('审核通过')</script>");
						return;
					} else {
						st.setCheckStatus(0);
						signTableService.update(st);
						String s = "";
						for (int i = 0; i < log.size(); i++) {
							s = s + log.get(i) + ";    ";
						}
						response.getWriter().write("<script>alert('" + s + "')</script>");
						return;
					}		
			} catch (Exception e) {
				response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
				e.printStackTrace();
			}
		}		
				
		// 解析柔道
		public void receiveExcelForJudo(HttpServletRequest request, HttpServletResponse response,
				String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
				String company) throws IOException {
				response.setContentType("text/html;charset=utf-8");
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
				SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
				try{
					String filePath = null;
					String filename = fileName;
					Date upload = today;
					int competition_id = Integer.parseInt(competition_id1);
					String frontUserId = front_user_id;
					filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
					System.out.println(filePath);
					List<String> log = new ArrayList<>();
					ExcelUtil excelutil = new ExcelUtil();

					int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
					int[] numberArray = new int[6];
					List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
					for (int number = 0; number < iSheetQuanitity; number++) {
						// 得到运动员数据
						String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
						List<String[]> list = new ArrayList<String[]>();
						if(strCurrentSheetName.indexOf("柔道甲组男") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
						}
						if(strCurrentSheetName.indexOf("柔道乙组男") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 11, true);
						}
						if(strCurrentSheetName.indexOf("柔道丙组男") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 11, true);
						}
						if(strCurrentSheetName.indexOf("柔道甲组女") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
						}
						if(strCurrentSheetName.indexOf("柔道乙组女") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
						}
						if(strCurrentSheetName.indexOf("柔道乙组女") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 12, true);
						}
						for (int i = 0; i < list.size(); i++) {
							for (int j = 8; j < list.get(i).length; j++) {
								if (!list.get(i)[j].equals("")) {
									if (list.get(i)[j].trim().equals("√")) {
										numberArray[j - 8]++;
									}else{
										response.getWriter().write("<script>alert('请勾选项目时用“√”。')</script>");
										return;
									}
								}
							}
						}
//						for (int i = 0; i < numberArray.length; i++) {
//							if (numberArray[i] > 2) {
//								response.getWriter().write("<script>alert('各个级别的运动运限报2名。')</script>");
//								return;
//							}
//						}
						// 得到所有合并单元格的数据
						List<String> list1 = excelutil.execelpoi(filePath, number, 0);
//						for (int i = 0; i < list1.size(); i++) {
//							System.out.println("kkkkkkkkkkkk"+list1.get(i));
//						}
						// 判断报名日期是否正确
						if (isApplyTime(response, competition_id, formatter1, upload)) {
							return;
						}

						// 判断人数是否正确
						if (isAtheteNum(response, competition_id, filePath, number, list, strCurrentSheetName)) {
							return;
						}

						for (int i = 0; i < list.size(); i++) {
							//判断性别是否符合
							log = isSex(list, i, log, strCurrentSheetName);
							//判断是否是注册运动员
							log = isAthlete(list, i, log);
							//判断运动员年龄是否符合
							log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
						}
						//判断同一张报名表中参赛人员是否重复
						String[] s = new String[list.size()];
						for (int i = 0; i < list.size(); i++) {
							log = Check(list, i, log, s);
						}
					}

					for (int number = 0; number < iSheetQuanitity; number++) {
						String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
						String str = "";
						String competitionArray = null;
						List<String[]> list = new ArrayList<String[]>();
						list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
						if (strCurrentSheetName.indexOf("甲组男") != -1) {
							competitionArray = "-55kg,-66kg,66kg+";
						}
						if (strCurrentSheetName.indexOf("甲组女") != -1) {
							competitionArray = "-48kg,-57kg,57kg+";
						}
						if (strCurrentSheetName.indexOf("乙组男") != -1) {
							competitionArray = "-41kg,-48kg,-55kg,55kg+";
						}
						if (strCurrentSheetName.indexOf("乙组女") != -1) {
							competitionArray = "-40kg,-44kg,-48kg,-52kg,52kg+";
						}
						if (strCurrentSheetName.indexOf("丙组男") != -1) {
							competitionArray = "-35kg,-41kg,-50kg,50kg+";
						}
						if (strCurrentSheetName.indexOf("丙组女") != -1) {
							competitionArray = "-35kg,-40kg,-44kg,-48kg,50kg+";
						}

						System.out.println(list.size());
						for (int i = 0; i < list.size(); i++) {
							for (int j = 0; j < list.get(i).length; j++) {
								System.out.println("aaaaaaa" + list.get(i)[j]);
							}
						}
						List<String> list1 = excelutil.execelpoi(filePath, number, 0);
						for (int i = 0; i < list1.size(); i++) {
							System.out.println(list1.get(i));
						}
						String phone = excelutil.onecell(filePath, number, 3, 7, false);
						System.out.println(phone);

						// 得到教练
						String capain = null;
						capain = capainSet(capain, list1);

						if (log.isEmpty()) {
							System.out.println("log为空");
							if (!list.isEmpty()) {
								for (int i = 0; i < list.size(); i++) {
									addAthlete(i, list, list1, formatter2, company, phone);
								}

								for (int i = 0; i < list.size(); i++) {
									String[] competitionArray1 = competitionArray.split(",");
									str = str + list.get(i)[2] + "-" + list.get(i)[3];
									for (int j = 8; j < list.get(i).length; j++) {
										if (!(list.get(i)[j].trim().equals("") || list.get(i)[j] == null)) {
											str = str + "-" + competitionArray1[j - 8];
										}
									}
									str = str + ";";
								}

								// 添加运动员队伍信息到数据库
								CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
								addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
										competition_type, filename, competitionInfo);
								
							}

						}
					}

					SignTableEntity st = signTableService.queryObjectByFilePath(filename);
					if (log.isEmpty()) {
						st.setCheckStatus(1);
						signTableService.update(st);
						response.getWriter().write("<script>alert('审核通过')</script>");
						return;
					} else {
						st.setCheckStatus(0);
						signTableService.update(st);
						String s = "";
						for (int i = 0; i < log.size(); i++) {
							s = s + log.get(i) + ";    ";
						}
						response.getWriter().write("<script>alert('" + s + "')</script>");
						return;
					}
			} catch (Exception e) {
				response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
				e.printStackTrace();
			}
		}
		
	    // 解析摔跤
		public void receiveExcelForWrestling(HttpServletRequest request, HttpServletResponse response,
				String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
				String company) throws IOException {
				response.setContentType("text/html;charset=utf-8");
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
				SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
				try{
					String filePath = null;
					String filename = fileName;
					Date upload = today;
					int competition_id = Integer.parseInt(competition_id1);
					String frontUserId = front_user_id;
					filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
					System.out.println(filePath);
					List<String> log = new ArrayList<>();
					ExcelUtil excelutil = new ExcelUtil();

					int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
					int[] numberArray = new int[8];
					List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
					for (int number = 0; number < iSheetQuanitity; number++) {
						// 得到运动员数据
						String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
						List<String[]> list = new ArrayList<String[]>();
						if(strCurrentSheetName.indexOf("古典甲组男") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
						}
						if(strCurrentSheetName.indexOf("古典乙组男") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
						}
						if(strCurrentSheetName.indexOf("古典丙组男") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 11, true);
						}
						if(strCurrentSheetName.indexOf("自由甲组男") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 11, true);
						}
						if(strCurrentSheetName.indexOf("自由甲组女") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 11, true);
						}
						if(strCurrentSheetName.indexOf("自由乙组男") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 11, true);
						}
						if(strCurrentSheetName.indexOf("自由乙组女") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
						}
						if(strCurrentSheetName.indexOf("自由丙组男") != -1) {
							list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
						}
						for (int i = 0; i < list.size(); i++) {
							for (int j = 8; j < list.get(i).length; j++) {
								System.out.println("llllllllllll"+list.get(i)[j]);
								if (!list.get(i)[j].equals("")) {
									if (list.get(i)[j].trim().equals("√")) {
										numberArray[j - 8]++;
									}else{
										response.getWriter().write("<script>alert('请勾选项目时用“√”。')</script>");
										return;
									}
								}
							}
						}
						for (int i = 0; i < numberArray.length; i++) {
							if (numberArray[i] > 2) {
								response.getWriter().write("<script>alert('各个级别的运动运限报2名。')</script>");
								return;
							}
						}
						// 得到所有合并单元格的数据
						List<String> list1 = excelutil.execelpoi(filePath, number, 0);
						for (int i = 0; i < list1.size(); i++) {
							System.out.println("kkkkkkkkkkkk"+list1.get(i));
						}
						// 判断报名日期是否正确
						if (isApplyTime(response, competition_id, formatter1, upload)) {
							return;
						}

						// 判断人数是否正确
						if (isAtheteNum(response, competition_id, filePath, number, list, strCurrentSheetName)) {
							return;
						}

						for (int i = 0; i < list.size(); i++) {
							//判断性别是否符合
							log = isSex(list, i, log, strCurrentSheetName);
							//判断是否是注册运动员
							log = isAthlete(list, i, log);
							//判断运动员年龄是否符合
							log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
						}
						//判断同一张报名表中参赛人员是否重复
						String[] s = new String[list.size()];
						for (int i = 0; i < list.size(); i++) {
							log = Check(list, i, log, s);
						}
					}

					for (int number = 0; number < iSheetQuanitity; number++) {
						String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
						String str = "";
						String competitionArray = null;
						List<String[]> list = new ArrayList<String[]>();
						list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
						if (strCurrentSheetName.indexOf("古典甲组男") != -1) {
							competitionArray = "48kg,55kg,60kg,66kg,72kg,72kg+";
						}
						if (strCurrentSheetName.indexOf("古典乙组男") != -1) {
							competitionArray = "40kg,46kg,52kg,58kg,63kg,63kg+";
						}
						if (strCurrentSheetName.indexOf("古典丙组男") != -1) {
							competitionArray = "36kg,42kg,50kg,50kg+";
						}
						if (strCurrentSheetName.indexOf("自由甲组男") != -1) {
							competitionArray = "48kg,53kg,58kg,58kg+";
						}
						if (strCurrentSheetName.indexOf("自由甲组女") != -1) {
							competitionArray = "41kg,46kg,51kg,56kg+";
						}
						if (strCurrentSheetName.indexOf("自由乙组男") != -1) {
							competitionArray = "40kg,46kg,52kg,52kg+";
						}
						if (strCurrentSheetName.indexOf("自由乙组女") != -1) {
							competitionArray = "35kg,40kg,45kg+";
						}
						if (strCurrentSheetName.indexOf("自由丙组男") != -1) {
							competitionArray = "40kg,45kg,50kg+";
						}

						System.out.println(list.size());
						for (int i = 0; i < list.size(); i++) {
							for (int j = 0; j < list.get(i).length; j++) {
								System.out.println("aaaaaaa" + list.get(i)[j]);
							}
						}
						List<String> list1 = excelutil.execelpoi(filePath, number, 0);
						for (int i = 0; i < list1.size(); i++) {
							System.out.println(list1.get(i));
						}
						String phone = excelutil.onecell(filePath, number, 3, 7, false);
						System.out.println(phone);

						// 得到教练
						String capain = null;
						capain = capainSet(capain, list1);

						if (log.isEmpty()) {
							System.out.println("log为空");
							if (!list.isEmpty()) {
								for (int i = 0; i < list.size(); i++) {
									addAthlete(i, list, list1, formatter2, company, phone);
								}

								for (int i = 0; i < list.size(); i++) {
									String[] competitionArray1 = competitionArray.split(",");
									str = str + list.get(i)[2] + "-" + list.get(i)[3];
									for (int j = 8; j < list.get(i).length; j++) {
										if (!(list.get(i)[j].trim().equals("") || list.get(i)[j] == null)) {
											str = str + "-" + competitionArray1[j - 8];
										}
									}
									str = str + ";";
								}

								// 添加运动员队伍信息到数据库
								CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
								addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
										competition_type, filename, competitionInfo);
								
							}

						}
					}

					SignTableEntity st = signTableService.queryObjectByFilePath(filename);
					if (log.isEmpty()) {
						st.setCheckStatus(1);
						signTableService.update(st);
						response.getWriter().write("<script>alert('审核通过')</script>");
						return;
					} else {
						st.setCheckStatus(0);
						signTableService.update(st);
						String s = "";
						for (int i = 0; i < log.size(); i++) {
							s = s + log.get(i) + ";    ";
						}
						response.getWriter().write("<script>alert('" + s + "')</script>");
						return;
					}
			} catch (Exception e) {
				response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
				e.printStackTrace();
			}
		}

      //解析特色项目学校羽毛球
		public void receiveExcelForTeseBadminton(HttpServletRequest request, HttpServletResponse response,
				String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
				String company) throws IOException {
			response.setContentType("text/html;charset=utf-8");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
			try{
				String filePath = null;
				String filename = fileName;
				Date upload = today;
				int competition_id = Integer.parseInt(competition_id1);
				String frontUserId = front_user_id;
				filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
				System.out.println(filePath);
				List<String> log = new ArrayList<>();
				ExcelUtil excelutil = new ExcelUtil();

				int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
				int[] numberArray = new int[8];
				List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
				for (int number = 0; number < iSheetQuanitity; number++) {
					// 得到运动员数据
					String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
					List<String[]> list = new ArrayList<String[]>();
					if(strCurrentSheetName.indexOf("初中组男单") != -1) {
						list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
					}
					if(strCurrentSheetName.indexOf("初中组男团") != -1) {
						list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
					}
					if(strCurrentSheetName.indexOf("初中组女单") != -1) {
						list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
					}
					if(strCurrentSheetName.indexOf("初中组女团") != -1) {
						list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
					}
					if(strCurrentSheetName.indexOf("小学甲组男单") != -1) {
						list = excelutil.readByPoi(filePath, number, 6, 5, 11, true);
					}
					if(strCurrentSheetName.indexOf("小学甲组男团") != -1) {
						list = excelutil.readByPoi(filePath, number, 6, 5, 11, true);
					}
					if(strCurrentSheetName.indexOf("小学甲组女单") != -1) {
						list = excelutil.readByPoi(filePath, number, 6, 5, 11, true);
					}
					if(strCurrentSheetName.indexOf("小学甲组女团") != -1) {
						list = excelutil.readByPoi(filePath, number, 6, 5, 11, true);
					}
					if(strCurrentSheetName.indexOf("小学乙组男单") != -1) {
						list = excelutil.readByPoi(filePath, number, 6, 5, 11, true);
					}
					if(strCurrentSheetName.indexOf("小学乙组男团") != -1) {
						list = excelutil.readByPoi(filePath, number, 6, 5, 11, true);
					}
					if(strCurrentSheetName.indexOf("小学乙组女单") != -1) {
						list = excelutil.readByPoi(filePath, number, 6, 5, 11, true);
					}
					if(strCurrentSheetName.indexOf("小学乙组女团") != -1) {
						list = excelutil.readByPoi(filePath, number, 6, 5, 11, true);
					}
					if(strCurrentSheetName.indexOf("小学丙组男单") != -1) {
						list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
					}
					if(strCurrentSheetName.indexOf("小学丙组男团") != -1) {
						list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
					}
					if(strCurrentSheetName.indexOf("小学丙组女单") != -1) {
						list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
					}
					if(strCurrentSheetName.indexOf("小学丙组女团") != -1) {
						list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
					}
					for (int i = 0; i < list.size(); i++) {
						for (int j = 8; j < list.get(i).length; j++) {
							System.out.println(list.get(i)[j]);
							if (!list.get(i)[j].equals("")) {
								if (list.get(i)[j].trim().equals("√")) {
									numberArray[j - 8]++;
								}else{
									response.getWriter().write("<script>alert('请勾选项目时用“√”。')</script>");
									return;
								}
							}
						}
					}
					int tuantixiangmu=0;
					if(strCurrentSheetName.indexOf("团") != -1){
					for (int i = 0; i < list.size(); i++) {
						for (int j = 8; j < list.get(i).length; j++) {
							System.out.println(list.get(i)[j]);
							if (!list.get(i)[j].equals("")) {
								if (!list.get(i)[j].trim().equals("√")) {
									response.getWriter().write("<script>alert('请勾选项目时用“√”。')</script>");
									return;
								}else{
									if(j==8){
										tuantixiangmu++;
									}
								}
							}
						}
					}
					}
					// 得到所有合并单元格的数据
					List<String> list1 = excelutil.execelpoi(filePath, number, 0);
					for (int i = 0; i < list1.size(); i++) {
						System.out.println("kkkkkkkkkkkk"+list1.get(i));
					}
					// 判断报名日期是否正确
					if (isApplyTime(response, competition_id, formatter1, upload)) {
						return;
					}

					// 判断人数是否正确
					if(strCurrentSheetName.indexOf("单")!=-1){
						if(list.size()>4){
							log.add("单打运动员限报4人");
						}
					}else{
					    if (isAtheteNum(response, competition_id, filePath, number, list, strCurrentSheetName)) {
						    return;
					    }
					}

					for (int i = 0; i < list.size(); i++) {
						//判断性别是否符合
						log = isSex(list, i, log, strCurrentSheetName);
						//判断运动员年龄是否符合
						log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
					}
					//判断同一张报名表中参赛人员是否重复
					String[] s = new String[list.size()];
					for (int i = 0; i < list.size(); i++) {
						log = Check(list, i, log, s);
					}
					if(strCurrentSheetName.indexOf("团") != -1){
						if(tuantixiangmu<3){
							response.getWriter().write("<script>alert('团体赛至少3人报名。')</script>");
							return;
						}
					}
					
				}

				for (int number = 0; number < iSheetQuanitity; number++) {
					String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
					String str = "";
					String competitionArray = null;
					List<String[]> list = new ArrayList<String[]>();
					list = excelutil.readByPoi(filePath, number, 6, 5, 13, true);
					if (strCurrentSheetName.indexOf("初中组男单") != -1) {
						competitionArray = "单打";
					}
					if (strCurrentSheetName.indexOf("初中组男团") != -1) {
						competitionArray = "团体";
					}
					if (strCurrentSheetName.indexOf("初中组女单") != -1) {
						competitionArray = "单打";
					}
					if (strCurrentSheetName.indexOf("初中组女团") != -1) {
						competitionArray = "团体";
					}
					if (strCurrentSheetName.indexOf("小学甲组男单") != -1) {
						competitionArray = "单打";
					}
					if (strCurrentSheetName.indexOf("小学甲组男团") != -1) {
						competitionArray = "团体";
					}
					if (strCurrentSheetName.indexOf("小学甲组女单") != -1) {
						competitionArray = "单打";
					}
					if (strCurrentSheetName.indexOf("小学甲组女团") != -1) {
						competitionArray = "团体";
					}
					if (strCurrentSheetName.indexOf("小学乙组男单") != -1) {
						competitionArray = "单打";
					}
					if (strCurrentSheetName.indexOf("小学乙组男团") != -1) {
						competitionArray = "团体";
					}
					if (strCurrentSheetName.indexOf("小学乙组女单") != -1) {
						competitionArray = "单打";
					}
					if (strCurrentSheetName.indexOf("小学乙组女团") != -1) {
						competitionArray = "团体";
					}
					if (strCurrentSheetName.indexOf("小学丙组男单") != -1) {
						competitionArray = "单打";
					}
					if (strCurrentSheetName.indexOf("小学丙组男团") != -1) {
						competitionArray = "团体";
					}
					if (strCurrentSheetName.indexOf("小学丙组女单") != -1) {
						competitionArray = "单打";
					}
					if (strCurrentSheetName.indexOf("小学丙组女团") != -1) {
						competitionArray = "团体";
					}

					for (int i = 0; i < list.size(); i++) {
						for (int j = 0; j < list.get(i).length; j++) {
							System.out.println(list.get(i)[j]);
						}
					}
					List<String> list1 = excelutil.execelpoi(filePath, number, 0);
					for (int i = 0; i < list1.size(); i++) {
						System.out.println(list1.get(i));
					}
					String phone = excelutil.onecell(filePath, number, 3, 7, false);
					System.out.println(phone);

					// 得到教练
					String capain = null;
					capain = capainSet(capain, list1);

					if (log.isEmpty()) {
						if (!list.isEmpty()) {
							for (int i = 0; i < list.size(); i++) {
								addAthlete(i, list, list1, formatter2, company, phone);
							}

							for (int i = 0; i < list.size(); i++) {
								String[] competitionArray1 = competitionArray.split(",");
								str = str + list.get(i)[2] + "-" + list.get(i)[3];
								for (int j = 8; j < list.get(i).length; j++) {
									if (!(list.get(i)[j].trim().equals("") || list.get(i)[j] == null)) {
										str = str + "-" + competitionArray1[j - 8];
									}
								}
								str = str + ";";
							}

							// 添加运动员队伍信息到数据库
							if(strCurrentSheetName.indexOf("单")!=-1){
								for(int i = 0; i < list.size(); i++){
									str = list.get(i)[2] + "-" + list.get(i)[3] + "-" + "单打";
									CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
									addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
											competition_type, filename, competitionInfo);
								}
							}else{
								CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
							addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
									competition_type, filename, competitionInfo);
							}
						}

					}
				}

				SignTableEntity st = signTableService.queryObjectByFilePath(filename);
				if (log.isEmpty()) {
					st.setCheckStatus(1);
					signTableService.update(st);
					response.getWriter().write("<script>alert('审核通过')</script>");
					return;
				} else {
					st.setCheckStatus(0);
					signTableService.update(st);
					String s = "";
					for (int i = 0; i < log.size(); i++) {
						s = s + log.get(i) + ";    ";
					}
					response.getWriter().write("<script>alert('" + s + "')</script>");
					return;
				}
		} catch (Exception e) {
			response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
			e.printStackTrace();
		}
	}
		
		//解析少年儿童乒乓球
		public void receiveExcelForPingpong(HttpServletRequest request, HttpServletResponse response,
				String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
				String company) throws IOException {
			response.setContentType("text/html;charset=utf-8");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
			try {
				String filePath = null;
				String filename = fileName;
				Date upload = today;
				int competition_id = Integer.parseInt(competition_id1);
				String frontUserId = front_user_id;
				filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
				System.out.println(filePath);
				List<String> log = new ArrayList<>();
				List<String> logs=new ArrayList<>();
				ExcelUtil excelutil = new ExcelUtil();

				int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
				List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
				for (int number = 0; number < iSheetQuanitity; number++) {
					// 得到运动员数据
					String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
					int[] numberArray = new int[6];
					List<String[]> list = new ArrayList<String[]>();
					list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
					for (int i = 0; i < list.size(); i++) {
						for (int j = 8; j < list.get(i).length; j++) {
							System.out.println(list.get(i)[j]);
							if (!list.get(i)[j].equals("")) {
								if (!list.get(i)[j].trim().equals("√")) {
									response.getWriter().write("<script>alert('请勾选项目时用“√”。')</script>");
									return;
								}
							}
						}
					}
					
					// 得到所以合并单元格的数据
					List<String> list1 = excelutil.execelpoi(filePath, number, 0);
					for (int i = 0; i < list1.size(); i++) {
						System.out.println(list1.get(i));
					}
					// 判断报名日期是否正确
					if (isApplyTime(response, competition_id, formatter1, upload)) {
						return;
					}
					// 判断人数是否正确
					if (isAtheteNum(response, competition_id, filePath, number, list, strCurrentSheetName)) {
						return;
					}
					for (int i = 0; i < list.size(); i++) {
						//判断是否是注册运动员
						log = isAthlete(list, i, log);
						//判断性别是否符合
						log = isSex(list, i, log, strCurrentSheetName);
						//判断运动员年龄是否符合
						log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
					}
					//判断同一张报名表中参赛人员是否重复
					String[] s = new String[list.size()];
					for (int i = 0; i < list.size(); i++) {
						log = Check(list, i, log, s);
					}
				}
				for (int number = 0; number < iSheetQuanitity; number++) {
					String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
					String str = "";
					String competitionArray = null;
					List<String[]> list = new ArrayList<String[]>();
					list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
					List<String> list1 = excelutil.execelpoi(filePath, number, 0);
					String phone = excelutil.onecell(filePath, number, 3, 7, false);
					competitionArray = "团体,双打,单打";

					// 得到教练
					String capain = null;
					capain = capainSet(capain, list1);

					if (log.isEmpty()) {
						System.out.println("log为空");
						if (!list.isEmpty()) {
							for (int i = 0; i < list.size(); i++) {
								addAthlete(i, list, list1, formatter2, company, phone);
							}

							for (int i = 0; i < list.size(); i++) {
								String[] competitionArray1 = competitionArray.split(",");
								str = str + list.get(i)[2] + "-" + list.get(i)[3];
								for (int j = 8; j < list.get(i).length; j++) {
									if (!(list.get(i)[j].trim().equals("") || list.get(i)[j] == null)) {
										str = str + "-" + competitionArray1[j - 8];
									}
								}
								str = str + ";";
							}

							// 添加运动员队伍信息到数据库
							CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
							addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
									competition_type, filename, competitionInfo);
							
						}

					}
				}

				SignTableEntity st = signTableService.queryObjectByFilePath(filename);
				if (log.isEmpty()) {
					st.setCheckStatus(1);
					signTableService.update(st);
					response.getWriter().write("<script>alert('审核通过')</script>");
					return;
				} else {
					st.setCheckStatus(0);
					signTableService.update(st);
					String s = "";
					for (int i = 0; i < log.size(); i++) {
						s = s + log.get(i) + ";    ";
					}
					response.getWriter().write("<script>alert('" + s + "')</script>");
					return;
				}
				
			}catch (Exception e) {
				response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
				e.printStackTrace();
			}
			
			
		}

		//解析乒乓球
				public void receiveExcelForPingpongball(HttpServletRequest request, HttpServletResponse response,
						String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
						String company) throws IOException {
					response.setContentType("text/html;charset=utf-8");
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
					SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
					try{
						String filePath = null;
						String filename = fileName;
						Date upload = today;
						int competition_id = Integer.parseInt(competition_id1);
						String frontUserId = front_user_id;
						filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
						System.out.println(filePath);
						List<String> log = new ArrayList<>();
						ExcelUtil excelutil = new ExcelUtil();

						int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
						List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
						for (int number = 0; number < iSheetQuanitity; number++) {
							// 得到运动员数据
							String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
							List<String[]> list = new ArrayList<String[]>();
							if(strCurrentSheetName.indexOf("乒乓小学甲组男单") != -1) {
								list = excelutil.readByPoi(filePath, number, 6, 5, 9, true);
							}
							if(strCurrentSheetName.indexOf("乒乓小学甲组男团") != -1) {
								list = excelutil.readByPoi(filePath, number, 6, 5, 9, true);
							}
							if(strCurrentSheetName.indexOf("乒乓小学甲组女单") != -1) {
								list = excelutil.readByPoi(filePath, number, 6, 5, 9, true);
							}
							if(strCurrentSheetName.indexOf("乒乓小学甲组女团") != -1) {
								list = excelutil.readByPoi(filePath, number, 6, 5, 9, true);
							}
							if(strCurrentSheetName.indexOf("乒乓小学乙组男单") != -1) {
								list = excelutil.readByPoi(filePath, number, 6, 5, 9, true);
							}
							if(strCurrentSheetName.indexOf("乒乓小学乙组男团") != -1) {
								list = excelutil.readByPoi(filePath, number, 6, 5, 9, true);
							}
							if(strCurrentSheetName.indexOf("乒乓小学乙组女单") != -1) {
								list = excelutil.readByPoi(filePath, number, 6, 5, 9, true);
							}
							if(strCurrentSheetName.indexOf("乒乓小学乙组女团") != -1) {
								list = excelutil.readByPoi(filePath, number, 6, 5, 9, true);
							}
							if(strCurrentSheetName.indexOf("乒乓小学丙组男单") != -1) {
								list = excelutil.readByPoi(filePath, number, 6, 5, 9, true);
							}
							if(strCurrentSheetName.indexOf("乒乓小学丙组男团") != -1) {
								list = excelutil.readByPoi(filePath, number, 6, 5, 9, true);
							}
							if(strCurrentSheetName.indexOf("乒乓小学丙组女单") != -1) {
								list = excelutil.readByPoi(filePath, number, 6, 5, 9, true);
							}
							if(strCurrentSheetName.indexOf("乒乓小学丙组女团") != -1) {
								list = excelutil.readByPoi(filePath, number, 6, 5, 9, true);
							}
							for (int i = 0; i < list.size(); i++) {
								for (int j = 8; j < list.get(i).length; j++) {
									if (!(list.get(i)[j].equals("")||list.get(i)[j].equals(null))) {
										if (!list.get(i)[j].trim().equals("√")) {
											response.getWriter().write("<script>alert('请勾选项目时用“√”。')</script>");
											return;
										}
									}
								}
							}
							// 得到所有合并单元格的数据
							List<String> list1 = excelutil.execelpoi(filePath, number, 0);
							for (int i = 0; i < list1.size(); i++) {
								System.out.println("kkkkkkkkkkkk"+list1.get(i));
							}
							// 判断报名日期是否正确
							if (isApplyTime(response, competition_id, formatter1, upload)) {
								return;
							}

							// 判断人数是否正确
							if(strCurrentSheetName.indexOf("单")!=-1){
								if(list.size()>5){
									response.getWriter().write("<script>alert('单打运动员限报5人。')</script>");
									return;
								}
							}else if(strCurrentSheetName.indexOf("团") != -1){
								if(list.size()>5){
									response.getWriter().write("<script>alert('团体赛限报5人。')</script>");
									return;
								}
							}
							
							for (int i = 0; i < list.size(); i++) {
								//判断性别是否符合
								log = isSex(list, i, log, strCurrentSheetName);
								//判断运动员年龄是否符合
								log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
							}
							//判断同一张报名表中参赛人员是否重复
							String[] s = new String[list.size()];
							for (int i = 0; i < list.size(); i++) {
								log = Check(list, i, log, s);
							}
							
							
						}

						for (int number = 0; number < iSheetQuanitity; number++) {
							String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
							String str = "";
							String competitionArray = null;
							List<String[]> list = new ArrayList<String[]>();
							list = excelutil.readByPoi(filePath, number, 6, 5, 16, true);
							if (strCurrentSheetName.indexOf("团") != -1) {
								competitionArray = "团体";
							}
							if (strCurrentSheetName.indexOf("单") != -1) {
								competitionArray = "单打";
							}

							for (int i = 0; i < list.size(); i++) {
								for (int j = 0; j < list.get(i).length; j++) {
									System.out.println(list.get(i)[j]);
								}
							}
							List<String> list1 = excelutil.execelpoi(filePath, number, 0);
							for (int i = 0; i < list1.size(); i++) {
								System.out.println(list1.get(i));
							}
							String phone = excelutil.onecell(filePath, number, 3, 7, false);
							System.out.println(phone);

							// 得到教练
							String capain = null;
							capain = capainSet(capain, list1);

							if (log.isEmpty()) {
								if (!list.isEmpty()) {
									for (int i = 0; i < list.size(); i++) {
										addAthlete(i, list, list1, formatter2, company, phone);
									}

									for (int i = 0; i < list.size(); i++) {
										String[] competitionArray1 = competitionArray.split(",");
										str = str + list.get(i)[2] + "-" + list.get(i)[3];
										for (int j = 8; j < list.get(i).length; j++) {
											if (!(list.get(i)[j].trim().equals("") || list.get(i)[j] == null)) {
												str = str + "-" + competitionArray1[j - 8];
											}
										}
										str = str + ";";
									}

									// 添加运动员队伍信息到数据库
									if(strCurrentSheetName.indexOf("单")!=-1){
										for(int i = 0; i < list.size(); i++){
											str = list.get(i)[2] + "-" + list.get(i)[3] + "-" + "单打";
											CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
											addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
													competition_type, filename, competitionInfo);
										}
									}else{
										CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
									addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
											competition_type, filename, competitionInfo);
									}
								}

							}
						}

						SignTableEntity st = signTableService.queryObjectByFilePath(filename);
						if (log.isEmpty()) {
							st.setCheckStatus(1);
							signTableService.update(st);
							response.getWriter().write("<script>alert('审核通过')</script>");
							return;
						} else {
							st.setCheckStatus(0);
							signTableService.update(st);
							String s = "";
							for (int i = 0; i < log.size(); i++) {
								s = s + log.get(i) + ";    ";
							}
							response.getWriter().write("<script>alert('" + s + "')</script>");
							return;
						}
				} catch (Exception e) {
					response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
					e.printStackTrace();
				}
			}
		
	//解析通用方法（注册运动员）
		public void receiveExcelForCurrency(HttpServletRequest request, HttpServletResponse response,
				String competition_id1, String front_user_id, String competition_type, Date today, String fileName,
				String company) throws IOException {
			response.setContentType("text/html;charset=utf-8");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
			try {
				String filePath = null;
				String filename = fileName;
				Date upload = today;
				int competition_id = Integer.parseInt(competition_id1);
				String frontUserId = front_user_id;
				filePath = request.getSession().getServletContext().getRealPath("upload") + "\\" + filename;
				System.out.println(filePath);
				List<String> log = new ArrayList<>();
				List<String> logs=new ArrayList<>();
				ExcelUtil excelutil = new ExcelUtil();

				int iSheetQuanitity = excelutil.getSheetQuantity(filePath);
				List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(competition_id);	
				for (int number = 0; number < iSheetQuanitity; number++) {
					// 得到运动员数据
					String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
					int[] numberArray = new int[6];
					List<String[]> list = new ArrayList<String[]>();
					list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
					for (int i = 0; i < list.size(); i++) {
						for (int j = 8; j < list.get(i).length; j++) {
							System.out.println(list.get(i)[j]);
							if (!list.get(i)[j].equals("")) {
								if (!list.get(i)[j].trim().equals("√")) {
									response.getWriter().write("<script>alert('请勾选项目时用“√”。')</script>");
									return;
								}
							}
						}
					}
					
					// 得到所以合并单元格的数据
					List<String> list1 = excelutil.execelpoi(filePath, number, 0);
					for (int i = 0; i < list1.size(); i++) {
						System.out.println(list1.get(i));
					}
					// 判断报名日期是否正确
					if (isApplyTime(response, competition_id, formatter1, upload)) {
						return;
					}
					// 判断人数是否正确
					if (isAtheteNum(response, competition_id, filePath, number, list, strCurrentSheetName)) {
						return;
					}
					for (int i = 0; i < list.size(); i++) {
						//判断是否是注册运动员
						log = isAthlete(list, i, log);
						//判断性别是否符合
						log = isSex(list, i, log, strCurrentSheetName);
						//判断运动员年龄是否符合
						log = isAge(list, i, log, strCurrentSheetName, formatter2, competition_id, competitionInfo);
					}
					//判断同一张报名表中参赛人员是否重复
					String[] s = new String[list.size()];
					for (int i = 0; i < list.size(); i++) {
						log = Check(list, i, log, s);
					}
				}
				for (int number = 0; number < iSheetQuanitity; number++) {
					String strCurrentSheetName = excelutil.getSheetNameByIndex(filePath, number);
					String str = "";
					String competitionArray = null;
					List<String[]> list = new ArrayList<String[]>();
					list = excelutil.readByPoi(filePath, number, 6, 5, 10, true);
					List<String> list1 = excelutil.execelpoi(filePath, number, 0);
					String phone = excelutil.onecell(filePath, number, 3, 7, false);
					competitionArray = "团体,双打,单打";

					// 得到教练
					String capain = null;
					capain = capainSet(capain, list1);

					if (log.isEmpty()) {
						System.out.println("log为空");
						if (!list.isEmpty()) {
							for (int i = 0; i < list.size(); i++) {
								addAthlete(i, list, list1, formatter2, company, phone);
							}

							for (int i = 0; i < list.size(); i++) {
								String[] competitionArray1 = competitionArray.split(",");
								str = str + list.get(i)[2] + "-" + list.get(i)[3];
								for (int j = 8; j < list.get(i).length; j++) {
									if (!(list.get(i)[j].trim().equals("") || list.get(i)[j] == null)) {
										str = str + "-" + competitionArray1[j - 8];
									}
								}
								str = str + ";";
							}

							// 添加运动员队伍信息到数据库
							CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
							addTeamInfo(str, capain, phone, list1, company, competitonName, strCurrentSheetName,
									competition_type, filename, competitionInfo);
							
						}

					}
				}

				SignTableEntity st = signTableService.queryObjectByFilePath(filename);
			System.out.println(filename);
				
				if (log.isEmpty()) {
					st.setCheckStatus(1);
					signTableService.update(st);
					response.getWriter().write("<script>alert('审核通过')</script>");
					return;
				} else {
					st.setCheckStatus(0);
					signTableService.update(st);
					String s = "";
					for (int i = 0; i < log.size(); i++) {
						s = s + log.get(i) + ";    ";
					}
					response.getWriter().write("<script>alert('" + s + "')</script>");
					return;
				}
				
			}catch (Exception e) {
				response.getWriter().write("<script>alert('报名表内容读取错误，请在最新版模板中按规则填写。')</script>");
				e.printStackTrace();
			}
			
			
		}

	// 判断报名是否截止
	public boolean isApplyTime(HttpServletResponse response, int competition_id, SimpleDateFormat formatter1,
			Date upload) throws Exception {
		CompetitionNameEntity competitonName = competitionNameService.queryObject(competition_id);
		Date dtStartDate = formatter1.parse(competitonName.getSignStartDate());
		Date dtEndDate = formatter1.parse(competitonName.getSignEndDate());
		
		// 判断上传文件是否超过报名日期
		if (upload.before(dtStartDate) || upload.after(dtEndDate)) {
			response.getWriter().write("<script>alert('报名日期已截止，请联系体育局相关负责人。')</script>");
			return true;
		}
		return false;
	}

	// 判断人数是否符合
	public boolean isAtheteNum(HttpServletResponse response, int iComID, String filePath, int number, List list,
			String strCurrentSheetName) throws Exception {

		List<CompetitionInfoEntity> competitionInfo = competitionInfoService.queryListByCompetitionId(iComID);	
		for (int iCompetitionIndex = 0; iCompetitionIndex < competitionInfo.size(); iCompetitionIndex++) {
			if (strCurrentSheetName.trim()
					.equals(competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getGroupName())) {
				if (strCurrentSheetName.indexOf("男") != -1) {
					if (list.size() > competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getMaxMen()) {
						response.getWriter().write("<script>alert('" + strCurrentSheetName + "不得超过"
								+ competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getMaxMen() + "人')</script>");
						return true;
					}
					if (list.size() < competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getMinMen()) {
						response.getWriter().write("<script>alert('" + strCurrentSheetName + "不得少于"
								+ competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getMinMen() + "人')</script>");
						return true;
					}
					
				} else if (strCurrentSheetName.indexOf("女") != -1) {
					if (list.size() > competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getMaxWomen()) {
						response.getWriter().write("<script>alert('" + strCurrentSheetName + "不得超过"
								+ competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getMaxWomen() + "人')</script>");
						return true;
					}
					if (list.size() < competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getMinWomen()) {
						response.getWriter().write("<script>alert('" + strCurrentSheetName + "不得少于"
								+ competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getMinWomen() + "人')</script>");
						return true;
					}
				} else {
					if (list.size() != 0
							&& (list.size() > competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getMaxSum() || list
									.size() < competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getMinSum())) {
						response.getWriter().write("<script>alert('" + strCurrentSheetName + "的人数为"
								+ competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getMinSum() + "人到"
								+ competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getMaxSum() + "人')</script>");
						return true;
					}
				}
			}
		}
		return false;
	}

//	public boolean isAtheteNum(HttpServletResponse response, int iComID, String filePath, int number, List list,
//			String strCurrentSheetName) throws Exception {
//
//		Object[] s = { iComID };
//		List<CompetitionInfo> competitionInfo = (List<CompetitionInfo>) competition
//				.createSQLQueryList("from CompetitionInfo where competitionId=?", CompetitionInfo.class, s);
//		if (strCurrentSheetName.indexOf("男") != -1) {
//			for (int iCompetitionIndex = 0; iCompetitionIndex < competitionInfo.size(); iCompetitionIndex++) {
//				if (competitionInfo.get(iCompetitionIndex).getGroupName().getGroupName().indexOf("男") != -1) {
//					int maxMen = competitionInfo.get(iCompetitionIndex).getGroupName().getMaxMen();
//					int minMen = competitionInfo.get(iCompetitionIndex).getGroupName().getMinMen();
//					if (list.size() > maxMen) {
//						response.getWriter().write("<script>alert('男运动员人数不得超过" + maxMen + "人')</script>");
//						return true;
//					}
//					if (list.size() < minMen) {
//						response.getWriter().write("<script>alert('男运动员人数不得少于" + minMen + "人')</script>");
//						return true;
//					}
//				}
//			}
//		} else {
//			for (int iCompetitionIndex = 0; iCompetitionIndex < competitionInfo.size(); iCompetitionIndex++) {
//				if (competitionInfo.get(iCompetitionIndex).getGroupName().getGroupName().indexOf("女") != -1) {
//					int maxWomen = competitionInfo.get(iCompetitionIndex).getGroupName().getMaxWomen();
//					int minWomen = competitionInfo.get(iCompetitionIndex).getGroupName().getMinWomen();
//					if (list.size() > maxWomen) {
//						response.getWriter().write("<script>alert('女运动员人数不得超过" + maxWomen + "人')</script>");
//						return true;
//					}
//					if (list.size() < minWomen) {
//						response.getWriter().write("<script>alert('女运动员人数不得少于" + minWomen + "人')</script>");
//						return true;
//					}
//				}
//			}
//		}
//		return false;
//	}
	
	//判断是不是注册运动员
	public List isAthlete(List<String[]> list, int i, List<String> log) throws Exception {
		Boolean b1 = true;
		RegisterAthleteEntity registerAthlete = registerAthleteService.queryObject(list.get(i)[2]);

		// 根据身份证号获得注册运动员对象
		if (registerAthlete != null) {
			System.out.println(registerAthlete.getName());
			// 判断运动员信息与注册运动员信息是否一致
			b1 = registerAthlete.getName().equals(list.get(i)[3]);
			if (!b1) {
				log.add(list.get(i)[3] + "姓名与注册信息不符");
			}
		}
	return log;
}
	
    //判断同一张报名表中参赛人员是否重复
	public List Check(List<String[]> list, int i,List<String> log, String[] idArr)throws Exception{
	
		idArr[i] = list.get(i)[2];
		if(i!=0){
			for(int j=0;j<i;j++)
		    if(((idArr[i]).equals(idArr[j]))){
			   log.add(list.get(i)[3] + "运动员信息重复");
		    }
		}
		
		return log;
	}
	// 判断运动员信息是否正确
//	public List isError(List<String[]> list, int i, List<String> log, String strCurrentSheetName,
//			SimpleDateFormat formatter2, int competition_id) throws Exception {
//		Boolean b1 = true;
//		RegisterAthlete registerAthlete = competition.getCNByIdCard(list.get(i)[2]);
//
//		// 根据身份证号获得注册运动员对象
//		if (registerAthlete != null) {
//			System.out.println(registerAthlete.getName());
//			// 判断运动员信息与注册运动员信息是否一致
//			b1 = registerAthlete.getName().equals(list.get(i)[3]);
//			if (!b1) {
//				log.add(list.get(i)[3] + "姓名与注册信息不符");
//			}
//
//			if (b1) {
//				if (strCurrentSheetName.indexOf("男") != -1) {
//					if (registerAthlete.getGender() == 1) {
//						log.add(list.get(i)[3] + "男队不能有女运动员");
//					}
//				} else {
//					if (registerAthlete.getGender() == 0) {
//						log.add(list.get(i)[3] + "女队不能有男运动员");
//					}
//				}
//			}
//
//			// 判断运动员出生日期是否符合规则
//			String birthday = list.get(i)[2].substring(6, 14);
//			Date dtBirthday = formatter2.parse(birthday);
//			Object[] s = { competition_id };
//			List<CompetitionInfo> competitionInfo = (List<CompetitionInfo>) competition
//					.createSQLQueryList("from CompetitionInfo where competitionId=?", CompetitionInfo.class, s);
//			if (strCurrentSheetName.indexOf("男") != -1) {
//				for (int iCompetitionIndex = 0; iCompetitionIndex < competitionInfo.size(); iCompetitionIndex++) {
//					if (competitionInfo.get(iCompetitionIndex).getGroupName().getGroupName().indexOf("男") != -1) {
//						Date dtMaxDate = competitionInfo.get(iCompetitionIndex).getGroupName().getBirthdayEarly();
//						Date dtMinDate = competitionInfo.get(iCompetitionIndex).getGroupName().getBirthdayAfter();
//						if (dtBirthday.after(dtMaxDate)) {
//							log.add(list.get(i)[3] + "运动员未到规定年龄");
//						}
//						if (dtBirthday.before(dtMinDate)) {
//							log.add(list.get(i)[3] + "运动员超出规定年龄");
//						}
//					}
//				}
//			} else {
//				for (int iCompetitionIndex = 0; iCompetitionIndex < competitionInfo.size(); iCompetitionIndex++) {
//					if (competitionInfo.get(iCompetitionIndex).getGroupName().getGroupName().indexOf("女") != -1) {
//						Date dtMaxDate = competitionInfo.get(iCompetitionIndex).getGroupName().getBirthdayEarly();
//						Date dtMinDate = competitionInfo.get(iCompetitionIndex).getGroupName().getBirthdayAfter();
//						if (dtBirthday.after(dtMaxDate)) {
//							log.add(list.get(i)[3] + "运动员未到规定年龄");
//						}
//						if (dtBirthday.before(dtMinDate)) {
//							log.add(list.get(i)[3] + "运动员超出规定年龄");
//						}
//					}
//				}
//			}
//		} else {
//			// 如果年龄小于多少可以直接过，标准在哪里？？？
//			String birthday = list.get(i)[2].substring(6, 14);
//			if (Integer.parseInt(birthday) <= 20101231) {
//				log.add(list.get(i)[3] + "运动员不存在");
//			}
//		}
//		return log;
//	}
//
//	public List isError2(List<String[]> list, int i, List<String> log, String strCurrentSheetName,
//			SimpleDateFormat formatter2, int competition_id, List<CompetitionInfo> competitionInfo) throws Exception {
//		Boolean b1 = true;
//		RegisterAthlete registerAthlete = competition.getCNByIdCard(list.get(i)[2]);
//
//		// 根据身份证号获得注册运动员对象
//		if (registerAthlete != null) {
//			System.out.println(registerAthlete.getName());
//			// 判断运动员信息与注册运动员信息是否一致
//			b1 = registerAthlete.getName().equals(list.get(i)[3]);
//			if (!b1) {
//				log.add(list.get(i)[3] + "姓名与注册信息不符");
//			}
//
//			if (b1) {
//				if (strCurrentSheetName.indexOf("男") != -1) {
//					if (registerAthlete.getGender() == 1) {
//						log.add(list.get(i)[3] + "男队不能有女运动员");
//					}
//				} else if (strCurrentSheetName.indexOf("女") != -1) {
//					if (registerAthlete.getGender() == 0) {
//						log.add(list.get(i)[3] + "女队不能有男运动员");
//					}
//				}
//			}
//
//			// 判断运动员出生日期是否符合规则
//			String birthday = list.get(i)[2].substring(6, 14);
//			Date dtBirthday = formatter2.parse(birthday);
//			for (int iCompetitionIndex = 0; iCompetitionIndex < competitionInfo.size(); iCompetitionIndex++) {
//				if (competitionInfo.get(iCompetitionIndex).getGroupName().getGroupName().trim()
//						.equals(strCurrentSheetName.trim())) {
//					Date dtMaxDate = competitionInfo.get(iCompetitionIndex).getGroupName().getBirthdayEarly();
//					Date dtMinDate = competitionInfo.get(iCompetitionIndex).getGroupName().getBirthdayAfter();
//					if (dtBirthday.after(dtMaxDate)) {
//						log.add(list.get(i)[3] + "运动员未到规定年龄");
//					}
//					if (dtBirthday.before(dtMinDate)) {
//						log.add(list.get(i)[3] + "运动员超出规定年龄");
//					}
//				}
//			}
//		} else {
//			log.add(list.get(i)[3] + "运动员不存在");
//		}
//		return log;
//	}
	
    //判断运动员的性别和年龄是否符合
//	public List isError3(List<String[]> list, int i, List<String> log, String strCurrentSheetName,
//			SimpleDateFormat formatter2, int competition_id, List<CompetitionInfo> competitionInfo) throws Exception {
//         
//		   String sex = list.get(i)[2].substring(16,17);
//         
//			if (strCurrentSheetName.indexOf("男") != -1) {
//				if ( ("0").equals(sex)||("2").equals(sex)||("4").equals(sex)||("6").equals(sex)||("8").equals(sex)) {
//					log.add(list.get(i)[3] +"  " + "男队不能有女运动员");
//				}
//			} else if (strCurrentSheetName.indexOf("女") != -1) {
//				if (("1").equals(sex)||("3").equals(sex)||("5").equals(sex)||("7").equals(sex)||("9").equals(sex)) {
//					log.add(list.get(i)[3] +"  " + "女队不能有男运动员");
//				}
//			}
//		
//			// 判断运动员出生日期是否符合规则
//			String birthday = list.get(i)[2].substring(6, 14);
//			Date dtBirthday = formatter2.parse(birthday);
//			for (int iCompetitionIndex = 0; iCompetitionIndex < competitionInfo.size(); iCompetitionIndex++) {
//				if (competitionInfo.get(iCompetitionIndex).getGroupName().getGroupName().trim()
//						.equals(strCurrentSheetName.trim())) {
//					Date dtMaxDate = competitionInfo.get(iCompetitionIndex).getGroupName().getBirthdayEarly();
//					Date dtMinDate = competitionInfo.get(iCompetitionIndex).getGroupName().getBirthdayAfter();
//					if (dtBirthday.after(dtMaxDate)) {
//						log.add(strCurrentSheetName + "-" + list.get(i)[3] + "运动员未到规定年龄");
//					}
//					if (dtBirthday.before(dtMinDate)) {
//						log.add(strCurrentSheetName + "-" + list.get(i)[3] + "运动员超出规定年龄");
//					}
//				}
//			}
//		return log;
//	}
	
	//判断田径运动员的性别与所报项目是否符合
	public List isError4(List<String[]> list, int i, List<String> log, String strCurrentSheetName,
			SimpleDateFormat formatter2, int competition_id, List<CompetitionInfoEntity> competitionInfo) throws Exception {
         
		   String sex = list.get(i)[2].substring(16,17);
			   if(strCurrentSheetName.indexOf("高中") != -1||strCurrentSheetName.indexOf("初中") != -1||strCurrentSheetName.indexOf("小学甲") != -1){
				   if (("0").equals(sex)||("2").equals(sex)||("4").equals(sex)||("6").equals(sex)||("8").equals(sex)) {
					   for(int j= 9;j< list.get(i).length;j=j+2){
						   if(!(list.get(i)[j].equals("")||list.get(i)[j].equals(null))){
							   log.add(list.get(i)[3] +"  " + "女运动员不能报男子项目");
						   }
					   }
				   }else{
					   for(int j= 8;j< list.get(i).length;j=j+2){
						   if(!(list.get(i)[j].equals("")||list.get(i)[j].equals(null))){
							   log.add(list.get(i)[3] +"  " + "男运动员不能报女子项目");
						   }
				      }
				   }
			   }else if(strCurrentSheetName.indexOf("小学乙") != -1){
				   if (("0").equals(sex)||("2").equals(sex)||("4").equals(sex)||("6").equals(sex)||("8").equals(sex)) {
					   for(int j= 10;j< list.get(i).length;j=j+2){
						   if(!(list.get(i)[j].equals("")||list.get(i)[j].equals(null))){
							   log.add(list.get(i)[3] +"  " + "女运动员不能报男子项目");
						   }
					   }
				   }else{
					   for(int j= 9;j< list.get(i).length;j=j+2){
						   if(!(list.get(i)[j].equals("")||list.get(i)[j].equals(null))){
							   log.add(list.get(i)[3] +"  " + "男运动员不能报女子项目");
						   }
				      }
				   }
			   }
		// 判断运动员出生日期是否符合规则
		String birthday = list.get(i)[2].substring(6, 14);
		Date dtBirthday = formatter2.parse(birthday);
		for (int iCompetitionIndex = 0; iCompetitionIndex < competitionInfo.size(); iCompetitionIndex++) {
			if (competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getGroupName().trim()
					.equals(strCurrentSheetName.trim())) {
				Date dtMaxDate = competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getBirthdayEarly();
				Date dtMinDate = competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getBirthdayAfter();
				if (dtBirthday.after(dtMaxDate)) {
					log.add(strCurrentSheetName + "-" + list.get(i)[3] + "运动员未到规定年龄");
				}
				if (dtBirthday.before(dtMinDate)) {
					log.add(strCurrentSheetName + "-" + list.get(i)[3] + "运动员超出规定年龄");
				}
			}
		}
		return log;
	}
	//判断年龄是否符合
	public List isAge(List<String[]> list, int i, List<String> log, String strCurrentSheetName,
			SimpleDateFormat formatter2, int competition_id, List<CompetitionInfoEntity> competitionInfo) throws Exception {
	
		String birthday = list.get(i)[2].substring(6, 14);
		Date dtBirthday = formatter2.parse(birthday);
		for (int iCompetitionIndex = 0; iCompetitionIndex < competitionInfo.size(); iCompetitionIndex++) {
			if (competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getGroupName().trim()
					.equals(strCurrentSheetName.trim())) {
				Date dtMaxDate = competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getBirthdayEarly();
				Date dtMinDate = competitionInfo.get(iCompetitionIndex).getGroupNameEntity().getBirthdayAfter();
				if (dtBirthday.after(dtMaxDate)) {
					log.add(strCurrentSheetName + "-" + list.get(i)[3] + "运动员未到规定年龄");
				}
				if (dtBirthday.before(dtMinDate)) {
					log.add(strCurrentSheetName + "-" + list.get(i)[3] + "运动员超出规定年龄");
				}
			}
		}
		return log;
	}
	
	//判断性别是否符合
	public List isSex(List<String[]> list, int i, List<String> log, String strCurrentSheetName) throws Exception {
	
		String sex = list.get(i)[2].substring(16,17);
		if (strCurrentSheetName.indexOf("男") != -1) {
			if ( ("0").equals(sex)||("2").equals(sex)||("4").equals(sex)||("6").equals(sex)||("8").equals(sex)) {
				log.add(list.get(i)[3] +"  " + "男队不能有女运动员");
			}
		} else if (strCurrentSheetName.indexOf("女") != -1) {
			if (("1").equals(sex)||("3").equals(sex)||("5").equals(sex)||("7").equals(sex)||("9").equals(sex)) {
				log.add(list.get(i)[3] +"  " + "女队不能有男运动员");
			}
		}
		return log;
	}
	
	// 将教练合并
	public String capainSet(String capain, List<String> list1) {
		if (list1.get(7).trim().equals("") ? false : true) {
			capain = list1.get(7).trim();
			if (list1.get(8).trim().equals("") ? false : true) {
				capain = capain + ";" + list1.get(8).trim();
				if (list1.get(9).trim().equals("") ? false : true) {
					capain = capain + ";" + list1.get(9).trim();
				}
			}
		}
		return capain;
	}

	// 添加进teaminfo表
	public void addTeamInfo(String str, String capain, String phone, List<String> list1, String company,
			CompetitionNameEntity competitonName, String strCurrentSheetName, String competition_type, String filename,List<CompetitionInfoEntity> competitionInfo) {
		Date myDate = new Date();
		myDate.toLocaleString();
		TeamInfoEntity teamInfo = new TeamInfoEntity();
		teamInfo.setAthleteIdSet(str);
		teamInfo.setNote("空");
		teamInfo.setSignType(0);
		teamInfo.setTeamCaptain(capain);
		teamInfo.setTeamContact(phone);
		teamInfo.setTeamLeader(list1.get(4));
		teamInfo.setCompany(company);
		//teamInfo.setTime(myDate);
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		teamInfo.setTeamName(year+"-" + competitonName.getName() + "-" + strCurrentSheetName + "-"
				+ (Integer.parseInt(competition_type) == 0 ? "竞技赛事" : "群众赛事"));
		teamInfo.setFilename(filename);
		teamInfoService.save(teamInfo);
		SignInfoEntity signInfo = new SignInfoEntity();
		
		signInfo.setCheckStatus(1);
		for(int i=0;i<competitionInfo.size();i++){
			if(competitionInfo.get(i).getGroupNameEntity().getGroupName().equals(strCurrentSheetName)){
				signInfo.setCompetitionId(competitionInfo.get(i).getId());
				signInfo.setTeamId(teamInfo.getId());
				signInfoService.save(signInfo);
			}
		}
		
	}
	
	// 添加进athlete表
	public void addAthlete(int i, List<String[]> list, List<String> list1, SimpleDateFormat formatter, String company,
			String phone) {
		RegisterAthleteEntity registerAthlete1 = registerAthleteService.queryObject(list.get(i)[2]);
		Date d = null;
		String IDCardNumber = list.get(i)[2];
		String birthday = IDCardNumber.substring(6, 10) + "-" + IDCardNumber.substring(10, 12) + "-"
				+ IDCardNumber.substring(12, 14);

		try {
			d = formatter.parse(birthday);
		} catch (ParseException e) {
			throw new RuntimeException();
		}

		AthleteEntity athlete = new AthleteEntity();

		if (registerAthlete1 != null) {
			athlete.setIdCard(list.get(i)[2]);
			athlete.setRegisterCode(registerAthlete1.getRegisterCode());
			athlete.setName(list.get(i)[3]);
			athlete.setGender(registerAthlete1.getGender());
			athlete.setBirthday(d);
			athlete.setCompany(company);
			athlete.setPhoneNumber(phone);
			athlete.setJerseyNumber(
					list.get(i)[6].trim().equals("") || list.get(i)[6].trim().equals("略") || list.get(i)[6] == null
							? i + 4 : Integer.parseInt(list.get(i)[6]));
			athlete.setCoach(
					list.get(i)[7].trim().equals("") || list.get(i)[7].trim().equals("略") || list.get(i)[7] == null
							? list1.get(6) : list.get(i)[7]);
			athlete.setNote("空");
		} else {
			athlete.setIdCard(list.get(i)[2]);
			athlete.setRegisterCode("未注册");
			athlete.setName(list.get(i)[3]);
			athlete.setGender(list.get(i)[4].trim().equals("男") ? 0 : 1);
			athlete.setBirthday(d);
			athlete.setCompany(company);
			athlete.setPhoneNumber(phone);
			athlete.setJerseyNumber(
					list.get(i)[6].trim().equals("") || list.get(i)[6].trim().equals("略") || list.get(i)[6] == null
							? i + 4 : Integer.parseInt(list.get(i)[6]));
			athlete.setCoach(
					list.get(i)[7].trim().equals("") || list.get(i)[7].trim().equals("略") || list.get(i)[7] == null
							? list1.get(6) : list.get(i)[7]);
			athlete.setNote("空");
		}
		athleteService.save(athlete);
	}
	
	
}
