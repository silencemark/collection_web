package com.collection.controller.pc.personal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.collection.redis.RedisUtil;
import com.collection.service.IndexService;
import com.collection.service.UserInfoService;
import com.collection.service.personal.PersonalService;
import com.collection.util.Constants;
import com.collection.util.CookieUtil;
import com.collection.util.Generate_Verification_Code;
import com.collection.util.Md5Util;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;
import com.collection.util.QRcode;
import com.collection.util.SDKTestSendTemplateSMS;
import com.collection.util.UserUtil;
import com.collection.util.email.EmailUtil;

/**
 * 个人中心
 *
 */
@Controller
@RequestMapping("/pc")
public class PersonalPcController extends BaseController {

	private transient static Log log = LogFactory
			.getLog(PersonalPcController.class);

	@Resource
	PersonalService personalService;
	 
	@Resource private UserInfoService userInfoService;
	@Resource private IndexService indexService;

	/**
	 * 得到当前用户的个人信息和所属部门
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@ResponseBody
	@RequestMapping("/getOrganizeByUserAll")
	public Map<String, Object> getOrganizeByUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//得到当前的用户信息
		Map<String, Object> userinfo=new HashMap<String, Object>();
		
		userinfo=this.userInfoService.getUserInfo(map);
		
		try {
			//查询当前用户所属的部门
			List<Map<String, Object>> datalist = this.indexService
					.getOrganizeByUser(map);
			
			resultMap.put("userinfo", userinfo);
			resultMap.put("datalist", datalist);	
			resultMap.put("status", 0);	
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);	
		}
		return resultMap;
	}
	
	
	/**
	 * 修改个人基本信息
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@ResponseBody
	@RequestMapping("/updateOrganizeUserInfo")
	public Map<String, Object> updateOrganizeUserInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		String headimage = (String) map.get("headimage");
		/*Integer sex =  Integer.valueOf((String)map.get("sex"));
		if("/app/appcssjs/images/defaultheadimage.png".equals(headimage)||"/app/appcssjs/images/user_female.png".equals(headimage)||"/app/appcssjs/images/user_male.png".equals(headimage)){
			headimage =( sex==0?"/app/appcssjs/images/user_female.png":"/app/appcssjs/images/user_male.png");				
		}*/
		try {
			//修改个人基本信息
			if(map.containsKey("userid")){
				map.put("headimage", headimage);
				map.put("headimgtype", 1);
				if(map.containsKey("realname") && !"".equals(map.get("realname"))){
					map.put("getRealPath", request.getSession().getServletContext().getRealPath(""));
					map.put("fileDirectory", Constants.FILEDIRECTORY);
				}
				this.personalService.updateUserBasicInfo(map);	
				if(map.containsKey("realname")){
					UserUtil.pushPCUser(request, "realname", map.get("realname")+"");
				}
				if(map.containsKey("position")){
					UserUtil.pushPCUser(request, "position", map.get("position")+"");
				}
				if(map.containsKey("birthday")){
					UserUtil.pushPCUser(request, "birthday", map.get("birthday")+"");
				}
				if(map.containsKey("sex")){
					UserUtil.pushPCUser(request, "sex", map.get("sex")+"");
				}
				if(map.containsKey("headimage")){
					UserUtil.pushPCUser(request, "headimage",headimage);
				}
			}
			resultMap.put("message", "修改成功");
			resultMap.put("status", 0);	
			
			model.addAttribute("userInfo",userInfo);
//			String cookiesid = userInfo.get("userid") + "_" + System.currentTimeMillis();
//			// cookiesid存入cookies中,有效期30分钟
//			CookieUtil.setCookie(response, UserUtil.PCUSERINFO, cookiesid, "/", null);
//			// 用户信息存入reids中,有效期30分钟
//			RedisUtil.setMap(cookiesid, userInfo);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", "修改错误");
			resultMap.put("status", 1);	
		}
		//修改完成返回查询页面
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
	@ResponseBody
	@RequestMapping("/updataPhoen")
	public Map<String, Object> updataPhoen(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 获取发送到手机的验证码
		//String V_Code = "1111";// (String)
		Map<String , Object> codemapa=RedisUtil.getObject(map.get("u_phone").toString());
		String V_Code="";
		if(codemapa != null && codemapa.size() >0){
			V_Code=codemapa.get("code")+"";
		}
		// 验证原手机验证码
		if (!map.containsKey("V_Code") || !V_Code.equals(map.get("V_Code"))) {
			resultMap.put("error", "验证码错误");
			resultMap.put("status", 1);	
			return resultMap;
		}
		// 获取发送到新手机的验证码
		Map<String , Object> codemapb=RedisUtil.getObject(map.get("phone").toString());
		String C_Code="";
		if(codemapb != null && codemapb.size() >0){
			C_Code=codemapb.get("code")+"";
		}
		// 验证新手机手机验证码
		if (!map.containsKey("C_Code") || !C_Code.equals(map.get("C_Code"))) {
			resultMap.put("error", "验证码错误");
			resultMap.put("status", 1);	
			return resultMap;
		}
		try {
			this.personalService.updateUserInfoPhoneAndPwd(map);
			resultMap.put("status", 0);	
			UserUtil.pushPCUser(request, "phone", map.get("phone")+"");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("error", "验证码错误");
			resultMap.put("status", 1);	
		}
		return resultMap;
	}

