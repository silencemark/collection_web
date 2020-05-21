package com.collection.util.email;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName:BaseEmail.java
 * @ClassPath:com.hypers.core.email
 * @Desciption:发送邮件的基础类
 * @Author: Robin
 * @Date: 2014年6月20日 下午6:45:02
 * 
 */
public abstract class BaseEmail {

	@Autowired
	EmailUtil emailUtil;

	/**
	 * @function:收件人
	 * @datetime:2014年6月20日 下午6:43:18
	 * @Author: Robin
	 * @return String
	 */
	protected abstract String toEmail();

	/**
	 * @function:邮件标题
	 * @datetime:2014年6月20日 下午6:43:27
	 * @Author: Robin
	 * @return StringBuffer
	 */
	protected abstract StringBuffer getTitleBuffer();

	/**
	 * @function:邮件内容
	 * @datetime:2014年6月20日 下午6:43:48
	 * @Author: Robin
	 * @return StringBuffer
	 */
	protected abstract StringBuffer getContentBuffer();

	/**
	 * @function:发送邮件
	 * @datetime:2014年6月20日 下午6:44:10
	 * @Author: Robin
	 * @return boolean
	 */
	protected boolean sendEmail() {
		return getEmailUtil().sendEmail(toEmail(), getTitleBuffer().toString(),
				getContentBuffer().toString());
	}

	public EmailUtil getEmailUtil() {
		return emailUtil;
	}

	public void setEmailUtil(EmailUtil emailUtil) {
		this.emailUtil = emailUtil;
	}

}
