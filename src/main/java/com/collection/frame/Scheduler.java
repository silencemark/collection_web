package com.collection.frame;

/*
 * Scheduler.java
 */
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.collection.common.BaseTask;
import com.collection.common.SpringBean;
import com.collection.common.SpringContext;
import com.collection.common.TaskExecutor;
import com.collection.service.ITaskService;
import com.collection.util.BaseUtils;

public class Scheduler {
	//任务失效状态
	public static final int EXPIRE  = 0;
	//任务正常状态
	public static final int NORMAL  = 1;
	//任务暂停状态
	public static final int PAUSE   = 2;
	//任务运行状态(内部使用)
	private static final int RUNNING = 3;
	//任务分配线程
	private static Thread thread = null;
	//任务处理服务
	private static ITaskService service;
	//使用注解的任务
	private static Map<String, String> taskAnno = new HashMap<String, String>();
	//兼容老功能
	private static Map<String, Integer> taskIds = new ConcurrentHashMap<String, Integer>();
	//任务集合
	private static Map<Integer, WorkTask> taskAll = new ConcurrentHashMap<Integer, WorkTask>();

	//日志操作
	private static final Logger logger = Logger.getLogger(Scheduler.class);

	/**
	 * 构造方法私有
	 */
	private Scheduler() {}
	
	/**
	 * 初始化定时任务调度器
	 * @param pkgPattern 指定包(可以有多个,逗号分隔)
	 * @throws Exception 
	 */
	public static synchronized void initialize(String pkgPattern) throws Exception {
		if (service != null) {
			logger.info("调度器已初始化");
			return;
		}
		
		//调度器服务
		service = (ITaskService) SpringContext.getBean(SpringBean.TASK_SERVICE);
		if (service == null) {
			throw new EBusiness("Task Service is null");
		}

		//获取添加BaseTask注解的类
		List<Class<?>> list = BaseUtils.doScaner(pkgPattern, BaseTask.class);
		for (Class<?> clazz : list) {
			if (!Crontask.class.isAssignableFrom(clazz)) {
				throw new EBusiness("[" + clazz.getSimpleName() + "] must extends Crontask");
			}
			Annotation anno = clazz.getAnnotation(BaseTask.class);
			if (anno != null) {
				taskAnno.put(clazz.getName(), BaseUtils.getClassBean(clazz));
			}
		}
		
		//清空任务集合
		taskAll.clear();
		taskIds.clear();
		//初始化定时任务
		List<Map<String, Object>> tasks = service.initialize();
		for (Map<String, Object> data : tasks) {
			Integer taskId = -1;
			String taskName = "";
			String taskClass = "";
			try {
				taskId = Integer.valueOf(data.get("taskid").toString());
				taskName = (String) data.get("taskname");
				taskClass = (String) data.get("taskclass");
				String bean = taskAnno.get(taskClass);
				//获取定时任务
				Crontask task = (Crontask) (bean != null ? SpringContext.getBean(bean) : Class.forName(taskClass).newInstance());
				//生成任务对象
				WorkTask crontask = new WorkTask(task, taskId, taskName);
				//设置任务状态
				crontask.setStatus(Integer.valueOf(data.get("status").toString()));
				//上次执行时间不为空则更新
				crontask.setExecTime((String) data.get("exectime"));
				//下次执行时间不为空则更新
				crontask.setNextTime((String) data.get("nexttime"));
				//如果下次执行时间为空
				if (crontask.getNextTime() == null) {
					crontask.calcNextTime();
				}
				//添加到任务集合
				taskAll.put(taskId, crontask);
				//兼容功能
				taskIds.put(taskClass, taskId);
				//日志输出
				logger.info("任务[" + taskId + ":" + taskClass + "]初始化成功");
			} catch (Throwable e) {
				logger.error("任务[" + taskId + ":" + taskClass + "]初始化失败:" + e.getMessage(), e);
			}
		}
		logger.info("定时任务初始化完成");
	}
	
	/**
	 * 启动任务调度器
	 */
	public static synchronized void startup() {
		// 调度器已启动则返回
		if (thread != null) {
			logger.info("任务调度器已启动!");
			return;
		}
	
		// 任务调度器
		final TaskExecutor executor = (TaskExecutor) SpringContext.getBean(SpringBean.TASK_EXECUTOR);
		if (executor == null) {
			throw new EBusiness("Task Executor is null");
		}
		
		// 启动任务分配线程
		thread = new Thread(new Runnable() {
			public void run() {
				// 任务分配
				while (true) {
					try {
						Date now = new Date();
						WorkTask crontask = null;
						Iterator<Entry<Integer, WorkTask>> it = taskAll.entrySet().iterator();
						while (it.hasNext()) {
							crontask = it.next().getValue();
							if (crontask != null && crontask.getStatus() == NORMAL) {
								if (crontask.m_lock.tryLock()) {
									try {
										if (crontask.getStatus() == NORMAL) {
											Date nextTime = crontask.getNextTime();
											//如果下次执行时间为空且任务加载失败,则重新加载任务信息
											if (nextTime == null && !crontask.isLoad() && crontask.loadCrontab()) {
												//任务加载成功则重新计算下次执行时间
												nextTime = crontask.calcNextTime();
											}
											
											//下次执行时间不为空且已过执行时间,则执行任务
											if (nextTime != null && !now.before(nextTime)) {
												crontask.setStatus(RUNNING);
												executor.submit(crontask);
												logger.info("任务[" + crontask.getTaskID() + ":" + crontask.getTaskClass() + "]调度开始");
											}
										}
									} catch (Throwable e) {
										logger.warn("任务[" + crontask.getTaskID() + ":" + crontask.getTaskClass() + "]处理异常:" + e.getMessage(), e);
									} finally {
										crontask.m_lock.unlock();
									}
								}
							}
						}
						Thread.sleep(200);
					} catch (InterruptedException e) {
						break; // 线程中断则停止分配任务并释放线程池
					} catch (Throwable e) {
						logger.warn("任务分配发生异常:" + e.getMessage(), e);
					}
				}
			}
		});
		thread.start();
		//记录启动日志
		logger.info("任务调度器启动完成!");
	}
	
