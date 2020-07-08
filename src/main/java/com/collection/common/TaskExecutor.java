package com.collection.common;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 线程池调度器
 */
@Component(SpringBean.TASK_EXECUTOR)
public class TaskExecutor {
	/**
	 * 核心线程数量
	 */
	@Value("${thread.corePoolSize}")
	private int corePoolSize;
	/**
	 * 最大线程数量(尽量不要超过最大连接数)
	 */
	@Value("${thread.maxPoolSize}")
	private int maxPoolSize;
	/**
	 * 终止前多余的空闲线程等待新任务的最长时间
	 */
	@Value("${thread.keepAliveSeconds}")
	private int keepAliveSeconds;
	/**
	 * 任务队列最大数量
	 */
	@Value("${thread.queueCapacity}")
	private int queueCapacity;
	
	/**
	 * 分配任务的线程
	 */
	private Thread dispatcher;
	/**
	 * 当前线程数量
	 */
	private AtomicInteger currentSize;
	/**
	 * 任务队列
	 */
	private LinkedBlockingQueue<Runnable> taskQueue;
	/**
	 * 空闲线程队列
	 */
	private LinkedBlockingQueue<Executor> idleQueue;
	/**
	 * 繁忙线程队列
	 */
	private ConcurrentLinkedQueue<Executor> busyQueue;
	
	/**
	 * 最大限制数量设置为100
	 */
	private static final int maxLimitedNum = 100;
	/**
	 * log日志对象
	 */
	private static final Logger logger = Logger.getLogger(TaskExecutor.class);

	/**
	 * 构造方法
	 */
	public TaskExecutor() {
		//初始化
		currentSize = new AtomicInteger(0);
		//创建任务分配线程
		dispatcher = new Thread(new Runnable() {
			// 执行任务线程
			private Executor executor;
			public void run() {
				// 任务分配
				while (true) {
					executor = null;
					Runnable task = null;
					try {
						// 获取队头任务
						task = taskQueue.take();
						// 获取空闲线程
						while (executor == null) {
							// 空闲现场池为空且未达到最大现场数则创建线程
							if (idleQueue.isEmpty() && currentSize.get() < maxPoolSize) {
								// 新增空闲线程
								executor = new Executor();
								executor.start();
								// 当前线程数量+1
								currentSize.incrementAndGet();
							} else {
								executor = idleQueue.take();
							}
							// 将任务交给线程执行
							if (!executor.submit(task)) {
								executor = null;
							}
						}
					} catch (InterruptedException e) {
						break; // 线程中断则停止分配任务并释放线程池
					} catch (Throwable e) {
						logger.warn("任务分配发生异常:" + e.getMessage(), e);
						if (task != null) {
							taskQueue.remove(task);
							taskQueue.offer(task);
						}
					}
				}

				// 终止繁忙线程池的线程
				while ((executor = busyQueue.poll()) != null) {
					try {
						executor.shutdown();
						executor.join();
						logger.info("线程[" + executor.getId() + "]已终止");
					} catch (Throwable e) {
						logger.warn("线程[" + executor.getId() + "]终止异常:" + e.getMessage());
					}
				}

				// 终止空闲线程池的线程
				while ((executor = idleQueue.poll()) != null) {
					try {
						executor.shutdown();
						executor.join();
						logger.info("线程[" + executor.getId() + "]已终止");
					} catch (Throwable e) {
						logger.warn("线程[" + executor.getId() + "]终止异常:" + e.getMessage());
					}
				}
				logger.info("任务分配线程[" + Thread.currentThread().getId() + "]已终止");
			}
		});
	}
	
	/**
	 * 初始化
	 */
	@PostConstruct
	public void initialize() {
		taskQueue = new LinkedBlockingQueue<Runnable>(queueCapacity);
		idleQueue = new LinkedBlockingQueue<Executor>();
		busyQueue = new ConcurrentLinkedQueue<Executor>();
		maxPoolSize = maxPoolSize > 0 ? (maxPoolSize > maxLimitedNum ? maxLimitedNum : maxPoolSize) : 0;
		dispatcher.start();
	}
	
	/**
	 * 关闭线程池，释放线程资源
	 */
	@PreDestroy
	public void destroy() {
		// 中断分配任务线程
		try {
			dispatcher.interrupt();
			dispatcher.join();
		} catch (InterruptedException e) {
			logger.info("线程[" + dispatcher.getId() + "]已终止");
		}
	}

	/**
	 * 提交Runnable任务
	 * @param task 任务
	 */
	public void submit(Runnable task) {
		try {
			taskQueue.put(task);
		} catch (InterruptedException e) {
			logger.info("执行任务[" + task.getClass().getName() + "]被中断:" + e.getMessage(), e);
		}
	}
	
	/**
	 * 内部工作线程类
	 */
	protected class Executor extends Thread{
		// 线程执行的任务
		private Runnable task = null;
		private Lock lock = new ReentrantLock(true);
		private Condition condition = lock.newCondition();
		// 线程状态(true-正常 false-停止)
		private boolean status = true;
		
		/**
		 * 提交任务给线程
		 * @param task 任务
		 * @return 是否成功交给线程
		 */
		public boolean submit(Runnable task) {
			lock.lock();
			try {
				if (status) {
					busyQueue.offer(this);
					this.task = task;
					condition.signal();
				}
				return status;
			} finally {
				lock.unlock();
			}
		}
		
		/**
		 * 停止线程任务
		 */
		public void shutdown() {
			lock.lock();
			try {
				status = false;
			} finally {
				lock.unlock();
			}
			this.interrupt();
		}

		/**
		 * 执行任务
		 */
		public void run() {
			while (status) {
				lock.lock();
				try {
					// 获取定时任务(keepAliveSeconds内无任务线程自动终止)
					if (task == null && !condition.await(keepAliveSeconds, TimeUnit.SECONDS)) {
						// 无任务
						if (task == null) {
							// 当前线程数量大于核心线程数量,释放多余空闲线程
							if (currentSize.get() > corePoolSize) {
								status = false;
								break;
							} else {
								continue;
							}
						}
					}
				} catch (Throwable e) {
					status = false;
					break;
				} finally {
					lock.unlock();
				}

				// 执行线程任务
				if (task != null) {
					try {
						task.run();
					} catch (Throwable e) {
						logger.warn("任务[" + task.getClass().getName() + "]执行异常:" + e.getMessage(), e);
					} finally {
						task = null;
					}
				}

				// 将线程从繁忙队列移除
				busyQueue.remove(this);
				// 将线程移到空闲队列
				idleQueue.offer(this);
			}
			// 当前线程数量-1
			currentSize.decrementAndGet();
			// 从队列中移除此线程
			busyQueue.remove(this);
			idleQueue.remove(this);
		}
	}
}	
