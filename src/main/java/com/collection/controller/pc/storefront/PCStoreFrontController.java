package com.collection.controller.pc.storefront;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collection.controller.userbackstage.UserIndexController.ExportExcel;
import com.collection.redis.RedisUtil;
import com.collection.service.IndexService;
import com.collection.service.storefront.EverydayReportService;
import com.collection.service.storefront.StoreFrontService;
import com.collection.service.worksheet.InspectService;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;
import com.collection.util.UserUtil;


/**
 * 店面管理
 * @author silence
 *
 */
@Controller
@RequestMapping("/pc")
public class PCStoreFrontController {
	private transient static Log log = LogFactory.getLog(PCStoreFrontController.class);
	
	@Autowired StoreFrontService storeFrontService;
	
	@Autowired IndexService indexService;
	
	@Autowired EverydayReportService  everydayReportService;
	
	@Autowired InspectService inspectService;
	
	/**
	 * 初始化添加巡店页面
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initInsertTourStore")
	public String initInsertTourStore(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		model.addAttribute("createtime",sdf1.format(new Date()));
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);
		return "/pc/restaurant/patrollog_add";
	}
	
	
	/**
	 * 添加巡店日志
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","organizeid":5,"arrivetime":"2016-07-22 10:10:00","leavetime":"2016-07-22 18:30:20","findproblem":"发现了一个小问题","solveproblem":"需要解决的问题","opinion":"门店意见","copyuserid":1}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertTourStore")
	@ResponseBody
	public Map<String, Object> insertTourStore(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		String tourstoreid="";
		try {
			tourstoreid=this.storeFrontService.insertTourStore(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		data.put("status",0);
		data.put("message","添加成功");
		return data;
	}
	
	/**
	 * 查询巡店日志列表 待处理
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getTourStoreList")
	public String getTourStoreList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		if(map.containsKey("status") && map.get("status").equals("")){
			map.remove("status");
		}
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		
		PageHelper page = new PageHelper(request);
		int num=this.storeFrontService.getTourStoreListNum(map);	
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.storeFrontService.getTourStoreList(map);
		model.addAttribute("datalist", datalist);
		model.addAttribute("pager",page.cateringPage().toString());
		
		model.addAttribute("map", map);
		
		return "/pc/restaurant/patrollog_list";
	}
	/**
	 * 
	 * 导出巡店日志
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportPatrollogList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportPatrollogList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/巡店日志列表.xls");
		
		if(map.containsKey("status") && map.get("status").equals("")){
			map.remove("status");
		}
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		List<Map<String, Object>> datalist=this.storeFrontService.getTourStoreList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "所巡店面", "巡店人", "到店时间", "离店时间","状态"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> data:datalist){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a", data.get("organizename")==null?"":data.get("organizename"));
	        datamap.put("b", data.get("createname")==null?"":data.get("createname"));
	        datamap.put("c", data.get("arrivetime")==null?"":data.get("arrivetime"));
	        datamap.put("d", data.get("leavetime")==null?"":data.get("leavetime"));
	        if(String.valueOf(data.get("status")).equals("1")){
	        	datamap.put("e","已处理");
	        }else{
	        	datamap.put("e","待处理");
	        }
	        dataset.add(datamap);
        }
        try  
        {
            OutputStream out = new FileOutputStream(importurl);  
            ex.exportExcel(headers, dataset, out);
            out.close();  
            System.out.println("excel导出成功！");
        }  
        catch (FileNotFoundException e)  
        {
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }
		return "巡店日志列表.xls";
	}
	
	
	/**
	 * 查询巡店日志详情页面
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getTourStoreInfo")
	public String getTourStoreInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
		HttpServletResponse response) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request); 
		model.addAttribute("userInfo", userInfo);
		Map<String, Object> tourstoreinfo=this.storeFrontService.getTourStoreInfo(map);
		model.addAttribute("tourstoreinfo", tourstoreinfo);
		model.addAttribute("map", map);
		return "/pc/restaurant/patrollog_detail";
	}
	
	/**
	 * 
	 * 导出巡店日志详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportPatrollogInfo",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportPatrollogInfo(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/巡店日志详情.xls");
		
		Map<String, Object> tourstoreinfo=this.storeFrontService.getTourStoreInfo(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>(); 
        String[] headers = { "名称", "内容", "", "","","","",""};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
        datamap.put("a", "所巡门店");
        datamap.put("b", tourstoreinfo.get("organizename")==null?"":tourstoreinfo.get("organizename"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "到店时间");
        datamap.put("b", tourstoreinfo.get("arrivetime")==null?"":tourstoreinfo.get("arrivetime"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "离店时间");
        datamap.put("b", tourstoreinfo.get("leavetime")==null?"":tourstoreinfo.get("leavetime"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "发现问题");
        datamap.put("b", tourstoreinfo.get("findproblem")==null?"":tourstoreinfo.get("findproblem"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "需解决问题");
        datamap.put("b", tourstoreinfo.get("solveproblem")==null?"":tourstoreinfo.get("solveproblem"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "门店意见");
        datamap.put("b", tourstoreinfo.get("opinion")==null?"":tourstoreinfo.get("opinion"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "创建人");
        datamap.put("b", tourstoreinfo.get("createname")==null?"":tourstoreinfo.get("createname"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "创建时间");
        datamap.put("b", tourstoreinfo.get("createtime")==null?"":tourstoreinfo.get("createtime"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "抄送人");
        datamap.put("b", tourstoreinfo.get("copyname")==null?"":tourstoreinfo.get("copyname"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "抄送人意见");
        datamap.put("b", tourstoreinfo.get("copyopinion")==null?"":tourstoreinfo.get("copyopinion"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "审批人意见");
        datamap.put("b", tourstoreinfo.get("opinion")==null?"":tourstoreinfo.get("opinion"));
        dataset.add(datamap);
        try  
        {
            OutputStream out = new FileOutputStream(importurl);  
            ex.exportExcel(headers, dataset, out);
            out.close();  
            System.out.println("excel导出成功！");
        }  
        catch (FileNotFoundException e)  
        {
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }
		return "巡店日志详情.xls";
	}
	
	
	/*-----------------------------------------人事轨迹 KPI星值考核---------------------------------------------------------------*/
	
