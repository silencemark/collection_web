package com.collection.controller.pc.worksheet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.collection.service.IndexService;
import com.collection.service.worksheet.RewardInfoService;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;
import com.collection.util.UserUtil;

@Controller
@RequestMapping(value = "/pc")
public class PCRewardInfoController extends BaseController {

	private transient static Log log = LogFactory
			.getLog(PCRewardInfoController.class);

	@Resource
	RewardInfoService rewardInfoService;

	@Resource
	IndexService indexService;

	/**
	 * 奖励单列表
	 *  传入参数：receiveid ：当前登陆者的useid,companyid
	 * 
	 * 案例：
	 * receiveid=1
	 * {"pager":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"message":"未阅读的奖励单信息查询成功！","status":0,"data":[{"id":"60760dd258a74669bd497d99b876a253","createtime":"2016-07-21 13:55","companyid":"67702cc412264f4ea7d2c5f692070457","punishid":"7825a38ea4bc43c59e53a40bd5810439","isread":0,"realname":"李大炮","resourceid":"7825a38ea4bc43c59e53a40bd5810439","receiveid":"1","resulttext":"kou tou"}]}
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getRewardListInfo")
	public String getRewardListInfo(@RequestParam Map<String, Object> map,
			Model model, HttpServletRequest request,
			HttpServletResponse response) {
		// 创建返回的map对象
		Map<String, Object> userInfo = UserUtil.getPCUser(request);
		map.put("receiveid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		
		// 创建分页对象
		PageHelper page = new PageHelper(request);
		// 查询总的数据条数
		int count = this.rewardInfoService.getRewardListCount(map);
		// 设置总数
		page.setTotalCount(count);
		// 初始化分页信息
		page.initPage(map);
		// 查询信息
		List<Map<String, Object>> rewardlist = this.rewardInfoService
				.getRewardListInfo(map);
		model.addAttribute("rewardlist", rewardlist);
		model.addAttribute("page", page.cateringPage().toString());
		model.addAttribute("map", map);
		return "/pc/worksheet/reward_list";
	}

	/**
	 * 查询奖励单的详细信息
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getRewardDetailInfo")
	public String getRewardDetailInfo(@RequestParam Map<String, Object> map,
			Model model, HttpServletRequest request,
			HttpServletResponse response) {
		// 创建返回的map对象

		Map<String, Object> userInfo = UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);

		Map<String, Object> rewardinfo = this.rewardInfoService
				.getRewardDetailInfo(map);
		rewardinfo.put("rangeliststr", JSONArray.fromObject(rewardinfo.get("rangelist")));
		rewardinfo.put("filelist", JSONArray.fromObject(rewardinfo.get("filelist")));
		model.addAttribute("rewardinfo", rewardinfo);
		
		model.addAttribute("map", map);

		return "/pc/worksheet/reward_detail";
	}
	
	/**
	 * 查询奖励单的详细信息
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getEmployeeRewardDetailInfo")
	public String getEmployeeRewardDetailInfo(@RequestParam Map<String, Object> map,
			Model model, HttpServletRequest request,
			HttpServletResponse response) {
		// 创建返回的map对象

		Map<String, Object> userInfo = UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);

		Map<String, Object> rewardinfo = this.rewardInfoService
				.getRewardDetailInfo(map);
		rewardinfo.put("rangeliststr", JSONArray.fromObject(rewardinfo.get("rangelist")));
		rewardinfo.put("filelist", JSONArray.fromObject(rewardinfo.get("filelist")));
		model.addAttribute("rewardinfo", rewardinfo);
		
		model.addAttribute("map", map);

		return "/pc/restaurant/employee_reward_detail";
	}
	
	/**
	 *添加奖励单信息
	 */
	@RequestMapping("/addReward")
	public String getPcOaNoticeDetail(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		request.setAttribute("map", map);
		return "/pc/worksheet/reward_add";
	}
	
