package com.collection.controller.managebackstage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.JCEMac.MD5;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.json.JSON;
import com.base.controller.BaseController;
import com.collection.controller.userbackstage.UserIndexController.ExportExcel;
import com.collection.redis.RedisUtil;
import com.collection.service.CompanyService;
import com.collection.service.IndexService;
import com.collection.service.SystemService;
import com.collection.service.UserInfoService;
import com.collection.service.oa.OfficeService;
import com.collection.util.CookieUtil;
import com.collection.util.Md5Util;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;
import com.collection.util.SDKTestSendTemplateSMS;
import com.collection.util.UserUtil;

/**
 * 使用方后台管理企业简报
 * @author silence
 *
 */
@Controller
@RequestMapping("/managebackstage")
public class ManageStatisticsController extends BaseController{
	private transient static Log log = LogFactory.getLog(ManageStatisticsController.class);
	@Resource private SystemService systemService;
	
	@Resource private CompanyService companyService;
	
	@Resource private UserInfoService userInfoService;
	
	@Resource private IndexService indexService;
	
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
	 * 今日活跃统计
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/getStatisticsToday")
	public String getStatisticsToday(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> data=new HashMap<String, Object>();
		//查询公司列表
		List<Map<String, Object>> companylist=this.companyService.getAllCompanyList(data);
		model.addAttribute("companylist", companylist);
		
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
			cal.add(Calendar.DAY_OF_MONTH, -30);
			cal1.add(Calendar.DAY_OF_MONTH, 0);
			map.put("starttime",sdf.format(cal.getTime()));
			map.put("endtime",sdf.format(cal1.getTime()));
		}
		if(!map.containsKey("companyid") || "".equals(map.get("companyid"))){
			List<Map<String, Object>> companycountlist=this.companyService.getCompanyStatistics(map);
			model.addAttribute("companycountlist", companycountlist);
		}
		map.put("isfristlogin", 0);
		int num = this.companyService.getCompanyUsedUserNum(map);
		map.remove("isfristlogin");
		List<Map<String, Object>> usercountlist=this.userInfoService.getUserStatistics(map);
		model.addAttribute("usercountlist", usercountlist);
		model.addAttribute("count",num);
		model.addAttribute("map", map);
		
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
        this.insertManageLog("", 1, "统计", "查看今日活跃统计信息。", userInfo.get("userid")+"");
		return "/managebackstage/statistics/count_daily";
	}
	
	/**
	 * 查询每个公司的活跃信息
	 * @param map
	 * @param response
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getCompanyActiveInfo")
	public Map<String,Object> getCompanyActiveInfo(@RequestParam Map<String,Object> map,
			HttpServletResponse response,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			PageHelper page = new PageHelper(request);
			int num = this.companyService.getAllCompanyListNum(map);
			page.setTotalCount(num);
			page.initPage(map);
			//查询公司列表
			List<Map<String, Object>> companylist=this.companyService.getAllComapnyListByPage(map);
			resultMap.put("comapnydetaillist", companylist);
			resultMap.put("pager", page.JSCateringPage().toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultMap;
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
		
		//初始化分页
		PageHelper pageHelper = new PageHelper(request);
		pageHelper.initPage(map);
		List<Map<String, Object>> companylist=this.companyService.getCompanyBlackList(map);
		int num =this.companyService.getCompanyBlackListNum(map);
		pageHelper.setTotalCount(num);
		model.addAttribute("pager", pageHelper.cateringPage().toString());
		model.addAttribute("map", map);
		model.addAttribute("companylist", companylist);
		
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
        this.insertManageLog("", 1, "统计", "查看红黑榜统计信息。", userInfo.get("userid")+"");
		return "/managebackstage/statistics/count_redblack";
	}
	
	/**
	 * 新增统计
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/getNewStatistics")
	public String getNewStatistics(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> data=new HashMap<String, Object>();
		
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
		List<Map<String, Object>> companycountlist=this.companyService.getNewCompanyStatistics(map);
		model.addAttribute("companycountlist", companycountlist);
		
		List<Map<String, Object>> usercountlist=this.userInfoService.getNewUserStatistics(map);
		model.addAttribute("usercountlist", usercountlist);
		
		model.addAttribute("map", map);
		
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
        this.insertManageLog("", 1, "统计", "查看新增统计信息。", userInfo.get("userid")+"");
		return "/managebackstage/statistics/count_increased";
	}
	
}
