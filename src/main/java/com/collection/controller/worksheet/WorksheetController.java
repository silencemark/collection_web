package com.collection.controller.worksheet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collection.service.worksheet.InspectService;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;


/**
 * 仓库管理   检查管理
 * @author silence
 *
 */
@Controller
@RequestMapping("/app")
public class WorksheetController {
	@SuppressWarnings("unused")
	private transient static Log log = LogFactory.getLog(WorksheetController.class);
	
	@Resource private InspectService inspectService;
	/**
	 * 
	 *  根据区域和公司查询模版 (当前用户是否在发布范围内)模版类型：1：离店  2：厨房 3：餐前
	 *  传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","type":1,"organizeid":5}
	 *  传出参数{"message":"查询成功","status":0,"templatelist":[{"createtime":"2016-07-22 10:31:37","companyid":"67702cc412264f4ea7d2c5f692070457","templatename":"浦东店26楼检查","updatetime":"2016-07-22 10:31:41","createid":"1","organizeid":"5","templateid":"1","type":1}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getInspectTemplate")
	@ResponseBody
	public Map<String, Object> getInspectTemplate(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		List<Map<String, Object>> templatelist=this.inspectService.getInspectTemplateList(map);
		data.put("templatelist", templatelist);
		data.put("status", 0);
		data.put("message", "查询成功");
		return data;
	}
	
	/**
	 * 
	 * 根据模版查询项目列表
	 * 传入参数{"templateid":1}
	 * 传出参数{"message":"查询成功","projectlist":[{"createtime":1469156848000,"updatetime":1469156923000,"createid":"1","templateid":"1","projectid":"1","projectname":"煤气阀门处理"}],"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getTemplateProjectList")
	@ResponseBody
	public Map<String, Object> getTemplateProjectList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		List<Map<String, Object>> projectlist=this.inspectService.getTemplateProjectList(map);
		data.put("projectlist", projectlist);
		data.put("status", 0);
		data.put("message", "查询成功");
		return data;
	}
	
	
	/**
	 * 查询离店单 未处理，已处理的未读数量
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getLeaveShopAllNotRead")
	public Map<String,Object> getLeaveShopAllNotRead(@RequestParam Map<String,Object> map , HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的未读数量
			map.put("status", 0);
			int num=this.inspectService.getLeaveShopListNum(map);
			
			//查询已处理的未读数量
			map.put("status", 1);
			int count=this.inspectService.getLeaveShopListTimesCount(map);
			
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
	 * 添加离店检查
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","organizeid":5,"examineuserid":1,"createid":"690fb669ed4d40219964baad7783abd4","starlist":"{"starlist":[{"projectid":"1","starlevel":4}]}"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertLeaveshop")
	@ResponseBody
	public Map<String, Object> insertLeaveshop(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		String inspectid="";
		try {
			inspectid=this.inspectService.insertLeaveshop(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		try {
			//添加到 项目评分表
			JSONObject json1=JSONObject.fromObject(map.get("starlist")+"");
			List<Map<String, Object>> starlist=(List<Map<String, Object>>)json1.get("starlist");
			int priority = 1;
			for(Map<String, Object> star:starlist){
				star.put("inspectid", inspectid);
				star.put("createid", map.get("createid"));
				star.put("priority", priority);
				this.inspectService.insertInspectStar(star);
				priority++;
			}
			data.put("status",0);
			data.put("message","添加成功");
			return data;
			
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
	}
	
	/**
	 * 查询离店检查列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0}
	 * 传出参数{"inspectlist":[{"inspectid":"2d18cfc5b44a48618422767b79b3ee05","createtime":"2016-07-22 11:21:30","companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":"0000-00-00 00:00:00","status":0,"createid":"690fb669ed4d40219964baad7783abd4","isread":1,"examineuserid":"1","organizeid":"5","organizename":"浦东金桥分店","createname":"silence","examinename":"李语然"}],"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getLeaveShopList")
	@ResponseBody
	public Map<String, Object> getLeaveShopList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		map.put("status", 0);
		PageScroll page = new PageScroll();
		int num=this.inspectService.getLeaveShopListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.inspectService.getLeaveShopList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("inspectlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	
	/**
	 * 查询已处理的离店检查信息
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getExamineLeaveShopList")
	public Map<String,Object> getExamineLeaveShopList(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		map.put("status", 1);
		PageScroll page = new PageScroll();
		int num=this.inspectService.getLeaveShopListTimesCount(map);	
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.inspectService.getLeaveShopListTimes(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("inspectlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	
	/**
	 * 查询离店检查详情页面
	 * 传入参数{"inspectid":"2d18cfc5b44a48618422767b79b3ee05"}
	 * 传出参数{"message":"查询成功","status":0,"inspectinfo":{"inspectid":"2d18cfc5b44a48618422767b79b3ee05","createtime":"2016-07-22 11:21:30","companyid":"67702cc412264f4ea7d2c5f692070457","starlist":[{"inspectid":"2d18cfc5b44a48618422767b79b3ee05","createid":"690fb669ed4d40219964baad7783abd4","starid":"26d9fe2adaff413aaf73da677d9add54","projectid":"1","projectname":"煤气阀门处理","starlevel":4}],"updatetime":"0000-00-00 00:00:00","status":0,"createid":"690fb669ed4d40219964baad7783abd4","examineuserid":"1","organizeid":"5","organizename":"浦东金桥分店","createname":"silence","examinename":"李语然"}}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getLeaveShopInfo")
	@ResponseBody
	public Map<String, Object> getLeaveShopInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			Map<String, Object> inspectinfo=this.inspectService.getLeaveShopInfo(map);
			data.put("inspectinfo", inspectinfo);
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
	 * 修改/抄送意见  离店检查
	 * 传入参数{"inspectid":"2d18cfc5b44a48618422767b79b3ee05","userid":"690fb669ed4d40219964baad7783abd4","opinion":"ohmygod"}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineLeaveShop")
	@ResponseBody
	public Map<String, Object> examineLeaveShop(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.inspectService.updateLeaveShop(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	
	
	/*----------------------------------------------------餐前检查------------------------------------------------------------*/
	
	
	/**
	 * 查询餐前检查已处理，未处理 未读数量
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getBeforeMealAllNotRead")
	public Map<String,Object> getBeforeMealAllNotRead(@RequestParam Map<String,Object> map , HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.inspectService.getBeforeMealListNum(map);	
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.inspectService.getBeforeMealListTimesCount(map);	
			
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
	 * 添加餐前检查
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","organizeid":5,"examineuserid":1,"createid":"690fb669ed4d40219964baad7783abd4","starlist":"{"starlist":[{"projectid":"2","starlevel":4}]}"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertBeforeMeal")
	@ResponseBody
	public Map<String, Object> insertBeforeMeal(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		String inspectid="";
		try {
			inspectid=this.inspectService.insertBeforeMeal(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		try {
			//添加到 项目评分表
			JSONObject json1=JSONObject.fromObject(map.get("starlist")+"");
			List<Map<String, Object>> starlist=(List<Map<String, Object>>)json1.get("starlist");
			int priority = 1;
			for(Map<String, Object> star:starlist){
				star.put("inspectid", inspectid);
				star.put("createid", map.get("createid"));
				star.put("priority", priority);
				this.inspectService.insertInspectStar(star);
				priority++;
			}
			data.put("status",0);
			data.put("message","添加成功");
			return data;
			
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
	}
	
	/**
	 * 查询餐前检查列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0}
	 * 传出参数{"inspectlist":[{"inspectid":"36c9c72248cb4b21977041a10ddde582","createtime":"2016-07-22 11:40:20","companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":"2016-07-22 11:44:33","status":0,"createid":"690fb669ed4d40219964baad7783abd4","isread":1,"examineuserid":"1","organizeid":"5","organizename":"浦东金桥分店","createname":"silence","examinename":"李语然"}],"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getBeforeMealList")
	@ResponseBody
	public Map<String, Object> getBeforeMealList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		map.put("status", 0);
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageScroll page = new PageScroll();
		int num=this.inspectService.getBeforeMealListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.inspectService.getBeforeMealList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("inspectlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 查询已经处理的餐前检查信息
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getExamineBeforeMealList")
	public Map<String,Object> getExamineBeforeMealList(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		map.put("status", 1);
		PageScroll page = new PageScroll();
		int num=this.inspectService.getBeforeMealListTimesCount(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.inspectService.getBeforeMealListTimes(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("inspectlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 查询餐前检查详情页面
	 * 传入参数{"inspectid":"36c9c72248cb4b21977041a10ddde582"}
	 * 传出参数{"message":"查询成功","status":0,"inspectinfo":{"inspectid":"36c9c72248cb4b21977041a10ddde582","createtime":"2016-07-22 11:40:20","companyid":"67702cc412264f4ea7d2c5f692070457","starlist":[{"inspectid":"36c9c72248cb4b21977041a10ddde582","createid":"690fb669ed4d40219964baad7783abd4","starid":"5218b93091734791adc77f1447c72806","projectid":"2","projectname":"厨房卫生检查","starlevel":4}],"updatetime":"2016-07-22 11:44:33","status":0,"createid":"690fb669ed4d40219964baad7783abd4","examineuserid":"1","organizeid":"5","organizename":"浦东金桥分店","createname":"silence","examinename":"李语然"}}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getBeforeMealInfo")
	@ResponseBody
	public Map<String, Object> getBeforeMealInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			Map<String, Object> inspectinfo=this.inspectService.getBeforeMealInfo(map);
			data.put("inspectinfo", inspectinfo);
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
	 * 修改/抄送意见  餐前检查
	 * 传入参数{"inspectid":"36c9c72248cb4b21977041a10ddde582","userid":"690fb669ed4d40219964baad7783abd4","opinion":"ohmygod"}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineBeforeMeal")
	@ResponseBody
	public Map<String, Object> examineBeforeMeal(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.inspectService.updateBeforeMeal(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	
	
/*----------------------------------------------------厨房检查------------------------------------------------------------*/

	
	/**
	 * 查询厨房检查已处理，未处理 的未读数量
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getKitchencheckNotRead")
	public Map<String,Object> getKitchencheckNotRead(@RequestParam Map<String,Object> map , HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的未读数量
			map.put("status", 0);
			int num=this.inspectService.getKitchenListNum(map);
			
			//查询已处理的未读数量
			map.put("status", 1);
			int count=this.inspectService.getKitchenListTimesCount(map);	
			
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
	 * 添加厨房检查
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","organizeid":5,"examineuserid":1,"createid":"690fb669ed4d40219964baad7783abd4","starlist":"{"starlist":[{"projectid":"3","starlevel":4}]}"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertKitchen")
	@ResponseBody
	public Map<String, Object> insertKitchen(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		String inspectid="";
		try {
			inspectid=this.inspectService.insertKitchen(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		try {
			//添加到 项目评分表
			JSONObject json1=JSONObject.fromObject(map.get("starlist")+"");
			List<Map<String, Object>> starlist=(List<Map<String, Object>>)json1.get("starlist");
			int priority = 1;
			for(Map<String, Object> star:starlist){
				star.put("inspectid", inspectid);
				star.put("createid", map.get("createid"));
				star.put("priority", priority);
				this.inspectService.insertInspectStar(star);
				priority++;
			}
			data.put("status",0);
			data.put("message","添加成功");
			return data;
			
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
	}
	
	/**
	 * 查询厨房检查列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0}
	 * 传出参数{"inspectlist":[{"inspectid":"6ad57bfb8a1a4a38a9621715a93b9b47","createtime":"2016-07-22 11:50:59","companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":"0000-00-00 00:00:00","status":0,"createid":"690fb669ed4d40219964baad7783abd4","isread":1,"examineuserid":"1","organizeid":"5","organizename":"浦东金桥分店","createname":"silence","examinename":"李语然"}],"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getKitchenList")
	@ResponseBody
	public Map<String, Object> getKitchenList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		map.put("status", 0);
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageScroll page = new PageScroll();
		int num=this.inspectService.getKitchenListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.inspectService.getKitchenList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("inspectlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 查询已经处理的厨房检查列表信息
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getExamineKitchenList")
	public Map<String,Object> getExamineKitchenList(@RequestParam Map<String,Object> map , HttpServletResponse response){
		map.put("status", 1);
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageScroll page = new PageScroll();
		int num=this.inspectService.getKitchenListTimesCount(map);	
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.inspectService.getKitchenListTimes(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("inspectlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 查询厨房检查详情页面
	 * 传入参数{"inspectid":"6ad57bfb8a1a4a38a9621715a93b9b47"}
	 * 传出参数{"message":"查询成功","status":0,"inspectinfo":{"inspectid":"6ad57bfb8a1a4a38a9621715a93b9b47","createtime":"2016-07-22 11:50:59","companyid":"67702cc412264f4ea7d2c5f692070457","starlist":[{"inspectid":"6ad57bfb8a1a4a38a9621715a93b9b47","createid":"690fb669ed4d40219964baad7783abd4","starid":"e5d260a199d84360a6668ef988286686","projectid":"3","projectname":"厨房卫生检查","starlevel":4}],"updatetime":"0000-00-00 00:00:00","status":0,"createid":"690fb669ed4d40219964baad7783abd4","examineuserid":"1","organizeid":"5","organizename":"浦东金桥分店","createname":"silence","examinename":"李语然"}}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getKitchenInfo")
	@ResponseBody
	public Map<String, Object> getKitchenInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			Map<String, Object> inspectinfo=this.inspectService.getKitchenInfo(map);
			data.put("inspectinfo", inspectinfo);
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
	 * 修改/抄送意见  厨房检查
	 * 传入参数{"inspectid":"6ad57bfb8a1a4a38a9621715a93b9b47","userid":"690fb669ed4d40219964baad7783abd4","opinion":"ohmygod"}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineKitchen")
	@ResponseBody
	public Map<String, Object> examineKitchen(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.inspectService.updateKitchen(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	
	
	
	/**
	 * 查询餐前检查信息--报表
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getReportPreCheckInfo")
	public Map<String,Object> getReportPreCheckInfo(@RequestParam Map<String,Object> map , 
			HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			//获取页面传入的查询时间
			String statisticsTime = String.valueOf(map.get("statisticsTime"));
			if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
				map.put("starttime", statisticsTime);
			}else{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				map.put("starttime", sdf.format(new Date()));
			}
			//查询当月的餐前检查信息
			List<Map<String,Object>> nowmonthlist = this.inspectService.getReportPrecheckInfo(map);
			int nowreportnum=this.inspectService.getBeforeMealListNumOnlyOne(map);	
			
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
			//查询上一个月的餐前检查信息
			List<Map<String,Object>> lastmonthlist = this.inspectService.getReportPrecheckInfo(map);
			int lastreportnum=this.inspectService.getBeforeMealListNumOnlyOne(map);	
			
			resultMap.put("nowmonth", nowmonth);
			resultMap.put("lastmonth", lastmonth);
			resultMap.put("lastmonthlist", lastmonthlist);
			resultMap.put("nowmonthlist", nowmonthlist);
			resultMap.put("nowreportnum", nowreportnum);
			resultMap.put("lastreportnum", lastreportnum);
			resultMap.put("status", 0);
			resultMap.put("message", "查询成功");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 查询厨房检查-报表
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getReportKitChencheckInfo")
	public Map<String,Object> getReportKitChencheckInfo(@RequestParam Map<String,Object> map , 
			HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			//获取页面传入的查询时间
			String statisticsTime = String.valueOf(map.get("statisticsTime"));
			if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
				map.put("starttime", statisticsTime);
			}else{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				map.put("starttime", sdf.format(new Date()));
			}
			//查询当月的厨房检查信息
			List<Map<String,Object>> nowmonthlist = this.inspectService.getReportKitChenCheckInfo(map);
			int nowreportnum=this.inspectService.getKitchenListNumOnlyOne(map);	
			
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
			//查询上一个月的厨房检查信息
			List<Map<String,Object>> lastmonthlist = this.inspectService.getReportKitChenCheckInfo(map);
			int lastreportnum=this.inspectService.getKitchenListNumOnlyOne(map);	
			
			resultMap.put("nowmonth", nowmonth);
			resultMap.put("lastmonth", lastmonth);
			resultMap.put("lastmonthlist", lastmonthlist);
			resultMap.put("nowmonthlist", nowmonthlist);
			resultMap.put("nowreportnum", nowreportnum);
			resultMap.put("lastreportnum", lastreportnum);
			resultMap.put("status", 0);
			resultMap.put("message", "查询成功");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 查询离店报告信息-报表
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getReportLeaveShopInfo")
	public Map<String,Object> getReportLeaveShopInfo(@RequestParam Map<String,Object> map ,
			HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			//获取页面传入的查询时间
			String statisticsTime = String.valueOf(map.get("statisticsTime"));
			if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
				map.put("starttime", statisticsTime);
			}else{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM"); 
				map.put("starttime", sdf.format(new Date()));
			}
			//查询当月的离店检查信息
			List<Map<String,Object>> nowmonthlist = this.inspectService.getReportLeaveShopInfo(map);
			int nowreportnum=this.inspectService.getLeaveShopListNumOnlyOne(map);
			
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
			//查询上一个月的离店检查信息
			List<Map<String,Object>> lastmonthlist = this.inspectService.getReportLeaveShopInfo(map);
			int lastreportnum=this.inspectService.getLeaveShopListNumOnlyOne(map);
			
			resultMap.put("nowmonth", nowmonth);
			resultMap.put("lastmonth", lastmonth);
			resultMap.put("lastmonthlist", lastmonthlist);
			resultMap.put("nowmonthlist", nowmonthlist);
			resultMap.put("nowreportnum", nowreportnum);
			resultMap.put("lastreportnum", lastreportnum);
			resultMap.put("status", 0);
			resultMap.put("message", "查询成功");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 查询厨房检查表单---表单唯一
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getKitchenListOnlyOne")
	@ResponseBody
	public Map<String, Object> getKitchenListOnlyOne(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		//获取页面传入的查询时间
		String statisticsTime = String.valueOf(map.get("statisticsTime"));
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			map.put("starttime", statisticsTime);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			map.put("starttime", sdf.format(new Date()));
		}
		PageScroll page = new PageScroll();
		int num=this.inspectService.getKitchenListNumOnlyOne(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.inspectService.getKitchenListOnlyOne(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("inspectlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	
	/**
	 * 查询离店标高单--表单唯一
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getLeaveShopListOnlyOne")
	@ResponseBody
	public Map<String, Object> getLeaveShopListOnlyOne(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		//获取页面传入的查询时间
		String statisticsTime = String.valueOf(map.get("statisticsTime"));
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			map.put("starttime", statisticsTime);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			map.put("starttime", sdf.format(new Date()));
		}
		PageScroll page = new PageScroll();
		int num=this.inspectService.getLeaveShopListNumOnlyOne(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.inspectService.getLeaveShopListOnlyOne(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("inspectlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	
	/**
	 * 查询餐前检查单--表单唯一
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getBeforeMealListOnlyOne")
	@ResponseBody
	public Map<String, Object> getBeforeMealListOnlyOne(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		//获取页面传入的查询时间
		String statisticsTime = String.valueOf(map.get("statisticsTime"));
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			map.put("starttime", statisticsTime);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			map.put("starttime", sdf.format(new Date()));
		}
		PageScroll page = new PageScroll();
		int num=this.inspectService.getBeforeMealListNumOnlyOne(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.inspectService.getBeforeMealListOnlyOne(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("inspectlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	/*------------------------------pc端--------------------------------*/
	/**
	 * 查询厨房检查-表单
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getKitchenListOnlyOnePC")
	@ResponseBody
	public Map<String, Object> getKitchenListOnlyOnePC(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		//获取页面传入的查询时间
		String statisticsTime = String.valueOf(map.get("statisticsTime"));
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			map.put("starttime", statisticsTime);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			map.put("starttime", sdf.format(new Date()));
		}
		PageHelper page = new PageHelper(request);
		int num=this.inspectService.getKitchenListNumOnlyOne(map);		
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.inspectService.getKitchenListOnlyOne(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("inspectlist", datalist);
		data.put("status",0);
		data.put("pager",page.JSCateringPage().toString());
		return data;
	}
	
	
	/**
	 * 查询离店标高单--表单唯一
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getLeaveShopListOnlyOnePC")
	@ResponseBody
	public Map<String, Object> getLeaveShopListOnlyOnePC(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		//获取页面传入的查询时间
		String statisticsTime = String.valueOf(map.get("statisticsTime"));
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			map.put("starttime", statisticsTime);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			map.put("starttime", sdf.format(new Date()));
		}
		PageHelper page = new PageHelper(request);
		int num=this.inspectService.getLeaveShopListNumOnlyOne(map);		
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.inspectService.getLeaveShopListOnlyOne(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("inspectlist", datalist);
		data.put("status",0);
		data.put("pager",page.JSCateringPage().toString());
		return data;
	}
	
	
	/**
	 * 查询餐前检查单--表单唯一
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getBeforeMealListOnlyOnePC")
	@ResponseBody
	public Map<String, Object> getBeforeMealListOnlyOnePC(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		//获取页面传入的查询时间
		String statisticsTime = String.valueOf(map.get("statisticsTime"));
		if(statisticsTime != null && !"".equals(statisticsTime) && !"null".equals(statisticsTime)){
			map.put("starttime", statisticsTime);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			map.put("starttime", sdf.format(new Date()));
		}
		PageHelper page = new PageHelper(request);
		int num=this.inspectService.getBeforeMealListNumOnlyOne(map);		
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.inspectService.getBeforeMealListOnlyOne(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("inspectlist", datalist);
		data.put("status",0);
		data.put("pager",page.JSCateringPage().toString());
		return data;
	}
}
