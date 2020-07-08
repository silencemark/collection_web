package com.collection.common;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取Spring注入的bean的工具类
 */
@Component
public class SpringContext implements ApplicationContextAware {
	
	private static ApplicationContext context;
	
	private static final Logger logger = Logger.getLogger(SpringContext.class);

	@Override
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		context = appContext;
	}

	/**
	 * 获取注入的bean
	 * @param id
	 */
	public static Object getBean(String id) {
		if (context == null) {
			logger.error("ApplicationContext is null");
			return null;
		}
		return context.getBean(id);
	}
	
	/**
	 * 根据类型获取注入的bean
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		if (context == null) {
			logger.error("ApplicationContext is null");
			return null;
		}
		return context.getBean(clazz);
	}
	
	/**
	 * 获取注入的bean(必须是scope="prototype")
	 * @param id
	 * @param args
	 */
	public static Object getBean(String id, Object... args) {
		if (context == null) {
			logger.error("ApplicationContext is null");
			return null;
		}
		return context.getBean(id, args);
	}
	
	/**
	 * 根据类型获取注入的所有的bean
	 * @param clazz
	 * @return
	 */
	public static <T> Map<String, T> getBeans(Class<T> clazz) {
		if (context == null) {
			logger.error("ApplicationContext is null");
			return null;
		}
		return context.getBeansOfType(clazz);
	}
	
	/**
	 * 获取注入Class的bean名称
	 * @param clazz 
	 */
	public static String[] getBeanNames(Class<?> clazz) {
		if (context == null) {
			logger.error("ApplicationContext is null");
			return null;
		}
		return context.getBeanNamesForType(clazz);
	}
}
