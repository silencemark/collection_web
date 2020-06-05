package com.collection.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collection.service.IAppUserCenterService;
import com.collection.util.Constants;
import com.collection.util.QRcode;


/**
 * 
 * @author silence
 *
 */
@Controller
@RequestMapping("/appUserCenter")
public class AppUserCenterController {
	
	@Resource private IAppUserCenterService appUserCenterService;
	
	private Logger logger = Logger.getLogger(getClass());

	/**
	 * 进入我的个人中心
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/myCenter")
	@ResponseBody
	public Map<String, Object> myCenter(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		data = this.appUserCenterService.getMyCenter(map);
		return data;	
	}
	
	/**
	 * 签到
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/signIn")
	@ResponseBody
	public Map<String, Object> signIn(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		this.appUserCenterService.signIn(map);
		return data;	
	}
	
	/**
	 * 获取会员体系和我的成长值
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/myGrowthValue")
	@ResponseBody
	public Map<String, Object> myGrowthValue(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = this.appUserCenterService.myGrowthValue(map);
		return data;	
	}
	
	/**
	 * 进行实名认证
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/certification")
	@ResponseBody
	public Map<String, Object> certification(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		this.appUserCenterService.certification(map);
		data.put("status", 0);
		data.put("message", "提交成功");
		return data;	
	}
	
	
	/**
	 * 查询实名认证进程
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getCertification")
	@ResponseBody
	public Map<String, Object> getCertification(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = this.appUserCenterService.getCertification(map);
		return data;	
	}
	
	/**
	 * 我的团队
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/myTeam")
	@ResponseBody
	public Map<String, Object> myTeam(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = this.appUserCenterService.myTeam(map);
		return data;	
	}
	
	/**
	 * 我的资产页面
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/myAssets")
	@ResponseBody
	public Map<String, Object> myAssets(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = this.appUserCenterService.myAssets(map);
		return data;	
	}
	
	
	/**
	 * 兑换记录
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getExchangeList")
	@ResponseBody
	public List<Map<String, Object>> getExchangeList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String, Object>> data = this.appUserCenterService.getExchangeList(map);
		return data;	
	}
	
	/**
	 * 输入价格点击兑换
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/exchangeVipCard")
	@ResponseBody
	public Map<String, Object> exchangeVipCard(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		this.appUserCenterService.exchangeVipCard(map);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("status", 0);
		data.put("message", "兑换成功");
		return data;	
	}
	
	/**
	 * 获取的邀请码
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/myInviteCode")
	@ResponseBody
	public Map<String, Object> myInviteCode(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//获取我的邀请码和 qr二维码地址
		Map<String, Object> codeMap = this.appUserCenterService.myInviteCode(map);
		//如果没有qr二维码就生成一个入库
		if(codeMap.get("invitecodeqrcode") == null) {
			//用注册地址带参 生成二维码
			String organizeid=UUID.randomUUID().toString().replaceAll("-", "");
			String codeContent=Constants.PROJECT_PATH+"app/restaurant/appraise_add.html?organizeid="+organizeid;
			QRcode qr=new QRcode();
			String qrcode=qr.getQRcode(codeMap.get("invitecodehttpurl").toString(), request, organizeid);
			codeMap.put("invitecodehttpurl", qrcode);
			this.appUserCenterService.updateQrcode(codeMap);
		}
		return codeMap;	
	}
	

	/**
	 * 进入个人资料修改页面
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getMyUserInfo")
	@ResponseBody
	public Map<String, Object> getMyUserInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = this.appUserCenterService.getMyUserInfo(map);
		return data;	
	}
	
}
