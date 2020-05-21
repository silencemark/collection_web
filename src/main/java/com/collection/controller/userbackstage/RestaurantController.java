package com.collection.controller.userbackstage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.service.IndexService;
import com.collection.service.UserInfoService;
import com.collection.service.oa.OfficeService;
import com.collection.service.storefront.EverydayReportService;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;
import com.collection.util.UserUtil;

/**
 * 使用方后台管理企业简报
 * @author silence
 *
 */
@Controller
@RequestMapping("/userbackstage")
public class RestaurantController extends BaseController{
	private transient static Log log = LogFactory.getLog(RestaurantController.class);
	@Autowired EverydayReportService  everydayReportService;
	
	@Resource private IndexService indexService;

	@Resource private UserInfoService userInfoService;
	/**
	 * 
	 * 查询每日报表的模版列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReportModuleList")
	public String getReportModuleList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userinfo=UserUtil.getUser(request);
		map.put("companyid", userinfo.get("companyid")+"");
		List<Map<String, Object>> modulelist=this.everydayReportService.getReportModuleList(map);
		model.addAttribute("modulelist", modulelist);
		model.addAttribute("map", map);
		
		//查询所有的字典信息
		List<Map<String, Object>> typelist=this.everydayReportService.getDictTypeList();
		model.addAttribute("typelist", typelist);
		
		this.insertManageLog(userinfo.get("companyid")+"", 2, "每日报表", "查询了每日报表模板列表信息。", userinfo.get("userid")+"");
		return "/userbackstage/storefront/restuarant";
	}
	@RequestMapping("/getReportModule")
	@ResponseBody
	public Map<String, Object> getReportModule(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		Map<String, Object> userinfo=UserUtil.getUser(request);
		map.put("companyid", userinfo.get("companyid")+"");
		List<Map<String, Object>> modulelist=this.everydayReportService.getReportModuleList(map);
		data.put("modulelist", modulelist);
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
	public Map<String,Object> getOrganize(@RequestParam Map<String,Object> map , HttpServletRequest request, 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			Map<String, Object> userinfo=UserUtil.getUser(request);
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
	 * 查询用户信息
	 * 传入参数{"organizeid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数{"message":"查询成功","modulelist":[{"createtime":1468983040000,"companyid":"67702cc412264f4ea7d2c5f692070457","modulename":"公司新闻","moduleimage":"1.jpg","updatetime":1468983120000,"createid":"690fb669ed4d40219964baad7783abd4","moduleid":"fdd61606a640459081bce3d02dd8e01a"}],"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getUserByOrganize")
	@ResponseBody
	public Map<String, Object> getUserByOrganize(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		PageHelper page = new PageHelper(request);
		int count = this.userInfoService.getUserListByOrganizeCount(map);
		page.setTotalCount(count);
		page.initPage(map);
		List<Map<String, Object>> userlist=this.userInfoService.getUserListByOrganize(map);
		data.put("pager2", page.JSCateringPage2().toString());
		data.put("pager", page.JSCateringPage().toString());
		data.put("pager1", page.JSCateringPage1().toString());
		data.put("pager3", page.JSCateringPage3().toString());
		data.put("userlist", userlist);
		return data;
	}

	/**
	 * 
	 * 模糊查询
	 * 传入参数{"realname":"李语然"}
	 * 传出参数{"message":"查询成功","modulelist":[{"createtime":1468983040000,"companyid":"67702cc412264f4ea7d2c5f692070457","modulename":"公司新闻","moduleimage":"1.jpg","updatetime":1468983120000,"createid":"690fb669ed4d40219964baad7783abd4","moduleid":"fdd61606a640459081bce3d02dd8e01a"}],"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	
	@ResponseBody
	@RequestMapping(value="/getSearchUserByName")
	public Map<String,Object> getSearchUserByName(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			Map<String, Object> userinfo=UserUtil.getUser(request);
			if(userinfo != null){
				if(userinfo.containsKey("managerole") && !"".equals(map.get("managerole"))){
					String managerole = String.valueOf(userinfo.get("managerole"));
					if("3".equals(managerole)){
						Map<String, Object> organizemap=new HashMap<String, Object>();
						organizemap.put("companyid", userinfo.get("companyid"));
						list = this.indexService.getOrganizeList(organizemap);
					}else if("2".equals(managerole)){
						Map<String, Object> organizemap=new HashMap<String, Object>();
						organizemap.put("userid", userinfo.get("userid"));
						organizemap.put("companyid", userinfo.get("companyid"));
						list = this.indexService.getOrganizeByUserList(organizemap);
					}
				}
			}
			List<String> organizes=new ArrayList<String>();
			for(Map<String, Object> organize:list){
				if(!String.valueOf(organize.get("isonlyread")).equals('1')){
					organizes.add(organize.get("organizeid")+"");
				}
			}
			map.put("organizes", organizes);
			PageHelper page = new PageHelper(request);
			int count = this.userInfoService.getUserListByOrganizeCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String, Object>> userlist=this.userInfoService.getUserListByOrganize(map);
			resultMap.put("userlist", userlist);
			resultMap.put("status", 0);
			resultMap.put("pager2", page.JSCateringPage2().toString());
			resultMap.put("pager", page.JSCateringPage().toString());
			resultMap.put("pager1", page.JSCateringPage1().toString());
			resultMap.put("pager3", page.JSCateringPage3().toString());
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
	 * 添加模版 字典信息
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	
	@RequestMapping(value="/insertTemplateReport")
	@ResponseBody
	public Map<String,Object> insertTemplateReport(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> data = new HashMap<String, Object>();

		Map<String, Object> userinfo=UserUtil.getUser(request);
		Map<String,Object> orgmap = this.everydayReportService.getOrganizeByOrganizeid(map);
		map.put("organizename", orgmap.get("organizename"));
		insertManageLog(userinfo.get("companyid")+"",2,"每日报表","新增"+map.get("organizename")+"每日报表模版",userinfo.get("userid")+"");
		
		//添加模版
		Map<String, Object> templatemap=new HashMap<String, Object>();
		templatemap.put("companyid", userinfo.get("companyid"));
		templatemap.put("organizeid", map.get("organizeid"));
		templatemap.put("templatename", map.get("organizename")+"每日报表模版");
		templatemap.put("createid", userinfo.get("userid"));
		String templateid=this.everydayReportService.insertTemplate(templatemap);
		try {
			JSONObject json=JSONObject.fromObject(map.get("datalist")+"");
			List<Map<String, Object>> extendlist=(List<Map<String, Object>>)json.get("extendlist");
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			//添加到字典扩展表
			for(Map<String, Object> extend:extendlist){
				extend.put("templateid", templateid);
				extend.put("createid", userinfo.get("userid"));
				this.everydayReportService.insertReportTemplateExtend(extend);
			}
			
			//添加到发布范围
			for(Map<String, Object> user:userlist){
				user.put("companyid", userinfo.get("companyid"));
 				user.put("resourceid", templateid);
 				user.put("resourcetype", 11);
 				if(user.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
 					user.put("organizeid", user.get("organizeid"));
 					user.put("type", 2);
 					
 					//添加到发布范围用户表
// 					List<Map<String,Object>> userinfolistall=new ArrayList<Map<String,Object>>();
 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
 					for(Map<String, Object> user1:userinfolist){
 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
 						releaserangemap.put("companyid", userinfo.get("companyid"));
 						releaserangemap.put("resourceid", templateid);
 						releaserangemap.put("resourcetype", 11);
 						releaserangemap.put("userid", user1.get("userid"));
 						releaserangemap.put("createid", userinfo.get("userid"));
 						this.indexService.insertReleaseRangeUser(releaserangemap);
 					}
 				}
 				if(user.containsKey("userid") && !"".equals(userinfo.get("userid"))){
 					user.put("userid", user.get("userid"));
 					user.put("type", 1);
 					
// 					//添加到发布范围用户表
 					Map<String, Object>  releaserangemap=new HashMap<String, Object>();
					releaserangemap.put("companyid", userinfo.get("companyid"));
					releaserangemap.put("resourceid", templateid);
					releaserangemap.put("resourcetype", 11);
					releaserangemap.put("userid", user.get("userid"));
					releaserangemap.put("createid", userinfo.get("userid"));
					this.indexService.insertReleaseRangeUser(releaserangemap);
 				}
 				
 				//添加到发布范围表
				this.indexService.insertReleaseRange(user);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			data.put("status", 1);
			data.put("message", "添加失败");
		}
		
		return data;
	}
	
	/**
	 * 
	 * 修改模版（删除）
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@ResponseBody
	@RequestMapping(value="/updateTemplate")
	public Map<String,Object> updateTemplate(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String, Object> userinfo=UserUtil.getUser(request);
		
		if(map.containsKey("delflag") && String.valueOf(map.get("delflag")).equals("1")){
			insertManageLog(userinfo.get("companyid")+"",2,"每日报表","删除"+map.get("templatename")+"每日报表模版",userinfo.get("userid")+"");
		}
		Map<String, Object> data=new HashMap<String, Object>();
		
		map.put("updateid",userinfo.get("userid"));
		this.everydayReportService.updateTemplate(map);
		return data;
	}
	/**
	 * 
	 * 查询模版
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@ResponseBody
	@RequestMapping(value="/getTemplateInfo")
	public Map<String,Object> getTemplateInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String, Object> templateinfo=this.everydayReportService.getTemplateInfo(map);
		return templateinfo;
	}
	
	
	/**
	 * 
	 * 修改模版
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	
	@RequestMapping(value="/updateTemplateReport")
	@ResponseBody
	public Map<String,Object> updateTemplateReport(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> data = new HashMap<String, Object>();
		
		Map<String, Object> userinfo=UserUtil.getUser(request);
		Map<String,Object> orgmap = this.everydayReportService.getOrganizeByOrganizeid(map);
		map.put("organizename", orgmap.get("organizename"));
		insertManageLog(userinfo.get("companyid")+"",2,"每日报表","修改"+map.get("organizename")+"每日报表模版",userinfo.get("userid")+"");
		
		//修改模版
		Map<String, Object> templatemap=new HashMap<String, Object>();
		templatemap.put("templateid", map.get("templateid"));
		templatemap.put("companyid", userinfo.get("companyid"));
		templatemap.put("organizeid", map.get("organizeid"));
		templatemap.put("templatename", map.get("organizename")+"每日报表模版");
		templatemap.put("updateid", userinfo.get("userid"));
		this.everydayReportService.updateTemplate(templatemap);
		String templateid=map.get("templateid")+"";
		
		//删除发布范围
		Map<String, Object> deleterange=new HashMap<String, Object>();
		deleterange.put("resourceid", templateid);
		deleterange.put("resourcetype", 11);
		this.indexService.deleteRange(deleterange);
		//删除扩展表
		Map<String, Object> extendmap=new HashMap<String, Object>();
		extendmap.put("templateid", templateid);
		this.everydayReportService.deleteTemplateExtend(extendmap);
		try {
			JSONObject json=JSONObject.fromObject(map.get("datalist")+"");
			List<Map<String, Object>> extendlist=(List<Map<String, Object>>)json.get("extendlist");
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			//添加到字典扩展表
			for(Map<String, Object> extend:extendlist){
				extend.put("templateid", templateid);
				extend.put("createid", userinfo.get("userid"));
				this.everydayReportService.insertReportTemplateExtend(extend);
			}
			
			//添加到发布范围
			for(Map<String, Object> user:userlist){
				user.put("companyid", userinfo.get("companyid"));
 				user.put("resourceid", templateid);
 				user.put("resourcetype", 11);
 				if(user.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
 					user.put("organizeid", user.get("organizeid"));
 					user.put("type", 2);
 					
 					//添加到发布范围用户表
 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
 					for(Map<String, Object> user1:userinfolist){
 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
 						releaserangemap.put("companyid", userinfo.get("companyid"));
 						releaserangemap.put("resourceid", templateid);
 						releaserangemap.put("resourcetype", 11);
 						releaserangemap.put("userid", user1.get("userid"));
 						releaserangemap.put("createid", userinfo.get("userid"));
 						this.indexService.insertReleaseRangeUser(releaserangemap);
 					}
 				}
 				if(user.containsKey("userid") && !"".equals(userinfo.get("userid"))){
 					user.put("userid", user.get("userid"));
 					user.put("type", 1);
 					
 					//添加到发布范围用户表
 					Map<String, Object>  releaserangemap=new HashMap<String, Object>();
					releaserangemap.put("companyid", userinfo.get("companyid"));
					releaserangemap.put("resourceid", templateid);
					releaserangemap.put("resourcetype", 11);
					releaserangemap.put("userid", user.get("userid"));
					releaserangemap.put("createid", userinfo.get("userid"));
					this.indexService.insertReleaseRangeUser(releaserangemap);
 				}
 				
 				//添加到发布范围表
				this.indexService.insertReleaseRange(user);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			data.put("status", 1);
			data.put("message", "添加失败");
		}
		return data;
	}
}
