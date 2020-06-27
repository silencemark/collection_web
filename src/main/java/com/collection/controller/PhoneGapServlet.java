package com.collection.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.collection.service.IAppVipCardService;

public class PhoneGapServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IAppVipCardService appVipCardService;
	private static Logger logger = Logger.getLogger(PhoneGapServlet.class);
	@Override
	public void init() throws ServletException { 
		super.init(); 
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:local/springMvc.xml"); 
		appVipCardService = (IAppVipCardService)context.getBean("appVipCardService");
		System.out.println(appVipCardService);
	}
 
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html,charset=utf-8");
		response.getWriter().println("����POST��ʽ�ϴ��ļ�");
		System.out.println("get.........");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		logger.info("come file upload....");
		
		File file1 = null;
		FileItemFactory factory = new DiskFileItemFactory();
		logger.info("factory=="+factory);
		ServletFileUpload upload = new ServletFileUpload(factory);
		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, Object>> fileList  = new ArrayList<Map<String,Object>>();
		params.put("orgid", request.getAttribute("orgid"));
		try {
			List<FileItem> list = upload.parseRequest(request);
			logger.info("List<FileItem> list=="+list+"list.size===="+list.size());
			// ��ȡ����
			for (FileItem fileItem : list) {
				if (fileItem.isFormField()) {
					try {
						params.put(fileItem.getFieldName(),
								fileItem.getString("UTF-8"));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}

				}
			}
			logger.info("++++++params000000000++++++++++");
			logger.info("++++++params++++++++++"+params);
			logger.info("++++++params11111111111++++++++++");
			logger.info("list==="+list);
			// ��ȡ�ϴ��ļ�
			for (FileItem fileItem : list) {
				logger.info("++++++fileItem.isFormField()++++++++++"+fileItem.isFormField());
				if (!fileItem.isFormField()) {
					String fileItemName = fileItem.getName();
					if(fileItemName!=null&&!"".equals(fileItemName)){
						if(fileItemName.indexOf("?")!=-1){
							fileItemName =fileItemName.substring(0,fileItemName.indexOf("?")); 
						}
					}
					logger.info("=======fileItemName=======+"+fileItemName);
					File remoteFile = new File(new String(fileItemName.getBytes(), "UTF-8"));
					String fileDirectory = "";
					if (params.get("FileDirectory") != null) {
						fileDirectory = "/"+ params.get("FileDirectory").toString()+"/";
					}else{
						fileDirectory = "/";
					}
					String fileName = "";
					logger.info("++++++remoteFile.getName()++++++++++"+remoteFile.getName());
					if (remoteFile.getName().lastIndexOf(".") >= 0) {
						fileName = "app_"+UUID.randomUUID().toString().replace("-", "")+ remoteFile.getName().substring(remoteFile.getName().lastIndexOf("."));
					}else{
						fileName = "app_"+UUID.randomUUID().toString().replace("-", "");
					}
					logger.info("++++++fileName++++++++++"+fileName);
					file1 = new File(this.getServletContext().getRealPath("customerfile" + fileDirectory), fileName);
					logger.info("++++++file1++++++++++"+file1);
					file1.getParentFile().mkdirs();
					file1.createNewFile();
					logger.info("++++++file1++++++++++"+file1);
					InputStream ins = fileItem.getInputStream();
					OutputStream ous = new FileOutputStream(file1);
					try {
						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = ins.read(buffer)) > -1) {
							ous.write(buffer, 0, len);
						}
					} finally {
						ous.close();
						ins.close();
					}
					logger.info("++++++fileName++++++++++"+fileName);
					if(fileName==null&&"".equals(fileName)){
						continue;
					}
					logger.info("come in ImageUtil.reduceImg begin");
					/*String oldrealpath = "";
					if(fileName.indexOf("wav")==-1&&fileName.lastIndexOf("amr")==-1){
						oldrealpath = request.getSession().getServletContext().getRealPath("")+"/customerfile" + fileDirectory+fileName;
						ImageUtil.reduceImg(oldrealpath ,oldrealpath, fileItem.getSize());
					}*/
					logger.info("come in ImageUtil.reduceImg begin");
					//File file = new File(oldrealpath);
					Map<String, Object> fileMap = new HashMap<String, Object>();
					String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+getServletContext().getContextPath();
					fileMap.put("httpUrl", basePath+"/customerfile" + fileDirectory+fileName);
					fileMap.put("webUrl", basePath);
					fileMap.put("fileName", fileItemName);
//					fileMap.put("fileSize", file.length()); 
					fileMap.put("fileSize", fileItem.getSize());
					fileMap.put("contentType", fileItem.getContentType());
					fileList.add(fileMap);
					logger.info("fileMap！"+fileMap.toString());
					try {
						Map<String,Object> paramMap = new HashMap<String,Object>();
						paramMap.put("companyid", params.get("FileDirectory"));
						paramMap.put("userid", params.get("userid"));
						paramMap.put("size", fileItem.getSize());
						String type =  null;
						if(params.get("type")!=null){
							type = (String) params.get("type");
						}
						if(type==null||"".equals(type)||"null".equals(type)){
							type="90";
						}
						paramMap.put("type", type);
						paramMap.put("url", "/customerfile"+fileDirectory+fileName);
						logger.info("appVipCardService.insertCustomerFileInfo begin！");
						//this.appVipCardService.insertCustomerFileInfo(paramMap);
						logger.info("appVipCardService.insertCustomerFileInfo end！");
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						logger.error("app 保存上传文件记录失败了！"+e.getMessage());
					}
				}
			}
		} catch (FileUploadException e) {
			logger.error("app 保存上传文件记录失败了！"+e.getMessage());
		}
		PrintWriter out = response.getWriter();
		out.print(JSONArray.fromObject(fileList));
	}

}