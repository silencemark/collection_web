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
import com.collection.util.Constants;
import com.collection.util.HtmlUtil;
import com.collection.util.PageHelper;
import com.collection.util.UserUtil;

@Controller
@RequestMapping(value="/userbackstage")
public class StarRankingRuleController extends BaseController{

	@Resource StarAssessService starAssessService;
	@Resource IndexService indexService;
	@Resource WorkSheetInspectService workSheetInspectService;
	
	/**
	 * 进入规则列表页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoRuleListPage")
	public String intoRuleListPage(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("map", map);
		return "/userbackstage/kpi/kpi_rulelist";
	}
	
	
	/**
	 * 查询规则列表
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getStarRankingRule")
	public Map<String,Object> getStarRankingRule(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			map.put("companyid", getUserInfo(request).get("companyid"));
			PageHelper page = new PageHelper(request);
			int count = this.starAssessService.getStarRuleListNum(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.starAssessService.getStarRuleList(map);
			
			resultMap.put("pager", page.JSCateringPage().toString());
			resultMap.put("rulelist", list);
			resultMap.put("status", 0);
			
			this.insertManageLog(getUserInfo(request).get("companyid")+"", 2, "KPI规则", "查询KPI规则列表。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 添加规则
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertStarRankingRuleInfo")
	public Map<String,Object> insertStarRankingRuleInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			map.put("companyid", getUserInfo(request).get("companyid"));
			map.put("createid", getUserInfo(request).get("userid"));
			map.put("content", HtmlUtil.returnHtmlStr(String.valueOf(map.get("content")), Constants.PROJECT_PATH));
			String ruleid = this.starAssessService.insertStarRankingRuleInfo(map);
			
			JSONObject rangejson=JSONObject.fromObject(map.get("rangelist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)rangejson.get("rangelist");
			for(Map<String, Object> user:userlist){
				user.put("companyid", getUserInfo(request).get("companyid"));
 				user.put("resourceid", ruleid);
 				user.put("resourcetype", 10);
 				if(user.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
 					user.put("organizeid", user.get("organizeid"));
 					user.put("type", 2);
 					
 					//添加到发布范围用户表
 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
 					for(Map<String, Object> users:userinfolist){
 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
 						releaserangemap.put("companyid", getUserInfo(request).get("companyid"));
 						releaserangemap.put("resourceid", ruleid);
 						releaserangemap.put("resourcetype", 10);
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
					releaserangemap.put("resourceid", ruleid);
					releaserangemap.put("resourcetype", 10);
					releaserangemap.put("userid", user.get("userid"));
					releaserangemap.put("createid", getUserInfo(request).get("userid"));
					this.indexService.insertReleaseRangeUser(releaserangemap);
 				}
 				
 				//添加到发布范围表
				this.indexService.insertReleaseRange(user);
			}
			
			resultMap.put("status", 0);
			
			this.insertManageLog(getUserInfo(request).get("companyid")+"", 2, "KPI规则", "添加KPI规则信息。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 进入修改页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoRuleEditor")
	public String intoRuleEditor(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("map", map);
		return "/userbackstage/kpi/kpi_ruleedit";
	}
	
	/**
	 * 修改规则信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateStarRankingRuleInfo")
	public Map<String,Object> updateStarRankingRuleInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			map.put("updateid", getUserInfo(request).get("userid"));
			map.put("content", HtmlUtil.returnHtmlStr(String.valueOf(map.get("content")), Constants.PROJECT_PATH));
			this.starAssessService.updateStarRankingRuleInfo(map);
			
			String ruleid = String.valueOf(map.get("ruleid"));
			
			Map<String,Object> rangemap = new HashMap<String, Object>();
			rangemap.put("resourceid", ruleid);
			rangemap.put("resourcetype", 10);
			this.workSheetInspectService.deleteReleaseRangeInfo(rangemap);
			
			JSONObject rangejson=JSONObject.fromObject(map.get("rangelist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)rangejson.get("rangelist");
			for(Map<String, Object> user:userlist){
				user.put("companyid", getUserInfo(request).get("companyid"));
 				user.put("resourceid", ruleid);
 				user.put("resourcetype", 10);
 				if(user.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
 					user.put("organizeid", user.get("organizeid"));
 					user.put("type", 2);
 					
 					//添加到发布范围用户表
 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
 					for(Map<String, Object> users:userinfolist){
 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
 						releaserangemap.put("companyid", getUserInfo(request).get("companyid"));
 						releaserangemap.put("resourceid", ruleid);
 						releaserangemap.put("resourcetype", 10);
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
					releaserangemap.put("resourceid", ruleid);
					releaserangemap.put("resourcetype", 10);
					releaserangemap.put("userid", user.get("userid"));
					releaserangemap.put("createid", getUserInfo(request).get("userid"));
					this.indexService.insertReleaseRangeUser(releaserangemap);
 				}
 				
 				//添加到发布范围表
				this.indexService.insertReleaseRange(user);
			}
			resultMap.put("status", 0);
			
			this.insertManageLog(getUserInfo(request).get("companyid")+"", 2, "KPI规则", "修改KPI规则信息。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 进入详情页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoRuleDetail")
	public String intoRuleDetail(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("map", map);
		return "/userbackstage/kpi/kpi_ruledetail";
	}
	
	/**
	 * 查询规则的详细信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getStarRankingRuleDetailInfo")
	public Map<String,Object> getStarRankingRuleDetailInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			Map<String,Object> rulemap = this.starAssessService.getStarRankingRuleDetailInfo(map);
			resultMap.put("status", 0);
			resultMap.put("rulemap", rulemap);
			
			this.insertManageLog(getUserInfo(request).get("companyid")+"", 2, "KPI规则", "查询KPI规则详情。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 删除规则信息
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteStarRankingRuleInfo")
	public String deleteStarRankingRuleInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			map.put("delflag", 1);
			this.starAssessService.updateStarRankingRuleInfo(map);
			
			this.insertManageLog(getUserInfo(request).get("companyid")+"", 2, "KPI规则", "删除KPI规则信息。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "/userbackstage/kpi/kpi_rulelist";
	}
	
	
	public Map<String,Object> getUserInfo(HttpServletRequest request){
		return UserUtil.getUser(request);
	}
}
