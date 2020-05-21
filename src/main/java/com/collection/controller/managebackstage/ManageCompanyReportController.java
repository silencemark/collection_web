package com.collection.controller.managebackstage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.service.IndexService;
import com.collection.service.purchase.PurchaseService;
import com.collection.service.storefront.EverydayReportService;
import com.collection.service.warehouse.WarehouseService;
import com.collection.service.worksheet.InspectService;
import com.collection.util.PageHelper;
import com.collection.util.UserUtil;

@Controller
@RequestMapping(value="/managebackstage")
public class ManageCompanyReportController extends BaseController{

	@Resource private PurchaseService purchaseService;
	
	@Resource private IndexService indexService;
	
	@Resource private WarehouseService warehouseService;
	
	@Resource private EverydayReportService everydayReportService;
	
	@Resource private InspectService inspectService;
	
	/**
	 * 采购统计 按类别 统计（统计的   默认七天前到今天）
	 * 传入参数{"organizeid":5,"starttime":"2016-07-12 00:00:00","endtime":"2016-07-15 20:00:00"} 不传starttime和endtime 默认7天
	 * 传出参数{"message":"查询成功","status":0,"materiallist":[{"num":1,"sumprice":10,"name":"shit","orderid":"9d54541939f6455c8253a4df4f77328b","type":"chicken","specifications":"500ml","materielid":"35468be776b043f586c180073dbf5e2f"}],"ordernum":1}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPurchaseMaterialType")
	@ResponseBody
	public Map<String, Object> getPurchaseMaterialType(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
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
			cal.add(Calendar.DAY_OF_MONTH, -6);
			cal1.add(Calendar.DAY_OF_MONTH, 1);
			map.put("starttime",sdf.format(cal.getTime()));
			map.put("endtime",sdf.format(cal1.getTime()));
		}
		
		Map<String, Object> data=new HashMap<String, Object>();
		
		Map<String,Object> purchaseMap =  this.purchaseService.getPurchasePayAmount(map);
		List<Map<String, Object>> typelist=this.purchaseService.getMaterialType(map);
		if(typelist!=null && typelist.size()>0){
			data.put("typelist",typelist);
		}else{
			data.put("typelist", "");
		}
		data.put("map", map);
		data.put("status",0);
		data.put("purchaseMap", purchaseMap);
		data.put("message", "查询成功");
		return data;
	}
	
	
	/**
	 * 采购明细（统计的   默认七天前到今天）
	 * 传入参数{"type":"chicken","organizeid":5,"starttime":"2016-07-12 00:00:00","endtime":"2016-07-15 20:00:00"} 不传starttime和endtime 默认7天
	 * 传出参数{"message":"查询成功","status":0,"materiallist":[{"num":1,"sumprice":10,"name":"shit","orderid":"9d54541939f6455c8253a4df4f77328b","type":"chicken","specifications":"500ml","materielid":"35468be776b043f586c180073dbf5e2f"}],"ordernum":1}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPurchaseMaterialDetail")
	@ResponseBody
	public Map<String, Object> getPurchaseMaterialDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
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
			cal.add(Calendar.DAY_OF_MONTH, -6);
			cal1.add(Calendar.DAY_OF_MONTH, 1);
			map.put("starttime",sdf.format(cal.getTime()));
			map.put("endtime",sdf.format(cal1.getTime()));
		}
		
//		PageScroll page = new PageScroll();
		int ordernum=this.purchaseService.getMaterialDetailNum(map);		
//		page.setTotalRecords(ordernum);
//		page.initPage(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("ordernum", ordernum);
		List<Map<String, Object>> materiallist=this.purchaseService.getMaterialDetail(map);
		if(materiallist!=null && materiallist.size()>0){
			data.put("materiallist",materiallist);
		}else{
			data.put("materiallist", "");
		}
		data.put("status",0);
//		data.put("page",page);
		data.put("message", "查询成功");
		return data;
	}
	
	/**
	 * 采购(入库)单（统计的   默认七天前到今天）
	 * 传入参数{"type":"chicken","organizeid":5,"starttime":"2016-07-12 00:00:00","endtime":"2016-07-15 20:00:00"}
	 * 传出参数{"message":"查询成功","status":0,"ordernum":1,"purchaselist":[{"createtime":1468571619000,"companyid":"67702cc412264f4ea7d2c5f692070457","result":1,"updatetime":1468572302000,"createid":"690fb669ed4d40219964baad7783abd4","status":1,"examineuserid":"1","updateid":"1","organizeid":"5","orderno":"CGD20160715101700001","opinion":"ohmygod","orderid":"9d54541939f6455c8253a4df4f77328b"}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPurchaseStatisticsList")
	@ResponseBody
	public Map<String, Object> getPurchaseStatisticsList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> data=new HashMap<String, Object>(); 
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
			cal.add(Calendar.DAY_OF_MONTH, -6);
			cal1.add(Calendar.DAY_OF_MONTH, 1);
			map.put("starttime",sdf.format(cal.getTime()));
			map.put("endtime",sdf.format(cal1.getTime()));
		}
		//PageScroll page = new PageScroll();
		int ordernum=this.purchaseService.getPurchaseStatisticsListNum(map);		
//		page.setTotalRecords(ordernum);
//		page.initPage(map);
		data.put("ordernum", ordernum);
		List<Map<String, Object>> purchaselist=this.purchaseService.getPurchaseStatisticsList(map);
		if(purchaselist!=null && purchaselist.size()>0){
			data.put("purchaselist",purchaselist);
		}else{
			data.put("purchaselist", "");
		}
		data.put("status",0);
//		data.put("page",page);
		data.put("message", "查询成功");
		return data;
	}
	
	/**
	 * 采购(入库)单详情页面
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPurchaseInfo")
	public String getPurchaseInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {

		//采购(入库)单详情
		Map<String, Object> purchaseInfo=this.purchaseService.getPurchaseInfo(map);
		//采购物料列表
		List<Map<String, Object>> materiallist=this.purchaseService.getPurchaseMaterialList(purchaseInfo);
		purchaseInfo.put("materiallist", materiallist);
		model.addAttribute("purchaseInfo", purchaseInfo);
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("map",map);
		return "/managebackstage/company/report/manage_purchase_detail";
	}
	
	/**
	 * 用料单详情页面
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getMaterialInfo")
	public String getMaterialInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//用料单详情
		Map<String, Object> materialInfo=this.warehouseService.getMaterialInfo(map);
		//用料单物料列表
		List<Map<String, Object>> materiallist=this.warehouseService.getUsedMaterialList(materialInfo);
		model.addAttribute("materialInfo", materialInfo);
		model.addAttribute("materiallist", materiallist);
		model.addAttribute("map", map);
		return "/managebackstage/company/report/manage_use_detail";
	}
	
	/**
	 * 退货单详情页面
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReturnInfo")
	public String getReturnInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//退货单详情
		Map<String, Object> returnInfo=this.warehouseService.getReturnInfo(map);
		//退货单物料列表
		List<Map<String, Object>> materiallist=this.warehouseService.getReturnMaterialList(returnInfo);
		model.addAttribute("returnInfo", returnInfo);
		model.addAttribute("materiallist", materiallist);
		model.addAttribute("map", map);

		return "/managebackstage/company/report/manage_returngoods_detail";
	}
	
	/**
	 * 报损单详情页面
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReportlossInfo")
	public String getReportlossInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//报损单详情
		Map<String, Object> reportlossInfo=this.warehouseService.getReportlossInfo(map);
		//报损单物料列表
		List<Map<String, Object>> materiallist=this.warehouseService.getReportlossMaterialList(reportlossInfo);
		model.addAttribute("reportlossInfo", reportlossInfo);
		model.addAttribute("materiallist", materiallist);
		model.addAttribute("map", map);
		return "/managebackstage/company/report/manage_breakdown_detail";
	}
	
	/**
	 * 进入月报表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoMonthlyReportPage")
	public String intoMonthlyReportPage(@RequestParam Map<String,Object> map ,
			HttpServletRequest request){
		map.put("type", 3);
		List<Map<String,Object>> list = this.indexService.getOrganizeList(map);
		if(list != null && list.size() > 0){
			map.put("organizeid", list.get(0).get("organizeid"));
			map.put("organizename", list.get(0).get("organizename"));
		}
		request.setAttribute("map", map);
		return "/managebackstage/company/report/manage_monthly_report";
	}
	
	/**
	 * 查询库存分析
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoManageAnalysisPage")
	public String intoManageAnalysisPage(@RequestParam Map<String,Object> map ,
			HttpServletRequest request){
		map.put("type", 3);
		List<Map<String,Object>> list = this.indexService.getOrganizeList(map);
		if(list != null && list.size() > 0){
			map.put("organizeid", list.get(0).get("organizeid"));
			map.put("organizename", list.get(0).get("organizename"));
		}
		request.setAttribute("map", map);
		return "/managebackstage/company/report/manage_analysis";
	}
	
/*-----------------------------------------------------每日报表--------------------------------------------------------------------------------------*/	
	
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
		map.put("type", 3);
		List<Map<String,Object>> list = this.indexService.getOrganizeList(map);
		if(list != null && list.size() > 0){
			map.put("organizeid", list.get(0).get("organizeid"));
			map.put("organizename", list.get(0).get("organizename"));
		}
		request.setAttribute("map", map);
		return "/managebackstage/company/report/report_yye";
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
		map.put("type", 3);
		List<Map<String,Object>> list = this.indexService.getOrganizeList(map);
		if(list != null && list.size() > 0){
			map.put("organizeid", list.get(0).get("organizeid"));
			map.put("organizename", list.get(0).get("organizename"));
		}
		request.setAttribute("map", map);
		return "/managebackstage/company/report/report_kll";
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
		map.put("type", 3);
		List<Map<String,Object>> list = this.indexService.getOrganizeList(map);
		if(list != null && list.size() > 0){
			map.put("organizeid", list.get(0).get("organizeid"));
			map.put("organizename", list.get(0).get("organizename"));
		}
		request.setAttribute("map", map);
		return "/managebackstage/company/report/report_rjxf";
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
		map.put("type", 3);
		List<Map<String,Object>> list = this.indexService.getOrganizeList(map);
		if(list != null && list.size() > 0){
			map.put("organizeid", list.get(0).get("organizeid"));
			map.put("organizename", list.get(0).get("organizename"));
		}
		request.setAttribute("map", map);
		return "/managebackstage/company/report/report_myd";
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
		map.put("type", 3);
		List<Map<String,Object>> list = this.indexService.getOrganizeList(map);
		if(list != null && list.size() > 0){
			map.put("organizeid", list.get(0).get("organizeid"));
			map.put("organizename", list.get(0).get("organizename"));
		}
		request.setAttribute("map", map);
		return "/managebackstage/company/report/report_cql";
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
		map.put("type", 3);
		List<Map<String,Object>> list = this.indexService.getOrganizeList(map);
		if(list != null && list.size() > 0){
			map.put("organizeid", list.get(0).get("organizeid"));
			map.put("organizename", list.get(0).get("organizename"));
		}
		request.setAttribute("map", map);
		return "/managebackstage/company/report/report_cqjc";
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
		map.put("type", 3);
		List<Map<String,Object>> list = this.indexService.getOrganizeList(map);
		if(list != null && list.size() > 0){
			map.put("organizeid", list.get(0).get("organizeid"));
			map.put("organizename", list.get(0).get("organizename"));
		}
		request.setAttribute("map", map);
		return "/managebackstage/company/report/report_cfjc";
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
		map.put("type", 3);
		List<Map<String,Object>> list = this.indexService.getOrganizeList(map);
		if(list != null && list.size() > 0){
			map.put("organizeid", list.get(0).get("organizeid"));
			map.put("organizename", list.get(0).get("organizename"));
		}
		request.setAttribute("map", map);
		return "/managebackstage/company/report/report_ldbg";
	}
	
	@RequestMapping("/getEverydayReportListOnlyOne")
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
	
	/**
	 * 查询厨房检查-表单
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getKitchenListOnlyOne")
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
	@RequestMapping("/getLeaveShopListOnlyOne")
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
	@RequestMapping("/getBeforeMealListOnlyOne")
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
		Map<String, Object> precheckinfo = this.inspectService.getBeforeMealInfo(map);
			
		precheckinfo.put("starlist", JSONArray.fromObject(precheckinfo.get("starlist")));

		model.addAttribute("precheckinfo", precheckinfo);
		model.addAttribute("map", map);
		return "/managebackstage/company/report/precheck_detail";
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

		Map<String, Object> leaveinfo = this.inspectService.getLeaveShopInfo(map);
				
		leaveinfo.put("starlist", JSONArray.fromObject(leaveinfo.get("starlist")));

		model.addAttribute("leaveinfo", leaveinfo);
		model.addAttribute("map", map);
		return "/managebackstage/company/report/leave_detail";
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
		Map<String, Object> KitchenInfo=this.inspectService.getKitchenInfo(map);
		KitchenInfo.put("starlist", JSONArray.fromObject(KitchenInfo.get("starlist")));
		model.addAttribute("KitchenInfo", KitchenInfo);
		model.addAttribute("map", map);
		return "/managebackstage/company/report/kitchencheck_detail";
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
		model.addAttribute("map",map);
		return "/managebackstage/company/report/dailyreport_detail";
	}
}