	/**
	 * 添加奖励单信息
	 * 
	 * 传入参数：
	 * userlist(组织机构id，单个的用户的id)：发布范围，
	 * filelist(3张图片的路径)，
	 * sound(语音的路径)，
	 * 奖励单参数：companyid，rewarduserid，realname，organizename，position，reason，（rewardresult/resulttext）,，examineuserid，createid
	 * ---->参数全传入，没有值但是也要有对象
	 * 案例：
	 * userids=1,2,690fb669ed4d40219964baad7783abd4&userlist={"userlist":[{"userid":"1"},{"organizeid":"3"},{"organizeid":"1"},{"organizeid":"4"}]}&filelist=1.jpg,2.jpg&sound=qbc.vcr&companyid=67702cc412264f4ea7d2c5f692070457&rewarduserid=2&realname=silence&organizename=zhangjiangfendian&position=manager&reason=what you doing&resulttext=kou tou&examineuserid=2&createid=690fb669ed4d40219964baad7783abd4
	 * {"userids":"1,2,690fb669ed4d40219964baad7783abd4","userlist":"{"userlist":[{"userid":"1"},{"organizeid":"3"},{"organizeid":"1"},{"organizeid":"4"}]}","filelist":"1.jpg,2.jpg","sound":"qbc.vcr","companyid":"67702cc412264f4ea7d2c5f692070457","rewarduserid":2,"realname":"silence","organizename":"zhangjiangfendian","position":"manager","reason":"what you doing","resulttext":"kou tou","examineuserid":2,"createid":"690fb669ed4d40219964baad7783abd4"}
	 * {"message":"奖励单新增成功","status":0}
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertRewardInfo" )
	public Map<String,Object> insertRewardInfo(@RequestParam Map<String,Object> map , 
			HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			//添加奖励单信息
			String punishid = this.rewardInfoService.insertRewardInfo(map);
			
			//发布范围
			JSONObject json=JSONObject.fromObject(map.get("userlist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String, Object> user:userlist){
				user.put("companyid", map.get("companyid"));
 				user.put("resourceid", punishid);
 				user.put("resourcetype", 2);
 				if(user.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
 					user.put("organizeid", user.get("organizeid"));
 					user.put("type", 2);
 				}
 				if(user.containsKey("userid") && !"".equals(map.get("userid"))){
 					user.put("userid", user.get("userid"));
 					user.put("type", 1);
 				}
 				
 				//添加到发布范围表
				this.indexService.insertReleaseRange(user);
			}
			
			
			Map<String,Object> filemap = new HashMap<String,Object>();
			filemap.put("companyid", map.get("companyid"));
			filemap.put("resourceid", punishid);
			filemap.put("resourcetype", 9);
			
			//添加添加图片的路径
			String files = String.valueOf(map.get("filelist"));
			if(!"".equals(files) && map.containsKey("filelist")){
				String[] filelist = files.split(",");
				for(String url : filelist){
					filemap.put("visiturl", url);
					filemap.put("type", 1);
					this.indexService.insertfile(filemap);
				}
			}
			
			//添加语音信息
			String sound = String.valueOf(map.get("sound"));
			if(!"".equals(map.get("sound")) && map.containsKey("sound")){
				filemap.put("visiturl", sound);
				filemap.put("type", 2);
				this.indexService.insertfile(filemap);
			}
			
			resultMap.put("status", 0);
			resultMap.put("message", "奖励单新增成功");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 0);
			resultMap.put("message", "奖励单新增出错");
		}
		
		return resultMap;
	}


	/**
	 * 奖励单导出
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportRewardList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportRewardList(@RequestParam Map<String, Object> map,
			HttpServletRequest request) {
		String importurl = request.getSession().getServletContext()
				.getRealPath("/upload/excel/奖励单列表.xls");

		List<Map<String, Object>> rewardlist = this.rewardInfoService
				.getRewardListInfo(map);

		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		String[] headers = { "奖励人", "奖励结果", "填写时间", "状态" };

		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> order : rewardlist) {
			TreeMap<String, Object> datamap = new TreeMap<String, Object>();
			datamap.put("a",
					order.get("realname") == null ? "" : order.get("realname"));
			datamap.put(
					"b",
					order.get("resulttext") == null ? "" : order
							.get("resulttext"));
			datamap.put(
					"c",
					order.get("createtime") == null ? "" : order
							.get("createtime"));
			if (String.valueOf(order.get("status")).equals("1")) {
				datamap.put("f", "已处理");
			} else {
				datamap.put("f", "未处理");
			}
			dataset.add(datamap);
		}
		try {
			OutputStream out = new FileOutputStream(importurl);
			ex.exportExcel(headers, dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "奖励单列表.xls";
	}
	 
	@RequestMapping(value = "/exportRewardDetailList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportRewardDetailList(@RequestParam Map<String, Object> map,
			HttpServletRequest request) {
		String importurl = request.getSession().getServletContext()
				.getRealPath("/upload/excel/奖励单详情.xls");

	Map<String, Object> rewardlist = this.rewardInfoService
				.getRewardDetailInfo(map);

		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		String[] headers = {"姓名","部门","职务","奖励结果","奖励原因","填写人","填写时间","审批人","审批人意见"};

		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		TreeMap<String, Object> datamap=new TreeMap<String, Object>();
		   
	       datamap.put("a", rewardlist.get("realname")==null?"":rewardlist.get("realname"));
	       datamap.put("b", rewardlist.get("organizename")==null?"":rewardlist.get("organizename"));
	       datamap.put("c", rewardlist.get("position")==null?"":rewardlist.get("position"));
	       datamap.put("d", rewardlist.get("resulttext")==null?"":rewardlist.get("resulttext"));
	       datamap.put("e", rewardlist.get("reason")==null?"":rewardlist.get("reason"));
	       datamap.put("f", rewardlist.get("createname")==null?"":rewardlist.get("createname"));
	       datamap.put("g", rewardlist.get("createtime")==null?"":rewardlist.get("createtime"));
	       datamap.put("h", rewardlist.get("examineusername")==null?"":rewardlist.get("examineusername"));
	       if(String.valueOf(rewardlist.get("status")).equals("1")){
	    	   
	    	   datamap.put("i", rewardlist.get("opinion")==null?"":rewardlist.get("opinion"));
	       }else{
	    	   
	    	   datamap.put("i", "");
	       }
	       dataset.add(datamap);
		try {
			OutputStream out = new FileOutputStream(importurl);
			ex.exportExcel(headers, dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "奖励单详情.xls";
	}

	/**
	 * 查询奖励类型
	 * 
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getRewardTypeListInfo", method = RequestMethod.POST)
	public Map<String, Object> getRewardTypeListInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			map.put("typeid", 1);
			List<Map<String, Object>> list = this.rewardInfoService
					.getDictionListInfo(map);
			if (list != null && list.size() > 0) {
				resultMap.put("status", 0);
				resultMap.put("data", list);
			} else {
				resultMap.put("status", 1);
				resultMap.put("data", "");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("data", "");
		}

		return resultMap;
	}

	/**
	 * 提交奖励单的审核信息 传入参数： result，opinion，updateid，punishid,companyid ---->参数全部传入
	 * 
	 * punishid=7825a38ea4bc43c59e53a40bd5810439&result=1&opinion=good is
	 * do&updateid=1&companyid=67702cc412264f4ea7d2c5f692070457
	 * {"punishid":"7825a38ea4bc43c59e53a40bd5810439"
	 * ,"result":1,"opinion":"good is do"
	 * ,"updateid":1,"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * {"message":"回复信息提交成功！","status":0}
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * 	@RequestMapping("/examineApplyOrder")
	@ResponseBody
	
	}
	
	 */
	
