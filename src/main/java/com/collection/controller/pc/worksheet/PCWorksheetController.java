package com.collection.controller.pc.worksheet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.controller.userbackstage.UserIndexController.ExportExcel;
import com.collection.service.worksheet.InspectService;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;
import com.collection.util.UserUtil;


/**
 * 仓库管理   检查管理
 * @author silence
 *
 */
@Controller
@RequestMapping("/pc")
public class PCWorksheetController  extends BaseController{
	@SuppressWarnings("unused")
	private transient static Log log = LogFactory.getLog(PCWorksheetController.class);
	
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
/**===============================================================离店检查========================================================================================================================================================================================******/
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
			int sta = 1;
			for(Map<String, Object> star:starlist){
				star.put("inspectid", inspectid);
				star.put("createid", map.get("createid"));
				star.put("priority", sta);
				this.inspectService.insertInspectStar(star);
				sta++;
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
	@RequestMapping("/getLeaveShopListInfo")
	public String getLeaveShopListInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		
		PageHelper page = new PageHelper(request);
		
		int num=this.inspectService.getLeaveShopListNum(map);		
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> leavelist=this.inspectService.getLeaveShopList(map);
		model.addAttribute("page", page.cateringPage().toString());
		model.addAttribute("leavelist", leavelist);
		model.addAttribute("map", map);
		return "/pc/worksheet/leave_list";
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
	@RequestMapping("/getLeavedetailInfo")
	public  String  getLeavedetailInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userInfo = UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);

		Map<String, Object> leaveinfo = this.inspectService.getLeaveShopInfo(map);
				
		leaveinfo.put("starlist", JSONArray.fromObject(leaveinfo.get("starlist")));

		model.addAttribute("leaveinfo", leaveinfo);
		model.addAttribute("map", map);
			return "/pc/worksheet/leave_detail";
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
	/**
	 * 离店检查列表导出
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportLeaveShopList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLeaveShopList(@RequestParam Map<String, Object> map,
			HttpServletRequest request) {
		String importurl = request.getSession().getServletContext()
				.getRealPath("/upload/excel/离店检查列表.xls");

		List<Map<String, Object>> leaveShopList = this.inspectService.getLeaveShopList(map);
		
				

		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		String[] headers = { "检查人","区域","检查时间","状态"};
	
		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> order : leaveShopList) {
			TreeMap<String, Object> datamap = new TreeMap<String, Object>();
			datamap.put("a",order.get("createname") == null ? "" : order.get("createname"));
			datamap.put("b",order.get("templatename") == null ? "" : order.get("templatename"));
			datamap.put("c",order.get("createtime")==null ? "":order.get("createtime"));
			if (String.valueOf(order.get("status")).equals("1")) {
				datamap.put("f", "已处理");
			} else {
				datamap.put("f", "未处理");
			}
			dataset.add(datamap);
		}
		try {
			OutputStream out = new FileOutputStream(importurl);
			ex.exportExcel(headers, dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "离店检查列表.xls";
	}


	/**
	 * 离店检查单详情
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/exportLeaveShopDetailList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLeaveShopDetailList(@RequestParam Map<String, Object> map,
			HttpServletRequest request) {
		String importurl = request.getSession().getServletContext()
				.getRealPath("/upload/excel/离店检查单详情.xls");

	Map<String, Object>  leaveShopList  = this.inspectService
				.getLeaveShopInfo(map);
		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		String[] headers = {"所属架构","检查区域","检查项目","检查人","填写时间","抄送人","抄送人意见"};

		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		TreeMap<String, Object> datamap=new TreeMap<String, Object>();
		   
	       datamap.put("a", leaveShopList.get("organizename")==null?"":leaveShopList.get("organizename"));
	       datamap.put("b", leaveShopList.get("templatename")==null?"":leaveShopList.get("templatename"));
	       datamap.put("c", leaveShopList.get("feeding")==null?"":leaveShopList.get("feeding"));
	       
	       datamap.put("d", leaveShopList.get("createname")==null?"":leaveShopList.get("createname"));
	       datamap.put("e", leaveShopList.get("createtime")==null?"":leaveShopList.get("createtime"));
	       
	       datamap.put("f", leaveShopList.get("examinename")==null?"":leaveShopList.get("examinename"));
	       
	       if (String.valueOf(leaveShopList.get("status")).equals("1")) {
	    	   datamap.put("g", leaveShopList.get("opinion")==null?"":leaveShopList.get("opinion"));
			} else {
				datamap.put("g", "");
			}
	       
	       dataset.add(datamap);
		try {
			OutputStream out = new FileOutputStream(importurl);
			ex.exportExcel(headers, dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "离店检查单详情.xls";
	}

	
	/*----------------------------------------------------餐前检查------------------------------------------------------------*/
	
	
	/**
	 *添加餐前检查信息
	 */
	@RequestMapping("/addBeforeMeal")
	public String getPcBeforeMealDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		request.setAttribute("map", map);
		return "/pc/worksheet/precheck_add";
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
			int sta = 1;
			for(Map<String, Object> star:starlist){
				star.put("inspectid", inspectid);
				star.put("createid", map.get("createid"));
				star.put("priority", sta);
				this.inspectService.insertInspectStar(star);
				sta++;
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
	public String getBeforeMealList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		
		PageHelper page = new PageHelper(request);
		int num=this.inspectService.getBeforeMealListNum(map);		
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> prechecklist=this.inspectService.getBeforeMealList(map);
		model.addAttribute("prechecklist", prechecklist);
		model.addAttribute("page",page.cateringPage().toString());
		model.addAttribute("map", map);
		return "/pc/worksheet/precheck_list";
		
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
	public String getPreCheckInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userInfo = UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);

