package com.collection.controller.managebackstage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collection.service.IManageBackStageService;
import com.collection.service.IndexService;
import com.collection.service.SystemService;
import com.collection.service.UserInfoService;
import com.collection.util.CookieUtil;
import com.collection.util.Md5Util;
import com.collection.util.PageHelper;
import com.collection.util.UserUtil;

/**
 * 使用方后台管理
 * @author silence
 *
 */
@Controller
@RequestMapping("/managebackstage")
public class ManageBackstageController {
	private transient static Log log = LogFactory.getLog(ManageBackstageController.class);
	
	@Resource private IManageBackStageService manageBackstageService;
	
	@Resource private SystemService systemService;
	
	@Resource private IndexService indexService;

	@Resource private UserInfoService userInfoService;
	
	
	/**
	 * 用户登录
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	public String adminLogin(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		//查询权限菜单
		model.addAttribute("map", map);
		if(map.containsKey("username") && map.get("username").equals("")){
			model.addAttribute("errors", "请输入用户名");
			model.addAttribute("username", map.get("username"));
			return "/managebackstage/login";
		}
		if(map.containsKey("password") && map.get("password").equals("")){
			model.addAttribute("errors", "请输入密码");
			return "/managebackstage/login";
		}
		if(map.size() > 0){
			Object oldcode = map.get("code");
			Object newcode = request.getSession().getAttribute("systemcode");
			if(oldcode == null || oldcode.equals("") || !String.valueOf(oldcode).equalsIgnoreCase(String.valueOf(newcode))){
				model.addAttribute("errors", "验证码输入错误");
				return "/managebackstage/login";
			}else{
				String password = Md5Util.getMD5(map.get("password")+""); 
				map.put("password", password);
				Map<String, Object> userMap=this.systemService.getUserInfo(map);  
				if(userMap != null && userMap.size()>0){
					 if(userMap.containsKey("status") && String.valueOf(userMap.get("status")).equals("0")){
						 model.addAttribute("errors", "您的帐号已被禁用");
						return "/managebackstage/login";
					 }
					// 登陆成功生成唯一cookiesid
					String cookiesid = userMap.get("userid") + "_" + System.currentTimeMillis();
					// cookiesid存入cookies中,有效期30分钟
					CookieUtil.setCookie(response, UserUtil.SYSTEMINFO, cookiesid, "/", null);
					// 用户信息存入reids中,有效期30分钟
					/*RedisUtil.setMap(cookiesid, userMap);*/
					//本地测试
					request.getSession().setAttribute(cookiesid, userMap);
					// 存储登录来源
					CookieUtil.setCookie(response, UserUtil.FROMINFO, "ADMIN", "/",null);			
					
					return "redirect:/managebackstage/index";
				} 
				else{
					model.addAttribute("errors", "用户名或密码输入错误");
					return "/managebackstage/login";
				}
			}
		}else{
			return "/managebackstage/login";
		} 		
	}
	
	/**
	 * 
	 * 首页
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/index")
	public String index(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//查询首页信息
		/*Map<String, Object> indexInfo=this.systemService.getIndexInfo();
		model.addAttribute("indexInfo", indexInfo);*/
		
		//初始化分页
		PageHelper pageHelper = new PageHelper(request);
		pageHelper.initPage(map);
		//List<Map<String, Object>> noticeList = systemService.getNoticeList(map);
		int num = 10;
		pageHelper.setTotalCount(num);
		//model.addAttribute("noticeList", noticeList);
		model.addAttribute("pager", pageHelper.cateringPage().toString());
		model.addAttribute("map", map);
		return "/managebackstage/index";
	}
	
	
	/**
	 * 
	 * 忘记密码
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initforget")
	public String initforget(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		return "/managebackstage/password_forget";
	}
	

	/**
	 * 
	 * 初始化重置密码
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initreset")
	public String initreset(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userInfo=this.systemService.getUserInfo(map);
		model.addAttribute("userInfo", userInfo);
		return "/managebackstage/password_reset";
	}
	
	/**
	 * 
	 * 重置密码
	 * @param map
	 * @param model
	 * @param request  updateUserInfo
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updatePassword")
	public String updatePassword(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		String password=Md5Util.getMD5(map.get("password")+"");
		map.put("password", password);
		this.systemService.updateUserInfo(map);
		return "redirect:/managebackstage/login";
	}
	
	/**
	 * 获取用户信息列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getUserList")
	public String getUserList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.manageBackstageService.getUserListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.manageBackstageService.getUserList(map);
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/collection/userinfo_list";
	}
	
	/**
	 * 
	 * 修改用户信息（状态 删除等等）
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updateUserStatus")
	public String updateUserStatus(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		map.put("updatetime", new Date());
		this.manageBackstageService.updateUserInfo(map);
		return "redirect:/managebackstage/getUserList";
	}
	
	/**
	 * 获取banner管理列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getBannerList")
	public String getBannerList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.manageBackstageService.getBannerListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.manageBackstageService.getBannerList(map);
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/collection/banner_list";
	}
	
	/**
	 * 初始化添加banner
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/initAddOrUpdateBanner")
	public String initAddOrUpdateBanner(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		if(map!= null && map.containsKey("bannerid")){
			Map<String, Object> bannerInfo=this.manageBackstageService.getBannerInfo(map);
			model.addAttribute("bannerInfo", bannerInfo);
		}
		return "/managebackstage/collection/banner_edit";
	}

	/**
	 * 修改banner图
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/updateBanner")
	@ResponseBody
	public Map<String, Object> updateBanner(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> data=new HashMap<String, Object>();
		
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		try {
			map.put("updateid", userInfo.get("userid"));
			map.put("updatetime",new Date());
			this.manageBackstageService.updateBanner(map);
			data.put("status", 0);
			data.put("message", "修改成功");
		} catch (Exception e) {
			data.put("status", 1);
			data.put("message", "修改失败");
		}
		return data;
	}
	
	/**
	 * 新增banner图
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/insertBanner")
	@ResponseBody
	public Map<String, Object> insertBanner(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			map.put("createid", userInfo.get("userid"));
			map.put("createtime",new Date());
			this.manageBackstageService.insertBanner(map);
			data.put("status", 0);
			data.put("message", "添加成功");
		} catch (Exception e) {
			data.put("status", 1);
			data.put("message", "添加失败");
		}
		return data;
	}
	
	/**
	 * 删除banner图
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/deleteBanner")
	@ResponseBody
	public Map<String, Object> deleteBanner(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			map.put("status", 0);
			map.put("updateid", userInfo.get("userid"));
			map.put("updatetime",new Date());
			this.manageBackstageService.updateBanner(map);
			data.put("status", 0);
			data.put("message", "删除成功");
		} catch (Exception e) {
			data.put("status", 1);
			data.put("message", "删除失败");
		}
		return data;
	}
	
}
