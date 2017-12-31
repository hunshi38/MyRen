package io.renren.modules.generator.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
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
import io.renren.modules.generator.entity.RegisterAthleteEntity;
import io.renren.modules.generator.service.RegisterAthleteService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;


/**
 * 注册运动员
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
@RestController
@RequestMapping("registerathlete")
public class RegisterAthleteController {
	@Autowired
	private RegisterAthleteService registerAthleteService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("registerathlete:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<RegisterAthleteEntity> registerAthleteList = registerAthleteService.queryList(query);
		int total = registerAthleteService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(registerAthleteList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{idCard}")
	@RequiresPermissions("registerathlete:info")
	public R info(@PathVariable("idCard") String idCard){
		RegisterAthleteEntity registerAthlete = registerAthleteService.queryObject(idCard);
		
		return R.ok().put("registerAthlete", registerAthlete);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody RegisterAthleteEntity registerAthlete){
		System.out.println("save a object");
		registerAthleteService.save(registerAthlete);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("registerathlete:update")
	public R update(@RequestBody RegisterAthleteEntity registerAthlete){
		registerAthleteService.update(registerAthlete);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("registerathlete:delete")
	public R delete(@RequestBody String[] idCards){
		registerAthleteService.deleteBatch(idCards);
		
		return R.ok();
	}
	@RequestMapping("/uploadexcel")
	public R uploadExcel(@RequestParam("file") MultipartFile file,
			ModelMap model,HttpServletRequest request, HttpServletResponse response) 
			throws Exception, IOException{
		System.out.println("jjjjj");
		
		//上传 
		String path = request.getSession().getServletContext().getRealPath("download");
        String fileName = UUID.randomUUID()+"-"+file.getOriginalFilename();

        System.out.println(path);
        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }

        //保存
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("fileUrl", request.getContextPath()+"/download/"+fileName);
        SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
        
        List<String[]> list=readByPoi(path+"\\"+fileName,0);
        for(int i=0;i<list.size();i++){
        	RegisterAthleteEntity registerAthlete=new RegisterAthleteEntity();
        	registerAthlete.setIdCard(list.get(i)[0].trim());
        	registerAthlete.setRegisterCode(list.get(i)[1].trim());
        	registerAthlete.setName(list.get(i)[2].trim());
        	registerAthlete.setGender(list.get(i)[3].trim().equals("男")?0:1);
        	registerAthlete.setBirthday(sim.parse(list.get(i)[4].trim()));
        	registerAthlete.setCompany(list.get(i)[5].trim());
    		save(registerAthlete);
        }
        return R.ok();
        
	}
	
	public static  List<String[]> readByPoi(String fileName,int sheetNumber) {
    	org.apache.poi.ss.usermodel.Workbook workbook = null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			String fileType=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		        if (fileType.equals("xls")) {  
		        	workbook = new HSSFWorkbook(new FileInputStream(fileName));  
		        }  
		        else if(fileType.equals("xlsx"))  
		        {  
		        	workbook = new XSSFWorkbook(new FileInputStream(fileName));  
		        }  
		         
		        //创建sheet对象  
			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(sheetNumber);
			 int firstRowIndex = sheet.getFirstRowNum()+1;  
	            int lastRowIndex = sheet.getLastRowNum(); 
	            int coloumNum=sheet.getRow(0).getPhysicalNumberOfCells();//获得总列数
	            for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex ++){  
	                Row row = sheet.getRow(rIndex);  
	                if(row != null){  
	                    int firstCellIndex = row.getFirstCellNum();  
	                    int lastCellIndex = row.getLastCellNum();  
	                    
	                    String[] s=new String[coloumNum];
	                    for(int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex ++){  
	                        
	                    	Cell cell = row.getCell(cIndex); 
	                        if(cIndex==4){
	                        	Date date = new Date();
	                        	String brithday=null;
	                        	if (fileType.equals("xls")) {  
	                        		date = HSSFDateUtil.getJavaDate( new Double( cell.getNumericCellValue() )); 
	            		        }  
	            		        else if(fileType.equals("xlsx"))  
	            		        {  
	            		        	date = XSSFDateUtil.getJavaDate( new Double( cell.getNumericCellValue() )); 
	            		        }
	                        	
	                        	SimpleDateFormat formatter; 
	                            formatter = new SimpleDateFormat ("yyyy-MM-dd"); 
	                            brithday= formatter.format(date); 
	                            s[cIndex]=brithday;
	                        }else{
	                        	
	                        	row.getCell(cIndex).setCellType(Cell.CELL_TYPE_STRING);
		                        String value = cell.getStringCellValue();  
		                        s[cIndex]=value;
	                        }
	                    }  
	                    list.add(s);  
	                }  
	            }  

		} catch (Exception e) {
//			Log.error(e.getMessage());
			e.printStackTrace();
			
		} 
		return list;
	}
	
	
}

 class XSSFDateUtil extends DateUtil {  
    protected static int absoluteDay(Calendar cal, boolean use1904windowing) {  
        return DateUtil.absoluteDay(cal, use1904windowing);  
    }  
	
	
	
}
