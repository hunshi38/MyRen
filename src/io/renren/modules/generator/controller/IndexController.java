package io.renren.modules.generator.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller

public class IndexController{
	@Autowired
	
@RequestMapping(value={"/indexeee"})
public ModelAndView index(HttpServletRequest request, HttpServletResponse response){
	

	 ModelAndView view = new ModelAndView("index");
	
	return view;
	}


}
