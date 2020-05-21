package com.collection.controller.managebackstage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.service.CompanyService;
import com.collection.service.IndexService;
import com.collection.service.SystemService;
import com.collection.service.UserInfoService;
import com.collection.util.Md5Util;
import com.collection.util.PageHelper;
import com.collection.util.SDKTestSendTemplateSMS;
import com.collection.util.UserUtil;

/**
 * 使用方后台管理用户相关
 * @author silence
 *
 */
@Controller
@RequestMapping("/managebackstage")
public class ManageAdminController extends BaseController {
	private transient static Log log = LogFactory.getLog(ManageAdminController.class);
	@Resource private SystemService systemService;
	
	@Resource private CompanyService companyService;
	
	@Resource private UserInfoService userInfoService;
	
	@Resource private IndexService indexService;
	
	/**
	 * 管理方用户列表
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/getAdminUserList")
	public String getAdminUserList(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		//初始化分页
		PageHelper pageHelper = new PageHelper(request);
		pageHelper.initPage(map);
		List<Map<String, Object>> userlist=this.systemService.getAdminUserList(map);
		int num =this.systemService.getAdminUserListNum(map);
		pageHelper.setTotalCount(num);
		model.addAttribute("userlist", userlist);
		model.addAttribute("pager", pageHelper.cateringPage().toString());
		model.addAttribute("map", map);
		this.insertManageLog("", 1, "管理员管理", "查看了管理方人员列表。", userInfo.get("userid")+"");
		return "/managebackstage/administrator/manage_list";
	}
	
	/**
	 * 修改用户信息
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/updateAdminUser")
	@ResponseBody
	public Map<String, Object> updateAdminUser(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> data=new HashMap<String, Object>();
		
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		//记录管理操作日志 
		if(map.containsKey("status") && String.valueOf(map.get("status")).equals("1")){
			insertManageLog("",1,"管理员管理","启用管理员"+map.get("realname"),userInfo.get("userid")+"");
		}else if(map.containsKey("status") && String.valueOf(map.get("status")).equals("0")){
			insertManageLog("",1,"管理员管理","禁用管理员"+map.get("realname"),userInfo.get("userid")+"");
		}else if(map.containsKey("delflag") && String.valueOf(map.get("delflag")).equals("1")){
			insertManageLog("",1,"管理员管理","删除管理员"+map.get("realname"),userInfo.get("userid")+"");
		}else{
			insertManageLog("",1,"管理员管理","修改管理员"+map.get("realname"),userInfo.get("userid")+"");
		}
		
		
		try {
			map.put("updatetime",new Date());
			this.systemService.updateAdminUser(map);
			data.put("status", 0);
			data.put("message", "操作成功");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "操作失败");
		}
		return data;
	}
	
	/**
	 * 修改用户信息
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/insertAdminUser")
	@ResponseBody
	public Map<String, Object> insertAdminUser(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		//记录管理操作日志 
		insertManageLog("",1,"管理员管理","添加管理员"+map.get("realname"),userInfo.get("userid")+"");
		
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			map.put("updatetime",new Date());
			this.systemService.insertAdminUser(map);
			data.put("status", 0);
			data.put("message", "添加成功");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加失败");
		}
		return data;
	}
	
	

	/**
	 * 重置用户密码
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/resetAdminPass")
	@ResponseBody
	public Map<String, Object> resetAdminPass(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		
		Map<String, Object> data=new HashMap<String, Object>();
		Map<String, Object> userInfo=this.systemService.getUserInfo(map);
		if(userInfo != null && userInfo.size()>0){
			String phone=userInfo.get("phone")+"";
			String code = "888888";
			// 发送验证码
			try {
				Random r = new Random();
				code = r.nextInt(10) + "" + r.nextInt(10) + "" + r.nextInt(10) + "" + r.nextInt(10)+ "" + r.nextInt(10)+ "" + r.nextInt(10);
				SDKTestSendTemplateSMS.sendSmsMessage(phone, userInfo.get("companyname")+"",userInfo.get("username")+"", code);
				data.put("status", 0);
				data.put("message", "重置成功");
				
				userInfo.put("password", Md5Util.getMD5(code));
				this.systemService.updateAdminUser(userInfo);
				
				Map<String, Object> user=UserUtil.getSystemUser(request);
				//记录管理操作日志 
				insertManageLog("",1,"管理员管理",userInfo.get("realname")+"重置了密码",user.get("userid")+"");
				
			} catch (Exception e) {
				data.put("status", 1);
				data.put("message", "重置失败，请稍候再试");
				return data;
			}
		}else{
			data.put("status", 1);
			data.put("message", "用户不存在");
		}
		return data;
	}
	/**
	 * 管理方用户列表
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/getAdminUserInfo")
	public String getAdminUserInfo(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		
		Map<String, Object> userInfo=this.systemService.getUserInfo(map);
		model.addAttribute("userInfo", userInfo);
		return "/managebackstage/administrator/manage_detail";
	}
	/**
	 * 初始化添加管理方用户
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/initAddAdminUser")
	public String initAddAdminUser(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		
		if(map!= null && map.containsKey("userid")){
			Map<String, Object> userInfo=this.systemService.getUserInfo(map);
			model.addAttribute("userInfo", userInfo);
		}
		return "/managebackstage/administrator/manage_edit";
	}
}
