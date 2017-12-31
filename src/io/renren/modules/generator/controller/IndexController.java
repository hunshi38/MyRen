package io.renren.modules.generator.controller;

import io.renren.common.utils.R;
import io.renren.modules.generator.entity.AthleteEntity;
import io.renren.modules.generator.entity.CheckStatusEntity;
import io.renren.modules.generator.entity.CompetitionInfoEntity;
import io.renren.modules.generator.entity.CompetitionNameEntity;
import io.renren.modules.generator.entity.GroupNameEntity;
import io.renren.modules.generator.entity.SignInfoEntity;
import io.renren.modules.generator.entity.TeamInfoEntity;
import io.renren.modules.generator.entity.param.PersonInfoParam;
import io.renren.modules.generator.entity.param.PublicSignParam;
import io.renren.modules.generator.service.AthleteService;
import io.renren.modules.generator.service.CheckStatusService;
import io.renren.modules.generator.service.CompetitionInfoService;
import io.renren.modules.generator.service.CompetitionNameService;
import io.renren.modules.generator.service.GroupNameService;
import io.renren.modules.generator.service.SignInfoService;
import io.renren.modules.generator.service.SignTableService;
import io.renren.modules.generator.service.TeamInfoService;
import io.renren.modules.sys.shiro.ShiroUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller

public class IndexController{
	@Autowired
	private TeamInfoService teamInfoService;
	@Autowired
	private CompetitionNameService competitionNameService;
	@Autowired
	private AthleteService athleteService;
	@Autowired
	private SignInfoService signInfoService;
	@Autowired
	private CheckStatusService checkStatusService;
	@Autowired
	private GroupNameService groupNameService;
	@Autowired
	private SignTableService signTableService;
	//首页
     @RequestMapping(value = "user/index")
	public String index() {
		
		return "modules/before/page.html";
	} 
     @RequestMapping(value ="user/noticelist")
     public String noticelist(){
    	 return "modules/before/list.html";
     }
     @RequestMapping(value="user/head")
     public String mainHead(){
    	 return "modules/before/head.html";
     }
     
     //公告详情页
    @RequestMapping(value = "user/single")
	public String noticeDetail() {
		System.out.println("index request");
		return "modules/before/single.html";
	}
 // 群众报名处理
 	@RequestMapping(value = "user/public")
 	public String publicIndex(){
 		
 		return "modules/before/publicSign.html";
 	}
 	@RequestMapping(value = "user/grade")
 	public String gradeIndex(){
 		return "modules/before/grade_query.html";
 	}
	@RequestMapping(value = "user/show")
 	public String showIndex(){
 		return "modules/before/show.html";
 	}
	// 群众报名处理
 	@ResponseBody
	@RequestMapping(value = "user/publicSubmit")
	public R publicSign(@RequestBody PublicSignParam publicSignParam)
			throws ParseException {
 		System.out.println("baoming start-----");
 		Integer competitionId = publicSignParam.getCompetitionId();
 		Integer eventId = publicSignParam.getEventId();
 		Integer groupId = publicSignParam.getGroupId();
 		CompetitionNameEntity competitionNameEntity = competitionNameService.queryObject(competitionId);
 		//报名时间判断
 		if(competitionNameEntity!=null){
 			
 			String startStr = competitionNameEntity.getSignStartDate();
 	        String endStr = competitionNameEntity.getSignEndDate();
 	     
 	        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");  
 	        Date startDate = dateFormat.parse(startStr);  
 	        Date endDate = dateFormat.parse(endStr);  
 	        Date curDate = new Date();
 	       int a = curDate.compareTo(startDate);
 	       int b = curDate.compareTo(endDate);
 	       if(!(a==1 && b ==-1)){
 	    	   return R.error(1, "目前不能报名");
 	       }
 			
 		}
 		
		TeamInfoEntity teamInfo = new TeamInfoEntity();
		PersonInfoParam persons[] = publicSignParam.getPersons();
		String teamSignType="";
		if (persons.length > 1) {
			teamInfo.setSignType(3);
			teamSignType = "团体";
		} else if (persons.length == 1) {
			teamInfo.setSignType(1);
			teamSignType = "个人";
		}
		String athleteSet = "";

		// 报名人员入表
		for (int i = 0; i < persons.length; i++) {
			PersonInfoParam p = persons[i];
			
			String name = p.getName().trim();
			String idCard = p.getIdCard().trim();
			String phoneNumber = p.getPhoneNumber().trim();
			String company = p.getCompany().trim();
			String sex = p.getSex().trim();
			Integer gender=0;
			if(sex.equals("女")){
				gender = 1;
			}
			
			SignInfoEntity signInfo2 =signInfoService.queryObjectByIdCardAndCompetitionId(competitionId, idCard);
			
			if(signInfo2!=null){
				System.out.println(signInfo2.getTeamId()+"teamid");
				return R.error(2, idCard+"重复报名");
			}
			AthleteEntity ath = athleteService.queryObjectByCardId(idCard);
			if (i == 0) {
				teamInfo.setTeamCaptain(name);
				teamInfo.setTeamContact(phoneNumber);
				teamInfo.setTeamLeader(name);
				teamInfo.setCompany(company);
				
			}
			if (i == persons.length - 1) {
				athleteSet += idCard + "-" + name+"-"+teamSignType;
			}else{
				athleteSet += idCard + "-" + name + "-"+teamSignType+";";
			}

			if (ath == null) { // 表中没有该人信息
				AthleteEntity athlete = new AthleteEntity();
				athlete.setName(name);
				athlete.setIdCard(idCard);
				String yy = idCard.substring(6, 10);
				String mm = idCard.substring(10, 12);
				String dd = idCard.substring(12, 14);
				Date birth = new SimpleDateFormat("yyyy-MM-dd").parse(yy + "-"
						+ mm + "-" + dd);
				athlete.setBirthday(birth);
				athlete.setPhoneNumber(phoneNumber);
				athlete.setCompany(company);
				athlete.setRegisterCode("未注册");
				athlete.setCoach("xxx/略");
				athlete.setGender(gender);
				athleteService.save(athlete);
			}
			

		}
//		// teamName未定
		String teamName="";
		String competitionName = competitionNameEntity.getName();
		teamName+=competitionName+"-";
		GroupNameEntity groupNameEntity = groupNameService.queryObject(groupId);
		teamName+= groupNameEntity.getGroupName()+"-"+"群众赛事";
		teamInfo.setTeamName(teamName);
		teamInfo.setAthleteIdSet(athleteSet);
		
		teamInfoService.save(teamInfo);
		Integer teamId = teamInfo.getId();
		SignInfoEntity signInfo = new SignInfoEntity();
		signInfo.setCompetitionId(publicSignParam.getCompetitionId());
		signInfo.setTeamId(teamId);
		signInfo.setCheckStatus(0);
		signInfo.setSignTime(new Timestamp(System.currentTimeMillis())); //设置报名时间
		signInfoService.save(signInfo);
		System.out.println("end ---------");

		return R.ok();
	}
    
    

}
