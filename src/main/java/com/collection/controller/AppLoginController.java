package com.collection.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collection.service.IAppLoginService;
import com.collection.util.DateUtil;
import com.collection.util.Md5Util;
import com.collection.util.SDKTestSendTemplateSMS;


/**
 * 
 * @author silence
 *
 */
@Controller
@RequestMapping("/appLogin")
public class AppLoginController {
	private transient static Log log = LogFactory.getLog(AppLoginController.class);
	
	@Resource private IAppLoginService appLoginService;
	
	private Logger logger = Logger.getLogger(getClass());

	/**
	 * 发送短信验证码
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getVerificationCode")
	@ResponseBody
	public Map<String, Object> sendSms(Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> data=new HashMap<String, Object>();
		String phone=map.get("phone")+"";
		String code = "888888";
		// 发送验证码
		try {
			Random r = new Random();
			code = r.nextInt(10) + "" + r.nextInt(10) + "" + r.nextInt(10) + "" + r.nextInt(10)+ "" + r.nextInt(10)+ "" + r.nextInt(10);
			SDKTestSendTemplateSMS.sendRegisterVerificationCodeMessage(phone, code,"30");
		} catch (Exception e) {
			data.put("status", 1);
			data.put("message", "发送验证码错误");
			return data;
		}
		log.info("code=" + code + "-------------------------------------");
		/*Map<String, Object> codemap=new HashMap<String, Object>();
		codemap.put("code", code);
		RedisUtil.setObject(phone,codemap, 1);*/
		request.getSession().setAttribute(phone, code);
		data.put("status", 0);
		data.put("message", "获取成功");
		return data;
	}

	/**
	 * 注册接口
	 * 传入参数{"checkcode":"121351","invitecode":"liao1","phone":15000042335,"password":123456}
	 * 传出参数{"message":"注册成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/register")
	@ResponseBody
	public Map<String, Object> register(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		//获取校验验证码判断是否等于输入的验证码
		String checkcode = (String) request.getSession().getAttribute(map.get("phone").toString());
		if (checkcode == null ){
			data.put("status", 1);
			data.put("message", "验证码已过期，请重新获取");
		}
		if (map.get("checkcode").equals(checkcode)) {
			//查询手机号是否存在
			boolean phoneflag= appLoginService.checkPhone(map);
			//如果存在 提示
			if (phoneflag) {
				data.put("status", 1);
				data.put("message", "手机号已存在, 请找回密码");
			}
			//注册入库
			appLoginService.insertUserInfo(map);
			data.put("status", 0);
			data.put("message", "注册成功");
		} else {
			data.put("status", 1);
			data.put("message", "验证码错误");
		}
		return data;
	}

	/**
	 * 忘记找回密码
	 * 传入参数{"phone":"15000042335","checkcode":"123456","password":"123456"}
	 * 传出参数{"message":"修改成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/findPassWord")
	public Map<String, Object> findPassWord(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			//获取校验验证码判断是否等于输入的验证码
			String checkcode = (String) request.getSession().getAttribute(map.get("phone").toString());
			if (checkcode == null ){
				data.put("status", 1);
				data.put("message", "验证码已过期，请重新获取");
			}
			if (map.get("checkcode").equals(checkcode)) {
				//查询手机号是否存在
				boolean phoneflag= appLoginService.checkPhone(map);
				//如果存在 提示
				if (phoneflag) {
					//修改密码
					appLoginService.updateUserInfo(map);
					data.put("status", 0);
					data.put("message", "密码修改成功");
				}
				
			} else {
				data.put("status", 1);
				data.put("message", "验证码错误");
			}
		} catch (Exception e) {
			data.put("status", 1);
			data.put("message", "找回密码失败");
		}
		return data;
	}
	
	/**
	 *  修改密码
	 * 传入参数{"phone":"15000042335","checkcode":"123456","password":123456}
	 * 传出参数{"message":"修改成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updateUserInfo")
	@ResponseBody
	public Map<String, Object> updateUserInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			//获取用户信息
			Map<String, Object> userInfo = this.appLoginService.getUserInfo(map);
			//加密原密码
			String oldpassword = Md5Util.getMD5(map.get("oldpassword").toString());
			if (oldpassword.equals(userInfo.get("password"))){
				//校验正确修改密码
				this.appLoginService.updateUserInfo(map);
				data.put("status", 0);
				data.put("message", "修改成功");
			}else{
				data.put("status", 1);
				data.put("message", "修改失败，原密码输入错误");
			}
		} catch (Exception e) {
			data.put("status", 1);
			data.put("message", "修改失败");
		}
		return data;
	}
	
	/**
	 * 登录接口
	 * 传入参数{"username":15000042335,"password":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/userlogin")
	@ResponseBody
	public Map<String, Object> userlogin(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> data=new HashMap<String, Object>();
		map.put("password", Md5Util.getMD5(map.get("password").toString()));
		logger.info("用户"+map.get("phone"+"登录系统"+DateUtil.sysDateTime()));
		Map<String, Object> userInfo = this.appLoginService.login(map);
		if(userInfo!=null && userInfo.size()>0){
			if ("1".equals(userInfo.get("status"))){
				data.put("status", 1);
				data.put("message", "您好，您的账号已被禁用 ！");
				return data;
			} else if ("2".equals(userInfo.get("status"))){
				data.put("status", 1);
				data.put("message", "您好，您的账号已被冻结！");
				return data;
			}
			data.put("status", 0);
			data.put("userInfo", userInfo);
			data.put("message", "登录成功");
			return data;
		}else{
			data.put("status", 1);
			data.put("message", "帐号或者密码错误,请重新输入");
			return data;
		}
	}
}
