package com.collection.util.email;
 

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;


 

/**
 * @ClassName:EmailUtil.java
 * @ClassPath:com.hypers.core.util
 * @Desciption:Email工具，用于发送邮件
 * @Author: robin
 * @Date: 2014年5月29日 下午3:51:45
 * 
 */

public class EmailUtil {
	private static final Logger LOGGER = Logger.getLogger(EmailUtil.class);

	private JavaMailSenderImpl mailSender;
	private SimpleMailMessage mailMessage;

	public EmailUtil() {

	} 
	public SimpleMailMessage getMailMessage() {
		return mailMessage;
	}

	public void setMailMessage(SimpleMailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}

	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}
 
	/**
	 * @function:发送邮件
	 * @datetime:2014年5月29日 下午8:56:36
	 * @Author: robin
	 * @param: @param toMail 接收方地址
	 * @param: @param title 标题
	 * @param: @param content 邮件内容
	 * @param: @return
	 * @return boolean
	 */
	public boolean sendEmail(String toMail, String title, String content) {

		SimpleMailMessage message = new SimpleMailMessage(mailMessage);
		// 设置email内容,
		message.setSubject(title);
		message.setText(content);

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(
					mimeMessage, true, "GBK");
			// 设置发件人
			messageHelper.setFrom(new InternetAddress(mailMessage.getFrom()));
			messageHelper.setTo(toMail);
			// messageHelper.setFrom("bfg@tom.com");
			messageHelper.setSubject(title);
			messageHelper.setText(content, true);
			mailSender.send(mimeMessage);
			return true;
		} catch (MailException e) {
			System.out.println(" send email fail !!! ");
			LOGGER.error(e);
			e.printStackTrace();
		} catch (MessagingException e) {
			LOGGER.error(e);
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		//EmailUtil emailUtil = (EmailUtil) SpringContext.getInstance().getBean("emailUtil");
		EmailUtil emailUtil = new EmailUtil();
		try
		{
			boolean flag = emailUtil
			.sendEmail("yiller_feel@sina.com", "赛飞门户初始登录密码",
					"管理员,您好！<br/>您初始用户名为：lirui<br/>您初始登录密码为：m59Y76QE<br/>点击登录 &nbsp; &nbsp;<a href='http://10.32.8.83/login/login' target='_blank'>http://10.32.8.83/login/login</a>");
			System.out.println("邮件发送！！" + flag);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
