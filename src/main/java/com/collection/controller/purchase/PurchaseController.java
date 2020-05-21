package com.collection.controller.purchase;

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
import com.collection.service.purchase.SupplierService;
import com.collection.util.PageScroll;


/**
 * 采购(入库)单
 * @author silence
 *
 */
@Controller
@RequestMapping("/app")
public class PurchaseController {
	private transient static Log log = LogFactory.getLog(PurchaseController.class);
	
	@Resource private PurchaseService purchaseService;
	
	@Resource private IndexService indexService;
	
	@Resource private SupplierService supplierService;
	/**
	 * 申购单/采购(入库)单 未读数量 查询
	 * 传入参数{"receiveid":1,"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数{"purchasenum":1,"applynum":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPurchaseNotreadNum")
	@ResponseBody
	public Map<String, Object> getPurchaseNotreadNum(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data =new HashMap<String, Object>();
		map.put("isread", 0);
		//申购
		List<Integer> resourcetypes=new ArrayList<Integer>();
		resourcetypes.add(2);
		map.put("resourcetypes", resourcetypes);
		int applynum=this.indexService.getForwordNotreadNum(map);
		data.put("applynum", applynum);
		//采购
		resourcetypes=new ArrayList<Integer>();
		resourcetypes.add(1);
		map.put("resourcetypes", resourcetypes);
		int purchasenum=this.indexService.getForwordNotreadNum(map);
		data.put("purchasenum", purchasenum);
		
		return data;
	}
	
	
	/**
	 * 查询采购入库 已处理未处理的 未读数量
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getPurchaseAllNotRead")
	public Map<String,Object> getPurchaseAllNotRead(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.purchaseService.getPurchaseListNum(map);	
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.purchaseService.getPurchaseListByDateNum(map);
			
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
	 * 采购(入库)单查询
	 * 传入参数{"userid":1,"status":0,"companyid":"67702cc412264f4ea7d2c5f692070457"}  status：0 未处理 1：已处理
	 * 传出参数{"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0,"orderlist":[{"isread":1,"createtime":1468571620000,"companyid":"67702cc412264f4ea7d2c5f692070457","createhour":"16:33","createid":"690fb669ed4d40219964baad7783abd4","status":0,"examineuserid":"1","materialprice":10,"organizeid":"5","createdate":"2016-07-15","orderno":"CGD20160715101700001","orderid":"9d54541939f6455c8253a4df4f77328b"}],"ordernum":1}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPurchaseOrder222")
	@ResponseBody
	public Map<String, Object> getPurchaseOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		PageScroll page = new PageScroll();
		int ordernum=this.purchaseService.getPurchaseListNum(map);		
		page.setTotalRecords(ordernum);
		page.initPage(map);
		List<Map<String, Object>> orderlist=this.purchaseService.getPurchaseList(map);
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
	@RequestMapping("/getPurchaseOrderByDate")
	@ResponseBody
	public Map<String, Object> getPurchaseOrderByDate(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
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
		int ordernum=this.purchaseService.getPurchaseListByDateNum(map);		
		page.setTotalRecords(ordernum);
		page.initPage(map);
		List<Map<String, Object>> list=this.purchaseService.getPurchaseListByDate(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("ordernum", ordernum);
		data.put("list", list);
		data.put("page",page);
		data.put("map", map);
		data.put("status",0);
		return data;
	}
	
	/**
	 * 采购(入库)单时间列表查询 根据 物料名字/类型查询
	 * 传入参数{"name":12,"unit":12,"companyid":"67702cc412264f4ea7d2c5f692070457","organizeid":5}
	 * 传出参数{"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"ordernum":1,"list":[{"createtime":"2016-07-25","count":1,"orderlist":[{"createtime":1469458146000,"companyid":"67702cc412264f4ea7d2c5f692070457","datacode":"001001001001","createid":"690fb669ed4d40219964baad7783abd4","status":0,"examineuserid":"1","materialprice":100,"organizeid":"5","maternum":1,"orderno":"CGD20160725224800001","orderid":"14530d80b2f74e30bf55e12bad0aeb1e","delflag":0},{"result":1,"updatetime":1469458439000,"status":1,"datacode":"001001001001","materialprice":600,"updateid":"1","examineuserid":"1","orderno":"CGD20160725225000002","createtime":1469458225000,"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":1,"opinion":"阿桑大苏打","orderid":"2fd957b0b6494d6da7388cc38e5c2384","delflag":0}]}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPurchaseDateByname")
	@ResponseBody
	public Map<String, Object> getPurchaseDateByname(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		PageScroll page = new PageScroll();
		int ordernum=this.purchaseService.getPurchaseDateBynameNum(map);		
		page.setTotalRecords(ordernum);
		page.initPage(map);
		List<Map<String, Object>> list=this.purchaseService.getPurchaseDateByname(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("ordernum", ordernum);
		data.put("list",list);
		data.put("page",page);
		return data;
	}
	
	
	/**
	 * 初始化添加申购单（获取申购单编号）
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数{"message":"获取成功","createtime":"2016-07-26 20:20:17","status":0,"dictlisttwo":[{"createtime":1469443787000,"datacode":"","remark":"","createid":"1","typeid":"4","ename":"","dataid":"7","cname":"每袋"},{"createtime":1469443804000,"datacode":"","remark":"","createid":"1","typeid":"4","ename":"","dataid":"8","cname":"每瓶"},{"createtime":1469443820000,"datacode":"","remark":"","createid":"1","typeid":"4","ename":"","dataid":"9","cname":"每罐"}],"dictlistone":[{"createtime":1469443686000,"datacode":"","remark":"","createid":"1","typeid":"3","ename":"","dataid":"3","cname":"千克"},{"createtime":1469443724000,"datacode":"","remark":"","createid":"1","typeid":"3","ename":"","dataid":"4","cname":"克"},{"createtime":1469443751000,"datacode":"","remark":"","createid":"1","typeid":"3","ename":"","dataid":"5","cname":"升"},{"createtime":1469443768000,"datacode":"","remark":"","createid":"1","typeid":"3","ename":"","dataid":"6","cname":"毫升"}],"orderno":"CGD20160726202000001"}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initInsertPurchase")
	@ResponseBody
	public Map<String, Object> initInsertPurchase(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("companyid") && !"".equals(map.get("companyid"))){
			int ordernum=this.purchaseService.getPurchaseNumByCompany(map);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmm");
			data.put("status", 0);
			data.put("orderno","CGD"+sdf.format(new Date())+String.format("%05d",(ordernum+1)));
			SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			data.put("createtime",sdf1.format(new Date()));
			//查询规格计量单位
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("typeid", 3);
			List<Map<String, Object>> datalist=this.indexService.getDictData(paramMap);
			data.put("dictlistone",datalist);
			//查询规格计数单位
			paramMap=new HashMap<String, Object>();
			paramMap.put("typeid", 4);
			List<Map<String, Object>> datalist1=this.indexService.getDictData(paramMap);
			data.put("dictlisttwo",datalist1);
			List<Map<String, Object>> supplierlist=this.supplierService.getSupplierList(map);
			data.put("supplierlist", supplierlist);
			data.put("message", "获取成功");
		}else{
			data.put("status", 1);
			data.put("message", "传入参数错误");
		}
		return data;
	}
	/**
	 * 添加采购(入库)单
	 * 传入参数{"orderno":"CGD20160715165700002","companyid":"67702cc412264f4ea7d2c5f692070457","datacode":001001001001,"organizeid":5,"examineuserid":1,"createid":"690fb669ed4d40219964baad7783abd4","materialprice":10,"materiallist":"{"materiallist":[{"companyid":"67702cc412264f4ea7d2c5f692070457","supplierid":"1","type":"chicken","name":"shit","specifications":"500ml","price":10,"num":1,"sumprice":10}]}"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertPurchase")
	@ResponseBody
	public Map<String, Object> insertPurchase(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>(); 
		String orderid="";
		try {
			//添加到采购(入库)单
			orderid=this.purchaseService.insertPurchase(map);
		} catch (Exception e) {
			// TODO: handle exception
			
			data.put("status", 1);
			data.put("message", "添加采购(入库)单错误");
			return data;
		}
		try {
			//添加到物料单
			JSONObject json=JSONObject.fromObject(map.get("materiallist")+"");
			List<Map<String, Object>> materiallist=(List<Map<String, Object>>)json.get("materiallist");
			for(Map<String, Object> material:materiallist){
 				material.put("companyid", map.get("companyid"));
				material.put("orderid", orderid);
				this.purchaseService.insertPurchaseMaterial(material);
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
	 * 采购(入库)单详情页面
	 * 传入参数{"userid":1,"orderid":"9d54541939f6455c8253a4df4f77328b"}
	 * 传出参数{"message":"查询成功","status":0,"purchaseInfo":{"createtime":1468571619000,"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","status":0,"examineuserid":"1","materialprice":10,"organizeid":"5","orderno":"CGD20160715101700001","orderid":"9d54541939f6455c8253a4df4f77328b","createname":"silence","examinename":"李语然"},"purchaselist":[{"createtime":1468571620000,"companyid":"67702cc412264f4ea7d2c5f692070457","num":1,"price":10,"sumprice":10,"name":"shit","orderid":"9d54541939f6455c8253a4df4f77328b","type":"chicken","supplierid":"1","specifications":"500ml","materielid":"35468be776b043f586c180073dbf5e2f"}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPurchaseInfo")
	@ResponseBody 
	public Map<String, Object> getPurchaseInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>(); 
		
		try {
			//采购(入库)单详情
			Map<String, Object> purchaseInfo=this.purchaseService.getPurchaseInfo(map);
			//采购物料列表
			List<Map<String, Object>> materiallist=this.purchaseService.getPurchaseMaterialListnew(purchaseInfo);
			purchaseInfo.put("materiallist", materiallist);
			data.put("purchaseInfo", purchaseInfo);
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
	 * 审核采购(入库)单  同意或者拒绝
	 * 传入参数{"userid":1,"orderid":"9d54541939f6455c8253a4df4f77328b","opinion":"ohmygod","result":1}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examinePurchaseOrder")
	@ResponseBody
	public Map<String, Object> examinePurchaseOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.purchaseService.updatePurchaseOrder(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	
	
	
	/* ------------------------------申购单 ---------------------------------------   */
	
