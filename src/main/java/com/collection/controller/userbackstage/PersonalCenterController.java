package com.collection.controller.userbackstage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.base.controller.BaseController;
import com.collection.controller.userbackstage.UserIndexController.ExportExcel;
import com.collection.redis.RedisUtil;
import com.collection.service.CompanyService;
import com.collection.service.IndexService;
import com.collection.service.UserInfoService;
import com.collection.service.personal.PersonalService;
import com.collection.util.Constants;
import com.collection.util.Md5Util;
import com.collection.util.PageHelper;
import com.collection.util.SDKTestSendTemplateSMS;
import com.collection.util.UserUtil;


/**
 * 使用方后台管理个人中心
 * @author Dsl
 *
 */
@Controller
@RequestMapping("/userbackstage")
public class PersonalCenterController extends BaseController {
	private transient static Log log = LogFactory.getLog(PersonalCenterController.class);
	@Resource private PersonalService personalService;
	
	@Resource private UserInfoService userInfoService;
	@Resource private IndexService indexService;
	@Resource private CompanyService companyService;
	
	/**
	 * 系统公告列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getSystemMessageListByUser")
	public String getSystemMessageListByUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getUser(request);
		map.put("userid", userinfo.get("userid").toString());
		
		try {
			
			PageHelper page = new PageHelper(request);
			int remindnum = this.personalService
					.getSystemMessageCountByUserId(map);
			page.setTotalCount(remindnum);
			page.initPage(map);
			
			//查询公告列表
			List<Map<String, Object>> dataList = this.personalService
					.getSystemMessageListByUserId(map);
			request.setAttribute("dataList", dataList);	
			request.setAttribute("userid", map.get("userid").toString());
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.insertManageLog(userinfo.get("companyid")+"", 2, "系统公告", "查看了公告列表信息。", userinfo.get("userid")+"");
		return "/userbackstage/memcenter/memcenter_notice";
	}
	
	
	/**
	 * 系统公告详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getSystemMessageNoticeById")
	public String getSystemMessageNoticeById(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getUser(request);
		map.put("userid", userinfo.get("userid").toString());
		try {
			
			//查询公告详情
			List<Map<String, Object>> dataList = this.personalService
					.getSystemMessageNoticeById(map);
		
			Map<String,Object> dataMap=new HashMap<String,Object>();
			dataMap  =  dataList!=null && dataList.size()>0?dataList.get(0):new HashMap<String,Object>();
			
			request.setAttribute("dataMap", dataMap);	
			model.addAttribute("data", "查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("data", "查询出错");
			return "/userbackstage/memcenter/memcenter_notice";
		}
		this.insertManageLog(userinfo.get("companyid")+"", 2, "系统公告", "查看了公告详情。", userinfo.get("userid")+"");
		return "/userbackstage/memcenter/memcenter_noticedetail";
	}
	
	
	
	
	/**
	 * 得到当前用户的个人信息和所属部门
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getOrganizeByUser")
	public String getOrganizeByUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getUser(request);
		
		userinfo=this.userInfoService.getUserInfo(userinfo);
		
		map.put("userid", userinfo.get("userid").toString());
		try {
			//查询当前用户所属的部门
			List<Map<String, Object>> datalist = this.indexService
					.getOrganizeByUser(map);
			if(datalist == null || datalist.size()<=0){
				List<Map<String,Object>> orglist = new ArrayList<Map<String,Object>>();
				Map<String,Object> orgmap = new HashMap<String, Object>();
				orgmap.put("organizename", userinfo.get("companyname"));
				orglist.add(orgmap);
				datalist = orglist;
			}
			request.setAttribute("userinfo", userinfo);
			request.setAttribute("datalist", datalist);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.insertManageLog(userinfo.get("companyid")+"", 2, "个人信息", "查看本人的详细信息。", userinfo.get("userid")+"");
		return "/userbackstage/memcenter/memcenter_info";
	}
	
	/**
	 * 修改个人基本信息
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/updateOrganizeUserInfo")
	public String updateOrganizeUserInfo(@RequestParam Map<String, Object> map,
			Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//Map<String, Object> data = new HashMap<String, Object>();
		// 得到当前的用户信息
		Map<String, Object> userinfo = UserUtil.getUser(request);
		/*String headimage = (String) map.get("headimage");
		Integer sex = Integer.valueOf((String) map.get("sex"));
		if ("/app/appcssjs/images/defaultheadimage.png".equals(headimage)
				|| "/app/appcssjs/images/user_female.png".equals(headimage)
				|| "/app/appcssjs/images/user_male.png".equals(headimage)) {
			headimage = (sex == 0 ? "/app/appcssjs/images/user_female.png"
					: "/app/appcssjs/images/user_male.png");
		}
		map.put("userid", userinfo.get("userid").toString());
		map.put("headimage", headimage);*/
		try {
			if(map.containsKey("realname") && !"".equals(map.get("realname"))){
				map.put("getRealPath", request.getSession().getServletContext().getRealPath(""));
				map.put("fileDirectory", Constants.FILEDIRECTORY);
				map.put("headimgtype", 1);
			}
			// 修改个人基本信息
			this.personalService.updateUserBasicInfo(map);
			//userinfo.put("headimage", headimage);

			request.setAttribute("userinfo", userinfo);
			model.addAttribute("data", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("data", "修改错误");
		}
		
		this.insertManageLog(userinfo.get("companyid")+"", 2, "个人信息", "修改了本人的基本信息。", userinfo.get("userid")+"");
		// 修改完成返回查询页面
		return "redirect:/userbackstage/getOrganizeByUser";
	}
	/**
	 * 查询当前需要修改的个人基本信息
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/selectUpdataUserInfo")
	public String selectUpdataUserInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getUser(request);
		try {
			//查询当前需要修改的个人基本信息,返回给修改页面
			userinfo=this.userInfoService.getUserInfo(userinfo);
			
			request.setAttribute("userinfo", userinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/userbackstage/memcenter/memcenter_infoedit";
	}

	/**
	 *重置密码页面
	 */
	@RequestMapping("/getPassword")
	public String getPassword() {
		return "/userbackstage/memcenter/memcenter_password";
	}
	
