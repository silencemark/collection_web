package com.collection.controller;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collection.service.IndexService;
import com.collection.service.oa.OfficeService;
import com.collection.util.Constants;

@Service

public class CreateVisitTaskLog implements Job {
	@Autowired
	private static CreateVisitTaskLog createVisitTaskLog;
	private OfficeService officeService;
	private IndexService indexService;
	public void init() {
		createVisitTaskLog = this;
		createVisitTaskLog.officeService=this.officeService;
		createVisitTaskLog.indexService = this.indexService;
	}
	
	public IndexService getIndexService() {
		return createVisitTaskLog.indexService;
	}

	public void setIndexService(IndexService indexService) {
		this.indexService = indexService;
	}

	public OfficeService getOfficeService() {
		return createVisitTaskLog.officeService;
	}

	public void setOfficeService(OfficeService officeService) {
		this.officeService = officeService;
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
			
			List<Map<String,Object>> tasklist = getOfficeService().getOverTaskOneHour(taskmap);
			if(tasklist != null && tasklist.size() > 0){
				for(Map<String,Object> map : tasklist){
					if(map.containsKey("assislist") && !"".equals(map.get("assislist"))){
						@SuppressWarnings("unchecked")
						List<Map<String,Object>> assislist = (List<Map<String, Object>>) map.get("assislist");
						for(Map<String,Object> mm : assislist){
							//给每个协办人发送信息
							try {
								String userid=mm.get("userid")+"";
								String title="您协助的任务【"+map.get("title")+"】还是有 1 个小时就过期了,点此查看详情";
								String url="/oa/task_detail.html?taskid="+map.get("taskid")+"&userid="+userid+"&cple=yes";
								//获取推送的类型
								String type = Constants.JPUSHTYPE;
								//registrationid推送
								if(type.equals("registrationid")){
									//查询用户的registrationid
									try {
										String registrationid = getIndexService().getRegistrationIdByUserId(userid);
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
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
					}
					
					//给负责人，创建人发送信息
					try {
				    	String userid=map.get("createid")+"";
						String title="您的任务【"+map.get("title")+"】还是有 1 个小时就过期了,点此查看详情";
						String url="/oa/task_detail.html?taskid="+map.get("taskid")+"&userid="+userid+"&cple=yes";
						//获取推送的类型
						String type = Constants.JPUSHTYPE;
						//registrationid推送
						if(type.equals("registrationid")){
							//查询用户的registrationid
							try {
								String registrationid = getIndexService().getRegistrationIdByUserId(userid);
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
						
						userid=map.get("examineuserid")+"";
						title="您负责的任务【"+map.get("title")+"】还是有 1 个小时就过期了,点此查看详情";
						url="/oa/task_detail.html?taskid="+map.get("taskid")+"&userid="+userid+"&cple=yes";
						//registrationid推送
						if(type.equals("registrationid")){
							//查询用户的registrationid
							try {
								String registrationid1 = getIndexService().getRegistrationIdByUserId(userid);
								if(!"".equals(registrationid1) && registrationid1 != null){
									//推送信息
									//JPushRegIdUtil.PushUrlByRegId(registrationid1, title, url);
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
						
			    	} catch (Exception e) {
						// TODO: handle exception
			    		e.printStackTrace();
					}
				}
			}
			
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
				String registrationid = getIndexService().getRegistrationIdByUserId(userid);
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