	/**
	 * 停止任务调度器
	 */
	public static synchronized void shutdown() {
		//中断分配任务线程
		if (thread != null) {
			try {
				thread.interrupt();
				thread.join();
			} catch (Exception e) {
				logger.info("线程" + thread.getId() + "停止异常:" + e.getMessage());
			}
		}
		
		//调度器置为空
		thread = null;
		//记录日志
		logger.info("任务调度器停止完成!");
	}
	
	/**
	 * 根据任务ID获取定时任务信息
	 * @param taskId 任务ID
	 * @return 任务列表(如果taskId为空,则返回所有定时任务信息)
	 */
	public static ArrayList<ArrayList<String>> getTasks(int taskId) {
		ArrayList<ArrayList<String>> taskList = new ArrayList<ArrayList<String>>();
		// 添加列名
		ArrayList<String> column = new ArrayList<String>();
		column.add("任务ID");
		column.add("任务Class");
		column.add("任务名称");
		column.add("任务表达式");
		column.add("开始时间(天)");
		column.add("结束时间(天)");
		column.add("开始日期");
		column.add("结束日期");
		column.add("延迟秒数");
		column.add("上次执行时间");
		column.add("下次执行时间");
		column.add("任务状态");
		column.add("生效日期");
		column.add("失效日期");
		taskList.add(column);

		// 获取定时任务
		try {
			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("taskid", taskId);
			List<Map<String, Object>> result = service.getTasks(requestMap);
			for (Map<String, Object> data : result) {
				column = new ArrayList<String>();
				column.add(data.get("taskid").toString());
				column.add((String) data.get("taskclass"));
				column.add((String) data.get("taskname"));
				column.add((String) data.get("crontab"));
				column.add((String) data.get("starttime"));
				column.add((String) data.get("endtime"));
				column.add((String) data.get("startdate"));
				column.add((String) data.get("enddate"));
				column.add(data.get("delaysecods").toString());
				column.add((String) data.get("exectime"));
				column.add((String) data.get("nexttime"));
				int status = Integer.valueOf(data.get("status").toString());
				column.add((status == NORMAL ? "正常" : (status == PAUSE ? "暂停" : (status == EXPIRE ? "失效" : "未知"))));
				column.add((String) data.get("validdate"));
				column.add((String) data.get("invaliddate"));
				taskList.add(column);
			}
		} catch (Exception e) {
			logger.warn("获取定时任务信息失败:" + e.getMessage(), e);
			return null;
		}
		return taskList;
	}
	
	/**
	 * 根据任务Class获取当前定时任务信息
	 * @param taskClass 任务Class名称
	 * @return 任务列表(如果taskId为空,则返回所有定时任务信息)
	 */
	public static ArrayList<ArrayList<String>> getTasks(String taskClass) {
		ArrayList<ArrayList<String>> taskList = new ArrayList<ArrayList<String>>();
		// 添加列名
		ArrayList<String> column = new ArrayList<String>();
		column.add("任务Class");
		column.add("任务名称");
		column.add("任务表达式");
		column.add("开始时间(天)");
		column.add("结束时间(天)");
		column.add("开始日期");
		column.add("结束日期");
		column.add("延迟秒数");
		column.add("上次执行时间");
		column.add("下次执行时间");
		column.add("任务状态");
		column.add("生效日期");
		column.add("失效日期");
		taskList.add(column);

		// 获取定时任务
		try {
			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("taskclass", taskClass);
			List<Map<String, Object>> result = service.getTasks(requestMap);
			for (Map<String, Object> data : result) {
				column = new ArrayList<String>();
				column.add((String) data.get("taskclass"));
				column.add((String) data.get("taskname"));
				column.add((String) data.get("crontab"));
				column.add((String) data.get("starttime"));
				column.add((String) data.get("endtime"));
				column.add((String) data.get("startdate"));
				column.add((String) data.get("enddate"));
				column.add(data.get("delaysecods").toString());
				column.add((String) data.get("exectime"));
				column.add((String) data.get("nexttime"));
				int status = Integer.valueOf(data.get("status").toString());
				column.add((status == NORMAL ? "正常" : (status == PAUSE ? "暂停" : (status == EXPIRE ? "失效" : "未知"))));
				column.add((String) data.get("validdate"));
				column.add((String) data.get("invaliddate"));
				taskList.add(column);
			}
		} catch (Exception e) {
			logger.warn("获取定时任务信息失败:" + e.getMessage(), e);
			return null;
		}
		return taskList;
	}
	
