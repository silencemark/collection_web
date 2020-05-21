package com.collection.util.email;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.collection.util.Constants;



/**
 * @ClassName:AgainSend.java
 * @ClassPath:com.hypers.core.email
 * @Desciption:再次发送的相关邮件（统一处理）
 * @Author: Robin
 * @Date: 2014年6月20日 下午6:11:24
 */
public class EmailProxy {
	private static final Logger LOGGER = Logger.getLogger(EmailProxy.class);
 
	 
	private EmailUtil emailUtil;
	  
	public EmailProxy() {
		super();
	}

	private String getRootPath(HttpServletRequest request) {
		return  Constants.PROJECT_PATH+"";
	}

	private StringBuffer getStylePath(String path) {
		return new StringBuffer(path).append("/client/css/style.css");
	}

	private StringBuffer getLogoPath(String path) {
		return new StringBuffer(path).append("/client/images/emaillogo.jpg");
	}
	 
 
 
	/**
	 * 发送邮箱注册激活码
	 * @return
	 */
	public boolean RegisterActivation(Map<String, Object> data,HttpServletRequest request){
		String path = getRootPath(request);
		boolean flag = false;
		if (null != data) {
			// 进行字符串替换
			String cssPath = getStylePath(path).toString();
			String logoPath = getLogoPath(path).toString();//
			if(null!=data && !data.isEmpty()){
				String email = data.get("email")==null?"":data.get("email").toString();
				String title = data.get("title")==null?"":data.get("title").toString();
				String content = data.get("content")==null?"":data.get("content").toString();
				 
				if (email!=null&&!email.trim().equals("")) {
					// 发送邮件
					//flag = getEmailUtil().sendEmail(email, title,accountEmail);
					flag = getEmailUtil().sendEmail(email, title,content);
					LOGGER.debug("邮件发送状态=" + flag);
					System.out.println("邮件发送！！" + flag);
				}
			}
		}
		return flag;
	}
	
	
	public EmailUtil getEmailUtil() {
		return emailUtil;
	}

	public void setEmailUtil(EmailUtil emailUtil) {
		this.emailUtil = emailUtil;
	}

	public String getDominPath(HttpServletRequest request) {
		String domin = request.getScheme() + "://" + request.getServerName() ;
		if(80 != request.getServerPort()){
			domin += (":"+request.getServerPort());
		}
		return domin ;
	}
 
}