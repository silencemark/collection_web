package com.collection.controller.warehouse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.collection.service.IndexService;
import com.collection.service.purchase.PurchaseService;
import com.collection.service.warehouse.WarehouseService;
import com.collection.util.PageScroll;


/**
 * 仓库管理
 * @author silence
 *
 */
@Controller
@RequestMapping("/app")
public class WarehouseController {
	private transient static Log log = LogFactory.getLog(WarehouseController.class);
	
	@Resource private WarehouseService warehouseService;
	
	@Resource private IndexService indexService;
	
	@Resource private PurchaseService purchaseService;
	/**
	 * 获取未读数量 仓库管理
	 * 传入参数{"receiveid":1,"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数{"materialnum":1,"renturnnum":1,"reportlossnum":1}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getWarehouseNotreadNum")
	@ResponseBody
	public Map<String, Object> inintLoginPassword(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		map.put("isread", 0);
		Map<String, Object> data =new HashMap<String, Object>();
		//用料单
		List<Integer> resourcetypes=new ArrayList<Integer>();
		resourcetypes.add(3);
		map.put("resourcetypes", resourcetypes);
		int usenum=this.indexService.getForwordNotreadNum(map);
		data.put("usenum", usenum);
		//退货单
		resourcetypes=new ArrayList<Integer>();
		resourcetypes.add(4);
		map.put("resourcetypes", resourcetypes);
		int returnnum=this.indexService.getForwordNotreadNum(map);
		data.put("returnnum", returnnum);
		//报损单
		resourcetypes=new ArrayList<Integer>();
		resourcetypes.add(5);
		map.put("resourcetypes", resourcetypes);
		int reportlossnum=this.indexService.getForwordNotreadNum(map);
		data.put("reportlossnum", reportlossnum);
		return data;
	}
	
	/**
	 * 查询 用料单 已处理，未处理的 未读数量
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getMaterialOrderAllNotRead")
	public Map<String,Object> getMaterialOrderAllNotRead(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.warehouseService.getMaterialListNum(map);	
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.warehouseService.getMaterialOrderByDateNum(map);	
			
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
	 * 用料单列表
	 * 传入参数{"userid":1,"status":0,"companyid":"67702cc412264f4ea7d2c5f692070457"}  status：0 未处理 1：已处理
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getMaterialOrder")
	@ResponseBody
	public Map<String, Object> getMaterialOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		PageScroll page = new PageScroll();
		int ordernum=this.warehouseService.getMaterialListNum(map);
		page.setTotalRecords(ordernum);
		page.initPage(map);
		List<Map<String, Object>> orderlist=this.warehouseService.getMaterialList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("ordernum", ordernum);
		data.put("orderlist", orderlist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 采购(入库)单查询(已处理)
	 * 传入参数 {"userid":1,"status":1,"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * status 1：已处理 0：未处理    starttime 和 endtime 不传 默认近7天
	 * 传出参数{"status":0,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"ordernum":1,"map":{"userid":"1","status":"1","companyid":"67702cc412264f4ea7d2c5f692070457","starttime":"2016-07-20","endtime":"2016-07-27","startnum":0,"rownum":10},"list":[{"createtime":"2016-07-25","count":1,"orderlist":[{"result":1,"updatetime":"2016-07-25 22:53:59","status":1,"isread":1,"materialprice":600,"examineuserid":"1","updateid":"1","createdate":"2016-07-25","orderno":"CGD20160725225000002","examinename":"李语然","createtime":"22:50:25","companyid":"67702cc412264f4ea7d2c5f692070457","createhour":"22:50","forwarduserid":"34da2a2a4fcc40d8bbb4686495351be4","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":1,"opinion":"阿桑大苏打","orderid":"2fd957b0b6494d6da7388cc38e5c2384","createname":"silence"},{"status":0,"isread":0,"examineuserid":"1","materialprice":100,"createdate":"2016-07-25","orderno":"CGD20160725224800001","examinename":"李语然","createtime":"22:49:06","companyid":"67702cc412264f4ea7d2c5f692070457","forwarduserid":"739aa3439efc4795b47f4308a0051cb9","createhour":"22:49","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":1,"orderid":"14530d80b2f74e30bf55e12bad0aeb1e","createname":"silence"},{"result":1,"updatetime":"2016-07-25 22:53:59","status":1,"isread":0,"materialprice":600,"examineuserid":"1","updateid":"1","createdate":"2016-07-25","orderno":"CGD20160725225000002","examinename":"李语然","createtime":"22:50:25","companyid":"67702cc412264f4ea7d2c5f692070457","createhour":"22:50","forwarduserid":"7dcf6ef75f084bd59e9030f537e638e9","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":1,"opinion":"阿桑大苏打","orderid":"2fd957b0b6494d6da7388cc38e5c2384","createname":"silence"},{"status":0,"isread":1,"examineuserid":"1","materialprice":100,"createdate":"2016-07-25","orderno":"CGD20160725224800001","examinename":"李语然","createtime":"22:49:06","companyid":"67702cc412264f4ea7d2c5f692070457","forwarduserid":"cc976ceae34a4f81adc2dbbdd3422d94","createhour":"22:49","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":1,"orderid":"14530d80b2f74e30bf55e12bad0aeb1e","createname":"silence"}]}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getMaterialOrderByDate")
	@ResponseBody
	public Map<String, Object> getMaterialOrderByDate(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
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
			cal.add(Calendar.DAY_OF_MONTH, -6);
			cal1.add(Calendar.DAY_OF_MONTH, 1);
			map.put("starttime",sdf.format(cal.getTime()));
			map.put("endtime",sdf.format(cal1.getTime()));
		}
		
		PageScroll page = new PageScroll();
		int ordernum=this.warehouseService.getMaterialOrderByDateNum(map);		
		page.setTotalRecords(ordernum);
		page.initPage(map);
		List<Map<String, Object>> list=this.warehouseService.getMaterialOrderByDate(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("ordernum", ordernum);
		data.put("list", list);
		data.put("page",page);
		data.put("map", map);
		data.put("status",0);
		return data;
	}
	
	/**
	 * 初始化添加用料单（获取用料单编号）
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initInsertMaterial")
	@ResponseBody
	public Map<String, Object> initInsertMaterial(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("companyid") && !"".equals(map.get("companyid"))){
			int materialnum=this.warehouseService.getMaterialNumByCompany(map);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmm");
			data.put("status", 0);
			data.put("orderno","YLD"+sdf.format(new Date())+String.format("%05d",(materialnum+1)));
			SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			data.put("createtime",sdf1.format(new Date()));
			//查询 采购(入库)单的物料明细的 所有 物料类型
			Map<String, Object> materialmap=new HashMap<String, Object>();
			materialmap.put("companyid", map.get("companyid"));
			materialmap.put("organizeid", map.get("organizeid"));
			materialmap.put("grouptype", 1);
			materialmap.put("result", 1);
			List<Map<String, Object>> materialTypeList=this.purchaseService.getPurchaseMaterialList(materialmap);
			//查询 采购(入库)单的物料明细的 所有 物料名称
			materialmap=new HashMap<String, Object>();
			materialmap.put("companyid", map.get("companyid"));
			materialmap.put("organizeid", map.get("organizeid"));
			materialmap.put("groupname", 1);
			materialmap.put("result", 1);
			List<Map<String, Object>> materialNameList=this.purchaseService.getPurchaseMaterialList(materialmap);

			data.put("materialTypeList",materialTypeList);
			data.put("materialNameList",materialNameList);
			data.put("message", "成功");
		}else{
			data.put("status", 1);
			data.put("message", "传入参数错误");
		}
		return data;
	}
	/**
	 * 获取下级采购(入库)单物料信息
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPurchaseMaterial")
	@ResponseBody
	public Map<String, Object> getPurchaseMaterial(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		List<Map<String, Object>> materialList=this.purchaseService.getPurchaseMaterialList(map);
		data.put("status", 0);
		data.put("materialList", materialList);
		return data;
	}
	
	@RequestMapping("/getapplyMaterialname")
	@ResponseBody
	public Map<String, Object> getApplyMaterialname(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		//查询数据
		
		List<Map<String, Object>> materialList=this.purchaseService.getApplynameMaterialList(map);
		data.put("status", 0);
		data.put("materialList", materialList);
		return data;
	}
	
	/**
	 * 添加用料单
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertMaterial")
	@ResponseBody
	public Map<String, Object> insertMaterial(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>(); 
		String orderid="";
		try {
			//添加到用料单
			orderid=this.warehouseService.insertMaterial(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加用料单错误");
			return data;
		}
		try {
			//添加到物料单
			JSONObject json=JSONObject.fromObject(map.get("materiallist")+"");
			List<Map<String, Object>> materiallist=(List<Map<String, Object>>)json.get("materiallist");
			for(Map<String, Object> material:materiallist){
				material.put("companyid", map.get("companyid"));
				material.put("orderid", orderid);
				this.warehouseService.insertUsedMaterial(material);
			}
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加用料物料错误");
			return data;
		}
		data.put("status", 0);
		data.put("message", "添加成功");
		return data;
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
	@ResponseBody
	public Map<String, Object> getMaterialInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			//用料单详情
			Map<String, Object> applyInfo=this.warehouseService.getMaterialInfo(map);
			//用料单物料列表
			List<Map<String, Object>> applylist=this.warehouseService.getUsedMaterialList(applyInfo);
			data.put("applyInfo", applyInfo);
			data.put("applylist", applylist);
			data.put("status", 0);
			data.put("message", "添加成功");
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "查询失败");
			return data;
		}
	}

	@RequestMapping("/getsendname")
	@ResponseBody
	public Map<String, Object> getSengNameByforwardid(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		
		if(!map.containsKey("forwarduserid") || map.get("forwarduserid")==null || "".equals(map.get("forwarduserid"))){
			data.put("status", 1);
			data.put("message", "参数");
			return data;
		}
		try {
			//用料单详情
			Map<String, Object> sendinfo=this.warehouseService.getSendNameByForadid(map);
			data.put("sendinfo", sendinfo);
			data.put("status", 0);
			data.put("message", "添加成功");
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "查询失败");
			return data;
		}
	}
	
	/**
	 * 审核用料单  同意或者拒绝
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineMaterialOrder")
	@ResponseBody
	public Map<String, Object> examineMaterialOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		String bool=this.warehouseService.updateMaterialOrder(map);
		if("1".equals(bool)){
			data.put("status", 0);
			data.put("message", "审核成功");
		}else{
			data.put("status", 1);
			data.put("message", "库存不足");
		}
		return data;
	}
	
	
	/*------------------------------------------------退货单------------------------------------------------------*/
	
	
	/**
	 * 查询 退货单 已处理，未处理的 未读数量
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getReturnOrderAllNotRead")
	public Map<String,Object> getReturnOrderAllNotRead(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.warehouseService.getReturnOrderListNum(map);	
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.warehouseService.getReturnOrderByDateNum(map);	
			
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
	 * 退货单列表(未处理)
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReturnOrder")
	@ResponseBody
	public Map<String, Object> getReturnOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		map.put("status",0);
		PageScroll page = new PageScroll();
		int ordernum=this.warehouseService.getReturnOrderListNum(map);		
		page.setTotalRecords(ordernum);
		page.initPage(map);
		List<Map<String, Object>> orderlist=this.warehouseService.getReturnOrderList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("ordernum", ordernum);
		data.put("orderlist", orderlist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 退货单列表(已处理)
	 * 传入参数 {"userid":1,"status":1,"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * status 1：已处理 0：未处理    starttime 和 endtime 不传 默认近7天
	 * 传出参数{"status":0,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"ordernum":1,"map":{"userid":"1","status":"1","companyid":"67702cc412264f4ea7d2c5f692070457","starttime":"2016-07-20","endtime":"2016-07-27","startnum":0,"rownum":10},"list":[{"createtime":"2016-07-25","count":1,"orderlist":[{"result":1,"updatetime":"2016-07-25 22:53:59","status":1,"isread":1,"materialprice":600,"examineuserid":"1","updateid":"1","createdate":"2016-07-25","orderno":"CGD20160725225000002","examinename":"李语然","createtime":"22:50:25","companyid":"67702cc412264f4ea7d2c5f692070457","createhour":"22:50","forwarduserid":"34da2a2a4fcc40d8bbb4686495351be4","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":1,"opinion":"阿桑大苏打","orderid":"2fd957b0b6494d6da7388cc38e5c2384","createname":"silence"},{"status":0,"isread":0,"examineuserid":"1","materialprice":100,"createdate":"2016-07-25","orderno":"CGD20160725224800001","examinename":"李语然","createtime":"22:49:06","companyid":"67702cc412264f4ea7d2c5f692070457","forwarduserid":"739aa3439efc4795b47f4308a0051cb9","createhour":"22:49","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":1,"orderid":"14530d80b2f74e30bf55e12bad0aeb1e","createname":"silence"},{"result":1,"updatetime":"2016-07-25 22:53:59","status":1,"isread":0,"materialprice":600,"examineuserid":"1","updateid":"1","createdate":"2016-07-25","orderno":"CGD20160725225000002","examinename":"李语然","createtime":"22:50:25","companyid":"67702cc412264f4ea7d2c5f692070457","createhour":"22:50","forwarduserid":"7dcf6ef75f084bd59e9030f537e638e9","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":1,"opinion":"阿桑大苏打","orderid":"2fd957b0b6494d6da7388cc38e5c2384","createname":"silence"},{"status":0,"isread":1,"examineuserid":"1","materialprice":100,"createdate":"2016-07-25","orderno":"CGD20160725224800001","examinename":"李语然","createtime":"22:49:06","companyid":"67702cc412264f4ea7d2c5f692070457","forwarduserid":"cc976ceae34a4f81adc2dbbdd3422d94","createhour":"22:49","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":1,"orderid":"14530d80b2f74e30bf55e12bad0aeb1e","createname":"silence"}]}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReturnOrderByDate")
	@ResponseBody
	public Map<String, Object> getReturnOrderByDate(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
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
			cal.add(Calendar.DAY_OF_MONTH, -6);
			cal1.add(Calendar.DAY_OF_MONTH, 1);
			map.put("starttime",sdf.format(cal.getTime()));
			map.put("endtime",sdf.format(cal1.getTime()));
		}
		
		PageScroll page = new PageScroll();
		int ordernum=this.warehouseService.getReturnOrderByDateNum(map);		
		page.setTotalRecords(ordernum);
		page.initPage(map);
		List<Map<String, Object>> list=this.warehouseService.getReturnOrderByDate(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("ordernum", ordernum);
		data.put("list", list);
		data.put("page",page);
		data.put("map", map);
		data.put("status",0);
		return data;
	}
	

	/**
	 * 初始化添加退货单（获取退货单编号）
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initInsertReturn")
	@ResponseBody
	public Map<String, Object> initInsertReturn(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("companyid") && !"".equals(map.get("companyid"))){
			int ordernum=this.warehouseService.getReturnNumByCompany(map);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmm");
			data.put("status", 0);
			data.put("orderno","THD"+sdf.format(new Date())+String.format("%05d",(ordernum+1)));
			SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			data.put("createtime",sdf1.format(new Date()));
			//查询 采购(入库)单的物料明细的 所有 物料类型
			Map<String, Object> materialmap=new HashMap<String, Object>();
			materialmap.put("companyid", map.get("companyid"));
			materialmap.put("organizeid", map.get("organizeid"));
			materialmap.put("grouptype", 1);
			materialmap.put("result", 1);
			List<Map<String, Object>> materialTypeList=this.purchaseService.getPurchaseMaterialList(materialmap);
			//查询 采购(入库)单的物料明细的 所有 物料名称
			materialmap=new HashMap<String, Object>();
			materialmap.put("companyid", map.get("companyid"));
			materialmap.put("organizeid", map.get("organizeid"));
			materialmap.put("groupname", 1);
			materialmap.put("result", 1);
			List<Map<String, Object>> materialNameList=this.purchaseService.getPurchaseMaterialList(materialmap);

			data.put("materialTypeList",materialTypeList);
			data.put("materialNameList",materialNameList);
			data.put("message", "成功");
		}else{
			data.put("status", 1);
			data.put("message", "传入参数错误");
		}
		return data;
	}
	
	/**
	 * 添加退货单
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertReturn")
	@ResponseBody
	public Map<String, Object> insertReturn(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>(); 
		String orderid="";
		try {
			//添加到退货单
			orderid=this.warehouseService.insertReturn(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加用料单错误");
			return data;
		}
		try {
			//添加到物料单
			JSONObject json=JSONObject.fromObject(map.get("materiallist")+"");
			List<Map<String, Object>> materiallist=(List<Map<String, Object>>)json.get("materiallist");
			for(Map<String, Object> material:materiallist){
				material.put("companyid", map.get("companyid"));
				material.put("orderid", orderid);
				this.warehouseService.insertReturnMaterial(material);
			}
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加退货物料错误");
			return data;
		}
		data.put("status", 0);
		data.put("message", "添加成功");
		return data;
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
	@ResponseBody
	public Map<String, Object> getReturnInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			//退货单详情
			Map<String, Object> applyInfo=this.warehouseService.getReturnInfo(map);
			//退货单物料列表
			List<Map<String, Object>> applylist=this.warehouseService.getReturnMaterialList(applyInfo);
			data.put("applyInfo", applyInfo);
			data.put("applylist", applylist);
			data.put("status", 0);
			data.put("message", "添加成功");
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "查询失败");
			return data;
		}
	}

	
	/**
	 * 审核退货单  同意或者拒绝
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineReturnOrder")
	@ResponseBody
	public Map<String, Object> examineReturnOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		String bool=this.warehouseService.updateReturnOrder(map);
		if("1".equals(bool)){
			data.put("status", 0);
			data.put("message", "审核成功");
		}else{
			data.put("status", 1);
			data.put("message", "库存不足");
		}
		return data;
	}
	
	
	
	/*------------------------------------------------报损单------------------------------------------------------*/
	