	/**
	 * 添加定时任务(隔日生效)
	 * @param taskClass 定时任务class
	 * @param taskName  定时任务名称
	 * @param crontab   定时任务表达式对象
	 * @return 任务ID
	 * @throws Exception
	 */
	public static synchronized int newTask(Class<? extends Crontask> taskClass, String taskName, Crontab crontab) throws Exception {
		// 调度器是否启动
		if (thread == null) {
			throw new EBusiness("任务调度器未启动!");
		}
		// 先判断是否有注解
		String bean = taskAnno.get(taskClass.getName());
		// 任务实例化
		Crontask task = (Crontask) (bean != null ? SpringContext.getBean(bean) : taskClass.newInstance());
		// 生成任务对象
		WorkTask crontask = new WorkTask(task, taskName, crontab);
		// 计算下次执行时间
		crontask.calcNextTime();
		// 添加到任务集合
		taskAll.put(crontask.getTaskID(), crontask);
		// 兼容老功能
		taskIds.put(crontask.getTaskClass(), crontask.getTaskID());
		// 日志记录
		logger.info("任务[" + taskName + "]已添加到定时任务调度列表中, 任务ID: " + crontask.getTaskID());
		// 返回任务ID
		return crontask.getTaskID();
	}
	
	/**
	 * 根据任务ID恢复定时任务调度
	 * @param taskId 任务ID
	 */
	public static void start(int taskId) {
		WorkTask crontask = taskAll.get(taskId);
		if (crontask == null) {
			logger.info("任务[" + taskId + "]不存在, 或者任务调度器未启动!");
			throw new EBusiness("任务[" + taskId + "]不存在, 或者任务调度器未启动!");
		}
		
		crontask.m_lock.lock();
		try  {
			//判断任务是否已过期
			if (crontask.getStatus() == EXPIRE) {
				throw new EBusiness("暂停任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]失败, 任务已过期失效");
			}
			
			//重新计算下次执行时间
			crontask.calcNextTime();
			
			try {
				//更新任务状态
				Map<String, Object> requestMap = new HashMap<String, Object>();
				requestMap.put("taskid", taskId);
				requestMap.put("status", NORMAL);
				service.update(requestMap);
				
				//设置任务状态
				crontask.setStatus(NORMAL);
				logger.info("任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]已恢复");
			} catch (Exception e) {
				throw new EBusiness("更新任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]状态失败:" + e.getMessage());
			}
		} finally {
			crontask.m_lock.unlock();
		}
	}
	
	/**
	 * 根据任务Class恢复定时任务调度
	 * 此功能只适用于TASKCLASS与TASKID唯一映射的情况，其他情况不适用
	 * @param taskClass 任务Class名称
	 */
	public static void start(String taskClass) {
		WorkTask crontask = null;
		Integer taskId = taskIds.get(taskClass);
		if (taskId == null || ((crontask = taskAll.get(taskId)) == null)) {
			logger.info("任务[" + taskClass + "]不存在, 或者任务调度器未启动!");
			throw new EBusiness("任务[" + taskClass + "]不存在, 或者任务调度器未启动!");
		}

		crontask.m_lock.lock();
		try  {
			//判断任务是否已过期
			if (crontask.getStatus() == EXPIRE) {
				throw new EBusiness("暂停任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]失败, 任务已过期失效");
			}
			
			//重新计算下次执行时间
			crontask.calcNextTime();
			
			try {
				//更新任务状态
				Map<String, Object> requestMap = new HashMap<String, Object>();
				requestMap.put("taskid", taskId);
				requestMap.put("status", NORMAL);
				service.update(requestMap);
				
				//设置任务状态
				crontask.setStatus(NORMAL);
				logger.info("任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]已恢复");
			} catch (Exception e) {
				throw new EBusiness("更新任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]状态失败:" + e.getMessage());
			}
		} finally {
			crontask.m_lock.unlock();
		}
	}
	
	/**
	 * 根据任务ID暂停定时任务调度
	 * @param taskId 任务ID
	 */
	public static void stop(int taskId) {
		WorkTask crontask = taskAll.get(taskId);
		if (crontask == null) {
			logger.info("任务[" + taskId + "]不存在, 或者任务调度器未启动!");
			throw new EBusiness("任务[" + taskId + "]不存在, 或者任务调度器未启动!");
		}
		
		crontask.m_lock.lock();
		try {
			//判断任务是否已过期
			if (crontask.getStatus() == EXPIRE) {
				throw new EBusiness("暂停任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]失败, 任务已过期失效");
			}
			
			try {
				//更新任务状态
				Map<String, Object> requestMap = new HashMap<String, Object>();
				requestMap.put("taskid", taskId);
				requestMap.put("status", PAUSE);
				service.update(requestMap);
				
				//设置任务状态
				crontask.setStatus(PAUSE);
				logger.info("任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]已暂停");
			} catch (Exception e) {
				throw new EBusiness("更新任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]状态失败:" + e.getMessage());
			}
		} finally {
			crontask.m_lock.unlock();
		}
	}
	
	/**
	 * 根据任务Class暂停定时任务调度
	 * 此功能只适用于TASKCLASS与TASKID唯一映射的情况，其他情况不适用
	 * @param taskClass 任务Class名称
	 */
	public static void stop(String taskClass) {
		WorkTask crontask = null;
		Integer taskId = taskIds.get(taskClass);
		if (taskId == null || ((crontask = taskAll.get(taskId)) == null)) {
			logger.info("任务[" + taskClass + "]不存在, 或者任务调度器未启动!");
			throw new EBusiness("任务[" + taskClass + "]不存在, 或者任务调度器未启动!");
		}
		
		crontask.m_lock.lock();
		try {
			//判断任务是否已过期
			if (crontask.getStatus() == EXPIRE) {
				throw new EBusiness("暂停任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]失败, 任务已过期失效");
			}

			try {
				//更新任务状态
				Map<String, Object> requestMap = new HashMap<String, Object>();
				requestMap.put("taskid", taskId);
				requestMap.put("status", PAUSE);
				service.update(requestMap);
				
				//设置任务状态
				crontask.setStatus(PAUSE);
				logger.info("任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]已暂停");
			} catch (Exception e) {
				throw new EBusiness("更新任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]状态失败:" + e.getMessage());
			}
		} finally {
			crontask.m_lock.unlock();
		}
	}
	
