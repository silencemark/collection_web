package com.collection.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

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

import com.collection.redis.RedisUtil;
import com.collection.service.CompanyService;
import com.collection.service.IndexService;
import com.collection.service.UserInfoService;
import com.collection.service.personal.PersonalService;
import com.collection.util.Constants;
import com.collection.util.Md5Util;
import com.collection.util.QRcode;
import com.collection.util.SDKTestSendTemplateSMS;

import net.sf.json.JSONObject;


/**
 * 
 * @author silence
 *
 */
@Controller
@RequestMapping("/app")
public class LoginController {
	private transient static Log log = LogFactory.getLog(LoginController.class);
	
	@Resource private IndexService indexService;
	
	@Resource private CompanyService companyService;
	
	@Resource private UserInfoService userInfoService;
	
	@Resource private PersonalService personalService;
	private Logger logger = Logger.getLogger(getClass());
	/**
	 * 获取验证码
	 * 传入参数{"phone":15000042335}
	 * 传出参数{"message":"获取成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 *//*
	@RequestMapping("/getVerificationCode")
	@ResponseBody
	public Map<String, Object> getVerificationCode(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		//传入有type=1是 修改用户时发送验证码   否则是注册时发送
		if(map.containsKey("type") && "1".equals(String.valueOf(map.get("type")))){
			List<Map<String, Object>> companylist=this.companyService.getCompanyListByPhone(map);
			if(companylist!=null && companylist.size()>0){
				return sendSms(map);
			}else{
				data.put("status", 1);
				data.put("message", "您的帐号不存在，请先注册");
				return data;
			}
		}else{
			Map<String, Object> userInfo=this.userInfoService.getUserInfo(map);
			if(userInfo != null && userInfo.size() > 0){
				data.put("status", 1);
				data.put("message", "此帐号已存在");
				return data;
			}
			return sendSms(map);
		}
	}
	
	*//**
	 * 发送短信验证码
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 *//*
	public Map<String, Object> sendSms(Map<String, Object> map) {
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
		Map<String, Object> codemap=new HashMap<String, Object>();
		codemap.put("code", code);
		RedisUtil.setObject(phone,codemap, 1);
		data.put("status", 0);
		data.put("message", "获取成功");
		return data;
	}
	
	*//**
	 * 注册  校验验证码
	 * 传入参数{"phone":15000042335,"code":3668}
	 * 传出参数{"message":"验证码输入正确","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 *//*
	@RequestMapping("/checkValide")
	@ResponseBody
	public Map<String, Object> checkValide(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		String phone=map.get("phone")+"";
		Map<String, Object> codemap=RedisUtil.getObject(phone);
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
	*//**
	 * 注册接口
	 * 传入参数{"companyname":"紫痕软件有限公司","realname":"silence","phone":15000042335,"password":123456}
	 * 传出参数{"message":"注册成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 *//*
	@RequestMapping("/register")
	@ResponseBody
	public Map<String, Object> register(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		//校验公司名称是否存在
		Map<String, Object> companymap=new HashMap<String, Object>();
		companymap.put("companyname", map.get("companyname"));
		companymap=this.companyService.getCompanyInfo(companymap);
		if(companymap!= null && companymap.size()>0){
			data.put("status", 1);
			data.put("message", "公司名称已存在，请重新输入");
			return data;
		}else{
			String userid=UUID.randomUUID().toString().replaceAll("-", "");
			
			String phone=map.get("phone")+"";
			//添加公司信息
			Map<String, Object> companyInfo=new HashMap<String, Object>();
			String companyid=UUID.randomUUID().toString().replaceAll("-", "");
			companyInfo.put("companyid", companyid);
			companyInfo.put("companyid", companyid);
			companyInfo.put("phone", phone);
			companyInfo.put("companyname", map.get("companyname"));
			companyInfo.put("createid", userid);
			this.companyService.insertCompanyInfo(companyInfo);
			
			//添加公司组织架构
			Map<String, Object> organizemap=new HashMap<String, Object>();
			String organizeid=UUID.randomUUID().toString().replaceAll("-", "");
			String datacode="";
			try {
				datacode=this.companyService.getMaxCompanyDataCode();
				datacode=(Integer.parseInt(datacode)+1)+"";
				organizemap.put("datacode", datacode);
			} catch (Exception e) {
				// TODO: handle exception
				log.error("字典编码获取错误");
			}
			organizemap.put("organizeid", organizeid);
			organizemap.put("lastcompanyid", companyid);
			organizemap.put("organizename", map.get("companyname"));
			organizemap.put("createid", userid);
			organizemap.put("priority", 1);
			organizemap.put("childnum", 1);
			organizemap.put("usernum", 1);
			this.companyService.insertOrganize(organizemap);
			
			//默认把公司名 添加成一个店面
			String organizeid1=UUID.randomUUID().toString().replaceAll("-", "");
			organizemap=new HashMap<String, Object>();
			//生成qrcode
			String codeContent=Constants.PROJECT_PATH+"app/restaurant/appraise_add.html?organizeid="+organizeid1;
			QRcode qr=new QRcode();
			String qrcode=qr.getQRcode(codeContent, request, organizeid1);
			
			organizemap.put("qrcode", qrcode);
			organizemap.put("type", 3);
			organizemap.put("parentid", organizeid);
			organizemap.put("datacode", datacode+"101");
			organizemap.put("companyid", companyid);
			organizemap.put("organizeid", organizeid1);
			organizemap.put("organizename", map.get("companyname"));
			organizemap.put("createid", userid);
			organizemap.put("createtime", new Date());
			organizemap.put("priority", 1);
			this.companyService.insertOrganizeMap(organizemap);
			//添加用户
			
			map.put("companyid", companyid);
			map.put("userid", userid);
			map.put("password", Md5Util.getMD5(map.get("password")+""));
			map.put("isfristlogin", 0);
			map.put("isinvite", 1);
			map.put("managerole", 3);
			map.put("gerRealPath", request.getSession().getServletContext().getRealPath(""));
			map.put("fileDirectory", Constants.FILEDIRECTORY);
			this.userInfoService.insertUserInfo(map);
			
			//修改公司创建者
			companyInfo=new HashMap<String, Object>();
			companyInfo.put("companyid", companyid);
			companyInfo.put("createid", userid);
			this.companyService.updateCompany(companyInfo);
			
			//注册成功的短信通知
			String website = Constants.PROJECT_PATH;
			SDKTestSendTemplateSMS.sendRegisterCompanySuccessMessage(phone, String.valueOf(map.get("companyname")), phone, website.substring(0,(website.length() - 1)), "40000 22628");
			
			data.put("status", 0);
			data.put("message", "注册成功");
			return data;
		}
		
		
	}
	
	*//**
	 * 找回密码第一步（根据手机号选择公司）
	 * 传入参数{"phone":15000042335,"code":3668}
	 * 传出参数{"message":"查询成功","companylist":[{"createtime":1468413492000,"companyid":"67702cc412264f4ea7d2c5f692070457","username":"15000042335","phone":"15000042335","updatetime":1468413498000,"createid":"690fb669ed4d40219964baad7783abd4","userid":"690fb669ed4d40219964baad7783abd4","companyname":"紫痕软件有限公司"}],"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 *//*
	@RequestMapping("/getCompanyByPhone")
	@ResponseBody
	public Map<String, Object> getCompanyByPhone(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>(); 
		String phone=map.get("phone")+"";
		Map<String, Object> codemap=RedisUtil.getObject(phone);
		if(codemap!=null && codemap.size()>0){
			if(String.valueOf(codemap.get("code")).equals(String.valueOf(map.get("code")))){//验证码验证
				List<Map<String, Object>> companylist=this.companyService.getCompanyListByPhone(map);
				if(companylist!=null && companylist.size()>0){
					data.put("status", 0);
					data.put("companylist", companylist);
					data.put("message", "查询成功");
					return data;
				}else{
					data.put("status", 1);
					data.put("message", "您的帐号不存在，请先注册");
					return data;
				}
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
	
	*//**
	 * 重置密码
	 * 传入参数{"userid":"690fb669ed4d40219964baad7783abd4","password":123456}
	 * 传出参数{"message":"修改成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 *//*
	@RequestMapping("/updateUserInfo")
	@ResponseBody
	public Map<String, Object> updateUserInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			Map<String, Object> usermap=new HashMap<String, Object>();
			usermap.put("userid", map.get("userid"));
			Map<String, Object> userInfo=this.userInfoService.getUserInfo(usermap);
			
			if(map.containsKey("password") && !"".equals(map.get("password"))){
				map.put("password", Md5Util.getMD5(map.get("password")+""));
			}
			if(userInfo!=null && userInfo.size()>0){
				this.userInfoService.updateUserInfo(map);
				data.put("status", 0);
				data.put("message", "修改成功");
			}else{
				data.put("status", 1);
				data.put("message", "用户不存在");
			}
			
		} catch (Exception e) {
			data.put("status", 1);
			data.put("message", "修改失败");
		}
		return data;
	}
	
	*//**
	 * 登录接口
	 * 传入参数{"username":15000042335,"password":132010}
	 * 传出参数{"message":"查询成功","companylist":[{"createtime":1468413492000,"companyid":"67702cc412264f4ea7d2c5f692070457","username":"15000042335","phone":"15000042335","updatetime":1468413498000,"createid":"690fb669ed4d40219964baad7783abd4","userid":"690fb669ed4d40219964baad7783abd4","companyname":"紫痕软件有限公司"}],"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 *//*
	@RequestMapping("/userlogin")
	@ResponseBody
	public Map<String, Object> userlogin(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		map.put("password", Md5Util.getMD5(map.get("password")+""));
		List<Map<String, Object>> companylist=this.companyService.getCompanyListByPhone(map);
		if(companylist!=null && companylist.size()>0){
			data.put("status", 0);
			data.put("companylist", companylist);
			data.put("message", "查询成功");
			return data;
		}else{
			data.put("status", 1);
			data.put("message", "帐号或者密码错误,请重新输入");
			return data;
		}
	}
	*//**
	 * 选择公司（当只有一个公司的时候直接调用接口）
	 * 传入参数{"userid":"690fb669ed4d40219964baad7783abd4"}
	 * 传出参数{"message":"查询成功","status":0,"userInfo":{"createtime":1468413494000,"companyid":"67702cc412264f4ea7d2c5f692070457","username":"15000042335","phone":"15000042335","sex":"1","userid":"690fb669ed4d40219964baad7783abd4","isfristlogin":0,"realname":"silence","status":1,"delflag":0,"companyname":"紫痕软件有限公司","isshowphone":0}}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 *//*
	@RequestMapping("/chooseCompany")
	@ResponseBody
	public Map<String, Object> chooseCompany(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		Map<String, Object> userInfo=this.userInfoService.getUserInfo(map);
		
		//查询用户权限
		Map<String, Object> powerMap=this.userInfoService.getUserPowerList(userInfo);
		
		userInfo.put("powerMap", powerMap);
		
		//查询用户权限
		Map<String, Object> companypowerMap=this.userInfoService.getCompanyPowerList(userInfo);
		
		userInfo.put("companypowerMap", companypowerMap);
		
		if(userInfo.containsKey("password")){userInfo.remove("password");}
		if(userInfo!= null && userInfo.size()>0){
			String logintoken = UUID.randomUUID().toString().replaceAll("-", "");
			userInfo.put(Constants.LOGIN_TOKENKEY, logintoken);
			data.put("status", 0);
			data.put("userInfo", userInfo);
			data.put("message", "查询成功");
			//登录成功修改最后登录时间
			Map<String, Object> loginmap=new HashMap<String, Object>();
			loginmap.put("logintime", new Date());
			loginmap.put("userid", userInfo.get("userid"));
			loginmap.put("companyid", userInfo.get("companyid"));
			this.userInfoService.updateLoginime(loginmap);
			String userid = (String) userInfo.get("userid");
			setLoginToken(logintoken, userid);
		}else{
			data.put("status", 1);
			data.put("message", "查询失败，用户信息错误");
		}
		return data;
	}

	private void setLoginToken(String loginToken, String userid) {
		Map<String,Object> tokenMap = RedisUtil.getMap(Constants.LOGIN_TOKENKEY);
		if(tokenMap==null){
			tokenMap = new HashMap<String,Object>();
		}
		tokenMap.put(userid, loginToken);
		RedisUtil.setMap(Constants.LOGIN_TOKENKEY, tokenMap);
	}
	
	*//**
	 * 判断 是否 第一次登录强制修改密码
	 * 传入参数{"userid":"690fb669ed4d40219964baad7783abd4","password":132010}
	 * 传出参数{"message":"修改成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 *//*
	@RequestMapping("/firstLoginUpdatePass")
	@ResponseBody
	public Map<String, Object> firstLoginUpdatePass(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("userid") && !"".equals(map.get("userid"))){
			map.put("password", Md5Util.getMD5(map.get("password")+""));
			map.put("isfristlogin", 0);
			this.userInfoService.updateUserInfo(map);
			Map<String, Object> userInfo=this.userInfoService.getUserInfo(map);
			//查询用户权限
			Map<String, Object> powerMap=this.userInfoService.getUserPowerList(userInfo);
			userInfo.put("powerMap", powerMap);
			data.put("userInfo", userInfo);
			data.put("status", 0);
			data.put("message", "修改成功");
		}else{
			data.put("status", 1);
			data.put("message", "修改失败，用户信息错误");
		}
		return data;
	}
	
	*//**
	 * 查询用户权限信息、
	 * 传入参数：userid
	 * @param map
	 * @param response
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping(value="/getUserPowerInfo")
	public Map<String,Object> getUserPowerInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			Map<String, Object> userInfo=this.userInfoService.getUserInfo(map);
			//查询用户权限
			Map<String, Object> powerMap=this.userInfoService.getUserPowerList(userInfo);
			data.put("status", 0);
			data.put("powerMap", powerMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			data.put("status", 1);
		}
		return data;
	}
	*//**
	 * 校验登录token
	 * @param map
	 * @param response
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping(value="/checklogintoken")
	public Map<String,Object> checkLoginToken(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		logger.debug(JSONObject.fromObject(map).toString());
		Map<String, Object> data=new HashMap<String, Object>();
		String userid = (String) map.get("userid");
		String logintoken = (String) map.get(Constants.LOGIN_TOKENKEY);
		//判断userid是否为空，为空则返回错误信息
		if(null==userid||"".equals(logintoken)){
			data.put("status", 1);
			data.put("message", "用户信息出错，请重新登录");
			return data;
		}
		//判断logintoken是否为空，为空则返回错误信息
		if(null==logintoken||"".equals(logintoken)){
			data.put("status", 1);
			data.put("message", "登录失效，请重新登录");
			return data;
		}
		Map<String,Object> tokenMap = RedisUtil.getMap(Constants.LOGIN_TOKENKEY);
		if(tokenMap == null || tokenMap.size() == 0){
			data.put("status", 1);
			data.put("message", "登录失效，请重新登录");
			return data;
		}
		logger.debug(JSONObject.fromObject(tokenMap).toString());
		logger.debug(tokenMap.get(userid));
		if(logintoken.equals(tokenMap.get(userid))){
			//最后一个权限更新时间
			String powerlastupdatetime = String.valueOf(map.get("powerlastupdatetime"));
			//获取用户权限更新时间
			String powerupdatetime = RedisUtil.getStringParam(Constants.POWER_UPDATE_TIME+userid);
			data.put("power", 1);
			if(powerupdatetime != null && powerupdatetime != ""){
				if(!powerlastupdatetime.equals(powerupdatetime)){
					data.put("power", 0);
					data.put("powertime", powerupdatetime);
				}
			}
			
			data.put("status", 0);
			data.put("message", "登录校验成功");
		}else{
			data.put("status", 1);
			data.put("message", "登录失效，请重新登录");
		}
		return data;
	}
	
	*//**
	 * 修改用户绑定的设备id
	 * @param map
	 * @param response
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping(value="/updateRegistrationIdByUserId")
	public String updateRegistrationIdByUserId(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		String msg = "success";
		try {
			//判断设备id和用户id都要必须存在
			if(map.containsKey("registrationid") && !"".equals(map.get("registrationid")) && map.containsKey("userid") && !"".equals(map.get("userid"))){
				this.userInfoService.updateUserRegistrationId(map);
			}else{
				msg = "error";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}*/
}
