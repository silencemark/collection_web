package com.collection.controller.userbackstage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.base.controller.BaseController;
import com.collection.controller.userbackstage.UserIndexController.ExportExcel;
import com.collection.service.CompanyService;
import com.collection.service.IndexService;
import com.collection.service.UserInfoService;
import com.collection.service.oa.OfficeService;
import com.collection.service.personal.PersonalService;
import com.collection.service.storefront.EverydayReportService;
import com.collection.util.Constants;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;
import com.collection.util.QRcode;
import com.collection.util.UserUtil;

/**
 * 使用方后台管理企业管理
 * @author silence
 *
 */
@Controller
@RequestMapping("/userbackstage")
public class CompanyController extends BaseController{
	private transient static Log log = LogFactory.getLog(CompanyController.class);
	@Autowired private CompanyService  companyService;
	
	@Resource private IndexService indexService;

	@Resource private UserInfoService userInfoService;
	
	@Resource private PersonalService personalService;
	/**
	 * 
	 * 查询管理日志
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getLogList")
	public String getLogList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userinfo=UserUtil.getUser(request);
		map.put("companyid", userinfo.get("companyid")+"");
		map.put("type", 2);
		
		//查询模块列表
		List<Map<String, Object>> modellist=this.companyService.getLogModelList(map);
		model.addAttribute("modellist", modellist);
		//初始化分页
		PageHelper pageHelper = new PageHelper(request);
		pageHelper.initPage(map);
		List<Map<String, Object>> loglist=this.companyService.getLogList(map);
		int num=this.companyService.getLogListNum(map);
		pageHelper.setTotalCount(num);
		model.addAttribute("loglist", loglist);
		model.addAttribute("pager", pageHelper.cateringPage().toString());
		model.addAttribute("map", map);
		insertManageLog(userinfo.get("companyid")+"",2,"管理日志","查看了管理日志。",userinfo.get("userid")+"");
		return "/userbackstage/company/business_log";
	}
	/**
	 * 
	 * 导出管理日志
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportLogList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/管理日志列表.xls");
		Map<String, Object> userinfo=UserUtil.getUser(request);
		map.put("companyid", userinfo.get("companyid")+"");
		map.put("type", 2);
		List<Map<String, Object>> loglist=this.companyService.getLogList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "操作人员", "操作描述", "操作时间"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> log:loglist){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a", log.get("realname")==null?"":log.get("realname"));
	        datamap.put("b", "【"+log.get("model")+"】"+log.get("content"));
	        datamap.put("c", log.get("createtime")==null?"":log.get("createtime"));
	        dataset.add(datamap);
        }
        try  
        {
            OutputStream out = new FileOutputStream(importurl);  
            ex.exportExcel(headers, dataset, out);
            out.close();  
            System.out.println("excel导出成功！");
    		insertManageLog(userinfo.get("companyid")+"",2,"管理日志","导出了管理日志。",userinfo.get("userid")+"");
        }  
        catch (FileNotFoundException e)  
        {
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }
		return "管理日志列表.xls";
	}
	/**
	 * 
	 * 查询banner
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getBannerList")
	public String getbannerList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userinfo=UserUtil.getUser(request);
		map.put("companyid", userinfo.get("companyid")+"");
		map.put("type", 2);
		
		//查询模块列表
		List<Map<String, Object>> bannerlist=this.companyService.getBannerList(map);
		model.addAttribute("bannerlist", bannerlist);
		insertManageLog(userinfo.get("companyid")+"",2,"APP图片配置","查看了APP-Banner列表。",userinfo.get("userid")+"");
		return "/userbackstage/company/business_appimg";
	}
	

/*    @RequestMapping("/uploadimage")
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, ModelMap model) {  
  
        System.out.println("开始");  
        String path = request.getSession().getServletContext().getRealPath("upload");  
        String fileName = file.getOriginalFilename(); 
        
        System.out.println(path);  
        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
  
        //保存  
        try {  
            file.transferTo(targetFile);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        model.addAttribute("fileUrl", request.getContextPath()+"/upload/"+fileName);  
  
        return "redirect:/userbackstage/getbannerList";  
    }*/
    