	@ResponseBody
	@RequestMapping(value = "/updateRewardInfo")
	public Map<String, Object> updateRewarddInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
			//创建返回的map对象
			Map<String,Object> resultMap = new HashMap<String,Object>();
			
			try {
				
				//审核信息提交
				this.rewardInfoService.updateRewarddInfo(map);
				
				//判断审核结果，如果result=1则表示同意，就给范围内的人和店发送信息。如果result=2或者result=“”,则表示审核未通过
				String result = String.valueOf(map.get("result"));
				if(!"".equals(result) && map.containsKey("result") && "1".equals(result)){
					
					Map<String,Object> rewardmap = this.rewardInfoService.getRewardDetailInfo(map);
					//添加奖励人转发表
					Map<String,Object> forwordmap=new HashMap<String, Object>();
					forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
					forwordmap.put("companyid",rewardmap.get("companyid"));
					forwordmap.put("resourceid",map.get("rewardid"));
					forwordmap.put("receiveid",rewardmap.get("rewarduserid"));
					forwordmap.put("createid",rewardmap.get("createid"));
					forwordmap.put("isread",0);
					forwordmap.put("resourcetype",9);
					forwordmap.put("createtime",new Date());
					forwordmap.put("delflag", 0);
					this.indexService.insertForword(forwordmap);
					
					//审核通过之后，给发送范围内的人员发送信息
					Map<String, Object> rangemap=new HashMap<String, Object>();
					rangemap.put("resourcetype", 2);
					rangemap.put("resourceid", map.get("rewardid"));
					List<Map<String,Object>> list = this.indexService.getRangeList(rangemap);
					
					for(Map<String, Object> user:list){
						user.put("companyid", map.get("companyid"));
		 				user.put("resourceid", map.get("punishid"));
		 				user.put("resourcetype", 2);
		 				if(user.containsKey("organizeid") && !"".equals(user.get("organizeid"))){
		 					user.put("organizeid", user.get("organizeid"));
		 					user.put("type", 2);
		 					
		 					//添加到发布范围用户表
		 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
		 					for(Map<String, Object> userinfo:userinfolist){
		 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
		 						releaserangemap.put("companyid", map.get("companyid"));
		 						releaserangemap.put("resourceid", map.get("punishid"));
		 						releaserangemap.put("resourcetype", 2);
		 						releaserangemap.put("userid", userinfo.get("userid"));
		 						this.indexService.insertReleaseRangeUser(releaserangemap);
		 					}
		 				}
		 				if(user.containsKey("userid") && !"".equals(user.get("userid"))){
		 					user.put("userid", user.get("userid"));
		 					user.put("type", 1);
		 					
		 					//添加到发布范围用户表
		 					Map<String, Object>  releaserangemap=new HashMap<String, Object>();
							releaserangemap.put("companyid", map.get("companyid"));
							releaserangemap.put("resourceid", map.get("punishid"));
							releaserangemap.put("resourcetype", 2);
							releaserangemap.put("userid", user.get("userid"));
							
							this.indexService.insertReleaseRangeUser(releaserangemap);
		 				}
					}
				}
				resultMap.put("status", 0);
				resultMap.put("message", "回复信息提交成功！");
			} catch (Exception e) {
				// TODO: handle exception
				log.error(e);
				resultMap.put("status", 1);
				resultMap.put("message", "回复信息提交出错");
			}
			
