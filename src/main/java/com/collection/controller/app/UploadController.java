package com.collection.controller.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.collection.util.ImageUtil;
import com.collection.util.UserUtil;

@Controller
@RequestMapping("/upload")
public class UploadController {

	
	private static final Logger LOGGER = Logger.getLogger(UploadController.class);
	/**
	 * 用户唯一ID
	 * @return
	 */
	public String getUserId(HttpServletRequest request){
		Map<String, Object> data=UserUtil.getUser(request);
		return data.get("userid")==null?"admin":data.get("userid") + ""; 
	}
	public Map<String,Object> getUser(HttpServletRequest request){
		return UserUtil.getUser(request);
	}

	/**
	 * 管理方--上传图片
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/manageheadimg",method=RequestMethod.POST)    
	@ResponseBody 
	public Map<String, Object> manageheadimg(HttpServletRequest request,
			HttpServletResponse response)  throws Exception{
		String filename = null;
		if(request.getParameter("filename") != null && !request.getParameter("filename").equals("")){
			filename = String.valueOf(request.getParameter("filename"));
		}else{
			filename = "myfiles";  
		}
		  
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; // 获得文件：
		List<MultipartFile> myfiles = multipartRequest.getFiles(filename);
		LOGGER.debug(myfiles.get(0).getContentType()); 
		
		Map<String, Object> map = new HashMap<String, Object>();
		for (MultipartFile myfile : myfiles) {
			if (myfile.isEmpty()) {
				map.put("error", "请选择文件后上传");
				return map;
			}else{
				CommonsMultipartFile cf= (CommonsMultipartFile)myfile; 
				DiskFileItem fi = (DiskFileItem)cf.getFileItem();
				File file= fi.getStoreLocation();
				String newImg = System.currentTimeMillis()/1000l+"_manageStage";
				
			    if(!file.exists()){
			    	file.createNewFile();
			    }
			    String path = request.getSession().getServletContext().getRealPath("upload/images")+"/"+newImg+".jpg"; 
			    Thumbnails.of(file).size(480,270).toFile(path);
			    //renameFile(file.toString(), path);
			    map.put("imgurl", path);
			    map.put("imgkey", "/upload/images/"+newImg+".jpg");
			}
		}
		return map;
	}

	/**
	 * 管理方--上传banner图片
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/managebannerimg",method=RequestMethod.POST)    
	@ResponseBody 
	public Map<String, Object> managebannerimg(HttpServletRequest request,
			HttpServletResponse response)  throws Exception{
		String filename = null;
		if(request.getParameter("filename") != null && !request.getParameter("filename").equals("")){
			filename = String.valueOf(request.getParameter("filename"));
		}else{
			filename = "myfiles";  
		}
		  
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; // 获得文件：
		List<MultipartFile> myfiles = multipartRequest.getFiles(filename);
		LOGGER.debug(myfiles.get(0).getContentType()); 
		
		Map<String, Object> map = new HashMap<String, Object>();
		for (MultipartFile myfile : myfiles) {
			if (myfile.isEmpty()) {
				map.put("error", "请选择文件后上传");
				return map;
			}else{
				CommonsMultipartFile cf= (CommonsMultipartFile)myfile; 
				DiskFileItem fi = (DiskFileItem)cf.getFileItem();
				File file= fi.getStoreLocation();
				String newImg = System.currentTimeMillis()/1000l+"_managebanner";
				
			    if(!file.exists()){
			    	file.createNewFile();
			    }
			    String path = request.getSession().getServletContext().getRealPath("upload/banner")+"/"+newImg+".jpg"; 
			    Thumbnails.of(file).size(1080,470).toFile(path);
			    map.put("imgurl", path);
			    map.put("imgkey", "/upload/banner/"+newImg+".jpg");
			}
		}
		return map;
	}
	
	/**
	 * 管理方--上传正方形图标
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/manageiconimg",method=RequestMethod.POST)    
	@ResponseBody 
	public Map<String, Object> manageiconimg(HttpServletRequest request,
			HttpServletResponse response)  throws Exception{
		String filename = null;
		if(request.getParameter("filename") != null && !request.getParameter("filename").equals("")){
			filename = String.valueOf(request.getParameter("filename"));
		}else{
			filename = "myfiles";  
		}
		  
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; // 获得文件：
		List<MultipartFile> myfiles = multipartRequest.getFiles(filename);
		LOGGER.debug(myfiles.get(0).getContentType()); 
		
		Map<String, Object> map = new HashMap<String, Object>();
		for (MultipartFile myfile : myfiles) {
			if (myfile.isEmpty()) {
				map.put("error", "请选择文件后上传");
				return map;
			}else{
				CommonsMultipartFile cf= (CommonsMultipartFile)myfile; 
				DiskFileItem fi = (DiskFileItem)cf.getFileItem();
				File file= fi.getStoreLocation();
				String newImg = System.currentTimeMillis()/1000l+"_manageicon";
				
			    if(!file.exists()){
			    	file.createNewFile();
			    }
			    String path = request.getSession().getServletContext().getRealPath("upload/banner")+"/"+newImg+".jpg"; 
			    Thumbnails.of(file).size(500,500).toFile(path);
			    map.put("imgurl", path);
			    map.put("imgkey", "/upload/banner/"+newImg+".jpg");
			}
		}
		return map;
	}
	
	/**
	 * 上传图片 客户端
	 * @param request
	 * @param response
	 * @return
	 */
	//@RequestMapping(value = "/headimg",method=RequestMethod.POST)    
	//@ResponseBody 
	public Map<String, Object> headimg(HttpServletRequest request,
			HttpServletResponse response)  throws Exception{
		String filename = null;
		if(request.getParameter("filename") != null && !request.getParameter("filename").equals("")){
			filename = String.valueOf(request.getParameter("filename"));
		}else{
			filename = "myfiles";  
		}
		  
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; // 获得文件：
		List<MultipartFile> myfiles = multipartRequest.getFiles(filename);
		LOGGER.debug(myfiles.get(0).getContentType()); 
		
		Map<String, Object> map = new HashMap<String, Object>();
		for (MultipartFile myfile : myfiles) {
			if (myfile.isEmpty()) {
				map.put("error", "请选择文件后上传");
				return map;
			}else{
				CommonsMultipartFile cf= (CommonsMultipartFile)myfile; 
				DiskFileItem fi = (DiskFileItem)cf.getFileItem();
				File file= fi.getStoreLocation();
				String newImg = new Date().getTime()+"_"+getUserId(request);
				
			    if(!file.exists()){
			    	file.createNewFile();
			    }
			    String companyid = String.valueOf(getUser(request).get("companyid"));
			    String size = String.valueOf(fi.getSize());
			    String fileurl = "/customerfile/"+companyid+"/"+newImg+".jpg";
			    String path = request.getSession().getServletContext().getRealPath("/customerfile/"+companyid)+"/"+newImg+".jpg";
			    String realpath = request.getSession().getServletContext().getRealPath("/customerfile/"+companyid);
			    File fil = new File(realpath); 
			    if(!fil.exists()){
			    	fil.mkdirs();
			    }
			    ImageUtil.reduceImg(file.toString(), path ,fi.getSize());
			    
			    File fis = new File(path);
			    
			    try {
			    	Map<String,Object> param = new HashMap<String,Object>();
			    	param.put("companyid", companyid);
			    	param.put("userid", this.getUser(request).get("userid"));
			    	param.put("size", fis.length());
			    	param.put("type", request.getParameter("type"));
			    	param.put("url", fileurl);
					//this.customerFileService.insertCustomerFileInfo(param);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			    
			    map.put("imgurl", path);
			    map.put("imgkey", fileurl);
			}
		}
		return map;
	}
	
	/**
	 * 使用方上传文件方法
	 * @param request
	 * @param response
	 * @return
	 */
	//@RequestMapping("/documentUpload")
	//@ResponseBody
	public Map<String, Object> documentUpload(HttpServletRequest request,
			HttpServletResponse response) {

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; // 获得文件：
		List<MultipartFile> myfiles = multipartRequest.getFiles("myfiles");
		
		String companyid = String.valueOf(getUser(request).get("companyid"));
		String size = "";
		// 上传文件的原名(即上传前的文件名字)
		String originalFilename = null;
		// 可以在上传文件的同时接收其它参数
		// 如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\upload\\文件夹中
		// 这里实现文件上传操作用的是commons.io.FileUtils类,它会自动判断/upload是否存在,不存在会自动创建
		String realPath = request.getSession().getServletContext()
				.getRealPath("/customerfile/"+companyid)+"/";
		// 设置响应给前台内容的数据格式
		response.setContentType("text/plain; charset=UTF-8");
		// 设置响应给前台内容的PrintWriter对象
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 如果只是上传一个文件,则只需要MultipartFile类型接收文件即可,而且无需显式指定@RequestParam注解
		// 如果想上传多个文件,那么这里就要用MultipartFile[]类型来接收文件,并且要指定@RequestParam注解
		// 上传多个文件时,前台表单中的所有<input
		// type="file"/>的name都应该是myfiles,否则参数里的myfiles无法获取到所有上传的文件
		
		List<Map<String, String>> datalist = new ArrayList<Map<String,String>>();
		String url = "";	
		for (MultipartFile myfile : myfiles) {
			if (myfile.isEmpty()) {
				map.put("error", "请选择文件后上传");
				return map;
			} else {
				originalFilename = myfile.getOriginalFilename();
				map.put("name", originalFilename);
				System.out.println("文件原名: " + originalFilename);
				originalFilename = new Date().getTime()
						+ ""
						+ originalFilename.substring(originalFilename
								.lastIndexOf("."));
				System.out.println("文件名称: " + myfile.getName());
				System.out.println("文件长度: " + myfile.getSize());
				System.out.println("文件类型: " + myfile.getContentType());
				System.out.println("========================================");
				try {
					// 这里不必处理IO流关闭的问题,因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
					// 此处也可以使用Spring提供的MultipartFile.transferTo(File
					// dest)方法实现文件的上传
					FileUtils.copyInputStreamToFile(myfile.getInputStream(),
							new File(realPath, originalFilename));
					
				
				//if(map.get("ify")!=null){//是否需要压缩	
					url="/customerfile/" + companyid +"/"+ originalFilename;
					size = String.valueOf(myfile.getSize());
					map.put("size", size);
					
				/*}else{ 
					//图片压缩处理	
					ImgCompress imgCom = new ImgCompress(realPath,originalFilename);  
					imgCom.resizeFix(400, 400); 
					url="/upload/y" + originalFilename;   
				}	*/
				        
				        
				        
				} catch (IOException e) {
					System.out.println("文件[" + originalFilename
							+ "]上传失败,堆栈轨迹如下");
					e.printStackTrace();
					map.put("error", "1`文件上传失败，请重试！！");
					return map;
				}
			}
		}
		// 此时在Windows下输出的是[D:\Develop\apache-tomcat-6.0.36\webapps\AjaxFileUpload\\upload\愤怒的小鸟.jpg]
		// System.out.println(realPath + "\\" + originalFilename);
		// 此时在Windows下输出的是[/AjaxFileUpload/upload/愤怒的小鸟.jpg]
		// System.out.println(Constants.PROJECT_PATH+"" + "/upload/" +
		// originalFilename);
		// 不推荐返回[realPath + "\\" + originalFilename]的值
		// 因为在Windows下<img src="file:///D:/aa.jpg">能被firefox显示,而<img
		// src="D:/aa.jpg">firefox是不认的
		 try {
		    	Map<String,Object> param = new HashMap<String,Object>();
		    	param.put("companyid", companyid);
		    	param.put("userid", this.getUser(request).get("userid"));
		    	param.put("size", size);
		    	param.put("type", request.getParameter("type"));
		    	param.put("url", originalFilename);
				//this.customerFileService.insertCustomerFileInfo(param);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		
		map.put("url", url);
		return map;
	} 
	
	
	/**
	 * 上传文件方法
	 * @param request
	 * @param response
	 * @return
	 */
	//@RequestMapping("/manageUpload")
	//@ResponseBody
	public Map<String, Object> manageUpload(HttpServletRequest request,
			HttpServletResponse response) {

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; // 获得文件：
		List<MultipartFile> myfiles = multipartRequest.getFiles("myfiles");

		// 可以在上传文件的同时接收其它参数
		// 如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\upload\\文件夹中
		// 这里实现文件上传操作用的是commons.io.FileUtils类,它会自动判断/upload是否存在,不存在会自动创建
		String realPath = request.getSession().getServletContext()
				.getRealPath("/upload/manage/");
		// 设置响应给前台内容的数据格式
		response.setContentType("text/plain; charset=UTF-8");
		// 设置响应给前台内容的PrintWriter对象
		Map<String, Object> map = new HashMap<String, Object>();
		// 上传文件的原名(即上传前的文件名字)
		String originalFilename = null;
		// 如果只是上传一个文件,则只需要MultipartFile类型接收文件即可,而且无需显式指定@RequestParam注解
		// 如果想上传多个文件,那么这里就要用MultipartFile[]类型来接收文件,并且要指定@RequestParam注解
		// 上传多个文件时,前台表单中的所有<input
		// type="file"/>的name都应该是myfiles,否则参数里的myfiles无法获取到所有上传的文件
		
		List<Map<String, String>> datalist = new ArrayList<Map<String,String>>();
		String url = "";	
		for (MultipartFile myfile : myfiles) {
			if (myfile.isEmpty()) {
				map.put("error", "请选择文件后上传");
				return map;
			} else {
				originalFilename = myfile.getOriginalFilename();
				map.put("name", originalFilename);
				System.out.println("文件原名: " + originalFilename);
				originalFilename = new Date().getTime()
						+ ""
						+ originalFilename.substring(originalFilename
								.lastIndexOf("."));
				System.out.println("文件名称: " + myfile.getName());
				System.out.println("文件长度: " + myfile.getSize());
				System.out.println("文件类型: " + myfile.getContentType());
				System.out.println("========================================");
				try {
					// 这里不必处理IO流关闭的问题,因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
					// 此处也可以使用Spring提供的MultipartFile.transferTo(File
					// dest)方法实现文件的上传
					FileUtils.copyInputStreamToFile(myfile.getInputStream(),
							new File(realPath, originalFilename));
					
				
				//if(map.get("ify")!=null){//是否需要压缩	
					url="/upload/manage/" + originalFilename;
					map.put("size", myfile.getSize());
					
				/*}else{ 
					//图片压缩处理	
					ImgCompress imgCom = new ImgCompress(realPath,originalFilename);  
					imgCom.resizeFix(400, 400); 
					url="/upload/y" + originalFilename;   
				}	*/
				        
				        
				        
				} catch (IOException e) {
					System.out.println("文件[" + originalFilename
							+ "]上传失败,堆栈轨迹如下");
					e.printStackTrace();
					map.put("error", "1`文件上传失败，请重试！！");
					return map;
				}
			}
		}
		// 此时在Windows下输出的是[D:\Develop\apache-tomcat-6.0.36\webapps\AjaxFileUpload\\upload\愤怒的小鸟.jpg]
		// System.out.println(realPath + "\\" + originalFilename);
		// 此时在Windows下输出的是[/AjaxFileUpload/upload/愤怒的小鸟.jpg]
		// System.out.println(Constants.PROJECT_PATH+"" + "/upload/" +
		// originalFilename);
		// 不推荐返回[realPath + "\\" + originalFilename]的值
		// 因为在Windows下<img src="file:///D:/aa.jpg">能被firefox显示,而<img
		// src="D:/aa.jpg">firefox是不认的
		map.put("url", url);
		return map;
	}
	
	/**
	 * 上传图片
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/imageUpload")
	@ResponseBody
	public Map<String, Object> uploadImage(HttpServletRequest request,
			HttpServletResponse response) {

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; // 获得文件：
		List<MultipartFile> myfiles = multipartRequest.getFiles("myfiles");

		String companyid = String.valueOf(getUser(request).get("companyid"));
		String size = null;
		// 上传文件的原名(即上传前的文件名字)
		String originalFilename = null;
		
		// 可以在上传文件的同时接收其它参数
		// 如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\upload\\文件夹中
		// 这里实现文件上传操作用的是commons.io.FileUtils类,它会自动判断/upload是否存在,不存在会自动创建
		String realPath = request.getSession().getServletContext()
				.getRealPath("/customerfile/"+companyid)+"/";
		// 设置响应给前台内容的数据格式
		response.setContentType("text/plain; charset=UTF-8");
		// 设置响应给前台内容的PrintWriter对象
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 如果只是上传一个文件,则只需要MultipartFile类型接收文件即可,而且无需显式指定@RequestParam注解
		// 如果想上传多个文件,那么这里就要用MultipartFile[]类型来接收文件,并且要指定@RequestParam注解
		// 上传多个文件时,前台表单中的所有<input
		// type="file"/>的name都应该是myfiles,否则参数里的myfiles无法获取到所有上传的文件
		
		List<Map<String, String>> datalist = new ArrayList<Map<String,String>>();
		String url = "";
		File fis = null;
		for (MultipartFile myfile : myfiles) {
			if (myfile.isEmpty()) {
				map.put("error", "请选择文件后上传");
				return map;
			} else {
				originalFilename = myfile.getOriginalFilename();
				map.put("name", originalFilename);
				System.out.println("文件原名: " + originalFilename);
				originalFilename = new Date().getTime()
						+ ""
						+ originalFilename.substring(originalFilename
								.lastIndexOf("."));
				System.out.println("文件名称: " + myfile.getName());
				System.out.println("文件长度: " + myfile.getSize());
				System.out.println("文件类型: " + myfile.getContentType());
				System.out.println("========================================");
				try {
					// 这里不必处理IO流关闭的问题,因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
					// 此处也可以使用Spring提供的MultipartFile.transferTo(File
					// dest)方法实现文件的上传
					FileUtils.copyInputStreamToFile(myfile.getInputStream(),
							new File(realPath, originalFilename));
					
					ImageUtil.reduceImg(realPath+originalFilename , realPath+originalFilename , myfile.getSize());
					fis = new File(realPath+originalFilename);
				//if(map.get("ify")!=null){//是否需要压缩	
					url="/customerfile/"+companyid+"/" + originalFilename;
					size = String.valueOf(myfile.getSize());
					map.put("size", size);
					
				/*}else{ 
					//图片压缩处理	
					ImgCompress imgCom = new ImgCompress(realPath,originalFilename);  
					imgCom.resizeFix(400, 400); 
					url="/upload/y" + originalFilename;   
				}	*/
				        
				        
				        
				} catch (IOException e) {
					System.out.println("文件[" + originalFilename
							+ "]上传失败,堆栈轨迹如下");
					e.printStackTrace();
					map.put("error", "1`文件上传失败，请重试！！");
					return map;
				}
			}
		}
		// 此时在Windows下输出的是[D:\Develop\apache-tomcat-6.0.36\webapps\AjaxFileUpload\\upload\愤怒的小鸟.jpg]
		// System.out.println(realPath + "\\" + originalFilename);
		// 此时在Windows下输出的是[/AjaxFileUpload/upload/愤怒的小鸟.jpg]
		// System.out.println(Constants.PROJECT_PATH+"" + "/upload/" +
		// originalFilename);
		// 不推荐返回[realPath + "\\" + originalFilename]的值
		// 因为在Windows下<img src="file:///D:/aa.jpg">能被firefox显示,而<img
		// src="D:/aa.jpg">firefox是不认的
		
		 try {
		    	Map<String,Object> param = new HashMap<String,Object>();
		    	param.put("companyid", companyid);
		    	param.put("userid", this.getUser(request).get("userid"));
		    	param.put("size", fis.length());
		    	param.put("type", request.getParameter("type"));
		    	param.put("url", originalFilename);
				//this.customerFileService.insertCustomerFileInfo(param);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		
		map.put("url", url);
		return map;
	}
	
	
	/**
	 * 享GO影视上传文件方法
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/manageUploadApk")
	@ResponseBody
	public Map<String, Object> manageUploadApk(HttpServletRequest request,
			HttpServletResponse response) {

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; // 获得文件：
		List<MultipartFile> myfiles = multipartRequest.getFiles("myfiles");

		// 可以在上传文件的同时接收其它参数
		// 如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\upload\\文件夹中
		// 这里实现文件上传操作用的是commons.io.FileUtils类,它会自动判断/upload是否存在,不存在会自动创建
		String realPath = request.getSession().getServletContext()
				.getRealPath("/upload/apk/");
		// 设置响应给前台内容的数据格式
		response.setContentType("text/plain; charset=UTF-8");
		// 设置响应给前台内容的PrintWriter对象
		Map<String, Object> map = new HashMap<String, Object>();
		// 上传文件的原名(即上传前的文件名字)
		String originalFilename = null;
		// 如果只是上传一个文件,则只需要MultipartFile类型接收文件即可,而且无需显式指定@RequestParam注解
		// 如果想上传多个文件,那么这里就要用MultipartFile[]类型来接收文件,并且要指定@RequestParam注解
		// 上传多个文件时,前台表单中的所有<input
		// type="file"/>的name都应该是myfiles,否则参数里的myfiles无法获取到所有上传的文件
		
		List<Map<String, String>> datalist = new ArrayList<Map<String,String>>();
		String url = "";	
		for (MultipartFile myfile : myfiles) {
			if (myfile.isEmpty()) {
				map.put("error", "请选择文件后上传");
				return map;
			} else {
				originalFilename = myfile.getOriginalFilename();
				map.put("name", originalFilename);
				System.out.println("文件原名: " + originalFilename);
				
				System.out.println("文件名称: " + myfile.getName());
				System.out.println("文件长度: " + myfile.getSize());
				System.out.println("文件类型: " + myfile.getContentType());
				System.out.println("========================================");
				try {
					//上传apk  判断apk是否已经存在，如果存在就先删除
					File file = new File(realPath);
					if(file.exists()){
						file.delete();
					}
					// 这里不必处理IO流关闭的问题,因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
					// 此处也可以使用Spring提供的MultipartFile.transferTo(File
					// dest)方法实现文件的上传
					FileUtils.copyInputStreamToFile(myfile.getInputStream(),
							new File(realPath, originalFilename));
					
				
				//if(map.get("ify")!=null){//是否需要压缩	
					url="upload/apk/" + originalFilename;
					map.put("size", myfile.getSize());
					
				/*}else{ 
					//图片压缩处理	
					ImgCompress imgCom = new ImgCompress(realPath,originalFilename);  
					imgCom.resizeFix(400, 400); 
					url="/upload/y" + originalFilename;   
				}	*/
				        
				        
				        
				} catch (IOException e) {
					System.out.println("文件[" + originalFilename
							+ "]上传失败,堆栈轨迹如下");
					e.printStackTrace();
					map.put("error", "1`文件上传失败，请重试！！");
					return map;
				}
			}
		}
		// 此时在Windows下输出的是[D:\Develop\apache-tomcat-6.0.36\webapps\AjaxFileUpload\\upload\愤怒的小鸟.jpg]
		// System.out.println(realPath + "\\" + originalFilename);
		// 此时在Windows下输出的是[/AjaxFileUpload/upload/愤怒的小鸟.jpg]
		// System.out.println(Constants.PROJECT_PATH+"" + "/upload/" +
		// originalFilename);
		// 不推荐返回[realPath + "\\" + originalFilename]的值
		// 因为在Windows下<img src="file:///D:/aa.jpg">能被firefox显示,而<img
		// src="D:/aa.jpg">firefox是不认的
		map.put("url", url);
		return map;
	}
	
	/**
	 * 文件的下载
	 * @param map
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/download")  
	public ResponseEntity<byte[]> download(@RequestParam Map<String,Object> map,HttpServletRequest request)
            throws IOException {
		String rspName=request.getSession().getServletContext().getRealPath("");
		//String rspName = request.getContextPath(); 
		HttpHeaders headers = new HttpHeaders();   
		rspName = rspName+map.get("fileName");   
		//rspName = rspName.replaceAll("/", "\\");
		System.out.println(rspName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  
		headers.setContentDispositionFormData("attachment", new String((map.get("fileName")+"").getBytes("gb2312"),  
		                "iso-8859-1"));  
		File file = new File(rspName);
		byte[] bytes = null;
		if(file.exists()){
			bytes = FileUtils.readFileToByteArray(file);  
		}
		return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
	}
	
	/**
	 * 删除文件信息
	 * @param map
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value="/deleteDocumentFile")
	public void deleteDocumentFile(@RequestParam Map<String,Object> map,HttpServletRequest request){
		try {
			if(map.containsKey("fileurl") && !"".equals(map.get("fileurl"))){
				String realpath = request.getSession().getServletContext().getRealPath("");
				realpath = realpath + map.get("fileurl");
				File file = new File(realpath);
				if(file.exists()){
					file.delete();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	 public void renameFile(String file, String toFile) {  
		  
	        File toBeRenamed = new File(file);  
	        //检查要重命名的文件是否存在，是否是文件  
	        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {  
	  
	            System.out.println("File does not exist: " + file);  
	            return;  
	        }  
	  
	        File newFile = new File(toFile);  
	  
	        //修改文件名  
	        if (toBeRenamed.renameTo(newFile)) {  
	            System.out.println("File has been renamed.");  
	        } else {  
	            System.out.println("Error renmaing file");  
	        }  
	  
	    }
	 
}
