package com.collection.controller.userbackstage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.service.IndexService;
import com.collection.service.starassess.StarAssessService;
import com.collection.service.worksheet.WorkSheetInspectService;
import com.collection.util.PageHelper;
import com.collection.util.UserUtil;


@Controller
@RequestMapping(value="/userbackstage")
public class StarEvaluateTemplateController extends BaseController{

	@Resource StarAssessService starAssessService;
	@Resource WorkSheetInspectService workSheetInspectService;
	@Resource IndexService indexService;
	
	/**
	 * 进入星值页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoStarPage")
	public String intoStarPage(@RequestParam Map<String,Object> map , HttpServletRequest request){
		String type = String.valueOf(map.get("type"));
		if("1".equals(type)){
			map.put("typename", "岗位星值");
			map.put("typeid", 12);
		}else if("2".equals(type)){
			map.put("typename", "综合星值");
			map.put("typeid", 13);
		}
		Map<String, Object> userinfo=UserUtil.getUser(request);
		String managerole = String.valueOf(getUserInfo(request).get("managerole"));
		map.put("managerole", managerole);
		request.setAttribute("map", map);
		request.setAttribute("user", userinfo);
		return "/userbackstage/kpi/kpi_jobstar";
	}
	
	
	/**
	 * 查询自评模板信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getEvaluateTemplate")
	public Map<String,Object> getEvaluateTemplate(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			map.put("companyid", getUserInfo(request).get("companyid"));
			Map<String,Object> orgmap = this.workSheetInspectService.getOrganizeDataCodeInfo(map);
			if(orgmap != null){
				map.put("datacode", orgmap.get("datacode"));
			}
			PageHelper page = new PageHelper(request);
			int count = this.starAssessService.getEvaluateTemplateListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.starAssessService.getEvaluateTemplateList(map);
			
			if(list != null && list.size() > 0){
				resultMap.put("status", 0);
				resultMap.put("templatelist", list);
			}else{
				resultMap.put("status", 1);
				resultMap.put("templatelist", "");
			}
			resultMap.put("pager", page.JSCateringPage().toString());
			
			String content = "";
			if("1".equals(map.get("type"))){
				content = "岗位星值";
			}else if("2".equals(map.get("type"))){
				content = "综合星值";
			}
			this.insertManageLog(getUserInfo(request).get("companyid")+"", 2, content, "查询了"+content+"列表信息。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("templatelist", "");
		}
		
		return resultMap;
	}
	
	/**
	 * 添加自模板信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertEvaluateTemplateInfo")
	public Map<String,Object> insertEvaluateTemplateInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			Map<String, Object> userinfo=UserUtil.getUser(request);
			
			Map<String,Object> tempmap = new HashMap<String,Object>();
			tempmap.put("companyid", userinfo.get("companyid"));
			tempmap.put("templatename", map.get("templatename"));
			tempmap.put("type", map.get("type"));
			tempmap.put("createid", userinfo.get("userid"));
			tempmap.put("organizeid", map.get("organizeid"));
			String templateid = this.starAssessService.insertEvaluateTemplateInfo(tempmap);
			
			
			JSONObject prolistjson=JSONObject.fromObject(map.get("prolist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> prolist=(List<Map<String, Object>>)prolistjson.get("prolist");
			int priority = 1;
			for(Map<String, Object> pro:prolist){
				pro.put("templateid", templateid);
				pro.put("createid", userinfo.get("userid"));
				pro.put("companyid", userinfo.get("companyid"));
				pro.put("organizeid", userinfo.get("organizeid"));
				pro.put("priority", priority);
				this.starAssessService.insertEvaluateProjectInfo(pro);
				priority++;
			}
			
			//发布范围
			JSONObject rangejson=JSONObject.fromObject(map.get("orglist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)rangejson.get("orglist");
			for(Map<String, Object> user:userlist){
				user.put("companyid", userinfo.get("companyid"));
 				user.put("resourceid", templateid);
 				user.put("resourcetype", 9);
 				if(user.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
 					user.put("organizeid", user.get("organizeid"));
 					user.put("type", 2);
 					
	 				//添加到发布范围用户表
 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
 					for(Map<String, Object> users:userinfolist){
 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
 						releaserangemap.put("companyid", userinfo.get("companyid"));
 						releaserangemap.put("resourceid", templateid);
 						releaserangemap.put("resourcetype", 9);
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
					releaserangemap.put("resourcetype", 9);
					releaserangemap.put("userid", user.get("userid"));
					releaserangemap.put("createid", userinfo.get("userid"));
					this.indexService.insertReleaseRangeUser(releaserangemap);
 				}
 				
 				//添加到发布范围表
				this.indexService.insertReleaseRange(user);
			}
			
			
			String type = String.valueOf(map.get("type"));
			String content = "";
			String module = "";
			if("1".equals(type)){
				module = "岗位星值";
				content = getUserInfo(request).get("realname")+"添加了一个岗位星值模板";
			}else if("2".equals(type)){
				module = "综合星值";
				content = getUserInfo(request).get("realname")+"添加了一个综合星值模板";
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
	 * 修改自评模板信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateEvaluateTemplate")
	public Map<String,Object> updateEvaluateTemplate(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			this.starAssessService.updateEvaluateTemplateInfo(map);
			
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 查询自评项目详情
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getEvaluateTemplateDetail")
	public Map<String,Object> getEvaluateTemplateDetail(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			Map<String,Object> detailmap = this.starAssessService.getEvaluateTemplateDetail(map);
			resultMap.put("status", 0);
			resultMap.put("detailmap", detailmap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 修改模板的检查项目和发布范围
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateEvaluateTemplateProject")
	public Map<String,Object> updateEvaluateTemplateProject(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			//修改模板名称
			this.starAssessService.updateEvaluateTemplateInfo(map);
			
			String templateid = String.valueOf(map.get("templateid"));
			Map<String,Object> promap = new HashMap<String,Object>();
			promap.put("templateid", templateid);
			promap.put("delflag", 1);
			//删除检查项目
			this.starAssessService.updateEvaluateProjectInfo(promap);
			//添加检查项目
			JSONObject prolistjson=JSONObject.fromObject(map.get("prolist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> prolist=(List<Map<String, Object>>)prolistjson.get("prolist");
			int priority = 1;
			for(Map<String, Object> pro:prolist){
				pro.put("templateid", templateid);
				pro.put("createid", getUserInfo(request).get("userid"));
				pro.put("companyid", getUserInfo(request).get("companyid"));
				pro.put("organizeid", getUserInfo(request).get("organizeid"));
				pro.put("priority", priority);
				this.starAssessService.insertEvaluateProjectInfo(pro);
				priority++;
			}
			
			
			//删除发布范围
			Map<String,Object> rangemap = new HashMap<String, Object>();
			rangemap.put("resourceid", templateid);
			rangemap.put("resourcetype", 9);
			this.workSheetInspectService.deleteReleaseRangeInfo(rangemap);
			
			JSONObject rangejson=JSONObject.fromObject(map.get("orglist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)rangejson.get("orglist");
			for(Map<String, Object> user:userlist){
				user.put("companyid", getUserInfo(request).get("companyid"));
 				user.put("resourceid", templateid);
 				user.put("resourcetype", 9);
 				if(user.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
 					user.put("organizeid", user.get("organizeid"));
 					user.put("type", 2);
 					
 					//添加到发布范围用户表
 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
 					for(Map<String, Object> users:userinfolist){
 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
 						releaserangemap.put("companyid", getUserInfo(request).get("companyid"));
 						releaserangemap.put("resourceid", templateid);
 						releaserangemap.put("resourcetype", 9);
 						releaserangemap.put("userid", users.get("userid"));
 						releaserangemap.put("createid", getUserInfo(request).get("userid"));
 						this.indexService.insertReleaseRangeUser(releaserangemap);
 					}
 				}
 				if(user.containsKey("userid") && !"".equals(map.get("userid"))){
 					user.put("userid", user.get("userid"));
 					user.put("type", 1);
 					
 					//添加到发布范围用户表
 					Map<String, Object>  releaserangemap=new HashMap<String, Object>();
					releaserangemap.put("companyid", getUserInfo(request).get("companyid"));
					releaserangemap.put("resourceid", templateid);
					releaserangemap.put("resourcetype", 9);
					releaserangemap.put("userid", user.get("userid"));
					releaserangemap.put("createid", getUserInfo(request).get("userid"));
					this.indexService.insertReleaseRangeUser(releaserangemap);
 				}
 				
 				//添加到发布范围表
				this.indexService.insertReleaseRange(user);
			}
			String content = "";
			String module = "";
			Map<String,Object> detailmap = this.starAssessService.getEvaluateDetailInfo(map);
			if(detailmap != null && detailmap.size() > 0){
				String type = String.valueOf(detailmap.get("type"));
				if("1".equals(type)){
					module = "岗位星值";
					content = getUserInfo(request).get("realname")+"修改岗位星值，名为"+detailmap.get("templatename")+"的模板信息。";
				}else if("2".equals(type)){
					module = "综合星值";
					content = getUserInfo(request).get("realname")+"修改综合星值，名为"+detailmap.get("templatename")+"的模板信息。";
				}
				this.insertManageLog(getUserInfo(request).get("companyid").toString(), 2, module, content, getUserInfo(request).get("userid").toString());
			}
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 查询公司的组织信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getOrganizeByCompanyid")
	public Map<String,Object> getOrganizeByCompanyid(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			Map<String,Object> orgmap = this.starAssessService.getOrganizeByCompanyId(map);
			resultMap.put("data", orgmap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("data", "");
		}
		return resultMap;
	}
	
	/**
	 * 查询模板名称是否存在
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getEvaluateTemplateNameIsExists")
	public String getEvaluateTemplateNameIsExists(@RequestParam Map<String,Object> map , HttpServletRequest request){
		String msg = "success";
		try {
			map.put("companyid", getUserInfo(request).get("companyid"));
			int num = this.starAssessService.getEValuateTemplateNameIsExists(map);
			if(num > 0){
				msg = "exists";
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
