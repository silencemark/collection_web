package com.collection.controller.managebackstage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.service.CompanyService;
import com.collection.service.managebackstage.CloudCapacityService;
import com.collection.util.PageHelper;
import com.collection.util.UserUtil;



@Controller
@RequestMapping(value="/managebackstage")
public class CloudCapacityController extends BaseController{

	@Resource CloudCapacityService cloudCapacityService;
	@Resource CompanyService companyService;
	
	/**
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getCloudCapacityList")
	public String getCloudCapacityList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.cloudCapacityService.getCloudCapacityListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.cloudCapacityService.getCloudCapacityList(map);
			
			request.setAttribute("list", list);
			request.setAttribute("map", map);
			request.setAttribute("pager", page.cateringPage().toString());
			
			this.insertManageLog("", 1, "申请记录", "查看了申请记录列表信息。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/managebackstage/usercloud/apply_list";
	}
	
	
	/**
	 * 查询扩容记录详情
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getCloudCapacityDetail")
	public String getCloudCapacityDetail(@RequestParam Map<String,Object> map , HttpServletRequest request){
		try {
			Map<String,Object> cloudmap = this.cloudCapacityService.getCloudCapacityDetail(map);
			
			request.setAttribute("cloudmap", cloudmap);
			request.setAttribute("map", map);
			
			this.insertManageLog("", 1, "申请记录", "查看了申请记录详细信息。", getUserInfo(request).get("userid")+"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "/managebackstage/usercloud/apply_detail";
	}
	
	/**
	 * 进入审核页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getUpdateCloudCapacityDetail")
	public String getUpdateCloudCapacityDetail(@RequestParam Map<String,Object> map , HttpServletRequest request){
		try {
			Map<String,Object> cloudmap = this.cloudCapacityService.getCloudCapacityDetail(map);
			
			request.setAttribute("cloudmap", cloudmap);
			request.setAttribute("map", map);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "/managebackstage/usercloud/apply_detail_examine";
	}
	
	/**
	 * 审核提交
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateCloudCapacity")
	public Map<String,Object> updateCloudCapacity(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			map.put("updateid", getUserInfo(request).get("userid"));
			this.cloudCapacityService.updateCloudCapacityInfo(map);
			String status = String.valueOf(map.get("status"));
			if(map.containsKey("status") && !"".equals(status)){
				Map<String,Object> detailmap = this.cloudCapacityService.getCloudCapacityDetail(map);
				String content = "";
				if("0".equals(status)){
					content = String.valueOf(getUserInfo(request).get("realname"))+"拒绝了"+detailmap.get("companyname")+"的扩容（"+detailmap.get("memory")+"）申请。";
				}else if("2".equals(status)){
					Map<String,Object> param =  new HashMap<String,Object>();
					param.put("edition", 2);
					param.put("ispromote", 1);
					param.put("companyid", map.get("companyid"));
					this.companyService.updateCompany(param);
					
					content = String.valueOf(getUserInfo(request).get("realname")+"同意了"+detailmap.get("companyname")+"的"+detailmap.get("memory")+"扩容申请。");
				}
				insertManageLog("",1,"扩容申请审核",content,getUserInfo(request).get("userid")+"");
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
	 * 修改公司是否参与宣传的状态
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateCompanyIsPromote")
	public Map<String,Object> updateCompanyIsPromote(@RequestParam Map<String,Object> map ,
			HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			if(map.containsKey("ispromote") && !"".equals(map.get("ispromote"))){
				String ispromote = String.valueOf(map.get("ispromote"));
				resultMap.put("ispromote", ispromote);
				resultMap.put("companyid", map.get("companyid"));
				this.companyService.updateCompany(resultMap);
				resultMap.clear();
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
	 * 获取当前管理方登陆用户的信息
	 * @param request
	 * @return
	 */
	public Map<String,Object> getUserInfo(HttpServletRequest request){
		return UserUtil.getSystemUser(request);
	}
}
