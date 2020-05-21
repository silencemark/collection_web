package com.collection.controller.userbackstage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

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
import com.collection.service.UserInfoService;
import com.collection.service.personal.PersonalService;
import com.collection.util.SDKTestSendTemplateSMS;
import com.collection.util.UserUtil;


/**
 * 使用方后台管理权限管理
 * @author Dsl
 *
 */
@Controller
@RequestMapping("/userbackstage")
public class BusinessController  extends BaseController {
	private transient static Log log = LogFactory.getLog(BusinessController.class);
	@Resource private PersonalService personalService;
	
	@Resource private UserInfoService userInfoService;
	@Resource private IndexService indexService;
	

	
	/**
	 * 权限管理：人员的企业权限
	 * @param map 输入参数：powerid, ownerid,delflag 
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getPowerByUserId")
	public String getPowerByUserId(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> userinfo=UserUtil.getUser(request);
		map.put("companyid", userinfo.get("companyid"));
		map.put("result", 1);
		try {			
			if(map.containsKey("userid") && !"".equals(map.get("userid"))){
				//查询出用户已有的权限管理列表
				map.put("ownerid",map.get("userid").toString());
				map.put("delflag",0);
				List<Map<String, Object>> datalist = this.personalService.getPowerByUserId(map);
				
				Map<String,Object> listmap = new HashMap<String, Object>();
				listmap.put("datalist", datalist);
				listmap.put("ownerid", map.get("userid").toString());
				request.setAttribute("listmap", JSONObject.fromObject(listmap));
			}
			
			Map<String,Object> userbasicmap = this.personalService.getVerificationPwd(map);
			String content = "查询了人员："+userbasicmap.get("realname")+"所拥有的权限信息。";
			this.insertManageLog(userinfo.get("companyid")+"",2,"组织架构",content,userinfo.get("userid")+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/userbackstage/business/business_personnelpower";
	}
	

	/**
	 *	权限管理：查询权限管理列表
	 * @param map companyid,result
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getPowerAll")
	@ResponseBody
	public Map<String,Object> getPowerAll(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> userinfo=UserUtil.getUser(request);
		map.put("companyid", userinfo.get("companyid"));
		map.put("result", 1);
		try {
			//查询出一级菜单
			List<Map<String, Object>> listone = this.personalService.getPowerOne(map);
			if(listone != null && listone.size() > 0){
				List<Map<String,Object>> countPower=new ArrayList<Map<String,Object>>();
				
				map.put("isparentid", "groupby");
				List<Map<String, Object>> listpid = this.personalService.getPowerTwo(map);
				map.remove("isparentid");
				
				for (int i = 0; i < listpid.size(); i++) {
					Map<String, Object> data = new HashMap<String, Object>();
					String parentid= listpid.get(i).get("parentid").toString();
					map.put("parentid", parentid);
					int count = this.personalService.getPowerOneCount(map);
					map.remove("parentid");
					data.put("powerid", parentid);//一级菜单id
					data.put("childnum", count);//一级菜单子集数量
					countPower.add(data);
				}
				//查询出对应得二级菜单
				List<Map<String, Object>> datalist = this.personalService.getPowerTwo(map);
				
				resultMap.put("listone", listone);
				resultMap.put("datalist", datalist);
				resultMap.put("countPower", countPower);//页面循环countPower,判断一级菜单是否等于它的powerid
						
			}
			
			resultMap.put("status", 0);
			model.addAttribute("success", "查询成功");
		
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errors", "查询失败");
		}
		return resultMap;
	}
	
	/**
	 *	权限管理：权限详情
	 * @param map 输入参数：powerid,ownerid(userid)
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getPowerByPowerid")
	@ResponseBody
	public Map<String,Object> getPowerByPowerid(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request, 
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			if(map != null && map.containsKey("powerid")){
				List<Map<String, Object>> datalist = this.personalService.getPowerAll(map);
				resultMap.put("datalist", datalist);
				
				Map<String, Object> userinfo=UserUtil.getUser(request);
				String content = "查看了权限名为："+datalist.get(0).get("powername")+"的详细信息。";
				this.insertManageLog(userinfo.get("companyid")+"",2,"组织架构/权限管理",content,userinfo.get("userid")+"");
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errors", "查询失败");
		}
		return resultMap;
	}
	
	/**
	 *	权限管理：为用户添加或修改权限
	 * @param map 输入参数：powerid, ownerid(userid),delflag 
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/insertUserPowerInfo")
	@ResponseBody
	public Map<String,Object> insertUserPowerInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		//获取当前登录人的基本信息
		Map<String, Object> userinfo=UserUtil.getUser(request);
		String userid=userinfo.get("userid").toString();
		
		String ownerid=map.get("ownerid").toString();
		map.put("ownerid",ownerid);//人员id
		map.put("updateid", userid);//当前修改人id
		try {
			if(map != null && !"".equals(map)){
				if(map.get("listInsrt") != null && !"".equals(map.get("listInsrt"))){	
					List<Map> list = JSONArray.parseArray(map.get("listInsrt").toString(), Map.class);
					for (int i = 0; i < list.size(); i++) {
						//根据选中的首先查找改人员之前是否存在过此权限
						String powerid=list.get(i).get("powerid").toString();
						map.put("powerid", powerid);
						List<Map<String, Object>> datalist = this.personalService.getPowerByUserId(map);
						if(datalist != null && datalist.size()>0){
							//修改权限
							map.put("delflag", 0);
							this.personalService.getdeletePowerByUserId(map);
							map.remove("delflag");   
						}else{
							//新增用户权限
							map.put("createid", userid);//当前修改人id
							this.personalService.insertUserPowerInfo(map);
						}
					}
					
				}
				
				//修改为取消
				if(map.get("listDelete") != null && !"".equals(map.get("listDelete"))){
					List<Map> list = JSONArray.parseArray(map.get("listDelete").toString(), Map.class);	
					for (int i = 0; i < list.size(); i++) {
						//根据选中的首先查找改人员之前是否存在过此权限
						String powerid=list.get(i).get("powerid").toString();
						map.put("powerid", powerid);
						map.put("delflag", 0);
						List<Map<String, Object>> datalist = this.personalService.getPowerByUserId(map);
						if(datalist != null &&  datalist.size()>0){
							//取消他的权限权限
							map.put("delflag", 1);
							this.personalService.getdeletePowerByUserId(map);
						}
					}
				}	
				
				try {
					//同步app权限
					realTiemUpdateUserPower(ownerid);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				Map<String,Object> userbasicmap = new HashMap<String,Object>();
				userbasicmap.put("userid", ownerid);
				userbasicmap = this.personalService.getVerificationPwd(userbasicmap);
				String content = "修改了"+userbasicmap.get("realname")+"的权限";
				this.insertManageLog(userinfo.get("companyid")+"",2,"组织架构",content,userinfo.get("userid")+"");
				resultMap.put("status", 0);
				resultMap.put("success", "权限设置成功");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 0);
			resultMap.put("errors", "权限设置失败");
		}
		return resultMap;
	}
	
	/**
	 *	权限管理：复制权限
	 * @param map 输入参数：ownerid(userid)
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getPowerCopyByUserid")
	@ResponseBody
	public Map<String,Object> getPowerCopyByUserid(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> userinfo=UserUtil.getUser(request);
		try {
			if(map != null && map.containsKey("ownerid")){
				map.put("delflag", 0);
				List<Map<String, Object>> datalist = this.personalService.getPowerByUserId(map);
				if(datalist != null || datalist.size()>0){
					for(int i=0;i<datalist.size();i++){
						//获取当前人员的所有权限powerid
						Map<String, Object> powermap=new HashMap<String, Object>();
						String powerid=datalist.get(i).get("powerid").toString();
						powermap.put("powerid", powerid);
						
						//获取需要复制权限人id
						String userid=map.get("userid").toString();
						powermap.put("ownerid", userid);
						//判断改人员是否拥有过这些权限
						List<Map<String, Object>> powerlist = this.personalService.getPowerByUserId(powermap);
						if(powerlist != null && powerlist.size()>0){
							//修改权限
							powermap.put("delflag", 0);
							this.personalService.getdeletePowerByUserId(powermap);
							powermap.remove("delflag");   
						}else{
							//新增用户权限
							powermap.put("createid", userid);//当前修改人id
							this.personalService.insertUserPowerInfo(powermap);
						}
					}
					
				}
				try {
					//同步app权限
					realTiemUpdateUserPower(String.valueOf(map.get("userid")));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				resultMap.put("success", "权限设置成功");	
			}
			
			String beifuzhizhe = String.valueOf(map.get("ownerid"));
			String jieshouzhe = String.valueOf(map.get("userid"));
			Map<String,Object> userbasicmap = new HashMap<String,Object>();
			userbasicmap.put("userid", beifuzhizhe);
			userbasicmap = this.personalService.getVerificationPwd(userbasicmap);
			Map<String,Object> jieshouuser = new HashMap<String,Object>();
			jieshouuser.put("userid", jieshouzhe);
			jieshouuser = this.personalService.getVerificationPwd(jieshouuser);
			String content = "把"+userbasicmap.get("realname")+"的权限复制给了"+jieshouuser.get("realname");
			insertManageLog(userinfo.get("companyid")+"",2,"组织架构",content,userinfo.get("userid")+"");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("errors", "复制权限失败");	
		}
		return resultMap;
	}


	/**
	 * 权限管理：人员的企业权限
	 * @param map 输入参数：powerid,
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getSelectPower")
	public String getSelectPower(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {	
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> userinfo=UserUtil.getUser(request);
		request.setAttribute("userinfo", userinfo);
		return "/userbackstage/business/business_power";
	}	
	

		/**
		 * 权限管理：企业权限列表
		 * @param map 输入参数：powerid,
		 * @param model
		 * @param request
		 * @return
		 * @author dsl
		 */
	@RequestMapping("/getSelectPowerCompany")
	@ResponseBody
	public Map<String,Object> getSelectPowerCompany(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
				HttpServletResponse response) {
			response.setHeader("Access-Control-Allow-Origin", "*");
			Map<String, Object> data = new HashMap<String, Object>();
			Map<String,Object> resultMap = new HashMap<String, Object>();
			Map<String, Object> userinfo=UserUtil.getUser(request);
			
			Map<String, Object> powerMap=RedisUtil.getMap(userinfo.get("userid")+"powerMap");
			resultMap.put("powerMap",powerMap);
			
			map.put("companyid", userinfo.get("companyid"));
			try {			
				//查询出一级菜单
				List<Map<String, Object>> listone = this.personalService.getPowerOne(map);
				if(listone != null && listone.size() > 0){
					List<String> parentidlist=new ArrayList<String>();
		 			for (int i = 0; i < listone.size(); i++) {
		 				String parentid= listone.get(i).get("powerid").toString();
						parentidlist.add(parentid);
					}
					map.put("parentidlist", parentidlist);
					//查询出对应得二级菜单
					List<Map<String, Object>> datalist = this.personalService.getPowerAll(map);
					
					resultMap.put("listone", listone);
					resultMap.put("datalist", datalist);
				}
				
				String content = "查询了公司权限信息。";
				insertManageLog(userinfo.get("companyid")+"",2,"企业权限",content,userinfo.get("userid")+"");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return resultMap;
	}
	
	/**
	 * 权限管理：一级企业权限列表
	 * @param map 输入参数：powerid,
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
@RequestMapping("/getSelectPowerCompanyone")
@ResponseBody
public Map<String,Object> getSelectPowerCompanyone(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> userinfo=UserUtil.getUser(request);
		
		Map<String, Object> powerMap=RedisUtil.getMap(userinfo.get("userid")+"powerMap");
		resultMap.put("powerMap",powerMap);
		
		map.put("companyid", userinfo.get("companyid"));
		try {			
			
				//查询出对应一级菜单
				List<Map<String, Object>> listone = this.personalService.getPowerAll(map);
				
				resultMap.put("listone", listone);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
}
	
	/**
	 * 权限管理：组织架构下企业权限中已有的人员
	 * @param map 输入参数：powerid,
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getSelectPowerByPowerid")
	@ResponseBody
	public Map<String,Object> getSelectPowerByPowerid(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> userinfo=UserUtil.getUser(request);
		
		Map<String, Object> powerMap=RedisUtil.getMap(userinfo.get("userid")+"powerMap");
		resultMap.put("powerMap",powerMap);
		map.put("companyid", userinfo.get("companyid"));
		try {			
			//查询出一级菜单
			List<Map<String, Object>> listone = this.personalService.getPowerOne(map);
			if(listone != null && listone.size() > 0){
				List<String> parentidlist=new ArrayList<String>();
	 			for (int i = 0; i < listone.size(); i++) {
	 				String parentid= listone.get(i).get("powerid").toString();
					parentidlist.add(parentid);
				}
				map.put("parentidlist", parentidlist);
				//查询出对应得二级菜单
				List<Map<String, Object>> datalist = this.personalService.getPowerAll(map);
				
				
				resultMap.put("listone", listone);
				resultMap.put("datalist", datalist);
						
				List<String> poweridlist=new ArrayList<String>();
				List<String> organizeidlist=new ArrayList<String>();
				
				if(datalist != null && datalist.size()>0 ){
					for (int i = 0; i < datalist.size(); i++) {
						poweridlist.add(datalist.get(i).get("powerid").toString());
					}
					map.put("poweridlist", poweridlist);
					List<Map<String, Object>> organizelist=this.personalService.getOrganizeidAll(map);
					for (int i = 0; i < organizelist.size(); i++) {
						organizeidlist.add(organizelist.get(i).get("organizeid").toString());
					}
					map.put("organizeidlist", organizeidlist);
					//查询出所有权限管理下面的人员
					List<Map<String, Object>> poweruserlist = this.personalService.getPowerUserByPowerId(map);
					resultMap.put("poweruserlist", poweruserlist);
				}
			
				
				resultMap.put("listone", listone);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}


/**
 *	权限管理：申请企业权限
 * @param map 输入参数：powerid
 * @param model
 * @param request
 * @return
 * @author dsl
 */
@RequestMapping("/getPowerByCompany")
public String getPowerByCompany(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
		HttpServletResponse response) {
	response.setHeader("Access-Control-Allow-Origin", "*");
	Map<String,Object> resultMap = new HashMap<String, Object>();
	//获取当前登录人的基本信息
	Map<String, Object> userinfo=UserUtil.getUser(request);
	String userid=userinfo.get("userid").toString();
	try {
		if(map != null && !"".equals(map)){
			map.put("companyid", userinfo.get("companyid"));
				List<Map<String, Object>> datalist = this.personalService.selectPowerCompany(map);
				if(datalist != null && datalist.size()>0){
					 map.put("updateid", userid);
					 map.put("status", 2);
					 map.put("delflag", 0);
					 map.put("powercompanyid", datalist.get(0).get("powercompanyid"));
					 this.personalService.getUpdatPowerCompanyByid(map);
				}else{
					map.put("createid", userid);
					map.put("status", 2);
					this.personalService.insertPowerCompany(map);
				}
				
				try {
					//权限申请短信通知
					Map<String,Object> manageusermap = this.personalService.getManageUserInfo(map);
					Map<String,Object> comanymap = this.personalService.getCompanyBasicInfo(map);
					String phone = String.valueOf(manageusermap.get("phone"));
					String realname = String.valueOf(manageusermap.get("realname"));
					String content = String.valueOf(comanymap.get("companyname"));
					SDKTestSendTemplateSMS.sendPowerApply(phone, realname, content);
				} catch (Exception e) {
					// TODO: handle exception
				}
			model.addAttribute("success", "权限申请成功");
			
			String content = "申请了企业权限";
			insertManageLog(userinfo.get("companyid")+"",2,"企业权限",content,userinfo.get("userid")+"");
		}		
	} catch (Exception e) {
		e.printStackTrace();
		model.addAttribute("errors", "权限设申请失败");
	}
	return "redirect:/userbackstage/getSelectPower";
}



/**
 *	权限管理：为多个用户添加或修改权限
 * @param map 输入参数：powerid, ownerid(userid),delflag 
 * @param model
 * @param request
 * @return
 * @author dsl
 */
@RequestMapping("/updataUserByPowerId")
@ResponseBody
public  Map<String, Object> updataUserByPowerId(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
		HttpServletResponse response) {
	response.setHeader("Access-Control-Allow-Origin", "*");
	Map<String,Object> resultMap = new HashMap<String, Object>();
	//获取当前登录人的基本信息
	Map<String, Object> userinfo=UserUtil.getUser(request);
	String userid=userinfo.get("userid").toString();
	try {
		if(map != null && !"".equals(map)){
			if(map.get("useridlist") != null && !"".equals(map.get("useridlist"))){	
				List<Map> list = JSONArray.parseArray(map.get("useridlist").toString(), Map.class);
				for (int i = 0; i < list.size(); i++) {
					//根据选中的首先查找改人员之前是否存在过此权限
					String ownerid=list.get(i).get("userid").toString();
					map.put("ownerid", ownerid);
					List<Map<String, Object>> datalist = this.personalService.getPowerByUserId(map);
					if(datalist != null && datalist.size()>0){
						//修改权限
						insertManageLog(userinfo.get("companyid")+"",2,"企业权限","修改权限给"+list.get(i).get("realname")+"",userinfo.get("userid")+"");
						map.put("delflag", 0);
						map.put("updateid", userid);//当前修改人id
						this.personalService.getdeletePowerByUserId(map);
						map.remove("delflag");   
						map.remove("updateid");   
					}else{
						//新增用户权限
						insertManageLog(userinfo.get("companyid")+"",2,"企业权限","添加权限给"+list.get(i).get("realname")+"",userinfo.get("userid")+"");
						map.put("createid", userid);//当前修改人id
						this.personalService.insertUserPowerInfo(map);
						map.remove("createid");   
					}
					
					//权限赋予成功之后 给app端 同步权限
					Map<String,String> params = new HashMap<String,String>();
					params.put("operation", "powerupdate");
					params.put("userid", ownerid);
					//JPushAliaseUtil.PushSilence(ownerid, params);
				}
				
			}
			if(map.containsKey("poweruserid")){
				map.put("delflag", 1);
				map.put("updateid", userid);//当前修改人id
				this.personalService.getdeletePowerByUserId(map);
			}
			resultMap.put("status",0);
			resultMap.put("success", "权限设置成功");
		}		
	} catch (Exception e) {
		e.printStackTrace();
		resultMap.put("status",1);
		resultMap.put("errors", "权限设置失败");
	}
	return resultMap;
}
/**
 *	权限管理：查询权限下已有的权限人员
 * @param map 输入参数：powerid, ownerid(userid),delflag 
 * @param model
 * @param request
 * @return
 * @author dsl
 */
@RequestMapping("/getPowerUser")
@ResponseBody
public  Map<String, Object> getPowerUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
		HttpServletResponse response) {
	response.setHeader("Access-Control-Allow-Origin", "*");
	Map<String,Object> resultMap = new HashMap<String, Object>();
	//获取当前登录人的基本信息
	Map<String, Object> userinfo=UserUtil.getUser(request);
	try {
		List<Map<String, Object>> userlist = new ArrayList<Map<String,Object>>();
			if(map.get("powerid") != null && !"".equals(map.get("powerid"))){	
				userlist = this.personalService.getPowerUser(map);
			}
			resultMap.put("userlist",userlist);
			resultMap.put("status",0);
			resultMap.put("success", "权限设置成功");			
	} catch (Exception e) {
		e.printStackTrace();
		resultMap.put("status",1);
		resultMap.put("errors", "权限设置失败");
	}
	return resultMap;
}


}