package com.collection.controller.managebackstage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collection.frame.Crontab;
import com.collection.frame.Scheduler;
import com.collection.service.IManageBackStageService;
import com.collection.service.ISystemService;
import com.collection.util.CookieUtil;
import com.collection.util.DateUtil;
import com.collection.util.Md5Util;
import com.collection.util.PageHelper;
import com.collection.util.UserUtil;

/**
 * 使用方后台管理
 * @author silence
 *
 */
@Controller
@RequestMapping("/managebackstage")
public class ManageBackstageController {
	@Resource private IManageBackStageService manageBackstageService;
	
	@Resource private ISystemService systemService;
	
	
	/**-----------------------------------------管理员登录及密码操作start-------------------------------------------------------**/
	
	/**
	 * 用户登录
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	public String adminLogin(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		//查询权限菜单
		model.addAttribute("map", map);
		if(map.containsKey("username") && map.get("username").equals("")){
			model.addAttribute("errors", "请输入用户名");
			model.addAttribute("username", map.get("username"));
			return "/managebackstage/login";
		}
		if(map.containsKey("password") && map.get("password").equals("")){
			model.addAttribute("errors", "请输入密码");
			return "/managebackstage/login";
		}
		if(map.size() > 0){
			Object oldcode = map.get("code");
			Object newcode = request.getSession().getAttribute("systemcode");
			if(oldcode == null || oldcode.equals("") || !String.valueOf(oldcode).equalsIgnoreCase(String.valueOf(newcode))){
				model.addAttribute("errors", "验证码输入错误");
				return "/managebackstage/login";
			}else{
				String password = Md5Util.getMD5(map.get("password")+""); 
				map.put("password", password);
				Map<String, Object> userMap=this.systemService.getUserInfo(map);  
				if(userMap != null && userMap.size()>0){
					 if(userMap.containsKey("status") && String.valueOf(userMap.get("status")).equals("0")){
						 model.addAttribute("errors", "您的帐号已被禁用");
						return "/managebackstage/login";
					 }
					// 登陆成功生成唯一cookiesid
					String cookiesid = userMap.get("userid") + "_" + System.currentTimeMillis();
					// cookiesid存入cookies中,有效期30分钟
					CookieUtil.setCookie(response, UserUtil.SYSTEMINFO, cookiesid, "/", null);
					// 用户信息存入reids中,有效期30分钟
					/*RedisUtil.setMap(cookiesid, userMap);*/
					//本地测试
					request.getSession().setAttribute(cookiesid, userMap);
					// 存储登录来源
					CookieUtil.setCookie(response, UserUtil.FROMINFO, "ADMIN", "/",null);			
					
