package com.collection.util;

import java.util.HashMap;
import java.util.Set;

import com.cloopen.rest.sdk.CCPRestSmsSDK;

public class SDKTestSendTemplateSMS {
	//帐号id
	private static String ACCOUNT_SID="aaf98f894dae9c16014db84e959e089d";
	//帐号令牌
	private static String AUTH_TOKEN="bd87e767a78241898b9f4d9a04c62382";
	//应用id
	private static String APPID="aaf98f894df52772014df69cb246029e";
	private static HashMap<String, Object> result = null;

	//初始化SDK
	private static CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
			
	/**
	 * @param args
	 */
	public static boolean sendSms(String phone,String code,String timenum) {
		
		//******************************注释*********************************************
		//*初始化服务器地址和端口                                                       *
		//*沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
		//*生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883");       *
		//*******************************************************************************
		restAPI.init("app.cloopen.com", "8883");
		
		//******************************注释*********************************************
		//*初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN     *
		//*ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取*
		//*参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN。                   *
		//*******************************************************************************
		restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);
		
		
		//******************************注释*********************************************
		//*初始化应用ID                                                                 *
		//*测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID     *
		//*应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID*
		//*******************************************************************************
		restAPI.setAppId(APPID);
		
		
		//******************************注释****************************************************************
		//*调用发送模板短信的接口发送短信                                                                  *
		//*参数顺序说明：                                                                                  *
		//*第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号                          *
		//*第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。    *
		//*系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
		//*第三个参数是要替换的内容数组。																														       *
		//**************************************************************************************************
		
