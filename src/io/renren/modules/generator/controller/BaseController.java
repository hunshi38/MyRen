package io.renren.modules.generator.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.common.utils.R;
import io.renren.modules.generator.entity.CompetitionNameEntity;
import io.renren.modules.generator.entity.NoticeBoardEntity;
import io.renren.modules.generator.service.CompetitionNameService;
import io.renren.modules.generator.service.NoticeBoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public class BaseController {
//	@Autowired
//	private CompetitionNameService competitionNameService;
//	@Autowired
//	private NoticeBoardService noticeBoardService;
//	
//	
//	@RequestMapping(value="/listall",method = RequestMethod.GET)
//	public R list(){
//		Map<String, Object> map = new HashMap<String,Object>();
//		List<CompetitionNameEntity> competitionNameList = competitionNameService.queryList(map);
//		
//		return R.ok().put("list",competitionNameList );
//	}
//	
//	@ResponseBody
//	@RequestMapping("/infobycid/{id}")
//	public R getInfoByCid(@PathVariable("id") Integer id){
//		NoticeBoardEntity noticeBoard = noticeBoardService.queryObjectByCid(id);
//		
//		return R.ok().put("noticeBoard", noticeBoard);
//	}
}