					return "redirect:/managebackstage/index";
				} 
				else{
					model.addAttribute("errors", "用户名或密码输入错误");
					return "/managebackstage/login";
				}
			}
		}else{
			return "/managebackstage/login";
		} 		
	}
	
	/**
	 * 
	 * 首页
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/index")
	public String index(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> indexInfo=this.manageBackstageService.getIndexInfo();
		model.addAttribute("indexInfo", indexInfo);
		
		//初始化分页
		PageHelper page = new PageHelper(request);
		int count = this.manageBackstageService.getNoticeListCount(map);
		page.setTotalCount(count);
		page.initPage(map);
		List<Map<String,Object>> noticeList = this.manageBackstageService.getNoticeList(map);
		request.setAttribute("noticeList", noticeList);
		request.setAttribute("map", map);
		request.setAttribute("pager", page.cateringPage().toString());
		return "/managebackstage/index";
	}
	
	
	/**
	 * 
	 * 忘记密码
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initforget")
	public String initforget(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		return "/managebackstage/password_forget";
	}
	

	/**
	 * 
	 * 初始化重置密码
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initreset")
	public String initreset(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userInfo=this.systemService.getUserInfo(map);
		model.addAttribute("userInfo", userInfo);
		return "/managebackstage/password_reset";
	}
	
	/**
	 * 
	 * 重置密码
	 * @param map
	 * @param model
	 * @param request  updateUserInfo
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updatePassword")
	public String updatePassword(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		String password=Md5Util.getMD5(map.get("password")+"");
		map.put("password", password);
		this.systemService.updateUserInfo(map);
		return "redirect:/managebackstage/login";
	}
	
	/**
	 * 得到当前用户的个人信息
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getSystemUser")
	public String getSystemUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		try {
			userinfo=this.systemService.getUserInfo(userinfo);
			request.setAttribute("userinfo", userinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "/managebackstage/memcenter/memcenter_info";
	}

	
	/**
	 * 得到当前用户的个人信息和所属部门
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getSystemUpdataUser")
	public String getSystemUpdataUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
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
	 * @author silence
	 */
	@RequestMapping("/updateSystemUserInfo")
	public String updateSystemUserInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
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
	 * @author silence
	 */
	@RequestMapping("/updataPassword")
	@ResponseBody
	public Map<String, Object> updataPassword(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//得到当前的用户信息
		Map<String, Object> userinfo=UserUtil.getSystemUser(request);
		map.put("userid", userinfo.get("userid").toString());
		map.put("password",Md5Util.getMD5(String.valueOf(map.get("password"))));
	
		try {
			
			// 验证密码是否正确
			this.systemService.getUserInfo(map);
			map.put("password",Md5Util.getMD5(String.valueOf(map.get("new_password"))));
			this.systemService.updateUserInfo(map);
			resultMap.put("success", "密码修改成功");
			resultMap.put("status", 0);
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("error", "密码修改错误");

		}
		return resultMap;
	}
	
	/**-----------------------------------------管理员登录及密码操作end-------------------------------------------------------**/
	
	/**-----------------------------------------用户信息管理start----------------------------------------------------**/
	
	/**
	 * 获取用户信息列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getUserList")
	public String getUserList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.manageBackstageService.getUserListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.manageBackstageService.getUserList(map);
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/collection/userinfo_list";
	}
	
	/**
	 * 
	 * 修改用户信息（状态 删除等等）
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updateUserStatus")
	public String updateUserStatus(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		map.put("updatetime", new Date());
		this.manageBackstageService.updateUserInfo(map);
		return "redirect:/managebackstage/getUserList";
	}
	
	/**-----------------------------------------用户信息管理end----------------------------------------------------**/
	
	
	
	/**-----------------------------------------banner管理start-----------------------------------------------------**/
	
	/**
	 * 获取banner管理列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getBannerList")
	public String getBannerList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		if(!map.containsKey("status")) {
			map.put("status", "1");
		}
		try {
			PageHelper page = new PageHelper(request);
			int count = this.manageBackstageService.getBannerListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.manageBackstageService.getBannerList(map);
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/collection/banner_list";
	}
	
	/**
	 * 初始化添加banner
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/initAddOrUpdateBanner")
	public String initAddOrUpdateBanner(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		if(map!= null && map.containsKey("bannerid")){
			Map<String, Object> bannerInfo=this.manageBackstageService.getBannerInfo(map);
			model.addAttribute("bannerInfo", bannerInfo);
		}
		return "/managebackstage/collection/banner_edit";
	}

	/**
	 * 修改banner图
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/updateBanner")
	@ResponseBody
	public Map<String, Object> updateBanner(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> data=new HashMap<String, Object>();
		
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		try {
			map.put("updateid", userInfo.get("userid"));
			map.put("updatetime",new Date());
			this.manageBackstageService.updateBanner(map);
			data.put("status", 0);
			data.put("message", "修改成功");
		} catch (Exception e) {
			data.put("status", 1);
			data.put("message", "修改失败");
		}
		return data;
	}
	
	/**
	 * 新增banner图
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/insertBanner")
	@ResponseBody
	public Map<String, Object> insertBanner(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			map.put("createid", userInfo.get("userid"));
			map.put("createtime",new Date());
			this.manageBackstageService.insertBanner(map);
			data.put("status", 0);
			data.put("message", "添加成功");
		} catch (Exception e) {
			data.put("status", 1);
			data.put("message", "添加失败");
		}
		return data;
	}
	
	/**
	 * 删除banner图
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/deleteBanner")
	public String deleteBanner(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		map.put("status", 0);
		map.put("updateid", userInfo.get("userid"));
		map.put("updatetime",new Date());
		this.manageBackstageService.updateBanner(map);
		return "redirect:/managebackstage/getBannerList";
	}
	
	/**-----------------------------------------banner管理end-----------------------------------------------------**/
	
	/**-----------------------------------------首页广告管理start-----------------------------------------------------**/
	
	/**
	 * 获取广告管理列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getAdvertList")
	public String getAdvertList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.manageBackstageService.getAdvertListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.manageBackstageService.getAdvertList(map);
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/collection/advert_list";
	}
	
	/**
	 * 初始化添加/修改广告
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/initAddOrUpdateAdvert")
	public String initAddOrUpdateAdvert(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		if(map!= null && map.containsKey("advertid")){
			Map<String, Object> advertInfo=this.manageBackstageService.getAdvertInfo(map);
			model.addAttribute("advertInfo", advertInfo);
		}
		return "/managebackstage/collection/advert_edit";
	}

	/**
	 * 修改广告图
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/updateAdvert")
	@ResponseBody
	public Map<String, Object> updateAdvert(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			this.manageBackstageService.updateAdvert(map);
			data.put("status", 0);
			data.put("message", "修改成功");
		} catch (Exception e) {
			data.put("status", 1);
			data.put("message", "修改失败");
		}
		return data;
	}
	
	/**
	 * 新增广告图
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/insertAdvert")
	@ResponseBody
	public Map<String, Object> insertAdvert(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			map.put("createid", userInfo.get("userid"));
			map.put("createtime",new Date());
			this.manageBackstageService.insertAdvert(map);
			data.put("status", 0);
			data.put("message", "添加成功");
		} catch (Exception e) {
			data.put("status", 1);
			data.put("message", "添加失败");
		}
		return data;
	}
	
	/**
	 * 删除广告图
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @author silence
	 * @return
	 */
	@RequestMapping("/deleteAdvert")
	public String deleteAdvert(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		this.manageBackstageService.deleteAdvert(map);
		return "redirect:/managebackstage/getAdvertList";
	}
	
	/**-----------------------------------------广告管理end-----------------------------------------------------**/
	
	
	/**-----------------------------------------订单管理start-------------------------------------------------------**/
	/**
	 * 获取订单管理列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getOrderList")
	public String getOrderList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.manageBackstageService.getOrderListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.manageBackstageService.getOrderList(map);
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/collection/order_list";
	}
	
	/**
	 * 审核拒绝
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateOrderStatus")
	public String updateOrderStatus(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		map.put("updateid", userInfo.get("userid"));
		map.put("updatetime",new Date());
		this.manageBackstageService.updateOrderStatus(map);
		return "redirect:/managebackstage/getOrderList";
	}
	
	/**
	 * 冻结双方用户
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/frozenOrder")
	@ResponseBody
	public Map<String, Object> frozenOrder(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			this.manageBackstageService.frozenOrder(map);
			data.put("status", 0);
			data.put("message", "冻结成功");
		} catch (Exception e) {
			data.put("status", 1);
			data.put("message", "冻结失败");
		}
		return data;
	}
	
	/**
	 * 冻结双方用户
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/returnOrder")
	@ResponseBody
	public Map<String, Object> returnOrder(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			this.manageBackstageService.returnOrder(map);
			data.put("status", 0);
			data.put("message", "退还成功");
		} catch (Exception e) {
			data.put("status", 1);
			data.put("message", "退还失败");
		}
		return data;
	}
	
	
	/**-----------------------------------------订单管理end-------------------------------------------------------**/
	
	
	/**-----------------------------------------首页免费电影管理start-------------------------------------------------------**/
	/**
	 * 获取首页免费电影管理列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getIndexMovieList")
	public String getIndexMovieList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.manageBackstageService.getIndexMovieListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.manageBackstageService.getIndexMovieList(map);
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/collection/indexmovie_list";
	}
	
	/**
	 * 新增/修改首页免费电影信息
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/insertOrupdateIndexMovie")
	public String updateIndexMovie(@RequestParam Map<String,Object> map , HttpServletRequest request){
		if(map!=null && map.get("movieid")!=null && !"".equals(map.get("movieid").toString())){
			//有主键id 修改信息
			Map<String, Object> userInfo=UserUtil.getSystemUser(request);
			map.put("updateid", userInfo.get("userid"));
			map.put("updatetime",new Date());
			this.manageBackstageService.updateIndexMovie(map);
		} else {
			//新增信息
			Map<String, Object> userInfo=UserUtil.getSystemUser(request);
			map.put("createid", userInfo.get("userid"));
			map.put("createtime",new Date());
			this.manageBackstageService.insertIndexMovie(map);
		}
		return "redirect:/managebackstage/getIndexMovieList";
	}
	
	/**-----------------------------------------首页免费电影管理end-------------------------------------------------------**/
	
	/**-----------------------------------------会员卡管理页面start-------------------------------------------------------**/
	/**
	 * 获取会员卡管理列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getMemberCardList")
	public String getMemberCardList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.manageBackstageService.getMemberCardListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.manageBackstageService.getMemberCardList(map);
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/collection/membercard_list";
	}
	
	/**
	 * 修改会员卡信息
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateMemberCard")
	public String updateMemberCard(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		map.put("updateid", userInfo.get("userid"));
		map.put("updatetime",new Date());
		//判断时刻点是否更改 是否更新到定时任务线程和表
		Map<String, Object> cardInfo = this.manageBackstageService.getMemberCardInfo(map);
		if(cardInfo == null || cardInfo.isEmpty()) {
			//查询获取配置的结算时间和结算次数
			String crontab = this.manageBackstageService.getCrontabTime(map);
			//说明需要更新定时任务
			Crontab corn = new Crontab(crontab, 0);
			//默认数据库cardid跟taskid一致，因为没有设置关联这个需要注意
			Scheduler.update(Integer.parseInt(cardInfo.get("cardid").toString()), corn, DateUtil.sysDateTime());
		}
		this.manageBackstageService.updateMemberCard(map);
		return "redirect:/managebackstage/getMemberCardList";
	}
	
	
	
	/**
	 * 获取会员卡手办列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getGarageKitList")
	public String getGarageKitList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.manageBackstageService.getGarageKitListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.manageBackstageService.getGarageKitList(map);
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/collection/garagekit_list";
	}
	
	/**
	 * 新增/修改手办信息
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/insertOrupdateGarageKit")
	public String insertOrupdateGarageKit(@RequestParam Map<String,Object> map , HttpServletRequest request){
		if(map!=null && map.get("kitid")!=null && !"".equals(map.get("kitid").toString())){
			//有主键id 修改信息
			Map<String, Object> userInfo=UserUtil.getSystemUser(request);
			map.put("updateid", userInfo.get("userid"));
			map.put("updatetime",new Date());
			this.manageBackstageService.updateGarageKit(map);
		} else {
			//新增信息
			Map<String, Object> userInfo=UserUtil.getSystemUser(request);
			map.put("createid", userInfo.get("userid"));
			map.put("createtime",new Date());
			this.manageBackstageService.insertGarageKit(map);
		}
		return "redirect:/managebackstage/getGarageKitList?cardid="+map.get("cardid");
	}
	
	/**-----------------------------------------会员卡管理页面end-------------------------------------------------------**/
	
	/**-----------------------------------------会员等级管理start-------------------------------------------------------**/
	/**
	 * 获取会员卡管理列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getLevelList")
	public String getLevelList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.manageBackstageService.getLevelListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.manageBackstageService.getLevelList(map);
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/collection/level_list";
	}
	/**
	 * 修改会员等级信息
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateLevel")
	public String updateLevel(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		map.put("updateid", userInfo.get("userid"));
		map.put("updatetime",new Date());
		this.manageBackstageService.updateLevel(map);
		return "redirect:/managebackstage/getLevelList";
	}
	/**-----------------------------------------会员等级管理end-------------------------------------------------------**/
	
	
	/**-----------------------------------------收款方式管理start-------------------------------------------------------**/
	/**
	 * 获取收款方式管理列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getPaymentMethodList")
	public String getPaymentMethodList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.manageBackstageService.getPaymentMethodListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.manageBackstageService.getPaymentMethodList(map);
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/collection/paymentmethod_list";
	}
	/**-----------------------------------------收款方式管理end-------------------------------------------------------**/
	
	/**-----------------------------------------实名认证管理start-------------------------------------------------------**/
	/**
	 * 获取实名认证管理列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getCertificationList")
	public String getCertificationList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.manageBackstageService.getCertificationListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.manageBackstageService.getCertificationList(map);
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/collection/certification_list";
	}
	/**
	 * 审核身份认证信息
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateCertification")
	public String updateCertification(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		map.put("updateid", userInfo.get("userid"));
		map.put("updatetime",new Date());
		this.manageBackstageService.updateCertification(map);
		return "redirect:/managebackstage/getCertificationList";
	}
	/**-----------------------------------------实名认证管理end-------------------------------------------------------**/
	
	
	/**-----------------------------------------投诉与建议管理start-------------------------------------------------------**/
	/**
	 * 获取投诉与建议列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getQuestionList")
	public String getQuestionList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.manageBackstageService.getQuestionListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.manageBackstageService.getQuestionList(map);
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/collection/question_list";
	}
	/**
	 * 回复投诉与建议
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/replyQuestion")
	public String replyQuestion(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		map.put("systemuserid", userInfo.get("userid"));
		map.put("updatetime",new Date());
		this.manageBackstageService.replyQuestion(map);
		return "redirect:/managebackstage/getQuestionList";
	}
	/**-----------------------------------------投诉建议管理end-------------------------------------------------------**/
	
	
	/**-----------------------------------------兑换记录管理start-------------------------------------------------------**/
	/**
	 * 获取兑换记录列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getExchangeList")
	public String getExchangeList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.manageBackstageService.getExchangeListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.manageBackstageService.getExchangeList(map);
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/collection/exchange_list";
	}

	/**-----------------------------------------兑换记录管理end-------------------------------------------------------**/
	
	
	/**-----------------------------------------系统通知消息start-------------------------------------------------------**/
	/**
	 * 获取系统发送的消息列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSysNoticeList")
	public String getSysNoticeList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.manageBackstageService.getSysNoticeListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.manageBackstageService.getSysNoticeList(map);
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/collection/notice_list";
	}
	
	/**
	 * 发送系统通知消息
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sendSysNotice")
	public String sendSysNotice(@RequestParam Map<String,Object> map , HttpServletRequest request){
		this.manageBackstageService.sendSysNotice(map);
		return "redirect:/managebackstage/getSysNoticeList";
	}
	/**-----------------------------------------系统通知消息end-------------------------------------------------------**/
	
	
	/**-----------------------------------------抢购概率管理start-------------------------------------------------------**/
	/**
	 * 获取抢购概率信息列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getRateList")
	public String getRateList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.manageBackstageService.getRateListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.manageBackstageService.getRateList(map);
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/collection/rate_list";
	}
	
	/**
	 * 新增或者修改抢购概率
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/insertOrUpdateRate")
	public String insertOrUpdateRate(@RequestParam Map<String,Object> map , HttpServletRequest request){
		if(map!=null && map.get("rateid")!=null && !"".equals(map.get("rateid").toString())){
			//有主键id 修改信息
			Map<String, Object> userInfo=UserUtil.getSystemUser(request);
			map.put("updateid", userInfo.get("userid"));
			map.put("updatetime",new Date());
			this.manageBackstageService.updateRate(map);
		} else {
			//新增信息
			Map<String, Object> userInfo=UserUtil.getSystemUser(request);
			map.put("createid", userInfo.get("userid"));
			map.put("createtime",new Date());
			this.manageBackstageService.insertRate(map);
		}
		return "redirect:/managebackstage/getRateList";
	}
	/**-----------------------------------------抢购概率管理end-------------------------------------------------------**/
}
