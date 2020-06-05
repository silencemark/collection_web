package com.collection.controller.managebackstage;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.controller.userbackstage.UserIndexController.ExportExcel;
import com.collection.service.CompanyService;
import com.collection.service.IndexService;
import com.collection.service.SystemService;
import com.collection.service.UserInfoService;
import com.collection.util.PageHelper;
import com.collection.util.UserUtil;

/**
 * 使用方后台管理企业简报
 * @author silence
 *
 */
@Controller
@RequestMapping("/managebackstage")
public class ManageSystemController extends BaseController{
	private transient static Log log = LogFactory.getLog(ManageSystemController.class);
	@Resource private SystemService systemService;
	
	@Resource private CompanyService companyService;
	
	@Resource private UserInfoService userInfoService;
	
	@Resource private IndexService indexService;
	
	/**
	 * 
	 * 查询管理日志
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/systemIndex")
	public String systemIndex(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		return "/managebackstage/system/system";
	}
	
	/**
	 * 
	 * 查询管理日志
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getLogList")
	public String getLogList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		map.put("type", 1);
		
		//查询模块列表
		List<Map<String, Object>> modellist=this.companyService.getLogModelList(map);
		model.addAttribute("modellist", modellist);
		//初始化分页
		PageHelper pageHelper = new PageHelper(request);
		pageHelper.initPage(map);
		List<Map<String, Object>> loglist=this.systemService.getLogList(map);
		int num=this.systemService.getLogListNum(map);
		pageHelper.setTotalCount(num);
		model.addAttribute("loglist", loglist);
		model.addAttribute("pager", pageHelper.cateringPage().toString());
		model.addAttribute("map", map);
		
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
        this.insertManageLog("", 1, "系统设置", "查看管理日志。", userInfo.get("userid")+"");
		return "/managebackstage/system/system_managelog";
	}
	/**
	 * 
	 * 导出管理日志
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportLogList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/管理员日志列表.xls");
		map.put("type", 1);
		List<Map<String, Object>> loglist=this.systemService.getLogList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "操作人员", "操作描述", "操作时间"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> log:loglist){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a", log.get("realname")==null?"":log.get("realname"));
	        datamap.put("b", "【"+log.get("model")+"】"+log.get("content"));
	        datamap.put("c", log.get("createtime")==null?"":log.get("createtime"));
	        dataset.add(datamap);
        }
        try  
        {
            OutputStream out = new FileOutputStream(importurl);  
            ex.exportExcel(headers, dataset, out);
            out.close();  
            System.out.println("excel导出成功！");
            
            Map<String, Object> userInfo=UserUtil.getSystemUser(request);
            this.insertManageLog("", 1, "系统设置", "导出了管理日志信息。", userInfo.get("userid")+"");
        }  
        catch (FileNotFoundException e)  
        {
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }
		return "管理员日志列表.xls";
	}
	

	/**
	 * 
	 * 每日报表设置
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/systemDailyReport")
	public String systemDailyReport(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		List<Map<String, Object>> datatypelist=this.systemService.getEverydayDataList(map);
		model.addAttribute("datatypelist", datatypelist);
		return "/managebackstage/system/system_dailyreport";
	}
	/**
	 * 
	 * 添加每日报表字典
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertDailyReportData")
	@ResponseBody
	public Map<String, Object> insertDailyReportData(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		
		insertManageLog("",1,"系统设置-每日报表","新增字典"+map.get("dataname"),userInfo.get("userid")+"");
		if(userInfo!=null){
			map.put("createid", userInfo.get("userid"));
		}
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			this.systemService.insertDailyReportData(map);
			data.put("status", 0);
			data.put("message", "添加成功");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加失败,请稍后再试");
		}
		return data;
	}
	
	/**
	 * 
	 * 添加每日报表字典
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updateDailyReportData")
	@ResponseBody
	public Map<String, Object> updateDailyReportData(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);

		insertManageLog("",1,"系统设置-每日报表","删除字典"+map.get("dataname"),userInfo.get("userid")+"");
		
		if(userInfo!=null){
			map.put("updateid", userInfo.get("userid"));
		}
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			this.systemService.updateDailyReportData(map);
			data.put("status", 0);
			data.put("message", "操作成功");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "操作失败,请稍后再试");
		}
		return data;
	}
	
	
}
