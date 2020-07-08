package com.collection.common;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.log4j.Logger;
import com.collection.frame.Scheduler;
import com.collection.frame.ThreadPool;

/**
 * 基础的启停类（定时任务等等）
 */
public class BaseApplication {
	/**
	 * 定时任务所在包名
	 */
	private String taskPkgPattern;
	/**
	 * 日志输出对象
	 */
	private static final Logger logger = Logger.getLogger(BaseApplication.class);

	/**
	 * 设置定时任务所在包名
	 * @param taskPkgPattern
	 */
	public void setTaskPkgPattern(String taskPkgPattern) {
		this.taskPkgPattern = taskPkgPattern;
	}

	/**
	 * 启动应用（定时任务相关）
	 * @throws Exception 
	 */
	@PostConstruct
	public void start() throws Exception {
		Scheduler.initialize(taskPkgPattern);
		Scheduler.startup();
		// 记录日志
		logger.info("Base application start successful");
	}

	/**
	 * 停止应用（定时任务相关）
	 */
	@PreDestroy
	public void stop() {
		try {
			// 停止定时任务调度
			Scheduler.shutdown();
			// 销毁线程池
			ThreadPool.getInstance().shutdown();
			// 记录日志
			logger.info("Base application stop successful");
		} catch (Exception e) {
			logger.info("Base application stop exception: " + e.getMessage(), e);
		}
	}
}
