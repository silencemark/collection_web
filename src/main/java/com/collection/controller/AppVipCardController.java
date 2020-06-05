package com.collection.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
		data.put("status", 1);
		data.put("message", "支付成功，等待审核");
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
	
	/**
	 * 获取会员VIP交易卖出的vip会员卡列表
	 * 传入参数{"ordernum":"123456"}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getSaleCardList")
	@ResponseBody
	public List<Map<String, Object>> getSaleCardList(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String, Object>> cardlist=this.appVipCardService.getSaleCardList(map);
		return cardlist;
	}
	
	/**
	 * 点击审核进入审核页面
	 * 传入参数{"ordernum":"123456"}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initExamine")
	@ResponseBody
	public Map<String, Object> initExamine(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> examine=this.appVipCardService.getExamineInfo(map);
		return examine;
	}
	
	/**
	 * 立即审核通过
	 * 传入参数{"ordernum":"123456"}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examinePast")
	@ResponseBody
	public Map<String, Object> examinePast(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		this.appVipCardService.examinePast(map);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("status", 1);
		data.put("message", "审核成功");
		return data;
	}
	
	
	/**
	 * 获取会员VIP已拥有（审核通过）vip会员卡列表
	 * 传入参数{"ordernum":"123456"}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getMyCardList")
	@ResponseBody
	public List<Map<String, Object>> getMyCardList(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String, Object>> cardlist=this.appVipCardService.getMyCardList(map);
		return cardlist;
	}
	
	/**
	 * 获取会员VIP卡对应的视频包
	 * 传入参数{"cardid":"123456"}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getMovieByCardId")
	@ResponseBody
	public Map<String, Object> getMovieByCardId(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> cardlist=this.appVipCardService.getMemberCardInfo(map);
		return cardlist;
	}
	
	/**
	 * 进入出售页面获取会员卡信息，（VIP出售按钮触发）
	 * 传入参数{"cardid":"123456"}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initSellCard")
	@ResponseBody
	public Map<String, Object> initSellCard(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> cardlist=this.appVipCardService.getSellCardInfo(map);
		return cardlist;
	}
	
	/**
	 * 输入价格 点击立即出售
	 * 传入参数{"price":"123456"}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/commitSellCard")
	@ResponseBody
	public Map<String, Object> commitSellCard(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		this.appVipCardService.commitSellCard(map);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("status", 1);
		data.put("message", "审核成功");
		return data;
	}
}
