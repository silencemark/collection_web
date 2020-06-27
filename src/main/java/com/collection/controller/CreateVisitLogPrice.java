package com.collection.controller;


import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collection.service.IAppVipCardService;
import com.collection.util.Constants;
import com.collection.util.SDKTestSendTemplateSMS;

@Service

public class CreateVisitLogPrice implements Job {
	@Autowired
	private static CreateVisitLogPrice createVisitLogPrice;
	private IAppVipCardService appVipCardService;
	public void init() {
		createVisitLogPrice = this;
		createVisitLogPrice.appVipCardService=this.appVipCardService;
	}

	public IAppVipCardService getappVipCardService() {
		return createVisitLogPrice.appVipCardService;
	}

	public void setappVipCardService(IAppVipCardService appVipCardService) {
		this.appVipCardService = appVipCardService;
	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		System.out.println("--------quertz 10:00 reserveamount");
	    //List<Map<String, Object>> returnlist=getappVipCardService().getReturnAmountListagree();
		List<Map<String, Object>> returnlist= null;
	    for(Map<String, Object> returnmap: returnlist){
	    	try {
		    	String userid=returnmap.get("createid")+"";
				String title="您有一个备用金归还日期将至,点此查看详情";
				String url="/oa/reserve_check.html?reserveamountid="+returnmap.get("reserveamountid");
				//获取推送的类型
				String type = Constants.JPUSHTYPE;
				//registrationid推送
				if(type.equals("registrationid")){
					//查询用户的registrationid
					try {
						//String registrationid = getappVipCardService().getRegistrationIdByUserId(userid);
						String registrationid = "";
						if(!"".equals(registrationid) && registrationid != null){
							//推送信息
							//JPushRegIdUtil.PushUrlByRegId(registrationid, title, url);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}else if(type.equals("userid")){//userid 推送
					try {
						//JPushAliaseUtil.PushUrlByAliase(userid, title, url);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				//发送短信
				SDKTestSendTemplateSMS.sendReturnAmountMessage(returnmap.get("phone")+"",returnmap.get("realname")+"","备用金",returnmap.get("returntime")+"日到期");
	    	} catch (Exception e) {
				// TODO: handle exception
			}
	    }
	    
	}
}
