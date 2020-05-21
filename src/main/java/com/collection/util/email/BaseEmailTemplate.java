package com.collection.util.email;

import javax.servlet.http.HttpServletRequest;

import com.collection.util.Constants;


/**
 * @ClassName:BaseEmailTemplate.java
 * @ClassPath:com.hypers.core.email
 * @Desciption:基础模版发送
 * @Author: Robin
 * @Date: 2014年6月20日 下午6:45:37
 * 
 */
public abstract class BaseEmailTemplate extends BaseEmail {

	private HttpServletRequest request;

	public BaseEmailTemplate(HttpServletRequest request) {
		setRequest(request);
	}

	/**
	 * @function:模版文件
	 * @datetime:2014年6月20日 下午6:52:30
	 * @Author: Robin
	 * @return String
	 */
	protected abstract String getTemplateName();
 
	@SuppressWarnings("unused")
	private String getCssPath() {
		return getRootPath() + "/client/css/style.css";
	}

	@Override
	protected StringBuffer getContentBuffer() {
		// String.format(readTemplateBuffer().toString(), args)
		return null;
	}

	protected String getRootPath() {
		return  Constants.PROJECT_PATH+"";
	}

	protected HttpServletRequest getRequest() {
		return request;
	}

	protected void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	private String getDominPath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":"
				+ request.getServerPort();
	}
}
