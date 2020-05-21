package com.collection.controller.managebackstage;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.JCEMac.MD5;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.json.JSON;
import com.collection.redis.RedisUtil;
import com.collection.service.IndexService;
import com.collection.service.SystemService;
import com.collection.service.UserInfoService;
import com.collection.service.oa.OfficeService;
import com.collection.util.CookieUtil;
import com.collection.util.Md5Util;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;
import com.collection.util.SDKTestSendTemplateSMS;
import com.collection.util.UserUtil;

/**
 * 使用方后台管理企业简报
 * @author silence
 *
 */
@Controller
@RequestMapping("/managebackstage")
public class ManageIndexController {
	private transient static Log log = LogFactory.getLog(ManageIndexController.class);
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
				String password=Md5Util.getMD5(map.get("password")+""); 
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
					RedisUtil.setMap(cookiesid, userMap);
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
		Map<String, Object> indexInfo=this.systemService.getIndexInfo();
		model.addAttribute("indexInfo", indexInfo);
		
		//初始化分页
		PageHelper pageHelper = new PageHelper(request);
		pageHelper.initPage(map);
		List<Map<String, Object>> noticeList = systemService.getNoticeList(map);
		int num = systemService.getNoticeListNum(map);
		pageHelper.setTotalCount(num);
		model.addAttribute("noticeList", noticeList);
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
	 * 获取验证码
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getVerificationCode")
	@ResponseBody
	public Map<String, Object> getVerificationCode(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		//判断是否存在此用户
		Map<String, Object> userInfo=this.systemService.getUserInfo(map);
		String code="888888";
		if(userInfo != null && userInfo.size()>0){
			String phone=map.get("phone")+"";
			Random r = new Random();
			code = r.nextInt(10) + "" + r.nextInt(10) + "" + r.nextInt(10) + "" + r.nextInt(10)+ "" + r.nextInt(10)+ "" + r.nextInt(10);
			try {
				SDKTestSendTemplateSMS.sendSms(phone, code,"30");
				Map<String, Object> codemap=new HashMap<String, Object>();
				codemap.put("code", code);
				RedisUtil.setObject("system"+phone,codemap, 1);
				data.put("status", 0);
				data.put("message", "获取成功");
			} catch (Exception e) {
				// TODO: handle exception
				data.put("status", 1);
				data.put("message", "验证码发送失败");
			}
		}else{
			data.put("status", 1);
			data.put("message", "此用户不存在");
		}
		return data;
	}

	/**
	 * 校验验证码
	 * 传入参数{"phone":15000042335,"code":3668}
	 * 传出参数{"message":"验证码输入正确","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/checkValide")
	@ResponseBody
	public Map<String, Object> checkValide(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		String phone=map.get("phone")+"";
		Map<String, Object> codemap=RedisUtil.getObject("system"+phone);
		if(codemap!=null && codemap.size()>0){
			if(String.valueOf(codemap.get("code")).equals(String.valueOf(map.get("code")))){//验证码验证
				data.put("status", 0);
				data.put("message", "验证码输入正确");
				return data;
			}else{
				data.put("status", 1);
				data.put("message", "验证码错误，请重新输入");
				return data;
			}
		}else{
			data.put("status", 1);
			data.put("message", "验证码已过期，请重新获取");
			return data;
		}
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
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updateUserInfo")
	public String updateUserInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		String password=Md5Util.getMD5(map.get("password")+"");
		map.put("password", password);
		this.systemService.updateUserInfo(map);
		return "redirect:/managebackstage/login";
	}
}