		Map<String, Object> precheckinfo = this.inspectService.getBeforeMealInfo(map);
			
		precheckinfo.put("starlist", JSONArray.fromObject(precheckinfo.get("starlist")));

		model.addAttribute("precheckinfo", precheckinfo);
		model.addAttribute("map", map);
			return "/pc/worksheet/precheck_detail";
		
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
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			map.put("status", 1);
			map.put("updatetime", new Date());
			map.put("updateid", map.get("userid"));
			this.inspectService.updateBeforeMeal(map);
			data.put("status", 0);
			data.put("message", "操作成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return data;
	}
	/**
	 * 餐前检查列表导出
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportBeforeMealList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportBeforeMealList(@RequestParam Map<String, Object> map,
			HttpServletRequest request) {
		String importurl = request.getSession().getServletContext()
				.getRealPath("/upload/excel/餐前检查列表.xls");

		List<Map<String, Object>> beforeMealList = this.inspectService.getBeforeMealList(map);
		
				

		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		String[] headers = { "检查人","区域","检查时间","状态"};
	
		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> order : beforeMealList) {
			TreeMap<String, Object> datamap = new TreeMap<String, Object>();
			datamap.put("a",order.get("createname") == null ? "" : order.get("createname"));
			datamap.put("b",order.get("templatename") == null ? "" : order.get("templatename"));
			datamap.put("c",order.get("createtime")==null ? "":order.get("createtime"));
			if (String.valueOf(order.get("status")).equals("1")) {
				datamap.put("f", "已处理");
			} else {
				datamap.put("f", "未处理");
			}
			dataset.add(datamap);
		}
		try {
			OutputStream out = new FileOutputStream(importurl);
			ex.exportExcel(headers, dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "餐前检查列表.xls";
	}


	/**
	 * 离店检查单详情
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/exportBeforeMealDetailList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportBeforeMealDetailList(@RequestParam Map<String, Object> map,
			HttpServletRequest request) {
		String importurl = request.getSession().getServletContext()
				.getRealPath("/upload/excel/餐前检查单详情.xls");

	Map<String, Object>  beforeMealList  = this.inspectService
				.getBeforeMealInfo(map);
		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		String[] headers = {"所属架构","检查区域","检查项目","检查人","填写时间","抄送人","抄送人意见"};

		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		TreeMap<String, Object> datamap=new TreeMap<String, Object>();
		   
	       datamap.put("a", beforeMealList.get("organizename")==null?"":beforeMealList.get("organizename"));
	       datamap.put("b", beforeMealList.get("templatename")==null?"":beforeMealList.get("templatename"));
	       datamap.put("c", beforeMealList.get("feeding")==null?"":beforeMealList.get("feeding"));
	       
	       datamap.put("d", beforeMealList.get("createname")==null?"":beforeMealList.get("createname"));
	       datamap.put("e", beforeMealList.get("createtime")==null?"":beforeMealList.get("createtime"));
	       
	       datamap.put("f", beforeMealList.get("examinename")==null?"":beforeMealList.get("examinename"));
	       
	       if (String.valueOf(beforeMealList.get("status")).equals("1")) {
	    	   datamap.put("g", beforeMealList.get("opinion")==null?"":beforeMealList.get("opinion"));
			} else {
				datamap.put("g", "");
			}
	       
	       dataset.add(datamap);
		try {
			OutputStream out = new FileOutputStream(importurl);
			ex.exportExcel(headers, dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "餐前检查单详情.xls";
	}

	
	
/*----------------------------------------------------厨房检查------------------------------------------------------------*/

	
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
			int sta = 1;
			for(Map<String, Object> star:starlist){
				star.put("inspectid", inspectid);
				star.put("createid", map.get("createid"));
				star.put("priority", sta);
				this.inspectService.insertInspectStar(star);
				sta++;
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
	public String getKitchenList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		
		PageHelper page = new PageHelper(request);
		int num=this.inspectService.getKitchenListNum(map);		
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> kitchenlist=this.inspectService.getKitchenList(map);
		model.addAttribute("kitchenlist", kitchenlist);
		model.addAttribute("page",page.cateringPage().toString());
		model.addAttribute("map", map);
		return "/pc/worksheet/kitchencheck_list";
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
	public String getKitchenInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userInfo", userInfo);
		
			Map<String, Object> KitchenInfo=this.inspectService.getKitchenInfo(map);
			
			KitchenInfo.put("starlist", JSONArray.fromObject(KitchenInfo.get("starlist")));
			model.addAttribute("KitchenInfo", KitchenInfo);
			model.addAttribute("map", map);
			return "/pc/worksheet/kitchencheck_detail";
			
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
	 * 厨房检查列表导出
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportKitchenList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportKitchenList(@RequestParam Map<String, Object> map,
			HttpServletRequest request) {
		String importurl = request.getSession().getServletContext()
				.getRealPath("/upload/excel/厨房检查列表.xls");

		List<Map<String, Object>> kitchenList= this.inspectService.getKitchenList(map);
		
				

		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		String[] headers = { "检查人","区域","检查时间","状态"};
	
		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> order : kitchenList) {
			TreeMap<String, Object> datamap = new TreeMap<String, Object>();
			datamap.put("a",order.get("createname") == null ? "" : order.get("createname"));
			datamap.put("b",order.get("templatename") == null ? "" : order.get("templatename"));
			datamap.put("c",order.get("createtime")==null ? "":order.get("createtime"));
			if (String.valueOf(order.get("status")).equals("1")) {
				datamap.put("f", "已处理");
			} else {
				datamap.put("f", "未处理");
			}
			dataset.add(datamap);
		}
		try {
			OutputStream out = new FileOutputStream(importurl);
			ex.exportExcel(headers, dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "厨房检查列表.xls";
	}


	/**
	 * 厨房检查单详情
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/exportKitchenDetailList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportKitchenDetailList(@RequestParam Map<String, Object> map,
			HttpServletRequest request) {
		String importurl = request.getSession().getServletContext()
				.getRealPath("/upload/excel/厨房检查单详情.xls");

	Map<String, Object> kitchenList  = this.inspectService
				.getKitchenInfo(map);
		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		String[] headers = {"所属架构","检查区域","检查项目","检查人","填写时间","抄送人","抄送人意见"};

		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		TreeMap<String, Object> datamap=new TreeMap<String, Object>();
		   
	       datamap.put("a", kitchenList.get("organizename")==null?"":kitchenList.get("organizename"));
	       datamap.put("b", kitchenList.get("templatename")==null?"":kitchenList.get("templatename"));
	       datamap.put("c", kitchenList.get("feeding")==null?"":kitchenList.get("feeding"));
	       
	       datamap.put("d", kitchenList.get("createname")==null?"":kitchenList.get("createname"));
	       datamap.put("e", kitchenList.get("createtime")==null?"":kitchenList.get("createtime"));
	       
	       datamap.put("f", kitchenList.get("examinename")==null?"":kitchenList.get("examinename"));
	       
	       if (String.valueOf(kitchenList.get("status")).equals("1")) {
	    	   datamap.put("g", kitchenList.get("opinion")==null?"":kitchenList.get("opinion"));
			} else {
				datamap.put("g", "");
			}
	       
	       dataset.add(datamap);
		try {
			OutputStream out = new FileOutputStream(importurl);
			ex.exportExcel(headers, dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "厨房检查单详情.xls";
	}

	
	
	/**
	 * 进入菜品成本页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoCostAdd")
	public String intoCostAdd(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("map", map);
		return "/pc/worksheet/cost_add";
	}
	
	/**
	 * 进入厨房检查页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoKitChencheckAdd")
	public String intoKitChencheckAdd(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("map", map);
		return "/pc/worksheet/kitchencheck_add";
	}
	
	/**
	 * 进入离店检查页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoLeaveAdd")
	public String intoLeaveAdd(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("map", map);
		return "/pc/worksheet/leave_add";
	}
}
