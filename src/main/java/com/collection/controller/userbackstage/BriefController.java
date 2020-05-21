package com.collection.controller.userbackstage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.service.IndexService;
import com.collection.service.oa.OfficeService;
import com.collection.service.worksheet.WorkSheetInspectService;
import com.collection.util.Constants;
import com.collection.util.HtmlUtil;
import com.collection.util.PageHelper;
import com.collection.util.UserUtil;

/**
 * 使用方后台管理企业简报
 * @author silence
 *
 */
@Controller
@RequestMapping("/userbackstage")
public class BriefController extends BaseController{
	private transient static Log log = LogFactory.getLog(BriefController.class);
	@Resource private OfficeService officeService;
	
	@Resource private IndexService indexService;
	
	@Resource private WorkSheetInspectService workSheetInspectService;

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
	public String getCompanyModuleList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		map.put("companyid", getUserInfo(request).get("companyid"));
		PageHelper page = new PageHelper(request);
		int count = this.officeService.getCompanyModuleListCount(map);
		page.setTotalCount(count);
		page.initPage(map);
		List<Map<String, Object>> modulelist=this.officeService.getCompanyModuleList(map);
		model.addAttribute("modulelist", modulelist);
		model.addAttribute("pager",page.cateringPage().toString());
		
		String content = "查询了企业简报栏目信息。";
		this.insertManageLog(String.valueOf(getUserInfo(request).get("companyid")), 2, "企业简报-栏目", content, String.valueOf(getUserInfo(request).get("userid")));
		return "/userbackstage/briefing/oa_briefing";
	}
	
	/**
	 * 修改简报栏目信息
	 * 传如参数：moduleimage，modulename，delflag
	 * 			moduleid
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateCompanyModuleInfo")
	public Map<String,Object> updateCompanyModuleInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			this.officeService.updateCompanyModuleInfo(map);
			
			String content = "将简报栏目名修改为"+map.get("modulename");
			this.insertManageLog(String.valueOf(getUserInfo(request).get("companyid")), 2, "企业简报-栏目", content, String.valueOf(getUserInfo(request).get("userid")));
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 添加简报栏目信息
	 * 传入参数:modulename,moduleimage
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertCompanyModuleInfo")
	public Map<String,Object> insertCompanyModuleInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("companyid", getUserInfo(request).get("companyid"));
			map.put("createid", getUserInfo(request).get("userid"));
			this.officeService.insertCompanyModule(map);
			
			String content = "新增了简报栏目，名为"+map.get("modulename");
			this.insertManageLog(String.valueOf(getUserInfo(request).get("companyid")), 2, "企业简报-栏目", content, String.valueOf(getUserInfo(request).get("userid")));
			
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	
	/**
	 * 进入企业简报列表页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoBriefListPage")
	public String intoBriefListPage(@RequestParam Map<String,Object> map, HttpServletRequest request){
		request.setAttribute("map", map);
		return "/userbackstage/briefing/oa_briefinglist";
	}
	
	
	/**
	 * 查询简报信息
	 * 传入参数：title，moduleid
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getBriefList")
	public Map<String,Object> getBriefList(@RequestParam Map<String,Object> map, HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			map.put("userid", getUserInfo(request).get("userid"));
			map.put("companyid", getUserInfo(request).get("companyid"));
			
			PageHelper page = new PageHelper(request);
			int count = this.officeService.getBriefListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.officeService.getBriefListInfo(map);
			
			resultMap.put("status", 0);
			resultMap.put("brieflist", list);
			resultMap.put("pager", page.JSCateringPage().toString());
			resultMap.put("map", map);
			
			String content = "查询简报列表。";
			this.insertManageLog(String.valueOf(getUserInfo(request).get("companyid")), 2, "企业简报", content, String.valueOf(getUserInfo(request).get("userid")));
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	
	/**
	 * 进入编辑页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoBriefingEditPage")
	public String intoBriefingEditPage(@RequestParam Map<String,Object> map, HttpServletRequest request){
		request.setAttribute("map", map);
		return "/userbackstage/briefing/oa_briefingedit";
	}
	
	/**
	 * 进入详情页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoBriefingDetialPage")
	public String intoBriefingDetialPage(@RequestParam Map<String,Object> map, HttpServletRequest request){
		Map<String,Object> briefmap = this.officeService.getBriefInfo(map);
		request.setAttribute("briefmap", briefmap);
		request.setAttribute("map", map);
		return "/userbackstage/briefing/oa_briefingdetail";
	}
	
	/**
	 * 添加企业简报
	 * 传入参数：moduleid，title，content
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertBriefInfo")
	public Map<String,Object> insertBriefInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			map.put("createid", getUserInfo(request).get("userid"));
			map.put("content", HtmlUtil.returnHtmlStr(String.valueOf(map.get("content")), Constants.PROJECT_PATH));
			String briefid = this.officeService.insertBrief(map);
			
			JSONObject rangejson=JSONObject.fromObject(map.get("rangelist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)rangejson.get("rangelist");
			for(Map<String, Object> user:userlist){
				user.put("companyid", getUserInfo(request).get("companyid"));
 				user.put("resourceid", briefid);
 				user.put("resourcetype", 7);
 				if(user.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
 					user.put("organizeid", user.get("organizeid"));
 					user.put("type", 2);
 					
 					//添加到发布范围用户表
 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
 					for(Map<String, Object> users:userinfolist){
 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
 						releaserangemap.put("companyid", getUserInfo(request).get("companyid"));
 						releaserangemap.put("resourceid", briefid);
 						releaserangemap.put("resourcetype", 7);
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
					releaserangemap.put("resourceid", briefid);
					releaserangemap.put("resourcetype", 7);
					releaserangemap.put("userid", user.get("userid"));
					releaserangemap.put("createid", getUserInfo(request).get("userid"));
					this.indexService.insertReleaseRangeUser(releaserangemap);
 				}
 				
 				//添加到发布范围表
				this.indexService.insertReleaseRange(user);
			}
			
			String content = "新增了一则标题为：“"+map.get("title")+"”的简报。";
			this.insertManageLog(String.valueOf(getUserInfo(request).get("companyid")), 2, "企业简报", content, String.valueOf(getUserInfo(request).get("userid")));
			
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 查询企业简报详情
	 * 传入参数：briefid
	 * @param map
	 * @param reqeust
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getBriefDetailInfo")
	public Map<String,Object> getBriefDetailInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			Map<String,Object> briefmap = this.officeService.getBriefInfo(map);
			resultMap.put("briefmap", briefmap);
			resultMap.put("map", map);
			resultMap.put("status", 0);
			
			String content = "查看了标题为："+briefmap.get("title")+"的详情信息。";
			this.insertManageLog(String.valueOf(getUserInfo(request).get("companyid")), 2, "企业简报", content, String.valueOf(getUserInfo(request).get("userid")));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	
	
	/**
	 * 修改企业简报信息
	 * 传入参数：title，content，briefid，rangelist
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateBriefInfo")
	public Map<String,Object> updateBriefInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			String briefid = String.valueOf(map.get("briefid"));
			map.put("content", HtmlUtil.returnHtmlStr(String.valueOf(map.get("content")), Constants.PROJECT_PATH));
			this.officeService.updateBriefInfo(map);
			
			Map<String,Object> rangemap = new HashMap<String, Object>();
			rangemap.put("resourceid", briefid);
			rangemap.put("resourcetype", 7);
			this.workSheetInspectService.deleteReleaseRangeInfo(rangemap);
			
			JSONObject rangejson=JSONObject.fromObject(map.get("rangelist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)rangejson.get("rangelist");
			for(Map<String, Object> user:userlist){
				user.put("companyid", getUserInfo(request).get("companyid"));
 				user.put("resourceid", briefid);
 				user.put("resourcetype", 7);
 				if(user.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
 					user.put("organizeid", user.get("organizeid"));
 					user.put("type", 2);
 					
 					//添加到发布范围用户表
 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
 					for(Map<String, Object> users:userinfolist){
 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
 						releaserangemap.put("companyid", getUserInfo(request).get("companyid"));
 						releaserangemap.put("resourceid", briefid);
 						releaserangemap.put("resourcetype", 7);
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
					releaserangemap.put("resourceid", briefid);
					releaserangemap.put("resourcetype", 7);
					releaserangemap.put("userid", user.get("userid"));
					releaserangemap.put("createid", getUserInfo(request).get("userid"));
					this.indexService.insertReleaseRangeUser(releaserangemap);
 				}
 				
 				//添加到发布范围表
				this.indexService.insertReleaseRange(user);
			}
			
			String content = "修改了标题为："+map.get("title")+"的简报信息";
			this.insertManageLog(String.valueOf(getUserInfo(request).get("companyid")), 2, "企业简报", content, String.valueOf(getUserInfo(request).get("userid")));
			
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	
	/**
	 * 删除企业简报信息
	 * 传入参数：delflag，briefid
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteBriefInfo")
	public String deleteBriefInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			map.put("delflag", 1);
			this.officeService.updateBriefInfo(map);
			
			String content = "删除了标题为："+map.get("title")+"的简报信息";
			this.insertManageLog(String.valueOf(getUserInfo(request).get("companyid")), 2, "企业简报", content, String.valueOf(getUserInfo(request).get("userid")));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return "redirect:/userbackstage/intoBriefListPage?moduleid="+map.get("moduleid");
	}
	
	
	public Map<String,Object> getUserInfo(HttpServletRequest request){
		return UserUtil.getUser(request);
	}
}
