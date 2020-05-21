package com.collection.controller.pc.kpi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.collection.redis.RedisUtil;
import com.collection.util.UserUtil;


@Controller
@RequestMapping(value="/pc")
public class KpiJobStarControllerPage {

	/**
	 * 进入岗位星值列表页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getStarAssessList")
	public String getStarAssessList(@RequestParam Map<String,Object> map, HttpServletRequest request){
		request.setAttribute("user", getUserInfo(request));
		Map<String, Object> powerMap=RedisUtil.getMap(getUserInfo(request).get("userid")+"powerMap");
		request.setAttribute("powerMap", powerMap);
		request.setAttribute("map", map);
		return "/pc/kpi/jobstar_list";
	}
	
	/**
	 * 进入岗位星值添加页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/jobStarAdd")
	public String jobStarAdd(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("user", getUserInfo(request));
		Map<String, Object> powerMap=RedisUtil.getMap(getUserInfo(request).get("userid")+"powerMap");
		request.setAttribute("powerMap", powerMap);
		request.setAttribute("map", map);
		return "/pc/kpi/jobstar_add";
	}
	
	/**
	 * 进入岗位星值详情页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/jobStarDetail")
	public String jobStarDetail(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("user", getUserInfo(request));
		Map<String, Object> powerMap=RedisUtil.getMap(getUserInfo(request).get("userid")+"powerMap");
		request.setAttribute("powerMap", powerMap);
		request.setAttribute("map", map);
		return "/pc/kpi/jobstar_detail";
	}
	
	/**
	 * 進入审核页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/jobStarCheck")
	public String jobStarCheck(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("map", map);
		request.setAttribute("user", getUserInfo(request));
		Map<String, Object> powerMap=RedisUtil.getMap(getUserInfo(request).get("userid")+"powerMap");
		request.setAttribute("powerMap", powerMap);
		return "/pc/kpi/jobstar_check";
	}
	/**
	 * 获取登陆者信息
	 * @param request
	 * @return
	 */
	public Map<String,Object> getUserInfo(HttpServletRequest request){
		return UserUtil.getPCUser(request);
	}
	/**
	 * 综合星值列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/allstarList")
	public String allstarList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("user", getUserInfo(request));
		Map<String, Object> powerMap=RedisUtil.getMap(getUserInfo(request).get("userid")+"powerMap");
		request.setAttribute("powerMap", powerMap);
		request.setAttribute("map", map);
		return "/pc/kpi/allstar_list";
	}
	/**
	 * 综合星值详情页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/allstarDetail")
	public String allstarDetail(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("user", getUserInfo(request));
		Map<String, Object> powerMap=RedisUtil.getMap(getUserInfo(request).get("userid")+"powerMap");
		request.setAttribute("powerMap", powerMap);
		request.setAttribute("map", map);
		return "/pc/kpi/allstar_detail";
	}
	/**
	 * 综合星值审核
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/allstarCheck")
	public String allstarCheck(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("user", getUserInfo(request));
		Map<String, Object> powerMap=RedisUtil.getMap(getUserInfo(request).get("userid")+"powerMap");
		request.setAttribute("powerMap", powerMap);
		request.setAttribute("map", map);
		return "/pc/kpi/allstar_check";
	}
	/**
	 * 综合星值添加
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/allstarAdd")
	public String allstarAdd(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request){
		request.setAttribute("user", getUserInfo(request));
		request.setAttribute("map", map);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm");
		model.addAttribute("createtime",sdf.format(new Date()));
		Map<String, Object> powerMap=RedisUtil.getMap(getUserInfo(request).get("userid")+"powerMap");
		request.setAttribute("powerMap", powerMap);
		return "/pc/kpi/allstar_add";
	}
	/**
	 * 综合星值列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/rankingList")
	public String rankingList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("user", getUserInfo(request));
		Map<String, Object> powerMap=RedisUtil.getMap(getUserInfo(request).get("userid")+"powerMap");
		request.setAttribute("powerMap", powerMap);
		request.setAttribute("map", map);
		return "/pc/kpi/ranking_list";
	}
	/**
	 * 综合星值列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/ruleList")
	public String ruleList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("user", getUserInfo(request));
		Map<String, Object> powerMap=RedisUtil.getMap(getUserInfo(request).get("userid")+"powerMap");
		request.setAttribute("powerMap", powerMap);
		request.setAttribute("map", map);
		return "/pc/kpi/rule_list";
	}
	/**
	 * 综合星值列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/ruleDetail")
	public String ruleDetail(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("user", getUserInfo(request));
		Map<String, Object> powerMap=RedisUtil.getMap(getUserInfo(request).get("userid")+"powerMap");
		request.setAttribute("powerMap", powerMap);
		request.setAttribute("map", map);
		return "/pc/kpi/rule_detail";
	}
	
	/**
	 * 进入分享圈页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoSharePage")
	public String intoSharePage(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("user", getUserInfo(request));
		Map<String, Object> powerMap=RedisUtil.getMap(getUserInfo(request).get("userid")+"powerMap");
		request.setAttribute("powerMap", powerMap);
		request.setAttribute("map", map);
		return "/pc/share/share_index";
	}
}
