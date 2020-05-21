package com.base.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.collection.redis.RedisUtil;
import com.collection.service.IndexService;
import com.collection.service.UserInfoService;
import com.collection.util.Constants;
import com.collection.util.Md5Util;

public class BaseController {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(BaseController.class);
	
	@Resource IndexService indexService;
	@Resource UserInfoService userInfoService;
	
	/**
	 * 递归查询组织下的所有人员
	 * @param chinese
	 * @param english
	 * @param userid
	 * @author silence
	 * @return
	 */
//	public List<Map<String, Object>> userinfolistall=new ArrayList<Map<String,Object>>();
	
	public List<Map<String, Object>> getUserByorganize(Map<String, Object> data){
		//查询当前进来的组织的人员
		/*List<Map<String, Object>> userlist=this.indexService.getUserList(data);
		userinfolistall.addAll(userlist);
		Map<String, Object> organizemap=new HashMap<String, Object>();
		organizemap.put("companyid", data.get("companyid"));
		organizemap.put("organizeid", data.get("organizeid"));
		//查询进来组织的儿子组织
		List<Map<String, Object>> organizelist=this.indexService.getOrganizeList(organizemap);
		if(organizelist != null && organizelist.size()>0){
			for(Map<String, Object> organize:organizelist){
				//递归调用
				getUserByorganize(organize);
			}
		}
		List<Map<String, Object>> temp = new ArrayList<Map<String,Object>>();
		//temp = userinfolistall;
		temp.addAll(0, userinfolistall);
		userinfolistall.clear();
		return temp;*/
		
		
		//查询某组织机构下面所有的人员信息
		return indexService.getUserListByOrganizeid(data);
	}
	
	
	
	public String getURL(HttpServletRequest request) {
		String str = request.getServletPath();
		String  url= str;
		if(request.getQueryString()!=null){
			url = str + "?" + request.getQueryString();
		} 
		return url;
	}
	public String getBasePath(HttpServletRequest request){
		String path = request.getContextPath();
		if ("/".equals(path)) path = "";
		int serverPort = request.getServerPort();
		String sPort = (serverPort == 80 || serverPort == 443)?"":(":" + request.getServerPort());
		String serverName = request.getServerName();
		String base = request.getScheme() + "://" + serverName+sPort; 
		String basePath = base + path + "/";
		return basePath;
	}
	
	
	public static boolean checkSecret(Map<String, Object> map){
		String secretcode=map.get("secretcode")+"";
		map.remove("secretcode");
		JSONObject json=new JSONObject();
		json=JSONObject.fromObject(map);
		
		String parameter=json.toString().substring(1,json.toString().length()-1);
		String[] array=parameter.split(",");
		String param="";
		String jsonrealut="";
		if(map.size()>0){
			for(int i=0;i<array.length;i++){
				String[] value=array[i].split(":");
				String value1=value[0];
				String value2=value[1].substring(1,value[1].length()-1);
				if(isNumber(value2)){
					param+=value1+":"+value2+",";
				}else{
					param+=value1+":\""+value2+"\",";
				}
			}
			jsonrealut="{"+param.substring(0,param.length()-1)+"}";
		}else{
			jsonrealut="";
		}
		String secret=Md5Util.getMD5(jsonrealut+Constants.INTERFACE_SECRET);
		boolean flag=false;
		if(secretcode.equals(secret)){
			flag=true;
		}
		return flag;
	}
	
	/**
	  * 判断字符串是否是整数
	  */
	 public static boolean isInteger(String value) {
		for (int i = value.length();--i>=0;){   
			 if (!Character.isDigit(value.charAt(i))){
			    return false;
			 }
		}
		return true;
	 }

	 /**
	  * 判断字符串是否是浮点数
	  */
	 public static boolean isDouble(String value) {
	  try {
	   Double.parseDouble(value);
	   if (value.contains("."))
	    return true;
	   return false;
	  } catch (NumberFormatException e) {
	   return false;
	  }
	 }

	 /**
	  * 判断字符串是否是数字
	  */
	 public static boolean isNumber(String value) {
	  return isInteger(value) || isDouble(value);
	 }
	
