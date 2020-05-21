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
import com.collection.service.oa.JournalService;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;

/**
 * 
 * @author pengqinghai
 *
 */
@Controller
@RequestMapping("/pc")
public class JournalPcController extends BaseController{
	@Resource private IndexService indexService;
	@Resource private  JournalService journalService;
	 
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
		try{
		PageHelper page = new PageHelper(request);
		int num=this.journalService.getJournalListCount(map);		
		page.setTotalCount(num);
		page.initPage(map);
		
		List<Map<String, Object>> dataList=this.journalService.getJournalList(map);
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
	
	/**
	 * 
	 * 导出日志列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportLogList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogRequestList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/日志列表.xls");
		List<Map<String, Object>> list=this.journalService.getJournalList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "标题","内容","申请时间","状态","点评星级"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> log:list){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        String title ="";
			if(Integer.parseInt(log.get("jotype").toString()) == 1){
				title="日报";
			}else if(Integer.parseInt(log.get("jotype").toString()) == 2){
				title="周报";
			}else if(Integer.parseInt(log.get("jotype").toString()) == 3){
				title="月报";
			}
			String name =log.get("realname").toString();
	        datamap.put("a",name+"的"+title);
	        datamap.put("b", log.get("completedwork")==null?"":"完成工作："+log.get("completedwork"));
	        datamap.put("c",  log.get("createtime")==null?"":log.get("createtime"));
	        datamap.put("d", Integer.parseInt(log.get("status").toString())==0?"待处理":"已处理");
	        datamap.put("e", log.get("commentlevel")==null?"":log.get("commentlevel")+"星");
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
		return "日志列表.xls";
	}
	
	/**
	 * 
	 * 导出日志详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportLogDetail",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogRequestDetail(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/日志详情.xls");
		Map<String, Object> mapLog=this.journalService.getLogDetailInfo(map);
        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>(); 
        String[] headers =new String [10];
        
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
        
        if(map.containsKey("jotype")){
        	if(Integer.parseInt(map.get("jotype").toString()) == 1){
        		headers[0] = "完成工作";
        		headers[1] = "未完成工作";
        		headers[2] = "需要帮助和支持";
        		headers[3] = "创建人";
        		headers[4] = "创建时间";
        		headers[5] = "点评人";
        		headers[6] = "点评时间";
        		headers[7] = "点评人意见";
        		headers[8] = "备注";
        		datamap.put("a", mapLog.get("completedwork")==null?"":mapLog.get("completedwork"));
        		datamap.put("b",  mapLog.get("notcompletedwork")==null?"":mapLog.get("notcompletedwork"));
        		datamap.put("c", mapLog.get("needhelp")==null?"":mapLog.get("needhelp"));
        		datamap.put("d",  mapLog.get("createname")==null?"":mapLog.get("createname"));
        		datamap.put("e",  mapLog.get("createtime")==null?"":mapLog.get("createtime"));	
        		datamap.put("f",  mapLog.get("commentusername")==null?"":mapLog.get("commentusername"));
        		datamap.put("g",  mapLog.get("updatetime")==null?"":mapLog.get("updatetime"));	
        	    datamap.put("h", mapLog.get("commentlevel")==null?"":mapLog.get("commentlevel")+"星");
        	    datamap.put("i", mapLog.get("remark")==null?"":mapLog.get("remark"));	
        	}else{
        		headers[0] = "完成工作";
        		headers[1] = "工作总结";
        		if(Integer.parseInt(map.get("jotype").toString()) == 2){
        			headers[2] = "下周工作计划";
        		}else if(Integer.parseInt(map.get("jotype").toString()) == 3){
        			headers[2] = "下月工作计划";
        		}
        		headers[3] = "需要帮助和支持";
        		headers[4] = "创建人";
        		headers[5] = "创建时间";
        		headers[6] = "点评人";
        		headers[7] = "点评时间";
        		headers[8] = "点评人意见";
        		headers[9] = "备注";
        		datamap.put("a", mapLog.get("completedwork")==null?"":mapLog.get("completedwork"));
        		datamap.put("b", mapLog.get("worksummary")==null?"":mapLog.get("worksummary"));
        		datamap.put("c", mapLog.get("nextplan")==null?"":mapLog.get("nextplan"));
         		datamap.put("d", mapLog.get("needhelp")==null?"":mapLog.get("needhelp"));
         		datamap.put("e",  mapLog.get("createname")==null?"":mapLog.get("createname"));
         		datamap.put("f",  mapLog.get("createtime")==null?"":mapLog.get("createtime"));	
         		datamap.put("g",  mapLog.get("commentusername")==null?"":mapLog.get("commentusername"));
         		datamap.put("h",  mapLog.get("updatetime")==null?"":mapLog.get("updatetime"));	
         	    datamap.put("i", mapLog.get("commentlevel")==null?"":mapLog.get("commentlevel")+"星");
         	    datamap.put("j", mapLog.get("remark")==null?"":mapLog.get("remark"));
        	}
        	
        }  
 
   
	  
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
		return "日志详情.xls";
	}
	
}
