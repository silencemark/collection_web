package com.collection.controller.storefront;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collection.service.IndexService;
import com.collection.service.storefront.StoreFrontService;
import com.collection.util.PageScroll;


/**
 * 店面管理
 * @author silence
 *
 */
@Controller
@RequestMapping("/app")
public class StoreFrontController {
	private transient static Log log = LogFactory.getLog(StoreFrontController.class);
	
	@Autowired StoreFrontService storeFrontService;
	
	@Autowired IndexService indexService;
	
	/**
	 * 店面管理 未读数量 查询
	 * 传入参数{"receiveid":1,"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数{"purchasenum":1,"applynum":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getStorefrontNotreadNum")
	@ResponseBody
	public Map<String, Object> getStorefrontNotreadNum(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data =new HashMap<String, Object>();
		map.put("isread", 0);
		//巡店日志
		List<Integer> resourcetypes=new ArrayList<Integer>();
		resourcetypes.add(22);
		map.put("resourcetypes", resourcetypes);
		int tourstorenum=this.indexService.getForwordNotreadNum(map);
		data.put("tourstorenum", tourstorenum);
		//每日报表
		resourcetypes=new ArrayList<Integer>();
		resourcetypes.add(21);
		map.put("resourcetypes", resourcetypes);
		int everydayreportnum=this.indexService.getForwordNotreadNum(map);
		data.put("everydayreportnum", everydayreportnum);
		
		return data;
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
	 * 查询巡店日志 已处理，未处理的 未读数量
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getTourStoreAllNotRead")
	public Map<String,Object> getTourStoreAllNotRead(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.storeFrontService.getTourStoreListNum(map);
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.storeFrontService.getTourDateListCount(map);
			
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
	 * 查询巡店日志列表 待处理
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0}
	 * 传出参数{"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0,"requestlist":[{"leavetime":"2016-07-22 18:30:20","status":0,"isread":1,"tourstoreid":"bf2d899f17c8401ab04a65187dfcebbf","copyuserid":"1","arrivetime":"2016-07-22 10:10:00","createtime":"2016-07-22 11:58:20","companyid":"67702cc412264f4ea7d2c5f692070457","findproblem":"发现了一个小问题","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","opinion":"门店意见","organizename":"浦东金桥分店","solveproblem":"需要解决的问题","createname":"silence","copyname":"李语然"}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getTourStoreList")
	@ResponseBody
	public Map<String, Object> getTourStoreList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		PageScroll page = new PageScroll();
		int num=this.storeFrontService.getTourStoreListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.storeFrontService.getTourStoreList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("requestlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 巡店日志已处理
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getTourStoreByDate")
	@ResponseBody
	public Map<String, Object> getTourStoreByDate(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
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
			cal1.add(Calendar.DAY_OF_MONTH, 0);
			map.put("starttime",sdf.format(cal.getTime()));
			map.put("endtime",sdf.format(cal1.getTime()));
		}
		PageScroll page = new PageScroll();
		int num=this.storeFrontService.getTourDateListCount(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.storeFrontService.getTourDateList(map);
		Map<String, Object> pvMap = null;
		for(Map<String,Object>data:datalist){
			pvMap=new HashMap<String, Object>();
			pvMap.put("companyid", map.get("companyid"));
			pvMap.put("userid", map.get("userid"));
			pvMap.put("status",map.get("status"));
			pvMap.put("createtime", data.get("createtime"));
			List<Map<String,Object>> detailList = this.storeFrontService.getTourStoreList(pvMap);
			data.put("tourList", detailList);
			data.put("count", detailList.size());
		}
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("requestlist", datalist);
		data.put("status",0);
		data.put("page",page);
		data.put("map", map);
		return data;
	}
	
	
	
	/**
	 * 查询巡店日志详情页面
	 * 传入参数{"tourstoreid":"bf2d899f17c8401ab04a65187dfcebbf"}
	 * 传出参数{"message":"查询成功","tourstoreinfo":{"leavetime":"2016-07-22 18:30:20","status":0,"isread":0,"tourstoreid":"bf2d899f17c8401ab04a65187dfcebbf","copyuserid":"1","arrivetime":"2016-07-22 10:10:00","createtime":"2016-07-22 11:58:20","companyid":"67702cc412264f4ea7d2c5f692070457","findproblem":"发现了一个小问题","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","opinion":"门店意见","organizename":"浦东金桥分店","solveproblem":"需要解决的问题","createname":"silence","copyname":"李语然"},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getTourStoreInfo")
	@ResponseBody
	public Map<String, Object> getTourStoreInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			Map<String, Object> tourstoreinfo=this.storeFrontService.getTourStoreInfo(map);
			data.put("tourstoreinfo", tourstoreinfo);
			data.put("status", 0);
			data.put("message", "查询成功");
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "查询失败");
			return data;
		}
	}
	
	/**
	 * 修改巡店日志（抄送意见）
	 * 传入参数{"tourstoreid":"2d18cfc5b44a48618422767b79b3ee05","userid":"690fb669ed4d40219964baad7783abd4","copyopinion":"ohmygod"}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineTourStore")
	@ResponseBody
	public Map<String, Object> examineTourStore(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.storeFrontService.updateTourStore(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	
	
	
	/*-----------------------------------------人事轨迹 KPI星值考核---------------------------------------------------------------*/
	
	/**
	 * 查询 人事轨迹 KPI星值考核列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","year":2016,"monthnum":7}
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPathKpiStarList")
	@ResponseBody
	public Map<String, Object> getKpiStarList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		PageScroll page = new PageScroll();
		int num=this.storeFrontService.getKpiStarListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.storeFrontService.getKpiStarList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("kpistarList", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 查询 人事轨迹 奖励记录列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","year":2016,"monthnum":7}
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPathRewardList")
	@ResponseBody
	public Map<String, Object> getPathRewardList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		PageScroll page = new PageScroll();
		int num=this.storeFrontService.getPathRewardListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.storeFrontService.getPathRewardList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("rewardlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
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
	@ResponseBody
	public Map<String, Object> getPathPunishList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		PageScroll page = new PageScroll();
		int num=this.storeFrontService.getPathPunishListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.storeFrontService.getPathPunishList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("punishlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	
	/*-----------------------------------------顾客满意 （扫码进入）---------------------------------------------------------------*/
	
	/**
	 * 添加顾客满意
	 * 传入参数
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertStoreEvaluate")
	@ResponseBody
	public Map<String, Object> insertStoreEvaluate(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		String evaluateid="";
		try {
			evaluateid=this.storeFrontService.insertStoreEvaluate(map);
		} catch (Exception e) {
			e.printStackTrace();
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
	 * 查询 人顾客满意 列表 (当传入 createid 是表示我的关注)
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","year":2016,"monthnum":7}
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getStoreEvaluateList")
	@ResponseBody
	public Map<String, Object> getStoreEvaluateList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		map.put("pageSize", "20");
		PageScroll page = new PageScroll();
		int num=this.storeFrontService.getStoreEvaluateListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.storeFrontService.getStoreEvaluateList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("punishlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 关注或者取消关注操作
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/followStoreEvaluate")
	@ResponseBody
	public Map<String, Object> followStoreEvaluate(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
	    data = this.storeFrontService.editFollow(map);
		return data;
	}
	
	
}
