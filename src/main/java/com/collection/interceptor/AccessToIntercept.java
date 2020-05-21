package com.collection.interceptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.collection.redis.RedisUtil;
import com.collection.util.Constants;
import com.collection.util.CookieUtil;
import com.collection.util.HttpHeaderUtil;
import com.collection.util.UserUtil;



/**
 * 拦截器
 * @author silence
 *
 */
public class AccessToIntercept implements HandlerInterceptor{
	
	private transient static Log log = LogFactory.getLog(AccessToIntercept.class);
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
	 
		if(HttpHeaderUtil.isWeiXinFrom(request)){
			return true;
		}
		//访问路径
        StringBuffer requesturl = request.getRequestURL();
        log.debug("requesturl--------"+requesturl);
        
        //登陆
        if(requesturl.indexOf("/managebackstage/login") > -1){
        	UserUtil.deleteSystemUser(request, response);
        	return true;
        	
        }
        //登陆页面
        if(requesturl.indexOf("/managebackstage/initforget") > -1
        	|| requesturl.indexOf("/managebackstage/getVerificationCode") > -1
        	|| requesturl.indexOf("/managebackstage/initreset") > -1
        	|| requesturl.indexOf("/managebackstage/updateUserInfo") > -1
        	|| requesturl.indexOf("/login/sendPhoneCode") > -1
        	|| requesturl.indexOf("/login/validateCode") > -1
        	|| requesturl.indexOf("/login/checkUserName") > -1
        		){
        	
        	return true;
        }
        //样式+图片+js
        if(requesturl.indexOf("/js/") > -1 ||
           requesturl.indexOf("/public/") > -1 ||
           requesturl.indexOf("/focus/") > -1 ||
           requesturl.indexOf("/black/") > -1 ||
           requesturl.indexOf("/sweetalert/") > -1||
           requesturl.indexOf("/theme/") > -1||
           requesturl.indexOf("/ueditor/") > -1||
           requesturl.indexOf("/app/")>-1||
           requesturl.indexOf("/head/")>-1||
           requesturl.indexOf("/userbackstage/")>-1||
           requesturl.indexOf("/upload/")>-1){
        	return true;
        }
      
        //获取是否ajax请求
        String requestType = request.getHeader("X-Requested-With");
        if (requestType != null && requestType.equals("XMLHttpRequest")) {
        	return true;
        }
       
        //获取访问路径
        StringBuffer url = requesturl;
        if(!(request.getQueryString()==null || request.getQueryString ().equals(""))){
        	url = new StringBuffer(requesturl+"?"+request.getQueryString());    	
        }
        CookieUtil.setCookie(response, UserUtil.URL, url.toString(), "/", null);
         
        
        //获取cookies中值
        String cookiesid= null;
        Cookie[] cookies=request.getCookies();
    	if(cookies!=null){
			for(int i=0;i<cookies.length;i++) {
				if(cookies[i].getName().equals(UserUtil.SYSTEMINFO)) {
					cookiesid=cookies[i].getValue();
				}
			}
		} 
    	log.debug("cookiesid_______"+cookiesid);
    	
    	
    	 
    	//cookies失效跳转登陆页面
    	if(cookiesid == null){
    		response.sendRedirect(Constants.PROJECT_PATH+"managebackstage/login"); 	
	    	return false;
    	}
    	
    	//判读redis中是否存在该cookiesid
    	if(!RedisUtil.exist(cookiesid)){	
    		response.sendRedirect(Constants.PROJECT_PATH+"managebackstage/login"); 		
    		return false;
    	}
    	
        return true;
	}
	
	public static boolean fileconversion(StringBuffer requesturl){
		if(requesturl.indexOf(".bmp") > -1 ||
				requesturl.indexOf(".gif") > -1 ||
				requesturl.indexOf(".jpg") > -1 ||
				requesturl.indexOf(".pic") > -1 ||
				requesturl.indexOf(".png") > -1 ||
				requesturl.indexOf(".tif") > -1 ||
				requesturl.indexOf(".css") > -1 ||
				requesturl.indexOf(".txt") > -1 ||
				requesturl.indexOf(".pdf") > -1 ||
				requesturl.indexOf(".doc") > -1 ||
				requesturl.indexOf(".wps") > -1 ||
				requesturl.indexOf(".html") > -1 ||
				requesturl.indexOf(".rar") > -1 ||
				requesturl.indexOf(".arj") > -1 ||
				requesturl.indexOf(".gz") > -1 ||
				requesturl.indexOf(".z") > -1 ||
				requesturl.indexOf(".avi") > -1 ||
				requesturl.indexOf(".mpg") > -1 ||
				requesturl.indexOf(".mov") > -1 ||
				requesturl.indexOf(".swf") > -1 ||
				requesturl.indexOf(".MID") > -1 ||
				requesturl.indexOf(".xls") > -1 ||
				requesturl.indexOf(".tmp") > -1 ||
				requesturl.indexOf(".iso") > -1 ||
				requesturl.indexOf(".zip") > -1){
			return true;
		}
		return false;
	}
	
	public static String StringFilter(String str){
		String regEx="[`~!@#$^*|{}':;'<>~！@#￥……*——|{}【】‘；：”“’。，、？]";      
		Pattern p = Pattern.compile(regEx); 
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
