package com.collection.common;

/**
 * Spring Bean常量定义类
 */
public class SpringBean {
	
	/**
	 * 禁止构造
	 */
	private SpringBean() {}
	/**
	 * Hibernate session factory
	 */
	public static final String SESSION_FACTORY = "sessionFactory";
	/**
	 * 任务调度器
	 */
	public static final String TASK_EXECUTOR = "taskExecutor";
	/**
	 * 任务调度服务
	 */
	public static final String TASK_SERVICE = "taskService";
}
