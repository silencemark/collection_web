package com.collection.controller.worksheet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.service.IndexService;
import com.collection.service.worksheet.RewardInfoService;
import com.collection.util.PageScroll;


@Controller
@RequestMapping(value="/app")
public class RewardInfoController extends BaseController{

	private transient static Log log = LogFactory.getLog(RewardInfoController.class);
	
	@Resource
	RewardInfoService rewardInfoService;
	
	@Resource
	IndexService indexService;
	
	
	
	/**
	 * 查询工作表单中的未读数
	 * 8.处罚单 9.奖励单 10.离店检查 11.餐前检查 12.厨房检查 13.报修单 14.菜品成平管理
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getWorkSheetNoReadNum" , method = RequestMethod.POST)
	public Map<String,Object> getWorkSheetNoReadNum(@RequestParam Map<String,Object> map , 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			
			String[] worktypeid = {"9","8","10","11","12","13","14"};
			String[] rangetype = {"2","3"};
			String[] worktypename = {"rewardnum","punishnum","leavenum","mealnum","kitchennum","repairnum","costnum"};
			Map<String,Object> data = new HashMap<String,Object>();
			map.put("isread", 0);
			for(int i=0 ; i < 7 ; i++){
				List<Integer> resourcetypes=new ArrayList<Integer>();
				resourcetypes.add(Integer.parseInt(worktypeid[i]));
				map.put("resourcetypes", resourcetypes);
				if(i<=1){
					List<Integer> rangetypes=new ArrayList<Integer>();
					rangetypes.add(Integer.parseInt(rangetype[i]));
					map.put("rangetypes", rangetypes);
				}
				int num=this.indexService.getForwordNotreadNum(map);
				data.put(worktypename[i], num);
				map.remove("rangetypes");
				map.remove("rangetypes");
			}
			resultMap.put("status", 0);
			resultMap.put("data", data);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("data", "");
		}
		
		return resultMap;
	}
	
	
	/**
	 * 查询奖励单 未处理，已处理的 未读数量
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getRewardAllNotRead")
	public Map<String,Object> getRewardAllNotRead(@RequestParam Map<String,Object> map , HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.rewardInfoService.getRewardListCount(map);
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.rewardInfoService.getRewardListTimesCount(map);
			
			resultMap.put("noexaminenum", num);
			resultMap.put("examinenum", count);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	
	/**
	 * 查询未处理的奖励单信息
	 * 传入参数：receiveid ：当前登陆者的useid,companyid
	 * 
	 * 案例：
	 * receiveid=1
	 * {"pager":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"message":"未阅读的奖励单信息查询成功！","status":0,"data":[{"id":"60760dd258a74669bd497d99b876a253","createtime":"2016-07-21 13:55","companyid":"67702cc412264f4ea7d2c5f692070457","punishid":"7825a38ea4bc43c59e53a40bd5810439","isread":0,"realname":"李大炮","resourceid":"7825a38ea4bc43c59e53a40bd5810439","receiveid":"1","resulttext":"kou tou"}]}
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getNotReadRewardListInfo" , method = RequestMethod.POST)
	public Map<String,Object> getNotReadRewardListInfo(@RequestParam Map<String,Object> map , 
			HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		if(!map.containsKey("receiveid") || "".equals(map.get("receiveid"))){
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：receiveid");
			return resultMap;
		}
		
		try {
			map.put("status", 0);
			
			//创建分页对象
			PageScroll page = new PageScroll();
			//查询总的数据条数
			int count = this.rewardInfoService.getRewardListCount(map);
			//设置总数
			page.setTotalRecords(count);
			//初始化分页信息
			page.initPage(map);
			
			//查询信息
			List<Map<String,Object>> list = this.rewardInfoService.getRewardListInfo(map);
			if(list.size() > 0 && list != null){
				resultMap.put("data", list);
				resultMap.put("status", 0);
				resultMap.put("message", "未阅读的奖励单信息查询成功！");
			}else{
				resultMap.put("data", "");
				resultMap.put("status", 1);
				resultMap.put("message", "未阅读的奖励单信息，暂无数据");
			}
			resultMap.put("page", page);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			
			resultMap.put("data", "");
			resultMap.put("status", 1);
			resultMap.put("message", "未阅读的奖励单信息查询出错了");
		}
		
		return resultMap;
	}
	
	
	/**
	 * 查询已处理的奖励单信息
	 * 传入参数：receiveid ：当前登陆者的useid,companyid
	 * 案例：
	 * receiveid=1
	 * {"receiveid":1}
	 * {"pager":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"message":"未阅读的奖励单信息查询成功！","status":0,"data":[{"id":"60760dd258a74669bd497d99b876a253","createtime":"2016-07-21 13:55","companyid":"67702cc412264f4ea7d2c5f692070457","punishid":"7825a38ea4bc43c59e53a40bd5810439","isread":0,"realname":"李大炮","resourceid":"7825a38ea4bc43c59e53a40bd5810439","receiveid":"1","resulttext":"kou tou"}]}
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getReadedRewardListInfo" , method = RequestMethod.POST)
	public Map<String,Object> getReadedRewardListInfo(@RequestParam Map<String,Object> map , 
			HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		if(!map.containsKey("receiveid") || "".equals(map.get("receiveid"))){
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：receiveid");
			return resultMap;
		}
		
		try {
			map.put("status", 1);
			
			//创建分页对象
			PageScroll page = new PageScroll();
			//查询总的数据条数
			int count = this.rewardInfoService.getRewardListTimesCount(map);
			//设置总数
			page.setTotalRecords(count);
			//初始化分页信息
			page.initPage(map);
			
			//查询信息
			List<Map<String,Object>> list = this.rewardInfoService.getRewardListTimes(map);
			if(list.size() > 0 && list != null){
				resultMap.put("data", list);
				resultMap.put("status", 0);
				resultMap.put("message", "未阅读的奖励单信息查询成功！");
			}else{
				resultMap.put("data", "");
				resultMap.put("status", 1);
				resultMap.put("message", "未阅读的奖励单信息，暂无数据");
			}
			resultMap.put("page", page);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			
			resultMap.put("data", "");
			resultMap.put("status", 1);
			resultMap.put("message", "未阅读的奖励单信息查询出错了");
		}
		
		return resultMap;
	}
	
	
	/**
	 * 查询奖励单的详细信息
	 * 传入参数：rewardid：奖励单id
	 * 案例：
	 * rewardid=7825a38ea4bc43c59e53a40bd5810439
	 * {"rewardid":"7825a38ea4bc43c59e53a40bd5810439"}
	 * 
	 * {"message":"奖励单详情查询成功","status":0,"data":{"position":"manager","punishid":"7825a38ea4bc43c59e53a40bd5810439","examineusername":"李大炮","reason":"what you doing","status":0,"examineuserid":"2","filelist":[{"createtime":"2016-07-21 13:55:16","companyid":"67702cc412264f4ea7d2c5f692070457","resourcetype":9,"fileid":"0a9ef2b08ca04f83832003848a2c41ff","resourceid":"7825a38ea4bc43c59e53a40bd5810439","type":2,"visiturl":"qbc.vcr"},{"createtime":"2016-07-21 13:55:16","companyid":"67702cc412264f4ea7d2c5f692070457","resourcetype":9,"fileid":"254c2535217048df9e00a3f2eb7a24f3","resourceid":"7825a38ea4bc43c59e53a40bd5810439","type":1,"visiturl":"1.jpg"},{"createtime":"2016-07-21 13:55:16","companyid":"67702cc412264f4ea7d2c5f692070457","resourcetype":9,"fileid":"65e0550b7bf84bab8c2b605f1718f63c","resourceid":"7825a38ea4bc43c59e53a40bd5810439","type":1,"visiturl":"2.jpg"}],"rangelist":[{"createtime":"2016-07-21 13:55:16","rangename":"李语然","resourcetype":"2","resourceid":"7825a38ea4bc43c59e53a40bd5810439"},{"createtime":"2016-07-21 13:55:16","rangename":"徐汇区分公司","resourcetype":"2","resourceid":"7825a38ea4bc43c59e53a40bd5810439"}],"createtime":"2016-07-21 13:55","createid":"690fb669ed4d40219964baad7783abd4","organizename":"zhangjiangfendian","createname":"silence","realname":"silence","resulttext":"kou tou"}}
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getRewardDetailInfo" , method = RequestMethod.POST)
	public Map<String,Object> getRewardDetailInfo(@RequestParam Map<String,Object> map , 
			HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();

		if(!map.containsKey("rewardid") && !map.containsKey("resourceid")){
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：rewardid或者resourceid");
			return resultMap;
		}
		
		try {
			
			//查询信息
			Map<String, Object> rewardmap = this.rewardInfoService.getRewardDetailInfo(map);
			
			resultMap.put("status", 0);
			resultMap.put("message", "奖励单详情查询成功");
			resultMap.put("data", rewardmap);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("data", "");
			resultMap.put("status", 1);
			resultMap.put("message", "奖励单详情查询出错");
		}
		
		return resultMap;
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
	@RequestMapping(value="/insertRewardInfo" , method = RequestMethod.POST)
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
	 * 查询奖励类型
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getRewardTypeListInfo" , method = RequestMethod.POST)
	public Map<String,Object> getRewardTypeListInfo(@RequestParam Map<String,Object> map , 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			map.put("typeid", 1);
			List<Map<String,Object>> list = this.rewardInfoService.getDictionListInfo(map);
			if(list != null && list.size() > 0){
				resultMap.put("status", 0);
				resultMap.put("data", list);
			}else{
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
	 * 提交奖励单的审核信息
	 * 传入参数：
	 * result，opinion，updateid，punishid,companyid
	 * ---->参数全部传入
	 * 
	 * punishid=7825a38ea4bc43c59e53a40bd5810439&result=1&opinion=good is do&updateid=1&companyid=67702cc412264f4ea7d2c5f692070457
	 * {"punishid":"7825a38ea4bc43c59e53a40bd5810439","result":1,"opinion":"good is do","updateid":1,"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * {"message":"回复信息提交成功！","status":0}
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateRewarddInfo" , method = RequestMethod.POST)
	public Map<String,Object> updateRewarddInfo(@RequestParam Map<String,Object> map , 
			HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
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
	 				user.put("resourceid", map.get("rewardid"));
	 				user.put("resourcetype", 2);
	 				if(user.containsKey("organizeid") && !"".equals(user.get("organizeid"))){
	 					user.put("organizeid", user.get("organizeid"));
	 					user.put("type", 2);
	 					
	 					//添加到发布范围用户表
	 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
	 					for(Map<String, Object> userinfo:userinfolist){
	 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
	 						releaserangemap.put("companyid", map.get("companyid"));
	 						releaserangemap.put("resourceid", map.get("rewardid"));
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
						releaserangemap.put("resourceid", map.get("rewardid"));
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
	 * 查询处罚单 未处理，已处理的 未读数量
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getPunishAllNotRead")
	public Map<String,Object> getPunishAllNotRead(@RequestParam Map<String,Object> map , HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.rewardInfoService.getPunishListCount(map);
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.rewardInfoService.getPunishListTimesCount(map);
			
			resultMap.put("noexaminenum", num);
			resultMap.put("examinenum", count);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	
	/**
	 * 查询未审核的处罚单的信息
	 * 传入参数：
	 * receiveid：（用户的userid）,companyid
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getNoExaminePunishListInfo" , method = RequestMethod.POST)
	public Map<String,Object> getNoExaminePunishListInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		if(!map.containsKey("receiveid") || "".equals(map.get("receiveid"))){
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：receiveid");
			return resultMap;
		}
		
		try {
			map.put("status", 0);
			
			//创建分页对象
			PageScroll page = new PageScroll();
			//查询总的数据条数
			int count = this.rewardInfoService.getPunishListCount(map);
			//设置总数
			page.setTotalRecords(count);
			//初始化分页信息
			page.initPage(map);
			
			//查询信息
			List<Map<String,Object>> list = this.rewardInfoService.getPunishListInfo(map);
			if(list.size() > 0 && list != null){
				resultMap.put("data", list);
				resultMap.put("status", 0);
				resultMap.put("message", "未审核的处罚单信息查询成功！");
			}else{
				resultMap.put("data", "");
				resultMap.put("status", 1);
				resultMap.put("message", "未审核的处罚单信息，暂无数据");
			}
			resultMap.put("page", page);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			
			resultMap.put("data", "");
			resultMap.put("status", 1);
			resultMap.put("message", "未审核的处罚单信息查询出错了");
		}
		
		return resultMap;
	}
	
	
	/**
	 * 查询已经审核的处罚单信息
	 *  传入参数：
	 * receiveid：（用户的userid）,companyid
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getExaminedPunishListInfo" , method = RequestMethod.POST)
	public Map<String,Object> getExaminedPunishListInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		if(!map.containsKey("receiveid") || "".equals(map.get("receiveid"))){
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：receiveid");
			return resultMap;
		}
		
		try {
			map.put("status", 1);
			
			//创建分页对象
			PageScroll page = new PageScroll();
			//查询总的数据条数
			int count = this.rewardInfoService.getPunishListTimesCount(map);
			//设置总数
			page.setTotalRecords(count);
			//初始化分页信息
			page.initPage(map);
			
			//查询信息
			List<Map<String,Object>> list = this.rewardInfoService.getPunishListTimesInfo(map);
			if(list.size() > 0 && list != null){
				resultMap.put("data", list);
				resultMap.put("status", 0);
				resultMap.put("message", "已审核的处罚单信息查询成功！");
			}else{
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
	 * 查询惩罚单的详细信息
	 * 传入参数：
	 * punishid：惩罚单编号，companyid：所属公司id
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getPunishDetailInfo" , method = RequestMethod.POST)
	public Map<String,Object> getPunishDetailInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		String punishid = String.valueOf(map.get("punishid"));
		if(!map.containsKey("punishid") || "".equals(punishid)){
			resultMap.put("data", "");
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：punishid");
			return resultMap;
		}
		
		try {
			
			//查询惩罚单信息
			Map<String,Object> punishmap = this.rewardInfoService.getPunishDetailInfo(map);
			if(punishmap != null && punishmap.size() > 0){
				resultMap.put("data", punishmap);
				resultMap.put("status", 0);
				resultMap.put("message", "惩罚单详情查询成功");
			}else{
				resultMap.put("data", "");
				resultMap.put("status", 1);
				resultMap.put("message", "惩罚单详情查询成功,但是暂无数据");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("data", "");
			resultMap.put("status", 1);
			resultMap.put("message", "惩罚单详情查询出错了");
		}
		
		return resultMap;

	}
	
	
	/**
	 * 提交审核信息
	 * 传入参数：
	 * result，opinion，updatetime，updateid，status，punishid，companyid
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updatePunishInfo" , method = RequestMethod.POST)
	public Map<String,Object> updatePunishInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
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
	 * 添加处罚单信息
	 * 传入参数：
	 * companyid，punishuserid，realname，organizename，position，reason，punishresult，examineuserid，createid
	 * userlist(组织机构id，单个的用户的id)：发布范围，
	 * filelist(3张图片的路径)，
	 * sound(语音的路径)，
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertPunishInfo" , method = RequestMethod.POST)
	public Map<String,Object> insertPunishInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
	
		try {
			//添加处罚单信息
			String punishid = this.rewardInfoService.insertPunishInfo(map);
			
			//发布范围
			JSONObject json=JSONObject.fromObject(map.get("userlist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String, Object> user:userlist){
				user.put("companyid", map.get("companyid"));
 				user.put("resourceid", punishid);
 				user.put("resourcetype", 3);
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
			filemap.put("resourcetype", 8);
			
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
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getPunishTypeListInfo" , method = RequestMethod.POST)
	public Map<String,Object> getPunishTypeListInfo(@RequestParam Map<String,Object> map , 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			map.put("typeid", 5);
			List<Map<String,Object>> list = this.rewardInfoService.getDictionListInfo(map);
			if(list != null && list.size() > 0){
				resultMap.put("status", 0);
				resultMap.put("data", list);
			}else{
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
	
	
/*--------------------------------------------------------------------------报修单-----------------------------------------------------------------------*/
	
	
	/**
	 * 查询报修单 已处理 未处理 的 未读数量
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getRepairAllNotRead")
	public Map<String,Object> getRepairAllNotRead(@RequestParam Map<String,Object> map , HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.rewardInfoService.getRepairListCount(map);
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.rewardInfoService.getRepairTimesListCount(map);
			
			resultMap.put("noexaminenum", num);
			resultMap.put("examinenum", count);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	
	/**
	 * 查询未处理的报修单信息
	 * 传入参数：
	 * receiveid（用户的userid）
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getNoExamineRepairListInfo" , method = RequestMethod.POST)
	public Map<String,Object> getNoExamineRepairListInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		if(!map.containsKey("receiveid") || "".equals(map.get("receiveid"))){
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：receiveid");
			return resultMap;
		}
		
		try {
			map.put("status", 0);
			
			//创建分页对象
			PageScroll page = new PageScroll();
			//查询总的数据条数
			int count = this.rewardInfoService.getRepairListCount(map);
			//设置总数
			page.setTotalRecords(count);
			//初始化分页信息
			page.initPage(map);
			
			//查询信息
			List<Map<String,Object>> list = this.rewardInfoService.getRepairListInfo(map);
			if(list.size() > 0 && list != null){
				resultMap.put("data", list);
				resultMap.put("status", 0);
				resultMap.put("message", "未审核的报修单信息查询成功！");
			}else{
				resultMap.put("data", "");
				resultMap.put("status", 1);
				resultMap.put("message", "未审核的报修单信息，暂无数据");
			}
			resultMap.put("page", page);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			
			resultMap.put("data", "");
			resultMap.put("status", 1);
			resultMap.put("message", "未审核的报修单信息查询出错了");
		}
		
		return resultMap;
	}
	
	
	/**
	 * 查询已审核的报修单的信息
	 * 传入参数：
	 * receiveid（用户的userid）
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getExamineRepairListInfo" , method = RequestMethod.POST)
	public Map<String,Object> getExamineRepairListInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		if(!map.containsKey("receiveid") || "".equals(map.get("receiveid"))){
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：receiveid");
			return resultMap;
		}
		
		try {
			map.put("status", 1);
			
			//创建分页对象
			PageScroll page = new PageScroll();
			//查询总的数据条数
			int count = this.rewardInfoService.getRepairTimesListCount(map);
			//设置总数
			page.setTotalRecords(count);
			//初始化分页信息
			page.initPage(map);
			
			//查询信息
			List<Map<String,Object>> list = this.rewardInfoService.getRepairTimesListInfo(map);
			if(list.size() > 0 && list != null){
				resultMap.put("data", list);
				resultMap.put("status", 0);
				resultMap.put("message", "已审核的报修单信息查询成功！");
			}else{
				resultMap.put("data", "");
				resultMap.put("status", 1);
				resultMap.put("message", "已审核的报修单信息，暂无数据");
			}
			resultMap.put("page", page);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			
			resultMap.put("data", "");
			resultMap.put("status", 1);
			resultMap.put("message", "已审核的报修单信息查询出错了");
		}
		
		return resultMap;
	}
	
	
	/**
	 * 查询报修单的详细信息
	 * 传入参数：
	 * repairid，companyid
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getRepairDetailInfo" , method = RequestMethod.POST)
	public Map<String,Object> getRepairDetailInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		if(!map.containsKey("repairid") || "".equals(map.get("repairid"))){
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：repairid");
			return resultMap;
		}
		
		try {
			//查询报修单信息
			Map<String,Object> repairmap = this.rewardInfoService.getRepairDetailInfo(map);
			if(repairmap != null && repairmap.size() > 0){
				resultMap.put("data", repairmap);
				resultMap.put("status", 0);
				resultMap.put("message", "查询报修单详情成功");
			}else{
				resultMap.put("data", "");
				resultMap.put("status", 1);
				resultMap.put("message", "查询报修单详情成功,暂无数据");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("data", "");
			resultMap.put("status", 1);
			resultMap.put("message", "查询报修单详情出错");
		}
		
		return resultMap;
	}
	
	
	/**
	 * 提交抄送人意见信息
	 * 传入参数：
	 * result，opinion，updateid，repairid，companyid
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateRepairInfo" , method = RequestMethod.POST)
	public Map<String,Object> updateRepairInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		if(!map.containsKey("repairid") || "".equals(map.get("repairid"))){
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：repairid");
			return resultMap;
		}
		
		try {
			//提交信息
			this.rewardInfoService.updateRepairInfo(map);
			resultMap.put("status", 0);
			resultMap.put("message", "抄送人意见提交成功");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "抄送人意见提交失败");
		}
		
		return resultMap;
	}
	
	
	/**
	 * 添加报修单信息
	 * 传入参数：
	 * companyid，findtime，description，examineuserid，createid
	 * filelist(3张图片的路径)，
	 * sound(语音的路径)，
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertRepairInfo" , method = RequestMethod.POST)
	public Map<String,Object> insertRepairInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			
			//添加
			String repairid = this.rewardInfoService.insertRepairInfo(map);
			
			
			Map<String,Object> filemap = new HashMap<String,Object>();
			filemap.put("companyid", map.get("companyid"));
			filemap.put("resourceid", repairid);
			filemap.put("resourcetype", 13);
			
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
			resultMap.put("message", "报修单新增成功");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			
			resultMap.put("status", 1);
			resultMap.put("message", "报修单新增出错");
		}
		
		return resultMap;
	}
	
	
/*-----------------------------------------------------------------菜品成本---------------------------------------------------------------------------------*/

