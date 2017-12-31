package io.renren.modules.generator.download;

import io.renren.modules.generator.anlyze.ExcelAnlyze;
import io.renren.modules.generator.entity.BallReportTable;
import io.renren.modules.generator.entity.CompetitionNameEntity;
import io.renren.modules.generator.entity.TeamInfoEntity;
import io.renren.modules.generator.service.CompetitionInfoService;
import io.renren.modules.generator.service.CompetitionNameService;
import io.renren.modules.generator.service.SignInfoService;
import io.renren.modules.generator.service.TeamInfoService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
@RequestMapping("/download")
public class Download {
	@Autowired
	SignInfoService signInfoService;
	@Autowired
	TeamInfoService teamInfoService;
	@Autowired
	CompetitionNameService competitionNameService;

	// @RequestMapping("/teamlist1")
	// public void teamList1(HttpServletRequest request,HttpServletResponse
	// response) throws UnsupportedEncodingException{
	// //这下面一行本地的时候解除注释，放到阿里云时候注释掉
	// String[] files=new
	// String(request.getParameter("datas").getBytes("ISO-8859-1"),"utf-8").split(",");//{"篮球报名表模板.xls","篮球报名表模板.xls"};
	// // String[]
	// files=request.getParameter("datas").split(",");//{"篮球报名表模板.xls","篮球报名表模板.xls"};
	// String page=request.getParameter("page");
	// XWPFDocument doc = new XWPFDocument();
	// XWPFParagraph teamList = doc.createParagraph();
	// teamList.setAlignment(ParagraphAlignment.CENTER);
	// teamList.setVerticalAlignment(TextAlignment.TOP);
	// XWPFRun team_list = teamList.createRun();
	// // 设置字体是否加粗
	// team_list.setBold(true);
	// team_list.setFontSize(20);
	// // 设置使用何种字体
	// team_list.setFontFamily("Courier");
	// // 设置上下两行之间的间距
	// team_list.setTextPosition(20);
	// team_list.setText("代表队名单");
	// for(int i=0;i<files.length;i++){
	// BallReportTable
	// ballReportTable=ExcelAnlyze.getAnlyzeEntity(request.getSession().getServletContext().getRealPath("/upload")+"\\"+files[i]);
	// int
	// teamNum=(ballReportTable.getAthletes().size()>0?1:0)+(ballReportTable.getAthletesOfWoman().size()>0?1:0);
	// for(int j=0;j<teamNum;j++){
	// XWPFParagraph teamPlace=doc.createParagraph();
	// XWPFParagraph teamGender=doc.createParagraph();
	// XWPFParagraph teamLeader=doc.createParagraph();
	// XWPFParagraph teamCoach=doc.createParagraph();
	// XWPFParagraph athletes=doc.createParagraph();
	// // 设置字体对齐方式
	//
	// teamPlace.setAlignment(ParagraphAlignment.CENTER);
	// teamPlace.setVerticalAlignment(TextAlignment.TOP);
	// teamGender.setAlignment(ParagraphAlignment.LEFT);
	// teamGender.setVerticalAlignment(TextAlignment.TOP);
	// teamLeader.setAlignment(ParagraphAlignment.LEFT);
	// teamLeader.setVerticalAlignment(TextAlignment.TOP);
	// teamCoach.setAlignment(ParagraphAlignment.LEFT);
	// teamCoach.setVerticalAlignment(TextAlignment.TOP);
	// athletes.setAlignment(ParagraphAlignment.LEFT);
	// athletes.setVerticalAlignment(TextAlignment.TOP);
	// // 第一页要使用p1所定义的属性
	//
	// XWPFRun team_place=teamPlace.createRun();
	// XWPFRun team_gender=teamGender.createRun();
	// XWPFRun team_leader=teamLeader.createRun();
	// XWPFRun team_coach=teamCoach.createRun();
	// XWPFRun athlete=athletes.createRun();
	//
	// // 设置字体是否加粗
	// team_place.setBold(true);
	// team_place.setFontSize(15);
	// // 设置使用何种字体
	// team_place.setFontFamily("Courier");
	// // 设置上下两行之间的间距
	// team_place.setTextPosition(20);
	// String strTeamPlace=ballReportTable.getCompany()[j];
	// team_place.setText(strTeamPlace.toLowerCase().contains("xx")?"用户未填写该项":strTeamPlace);
	// // 设置字体是否加粗
	// team_gender.setBold(true);
	// team_gender.setFontSize(12);
	// // 设置使用何种字体
	// team_gender.setFontFamily("宋体");
	// // 设置上下两行之间的间距
	// team_gender.setTextPosition(30);
	// if(teamNum==1){
	// if(ballReportTable.getAthletes().get(0).getGender()==1){
	// team_gender.setText("男队");
	// }else{
	// team_gender.setText("女队");
	// }
	// }else{
	// team_gender.setText(j==0?"男队":"女队");
	// }
	//
	// // 设置字体是否加粗
	// team_leader.setBold(false);
	// team_leader.setFontSize(12);
	// // 设置使用何种字体
	// team_leader.setFontFamily("宋体");
	// // 设置上下两行之间的间距
	// team_leader.setTextPosition(30);
	// team_leader.setText("领队:\t");
	// String strTeamLeader=ballReportTable.getLeader()[j] + "-" +
	// ballReportTable.getPhoneNum()[j];
	// team_leader.setText(strTeamLeader.toLowerCase().contains("xx")?"":strTeamLeader);
	// // 设置字体是否加粗
	// team_coach.setBold(false);
	// team_coach.setFontSize(12);
	// // 设置使用何种字体
	// team_coach.setFontFamily("宋体");
	// // 设置上下两行之间的间距
	// team_coach.setTextPosition(30);
	// team_coach.setText("教练:\t");
	// String strTeamCoach1=ballReportTable.getCoach1()[j];
	// String strTeamCoach2=ballReportTable.getCoach2()[j];
	// String strTeamCoach3=ballReportTable.getCoach3()[j];
	// team_coach.setText(strTeamCoach1.toLowerCase().contains("xx")?"":strTeamCoach1+"\t");
	// team_coach.setText(strTeamCoach2.toLowerCase().contains("xx")?"":strTeamCoach2+"\t");
	// team_coach.setText(strTeamCoach3.toLowerCase().contains("xx")?"":strTeamCoach3);
	// // 设置字体是否加粗
	// athlete.setBold(false);
	// athlete.setFontSize(12);
	// // 设置使用何种字体
	// athlete.setFontFamily("宋体");
	// // 设置上下两行之间的间距
	// athlete.setTextPosition(30);
	// athlete.setText("运动员:\t");
	// if(j==0){
	// int count=1;
	// for(Athlete ath:ballReportTable.getAthletes()){
	// if(ath!=null&&ath.getId()!=null&&ath.getIdCard()!=null){
	// athlete.setText(ath.getName()+"\t");
	// DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	// athlete.setText(df.format(ath.getBirthday())+"\t");
	// if(count%2==0){
	// athlete.addBreak();
	// athlete.setText("\t\t");
	// }
	// count++;
	//
	// }
	//
	// }
	// }else{
	// int count=1;
	// for(AthleteEntity ath:ballReportTable.getAthletesOfWoman()){
	// if(ath!=null&&ath.getId()!=null&&ath.getIdCard()!=null){
	// athlete.setText(ath.getName()+"\t");
	// DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	// athlete.setText(df.format(ath.getBirthday())+"\t");
	// if(count%2==0){
	// athlete.addBreak();
	// athlete.setText("\t\t");
	// }
	// count++;
	// }
	//
	// }
	// }
	//
	// }
	//
	//
	// }
	//
	// FileOutputStream out;
	// try {
	// //将文件保存到本地
	// String fileName="队伍信息.doc";
	// out = new
	// FileOutputStream(request.getSession().getServletContext().getRealPath("/download"+"\\"+fileName));
	// doc.write(out);
	// out.close();
	// // 以下代码可进行文件下载
	// response.reset();
	// response.setContentType("application/x-msdownloadoctet-stream;charset=utf-8");
	// response.setHeader("Content-Disposition","attachment;filename=\"" +
	// URLEncoder.encode(fileName, "UTF-8"));
	// OutputStream cout = response.getOutputStream();
	// doc.write(cout);
	// cout.flush();
	// cout.close();
	//
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	// @SuppressWarnings({ "unchecked", "rawtypes" })
	// @RequestMapping("/teamlist")
	// public void teamList(HttpServletRequest request, HttpServletResponse
	// response) throws Exception {
	// //String[] ids = request.getParameter("datas").split(",");
	// String[] datas=null;
	// if(request.getParameter("datas")!=null)
	// datas=request.getParameter("datas").split(",");
	// String id=request.getParameter("id");
	// List list = new ArrayList();
	// if(id==null){//批量导出
	//
	// if (datas[3].equals("-1")) {
	// list = cs.getAllRecors(
	// "select teamId from SignInfo where competitionId in (select id from CompetitionInfo where competitionId=?)",
	// new Object[] { Integer.parseInt(datas[2]) });
	// } else {
	// list = cs.getAllRecors(
	// "select teamId from SignInfo where competitionId = (select id from CompetitionInfo where groupId=?)",
	// new Object[] { Integer.parseInt(datas[3]) });
	// }
	// if (list.size() == 0) {
	// request.setAttribute("message", "该项目目前没有人报名");
	// request.getRequestDispatcher("/error.jsp").forward(request, response);
	// return;
	// }
	// }else{//单个队伍名单导出
	// list.add(Integer.parseInt(id));
	// }
	// XWPFDocument doc = new XWPFDocument();
	// XWPFParagraph teamList = doc.createParagraph();
	// teamList.setAlignment(ParagraphAlignment.CENTER);
	// teamList.setVerticalAlignment(TextAlignment.TOP);
	// XWPFRun team_list = teamList.createRun();
	// // 设置字体是否加粗
	// team_list.setBold(true);
	// team_list.setFontSize(20);
	// // 设置使用何种字体
	// team_list.setFontFamily("Courier");
	// // 设置上下两行之间的间距
	// team_list.setTextPosition(20);
	// team_list.setText("代表队名单");
	// List<TeamInfo> teamInfo = (List<TeamInfo>)
	// cs.fuzzyQuery("from TeamInfo where id in (:params) order by company",
	// null, list);
	// for (int j = 0; j < teamInfo.size(); j++) {
	// String tablename = teamInfo.get(j).getTeamName().split("-")[1];
	// String name = teamInfo.get(j).getTeamName().split("-")[1];
	// String company = teamInfo.get(j).getCompany();
	// String leader = teamInfo.get(j).getTeamLeader();
	// String phoneNum = teamInfo.get(j).getTeamContact();
	// String coach=teamInfo.get(j).getTeamCaptain();
	// if(coach!=null){
	// String[] coachs = coach.split(";");
	// coach="";
	// for(String str:coachs){
	// if(!(str.toLowerCase().contains("XX"))){
	// coach+=str+" ";
	// }
	// }
	// }
	// if(leader!=null){
	// String[] leaders = leader.split(";");
	// leader="";
	// for(String str:leaders){
	// if(!(str.toLowerCase().contains("XX"))){
	// leader+=str+" ";
	// }
	// }
	// }
	//
	// String[] althlete_set = teamInfo.get(j).getAthleteIdSet().split(";");
	// List<String> althleteList = new ArrayList<String>();
	// for(int i=0;i<althlete_set.length;i++){
	// if(althlete_set[i].equals("-")){
	// continue;
	// }
	// String althleteIdCard=althlete_set[i].split("-")[0];
	// String birthday=althleteIdCard.substring(6, 10) + "-"+
	// althleteIdCard.substring(10, 12) + "-" + althleteIdCard.substring(12,
	// 14);
	// String athlete_name=althlete_set[i].split("-")[1];
	// String althleteInfo=athlete_name+" "+birthday+" "+althleteIdCard;
	// althleteList.add(althleteInfo);
	// }
	// XWPFParagraph teamPlace = doc.createParagraph();
	// XWPFParagraph teamGender = doc.createParagraph();
	// XWPFParagraph teamLeader = doc.createParagraph();
	// XWPFParagraph teamContact = doc.createParagraph();
	// XWPFParagraph teamCoach = doc.createParagraph();
	// XWPFParagraph athletes = doc.createParagraph();
	// // 设置字体对齐方式
	//
	// teamPlace.setAlignment(ParagraphAlignment.CENTER);
	// teamPlace.setVerticalAlignment(TextAlignment.TOP);
	// teamGender.setAlignment(ParagraphAlignment.LEFT);
	// teamGender.setVerticalAlignment(TextAlignment.TOP);
	// teamLeader.setAlignment(ParagraphAlignment.LEFT);
	// teamLeader.setVerticalAlignment(TextAlignment.TOP);
	// teamContact.setAlignment(ParagraphAlignment.LEFT);
	// teamContact.setVerticalAlignment(TextAlignment.TOP);
	// teamCoach.setAlignment(ParagraphAlignment.LEFT);
	// teamCoach.setVerticalAlignment(TextAlignment.TOP);
	// athletes.setAlignment(ParagraphAlignment.LEFT);
	// athletes.setVerticalAlignment(TextAlignment.TOP);
	// // 第一页要使用p1所定义的属性
	//
	// XWPFRun team_place = teamPlace.createRun();
	// XWPFRun team_gender = teamGender.createRun();
	// XWPFRun team_leader = teamLeader.createRun();
	// XWPFRun team_contact = teamContact.createRun();
	// XWPFRun team_coach = teamCoach.createRun();
	// XWPFRun athlete = athletes.createRun();
	//
	// // 设置字体是否加粗
	// team_place.setBold(true);
	// team_place.setFontSize(15);
	// // 设置使用何种字体
	// team_place.setFontFamily("Courier");
	// // 设置上下两行之间的间距
	// team_place.setTextPosition(20);
	// team_place.setText(company);
	// // 设置字体是否加粗
	// team_gender.setBold(true);
	// team_gender.setFontSize(12);
	// // 设置使用何种字体
	// team_gender.setFontFamily("宋体");
	// // 设置上下两行之间的间距
	// team_gender.setTextPosition(30);
	// team_gender.setText(tablename);
	// // 设置字体是否加粗
	// team_leader.setBold(false);
	// team_leader.setFontSize(12);
	// // 设置使用何种字体
	// team_leader.setFontFamily("宋体");
	// // 设置上下两行之间的间距
	// team_leader.setTextPosition(30);
	// team_leader.setText("领队:\t");
	// team_leader.setText(leader);
	//
	// // 设置字体是否加粗
	// team_contact.setBold(false);
	// team_contact.setFontSize(12);
	// // 设置使用何种字体
	// team_contact.setFontFamily("宋体");
	// // 设置上下两行之间的间距
	// team_contact.setTextPosition(30);
	// team_contact.setText("联系人:\t");
	// team_contact.setText(phoneNum.toLowerCase().contains("xx") ? "\t" :
	// phoneNum + "\t");
	// // 设置字体是否加粗
	// team_coach.setBold(false);
	// team_coach.setFontSize(12);
	//
	// // 设置使用何种字体
	// team_coach.setFontFamily("宋体");
	// // 设置上下两行之间的间距
	// team_coach.setTextPosition(30);
	// team_coach.setText("教练:\t");
	// team_coach.setText(coach);
	// /*team_coach.setText(coach2.toLowerCase().contains("xx") ? "" : coach2 +
	// "\t");
	// team_coach.setText(coach3.toLowerCase().contains("xx") ? "" : coach3);*/
	// // 设置字体是否加粗
	// athlete.setBold(false);
	// athlete.setFontSize(12);
	// // 设置使用何种字体
	// athlete.setFontFamily("宋体");
	// // 设置上下两行之间的间距
	// athlete.setTextPosition(30);
	// athlete.setText("运动员:\t");
	// for (int k = 0; k < althleteList.size(); k++) {
	// athlete.setText(althleteList.get(k));
	// athlete.addBreak();
	// athlete.setText("\t\t");
	//
	// }
	//
	// }
	//
	// FileOutputStream out;
	// try {
	// // 将文件保存到本地
	// String fileName = "队伍信息.doc";
	// out = new FileOutputStream(
	// request.getSession().getServletContext().getRealPath("/download" + "\\" +
	// fileName));
	// doc.write(out);
	// out.close();
	// // 以下代码可进行文件下载
	// response.reset();
	// response.setContentType("application/x-msdownloadoctet-stream;charset=utf-8");
	// response.setHeader("Content-Disposition", "attachment;filename=\"" +
	// URLEncoder.encode(fileName, "UTF-8"));
	// OutputStream cout = response.getOutputStream();
	// doc.write(cout);
	// cout.flush();
	// cout.close();
	//
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//

