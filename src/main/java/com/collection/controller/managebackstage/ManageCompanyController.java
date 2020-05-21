package com.collection.controller.managebackstage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.JCEMac.MD5;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.json.JSON;
import com.base.controller.BaseController;
import com.collection.controller.userbackstage.UserIndexController.ExportExcel;
import com.collection.redis.RedisUtil;
import com.collection.service.CompanyService;
import com.collection.service.IndexService;
import com.collection.service.SystemService;
import com.collection.service.UserInfoService;
import com.collection.service.memorandum.MemorandumService;
import com.collection.service.oa.OfficeService;
import com.collection.service.purchase.PurchaseService;
import com.collection.service.starassess.StarAssessService;
import com.collection.service.storefront.StoreFrontService;
import com.collection.util.CookieUtil;
import com.collection.util.Md5Util;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;
import com.collection.util.SDKTestSendTemplateSMS;
import com.collection.util.UserUtil;

/**
 * 使用方后台管理企业简报
 * @author silence
 *
 */
@Controller
@RequestMapping("/managebackstage")
public class ManageCompanyController extends BaseController {
	private transient static Log log = LogFactory.getLog(ManageCompanyController.class);
	@Resource private SystemService systemService;
	
	@Resource private CompanyService companyService;
	
	@Resource private UserInfoService userInfoService;
	
	@Resource private IndexService indexService;
	
	@Resource private PurchaseService purchaseService;
	
	@Resource private OfficeService officeService;
	
	@Resource private StoreFrontService storeFrontService;
	
	@Resource private MemorandumService memorandumService;
	
	@Resource private StarAssessService starAssessService;
	
