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
import com.collection.service.oa.JournalService;
import com.collection.util.PageScroll;

/**
 * 
 * @author pengqinghai
 *
 */
@Controller
@RequestMapping("/app")
public class JournalAppController extends BaseController{
	@Resource private IndexService indexService;
	@Resource private  JournalService journalService;
	 
	
	/**
	 * 查询日志 已处理，未处理 的 未读数量
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getJournalAllNotRead")
	public Map<String,Object> getJournalAllNotRead(@RequestParam Map<String,Object> map , HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.journalService.getJournalListCount(map);	
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.journalService.getJournalTimesListCount(map);
			
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
	 * 得到日志列表
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getJournalList")
	@ResponseBody
	public Map<String, Object> getJournalList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		map.put("status", 0);
		try{
		PageScroll page = new PageScroll();
		int remindnum=this.journalService.getJournalListCount(map);		
		page.setTotalRecords(remindnum);
		page.initPage(map);
		List<Map<String, Object>> dataList=this.journalService.getJournalList(map);
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
	 * 查询已经处理的日志的时间列表
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getExamineJournalList" , method = RequestMethod.POST)
	public Map<String,Object> getExamineJournalList(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		map.put("status", 1);
		try {
			PageScroll page = new PageScroll();
			int count = this.journalService.getJournalTimesListCount(map);
			page.setTotalRecords(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.journalService.getJournalTimesList(map);
			if(list != null && list.size() > 0){
				data.put("data", list);
				data.put("status", 0);
			}else{
				data.put("data", "");
				data.put("status", 1);
			}
			data.put("page", page);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			data.put("data", "");
			data.put("status", 1);
		}
		
		return data;
	}
	
	/**
	 * 新增日报
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addDaily")
	@ResponseBody
	public Map<String, Object> insertDaily(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		try{
	    int row = this.journalService.insertDaily(map);
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
	 * 新增周报
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addtWeekly")
	@ResponseBody
	public Map<String, Object> insertWeekly(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		try{
	    int row = this.journalService.insertWeekly(map);
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
	 * 新增月报
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addMonthly")
	@ResponseBody
	public Map<String, Object> insertMonthly(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		try{
	    int row = this.journalService.insertMonthly(map);
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
	 * 得到日志详情
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getJournalDetaiInfo")
	@ResponseBody
	public Map<String, Object> getJournalDetaiInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		try{
	      Map<String, Object> jomap=this.journalService.getLogDetailInfo(map);
		  if(jomap != null){
			  data.put("data", jomap);
			  data.put("status", "0");
			  data.put("msg", "查询成功");
		  }else{
			  data.put("status", "1");
			  data.put("msg", "查询参数缺失");
		  }
		
		}catch(Exception e){
			data.put("status", "1");
			data.put("msg", "查询失败");
			e.printStackTrace();
		}
		return data;
	}
	
	
	/**
	 * 提交抄送人意见
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateJournalInfo" , method = RequestMethod.POST)
	public Map<String,Object> updateJournalInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		
		try {
			this.journalService.updateJournalInfo(map);
			data.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
		}
		
		return data;
	}
	
	
	
}