	@RequestMapping("/teamlist")
	public void teamList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Integer year = Integer.parseInt(request.getParameter("year"));
		Integer type = Integer.parseInt(request.getParameter("type"));
		Integer competitionId = Integer.parseInt(request
				.getParameter("competitionId"));
		Integer groupId = Integer.parseInt(request.getParameter("groupId"));

		String id = request.getParameter("ids");
		System.out.println("year" + year + "type" + type + "cid"
				+ competitionId + "gid" + groupId + "ids" + id);
		List list = new ArrayList();
		if (id == null) {// 批量导出

			if (groupId == -1) {
				list = signInfoService.getTeamListByCompetitonId(competitionId);

			} else {
				list = signInfoService.getTeamListByGroupId(groupId);
			}
			if (list.size() == 0) {
				request.setAttribute("message", "该项目目前没有人报名");
				request.getRequestDispatcher("/error.jsp").forward(request,
						response);
				return;
			}
		} else {// 单个队伍名单导出
			id = id.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "");
			String idArray[] = id.split(",");
			for (int i = 0; i < idArray.length; i++) {
				System.out.println(Integer.parseInt(idArray[i])+"ss");
				list.add(Integer.parseInt(idArray[i]));
			}

		}
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet();
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("报名单位");
		row.createCell(1).setCellValue("领队");
		row.createCell(2).setCellValue("联系电话");
		row.createCell(3).setCellValue("教练");
		row.createCell(4).setCellValue("运动员");
		row.createCell(5).setCellValue("出生年月");
		row.createCell(6).setCellValue("身份证号");
		row.createCell(7).setCellValue("组别");
		// 设置列的宽度
		sheet.setColumnWidth(0, 255 * 24);
		sheet.setColumnWidth(1, 255 * 10);
		sheet.setColumnWidth(2, 255 * 15);
		sheet.setColumnWidth(3, 255 * 10);
		sheet.setColumnWidth(4, 255 * 15);
		sheet.setColumnWidth(5, 255 * 15);
		sheet.setColumnWidth(6, 255 * 20);
		sheet.setColumnWidth(7, 255 * 50);
		// }
		// 定义单元格格式，添加单元格表样式，并添加到工作簿
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		// 设置单元格水平对齐类型
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//

		cellStyle.setWrapText(true);// 指定单元格自动换行

		List<TeamInfoEntity> teamInfo = teamInfoService.getTeamList(list);
		int k = 1;
		System.out.println("list size"+list.size()+"team size"+teamInfo.size());
		for (int i = 0; i < list.size()&& i<teamInfo.size(); i++) {
			System.out.println("for"+i);
			String name = teamInfo.get(i).getTeamName().split("-")[1];
			String company = teamInfo.get(i).getCompany();
			String leader = teamInfo.get(i).getTeamLeader();
			String phoneNum = teamInfo.get(i).getTeamContact();
			String coach = teamInfo.get(i).getTeamCaptain();

			String[] althlete_set = teamInfo.get(i).getAthleteIdSet()
					.split(";");
			for (int j = 0; j < althlete_set.length; j++) {
				String althleteIdCard = althlete_set[j].split("-")[0];
				String birthday = althleteIdCard.substring(6, 10) + "-"
						+ althleteIdCard.substring(10, 12) + "-"
						+ althleteIdCard.substring(12, 14);
				String athlete_name = althlete_set[j].split("-")[1];
				row = sheet.createRow(k++);
				row.createCell(0).setCellValue(company);
				row.createCell(1).setCellValue(leader);
				row.createCell(2).setCellValue(phoneNum);
				row.createCell(3).setCellValue(coach);
				row.createCell(4).setCellValue(athlete_name);
				row.createCell(5).setCellValue(birthday);
				row.createCell(6).setCellValue(althleteIdCard);
				row.createCell(7).setCellValue(name);

			}
		}

		FileOutputStream out;
		try {
			// 将文件保存到本地
			String fileName = "队伍信息.xls";
			out = new FileOutputStream(request.getSession().getServletContext()
					.getRealPath("/download" + "\\" + fileName));
			workbook.write(out);
			out.close();
			// 以下代码可进行文件下载
			response.reset();
			response.setContentType("application/x-msdownloadoctet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ URLEncoder.encode(fileName, "UTF-8"));
			OutputStream cout = response.getOutputStream();
			workbook.write(cout);
			cout.flush();
			cout.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * @RequestMapping("/race_number") public void RaceNumber(HttpServletRequest
	 * request, HttpServletResponse response) { String[] datas =
	 * request.getParameter("datas").split(","); String competitionType =
	 * Integer.parseInt(datas[1]) == 0 ? "竞技赛事" : "群众赛事"; String[] place = new
	 * String[] { "南湖", "秀洲", "嘉善", "平湖", "海盐", "海宁", "桐乡" }; String[]
	 * shortPlace = new String[] { "nh", "xz", "js", "ph", "hy", "hn", "tx" };
	 * CompetitionName competitionName = (CompetitionName)
	 * cs.getResultOne("from CompetitionName where id=?", new Object[] {
	 * Integer.parseInt(datas[2]) }); // competitionName.clone(); Map<String,
	 * Object> dataMap = new HashMap<String, Object>(); int leaderNumOfMan = 0;
	 * int leaderNumOfWoman = 0; int coachNumOfMan = 0; int coachNumOfWoman = 0;
	 * int athleteNumOfMan = 0; int athleteNumOfWoman = 0; int total = 0;//
	 * 参赛总人数 for (int i = 0; i < place.length; i++) { List<?> lists =
	 * cs.fuzzyQuery("from TeamInfo where teamName like ?", new Object[] { "%" +
	 * place[i] + "%" + competitionName.getName() + "%" + competitionType + "%"
	 * }); if (lists != null && lists.size() > 0) { int totalNum = 0; int count
	 * = 0; for (Object obj : lists) { count++; TeamInfo teamInfo = (TeamInfo)
	 * obj; int leaderNum = 0; if (teamInfo.getTeamName().contains("男")) { //
	 * 领队人数 String leader = teamInfo.getTeamLeader(); if
	 * (leader.trim().equals("") || leader.trim() .contains("略")
	 * ||leader.trim().toLowerCase(). contains("xx") ) { leaderNum = 0; } else
	 * if (leader.trim().contains("、")) { for (String str : leader.split("、")) {
	 * if (!(leader.toLowerCase().contains("xx"))) { leaderNum++; } } } else if
	 * (leader.trim().contains(",")) { for (String str : leader.split(",")) { if
	 * (!(leader.toLowerCase().contains("xx"))) { leaderNum++; } } } else if
	 * (leader.trim().contains(";")) { for (String str : leader.split(";")) { if
	 * (!(leader.toLowerCase().contains("xx"))) { leaderNum++; } } } else {
	 * leaderNum = 1; }
	 * 
	 * dataMap.put(shortPlace[i] + "Num1", leaderNum); leaderNumOfMan +=
	 * leaderNum; // 教练人数统计 int coachNum = 0; if
	 * (teamInfo.getTeamCaptain().trim().equals("") ||
	 * teamInfo.getTeamCaptain().trim().contains("略")) { coachNum = 0;
	 * 
	 * } else { // 将教练根据分号切割成一个数组 String[] nums =
	 * teamInfo.getTeamCaptain().split(";"); for (String num : nums) {//
	 * 遍历数组，如果数组中的元素不存在xx,则教练数加1 if (!(num.toLowerCase().contains("xx"))) {
	 * coachNum++; } } // coachNum=teamInfo.getTeamCaptain().split(";").length;
	 * } coachNumOfMan += coachNum; dataMap.put(shortPlace[i] + "Num3",
	 * coachNum); String[] athletes = teamInfo.getAthleteIdSet().split(";");
	 * dataMap.put(shortPlace[i] + "Num5", athletes.length); athleteNumOfMan +=
	 * athletes.length; totalNum += leaderNum + coachNum + athletes.length;
	 * 
	 * } else { // 领队人数 String leader = teamInfo.getTeamLeader(); if
	 * (leader.trim().equals("") || leader.trim() .contains("略")
	 * ||leader.trim().toLowerCase(). contains("xx") ) { leaderNum = 0; } else
	 * if (leader.trim().contains("、")) { for (String str : leader.split("、")) {
	 * if (!(leader.toLowerCase().contains("xx"))) { leaderNum++; } } } else if
	 * (leader.trim().contains(",")) { for (String str : leader.split(",")) { if
	 * (!(leader.toLowerCase().contains("xx"))) { leaderNum++; } } } else if
	 * (leader.trim().contains(";")) { for (String str : leader.split(";")) { if
	 * (!(leader.toLowerCase().contains("xx"))) { leaderNum++; } } } else {
	 * leaderNum = 1; } leaderNumOfWoman += leaderNum; dataMap.put(shortPlace[i]
	 * + "Num2", leaderNum); // 教练人数统计 int coachNum = 0; if
	 * (teamInfo.getTeamCaptain().trim().equals("") ||
	 * teamInfo.getTeamCaptain().trim().contains("略")) { coachNum = 0;
	 * 
	 * } else { String[] nums = teamInfo.getTeamCaptain().split(";"); for
	 * (String num : nums) { if (!(num.toLowerCase().contains("xx"))) {
	 * coachNum++; } } // coachNum=teamInfo.getTeamCaptain().split(";").length;
	 * } coachNumOfWoman += coachNum; dataMap.put(shortPlace[i] + "Num4",
	 * coachNum); String[] athletes = teamInfo.getAthleteIdSet().split(";");
	 * dataMap.put(shortPlace[i] + "Num6", athletes.length); athleteNumOfWoman
	 * += athletes.length; totalNum += leaderNum + coachNum + athletes.length; }
	 * } if (count == 1) { TeamInfo t = (TeamInfo) lists.get(0); if
	 * (t.getTeamName().contains("男")) { for (int j = 2; j <= 6; j += 2) {
	 * dataMap.put(shortPlace[i] + "Num" + j, 0); } } else {
	 * 
	 * for (int j = 1; j <= 5; j += 2) { dataMap.put(shortPlace[i] + "Num" + j,
	 * 0); } } } // }
	 * 
	 * total += totalNum; dataMap.put(shortPlace[i] + "Total", totalNum); } else
	 * { for (int j = 1; j <= 6; j++) { dataMap.put(shortPlace[i] + "Num" + j,
	 * 0); } dataMap.put(shortPlace[i] + "Total", 0);
	 * 
	 * }
	 * 
	 * } dataMap.put("total1", leaderNumOfMan); dataMap.put("total2",
	 * leaderNumOfWoman); dataMap.put("total3", coachNumOfMan);
	 * dataMap.put("total4", coachNumOfWoman); dataMap.put("total5",
	 * athleteNumOfMan); dataMap.put("total6", athleteNumOfWoman);
	 * dataMap.put("total", total); DocumentHandler doc = new DocumentHandler();
	 * doc.createDoc(dataMap,
	 * request.getSession().getServletContext().getRealPath("/download") + "\\"
	 * + "参赛人数统计表.doc", "totalList2.ftl",response); }
	 */

	@RequestMapping("/total_list")
	public void totalList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Integer year = Integer.parseInt(request.getParameter("year"));
		Integer type = Integer.parseInt(request.getParameter("type"));
		Integer competitionId = Integer.parseInt(request
				.getParameter("competitionId"));
		Integer groupId = Integer.parseInt(request.getParameter("groupId"));
		System.out.println("year" + year + "type" + type + "comp"
				+ competitionId + "groupId" + groupId);

		String competitionType = type == 0 ? "竞技赛事" : "群众赛事";

		List<Integer> list = null;
		if (groupId == -1) {
			list = signInfoService.getTeamListByCompetitonId(competitionId);

		} else {
			list = signInfoService.getTeamListByGroupId(groupId);
		}
		System.out.println("list size" + list.size());
		if (list.size() == 0) {
			request.setAttribute("message", "该项目目前没有人报名");
			request.getRequestDispatcher("/error.jsp").forward(request,
					response);
			return;
		}
		List<String> companys = teamInfoService.getCompanyList(list);
		System.out.println("companys size" + companys.size());
		CompetitionNameEntity competitionName = competitionNameService
				.queryObject(competitionId);
		System.out.println("competitionName" + competitionName.getName());
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("title", competitionName.getName());
		List<Map<String, Object>> newsList = new ArrayList<Map<String, Object>>();
		int leaderTotalNum = 0;
		int coachTotalNum = 0;
		int athleteTotalOfMan = 0;
		int athleteTotalOfWoman = 0;
		int total = 0;// 参赛总人数
		for (int i = 0; i < companys.size(); i++) {
			int athleteNumOfMan = 0;
			int athleteNumOfWoman = 0;
			Set<String> womanSet = new HashSet<String>();
			Set<String> manSet = new HashSet<String>();
			Set<String> leaderSet = new HashSet<String>();
			Set<String> coachSet = new HashSet<String>();
			List<?> teamInfoList = null;
			System.out.println("year" + year + "company" + i + ":"
					+ companys.get(i));
			teamInfoList = teamInfoService.fuzzyQuery(year + "",
					companys.get(i), list);
			System.out.println("teamList" + i + "--" + teamInfoList.size());

			if (teamInfoList != null && teamInfoList.size() > 0) {
				int totalNum = 0;
				for (Object obj : teamInfoList) {
					TeamInfoEntity teamInfo = (TeamInfoEntity) obj;
					String leader = teamInfo.getTeamLeader();
					if (leader.trim().equals("")) {
					} else if (leader.trim().contains(",")) {
						String[] strArray = leader.trim().split(",");
						for (String str : strArray) {
							leaderSet.add(str);
						}
					} else if (leader.trim().contains(";")) {
						String[] strArray = leader.trim().split(";");
						for (String str : strArray) {
							leaderSet.add(str);
						}
					} else if (leader.trim().contains(" ")) {
						String[] strArray = leader.trim().split(" ");
						for (String str : strArray) {
							leaderSet.add(str);
						}
					} else if (leader.trim().contains("、")) {
						String[] strArray = leader.trim().split("、");
						for (String str : strArray) {
							leaderSet.add(str);
						}
					} else {
						leaderSet.add(leader);
					}
					String coach = teamInfo.getTeamCaptain();
					if (coach.trim().equals("")) {
					} else if (coach.trim().contains(",")) {
						String[] strArray = coach.trim().split(",");
						for (String str : strArray) {
							coachSet.add(str);
						}
					} else if (coach.trim().contains(";")) {
						String[] strArray = coach.trim().split(";");
						for (String str : strArray) {
							coachSet.add(str);
						}
					} else if (coach.trim().contains(" ")) {
						String[] strArray = coach.trim().split(" ");
						for (String str : strArray) {
							coachSet.add(str);
						}
					} else if (coach.trim().contains("、")) {
						String[] strArray = coach.trim().split("、");
						for (String str : strArray) {
							coachSet.add(str);
						}
					} else {
						coachSet.add(coach);
					}
					String athletes = teamInfo.getAthleteIdSet();
					for (String str : athletes.split(";")) {

						int sex = Integer.parseInt(str.substring(16, 17));
						if (sex % 2 == 0) {
							womanSet.add(str);
						} else {
							manSet.add(str);
						}

					}

				}
				athleteNumOfMan = manSet.size();
				athleteNumOfWoman = womanSet.size();
				totalNum = leaderSet.size() + coachSet.size() + manSet.size()
						+ womanSet.size();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("company", companys.get(i));
				map.put("leader", leaderSet.size());
				map.put("coach", coachSet.size());
				map.put("man", athleteNumOfMan);
				map.put("woman", athleteNumOfWoman);
				map.put("total", totalNum);
				newsList.add(map);
				/*
				 * dataMap.put(shortPlace[i]+"Total", totalNum);
				 * dataMap.put(shortPlace[i]+"Num1", leaderSet.size());
				 * dataMap.put(shortPlace[i]+"Num2", coachSet.size());
				 * dataMap.put(shortPlace[i]+"Num3", athleteNumOfMan);
				 * dataMap.put(shortPlace[i]+"Num4", athleteNumOfWoman);
				 */

			} /*
			 * else { for (int j = 1; j <= 4; j++) { dataMap.put(shortPlace[i] +
			 * "Num" + j, 0); } dataMap.put(shortPlace[i] + "Total", 0);
			 * 
			 * }
			 */
			leaderTotalNum += leaderSet.size();
			coachTotalNum += coachSet.size();
			athleteTotalOfMan += athleteNumOfMan;
			athleteTotalOfWoman += athleteNumOfWoman;

		}
		total = leaderTotalNum + coachTotalNum + athleteTotalOfMan
				+ athleteTotalOfWoman;
		dataMap.put("total1", leaderTotalNum);
		dataMap.put("total2", coachTotalNum);
		dataMap.put("total3", athleteTotalOfMan);
		dataMap.put("total4", athleteTotalOfWoman);
		dataMap.put("total", total);
		dataMap.put("newsList", newsList);
		DocumentHandler doc = new DocumentHandler();
		doc.createDoc(dataMap, request.getSession().getServletContext()
				.getRealPath("/download")
				+ "\\" + "参赛人数统计表.doc", "totallist.ftl", response);
	}

}

