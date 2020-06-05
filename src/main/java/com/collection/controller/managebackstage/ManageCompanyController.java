package com.collection.controller.managebackstage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.controller.userbackstage.UserIndexController.ExportExcel;
import com.collection.service.CompanyService;
import com.collection.service.IndexService;
import com.collection.service.SystemService;
import com.collection.service.UserInfoService;
import com.collection.service.purchase.PurchaseService;
import com.collection.service.starassess.StarAssessService;
import com.collection.util.Md5Util;
import com.collection.util.PageHelper;
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
	
}