	/**
	 * 查询 人事轨迹 KPI星值考核列表
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPathKpiStarList")
	public String getKpiStarList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		if(!map.containsKey("userid") || "".equals(map.get("userid"))){
			map.put("userid", userInfo.get("userid"));
			map.put("searchrealname", userInfo.get("realname"));
		}
		map.put("companyid", userInfo.get("companyid"));
		model.addAttribute("userInfo",userInfo);
		
		PageHelper page = new PageHelper(request);
		int num=this.storeFrontService.getKpiStarListNum(map);			
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.storeFrontService.getKpiStarList(map);
		model.addAttribute("datalist", datalist);
		model.addAttribute("pager",page.cateringPage().toString());
		
		model.addAttribute("map", map);
		return "/pc/restaurant/employee_kpi";
	}
	
	/**
	 * 
	 * 导出kpi考核
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportEmployeeKpi",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportEmployeeKpi(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/KPI星值考核列表.xls");
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		if(!map.containsKey("userid") || "".equals(map.get("userid"))){
			map.put("userid", userInfo.get("userid"));
		}
		map.put("companyid", userInfo.get("companyid"));
		List<Map<String, Object>> datalist=this.storeFrontService.getKpiStarList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "姓名", "考核时间", "总星值", "岗位星值","综合星值"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> data:datalist){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a", userInfo.get("realname")==null?"":userInfo.get("realname"));
	        datamap.put("b", data.get("createtime")==null?"":data.get("createtime"));
	        datamap.put("c", data.get("sumstar")==null?"":data.get("sumstar"));
	        datamap.put("d", data.get("selfstar")==null?"":data.get("selfstar"));
	        datamap.put("e", data.get("overallstar")==null?"":data.get("overallstar"));
	        dataset.add(datamap);
        }
        try  
        {
            OutputStream out = new FileOutputStream(importurl);  
            ex.exportExcel(headers, dataset, out);
            out.close();  
            System.out.println("excel导出成功！");
        }  
        catch (FileNotFoundException e)  
        {
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }
		return "KPI星值考核列表.xls";
	}
	
	/**
	 * 查询 人事轨迹 奖励记录列表
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPathRewardList")
	public String getPathRewardList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		if(!map.containsKey("userid") || "".equals(map.get("userid"))){
			map.put("userid", userInfo.get("userid"));
			map.put("searchrealname", userInfo.get("realname"));
		}
		map.put("companyid", userInfo.get("companyid"));
		model.addAttribute("userInfo",userInfo);
		PageHelper page = new PageHelper(request);
		int num=this.storeFrontService.getPathRewardListNum(map);			
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.storeFrontService.getPathRewardList(map);
		model.addAttribute("datalist", datalist);
		model.addAttribute("pager",page.cateringPage().toString());
		
		model.addAttribute("map", map);
		return "/pc/restaurant/employee_reward";
	}
	
	/**
	 * 
	 * 导出人事轨迹 奖励记录列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportEmployeeReward",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportEmployeeReward(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/奖励记录列表.xls");
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		if(!map.containsKey("userid") || "".equals(map.get("userid"))){
			map.put("userid", userInfo.get("userid"));
		}
		map.put("companyid", userInfo.get("companyid"));
		List<Map<String, Object>> datalist=this.storeFrontService.getPathRewardList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "姓名", "奖励结果", "获奖时间"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> data:datalist){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a", userInfo.get("realname")==null?"":userInfo.get("realname"));
	        datamap.put("b",  data.get("rewardresult")==null?"":data.get("rewardresult"));
	        datamap.put("c",data.get("createtime")==null?"":data.get("createtime"));
	        dataset.add(datamap);
        }
        try  
        {
            OutputStream out = new FileOutputStream(importurl);  
            ex.exportExcel(headers, dataset, out);
            out.close();  
            System.out.println("excel导出成功！");
        }  
        catch (FileNotFoundException e)  
        {
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }
		return "奖励记录列表.xls";
	}
	
	
	/**
	 * 查询 人事轨迹处罚记录列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","year":2016,"monthnum":7}
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPathPunishList")
	public String getPathPunishList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		if(!map.containsKey("userid") || "".equals(map.get("userid"))){
			map.put("userid", userInfo.get("userid"));
			map.put("searchrealname", userInfo.get("realname"));
		}
		map.put("companyid", userInfo.get("companyid"));
		model.addAttribute("userInfo",userInfo);
		PageHelper page = new PageHelper(request);
		int num=this.storeFrontService.getPathPunishListNum(map);			
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.storeFrontService.getPathPunishList(map);
		model.addAttribute("datalist", datalist);
		model.addAttribute("pager",page.cateringPage().toString());
		
		model.addAttribute("map", map);
		
		return "/pc/restaurant/employee_punish";
	}
	
	/**
	 * 
	 * 导出人事轨迹 处罚记录列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportEmployeePunish",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportEmployeePunish(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/处罚记录列表.xls");
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		if(!map.containsKey("userid") || "".equals(map.get("userid"))){
			map.put("userid", userInfo.get("userid"));
		}
		map.put("companyid", userInfo.get("companyid"));
		List<Map<String, Object>> datalist=this.storeFrontService.getPathPunishList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "姓名", "处罚结果", "处罚时间"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> data:datalist){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a", userInfo.get("realname")==null?"":userInfo.get("realname"));
	        datamap.put("b",  data.get("punishresult")==null?"":data.get("punishresult"));
	        datamap.put("c",data.get("createtime")==null?"":data.get("createtime"));
	        dataset.add(datamap);
        }
        try  
        {
            OutputStream out = new FileOutputStream(importurl);  
            ex.exportExcel(headers, dataset, out);
            out.close();  
            System.out.println("excel导出成功！");
        }  
        catch (FileNotFoundException e)  
        {
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }
		return "处罚记录列表.xls";
	}
	
	
	/*-----------------------------------------顾客满意 ---------------------------------------------------------------*/
	