class DocumentHandler {
	private Configuration configuration = null;

	public DocumentHandler() {
		configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
	}

	public void createDoc(Map<String, Object> dataMap, String fileName,
			String templateName, HttpServletResponse response) {
		// dataMap 要填入模本的数据文件
		// 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
		// 这里我们的模板是放在template包下面
		configuration.setClassForTemplateLoading(this.getClass(), "/");
		Template t = null;
		try {
			// test.ftl为要装载的模板
			t = configuration.getTemplate(templateName);
			// 输出文档路径及名称
			File outFile = new File(fileName);

			Writer out = null;
			FileOutputStream fos = null;
			fos = new FileOutputStream(outFile);
			OutputStreamWriter oWriter = new OutputStreamWriter(fos, "UTF-8");
			// 这个地方对流的编码不可或缺，使用main（）单独调用时，应该可以，但是如果是web请求导出时导出后word文档就会打不开，并且包XML文件错误。主要是编码格式不正确，无法解析。
			// out = new BufferedWriter(new OutputStreamWriter(new
			// FileOutputStream(outFile)));
			out = new BufferedWriter(oWriter);
			t.process(dataMap, out);
			out.close();
			fos.close();
			// 以下代码可进行文件下载
			FileInputStream in = new FileInputStream(fileName);
			System.out.println("------Response start-----");
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ URLEncoder.encode("参赛人数统计表.doc", "UTF-8"));
			OutputStream cout = response.getOutputStream();
			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = in.read(buf)) != -1) {
				cout.write(buf, 0, len);
			}
			System.out.println("------Response end-----");
			in.close();
			cout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
