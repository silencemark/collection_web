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
import com.collection.service.warehouse.WarehouseService;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;
import com.collection.util.UserUtil;


/**
 * 仓库管理
 * @author silence
 *
 */
@Controller
@RequestMapping("/pc")
public class PCWarehouseController {
	private transient static Log log = LogFactory.getLog(PCWarehouseController.class);
	
	@Resource private WarehouseService warehouseService;
	
	@Resource private IndexService indexService;
	
	@Resource private PurchaseService purchaseService;
	
	/**
	 * 用料单列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getMaterialOrder")
	public String getMaterialOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		if(map.containsKey("status") && map.get("status").equals("")){
			map.remove("status");
		}
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		
		PageHelper page = new PageHelper(request);
		int ordernum=this.warehouseService.getMaterialListNum(map);
		page.setTotalCount(ordernum);
		page.initPage(map);
		List<Map<String, Object>> orderlist=this.warehouseService.getMaterialList(map);
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("pager",page.cateringPage().toString());
		
		model.addAttribute("map", map);
		Map<String, Object> powerMap=RedisUtil.getMap(map.get("userid")+"powerMap");
		model.addAttribute("powerMap",powerMap);
		return "/pc/warehouse/use_list";
	}

	/**
	 * 
	 * 导出用料单
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportUseList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportUseList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/用料单列表.xls");
		if(map.containsKey("status") && map.get("status").equals("")){
			map.remove("status");
		}
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		List<Map<String, Object>> orderlist=this.warehouseService.getMaterialList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "用料单编号", "申请人", "申请时间", "项数","金额","状态"};
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
		return "用料单列表.xls";
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
	public String initInsertMaterial(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("companyid", userInfo.get("companyid"));
		map.put("organizeid", userInfo.get("organizeid"));
		model.addAttribute("userInfo", userInfo);
		
		int materialnum=this.warehouseService.getMaterialNumByCompany(map);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmm");
		model.addAttribute("orderno","YLD"+sdf.format(new Date())+String.format("%05d",(materialnum+1)));
		SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		model.addAttribute("createtime",sdf1.format(new Date()));
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

		model.addAttribute("materialTypeList",materialTypeList);
		model.addAttribute("materialNameList",materialNameList);

		return "/pc/warehouse/use_add";
	}
	
	
	/**
	 * 获取类型和名称 根据 组织id
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getMaterialTypeNameList")
	@ResponseBody
	public Map<String, Object> getMaterialTypeNameList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
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
		
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("materialTypeList",materialTypeList);
		data.put("materialNameList",materialNameList);
		data.put("status", 0);
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
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);
		//用料单详情
		Map<String, Object> materialInfo=this.warehouseService.getMaterialInfo(map);
		//用料单物料列表
		List<Map<String, Object>> materiallist=this.warehouseService.getUsedMaterialList(materialInfo);
		model.addAttribute("materialInfo", materialInfo);
		model.addAttribute("materiallist", materiallist);
		model.addAttribute("map", map);
		return "/pc/warehouse/use_detail";
	}
	
	/**
	 * 
	 * 导出用料单详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportUseInfo",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportUseInfo(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/用料单详情.xls");
		
		//用料单详情
		Map<String, Object> materialInfo=this.warehouseService.getMaterialInfo(map);
		//用料单物料列表
		List<Map<String, Object>> materiallist=this.warehouseService.getUsedMaterialList(materialInfo);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>(); 
        String[] headers = { "名称", "内容", "", "","","","",""};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
        datamap.put("a", "用料单编号");
        datamap.put("b", materialInfo.get("orderno")==null?"":materialInfo.get("orderno"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "店面");
        datamap.put("b", materialInfo.get("organizename")==null?"":materialInfo.get("organizename"));
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
        datamap.put("b", "共"+materialInfo.get("maternum")+"项");
        datamap.put("c", "");
        datamap.put("d", "");
        datamap.put("e", "");
        datamap.put("f", "");
        datamap.put("g", "");
        datamap.put("h", "合计￥"+materialInfo.get("materialprice"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "申请人");
        datamap.put("b", materialInfo.get("createname")==null?"":materialInfo.get("createname"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "申请时间");
        datamap.put("b", materialInfo.get("createtime")==null?"":materialInfo.get("createtime"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "审批人");
        datamap.put("b", materialInfo.get("examinename")==null?"":materialInfo.get("examinename"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "审批时间");
        datamap.put("b", materialInfo.get("updatetime")==null?"":materialInfo.get("updatetime"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "审批人意见");
        datamap.put("b", materialInfo.get("opinion")==null?"":materialInfo.get("opinion"));
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
		return "用料单详情.xls";
	}
	/*------------------------------------------------退货单------------------------------------------------------*/
	
