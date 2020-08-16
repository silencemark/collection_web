package com.collection.controller.app;

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
import com.collection.service.IAppIndexService;
import com.collection.util.FileDeal;


/**
 * 首页功能
 * @author silence
 *
 */
@Controller
@RequestMapping("/appIndex")
public class AppIndexController extends BaseController{
	@SuppressWarnings("unused")
	private transient static Log log = LogFactory.getLog(AppIndexController.class);
	
	@Resource private IAppIndexService appindexService;
	
	
	/**
	 * 首页banner图
	 * 传入参数{"type":"1"}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getBanner")
	@ResponseBody
	public List<Map<String, Object>> getBanner(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		return this.appindexService.getHomePageBanner(map);
	}
	
	/**
	 * 首页广告信息
	 * 传入参数{}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getAdvertisement")
	@ResponseBody
	public List<Map<String, Object>> getAdvertisement(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		return this.appindexService.getAdvertisement(map);
	}
	
	
	/**
	 * 首页免费影片列表
	 * 传入参数{"type":1， "userid":"45651"}
	 * 传出参数
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getHomePageMovie")
	@ResponseBody
	public List<Map<String, Object>> getHomePageMovie(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		return this.appindexService.getHomePageMovie(map);
	}
	
	/**
	 * 首页模糊查询影片接口
	 * 传入参数{"type":1， "userid":"45651"}
	 * 传出参数
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getHomePageVideoDesc")
	@ResponseBody
	public List<Map<String, Object>> getHomePageVideoDesc(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		return this.appindexService.getHomePageVideoDesc(map);
	}
	
	/**
	 * 上传朋友圈图片集合，发布动态
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addCommunity")
	@ResponseBody
	public Map<String, Object> addCommunity(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		return this.appindexService.addCommunity(map);
	}
	
	/**
	 * 查询朋友圈社区分享（传用户uid表示查自己的）
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getCommunityList")
	@ResponseBody
	public Map<String, Object> getCommunityList(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		return this.appindexService.getCommunityList(map);
	}
	
	/**
	 * 查询朋友圈社区分享详情（传分享ID）
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getCommunityDetail")
	@ResponseBody
	public Map<String, Object> getCommunityDetail(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		return this.appindexService.getCommunityDetail(map);
	}
	
	/**
	 * 点赞记录表点赞接口加一
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/likeCommunity")
	@ResponseBody
	public Map<String, Object> likeCommunity(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		this.appindexService.likeCommunity(map);
		data.put("status", 0);
		if("1".equals(map.get("status").toString())) {
			data.put("message", "点赞成功");
		} else {
			data.put("message", "取消点赞成功");
		}
		return data;
	}
	
	/**
	 * 查询朋友圈社区的评论
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getCommunityComment")
	@ResponseBody
	public Map<String, Object> getCommunityComment(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		data.put("resultlist", this.appindexService.getCommunityComment(map));
		return data;
	}
	
	/**
	 * 朋友圈社区新增评论
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addCommunityComment")
	@ResponseBody
	public Map<String, Object> addCommunityComment(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		this.appindexService.addCommunityComment(map);
		data.put("status", 0);
		data.put("message", "评论成功");
		return data;
	}
	
	/**
	 * 朋友圈社区新增回复
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addCommunityReply")
	@ResponseBody
	public Map<String, Object> addCommunityReply(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//校验登录token签名是否正确
		boolean signflag = checkToeknSign(map);
		if (!signflag){
			data.put("status", 1);
			data.put("message", "签名校验失败");
			return data;
		}
		this.appindexService.addCommunityReply(map);
		data.put("status", 0);
		data.put("message", "回复成功");
		return data;
	}
	
	/**
	 * 首页查询最新的一条通知消息
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getNewSystemNotice")
	@ResponseBody
	public Map<String, Object> getNewSystemNotice(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		return this.appindexService.getNewSystemNotice(map);
	}
}