	/**
	 * 修改密码 new_password，userid，password ------>全部必填
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUserPassWordInfo", method = RequestMethod.POST)
	public Map<String, Object> updateUserPassWordInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response,HttpServletRequest request) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (!map.containsKey("new_password") || !map.containsKey("password")
				|| "".equals(map.get("password"))
				|| "".equals(map.get("new_password"))) {
			resultMap.put("status", 1);
			resultMap.put("message", "密码不能为空");
			return resultMap;
		}

		try {
			map.put("new_password",
					Md5Util.getMD5(String.valueOf(map.get("new_password"))));
			map.put("password",
					Md5Util.getMD5(String.valueOf(map.get("password"))));
			// 验证密码是否正确
			Map<String, Object> vmap = this.personalService
					.getVerificationPwd(map);
			if (vmap.size() < 1 && vmap == null) {
				resultMap.put("status", 1);
				resultMap.put("message", "密码错误");
				return resultMap;
			}
			// 修改
			this.personalService.updateUserInfoPhoneAndPwd(map);
			resultMap.put("status", 0);
			resultMap.put("message", "密码修改成功");
			UserUtil.pushPCUser(request, "password", map.get("password")+"");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "密码修改出错");
		}

		return resultMap;
	}

	/**
	 * 得到系统公告列表
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getSystemMessageListByUser")
	@ResponseBody
	public Map<String, Object> getNoticeList(
			@RequestParam Map<String, Object> map, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			
			PageHelper page = new PageHelper(request);
			int remindnum = this.personalService
					.getSystemMessageCountByUserId(map);
			page.setTotalCount(remindnum);
			page.initPage(map);
			
			//查询公告列表
			List<Map<String, Object>> dataList = this.personalService
					.getSystemMessageListByUserId(map);
			data.put("dataList", dataList);
			data.put("pager", page.JSCateringPage().toString());
			data.put("status", "0");
		} catch (Exception e) {
			e.printStackTrace();
			data.put("status", "1");
		}

		return data;
	}
	
	
	
	/**
	 * 得到系统公告详情
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getSystemMessageNoticeById")
	public String getNoticeById(
			@RequestParam Map<String, Object> map, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> userInfo=UserUtil.getPCUser(request);
		model.addAttribute("userInfo",userInfo);
		try {
			if(map.containsKey("messageid")){
				List<Map<String, Object>> dataList = this.personalService
						.getSystemMessageListByUserId(map);
				Map<String,Object> dataMap=new HashMap<String,Object>();
				dataMap  =  dataList!=null && dataList.size()>0?dataList.get(0):new HashMap<String,Object>();
				request.setAttribute("dataMap", dataMap);
			}
			request.setAttribute("status", "0");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("status", "1");
		}

		return "/pc/member/notice_detail";
	}


	
	/**
	 * 发送邮件
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updataEmail" , method = RequestMethod.POST)
	public Map<String,Object> getEmailVerificationCode(@RequestParam Map<String, Object> map, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		try {
			this.personalService.updateUserBasicInfo(map);
			data.put("status", "0");
		} catch (Exception e) {
			e.printStackTrace();
			data.put("status", "1");
		}

		
		return data;
	}

	
	
}
