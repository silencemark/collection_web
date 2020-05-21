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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.service.IndexService;
import com.collection.service.oa.NoticeService;
import com.collection.util.PageScroll;

/**
 * 
 * @author silence
 *
 */
@Controller
@RequestMapping("/app")
public class NoticeAppController extends BaseController{
	@Resource private IndexService indexService;
	@Resource private  NoticeService noticeService;
	 
	/**
	 * 得到通知列表
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getNoticeList")
	@ResponseBody
	public Map<String, Object> getNoticeList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		try{
		PageScroll page = new PageScroll();
		int remindnum=this.noticeService.getNoticeListCount(map);		
		page.setTotalRecords(remindnum);
		page.initPage(map);
		List<Map<String, Object>> ntList=this.noticeService.getNoticeList(map);
		data.put("ntList", ntList);
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
	 * 新增通知
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/insertNotice")
	@ResponseBody
	public Map<String, Object> insertNotice(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		try{
	    int row = this.noticeService.addNotice(map);
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
	 * 得到通知详情
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getNoticeDetail")
	@ResponseBody
	public Map<String, Object> getNoticeDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		try{
	      Map<String,Object> noticemap =this.noticeService.getNoticeDetail(map);
		  if(noticemap != null){
			  data.put("data", noticemap);
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
	
	
	
}
