package com.collection.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.print.attribute.HashAttributeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.service.IAppVipCardService;


/**
 * VIP会员卡相关功能
 * @author silence
 *
 */
@Controller
@RequestMapping("/appVipCard")
public class AppVipCardController extends BaseController{
	@SuppressWarnings("unused")
	private transient static Log log = LogFactory.getLog(AppVipCardController.class);
	
	@Resource private IAppVipCardService appVipCardService;
	
	
	/**
	 * 获取会员VIP抢购列表页面
	 * 传入参数{"type":"1"}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getVipCardList")
	@ResponseBody
	public List<Map<String, Object>> getVipCardList(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String, Object>> cardlist=this.appVipCardService.getVipCardList(map);
		return cardlist;
	}
	
	/**
	 * VIP会员卡抢购入口逻辑处理
	 * 传入参数{"type":"1"}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/rushToBuyCard")
	@ResponseBody
	public Map<String, Object> rushToBuyCard(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		//抢购逻辑x需要思考一下
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("status", 0);
		data.put("message", "抢购成功");
		data.put("orderid", 11111);
		return data;
	}
	
	
	/**
	 * 获取VIP会员卡（待支付）列表页面
	 * 传入参数{"type":"1"}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getWaitPayCard")
	@ResponseBody
	public List<Map<String, Object>> getWaitPayCard(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String, Object>> cardlist=this.appVipCardService.getWaitPayCard(map);
		return cardlist;
	}
	
	
	/**
	 * 进入立即支付页面
	 * 传入参数{"ordernum":"123456"}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getPayVipCardInfo")
	@ResponseBody
	public Map<String, Object> getPayVipCardInfo(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> card=this.appVipCardService.getPayVipCardInfo(map);
		return card;
	}
	
	
	/**
	 * 上传支付凭证
	 * 传入参数{"ordernum":"123456"}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/payVipCard")
	@ResponseBody
	public Map<String, Object> payVipCard(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		this.appVipCardService.payVipCard(map);
		Map<String, Object> data = new HashMap<String, Object>();
		return data;
	}
	
	/**
	 * 联系卖家 获取卖家信息
	 * 传入参数{"ordernum":"123456"}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getSellUserInfo")
	@ResponseBody
	public Map<String, Object> getSellUserInfo(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = this.appVipCardService.getContactPhone(map);
		return data;
	}
	
}