	/**
	 * 根据任务ID更新调度任务(指定时间生效和失效)
	 * @param taskId    任务ID
	 * @param crontab   定时任务表达式对象
	 * @param validDate 新定时任务有效期(yyyy-MM-dd或yyyy-MM-dd HH:mm:ss),为空则隔日生效
	 */
	public static void update(int taskId, Crontab crontab, String validDate) {
		if (crontab == null) {
			throw new EBusiness("定时任务表达式对象不允许为空");
		}
		
		WorkTask crontask = taskAll.get(taskId);
		if (crontask == null) {
			logger.info("任务[" + taskId + "]不存在, 或者任务调度器未启动!");
			throw new EBusiness("任务[" + taskId + "]不存在, 或者任务调度器未启动!");
		}
		
		crontask.m_lock.lock();
		try  {
			//如果是yyyy-MM-dd则补全
			if (validDate != null && validDate.trim().length() <= 10) {
				validDate = validDate.trim() + " 00:00:00";
			}
			
			//更新任务信息
			crontask.setCrontab(crontab, validDate, null);
			//任务没有在执行则重新计算下次执行时间
			if (crontask.getStatus() != RUNNING) {
				crontask.calcNextTime();
			}
			logger.info("任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]的调度信息已更新");
		} finally {
			crontask.m_lock.unlock();
		}
	}
	/**
	 * 根据任务ID更新调度任务(指定时间生效和失效)
	 * @param taskId    任务ID
	 * @param crontab   定时任务表达式对象
	 * @param validDate 新定时任务生效日期(yyyy-MM-dd或yyyy-MM-dd HH:mm:ss),为空则隔日生效
	 * @param invalDate 新定时任务失效日期
	 */
	public static void update(int taskId, Crontab crontab, String validDate,String invalDate) {
		if (crontab == null) {
			throw new EBusiness("定时任务表达式对象不允许为空");
		}
		
		WorkTask crontask = taskAll.get(taskId);
		if (crontask == null) {
			logger.info("任务[" + taskId + "]不存在, 或者任务调度器未启动!");
			throw new EBusiness("任务[" + taskId + "]不存在, 或者任务调度器未启动!");
		}
		
		crontask.m_lock.lock();
		try  {
			//如果是yyyy-MM-dd则补全
			if (validDate != null && validDate.trim().length() <= 10) {
				validDate = validDate.trim() + " 00:00:00";
			}
			
			//更新任务信息
			crontask.setCrontab(crontab, validDate, invalDate);
			//任务没有在执行则重新计算下次执行时间
			if (crontask.getStatus() != RUNNING) {
				crontask.calcNextTime();
			}
			logger.info("任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]的调度信息已更新");
		} finally {
			crontask.m_lock.unlock();
		}
	}
	
	/**
	 * 根据任务ID删除定时任务调度（置为过期）
	 * @param taskId 任务ID
	 */
	public static void delete(int taskId) {
		WorkTask crontask = taskAll.get(taskId);
		if (crontask == null) {
			logger.info("任务[" + taskId + "]不存在, 或者任务调度器未启动!");
			throw new EBusiness("任务[" + taskId + "]不存在, 或者任务调度器未启动!");
		}
		
		crontask.m_lock.lock();
		try {
			//判断任务是否已过期
			if (crontask.getStatus() == EXPIRE) {
				return;
			}
			
			try {
				//更新任务状态
				Map<String, Object> requestMap = new HashMap<String, Object>();
				requestMap.put("taskid", taskId);
				requestMap.put("status", EXPIRE);
				service.update(requestMap);
				//设置任务状态
				crontask.setStatus(EXPIRE);
				logger.info("任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]已废弃");
			} catch (Exception e) {
				throw new EBusiness("更新任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]状态失败:" + e.getMessage());
			}
		} finally {
			crontask.m_lock.unlock();
		}
	}
	
	/**
	 * 根据任务Class删除定时任务调度（置为过期）
	 * 此功能只适用于TASKCLASS与TASKID唯一映射的情况，其他情况不适用
	 * @param taskClass 任务Class名称
	 */
	public static void delete(String taskClass) {
		WorkTask crontask = null;
		Integer taskId = taskIds.get(taskClass);
		if (taskId == null || ((crontask = taskAll.get(taskId)) == null)) {
			logger.info("任务[" + taskClass + "]不存在, 或者任务调度器未启动!");
			throw new EBusiness("任务[" + taskClass + "]不存在, 或者任务调度器未启动!");
		}
		
		crontask.m_lock.lock();
		try {
			//判断任务是否已过期
			if (crontask.getStatus() == EXPIRE) {
				return;
			}

			try {
				//更新任务状态
				Map<String, Object> requestMap = new HashMap<String, Object>();
				requestMap.put("taskid", taskId);
				requestMap.put("status", EXPIRE);
				service.update(requestMap);
				
				//设置任务状态
				crontask.setStatus(EXPIRE);
				logger.info("任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]已废弃");
			} catch (Exception e) {
				throw new EBusiness("更新任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]状态失败:" + e.getMessage());
			}
		} finally {
			crontask.m_lock.unlock();
		}
	}
	
