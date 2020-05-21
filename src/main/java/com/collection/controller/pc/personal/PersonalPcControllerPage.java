package com.collection.controller.pc.personal;

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
 * 个人中心
 *
 */
@Controller
@RequestMapping("/pc")
public class PersonalPcControllerPage extends BaseController {

	private transient static Log log = LogFactory
			.getLog(PersonalPcControllerPage.class);
	/**
	 *个人信息
	 */
	@RequestMapping("/memcenter_info")
	public String getMemcenterInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		return "/pc/member/memcenter_info";
	}
	/**
	 *个人信息修改
	 */
	@RequestMapping("/memcenter_infoedit")
	public String getMemcenterInfoEdit(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		return "/pc/member/memcenter_infoedit";
	}
	/**
	 *重置密码
	 */
	@RequestMapping("/password")
	public String getMemcenterPassword(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		return "/pc/member/password";
	}
	
	/**
	 *系统公告列表
	 */
	@RequestMapping("/notice_list")
	public String getMemcenterNotice(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		return "/pc/member/notice_list";
	}


}
