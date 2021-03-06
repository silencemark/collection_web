package com.collection.controller.app;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.redis.RedisUtil;
import com.collection.service.IAppLoginService;
import com.collection.service.IAppUserCenterService;
import com.collection.util.Constants;
import com.collection.util.Md5Util;
import com.collection.util.QRcode;
import com.collection.util.SharePictureBiz;

/**
 * 
 * @author silence
 *
 */
@Controller
@RequestMapping("/appUserCenter")
public class AppUserCenterController extends BaseController{
	
	@Resource private IAppUserCenterService appUserCenterService;
	
	@Resource private IAppLoginService appLoginService;
	
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
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
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
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		return this.appUserCenterService.signIn(map);
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
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		data = this.appUserCenterService.myGrowthValue(map);
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
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		Map<String, Object> certMap = this.appUserCenterService.getCertification(map);
		if(certMap!= null && certMap.size() > 0 && "0".equals(certMap.get("status").toString())) {
			data.put("status", 1);
			data.put("message", "已提交审核，请勿重新提交");
		} else if (certMap!= null && certMap.size() > 0 && "1".equals(certMap.get("status").toString())) {
			data.put("status", 1);
			data.put("message", "已实名审核通过，请勿重新提交");
		} else {
			data = this.appUserCenterService.certification(map);
		}
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
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		data = this.appUserCenterService.getCertification(map);
		return data;	
	}
	
	/**
	 * 我的团队/我的好友
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
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		data = this.appUserCenterService.myTeam(map);
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
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		data = this.appUserCenterService.myAssets(map);
		return data;	
	}
	
	
	/**
	 * 兑换提现记录
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
	public Map<String, Object> getExchangeList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		data.put("resultlist", this.appUserCenterService.getExchangeList(map));
		return data;	
	}
	
	/**
	 * 输入价格点击兑换/提现
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
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		return this.appUserCenterService.exchangeVipCard(map);
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
	 * @throws IOException 
	 */
	@RequestMapping("/myInviteCode")
	@ResponseBody
	public Map<String, Object> myInviteCode(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map<String, Object> codeMap = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			codeMap.put("status", 1);
			codeMap.put("message", "签名校验失败");
			return codeMap;
		}
		//获取我的邀请码和 qr二维码地址
		codeMap = this.appUserCenterService.myInviteCode(map);
		
		String codeContent = Constants.INVITE_PATH + codeMap.get("invitecodehttpurl");
		codeMap.put("invitecodehttpurl", codeContent);
		//如果没有qr二维码就生成一个入库
		if(codeMap.get("invitecodeqrcode") == null) {
			//用注册地址带参 生成二维码
			String organizeid = UUID.randomUUID().toString().replaceAll("-", "");
			QRcode qr = new QRcode();
			@SuppressWarnings("static-access")
			String qrcode=qr.getQRcode(codeContent , request, organizeid);
			
			String f = request.getSession().getServletContext().getRealPath("upload/qrcodes/");
			String newImg = "/"+System.currentTimeMillis()/1000l + "_invitecode.jpg";
			SharePictureBiz share = new SharePictureBiz();
			share.retrievePicture(f + newImg, request.getSession().getServletContext().getRealPath("/") + qrcode, codeMap.get("invitecode").toString());
			codeMap.put("invitecodeqrcode", "/upload/qrcodes/"+newImg);
			codeMap.put("qrcode", qrcode);
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
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		data = this.appUserCenterService.getMyUserInfo(map);
		return data;	
	}
	
	/**
	 * 修改头像
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updateHeadImg")
	@ResponseBody
	public Map<String, Object> updateHeadImg(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		return this.appUserCenterService.updateHeadImg(map);
	}
	
	/**
	 * 修改昵称
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updateNickname")
	@ResponseBody
	public Map<String, Object> updateNickname(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		return this.appUserCenterService.updateNickName(map);
		
	}
	
	/**
	 * 修改登录密码
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updatePassWord")
	@ResponseBody
	public Map<String, Object> updatePassWord(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//1、把密码都MD5加密
		map.put("oldpassword", Md5Util.getMD5(map.get("oldpassword").toString()));
		map.put("newpassword", Md5Util.getMD5(map.get("newpassword").toString()));
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		data  = this.appUserCenterService.updatePassWord(map);
		return data;	
	}
	
	/**
	 * 修改设置支付密码
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/setPayPassWord")
	@ResponseBody
	public Map<String, Object> setPayPassWord(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		//获取的手机号验证码
		Map<String, Object> checkcodeMap = RedisUtil.getObject(""+map.get("phone"));
		String checkcode = null;
		if (checkcodeMap != null && checkcodeMap.size() > 0) {
			checkcode = checkcodeMap.get("code").toString();
		}
		if (checkcode == null){
			data.put("status", 1);
			data.put("message", "验证码已过期，请重新获取");
		}
		if (map.get("checkcode").equals(checkcode)) {
			//支付密码加密
			map.put("paypassword", Md5Util.getMD5(map.get("paypassword").toString()));
			this.appUserCenterService.setPayPassWord(map);
			data.put("status", 0);
			data.put("message", "支付密码设置成功");
		} else {
			data.put("status", 1);
			data.put("message", "验证码输入错误");
		}
		return data;	
	}
	
	/**
	 * 收款方式管理页面查询
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPaymentMethod")
	@ResponseBody
	public Map<String, Object> getPaymentMethod(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		data = this.appUserCenterService.getPaymentMethod(map);
		return data;
	}
	
	/**
	 * 修改收款方式
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updateAddPaymentMethod")
	@ResponseBody
	public Map<String, Object> updateAddPaymentMethod(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		return this.appUserCenterService.updatePaymentMethod(map);
	}
	
	/**
	 * 获取我提过的问题 及回复
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getMyQuestion")
	@ResponseBody
	public Map<String, Object> getMyQuestion(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		data.put("resultlist", this.appUserCenterService.getMyQuestion(map));
		return data;
	}
	
	/**
	 * 提交问题投诉建议
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/addMyQuestion")
	@ResponseBody
	public Map<String, Object> addMyQuestion(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		this.appUserCenterService.addMyQuestion(map);
		data.put("status", 0);
		data.put("message", "提交成功");
		return data;
	}
	
	/**
	 * 获取用户的通知信息列表
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getUserNotice")
	@ResponseBody
	public Map<String, Object> getUserNotice(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		return this.appUserCenterService.getUserNotice(map);
	}
	
	/**
	 * 获取用户的未读消息数量
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getNoticeUnreadNum")
	@ResponseBody
	public Map<String, Object> getNoticeUnreadNum(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		return this.appUserCenterService.getNoticeUnreadNum(map);
	}
	
	/**
	 * 改变用户通知消息 的未读/已读状态
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updateNoticeStatus")
	@ResponseBody
	public Map<String, Object> updateNoticeStatus(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		this.appUserCenterService.updateNoticeStatus(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	
	/**
	 * 获取xgo明细记录列表信息
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getXgoRecord")
	@ResponseBody
	public Map<String, Object> getXgoRecord(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		data.put("resultlist", this.appUserCenterService.getXgoRecord(map));
		return data;
	}
	
	/**
	 * 转赠xgo给对方账户
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/giveXgoToOther")
	@ResponseBody
	public Map<String, Object> giveXgoToOther(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		//获取用户信息
		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("userid", map.get("userid"));
		userInfo = this.appLoginService.getUserInfo(userInfo);
		//加密支付密码
		String paypassword = Md5Util.getMD5(map.get("paypassword").toString());
		if (paypassword.equals(userInfo.get("paypassword"))){
			//校验正确支付密码
			data = this.appUserCenterService.giveXgoToOther(map);
		} else {
			data.put("status", 1);
			data.put("message", "支付密码错误");
		}
		return data;
	}
	
	/**
	 * 获取收益排行榜
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getRank")
	@ResponseBody
	public Map<String, Object> getRank(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		data.put("resultlist", this.appUserCenterService.getRank(map));
		return data;
	}
	
	/**
	 * 查询我的收货地址列表
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getAddressList")
	@ResponseBody
	public Map<String, Object> getAddressList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		data.put("resultlist", this.appUserCenterService.getAddressList(map));
		return data;
	}
	
	/**
	 * 新增/修改收货地址
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertOrUpdateAddress")
	@ResponseBody
	public Map<String, Object> insertOrUpdateAddress(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		//有地址主键id就是修改
		if(map.containsKey("addressid") && map.get("addressid") != null && !"".equals(map.get("addressid").toString())) {
			map.put("updatetime", new Date());
			this.appUserCenterService.updateAddress(map);
			data.put("status", 0);
			data.put("message", "修改收货地址成功");
			return data;
		} else {
			//新增地址
			map.put("createtime", new Date());
			this.appUserCenterService.insertAddress(map);	
			data.put("status", 0);
			data.put("message", "新增收货地址成功");
			return data;
		}
	}
	
	/**
	 * 设置默认收货地址
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/setDefaultAddress")
	@ResponseBody
	public Map<String, Object> setDefaultAddress(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		map.put("updatetime", new Date());
		this.appUserCenterService.setDefaultAddress(map);
		data.put("status", 0);
		data.put("message", "默认收货地址成功");
		return data;
	}
	
	/**
	 * 删除收货地址
	 * 传入参数{"userid":132010}
	 * 传出参数{"message":"查询成功","userInfo":{""},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/deleteAddress")
	@ResponseBody
	public Map<String, Object> deleteAddress(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		map.put("updatetime", new Date());
		this.appUserCenterService.deleteAddress(map);
		data.put("status", 0);
		data.put("message", "删除收货地址成功");
		return data;
	}
	
}