	/**
	 * 根据任务ID更新调度任务(隔日生效)
	 * @param taskId  任务ID
	 * @param crontab 定时任务表达式对象
	 */
	public static void update(int taskId, Crontab crontab) {
		//有效期设置为空,默认下一个工作日生效,失效期默认为空
		update(taskId, crontab, null);
	}
	
	/**
	 * 根据任务Class更新调度任务(指定时间生效和失效)
	 * 此功能只适用于TASKCLASS与TASKID唯一映射的情况，其他情况不适用
	 * @param taskClass  任务Class名称
	 * @param crontab    定时任务表达式对象
	 * @param validDate  新定时任务有效期(yyyy-MM-dd或yyyy-MM-dd HH:mm:ss),为空则隔日生效
	 */
	public static void update(String taskClass, Crontab crontab, String validDate) {
		if (crontab == null) {
			throw new EBusiness("定时任务表达式对象不允许为空");
		}
		
		WorkTask crontask = null;
		Integer taskId = taskIds.get(taskClass);
		if (taskId == null || ((crontask = taskAll.get(taskId)) == null)) {
			logger.info("任务[" + taskClass + "]不存在, 或者任务调度器未启动!");
			throw new EBusiness("任务[" + taskClass + "]不存在, 或者任务调度器未启动!");
		}
		
		crontask.m_lock.lock();
		try {
			//如果是yyyy-MM-dd则补全
			if (validDate != null && validDate.trim().length() <= 10) {
				validDate = validDate.trim() + " 00:00:00";
			}
			
			//更新任务信息
			crontask.setCrontab(crontab, validDate, null);
			//任务没有在执行则重新计算下次执行时间
			if (crontask.getStatus() != RUNNING) {
				crontask.calcNextTime();
			}
			logger.info("任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]的调度信息已更新");
		} finally {
			crontask.m_lock.unlock();
		}
	}
	
	/**
	 * 根据任务Class更新调度任务(隔日生效)
	 * 此功能只适用于TASKCLASS与TASKID唯一映射的情况，其他情况不适用
	 * @param taskClass 任务Class名称
	 * @param crontab   定时任务表达式对象
	 */
	public static void update(String taskClass, Crontab crontab) {
		//有效期设置为空,默认下一个工作日生效,失效期默认为空
		update(taskClass, crontab, null);
	}
	
	/**
	 * 根据任务ID直接执行任务
	 * @param taskId 任务ID
	 */
	public static void execute(int taskId) {
		WorkTask crontask = taskAll.get(taskId);
		if (crontask == null) {
			logger.info("任务[" + taskId + "]不存在, 或者任务调度器未启动!");
			throw new EBusiness("任务[" + taskId + "]不存在, 或者任务调度器未启动!");
		}
		
		crontask.m_lock.lock();
		try {
			if (crontask.getStatus() == RUNNING) {
				throw new EBusiness("任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]正在执行中, 无需重新执行");
			}
			crontask.setStatus(RUNNING);
		} finally {
			crontask.m_lock.unlock();
		}
		
		crontask.execute();
		
		// 更新任务状态
		crontask.m_lock.lock();
		try {
			crontask.setStatus(crontask.getStatus() == RUNNING ? NORMAL : crontask.getStatus());
		} finally {
			crontask.m_lock.unlock();
		}
		logger.info("任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]执行完成");
	}
	
	/**
	 * 根据任务Class直接执行任务
	 * 此功能只适用于TASKCLASS与TASKID唯一映射的情况，其他情况不适用
	 * @param taskClass 任务Class名称
	 */
	public static void execute(String taskClass) {
		WorkTask crontask = null;
		Integer taskId = taskIds.get(taskClass);
		if (taskId == null || ((crontask = taskAll.get(taskId)) == null)) {
			logger.info("任务[" + taskClass + "]不存在, 或者任务调度器未启动!");
			throw new EBusiness("任务[" + taskClass + "]不存在, 或者任务调度器未启动!");
		}
		
		crontask.m_lock.lock();
		try {
			if (crontask.getStatus() == RUNNING) {
				throw new EBusiness("任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]正在执行中, 无需重新执行");
			}
			crontask.setStatus(RUNNING);
		} finally {
			crontask.m_lock.unlock();
		}
		
		crontask.execute();
		
		// 更新任务状态
		crontask.m_lock.lock();
		try {
			crontask.setStatus(crontask.getStatus() == RUNNING ? NORMAL : crontask.getStatus());
		} finally {
			crontask.m_lock.unlock();
		}
		logger.info("任务[" + crontask.getTaskID() + ":" + crontask.getTaskName() + "]执行完成");
	}

	/**
	 * 调度任务类<不允许修改类的访问级别>
	 */
	protected static class WorkTask implements Runnable {
		//任务对象
		private Crontask m_task      = null;
		//任务ID
		private int      m_taskid    = -1;
		//任务Class名称
		private String   m_taskClass = null;
		//任务名称
		private String   m_taskName  = null;
		