	/**
	 * 退货单列表(待处理)
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReturnOrder")
	public String getReturnOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		if(map.containsKey("status") && map.get("status").equals("")){
			map.remove("status");
		}
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		
		PageHelper page = new PageHelper(request);
		int ordernum=this.warehouseService.getReturnOrderListNum(map);	
		page.setTotalCount(ordernum);
		page.initPage(map);
		List<Map<String, Object>> orderlist=this.warehouseService.getReturnOrderList(map);
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("pager",page.cateringPage().toString());
		
		model.addAttribute("map", map);
		Map<String, Object> powerMap=RedisUtil.getMap(map.get("userid")+"powerMap");
		model.addAttribute("powerMap",powerMap);
		return "/pc/warehouse/returngoods_list";
	}
	

	/**
	 * 
	 * 导出退货单
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportReturnList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportReturnList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/退货单列表.xls");
		if(map.containsKey("status") && map.get("status").equals("")){
			map.remove("status");
		}
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		List<Map<String, Object>> orderlist=this.warehouseService.getMaterialList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "退货单编号", "申请人", "申请时间", "项数","金额","状态"};
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
		return "退货单列表.xls";
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
	public String initInsertReturn(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("companyid", userInfo.get("companyid"));
		map.put("organizeid", userInfo.get("organizeid"));
		model.addAttribute("userInfo", userInfo);
		
		int ordernum=this.warehouseService.getReturnNumByCompany(map);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmm");
		model.addAttribute("orderno","THD"+sdf.format(new Date())+String.format("%05d",(ordernum+1)));
		SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		model.addAttribute("createtime",sdf1.format(new Date()));
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

		model.addAttribute("materialTypeList",materialTypeList);
		model.addAttribute("materialNameList",materialNameList);
			
		return "/pc/warehouse/returngoods_add";
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
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);
		//退货单详情
		Map<String, Object> returnInfo=this.warehouseService.getReturnInfo(map);
		//退货单物料列表
		List<Map<String, Object>> materiallist=this.warehouseService.getReturnMaterialList(returnInfo);
		model.addAttribute("returnInfo", returnInfo);
		model.addAttribute("materiallist", materiallist);
		model.addAttribute("map", map);

		return "/pc/warehouse/returngoods_detail";
	}
	
	/**
	 * 
	 * 导出退货单详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportReturnInfo",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportReturnInfo(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/退货单详情.xls");
		
		//退货单详情
		Map<String, Object> returnInfo=this.warehouseService.getReturnInfo(map);
		//退货单物料列表
		List<Map<String, Object>> materiallist=this.warehouseService.getReturnMaterialList(returnInfo);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>(); 
        String[] headers = { "名称", "内容", "", "","","","",""};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
        datamap.put("a", "退货单编号");
        datamap.put("b", returnInfo.get("orderno")==null?"":returnInfo.get("orderno"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "店面");
        datamap.put("b", returnInfo.get("organizename")==null?"":returnInfo.get("organizename"));
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
        datamap.put("b", "共"+returnInfo.get("maternum")+"项");
        datamap.put("c", "");
        datamap.put("d", "");
        datamap.put("e", "");
        datamap.put("f", "");
        datamap.put("g", "");
        datamap.put("h", "合计￥"+returnInfo.get("materialprice"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "申请人");
        datamap.put("b", returnInfo.get("createname")==null?"":returnInfo.get("createname"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "申请时间");
        datamap.put("b", returnInfo.get("createtime")==null?"":returnInfo.get("createtime"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "审批人");
        datamap.put("b", returnInfo.get("examinename")==null?"":returnInfo.get("examinename"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "审批时间");
        datamap.put("b", returnInfo.get("updatetime")==null?"":returnInfo.get("updatetime"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "审批人意见");
        datamap.put("b", returnInfo.get("opinion")==null?"":returnInfo.get("opinion"));
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
		return "退货单详情.xls";
	}
	
	/*------------------------------------------------报损单------------------------------------------------------*/
	