	/**
	 * 修改密码
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/updataPassword")
	public String updataPassword(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getUser(request);
		map.put("userid", userinfo.get("userid").toString());
		map.put("new_password",
				Md5Util.getMD5(String.valueOf(map.get("new_password"))));
		map.put("password",
				Md5Util.getMD5(String.valueOf(map.get("password"))));
		try {
			
			// 验证密码是否正确
			Map<String, Object> vmap = this.personalService
					.getVerificationPwd(map);
			if (vmap.size() < 1 && vmap == null) {
				model.addAttribute("errors", "密码修改错误");
				return "/userbackstage/memcenter/memcenter_password";
			}
			this.personalService.updateUserInfoPhoneAndPwd(map);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errors", "密码修改错误");
			return "/userbackstage/memcenter/memcenter_password";
		}
		this.insertManageLog(userinfo.get("companyid")+"", 2, "个人信息", "修改了本人的密码。", userinfo.get("userid")+"");
		return "redirect:/pc/login";
	}
	
	
	/**
	 * 修改手机号
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/updataPhoen")
	public String updataPhoen(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getUser(request);
		map.put("userid", userinfo.get("userid").toString());
		// 获取发送到手机的验证码
		//String V_Code = "1111";// (String)
		Map<String , Object> codemapa=RedisUtil.getObject(map.get("u_phone").toString());
		String V_Code="";
		if(codemapa != null && codemapa.size() >0){
			V_Code=codemapa.get("code")+"";
		}
		// 验证原手机验证码
		if (!map.containsKey("V_Code") || !V_Code.equals(map.get("V_Code"))) {
			model.addAttribute("errors", "验证码错误");
					return "redirect:/userbackstage/getOrganizeByUser";
		}
		// 获取发送到新手机的验证码
		Map<String , Object> codemapb=RedisUtil.getObject(map.get("phone").toString());
		String C_Code="";
		if(codemapb != null && codemapb.size() >0){
			C_Code=codemapb.get("code")+"";
		}
		// 验证新手机手机验证码
		if (!map.containsKey("C_Code") || !C_Code.equals(map.get("C_Code"))) {
			model.addAttribute("errors", "验证码错误");
					return "redirect:/userbackstage/getOrganizeByUser";
		}
		
		try {
			this.personalService.updateUserInfoPhoneAndPwd(map);
			UserUtil.pushUser(request, "phone",map.get("phone").toString());
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errors", "密码修改错误");
		}
		
		this.insertManageLog(userinfo.get("companyid")+"", 2, "个人信息", "修改了本人的手机号。", userinfo.get("userid")+"");
		return "redirect:/userbackstage/getOrganizeByUser";
	}
	
	/**
	 * 修改邮箱
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/updataEmail")
	public String updataEmail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getUser(request);
		
		userinfo=this.userInfoService.getUserInfo(userinfo);
		map.put("userid", userinfo.get("userid").toString());
	
		try {
			//修改用户信息
			this.personalService.updateUserBasicInfo(map);
			model.addAttribute("sueecss", "邮箱修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errors", "邮箱修改错误");
		}
		this.insertManageLog(userinfo.get("companyid")+"", 2, "个人信息", "修改了本人的邮箱。", userinfo.get("userid")+"");
		return this.getOrganizeByUser(map, model, request, response);
	}
	
	/**
	 * 人员管理：查询组织下人员信息页面一加载
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getNextOrgainize")
	public String getNextOrgainize(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getUser(request);
		Map<String, Object> powerMap=RedisUtil.getMap(userinfo.get("userid")+"powerMap");
		model.addAttribute("powerMap",powerMap);
		model.addAttribute("userinfo",userinfo);
		this.insertManageLog(userinfo.get("companyid")+"", 2, "组织架构", "查询了组织架构下人员信息。", userinfo.get("userid")+"");
		return "/userbackstage/business/business_personnel";
	}
	
	/**
	 * 人员管理：查询组织下人员信息
	  * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getNextOrgainizeById")
	@ResponseBody
	public Map<String,Object> getNextOrgainizeById(@RequestParam Map<String, Object> map,  HttpServletRequest request) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> userinfo=UserUtil.getUser(request);
//		map.put("userid", userinfo.get("userid")+"");
		map.put("companyid", userinfo.get("companyid")+"");
		try {
			PageHelper page = new PageHelper(request);
			int count = this.indexService.getUserListAllCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String, Object>> userlist=this.indexService.getUserListAll(map);
			resultMap.put("status", 0);
			resultMap.put("userlist", userlist);
			resultMap.put("userinfo", userinfo);
			resultMap.put("pager", page.JSCateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("userlist", "");
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	
	
	/**
	 * 人员管理：禁用/启用
	 * @param map 传入  必须参数userid，status，organizeid：所属的组织  ，list：多选的userid,
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getUpdataByUser")
	public String getUpdataByUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> mapnl=new HashMap<String, Object>();
		//选中多个人员,选中多少usernum便是多少，
		if(map.get("list") != null && !"".equals(map.get("list"))){
						//获取所选中的userid 获取所选中的organizeid	
			List<Map> list = JSONArray.parseArray(map.get("list").toString(), Map.class);	
			List<String> useridlist=new ArrayList<String>();
			for(int i=0;i<list.size();i++){  					
					String userid=list.get(i).get("userid").toString();
					useridlist.add(userid);
				}	
			map.put("usernum", list.size());
			map.put("useridlist", useridlist);
		}else{
			map.put("usernum",1);
		}
		try {
			if(map.containsKey("userid") || map.containsKey("useridlist")){
				int status =Integer.parseInt(map.get("status").toString());
				if(status ==  0){
					model.addAttribute("success", "禁用成功");
				}else{
					model.addAttribute("success", "启用成功");
				}
				this.personalService.updateUserBasicInfo(map);
			}
			model.addAttribute("status", 0);	
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("status", 1);
			model.addAttribute("errors", "操作失败");
		}
		Map<String, Object> userinfo=UserUtil.getUser(request);
		String content = "";
		if(map.containsKey("status") && "0".equals(map.get("status"))){
			content = "禁用了"+map.get("usernum")+"个人员。";
		}else{
			content = "启用了"+map.get("usernum")+"个人员。";
		}
		this.insertManageLog(userinfo.get("companyid")+"", 2, "组织架构", content, userinfo.get("userid")+"");
			return "redirect:/userbackstage/getNextOrgainize";
	}
/*
	*//**
	 * 人员管理：删除
	 * @param map 传入  必须参数userid，manageid，organizeid：所属的组织  ，list：多选的userid，manageid
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 *//*
	@RequestMapping("/getDeleteByUser")
	public String getDeleteByUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> mapnl=new HashMap<String, Object>();
		map.put("delflag", 1);
		//选中多个人员,选中多少usernum便是多少，
		if(map.get("list") != null && !"".equals(map.get("list"))){
			//获取所选中的userid 获取所选中的organizeid	
			List<Map> list = JSONArray.parseArray(map.get("list").toString(), Map.class);
			List<String> useridlist=new ArrayList<String>();
			List<String> manageidlist=new ArrayList<String>();
			List<String> organizelist = new ArrayList<String>();
			for(int i=0;i<list.size();i++){  					
				String userid=list.get(i).get("userid").toString();
				String manageid=list.get(i).get("manageid").toString();
				String organizeid = list.get(i).get("organizeid").toString();
				useridlist.add(userid);
				manageidlist.add(manageid);
				organizelist.add(organizeid);
			}	
			map.put("usernum", list.size());
			map.put("useridlist", useridlist);
			map.put("manageidlist", manageidlist);
			map.put("organizelist", organizelist);
		}
		try {
			if(map != null && !"".equals(map)){
				if(map.containsKey("userid") || map.containsKey("useridlist")){
					// 修改用户	
					this.personalService.updateUserBasicInfo(map);
					// 修改用户组织关联表
					if(map.containsKey("manageid") || map.containsKey("manageidlist")){
						this.personalService.updateOrganizeUserInfo(map);
					}
//					if(map.containsKey("usernum")){
//						map.put("usernum",1);
//					}
					if(map.containsKey("organizeid") || map.containsKey("organizelist")){
						this.personalService.updateUserOrganize(map);
					}
					
					//删除之后发送短信
					try {
						if(map.containsKey("useridlist") && !"".equals(map.get("useridlist"))){
							List<String> useridlist = (List<String>) map.get("useridlist");
							for(String userid : useridlist){
								Map<String,Object> usermap = new HashMap<String,Object>();
								usermap.put("userid", userid);
								//查询用户信息
								usermap = this.personalService.getUserInfo(usermap);
								String phone = String.valueOf(usermap.get("phone"));
								String username = String.valueOf(usermap.get("realname"));
								String content = "从【"+usermap.get("companyname")+"】移出";
								SDKTestSendTemplateSMS.sendDeleteUser(phone, username, content);
								
								//删除消息中的群组的信息
								try {
									//查询用户所在的群组的信息
									List<Map<String,Object>> grouplist = this.chatService.getGroupListInfoByUserid(usermap);
									if(grouplist != null && grouplist.size() > 0){
										for(Map<String,Object> groupmap : grouplist){
											//查询每个群组中的人数
											int usernum = this.chatService.getGroupUserNumByGroupId(groupmap);
											//当为一对一单聊
											if(usernum <=2 && usernum > 0){
												Map<String,Object> onemap = new HashMap<String,Object>();
												onemap.put("groupid", groupmap.get("groupid"));
												onemap.put("delflag", 1);
												this.chatService.deleteGroupUserInfo(onemap);
												this.chatService.updateGroupInfo(onemap);
											}else if(usernum > 2){//当为群聊的时候
												Map<String,Object> moremap = new HashMap<String,Object>();
												moremap.put("groupuserid", groupmap.get("groupuserid"));
												this.chatService.deleteGroupUserInfo(moremap);
												moremap.clear();
												String groupname = String.valueOf(groupmap.get("groupname"));
												String realname = String.valueOf(groupmap.get("realname"));
												int groupnum = groupname.indexOf(realname);
												int namenum = realname.length();
												if(groupnum > 0 && groupnum < (groupname.length() - namenum)){
													groupname = groupname.substring(0,groupnum - 1) + groupname.substring(groupnum+namenum , groupname.length());
												}else if(groupnum >= (groupname.length() - namenum)){
													groupname = groupname.substring(0 , groupnum - 1);
												}else if(groupnum == 0){
													groupname = groupname.substring(namenum + 1 , groupname.length());
												}
												moremap.put("groupname", groupname);
												moremap.put("groupid", groupmap.get("groupid"));
												this.chatService.updateGroupInfo(moremap);
											}
											//将删除人员未读的信息的状态改为已读
											this.chatService.updateChatRecordStatus(groupmap);
										}
									}
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								
							}
						}else if(map.containsKey("userid") && !"".equals(map.get("userid"))){
							//查询用户信息
							Map<String,Object> usermap = this.personalService.getUserInfo(map);
							String phone = String.valueOf(usermap.get("phone"));
							String username = String.valueOf(usermap.get("realname"));
							String content = "从【"+usermap.get("companyname")+"】移出";
							SDKTestSendTemplateSMS.sendDeleteUser(phone, username, content);
							
							//删除消息中的群组的信息
							try {
								//查询用户所在的群组的信息
								List<Map<String,Object>> grouplist = this.chatService.getGroupListInfoByUserid(map);
								if(grouplist != null && grouplist.size() > 0){
									for(Map<String,Object> groupmap : grouplist){
										//查询每个群组中的人数
										int usernum = this.chatService.getGroupUserNumByGroupId(groupmap);
										//当为一对一单聊
										if(usernum <=2 && usernum > 0){
											Map<String,Object> onemap = new HashMap<String,Object>();
											onemap.put("groupid", groupmap.get("groupid"));
											onemap.put("delflag", 1);
											this.chatService.deleteGroupUserInfo(onemap);
											this.chatService.updateGroupInfo(onemap);
										}else if(usernum > 2){//当为群聊的时候
											Map<String,Object> moremap = new HashMap<String,Object>();
											moremap.put("groupuserid", groupmap.get("groupuserid"));
											this.chatService.deleteGroupUserInfo(moremap);
											moremap.clear();
											String groupname = String.valueOf(groupmap.get("groupname"));
											String realname = String.valueOf(groupmap.get("realname"));
											int groupnum = groupname.indexOf(realname);
											int namenum = realname.length();
											if(groupnum > 0 && groupnum < (groupname.length() - namenum)){
												groupname = groupname.substring(0,groupnum - 1) + groupname.substring(groupnum+namenum , groupname.length());
											}else if(groupnum >= (groupname.length() - namenum)){
												groupname = groupname.substring(0 , groupnum - 1);
											}else if(groupnum == 0){
												groupname = groupname.substring(namenum + 1 , groupname.length());
											}
											moremap.put("groupname", groupname);
											moremap.put("groupid", groupmap.get("groupid"));
											this.chatService.updateGroupInfo(moremap);
										}
										//将删除人员未读的信息的状态改为已读
										this.chatService.updateChatRecordStatus(groupmap);
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			model.addAttribute("status", 0);
			model.addAttribute("success", "成功删除");
			
			Map<String, Object> userinfo=UserUtil.getUser(request);
			this.insertManageLog(userinfo.get("companyid")+"", 2, "组织架构", "删除了"+map.get("usernum")+"个人员。", userinfo.get("userid")+"");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("status",1);
			model.addAttribute("errors", "删除失败");
			
		}
		
			return "redirect:/userbackstage/getNextOrgainize";
	}*/
	
