package com.collection.controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;


public class TaskExecution implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		SchedulerFactory sf;   
		Scheduler sched;
		JobDetail job;
		CronTrigger trigger;
		try {
			sf = new StdSchedulerFactory();
			sched = sf.getScheduler();
			// 定时生成日志信息
			job = new JobDetail("memorandum", "group1", CreateVisitLog.class);
			trigger = new CronTrigger("trigger1", "group1", "memorandum", "group1",
					"0 0/2 * * * ?");// 秒：分：时
			sched.addJob(job, true);
			sched.scheduleJob(trigger);
			sched.start();
			
			sf = new StdSchedulerFactory();
			sched = sf.getScheduler();
			// 备用金定时生成日志信息
			job = new JobDetail("reserveprice", "group2", CreateVisitLogPrice.class);
			trigger = new CronTrigger("trigger2", "group2", "reserveprice", "group2",
					"0 0 10 * * ?");// 秒：分：时
			sched.addJob(job, true);
			sched.scheduleJob(trigger);
			sched.start();
			
			
			//监听任务进度，离完成1个小时前 发送提示信息
			sf = new StdSchedulerFactory();
			sched = sf.getScheduler();
			job = new JobDetail("listeningTask","group3",CreateVisitTaskLog.class);
			trigger = new CronTrigger("trigger3","group3","listeningTask","group3",
					"0 0/1 * * * ?");
			sched.addJob(job, true);
			sched.scheduleJob(trigger);
			sched.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}