		//**************************************举例说明***********************************************************************
		//*假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为           *
		//*result = restAPI.sendTemplateSMS("13800000000","1" ,new String[]{"6532","5"});																		  *
		//*则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入     *
		//*********************************************************************************************************************
		result = restAPI.sendTemplateSMS(phone,"37546" ,new String[]{code,timenum});
		
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
			return true;
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return false;
		}
	}
	
	/**
	 * @param args
	 */
	public static boolean sendSmsMessage(String phone,String companyname,String username,String password) {
		
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);
		restAPI.setAppId(APPID);
		result = restAPI.sendTemplateSMS(phone,"108816" ,new String[]{companyname,username,password});
		
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
			return true;
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return false;
		}
	}
	
	/**
	 * 发送请示，和备用金的非常紧急信息
	 * @param phone
	 * @param examinename
	 * @param createname
	 * @param content
	 * @return
	 */
	public static boolean sendUrgentMessage(String phone,String examinename,String createname,String content) {
		
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);
		restAPI.setAppId(APPID);
		result = restAPI.sendTemplateSMS(phone,"117145" ,new String[]{examinename,createname,content});
		
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
			return true;
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return false;
		}
	}
	
	/**
	 * 空间扩容，发送短信提醒
	 * 【餐饮大师】尊敬的{1}，{2}向您申请空间扩容，请尽快处理
	 * @param phone
	 * @param examinename
	 * @param companyname
	 * @return
	 */
	public static boolean sendExpansionMessage(String phone,String examinename,String companyname) {
		
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);
		restAPI.setAppId(APPID);
		result = restAPI.sendTemplateSMS(phone,"117150" ,new String[]{examinename,companyname});
		
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
			return true;
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return false;
		}
	}
	
	/**
	 * 备用金到期通知
	 * 【餐饮大师】尊敬的{1}，您申请{2}已经{3}，请尽快处理！
	 * @param phone
	 * @param username{1}
	 * @param content{2}
	 * @param remark{3}
	 * @return
	 */
	public static boolean sendReturnAmountMessage(String phone,String username,String content,String remark) {
		
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);
		restAPI.setAppId(APPID);
		result = restAPI.sendTemplateSMS(phone,"122213" ,new String[]{username,content,remark});
		
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
			return true;
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return false;
		}
	}
	
	
	/**
	 * 請示，備用金 審覈之後發送的短信提醒
	 * 【餐饮大师】尊敬的{1}，您申请{2}已经{3}，请尽快处理！
	 * @param phone
	 * @param examinename
	 * @param content
	 * @param remark
	 * @return
	 */
	public static boolean sendExamineUrgentMessage(String phone,String examinename,String content,String remark) {
		
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);
		restAPI.setAppId(APPID);
		result = restAPI.sendTemplateSMS(phone,"119764" ,new String[]{examinename,content,remark});
		
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
			return true;
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return false;
		}
	}
	
	/**
	 * 人员删除短信通知
	 * 【餐饮大师】尊敬的{1}，您的帐号已经{2}，如有问题请联系管理员！	
	 * @param phone
	 * @param username
	 * @param content
	 * @return
	 */
	public static boolean sendDeleteUser(String phone,String username,String content) {
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);
		restAPI.setAppId(APPID);
		result = restAPI.sendTemplateSMS(phone,"117147" ,new String[]{username,content});
		
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
			return true;
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return false;
		}
	}
	
	/**
	 * 权限申请通知模板
	 * @param phone
	 * @param username
	 * @param content
	 * @return
	 */
	public static boolean sendPowerApply(String phone,String username,String content) {
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);
		restAPI.setAppId(APPID);
		result = restAPI.sendTemplateSMS(phone,"117148" ,new String[]{username,content});
		
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
			return true;
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return false;
		}
	}
	
	/**
	 * 权限申请审核信息反馈模板
	 * @param phone
	 * @param username
	 * @param content
	 * @return
	 */
	public static boolean sendPowerApplyFeedBack(String phone,String username,String content) {
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);
		restAPI.setAppId(APPID);
		result = restAPI.sendTemplateSMS(phone,"117149" ,new String[]{username,content});
		
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
			return true;
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return false;
		}
	}
	
	/**
	 * 空间扩容申请
	 * @param phone
	 * @param username
	 * @param content
	 * @return
	 */
	public static boolean sendCloudApply(String phone,String username,String content) {
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);
		restAPI.setAppId(APPID);
		result = restAPI.sendTemplateSMS(phone,"117150" ,new String[]{username,content});
		
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
			return true;
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return false;
		}
	}
	
	/**
	 * 公司注册成功之后的短信提醒
	 * 【餐饮大师】恭喜您成为{1}的超级管理员，账号：{2}，请妥善保管。更多功能请登录网址{3}，如有疑问，可拨打服务热线{4}。
	 * @param phone 发送对象的手机号
	 * @param companyname 公司注册者的名字
	 * @param number 公司注册者的手机号
	 * @param website 使用方登陆的网址
	 * @param telphone 热线电话
	 * @return
	 */
	public static boolean sendRegisterCompanySuccessMessage(String phone,String companyname,String number , String website , String telphone) {
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);
		restAPI.setAppId(APPID);
		result = restAPI.sendTemplateSMS(phone,"128938" ,new String[]{companyname,number,website,telphone});
		
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
			return true;
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return false;
		}
	}
	
	/**
	 * 员工邀请短信通知
	 * 【餐饮大师】尊敬的{1}，{2}邀请您加入{3}，您的个人账号是：{4}，密码是：{5}。
	 * @param phone 接受者手机号
	 * @param username 被邀请者的名称
	 * @param invitationname 邀请者的名称
	 * @param companyname 公司名称
	 * @param userphone 被邀请者账号（手机号）
	 * @param loginpwd 登陆密码
	 * @return
	 */
	public static boolean sendBeInvitationUserMessage(String phone,String username,String invitationname , String companyname , String userphone , String loginpwd) {
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);
		restAPI.setAppId(APPID);
		result = restAPI.sendTemplateSMS(phone,"128945" ,new String[]{username,invitationname,companyname,userphone,loginpwd});
		
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
			return true;
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return false;
		}
	}
	
	/**
	 * 注册公司发送验证码
	 * 【餐饮大师】尊敬的用户，欢迎注册餐饮大师，您的本次操作验证码为：{1}，请在{2}分钟内完成操作，如非本人操作请忽略本条短信。
	 * @param phone 接受者手机号码
	 * @param vc 验证码
	 * @param mintue 有效时间
	 * @return
	 */
	public static boolean sendRegisterVerificationCodeMessage(String phone,String vc,String mintue) {
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);
		restAPI.setAppId(APPID);
		result = restAPI.sendTemplateSMS(phone,"128948" ,new String[]{vc,mintue});
		
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
			return true;
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return false;
		}
	}
	
	
}

