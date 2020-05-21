package com.collection.controller.pc.oa;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.controller.userbackstage.UserIndexController.ExportExcel;
import com.collection.service.IndexService;
import com.collection.service.oa.ApprovalService;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;

/**
 * 
 * @author silence
 *
 */
@Controller
@RequestMapping("/pc")
public class ApprovaPcController extends BaseController{
	@Resource private IndexService indexService;
	@Resource private  ApprovalService approvalService;
	 
	/**
	 * 得到通用审批列表
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getCurrencyExamineList")
	@ResponseBody
	public Map<String, Object> getCurrencyExamineList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		try{
		PageHelper page = new PageHelper(request);
		int num=this.approvalService.getCurrencyExamineListCount(map);		
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> dataList=this.approvalService.getCurrencyExamineList(map);
		data.put("dataList", dataList);
		data.put("pager", page.JSCateringPage().toString());
		data.put("status", "0");
		data.put("msg", "查询成功");
		}catch(Exception e){
			e.printStackTrace();
			data.put("status", "1");
			data.put("msg", "查询失败");
		}
		
		return data;
	}
	
	
	/**
	 * 查询通用审批详情
	 * currencyexamineid
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getCurrencyExamineDetailInfo" , method = RequestMethod.POST)
	public Map<String,Object> getCurrencyExamineDetailInfo(@RequestParam Map<String,Object> map , 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			Map<String,Object> detail = this.approvalService.getCurrencyExamineDetailInfo(map);
			if(detail != null){
				resultMap.put("status", 0);
				resultMap.put("data", detail);
				resultMap.put("message", "查询成功");
			}else{
				resultMap.put("status", 1);
				resultMap.put("data", "");
				resultMap.put("message", "查询成功，暂无数据");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("data", "");
			resultMap.put("message", "查询出错");
		}
		
		return resultMap;
	}
	
	/**
	 * 新增通用审批
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addCurrencyExamine")
	@ResponseBody
	public Map<String, Object> addCurrencyExamine(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		try{
	    int row = this.approvalService.inserCurrencyExamine(map);
		 if(row>0){
			 data.put("status", "0");
		     data.put("msg", "操作成功");
		 }else{
			 data.put("status", "1");
		     data.put("msg", "操作失败");
		 }
		}catch(Exception e){
			 data.put("status", "2");
		     data.put("msg", "操作异常");
			e.printStackTrace();
		}
		return data;
	}
	
	
	/**
	 * 提交审核信息
	 * 传入参数：result，opinion，userid，currencyexamineid
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateCurrencyExamineInfo" , method = RequestMethod.POST)
	public Map<String,Object> updateCurrencyExamineInfo(@RequestParam Map<String,Object> map , 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		
		try {
			this.approvalService.updateCurrencyExamineInfo(map);
			
			data.put("status", 0);
			data.put("message", "修改成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			data.put("status", 1);
			data.put("message", "修改出错了");
		}
		
		return data;
	}
	/**
	 * 
	 * 导出审批列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportCurrencyList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogRequestList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/审批列表.xls");
		List<Map<String, Object>> list=this.approvalService.getCurrencyExamineList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "申请人","申请内容","申请时间","状态","审批结果"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> log:list){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a",log.get("createname")==null?"":""+log.get("createname"));
	        datamap.put("b",log.get("content")==null?"":""+log.get("content"));
	        datamap.put("c",  log.get("details")==null?"":log.get("details"));
	        datamap.put("d", Integer.parseInt(log.get("status").toString())==0?"待处理":"已处理");
	        if(log.containsKey("result")){
	        	datamap.put("e", Integer.parseInt(log.get("result").toString())==1?"已同意":"已拒绝");
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
		return "审批列表.xls";
	}
	
	/**
	 * 
	 * 导出审批详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportCurrencyDetail",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogRequestDetail(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/审批详情.xls");
		
		Map<String, Object> mapLog=this.approvalService.getCurrencyExamineDetailInfo(map);
        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>(); 
        String[] headers = { "申请内容","审批内容","填写人","填写时间","审批人","状态","审批结果","审批意见"};
        
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
        
        datamap.put("a",mapLog.get("content")==null?"":""+mapLog.get("content"));
        datamap.put("b",  mapLog.get("details")==null?"":mapLog.get("details"));
        datamap.put("c",mapLog.get("createname")==null?"":""+mapLog.get("createname"));
        datamap.put("d",  mapLog.get("createtime")==null?"":mapLog.get("createtime"));
        datamap.put("e",  mapLog.get("examinename")==null?"":mapLog.get("examinename"));
        datamap.put("f", Integer.parseInt(mapLog.get("status").toString())==0?"待处理":"已处理");
        if(mapLog.containsKey("result")){
        	datamap.put("g", Integer.parseInt(mapLog.get("result").toString())==1?"已同意":"已拒绝");
        }
        datamap.put("h",  mapLog.get("opinion")==null?"":mapLog.get("opinion"));
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
		return "审批详情.xls";
	}
}
