package com.collection.controller.purchase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collection.service.IndexService;
import com.collection.service.purchase.SupplierService;
import com.collection.util.PageScroll;


/**
 * 供应商
 * @author silence
 *
 */
@Controller
@RequestMapping("/app")
public class SupplierController {
	private transient static Log log = LogFactory.getLog(SupplierController.class);
	
	@Resource private SupplierService supplierService;
	
	@Resource private IndexService indexService;
	
	/**
	 * 供应商列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","suppliername":"silence"} 可不传模糊条件供应商名称
	 * 传出参数{"suppliernum":1,"status":0,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"supplierlist":[{"position":"店长","suppliertype":"abc","phone":"15000042335","updatetime":1468581448000,"contactsname":"李语然","suppliername":"silencesupplier","updateid":"690fb669ed4d40219964baad7783abd4","provinceid":"31","supplierno":"GYS20160715190400001","createtime":1468581238000,"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","address":"浦东新区","supplierid":"1"}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getSupplierList")
	@ResponseBody
	public Map<String, Object> getSupplierList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		map.put("status",0);
		PageScroll page = new PageScroll();
		int suppliernum=this.supplierService.getSupplierListNum(map);		
		page.setTotalRecords(suppliernum);
		page.initPage(map);
		List<Map<String, Object>> supplierlist=this.supplierService.getSupplierList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("suppliernum", suppliernum);
		data.put("supplierlist", supplierlist);
		data.put("page",page);
		data.put("status",0);
		return data;
	}
	
	/**
	 * 初始化添加供应商（获取供应商编号）
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数{"message":"成功","status":0,"provincelist":[{"createtime":1454316270000,"ifactive":1,"priority":11,"areaid":11,"parentid":4,"cname":"北京"},{"createtime":1454316270000,"ifactive":1,"priority":12,"areaid":12,"parentid":4,"cname":"天津"},{"createtime":1454316270000,"ifactive":1,"priority":91,"areaid":91,"parentid":4,"cname":"澳门"}],"supplierno":"GYS20160715190400001"}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initInsertSupplier")
	@ResponseBody
	public Map<String, Object> initInsertSupplier(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("companyid") && !"".equals(map.get("companyid"))){
			int suppliernum=this.supplierService.getSupplierNumByCompany(map);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmm");
			data.put("status", 0);
			data.put("supplierno","GYS"+sdf.format(new Date())+String.format("%05d",(suppliernum+1)));
			Map<String, Object> city= new HashMap<String, Object>();
			city.put("parentid", 4);
			List<Map<String, Object>> provincelist=this.indexService.getCityList(city);
			data.put("provincelist",provincelist);
			data.put("message", "成功");
		}else{
			data.put("status", 1);
			data.put("message", "传入参数错误");
		}
		return data;
	}
	
	
	/**
	 * 添加供应商/修改供应商 传入 supplierid时是修改
	 * 传入参数{"supplierid":1,"userid":"690fb669ed4d40219964baad7783abd4","companyid":"67702cc412264f4ea7d2c5f692070457","supplierno":"GYS20160715190400001","suppliername":"silencesupplier","suppliertype":"abc","contactsname":"李语然","phone":15000042335,"position":"店员","provinceid":31,"address":"浦东新区"}
	 * 传出参数{"message":"修改成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/editSupplier")
	@ResponseBody
	public Map<String, Object> editSupplier(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("supplierid") && !"".equals(map.get("supplierid"))){
			map.put("updateid", map.get("userid"));
			map.put("updatetime", new Date());
			this.supplierService.updateSupplier(map);
			data.put("status", 0);
			data.put("message", "修改成功");
		}else{
			map.put("createid", map.get("userid"));
			this.supplierService.insertSupplier(map);
			data.put("status", 0);
			data.put("message", "添加成功");
		}
		return data;
	}
	
	
	/**
	 * 供应商详情
	 * 传入参数{"supplierid":1}
	 * 传出参数{"message":"成功","status":0,"supplierInfo":{"position":"店长","suppliertype":"abc","phone":"15000042335","updatetime":1468581448000,"contactsname":"李语然","updatename":"silence","suppliername":"silencesupplier","updateid":"690fb669ed4d40219964baad7783abd4","provinceid":"31","supplierno":"GYS20160715190400001","createtime":1468581238000,"companyid":"67702cc412264f4ea7d2c5f692070457","provincename":"上海","address":"浦东新区","createid":"690fb669ed4d40219964baad7783abd4","createname":"silence","supplierid":"1"}}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getSupplierInfo")
	@ResponseBody
	public Map<String, Object> getSupplierInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("supplierid") && !"".equals(map.get("supplierid"))){
			Map<String, Object> supplierInfo=this.supplierService.getSupplierInfo(map);
			data.put("supplierInfo",supplierInfo);
			data.put("status", 0);
			data.put("message", "成功");
		}else{
			data.put("status", 1);
			data.put("message", "传入参数错误");
		}
		return data;
	}
}
