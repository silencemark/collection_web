package com.collection.controller.oa;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.collection.service.IndexService;
import com.collection.service.oa.ApprovalService;
import com.collection.util.PageScroll;

/**
 * 
 * @author silence
 *
 */
@Controller
@RequestMapping("/app")
public class ApprovalAppController extends BaseController{
	@Resource private IndexService indexService;
	@Resource private  ApprovalService approvalService;
	 
	
	/**
	 * 查询通用审批 已处理，未处理的未读数量
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getCurrencyExamineAllNotRead")
	public Map<String,Object> getCurrencyExamineAllNotRead(@RequestParam Map<String,Object> map , HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.approvalService.getCurrencyExamineListCount(map);	
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.approvalService.getCurrencyExamineTimesCount(map);	
			
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
		map.put("status", 0);
		Map<String, Object> data=new HashMap<String, Object>();
		try{
		PageScroll page = new PageScroll();
		int remindnum=this.approvalService.getCurrencyExamineListCount(map);		
		page.setTotalRecords(remindnum);
		page.initPage(map);
		List<Map<String, Object>> dataList=this.approvalService.getCurrencyExamineList(map);
		data.put("dataList", dataList);
		data.put("page",page);
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
	 * 查询已经处理了的通用审批信息
	 * 传入参数：companyid，userid，status
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getExamineCurrencyExamineList" , method = RequestMethod.POST)
	public Map<String,Object> getExamineCurrencyExamineList(@RequestParam Map<String,Object> map , 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		try{
		PageScroll page = new PageScroll();
		int remindnum=this.approvalService.getCurrencyExamineTimesCount(map);	
		page.setTotalRecords(remindnum);
		page.initPage(map);
		List<Map<String, Object>> dataList=this.approvalService.getCurrencyExamineTimesList(map);
		data.put("dataList", dataList);
		data.put("page",page);
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
}