			return resultMap;

	}

	/*------------------------------------------------------------------处罚单----------------------------------------------------------------------------------*/

	/**
	 * 查询未审核的处罚单的信息 传入参数： receiveid：（用户的userid）,companyid
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getPunishListInfo")
	public String getPunishListInfo(@RequestParam Map<String, Object> map,
			Model model, HttpServletRequest request,
			HttpServletResponse response) {
		// 创建返回的map对象
		Map<String, Object> userInfo = UserUtil.getPCUser(request);
		map.put("receiveid", userInfo.get("userid"));
		map.put("companyid", userInfo.get("companyid"));
		// 创建分页对象
		PageHelper page = new PageHelper(request);
		// 查询总的数据条数
		int count = this.rewardInfoService.getPunishListCount(map);
		// 设置总数
		page.setTotalCount(count);
		// 初始化分页信息
		page.initPage(map);

		// 查询信息
		List<Map<String, Object>> Punishlist = this.rewardInfoService
				.getPunishListInfo(map);

		model.addAttribute("Punishlist", Punishlist);// 对象集合
		model.addAttribute("page", page.cateringPage().toString());// 分页参数
		model.addAttribute("map", map);

		return "/pc/worksheet/punish_list";
	}

	/**
	 * 查询已经审核的处罚单信息 传入参数： receiveid：（用户的userid）,companyid
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getExaminedPunishListInfo", method = RequestMethod.POST)
	public Map<String, Object> getExaminedPunishListInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (!map.containsKey("receiveid") || "".equals(map.get("receiveid"))) {
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：receiveid");
			return resultMap;
		}

		try {
			map.put("status", 1);

			// 创建分页对象
			PageScroll page = new PageScroll();
			// 查询总的数据条数
			int count = this.rewardInfoService.getPunishListTimesCount(map);
			// 设置总数
			page.setTotalRecords(count);
			// 初始化分页信息
			page.initPage(map);

			// 查询信息
			List<Map<String, Object>> list = this.rewardInfoService
					.getPunishListTimesInfo(map);
			if (list.size() > 0 && list != null) {
				resultMap.put("data", list);
				resultMap.put("status", 0);
				resultMap.put("message", "已审核的处罚单信息查询成功！");
			} else {
				resultMap.put("data", "");
				resultMap.put("status", 1);
				resultMap.put("message", "已审核的处罚单信息，暂无数据");
			}
			resultMap.put("page", page);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);

			resultMap.put("data", "");
			resultMap.put("status", 1);
			resultMap.put("message", "已审核的处罚单信息查询出错了");
		}

		return resultMap;
	}

	/**
	 * 查询惩罚单的详细信息 传入参数： punishid：惩罚单编号，companyid：所属公司id
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getPunishDetailInfo")
	public String getPunishDetailInfo(@RequestParam Map<String, Object> map,
			Model model, HttpServletRequest request,
			HttpServletResponse response) {
		// 创建返回的map对象
		Map<String, Object> punishinfo=this.rewardInfoService.getPunishDetailInfo(map);
		punishinfo.put("filelist", JSONArray.fromObject(punishinfo.get("filelist")));//图片和文件转成json数据
		punishinfo.put("rangelist", JSONArray.fromObject(punishinfo.get("rangelist")));
		model.addAttribute("punishinfo", punishinfo);
		model.addAttribute("map", map);

		
		return "/pc/worksheet/punish_detail";

	}
	
	
	@RequestMapping(value = "/getEmployeePunishDetailInfo")
	public String getEmployeePunishDetailInfo(@RequestParam Map<String, Object> map,
			Model model, HttpServletRequest request,
			HttpServletResponse response) {
		// 创建返回的map对象
		Map<String, Object> punishinfo=this.rewardInfoService.getPunishDetailInfo(map);
		punishinfo.put("filelist", JSONArray.fromObject(punishinfo.get("filelist")));//图片和文件转成json数据
		punishinfo.put("rangelist", JSONArray.fromObject(punishinfo.get("rangelist")));
		model.addAttribute("punishinfo", punishinfo);
		model.addAttribute("map", map);

		
		return "/pc/restaurant/employee_punish_detail";

	}

	/**
	 * 提交审核信息 传入参数： result，opinion，updatetime，updateid，status，punishid，companyid
	 * public Map<String, Object> examineApplyOrder(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.purchaseService.updateApplyOrder(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePunishInfo")
	public Map<String, Object> updatePunishInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		String punishid = String.valueOf(map.get("punishid"));
		if(!map.containsKey("punishid") || "".equals(punishid)){
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：punishid");
			return resultMap;
		}
		
		try {
			
			//提交审核
			this.rewardInfoService.updatePunishInfo(map);
			
			//判断审核结果，如果result=1则表示同意，就给范围内的人和店发送信息。如果result=2或者result=“”,则表示审核未通过
			String result = String.valueOf(map.get("result"));
			if(!"".equals(result) && map.containsKey("result") && "1".equals(result)){
				
				//查询惩罚单信息
				Map<String,Object> punishmap = this.rewardInfoService.getPunishDetailInfo(map);
				//添加懲罰者转发表
				Map<String,Object> forwordmap=new HashMap<String, Object>();
				forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
				forwordmap.put("companyid",punishmap.get("companyid"));
				forwordmap.put("resourceid",punishid);
				forwordmap.put("receiveid",punishmap.get("punishuserid"));
				forwordmap.put("createid",punishmap.get("createid"));
				forwordmap.put("isread",0);
				forwordmap.put("resourcetype",8);
				forwordmap.put("createtime",new Date());
				forwordmap.put("delflag", 0);
				this.indexService.insertForword(forwordmap);
				
				//审核通过之后，给发送范围内的人员发送信息
				Map<String, Object> rangemap=new HashMap<String, Object>();
				rangemap.put("resourcetype", 3);
				rangemap.put("resourceid", map.get("punishid"));
				List<Map<String,Object>> list = this.indexService.getRangeList(rangemap);
				
				for(Map<String, Object> user:list){
					user.put("companyid", map.get("companyid"));
	 				user.put("resourceid", map.get("punishid"));
	 				user.put("resourcetype", 3);
	 				if(user.containsKey("organizeid") && !"".equals(user.get("organizeid"))){
	 					user.put("organizeid", user.get("organizeid"));
	 					user.put("type", 2);
	 					
	 					//添加到发布范围用户表
	 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
	 					for(Map<String, Object> userinfo:userinfolist){
	 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
	 						releaserangemap.put("companyid", map.get("companyid"));
	 						releaserangemap.put("resourceid", map.get("punishid"));
	 						releaserangemap.put("resourcetype", 3);
	 						releaserangemap.put("userid", userinfo.get("userid"));
	 						this.indexService.insertReleaseRangeUser(releaserangemap);
	 					}
	 				}
	 				if(user.containsKey("userid") && !"".equals(user.get("userid"))){
	 					user.put("userid", user.get("userid"));
	 					user.put("type", 1);
	 					
	 					//添加到发布范围用户表
	 					Map<String, Object>  releaserangemap=new HashMap<String, Object>();
						releaserangemap.put("companyid", map.get("companyid"));
						releaserangemap.put("resourceid", map.get("punishid"));
						releaserangemap.put("resourcetype", 3);
						releaserangemap.put("userid", user.get("userid"));
						
						this.indexService.insertReleaseRangeUser(releaserangemap);
	 				}
				}
			}
			
			resultMap.put("status", 0);
			resultMap.put("message", "惩罚单详情查询成功");
				
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "惩罚单详情查询出错了");
		}
		
		return resultMap;
	}

	/**
	 * 添加处罚单信息 传入参数：
	 * companyid，punishuserid，realname，organizename，position，reason
	 * ，punishresult，examineuserid，createid userlist(组织机构id，单个的用户的id)：发布范围，
	 * filelist(3张图片的路径)， sound(语音的路径)，
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/insertPunishInfo", method = RequestMethod.POST)
	public Map<String, Object> insertPunishInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			// 添加处罚单信息
			String punishid = this.rewardInfoService.insertPunishInfo(map);

			// 发布范围
			JSONObject json = JSONObject.fromObject(map.get("userlist") + "");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist = (List<Map<String, Object>>) json
					.get("userlist");
			for (Map<String, Object> user : userlist) {
				user.put("companyid", map.get("companyid"));
				user.put("resourceid", punishid);
				user.put("resourcetype", 3);
				if (user.containsKey("organizeid")
						&& !"".equals(map.get("organizeid"))) {
					user.put("organizeid", user.get("organizeid"));
					user.put("type", 2);
				}
				if (user.containsKey("userid") && !"".equals(map.get("userid"))) {
					user.put("userid", user.get("userid"));
					user.put("type", 1);
				}

				// 添加到发布范围表
				this.indexService.insertReleaseRange(user);
			}

			Map<String, Object> filemap = new HashMap<String, Object>();
			filemap.put("companyid", map.get("companyid"));
			filemap.put("resourceid", punishid);
			filemap.put("resourcetype", 8);

			// 添加添加图片的路径
			String files = String.valueOf(map.get("filelist"));
			if (!"".equals(files) && map.containsKey("filelist")) {
				String[] filelist = files.split(",");
				for (String url : filelist) {
					filemap.put("visiturl", url);
					filemap.put("type", 1);
					this.indexService.insertfile(filemap);
				}
			}

			// 添加语音信息
			String sound = String.valueOf(map.get("sound"));
			if (!"".equals(map.get("sound")) && map.containsKey("sound")) {
				filemap.put("visiturl", sound);
				filemap.put("type", 2);
				this.indexService.insertfile(filemap);
			}

			resultMap.put("status", 0);
			resultMap.put("message", "处罚单新增成功");

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", 1);
		}

		return resultMap;
	}

	/**
	 * 查询惩罚类型
	 * 
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPunishTypeListInfo", method = RequestMethod.POST)
	public Map<String, Object> getPunishTypeListInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			map.put("typeid", 5);
			List<Map<String, Object>> list = this.rewardInfoService
					.getDictionListInfo(map);
			if (list != null && list.size() > 0) {
				resultMap.put("status", 0);
				resultMap.put("data", list);
			} else {
				resultMap.put("status", 1);
				resultMap.put("data", "");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("data", "");
		}

		return resultMap;
	}
	
	/**
	 * 进入 添加页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoPunishAddPage")
	public String intoPunishAddPage(@RequestParam Map<String,Object> map , HttpServletRequest  request){
		request.setAttribute("map", map);
		return "/pc/worksheet/punish_add";
	}
	
	/**
	 * 处罚单列表导出
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportPunishList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportPunishList(@RequestParam Map<String, Object> map,
			HttpServletRequest request) {
		String importurl = request.getSession().getServletContext()
				.getRealPath("/upload/excel/惩罚单列表.xls");

		List<Map<String, Object>> punishlist = this.rewardInfoService.getPunishListInfo(map);
				

		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		String[] headers = { "处罚人", "处罚结果", "填写时间", "状态" };
	
		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> order : punishlist) {
			TreeMap<String, Object> datamap = new TreeMap<String, Object>();
			datamap.put("a",
					order.get("realname") == null ? "" : order.get("realname"));
			datamap.put(
					"b",
					order.get("resulttext") == null ? "" : order
							.get("resulttext"));
			datamap.put(
					"c",
					order.get("createtime") == null ? "" : order
							.get("createtime"));
			if (String.valueOf(order.get("status")).equals("1")) {
				datamap.put("f", "已处理");
			} else {
				datamap.put("f", "未处理");
			}
			dataset.add(datamap);
		}
		try {
			OutputStream out = new FileOutputStream(importurl);
			ex.exportExcel(headers, dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "惩罚单列表.xls";
	}
	/**
	 * 处罚单详情导出
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */

	 
	@RequestMapping(value = "/exportPunishDetailList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportPunishDetailList(@RequestParam Map<String, Object> map,
			HttpServletRequest request) {
		String importurl = request.getSession().getServletContext()
				.getRealPath("/upload/excel/处罚单详情.xls");

	Map<String, Object> punishlist = this.rewardInfoService
				.getPunishDetailInfo(map);

		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		String[] headers = {"姓名","部门","职务","处罚结果","处罚原因","填写人","填写时间","审批人","审批人意见"};

		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		TreeMap<String, Object> datamap=new TreeMap<String, Object>();
		   
	       datamap.put("a", punishlist.get("realname")==null?"":punishlist.get("realname"));
	       datamap.put("b", punishlist.get("organizename")==null?"":punishlist.get("organizename"));
	       datamap.put("c", punishlist.get("position")==null?"":punishlist.get("position"));
	       datamap.put("d", punishlist.get("punishresult")==null?"":punishlist.get("punishresult"));
	       datamap.put("e", punishlist.get("reason")==null?"":punishlist.get("reason"));
	       datamap.put("f", punishlist.get("createname")==null?"":punishlist.get("createname"));
	       datamap.put("g", punishlist.get("createtime")==null?"":punishlist.get("createtime"));
	       datamap.put("h", punishlist.get("examinename")==null?"":punishlist.get("examinename"));
	       
	       if (String.valueOf(punishlist.get("status")).equals("1")) {
	    	   datamap.put("i", punishlist.get("opinion")==null?"":punishlist.get("opinion"));
			} else {
				datamap.put("i", "");
			}
	       
	       dataset.add(datamap);
		try {
			OutputStream out = new FileOutputStream(importurl);
			ex.exportExcel(headers, dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "处罚单详情.xls";
	}

	/*--------------------------------------------------------------------------报修单-----------------------------------------------------------------------*/

	/**
	 * 查询未处理的报修单信息 传入参数： receiveid（用户的userid）
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getRepairListInfo")
	public String  getRepairListInfo(@RequestParam Map<String, Object> map,
			Model model, HttpServletRequest request,
			HttpServletResponse response) {
		// 创建返回的map对象
		Map<String, Object> userInfo = UserUtil.getPCUser(request);
		map.put("receiveid", userInfo.get("userid"));
		// 创建分页对象
		PageHelper page = new PageHelper(request);
			// 创建分页对象
			// 查询总的数据条数
			int count = this.rewardInfoService.getRepairListCount(map);
			// 设置总数
			page.setTotalCount(count);
			// 初始化分页信息
			page.initPage(map);

			// 查询信息
			List<Map<String, Object>> repairlist = this.rewardInfoService
					.getRepairListInfo(map);
	           model.addAttribute("repairlist", repairlist);
	           model.addAttribute("page", page.cateringPage().toString());
	           model.addAttribute("map", map);

		return "/pc/worksheet/repair_list";
	}


	/**
	 * 查询报修单的详细信息 传入参数： repairid，companyid
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getRepairDetailInfo")
	public String getRepairDetailInfo(@RequestParam Map<String, Object> map,
			Model model, HttpServletRequest request,
			HttpServletResponse response) {
		// 创建返回的map对象
		Map<String, Object> userInfo = UserUtil.getPCUser(request);
		map.put("companyid", userInfo.get("companyid"));


		Map<String, Object> repairinfo = this.rewardInfoService
				.getRepairDetailInfo(map);
		repairinfo.put("filelist", JSONArray.fromObject(repairinfo.get("filelist")));
		model.addAttribute("repairinfo", repairinfo);
		model.addAttribute("map", map);
		return "/pc/worksheet/repair_detail";
	}

	/**
	 * 提交抄送人意见信息 传入参数： result，opinion，updateid，repairid，companyid
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateRepairInfo")
	public Map<String, Object> updateRepairInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		// 创建返回的map对象
		Map<String, Object> data = new HashMap<String, Object>();
        
		map.put("status", 1);
		
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.rewardInfoService.updateRepairInfo(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}

	/**
	 * 进入报修单的添加页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/intoRepairAdd")
	public String intoRepairAdd(@RequestParam Map<String, Object> map, HttpServletRequest request){
		request.setAttribute("map", map);
		return "/pc/worksheet/repair_add";
	}
	
	/**
	 * 添加报修单信息 传入参数： companyid，findtime，description，examineuserid，createid
	 * filelist(3张图片的路径)， sound(语音的路径)，
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/insertRepairInfo", method = RequestMethod.POST)
	public Map<String, Object> insertRepairInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {

			// 添加
			String repairid = this.rewardInfoService.insertRepairInfo(map);

			Map<String, Object> filemap = new HashMap<String, Object>();
			filemap.put("companyid", map.get("companyid"));
			filemap.put("resourceid", repairid);
			filemap.put("resourcetype", 13);

			// 添加添加图片的路径
			String files = String.valueOf(map.get("filelist"));
			if (!"".equals(files) && map.containsKey("filelist")) {
				String[] filelist = files.split(",");
				for (String url : filelist) {
					filemap.put("visiturl", url);
					filemap.put("type", 1);
					this.indexService.insertfile(filemap);
				}
			}

			// 添加语音信息
			String sound = String.valueOf(map.get("sound"));
			if (!"".equals(map.get("sound")) && map.containsKey("sound")) {
				filemap.put("visiturl", sound);
				filemap.put("type", 2);
				this.indexService.insertfile(filemap);
			}

			resultMap.put("status", 0);
			resultMap.put("message", "报修单新增成功");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);

			resultMap.put("status", 1);
			resultMap.put("message", "报修单新增出错");
		}

		return resultMap;
	}
	/**
	 * 报修单列表导出
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportRepairList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportRepairList(@RequestParam Map<String, Object> map,
			HttpServletRequest request) {
		String importurl = request.getSession().getServletContext()
				.getRealPath("/upload/excel/报修单列表.xls");

		List<Map<String, Object>> repairlist = this.rewardInfoService.getRepairListInfo(map);
				

		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		String[] headers = { "描述", "发现时间", "填写人", "状态" };
	
		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> order : repairlist) {
			TreeMap<String, Object> datamap = new TreeMap<String, Object>();
			datamap.put("a",order.get("description") == null ? "" : order.get("description"));
			datamap.put("b",order.get("findtime") == null ? "" : order.get("findtime"));
			datamap.put("c",order.get("createtime") == null ? "" : order.get("createtime"));
			if (String.valueOf(order.get("status")).equals("1")) {
				datamap.put("d", "已处理");
			} else {
				datamap.put("d", "未处理");
			}
			dataset.add(datamap);
		}
		try {
			OutputStream out = new FileOutputStream(importurl);
			ex.exportExcel(headers, dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "报修单列表.xls";
	}
	/**
	 * 处罚单详情导出
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */

	 
	@RequestMapping(value = "/exportRepairDetailList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportRepairDetailList(@RequestParam Map<String, Object> map,
			HttpServletRequest request) {
		String importurl = request.getSession().getServletContext()
				.getRealPath("/upload/excel/保修单详情.xls");

	Map<String, Object> repairlist = this.rewardInfoService
				.getRepairDetailInfo(map);

		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		String[] headers = {"发现时间","地点及描述","填写人","填写时间","抄送人","抄送人意见"};

		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		TreeMap<String, Object> datamap=new TreeMap<String, Object>();
		   
	       datamap.put("a", repairlist.get("findtime")==null?"":repairlist.get("findtime"));
	       datamap.put("b", repairlist.get("description")==null?"":repairlist.get("description"));
	       datamap.put("c", repairlist.get("createname")==null?"":repairlist.get("createname"));
	       datamap.put("d", repairlist.get("createtime")==null?"":repairlist.get("createtime"));
	       datamap.put("e", repairlist.get("examinename")==null?"":repairlist.get("examinename"));
	       
	       if (String.valueOf(repairlist.get("status")).equals("1")) {
	    	   datamap.put("f", repairlist.get("opinion")==null?"":repairlist.get("opinion"));
			} else {
				datamap.put("f", "");
			}
	       
	       dataset.add(datamap);
		try {
			OutputStream out = new FileOutputStream(importurl);
			ex.exportExcel(headers, dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "保修单详情.xls";
	}

	/*-----------------------------------------------------------------菜单成本---------------------------------------------------------------------------------*/

	/**
	 * 查询未处理的菜单成本列表信息 传入参数： receiveid（用户的userid）,companyid
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getcostListInfo")
	public String getCostListInfo(@RequestParam Map<String, Object> map,
			Model model, HttpServletRequest request,
			HttpServletResponse response) {
		// 创建返回的map对象
		Map<String, Object> userInfo = UserUtil.getPCUser(request);
		 map.put("receiveid", userInfo.get("userid"));
		 map.put("companyid", userInfo.get("companyid"));
           PageHelper page=new PageHelper(request);
           
           int count=rewardInfoService.getDishesListCount(map);
           page.setTotalCount(count);
           page.initPage(map);
           List<Map<String, Object>> costlist =this.rewardInfoService.getDishesListInfo(map);
           model.addAttribute("costlist", costlist);
           model.addAttribute("page", page.cateringPage().toString());
           model.addAttribute("map", map);
			return "/pc/worksheet/cost_list";
		}

		

	/**
	 * 查询已经处理的菜品成本信息 传入参数： receiveid（用户的userid）,companyid
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getExmaineDishesListInfo", method = RequestMethod.POST)
	public Map<String, Object> getExmaineDishesListInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (!map.containsKey("receiveid") || "".equals(map.get("receiveid"))) {
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：receiveid");
			return resultMap;
		}

		try {
			map.put("status", 1);

			// 创建分页对象
			PageScroll page = new PageScroll();
			// 查询总的数据条数
			int count = this.rewardInfoService.getDishesListTimesCount(map);
			// 设置总数
			page.setTotalRecords(count);
			// 初始化分页信息
			page.initPage(map);

			// 查询信息
			List<Map<String, Object>> list = this.rewardInfoService
					.getDishesListTimesInfo(map);
			if (list.size() > 0 && list != null) {
				resultMap.put("data", list);
				resultMap.put("status", 0);
				resultMap.put("message", "已审核的菜品成本信息查询成功！");
			} else {
				resultMap.put("data", "");
				resultMap.put("status", 1);
				resultMap.put("message", "已审核的菜品成本信息，暂无数据");
			}
			resultMap.put("page", page);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);

			resultMap.put("data", "");
			resultMap.put("status", 1);
			resultMap.put("message", "已审核的菜品成本信息查询出错了");
		}

		return resultMap;
	}

	/**
	 * 查询菜单成本信息详情 传入参数： dishesid，companyid
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getcostDetailInfo")
	public String getCostDetailInfo(@RequestParam Map<String, Object> map,
			Model model, HttpServletRequest request,
			HttpServletResponse response) {

		Map<String, Object> userInfo = UserUtil.getPCUser(request);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("companyid", userInfo.get("companyid"));

		Map<String, Object> costinfo = this.rewardInfoService
				.getDishesDetailInfo(map);
		costinfo.put("filelist", JSONArray.fromObject(costinfo.get("filelist")));
		model.addAttribute("costinfo", costinfo);
		model.addAttribute("map", map);
		return "/pc/worksheet/cost_detail";
	}

	/**
	 * 查询菜品类型信息
	 * 
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDishesTypeListInfo", method = RequestMethod.POST)
	public Map<String, Object> getDishesTypeListInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			map.put("typeid", 6);
			List<Map<String, Object>> list = this.rewardInfoService
					.getDictionListInfo(map);
			if (list != null && list.size() > 0) {
				resultMap.put("status", 0);
				resultMap.put("data", list);
			} else {
				resultMap.put("status", 1);
				resultMap.put("data", "");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("data", "");
		}

		return resultMap;
	}

	/**
	 * 提交抄送人意见 传入参数： result，opinion，updateid，dishesid，companyid
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateDishesInfo", method = RequestMethod.POST)
	public Map<String, Object> updateDishesInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		// 创建返回的map对象
		Map<String, Object> data = new HashMap<String, Object>();
        
		map.put("status", 1);
		
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.rewardInfoService.updateDishesInfo(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}

	/**
	 * 添加菜品成品信息 传入参数：
	 * companyid，orgid，dishestype，dishesname，feeding，costprice，price
	 * ，costrate，description，examineuserid，createid， filelist(3张图片的路径)，
	 * sound(语音的路径)，
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/insertDishesInfo", method = RequestMethod.POST)
	public Map<String, Object> insertDishesInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {

			// 添加
			String repairid = this.rewardInfoService.insertDishesInfo(map);

			Map<String, Object> filemap = new HashMap<String, Object>();
			filemap.put("companyid", map.get("companyid"));
			filemap.put("resourceid", repairid);
			filemap.put("resourcetype", 14);

			// 添加添加图片的路径
			String files = String.valueOf(map.get("filelist"));
			if (!"".equals(files) && map.containsKey("filelist")) {
				String[] filelist = files.split(",");
				for (String url : filelist) {
					filemap.put("visiturl", url);
					filemap.put("type", 1);
					this.indexService.insertfile(filemap);
				}
			}

			// 添加语音信息
			String sound = String.valueOf(map.get("sound"));
			if (!"".equals(map.get("sound")) && map.containsKey("sound")) {
				filemap.put("visiturl", sound);
				filemap.put("type", 2);
				this.indexService.insertfile(filemap);
			}

			resultMap.put("status", 0);
			resultMap.put("message", "菜品成本单新增成功");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);

			resultMap.put("status", 1);
			resultMap.put("message", "菜品成本单新增出错");
		}

		return resultMap;
	}

	/**
	 * 修改信息的阅读状态 resourceid，receiveid，resourcetype（以转发表中的类型id为标准）
	 * 
	 * 转发表：1：采购(入库)单 2：申购单3.用料单 4.退货单 5.报损单 6.岗位星值 7.综合星级 8.处罚单 9.奖励单 10.离店检查
	 * 11.餐前检查 12.厨房检查 13.报修单 14.菜品成平管理 15.通用审批 16.请示 17 报销 18.请假 19.任务 20
	 * 备用金申请,21 每日报表 22.巡店日志，23,日报24 周报 25月报，27 通知）
	 * 
	 * 发布范围表：1：通知 2：奖励单 3：处罚单 4日报 5周报 6 月报 7.简报8.检查模版,9.岗位星值项目 10 星值规则，11 每日报表）
	 * 
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/upateUserNoReadStatus", method = RequestMethod.POST)
	public Map<String, Object> upateUserNoReadStatus(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			String[] forwardtypes = { "27", "9", "8", "23", "24", "25", "21" };
			String[] releaseRangetypes = { "1", "2", "3", "4", "5", "6", "11" };
			if (map.containsKey("resourcetype")
					&& !"".equals(map.get("resourcetype"))) {
				map.put("isread", 1);
				// 修改转发表中的阅读状态
				this.rewardInfoService.updateForwardReadStatus(map);

				String type = String.valueOf(map.get("resourcetype"));
				for (int i = 0; i < 7; i++) {
					if (type.equals(forwardtypes[i])) {
						map.put("resourcetype", releaseRangetypes[i]);
						// 修改发布范围表中的阅读状态
						this.rewardInfoService.updateRangeReadStatus(map);
					}
				}
			}

			resultMap.put("status", 0);
			resultMap.put("message", "已修改为已读");
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("status", 1);
			resultMap.put("message", "修改失败");
		}

		return resultMap;
	}
	
	/**
	 * 菜单成本列表导出
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportDishesList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportDishesList(@RequestParam Map<String, Object> map,
			HttpServletRequest request) {
		String importurl = request.getSession().getServletContext()
				.getRealPath("/upload/excel/菜单成本列表.xls");

		List<Map<String, Object>> repairlist = this.rewardInfoService.getDishesListInfo(map);
		
				

		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		String[] headers = { "菜名字", "类别", "成本价", "售价" ,"制作人","填写时间","状态"};
	
		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> order : repairlist) {
			TreeMap<String, Object> datamap = new TreeMap<String, Object>();
			datamap.put("a",order.get("dishesname") == null ? "" : order.get("dishesname"));
			datamap.put("b",order.get("dishestypename") == null ? "" : order.get("dishestypename"));
			datamap.put("c",order.get("costprice") == null ? "" : order.get("costprice"));
			datamap.put("d",order.get("price")==null ? "":order.get("price"));
			datamap.put("e",order.get("createname")==null ? "":order.get("createname"));
			datamap.put("f",order.get("createtime")==null ? "":order.get("createtime"));
			if (String.valueOf(order.get("status")).equals("1")) {
				datamap.put("g", "已处理");
			} else {
				datamap.put("g", "未处理");
			}
			dataset.add(datamap);
		}
		try {
			OutputStream out = new FileOutputStream(importurl);
			ex.exportExcel(headers, dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "菜单成本列表.xls";
	}


	/**
	 * 菜单成本单详情
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/exportDishesDetailList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportDishesDetailList(@RequestParam Map<String, Object> map,
			HttpServletRequest request) {
		String importurl = request.getSession().getServletContext()
				.getRealPath("/upload/excel/菜单成本单详情.xls");

	Map<String, Object>  disheslist = this.rewardInfoService
				.getDishesDetailInfo(map);

		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		String[] headers = {"菜品类别","菜品名称","投料标准","成本价","售价","成本率","地点及描述","制作人","填写时间","抄送人","抄送人意见"};

		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		TreeMap<String, Object> datamap=new TreeMap<String, Object>();
		   
	       datamap.put("a", disheslist.get("dishestypename")==null?"":disheslist.get("dishestypename"));
	       datamap.put("b", disheslist.get("dishesname")==null?"":disheslist.get("dishesname"));
	       datamap.put("c", disheslist.get("feeding")==null?"":disheslist.get("feeding"));
	       datamap.put("d", disheslist.get("costprice")==null?"":disheslist.get("costprice"));
	       datamap.put("e", disheslist.get("price")==null?"":disheslist.get("price"));
	       datamap.put("f", disheslist.get("costrate")==null?"":disheslist.get("costrate"));
	       datamap.put("g", disheslist.get("organizename")==null?"":disheslist.get("organizename"));
	       datamap.put("h", disheslist.get("createname")==null?"":disheslist.get("createname"));
	       datamap.put("i", disheslist.get("createtime")==null?"":disheslist.get("createtime"));
	       datamap.put("j", disheslist.get("examinename")==null?"":disheslist.get("examinename"));
	       
	       if (String.valueOf(disheslist.get("status")).equals("1")) {
	    	   datamap.put("k", disheslist.get("opinion")==null?"":disheslist.get("opinion"));
			} else {
				datamap.put("k", "");
			}
	       
	       dataset.add(datamap);
		try {
			OutputStream out = new FileOutputStream(importurl);
			ex.exportExcel(headers, dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "菜单成本单详情.xls";
	}

}
