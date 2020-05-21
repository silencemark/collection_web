package com.collection.controller.pc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.collection.service.IndexService;
import com.collection.util.UserUtil;

/**
 * 登陆相关的页面的跳转
 * @author 亚历山大
 *
 */
@Controller
@RequestMapping(value="/pc")
public class LoginJumPageController {

	@Resource private IndexService indexService;
	
	/**
	 * pc前端登陆页面
	 * @return
	 */
	@RequestMapping(value="/login")
	public String login(){
		return "/pc/home/login";
	}
	
	/**
	 * pc前端 index首页
	 * @return
	 */
	@RequestMapping(value="/index")
	public String indexPage(Model model,HttpServletRequest request){
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		List<Map<String, Object>> remindlist=this.indexService.getRemindList(userInfo);
		model.addAttribute("remindlist", remindlist);
		int remindnum=this.indexService.getRemindnum(userInfo);		
		model.addAttribute("remindnum", remindnum);
		model.addAttribute("userInfo", userInfo);
		return "/pc/home/index";
	}
	
	/**
	 * 进入重置密码页面
	 * @return
	 */
	@RequestMapping(value="/resetPassword")
	public String resetPassword(Model model,HttpServletRequest request){
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);
		return "/pc/home/reset_assword";
	}
	
	/**
	 * 进入公司选择页面
	 * @return
	 */
	@RequestMapping(value="/loginChangeCompany")
	public String loginChangeCompany(Model model,HttpServletRequest request){
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);
		return "/pc/home/login_choice";
	}
	
	/**
	 * 注册页面1
	 * @return
	 */
	@RequestMapping(value="/reg1")
	public String reg1(Model model,HttpServletRequest request){
		
		return "/pc/home/reg_01";
	}
	
	/**
	 * 注册页面2
	 * @return
	 */
	@RequestMapping(value="/reg2")
	public String reg2(@RequestParam Map<String, Object> map,Model model,HttpServletRequest request){
		model.addAttribute("map",map);
		return "/pc/home/reg_02";
	}

	/**
	 * 注册页面3
	 * @return
	 */
	@RequestMapping(value="/reg3")
	public String reg3(@RequestParam Map<String, Object> map,Model model,HttpServletRequest request){
		model.addAttribute("map",map);
		return "/pc/home/reg_03";
	}

	/**
	 * 注册页面4
	 * @return
	 */
	@RequestMapping(value="/reg4")
	public String reg4(Model model,HttpServletRequest request){
		return "/pc/home/reg_04";
	}
	
	/**
	 * 用户协议
	 * @return
	 */
	@RequestMapping(value="/agreement")
	public String agreement(Model model,HttpServletRequest request){
		return "/pc/home/agreement";
	}
	
	/**
	 * 忘记密码页面
	 * @return
	 */
	@RequestMapping(value="/forgetpass")
	public String forgetpass(Model model,HttpServletRequest request){
		return "/pc/home/find_password1";
	}
	
	/**
	 * 忘记密码选择公司页面
	 * @return
	 */
	@RequestMapping(value="/findcompanylist")
	public String findcompanylist(Model model,HttpServletRequest request){
		return "/pc/home/find_choice";
	}
	
	/**
	 * 重置密码页面
	 * @return
	 */
	@RequestMapping(value="/resetpass")
	public String resetpass(@RequestParam Map<String, Object> map,Model model,HttpServletRequest request){
		model.addAttribute("map",map);
		return "/pc/home/find_password2";
	}
}