	/**
	 * 管理方后台公司信息
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/getCompanyList")
	public String getCompanyList(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> data=new HashMap<String, Object>();
		//初始化分页
		PageHelper pageHelper = new PageHelper(request);
		pageHelper.initPage(map);
		List<Map<String, Object>> companylist=this.companyService.getAllCompanyList(map);
		int num =this.companyService.getAllCompanyListNum(map);
		pageHelper.setTotalCount(num);
		model.addAttribute("companylist", companylist);
		model.addAttribute("pager", pageHelper.cateringPage().toString());
		model.addAttribute("map", map);
		
		return "/managebackstage/company/business_list";
	}
	/**
	 * 管理方导出企业信息
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/exportCompanyList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportCompanyList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/企业信息列表.xls");
		
		List<Map<String, Object>> companylist=this.companyService.getAllCompanyList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "公司名称", "人数/人", "联系人","联系电话","注册时间"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> data:companylist){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a", data.get("companyname")==null?"":data.get("companyname"));
	        datamap.put("b", data.get("usernum"));
	        datamap.put("c", data.get("contactperson")==null?"":data.get("contactperson"));
	        datamap.put("d", data.get("phone")==null?"":data.get("phone"));
	        datamap.put("e", data.get("createtime")==null?"":data.get("createtime"));
	        dataset.add(datamap);
        }
        try  
        {
            OutputStream out = new FileOutputStream(importurl);  
            ex.exportExcel(headers, dataset, out);
            out.close();  
            System.out.println("excel导出成功！");
            
            Map<String, Object> userInfo=UserUtil.getSystemUser(request);
            this.insertManageLog("", 1, "企业信息", "导出了企业信息", userInfo.get("userid")+"");
        }  
        catch (FileNotFoundException e)  
        {
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }
		return "企业信息列表.xls";
	}
	
	/**
	 * 公司信息
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/getCompanyInfo")
	public String getCompanyInfo(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> companyInfo=this.companyService.getCompanyInfo(map);
		
		model.addAttribute("companyInfo", companyInfo);
		return "/managebackstage/company/business_detail";
	}
	
	/**
	 * 重置用户密码
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/resetPass")
	@ResponseBody
	public Map<String, Object> resetPass(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		
		Map<String, Object> data=new HashMap<String, Object>();
		Map<String, Object> userInfo=this.userInfoService.getUserInfo(map);
		if(userInfo != null && userInfo.size()>0){
			String phone=userInfo.get("phone")+"";
			String code = "888888";
			// 发送验证码
			try {
				Random r = new Random();
				code = r.nextInt(10) + "" + r.nextInt(10) + "" + r.nextInt(10) + "" + r.nextInt(10)+ "" + r.nextInt(10)+ "" + r.nextInt(10);
				SDKTestSendTemplateSMS.sendSmsMessage(phone, userInfo.get("companyname")+"",userInfo.get("username")+"", code);
				data.put("status", 0);
				data.put("message", "重置成功");
				
				userInfo.put("password", Md5Util.getMD5(code));
				this.userInfoService.updateUserInfo(userInfo);
			} catch (Exception e) {
				data.put("status", 1);
				data.put("message", "重置失败，请稍候再试");
				return data;
			}
		}else{
			data.put("status", 1);
			data.put("message", "用户不存在");
		}
		return data;
	}
	
	/**
	 * 公司信息
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/getCompanyPower")
	public String getCompanyPower(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		
		Map<String, Object> companyInfo=this.companyService.getCompanyInfo(map);
		model.addAttribute("companyInfo", companyInfo);
		
		List<Map<String, Object>> powerlist=this.companyService.getPowerByCompany(map);
		model.addAttribute("powerlist", powerlist);
		return "/managebackstage/company/business_power";
	}

	/**
	 * 修改公司权限
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateCompanyPower")
	@ResponseBody
	public Map<String, Object> updateCompanyPower(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		//记录管理操作日志 
		if(map.containsKey("result") && String.valueOf(map.get("result")).equals("1")){
			insertManageLog("",1,"企业信息","同意公司"+map.get("companyname")+"申请的权限"+map.get("powername"),userInfo.get("userid")+"");
		}else{
			insertManageLog("",1,"企业信息","拒绝公司"+map.get("companyname")+"申请的权限"+map.get("powername"),userInfo.get("userid")+"");
		}
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("powerid") && map.get("powerid")!=null && !"".equals(map.get("powerid")) && map.get("powerid").toString().length()>3){
			map.put("powerid",map.get("powerid").toString().substring(0,3));
		}else{
			data.put("status", 1);
			data.put("message", "操作失败");
			return data;
		}
		try {
			
			map.put("status",1);

			map.put("updateid",userInfo.get("userid"));
			map.put("updatetime",new Date());
			this.companyService.updateCompanyPower(map);
			
			try {
				//查询公司信息
				Map<String,Object> comapanymap = this.companyService.getCompanyInfo(map);
				String phone = String.valueOf(comapanymap.get("phone"));
				String username = String.valueOf(comapanymap.get("createname"));
				String content = "";
				if(map.containsKey("result") && String.valueOf(map.get("result")).equals("1")){
					this.companyService.updatecompanyedition(map);
					content = "被餐饮大师审核通过了";
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("edition", 2);
					param.put("companyid", map.get("companyid"));
					this.companyService.updateCompany(param);
				}else{
					content = "被餐饮大师拒绝了";
				}
				SDKTestSendTemplateSMS.sendPowerApplyFeedBack(phone, username, content);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			data.put("status", 0);
			data.put("message", "操作成功");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "操作失败");
		}
		return data;
	}
	
	/**
	 * 修改公司信息
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateCompany")
	@ResponseBody
	public Map<String, Object> updateCompany(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> data=new HashMap<String, Object>();
		Map<String, Object> userInfo=UserUtil.getSystemUser(request);
		//记录管理操作日志 
		if(map.containsKey("status") && String.valueOf(map.get("status")).equals("1")){
			insertManageLog("",1,"企业信息","启用公司"+map.get("companyname"),userInfo.get("userid")+"");
		}else{
			insertManageLog("",1,"企业信息","禁用公司"+map.get("companyname"),userInfo.get("userid")+"");
		}
		
		try {
			map.put("updateid",userInfo.get("userid"));
			map.put("updatetime",new Date());
			this.companyService.updateCompany(map);
			
			data.put("status", 0);
			data.put("message", "操作成功");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "操作失败");
		}
		return data;
	}
	
	
	
	/**
	 * 
	 * 查询公司所有组织架构
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getOrganizeBySystem")
	@ResponseBody
	public Map<String, Object> getOrganizeBySystem(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		
		Map<String, Object> companymap=this.companyService.getCompanyInfo(map);
		Map<String, Object> organizemap=new HashMap<String, Object>();
		organizemap.put("companyid", map.get("companyid"));
		List<Map<String, Object>> list = this.indexService.getOrganizeList(organizemap);
		data.put("organizelist", list);
		data.put("companyname", companymap.get("companyname"));
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
		model.addAttribute("map", map);
		return "/managebackstage/company/business_structure";
	}
	
	/**
	 * 查询某公司的分享圈的信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/intoCompanySharePage")
	public String intoCompanySharePage(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> companyInfo=this.companyService.getCompanyInfo(map);
		request.setAttribute("companyInfo", companyInfo);
		request.setAttribute("map", map);
		return "/managebackstage/company/manageshare";
	}
	
	/**
	 * 查询公司的通讯录信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/intoCompanyMailListPage")
	public String intoCompanyMailListPage(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> companyInfo=this.companyService.getCompanyInfo(map);
		request.setAttribute("companyInfo", companyInfo);
		request.setAttribute("map", map);
		return "/managebackstage/company/managemaillist";
	}
	
	/**
	 * 查询企业通讯录信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getNextOrgainizenew")
	@ResponseBody
	public Map<String, Object> getNextOrgainizenew(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		
		PageHelper page = new PageHelper(request);
		int num = this.indexService.getUserListnewCount(map);
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> userlist=this.indexService.getUserListnew(map);
		
		if(userlist==null || userlist.size()==0){
			data.put("userlist", "");
		}else{
			data.put("userlist",userlist);
			data.put("pager", page.JSCateringPage().toString());
		}
		return data;
	}
	
	/**
	 * 查询公司的采购单信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getManagePurchaseOrder")
	public String getPurchaseOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		if(map.containsKey("status") && map.get("status").equals("")){
			map.remove("status");
		}
		PageHelper page = new PageHelper(request);
		int ordernum=this.purchaseService.getPurchaseListNum(map);
		page.setTotalCount(ordernum);
		page.initPage(map);
		List<Map<String, Object>> orderlist=this.purchaseService.getPurchaseList(map);
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("pager",page.cateringPage().toString());
		
		model.addAttribute("map", map);
		return "/managebackstage/company/manage_purchase_list";
	}
	
	/**
	 * 进入KPI排行管理页面
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/intoManageRankingListPage")
	public String intoManageRankingListPage(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> companyInfo=this.companyService.getCompanyInfo(map);
		request.setAttribute("companyInfo", companyInfo);
		model.addAttribute("map", map);
		return "/managebackstage/company/manage_ranking_list";
	}
	
	
	/**
	 * 
	 * 企业简报模块列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getManageCompanyModuleList")
	public String getManageCompanyModuleList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		PageHelper page = new PageHelper(request);
		int count = this.officeService.getCompanyModuleListCount(map);
		page.setTotalCount(count);
		page.initPage(map);
		List<Map<String, Object>> modulelist=this.officeService.getCompanyModuleList(map);
		model.addAttribute("modulelist", modulelist);
		model.addAttribute("pager",page.cateringPage().toString());
		model.addAttribute("map",map);
		
		return "/managebackstage/company/manage_oa_briefing";
	}
	
	/**
	 * 进入企业简报列表页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoBriefListPage")
	public String intoBriefListPage(@RequestParam Map<String,Object> map, HttpServletRequest request){
		request.setAttribute("map", map);
		return "/managebackstage/company/manage_oa_briefinglist";
	}
	
	/**
	 * 查询简报信息
	 * 传入参数：title，moduleid
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getManageBriefList")
	public Map<String,Object> getManageBriefList(@RequestParam Map<String,Object> map, HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			
			PageHelper page = new PageHelper(request);
			int count = this.officeService.getBriefListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.officeService.getBriefListInfo(map);
			
			resultMap.put("status", 0);
			resultMap.put("brieflist", list);
			resultMap.put("pager", page.JSCateringPage().toString());
			resultMap.put("map", map);
			
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 进入详情页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoManageBriefingDetialPage")
	public String intoManageBriefingDetialPage(@RequestParam Map<String,Object> map, HttpServletRequest request){
		Map<String,Object> briefmap = this.officeService.getBriefInfo(map);
		request.setAttribute("briefmap", briefmap);
		request.setAttribute("map", map);
		return "/managebackstage/company/manage_oa_briefingdetail";
	}
	
	
	/**
	 * 查询公司文件夹列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getManageCompanyCloudModuleList")
	public String getManageCompanyCloudModuleList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		try {
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
			request.setAttribute("map", map);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "/managebackstage/company/manage_oa_skydrive";
	}
	
	
	/**
	 * 进入文件页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoManageCompanyCloudFilePage")
	public String intoManageCompanyCloudFilePage(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("map", map);
		return "/managebackstage/company/manage_oa_skydrivelist";
	}
	
	/**
	 * 查询云盘文件
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getManageCompanyCloudFileList")
	public Map<String,Object> getManageCompanyCloudFileList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			PageHelper page = new PageHelper(request);
			int count = this.officeService.getCompanyCloudFileListCount(map);
			page.setTotalCount(count);
			page.initPage(map);
			List<Map<String,Object>> list = this.officeService.getCompanyCloudFileList(map);
			
			resultMap.put("filelist", list);
			resultMap.put("page", page.JSCateringPage().toString());
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	
	
	/**
	 * 进入详情页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoManageCloudFileDetail")
	public String intoManageCloudFileDetail(@RequestParam Map<String,Object> map , HttpServletRequest request){
		request.setAttribute("map", map);
		return "/managebackstage/company/manage_oa_skydrivedetail";
	}
	
	/**
	 * 查询文件详情
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getManageCompanyCloudFileInfo")
	public Map<String,Object> getCompanyCloudFileInfo(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			Map<String,Object> filemap = this.officeService.getCompanyCloudFileInfo(map);
			
			resultMap.put("status", 0);
			resultMap.put("filemap", filemap);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	
	/**
	 * 查询 顾客满意列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/getManageStoreEvaluateList")
	public String getStoreEvaluateList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		if(!map.containsKey("organizeid") || "".equals(map.get("organizeid"))){
			Map<String, Object> companyInfo=this.companyService.getCompanyInfo(map);
			map.put("organizename", companyInfo.get("companyname"));
		}
		
		PageHelper page = new PageHelper(request);
		int num=this.storeFrontService.getStoreEvaluateListNum(map);				
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.storeFrontService.getStoreEvaluateList(map);
		model.addAttribute("datalist", datalist);
		model.addAttribute("pager",page.cateringPage().toString());
		
		model.addAttribute("map", map);
		return "/managebackstage/company/manage_appraise_list";
	}
	
	/**
	 * 查询分享圈
	 * 传入参数{"organizeid":5}
	 * 传出参数{"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0,"sharelist":[{"content":"真是子啊","createtime":1468834092000,"companyid":"67702cc412264f4ea7d2c5f692070457","commentlist":[{"commentid":"1","content":"哈哈哈哈","createtime":1468834550000,"updatetime":1468834558000,"userid":"1","resourcetype":26,"createname":"李语然","resourceid":"bf15672191674a6a8f46ce0c4b885c3b"}],"createid":"690fb669ed4d40219964baad7783abd4","shareid":"bf15672191674a6a8f46ce0c4b885c3b","imagelist":[{"createtime":1468834092000,"companyid":"67702cc412264f4ea7d2c5f692070457","resourcetype":26,"fileid":"25d71f20fa2a410196cebe280cccd86c","resourceid":"bf15672191674a6a8f46ce0c4b885c3b","type":1,"visiturl":"sss"},{"createtime":1468834092000,"companyid":"67702cc412264f4ea7d2c5f692070457","resourcetype":26,"fileid":"5107f866f0ca40f5978dd722be925fa4","resourceid":"bf15672191674a6a8f46ce0c4b885c3b","type":1,"visiturl":"aaa"},{"createtime":1468834093000,"companyid":"67702cc412264f4ea7d2c5f692070457","resourcetype":26,"fileid":"bc1eaacae2d54738827311bed4e7ebcb","resourceid":"bf15672191674a6a8f46ce0c4b885c3b","type":1,"visiturl":"aaa"}],"createname":"silence","praiselist":[{"createtime":1468834714000,"updatetime":1468834715000,"createid":"1","shareid":"bf15672191674a6a8f46ce0c4b885c3b","ispraise":1,"createname":"李语然","praiseid":"1"}]}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getShareList")
	@ResponseBody
	public Map<String, Object> getShareList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		PageScroll page = new PageScroll();
		int num=this.memorandumService.getShareListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> sharelist=this.memorandumService.getShareList(map);
		data.put("num", num);
		data.put("sharelist", sharelist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 星值排行
	 * 传入参数{"year":2016,"monthnum":7,"organizeid":5,"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数{"userlist":[{"phone":"15000042335","allstar":9,"userid":"690fb669ed4d40219964baad7783abd4","overallstar":4,"selfstar":5,"realname":"silence","status":1}],"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getRankingList")
	@ResponseBody
	public Map<String, Object> getRankingList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//查询当前组织的datacode
		Map<String,Object> orgmap = this.indexService.getOrganizeInfo(map);
		map.remove("organizeid");
		map.put("datacode", orgmap.get("datacode"));
				
		PageHelper page = new PageHelper(request);
		int num=this.starAssessService.getUserStarListNum(map);		
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> userlist=this.starAssessService.getUserStarList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("userlist", userlist);
		data.put("status",0);
		data.put("pager",page.JSCateringPage().toString());
		return data;
	}
	
	/**
	 * 查询公司的日或人数
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getManageStatisticsToday")
	public String getStatisticsToday(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		if((!map.containsKey("starttime") || "".equals(map.get("starttime"))) && (!map.containsKey("endtime") || "".equals(map.get("endtime")))){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String stoptime = sdf.format(new Date());
			Calendar cal = Calendar.getInstance();
			Calendar cal1 = Calendar.getInstance();
			try {
				cal.setTime(sdf.parse(stoptime));
				cal1.setTime(sdf.parse(stoptime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cal.add(Calendar.DAY_OF_MONTH, -30);
			cal1.add(Calendar.DAY_OF_MONTH, 0);
			map.put("starttime",sdf.format(cal.getTime()));
			map.put("endtime",sdf.format(cal1.getTime()));
		}
		int num = this.companyService.getCompanyUsedUserNum(map);
		List<Map<String, Object>> usercountlist=this.userInfoService.getUserStatistics(map);
		model.addAttribute("usercountlist", usercountlist);
		model.addAttribute("count",num);
		model.addAttribute("map", map);
		
		return "/managebackstage/company/report/count_daily";
	}
}
