package com.collection.controller.pc.oa;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.controller.oa.OfficeController;
import com.collection.controller.userbackstage.UserIndexController.ExportExcel;
import com.collection.service.IndexService;
import com.collection.service.oa.NoticeService;
import com.collection.service.oa.OfficeService;
import com.collection.util.PageHelper;
import com.collection.util.SDKTestSendTemplateSMS;

/**
 * oa办公
 *
 */
@Controller
@RequestMapping("/pc")
public class NoticePcController extends BaseController {

	@Resource private IndexService indexService;
	@Resource private  NoticeService noticeService;
	
	@Resource private OfficeService officeService;
	private transient static Log log = LogFactory.getLog(OfficeController.class);

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

			PageHelper page = new PageHelper(request);
			int remindnum = this.noticeService.getNoticeListCount(map);	
			page.setTotalCount(remindnum);
			page.initPage(map);
			List<Map<String, Object>> ntList=this.noticeService.getNoticeList(map);
			
			data.put("ntList", ntList);
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
	 * 删除通知详情
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updatenotice")
	@ResponseBody
	public Map<String, Object> updatenotice(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		try{
		  if(map != null){
			  this.noticeService.updatenotice(map);
			  data.put("status", "0");
			  data.put("msg", "删除成功");
		  }else{
			  data.put("status", "1");
			  data.put("msg", "删除失败");
		  }
		
		}catch(Exception e){
			data.put("status", "1");
			data.put("msg", "删除失败");
			e.printStackTrace();
		}
		return data;
	}
	/**
	 * 查询用户组织架构信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getOrganize")
	public Map<String,Object> getOrganize(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			Map<String, Object> userinfo=(Map<String, Object>) map.get("userInfo");
			if(userinfo != null){
				if(userinfo.containsKey("managerole") && !"".equals(map.get("managerole"))){
					String managerole = String.valueOf(userinfo.get("managerole"));
					if("3".equals(managerole)){
						Map<String, Object> organizemap=new HashMap<String, Object>();
						organizemap.put("companyid", userinfo.get("companyid"));
						list = this.indexService.getOrganizeList(organizemap);
					}else if("2".equals(managerole)){
						map.put("userid", userinfo.get("userid"));
						map.put("companyid", userinfo.get("companyid"));
						list = this.indexService.getOrganizeByUserList(map);
					}
				}
			}
			resultMap.put("compandyname", userinfo.get("companyname"));
			resultMap.put("type", userinfo.get("managerole"));
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
	 * 
	 * 导出通知列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportnotice",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/OA办公通知列表.xls");
		List<Map<String, Object>> ntList=this.noticeService.getNoticeList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "标题","发布人","发布时间", "状态"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> log:ntList){
        	
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a", log.get("title")==null?"":log.get("title"));
	        datamap.put("b",  log.get("realname")==null?"":log.get("realname"));
	        datamap.put("c", log.get("createtime")==null?"":log.get("createtime"));
	        datamap.put("d", Integer.parseInt(log.get("isread").toString())==0?"未读":"已读");
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
		return "OA办公通知列表.xls";
	}
	
	
	/**
	 * 查询请假列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0}
	 * 传出参数{"leavelist":[{"endtime":"2016-07-28 10:00:00","starttime":"2016-07-24 08:00:00","reason":"我发高烧啦","status":0,"isread":1,"examineuserid":"1","leavetypename":"病假","examinename":"李语然","createtime":"2016-07-22 09:41:48","companyid":"67702cc412264f4ea7d2c5f692070457","leaveid":"846aa3dec6f64728b04e7d4fab4431fa","createid":"690fb669ed4d40219964baad7783abd4","leavetype":"2","createname":"silence","daynum":4,"hournum":2}],"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getLeaveList")
	@ResponseBody
	public Map<String, Object> getLeaveList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		PageHelper page = new PageHelper(request);
		int num=this.officeService.getLeaveListNum(map);	
		page.setTotalCount(num);
		page.initPage(map);
		
		List<Map<String, Object>> datalist=this.officeService.getLeaveList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("leavelist", datalist);
		data.put("status",0);
		data.put("pager", page.JSCateringPage().toString());
		return data;
	}
	/**
	 * 
	 * 导出请假列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportLogLeaveList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogLeaveList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/请假列表.xls");
		List<Map<String, Object>> ntList=this.officeService.getLeaveList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "请假人","类别","请假事由", "时长","填写时间","状态","审批结果"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> log:ntList){
        	
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a", log.get("createname")==null?"":log.get("createname"));
	        datamap.put("b",  log.get("leavetypename")==null?"":log.get("leavetypename"));
	        datamap.put("c", log.get("reason")==null?"":log.get("reason"));
	        
	        String daynum=log.get("daynum")==null?"":log.get("daynum").toString();
	        String hournum=log.get("hournum")==null?"":log.get("hournum").toString();
	        datamap.put("d",daynum+"天"+hournum+"小时");
	        datamap.put("e",  log.get("createtime")==null?"":log.get("createtime"));
	        datamap.put("f", Integer.parseInt(log.get("status").toString())==0?"待处理":"已处理");
	        if(log.get("status") != ""  && !"".equals(log.get("status"))){
	         	String result="";
	         	if(log.containsKey("result")){
	         		if(Integer.parseInt(log.get("result").toString()) == 1){
	          		 	result="已同意";
		   	        }else if(Integer.parseInt(log.get("result").toString()) == 2){
		   	        		result="已拒绝";
		   	        }
	         	}
	           datamap.put("g", result);
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
		return "请假列表.xls";
	}

	/**
	 * 查询请假详情页面
	 * 传入参数{"leaveid":"846aa3dec6f64728b04e7d4fab4431fa"}
	 * 传出参数{"message":"查询成功","leaveinfo":{"endtime":"2016-07-28 10:00:00","starttime":"2016-07-24 08:00:00","reason":"我发高烧啦","status":0,"examineuserid":"1","leavetypename":"病假","filelist":[{"createtime":"2016-07-22 09:41:49","companyid":"67702cc412264f4ea7d2c5f692070457","resourcetype":18,"fileid":"0255b5a531b7491387b7c224429d1887","resourceid":"846aa3dec6f64728b04e7d4fab4431fa","type":1,"visiturl":"lalala.jpg","size":1200}],"examinename":"李语然","createtime":"2016-07-22 09:41:48","companyid":"67702cc412264f4ea7d2c5f692070457","leaveid":"846aa3dec6f64728b04e7d4fab4431fa","createid":"690fb669ed4d40219964baad7783abd4","leavetype":"2","createname":"silence","daynum":4,"hournum":2},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getLeaveInfo")
	@ResponseBody
	public Map<String, Object> getLeaveInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			//请假表详情
			Map<String, Object> leaveinfo=this.officeService.getLeaveInfo(map);
			data.put("leaveinfo", leaveinfo);
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
	 * 
	 * 导出请假详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportLogLeaveDetail",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogLeaveDetail(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/请假详情.xls");
		Map<String, Object> leaveinfo=this.officeService.getLeaveDetail(map);
        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "请假人","申请时间","类别","请假事由", "时长","开始时间","结束时间","审批人","审批结果"};
        
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
        
	    datamap.put("a", leaveinfo.get("createname")==null?"":leaveinfo.get("createname"));
	    datamap.put("b",  leaveinfo.get("createtime")==null?"":leaveinfo.get("createtime"));
	    datamap.put("c",  leaveinfo.get("leavetypename")==null?"":leaveinfo.get("leavetypename"));
	    datamap.put("d", leaveinfo.get("reason")==null?"":leaveinfo.get("reason"));
	    String daynum=leaveinfo.get("daynum")==null?"":leaveinfo.get("daynum").toString();
	    String hournum=leaveinfo.get("hournum")==null?"":leaveinfo.get("hournum").toString();
	    datamap.put("e",daynum+"天"+hournum+"小时");
	    datamap.put("f",  leaveinfo.get("starttime")==null?"":leaveinfo.get("starttime"));
	    datamap.put("g",  leaveinfo.get("endtime")==null?"":leaveinfo.get("endtime"));
	    datamap.put("h",  leaveinfo.get("examinename")==null?"":leaveinfo.get("examinename"));
	    if(leaveinfo.get("status") != ""  && !"".equals(leaveinfo.get("status"))){
		     	String result="";
		    	if(leaveinfo.containsKey("result")){
			       	 if(Integer.parseInt(leaveinfo.get("result").toString()) == 1){
			       		 	result="已同意";
				        }else if(Integer.parseInt(leaveinfo.get("result").toString()) == 2){
				        	result="已拒绝";
				        }
		    	}
	       	 datamap.put("i", result);
        	
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
		return "请假详情.xls";
	}
	
	/**
	 * 查询请假类型列表
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getLeaveTypeList" , method = RequestMethod.POST)
	public Map<String,Object> getLeaveTypeList(@RequestParam Map<String,Object> map , 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		map.put("typeid", 2);
		List<Map<String, Object>> datalist=this.indexService.getDictData(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("datalist", datalist);
		data.put("status",0);
		return data;
	}
	
	/**
	 * 添加请假单
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","leavetype":2,"starttime":"2016-07-24 08:00:00","endtime":"2016-07-28 10:00:00","daynum":4,"hournum":2,"reason":"我发高烧啦","examineuserid":1,"filelist":"{"filelist":[{"visiturl":"lalala.jpg","type":1,"size":1200}]}"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertLeave")
	@ResponseBody
	public Map<String, Object> insertLeave(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		String leaveid="";
		try {
			leaveid=this.officeService.insertLeave(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		try {
			//添加到 图片/录音表
			Map<String,Object> filemap = new HashMap<String,Object>();
			filemap.put("companyid", map.get("companyid"));
			filemap.put("resourceid", leaveid);
			filemap.put("resourcetype", 18);
			//添加添加图片的路径
			String files = String.valueOf(map.get("filelist"));
			if(!"".equals(files) && map.containsKey("filelist")){
				String[] filelist = files.split(",");
				for(String url : filelist){
					filemap.put("visiturl", url);
					filemap.put("type", 1);
					this.indexService.insertfile(filemap);
				}
			}
			
			//添加语音信息
			String sound = String.valueOf(map.get("sound"));
			if(!"".equals(map.get("sound")) && map.containsKey("sound")){
				filemap.put("visiturl", sound);
				filemap.put("type", 2);
				this.indexService.insertfile(filemap);
			}
			
			data.put("status",0);
			data.put("message","添加成功");
			return data;
			
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		
		
	}
	/**
	 * 审批请假（修改）
	 * 传入参数{"leaveid":"846aa3dec6f64728b04e7d4fab4431fa","userid":"690fb669ed4d40219964baad7783abd4","opinion":"balalala","result":1}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineLeave")
	@ResponseBody
	public Map<String, Object> examineLeave(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.officeService.updateLeave(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	/**
	 * 查询请示列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0}
	 * 传出参数{"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0,"requestlist":[{"content":"要要要","createtime":"2016-07-22 10:08:52","companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":"2016-07-22 10:09:26","reason":"我要一个奖励","status":0,"createid":"690fb669ed4d40219964baad7783abd4","isread":1,"urgentlevel":1,"examineuserid":"1","createname":"silence","requestid":"3c763b6d99cc4ef5a019c06c73ec93f2","examinename":"李语然"}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getRequestList")
	@ResponseBody
	public Map<String, Object> getRequestList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		PageHelper page = new PageHelper(request);
		int num=this.officeService.getRequestListNum(map);	
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getRequestList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("requestlist", datalist);
		data.put("status",0);
		data.put("pager", page.JSCateringPage().toString());
		return data;
	}
	/**
	 * 
	 * 导出请示列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportLogRequestList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogRequestList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/请示列表.xls");
		List<Map<String, Object>> list=this.officeService.getRequestList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "申请人","紧急程度","事由","申请时间","状态","审批结果"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> log:list){
        	
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a", log.get("createname")==null?"":log.get("createname"));
	        String family ="";
	        if(log.containsKey("urgentlevel")){
	        	 if(Integer.parseInt(log.get("urgentlevel").toString()) == 1){
	 	        	family="普通";
	 	        }else if(Integer.parseInt(log.get("urgentlevel").toString()) == 2){
	 	        	family="紧急";
	 	        }else if(Integer.parseInt(log.get("urgentlevel").toString()) == 3){
	 	        	family="非常紧急";
	 	        }
	        }
	       
	        datamap.put("b",  family);
	        datamap.put("c", log.get("reason")==null?"":log.get("reason"));
	        datamap.put("d",  log.get("createtime")==null?"":log.get("createtime"));
	        datamap.put("e", Integer.parseInt(log.get("status").toString())==0?"待处理":"已处理");
	        if(log.get("status") != ""  && !"".equals(log.get("status"))){
	        	String result="";
	        	if(log.containsKey("result")){
	        		 if(Integer.parseInt(log.get("result").toString()) == 1){
		        		 result="已同意";
		 	        }else if(Integer.parseInt(log.get("result").toString()) == 2){
		 	        	 result="已拒绝";
		 	        }
	        	}
	        	
	        	datamap.put("f", result);
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
		return "请示列表.xls";
	}
	
	/**
	 * 查询请示详情页面
	 * 传入参数{"requestid":"3c763b6d99cc4ef5a019c06c73ec93f2"}
	 * 传出参数{"message":"查询成功","status":0,"requestinfo":{"createtime":"2016-07-22 10:08:52","content":"要要要","companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":"2016-07-22 10:09:26","createid":"690fb669ed4d40219964baad7783abd4","status":0,"reason":"我要一个奖励","urgentlevel":1,"examineuserid":"1","createname":"silence","filelist":[{"createtime":"2016-07-22 10:08:53","companyid":"67702cc412264f4ea7d2c5f692070457","resourcetype":16,"fileid":"efa73e81c1794840b8269cc0d2f79b16","resourceid":"3c763b6d99cc4ef5a019c06c73ec93f2","type":1,"visiturl":"bulubulu.jpg","size":1200}],"requestid":"3c763b6d99cc4ef5a019c06c73ec93f2","examinename":"李语然"}}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getRequestInfo")
	@ResponseBody
	public Map<String, Object> getRequestInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			Map<String, Object> requestinfo=this.officeService.getRequestInfo(map);
			data.put("requestinfo", requestinfo);
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
	 * 请示（修改）
	 * 传入参数{"requestid":"3c763b6d99cc4ef5a019c06c73ec93f2","userid":"690fb669ed4d40219964baad7783abd4","opinion":"LALALA","result":1}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineRequest")
	@ResponseBody
	public Map<String, Object> examineRequest(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.officeService.updateRequest(map);
		
		try {

			Map<String,Object> askmap = this.officeService.getRequestInfo(map);
			String level = String.valueOf(map.get("level"));
			if("2".equals(level)){
				try {
					//推送信息
					String userid=askmap.get("createid")+"";
					String title=askmap.get("examinename") + "已经审批您的紧急请示,点击查看";
					String url="/oa/ask_detail.html?requestid="+map.get("requestid")+"&userid="+userid;
					JPushAndriodAndIosMessage(userid, title, url);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}else if("3".equals(level)){
				try {
					//推送信息
					String userid=askmap.get("createid")+"";
					String title=askmap.get("examinename") + "已经审批您的非常紧急请示,点击查看";
					String url="/oa/ask_detail.html?requestid="+map.get("requestid")+"&userid="+userid;
					JPushAndriodAndIosMessage(userid, title, url);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				String phone = String.valueOf(askmap.get("createphone"));
				String examinename = String.valueOf(askmap.get("createname"));
				String content = "的非常紧急请示";
				String remark = "被"+askmap.get("examinename")+"审核";
				SDKTestSendTemplateSMS.sendExamineUrgentMessage(phone, examinename, content, remark);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	
	/**
	 * 
	 * 导出请示详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportLogRequestDetail",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogRequestDetail(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/请示详情.xls");
		Map<String, Object> leaveinfo=this.officeService.getRequestInfo(map);
        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "请示人","申请时间","紧急程度","事由","具体内容","审批人","审批结果"};
        
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
        
	    datamap.put("a", leaveinfo.get("createname")==null?"":leaveinfo.get("createname"));
	    datamap.put("b",  leaveinfo.get("createtime")==null?"":leaveinfo.get("createtime"));
	   
	     String urgentlevel="";
	    if(leaveinfo.containsKey("urgentlevel")){
		        if(Integer.parseInt(leaveinfo.get("urgentlevel").toString()) == 1){
		        	urgentlevel="普通";
		        }else if(Integer.parseInt(leaveinfo.get("urgentlevel").toString()) == 2){
		        	urgentlevel="紧急";
		        }else if(Integer.parseInt(leaveinfo.get("urgentlevel").toString()) == 3){
		        	urgentlevel="非常紧急";
		        }
	    }
       	 datamap.put("c", urgentlevel);
 
	    
	    datamap.put("d", leaveinfo.get("reason")==null?"":leaveinfo.get("reason"));
	    datamap.put("f",  leaveinfo.get("content")==null?"":leaveinfo.get("content"));
	    datamap.put("g",  leaveinfo.get("examinename")==null?"":leaveinfo.get("examinename"));
	    if(leaveinfo.get("status") != ""  && !"".equals(leaveinfo.get("status"))){
		     	String result="";
		    	if(leaveinfo.containsKey("result")){
			       	 if(Integer.parseInt(leaveinfo.get("result").toString()) == 1){
			       		 	result="已同意";
				        }else if(Integer.parseInt(leaveinfo.get("result").toString()) == 2){
				        	result="已拒绝";
				        }
		    	}
	       	 datamap.put("h", result);
        	
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
		return "请示详情.xls";
	}
	/**
	 * 添加请示单
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","reason":"我要一个奖励","content":"要要要","examineuserid":1,"urgentlevel":1,"filelist":"{"filelist":[{"visiturl":"bulubulu.jpg","type":1,"size":1200}]}"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertRequest")
	@ResponseBody
	public Map<String, Object> insertRequest(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		String requestid="";
		try {
			requestid=this.officeService.insertRequest(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		try {
			Map<String,Object> filemap = new HashMap<String,Object>();
			filemap.put("companyid", map.get("companyid"));
			filemap.put("resourceid", requestid);
			filemap.put("resourcetype", 16);
			//添加添加图片的路径
			String files = String.valueOf(map.get("filelist"));
			if(!"".equals(files) && map.containsKey("filelist")){
				String[] filelist = files.split(",");
				for(String url : filelist){
					filemap.put("visiturl", url);
					filemap.put("type", 1);
					this.indexService.insertfile(filemap);
				}
			}
			
			//添加语音信息
			String sound = String.valueOf(map.get("sound"));
			if(!"".equals(map.get("sound")) && map.containsKey("sound")){
				filemap.put("visiturl", sound);
				filemap.put("type", 2);
				this.indexService.insertfile(filemap);
			}
			
			try {
				String level = String.valueOf(map.get("urgentlevel"));
				if("3".equals(level)){
					map.put("requestid", requestid);
					Map<String,Object> askmap = this.officeService.getRequestInfo(map);
					String phone = String.valueOf(askmap.get("examinephone"));
					String examinename = String.valueOf(askmap.get("examinename"));
					String createname = String.valueOf(askmap.get("createname"));
					String content = "一个非常紧急请示";
					SDKTestSendTemplateSMS.sendUrgentMessage(phone, examinename, createname, content);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			data.put("status",0);
			data.put("message","添加成功");
			return data;
			
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
	}
	
	/**
	 * 查询报销列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0}
	 * 传出参数{"expenselist":[{"detailprice":100,"updatetime":"0000-00-00 00:00:00","detailnum":1,"status":0,"isread":1,"examineuserid":"1","examinename":"李语然","createtime":"2016-07-22 09:54:32","companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","expenseid":"75a3c5bc30b545c2a76eaeb3521423a8","createname":"silence","expenseno":"BXD20160722094800001"}],"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getExpenseList")
	@ResponseBody
	public Map<String, Object> getExpenseList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		PageHelper page = new PageHelper(request);
		int num=this.officeService.getExpenseListNum(map);		
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getExpenseList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("expenselist", datalist);
		data.put("status",0);
		data.put("pager", page.JSCateringPage().toString());
		return data;
	}
	
	/**
	 * 查询报销详情页面
	 * 传入参数{"expenseid":"75a3c5bc30b545c2a76eaeb3521423a8"}
	 * 传出参数{"message":"查询成功","status":0,"expenseinfo":{"createtime":"2016-07-22 09:54:32","companyid":"67702cc412264f4ea7d2c5f692070457","detailprice":100,"updatetime":"0000-00-00 00:00:00","status":0,"createid":"690fb669ed4d40219964baad7783abd4","detailnum":1,"examineuserid":"1","expenseid":"75a3c5bc30b545c2a76eaeb3521423a8","expenseno":"BXD20160722094800001","createname":"silence","examinename":"李语然","expensedetaillist":[{"createtime":1469152472000,"detail":"明细明细","price":100,"createid":"690fb669ed4d40219964baad7783abd4","expenseid":"75a3c5bc30b545c2a76eaeb3521423a8","expensedetailid":"3b63f0eba0994d7eb2ddad1062dba744","type":"衣服"}]}}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getExpenseInfo")
	@ResponseBody
	public Map<String, Object> getExpenseInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			Map<String, Object> expenseinfo=this.officeService.getExpenseInfo(map);
			data.put("expenseinfo", expenseinfo);
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
	 * 审批报销（修改）
	 * 传入参数{"expenseid":"75a3c5bc30b545c2a76eaeb3521423a8","userid":1,"opinion":"bulubulu","result":1}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineExpense")
	@ResponseBody
	public Map<String, Object> examineExpense(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.officeService.updateExpense(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	/**
	 * 
	 * 导出报销列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportExpenseList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogExpenseList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/报销列表.xls");
		List<Map<String, Object>> list=	this.officeService.getExpenseList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = {"采购(入库)单编号","采购人","申请时间","项数","金额","状态","审批结果"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> log:list){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a",log.get("expenseno")==null?"":""+log.get("expenseno"));
	        datamap.put("b",log.get("createname")==null?"":""+log.get("createname"));
	        datamap.put("c",  log.get("createtime")==null?"":log.get("createtime"));
	        datamap.put("d",  log.get("detailnum")==null?"":log.get("detailnum")+"项");
	        datamap.put("e",  log.get("detailprice")==null?"":"￥"+log.get("detailprice"));
	        datamap.put("f", Integer.parseInt(log.get("status").toString())==0?"待处理":"已处理");
	        if(log.containsKey("result")){
	        	String row ="";
	        	if(Integer.parseInt(log.get("result").toString())==1){
	        		row="已同意";
	        	}else if(Integer.parseInt(log.get("result").toString())==2){
	        		row="已拒绝";
	        	}
	        	datamap.put("g",row);
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
		return "报销列表.xls";
	}
	
	/**
	 * 
	 * 导出报销详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportExpenseDetail",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogExpenseDetail(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/报销详情.xls");
		
		Map<String, Object> mapLog=this.officeService.getExpenseInfo(map);
        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>(); 
        String[] headers = {"报销单编号","类别","明细","金额","项数","合计","申请人","申请时间","状态","审批人","审批结果","审批意见"};
        
        
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        
        
        List<Map<String, Object>> list = (List<Map<String, Object>>) mapLog.get("expensedetaillist");
        
        for(Map<String, Object> log:list){
        	TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a",mapLog.get("expenseno")==null?"":""+mapLog.get("expenseno"));
	        datamap.put("b",log.get("type")==null?"":""+log.get("type"));
	        datamap.put("c",log.get("detail")==null?"":log.get("detail"));
	        datamap.put("d",log.get("price")==null?"":log.get("price"));
	        datamap.put("e",mapLog.get("detailnum")==null?"":mapLog.get("detailnum")+"项");
	        datamap.put("f",mapLog.get("detailprice")==null?"":"￥"+mapLog.get("detailprice"));
	        datamap.put("g",mapLog.get("createname")==null?"":""+mapLog.get("createname"));
	        datamap.put("h",mapLog.get("createtime")==null?"":mapLog.get("createtime"));
	        datamap.put("i",Integer.parseInt(mapLog.get("status").toString())==0?"待处理":"已处理");
	        datamap.put("j",mapLog.get("examinename")==null?"":""+mapLog.get("examinename"));
	        if(mapLog.containsKey("result")){
	        	String row ="";
	        	if(Integer.parseInt(mapLog.get("result").toString())==1){
	        		row="已同意";
	        	}else if(Integer.parseInt(mapLog.get("result").toString())==2){
	        		row="已拒绝";
	        	}
	        	datamap.put("k",row);
	        }
	        datamap.put("l",mapLog.get("opinion")==null?"":""+mapLog.get("opinion"));
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
		return "报销详情.xls";
	}
	
	/**
	 * 初始化添加报销（获取报销单编号）
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数{"message":"成功","status":0,"expenseno":"BXD20160722094800001"}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initInsertExpense")
	@ResponseBody
	public Map<String, Object> initInsertExpense(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("companyid") && !"".equals(map.get("companyid"))){
			int ordernum=this.officeService.getExpenseNumByCompany(map);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmm");
			data.put("status", 0);
			data.put("expenseno","BXD"+sdf.format(new Date())+String.format("%05d",(ordernum+1)));
			data.put("message", "成功");
		}else{
			data.put("status", 1);
			data.put("message", "传入参数错误");
		}
		return data;
	}
	/**
	 * 添加报销单
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","expenseno":"BXD20160722094800001","examineuserid":1,"createid":"690fb669ed4d40219964baad7783abd4","detailprice":100,"detailnum":1,"detaillist":"{"detaillist":[{"type":"衣服","price":100,"detail":"明细明细"}]}"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertExpense")
	@ResponseBody
	public Map<String, Object> insertExpense(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		String expenseid="";
		try {
			expenseid=this.officeService.insertExpense(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		try {
			//添加到报销明细表
			JSONObject json=JSONObject.fromObject(map.get("detaillist")+"");
			List<Map<String, Object>> detaillist=(List<Map<String, Object>>)json.get("detaillist");
			for(Map<String, Object> detail:detaillist){
				detail.put("createid", map.get("createid"));
				detail.put("expenseid", expenseid);
				this.officeService.insertExpenseDetail(detail);
			}
			
			data.put("status",0);
			data.put("message","添加成功");
			return data;
			
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		
		
	}
	/**
	 * 添加任务
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","title":"标题1","content":"内容内容","endtime":"2016-07-29 22:21:21","examineuserid":1,"userlist":"{"userlist":[{"assistuserid":"2"}]}","filelist":"{"filelist":[{"visiturl":"2.jpg","type":1,"size":1200}]}"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertTask")
	@ResponseBody
	public Map<String, Object> insertTask(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		String taskid="";
		try {
			taskid=this.officeService.insertTask(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		try {
			//添加到协办人员表
			JSONObject json=JSONObject.fromObject(map.get("userlist")+"");
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String, Object> user:userlist){
				user.put("companyid", map.get("companyid"));
				user.put("taskid", taskid);
				user.put("createid", map.get("createid"));
				user.put("assistuserid", user.get("userid"));
				this.officeService.insertTaskAssist(user);
			}
			
			Map<String,Object> filemap = new HashMap<String,Object>();
			filemap.put("companyid", map.get("companyid"));
			filemap.put("resourceid", taskid);
			filemap.put("resourcetype", 19);
			//添加添加图片的路径
			String files = String.valueOf(map.get("filelist"));
			if(!"".equals(files) && map.containsKey("filelist")){
				String[] filelist = files.split(",");
				for(String url : filelist){
					filemap.put("visiturl", url);
					filemap.put("type", 1);
					this.indexService.insertfile(filemap);
				}
			}
			
			data.put("status",0);
			data.put("message","添加成功");
			return data;
			
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		
		
	}
	
	/**
	 * 查询任务 列表 传入 nooverdue=1 查询 未过期的    传入 isoverdue=1 查询已过期的（两者不可同时传入）
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0,"isoverdue":1}
	 * 传出参数{"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0,"tasklist":[{"createtime":1469002015000,"content":"任务内容","endtime":1469888481000,"companyid":"67702cc412264f4ea7d2c5f692070457","title":"任务标题","createhour":"16:06","forwarduserid":"15b5296695d94447a8380b2dd80d2e06","status":0,"createid":"690fb669ed4d40219964baad7783abd4","taskid":"c4df74e76e2d4927bf7bbb4b32d3b937","examineuserid":"1","createdate":"2016-07-20","createname":"silence","assistlist":[{"realname":"李大炮"}],"examinename":"李语然"}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getTaskList")
	@ResponseBody
	public Map<String, Object> getTaskList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageHelper page=new PageHelper(request);
		int num=this.officeService.getTaskListNum(map);		
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getTaskList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("tasklist", datalist);
		data.put("status",0);
		data.put("pager",page.JSCateringPage().toString());
		return data;
	}
	
	/**
	 * 查询任务表详情页面
	 * 传入参数{"taskid":"000c8a6c252249ff9e31df34e1ec1a95"}
	 * 传出参数{"message":"查询成功","taskinfo":{"content":"内容内容","endtime":"2016-07-29 22:21:21","createtime":"2016-07-22 09:18:38","companyid":"67702cc412264f4ea7d2c5f692070457","title":"标题1","status":0,"createid":"690fb669ed4d40219964baad7783abd4","taskid":"000c8a6c252249ff9e31df34e1ec1a95","examineuserid":"1","createname":"silence","assistlist":[{"realname":"李大炮"}],"examinename":"李语然"},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getTaskInfo")
	@ResponseBody
	public Map<String, Object> getTaskInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			//备用金表详情
			Map<String, Object> taskinfo=this.officeService.getTaskInfo(map);
			data.put("taskinfo", taskinfo);
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
	 * 完成任务 （修改）
	 * 传入参数{"taskid":"000c8a6c252249ff9e31df34e1ec1a95","userid":"690fb669ed4d40219964baad7783abd4","opinion":"lalalla"}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineTask")
	@ResponseBody
	public Map<String, Object> examineTask(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.officeService.updateTask(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	
	/**
	 * 
	 * 导出任务列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportTaskList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogTaskList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/任务列表.xls");
		List<Map<String, Object>> list=	this.officeService.getTaskList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = {"发布人","责任人","协办人","截至时间","状态"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> log:list){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a",log.get("createname")==null?"":""+log.get("createname"));
	        datamap.put("b",log.get("examinename")==null?"":""+log.get("examinename"));
	    	String assi = "";
	        if(log.containsKey("assistlist")){
	        
	        	List<Map<String, Object>> assistlist = (List<Map<String, Object>>) log.get("assistlist");
	        	int index = 0 ;
	        	for (int i = 0; i < assistlist.size(); i++) {
	        		if(index>0){
						assi+=",";
					}
					if(assistlist.get(i)!=null){
						index++;
						assi+=assistlist.get(i).get("realname");
					}
					
				}
	       
	        }
	        datamap.put("c",  assi);
	        datamap.put("d",  log.get("endtime")==null?"":log.get("endtime"));
	        datamap.put("e", Integer.parseInt(log.get("status").toString())==0?"待处理":"已处理");
	        if(log.containsKey("result")){
	        	String row ="";
	        	if(Integer.parseInt(log.get("result").toString())==1){
	        		row="已同意";
	        	}else if(Integer.parseInt(log.get("result").toString())==2){
	        		row="已拒绝";
	        	}
	        	datamap.put("g",row);
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
		return "任务列表.xls";
	}
	
	/**
	 * 
	 * 导出任务详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportTaskDetail",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogTaskDetail(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/任务详情.xls");
		
		Map<String, Object> mapLog=this.officeService.getTaskInfo(map);
        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>(); 
        String[] headers = {"主题","截至时间","内容","任务下达人","责任人","协办人","状态"};
        
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
        
        datamap.put("a",mapLog.get("title")==null?"":""+mapLog.get("title"));
        datamap.put("b",  mapLog.get("endtime")==null?"":mapLog.get("endtime"));
        datamap.put("c",mapLog.get("content")==null?"":""+mapLog.get("content"));
        datamap.put("d",  mapLog.get("createname")==null?"":mapLog.get("createname"));
        datamap.put("e",  mapLog.get("examinename")==null?"":mapLog.get("examinename"));
    	String assi = "";
	    if(mapLog.containsKey("assistlist")){    
        	List<Map<String, Object>> assistlist = (List<Map<String, Object>>) mapLog.get("assistlist");
        	int index = 0 ;
        	for (int i = 0; i < assistlist.size(); i++) {
        		if(index>0){
					assi+=",";
				}
				if(assistlist.get(i)!=null){
					index++;
					assi+=assistlist.get(i).get("realname");
				}
				
			}
         	
	    }
	    datamap.put("f",  assi);
        datamap.put("g", Integer.parseInt(mapLog.get("status").toString())==0?"待处理":"已处理");
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
		return "任务详情.xls";
	}
	
	/**
	 * 荣誉榜
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","starttime":"2016-07-10 08:32:12","endtime":"2016-07-30 10:10:21"}
	 * 传出参数{"userlist":[{"createtime":1469000872000,"rewardid":"1","rewardresult":"口头嘉奖","realname":"李语然"}],"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getUserRewardList")
	@ResponseBody
	public Map<String, Object> getUserRewardList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageHelper page=new PageHelper(request);
		int num=this.officeService.getUserRewardListNum(map);		
		page.setTotalCount(num);	
		page.initPage(map);
		
		List<Map<String, Object>> datalist=this.officeService.getUserRewardList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("userlist", datalist);
		data.put("status",0);
		data.put("pager", page.JSCateringPage().toString());
		return data;
	}
	/**
	 * 
	 * 导出荣誉列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportUserRewardList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportUserRewardList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/荣誉榜.xls");
		List<Map<String, Object>> list=	this.officeService.getUserRewardList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = {"姓名","奖励结果","获奖时间"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> log:list){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a",log.get("realname")==null?"":""+log.get("realname"));
	        datamap.put("b",log.get("rewardresult")==null?"":""+log.get("rewardresult"));	
	        datamap.put("c",  log.get("createtime")==null?"":log.get("createtime"));
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
		return "荣誉榜.xls";
	}
	/**
	 * 员工关怀  直接进默认当月
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","monthnum":7}
	 * 传出参数{"userlist":[{"position":"店长","birthday":"7月19日","userid":"1","organizelist":[{"createtime":1468459014000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459058000,"datacode":"001","address":"浦东新区","organizeid":"1","organizename":"上海紫痕软件有限公司","type":1}],"realname":"李语然"}],"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0,"month":"7"}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getUserBirthdayList")
	@ResponseBody
	public Map<String, Object> getUserBirthdayList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		PageHelper page=new PageHelper(request);
		int num=this.officeService.getUserBirthdayListNum(map);		
		page.setTotalCount(num);	
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getUserBirthdayList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("userlist", datalist);
		data.put("monthnum", map.get("monthnum"));
		data.put("status",0);
		data.put("pager", page.JSCateringPage().toString());
		return data;
	}
	
	/**
	 * 
	 * 导出员工关怀列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportUserBirthdayList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportUserBirthdayList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/员工关怀.xls");
		List<Map<String, Object>> list=	this.officeService.getUserBirthdayList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = {"姓名","生日","职务","所属部门"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> log:list){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a",log.get("realname")==null?"":""+log.get("realname"));
	        datamap.put("b",log.get("birthday")==null?"":""+log.get("birthday"));	
	        datamap.put("c",  log.get("position")==null?"":log.get("position"));
	        datamap.put("d",  log.get("organizename")==null?"":log.get("organizename"));
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
		return "员工关怀.xls";
	}
	/**
	 * 
	 * 企业简报模块列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数{"message":"查询成功","modulelist":[{"createtime":1468983040000,"companyid":"67702cc412264f4ea7d2c5f692070457","modulename":"公司新闻","moduleimage":"1.jpg","updatetime":1468983120000,"createid":"690fb669ed4d40219964baad7783abd4","moduleid":"fdd61606a640459081bce3d02dd8e01a"}],"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getCompanyModuleList")
	@ResponseBody
	public Map<String, Object> getCompanyModuleList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		List<Map<String, Object>> modulelist=this.officeService.getCompanyModuleList(map);
		data.put("modulelist", modulelist);
		data.put("status", 0);
 		data.put("message", "查询成功");
		return data;
	}
	/**
	 * 简报列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4"}
	 * 传出参数{"briefList":[{"content":"哈哈哈哈哈哈哈哈哈哈哈","createtime":1468985819000,"title":"测试简报","createid":"690fb669ed4d40219964baad7783abd4","isread":0,"moduleid":"7205908da0924ecb96fa0e3e5659de04","briefid":"3ba46512f04449eea44ed9eb7817b98d"}],"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getBriefList")
	@ResponseBody
	public Map<String, Object> getBriefList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		PageHelper page=new PageHelper(request);
		int num=this.officeService.getBriefListNum(map);	
		page.setTotalCount(num);	
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getBriefList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("briefList", datalist);
		data.put("status",0);
		data.put("pager", page.JSCateringPage().toString());
		return data;
	}
	/**
	 * 简报详情
	 * 传入参数{"briefid":"3ba46512f04449eea44ed9eb7817b98d"}
	 * 传出参数{"message":"查询成功","status":0,"briefInfo":{"content":"哈哈哈哈哈哈哈哈哈哈哈","createtime":1468985819000,"title":"测试简报","createid":"690fb669ed4d40219964baad7783abd4","moduleid":"7205908da0924ecb96fa0e3e5659de04","rangelist":[{"createtime":1468985828000,"rangename":"李语然","resourcetype":"7","resourceid":"3ba46512f04449eea44ed9eb7817b98d"},{"createtime":1468985865000,"rangename":"浦东新区分公司","resourcetype":"7","resourceid":"3ba46512f04449eea44ed9eb7817b98d"}],"briefid":"3ba46512f04449eea44ed9eb7817b98d"}}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getBriefInfo")
	@ResponseBody
	public Map<String, Object> getBriefInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		Map<String, Object> briefInfo=this.officeService.getBriefInfo(map);
		data.put("briefInfo", briefInfo);
		data.put("status",0);
		data.put("message","查询成功");
		return data;
	}
	
	/**
	 * 
	 * 简报列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportBriefList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportBriefList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/简报列表.xls");
		List<Map<String, Object>> list=	this.officeService.getBriefList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = {"标题","发布时间","发布内容"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> log:list){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a",log.get("title")==null?"":""+log.get("title"));
	        datamap.put("b",log.get("createtime")==null?"":""+log.get("createtime"));	
	        datamap.put("c",  log.get("content")==null?"":log.get("content"));

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
		return "简报列表.xls";
	}
	/**
	 * 查询备用金表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0}
	 * 传出参数{"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0,"reserveamountlist":[{"reserveamountid":"40f352b2c0434fea89ba718f48d083b1","updatetime":1469001500000,"status":0,"remark":"麻烦了","reason":"生病需要资金","urgentlevel":2,"examineuserid":"1","createdate":"2016-07-20","returntime":1471737600000,"amount":500000,"createtime":1469001215000,"companyid":"67702cc412264f4ea7d2c5f692070457","createhour":"15:53","forwarduserid":"7573e593b2474e44bbc53c717c20305a","createid":"690fb669ed4d40219964baad7783abd4","usetime":1469406261000}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReserveAmountList")
	@ResponseBody
	public Map<String, Object> getReserveAmountList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		PageHelper page = new PageHelper(request);
		int num=this.officeService.getReserveAmountListNum(map);		
		page.setTotalCount(num);	
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getReserveAmountList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("datalist", datalist);
		data.put("status",0);
		data.put("pager",page.JSCateringPage().toString());
		return data;
	}
	
	/**
	 * 查询备用金表详情页面
	 * 传入参数{"reserveamountid":"40f352b2c0434fea89ba718f48d083b1"}
	 * 传出参数{"message":"查询成功","status":0,"reserveAmountInfo":{"reserveamountid":"40f352b2c0434fea89ba718f48d083b1","updatetime":1469001500000,"status":0,"remark":"麻烦了","reason":"生病需要资金","urgentlevel":2,"examineuserid":"1","returntime":1471737600000,"examinename":"李语然","amount":500000,"createtime":1469001503000,"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","usetime":1469406261000,"createname":"silence"}}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReserveAmountInfo")
	@ResponseBody
	public Map<String, Object> getReserveAmountInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			//备用金表详情
			Map<String, Object> reserveamountinfo=this.officeService.getReserveAmountInfo(map);
			data.put("reserveamountinfo", reserveamountinfo);
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
	 * 审核备用金  同意或者拒绝
	 * 传入参数{"userid":1,"reserveamountid":"9d54541939f6455c8253a4df4f77328b","opinion":"ohmygod","result":1}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineReserveAmount")
	@ResponseBody
	public Map<String, Object> examineReserveAmount(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.officeService.updateReserveAmount(map);
		

		String level = String.valueOf(map.get("level"));
		if(!"1".equals(level)){
			try {
				String jinji = "紧急";
				if("3".equals(level)){
					jinji = "非常紧急";
				}
				Map<String,Object> reservemap = this.officeService.getReserveAmountInfo(map);
				String userid=reservemap.get("createid")+"";
				String title=reservemap.get("examinename")+ "已经审核您的"+jinji+"备用金申请,请查看";
				String url="/oa/reserve_detail.html?reserveamountid="+map.get("reserveamountid")+"&userid="+userid;
				JPushAndriodAndIosMessage(userid, title, url);
				
				if("3".equals(level)){
					String phone = String.valueOf(reservemap.get("createphone"));
					String examinename = String.valueOf(reservemap.get("createname"));
					String createname = String.valueOf(reservemap.get("examinename"))+"审核您的非常紧急备用金申请，已";
					String content = "审核结果";
					SDKTestSendTemplateSMS.sendUrgentMessage(phone, examinename, createname, content);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	/**
	 * 
	 * 导出备用金列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportReserveList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportReserveList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/备用金列表.xls");
		List<Map<String, Object>> list=	this.officeService.getReserveAmountList(map);

	    ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "申请人","紧急程度","事由","申请时间","状态","审批结果"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> log:list){
        	
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a", log.get("createname")==null?"":log.get("createname"));
	        String family ="";
	        if(log.containsKey("urgentlevel")){
	        	 if(Integer.parseInt(log.get("urgentlevel").toString()) == 1){
	 	        	family="普通";
	 	        }else if(Integer.parseInt(log.get("urgentlevel").toString()) == 2){
	 	        	family="紧急";
	 	        }else if(Integer.parseInt(log.get("urgentlevel").toString()) == 3){
	 	        	family="非常紧急";
	 	        }
	        }
	       
	        datamap.put("b",  family);
	        datamap.put("c", log.get("reason")==null?"":log.get("reason"));
	        datamap.put("d",  log.get("createtime")==null?"":log.get("createtime"));
	        datamap.put("e", Integer.parseInt(log.get("status").toString())==0?"待处理":"已处理");
	        if(log.get("status") != ""  && !"".equals(log.get("status"))){
	        	String result="";
	        	if(log.containsKey("result")){
	        		 if(Integer.parseInt(log.get("result").toString()) == 1){
		        		 result="已同意";
		 	        }else if(Integer.parseInt(log.get("result").toString()) == 2){
		 	        	 result="已拒绝";
		 	        }
	        	}
	        	
	        	datamap.put("f", result);
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
		return "备用金列表.xls";
	}
	/**
	 * 
	 * 导出备用金详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportReserveDetail",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportReserveDetail(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/备用金详情.xls");
		Map<String, Object> leaveinfo=this.officeService.getReserveAmountInfo(map);
        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = {"请示人","请示时间","事由","紧急程度","申请金额","使用日期","归还日期","备注","审批人","审批结果","审批意见"};
        
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
        
	    datamap.put("a", leaveinfo.get("createname")==null?"":leaveinfo.get("createname"));
	    datamap.put("b",  leaveinfo.get("createtime")==null?"":leaveinfo.get("createtime"));
	    datamap.put("c", leaveinfo.get("reason")==null?"":leaveinfo.get("reason"));

	    String urgentlevel="";
	    if(leaveinfo.containsKey("urgentlevel")){
		        if(Integer.parseInt(leaveinfo.get("urgentlevel").toString()) == 1){
		        	urgentlevel="普通";
		        }else if(Integer.parseInt(leaveinfo.get("urgentlevel").toString()) == 2){
		        	urgentlevel="紧急";
		        }else if(Integer.parseInt(leaveinfo.get("urgentlevel").toString()) == 3){
		        	urgentlevel="非常紧急";
		        }
	    }
       	datamap.put("d", urgentlevel);
 	    datamap.put("e", leaveinfo.get("amount")==null?"":"￥"+leaveinfo.get("amount"));
	    datamap.put("f",  leaveinfo.get("usetime")==null?"":leaveinfo.get("usetime"));
 	    datamap.put("g",  leaveinfo.get("returntime")==null?"":leaveinfo.get("returntime"));
	    datamap.put("h", leaveinfo.get("remark")==null?"":leaveinfo.get("remark"));
	    datamap.put("i",  leaveinfo.get("examinename")==null?"":leaveinfo.get("examinename"));
	    if(leaveinfo.get("status") != ""  && !"".equals(leaveinfo.get("status"))){
		     	String result="";
		    	if(leaveinfo.containsKey("result")){
			       	 if(Integer.parseInt(leaveinfo.get("result").toString()) == 1){
			       		 	result="已同意";
				        }else if(Integer.parseInt(leaveinfo.get("result").toString()) == 2){
				        	result="已拒绝";
				        }
		    	}
	       	 datamap.put("j", result);
        	
        }
	    datamap.put("k",  leaveinfo.get("opinion")==null?"":leaveinfo.get("opinion"));
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
		return "备用金详情.xls";
	}
	/**
	 * 添加备用金表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","reason":"生病需要资金","urgentlevel":2,"amount":500000,"usetime":"2016-07-25 08:24:21","returntime":"2016-08-21 08:00:00","remark":"麻烦了","examineuserid":1,"createid":"690fb669ed4d40219964baad7783abd4"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertReserveAmount")
	@ResponseBody
	public Map<String, Object> insertReserveAmount(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			String reserveamountid = this.officeService.insertReserveAmount(map);
			
			String level = String.valueOf(map.get("urgentlevel"));
			if("3".equals(level)){
				try {
					map.put("reserveamountid", reserveamountid);
					Map<String,Object> reservemap = this.officeService.getReserveAmountInfo(map);
					String phone = String.valueOf(reservemap.get("examinephone"));
					String examinename = String.valueOf(reservemap.get("examinename"));
					String createname = String.valueOf(reservemap.get("createname"));
					String content = "非常紧急备用金申请";
					SDKTestSendTemplateSMS.sendUrgentMessage(phone, examinename, createname, content);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
			data.put("status",0);
			data.put("message","添加成功");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			
		}
		return data;
	}
	/**
	 * 
	 * 企业云盘模块列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数{"message":"查询成功","modulelist":[{"createtime":1468995146000,"companyid":"67702cc412264f4ea7d2c5f692070457","modulename":"浦东店","moduleimage":"2.jpg","updatetime":1468995527000,"createid":"690fb669ed4d40219964baad7783abd4","moduleid":"682d94bbccf24c8bab4a19fc24e87381"}],"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getCompanyCloudModuleList")
	@ResponseBody
	public Map<String, Object> getCompanyCloudModuleList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		List<Map<String, Object>> modulelist=this.officeService.getCompanyCloudModuleList(map);
		data.put("modulelist", modulelist);
		data.put("status", 0);
 		data.put("message", "查询成功");
		return data;
	}
	
	/**
	 * 企业云盘列表文件/文件夹  查询
	 * 传入参数{"filetype":1,"moduleid":"682d94bbccf24c8bab4a19fc24e87381","filename":"jpg","parentid":1}
	 * 传出参数{"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0,"companycloudlist":[{"createtime":1468996983000,"updatetime":1468996985000,"createid":"1","fileurl":"www.baidu.com","filename":"2.jpg","cloudid":"1","filetype":1,"parentid":"1","fileid":"2","memory":5}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getCompanyCloudList")
	@ResponseBody
	public Map<String, Object> getCompanyCloudList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(!map.containsKey("parentid") || "".equals(map.get("parentid"))){
			map.put("isparentid", 1);
		}
		PageHelper page=new PageHelper(request);
		int num=this.officeService.getCompanyCloudListNum(map);		
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getCompanyCloudList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("datalist", datalist);
		data.put("status",0);
		data.put("pager",page.JSCateringPage().toString());
		return data;
	}
	/**
	 * 查询文件详情
	 * 传入参数{"reserveamountid":"40f352b2c0434fea89ba718f48d083b1"}
	 * 传出参数{"message":"查询成功","status":0,"reserveAmountInfo":{"reserveamountid":"40f352b2c0434fea89ba718f48d083b1","updatetime":1469001500000,"status":0,"remark":"麻烦了","reason":"生病需要资金","urgentlevel":2,"examineuserid":"1","returntime":1471737600000,"examinename":"李语然","amount":500000,"createtime":1469001503000,"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","usetime":1469406261000,"createname":"silence"}}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getCompanyCloudFileInfo")
	@ResponseBody
	public Map<String, Object> getCompanyCloudDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			//详情
			Map<String, Object> reserveamountinfo=this.officeService.getCompanyCloudFileInfo(map);
			data.put("datamap", reserveamountinfo);
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
	 * 餐饮大师云盘列表文件/文件夹  查询
	 * 传入参数{"type":1,"filetype":1,"parentid":1}
	 * 传出参数{"systemcloudlist":[{"createtime":1468994060000,"updatetime":1468994063000,"createid":"1","filename":"2.jpg","filetype":1,"parentid":"1","fileid":"2","type":1,"memory":10}],"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getSystemCloudList")
	@ResponseBody
	public Map<String, Object> getSystemCloudList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(!map.containsKey("parentid") || "".equals(map.get("parentid"))){
			map.put("isparentid", 1);
		}
		PageHelper page = new PageHelper(request);
		int num=this.officeService.getSystemCloudListNum(map);		
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getSystemCloudList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("datalist", datalist);
		data.put("status",0);
		data.put("pager",page.JSCateringPage().toString());
		return data;
	}


	
}
