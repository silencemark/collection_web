package com.collection.controller.managebackstage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.service.managebackstage.SystemCloudService;
import com.collection.util.PageHelper;
import com.collection.util.UserUtil;

@Controller
@RequestMapping(value="/managebackstage")
public class SystemCloudController extends BaseController{
	
	@Resource SystemCloudService systemCloudService;
	
	/**
	 * 进入文件类型选择页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoSkyDrivePage")
	public String intoSkyDrivePage(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("map", map);
		return "/managebackstage/cloud/skydrive";
	}
	
	/**
	 * 查询云盘文件列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSystemCloudFileList")
	public String getSystemCloudFileList(@RequestParam Map<String,Object> map , 
			HttpServletRequest request , HttpServletResponse response){
		
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			map.put("type", 1);
			PageHelper page = new PageHelper(request);
			int count = this.systemCloudService.getSystemCloudListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.systemCloudService.getSystemCloudListInfo(map);
			
			request.setAttribute("map", map);
			request.setAttribute("filelist", list);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return "/managebackstage/cloud/skydrive_file";
	}
	
	/**
	 * 查询云盘视频列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSystemCloudVedioList")
	public String getSystemCloudVedioList(@RequestParam Map<String,Object> map , 
			HttpServletRequest request , HttpServletResponse response){
		
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			map.put("type", 2);
			PageHelper page = new PageHelper(request);
			int count = this.systemCloudService.getSystemCloudListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.systemCloudService.getSystemCloudListInfo(map);
			
			request.setAttribute("map", map);
			request.setAttribute("filelist", list);
			request.setAttribute("pager", page.cateringPage().toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return "/managebackstage/cloud/skydrive_vedio";
	}
	
	/**
	 * 查询父级的parentid
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getFolderParentId")
	public Map<String,Object> getFolderParentId(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			String parentid = this.systemCloudService.getFolderParentid(map);
			resultMap.put("parentid", parentid);
			resultMap.put("status", 0);
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
	@RequestMapping(value="/updateSystemCloudInfo")
	public Map<String,Object> updateSystemCloudInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			map.put("updateid", getUserInfo(request).get("userid"));
			this.systemCloudService.updateSystemCloudInfo(map);
			Map<String,Object> detailmap = this.systemCloudService.getSystemCloudDetailInfo(map);
			
			if(detailmap != null && detailmap.size() > 0){
				String filetype = String.valueOf(detailmap.get("filetype"));
				String type = String.valueOf(detailmap.get("type"));
				
				String content = getUserInfo(request).get("realname")+"将";
				if("1".equals(type)){
					content += "文件里面的名为"+detailmap.get("filename")+"的";
				}else{
					content += "视频里面的名为"+detailmap.get("filename")+"的";
				}
				if("1".equals(filetype)){
					content += "文件的名称修改为"+map.get("filename");
				}else{
					content += "文件夹的名称修改为"+map.get("filename");
				}
				
				insertManageLog("",1,"餐饮大师网盘",content,getUserInfo(request).get("userid")+"");
			}
			
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}

	/**
	 * 删除文件信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/deleteSystemCloud")
	public Map<String,Object> deleteSystemCloud(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			JSONObject json=JSONObject.fromObject(map.get("filelist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> filelist=(List<Map<String, Object>>)json.get("filelist");
			for(Map<String, Object> filemap:filelist){
				filemap.put("updateid", getUserInfo(request).get("userid"));
				filemap.put("delflag", 1);
				this.systemCloudService.updateSystemCloudInfo(filemap);
				
				Map<String,Object> detailmap = this.systemCloudService.getSystemCloudDetailInfo(filemap);
				
				if(detailmap != null && detailmap.size() > 0){
					String filetype = String.valueOf(detailmap.get("filetype"));
					String type = String.valueOf(detailmap.get("type"));
					
					String content = getUserInfo(request).get("realname")+"将";
					if("1".equals(type)){
						content += "文件里面的名为"+detailmap.get("filename")+"的";
					}else{
						content += "视频里面的名为"+detailmap.get("filename")+"的";
					}
					if("1".equals(filetype)){
						content += "文件进行了删除操作";
					}else{
						content += "文件夹进行了删除操作";
					}
					
					insertManageLog("",1,"餐饮大师网盘",content,getUserInfo(request).get("userid")+"");
				}
			}
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 查询文件夹内部是否存在  文件
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getFolderChildCount")
	public Map<String,Object> getFolderChild(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			int count = 0;
			JSONObject json=JSONObject.fromObject(map.get("filelist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> filelist=(List<Map<String, Object>>)json.get("filelist");
			for(Map<String, Object> filemap:filelist){
				count += this.systemCloudService.getSystemCloudListCount(filemap);
			}
			resultMap.put("count", count);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 添加云盘文件
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertSystemCloudInfo")
	public Map<String,Object> insertSystemCloudInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			map.put("createid", getUserInfo(request).get("userid"));
			this.systemCloudService.insertSystemCloudInfo(map);
			
			String filetype = String.valueOf(map.get("filetype"));
			String type = String.valueOf(map.get("type"));
			String content = getUserInfo(request).get("realname")+"向";
			if("1".equals(type)){
				content += "文件里面添加了名为"+map.get("filename")+"的";
			}else{
				content += "视频里面添加了名为"+map.get("filename")+"的";
			}
			if("1".equals(filetype)){
				content += "文件";
			}else{
				content += "文件夹";
			}
			
			insertManageLog("",1,"餐饮大师网盘",content,getUserInfo(request).get("userid")+"");
			
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 获取当前管理方登陆用户的信息
	 * @param request
	 * @return
	 */
	public Map<String,Object> getUserInfo(HttpServletRequest request){
		return UserUtil.getSystemUser(request);
	}
}
