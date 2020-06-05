package com.collection.controller;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collection.service.IndexService;

@Service

public class CreateVisitLog implements Job {
	@Autowired
	private static CreateVisitLog createVisitLog;
	private IndexService indexService;
	//private MemorandumService memorandumService;
	public void init() {
		createVisitLog = this;
		createVisitLog.indexService=this.indexService;
		//createVisitLog.memorandumService=this.memorandumService;
	}
	/*public MemorandumService getMemorandumService() {
		return createVisitLog.memorandumService;
	}

	public void setMemorandumService(MemorandumService memorandumService) {
		this.memorandumService = memorandumService;
	}*/

	public IndexService getIndexService() {
		return createVisitLog.indexService;
	}

	public void setIndexService(IndexService indexService) {
		this.indexService = indexService;
	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		//System.out.println("--------quertz 2minute 备忘录推送");
		//查询备忘录
		//List<Map<String, Object>> memolist=getMemorandumService().getAllMemorandumList();
		/*for(Map<String, Object> memo:memolist){
			//推送信息
			try {
				String userid=memo.get("createid")+"";
				String title="您有一个备忘录提醒："+memo.get("content")+",点此查看";
				String url="/memorandum/meno_detail.html?memorandumid="+memo.get("memorandumid");
				
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
				System.out.println("推送error-----");
			}
		}*/
	}
}