	/**
	 * 查询 顾客满意列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/getStoreEvaluateList")
	public String getStoreEvaluateList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		if(!map.containsKey("organizeid") || "".equals(map.get("organizeid"))){
			map.put("organizeid", userInfo.get("organizeid"));
			map.put("organizename", userInfo.get("organizename"));
		}
		map.put("companyid", userInfo.get("companyid"));
		map.put("userid", userInfo.get("userid"));
		model.addAttribute("userInfo",userInfo);
		PageHelper page = new PageHelper(request);
		int num=this.storeFrontService.getStoreEvaluateListNum(map);				
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.storeFrontService.getStoreEvaluateList(map);
		model.addAttribute("datalist", datalist);
		model.addAttribute("pager",page.cateringPage().toString());
		
		model.addAttribute("map", map);
		return "/pc/restaurant/appraise_list";
	}
	
	/**
	 * 查询我的关注  顾客满意列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getMyStoreEvaluate")
	public String getMyStoreEvaluate(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		if(!map.containsKey("organizeid") || "".equals(map.get("organizeid"))){
			map.put("organizeid", userInfo.get("organizeid"));
			map.put("organizename", userInfo.get("organizename"));
		}
		map.put("createid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		map.put("isfollow", 1);
		model.addAttribute("userInfo",userInfo);
		PageHelper page = new PageHelper(request);
		int num=this.storeFrontService.getStoreEvaluateListNum(map);				
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.storeFrontService.getStoreEvaluateList(map);
		model.addAttribute("datalist", datalist);
		model.addAttribute("pager",page.cateringPage().toString());
		
		model.addAttribute("map", map);
		return "/pc/restaurant/appraise_my";
	}
	//----------------------------------------------统计------------------------------------------------------------------

	/**
	 * 查询每日报表营业额
	 * 传入参数{"organizeid":"11"}
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReportIncomeChart")
	public String getReportIncomeChart(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		return "/pc/restaurant/report_yye";
	}
	/**
	 * 查询每日报表客流量
	 * 传入参数{"organizeid":"11"}
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReportFlowChart")
	public String getReportFlowChart(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		return "/pc/restaurant/report_kll";
	}
	
	/**
	 * 查询每日报表人均消费
	 * 传入参数{"organizeid":"11"}
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReportConsumptionChart")
	public String getReportConsumptionChart(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		return "/pc/restaurant/report_rjxf";
	}
	
	
	/**
	 * 查询每日报表 满意度
	 * 传入参数{"organizeid":"11"}
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReportSatisfyChart")
	public String getReportSatisfyChart(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		return "/pc/restaurant/report_myd";
	}
	
	/**
	 * 查询每日报表 出勤率
	 * 传入参数{"organizeid":"11"}
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReportAttendanceChart")
	public String getReportAttendanceChart(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		return "/pc/restaurant/report_cql";
	}
	
	/**
	 * 查询餐前检查信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getReportPreCheckPage")
	public String getReportPreCheckInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		return "/pc/restaurant/report_cqjc";
	}
	
	/**
	 * 查询厨房检查信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getReportKitChencheckPage")
	public String getReportKitChencheckInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		return "/pc/restaurant/report_cfjc";
	}
	
	/**
	 * 查询离店报告单信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getReportLeavePage")
	public String getReportLeaveInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		return "/pc/restaurant/report_ldbg";
	}
	
	
	//--------------------------------------------每日报表--------------------------------------------------------------
	/**
	 * 查询每日报表列表 待处理
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getEverydayReportList")
	public String getEverydayReportList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		if(map.containsKey("status") && map.get("status").equals("")){
			map.remove("status");
		}
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		
		PageHelper page = new PageHelper(request);
		int num=this.everydayReportService.getEverydayReportListNum(map);				
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.everydayReportService.getEverydayReportList(map);
		model.addAttribute("datalist", datalist);
		model.addAttribute("pager",page.cateringPage().toString());
		
		model.addAttribute("map", map);
		
		Map<String, Object> powerMap=RedisUtil.getMap(userInfo.get("userid")+"powerMap");
		model.addAttribute("powerMap",powerMap);
		
		return "/pc/restaurant/dailyreport_list";
	}
	
	/**
	 * 
	 * 导出每日报表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportDailyreportList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportDailyreportList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/每日报表列表.xls");
		
		if(map.containsKey("status") && map.get("status").equals("")){
			map.remove("status");
		}
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		List<Map<String, Object>> datalist=this.everydayReportService.getEverydayReportList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "店面", "填写人", "填写时间","状态"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> data:datalist){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a", data.get("organizename")==null?"":data.get("organizename"));
	        datamap.put("b", data.get("createname")==null?"":data.get("createname"));
	        datamap.put("c", data.get("createtime")==null?"":data.get("createtime"));
	        if(String.valueOf(data.get("status")).equals("1")){
	        	datamap.put("d","已处理");
	        }else{
	        	datamap.put("d","待处理");
	        }
	        dataset.add(datamap);
        }
        try  
        {
            OutputStream out = new FileOutputStream(importurl);  
            ex.exportExcel(headers, dataset, out);
            out.close();  
            System.out.println("excel导出成功！");
        }  
        catch (FileNotFoundException e)  
        {
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }
		return "每日报表列表.xls";
	}
	
	/**
	 * 查询每日报表详情
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getDailyreportInfo")
	public String getDailyreportInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> reportInfo=this.everydayReportService.getEverydayReportInfo(map);
		model.addAttribute("reportInfo", reportInfo);
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("map",map);
		return "/pc/restaurant/dailyreport_detail";
	}
	
	/**
	 * 初始化新增每日报表接口
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initInsertDailyreport")
	public String initInsertDailyreport(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);
		SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		model.addAttribute("createtime",sdf1.format(new Date()));
		return "/pc/restaurant/dailyreport_add";
	}
	
}
