package com.collection.controller;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collection.service.IAppVipCardService;
import com.collection.util.Constants;

@Service

public class CreateVisitTaskLog implements Job {
	@Autowired
	private static CreateVisitTaskLog createVisitTaskLog;
	//private OfficeService officeService;
	private IAppVipCardService appVipCardService;
	public void init() {
		createVisitTaskLog = this;
		//createVisitTaskLog.officeService=this.officeService;
		createVisitTaskLog.appVipCardService = this.appVipCardService;
	}
	
	public IAppVipCardService getappVipCardService() {
		return createVisitTaskLog.appVipCardService;
	}

	public void setappVipCardService(IAppVipCardService appVipCardService) {
		this.appVipCardService = appVipCardService;
	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		
		try {
			Map<String,Object> taskmap = new HashMap<String,Object>();
			
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			calendar.add(Calendar.HOUR_OF_DAY, 1); 
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);
			calendar.set(year, month, day, hour, minute, second);
			Date date = calendar.getTime();
			taskmap.put("endtime", date);
		
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * 推送通知栏信息
	 * @param userid
	 * @param title
	 * @param url
	 */
	public void JPushAndriodAndIosMessage(String userid, String title, String url){
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
	} 
}
