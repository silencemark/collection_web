package com.base.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.collection.util.Constants;
import com.collection.util.Md5Util;

public class BaseController {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(BaseController.class);
	
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
	
	
	/**
	 * 校验登录token签名是否正确
	 * @param map
	 * @return
	 */
	public static boolean checkToeknSign(Map<String, Object> map){
		String token = map.get("token").toString();
		String timestamp = map.get("timestamp").toString();
		String sign = map.get("sign").toString();
		String signStr = token.concat(timestamp).concat(Constants.INTERFACE_SECRET);
		String signcode = Md5Util.getMD5(signStr);
		boolean flag=false;
		Long s = (System.currentTimeMillis() - Long.parseLong(timestamp)) / (1000);
		//判断3秒内的请求可取
		if(signcode.equals(sign) && s < 500){
			flag = true;
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
}
