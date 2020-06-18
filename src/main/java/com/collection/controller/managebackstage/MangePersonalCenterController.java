package com.collection.controller.managebackstage;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.alibaba.fastjson.JSONArray;
import com.base.controller.BaseController;
import com.collection.redis.RedisUtil;
import com.collection.service.IndexService;
import com.collection.service.SystemService;
import com.collection.service.personal.PersonalService;
import com.collection.util.Constants;
import com.collection.util.HtmlUtil;
import com.collection.util.Md5Util;
import com.collection.util.PageHelper;
import com.collection.util.UserUtil;


/**
 * 管理方后台管理
 * @author Dsl
 *
 */
@Controller
@RequestMapping("/managebackstage")
public class MangePersonalCenterController  extends BaseController {
	private transient static Log log = LogFactory.getLog(MangePersonalCenterController.class);
	@Resource private SystemService systemService;
	@Resource private PersonalService personalService;
	@Resource private IndexService indexService;
	
	
	
	/**
	 * 得到当前用户的个人信息
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getSystemUser")
	public String getSystemUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		try {
			userinfo=this.systemService.getUserInfo(userinfo);
			request.setAttribute("userinfo", userinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
        this.insertManageLog("", 1, "个人中心", "查看"+userInfo.get("realname")+"的个人信息。", userInfo.get("userid")+"");
		return "/managebackstage/memcenter/memcenter_info";
	}

	
	/**
	 * 得到当前用户的个人信息和所属部门
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getSystemUpdataUser")
	public String getSystemUpdataUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);	
		try {
			userinfo=this.systemService.getUserInfo(userinfo);
			request.setAttribute("userinfo", userinfo);
		} catch (Exception e) {
					e.printStackTrace();
		}
		return "/managebackstage/memcenter/memcenter_infoedit";
	}
	
	/**
	 * 修改个人基本信息
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/updateSystemUserInfo")
	public String updateSystemUserInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		try {
			//修改个人基本信息
			this.systemService.updateUserInfo(map);	
			
			model.addAttribute("success", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "修改错误");
		}
		
        this.insertManageLog("", 1, "个人中心", "修改了"+userinfo.get("realname")+"的个人基本信息。", userinfo.get("userid")+"");
		//修改完成返回查询页面
		return this.getSystemUser(map,model,request,response);
	}
	
	
	/**
	 *重置密码页面
	 */
	@RequestMapping("/getPassword")
	public String getPassword() {
		return "/managebackstage/memcenter/memcenter_password";
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
	@ResponseBody
	public Map<String, Object> updataPassword(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		map.put("password",Md5Util.getMD5(String.valueOf(map.get("password"))));
	
		try {
			
			// 验证密码是否正确
			Map<String, Object> vmap = this.systemService.getUserInfo(map);
			if (vmap.size() < 1 && vmap == null) {
				resultMap.put("error", "密码修改错误");
				resultMap.put("status", 1);
			}
			map.put("password",Md5Util.getMD5(String.valueOf(map.get("new_password"))));
			this.systemService.updateUserInfo(map);
			resultMap.put("success", "密码修改成功");
			resultMap.put("status", 0);
			
	        this.insertManageLog("", 1, "个人中心", "修改了"+userinfo.get("realname")+"的密码。", userinfo.get("userid")+"");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("error", "密码修改错误");

		}
		return resultMap;
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
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
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
			model.addAttribute("error", "验证码错误");
		}
		// 获取发送到新手机的验证码
		Map<String , Object> codemapb=RedisUtil.getObject(map.get("phone").toString());
		String C_Code="";
		if(codemapb != null && codemapb.size() >0){
			C_Code=codemapb.get("code")+"";
		}
		// 验证新手机手机验证码
		if (!map.containsKey("C_Code") || !C_Code.equals(map.get("C_Code"))) {
			model.addAttribute("error", "验证码错误");
		}
		
		try {
			this.systemService.updateUserInfo(map);
			UserUtil.pushSystemUser(request, "phone",map.get("phone").toString());
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "手机号码修改错误");
		}
		this.insertManageLog("", 1, "个人中心", "修改了"+userinfo.get("realname")+"的手机号。", userinfo.get("userid")+"");
		return this.getSystemUser(map,model,request,response);
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
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		
		map.put("userid", userinfo.get("userid").toString());
		try {
			//修改用户信息
			this.systemService.updateUserInfo(map);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errors", "邮箱修改错误");
		}
		this.insertManageLog("", 1, "个人中心", "修改了"+userinfo.get("realname")+"的邮箱。", userinfo.get("userid")+"");
		return this.getSystemUser(map,model,request,response);
	}
	
	
	/**
	 * 系统公告列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getSystemMessage")
	public String getSystemMessage(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		
		try {
			
			PageHelper page = new PageHelper(request);
			int remindnum = this.systemService
					.getSystemMessageCount(map);
			page.setTotalCount(remindnum);
			page.initPage(map);
			
			//查询公告列表
			List<Map<String, Object>> dataList = this.systemService.getSystemMessage(map);
			request.setAttribute("dataList", dataList);	
			request.setAttribute("pager", page.cateringPage().toString());
			
			this.insertManageLog("", 1, "系统公告", "查询了系统公告列表信息", userinfo.get("userid")+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/memcenter/memcenter_notice";
	}
	
	/**
	 * 系统公告详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getSystemMessageDetail")
	public String getSystemMessageDetails(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		
		try {
			//查询公告列表
			List<Map<String, Object>> dataList = this.systemService.getSystemMessage(map);
			Map<String,Object> dataMap=new HashMap<String,Object>();
			dataMap  =  dataList!=null && dataList.size()>0?dataList.get(0):new HashMap<String,Object>();
			
			request.setAttribute("dataMap", dataMap);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.insertManageLog("", 1, "系统公告", "查询了系统公告详情信息。", userinfo.get("userid")+"");
		return "/managebackstage/memcenter/memcenter_noticedetail";
	}
	
	/**
	 * 系统公告修改
	 * @param map messageid
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getSystemMessageDetailUpdata")
	public String getSystemMessageDetailUpdata(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		
		try {
			//查询公告列表
			List<Map<String, Object>> dataList = this.systemService.getSystemMessage(map);
			Map<String,Object> dataMap=new HashMap<String,Object>();
			dataMap  =  dataList!=null && dataList.size()>0?dataList.get(0):new HashMap<String,Object>();
			
			request.setAttribute("dataMap", dataMap);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/memcenter/system_noticeupdata";
	}
	
	/**
	 * 修改系统公告
	 * @param map messageid
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/updataSystemMessageDetail")
	@ResponseBody
	public Map<String, Object>  updataSystemMessageDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		Map<String,Object> resultMap=new HashMap<String,Object>();
		try {
			
			if(map.containsKey("messageid")  && !"".equals(map.get("messageid"))){
				map.put("updateid", userinfo.get("userid").toString());
				map.put("content", HtmlUtil.returnHtmlStr(String.valueOf(map.get("content")), Constants.PROJECT_PATH));
				this.systemService.updataSystemMessageDetail(map);
				resultMap.put("status", 0);
				insertManageLog(userinfo.get("companyid")+"",1,"系统公告","修改公告信息",userinfo.get("userid")+"");
			}else{
				resultMap.put("status",1);
			}
		

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 *新增公告页面
	 */
	@RequestMapping("/noticeadd")
	public String noticeadd() {
		return "/managebackstage/memcenter/system_noticeadd";
	}
	/**
	 * 新增系统公告
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/insertSystemMessage")
	@ResponseBody
	public Map<String, Object> insertSystemMessage(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> resultMap=new HashMap<String,Object>();
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		
		try {
			if(map != null){
				map.put("createid", userinfo.get("userid").toString());
				map.put("content", HtmlUtil.returnHtmlStr(String.valueOf(map.get("content")), Constants.PROJECT_PATH));
				this.systemService.insertSystemMessage(map);
				 insertManageLog(userinfo.get("companyid")+"",1,"系统公告","发布公告",userinfo.get("userid")+"");
				resultMap.put("status",0);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status",1);
		}
		return resultMap;
	}
	
	
	/**
	 * 意见反馈列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getSystemBacklist")
	public String getSystemBacklist(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		
		try {
			
			PageHelper page = new PageHelper(request);
			int remindnum = this.systemService
					.getSystemBackCount(map);
			page.setTotalCount(remindnum);
			page.initPage(map);
			
			//查询公告列表
			List<Map<String, Object>> dataList = this.systemService.getSystemBacklist(map);
			request.setAttribute("dataList", dataList);	
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.insertManageLog("", 1, "系统反馈", "查询了系统反馈列表信息", userinfo.get("userid")+"");
		return "/managebackstage/memcenter/system_suggestions";
	}
	
	
	/**
	 * 意见反馈详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getSystemBackDetail")
	public String getSystemBackDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		
		try {
			List<Map<String, Object>> dataList = this.systemService.getSystemBacklist(map);
			
			Map<String,Object> dataMap=new HashMap<String,Object>();
			dataMap  =  dataList!=null && dataList.size()>0?dataList.get(0):new HashMap<String,Object>();
			if(Integer.parseInt(dataMap.get("status").toString()) == 1 ){
				 Map<String, Object> data = this.systemService.getSystemBackReply(map);
				 request.setAttribute("data", data);
			}
			
			request.setAttribute("dataMap", dataMap);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.insertManageLog("", 1, "系统反馈", "查询了系统反馈详细信息", userinfo.get("userid")+"");
		return "/managebackstage/memcenter/system_suggestionsdetail";
	}
	

	/**
	 * 意见反馈处理
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/updataSystemBack")
	@ResponseBody
	public Map<String, Object> updataSystemBack(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(map.containsKey("status") && !"".equals("status")){
				 int status = Integer.parseInt(map.get("status").toString());
				 if(status == 1){
					 map.put("isread", 0);
					 this.systemService.updataSystemBack(map);
					 this.systemService.insertSystemBackReply(map);
					 resultMap.put("message", "反馈成功");
					//推送信息
					String userid=map.get("userid")+"";
					String title=userinfo.get("realname")+"处理了您的反馈信息,请你查看！";
					String url="/member/system_suggestiondetail.html?feedbackid="+map.get("feedbackid")+"";
					JPushAndriodAndIosMessage(userid, title, url);
					
				 }else if(status == 2){
					 this.systemService.updataSystemBack(map);
					 resultMap.put("message", "忽略成功");
				 }
				 insertManageLog(userinfo.get("companyid")+"",1,"意见反馈","反馈处理",userinfo.get("userid")+"");
				 resultMap.put("status", 0);
				
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}

	/**
	 *pc头部菜单列表
	 */
	@RequestMapping("/getSystemPctop")
	public String getSystemPctop(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		
		try {
			PageHelper page = new PageHelper(request);
			int remindnum = this.systemService.getSystemPctopCount(map);
			page.setTotalCount(remindnum);
			page.initPage(map);
			
			List<Map<String, Object>> dataList = this.systemService.getSystemPctop(map);
			List<Map<String, Object>> modularList = this.systemService.getSystemPctopModular(map);
			request.setAttribute("dataList", dataList);	
			request.setAttribute("modularList", modularList);
			request.setAttribute("pager", page.cateringPage().toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/memcenter/system_headmenu";
	}
	
	/**
	 *	头部菜单详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getSystemPctopDetails")
	@ResponseBody
	public Map<String, Object> getSystemPctopDetails(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> dataList = this.systemService.getSystemPctop(map);
			
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap  =  dataList!=null && dataList.size()>0?dataList.get(0):new HashMap<String,Object>();
			resultMap.put("dataMap", dataMap);	
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status",1);
		}
		return resultMap;
	}

	/**
	 *	头部菜单设置修改
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/updateSystemPctop")
	@ResponseBody
	public Map<String, Object> updateSystemPctop(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(map.containsKey("codeid") && !"".equals("codeid")){
				map.put("updateid", userinfo.get("userid").toString());
				this.systemService.updateSystemPctop(map);
				if(map.containsKey("delflag")){
					insertManageLog(userinfo.get("companyid")+"",1,"头部菜单设置","头部菜单设置删除"+map.get("name")+"",userinfo.get("userid")+"");
				}
				resultMap.put("message", "修改成功");
				resultMap.put("status", 0);	
			}else{
				resultMap.put("message", "修改失败");
				resultMap.put("status", 1);	
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status",1);
		}
		return resultMap;
	}
	/**
	 *	头部菜单设置:新增
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/insertSystemPctop")
	@ResponseBody
	public Map<String, Object> insertSystemPctop(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(map != null ){
				map.put("createid", userinfo.get("userid").toString());
				this.systemService.insertSystemPctop(map);
				resultMap.put("message", "添加成功");
				resultMap.put("status", 0);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status",1);
		}
		return resultMap;
	}
	
	/**
	 *	字典管理
	 ** @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getSystemDict")
	public String getSystemDict(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		
		try {
			PageHelper page = new PageHelper(request);
			int remindnum = this.systemService.getSystemDictTypeCount(map);
			page.setTotalCount(remindnum);
			page.initPage(map);
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> dataList = this.systemService.getSystemDictType(map);
			
			if(dataList != null && dataList.size()> 0){
				for (int i = 0; i < dataList.size(); i++) {
					map.put("typeid", dataList.get(i).get("datatypeid"));
					List<Map<String, Object>> dictList = this.systemService.getSystemDict(map);
					list.addAll(dictList);
				}
			}
			
			request.setAttribute("dataList", dataList);	
			request.setAttribute("list", list);
			request.setAttribute("pager", page.cateringPage().toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/memcenter/system_dictionary";
	}
	
	/**
	 *	字典管理:修改
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/updateSystemDict")
	@ResponseBody
	public Map<String, Object> updateSystemDict(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(map != null ){
				map.put("updateid", userinfo.get("userid").toString());
				this.systemService.updateSystemDict(map);
				resultMap.put("message", "修改成功");
				resultMap.put("status", 0);
				if(map.containsKey("delflag")){
					insertManageLog(userinfo.get("companyid")+"",1,"字典管理","删除字典"+map.get("cname")+"",userinfo.get("userid")+"");
				}else{
					insertManageLog(userinfo.get("companyid")+"",1,"字典管理","修改字典"+map.get("name")+"",userinfo.get("userid")+"");
				}	   
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status",1);
		}
		return resultMap;
	}
	
	/**
	 *	字典管理:新增
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/insertSystemDict")
	@ResponseBody
	public Map<String, Object> insertSystemDict(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(map != null ){
				map.put("createid", userinfo.get("userid").toString());
				this.systemService.insertSystemDict(map);
				insertManageLog(userinfo.get("companyid")+"",1,"字典管理","新增字典"+map.get("cname")+"",userinfo.get("userid")+"");
				resultMap.put("message", "添加成功");
				resultMap.put("status", 0);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status",1);
		}
		return resultMap;
	}
	
	
	
	/**
	 *	配置无权限提醒
	 ** @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getSystemDictPower")
	public String getSystemDictPower(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		
		try {

			List<String> datacodelist=new ArrayList<String>();
			datacodelist.add(Constants.DICT_COMNOPOWER_MSG);//公司无权限code
			datacodelist.add(Constants.DICT_PERSONNOPOWER_MSG);//公司有权限员工无权限code
			map.put("datacodelist", datacodelist);
			List<Map<String, Object>> datalist = this.systemService.getSystemDict(map);
	
			request.setAttribute("datalist", datalist);	

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/memcenter/system_nonepower";
	}
	
	/**
	 *	配置无权限提醒:修改
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/updateSystemDictPower")
	@ResponseBody
	public Map<String, Object> updateSystemDictPower(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(map != null ){
				List<Map> list = JSONArray.parseArray(map.get("list").toString(), Map.class);;
	
				for(Map<String,Object> mm : list){
					mm.put("updateid", userinfo.get("userid").toString());
					this.systemService.updateSystemDict(mm);
				}
				resultMap.put("message", "修改成功");
				resultMap.put("status", 0);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status",1);
		}
		return resultMap;
	}
	
	/**
	 *	轮番图列表
	 ** @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getManageBanner")
	public String getManageBanner(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		
		try {
			map.put("type", 1);
			List<Map<String, Object>> datalist = this.systemService.getManageBanner(map);
	
			request.setAttribute("datalist", datalist);	

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/memcenter/system_img";
	}
	

	/**
	 *	轮番图：模块详情
	 ** @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getManageBannerDetail")
	public String getManageBannerDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		
		try {
			if(map.containsKey("model") && !"".equals("model")){
				map.put("type", 1);
				List<Map<String, Object>> datalist = this.systemService.getManageBanner(map);
				request.setAttribute("datalist", datalist);	
				request.setAttribute("model", map.get("model"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/memcenter/system_imgedit";
	}


	/**
	 *	轮番图:图片详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getBannerDetail")
	@ResponseBody
	public Map<String, Object> getBannerDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(map.containsKey("id")){
				 map.put("type", 1);
				 List<Map<String, Object>> datalist =this.systemService.getManageBanner(map);
				 resultMap.put("datalist", datalist);
				 resultMap.put("status", 0);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status",1);
		}
		return resultMap;
	}
	
	
	/**
	 *	轮番图:修改
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/updateManageBanner")
	@ResponseBody
	public Map<String, Object> updateManageBanner(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(map.containsKey("id") ){
				 map.put("type", 1);
				 this.systemService.updateManageBanner(map);
				 String content="";
				 String flag = "修改";
				 if(map.containsKey("delflag")){
					 flag="删除";
				 }
				 if(map.containsKey("model")){
						int mode = Integer.parseInt(map.get("model").toString()+"");
						if(mode == 1 ){
							content="首页";
						}else if(mode == 2 ){
							content="采购管理";
						}else if(mode == 3 ){
							content="仓库管理";
						}else if(mode == 4 ){
							content="工作表单";
						}else if(mode == 5 ){
							content="OA办公";
						}else if(mode == 6 ){
							content="店面管理";
						}else if(mode == 7 ){
							content="KPI星级管理";
						}  
					}
				insertManageLog(userinfo.get("companyid")+"",1,"轮播图",flag+content+"图片",userinfo.get("userid")+"");
				 resultMap.put("status", 0);
			}else{
				 resultMap.put("status", 1);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status",1);
		}
		return resultMap;
	}
	
	
	/**
	 *	轮番图:新增
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/insertManageBanner")
	@ResponseBody
	public Map<String, Object> insertManageBanner(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(map != null ){
				List<Map<String, Object>> datalist =this.systemService.getManageBannerSort(map);
				int priority = 0;
				if(datalist!=null && datalist.size()>0){
					priority = Integer.parseInt(datalist.get(0).get("priority").toString());
				}			
				map.put("priority", priority+1);
				map.put("createid", userinfo.get("userid").toString());
				map.put("type", 1);
				this.systemService.insertManageBanner(map);
				String content="";
				if(map.containsKey("model")){
					int mode = Integer.parseInt(map.get("model").toString()+"");
					if(mode == 1 ){
						content="首页";
					}else if(mode == 2 ){
						content="采购管理";
					}else if(mode == 3 ){
						content="仓库管理";
					}else if(mode == 4 ){
						content="工作表单";
					}else if(mode == 5 ){
						content="OA办公";
					}else if(mode == 6 ){
						content="店面管理";
					}else if(mode == 7 ){
						content="KPI星级管理";
					}  
				}
				insertManageLog(userinfo.get("companyid")+"",1,"轮播图","新增"+content+"图片",userinfo.get("userid")+"");
				resultMap.put("message", "添加成功");
				resultMap.put("status", 0);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status",1);
		}
		return resultMap;
	}
	/**
	 *	APP参数配置
	 ** @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getManageaAppconfig")
	public String getManageaAppconfig(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		try {
				List<Map<String, Object>> datalist = this.systemService.getManageaAppconfig(map);
				request.setAttribute("datalist", datalist);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/memcenter/system_varset";
	}
	/**
	 *	APP参数配置:修改
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/updateManageAppconfig")
	@ResponseBody
	public Map<String, Object> updateManageAppconfig(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//修改图片
			if(map.containsKey("codeid") || map.containsKey("model")){
				//更新启动页版本
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("codeid", map.get("codeid"));
				paramMap.put("model", map.get("model"));
				paramMap.put("updaterid", userinfo.get("userid"));
				indexService.updateSysParam(paramMap);
				
				 this.systemService.updateManageAppconfig(map);
				 if(map.containsKey("delflag")){
					 insertManageLog(userinfo.get("companyid")+"",1,"app参数配置","删除图片",userinfo.get("userid")+"");
				 }else{
					 insertManageLog(userinfo.get("companyid")+"",1,"app参数配置","修改图片",userinfo.get("userid")+"");
				 } 
			}
			//修改ios内容
			if(map.containsKey("ioscontent") && map.containsKey("iosurl")){
				map.put("model", "iosmodel");
				map.put("content", map.get("ioscontent"));
				map.put("url", map.get("iosurl"));
				this.systemService.updateManageAppconfig(map);
				map.remove("url");
				insertManageLog(userinfo.get("companyid")+"",1,"app参数配置","修改IOS内容",userinfo.get("userid")+"");
			}
			//修改android内容
			if(map.containsKey("androidcontent")){
				map.put("model", "androidmodel");
				map.put("content", map.get("androidcontent"));
				this.systemService.updateManageAppconfig(map);	
				insertManageLog(userinfo.get("companyid")+"",1,"app参数配置","修改android内容",userinfo.get("userid")+"");
			}
			resultMap.put("status", 0);
			resultMap.put("message", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", "修改失败");
			resultMap.put("status",1);
		}
		return resultMap;
	}	
	/**
	 *	APP参数配置:新增
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/insertManageAppconfigr")
	@ResponseBody
	public Map<String, Object> insertManageAppconfigr(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(map != null ){
				
				//更新启动页版本
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("codeid", map.get("codeid"));
				paramMap.put("model", map.get("model"));
				paramMap.put("updaterid", userinfo.get("userid"));
				indexService.updateSysParam(paramMap);
				
				int order = this.systemService.getManageaAppconfigOrder(map);
				map.put("order", order);
				map.put("createid", userinfo.get("userid").toString());
				this.systemService.insertManageAppconfigr(map);
				
				
				
				if(map.containsKey("content")){
					insertManageLog(userinfo.get("companyid")+"",1,"app参数配置","上传的Android应用市场",userinfo.get("userid")+"");
				}else{
					insertManageLog(userinfo.get("companyid")+"",1,"app参数配置","新增引导图",userinfo.get("userid")+"");
				}
				
				resultMap.put("message", "添加成功");
				resultMap.put("status", 0);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status",1);
		}
		return resultMap;
	}

}