		//任务crontab
		private Crontab  m_crontab   = null;
		//任务当前执行时间
		private Date     m_execTime  = null;
		//任务下次执行时间
		private Date     m_nextTime  = null;
		//任务状态
		private int      m_status    = NORMAL;
		//crontab的有效期日期
		private Calendar m_validDate = null;
		//任务加载标识(true-成功 false-失败)
		private boolean  m_isLoad    = true;
		//可重入互斥锁
		protected ReentrantLock m_lock = new ReentrantLock(true);
		//时间格式
		private final SimpleDateFormat fmtAll = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		/**
		 * 构造方法(已有任务构造)
		 * @param task      任务对象
		 * @param taskid    任务ID
		 * @param taskName  定时任务名称
		 */
		public WorkTask(Crontask task, int taskid, String taskName) {
			if (task == null) {
				throw new EBusiness("定时任务对象不允许为空");
			}
			
			if (taskName == null || taskName.trim().length() <= 0) {
				throw new EBusiness("定时任务名称不允许为空");
			}
			
			//设置任务
			m_task = task;
			m_taskClass = m_task.getClass().getName();
			
			//设置任务名称
			m_taskid = taskid;
			m_taskName = taskName;
			
			//加载crontab
			if (!loadCrontab()) {
				throw new EBusiness("任务[" + m_taskid + ":" + m_taskClass + "]的表达式加载失败");
			}
			
			//初始化Task
			m_task.initialize(this);
			logger.info("任务[" + m_taskid + ":" + m_taskClass + "]的表达式信息[" + (m_crontab == null ? "" : m_crontab) + "]");
		}
		
		/**
		 * 构造方法(新任务构造)
		 * @param task      任务对象
		 * @param taskName  定时任务名称
		 * @param crontab   定时任务表达式,格式:分 时 日 月 周
		 */
		public WorkTask(Crontask task, String taskName, Crontab crontab) {
			if (task == null) {
				throw new EBusiness("定时任务对象不允许为空");
			}
			
			if (taskName == null || taskName.trim().length() <= 0) {
				throw new EBusiness("定时任务名称不允许为空");
			}
			
			//设置任务
			m_task = task;
			m_taskClass = m_task.getClass().getName();
			
			//设置任务名称
			m_taskName = taskName;
			
			//添加任务信息
			try {
				Map<String, Object> requestMap = new HashMap<String, Object>();
				requestMap.put("taskclass", m_taskClass);
				requestMap.put("taskname", m_taskName);
				requestMap.put("status", m_status);
				m_taskid = service.add(requestMap);
				logger.info("新增任务唯一taskid："+m_taskid);
			} catch (Exception e) {
				throw new EBusiness("保存定时任务到数据库错误:" + e.getMessage());
			}
			
			//设置任务表达式(隔日生效)
			setCrontab(crontab, null, null);
			
			//初始化Task
			m_task.initialize(this);
			logger.info("任务[" + m_taskid + ":" + m_taskClass + "]的表达式信息[" + (m_crontab == null ? "" : m_crontab) + "]");
		}
		
		/**
		 * 获取任务ID(对象构造后不会变的值)
		 * @return 任务ID
		 */
		public int getTaskID() {
			return m_taskid;
		}
		
		/**
		 * 获取任务Class名称(对象构造后不会变的值)
		 * @return 任务Class名称
		 */
		public String getTaskClass() {
			return m_taskClass;
		}

		/**
		 * 获取任务名称
		 * @return 定时任务名称
		 */
		public String getTaskName() {
			return m_taskName;
		}
		
		/**
		 * 设置新的crontab任务
		 * @param crontab   定时任务表达式对象
		 * @param validDate 新定时任务有效期(yyyy-MM-dd HH:mm:ss)
		 * @param invalDate 新定时任务失效期(yyyy-MM-dd HH:mm:ss)
		 */
		private void setCrontab(Crontab crontab, String validDate, String invalDate) {
			if (crontab == null) {
				throw new EBusiness("定时任务表达式对象不允许为空");
			}
			
			try {
				Calendar validdate = null;
				if (validDate != null && validDate.trim().length() > 0) {
					validdate = Calendar.getInstance();
					validdate.setTime(fmtAll.parse(validDate));
					validdate.set(Calendar.MILLISECOND, 0);
				}
				
				//如果有效期为空,则设置为下一日生效
				if (validdate == null) {
					validDate = fmtAll.format(new Date(System.currentTimeMillis() + 24*60*60*1000)).substring(0, 10);
					validDate = validDate + " 00:00:00";
				}
				
				//如果失效期不为空,则判断其
				if (invalDate != null) {
					Date invaldate = fmtAll.parse(invalDate);
					if (invaldate.before(fmtAll.parse(validDate))) {
						throw new EBusiness("任务表达式生效时间必须小于等于失效时间");
					}
					
					if (!invaldate.after(new Date())) {
						throw new EBusiness("任务表达式失效时间必须大于当前时间");
					}
				}
				
				//修改前一任务表达式的失效日期和状态
				Map<String, Object> requestMap = new HashMap<String, Object>();
				requestMap.put("taskid", m_taskid);
				requestMap.put("validdate", validDate);
				service.updateCrontab(requestMap);
					
				
				
				//添加新的任务表达式
				String startDate = crontab.getStartDate();
				String endDate   = crontab.getEndDate();
				requestMap = new HashMap<String, Object>();
				requestMap.put("taskid", m_taskid);
				requestMap.put("crontab", crontab.getCrontab());
				requestMap.put("starttime", crontab.getStartTime());
				requestMap.put("endtime", crontab.getEndTime());
				requestMap.put("startdate", startDate == null ? null : startDate.substring(0, 10));
				requestMap.put("enddate", endDate == null ? null : endDate.substring(0,10));
				requestMap.put("delaysecods", crontab.getDelaySec());
				requestMap.put("status", Scheduler.NORMAL);
				requestMap.put("validdate", validDate);
				requestMap.put("invaliddate", invalDate);
				service.addCrontab(requestMap);
				//重新加载当前任务信息
				loadCrontab();
			} catch (Exception e) {
				throw new EBusiness("更新任务[" + m_taskid + ":" + m_taskName + "]信息失败:" + e.getMessage(), e);
			}
		}
		
