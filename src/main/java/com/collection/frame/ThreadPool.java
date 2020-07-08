package com.collection.frame;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;

/*
 * 线程池没有设置最小线程数,因为线程如果在m_keepAliveTime秒内没有接收到任务,线程就自动终止
 * 线程池调用shutdown后线程池资源已释放，如果再调用此实例的方法则会重新创建线一个程池
 */
public class ThreadPool {
	//分配任务的线程
	private Thread m_thread = null;
	//当前线程数量
	private AtomicInteger m_curPoolSize;
	//最大线程数量(尽量不要超过最大连接数)
	private int m_maxPoolSize = 40;
	//终止前多余的空闲线程等待新任务的最长时间(秒)
	private int m_keepAliveTime = 600; //10分钟
	//单例模式的线程池
	private static volatile ThreadPool m_threadPool = null;
	//任务队列
	private LinkedBlockingQueue<Runnable>     m_taskQueue = new LinkedBlockingQueue<Runnable>(2000);
	//空闲线程队列
	private LinkedBlockingQueue<WorkThread>   m_idleQueue = new LinkedBlockingQueue<WorkThread>();
	//繁忙线程队列
	private ConcurrentLinkedQueue<WorkThread> m_busyQueue = new ConcurrentLinkedQueue<WorkThread>();
	//log日志对象
	private static Logger m_logger = Logger.getLogger(ThreadPool.class);

	/**
	 * 只允许一个实例
	 */
	private ThreadPool()
	{
		m_curPoolSize = new AtomicInteger(0);
		//启动任务分配线程
		m_thread = new Thread(new Runnable(){
			//执行任务线程
			private WorkThread m_taskThread = null;
			
			public void run()
			{
				//任务分配
				while(true)
				{
					m_taskThread  = null;
					Runnable task = null;
					
					try
					{
						//获取队头任务
						task = m_taskQueue.take();
						//获取空闲线程
						while (m_taskThread == null)
						{
							//空闲现场池为空且未达到最大现场数则创建线程
							if(m_idleQueue.isEmpty() && m_curPoolSize.get() < m_maxPoolSize)
							{
								//新增空闲线程
								m_taskThread = new WorkThread();
								m_taskThread.start();
								m_curPoolSize.incrementAndGet();
							}
							else
							{
								m_taskThread = m_idleQueue.take();
							}
							//将任务交给线程执行
							if (!m_taskThread.execute(task)) {
								m_taskThread = null;
							}
						}
					}
					catch (InterruptedException e)
					{
						break; //线程中断则停止分配任务并释放线程池
					}
					catch (Throwable e)
					{
						m_logger.warn("任务分配发生异常:" + e.getMessage());
						if (task != null) {
							m_taskQueue.remove(task);
							m_taskQueue.offer(task);
						}
					}
				}
				
				//终止繁忙线程池的线程
				while((m_taskThread = m_busyQueue.poll()) != null)
				{
					try
					{
						m_taskThread.shutdown();
						m_taskThread.join();
						m_logger.info("线程" + m_taskThread.getId() + "已终止");
					} catch (Throwable e) {
						m_logger.warn("线程" + m_taskThread.getId() + "终止异常:" + e.getMessage());
					}
				}
					
				//终止空闲线程池的线程
				while((m_taskThread = m_idleQueue.poll()) != null)
				{
					try
					{
						m_taskThread.shutdown();
						m_taskThread.join();
						m_logger.info("线程" + m_taskThread.getId() + "已终止");
					} catch (Throwable e) {
						m_logger.warn("线程" + m_taskThread.getId() + "终止异常:" + e.getMessage());
					}
				}
				
				m_logger.info("任务分配线程" + Thread.currentThread().getId() + "已终止");
			}
		});
		m_thread.start();
	}

	/**
	 * 单例模式
	 * @return 线程池对象
	 */
	public static ThreadPool getInstance()
	{
		if(m_threadPool == null)
		{
			synchronized(ThreadPool.class)
			{
				if(m_threadPool == null)
				{
					m_threadPool = new ThreadPool();
				}
			}
		}
		
		return m_threadPool;
	}

	/**
	 * 执行Runnable任务
	 * @param task 任务
	 */
	public void execute(Runnable task)
	{
		try
		{
			m_taskQueue.put(task);
		}
		catch (InterruptedException e)
		{
			m_logger.info("执行任务<" + task.getClass().getName() + ">被中断:" + e.getMessage());
		}
	}
	
	/**
	 * 关闭线程池，释放线程资源
	 */
	public synchronized void shutdown()
	{
		//中断分配任务线程
		try
		{
			m_thread.interrupt();
			m_thread.join();
		}
		catch (InterruptedException e)
		{
			m_logger.info("线程" + m_thread.getId() + "已终止");
		}
		
		//单例线程池置空
		m_threadPool = null;
	}
	
	/**
	 * 内部工作线程类
	 */
	protected class WorkThread extends Thread{
		//线程执行的任务
		private Runnable  m_task = null;
		private Lock      m_lock = new ReentrantLock(true);
		private Condition m_cont = m_lock.newCondition();
		//线程状态(true-正常 false-停止)
		private boolean m_status = true;
		
		/**
		 * 提交任务给线程
		 * @param task 任务
		 * @return 是否成功交给线程
		 */
		public boolean execute(Runnable task)
		{
			m_lock.lock();
			try
			{
				if (m_status)
				{
					m_busyQueue.offer(this);
					m_task = task;
					m_cont.signal();
					return true;
				}
				return false;
			} finally {
				m_lock.unlock();
			}
		}
		
		/**
		 * 停止线程任务
		 */
		public void shutdown()
		{
			m_lock.lock();
			try
			{
				m_status = false;
			} finally {
				m_lock.unlock();
			}
			this.interrupt();
		}
		
		/**
		 * 执行任务
		 */
		public void run()
		{
			while(m_status)
			{
				m_lock.lock();
				try
				{
					//获取定时任务(m_keepAliveTime内无任务线程自动终止)
					if (m_task == null && !m_cont.await(m_keepAliveTime, TimeUnit.SECONDS)) {
						if (m_task == null) {
							m_status = false;
							break;
						}
					}
				} catch (Throwable e) {
					m_status = false;
					break;
				} finally {
					m_lock.unlock();
				}
				
				//执行线程任务
				if (m_task != null) 
				{
					try
					{
						m_task.run();
					} catch (Throwable e) {
						m_logger.warn("任务<" + m_task.getClass().getName() + ">执行异常:" + e.getMessage(), e);
					} finally {
						m_task = null;
					}
				}
				
				//将线程从繁忙队列移除
				m_busyQueue.remove(this);
				//将线程移到空闲队列
				m_idleQueue.offer(this);
			}
			
			m_curPoolSize.decrementAndGet();
			//从队列中移除此线程
			m_busyQueue.remove(this);
			m_idleQueue.remove(this);
		}
	}
}	
