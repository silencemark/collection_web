package com.collection.controller.managebackstage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.base.controller.BaseController;
import com.collection.service.IManageStatisticsService;

/**
 * 后台管理统计相关
 * @author silence
 *
 */
@Controller
@RequestMapping("/managebackstage")
public class ManageStatisticsController extends BaseController{
	
	@Resource private IManageStatisticsService manageStatisticsService;
	
	/**
	 * 统计首页
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/statisticsIndex")
	public String statisticsIndex(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		return "/managebackstage/statistics/count";
	}
	
	/**
	 * 每日活跃用户统计
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/getStatisticsToday")
	public String getStatisticsToday(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		if((!map.containsKey("starttime") || "".equals(map.get("starttime"))) && (!map.containsKey("endtime") || "".equals(map.get("endtime")))){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String stoptime = sdf.format(new Date());
			Calendar cal = Calendar.getInstance();
			Calendar cal1 = Calendar.getInstance();
			try {
				cal.setTime(sdf.parse(stoptime));
				cal1.setTime(sdf.parse(stoptime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			cal.add(Calendar.DAY_OF_MONTH, -30);
			cal1.add(Calendar.DAY_OF_MONTH, 1);
			map.put("starttime",sdf.format(cal.getTime()));
			map.put("endtime",sdf.format(cal1.getTime()));
		}
		//每日活跃人数
		List<Map<String, Object>> activityuserlist=this.manageStatisticsService.getActivityStatistics(map);
		model.addAttribute("activityuserlist", activityuserlist);
		model.addAttribute("map", map);
		return "/managebackstage/statistics/activity_statistics";
	}
	
	
	
	/**
	 * 每日新增用户统计
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/getNewUserStatistics")
	public String getNewUserStatistics(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		if((!map.containsKey("starttime") || "".equals(map.get("starttime"))) && (!map.containsKey("endtime") || "".equals(map.get("endtime")))){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String stoptime = sdf.format(new Date());
			Calendar cal = Calendar.getInstance();
			Calendar cal1 = Calendar.getInstance();
			try {
				cal.setTime(sdf.parse(stoptime));
				cal1.setTime(sdf.parse(stoptime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			cal.add(Calendar.DAY_OF_MONTH, -6);
			cal1.add(Calendar.DAY_OF_MONTH, 1);
			map.put("starttime",sdf.format(cal.getTime()));
			map.put("endtime",sdf.format(cal1.getTime()));
		}
		//每日用户总数
		List<Map<String, Object>> sumuserlist=this.manageStatisticsService.getSumUserStatistics(map);
		model.addAttribute("sumuserlist", sumuserlist);
		//每日用户新增人数
		List<Map<String, Object>> newuserlist=this.manageStatisticsService.getNewUserStatistics(map);
		model.addAttribute("newuserlist", newuserlist);
		model.addAttribute("map", map);
		return "/managebackstage/statistics/user_statistics";
	}
	
	/**
	 * 红黑榜
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/getBlackList")
	public String getBlackList(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		
		if((!map.containsKey("starttime") || "".equals(map.get("starttime"))) && (!map.containsKey("endtime") || "".equals(map.get("endtime")))){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String stoptime = sdf.format(new Date());
			Calendar cal = Calendar.getInstance();
			Calendar cal1 = Calendar.getInstance();
			try {
				cal.setTime(sdf.parse(stoptime));
				cal1.setTime(sdf.parse(stoptime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cal.add(Calendar.DAY_OF_MONTH, -6);
			cal1.add(Calendar.DAY_OF_MONTH, 1);
			map.put("starttime",sdf.format(cal.getTime()));
			map.put("endtime",sdf.format(cal1.getTime()));
		}
		return "/managebackstage/statistics/count_redblack";
	}
	
	/**
	 * 订单 及金额 统计
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/getOrderStatistics")
	public String getNewStatistics(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		if((!map.containsKey("starttime") || "".equals(map.get("starttime"))) && (!map.containsKey("endtime") || "".equals(map.get("endtime")))){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String stoptime = sdf.format(new Date());
			Calendar cal = Calendar.getInstance();
			Calendar cal1 = Calendar.getInstance();
			try {
				cal.setTime(sdf.parse(stoptime));
				cal1.setTime(sdf.parse(stoptime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			cal.add(Calendar.DAY_OF_MONTH, -6);
			cal1.add(Calendar.DAY_OF_MONTH, 1);
			map.put("starttime",sdf.format(cal.getTime()));
			map.put("endtime",sdf.format(cal1.getTime()));
		}
		List<Map<String, Object>> ordercountlist=this.manageStatisticsService.getNewOrderStatistics(map);
		model.addAttribute("ordercountlist", ordercountlist);
		
		List<Map<String, Object>> ordermoneylist=this.manageStatisticsService.getNewMoneyStatistics(map);
		model.addAttribute("ordermoneylist", ordermoneylist);
		model.addAttribute("map", map);
		return "/managebackstage/statistics/count_increased";
	}
	
}
