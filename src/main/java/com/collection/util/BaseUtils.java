package com.collection.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

import com.collection.common.SpringContext;

/**
 * 基础工具类
 * @author silence 
 */
public class BaseUtils {
	/**
	 * 1D标准天数
	 */
	public static final int _1D = 1;
	/**
	 * 1W标准天数
	 */
	public static final int _1W = 7;
	/**
	 * 1M标准天数
	 */
	public static final int _1M = 30;
	/**
	 * 1Y标准天数
	 */
	public static final int _1Y = 360;

	//日志输出
	private static final Logger logger = Logger.getLogger(BaseUtils.class);
	
	/**
	 * 禁止对象构造
	 */
	private BaseUtils() {}
	
	/**
	 * 根据Class获取其默认的bean名称
	 * @param clazz
	 * @return
	 */
	public static String getClassBean(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		
		String[] beanNames = SpringContext.getBeanNames(clazz);
		if (beanNames == null) {
			throw new RuntimeException("Class[" + clazz.getName() + "]不存在被Spring管理的bean!");
		}
		if (beanNames.length != 1) {
			throw new RuntimeException("Class[" + clazz.getName() + "]存在0个或多个被Spring管理的bean!");
		}
		return beanNames[0];
	}
	
	/**
	 * 根据Class获取其默认的bean名称
	 * @param clazz
	 * @return
	 */
	public static String getClassBean(String clazz) {
		if (clazz == null) {
			return null;
		}
		
		try {
			return getClassBean(Class.forName(clazz));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Class[" + clazz + "]不存在: " + e.getMessage());
		}
	}
	
	/**
	 * 获取ClassLoader
	 * @return
	 */
	public static ClassLoader getClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable e) {
		}
		if (cl == null) {
			cl = BaseUtils.class.getClassLoader();
			if (cl == null) {
				try {
					cl = ClassLoader.getSystemClassLoader();
				} catch (Throwable e) {
				}
			}
		}
		return cl;
	}
	
	/**
	 * 扫描指定包路径下的class
	 * @param locationPattern 指定包(可以有多个,逗号分隔)
	 * @return
	 * @throws IOException
	 */
	public static List<Class<?>> doScaner(String locationPattern) throws IOException {
		return doScaner(locationPattern, null);
	}
	
	/**
	 * 扫描指定包路径下的添加指定注解的类
	 * @param locationPattern 指定包(可以有多个,逗号分隔)
	 * @param annotation 注解
	 * @return
	 * @throws IOException
	 */
	public static List<Class<?>> doScaner(String locationPattern, Class<? extends Annotation> annotation) throws IOException {
		if (locationPattern == null) {
			return null;
		}
		
		String[] locations = locationPattern.split(",");
		List<Class<?>> list = new ArrayList<Class<?>>();
		ClassLoader classloader = getClassLoader();
		for (int i = 0; i < locations.length; i++) {
			String pkgName = locations[i].trim();
			String location = pkgName.replace('.', '/');
			Enumeration<URL> urls = classloader != null ? classloader.getResources(location) : ClassLoader.getSystemResources(location);
			if (urls == null) {
				throw new RuntimeException("package<" + pkgName + "> scanned failed");
			}
			while (urls.hasMoreElements()) {
				URL url = (URL) urls.nextElement();
				if (url == null) {
					continue;
				}
				
				if ("file".equals(url.getProtocol())) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					List<Class<?>> result = findClassName(pkgName, filePath, annotation);
					if (result != null) {
						list.addAll(result);
					}
				} else if ("jar".equals(url.getProtocol())) {
					JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
					JarFile jarFile = jarURLConnection.getJarFile();
					List<Class<?>> result = findClassName(pkgName, jarFile, annotation);
					if (result != null) {
						list.addAll(result);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 查找指定包所在路径下指定包下的class
	 * @param pkgName  包名
	 * @param filePath 包所在路径
	 * @param annotation 注解
	 * @return
	 */
	public static List<Class<?>> findClassName(String pkgName, String filePath, Class<? extends Annotation> annotation) {
		if (pkgName == null || filePath == null) {
			return null;
		}
		
		File dir = new File(filePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return null;
		}
		
		//只处理目录和class
		File[] dirFiles = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory() || (file.isFile() && file.getName().endsWith(".class"));
			}
		});
		
		List<Class<?>> list = new ArrayList<Class<?>>();
		for (File file : dirFiles) {
			if (file.isDirectory()) {
				List<Class<?>> result = findClassName(pkgName + "." + file.getName(), file.getAbsolutePath(), annotation);
				if (result != null) {
					list.addAll(result);
				}
			} else {
				try {
					Class<?> clazz = Class.forName(pkgName + "." + file.getName().substring(0, file.getName().length() - 6));
					if (annotation == null || clazz.isAnnotationPresent(annotation)) {
						list.add(clazz);
					}
				} catch (Throwable e) {
				}
			}
		}
		return list;
	}
	
	/**
	 * 查找指定jar包下指定包下的class
	 * @param pkgName 包名
	 * @param jarFile jar文件对象
	 * @param annotation 注解
	 * @return
	 */
	public static List<Class<?>> findClassName(String pkgName, JarFile jarFile, Class<? extends Annotation> annotation) {
		String prefix = pkgName + ".";
		List<Class<?>> list = new ArrayList<Class<?>>();
		Enumeration<JarEntry> jarEntries = jarFile.entries();
		while (jarEntries.hasMoreElements()) {
			JarEntry jarEntry = jarEntries.nextElement();
			String jarEntryName = jarEntry.getName();
			if (!jarEntryName.endsWith(".class")) {
				continue;
			}
			String clazzName = jarEntryName.replace('/', '.');
			if (clazzName.startsWith(prefix)) {
				try {
					Class<?> clazz = Class.forName(clazzName.substring(0, clazzName.length() - 6));
					if (annotation == null || clazz.isAnnotationPresent(annotation)) {
						list.add(clazz);
					}
				} catch (Throwable e) {
				}
			}
		}
		return list;
	}
	
	/**
	 * 执行Shell脚本
	 * @param command
	 */
	public static void execShell(String command){
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			InputStream isOut = process.getInputStream();
			// 取走子进程输出流信息
			try {
				while (isOut.read() != -1) {
	            	//do nothing
	            }
			} finally {
				if (isOut != null) {
					try {
						isOut.close();
					} catch (IOException e) {
					}
				}
			}
			InputStream isErr = process.getErrorStream();
            // 取走子进程错误流信息
			try {
	            while (isErr.read() != -1) {
	            	//do nothing
	            }
			} finally {
				if (isErr != null) {
					try {
						isErr.close();
					} catch (IOException e) {
					}
				}
			}
            process.waitFor();  
		} catch (Exception e) {
			logger.info("发送消息执行命令行出现异常:" + e.getMessage(), e);
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
	}
}
