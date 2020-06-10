package com.collection.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.redis.RedisUtil;
import com.collection.service.IndexService;
import com.collection.service.UserInfoService;
import com.collection.service.personal.PersonalService;
import com.collection.service.purchase.PurchaseService;
import com.collection.util.PageScroll;
import com.collection.util.baidujingweicity;
import com.collection.util.heweather;


/**
 * 
 * @author silence
 *
 */
@Controller
@RequestMapping("/app")
public class IndexController extends BaseController{
	@SuppressWarnings("unused")
	private transient static Log log = LogFactory.getLog(IndexController.class);
	
	@Resource private IndexService indexService;
	
	@Resource private PurchaseService purchaseService;
	
	@Resource private UserInfoService userInfoService;
	
	@Resource private PersonalService personalService;
	
	/**
	 * 未读提醒列表
	 * 传入参数{"userid":"690fb669ed4d40219964baad7783abd4"}
	 * 传出参数{"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"remindnum":1,"remindlist":[{"content":"鼠疫申请了一个申购单，请您审批","createtime":1467871827000,"companyid":"1","title":"审批","noticeid":"1","updatetime":1468414833000,"createid":"1","status":0,"updateid":"1","userid":"690fb669ed4d40219964baad7783abd4","linkurl":"http://www.baidu.com","delflag":0}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getRemindList")
	@ResponseBody
	public Map<String, Object> getRemindList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageScroll page = new PageScroll();
		
		int remindnum=this.indexService.getRemindnum(map);
		page.setTotalRecords(remindnum);
		page.initPage(map);
		List<Map<String, Object>> remindlist=this.indexService.getRemindList(map);
		
		try {
			//添加用户的使用日志
			if(map.containsKey("userid") && !"".equals(map.get("userid"))){
				Map<String,Object> usermap = this.personalService.getVerificationPwd(map);
				map.put("companyid", usermap.get("companyid"));
				this.indexService.insertUserUseLog(map);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("remindnum", remindnum);
		data.put("remindlist", remindlist);
		data.put("page",page);

		return data;
	}
	/**
	 * 首页banner列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","model":1}
	 * 传出参数[{"id":"1","model":1,"updatetime":1467882618000,"createid":"1","priority":1,"type":1,"delflag":0,"imgurl":"C:\\Users\\lenovo\\Desktop\\樱花\\9510812_132346230000_2.jpg","httpurl":"/upload/images/9510812_132346230000_2.jpg"}]
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getHomePageBanner")
	@ResponseBody
	public List<Map<String, Object>> getHomePageBanner(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		//model  1：首页 2：采购管理 3 仓库管理  4.工作表单 5.OA办公 6.店面管理 7.KPI星级管理
		List<Map<String, Object>> bannerlist=this.indexService.getHomePageBanner(map);
		return bannerlist;
	}
	
	/**
	 * 子页面banner列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","model":2}
	 * 传出参数[{"id":"4","model":2,"updatetime":1468415250000,"createid":"1","priority":1,"type":2,"delflag":0,"imgurl":"C:\\Users\\lenovo\\Desktop\\樱花\\41215_132821014_2.jpg","httpurl":"/upload/images/41215_132821014_2.jpg"}]
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getBannerList")
	@ResponseBody
	public List<Map<String, Object>> getBannerList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		//model  1：首页 2：采购管理 3 仓库管理  4.工作表单 5.OA办公 6.店面管理 7.KPI星级管理	
		List<Map<String, Object>> bannerlist=this.indexService.getBannerList(map);
		return bannerlist;
	}
	/**
	 * 查询
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","receiveid":"690fb669ed4d40219964baad7783abd4"}
	 * 传出参数{"purchasenum":0,"warehousenum":0,"OAofficenum":0,"starassessnum":0,"worksheetnum":0,"storefrontnum":0}
	 * 	采购管理未读数   （申购单，采购(入库)单）
	 * 	仓库管理未读数	（用料单，退货单，报损单 ）
	 * 	工作表单未读数 （处罚单，奖励单，报修单，菜品成品单 ，离店检查，餐前检查，厨房检查）
	 * 	OA办公未读数量 （通知 ，日志，通用审批，请示，报销，请假，任务，备用金）
	 * 	店面管理未读数量 （每日报表，巡店日志）
	 * 	KPI星级审核  （岗位星值自评，  综合星值自评）
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getHomeNotreadNum")
	@ResponseBody
	public Map<String, Object> getHomeNotreadNum(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		map.put("isread", 0);
		Map<String, Object> data=new HashMap<String, Object>();
		//采购
		List<Integer> resourcetypes=new ArrayList<Integer>();
		resourcetypes.add(1);
		resourcetypes.add(2);
		map.put("resourcetypes", resourcetypes);
		int purchasenum=this.indexService.getForwordNotreadNum(map);
		data.put("purchasenum", purchasenum);
		
		//仓库
		resourcetypes=new ArrayList<Integer>();
		resourcetypes.add(3);
		resourcetypes.add(4);
		resourcetypes.add(5);
		map.put("resourcetypes", resourcetypes);
		int warehousenum=this.indexService.getForwordNotreadNum(map);
		data.put("warehousenum", warehousenum);
		
		//工作表单
		resourcetypes=new ArrayList<Integer>();
		resourcetypes.add(8);
		resourcetypes.add(9);
		resourcetypes.add(10);
		resourcetypes.add(11);
		resourcetypes.add(12);
		resourcetypes.add(13);
		resourcetypes.add(14);
		List<Integer> rangetypes=new ArrayList<Integer>();
		rangetypes.add(2);
		rangetypes.add(3);
		map.put("resourcetypes", resourcetypes);
		map.put("rangetypes", rangetypes);
		int worksheetnum=this.indexService.getForwordNotreadNum(map);
		data.put("worksheetnum", worksheetnum);
		map.remove("rangetypes");
		//OA办公
		resourcetypes=new ArrayList<Integer>();
		resourcetypes.add(15);
		resourcetypes.add(16);
		resourcetypes.add(17);
		resourcetypes.add(18);
		resourcetypes.add(19);
		
		resourcetypes.add(20);
		resourcetypes.add(23);
		resourcetypes.add(24);
		resourcetypes.add(25);
		resourcetypes.add(27);
		rangetypes=new ArrayList<Integer>();
		rangetypes.add(1);
		rangetypes.add(4);
		rangetypes.add(5);
		rangetypes.add(6);
		//rangetypes.add(7);
		map.put("rangetypes", rangetypes);
		map.put("resourcetypes", resourcetypes);
		int OAofficenum=this.indexService.getForwordNotreadNum(map);
		map.remove("rangetypes");
		
		rangetypes=new ArrayList<Integer>();
		rangetypes.add(7);
		map.put("rangetypes", rangetypes);
		int jianbao = this.indexService.getRangeNotreadNum(map);
		map.remove("rangetypes");
		data.put("OAofficenum", OAofficenum+jianbao);
		
		//店面
		resourcetypes=new ArrayList<Integer>();
		resourcetypes.add(21);
		resourcetypes.add(22);
		map.put("resourcetypes", resourcetypes);
		int storefrontnum=this.indexService.getForwordNotreadNum(map);
		data.put("storefrontnum", storefrontnum);
		map.remove("rangetypes");
		
		//KPI星级审核
		resourcetypes=new ArrayList<Integer>();
		resourcetypes.add(6);
		resourcetypes.add(7);
		
		map.put("resourcetypes", resourcetypes);
		int starassessnum=this.indexService.getForwordNotreadNum(map);
		data.put("starassessnum", starassessnum);
		map.remove("rangetypes");
		
		return data;
	}
	
	
	
	
	/**
	 * 查询组织下级及人员 （无权限查询地址）
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","organizeid":1}  （查询第一级时可不传organizeid）
	 * 传出参数{"userlist":[{"phone":"15000042335","sex":"1","updatetime":1468459805000,"identityname":"店员","userid":"1","password":"7cb49971f4b0c96f0c10bcc40355a167","isshowphone":1,"createtime":1468459793000,"companyid":"67702cc412264f4ea7d2c5f692070457","username":"15000042335","email":"lucky_markli@sina.com","isfristlogin":1,"realname":"李语然","status":1,"identitytype":2,"delflag":0}],"organizelist":[{"createtime":1468459112000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459279000,"datacode":"001001","address":"","organizeid":"2","organizename":"浦东新区分公司","parentid":"1","type":1},{"createtime":1468459159000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459212000,"datacode":"001002","address":"徐汇区","organizeid":"3","organizename":"徐汇区分公司","parentid":"1","type":1}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getNextOrgainize")
	@ResponseBody
	public Map<String, Object> getNextOrgainize(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		if(!map.containsKey("organizeid") || "".equals(map.get("organizeid"))){
			map.put("iscompany", 1);
		}
		if(map.get("isnopower") == "1" || "1".equals(map.get("isnopower"))){
			data.put("userlist", "");
		}else{
			List<Map<String, Object>> userlist=this.indexService.getUserList(map);
			if(userlist==null || userlist.size()==0){
				data.put("userlist", "");
			}else{
				data.put("userlist", userlist);
			}
		}
		
		List<Map<String, Object>> organizelist=this.indexService.getNextOrgainizePurchase(map);
		if(organizelist==null || organizelist.size()==0){
			data.put("organizelist", "");
		}else{
			data.put("organizelist", organizelist);
		}
		return data;
	}
	
	@RequestMapping("/getNextOrgainizenew")
	@ResponseBody
	public Map<String, Object> getNextOrgainizenew(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		List<Map<String, Object>> userlist=this.indexService.getUserListnew(map);
		if(userlist==null || userlist.size()==0){
			data.put("userlist", "");
		}else{
			List<Map<String,Object>> nonelist=new ArrayList<Map<String,Object>>();
			
			//添加空字母的项
			Map<String,Object> nonemap=new HashMap<String,Object>();
			List<Map<String,Object>> nonetwolist=new ArrayList<Map<String,Object>>();
			for(Map<String,Object> m:userlist){
				if(m.get("english")==null || "".equals(m.get("english"))){
					Map<String,Object> nonetwomap=new HashMap<String,Object>();
					nonetwomap.put("english","empty");
					nonetwomap.put("realname",m.get("realname"));
					nonetwomap.put("phone",m.get("phone"));
					nonetwomap.put("userid",m.get("userid"));
					nonetwolist.add(nonetwomap);
				}
			}
			nonemap.put("english","empty");
			nonemap.put("realnamelist",nonetwolist);
			nonelist.add(nonemap);
			//循环添加字母项
			String[] arybig={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
			String[] arysort={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
			int len=arybig.length;
			for(int i=0;i<len;i++){
				Map<String,Object> 	onemap=new HashMap<String,Object>();
				onemap.put("english",arybig[i]);
				List<Map<String,Object>> onelist=new ArrayList<Map<String,Object>>();
				for(Map<String,Object> m:userlist){
					if(arybig[i].equals(m.get("english")) || arysort[i].equals(m.get("english"))){
						onelist.add(m);
					}
				}
				onemap.put("realnamelist",onelist);
				nonelist.add(onemap);
			}
			data.put("userlist",nonelist);
		}
		return data;
	}
	
	/**
	 * 采购管理、每日统计、仓库个历  统计切换店面 查询
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","organizeid":1,userid:"1"}  （查询第一级时可不传organizeid）
	 * 传出参数{"userlist":[{"phone":"15000042335","sex":"1","updatetime":1468459805000,"identityname":"店员","userid":"1","password":"7cb49971f4b0c96f0c10bcc40355a167","isshowphone":1,"createtime":1468459793000,"companyid":"67702cc412264f4ea7d2c5f692070457","username":"15000042335","email":"lucky_markli@sina.com","isfristlogin":1,"realname":"李语然","status":1,"identitytype":2,"delflag":0}],"organizelist":[{"createtime":1468459112000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459279000,"datacode":"001001","address":"","organizeid":"2","organizename":"浦东新区分公司","parentid":"1","type":1},{"createtime":1468459159000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459212000,"datacode":"001002","address":"徐汇区","organizeid":"3","organizename":"徐汇区分公司","parentid":"1","type":1}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getNextOrgainizePurchase")
	@ResponseBody
	public Map<String, Object> getNextOrgainizePurchase(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		if(!map.containsKey("organizeid") || "".equals(map.get("organizeid"))){
			map.put("iscompany", 1);
		}
		List<Map<String, Object>> organizelist=this.indexService.getNextOrgainizePurchase(map);
		if(organizelist==null || organizelist.size()==0){
			data.put("organizelist", "");
		}else{
			data.put("organizelist", organizelist);
		}
		return data;
	}
	
	/**
	 * 查询企业架构中的组织信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getStructureMangeOrganizeList")
	public Map<String,Object> getStructureMangeOrganizeList(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String,Object>> organizelist = this.indexService.getUserManageStucture(map);
		if(organizelist==null || organizelist.size()==0){
			resultMap.put("organizelist", "");
		}else{
			resultMap.put("organizelist", organizelist);
		}
		return resultMap;
	}
	
	/**
	 * 查询组织下级及人员禁用
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","organizeid":1,userid:"1"}  （查询第一级时可不传organizeid）
	 * 传出参数{"userlist":[{"phone":"15000042335","sex":"1","updatetime":1468459805000,"identityname":"店员","userid":"1","password":"7cb49971f4b0c96f0c10bcc40355a167","isshowphone":1,"createtime":1468459793000,"companyid":"67702cc412264f4ea7d2c5f692070457","username":"15000042335","email":"lucky_markli@sina.com","isfristlogin":1,"realname":"李语然","status":1,"identitytype":2,"delflag":0}],"organizelist":[{"createtime":1468459112000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459279000,"datacode":"001001","address":"","organizeid":"2","organizename":"浦东新区分公司","parentid":"1","type":1},{"createtime":1468459159000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459212000,"datacode":"001002","address":"徐汇区","organizeid":"3","organizename":"徐汇区分公司","parentid":"1","type":1}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getNextOrgainizePurchaseUser")
	@ResponseBody
	public Map<String, Object> getNextOrgainizePurchaseUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		map.put("disabled", "");
		List<Map<String, Object>> organizelist=this.indexService.getUserManageStucture(map);
		if(organizelist==null || organizelist.size()==0){
			data.put("organizelist", "");
		}else{
			data.put("organizelist", organizelist);
		}
		return data;
	}
	
	/**
	 * 查询组织下级及人员禁用
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","organizeid":1}  （查询第一级时可不传organizeid）
	 * 传出参数{"userlist":[{"phone":"15000042335","sex":"1","updatetime":1468459805000,"identityname":"店员","userid":"1","password":"7cb49971f4b0c96f0c10bcc40355a167","isshowphone":1,"createtime":1468459793000,"companyid":"67702cc412264f4ea7d2c5f692070457","username":"15000042335","email":"lucky_markli@sina.com","isfristlogin":1,"realname":"李语然","status":1,"identitytype":2,"delflag":0}],"organizelist":[{"createtime":1468459112000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459279000,"datacode":"001001","address":"","organizeid":"2","organizename":"浦东新区分公司","parentid":"1","type":1},{"createtime":1468459159000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459212000,"datacode":"001002","address":"徐汇区","organizeid":"3","organizename":"徐汇区分公司","parentid":"1","type":1}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getNextOrgainizeDisabled")
	@ResponseBody
	public Map<String, Object> getNextOrgainizeDisabled(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		if(!map.containsKey("organizeid") || "".equals(map.get("organizeid"))){
			map.put("iscompany", 1);
		}else{
			List<Map<String, Object>> userlist=this.indexService.getUserListDisabled(map);
			if(userlist==null || userlist.size()==0){
				data.put("userlist", "");
			}else{
				data.put("userlist", userlist);
			}
		}
		List<Map<String, Object>> organizelist=this.indexService.getOrganizeList(map);
		if(organizelist==null || organizelist.size()==0){
			data.put("organizelist", "");
		}else{
			data.put("organizelist", organizelist);
		}
		return data;
	}
	
	
	/**
	 * 模糊查询当前公司的人
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","realname":"李白"}  （查询第一级时可不传organizeid）
	 * 传出参数{"userlist":[{"phone":"15000042335","sex":"1","updatetime":1468459805000,"identityname":"店员","userid":"1","password":"7cb49971f4b0c96f0c10bcc40355a167","isshowphone":1,"createtime":1468459793000,"companyid":"67702cc412264f4ea7d2c5f692070457","username":"15000042335","email":"lucky_markli@sina.com","isfristlogin":1,"realname":"李语然","status":1,"identitytype":2,"delflag":0}],"organizelist":[{"createtime":1468459112000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459279000,"datacode":"001001","address":"","organizeid":"2","organizename":"浦东新区分公司","parentid":"1","type":1},{"createtime":1468459159000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459212000,"datacode":"001002","address":"徐汇区","organizeid":"3","organizename":"徐汇区分公司","parentid":"1","type":1}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getUserBySearch")
	@ResponseBody
	public Map<String, Object> getUserBySearch(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		List<Map<String, Object>> userlist=this.indexService.getUserBySearch(map);
		if(userlist==null || userlist.size()==0){
			data.put("userlist", "");
		}else{
			data.put("userlist", userlist);
		}
		data.put("status", 0);
		return data;
	}
	/**
	 * 模糊查询当前公司禁用的人
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","realname":"李白"}  （查询第一级时可不传organizeid）
	 * 传出参数{"userlist":[{"phone":"15000042335","sex":"1","updatetime":1468459805000,"identityname":"店员","userid":"1","password":"7cb49971f4b0c96f0c10bcc40355a167","isshowphone":1,"createtime":1468459793000,"companyid":"67702cc412264f4ea7d2c5f692070457","username":"15000042335","email":"lucky_markli@sina.com","isfristlogin":1,"realname":"李语然","status":1,"identitytype":2,"delflag":0}],"organizelist":[{"createtime":1468459112000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459279000,"datacode":"001001","address":"","organizeid":"2","organizename":"浦东新区分公司","parentid":"1","type":1},{"createtime":1468459159000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459212000,"datacode":"001002","address":"徐汇区","organizeid":"3","organizename":"徐汇区分公司","parentid":"1","type":1}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getUserBySearchDisabled")
	@ResponseBody
	public Map<String, Object> getUserBySearchDisabled(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		List<Map<String, Object>> userlist=this.indexService.getUserBySearchDisabled(map);
		if(userlist==null || userlist.size()==0){
			data.put("userlist", "");
		}else{
			data.put("userlist", userlist);
		}
		data.put("status", 0);
		return data;
	}
	/**
	 * 模糊查询当前公司的组织
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","organizename":"李白","type":3}  
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getOrganizeBySearch")
	@ResponseBody
	public Map<String, Object> getOrganizeBySearch(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		List<Map<String, Object>> organizelist=this.indexService.getOrganizeBySearch(map);
		if(organizelist==null || organizelist.size()==0){
			data.put("organizelist", "");
		}else{
			data.put("organizelist", organizelist);
		}
		data.put("status", 0);
		return data;
	}
	
	
	/**
	 * 模糊查询当前公司的组织
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","organizename":"李白","type":3}  
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getOrganizeUserBySearch")
	@ResponseBody
	public Map<String, Object> getOrganizeUserBySearch(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		List<Map<String, Object>> userlist=this.indexService.getUserList(map);
		if(userlist==null || userlist.size()==0){
			data.put("userlist", "");
		}else{
			data.put("userlist", userlist);
		}
		List<Map<String, Object>> organizelist=this.indexService.getOrganizeList(map);
		if(organizelist==null || organizelist.size()==0){
			data.put("organizelist", "");
		}else{
			data.put("organizelist", organizelist);
		}
		data.put("status", 0);
		return data;
	}
	
	
	
	/**
	 * 查询当前用户所在的所有店面
	 * 传入参数{"userid":"690fb669ed4d40219964baad7783abd4",companyid}
	 * 传出参数{"message":"查询成功","status":0,"shoplist":[{"createtime":1468459278000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459284000,"datacode":"001001001001","address":"浦东新区新金桥路","organizeid":"5","organizename":"浦东金桥分店","parentid":"4","type":3}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getShopListByUser")
	@ResponseBody
	public Map<String, Object> getShopListByUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("userid") && !"".equals(map.get("userid"))){
			List<Map<String, Object>> organizelist=this.indexService.getOrganizeListByUser(map);
			
			List<Map<String, Object>> shoplist=new ArrayList<Map<String,Object>>();
			for(Map<String, Object> shop:organizelist){
				if("3".equals(String.valueOf(shop.get("type")))){
					shoplist.add(shop);
				}
			}
			data.put("status", 0);
			data.put("message", "查询成功");
			if(shoplist==null || shoplist.size()==0){
				data.put("shoplist", "");
			}else{
				data.put("shoplist", shoplist);
			}
		}else{
			data.put("status", 1);
			data.put("message", "传入参数错误");
		}
		return data;
	}
	

	/**
	 * 查询当前用户所在的区域以及下级的组织架构
	 * 传入参数{"userid":"690fb669ed4d40219964baad7783abd4"}
	 * 传出参数{"message":"查询成功","status":0,"organizelist":[{"createtime":1468459278000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459284000,"datacode":"001001001001","address":"浦东新区新金桥路","organizeid":"5","organizename":"浦东金桥分店","parentid":"4","type":3}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getOrganizeListByUser")
	@ResponseBody
	public Map<String, Object> getOrganizeListByUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("userid") && !"".equals(map.get("userid"))){
			List<Map<String, Object>> organizelist=this.indexService.getOrganizeListByUser(map);
			data.put("status", 0);
			data.put("message", "查询成功");
			if(organizelist==null || organizelist.size()==0){
				data.put("organizelist", "");
			}else{
				data.put("organizelist", organizelist);
			}
		}else{
			data.put("status", 1);
			data.put("message", "传入参数错误");
		}
		return data;
	}
	
	/**
	 * 查询当前用户所在的区域以及下级的组织架构
	 * 传入参数{"userid":"690fb669ed4d40219964baad7783abd4"}
	 * 传出参数{"message":"查询成功","status":0,"organizelist":[{"createtime":1468459278000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459284000,"datacode":"001001001001","address":"浦东新区新金桥路","organizeid":"5","organizename":"浦东金桥分店","parentid":"4","type":3}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getOrganizeByUser")
	@ResponseBody
	public Map<String, Object> getOrganizeByUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("userid") && !"".equals(map.get("userid"))){
			List<Map<String, Object>> organizelist=this.indexService.getOrganizeByUser(map);
			data.put("status", 0);
			data.put("message", "查询成功");
			if(organizelist==null || organizelist.size()==0){
				data.put("organizelist", "");
			}else{
				data.put("organizelist", organizelist);
			}
		}else{
			data.put("status", 1);
			data.put("message", "传入参数错误");
		}
		return data;
	}
	/**
	 * 查询评论
	 * 传入参数{"orderid":"4f9f463c50ea4267aef0efb619d4403c","resourcetype":2}
	 * 传出参数{"num":1,"commentlist":[{"commentid":"ebee4a6166894fc195c4e8efd8ee905f","content":"是是是","createtime":"2016-07-26 13:37:27","headimage":"../appcssjs/images/index/banner_img.png","userid":"690fb669ed4d40219964baad7783abd4","resourcetype":2,"realname":"silence","resourceid":"4f9f463c50ea4267aef0efb619d4403c","replyname":""}],"status":0,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1}}
	 * 
	 * resourcetype
	 * 评论来源类型（1：采购(入库)单 2：申购单3.用料单 4.退货单 5.报损单 6.岗位星值 7.综合星级 8.处罚单 9.奖励单 10.离店检查 11.餐前检查 12.厨房检查 13.报修单 14.菜品成平管理 15.通用审批 16.请示 17 报销 18.请假 19.任务 20 备用金申请,21 每日报表 22.巡店日志, 23:日报 24周报 25 月报，26 分享圈）
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getOrderComment")
	@ResponseBody
	public Map<String, Object> getOrderComment(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		PageScroll page = new PageScroll();
		int num=this.purchaseService.getPurchaseCommentListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> commentlist=this.purchaseService.getPurchaseCommentList(map);
		
		data.put("num", num);
		data.put("commentlist", commentlist);
		data.put("page",page);
		data.put("status", 0);
		return data;
	}
	
	
	/**
	 * 添加评论
	 * 传入参数{"userid":1,"content":"测试","resourceid":"d460f29996694dd383264395bf408cb2","resourcetype":2,"parentid":"6d57ae32c44749b58f151e47c1c36b7c"}
	 * 传出参数{"message":"添加成功","status":0}
	 * resourcetype
	 * 评论来源类型（1：采购(入库)单 2：申购单3.用料单 4.退货单 5.报损单 6.岗位星值 7.综合星级 8.处罚单 9.奖励单 10.离店检查 11.餐前检查 12.厨房检查 13.报修单 14.菜品成平管理 15.通用审批 16.请示 17 报销 18.请假 19.任务 20 备用金申请,21 每日报表 22.巡店日志, 23:日报 24周报 25 月报，26 分享圈）
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertComment")
	@ResponseBody
	public Map<String, Object> insertComment(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		this.purchaseService.insertComment(map);
		data.put("status", 0);
		data.put("message", "添加成功");
		return data;
	}

	/**
	 * 转发
	 * 
	 * companyid  resourceid  receiveid  createid resourcetype
	 * resourcetype
	 * 来源类型（1：采购(入库)单 2：申购单3.用料单 4.退货单 5.报损单 6.岗位星值 7.综合星级 8.处罚单 9.奖励单 10.离店检查 11.餐前检查 12.厨房检查 13.报修单 14.菜品成平管理 15.通用审批 16.请示 17 报销 18.请假 19.任务 20 备用金申请,21 每日报表 22.巡店日志, 23:日报 24周报 25 月报）
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/forward")
	@ResponseBody
	public Map<String, Object> forward(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			String receiveids = String.valueOf(map.get("receiveids"));
			if(map.containsKey("receiveids") && !"".equals(receiveids)){
				String [] receives = receiveids.split(",");
				for(String id : receives){
					map.put("receiveid", id);
					this.purchaseService.insertForword(map);
				}
			}
			data.put("status", 0);
			data.put("message", "添加成功");
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "转发错误");
			return data;
		}
	}
	
	/**
	 * 已经不用此接口了 用updateIsread 代替
	 * 转发表/发布范围表 标记为已读 data 为转发表id 或者发布范围表id 或者 协办人id
	 * 传入参数{"dataid":2,"isread":1}
	 * 传出参数{"message":"标记已读成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updateReadStatus")
	@ResponseBody
	public Map<String, Object> readForword(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("dataid") && !"".equals(map.get("dataid"))){
			this.indexService.updateForwardRangeReadStatus(map);
			data.put("status", 0);
			data.put("message", "标记已读成功");
		}else{
			data.put("status", 1);
			data.put("message", "传入参数不正确");
		}
		return data;
	}
	
	/**
	 * 标记为未读/或者已读    data 为转发表id 或者发布范围表id 或者 协办人id
	 * 传入参数{"receiveid":2,"isread":0,"resourceid":"4111215"}
	 * 传出参数{"message":"标记未读成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/updateIsread")
	@ResponseBody
	public Map<String, Object> updateIsread(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("resourceid") && !"".equals(map.get("resourceid")) && map.containsKey("receiveid") && !"".equals(map.get("receiveid"))){
			this.indexService.updateIsread(map);
			data.put("status", 0);
			data.put("message", "标记成功");
		}else{
			data.put("status", 1);
			data.put("message", "传入参数不正确");
		}
		return data;
	}

	
	/**
	 * 得到区域列表
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getAreaList")
	@ResponseBody
	public Map<String, Object> getAreaList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		List<Map<String,Object>> dataList = this.indexService.getCityList(map);
		data.put("dataList", dataList);
		return data;
	}
	
	/**
	 * 得到根据经纬查询城市列表
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getcity")
	@ResponseBody
	public Map<String, Object> getcity(HttpServletRequest request,@RequestParam Map<String, Object> map, Model model
			,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> cityInfo=new HashMap<String, Object>();
		if(map.get("lat")!=null && !map.get("lat").equals("") && map.get("lng") !=null && !map.get("lng").equals("")){
			baidujingweicity city=new baidujingweicity();
			String cityname=city.getcity(map.get("lat")+"", map.get("lng")+"");
			cityInfo.put("cityname", cityname);
			RedisUtil.setObject("cityInfo"+map.get("userid"), cityInfo, 4);
			
			cityInfo=this.indexService.getCityId(cityInfo);
			
			Map<String, Object> weathermap=new HashMap<String, Object>();
			if(cityInfo!= null && cityInfo.size()>0){
				heweather hewe=new heweather();
				String weathername=hewe.getweathername(cityInfo.get("id")+"");
				String temperature=hewe.getTemperature(cityInfo.get("id")+"");
				weathermap.put("weathername", weathername);
				weathermap.put("temperature", temperature);
				weathermap.put("cityname", cityInfo.get("city"));
				
				String weatherurl=this.indexService.getWeatherUrl(weathername);
				weathermap.put("weatherurl", weatherurl);
				
				SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
				weathermap.put("week", dateFm.format(new Date()));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				weathermap.put("datetime", sdf.format(new Date()));
				cityInfo.put("weathermap", weathermap);
			}
		}
		return cityInfo;
	}
	
	/**
	 * 根据城市查询天气
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getWeather")
	@ResponseBody
	public Map<String, Object> getWeather(HttpServletRequest request,@RequestParam Map<String, Object> map, Model model
			,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("weathermap", "");
		
		Map<String, Object> cityInfo=RedisUtil.getObject("cityInfo"+map.get("userid"));
		if(cityInfo!= null && cityInfo.size()>0){
			cityInfo=this.indexService.getCityId(cityInfo);
			if(cityInfo!= null && cityInfo.size()>0){
				heweather hewe=new heweather();
				Map<String, Object> weathermap=new HashMap<String, Object>();
				String weathername=hewe.getweathername(cityInfo.get("id")+"");
				String temperature=hewe.getTemperature(cityInfo.get("id")+"");
				weathermap.put("weathername", weathername);
				weathermap.put("temperature", temperature);
				weathermap.put("cityname", cityInfo.get("city"));
				
				String weatherurl=this.indexService.getWeatherUrl(weathername);
				weathermap.put("weatherurl", weatherurl);
				
				SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
				weathermap.put("week", dateFm.format(new Date()));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				weathermap.put("datetime", sdf.format(new Date()));
				data.put("status", 0);
				data.put("weathermap", weathermap);
			}
		}else{
			data.put("status", 1);
			data.put("message", "城市信息已过期");
		}
		return data;
	}
	/**
	 * 查询 组织名称
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getOrganizeName")
	@ResponseBody
	public Map<String, Object> getOrganizeName(HttpServletRequest request,@RequestParam Map<String, Object> map, Model model
			,HttpServletResponse response) {
		Map<String, Object> organizemap=this.indexService.getOrganizeInfo(map);
		return organizemap;
		
	}
	
	/**
	 * 改变是否已读的状态
	 * @param request
	 * @param response
	 * @return
	 * @author silence
	 */
	@RequestMapping("/changeIsReadAll")
	@ResponseBody
	public Map<String, Object> changeIsReadAll(HttpServletRequest request,@RequestParam Map<String, Object> map, Model model
			,HttpServletResponse response) {
		
		this.indexService.updateIsreadAll(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("status", 0);
		return data;
		
	}
	
	
	/**
	 * 根据发布范围查询组织架构店面
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getShopListByReleaseRange")
	public Map<String,Object> getShopListByReleaseRange(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			List<Map<String,Object>> list = this.indexService.getShopListByReleaseRange(map);
			resultMap.put("shoplist", list);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 根据发布范围查询组织架构信息
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getOrganizeListByReleaseRange")
	public Map<String,Object> getOrganizeListByReleaseRange(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			List<Map<String,Object>> list = this.indexService.getOrganizeListByReleaseRange(map);
			resultMap.put("organizelist", list);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	
	
	
	/**
	 * 推送审核信息
	 * @param request
	 * @param response
	 * @return
	 * @author silence
	 */
	@RequestMapping("/pushMessage")
	@ResponseBody
	public Map<String, Object> pushMessage(HttpServletRequest request,@RequestParam Map<String, Object> map, Model model
			,HttpServletResponse response) {
		//推送信息
		try {
			String userid=map.get("userid")+"";
			String title=map.get("title")+"";
			String url=map.get("url")+"";
			JPushAndriodAndIosMessage(userid, title, url);
		} catch (Exception e) {
			// TODO: handle exception
		}
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("status", 0);
		return data;
		
	}
	
/*
	@RequestMapping("/phonegapupload")
	@ResponseBody
	public void phonegapupload(HttpServletRequest request,@RequestParam Map<String, Object> map, Model model
			,HttpServletResponse response) {
		
		response.setContentType("text/html;charset=UTF-8");
		File file1 = null;
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, Object>> fileList  = new ArrayList<Map<String,Object>>();
		params.put("orgid", request.getAttribute("orgid"));
		try {
			List<FileItem> list = upload.parseRequest(request);
			// 获取参数
			for (FileItem fileItem : list) {
				if (fileItem.isFormField()) {
					try {
						try {
							params.put(fileItem.getFieldName(),
									fileItem.getString("UTF-8"));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}

				}
			}
			// 获取上传文件
			for (FileItem fileItem : list) {
				if (!fileItem.isFormField()) {
					File remoteFile;
					try {
						remoteFile = new File(new String(fileItem.getName()
								.getBytes(), "UTF-8"));
						String fileDirectory = "";
						if (params.get("FileDirectory") != null) {
							fileDirectory = "/"
									+ params.get("FileDirectory").toString()+"/";
						}else{
							fileDirectory = "/";
						}
						String fileName = "";
						if (remoteFile.getName().lastIndexOf(".") >= 0) {
							fileName = UUID.randomUUID().toString()
									.replace("-", "")
									+ remoteFile.getName().substring(
											remoteFile.getName().lastIndexOf("."));
						}else{
							fileName = UUID.randomUUID().toString()
									.replace("-", "");
						}
						file1 = new File(request.getSession().getServletContext().getRealPath(
								"fileupload" + fileDirectory), fileName);
						file1.getParentFile().mkdirs();
						file1.createNewFile();
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
						Map<String, Object> fileMap = new HashMap<String, Object>();
						String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getSession().getServletContext().getContextPath();
						fileMap.put("httpUrl", basePath+"/fileupload" + fileDirectory+fileName);
						fileMap.put("webUrl", basePath);
						fileMap.put("fileName", fileItem.getName());
						fileMap.put("fileSize", fileItem.getSize());
						fileMap.put("contentType", fileItem.getContentType());
						fileList.add(fileMap);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (FileUploadException e) {

		}
		
		try {
			PrintWriter out = response.getWriter();
			out.print(JSONArray.fromObject(fileList));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
	
	public static void main(String[] args) {
		String userid="690fb669ed4d40219964baad7783abd4";
		String title="测试一下推送";
		String url="/purchase/apply_list0.html";
		//JPushAliaseUtil.PushUrlByAliase(userid, title, url);
	}
	
	
	
	@RequestMapping("/getPromptInfo")
	@ResponseBody
	public Map<String, Object> getPromptInfo(HttpServletRequest request,@RequestParam Map<String, Object> map, Model model
			,HttpServletResponse response) {
		
		Map<String, Object> datamap=this.indexService.getDataList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("status", 0);
		data.put("datamap", datamap);
		return data;
		
	}
	
	/**
	 * 根据每日报表发布范围查询组织架构店面
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getOrganizeListByDailyReport")
	public Map<String,Object> getOrganizeListByDailyReport(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			List<Map<String,Object>> list = this.indexService.getOrganizeListByDailyReport(map);
			resultMap.put("shoplist", list);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	
	/**
	 * 查询我所属的组织
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getOrganizelist")
	public Map<String,Object> getOrganizelist(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			List<Map<String,Object>> list = this.indexService.getOrganizeByUser(map);
			resultMap.put("organizelist", list);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	
	
	/**
	 * 获取版本号
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getResourceVersion")
	@ResponseBody
	public Map<String,Object> getResourceVersion(@RequestParam Map<String,Object> map , HttpServletRequest request){

		Map<String, Object> data=new HashMap<String, Object>();
		String versionnum=this.indexService.getResourceVersion();
		data.put("version", versionnum);
		return data;
	}
	
	/**
	 * 获取启动页、引导页当前版本号及对应内容
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getLaunched")
	@ResponseBody
	public Map<String,Object> getLaunched(@RequestParam("version") int version, HttpServletRequest request){
		try {
			return this.indexService.getLaunchedVersion(version);
		} catch (Exception e) {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("success", false);
			returnMap.put("version", version);
			returnMap.put("errormsg", e.toString());
			return returnMap;
		}
	}
	
	/**
	 * 获取底部菜单当前版本号及对应内容
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getBottomMenu")
	@ResponseBody
	public Map<String,Object> getBottomMenu(@RequestParam("version") int version, HttpServletRequest request){
		try {
			return this.indexService.getBottomMenuVersion(version);
		} catch (Exception e) {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("success", false);
			returnMap.put("version", version);
			returnMap.put("errormsg", e.toString());
			return returnMap;
		}
	}
	
	/**
	 * 选择公司（当只有一个公司的时候直接调用接口）
	 * 传入参数{"userid":"690fb669ed4d40219964baad7783abd4"}
	 * 传出参数{"message":"查询成功","status":0,"userInfo":{"createtime":1468413494000,"companyid":"67702cc412264f4ea7d2c5f692070457","username":"15000042335","phone":"15000042335","sex":"1","userid":"690fb669ed4d40219964baad7783abd4","isfristlogin":0,"realname":"silence","status":1,"delflag":0,"companyname":"紫痕软件有限公司","isshowphone":0}}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	/*@RequestMapping("/dwrlogin")
	@ResponseBody
	public Map<String, Object> dwrlogin(@RequestParam Map<String, Object> map,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			DwrUtil.DwrLogin(map);
			data.put("success", true);
		} catch (Exception e) {
			data.put("success", false);
		}
		return data;
	}
	*/
	/**
	 * 删除用户的关联数据
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/deleteForwardUserInfo")
	public Map<String,Object> deleteForwardUserInfo(@RequestParam Map<String,Object> map , 
			HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			this.indexService.deleteForwaredUserInfo(map);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	
	/**
	 * 查询正式发布的版本号信息
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getFormalVersionNumber")
	public String getFormalVersionNumber(@RequestParam Map<String,Object> map , 
			HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");  
		String version = "";
		try {
			version =  this.indexService.getFormalVersionNumber("formalversion");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return version;
	}
}