	/**
	 * 
	 * 修改/添加banner
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/editbanner")
	@ResponseBody
	public Map<String, Object> editbanner(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> userinfo=UserUtil.getUser(request);

		insertManageLog(userinfo.get("companyid")+"",2,"APP图片配置","修改了APP图片",userinfo.get("userid")+"");
		
		Map<String, Object> data=new HashMap<String, Object>();
		JSONObject json=JSONObject.fromObject(map.get("bannerlist")+"");
		List<Map<String, Object>> bannerlist=(List<Map<String, Object>>)json.get("bannerlist");
		//添加到字典扩展表
		for(Map<String, Object> banner:bannerlist){
			banner.put("companyid", userinfo.get("companyid"));
			//修改
			if(banner.containsKey("id") && !banner.get("id").equals("")){
				banner.put("updateid", userinfo.get("userid"));
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				banner.put("updatetime",sdf.format(new Date()));
				this.companyService.updateBanner(banner);
			}
			//新增
			else{
				banner.put("createid", userinfo.get("userid"));
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				banner.put("createtime",sdf.format(new Date()));
				this.companyService.insertBanner(banner);
			}
		}
		return data;
	}
	
	/**
	 * 
	 * 删除/修改banner
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/deletebanner")
	@ResponseBody
	public Map<String, Object> deletebanner(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		Map<String, Object> userinfo=UserUtil.getUser(request);
		map.put("updateid", userinfo.get("userid"));
		map.put("updatetime", new Date());
		this.companyService.updateBanner(map);
		insertManageLog(userinfo.get("companyid")+"",2,"APP图片配置","删除了一张Banner配图。",userinfo.get("userid")+"");
		return data;
	}
	/**
	 * 
	 * 查询组织架构
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getOrganizeList")
	public String getOrganizeList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		return "/userbackstage/company/business_structure";
	}
	
	/**
	 * 查询组织信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getOrganizeInfo")
	@ResponseBody
	public Map<String, Object> getOrganizeInfo(HttpServletRequest request,@RequestParam Map<String, Object> map, Model model
			,HttpServletResponse response) {
		List<Map<String, Object>> userlist=new ArrayList<Map<String,Object>>();
		Map<String, Object> userinfo=UserUtil.getUser(request);
		if(!map.containsKey("organizeid") || map.get("organizeid").equals("")){
			map.put("companyid", userinfo.get("companyid"));
			map.put("iscompany", 1);
			
			//查询公司所有用户
			Map<String, Object> companymap=new HashMap<String, Object>();
			companymap.put("companyid", userinfo.get("companyid"));
			userlist=this.userInfoService.getUserList(companymap);
		}else{
//			List<Map<String,Object>> userinfolistall=new ArrayList<Map<String,Object>>();
			map.put("companyid", userinfo.get("companyid"));
			userlist=getUserByorganize(map);
		}
		Map<String, Object> organizemap=this.indexService.getOrganizeInfo(map);
		organizemap.put("usernum", userlist.size());
		return organizemap;
	}
	/**
	 * 添加组织信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertOrganize")
	@ResponseBody
	public Map<String, Object> insertOrganize(HttpServletRequest request,@RequestParam Map<String, Object> map, Model model
			,HttpServletResponse response) {
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> userinfo=UserUtil.getUser(request);
		insertManageLog(userinfo.get("companyid")+"",2,"企业管理-组织管理","新增了组织"+map.get("organizename"),userinfo.get("userid")+"");
		
		Map<String, Object> organizemap=new HashMap<String, Object>();
		organizemap.put("organizeid", map.get("updataorganizeid"));
		organizemap=this.indexService.getOrganizeInfo(organizemap);
		if(organizemap != null && organizemap.size()>0){
			map.put("updatadatacode", organizemap.get("datacode"));
			map.put("lastcompanyid", organizemap.get("companyid"));
			
			if(map.containsKey("type") && "3".equals(map.get("type"))){
				int count = this.companyService.getOrganizeStoreIsExsits(organizemap);
				if(count > 0){
					resultMap.put("status", 1);
					resultMap.put("message", "店面下面不能有店面");
					return resultMap;
				}
			}
		}
		
		try {
			String organizeid=UUID.randomUUID().toString().replaceAll("-", "");
			
			//生成qrcode
			
			String codeContent=Constants.PROJECT_PATH+"app/restaurant/appraise_add.html?organizeid="+organizeid;
			QRcode qr=new QRcode();
			String qrcode=qr.getQRcode(codeContent, request, organizeid);
			
			map.put("qrcode", qrcode);
			map.put("organizeid", organizeid);
			
			map.put("userid", userinfo.get("userid"));
			map.put("companyid", userinfo.get("companyid"));
			this.personalService.insertOrganizeInfo(map);
			
			resultMap.put("status", 0);
			resultMap.put("message", "新增成功");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "新增出错");
		}

		return resultMap;
	}
	
	
	/**
	 * 修改组织架构信息 传入参数：
	 * organizename，type，parentid，address，delflag，organizeid,datacode
	 * ，companyid
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateOrganize", method = RequestMethod.POST)
	public Map<String, Object> updateOrganize(
			@RequestParam Map<String, Object> map, HttpServletResponse response,HttpServletRequest request) {
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();
				
		Map<String, Object> userinfo=UserUtil.getUser(request);
		insertManageLog(userinfo.get("companyid")+"",2,"企业管理-组织管理","修改了组织"+map.get("organizename"),userinfo.get("userid")+"");
		
		Map<String, Object> organizemap=new HashMap<String, Object>();
		organizemap.put("organizeid", map.get("lastorganizeid"));
		organizemap=this.indexService.getOrganizeInfo(organizemap);
		if(organizemap != null && organizemap.size()>0){
			map.put("lastcompanyid", organizemap.get("companyid"));
			map.put("lastdatacode", organizemap.get("datacode"));
		}
		
		organizemap=new HashMap<String, Object>();
		organizemap.put("organizeid", map.get("updataorganizeid"));
		organizemap=this.indexService.getOrganizeInfo(organizemap);
		if(organizemap != null && organizemap.size()>0){
			map.put("parentid", organizemap.get("parentid"));
			map.put("updatadatacode", organizemap.get("datacode"));
			
			//判断组织的上下级是否存在店面
			if(map.containsKey("updatatype") && "3".equals(map.get("updatatype"))){
				organizemap.put("update", "");
				organizemap.put("myorganizeid", map.get("lastorganizeid"));
				int count = this.companyService.getOrganizeStoreIsExsits(organizemap);
				organizemap.remove("update");
				organizemap.remove("myorganizeid");
				if(count > 0){
					resultMap.put("status", 1);
					resultMap.put("message", "店面下面不能有店面");
					return resultMap;
				}
			}
		}
		
		try {
			
			this.personalService.updateOrganizeInfo(map);
			resultMap.put("message", "修改成功");
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "修改出错");
		}

		return resultMap;
	}
	
	@RequestMapping("/download") 
	public ResponseEntity<byte[]> download(@RequestParam Map<String,Object> map,HttpServletRequest request)
            throws IOException {
		String fileName=(map.get("fileName")+"").replace("/upload/qrcodes/", "");
		String rspName=request.getSession().getServletContext().getRealPath("");
		HttpHeaders headers = new HttpHeaders();  
		rspName = rspName+"/"+map.get("fileName");  
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  
		headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("gb2312"),  
		                "iso-8859-1"));
		File file = new File(rspName);
		if(!file.exists()){
			System.out.println("文件不存在");
			return null;
		}
		byte[] bytes = FileUtils.readFileToByteArray(file);  
		
/*		try {  
		    if (file.exists()){
		        file.delete();  
		    }
		} catch (Exception e){
		    e.printStackTrace();  
		}  */
		return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
	}
	
	/**
	 * 查询公司信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getCompanyInfo")
	public String getCompanyInfo(HttpServletRequest request,@RequestParam Map<String, Object> map, Model model
			,HttpServletResponse response) {
		Map<String, Object> userinfo=UserUtil.getUser(request);
		Map<String, Object> companymap=new HashMap<String, Object>();
		companymap.put("companyid", userinfo.get("companyid"));
		companymap=this.companyService.getCompanyInfo(companymap);
		model.addAttribute("companymap", companymap);
		insertManageLog(userinfo.get("companyid")+"",2,"餐饮公司管理","查看了公司信息",userinfo.get("userid")+"");
		return "/userbackstage/company/business_cpdetail";
	}
	
	
	/**
	 * 修改公司信息 传入参数：
	 * organizename，type，parentid，address，delflag，organizeid,datacode
	 * ，companyid
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateCompany")
	public Map<String, Object> updateCompany(
			@RequestParam Map<String, Object> map,
			HttpServletResponse response, HttpServletRequest request) {

		// 创建返回的map对象
		Map<String, Object> data = new HashMap<String, Object>();
		this.companyService.updateCompany(map);
		if (map.containsKey("logourl")) {
			UserUtil.pushPCUser(request, "logourl", map.get("logourl") + "");
		}
		if (map.containsKey("companyname")) {
			UserUtil.pushPCUser(request, "companyname", map.get("companyname")
					+ "");
		}
		Map<String, Object> userinfo = UserUtil.getUser(request);
		insertManageLog(userinfo.get("companyid") + "", 2, "餐饮公司管理",
				"修改了公司信息", userinfo.get("userid") + "");
		// 删除原来的营业执照
		Map<String, Object> licensemap = new HashMap<String, Object>();
		licensemap.put("companyid", map.get("companyid"));
		this.companyService.deleteLicense(licensemap);

		JSONObject json = JSONObject.fromObject(map.get("imagelist") + "");
		List<Map<String, Object>> imagelist = (List<Map<String, Object>>) json
				.get("imagelist");
		for (Map<String, Object> image : imagelist) {
			image.put("companyid", map.get("companyid"));
			image.put("createid", userinfo.get("userid"));
			this.companyService.insertLicense(image);
		}
		return data;
	}
	
	/**
	 * 管理员管理
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getManageList")
	public String getManageList(HttpServletRequest request,@RequestParam Map<String, Object> map, Model model
			,HttpServletResponse response) {
		Map<String, Object> userInfo=UserUtil.getUser(request);
		//查询所有的权限菜单
		List<Map<String, Object>> functionlist=new ArrayList<Map<String,Object>>();
		map.put("managerole", userInfo.get("managerole"));
		functionlist=this.userInfoService.getMenuList(map);
		model.addAttribute("functionlist", functionlist);
		//查询管理员列表
		List<Integer> rolelist=new ArrayList<Integer>();
		rolelist.add(2);
		rolelist.add(3);
		map.put("rolelist", rolelist);
		map.put("companyid", userInfo.get("companyid"));
		
		
		PageHelper pageHelper = new PageHelper(request);
		pageHelper.initPage(map);
		List<Map<String, Object>> userlist=this.userInfoService.getUserList(map);
		int num=this.userInfoService.getUserListNum(map);
		pageHelper.setTotalCount(num);
		model.addAttribute("userlist", userlist);
		model.addAttribute("pager", pageHelper.cateringPage().toString());
		model.addAttribute("map", map);
		insertManageLog(userInfo.get("companyid")+"",2,"管理员管理","查看了管理员列表信息",userInfo.get("userid")+"");
		return "/userbackstage/company/business_manage";
	}	
	
	/**
	 * 管理员管理异步
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getManageListByAjax")
	@ResponseBody
	public Map<String, Object> getManageListByAjax(HttpServletRequest request,@RequestParam Map<String, Object> map, Model model
			,HttpServletResponse response) {
		Map<String, Object> userInfo=UserUtil.getUser(request);
		//查询管理员列表
		List<Integer> rolelist=new ArrayList<Integer>();
		rolelist.add(2);
		rolelist.add(3);
		map.put("rolelist", rolelist);
		map.put("companyid", userInfo.get("companyid"));
		
		List<Map<String, Object>> userlist=this.userInfoService.getUserList(map);
		Map<String, Object> data =new HashMap<String, Object>();
		data.put("userlist", userlist);
		return data;
	}
	/**
	 * 修改用户管理信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updateUserInfoRole")
	@ResponseBody
	public Map<String, Object> updateUserInfoRole(HttpServletRequest request,@RequestParam Map<String, Object> map, Model model
			,HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		
		Map<String, Object> usermap=new HashMap<String, Object>();
		usermap.put("userid", map.get("userid"));
		usermap=this.userInfoService.getUserInfo(usermap);
		Map<String, Object> userinfo=UserUtil.getUser(request);
		insertManageLog(userinfo.get("companyid")+"",2,"企业管理-管理员管理","添加了管理员"+usermap.get("realname"),userinfo.get("userid")+"");
		
		try {
			this.userInfoService.updateUserInfo(map);
			
			JSONObject json=JSONObject.fromObject(map.get("datalist")+"");
			List<Map<String, Object>> functionlist=(List<Map<String, Object>>)json.get("functionlist");
			
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("organizelist");
			
			for(Map<String, Object> function:functionlist){
				function.put("userid", map.get("userid"));
				this.companyService.insertUserFunction(function);
			}
			//添加到发布范围
			for(Map<String, Object> user:userlist){
				user.put("companyid", userinfo.get("companyid"));
				user.put("resourceid", map.get("userid"));
				user.put("resourcetype", 14);
					//添加到发布范围表
				this.indexService.insertReleaseRange(user);
			}
			data.put("status", 0);
			data.put("message", "操作成功");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "操作失败，请稍候再试");
		}
		return data;
	}
	/**
	 * 移出管理信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author silence
	 */
	@RequestMapping("/deleteManageRole")
	@ResponseBody
	public Map<String, Object> deleteManageRole(HttpServletRequest request,@RequestParam Map<String, Object> map, Model model
			,HttpServletResponse response) {
		
		Map<String, Object> usermap=new HashMap<String, Object>();
		usermap.put("userid", map.get("userid"));
		usermap=this.userInfoService.getUserInfo(usermap);
		Map<String, Object> userinfo=UserUtil.getUser(request);
		insertManageLog(userinfo.get("companyid")+"",2,"企业管理-管理员管理","移除了"+usermap.get("realname")+"的管理权限",userinfo.get("userid")+"");
		
		
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			if(String.valueOf(map.get("managerole")).equals("2")){
				//删除发布范围表的数据
				Map<String, Object> rangemap=new HashMap<String, Object>();
				rangemap.put("resourceid", map.get("userid"));
				rangemap.put("resourcetype", 14);
				this.indexService.deleteRange(rangemap);
			}
			map.put("managerole", 1);
			this.userInfoService.updateUserInfo(map);
			data.put("status", 0);
			data.put("message", "操作成功");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "操作失败，请稍候再试");
		}
		return data;
	}
	
	/**
	 * 组织架构是否存在店面
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getOrganizeStoreIsExsits")
	public String getOrganizeStoreIsExsits(@RequestParam Map<String,Object> map , HttpServletRequest request){
		String msg = "success";
		try {
			int count = this.companyService.getOrganizeStoreIsExsits(map);
			if(count > 0){
				msg = "existe";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return msg;
	}
	
	
	/**
	 * 查询某组织下最大的排序值
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getParentOrganizeMaxPriority")
	public String getParentOrganizeMaxPriority(@RequestParam Map<String,Object> map , HttpServletRequest request){
		String priority = "";
		try {
			int num = this.companyService.getOrganizeMaxPriorityNum(map);
			priority = num+"";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return priority;
	}
}
