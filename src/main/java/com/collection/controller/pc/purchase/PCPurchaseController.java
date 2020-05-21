package com.collection.controller.pc.purchase;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collection.controller.userbackstage.UserIndexController.ExportExcel;
import com.collection.redis.RedisUtil;
import com.collection.service.IndexService;
import com.collection.service.purchase.PurchaseService;
import com.collection.service.purchase.SupplierService;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;
import com.collection.util.UserUtil;


/**
 * 采购管理
 * @author silence
 *
 */
@Controller
@RequestMapping("/pc")
public class PCPurchaseController {
	private transient static Log log = LogFactory.getLog(PCPurchaseController.class);
	
	@Resource private PurchaseService purchaseService;
	
	@Resource private IndexService indexService;
	
	@Resource private SupplierService supplierService;
	
	/**
	 * 采购(入库)单查询
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPurchaseOrder")
	public String getPurchaseOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		if(map.containsKey("status") && map.get("status").equals("")){
			map.remove("status");
		}
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		
		PageHelper page = new PageHelper(request);
		int ordernum=this.purchaseService.getPurchaseListNum(map);
		page.setTotalCount(ordernum);
		page.initPage(map);
		List<Map<String, Object>> orderlist=this.purchaseService.getPurchaseList(map);
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("pager",page.cateringPage().toString());
		
		model.addAttribute("map", map);
		
		
		Map<String, Object> powerMap=RedisUtil.getMap(map.get("userid")+"powerMap");
		model.addAttribute("powerMap",powerMap);
		
		return "/pc/purchase/purchase_list";
	}

	/**
	 * 
	 * 导出采购(入库)单
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportPurchaseList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportPurchaseList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/采购(入库)单列表.xls");
		if(map.containsKey("status") && map.get("status").equals("")){
			map.remove("status");
		}
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		List<Map<String, Object>> orderlist=this.purchaseService.getPurchaseList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "采购(入库)单编号", "申请人", "申请时间", "项数","金额","状态"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> order:orderlist){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a", order.get("orderno")==null?"":order.get("orderno"));
	        datamap.put("b", order.get("createname")==null?"":order.get("createname"));
	        datamap.put("c", order.get("createtime")==null?"":order.get("createtime"));
	        datamap.put("d", order.get("maternum")==null?"":order.get("maternum"));
	        datamap.put("e", order.get("materialprice")==null?"":order.get("materialprice"));
	        if(String.valueOf(order.get("status")).equals("1")){
	        	datamap.put("f","已处理");
	        }else{
	        	datamap.put("f","待处理");
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
		return "采购(入库)单列表.xls";
	}
	
	/**
	 * 初始化添加申购单（获取申购单编号）
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initInsertPurchase")
	public String initInsertPurchase(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {

		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("companyid", userInfo.get("companyid"));
		
		int ordernum=this.purchaseService.getPurchaseNumByCompany(map);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmm");
		model.addAttribute("orderno","CGD"+sdf.format(new Date())+String.format("%05d",(ordernum+1)));
		SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		model.addAttribute("createtime",sdf1.format(new Date()));
		//查询规格计量单位
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("typeid", 3);
		List<Map<String, Object>> datalist=this.indexService.getDictData(paramMap);
		model.addAttribute("dictlistone",datalist);
		//查询规格计数单位
		paramMap=new HashMap<String, Object>();
		paramMap.put("typeid", 4);
		List<Map<String, Object>> datalist1=this.indexService.getDictData(paramMap);
		model.addAttribute("dictlisttwo",datalist1);
		List<Map<String, Object>> supplierlist=this.supplierService.getSupplierList(map);
		model.addAttribute("supplierlist", supplierlist);
		
		model.addAttribute("userInfo", userInfo);
		return "/pc/purchase/purchase_add";
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
		return "/pc/purchase/purchase_detail";
	}
	

	/**
	 * 
	 * 导出采购(入库)单详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportPurchaseInfo",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportPurchaseInfo(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/采购(入库)单详情.xls");
		
		//采购(入库)单详情
		Map<String, Object> purchaseInfo=this.purchaseService.getPurchaseInfo(map);
		//采购物料列表
		List<Map<String, Object>> materiallist=this.purchaseService.getPurchaseMaterialList(purchaseInfo);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>(); 
        String[] headers = { "名称", "内容", "", "","","","",""};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
        datamap.put("a", "采购(入库)单编号");
        datamap.put("b", purchaseInfo.get("orderno")==null?"":purchaseInfo.get("orderno"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "店面");
        datamap.put("b", purchaseInfo.get("organizename")==null?"":purchaseInfo.get("organizename"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "物料明细");
        datamap.put("b", "类别");
        datamap.put("c", "名称");
        datamap.put("d", "单位");
        datamap.put("e", "数量");
        datamap.put("f", "单价");
        datamap.put("g", "总价");
        datamap.put("h", "规格");
        dataset.add(datamap);
        for(int i=0;i<materiallist.size();i++){
        	datamap=new TreeMap<String, Object>();
            datamap.put("a", "");
            datamap.put("b", materiallist.get(i).get("type")==null?"":materiallist.get(i).get("type"));
            datamap.put("c", materiallist.get(i).get("name")==null?"":materiallist.get(i).get("name"));
            datamap.put("d", materiallist.get(i).get("unit")==null?"":materiallist.get(i).get("unit"));
            datamap.put("e", materiallist.get(i).get("num")==null?"":materiallist.get(i).get("num"));
            datamap.put("f", materiallist.get(i).get("price")==null?"":materiallist.get(i).get("price"));
            datamap.put("g", materiallist.get(i).get("sumprice")==null?"":materiallist.get(i).get("sumprice"));
            datamap.put("h", materiallist.get(i).get("specificationsall")==null?"":materiallist.get(i).get("specificationsall"));
            dataset.add(datamap);
        }
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "");
        datamap.put("b", "共"+purchaseInfo.get("maternum")+"项");
        datamap.put("c", "");
        datamap.put("d", "");
        datamap.put("e", "");
        datamap.put("f", "");
        datamap.put("g", "");
        datamap.put("h", "合计￥"+purchaseInfo.get("materialprice"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "采购人");
        datamap.put("b", purchaseInfo.get("createname")==null?"":purchaseInfo.get("createname"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "申请时间");
        datamap.put("b", purchaseInfo.get("createtime")==null?"":purchaseInfo.get("createtime"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "审批人");
        datamap.put("b", purchaseInfo.get("examinename")==null?"":purchaseInfo.get("examinename"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "审批时间");
        datamap.put("b", purchaseInfo.get("updatetime")==null?"":purchaseInfo.get("updatetime"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "审批人意见");
        datamap.put("b", purchaseInfo.get("opinion")==null?"":purchaseInfo.get("opinion"));
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
		return "采购(入库)单详情.xls";
	}
	
	
	/* ------------------------------申购单 ---------------------------------------   */
	