		/**
		 * 返回任务表达式信息
		 */
		public Crontab getCrontab() {
			return m_crontab;
		}
		
		/**
		 * 重新加载定时任务表达式信息
		 */
		private boolean loadCrontab() {
			try {
				Map<String, Object> requestMap = new HashMap<String, Object>();
				requestMap.put("taskid", m_taskid);
				Map<String, Object> data = service.getCrontab(requestMap);
				if (data.size() > 0) {
					//创建定时任务表达式对象
					Crontab crontab = new Crontab((String) data.get("crontab"), (String) data.get("starttime"), (String) data.get("endtime"),
							(String) data.get("startdate"), (String) data.get("enddate"), Integer.valueOf(data.get("delaysecods").toString()));

					//设置任务有效期
					Calendar validDate = Calendar.getInstance();
					String   validdate = (String) data.get("validdate");
					validDate.setTime(fmtAll.parse(validdate));
					validDate.set(Calendar.MILLISECOND, 0);
					
					//更新开始日期
					String startdate = crontab.getStartDate();
					if (startdate != null) {
						Calendar startDate = Calendar.getInstance();
						startDate.setTime(fmtAll.parse(startdate));
						startDate.set(Calendar.MILLISECOND, 0);
						if (startDate.before(validDate)) {
							crontab.setStartDate(fmtAll.format(validDate.getTime()));
						}
					} else {
						crontab.setStartDate(fmtAll.format(validDate.getTime()));
					}
					
					//设置任务失效期
					Calendar invalDate = null;
					String   invaldate = (String) data.get("invaliddate");
					if (invaldate != null && invaldate.trim().length() > 0) {
						invalDate = Calendar.getInstance();
						invalDate.setTime(fmtAll.parse(invaldate));
						invalDate.set(Calendar.MILLISECOND, 999);
						//更新结束日期
						String enddate = crontab.getEndDate();
						if (enddate != null) {
							Calendar endDate = Calendar.getInstance();
							endDate.setTime(fmtAll.parse(enddate));
							endDate.set(Calendar.MILLISECOND, 999);
							if (endDate.after(invalDate)) {
								crontab.setEndDate(fmtAll.format(invalDate.getTime()));
							}
						} else {
							crontab.setEndDate(fmtAll.format(invalDate.getTime()));
						}
					}
					
					//更新任务信息
					m_crontab   = crontab;
					m_validDate = validDate;
				} else {
					//任务表达式置空,有效期置空
					m_crontab   = null;
					m_validDate = null;
				}
				//任务加载成功
				m_isLoad = true;
				//记录日志信息
				logger.info("加载任务[" + m_taskid + ":" + m_taskClass + "]信息完成");
			} catch (Throwable e) {
				//任务加载失败
				m_isLoad = false;
				logger.info("加载任务[" + m_taskid + ":" + m_taskClass + "]信息失败:" + e.getMessage(), e);
			}
			
			return m_isLoad;
		}
		
		/**
		 * 设置定时任务上次执行时间
		 * @param execTime 定时任务上次执行时间,格式yyyy-MM-dd HH:mm:ss
		 */
		private void setExecTime(String execTime) {
			if (execTime != null) {
				try {
					m_execTime = fmtAll.parse(execTime);
				} catch (Exception e) {
					throw new EBusiness("上次执行时间[" + execTime + "]格式不正确,正确格式应该是yyyy-MM-dd HH:mm:ss");
				}
			} else {
				m_execTime = null;
			}
		}
		
		/**
		 * 获取定时任务上次执行时间
		 * @return 定时任务下次执行时间
		 */
		public Date getExecTime() {
			return m_execTime;
		}
		
		/**
		 * 设置定时任务下次执行时间
		 * @param nextTime 定时任务下次执行时间,格式yyyy-MM-dd HH:mm:ss
		 */
		private void setNextTime(String nextTime) {
			if (nextTime != null) {
				try {
					m_nextTime = fmtAll.parse(nextTime);
				} catch (Exception e) {
					throw new EBusiness("下次执行时间[" + nextTime + "]格式不正确,正确格式应该是yyyy-MM-dd HH:mm:ss");
				}
			} else {
				m_nextTime = null;
			}
		}
		
		/**
		 * 获取定时任务下次执行时间
		 * @return 定时任务下次执行时间
		 */
		public Date getNextTime() {
			return m_nextTime;
		}
		
		/**
		 * 获取定时任务无延迟的下次执行时间
		 * @return 定时任务下次执行时间
		 */
		public Date getNextTimeNoDelay() {
			if (m_nextTime != null && m_crontab != null && m_crontab.getDelaySec() != 0) {
				return new Date(m_nextTime.getTime() - m_crontab.getDelaySec() * 1000);
			} else {
				return m_nextTime;
			}
		}
		
