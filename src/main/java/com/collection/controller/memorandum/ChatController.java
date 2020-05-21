package com.collection.controller.memorandum;

import java.util.ArrayList;
import java.util.Date;
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

import com.base.controller.BaseController;
import com.collection.redis.RedisUtil;
import com.collection.rong.ApiHttpClient;
import com.collection.rong.models.FormatType;
import com.collection.rong.models.SdkHttpResult;
import com.collection.service.UserInfoService;
import com.collection.service.chat.ChatService;
import com.collection.util.Constants;
import com.collection.util.PageScroll;


/**
 * 消息聊天
 * @author silence
 *
 */
@Controller
@RequestMapping("/app")
public class ChatController extends BaseController{
	private transient static Log log = LogFactory.getLog(ChatController.class);
	
	@Resource UserInfoService userInfoService;
	
	@Resource ChatService chatService;
	/**
	 * 获取token
	 * 传入参数：userid,realname,headimage
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getToken")
	@ResponseBody
	public Map<String, Object> getToken(@RequestParam Map<String, String> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		Map<String, Object> tokenInfo=RedisUtil.getObject(map.get("userid")+"token");
		if(tokenInfo!= null && tokenInfo.size()>0){
			data.put("tokenid", tokenInfo.get("tokenid")+"");
			return data;
		}else{
			//餐饮大师应用
			//String key = "qd46yzrf44bkf";
			//String secret = "mL9Oi4eIisJvQv";
			//String key = "kj7swf8o77ux2";
			//String secret = "Ye3gd38ghaM";
	
			SdkHttpResult result = null;
	
			//获取token
			try {
				result = ApiHttpClient.getToken(Constants.cloudappkey, Constants.cloudappsecret, map.get("userid")+"",  map.get("realname")+"",
						 map.get("headimage")+"", FormatType.json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("gettoken=" + result);
			JSONObject json=new JSONObject();
			json=json.fromObject(result.getResult());
			String tokenid=json.get("token")+"";
			Map<String, Object> tokenmap=new HashMap<String, Object>();
			tokenmap.put("tokenid", tokenid);
			RedisUtil.setObject(map.get("userid")+"token", tokenmap,1);
			
			data.put("tokenid", tokenid);
			return data;
		}
	}
	
	@RequestMapping("/talk")
	public String talk(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		model.addAttribute("map", map);
		return "/memorandum/talk";
	}
	
	@RequestMapping("/chatindex")
	public String chatindex(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		model.addAttribute("map", map);
		return "/memorandum/index";
	}
	
	/**
	 * 得到用户的所有聊天会话
	 * @param userid
	 * @return
	 */
	@RequestMapping("/getMyAllCartList")
	@ResponseBody
	public Map<String, Object> getMyAllCartList(@RequestParam Map<String, Object> map ,HttpServletRequest request){
		Map<String, Object> data = new HashMap<String, Object>();
		try{
			PageScroll page = new PageScroll();
			int num=this.chatService.getMyAllCartListCount(map);	
			page.setTotalRecords(num);
			page.initPage(map);
			List<Map<String, Object>> dataList=this.chatService.getMyAllCartList(map);
			data.put("num", num);
			data.put("status",0);
			data.put("page",page);
			data.put("dataList", dataList);
			data.put("msg", "查询成功");
		}catch(Exception e){
			e.printStackTrace();
			data.put("status", "1");
			data.put("msg", "查询失败");
		}
		return data;
	}
	/**
	 * 创建群聊
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @author silence
	 * @return
	 */
	@RequestMapping("/createManyGroup")
	@ResponseBody
	@SuppressWarnings({ "unchecked" })
	public Map<String, Object> createManyGroup(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			//发布范围
			JSONObject json=JSONObject.fromObject(map.get("userlist")+"");
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			List<Map<String, Object>> useralllist=new ArrayList<Map<String,Object>>();
			for(Map<String, Object> user:userlist){
				if(user.containsKey("userid") && !"".equals(user.get("userid"))){
					Map<String, Object> userinfo=this.userInfoService.getUserInfo(user);
					useralllist.add(userinfo);
				}
				if(user.containsKey("organizeid") && !"".equals(user.get("organizeid"))){
					user.put("organizeid", user.get("organizeid"));
					user.put("type", 2);
					//添加到发布范围用户表
					List<Map<String, Object>> userinfolist=getUserByorganize(user);
					useralllist.addAll(userinfolist);
				}
			}
			//list去重复
			for(int i=0;i<useralllist.size()-1 ;i++)   { 
				for(int j=useralllist.size()-1 ;j>i; j--){ 
					  if((useralllist.get(j).get("userid")).equals(useralllist.get(i).get("userid"))){
						  useralllist.remove(j); 
					  } 
				} 
			}
			data = this.chatService.createManyGroup(map,useralllist);
			data.put("success", true);
			data.put("status", 0);
		} catch (Exception e) {
			data.put("success", false); 
			data.put("msg", e.toString()); 
		}
		return data;
	}
	
	/**
	 * 创建单聊
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @author silence
	 * @return
	 */
	@RequestMapping("/createOneGroup")
	@ResponseBody
	public Map<String, Object> createOneGroup(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			data = this.chatService.createOneGroup(map);
			data.put("success", true);
			data.put("status", 0);
		} catch (Exception e) {
			data.put("success", false); 
			data.put("msg", e.toString()); 
		}
		return data;
	}

	@RequestMapping("/getGroupList")
	@ResponseBody
	public Map<String, Object> getGroupList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		String [] groupids=(map.get("groupids")+"").split(",");
		Map<String, Object> groupMap=new HashMap<String, Object>();
		groupMap.put("groupids", groupids);
		groupMap.put("userid", map.get("userid"));
		List<Map<String, Object>> grouplist=this.chatService.getChatGroupList(groupMap);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("grouplist", grouplist);
		return data;
	}
	
	//查询我和群的信息
	@RequestMapping("/getGroupInfo")
	@ResponseBody
	public Map<String, Object> getGroupInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> groupInfo=this.chatService.getChatGroupInfo(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("groupInfo", groupInfo);
		return data;
	}
	
	/**
	 * 查询群的发送人员信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @author silence
	 * @return
	 */
	
	@RequestMapping("/getSendUserList")
	@ResponseBody
	public Map<String, Object> getSendUserList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		String [] userids=(map.get("senderUserIds")+"").split(",");
		Map<String, Object> groupMap=new HashMap<String, Object>();
		groupMap.put("userids", userids);
		List<Map<String, Object>> userlist=this.chatService.getChatUserList(groupMap);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("userlist", userlist);
		
		//查询群信息
		groupMap=new HashMap<String, Object>();
		groupMap.put("groupid", map.get("groupid"));
		groupMap.put("this_userid", map.get("this_userid"));
		groupMap=this.chatService.getChatGroupInfo(groupMap);
		
		data.put("groupMap", groupMap);
		return data;
	}
	
	/**
	 * 新增消息记录
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @author silence
	 * @return
	 */
	
	@RequestMapping("/insertChatRecord")
	@ResponseBody
	public Map<String, Object> insertChatRecord(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		this.chatService.insertChatRecord(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("status", 0);
		return data;
	}
	
	
	/**
	 * 修改群信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @author silence
	 * @return
	 */
	
	@RequestMapping("/updateGroupName")
	@ResponseBody
	public Map<String, Object> updateGroupName(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		
		String groupId=map.get("groupid")+""; 
		String groupName=map.get("groupname")+"'"; 
		//刷新群信息
		try {
			SdkHttpResult result = ApiHttpClient.refreshGroupInfo(Constants.cloudappkey, Constants.cloudappsecret,groupId, groupName, FormatType.json);
			log.info("createGroup===="+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.chatService.updateGroup(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("status", 0);
		return data;
	}


	/**
	 * 添加用户群聊表
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @author silence
	 * @return
	 */
	
	@RequestMapping("/addUserGroup")
	@ResponseBody
	public Map<String, Object> addUserGroup(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		String userid=map.get("userid")+"";
		String groupId=map.get("groupid")+""; 
		String groupName=map.get("groupname")+"'"; 
		//刷新群信息
		try {
			SdkHttpResult result = ApiHttpClient.joinGroup(Constants.cloudappkey, Constants.cloudappsecret,userid,groupId, groupName, FormatType.json);
			log.info("createGroup===="+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.chatService.insertUserToGroup(map);
		
		Map<String, Object> userInfo=this.userInfoService.getUserInfo(map);
		
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("userInfo", userInfo);
		data.put("status", 0);
		return data;
	}
	
	/**
	 * 修改清除消息记录
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @author silence
	 * @return
	 */
	
	@RequestMapping("/updateUserGroupCleantime")
	@ResponseBody
	public Map<String, Object> updateUserGroupCleantime(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		map.put("cleantime", new Date());
		
		this.chatService.updateUserGroup(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("status", 0);
		return data;
	}
	
	/**
	 * 修改用户群组信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @author silence
	 * @return
	 */
	
	@RequestMapping("/updateUserGroup")
	@ResponseBody
	public Map<String, Object> updateUserGroup(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		
		this.chatService.updateUserGroup(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("status", 0);
		return data;
	}
	
	/**
	 * 查询用户群组信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @author silence
	 * @return
	 */
	
	@RequestMapping("/getUserGroup")
	@ResponseBody
	public Map<String, Object> getUserGroup(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> data=this.chatService.getUserGroup(map);
		return data;
	}
	// begin=============================聊天信息 及其状态===================
	/**
	 * 标记语音
	 * @param map
	 * @return
	 */
	@RequestMapping("/signVoiceChatRecordStatus")
	@ResponseBody
	public Map<String, Object> signVoiceChatRecordStatus(@RequestParam Map<String, Object> map){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("success", this.chatService.signVoiceChatRecordStatus(map));
		return returnMap;
	}
	
	/**
	 * 标记已读状态
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/signChatRecordStatus")
	@ResponseBody
	public Map<String, Object> signChatRecordStatus(@RequestParam Map<String, Object> map){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("success", this.chatService.signChatRecordStatus(map));
		return returnMap;
	}
	
	/**
	 * 获取用户是否有未读信息数
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/getChatRecordStatusCount")
	@ResponseBody
	public Map<String, Object> getChatRecordStatusCount(@RequestParam Map<String, Object> map){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("count", this.chatService.getChatRecordStatusCount(map));
		returnMap.put("success", true);
		return returnMap;
	}
	
	/**
	 * 清空聊天记录
	 * @param map
	 * @return
	 */
	@RequestMapping("/deleteChatRecord")
	@ResponseBody
	public Map<String, Object> deleteChatRecord(@RequestParam Map<String, Object> map){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("count", this.chatService.deleteChatRecord(map));
		returnMap.put("success", true);
		return returnMap;
	}
	  
	/**
	 * app 聊天记录 列表
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/getChatRecordList")
	@ResponseBody
	public Map<String, Object> getChatRecordList(@RequestParam Map<String, Object> map ,HttpServletRequest request){
		Map<String, Object> data = new HashMap<String, Object>();
		try{
			PageScroll page = new PageScroll();
			int num=this.chatService.getChatRecordListCount(map);	
			page.setTotalRecords(num);
			page.initPage(map);
			List<Map<String, Object>> dataList=this.chatService.getChatRecordList(map);
			data.put("num", num);
			data.put("status",0);
			data.put("page",page);
			data.put("dataList", dataList);
			data.put("msg", "查询成功");
		}catch(Exception e){
			e.printStackTrace();
			data.put("status", "1");
			data.put("msg", "查询失败");
		}
		return data;
	}
	 
	// end=============================聊天信息 及其状态===================
	
	/**
	 * 退出讨论组
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/userBackGroup")
	public Map<String,Object> userBackGroup(@RequestParam Map<String,Object> map ,
			HttpServletRequest request , HttpServletResponse response){
		 Map<String,Object> resultMap = new HashMap<String,Object>();
		 try {
			 if(map.containsKey("userid") && !"".equals(map.get("userid")) && map.containsKey("groupid") && !"".equals(map.get("groupid"))){
				List<Map<String,Object>> grouplist = this.chatService.getGroupListInfoByUserid(map);
				if(grouplist != null && grouplist.size() == 1){
					Map<String,Object> groupmap = grouplist.get(0);
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
					
					resultMap.put("status", 0);
					resultMap.put("usernum", usernum);
					resultMap.put("groupid", groupmap.get("groupid"));
				}else{
					resultMap.put("status", 1);
					resultMap.put("message", "查询条件不够");
				}
			 }else{
				 resultMap.put("status", 1);
				 resultMap.put("message", "查询条件不够");
			 }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("message", "查询出错了");
		}
		 return resultMap;
	}
	
}
