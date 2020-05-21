package com.collection.controller.oa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.collection.service.IndexService;
import com.collection.service.oa.OfficeService;
import com.collection.util.PageScroll;
import com.collection.util.SDKTestSendTemplateSMS;

/**
 * oa办公管理
 * @author silence
 *
 */
@Controller
@RequestMapping("/app")
public class OfficeController extends BaseController{
	private transient static Log log = LogFactory.getLog(OfficeController.class);
	
	@Resource private OfficeService officeService;
	
	@Resource private IndexService indexService;
	
	
	/**
	 * 查询oa各功能的未读数量
	 * 
	 *  转发表：1：采购(入库)单 2：申购单3.用料单 4.退货单 5.报损单 6.岗位星值 7.综合星级 8.处罚单 9.奖励单 10.离店检查 11.餐前检查 12.厨房检查 13.报修单 14.菜品成平管理 15.通用审批 
	 * 		16.请示 17 报销 18.请假 19.任务 20 备用金申请,21 每日报表 22.巡店日志，23,日报24 周报 25月报，27 通知）
	 * 
	 * 发布范围表：1：通知 2：奖励单 3：处罚单 4日报 5周报 6 月报 7.简报8.检查模版,9.岗位星值项目 10  星值规则，11 每日报表）
	 * 
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getOANoReadNum" , method = RequestMethod.POST)
	public Map<String,Object> getOANoReadNum(@RequestParam Map<String,Object> map , 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//创建返回的map对象
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			
			String[] worktypeid = {"23","24","25","27","15","16","17","18","19","20"};
			String[] rangetype = {"4","5","6","1"};
			String[] worktypename = {"ribao","zhoubao","yuebao","tongzhi","shenpi","qingshi","baoxiao","qingjia","renwu","beiyongjin"};
			Map<String,Object> data = new HashMap<String,Object>();
			map.put("isread", 0);
			List<Integer> resourcetypes=null;
			for(int i=0 ; i < 10 ; i++){
				resourcetypes=new ArrayList<Integer>();
				resourcetypes.add(Integer.parseInt(worktypeid[i]));
				map.put("resourcetypes", resourcetypes);
				if(i<=3){
					List<Integer> rangetypes=new ArrayList<Integer>();
					rangetypes.add(Integer.parseInt(rangetype[i]));
					map.put("rangetypes", rangetypes);
				}
				int num=this.indexService.getForwordNotreadNum(map);
				data.put(worktypename[i], num);
				map.remove("rangetypes");
				map.remove("rangetypes");
			}
			resourcetypes=new ArrayList<Integer>();
			resourcetypes.add(7);
			map.put("rangetypes", resourcetypes);
			int jianbao = this.indexService.getRangeNotreadNum(map);
			data.put("jianbao", jianbao);
			
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
	 * 
	 * 新增企业简报模块
	 * 传入参数 {"companyid":"67702cc412264f4ea7d2c5f692070457","modulename":"公司新闻","moduleimage":"1.jpg","createid":"690fb669ed4d40219964baad7783abd4"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertCompanyModule")
	@ResponseBody
	public Map<String, Object> insertCompanyModule(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		this.officeService.insertCompanyModule(map);
		data.put("status", 0);
		data.put("message", "添加成功");
		return data;
	}
	
	/**
	 * 
	 * 企业简报模块列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数{"message":"查询成功","modulelist":[{"createtime":1468983040000,"companyid":"67702cc412264f4ea7d2c5f692070457","modulename":"公司新闻","moduleimage":"1.jpg","updatetime":1468983120000,"createid":"690fb669ed4d40219964baad7783abd4","moduleid":"fdd61606a640459081bce3d02dd8e01a"}],"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getCompanyModuleList")
	@ResponseBody
	public Map<String, Object> getCompanyModuleList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		List<Map<String, Object>> modulelist=this.officeService.getCompanyModuleList(map);
		data.put("modulelist", modulelist);
		data.put("status", 0);
 		data.put("message", "查询成功");
		return data;
	}
	
	/**
	 * 
	 * 新建简报
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","moduleid":"7205908da0924ecb96fa0e3e5659de04","title":"测试简报","content":"哈哈哈哈哈哈哈哈哈哈哈","userlist":"{"userlist":[{"userid":"1"},{"organizeid":"2"}]}"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 * 
	 */
	@RequestMapping("/insertBrief")
	@ResponseBody
	public Map<String, Object> insertBrief(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		String briefid="";
		try {
			briefid=this.officeService.insertBrief(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加简报错误");
			return data;
		}
		try{
			//发布范围
			JSONObject json=JSONObject.fromObject(map.get("userlist")+"");
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String, Object> user:userlist){
				user.put("companyid", map.get("companyid"));
 				user.put("resourceid", briefid);
 				user.put("resourcetype", 7);
 				if(user.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
 					user.put("organizeid", user.get("organizeid"));
 					user.put("type", 2);
 					
 					//添加到发布范围用户表
 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
 					for(Map<String, Object> userinfo:userinfolist){
 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
 						releaserangemap.put("companyid", map.get("companyid"));
 						releaserangemap.put("resourceid", briefid);
 						releaserangemap.put("resourcetype", 7);
 						releaserangemap.put("userid", userinfo.get("userid"));
 						releaserangemap.put("createid", map.get("createid"));
 						this.indexService.insertReleaseRangeUser(releaserangemap);
 					}
 				}
 				if(user.containsKey("userid") && !"".equals(map.get("userid"))){
 					user.put("userid", user.get("userid"));
 					user.put("type", 1);
 					
 					//添加到发布范围用户表
 					Map<String, Object>  releaserangemap=new HashMap<String, Object>();
					releaserangemap.put("companyid", map.get("companyid"));
					releaserangemap.put("resourceid", briefid);
					releaserangemap.put("resourcetype", 7);
					releaserangemap.put("userid", user.get("userid"));
					releaserangemap.put("createid", map.get("createid"));
					this.indexService.insertReleaseRangeUser(releaserangemap);
 				}
 				
 				//添加到发布范围表
				this.indexService.insertReleaseRange(user);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加发布范围错误");
			return data;
		}
		data.put("status", 0);
 		data.put("message", "添加成功");
		return data;
	}
	
	/**
	 * 简报列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4"}
	 * 传出参数{"briefList":[{"content":"哈哈哈哈哈哈哈哈哈哈哈","createtime":1468985819000,"title":"测试简报","createid":"690fb669ed4d40219964baad7783abd4","isread":0,"moduleid":"7205908da0924ecb96fa0e3e5659de04","briefid":"3ba46512f04449eea44ed9eb7817b98d"}],"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getBriefList")
	@ResponseBody
	public Map<String, Object> getBriefList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageScroll page = new PageScroll();
		int num=this.officeService.getBriefListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getBriefList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("briefList", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 简报详情
	 * 传入参数{"briefid":"3ba46512f04449eea44ed9eb7817b98d"}
	 * 传出参数{"message":"查询成功","status":0,"briefInfo":{"content":"哈哈哈哈哈哈哈哈哈哈哈","createtime":1468985819000,"title":"测试简报","createid":"690fb669ed4d40219964baad7783abd4","moduleid":"7205908da0924ecb96fa0e3e5659de04","rangelist":[{"createtime":1468985828000,"rangename":"李语然","resourcetype":"7","resourceid":"3ba46512f04449eea44ed9eb7817b98d"},{"createtime":1468985865000,"rangename":"浦东新区分公司","resourcetype":"7","resourceid":"3ba46512f04449eea44ed9eb7817b98d"}],"briefid":"3ba46512f04449eea44ed9eb7817b98d"}}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getBriefInfo")
	@ResponseBody
	public Map<String, Object> getBriefInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		Map<String, Object> briefInfo=this.officeService.getBriefInfo(map);
		data.put("briefInfo", briefInfo);
		data.put("status",0);
		data.put("message","查询成功");
		return data;
	}
	
	
	/**
	 * 餐饮大师云盘列表文件/文件夹  查询
	 * 传入参数{"type":1,"filetype":1,"parentid":1}
	 * 传出参数{"systemcloudlist":[{"createtime":1468994060000,"updatetime":1468994063000,"createid":"1","filename":"2.jpg","filetype":1,"parentid":"1","fileid":"2","type":1,"memory":10}],"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getSystemCloudList")
	@ResponseBody
	public Map<String, Object> getSystemCloudList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(!map.containsKey("parentid") || "".equals(map.get("parentid"))){
			map.put("isparentid", 1);
		}
		PageScroll page = new PageScroll();
		int num=this.officeService.getSystemCloudListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getSystemCloudList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("systemcloudlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 
	 * 新增企业云盘模块
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","modulename":"浦东店","moduleimage":"2.jpg","createid":"690fb669ed4d40219964baad7783abd4"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertCompanyCloudModule")
	@ResponseBody
	public Map<String, Object> insertCompanyCloudModule(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		this.officeService.insertCompanyCloudModule(map);
		data.put("status", 0);
		data.put("message", "添加成功");
		return data;
	}
	
	/**
	 * 
	 * 企业云盘模块列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数{"message":"查询成功","modulelist":[{"createtime":1468995146000,"companyid":"67702cc412264f4ea7d2c5f692070457","modulename":"浦东店","moduleimage":"2.jpg","updatetime":1468995527000,"createid":"690fb669ed4d40219964baad7783abd4","moduleid":"682d94bbccf24c8bab4a19fc24e87381"}],"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getCompanyCloudModuleList")
	@ResponseBody
	public Map<String, Object> getCompanyCloudModuleList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		List<Map<String, Object>> modulelist=this.officeService.getCompanyCloudModuleList(map);
		data.put("modulelist", modulelist);
		data.put("status", 0);
 		data.put("message", "查询成功");
		return data;
	}
	
	/**
	 * 企业云盘列表文件/文件夹  查询
	 * 传入参数{"filetype":1,"moduleid":"682d94bbccf24c8bab4a19fc24e87381","filename":"jpg","parentid":1}
	 * 传出参数{"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0,"companycloudlist":[{"createtime":1468996983000,"updatetime":1468996985000,"createid":"1","fileurl":"www.baidu.com","filename":"2.jpg","cloudid":"1","filetype":1,"parentid":"1","fileid":"2","memory":5}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getCompanyCloudList")
	@ResponseBody
	public Map<String, Object> getCompanyCloudList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
//		if(!map.containsKey("parentid") || "".equals(map.get("parentid"))){
//			map.put("isparentid", 1);
//		}
		PageScroll page = new PageScroll();
		int num=this.officeService.getCompanyCloudListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getCompanyCloudList(map);
		
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("companycloudlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 查询企业云盘文件信息详情
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getCompanyCloudInfo")
	public Map<String,Object> getCompanyCloudInfo(@RequestParam Map<String,Object> map ,
			HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			Map<String,Object> clouddetail = this.officeService.getCompanyCloudFileInfo(map);
			resultMap.put("data", clouddetail);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
		}
		return resultMap;
	}
	
	/**
	 * 员工关怀  直接进默认当月
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","monthnum":7}
	 * 传出参数{"userlist":[{"position":"店长","birthday":"7月19日","userid":"1","organizelist":[{"createtime":1468459014000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468459058000,"datacode":"001","address":"浦东新区","organizeid":"1","organizename":"上海紫痕软件有限公司","type":1}],"realname":"李语然"}],"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0,"month":"7"}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getUserBirthdayList")
	@ResponseBody
	public Map<String, Object> getUserBirthdayList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(!map.containsKey("monthnum") || "".equals(map.get("monthnum"))){
			SimpleDateFormat sdf=new SimpleDateFormat("MM");
			try {
				map.put("monthnum", Integer.parseInt(sdf.format(new Date()))+"");
			} catch (Exception e) {
				// TODO: handle exception
				log.debug("时间转换错误");
			}
			
		}
		PageScroll page = new PageScroll();
		int num=this.officeService.getUserBirthdayListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getUserBirthdayList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("userlist", datalist);
		data.put("monthnum", map.get("monthnum"));
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 荣誉榜
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","starttime":"2016-07-10 08:32:12","endtime":"2016-07-30 10:10:21"}
	 * 传出参数{"userlist":[{"createtime":1469000872000,"rewardid":"1","rewardresult":"口头嘉奖","realname":"李语然"}],"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getUserRewardList")
	@ResponseBody
	public Map<String, Object> getUserRewardList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageScroll page = new PageScroll();
		int num=this.officeService.getUserRewardListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getUserRewardList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("userlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/*-------------------------------------备用金申请------------------------------------------------*/
	
	
	/**
	 * 查询备用金 已处理，未处理 的未读数量
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getReserveAmountAllNotRead")
	public Map<String,Object> getReserveAmountAllNotRead(@RequestParam Map<String,Object> map , HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.officeService.getReserveAmountListNum(map);	
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.officeService.getReserveAmountByDateNum(map);	
			
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
	 * 添加备用金表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","reason":"生病需要资金","urgentlevel":2,"amount":500000,"usetime":"2016-07-25 08:24:21","returntime":"2016-08-21 08:00:00","remark":"麻烦了","examineuserid":1,"createid":"690fb669ed4d40219964baad7783abd4"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertReserveAmount")
	@ResponseBody
	public Map<String, Object> insertReserveAmount(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			String reserveamountid = this.officeService.insertReserveAmount(map);
			
			String level = String.valueOf(map.get("urgentlevel"));
			if("3".equals(level)){
				try {
					map.put("reserveamountid", reserveamountid);
					Map<String,Object> reservemap = this.officeService.getReserveAmountInfo(map);
					String phone = String.valueOf(reservemap.get("examinephone"));
					String examinename = String.valueOf(reservemap.get("examinename"));
					String createname = String.valueOf(reservemap.get("createname"));
					String content = "非常紧急备用金申请";
					SDKTestSendTemplateSMS.sendUrgentMessage(phone, examinename, createname, content);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
			data.put("status",0);
			data.put("message","添加成功");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			
		}
		return data;
	}
	
	/**
	 * 查询备用金表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0}
	 * 传出参数{"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0,"reserveamountlist":[{"reserveamountid":"40f352b2c0434fea89ba718f48d083b1","updatetime":1469001500000,"status":0,"remark":"麻烦了","reason":"生病需要资金","urgentlevel":2,"examineuserid":"1","createdate":"2016-07-20","returntime":1471737600000,"amount":500000,"createtime":1469001215000,"companyid":"67702cc412264f4ea7d2c5f692070457","createhour":"15:53","forwarduserid":"7573e593b2474e44bbc53c717c20305a","createid":"690fb669ed4d40219964baad7783abd4","usetime":1469406261000}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReserveAmountList")
	@ResponseBody
	public Map<String, Object> getReserveAmountList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		PageScroll page = new PageScroll();
		int num=this.officeService.getReserveAmountListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getReserveAmountList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("reserveamountlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 查询备用金表
	 * 传入参数 {"userid":1,"status":1,"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * status 1：已处理 0：未处理    starttime 和 endtime 不传 默认近7天
	 * 传出参数{"status":0,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"ordernum":1,"map":{"userid":"1","status":"1","companyid":"67702cc412264f4ea7d2c5f692070457","starttime":"2016-07-20","endtime":"2016-07-27","startnum":0,"rownum":10},"list":[{"createtime":"2016-07-25","count":1,"orderlist":[{"result":1,"updatetime":"2016-07-25 22:12:13","status":1,"materialprice":1200,"examineuserid":"1","updateid":"1","orderno":"SGD20160725195900001","examinename":"李语然","createtime":"20:00:50","companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":2,"opinion":"阿斯达斯","organizename":"浦东金桥分店","orderid":"4f9f463c50ea4267aef0efb619d4403c","createname":"silence"},{"result":1,"updatetime":"2016-07-25 22:12:13","status":1,"materialprice":1200,"examineuserid":"1","updateid":"1","orderno":"SGD20160725195900001","examinename":"李语然","createtime":"20:00:50","companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","organizeid":"5","maternum":2,"opinion":"阿斯达斯","organizename":"浦东金桥分店","orderid":"4f9f463c50ea4267aef0efb619d4403c","createname":"silence"}]}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReserveAmountByDate")
	@ResponseBody
	public Map<String, Object> getReserveAmountByDate(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
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
			cal.add(Calendar.DAY_OF_MONTH, -6);
			cal1.add(Calendar.DAY_OF_MONTH, 1);
			map.put("starttime",sdf.format(cal.getTime()));
			map.put("endtime",sdf.format(cal1.getTime()));
		}
		
		PageScroll page = new PageScroll();
		int num=this.officeService.getReserveAmountByDateNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> list=this.officeService.getReserveAmountByDate(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("list", list);
		data.put("page",page);
		data.put("map", map);
		data.put("status",0);
		return data;
	}
	
	
	/**
	 * 查询备用金表详情页面
	 * 传入参数{"reserveamountid":"40f352b2c0434fea89ba718f48d083b1"}
	 * 传出参数{"message":"查询成功","status":0,"reserveAmountInfo":{"reserveamountid":"40f352b2c0434fea89ba718f48d083b1","updatetime":1469001500000,"status":0,"remark":"麻烦了","reason":"生病需要资金","urgentlevel":2,"examineuserid":"1","returntime":1471737600000,"examinename":"李语然","amount":500000,"createtime":1469001503000,"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","usetime":1469406261000,"createname":"silence"}}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getReserveAmountInfo")
	@ResponseBody
	public Map<String, Object> getReserveAmountInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			//备用金表详情
			Map<String, Object> reserveamountinfo=this.officeService.getReserveAmountInfo(map);
			data.put("reserveamountinfo", reserveamountinfo);
			data.put("status", 0);
			data.put("message", "查询成功");
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "查询失败");
			return data;
		}
	}
	
	/**
	 * 审核备用金  同意或者拒绝
	 * 传入参数{"userid":1,"reserveamountid":"9d54541939f6455c8253a4df4f77328b","opinion":"ohmygod","result":1}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineReserveAmount")
	@ResponseBody
	public Map<String, Object> examineReserveAmount(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.officeService.updateReserveAmount(map);
		
		String level = String.valueOf(map.get("level"));
		if(!"1".equals(level)){
			try {
				String jinji = "紧急";
				if("3".equals(level)){
					jinji = "非常紧急";
				}
				Map<String,Object> reservemap = this.officeService.getReserveAmountInfo(map);
				String userid=reservemap.get("createid")+"";
				String title=reservemap.get("examinename")+ "已经审核您的"+jinji+"备用金申请,请查看";
				String url="/oa/reserve_detail.html?reserveamountid="+map.get("reserveamountid")+"&userid="+userid;
				JPushAndriodAndIosMessage(userid, title, url);
				
				if("3".equals(level)){
					String phone = String.valueOf(reservemap.get("createphone"));
					String examinename = String.valueOf(reservemap.get("createname"));
					String createname = "的非常紧急备用金";
					String content = "被"+String.valueOf(reservemap.get("examinename"))+"审核";
					//【餐饮大师】尊敬的{1}，您申请{2}已经{3}，请尽快处理！
					SDKTestSendTemplateSMS.sendExamineUrgentMessage(phone, examinename, createname, content);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	
	
	/*-------------------------------------任务------------------------------------------------*/
	
	
	/**
	 * 查询任务，未处理，已过期，已完成 的未读数量
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getTaskAllNotRead")
	public Map<String,Object> getTaskAllNotRead(@RequestParam Map<String,Object> map , HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			map.put("nooverdue", 1);
			int num=this.officeService.getTaskListNum(map);
			map.remove("nooverdue");
			
			//查询已过期的 未读数量
			map.put("isoverdue", 1);
			int overnum = this.officeService.getTaskTimsListCount(map);
			map.remove("isoverdue");
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.officeService.getTaskTimsListCount(map);	
			
			resultMap.put("noexaminenum", num);
			resultMap.put("overnum", overnum);
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
	 * 添加任务
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","title":"标题1","content":"内容内容","endtime":"2016-07-29 22:21:21","examineuserid":1,"userlist":"{"userlist":[{"assistuserid":"2"}]}","filelist":"{"filelist":[{"visiturl":"2.jpg","type":1,"size":1200}]}"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertTask")
	@ResponseBody
	public Map<String, Object> insertTask(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		String taskid="";
		try {
			taskid=this.officeService.insertTask(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		try {
			//添加到协办人员表
			if(map.containsKey("userlist") && !"".equals(map.get("userlist"))){
				JSONObject json=JSONObject.fromObject(map.get("userlist")+"");
				List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
				for(Map<String, Object> user:userlist){
					user.put("companyid", map.get("companyid"));
					user.put("taskid", taskid);
					user.put("createid", map.get("createid"));
					user.put("assistuserid", user.get("userid"));
					this.officeService.insertTaskAssist(user);
				}
			}
			Map<String,Object> filemap = new HashMap<String,Object>();
			filemap.put("companyid", map.get("companyid"));
			filemap.put("resourceid", taskid);
			filemap.put("resourcetype", 19);
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
			
			data.put("status",0);
			data.put("message","添加成功");
			return data;
			
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		
		
	}
	
	/**
	 * 查询任务 列表 传入 nooverdue=1 查询 未过期的    传入 isoverdue=1 查询已过期的（两者不可同时传入）
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0,"isoverdue":1}
	 * 传出参数{"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0,"tasklist":[{"createtime":1469002015000,"content":"任务内容","endtime":1469888481000,"companyid":"67702cc412264f4ea7d2c5f692070457","title":"任务标题","createhour":"16:06","forwarduserid":"15b5296695d94447a8380b2dd80d2e06","status":0,"createid":"690fb669ed4d40219964baad7783abd4","taskid":"c4df74e76e2d4927bf7bbb4b32d3b937","examineuserid":"1","createdate":"2016-07-20","createname":"silence","assistlist":[{"realname":"李大炮"}],"examinename":"李语然"}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getTaskList")
	@ResponseBody
	public Map<String, Object> getTaskList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		map.put("status", 0);
		map.put("nooverdue", 1);
		PageScroll page = new PageScroll();
		int num=this.officeService.getTaskListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getTaskList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("tasklist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	
	/**
	 * 查询已过期或者已完成的任务列表信息
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getTaskExamine" , method = RequestMethod.POST)
	public Map<String,Object> getTaskExamine(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			PageScroll page = new PageScroll();
			int num=this.officeService.getTaskTimsListCount(map);	
			page.setTotalRecords(num);
			page.initPage(map);
			List<Map<String, Object>> datalist=this.officeService.getTaskTimeList(map);
			
			if(datalist != null && datalist.size() > 0){
				resultMap.put("status", 0);
				resultMap.put("tasklist", datalist);
			}else{
				resultMap.put("status", 1);
				resultMap.put("tasklist", "");
			}
			resultMap.put("page",page);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("tasklist", "");
		}
		
		return resultMap;
	}
	
	/**
	 * 查询任务表详情页面
	 * 传入参数{"taskid":"000c8a6c252249ff9e31df34e1ec1a95"}
	 * 传出参数{"message":"查询成功","taskinfo":{"content":"内容内容","endtime":"2016-07-29 22:21:21","createtime":"2016-07-22 09:18:38","companyid":"67702cc412264f4ea7d2c5f692070457","title":"标题1","status":0,"createid":"690fb669ed4d40219964baad7783abd4","taskid":"000c8a6c252249ff9e31df34e1ec1a95","examineuserid":"1","createname":"silence","assistlist":[{"realname":"李大炮"}],"examinename":"李语然"},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getTaskInfo")
	@ResponseBody
	public Map<String, Object> getTaskInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			//备用金表详情
			Map<String, Object> taskinfo=this.officeService.getTaskInfo(map);
			data.put("taskinfo", taskinfo);
			data.put("status", 0);
			data.put("message", "查询成功");
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "查询失败");
			return data;
		}
	}
	
	/**
	 * 完成任务 （修改）
	 * 传入参数{"taskid":"000c8a6c252249ff9e31df34e1ec1a95","userid":"690fb669ed4d40219964baad7783abd4","opinion":"lalalla"}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineTask")
	@ResponseBody
	public Map<String, Object> examineTask(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.officeService.updateTask(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	
	
	/*-------------------------------------请假单------------------------------------------------*/
	
	
	/**
	 * 查询请假单，已处理，未处理 的 未读数量
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getLeaveAllNotRead")
	public Map<String,Object> getLeaveAllNotRead(@RequestParam Map<String,Object> map , HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.officeService.getLeaveListNum(map);
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.officeService.getLeaveTimesCount(map);		
			
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
	 * 查询请假类型列表
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getLeaveTypeList" , method = RequestMethod.POST)
	public Map<String,Object> getLeaveTypeList(@RequestParam Map<String,Object> map , 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		map.put("typeid", 2);
		List<Map<String, Object>> datalist=this.indexService.getDictData(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("leavetypelist", datalist);
		data.put("status",0);
		return data;
	}
	
	/**
	 * 添加请假单
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","leavetype":2,"starttime":"2016-07-24 08:00:00","endtime":"2016-07-28 10:00:00","daynum":4,"hournum":2,"reason":"我发高烧啦","examineuserid":1,"filelist":"{"filelist":[{"visiturl":"lalala.jpg","type":1,"size":1200}]}"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertLeave")
	@ResponseBody
	public Map<String, Object> insertLeave(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		String leaveid="";
		try {
			leaveid=this.officeService.insertLeave(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		try {
			//添加到 图片/录音表
			Map<String,Object> filemap = new HashMap<String,Object>();
			filemap.put("companyid", map.get("companyid"));
			filemap.put("resourceid", leaveid);
			filemap.put("resourcetype", 18);
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
			
			data.put("status",0);
			data.put("message","添加成功");
			return data;
			
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		
		
	}
	
	/**
	 * 查询请假列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0}
	 * 传出参数{"leavelist":[{"endtime":"2016-07-28 10:00:00","starttime":"2016-07-24 08:00:00","reason":"我发高烧啦","status":0,"isread":1,"examineuserid":"1","leavetypename":"病假","examinename":"李语然","createtime":"2016-07-22 09:41:48","companyid":"67702cc412264f4ea7d2c5f692070457","leaveid":"846aa3dec6f64728b04e7d4fab4431fa","createid":"690fb669ed4d40219964baad7783abd4","leavetype":"2","createname":"silence","daynum":4,"hournum":2}],"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getLeaveList")
	@ResponseBody
	public Map<String, Object> getLeaveList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		map.put("status", 0);
		PageScroll page = new PageScroll();
		int num=this.officeService.getLeaveListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getLeaveList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("leavelist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	
	/**
	 * 查询已经处理的请假单列表
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getExamineLeavelist" , method = RequestMethod.POST)
	public Map<String,Object> getExamineLeavelist(@RequestParam Map<String,Object> map , 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		map.put("status", 1);
		PageScroll page = new PageScroll();
		int num=this.officeService.getLeaveTimesCount(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getLeaveTimesList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("leavelist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 查询请假详情页面
	 * 传入参数{"leaveid":"846aa3dec6f64728b04e7d4fab4431fa"}
	 * 传出参数{"message":"查询成功","leaveinfo":{"endtime":"2016-07-28 10:00:00","starttime":"2016-07-24 08:00:00","reason":"我发高烧啦","status":0,"examineuserid":"1","leavetypename":"病假","filelist":[{"createtime":"2016-07-22 09:41:49","companyid":"67702cc412264f4ea7d2c5f692070457","resourcetype":18,"fileid":"0255b5a531b7491387b7c224429d1887","resourceid":"846aa3dec6f64728b04e7d4fab4431fa","type":1,"visiturl":"lalala.jpg","size":1200}],"examinename":"李语然","createtime":"2016-07-22 09:41:48","companyid":"67702cc412264f4ea7d2c5f692070457","leaveid":"846aa3dec6f64728b04e7d4fab4431fa","createid":"690fb669ed4d40219964baad7783abd4","leavetype":"2","createname":"silence","daynum":4,"hournum":2},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getLeaveInfo")
	@ResponseBody
	public Map<String, Object> getLeaveInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			//请假表详情
			Map<String, Object> leaveinfo=this.officeService.getLeaveInfo(map);
			data.put("leaveinfo", leaveinfo);
			data.put("status", 0);
			data.put("message", "查询成功");
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "查询失败");
			return data;
		}
	}
	
	/**
	 * 审批请假（修改）
	 * 传入参数{"leaveid":"846aa3dec6f64728b04e7d4fab4431fa","userid":"690fb669ed4d40219964baad7783abd4","opinion":"balalala","result":1}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineLeave")
	@ResponseBody
	public Map<String, Object> examineLeave(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.officeService.updateLeave(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	
	/*-------------------------------------报销单------------------------------------------------*/
	
	
	/**
	 * 查询报销单已处理，未处理的 未读数量
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getExpenseAllNotRead")
	public Map<String,Object> getExpenseAllNotRead(@RequestParam Map<String,Object> map , HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.officeService.getExpenseListNum(map);	
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.officeService.getExpenseTimesListCount(map);	
			
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
	 * 初始化添加报销（获取报销单编号）
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数{"message":"成功","status":0,"expenseno":"BXD20160722094800001"}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/initInsertExpense")
	@ResponseBody
	public Map<String, Object> initInsertExpense(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		if(map.containsKey("companyid") && !"".equals(map.get("companyid"))){
			int ordernum=this.officeService.getExpenseNumByCompany(map);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmm");
			data.put("status", 0);
			data.put("expenseno","BXD"+sdf.format(new Date())+String.format("%05d",(ordernum+1)));
			data.put("message", "成功");
		}else{
			data.put("status", 1);
			data.put("message", "传入参数错误");
		}
		return data;
	}
	/**
	 * 添加报销单
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","expenseno":"BXD20160722094800001","examineuserid":1,"createid":"690fb669ed4d40219964baad7783abd4","detailprice":100,"detailnum":1,"detaillist":"{"detaillist":[{"type":"衣服","price":100,"detail":"明细明细"}]}"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertExpense")
	@ResponseBody
	public Map<String, Object> insertExpense(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		String expenseid="";
		try {
			expenseid=this.officeService.insertExpense(map);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		try {
			//添加到报销明细表
			JSONObject json=JSONObject.fromObject(map.get("detaillist")+"");
			List<Map<String, Object>> detaillist=(List<Map<String, Object>>)json.get("detaillist");
			for(Map<String, Object> detail:detaillist){
				detail.put("createid", map.get("createid"));
				detail.put("expenseid", expenseid);
				this.officeService.insertExpenseDetail(detail);
			}
			
			data.put("status",0);
			data.put("message","添加成功");
			return data;
			
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		
		
	}
	
	/**
	 * 查询报销列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0}
	 * 传出参数{"expenselist":[{"detailprice":100,"updatetime":"0000-00-00 00:00:00","detailnum":1,"status":0,"isread":1,"examineuserid":"1","examinename":"李语然","createtime":"2016-07-22 09:54:32","companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","expenseid":"75a3c5bc30b545c2a76eaeb3521423a8","createname":"silence","expenseno":"BXD20160722094800001"}],"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getExpenseList")
	@ResponseBody
	public Map<String, Object> getExpenseList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		map.put("status", 0);
		PageScroll page = new PageScroll();
		int num=this.officeService.getExpenseListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getExpenseList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("expenselist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 查询已经处理了的报销单
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getExamineExpenseList" , method = RequestMethod.POST)
	public Map<String,Object> getExamineExpenseList(@RequestParam Map<String,Object> map , 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		map.put("status", 1);
		PageScroll page = new PageScroll();
		int num=this.officeService.getExpenseTimesListCount(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getExpenseTimesList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("expenselist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 查询报销详情页面
	 * 传入参数{"expenseid":"75a3c5bc30b545c2a76eaeb3521423a8"}
	 * 传出参数{"message":"查询成功","status":0,"expenseinfo":{"createtime":"2016-07-22 09:54:32","companyid":"67702cc412264f4ea7d2c5f692070457","detailprice":100,"updatetime":"0000-00-00 00:00:00","status":0,"createid":"690fb669ed4d40219964baad7783abd4","detailnum":1,"examineuserid":"1","expenseid":"75a3c5bc30b545c2a76eaeb3521423a8","expenseno":"BXD20160722094800001","createname":"silence","examinename":"李语然","expensedetaillist":[{"createtime":1469152472000,"detail":"明细明细","price":100,"createid":"690fb669ed4d40219964baad7783abd4","expenseid":"75a3c5bc30b545c2a76eaeb3521423a8","expensedetailid":"3b63f0eba0994d7eb2ddad1062dba744","type":"衣服"}]}}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getExpenseInfo")
	@ResponseBody
	public Map<String, Object> getExpenseInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			Map<String, Object> expenseinfo=this.officeService.getExpenseInfo(map);
			data.put("expenseinfo", expenseinfo);
			data.put("status", 0);
			data.put("message", "查询成功");
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "查询失败");
			return data;
		}
	}
	
	/**
	 * 审批报销（修改）
	 * 传入参数{"expenseid":"75a3c5bc30b545c2a76eaeb3521423a8","userid":1,"opinion":"bulubulu","result":1}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineExpense")
	@ResponseBody
	public Map<String, Object> examineExpense(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.officeService.updateExpense(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	
	/*-------------------------------------请示----------------------------------------------*/
	
	
	/**
	 * 查询请示单 已处理，未处理的 未读数量
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getRequestAllNotRead")
	public Map<String,Object> getRequestAllNotRead(@RequestParam Map<String,Object> map , HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			map.put("isread", 0);
			
			//查询未处理的 未读数量
			map.put("status", 0);
			int num=this.officeService.getRequestListNum(map);	
			
			//查询已处理的为读数量
			map.put("status", 1);
			int count=this.officeService.getRequestTimesCount(map);	
			
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
	 * 添加请示单
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","reason":"我要一个奖励","content":"要要要","examineuserid":1,"urgentlevel":1,"filelist":"{"filelist":[{"visiturl":"bulubulu.jpg","type":1,"size":1200}]}"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertRequest")
	@ResponseBody
	public Map<String, Object> insertRequest(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>();
		String requestid="";
		try {
			requestid=this.officeService.insertRequest(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
		try {
			Map<String,Object> filemap = new HashMap<String,Object>();
			filemap.put("companyid", map.get("companyid"));
			filemap.put("resourceid", requestid);
			filemap.put("resourcetype", 16);
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
			
			try {
				String level = String.valueOf(map.get("urgentlevel"));
				if("3".equals(level)){
					map.put("requestid", requestid);
					Map<String,Object> askmap = this.officeService.getRequestInfo(map);
					String phone = String.valueOf(askmap.get("examinephone"));
					String examinename = String.valueOf(askmap.get("examinename"));
					String createname = String.valueOf(askmap.get("createname"));
					String content = "一个非常紧急请示";
					SDKTestSendTemplateSMS.sendUrgentMessage(phone, examinename, createname, content);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			data.put("status",0);
			data.put("message","添加成功");
			return data;
			
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
			data.put("message","添加失败");
			return data;
		}
	}
	
	/**
	 * 查询请示列表
	 * 传入参数{"companyid":"67702cc412264f4ea7d2c5f692070457","userid":"690fb669ed4d40219964baad7783abd4","status":0}
	 * 传出参数{"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0,"requestlist":[{"content":"要要要","createtime":"2016-07-22 10:08:52","companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":"2016-07-22 10:09:26","reason":"我要一个奖励","status":0,"createid":"690fb669ed4d40219964baad7783abd4","isread":1,"urgentlevel":1,"examineuserid":"1","createname":"silence","requestid":"3c763b6d99cc4ef5a019c06c73ec93f2","examinename":"李语然"}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getRequestList")
	@ResponseBody
	public Map<String, Object> getRequestList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		map.put("status", 0);
		PageScroll page = new PageScroll();
		int num=this.officeService.getRequestListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getRequestList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("requestlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 查询已经处理的请示单信息
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getExamineRequestList" , method = RequestMethod.POST)
	public Map<String,Object> getExamineRequestList(@RequestParam Map<String,Object> map , HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		map.put("status", 1);
		PageScroll page = new PageScroll();
		int num=this.officeService.getRequestTimesCount(map);	
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> datalist=this.officeService.getRequestTimesList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("requestlist", datalist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 查询报销详情页面
	 * 传入参数{"requestid":"3c763b6d99cc4ef5a019c06c73ec93f2"}
	 * 传出参数{"message":"查询成功","status":0,"requestinfo":{"createtime":"2016-07-22 10:08:52","content":"要要要","companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":"2016-07-22 10:09:26","createid":"690fb669ed4d40219964baad7783abd4","status":0,"reason":"我要一个奖励","urgentlevel":1,"examineuserid":"1","createname":"silence","filelist":[{"createtime":"2016-07-22 10:08:53","companyid":"67702cc412264f4ea7d2c5f692070457","resourcetype":16,"fileid":"efa73e81c1794840b8269cc0d2f79b16","resourceid":"3c763b6d99cc4ef5a019c06c73ec93f2","type":1,"visiturl":"bulubulu.jpg","size":1200}],"requestid":"3c763b6d99cc4ef5a019c06c73ec93f2","examinename":"李语然"}}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getRequestInfo")
	@ResponseBody
	public Map<String, Object> getRequestInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			Map<String, Object> requestinfo=this.officeService.getRequestInfo(map);
			data.put("requestinfo", requestinfo);
			data.put("status", 0);
			data.put("message", "查询成功");
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "查询失败");
			return data;
		}
	}
	
	/**
	 * 审批报销（修改）
	 * 传入参数{"requestid":"3c763b6d99cc4ef5a019c06c73ec93f2","userid":"690fb669ed4d40219964baad7783abd4","opinion":"LALALA","result":1}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineRequest")
	@ResponseBody
	public Map<String, Object> examineRequest(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.officeService.updateRequest(map);
		
		try {

			Map<String,Object> askmap = this.officeService.getRequestInfo(map);
			String level = String.valueOf(map.get("level"));
			if("2".equals(level)){
				try {
					//推送信息
					String userid=askmap.get("createid")+"";
					String title=askmap.get("examinename") + "已经审批您的紧急请示,点击查看";
					String url="/oa/ask_detail.html?requestid="+map.get("requestid")+"&userid="+userid;
					JPushAndriodAndIosMessage(userid, title, url);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}else if("3".equals(level)){
				try {
					//推送信息
					String userid=askmap.get("createid")+"";
					String title=askmap.get("examinename") + "已经审批您的非常紧急请示,点击查看";
					String url="/oa/ask_detail.html?requestid="+map.get("requestid")+"&userid="+userid;
					JPushAndriodAndIosMessage(userid, title, url);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				String phone = String.valueOf(askmap.get("createphone"));
				String examinename = String.valueOf(askmap.get("createname"));
				String content = "的非常紧急请示";
				String remark = "被"+askmap.get("examinename")+"审核";
				SDKTestSendTemplateSMS.sendExamineUrgentMessage(phone, examinename, content, remark);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
}