	/**
	 * 申购单查询
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getApplyOrder")
	public String getApplyOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		if(map.containsKey("status") && map.get("status").equals("")){
			map.remove("status");
		}
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		
		PageHelper page = new PageHelper(request);
		int ordernum=this.purchaseService.getApplyOrderListNum(map);		
		page.setTotalCount(ordernum);
		page.initPage(map);
		List<Map<String, Object>> orderlist=this.purchaseService.getApplyOrderList(map);
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("pager",page.cateringPage().toString());
		model.addAttribute("map", map);
		
		Map<String, Object> powerMap=RedisUtil.getMap(map.get("userid")+"powerMap");
		model.addAttribute("powerMap",powerMap);
		return "/pc/purchase/apply_list";
	}
	
	/**
	 * 
	 * 导出申购单
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportApplyList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportApplyList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/申购单列表.xls");
		if(map.containsKey("status") && map.get("status").equals("")){
			map.remove("status");
		}
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		
		List<Map<String, Object>> orderlist=this.purchaseService.getApplyOrderList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "申购单编号", "申请人", "申请时间", "项数","金额","状态"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> order:orderlist){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a", order.get("orderno")==null?"":order.get("orderno"));
	        datamap.put("b", order.get("createname")==null?"":order.get("createname"));
	        datamap.put("c", order.get("createtime")==null?"":order.get("createtime"));
	        datamap.put("d", order.get("maternum")==null?"":order.get("maternum"));
	        datamap.put("e", order.get("materialprice")==null?"":order.get("materialprice"));
	        if(String.valueOf(order.get("status")).equals("1")){
	        	datamap.put("f","已处理");
	        }else{
	        	datamap.put("f","待处理");
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
		return "申购单列表.xls";
	}
	
	/**
	 * 初始化添加申购单（获取申购单编号）
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initInsertApply")
	public String initInsertApply(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		int ordernum=this.purchaseService.getApplyNumByCompany(map);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmm");
		model.addAttribute("orderno","SGD"+sdf.format(new Date())+String.format("%05d",(ordernum+1)));
		SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		model.addAttribute("createtime",sdf1.format(new Date()));
		//查询规格计量单位
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("typeid", 3);
		List<Map<String, Object>> datalist=this.indexService.getDictData(paramMap);
		model.addAttribute("dictlistone",datalist);
		//查询规格计数单位
		paramMap=new HashMap<String, Object>();
		paramMap.put("typeid", 4);
		List<Map<String, Object>> datalist1=this.indexService.getDictData(paramMap);
		model.addAttribute("dictlisttwo",datalist1);
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);
		return "/pc/purchase/apply_add";
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
	public String getApplyInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//申购单详情
		Map<String, Object> applyInfo=this.purchaseService.getApplyInfo(map);
		
		//申购物料列表
		List<Map<String, Object>> materiallist=this.purchaseService.getApplyMaterialList(applyInfo);
		
		applyInfo.put("materiallist", materiallist);
		model.addAttribute("applyInfo", applyInfo);
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("map", map);
		return "/pc/purchase/apply_detail";
	}
	
	/**
	 * 
	 * 导出申购单详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportApplyInfo",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportApplyInfo(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/申购单详情.xls");
		
		//申购单详情
		Map<String, Object> applyInfo=this.purchaseService.getApplyInfo(map);
		//申购物料列表
		List<Map<String, Object>> materiallist=this.purchaseService.getApplyMaterialList(applyInfo);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>(); 
        String[] headers = { "名称", "内容", "", "","","","",""};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
        datamap.put("a", "申购单编号");
        datamap.put("b", applyInfo.get("orderno")==null?"":applyInfo.get("orderno"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "店面");
        datamap.put("b", applyInfo.get("organizename")==null?"":applyInfo.get("organizename"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "物料明细");
        datamap.put("b", "类别");
        datamap.put("c", "名称");
        datamap.put("d", "单位");
        datamap.put("e", "数量");
        datamap.put("f", "单价");
        datamap.put("g", "总价");
        datamap.put("h", "规格");
        dataset.add(datamap);
        for(int i=0;i<materiallist.size();i++){
        	datamap=new TreeMap<String, Object>();
            datamap.put("a", "");
            datamap.put("b", materiallist.get(i).get("type")==null?"":materiallist.get(i).get("type"));
            datamap.put("c", materiallist.get(i).get("name")==null?"":materiallist.get(i).get("name"));
            datamap.put("d", materiallist.get(i).get("unit")==null?"":materiallist.get(i).get("unit"));
            datamap.put("e", materiallist.get(i).get("num")==null?"":materiallist.get(i).get("num"));
            datamap.put("f", materiallist.get(i).get("price")==null?"":materiallist.get(i).get("price"));
            datamap.put("g", materiallist.get(i).get("sumprice")==null?"":materiallist.get(i).get("sumprice"));
            datamap.put("h", materiallist.get(i).get("specificationsall")==null?"":materiallist.get(i).get("specificationsall"));
            dataset.add(datamap);
        }
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "");
        datamap.put("b", "共"+applyInfo.get("maternum")+"项");
        datamap.put("c", "");
        datamap.put("d", "");
        datamap.put("e", "");
        datamap.put("f", "");
        datamap.put("g", "");
        datamap.put("h", "合计￥"+applyInfo.get("materialprice"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "申购人");
        datamap.put("b", applyInfo.get("createname")==null?"":applyInfo.get("createname"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "申请时间");
        datamap.put("b", applyInfo.get("createtime")==null?"":applyInfo.get("createtime"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "审批人");
        datamap.put("b", applyInfo.get("examinename")==null?"":applyInfo.get("examinename"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "审批时间");
        datamap.put("b", applyInfo.get("updatetime")==null?"":applyInfo.get("updatetime"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "审批人意见");
        datamap.put("b", applyInfo.get("opinion")==null?"":applyInfo.get("opinion"));
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
		return "申购单详情.xls";
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
	 * 查询用户组织架构信息
	 * @param map
	 * @param request
	 * @author silence
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getPCOrganize")
	public Map<String,Object> getPCOrganize(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			Map<String, Object> userinfo=UserUtil.getPCUser(request);
			Map<String, Object> organizemap=new HashMap<String, Object>();
			organizemap.put("companyid", userinfo.get("companyid"));
			list = this.indexService.getOrganizeList(organizemap);
			resultMap.put("organizelist", list);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("organizelist", "");
			resultMap.put("status", 1);
		}
		
		return resultMap;
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
	 * 供应商列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getSupplierList")
	public String getSupplierList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);
		map.put("companyid", userInfo.get("companyid"));
		PageHelper page = new PageHelper(request);
		int num=this.supplierService.getSupplierListNum(map);		
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> supplierlist=this.supplierService.getSupplierList(map);
		model.addAttribute("supplierlist", supplierlist);
		model.addAttribute("pager",page.cateringPage().toString());
		model.addAttribute("map", map);
		
		Map<String, Object> powerMap=RedisUtil.getMap(userInfo.get("userid")+"powerMap");
		model.addAttribute("powerMap",powerMap);
		
		return "/pc/purchase/supplier_list";
	}
	
	/**
	 * 
	 * 导出采购(入库)单
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportSupplierList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportSupplierList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/供应商列表.xls");
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("companyid", userInfo.get("companyid"));
		List<Map<String, Object>> supplierlist=this.supplierService.getSupplierList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "供应商编号", "供应商名称", "联系人", "联系电话"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> supplier:supplierlist){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a", supplier.get("supplierno")==null?"":supplier.get("supplierno"));
	        datamap.put("b", supplier.get("suppliername")==null?"":supplier.get("suppliername"));
	        datamap.put("c", (supplier.get("contactsname")==null?"":supplier.get("contactsname")+"")+(supplier.get("position")==null?"":"("+supplier.get("position")+")"));
	        datamap.put("d", supplier.get("phone")==null?"":supplier.get("phone"));
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
		return "供应商列表.xls";
	}
	
	/**
	 * 初始化添加供应商（获取供应商编号）
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initEditSupplier")
	public String initEditSupplier(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);
		map.put("companyid", userInfo.get("companyid"));
		
		if(map.containsKey("supplierid") && !"".equals(map.get("supplierid"))){
			Map<String, Object> supplierInfo=this.supplierService.getSupplierInfo(map);
			model.addAttribute("supplierInfo",supplierInfo);
		}else{
			int suppliernum=this.supplierService.getSupplierNumByCompany(map);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmm");
			model.addAttribute("supplierno","GYS"+sdf.format(new Date())+String.format("%05d",(suppliernum+1)));
		}
		Map<String, Object> city= new HashMap<String, Object>();
		city.put("parentid", 4);
		List<Map<String, Object>> provincelist=this.indexService.getCityList(city);
		model.addAttribute("provincelist",provincelist);
		
		Map<String, Object> powerMap=RedisUtil.getMap(userInfo.get("userid")+"powerMap");
		model.addAttribute("powerMap",powerMap);
		
		return "/pc/purchase/supplier_add";
	}
	
	
	/**
	 * 添加供应商/修改供应商 传入 supplierid时是修改
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/editSupplier")
	public String editSupplier(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		if(map.containsKey("supplierid") && !"".equals(map.get("supplierid"))){
			map.put("updateid", map.get("userid"));
			map.put("updatetime", new Date());
			this.supplierService.updateSupplier(map);
		}else{
			map.put("createid", map.get("userid"));
			this.supplierService.insertSupplier(map);
		}
		return "redirect:/pc/getSupplierList";
	}
	
	
	/**
	 * 供应商详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getSupplierInfo")
	public String getSupplierInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> supplierInfo=this.supplierService.getSupplierInfo(map);
		model.addAttribute("supplierInfo",supplierInfo);
		
		return "/pc/purchase/supplier_detail";
	}
	
	/**
	 * 
	 * 导出供应商详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportSupplierInfo",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportSupplierInfo(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/供应商详情.xls");
		Map<String, Object> supplierInfo=this.supplierService.getSupplierInfo(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "名称", "内容"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
        datamap.put("a", "供应商编号");
        datamap.put("b", supplierInfo.get("supplierno")==null?"":supplierInfo.get("supplierno"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "供应商名称");
        datamap.put("b", supplierInfo.get("suppliername")==null?"":supplierInfo.get("suppliername"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "供应商类型");
        datamap.put("b", supplierInfo.get("suppliertype")==null?"":supplierInfo.get("suppliertype"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "联系人");
        datamap.put("b", supplierInfo.get("contactsname")==null?"":supplierInfo.get("contactsname"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "联系人电话");
        datamap.put("b", supplierInfo.get("phone")==null?"":supplierInfo.get("phone"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "职务");
        datamap.put("b", supplierInfo.get("position")==null?"":supplierInfo.get("position"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "联系地址");
        datamap.put("b", (supplierInfo.get("provincename")==null?"":supplierInfo.get("provincename")+"")+(supplierInfo.get("cityname")==null?"":supplierInfo.get("cityname")+"")+(supplierInfo.get("districtname")==null?"":supplierInfo.get("districtname")+"")+(supplierInfo.get("address")==null?"":supplierInfo.get("address")+""));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "纳税人识别号");
        datamap.put("b", supplierInfo.get("taxpayernum")==null?"":supplierInfo.get("taxpayernum"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "开户行");
        datamap.put("b", supplierInfo.get("bankname")==null?"":supplierInfo.get("bankname"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "银行账号");
        datamap.put("b", supplierInfo.get("bankaccount")==null?"":supplierInfo.get("bankaccount"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "简介");
        datamap.put("b", supplierInfo.get("introduction")==null?"":supplierInfo.get("introduction"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "创建人");
        datamap.put("b", supplierInfo.get("createname")==null?"":supplierInfo.get("createname"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "创建时间");
        datamap.put("b", supplierInfo.get("createtime")==null?"":supplierInfo.get("createtime"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "修改人");
        datamap.put("b", supplierInfo.get("updatename")==null?"":supplierInfo.get("updatename"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "修改时间");
        datamap.put("b", supplierInfo.get("updatetime")==null?"":supplierInfo.get("updatetime"));
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
		return "供应商详情.xls";
	}
	/**
	 * 月报表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/monthlyReport")
	public String monthlyReport(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		
		return "/pc/purchase/monthly_report";
	}
	
}