		/**
		 * 获取当天结束时间
		 * @return 当天结束时间
		 * @throws Exception 
		 */
		public Date getEndTime() {
			if (m_crontab ==  null) {
				throw new EBusiness("定时任务表达式对象为空, 不能跳到下一日");
			}
			
			//结束时间
			Calendar endTime = Calendar.getInstance();
			//获取结束时间
			String time = m_crontab.getEndTime();
			if (time != null && time.trim().length() > 0) {
				endTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.substring(0, 2)));
				endTime.set(Calendar.MINUTE, Integer.valueOf(time.substring(3, 5)));
				endTime.set(Calendar.SECOND, Integer.valueOf(time.substring(6, 8)));
				endTime.set(Calendar.MILLISECOND, 0);
			} else {
				endTime.set(Calendar.HOUR_OF_DAY, 23);
				endTime.set(Calendar.MINUTE, 59);
				endTime.set(Calendar.SECOND, 59);
				endTime.set(Calendar.MILLISECOND, 0);
			}
			return endTime.getTime();
		}
		
		/**
		 * 计算下次执行时间
		 * @return 下次执行时间
		 * @throws Exception 
		 */
		private Date calcNextTime() {
			//计算非延迟下的上次执行时间
			Date execTime = null;
			if (m_execTime != null && m_crontab != null) {
				execTime = m_crontab.getTimeNoDelay(m_execTime);
			}
			
			//计算下次执行时间
			if (m_crontab == null) {
				m_nextTime = null;
			} else {
				m_nextTime = m_crontab.calcNextTime(execTime);
			}
			
			//更新任务信息
			try {
				//如果下次执行时间为空则更新任务表达式信息
				while (m_nextTime == null && m_validDate != null) {
					//更新表达式状态
					Map<String, Object> requestMap = new HashMap<String, Object>();
					requestMap.put("taskid", m_taskid);
					requestMap.put("status", m_nextTime == null ? EXPIRE : NORMAL);
					requestMap.put("validdate", fmtAll.format(m_validDate.getTime()));
					service.updateCrontabStatus(requestMap);
					//加载任务表达式信息
					if (!loadCrontab()) {
						break;
					}
					
					//重新计算下次执行时间
					if (m_crontab == null) {
						m_nextTime = null;
					} else {
						m_nextTime = m_crontab.calcNextTime(execTime);
					}
				}
				//更新任务执行时间
				Map<String, Object> requestMap = new HashMap<String, Object>();
				requestMap.put("taskid", m_taskid);
				requestMap.put("exectime", (m_execTime == null ? null : fmtAll.format(m_execTime)));
				requestMap.put("nexttime", (m_nextTime == null ? null : fmtAll.format(m_nextTime)));
				service.update(requestMap);
			} catch (Throwable e) {
				throw new EBusiness("更新任务[" + m_taskid + ":" + m_taskName + "]信息失败:" + e.getMessage());
			}

			return m_nextTime;
		}
		
		/**
		 * 设置任务状态
		 * @param status 定时任务状态
		 */
		private void setStatus(int status) {
			// 更新任务状态
			m_status = status;
		}
		
		/**
		 * 获取任务状态
		 * @return 定时任务状态
		 */
		public int getStatus() {
			return m_status;
		}
		
		/**
		 * 跳到下一日执行
		 */
		public void nextDate() {
			if (m_crontab ==  null) {
				throw new EBusiness("定时任务表达式对象为空, 不能跳到下一日");
			}
			
			//默认下一日
			String nextDate = fmtAll.format(new Date(System.currentTimeMillis()+24*60*60*1000)).substring(0, 10);
			
			//如果是跨天的情况需要特殊处理
			String stime = m_crontab.getStartTime();
			String etime = m_crontab.getEndTime();
			if (stime != null && etime != null) {
				Calendar sTime = Calendar.getInstance();
				Calendar eTime = Calendar.getInstance();
				sTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(stime.substring(0, 2)));
				sTime.set(Calendar.MINUTE, Integer.valueOf(stime.substring(3, 5)));
				sTime.set(Calendar.SECOND, Integer.valueOf(stime.substring(6, 8)));
				sTime.set(Calendar.MILLISECOND, 0);
				eTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(etime.substring(0, 2)));
				eTime.set(Calendar.MINUTE, Integer.valueOf(etime.substring(3, 5)));
				eTime.set(Calendar.SECOND, Integer.valueOf(etime.substring(6, 8)));
				eTime.set(Calendar.MILLISECOND, 0);
				if (!sTime.before(eTime) && System.currentTimeMillis() < sTime.getTimeInMillis()) {
					nextDate = fmtAll.format(sTime.getTime()).substring(0, 10);
				}
			}
			//设置开始日期为下一日
			m_crontab.setStartDate(nextDate);
		}
		
		/**
		 * 获取任务加载标识
		 * @return 获取任务加载标识(true-成功 false-失败)
		 */
		private boolean isLoad() {
			return m_isLoad;
		}
		
		/**
		 * 执行任务
		 */
		private void execute() {
			try {
				m_task.run();
			} catch (Throwable e) {
				logger.warn("任务[" + m_taskid + ":" + m_taskName + "]执行异常:" + e.getMessage(), e);
			}
		}
		
		/**
		 * 执行定时任务
		 */
		public void run() {
			// 加锁
			m_lock.lock();
			try {
				logger.info("任务[" + m_taskid + ":" + m_taskName + "]执行开始, 本次执行时间[" + (m_nextTime == null ? " " : fmtAll.format(m_nextTime)) + "]");
			} finally {
				m_lock.unlock(); // 解锁
			}
			
			// 执行任务
			execute();
			
			// 加锁
			m_lock.lock();
			Date nextTime = null;
			try {
				// 更新任务执行时间
				m_execTime = m_nextTime;
				// 计算下次执行时间
				nextTime = calcNextTime();
				// 记录日志
				logger.info("任务[" + m_taskid + ":" + m_taskName + "]执行结束, 下次执行时间[" + (nextTime == null ? " " : fmtAll.format(nextTime)) + "]");
			} catch (Throwable e) {
				logger.warn("任务[" + m_taskid + ":" + m_taskName + "]执行结束, 计算下次执行时间异常:" + e.getMessage(), e);
			} finally {
				m_status = (m_status == RUNNING ? NORMAL : m_status); // 更新任务状态
				m_lock.unlock(); // 解锁
			}
		}
	}
}
