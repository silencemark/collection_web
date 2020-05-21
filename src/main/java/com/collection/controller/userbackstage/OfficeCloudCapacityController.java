package com.collection.controller.userbackstage;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.service.IndexService;
import com.collection.service.oa.OfficeService;
import com.collection.service.personal.PersonalService;
import com.collection.service.worksheet.WorkSheetInspectService;
import com.collection.util.Constants;
import com.collection.util.HtmlUtil;
import com.collection.util.PageHelper;
import com.collection.util.SDKTestSendTemplateSMS;
import com.collection.util.UserUtil;


@Controller
@RequestMapping(value="/userbackstage")
public class OfficeCloudCapacityController extends BaseController{

	@Resource
	OfficeService officeService;
	
	@Resource IndexService indexService;
	
	@Resource WorkSheetInspectService workSheetInspectService;
	@Resource PersonalService personalService;
	
	/**
	 * 进入扩容页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoAddCloudCapacity")
	public String intoAddCloudCapacity(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("map", map);
		return "/userbackstage/OA/oa_enlarge";
	}
	
	/**
	 * 添加扩容申请信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertCloudCapacity")
	public Map<String,Object> insertCloudCapacity(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			map.put("companyid", getUserInfo(request).get("companyid"));
			Map<String,Object> cloudmap = this.officeService.getCompanyCloudInfo(map);
			if(cloudmap != null && cloudmap.size() > 0){
				long memory = Long.parseLong(String.valueOf(map.get("memory")))*1024*1024;
				
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("cloudid", cloudmap.get("cloudid"));
				paramMap.put("memory", memory);
				paramMap.put("createid", getUserInfo(request).get("userid"));
				this.officeService.insertCloudCapacityInfo(paramMap);
				
				try {
					Map<String,Object> manageusermap = this.personalService.getManageUserInfo(paramMap);
					Map<String,Object> companymap = this.personalService.getCompanyBasicInfo(map);
					String phone = String.valueOf(manageusermap.get("phone"));
					String username = String.valueOf(manageusermap.get("realname"));
					String content = String.valueOf(companymap.get("companyname"));
					SDKTestSendTemplateSMS.sendCloudApply(phone, username, content);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				resultMap.put("status", 0);
			}else{
				resultMap.put("status", 1);
			}
			
			this.insertManageLog(getUserInfo(request).get("companyid")+"", 2, "扩容", "添加了扩容申请信息。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 查询扩容记录
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getCloudCapacity")
	public String getCloudCapacity(@RequestParam Map<String,Object> map , HttpServletRequest request){
		try {
			map.put("companyid", getUserInfo(request).get("companyid"));
			String sjmemorycount = this.officeService.getCloudCapacitySjmemoryCount(map);
			PageHelper page = new PageHelper(request);
			int count = this.officeService.getCloudCapacityCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.officeService.getCloudCapacityList(map);
			Map<String,Object> cloudmap = this.officeService.getCompanyCloudInfo(map);
			String maxmemory = cloudmap.get("maxmemory")+"";
			request.setAttribute("pager", page.cateringPage().toString());
			request.setAttribute("cloudlist", list);
			request.setAttribute("memoryCount", maxmemory);
			request.setAttribute("feedmemory", (Float.parseFloat(maxmemory) - Float.parseFloat(sjmemorycount)));
			
			this.insertManageLog(getUserInfo(request).get("companyid")+"", 2, "扩容", "查看了扩容记录。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return "/userbackstage/OA/oa_enlargelog";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 查询公司文件夹列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getCompanyCloudModuleList")
	public String getCompanyCloudModuleList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		try {
			map.put("companyid", getUserInfo(request).get("companyid"));
			PageHelper page = new PageHelper(request);
			int count = this.officeService.getCompanyCloudModuleListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			
			List<Map<String,Object>> list = this.officeService.getCompanyCloudModuleList(map);
			
			Map<String,Object> cloudmap = this.officeService.getCompanyCloudInfo(map);
			
			String maxmemory = String.valueOf(cloudmap.get("maxmemory"));
			String usedmemory = String.valueOf(cloudmap.get("usedmemory"));
			if(!cloudmap.containsKey("maxmemory") || "".equals(maxmemory)){
				maxmemory = "0";
			}
			if(!cloudmap.containsKey("usedmemory") || "".equals(usedmemory)){
				usedmemory = "0";
			}
			
			request.setAttribute("maxmemory", maxmemory);
			request.setAttribute("usedmemory", usedmemory);
			request.setAttribute("sy_memory", (Float.parseFloat(maxmemory)-Float.parseFloat(usedmemory)));
			request.setAttribute("modulelist", list);
			request.setAttribute("pager", page.cateringPage().toString());
			
			this.insertManageLog(getUserInfo(request).get("companyid")+"", 2, "企业云盘", "查看企业云盘栏目信息。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "/userbackstage/OA/oa_skydrive";
	}
	
	/**
	 * 添加文件夹
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertCompanyCloudModuleInfo")
	public Map<String,Object> insertCompanyCloudModuleInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			map.put("companyid", getUserInfo(request).get("companyid"));
			map.put("createid", getUserInfo(request).get("userid"));
			this.officeService.insertCompanyCloudModule(map);
			resultMap.put("status", 0);
			
			this.insertManageLog(getUserInfo(request).get("companyid")+"", 2, "企业云盘", "新建了一个栏目。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 修改文件夹信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateCompanyCloudModuleInfo")
	public Map<String,Object> updateCompanyCloudModuleInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			map.put("updateid", getUserInfo(request).get("userid"));
			map.put("updatetime", new Date());
			this.officeService.updateCompanyCloudModuleInfo(map);
			resultMap.put("status", 0);
			
			this.insertManageLog(getUserInfo(request).get("companyid")+"", 2, "企业云盘", "修改了栏目信息。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	

	
	
	
	
	
	
	/**
	 * 进入文件页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoCompanyCloudFilePage")
	public String intoCompanyCloudFilePage(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("map", map);
		return "/userbackstage/OA/oa_skydrivelist";
	}
	
	/**
	 * 查询云盘文件
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getCompanyCloudFileList")
	public Map<String,Object> getCompanyCloudFileList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			map.put("companyid", getUserInfo(request).get("companyid"));
			PageHelper page = new PageHelper(request);
			int count = this.officeService.getCompanyCloudFileListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.officeService.getCompanyCloudFileList(map);
			
			resultMap.put("filelist", list);
			resultMap.put("page", page.JSCateringPage().toString());
			resultMap.put("status", 0);
			
			
			this.insertManageLog(getUserInfo(request).get("companyid")+"", 2, "企业云盘", "查询了文件列表。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 添加文件
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertCompanyCloudFileInfo")
	public Map<String,Object> insertCompanyCloudFileInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
//			if(!map.containsKey("memory")){
//				resultMap.put("status", 1);
//				resultMap.put("message", "memory未传入值");
//				return resultMap;
//			}
			map.put("companyid", getUserInfo(request).get("companyid"));
			Map<String,Object> cloudmap = this.officeService.getCompanyCloudInfo(map);
			if(cloudmap != null && cloudmap.size() > 0){
				map.put("cloudid", cloudmap.get("cloudid"));
				map.put("createid", getUserInfo(request).get("userid"));
				map.put("content", HtmlUtil.returnHtmlStr(String.valueOf(map.get("content")), Constants.PROJECT_PATH));
				String fileid = this.officeService.insertCompanyCloudFileInfo(map);
				
				//修改内存
//				map.put("usedmemory", Float.parseFloat(String.valueOf(map.get("memory"))));
//				this.officeService.updateCompanyCloudUseMemoryInfo(map);
				
				JSONObject rangejson=JSONObject.fromObject(map.get("rangelist")+"");
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> userlist=(List<Map<String, Object>>)rangejson.get("rangelist");
				for(Map<String, Object> user:userlist){
					user.put("companyid", getUserInfo(request).get("companyid"));
	 				user.put("resourceid", fileid);
	 				user.put("resourcetype", 13);
	 				if(user.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
	 					user.put("organizeid", user.get("organizeid"));
	 					user.put("type", 2);
	 					
	 					//添加到发布范围用户表
	 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
	 					for(Map<String, Object> users:userinfolist){
	 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
	 						releaserangemap.put("companyid", getUserInfo(request).get("companyid"));
	 						releaserangemap.put("resourceid", fileid);
	 						releaserangemap.put("resourcetype", 13);
	 						releaserangemap.put("userid", users.get("userid"));
	 						releaserangemap.put("createid", getUserInfo(request).get("userid"));
	 						this.indexService.insertReleaseRangeUser(releaserangemap);
	 					}
	 				}
	 				if(user.containsKey("userid") && !"".equals(map.get("userid"))){
	 					user.put("userid", user.get("userid"));
	 					user.put("type", 1);
	 					
	 					//添加到发布范围用户表
	 					Map<String, Object>  releaserangemap=new HashMap<String, Object>();
						releaserangemap.put("companyid", getUserInfo(request).get("companyid"));
						releaserangemap.put("resourceid", fileid);
						releaserangemap.put("resourcetype", 13);
						releaserangemap.put("userid", user.get("userid"));
						releaserangemap.put("createid", getUserInfo(request).get("userid"));
						this.indexService.insertReleaseRangeUser(releaserangemap);
	 				}
	 				
	 				//添加到发布范围表
					this.indexService.insertReleaseRange(user);
				}
				
			}else{
				resultMap.put("status", 1);
				return resultMap;
			}
			resultMap.put("status", 0);
			
			this.insertManageLog(getUserInfo(request).get("companyid")+"", 2, "企业云盘", "添加了一个标题为："+map.get("title")+"的云盘文件。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 进入修改页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoUpdateCloudFile")
	public String intoUpdateCloudFile(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("map", map);
		return "/userbackstage/OA/oa_skydriveedit";
	}
	
	/**
	 * 进入详情页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoCloudFileDetail")
	public String intoCloudFileDetail(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("map", map);
		return "/userbackstage/OA/oa_skydrivedetail";
	}
	
	/**
	 * 查询文件详情
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getCompanyCloudFileInfo")
	public Map<String,Object> getCompanyCloudFileInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			Map<String,Object> filemap = this.officeService.getCompanyCloudFileInfo(map);
			
			resultMap.put("status", 0);
			resultMap.put("filemap", filemap);
			
			this.insertManageLog(getUserInfo(request).get("companyid")+"", 2, "企业云盘", "查看了文件的详细信息。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 修改文件信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/upateCompanyCloudFileInfo")
	 public Map<String,Object> upateCompanyCloudFileInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
//			if(!map.containsKey("oldfileurl") || !map.containsKey("memory") || !map.containsKey("oldmemory")){
//				resultMap.put("status", 1);
//				resultMap.put("message", "参数不全");
//				return resultMap;
//			}
			
			String fileid = String.valueOf(map.get("fileid"));
			map.put("content", HtmlUtil.returnHtmlStr(String.valueOf(map.get("content")), Constants.PROJECT_PATH));
			this.officeService.updateCompanyCloudFileInfo(map);
			
			//删除文件
//			String realpath = request.getSession().getServletContext().getRealPath("");
//			realpath = realpath+map.get("oldfileurl");
//			File file = new File(realpath);
//			if(file.exists()){
//				file.delete();
//			}
			
			//修改内存大小
//			map.put("companyid", getUserInfo(request).get("companyid"));
//			map.put("usedmemory", (Float.parseFloat(String.valueOf(map.get("memory"))) - Float.parseFloat(String.valueOf(map.get("oldmemory")))));
//			this.officeService.updateCompanyCloudUseMemoryInfo(map);
			
			Map<String,Object> rangemap = new HashMap<String, Object>();
			rangemap.put("resourceid", fileid);
			rangemap.put("resourcetype", 13);
			this.workSheetInspectService.deleteReleaseRangeInfo(rangemap);
			
			JSONObject rangejson=JSONObject.fromObject(map.get("rangelist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)rangejson.get("rangelist");
			for(Map<String, Object> user:userlist){
				user.put("companyid", getUserInfo(request).get("companyid"));
 				user.put("resourceid", fileid);
 				user.put("resourcetype", 13);
 				if(user.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
 					user.put("organizeid", user.get("organizeid"));
 					user.put("type", 2);
 					
 					//添加到发布范围用户表
 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
 					for(Map<String, Object> users:userinfolist){
 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
 						releaserangemap.put("companyid", getUserInfo(request).get("companyid"));
 						releaserangemap.put("resourceid", fileid);
 						releaserangemap.put("resourcetype", 13);
 						releaserangemap.put("userid", users.get("userid"));
 						releaserangemap.put("createid", getUserInfo(request).get("userid"));
 						this.indexService.insertReleaseRangeUser(releaserangemap);
 					}
 				}
 				if(user.containsKey("userid") && !"".equals(map.get("userid"))){
 					user.put("userid", user.get("userid"));
 					user.put("type", 1);
 					
 					//添加到发布范围用户表
 					Map<String, Object>  releaserangemap=new HashMap<String, Object>();
					releaserangemap.put("companyid", getUserInfo(request).get("companyid"));
					releaserangemap.put("resourceid", fileid);
					releaserangemap.put("resourcetype", 13);
					releaserangemap.put("userid", user.get("userid"));
					releaserangemap.put("createid", getUserInfo(request).get("userid"));
					this.indexService.insertReleaseRangeUser(releaserangemap);
 				}
 				
 				//添加到发布范围表
				this.indexService.insertReleaseRange(user);
			}
			
			resultMap.put("status", 0);
			
			this.insertManageLog(getUserInfo(request).get("companyid")+"", 2, "企业云盘", "修改了云盘文件信息。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	 }
	
	/**
	 * 删除云盘文件信息
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteCompanyCloudFileInfo")
	public String deleteCompanyCloudFileInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
//			if(!map.containsKey("usedmemory")){
//				return "redirect:/userbackstage/intoCompanyCloudFilePage?moduleid="+map.get("moduleid");
//			}else{
//				map.put("usedmemory", (0 - Float.parseFloat(String.valueOf(map.get("usedmemory")))));
//			}
//			if(!map.containsKey("oldfileurl")){
//				return "redirect:/userbackstage/intoCompanyCloudFilePage?moduleid="+map.get("moduleid");
//			}
//			String realpath = request.getSession().getServletContext().getRealPath("");
//			realpath = realpath+map.get("oldfileurl");
//			File file = new File(realpath);
//			if(file.exists()){
//				file.delete();
//			}
//			
//			map.put("companyid", getUserInfo(request).get("companyid"));
//			this.officeService.updateCompanyCloudUseMemoryInfo(map);
			
			map.put("delflag", 1);
			this.officeService.updateCompanyCloudFileInfo(map);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "redirect:/userbackstage/intoCompanyCloudFilePage?moduleid="+map.get("moduleid");
	}
	
	public Map<String,Object> getUserInfo(HttpServletRequest request){
		return UserUtil.getUser(request);
	}
	
}
