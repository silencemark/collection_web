package com.collection.controller.userbackstage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.service.IndexService;
import com.collection.service.worksheet.WorkSheetInspectService;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;
import com.collection.util.UserUtil;


@Controller
@RequestMapping(value="/userbackstage")
public class KitchenCheckController extends BaseController{

	@Resource WorkSheetInspectService workSheetInspectService;
	@Resource IndexService indexService;
	
	
	/**
	 * 进入厨房检查页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoKitchenCheckPage")
	public String intoKitchenCheckPage(@RequestParam Map<String,Object> map , HttpServletRequest request){
	
//		try {
//			PageScroll page = new PageScroll();
//			int count = this.workSheetInspectService.getInspectTemplateCount(map);
//			page.setTotalRecords(count);
//			page.initPage(map);
//			List<Map<String,Object>> list = this.workSheetInspectService.getInspectTemplateList(map);
//			request.setAttribute("kitchenlist", list);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		Map<String, Object> userinfo=UserUtil.getUser(request);
		map.put("managerole", userinfo.get("managerole"));
		if(map.containsKey("type") && !"".equals(map.get("type"))){
			String type = String.valueOf(map.get("type"));
			if("1".equals(type)){
				map.put("workname", "离店检查");
				map.put("typeid", 11);
			}else if("2".equals(type)){
				map.put("workname", "厨房检查");
				map.put("typeid", 9);
			}else if("3".equals(type)){
				map.put("workname", "餐前检查");
				map.put("typeid", 10);
			}
		}
		request.setAttribute("map", map);
		request.setAttribute("user", getUserInfo(request));
		request.setAttribute("userinfo", userinfo);
		return "/userbackstage/worksheet/worksheet_kitchencheck";
	}
	
	/**
	 * 查询检查模板信息
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getInspectTemplateList")
	public Map<String,Object> getInspectTemplateList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			map.put("companyid", getUserInfo(request).get("companyid"));
			if(map.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
				Map<String,Object> orgmap = this.workSheetInspectService.getOrganizeDataCodeInfo(map);
				map.put("datacode", orgmap.get("datacode"));
			}
			PageHelper page = new PageHelper(request);
			int count = this.workSheetInspectService.getInspectTemplateCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.workSheetInspectService.getInspectTemplateList(map);
			
			if(list != null && list.size() > 0){
				resultMap.put("status", 0);
				resultMap.put("templatelist", list);
			}else{
				resultMap.put("status", 1);
				resultMap.put("templatelist", "");
			}
			resultMap.put("pager", page.JSCateringPage().toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("templatelist", "");
		}
		
		return resultMap;
	}
	
	/**
	 * 修改检查模板信息
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateInspectTemplateInfo")
	public Map<String,Object> updateInspectTemplateInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			this.workSheetInspectService.updateInspectTemplate(map);
			String content = "";
			String module = "";
			String type = String.valueOf(map.get("type"));
			if("1".equals(type)){
				module = "离店检查模板";
				content = "刪除了一个离店检查模板";
			}else if("2".equals(type)){
				module = "厨房检查模板";
				content = "刪除了一个厨房检查模板";
			}else if("3".equals(type)){
				module = "餐前检查模板";
				content = "刪除了一个餐前检查模板";
			}
			this.insertManageLog(getUserInfo(request).get("companyid")+"", 2, module, content, getUserInfo(request).get("userid")+"");
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	
	/***
	 * 添加检查模板信息
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertInspectTemplateInfo")
	public Map<String,Object> insertInspectTemplateInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			Map<String, Object> userinfo=UserUtil.getUser(request);
			
			Map<String,Object> tempmap = new HashMap<String,Object>();
			tempmap.put("companyid", userinfo.get("companyid"));
			tempmap.put("templatename", map.get("templatename"));
			tempmap.put("type", map.get("type"));
			tempmap.put("createid", userinfo.get("userid"));
			tempmap.put("organizeid", map.get("organizeid"));
			String templateid = this.workSheetInspectService.insertInspectTemplate(tempmap);
			
			JSONObject prolistjson=JSONObject.fromObject(map.get("prolist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> prolist=(List<Map<String, Object>>)prolistjson.get("prolist");
			int priority = 1;
			for(Map<String, Object> pro:prolist){
				pro.put("templateid", templateid);
				pro.put("createid", userinfo.get("userid"));
				pro.put("priority", priority);
				this.workSheetInspectService.insertTemplateProject(pro);
				priority++;
			}
			
			JSONObject rangejson=JSONObject.fromObject(map.get("orglist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)rangejson.get("orglist");
			for(Map<String, Object> user:userlist){
				user.put("companyid", userinfo.get("companyid"));
 				user.put("resourceid", templateid);
 				user.put("resourcetype", 8);
 				if(user.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
 					user.put("organizeid", user.get("organizeid"));
 					user.put("type", 2);
 					
 					//添加到发布范围用户表
 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
 					for(Map<String, Object> users:userinfolist){
 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
 						releaserangemap.put("companyid", userinfo.get("companyid"));
 						releaserangemap.put("resourceid", templateid);
 						releaserangemap.put("resourcetype", 8);
 						releaserangemap.put("userid", users.get("userid"));
 						releaserangemap.put("createid", userinfo.get("userid"));
 						this.indexService.insertReleaseRangeUser(releaserangemap);
 					}
 				}
 				if(user.containsKey("userid") && !"".equals(map.get("userid"))){
 					user.put("userid", user.get("userid"));
 					user.put("type", 1);
 					
 					//添加到发布范围用户表
 					Map<String, Object>  releaserangemap=new HashMap<String, Object>();
					releaserangemap.put("companyid", userinfo.get("companyid"));
					releaserangemap.put("resourceid", templateid);
					releaserangemap.put("resourcetype", 8);
					releaserangemap.put("userid", user.get("userid"));
					releaserangemap.put("createid", userinfo.get("userid"));
					this.indexService.insertReleaseRangeUser(releaserangemap);
 				}
 				
 				//添加到发布范围表
				this.indexService.insertReleaseRange(user);
			}
			
		
			
			String content = "";
			String module = "";
			String type = String.valueOf(map.get("type"));
			if("1".equals(type)){
				module = "离店检查模板";
				content = "添加了一个离店检查模板";
			}else if("2".equals(type)){
				module = "厨房检查模板";
				content = "添加了一个厨房检查模板";
			}else if("3".equals(type)){
				module = "餐前检查模板";
				content = "添加了一个餐前检查模板";
			}
			this.insertManageLog(getUserInfo(request).get("companyid").toString(), 2, module, content, getUserInfo(request).get("userid").toString());
			
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	
	/**
	 * 查询检查项目信息
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getTemplateProjectList")
	public Map<String,Object> getTemplateProjectList(@RequestParam Map<String,Object> map){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			List<Map<String,Object>> list = this.workSheetInspectService.getTemplateProjectList(map);
			Map<String, Object> rangemap=new HashMap<String, Object>();
			rangemap.put("resourcetype", 8);
			rangemap.put("resourceid", map.get("templateid"));
			List<Map<String,Object>> rangelist = this.indexService.getRangeList(rangemap);
			
			if(list.size() > 0 && list != null){
				resultMap.put("status", 0);
				resultMap.put("projectlist", list);
				resultMap.put("rangelist", rangelist);
			}else{
				resultMap.put("status", 1);
				resultMap.put("projectlist", "");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("projectlist", "");
		}
		
		return resultMap;
	}
	
	
	/**
	 * 查询用户组织架构信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getOrganizeListInfo")
	public Map<String,Object> getOrganizeListInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			Map<String, Object> userinfo=UserUtil.getUser(request);
			if(userinfo != null){
				if(userinfo.containsKey("managerole") && !"".equals(map.get("managerole"))){
					String managerole = String.valueOf(userinfo.get("managerole"));
					if("3".equals(managerole)){
						map.put("companyid", userinfo.get("companyid"));
						list = this.workSheetInspectService.getOrganizeList(map);
					}else if("2".equals(managerole)){
						map.put("userid", userinfo.get("userid"));
						list = this.workSheetInspectService.getOrganizeList(map);
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
	 * 查询组织机构下的人员
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getOrganizeUserList")
	public Map<String,Object> getOrganizeUserList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.workSheetInspectService.getUserInfoCountByOrganizeid(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.workSheetInspectService.getUserInfoByOrganizeid(map);
			resultMap.put("pager", page.JSCateringPage2().toString());
			resultMap.put("status", 0);
			resultMap.put("userlist", list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	
	/**
	 * 修改范围
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateRangeInfo")
	public Map<String,Object> updateRangeInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, Object> userinfo=UserUtil.getUser(request);
			
			String templateid = String.valueOf(map.get("templateid"));
			
			//修改模板区域名称
			this.workSheetInspectService.updateInspectTemplate(map);
			
			// 删除检查项目
			this.workSheetInspectService.deleteTemplateProjectInfo(map);
			
			// 添加检查项目
			JSONObject prolistjson=JSONObject.fromObject(map.get("prolist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> prolist=(List<Map<String, Object>>)prolistjson.get("prolist");
			int priority = 1;
			for(Map<String, Object> pro:prolist){
				pro.put("templateid", templateid);
				pro.put("createid", userinfo.get("userid"));
				pro.put("priority", priority);
				this.workSheetInspectService.insertTemplateProject(pro);
				priority++;
			}
			
			Map<String,Object> rangemap = new HashMap<String, Object>();
			rangemap.put("resourceid", templateid);
			rangemap.put("resourcetype", 8);
			this.workSheetInspectService.deleteReleaseRangeInfo(rangemap);
			
			JSONObject rangejson=JSONObject.fromObject(map.get("rangelist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)rangejson.get("rangelist");
			for(Map<String, Object> user:userlist){
				user.put("companyid", userinfo.get("companyid"));
 				user.put("resourceid", templateid);
 				user.put("resourcetype", 8);
 				if(user.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
 					user.put("organizeid", user.get("organizeid"));
 					user.put("type", 2);
 					
 					//添加到发布范围用户表
 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
 					for(Map<String, Object> users:userinfolist){
 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
 						releaserangemap.put("companyid", userinfo.get("companyid"));
 						releaserangemap.put("resourceid", templateid);
 						releaserangemap.put("resourcetype", 8);
 						releaserangemap.put("userid", users.get("userid"));
 						releaserangemap.put("createid", userinfo.get("userid"));
 						this.indexService.insertReleaseRangeUser(releaserangemap);
 					}
 				}
 				if(user.containsKey("userid") && !"".equals(map.get("userid"))){
 					user.put("userid", user.get("userid"));
 					user.put("type", 1);
 					
 					//添加到发布范围用户表
 					Map<String, Object>  releaserangemap=new HashMap<String, Object>();
					releaserangemap.put("companyid", userinfo.get("companyid"));
					releaserangemap.put("resourceid", templateid);
					releaserangemap.put("resourcetype", 8);
					releaserangemap.put("userid", user.get("userid"));
					releaserangemap.put("createid", userinfo.get("userid"));
					this.indexService.insertReleaseRangeUser(releaserangemap);
 				}
 				
 				//添加到发布范围表
				this.indexService.insertReleaseRange(user);
			}
			
			String content = "";
			String module = "";
			String type = String.valueOf(map.get("type"));
			if("1".equals(type)){
				module = "离店检查模板";
				content = "修改了一个离店检查模板";
			}else if("2".equals(type)){
				module = "厨房检查模板";
				content = "修改了一个厨房检查模板";
			}else if("3".equals(type)){
				module = "餐前检查模板";
				content = "修改了一个餐前检查模板";
			}
			this.insertManageLog(getUserInfo(request).get("companyid").toString(), 2, module, content, getUserInfo(request).get("userid").toString());
			
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	
	
	/**
	 * 查询默认的模板信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getDefaultTemplateList")
	public Map<String,Object> getDefaultTemplateList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			List<Map<String,Object>> templist = this.workSheetInspectService.getDefaultTemplateList(map);
			if(templist != null && templist.size() > 0){
				resultMap.put("data", templist);
				resultMap.put("status", 0);
			}else{
				resultMap.put("data", "");
				resultMap.put("status", 1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("data", "");
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	
	/**
	 * 查询默认的检查项目列表信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getDefaultProjectList")
	public Map<String,Object> getDefaultProjectList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			List<Map<String,Object>> prolist = this.workSheetInspectService.getDefaultProjectList(map);
			if(prolist != null && prolist.size() > 0){
				resultMap.put("data", prolist);
				resultMap.put("status", 0);
			}else{
				resultMap.put("data", "");
				resultMap.put("status", 1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("data", "");
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	
	/**
	 * 查询组织架构的类型
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getOrganizeTypeInfo")
	public String getOrganizeTypeInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		String type = "";
		try {
			Map<String,Object> orgmap = this.workSheetInspectService.getOrganizeDataCodeInfo(map);
			if(orgmap != null && orgmap.size() > 0){
				type = String.valueOf(orgmap.get("type"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return type;
	}
	
	/**
	 * 查询模板名称时候重复
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getTemplateNameIsExists")
	public String getTemplateNameIsExists(@RequestParam Map<String,Object> map , HttpServletRequest request){
		String msg = "error";
		try {
			map.put("companyid", getUserInfo(request).get("companyid"));
			int count = this.workSheetInspectService.getTemplateNameIsExists(map);
			if(count == 0){
				msg = "success";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}
	
	public Map<String,Object> getUserInfo(HttpServletRequest request){
		return UserUtil.getUser(request);
	}
}