	/**
	 * 查询报损单 已处理，未处理 的 未读数量
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getReportlossOrderAllNotRead")
	public Map<String,Object> getReportlossOrderAllNotRead(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.warehouseService.getReportlossOrderListNum(map);
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.warehouseService.getReportlossOrderByDateNum(map);
			
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
	 * 报损单列表(未处理)
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReportlossOrder")
	@ResponseBody
	public Map<String, Object> getReportlossOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		map.put("status",0);
		PageScroll page = new PageScroll();
		int ordernum=this.warehouseService.getReportlossOrderListNum(map);		
		page.setTotalRecords(ordernum);
		page.initPage(map);
		List<Map<String, Object>> orderlist=this.warehouseService.getReportlossOrderList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("ordernum", ordernum);
		data.put("orderlist", orderlist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	
	/**
	 * 采购(入库)单查询(已处理)
	 * 传入参数 {"userid":1,"status":1,"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * status 1：已处理 0：未处理    starttime 和 endtime 不传 默认近7天
	 * 传出参数{"status":0,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"ordernum":1,"map":{"userid":"1","status":"1","companyid":"67702cc412264f4ea7d2c5f692070457","starttime":"2016-07-20","endtime":"2016-07-27","startnum":0,"rownum":10},"list":[{"createtime":"2016-07-25","count":1,"orderlist":[{"result":1,"updatetime":"2016-07-25 22:53:59","status":1,"isread":1,"materialprice":600,"examineuserid":"1","updateid":"1","createdate":"2016-07-25","orderno":"CGD20160725225000002","examinename":"李语然","createtime":"22:50:25","companyid":"67702cc412264f4ea7d2c5f692070457","createhour":"22:50","forwarduserid":"34da2a2a4fcc40d8bbb4686495351be4","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":1,"opinion":"阿桑大苏打","orderid":"2fd957b0b6494d6da7388cc38e5c2384","createname":"silence"},{"status":0,"isread":0,"examineuserid":"1","materialprice":100,"createdate":"2016-07-25","orderno":"CGD20160725224800001","examinename":"李语然","createtime":"22:49:06","companyid":"67702cc412264f4ea7d2c5f692070457","forwarduserid":"739aa3439efc4795b47f4308a0051cb9","createhour":"22:49","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":1,"orderid":"14530d80b2f74e30bf55e12bad0aeb1e","createname":"silence"},{"result":1,"updatetime":"2016-07-25 22:53:59","status":1,"isread":0,"materialprice":600,"examineuserid":"1","updateid":"1","createdate":"2016-07-25","orderno":"CGD20160725225000002","examinename":"李语然","createtime":"22:50:25","companyid":"67702cc412264f4ea7d2c5f692070457","createhour":"22:50","forwarduserid":"7dcf6ef75f084bd59e9030f537e638e9","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":1,"opinion":"阿桑大苏打","orderid":"2fd957b0b6494d6da7388cc38e5c2384","createname":"silence"},{"status":0,"isread":1,"examineuserid":"1","materialprice":100,"createdate":"2016-07-25","orderno":"CGD20160725224800001","examinename":"李语然","createtime":"22:49:06","companyid":"67702cc412264f4ea7d2c5f692070457","forwarduserid":"cc976ceae34a4f81adc2dbbdd3422d94","createhour":"22:49","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":1,"orderid":"14530d80b2f74e30bf55e12bad0aeb1e","createname":"silence"}]}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReportlossOrderByDate")
	@ResponseBody
	public Map<String, Object> getReportlossOrderByDate(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
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
			cal.add(Calendar.DAY_OF_MONTH, -6);
			cal1.add(Calendar.DAY_OF_MONTH, 1);
			map.put("starttime",sdf.format(cal.getTime()));
			map.put("endtime",sdf.format(cal1.getTime()));
		}
		
		PageScroll page = new PageScroll();
		int ordernum=this.warehouseService.getReportlossOrderByDateNum(map);		
		page.setTotalRecords(ordernum);
		page.initPage(map);
		List<Map<String, Object>> list=this.warehouseService.getReportlossOrderByDate(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("ordernum", ordernum);
		data.put("list", list);
		data.put("page",page);
		data.put("map", map);
		data.put("status",0);
		return data;
	}
	
	

	/**
	 * 初始化添加报损单（获取报损单编号）
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initInsertReportloss")
	@ResponseBody
	public Map<String, Object> initInsertReportloss(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("companyid") && !"".equals(map.get("companyid"))){
			int ordernum=this.warehouseService.getReportlossNumByCompany(map);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmm");
			data.put("status", 0);
			data.put("orderno","BSD"+sdf.format(new Date())+String.format("%05d",(ordernum+1)));
			SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			data.put("createtime",sdf1.format(new Date()));
			//查询 采购(入库)单的物料明细的 所有 物料类型
			Map<String, Object> materialmap=new HashMap<String, Object>();
			materialmap.put("companyid", map.get("companyid"));
			materialmap.put("organizeid", map.get("organizeid"));
			materialmap.put("grouptype", 1);
			materialmap.put("result", 1);
			List<Map<String, Object>> materialTypeList=this.purchaseService.getPurchaseMaterialList(materialmap);
			//查询 采购(入库)单的物料明细的 所有 物料名称
			materialmap=new HashMap<String, Object>();
			materialmap.put("companyid", map.get("companyid"));
			materialmap.put("organizeid", map.get("organizeid"));
			materialmap.put("groupname", 1);
			materialmap.put("result", 1);
			List<Map<String, Object>> materialNameList=this.purchaseService.getPurchaseMaterialList(materialmap);

			data.put("materialTypeList",materialTypeList);
			data.put("materialNameList",materialNameList);
			data.put("message", "成功");
		}else{
			data.put("status", 1);
			data.put("message", "传入参数错误");
		}
		return data;
	}
	
	/**
	 * 添加报损单
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertReportloss")
	@ResponseBody
	public Map<String, Object> insertReportloss(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>(); 
		System.out.println(map.get("materiallist"));
		String orderid="";
		try {
			//添加到报损单
			orderid=this.warehouseService.insertReportloss(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加用料单错误");
			return data;
		}
		try {
			//添加到物料单
			JSONObject json=JSONObject.fromObject(map.get("materiallist")+"");
			List<Map<String, Object>> materiallist=(List<Map<String, Object>>)json.get("materiallist");
			for(Map<String, Object> material:materiallist){
				material.put("companyid", map.get("companyid"));
				material.put("orderid", orderid);
				this.warehouseService.insertReportlossMaterial(material);
			}
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加报损物料错误");
			return data;
		}
		data.put("status", 0);
		data.put("message", "添加成功");
		return data;
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
	@ResponseBody
	public Map<String, Object> getReportlossInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			//报损单详情
			Map<String, Object> applyInfo=this.warehouseService.getReportlossInfo(map);
			//报损单物料列表
			List<Map<String, Object>> applylist=this.warehouseService.getReportlossMaterialList(applyInfo);
			data.put("applyInfo", applyInfo);
			data.put("applylist", applylist);
			data.put("status", 0);
			data.put("message", "添加成功");
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "查询失败");
			return data;
		}
	}

	
	/**
	 * 审核报损单  同意或者拒绝
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineReportlossOrder")
	@ResponseBody
	public Map<String, Object> examineReportlossOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		String bool=this.warehouseService.updateReportlossOrder(map);
		if("1".equals(bool)){
			data.put("status", 0);
			data.put("message", "审核成功");
		}else{
			data.put("status", 1);
			data.put("message", "库存不足");
		}
		return data;
	}
	
	/**
	 * 仓库管理库存统计  今日统计
	 * 传入参数{"organizeid":5} 不传starttime和endtime 默认7天
	 * 传出参数{"message":"查询成功","status":0,"materiallist":[{"num":1,"sumprice":10,"name":"shit","orderid":"9d54541939f6455c8253a4df4f77328b","type":"chicken","specifications":"500ml","materielid":"35468be776b043f586c180073dbf5e2f"}],"ordernum":1}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getStockInfo")
	@ResponseBody
	public Map<String, Object> getStockInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> data=new HashMap<String, Object>();
		
		Map<String,Object> purchaseMap = this.purchaseService.getStockPayAmountInfo(map);
		List<Map<String, Object>> typelist=this.purchaseService.getStockInfo(map);
		
		double sumprice=0;
		for(Map<String, Object> type:typelist){
			sumprice+=Double.parseDouble(type.get("value")+"");
		}
		//计算每个的占比/100
		int num=0;
		double fiveprice=0;
		for(Map<String, Object> type:typelist){
			double special = Double.parseDouble(type.get("value")+"")/sumprice;
			if(special < 0.05){
				num++;
				fiveprice+=special;
			}
			type.put("special", special);
		}
		
		for(Map<String, Object> type:typelist){
			double special = Double.parseDouble(type.get("special")+"");
			if(special < 0.05){
				type.put("value", sumprice*0.05);
			}else{
				type.put("value", sumprice*(special/(1-fiveprice))*(100-num*5)/100);
			}
		}
		
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
	 * 库存明细
	 * 传入参数{"type":"chicken","organizeid":5,"starttime":"2016-07-12 00:00:00","endtime":"2016-07-15 20:00:00"} 不传starttime和endtime 默认7天
	 * 传出参数{"message":"查询成功","status":0,"materiallist":[{"num":1,"sumprice":10,"name":"shit","orderid":"9d54541939f6455c8253a4df4f77328b","type":"chicken","specifications":"500ml","materielid":"35468be776b043f586c180073dbf5e2f"}],"ordernum":1}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getStockMaterialDetail")
	@ResponseBody
	public Map<String, Object> getStockMaterialDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		
		Map<String, Object> data=new HashMap<String, Object>();
		map.put("maxstock",1);
		List<Map<String, Object>> materiallist=this.purchaseService.getMaterialDetail(map);
		if(materiallist!=null && materiallist.size()>0){
			data.put("materiallist",materiallist);
		}else{
			data.put("materiallist", "");
		}
		data.put("status",0);
		data.put("message", "查询成功");
		return data;
	}
	
	/**
	 * 库存分析统计（统计的   默认七天前到今天）
	 * 传入参数{"organizeid":5,"starttime":"2016-07-12 00:00:00","endtime":"2016-07-15 20:00:00"} 不传starttime和endtime 默认7天
	 * 传出参数{"message":"查询成功","status":0,"materiallist":[{"num":1,"sumprice":10,"name":"shit","orderid":"9d54541939f6455c8253a4df4f77328b","type":"chicken","specifications":"500ml","materielid":"35468be776b043f586c180073dbf5e2f"}],"ordernum":1}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getMaterialGroupList")
	@ResponseBody
	public Map<String, Object> getMaterialGroupList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
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
		
		Map<String,Object> purchaseMap = this.purchaseService.getStockPayAmountInfo(map);
		List<Map<String, Object>> typelist=this.warehouseService.getMaterialGroupList(map);
		
		double sumprice=0;
		for(Map<String, Object> type:typelist){
			sumprice+=Double.parseDouble(type.get("value")+"");
		}
		//计算每个的占比/100
		int num=0;
		double fiveprice=0;
		for(Map<String, Object> type:typelist){
			double special = Double.parseDouble(type.get("value")+"")/sumprice;
			if(special < 0.05&&special>0){
				num++;
				fiveprice+=special;
			}
			type.put("special", special);
		}
		
		for(Map<String, Object> type:typelist){
			double special = Double.parseDouble(type.get("special")+"");
			
			if(special==0){
				type.put("value", special);
			}else if(special < 0.05){
				type.put("value", sumprice*0.05);
			}else{
				type.put("value", sumprice*(special/(1-fiveprice))*(100-num*5)/100);
			}
		}
		
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
	 * 库存明细（统计的   默认七天前到今天）
	 * 传入参数{"type":"1","organizeid":5,"starttime":"2016-07-12 00:00:00","endtime":"2016-07-15 20:00:00"} 不传starttime和endtime 默认7天
	 * 传出参数{"message":"查询成功","status":0,"materiallist":[{"num":1,"sumprice":10,"name":"shit","orderid":"9d54541939f6455c8253a4df4f77328b","type":"chicken","specifications":"500ml","materielid":"35468be776b043f586c180073dbf5e2f"}],"ordernum":1}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getStockMaterialDetailByStatistics")
	@ResponseBody
	public Map<String, Object> getStockMaterialDetailByStatistics(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
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
		
		/*int ordernum=this.purchaseService.getStockMaterialDetailNum(map);	
		data.put("ordernum", ordernum);*/
		Map<String, Object> data=new HashMap<String, Object>();
		