	/**
	 * 得到当前用户选择的语言类型
	 * @return
	 */
	public String selectedLanguage(String userid){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("userid", userid);
		int type = 0;//this.userInfoService.getUserLanguageInfo(paramMap);
		String language = "chinese";
		if(type != 0){
			language = "english";
		}
		return language;
	}
	
	/**
	 * 判断参数是否为空
	 * @param value
	 * @return
	 */
	public boolean isNotEmpty(Object value){
		boolean fag = false;
		//判断是否为空
		if(value != null && !"".equals(String.valueOf(value)) && !String.valueOf(value).equals("undefined") && !String.valueOf(value).equals("null")){
			fag=true;
		}
		return fag;
	}
	
	/**
	 * 判断显示的语言信息
	 * @param chinese
	 * @param english
	 * @param userid
	 * @return
	 */
	public String showMessage(String chinese,String english,String userid){
		//用户选择的语言
		String language = this.selectedLanguage(userid);
		//返回字段
		String message = "";
		//判断是否是中文
		if(language.equals("chinese")){
			message=chinese;
		}else{
			message=english;
		}
		return message;
	}
	
	/**
	 * 数字转汉字
	 * @param chinese
	 * @param english
	 * @param userid
	 * @author silence
	 * @return
	 */
	 public static StringBuffer getHanzi(int d) {
	       String[] str = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
	       String ss[] = new String[] { "", "十", "百", "千", "万", "十", "百", "千", "亿" };
	       String s = String.valueOf(d);
	       System.out.println(s);
	       StringBuffer sb = new StringBuffer();
	       for (int i = 0; i < s.length(); i++) {
	           String index = String.valueOf(s.charAt(i));
	           sb = sb.append(str[Integer.parseInt(index)]);
	       }
	       String sss = String.valueOf(sb);
	       int i = 0;
	       for (int j = sss.length(); j > 0; j--) {
	           sb = sb.insert(j, ss[i++]);
	       }
	       return sb;
	   }
	/**
	 * 添加管理日志
	 * @param chinese
	 * @param english
	 * @param userid
	 * @author silence
	 * @return
	 */
	 public void insertManageLog(String companyid,int type,String model,String content,String userid) {
		 Map<String, Object> logmap=new HashMap<String, Object>();
		 logmap.put("companyid", companyid);
		 logmap.put("type", type);
		 logmap.put("model", model);
		 logmap.put("content", content);
		 logmap.put("userid", userid);
		 this.indexService.insertManageLog(logmap);
	 }
	 
	 /**
	  * 同步该用户app权限
	  * @param userid
	  */
	 public void realTiemUpdateUserPower(String userid){
		String updatepowertime = String.valueOf(new Date().getTime());
		//将该用户的权限更新时间保存到redis
		RedisUtil.setStringParam(Constants.POWER_UPDATE_TIME+userid, updatepowertime, null);
		//权限赋予成功之后 给app端 同步权限
//		Map<String,String> params = new HashMap<String,String>();
//		params.put("operation", "powerupdate");
//		params.put("userid", userid);
//		JPushAliaseUtil.PushSilence(userid, params);
		//更新用户信息updatepowertime字段
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userid", userid);
		map.put("updatepowertime", updatepowertime);
		this.userInfoService.updateUserInfo(map);
	 }
	 
	 
	 /**
		 * 推送通知栏信息
		 * @param userid
		 * @param title
		 * @param url
		 */
		public void JPushAndriodAndIosMessage(String userid, String title, String url){
			//获取推送的类型
			/*String type = Constants.JPUSHTYPE;
			//registrationid推送
			if(type.equals("registrationid")){
				//查询用户的registrationid
				try {
					String registrationid = this.indexService.getRegistrationIdByUserId(userid);
					if(!"".equals(registrationid) && registrationid != null){
						//推送信息
						JPushRegIdUtil.PushUrlByRegId(registrationid, title, url);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}else if(type.equals("userid")){//userid 推送
				try {
					JPushAliaseUtil.PushUrlByAliase(userid, title, url);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}*/
		} 
	 
}
