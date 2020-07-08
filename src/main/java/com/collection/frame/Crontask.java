package com.collection.frame;

import java.util.Date;
import org.apache.log4j.Logger;

/**
 * 定时任务抽象类
 */
public abstract class Crontask implements Runnable {
	// 任务调度信息
	private Scheduler.WorkTask worktask;
	// 日志操作
	protected static final Logger logger = Logger.getLogger(Crontask.class);

	/**
	 * 初始化任务信息
	 */
	final protected void initialize(Scheduler.WorkTask worktask) {
		//只初始化一次
		if (this.worktask != null) {
			return;
		}
		this.worktask = worktask;
	}

	/**
	 * 获取任务ID
	 * @return 任务ID
	 */
	final public int getTaskID() {
		return worktask.getTaskID();
	}

	/**
	 * 获取任务Class名称(对象构造后不会变的值)
	 * @return 任务Class名称
	 */
	final public String getTaskClass() {
		return this.getClass().getName();
	}

	/**
	 * 获取任务名称
	 * @return 定时任务名称
	 */
	final public String getTaskName() {
		return worktask.getTaskName();
	}

	/**
	 * 获取任务的上次执行时间
	 * @return 任务的正常执行时间
	 */
	final public Date getPreTime() {
		worktask.m_lock.lock();
		try {
			return worktask.getExecTime();
		} finally {
			worktask.m_lock.unlock();
		}
	}

	/**
	 * 获取任务的正常执行时间
	 * @return 任务的正常执行时间
	 */
	final public Date getRunTime() {
		worktask.m_lock.lock();
		try {
			return worktask.getNextTime();
		} finally {
			worktask.m_lock.unlock();
		}
	}

	/**
	 * 获取任务的无延迟正常执行时间
	 * @return 任务的正常执行时间
	 */
	final public Date getRunTimeNoDelay() {
		worktask.m_lock.lock();
		try {
			return worktask.getNextTimeNoDelay();
		} finally {
			worktask.m_lock.unlock();
		}
	}

	/**
	 * 跳到下一日开始执行
	 */
	final public void go2NextDay() {
		worktask.m_lock.lock();
		try {
			// 跳到下一日
			worktask.nextDate();
			logger.info("任务[" + this.getTaskID() + ":" + this.getTaskClass() + "]已跳到下一日执行");
		} catch (Exception e) {
			logger.info("任务[" + this.getTaskID() + ":" + this.getTaskClass() + "]跳到下一日异常:" + e.getMessage(), e);
		} finally {
			worktask.m_lock.unlock();
		}
	}

	/**
	 * 是否任务已是当日最后一次执行
	 * @return 是否已过当日结束时间(true-已过当日结束时间 false-没有过当日结束时间)
	 */
	final public boolean isEndOfDay() {
		worktask.m_lock.lock();
		try {
			if (worktask.getCrontab() == null) {
				return true;
			}

			Date nexttime = worktask.getCrontab().calcNextTime(worktask.getNextTime());
			if (nexttime == null) {
				return true;
			} else {
				return nexttime.after(worktask.getEndTime());
			}
		} catch (Exception e) {
			return true;
		} finally {
			worktask.m_lock.unlock();
		}
	}
}
