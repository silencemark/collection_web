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

import com.collection.service.worksheet.OrganizeTemplateService;
import com.collection.util.UserUtil;


@Controller
@RequestMapping(value="/userbackstage")
public class OrganizeTemplateController {

	@Resource 
	OrganizeTemplateService organizeTemplateService;
	
	
	/**
	 * 查询组织的检查模板列表信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getOrganizeTemplateListInfo")
	public Map<String,Object> getOrganizeTemplateListInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			List<Map<String,Object>> list = this.organizeTemplateService.getOrganizeTemplateListInfo(map);
			if(list != null && list.size() > 0){
				resultMap.put("status", 0);
				resultMap.put("templatelist", list);
			}else{
				resultMap.put("status", 1);
				resultMap.put("templatelist", "");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("templatelist", "");
		}
		
		return resultMap;
	}
	
	
	/**
	 * 查询模板的检查项目列表信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getOrganizeTemplateProjectListInfo")
	public Map<String,Object> getOrganizeTemplateProjectListInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String,Object>> list = this.organizeTemplateService.getOrganizeTemplateProjectListInfo(map);
			if(list != null && list.size() > 0){
				resultMap.put("status", 0);
				resultMap.put("projectlist", list);
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
	 * 添加检查模板检查项目信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertOrganizeTemplateInfo")
	public String insertOrganizeTemplateInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		String msg = "success";
		try {
			int maxpriority = this.organizeTemplateService.getOrganizeTemplateMaxPriority(map);
			map.put("priority", maxpriority);
			map.put("createid", getUserInfo(request).get("userid"));
			//添加模板信息
			String templateid = this.organizeTemplateService.insertOrganizeTemplateInfo(map);
			//得到该模板的检查项目信息
			JSONObject rangejson=JSONObject.fromObject(map.get("prolist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> projectlist = (List<Map<String, Object>>)rangejson.get("prolist");
			int priority = 1;
			//循环插入检查项目信息
			for(Map<String,Object> promap : projectlist){
				promap.put("createid", getUserInfo(request).get("userid"));
				promap.put("priority", priority);
				promap.put("templateid", templateid);
				this.organizeTemplateService.insertOrganizeTemplateProjectInfo(promap);
				priority++;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}
	
	/**
	 * 添加检查项目
	 * 传入参数：templateid，projectname
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertOrganizeTemplateProjectInfo")
	public String insertOrganizeTemplateProjectInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		String msg = "success";
		try {
			int priority = this.organizeTemplateService.getOrganizeTemplateProjectMaxPriority(map);
			map.put("priority", priority);
			map.put("createid", getUserInfo(request).get("userid"));
			String projectid = this.organizeTemplateService.insertOrganizeTemplateProjectInfo(map);
			msg = projectid;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}
	
	/**
	 * 修改检查模板信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateOrganizeTemplateInfo")
	public String updateOrganizeTemplateInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		String msg = "success";
		try {
			map.put("updateid", getUserInfo(request).get("userid"));
			this.organizeTemplateService.updateOrganizeTemplateInfo(map);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}
	
	
	/**
	 * 修改检查项目信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateOrganizeTemplateProjectInfo")
	public String updateOrganizeTemplateProjectInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		String msg = "success";
		try {
			map.put("updateid", getUserInfo(request).get("userid"));
			this.organizeTemplateService.updateOrganizeTemplateProjectInfo(map);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}
	
	/**
	 * 验证模板名称是否重复
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getOrganizeTemplateNameIsExists")
	public String getOrganizeTemplateNameIsExists(@RequestParam Map<String,Object> map , HttpServletRequest request){
		String msg = "success";
		try {
			map.put("companyid", getUserInfo(request).get("companyid"));
			int num = this.organizeTemplateService.getOrganizeTemplateNameIsExsit(map);
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
	
	
	/**
	 * 获取当前登陆者信息
	 * @param request
	 * @return
	 */
	public Map<String,Object> getUserInfo(HttpServletRequest request){
		return UserUtil.getUser(request);
	}
}