	/**
	 * 报损单列表(待处理)
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReportlossOrder")
	public String getReportlossOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		if(map.containsKey("status") && map.get("status").equals("")){
			map.remove("status");
		}
		
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		
		PageHelper page = new PageHelper(request);
		int ordernum=this.warehouseService.getReportlossOrderListNum(map);	
		page.setTotalCount(ordernum);
		page.initPage(map);
		List<Map<String, Object>> orderlist=this.warehouseService.getReportlossOrderList(map);
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("pager",page.cateringPage().toString());
		
		model.addAttribute("map", map);
		Map<String, Object> powerMap=RedisUtil.getMap(map.get("userid")+"powerMap");
		model.addAttribute("powerMap",powerMap);
		return "/pc/warehouse/breakdown_list";
	}
	
	/**
	 * 
	 * 导出报损单
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportReportLossList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportReportLossList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/报损单列表.xls");
		if(map.containsKey("status") && map.get("status").equals("")){
			map.remove("status");
		}
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("userid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		
		List<Map<String, Object>> orderlist=this.warehouseService.getReportlossOrderList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "报损单编号", "申请人", "申请时间", "项数","金额","状态"};
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
		return "报损单列表.xls";
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
	public String initInsertReportloss(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		map.put("organizeid", userInfo.get("organizeid"));
		map.put("companyid", userInfo.get("companyid"));
		model.addAttribute("userInfo", userInfo);
		int ordernum=this.warehouseService.getReportlossNumByCompany(map);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmm");
		model.addAttribute("orderno","BSD"+sdf.format(new Date())+String.format("%05d",(ordernum+1)));
		SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		model.addAttribute("createtime",sdf1.format(new Date()));
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

		model.addAttribute("materialTypeList",materialTypeList);
		model.addAttribute("materialNameList",materialNameList);
		return "/pc/warehouse/breakdown_add";
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
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);
		
		//报损单详情
		Map<String, Object> reportlossInfo=this.warehouseService.getReportlossInfo(map);
		//报损单物料列表
		List<Map<String, Object>> materiallist=this.warehouseService.getReportlossMaterialList(reportlossInfo);
		model.addAttribute("reportlossInfo", reportlossInfo);
		model.addAttribute("materiallist", materiallist);
		model.addAttribute("map", map);
		return "/pc/warehouse/breakdown_detail";
	}
	/**
	 * 
	 * 导出报损单详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportReportLossInfo",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportReportLossInfo(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/报损单详情.xls");
		
		//报损单详情
		Map<String, Object> reportlossInfo=this.warehouseService.getReportlossInfo(map);
		//报损单物料列表
		List<Map<String, Object>> materiallist=this.warehouseService.getReportlossMaterialList(reportlossInfo);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>(); 
        String[] headers = { "名称", "内容", "", "","","","",""};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
        datamap.put("a", "报损单编号");
        datamap.put("b", reportlossInfo.get("orderno")==null?"":reportlossInfo.get("orderno"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "店面");
        datamap.put("b", reportlossInfo.get("organizename")==null?"":reportlossInfo.get("organizename"));
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
        datamap.put("b", "共"+reportlossInfo.get("maternum")+"项");
        datamap.put("c", "");
        datamap.put("d", "");
        datamap.put("e", "");
        datamap.put("f", "");
        datamap.put("g", "");
        datamap.put("h", "合计￥"+reportlossInfo.get("materialprice"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "申请人");
        datamap.put("b", reportlossInfo.get("createname")==null?"":reportlossInfo.get("createname"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "申请时间");
        datamap.put("b", reportlossInfo.get("createtime")==null?"":reportlossInfo.get("createtime"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "审批人");
        datamap.put("b", reportlossInfo.get("examinename")==null?"":reportlossInfo.get("examinename"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "审批时间");
        datamap.put("b", reportlossInfo.get("updatetime")==null?"":reportlossInfo.get("updatetime"));
        dataset.add(datamap);
        datamap=new TreeMap<String, Object>();
        datamap.put("a", "审批人意见");
        datamap.put("b", reportlossInfo.get("opinion")==null?"":reportlossInfo.get("opinion"));
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
		return "报损单详情.xls";
	}
	
	/**
	 * 库存信息
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getAnalysis")
	public String getAnalysis(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);
		
		return "/pc/warehouse/analysis";
	}
	
}
