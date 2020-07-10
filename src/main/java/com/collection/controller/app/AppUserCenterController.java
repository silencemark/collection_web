package com.collection.controller.app;

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
import com.collection.service.IAppUserCenterService;
import com.collection.util.Constants;
import com.collection.util.Md5Util;
import com.collection.util.QRcode;

/**
 * 
 * @author silence
 *
 */
@Controller
@RequestMapping("/appUserCenter")
public class AppUserCenterController extends BaseController{
	
	@Resource private IAppUserCenterService appUserCenterService;
	
	//private Logger logger = Logger.getLogger(getClass());

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
		this.appUserCenterService.signIn(map);
		data.put("status", 0);
		data.put("message", "签到成功");
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
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
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
			this.appUserCenterService.certification(map);
			data.put("status", 0);
			data.put("message", "提交成功");
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
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		data = this.appUserCenterService.exchangeVipCard(map);
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
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			codeMap.put("status", 1);
			codeMap.put("message", "签名校验失败");
			return codeMap;
		}
		//如果没有qr二维码就生成一个入库
		if(codeMap.get("invitecodeqrcode") == null) {
			//用注册地址带参 生成二维码
			String organizeid = UUID.randomUUID().toString().replaceAll("-", "");
			String codeContent = Constants.PROJECT_PATH + codeMap.get("invitecodehttpurl");
			QRcode qr = new QRcode();
			@SuppressWarnings("static-access")
			String qrcode=qr.getQRcode(codeContent , request, organizeid);
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
		this.appUserCenterService.updateNickName(map);
		data.put("status", 0);
		data.put("message", "昵称修改成功");
		return data;	
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
}
