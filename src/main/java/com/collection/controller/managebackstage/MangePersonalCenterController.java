package com.collection.controller.managebackstage;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.service.ISystemService;
import com.collection.util.Md5Util;
import com.collection.util.UserUtil;


/**
 * 管理方后台管理
 * @author silence
 *
 */
@Controller
@RequestMapping("/managebackstage")
public class MangePersonalCenterController  extends BaseController {
	@Resource private ISystemService systemService;
	
	/**
	 * 得到当前用户的个人信息
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getSystemUser")
	public String getSystemUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		try {
			userinfo=this.systemService.getUserInfo(userinfo);
			request.setAttribute("userinfo", userinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "/managebackstage/memcenter/memcenter_info";
	}

	
	/**
	 * 得到当前用户的个人信息和所属部门
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getSystemUpdataUser")
	public String getSystemUpdataUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);	
		try {
			userinfo=this.systemService.getUserInfo(userinfo);
			request.setAttribute("userinfo", userinfo);
		} catch (Exception e) {
					e.printStackTrace();
		}
		return "/managebackstage/memcenter/memcenter_infoedit";
	}
	
	/**
	 * 修改个人基本信息
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updateSystemUserInfo")
	public String updateSystemUserInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		try {
			//修改个人基本信息
			this.systemService.updateUserInfo(map);	
			
			model.addAttribute("success", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "修改错误");
		}
		
		//修改完成返回查询页面
		return this.getSystemUser(map,model,request,response);
	}
	
	
	/**
	 *重置密码页面
	 */
	@RequestMapping("/getPassword")
	public String getPassword() {
		return "/managebackstage/memcenter/memcenter_password";
	}

	
	
	/**
	 * 修改密码
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updataPassword")
	@ResponseBody
	public Map<String, Object> updataPassword(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		map.put("password",Md5Util.getMD5(String.valueOf(map.get("password"))));
	
		try {
			
			// 验证密码是否正确
			this.systemService.getUserInfo(map);
			map.put("password",Md5Util.getMD5(String.valueOf(map.get("new_password"))));
			this.systemService.updateUserInfo(map);
			resultMap.put("success", "密码修改成功");
			resultMap.put("status", 0);
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("error", "密码修改错误");

		}
		return resultMap;
	}
}