	/**
	 * 查询菜品成本未处理，已处理的未读数量
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getDishesAllNotRead")
	public Map<String,Object> getDishesAllNotRead(@RequestParam Map<String,Object> map , HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理未读数量
			map.put("status", 0);
			int count = this.rewardInfoService.getDishesListCount(map);
			//查询已处理未读数量
			map.put("status", 1);
			int num = this.rewardInfoService.getDishesListTimesCount(map);
			resultMap.put("noexamiennum", count);
			resultMap.put("examinenum", num);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	/**
	 * 根据区域id获取菜品类型
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getDishestypeListInfoByOrganizeId")
	public Map<String,Object> getDishestypeListInfoByOrganizeId(@RequestParam Map<String,Object> map ,
			HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String,Object>> dishestype = this.rewardInfoService.getDishestypeListInfoByOrganizeId(map);
			resultMap.put("data", dishestype);
			
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	
	
	/**
	 * 查询未处理的菜品成本列表信息
	 * 传入参数：
	 * receiveid（用户的userid）,companyid
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getNoExmaineDishesListInfo" , method = RequestMethod.POST)
	public Map<String,Object> getNoExmaineDishesListInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		if(!map.containsKey("receiveid") || "".equals(map.get("receiveid"))){
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：receiveid");
			return resultMap;
		}
		
		try {
			map.put("status", 0);
			
			//创建分页对象
			PageScroll page = new PageScroll();
			//查询总的数据条数
			int count = this.rewardInfoService.getDishesListCount(map);
			//设置总数
			page.setTotalRecords(count);
			//初始化分页信息
			page.initPage(map);
			
			//查询信息
			List<Map<String,Object>> list = this.rewardInfoService.getDishesListInfo(map);
			if(list.size() > 0 && list != null){
				resultMap.put("data", list);
				resultMap.put("status", 0);
				resultMap.put("message", "未审核的菜品成本信息查询成功！");
			}else{
				resultMap.put("data", "");
				resultMap.put("status", 1);
				resultMap.put("message", "未审核的菜品成本信息，暂无数据");
			}
			resultMap.put("page", page);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			
			resultMap.put("data", "");
			resultMap.put("status", 1);
			resultMap.put("message", "未审核的菜品成本信息查询出错了");
		}
		
		return resultMap;
	}
	
	
	/**
	 * 查询已经处理的菜品成本信息
	 * 传入参数：
	 * receiveid（用户的userid）,companyid
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getExmaineDishesListInfo" , method = RequestMethod.POST)
	public Map<String,Object> getExmaineDishesListInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		if(!map.containsKey("receiveid") || "".equals(map.get("receiveid"))){
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：receiveid");
			return resultMap;
		}
		
		try {
			map.put("status", 1);
			
			//创建分页对象
			PageScroll page = new PageScroll();
			//查询总的数据条数
			int count = this.rewardInfoService.getDishesListTimesCount(map);
			//设置总数
			page.setTotalRecords(count);
			//初始化分页信息
			page.initPage(map);
			
			//查询信息
			List<Map<String,Object>> list = this.rewardInfoService.getDishesListTimesInfo(map);
			if(list.size() > 0 && list != null){
				resultMap.put("data", list);
				resultMap.put("status", 0);
				resultMap.put("message", "已审核的菜品成本信息查询成功！");
			}else{
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
	 * 查询菜单成本信息详情
	 * 传入参数：
	 * dishesid，companyid
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getDishesDetailInfo" , method = RequestMethod.POST)
	public Map<String,Object> getDishesDetailInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		if(!map.containsKey("dishesid") || "".equals(map.get("dishesid"))){
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：dishesid");
			return resultMap;
		}
		
		try {
			//查询
			Map<String,Object> dishesmap = this.rewardInfoService.getDishesDetailInfo(map);
			if(dishesmap != null && dishesmap.size() > 0){
				resultMap.put("data", dishesmap);
				resultMap.put("status", 0);
				resultMap.put("message", "查询报修单详情成功");
			}else{
				resultMap.put("data", "");
				resultMap.put("status", 1);
				resultMap.put("message", "查询报修单详情成功,暂无数据");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("data", "");
			resultMap.put("status", 1);
			resultMap.put("message", "查询报修单详情失败");
		}
		
		return resultMap;
	}
	
	
	/**
	 * 查询菜品类型信息
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getDishesTypeListInfo" , method = RequestMethod.POST)
	public Map<String,Object> getDishesTypeListInfo(@RequestParam Map<String,Object> map , 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			map.put("typeid", 6);
			List<Map<String,Object>> list = this.rewardInfoService.getDictionListInfo(map);
			if(list != null && list.size() > 0){
				resultMap.put("status", 0);
				resultMap.put("data", list);
			}else{
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
	 * 提交抄送人意见
	 * 传入参数：
	 * result，opinion，updateid，dishesid，companyid
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateDishesInfo" , method = RequestMethod.POST)
	public Map<String,Object> updateDishesInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		if(!map.containsKey("dishesid") || "".equals(map.get("dishesid"))){
			resultMap.put("status", 1);
			resultMap.put("message", "未传入参数：dishesid");
			return resultMap;
		}
		
		try {
			//修改
			this.rewardInfoService.updateDishesInfo(map);
			resultMap.put("status", 0);
			resultMap.put("message", "抄送人意见提交成功");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "抄送人意见提交出错");
		}
		
		return resultMap;
	}
	
	
	/**
	 * 添加菜品成品信息
	 * 传入参数：
	 * companyid，orgid，dishestype，dishesname，feeding，costprice，price，costrate，description，examineuserid，createid，
	 * filelist(3张图片的路径)，
	 * sound(语音的路径)，
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertDishesInfo" , method = RequestMethod.POST)
	public Map<String,Object> insertDishesInfo(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			
			//添加
			String repairid = this.rewardInfoService.insertDishesInfo(map);
			
			Map<String,Object> filemap = new HashMap<String,Object>();
			filemap.put("companyid", map.get("companyid"));
			filemap.put("resourceid", repairid);
			filemap.put("resourcetype", 14);
			
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
	 * 修改信息的阅读状态
	 * resourceid，receiveid，resourcetype（以转发表中的类型id为标准）
	 * 
	 * 转发表：1：采购(入库)单 2：申购单3.用料单 4.退货单 5.报损单 6.岗位星值 7.综合星级 8.处罚单 9.奖励单 10.离店检查 11.餐前检查 12.厨房检查 13.报修单 14.菜品成平管理 15.通用审批 
	 * 		16.请示 17 报销 18.请假 19.任务 20 备用金申请,21 每日报表 22.巡店日志，23,日报24 周报 25月报，27 通知）
	 * 
	 * 发布范围表：1：通知 2：奖励单 3：处罚单 4日报 5周报 6 月报 7.简报8.检查模版,9.岗位星值项目 10  星值规则，11 每日报表）
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/upateUserNoReadStatus" , method = RequestMethod.POST)
	public Map<String,Object> upateUserNoReadStatus(@RequestParam Map<String,Object> map , 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			String[] forwardtypes = {"27","9","8","23","24","25","21"}; 
			String[] releaseRangetypes = {"1","2","3","4","5","6","11"};
			if(map.containsKey("resourcetype") && !"".equals(map.get("resourcetype"))){
				map.put("isread", 1);
				//修改转发表中的阅读状态
				this.rewardInfoService.updateForwardReadStatus(map);
				
				String type = String.valueOf(map.get("resourcetype"));
				for(int i=0 ; i<7; i++){
					if(type.equals(forwardtypes[i])){
						map.put("resourcetype", releaseRangetypes[i]);
						//修改发布范围表中的阅读状态
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
}