		List<Map<String, Object>> materiallist=this.warehouseService.getStockMaterialDetail(map);
		if(materiallist!=null && materiallist.size()>0){
			data.put("materiallist",materiallist);
		}else{
			data.put("materiallist", "");
		}
		data.put("status",0);
		data.put("message", "查询成功");
		return data;
	}
	
	
	/**
	 * 各种单（统计的   默认七天前到今天）
	 * 传入参数{"type":"1","organizeid":5,"starttime":"2016-07-12 00:00:00","endtime":"2016-07-15 20:00:00"}
	 * 传出参数{"message":"查询成功","status":0,"ordernum":1,"purchaselist":[{"createtime":1468571619000,"companyid":"67702cc412264f4ea7d2c5f692070457","result":1,"updatetime":1468572302000,"createid":"690fb669ed4d40219964baad7783abd4","status":1,"examineuserid":"1","updateid":"1","organizeid":"5","orderno":"CGD20160715101700001","opinion":"ohmygod","orderid":"9d54541939f6455c8253a4df4f77328b"}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getStockOrderByStatistics")
	@ResponseBody
	public Map<String, Object> getStockOrderByStatistics(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
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
		List<Map<String, Object>> orderlist=this.warehouseService.getStockOrderByStatistics(map);
		if(orderlist!=null && orderlist.size()>0){
			data.put("orderlist",orderlist);
		}else{
			data.put("orderlist", "");
		}
		data.put("status",0);
		data.put("message", "查询成功");
		return data;
	}
	
}
