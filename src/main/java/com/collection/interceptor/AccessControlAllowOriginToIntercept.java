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
 * 
 * @author HYL
 * 
 */
public class AccessControlAllowOriginToIntercept implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2) throws Exception {

		// 访问路径
		StringBuffer requesturl = request.getRequestURL();
		// 登陆
		if (requesturl.indexOf("/app/") > -1) {
			response.setHeader("Access-Control-Allow-Origin", "*");
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
	}

}
