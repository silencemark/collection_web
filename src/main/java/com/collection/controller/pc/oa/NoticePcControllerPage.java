package com.collection.controller.pc.oa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.base.controller.BaseController;
import com.collection.util.UserUtil;

/**
 * oa办公
 *
 */
@Controller
@RequestMapping("/pc")
public class NoticePcControllerPage extends BaseController {

	/**
	 *通知列表
	 */
	@RequestMapping("/oa_notice_list")
	public String getPcOaNotice(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		return "/pc/oa/notice_list";
	}
	/**
	 *通知详情
	 */
	@RequestMapping("/oa_notice_detail")
	public String getPcOaNoticeDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/notice_detail";
	}
	
	/**
	 *新增通知详情
	 */
	@RequestMapping("/oa_notice_add")
	public String getPcOaNoticeAdd(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm");
		model.addAttribute("createtime",sdf.format(new Date()));
		return "/pc/oa/notice_add";
	}
	/**
	 *请假列表
	 */
	@RequestMapping("/getPcOaRestList")
	public String getPcOaRestList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		return "/pc/oa/rest_list";
	}
	/**
	 *请假详情
	 */
	@RequestMapping("/getPcOaRestDetail")
	public String getPcOaRestDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/rest_detail";
	}
	/**
	 *请假审批
	 */
	@RequestMapping("/getPcOaRestCheck")
	public String getPcOaRestCheck(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/rest_check";
	}
	/**
	 *请假申请
	 */
	@RequestMapping("/getPcOaRestAdd")
	public String getPcOaRestAdd(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm");
		model.addAttribute("createtime",sdf.format(new Date()));
		return "/pc/oa/rest_add";
	}


	/**
	 *请示列表
	 */
	@RequestMapping("/getPcOaAskList")
	public String getPcOaAskList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		return "/pc/oa/ask_list";
	}
	/**
	 *请示详情
	 */
	@RequestMapping("/getPcOaAskDetail")
	public String getPcOaAskDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/ask_detail";
	}
	/**
	 *请示审核
	 */
	@RequestMapping("/getPcOaAskCheck")
	public String getPcOaAskCheck(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/ask_check";
	}
	/**
	 *请示申请
	 */
	@RequestMapping("/getPcOaAskAdd")
	public String getPcOaAskAdd(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm");
		model.addAttribute("createtime",sdf.format(new Date()));
		return "/pc/oa/ask_add";
	}
	
	/**
	 *日志列表
	 */
	@RequestMapping("/getPcOaLogList")
	public String getPcOaLogList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		
		return "/pc/oa/log_list";
	}
	/**
	 *日志详情
	 */
	@RequestMapping("/getPcOaLogDetail")
	public String getPcOaLogDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/log_detail";
	}
	
	/**
	 *日报新增
	 */
	@RequestMapping("/getPcOaLogAddDay")
	public String getPcOaLogAddDay(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm");
		model.addAttribute("createtime",sdf.format(new Date()));
		SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("createdate",sdf1.format(new Date()));
		return "/pc/oa/add_dayreport";
	}
	/**
	 *周报新增
	 */
	@RequestMapping("/getPcOaLogAddWeek")
	public String getPcOaLogAddWeek(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm");
		model.addAttribute("createtime",sdf.format(new Date()));
		SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		date.setDate(date.getDate()-7);
		model.addAttribute("createdate",sdf1.format(date));
		model.addAttribute("enddate",sdf1.format(new Date()));
		return "/pc/oa/add_weekreport";
	}

	/**
	 *月报新增
	 */
	@RequestMapping("/getPcOaLogAddMonth")
	public String getPcOaLogAddMonth(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm");
		model.addAttribute("createtime",sdf.format(new Date()));
		SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		date.setDate(date.getDate()-30);
		model.addAttribute("createdate",sdf1.format(date));
		model.addAttribute("enddate",sdf1.format(new Date()));
		return "/pc/oa/add_monthreport";
	}

	/**
	 *通用审批列表
	 */
	@RequestMapping("/getPcOaApprovaList")
	public String getPcOaApprovalList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		
		return "/pc/oa/approval_list";
	}
	/**
	 *通用审批详情
	 */
	@RequestMapping("/getPcOaApprovaDetail")
	public String getPcOaApprovaDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/approval_detail";
	}
	/**
	 *通用审批
	 */
	@RequestMapping("/getPcOaApprovaCheck")
	public String getPcOaApprovaCheck(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/approval_check";
	}
	/**
	 *通用审批新增
	 */
	@RequestMapping("/getPcOaApprovaAdd")
	public String getPcOaApprovaAdd(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm");
		model.addAttribute("createtime",sdf.format(new Date()));
		return "/pc/oa/approval_add";
	}
	/**
	 *报销列表
	 */
	@RequestMapping("/getPcOaExpenseList")
	public String getPcOaExpenseList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		
		return "/pc/oa/expenseaccount_list";
	}
	/**
	 *报销详情
	 */
	@RequestMapping("/getPcOaExpenseDetail")
	public String getPcOaExpenseDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/expenseaccount_detail";
	}
	/**
	 *报销审批
	 */
	@RequestMapping("/getPcOaExpenseCheck")
	public String getPcOaExpenseCheck(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/expenseaccount_check";
	}
	/**
	 *报销新增
	 */
	@RequestMapping("/getPcOaExpenseAdd")
	public String getPcOaExpenseAdd(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		int ordernum=1;
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmm");
		model.addAttribute("orderno","SGD"+sdf.format(new Date())+String.format("%05d",(ordernum+1)));
		SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd HH:mm");
		model.addAttribute("createtime",sdf1.format(new Date()));
		return "/pc/oa/expenseaccount_add";
	}
	/**
	 *任务列表
	 */
	@RequestMapping("/getPcOaTaskList")
	public String getPcOaTaskList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		
		return "/pc/oa/task_list";
	}
	/**
	 *任务详情
	 */
	@RequestMapping("/getPcOaTaskDetail")
	public String getPcOaTaskDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/task_detail";
	}
	/**
	 *任务新增
	 */
	@RequestMapping("/getPcOaTaskAdd")
	public String getPcOaTaskeAdd(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm");
		model.addAttribute("createtime",sdf.format(new Date()));
		return "/pc/oa/task_add";
	}
	/**
	 *荣誉榜列表
	 */
	@RequestMapping("/getPcOaHonourList")
	public String getPcOaHonourList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		
		return "/pc/oa/honour";
	}
	/**
	 *员工关怀列表
	 */
	@RequestMapping("/getPcOaWelfareList")
	public String getPcOaWelfareList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		
		return "/pc/oa/welfare";
	}
	/**
	 *企业简报栏目
	 */
	@RequestMapping("/getPcOaBriefing")
	public String getPcOaBriefing(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		
		return "/pc/oa/briefing";
	}
	/**
	 *企业简报列表
	 */
	@RequestMapping("/getPcOaBriefingList")
	public String getPcOaBriefingList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/briefing_list";
	}
	/**
	 *企业简报详情
	 */
	@RequestMapping("/getPcOaBriefingDetail")
	public String getPcOaBriefingDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/briefing_detail";
	}
	/**
	 *备用金申请列表
	 */
	@RequestMapping("/getPcOaReserveList")
	public String getPcOaReserveList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		
		return "/pc/oa/reserve_list";
	}

	/**
	 *备用金申请详情
	 */
	@RequestMapping("/getPcOaReserveDetail")
	public String getPcOaReserveDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/reserve_detail";
	}
	/**
	 *备用金申请审批
	 */
	@RequestMapping("/getPcOaReserveCheck")
	public String getPcOaReserveCheck(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/reserve_check";
	}
	/**
	 *备用金申请新增
	 */
	@RequestMapping("/getPcOaReserveAdd")
	public String getPcOaReserveAdd(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("createtime",sdf.format(new Date()));
		return "/pc/oa/reserve_add";
	}
	/**
	 *企业网盘栏目
	 */
	@RequestMapping("/getPcOaSkydrive")
	public String getPcOaSkydrive(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		
		return "/pc/oa/skydrive";
	}

	/**
	 *企业网盘列表
	 */
	@RequestMapping("/getPcOaSkydriveList")
	public String getPcOaSkydriveList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/skydrive_list";
	}
	/**
	 *企业网盘详情
	 */
	@RequestMapping("/getPcOaSkydriveDetail")
	public String getPcOaSkydriveDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/skydrive_detail";
	}
	/**
	 *餐饮大师网盘列表
	 */
	@RequestMapping("/getPcOaSkydriveFood")
	public String getPcOaSkydriveFood(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/skydrive_food";
	}
	/**
	 *餐饮大师网盘列表
	 */
	@RequestMapping("/getPcOaSkydriveFoodList")
	public String getPcOaSkydriveFoodList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/skydrive_food_list";
	}
	/**
	 *餐饮大师网盘详情
	 */
	@RequestMapping("/getPcOaSkydriveFoodDetail")
	public String getPcOaSkydriveFoodDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		request.setAttribute("map", map);
		return "/pc/oa/skydrive_food_detail";
	}
}
