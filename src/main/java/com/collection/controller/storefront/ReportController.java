package com.collection.controller.storefront;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collection.service.storefront.EverydayReportService;
import com.collection.service.storefront.StoreFrontService;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;


/**
 * 店面管理
 * @author silence
 *
 */
@Controller
@RequestMapping("/app")
public class ReportController {
	private transient static Log log = LogFactory.getLog(ReportController.class);
	
	@Autowired StoreFrontService storeFrontService;
	
	@Autowired EverydayReportService  everydayReportService;
	
	
	/**
	 * 查询 每日报表 已处理，未处理的 未读数量
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getEverydayReportAllNotRead")
	public Map<String,Object> getEverydayReportAllNotRead(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.everydayReportService.getEverydayReportListNum(map);
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.everydayReportService.getEverydayReportByDateNum(map);
			
			resultMap.put("noexaminenum", num);
			resultMap.put("examinenum", count);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	
	/**
	 * 查询每日报表列表 待处理
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0}
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getEverydayReportList")
	@ResponseBody
	public Map<String, Object> getEverydayReportList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		PageScroll page = new PageScroll();
		int num=this.everydayReportService.getEverydayReportListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.everydayReportService.getEverydayReportList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("reportlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	

	/**
	 * 采购(入库)单查询(已处理)
	 * 传入参数 {"userid":1,"status":1,"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * status 1：已处理 0：未处理    starttime 和 endtime 不传 默认近7天
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getEverydayReportByDate")
	@ResponseBody
	public Map<String, Object> getEverydayReportByDate(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		if((!map.containsKey("starttime") || "".equals(map.get("starttime"))) && (!map.containsKey("endtime") || "".equals(map.get("endtime")))){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String stoptime = sdf.format(new Date());
			Calendar cal = Calendar.getInstance();
			Calendar cal1 = Calendar.getInstance();
			try {
				cal.setTime(sdf.parse(stoptime));
				cal1.setTime(sdf.parse(stoptime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cal.add(Calendar.DAY_OF_MONTH, -7);
			cal1.add(Calendar.DAY_OF_MONTH,1);
			map.put("starttime",sdf.format(cal.getTime()));
			map.put("endtime",sdf.format(cal1.getTime()));
		}
		
		PageScroll page = new PageScroll();
		int num=this.everydayReportService.getEverydayReportByDateNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> list=this.everydayReportService.getEverydayReportByDate(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("list", list);
		data.put("page",page);
		data.put("map", map);
		data.put("status",0);
		return data;
	}
	
	/**
	 * 根据组织查询模版（用户是否有权限）
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0}
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getEverydayReportModule")
	@ResponseBody
	public Map<String, Object> getEverydayReportModule(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		List<Map<String, Object>> datalist=this.everydayReportService.getEverydayReportModule(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("modulelist", datalist);
		data.put("status",0);
		return data;
	}
	
	/**
	 * 根据模版查询每日报表的字典
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0}
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getEverydayReportDictList")
	@ResponseBody
	public Map<String, Object> getEverydayReportDictList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//查询组织的最后一次的编制人数（今天以前）
		String lastnum=this.everydayReportService.getLastBianjiNum(map);
		
		List<Map<String, Object>> datalist=this.everydayReportService.getEverydayReportDictList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("datalist", datalist);
		data.put("lastnum", lastnum);
		data.put("status",0);
		return data;
	}
	
	
	/**
	 * 初始化新增每日报表接口
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数{"createtime":"2016-07-26 20:20:17"}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initInsertEverydayReport")
	@ResponseBody
	public Map<String, Object> initInsertEverydayReport(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("status", 0);
		SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		
		data.put("createtime",sdf1.format(new Date()));
		data.put("message", "获取成功");
		return data;
	}
	
	/**
	 * 新增每日报表接口
	 * 传入参数
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertEverydayReport")
	@ResponseBody
	public Map<String, Object> insertEverydayReport(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		String reportid="";
		
		//查询店面今日每日报表数据
		Map<String, Object> reportmap=new HashMap<String, Object>();
		reportmap.put("organizeid", map.get("organizeid"));
		reportmap.put("statisticaltime", map.get("statisticaltime"));
		reportmap=this.everydayReportService.getTodayReport(reportmap);
		if(reportmap!= null && reportmap.size()> 0 ){
			data.put("status", 1);
			data.put("message", "当前店铺已经填写过每日报表");
			return data;
		}
		try {
			//添加到每日报表
			reportid=this.everydayReportService.insertEverydayReport(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加每日报表错误");
			return data;
		}
		try {
			
			JSONObject json=JSONObject.fromObject(map.get("extendlist")+"");
			List<Map<String, Object>> extendlist=(List<Map<String, Object>>)json.get("extendlist");
			for(Map<String, Object> extend:extendlist){
				extend.put("companyid", map.get("companyid"));
				extend.put("reportid", reportid);
				this.everydayReportService.insertReportExtend(extend);
			}
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加采购物料错误");
			return data;
		}
		data.put("status", 0);
		data.put("message", "添加成功");
		return data;
	}
	
	/**
	 * 每日报表详情查询
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0}
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getEverydayReportInfo")
	@ResponseBody
	public Map<String, Object> getEverydayReportInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> data=new HashMap<String, Object>();
		try{
			Map<String, Object> reportInfo=this.everydayReportService.getEverydayReportInfo(map);
			data.put("reportInfo", reportInfo);
			data.put("status",0);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "查询失败");
		}
		return data;
	}
	
	/**
	 * 抄送采购(入库)单  修改抄送意见
	 * 传入参数{"userid":1,"orderid":"9d54541939f6455c8253a4df4f77328b","opinion":"ohmygod","result":1}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/copyEverydayReport")
	@ResponseBody
	public Map<String, Object> copyEverydayReport(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.everydayReportService.updateEverydayReport(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	
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
	@ResponseBody
	public Map<String, Object> getReportIncomeChart(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> data=new HashMap<String, Object>();
		
		String statisticsTime = String.valueOf(map.get("statisticsTime"));
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			map.put("starttime", statisticsTime);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			map.put("starttime", sdf.format(new Date()));
		}
		//查询当月的所有的营业额信息
		List<Map<String, Object>> nowmonthlist=this.everydayReportService.getReportIncomeChart(map);
		
		int nowmonth = 0;
		int lastmonth = 0;
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			String[] times = statisticsTime.split("-");
			nowmonth = Integer.parseInt(times[1]);
			int mon = Integer.parseInt(times[1]) - 1;
			int yer = Integer.parseInt(times[0]);
			if(mon<=0){
				mon = 12;
				yer = yer - 1;
			}
			lastmonth = mon;
			if(mon >= 10){
				map.put("starttime", yer+"-"+mon);
			}else if(mon < 10 && mon >= 1){
				map.put("starttime", yer+"-0"+mon);
			}
		}else{
			int month=new Date().getMonth();
			nowmonth = month + 1;
			SimpleDateFormat sdf3 = new SimpleDateFormat("YYYY");
			int year=Integer.parseInt(sdf3.format(new Date()));
			if(month==0){
				month=12;
				year--;
			}
			lastmonth = month;
			if(month<10){
				map.put("starttime", year+"-0"+month);
			}else{
				map.put("starttime", year+"-"+month);
			}
		}
		//查询上一个月的营业额信息
		List<Map<String, Object>> lastmonthlist=this.everydayReportService.getReportIncomeChart(map);
		
		data.put("nowmonth", nowmonth);
		data.put("lastmonth", lastmonth);
		data.put("lastmonthlist", lastmonthlist);
		data.put("nowmonthlist", nowmonthlist);
		data.put("status", 0);
		data.put("message", "查询成功");
		return data;
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
	@ResponseBody
	public Map<String, Object> getReportFlowChart(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> data=new HashMap<String, Object>();
		
		//获取页面传入的查询时间
		String statisticsTime = String.valueOf(map.get("statisticsTime"));
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			map.put("starttime", statisticsTime);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			map.put("starttime", sdf.format(new Date()));
		}
		//查询当月客流量
		List<Map<String, Object>> nowmonthlist=this.everydayReportService.getReportFlowChart(map);
		
		int nowmonth = 0;
		int lastmonth = 0;
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			String[] times = statisticsTime.split("-");
			nowmonth = Integer.parseInt(times[1]);
			int mon = Integer.parseInt(times[1]) - 1;
			int yer = Integer.parseInt(times[0]);
			if(mon<=0){
				mon = 12;
				yer = yer - 1;
			}
			lastmonth = mon;
			if(mon >= 10){
				map.put("starttime", yer+"-"+mon);
			}else if(mon < 10 && mon >= 1){
				map.put("starttime", yer+"-0"+mon);
			}
		}else{
			int month=new Date().getMonth();
			nowmonth = month + 1;
			SimpleDateFormat sdf3 = new SimpleDateFormat("YYYY");
			int year=Integer.parseInt(sdf3.format(new Date()));
			if(month==0){
				month=12;
				year--;
			}
			lastmonth = month;
			if(month<10){
				map.put("starttime", year+"-0"+month);
			}else{
				map.put("starttime", year+"-"+month);
			}
		}
		//查询上一月的客流量信息
		List<Map<String, Object>> lastmonthlist=this.everydayReportService.getReportFlowChart(map);
		
		data.put("nowmonth", nowmonth);
		data.put("lastmonth", lastmonth);
		data.put("lastmonthlist", lastmonthlist);
		data.put("nowmonthlist", nowmonthlist);
		data.put("status", 0);
		data.put("message", "查询成功");
		return data;
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
	@ResponseBody
	public Map<String, Object> getReportConsumptionChart(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> data=new HashMap<String, Object>();
		
		//获取页面传入的查询时间
		String statisticsTime = String.valueOf(map.get("statisticsTime"));
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			map.put("starttime", statisticsTime);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			map.put("starttime", sdf.format(new Date()));
		}
		//查询当月的人均消费信息
		List<Map<String, Object>> nowmonthlist=this.everydayReportService.getReportConsumptionChart(map);
		int nowreportnum = this.everydayReportService.getReportConsumptionChartCount(map);
		
		int nowmonth = 0;
		int lastmonth = 0;
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			String[] times = statisticsTime.split("-");
			nowmonth = Integer.parseInt(times[1]);
			int mon = Integer.parseInt(times[1]) - 1;
			int yer = Integer.parseInt(times[0]);
			if(mon<=0){
				mon = 12;
				yer = yer - 1;
			}
			lastmonth = mon;
			if(mon >= 10){
				map.put("starttime", yer+"-"+mon);
			}else if(mon < 10 && mon >= 1){
				map.put("starttime", yer+"-0"+mon);
			}
		}else{
			int month=new Date().getMonth();
			nowmonth = month + 1;
			SimpleDateFormat sdf3 = new SimpleDateFormat("YYYY");
			int year=Integer.parseInt(sdf3.format(new Date()));
			if(month==0){
				month=12;
				year--;
			}
			lastmonth = month;
			if(month<10){
				map.put("starttime", year+"-0"+month);
			}else{
				map.put("starttime", year+"-"+month);
			}
		}
		//查询上一个月人均消费信息
		List<Map<String, Object>> lastmonthlist=this.everydayReportService.getReportConsumptionChart(map);
		int lastreportnum = this.everydayReportService.getReportConsumptionChartCount(map);
		
		data.put("nowmonth", nowmonth);
		data.put("lastmonth", lastmonth);
		data.put("lastmonthlist", lastmonthlist);
		data.put("nowmonthlist", nowmonthlist);
		data.put("nowreportnum", nowreportnum);
		data.put("lastreportnum", lastreportnum);
		data.put("status", 0);
		data.put("message", "查询成功");
		return data;
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
	@ResponseBody
	public Map<String, Object> getReportSatisfyChart(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> data=new HashMap<String, Object>();
		
		//获取页面传入的查询时间
		String statisticsTime = String.valueOf(map.get("statisticsTime"));
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			map.put("starttime", statisticsTime);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			map.put("starttime", sdf.format(new Date()));
		}
		//查询当月的顾客满意度信息
		List<Map<String, Object>> monthlist=this.everydayReportService.getReportSatisfyChart(map);
		int reportnum = this.everydayReportService.getReportSatisfyChartCount(map);
		
		data.put("monthlist", monthlist);
		data.put("reportnum", reportnum);
		data.put("status", 0);
		data.put("message", "查询成功");
		return data;
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
	@ResponseBody
	public Map<String, Object> getReportAttendanceChart(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> data=new HashMap<String, Object>();
		
		//获取页面传入的查询时间
		String statisticsTime = String.valueOf(map.get("statisticsTime"));
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			map.put("starttime", statisticsTime);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			map.put("starttime", sdf.format(new Date()));
		}
		//查询当月的出勤人数
		List<Map<String, Object>> nowmonthlist=this.everydayReportService.getReportAttendanceChart(map);
		
		int nowmonth = 0;
		int lastmonth = 0;
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			String[] times = statisticsTime.split("-");
			nowmonth = Integer.parseInt(times[1]);
			int mon = Integer.parseInt(times[1]) - 1;
			int yer = Integer.parseInt(times[0]);
			if(mon<=0){
				mon = 12;
				yer = yer - 1;
			}
			lastmonth = mon;
			if(mon >= 10){
				map.put("starttime", yer+"-"+mon);
			}else if(mon < 10 && mon >= 1){
				map.put("starttime", yer+"-0"+mon);
			}
		}else{
			int month=new Date().getMonth();
			nowmonth = month + 1;
			SimpleDateFormat sdf3 = new SimpleDateFormat("YYYY");
			int year=Integer.parseInt(sdf3.format(new Date()));
			if(month==0){
				month=12;
				year--;
			}
			lastmonth = month;
			if(month<10){
				map.put("starttime", year+"-0"+month);
			}else{
				map.put("starttime", year+"-"+month);
			}
		}
		//查询上一月的出勤率信息
		List<Map<String, Object>> lastmonthlist=this.everydayReportService.getReportAttendanceChart(map);
		
		Map<String,Object> yyeMap = new HashMap<String,Object>();
		for(Map<String,Object> nowmap : nowmonthlist){
			yyeMap.put(nowmonth+String.valueOf(nowmap.get("comparetime")), nowmap.get("price"));
		}
		for(Map<String,Object> lastmap : lastmonthlist){
			yyeMap.put(lastmonth+String.valueOf(lastmap.get("comparetime")), lastmap.get("price"));
		}
		
		
		data.put("nowmonth", nowmonth);
		data.put("lastmonth", lastmonth);
		data.put("lastmonthlist", lastmonthlist);
		data.put("nowmonthlist", nowmonthlist);
		data.put("yyeMap", yyeMap);
		data.put("status", 0);
		data.put("message", "查询成功");
		return data;
	}
	
	/**
	 * 查询每日报表----每条唯一
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getEverydayReportListOnlyOne")
	@ResponseBody
	public Map<String, Object> getEverydayReportListOnlyOne(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//获取页面传入的查询时间
		String statisticsTime = String.valueOf(map.get("statisticsTime"));
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			map.put("starttime", statisticsTime);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			map.put("starttime", sdf.format(new Date()));
		}
		PageScroll page = new PageScroll();
		int num=this.everydayReportService.getEverydayReportListNumOnlyOne(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.everydayReportService.getEverydayReportListOnlyOne(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("reportlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	@RequestMapping("/getEverydayReportListOnlyOnePC")
	@ResponseBody
	public Map<String, Object> getEverydayReportListOnlyOnePC(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//获取页面传入的查询时间
		String statisticsTime = String.valueOf(map.get("statisticsTime"));
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			map.put("starttime", statisticsTime);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			map.put("starttime", sdf.format(new Date()));
		}
		PageHelper page = new PageHelper(request);
		int num=this.everydayReportService.getEverydayReportListNumOnlyOne(map);		
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.everydayReportService.getEverydayReportListOnlyOne(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("reportlist", datalist);
		data.put("status",0);
		data.put("pager",page.JSCateringPage().toString());
		return data;
	}
}