	/**
	 * 查询申购单 未处理，已处理的未读数量
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getApplyOrderAllNotRead")
	public Map<String,Object> getApplyOrderAllNotRead(@RequestParam Map<String,Object> map , HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.purchaseService.getApplyOrderListNum(map);
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.purchaseService.getApplyOrderByDateNum(map);
			
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
	 * 申购单查询(未处理)
	 * 传入参数 {"userid":1,"status":0,"companyid":"67702cc412264f4ea7d2c5f692070457"} status 1：已处理 0：未处理
	 * 传出参数{"status":0,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"orderlist":[{"createtime":1468573752000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468575397000,"createhour":"17:09","createid":"690fb669ed4d40219964baad7783abd4","status":0,"examineuserid":"1","materialprice":10,"organizeid":"5","createdate":"2016-07-15","orderno":"SGD20160715165800001","orderid":"d460f29996694dd383264395bf408cb2"}],"ordernum":1}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getApplyOrder")
	@ResponseBody
	public Map<String, Object> getApplyOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		PageScroll page = new PageScroll();
		int ordernum=this.purchaseService.getApplyOrderListNum(map);		
		page.setTotalRecords(ordernum);
		page.initPage(map);
		List<Map<String, Object>> orderlist=this.purchaseService.getApplyOrderList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("ordernum", ordernum);
		data.put("orderlist", orderlist);
		data.put("page",page);
		data.put("status",0);
		return data;
	}
	
	/**
	 * 申购单查询(已处理)
	 * 传入参数 {"userid":1,"status":1,"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * status 1：已处理 0：未处理    starttime 和 endtime 不传 默认近7天
	 * 传出参数{"status":0,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"ordernum":1,"map":{"userid":"1","status":"1","companyid":"67702cc412264f4ea7d2c5f692070457","starttime":"2016-07-20","endtime":"2016-07-27","startnum":0,"rownum":10},"list":[{"createtime":"2016-07-25","count":1,"orderlist":[{"result":1,"updatetime":"2016-07-25 22:12:13","status":1,"materialprice":1200,"examineuserid":"1","updateid":"1","orderno":"SGD20160725195900001","examinename":"李语然","createtime":"20:00:50","companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":2,"opinion":"阿斯达斯","organizename":"浦东金桥分店","orderid":"4f9f463c50ea4267aef0efb619d4403c","createname":"silence"},{"result":1,"updatetime":"2016-07-25 22:12:13","status":1,"materialprice":1200,"examineuserid":"1","updateid":"1","orderno":"SGD20160725195900001","examinename":"李语然","createtime":"20:00:50","companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":2,"opinion":"阿斯达斯","organizename":"浦东金桥分店","orderid":"4f9f463c50ea4267aef0efb619d4403c","createname":"silence"}]}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getApplyOrderByDate")
	@ResponseBody
	public Map<String, Object> getApplyOrderByDate(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
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
		int ordernum=this.purchaseService.getApplyOrderByDateNum(map);		
		page.setTotalRecords(ordernum);
		page.initPage(map);
		List<Map<String, Object>> list=this.purchaseService.getApplyOrderByDate(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("ordernum", ordernum);
		data.put("list", list);
		data.put("page",page);
		data.put("map", map);
		data.put("status",0);
		return data;
	}
	
	/**
	 * 初始化添加申购单（获取申购单编号）
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数{"message":"操作成功","createtime":"2016-07-26 20:19:51","status":0,"dictlisttwo":[{"createtime":1469443787000,"datacode":"","remark":"","createid":"1","typeid":"4","ename":"","dataid":"7","cname":"每袋"},{"createtime":1469443804000,"datacode":"","remark":"","createid":"1","typeid":"4","ename":"","dataid":"8","cname":"每瓶"},{"createtime":1469443820000,"datacode":"","remark":"","createid":"1","typeid":"4","ename":"","dataid":"9","cname":"每罐"}],"dictlistone":[{"createtime":1469443686000,"datacode":"","remark":"","createid":"1","typeid":"3","ename":"","dataid":"3","cname":"千克"},{"createtime":1469443724000,"datacode":"","remark":"","createid":"1","typeid":"3","ename":"","dataid":"4","cname":"克"},{"createtime":1469443751000,"datacode":"","remark":"","createid":"1","typeid":"3","ename":"","dataid":"5","cname":"升"},{"createtime":1469443768000,"datacode":"","remark":"","createid":"1","typeid":"3","ename":"","dataid":"6","cname":"毫升"}],"orderno":"SGD20160726201900003"}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initInsertApply")
	@ResponseBody
	public Map<String, Object> initInsertApply(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("companyid") && !"".equals(map.get("companyid"))){
			int ordernum=this.purchaseService.getApplyNumByCompany(map);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmm");
			data.put("status", 0);
			data.put("orderno","SGD"+sdf.format(new Date())+String.format("%05d",(ordernum+1)));
			SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			data.put("createtime",sdf1.format(new Date()));
			//查询规格计量单位
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("typeid", 3);
			List<Map<String, Object>> datalist=this.indexService.getDictData(paramMap);
			data.put("dictlistone",datalist);
			//查询规格计数单位
			paramMap=new HashMap<String, Object>();
			paramMap.put("typeid", 4);
			List<Map<String, Object>> datalist1=this.indexService.getDictData(paramMap);
			data.put("dictlisttwo",datalist1);
			data.put("message", "操作成功");
		}else{
			data.put("status", 1);
			data.put("message", "传入参数错误");
		}
		return data;
	}
	/**
	 * 添加申购单
	 * 传入参数{"orderno":"SGD20160715165800001","companyid":"67702cc412264f4ea7d2c5f692070457","datacode":001001001001,"organizeid":5,"examineuserid":1,"createid":"690fb669ed4d40219964baad7783abd4","materialprice":10,"materiallist":"{"materiallist":[{"companyid":"67702cc412264f4ea7d2c5f692070457","type":"chicken","name":"shit","specifications":"500ml","price":10,"num":1,"sumprice":10}]}"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertApply")
	@ResponseBody
	public Map<String, Object> insertApply(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>(); 
		String orderid="";
		try {
			//添加到申购单
			orderid=this.purchaseService.insertApply(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加申购单错误");
			return data;
		}
		try {
			//添加到物料单
			JSONObject json=JSONObject.fromObject(map.get("materiallist")+"");
			List<Map<String, Object>> materiallist=(List<Map<String, Object>>)json.get("materiallist");
			for(Map<String, Object> material:materiallist){
				material.put("companyid", map.get("companyid"));
				material.put("orderid", orderid);
				this.purchaseService.insertApplyMaterial(material);
			}
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加申购物料错误");
			return data;
		}
		data.put("status", 0);
		data.put("message", "添加成功");
		return data;
	}
	
	
	/**
	 * 申购单详情页面
	 * 传入参数{"orderid":"d460f29996694dd383264395bf408cb2"}
	 * 传出参数{"message":"查询成功","applyInfo":{"createtime":1468574964000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468574965000,"createid":"690fb669ed4d40219964baad7783abd4","examineuserid":"1","materialprice":10,"organizeid":"5","orderno":"SGD20160715165800001","orderid":"d460f29996694dd383264395bf408cb2","createname":"silence","examinename":"李语然"},"applylist":[],"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getApplyInfo")
	@ResponseBody
	public Map<String, Object> getApplyInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			//申购单详情
			Map<String, Object> applyInfo=this.purchaseService.getApplyInfo(map);
			//申购物料列表
			List<Map<String, Object>> materiallist=this.purchaseService.getApplyMaterialList(applyInfo);
			
			applyInfo.put("materiallist", materiallist);
			data.put("applyInfo", applyInfo);
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
	 * 审核申购单  同意或者拒绝
	 * 传入参数{"orderid":"d460f29996694dd383264395bf408cb2","userid":1,"opinion":"yes","result":1}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineApplyOrder")
	@ResponseBody
	public Map<String, Object> examineApplyOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.purchaseService.updateApplyOrder(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	

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
		data.put("purchaseMap", purchaseMap);
		data.put("status",0);
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
	
	
	@RequestMapping("/getPurchaseTypeList")
	@ResponseBody
	public Map<String, Object> getPurchaseTypeList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String,Object>> purchasetype = this.purchaseService.getPurchaseTypeList(map);
			if(purchasetype!=null && purchasetype.size()>0){
				resultMap.put("data", purchasetype);
			}else{
				resultMap.put("data", "");
			}			
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	@RequestMapping("/getApplyTypeList")
	@ResponseBody
	public Map<String, Object> getApplyTypeList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String,Object>> applytype = this.purchaseService.getApplyTypeList(map);
			if(applytype!=null && applytype.size()>0){
				resultMap.put("data", applytype);
			}else{
				resultMap.put("data", "");
			}			
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	
	
	
	
}