	/**
	 * 人员管理：邀请
	 * @param map 传入  必须参数userid，phone，username，organizename：所属的组织  ，list：多选的userid，phone,username
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/updateUserInvitation")
	public String updateUserInvitation(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> mapnl=new HashMap<String, Object>();
		int num =0 ;//判断邀请多少个
		try {
			//选中多个人员
			if(map.get("list") != null && !"".equals(map.get("list"))){
					//获取所选中的userid
					List<Map> list = JSONArray.parseArray(map.get("list").toString(), Map.class);
					List<String> useridlist=new ArrayList<String>();
					List<String> userPhoneList=new ArrayList<String>();
					
					for(int i=0;i<list.size();i++){  					
						//随机生成6位数密码
						Random random = new Random(); 
						int pass = 0;
						while(pass < 100000){
							pass = (int)(Math.random()*1000000);
						}
						String  password  = Md5Util.getMD5(String.valueOf(pass));
						
						String userid=list.get(i).get("userid").toString();
						map.put("userid", userid);
						Map<String, Object> userinfo=this.userInfoService.getUserInfo(map);
						String companyname=userinfo.get("companyname")+"";	
						String phone=list.get(i).get("phone")+"";	
						String username=list.get(i).get("username")+"";	
						
						//循环修改密码
						Map<String, Object> usermap = new HashMap<String, Object>();
						
						usermap.put("userid",userid);
						usermap.put("invitetime", new Date());
						usermap.put("isinvite", 1);
						usermap.put("password",password);
						this.personalService.updateUserBasicInfo(usermap);
						
						useridlist.add(userid);
						//调用手机短信发送加密前密码	
						boolean bool = SDKTestSendTemplateSMS.sendBeInvitationUserMessage(phone, username, String.valueOf(map.get("updatename")), String.valueOf(map.get("organizename")), phone, String.valueOf(pass));
						if(bool){
							num++;
						}
					}
				
			}else{
				if(map.containsKey("userid") || map.containsKey("useridlist")){
					Map<String, Object> userinfo=this.userInfoService.getUserInfo(map);
					// 修改为邀请状态
					map.put("invitetime", new Date());
					map.put("isinvite", 1);
					Random random = new Random(); 
					int pass = 0;
					while(pass < 100000){
						pass = (int)(Math.random()*1000000);
					}
					String  password  = Md5Util.getMD5(String.valueOf(pass));
					map.put("password", password);
					this.personalService.updateUserBasicInfo(map);
					//发送邀请信息
					boolean bool = SDKTestSendTemplateSMS.sendBeInvitationUserMessage(map.get("phone").toString(), String.valueOf(map.get("username")), String.valueOf(map.get("updatename")), String.valueOf(map.get("organizename")), map.get("phone").toString(), String.valueOf(pass));
					if(bool){
						num++;
					}
				}
			}
			model.addAttribute("status", 0);
			model.addAttribute("success", "邀请"+num+"个人员成功");
				
			Map<String, Object> userinfo=UserUtil.getUser(request);
			this.insertManageLog(userinfo.get("companyid")+"", 2, "组织架构", "邀请了"+num+"个人员。", userinfo.get("userid")+"");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("status", 0);
			model.addAttribute("errors", "邀请失败");
		}
		
		return "redirect:/userbackstage/getNextOrgainize";
	}
	

	/**
	 * 人员管理：得到用户个人信息和所属部门
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getUserByUserid")
	@ResponseBody
	public Map<String,Object> getUserByUserid(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String,Object>  userinfo=this.userInfoService.getUserInfo(map);
			//查询当前用户所属的部门
			List<Map<String, Object>> datalist = this.indexService.getOrganizeByUser(map);

			resultMap.put("status", 0);
			resultMap.put("datalist", datalist);
			resultMap.put("userinfo", userinfo);
			Map<String, Object> userInfo=UserUtil.getUser(request);
			this.insertManageLog(userInfo.get("companyid")+"", 2, "组织架构", "查询了"+userinfo.get("realname")+"的详细信息。", userInfo.get("userid")+"");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errors", "查询失败");
			return resultMap;
		}
			return resultMap;
	}
	
	/**
	 * 人员管理：修改人员的所属部门和职务
	 * @param map 用户的基本信息 必填：userid,  当前部门：organizeid 修改后的 updataorganizeid
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getUpdataUserByUserid")
	public String getUpdataUserByUserid(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> userinfo=UserUtil.getUser(request);
		try {
			if(map != null && !"".equals(map)){
				
				//修改当前部门的usernum-1,然后获取修改后的部门usernum+1并修改该用户的部门
				if(map.containsKey("organizelist") && !"".equals(map.get("organizelist"))){
					JSONObject json=JSONObject.fromObject(map.get("organizelist")+"");
					List<Map<String, Object>> organizelist=(List<Map<String, Object>>)json.get("organizelist");
					if(organizelist!= null && organizelist.size() > 0){
						//删除用户原有的组织
						this.personalService.deleteUserorganize(map);
						
						for(Map<String, Object> organize:organizelist){
							Map<String, Object> organizemap=new HashMap<String, Object>();
							organizemap.put("organizeid",organize.get("organizeid"));
							organizemap.put("userid",map.get("userid"));
							organizemap.put("companyid",map.get("companyid"));
							organizemap.put("createid", map.get("updateid"));
							this.companyService.insertOrganizeUser(organizemap);
							organizemap.put("usernum", 1);
							this.personalService.updateUserOrganizeAdd(organizemap);
						}
					}
				}
				if(map.containsKey("realname") && !"".equals(map.get("realname"))){
					map.put("getRealPath", request.getSession().getServletContext().getRealPath(""));
					map.put("fileDirectory", Constants.FILEDIRECTORY);
					map.put("headimgtype", 13);
				}
				//修改个人基本信息
				this.personalService.updateUserBasicInfo(map);	
				
				insertManageLog(userinfo.get("companyid")+"",2,"组织架构","修改"+map.get("realname")+""+"个人信息",userinfo.get("userid")+"");
				/*if(map.containsKey("updataorganizeid") || !"".equals(map.get("updataorganizeid"))){
					map.put("usernum",1);
					//当前部门的usernum -1
					this.personalService.updateUserOrganize(map);
					
					//如果没传manageid  那么久获取当前的的manageid
					if(!map.containsKey("manageid") && !"".equals(map.get("manageid"))){
						String manageid = this.personalService.selectDataManageid(map);
						map.put("manageid", manageid);
					}
					
					String organizeid = map.get("updataorganizeid").toString();
					map.put("organizeid",organizeid);
					//修改组织人员所属部门
					this.personalService.updateOrganizeUserInfo(map);
					//修改部门的usernum +1
					this.personalService.updateUserOrganizeAdd(map);
			
				}*/
			}	
			model.addAttribute("status", 0);
			model.addAttribute("success", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("status", 1);
			model.addAttribute("errors", "修改失败");
		}
		return "redirect:/userbackstage/getNextOrgainize";
	}
	
	/**
	 * 人员管理：新增人员
	 * @param map 用户的基本信息 必填：userid, organizeid 
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getInsertUserByUserid")
	@ResponseBody
	public  Map<String,Object> getInsertUserByUserid(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userinfo=UserUtil.getUser(request);
		map.put("companyid", userinfo.get("companyid"));
		map.put("createid", userinfo.get("userid"));
		
		
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			if(map != null && !"".equals(map)){
				map.put("username", map.get("phone"));
				//修改部门的usernum +1
				map.put("usernum", 1);
				//新增人员
				this.personalService.insertUserInfo(map);
				model.addAttribute("status", 0);
				resultMap.put("success", "添加成功");
			
			}
			
			this.insertManageLog(userinfo.get("companyid")+"", 2, "组织架构", "新增了一个名为："+map.get("realname")+"人员。", userinfo.get("userid")+"");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("status", 1);
			resultMap.put("errors", "添加失败");
		}
		return resultMap;
	}

	/**
	 * 
	 * 模糊查询
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	
	@ResponseBody
	@RequestMapping(value="/getSearchUserByAll")
	public Map<String,Object> getSearchUserByAll(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			Map<String, Object> userinfo=UserUtil.getUser(request);
			if(userinfo != null){
				if(userinfo.containsKey("managerole") && !"".equals(map.get("managerole"))){
					String managerole = String.valueOf(userinfo.get("managerole"));
					if("3".equals(managerole)){
						Map<String, Object> organizemap=new HashMap<String, Object>();
						organizemap.put("companyid", userinfo.get("companyid"));
						list = this.indexService.getOrganizeList(organizemap);
					}else if("2".equals(managerole)){
						Map<String, Object> organizemap=new HashMap<String, Object>();
						organizemap.put("userid", userinfo.get("userid"));
						organizemap.put("companyid", userinfo.get("companyid"));
						list = this.indexService.getOrganizeByUserList(organizemap);
					}
				}
			}
			List<String> organizes=new ArrayList<String>();
			for(Map<String, Object> organize:list){
				if(!String.valueOf(organize.get("isonlyread")).equals('1')){
					organizes.add(organize.get("organizeid")+"");
				}
			}
			map.put("organizes", organizes);
			List<Map<String, Object>> userlist=this.personalService.getUserListByOrganize(map);
			resultMap.put("userlist", userlist);
			resultMap.put("userinfo", userinfo);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("organizelist", "");
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 
	 * 导出人员管理
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportUserList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/人员管理列表.xls");
		Map<String, Object> userinfo=UserUtil.getUser(request);
		map.put("companyid", userinfo.get("companyid")+"");
		map.put("userid",userinfo.get("userid"+""));
		List<Map<String, Object>> loglist=this.indexService.getUserListAll(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "姓名","性别","所属部门", "职位","手机号码","创建时间","最后登录时间"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> log:loglist){
        	
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a", log.get("realname")==null?"":log.get("realname"));
	        if(log.containsKey("sex") && log.get("sex")!=null){
	        	if(Integer.parseInt(log.get("sex").toString())==1){
	        		 datamap.put("b","男");
	        	}else if(Integer.parseInt(log.get("sex").toString())==0){
	        		 datamap.put("b","女");
	        	}
	        	 
	        }else{
	        	datamap.put("b", "");
	        }
	        datamap.put("c", log.get("organizename")==null?"":log.get("organizename"));
	        datamap.put("d", log.get("position")==null?"":log.get("position"));
	        String phone = log.get("phone").toString();
	        datamap.put("e", Integer.parseInt(log.get("isshowphone").toString())==1?phone:phone.substring(0,3)+"****"+phone.substring(7,phone.length()));
	        datamap.put("f", log.get("new_createtime")==null?"":log.get("new_createtime"));
	        datamap.put("g", log.get("new_logintime")==null?"":log.get("new_logintime"));
	        dataset.add(datamap);
        }
        try  
        {
            OutputStream out = new FileOutputStream(importurl);  
            ex.exportExcel(headers, dataset, out);
            out.close();  
            System.out.println("excel导出成功！");
            
            this.insertManageLog(userinfo.get("companyid")+"", 2, "组织架构", "导出了人员信息。", userinfo.get("userid")+"");
        }  
        catch (FileNotFoundException e)  
        {
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }
		return "人员管理列表.xls";
	}
	
}
