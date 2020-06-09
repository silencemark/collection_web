package com.collection.controller.personal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

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

import com.alibaba.fastjson.JSONArray;
import com.base.controller.BaseController;
import com.collection.redis.RedisUtil;
import com.collection.service.CompanyService;
import com.collection.service.IndexService;
import com.collection.service.UserInfoService;
import com.collection.service.personal.PersonalService;
import com.collection.util.Constants;
import com.collection.util.Generate_Verification_Code;
import com.collection.util.Md5Util;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;
import com.collection.util.QRcode;
import com.collection.util.SDKTestSendTemplateSMS;
import com.collection.util.UserUtil;
import com.collection.util.email.EmailUtil;

/**
 * 个人中心
 *
 */
@Controller
@RequestMapping("/app")
public class PersonalAppController extends BaseController {

	private transient static Log log = LogFactory
			.getLog(PersonalAppController.class);
	@Resource private IndexService indexService;
	@Resource
	PersonalService personalService;

	@Resource
	CompanyService companyService;
	
	@Resource private UserInfoService userInfoService;
	
	/**
	 * 修改用户手机号码 传入参数： phone，userid，password,V_Code(验证码) -------> 所有的参数必填
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUserPhone", method = RequestMethod.POST)
	public Map<String, Object> updateUserPhone(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 根据手机号发送验证码--未做
		// 获取发送到手机的验证码
		//String V_Code = "1111";// (String)
		Map<String , Object> codemap=RedisUtil.getObject(map.get("phone")+"");
		String V_Code="";
		if(codemap != null && codemap.size() >0){
			V_Code=codemap.get("code")+"";
		}
		// 验证 验证码
		if (!map.containsKey("V_Code") || !V_Code.equals(map.get("V_Code"))) {
			resultMap.put("status", 1);
			resultMap.put("message", "验证码错误");
			return resultMap;
		}

		if (!map.containsKey("password") || "".equals(map.get("password"))) {
			resultMap.put("status", 1);
			resultMap.put("message", "密码不能为空");
			return resultMap;
		}

		try {
			// 密码加密
			map.put("password",
					Md5Util.getMD5(String.valueOf(map.get("password"))));
			// 验证密码是否正确
			Map<String, Object> vmap = this.personalService
					.getVerificationPwd(map);
			if (vmap == null || vmap.size() < 1) {
				resultMap.put("status", 1);
				resultMap.put("message", "密码错误");
				return resultMap;
			}

			// 修改
			this.personalService.updateUserInfoPhoneAndPwd(map);
			resultMap.put("status", 0);
			resultMap.put("message", "电话号码修改成功");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "电话号码修改出错");
		}

		return resultMap;
	}

	/**
	 * 添加反馈意见 companyid，description，createid -------> 所有的参数必填
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/insertFeedBackInfo", method = RequestMethod.POST)
	public Map<String, Object> insertFeedBackInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (!map.containsKey("description")
				|| "".equals(map.get("description"))) {
			resultMap.put("status", 1);
			resultMap.put("message", "反馈内容不能为空");
			return resultMap;
		}
		try {
			this.personalService.insertFeedBackInfo(map);
			resultMap.put("status", 0);
			resultMap.put("message", "添加反馈意见信息成功");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "添加反馈意见信息出错");
		}

		return resultMap;
	}

	/**
	 * 查询反馈信息列表 传入参数： companyid，createid(必填) 分页参数（必填）
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getFeedBackListInfo", method = RequestMethod.POST)
	public Map<String, Object> getFeedBackListInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			// 创建分页对象
			PageScroll page = new PageScroll();
			// 查询信息总数
			int count = this.personalService.getFeedBackListCount(map);
			// 设置总数
			page.setTotalRecords(count);
			// 初始化分页信息
			page.initPage(map);

			// 查询反馈信息
			List<Map<String, Object>> list = this.personalService
					.getFeedBackListInfo(map);
				resultMap.put("status", 0);
				resultMap.put("data", list);
				resultMap.put("message", "反馈信息查询成功");
			
			resultMap.put("page", page);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("data", "");
			resultMap.put("message", "反馈信息查询出错");
		}

		return resultMap;
	}

	/**
	 * 查询反馈信息的详情 传入参数： companyid，createid，feedbackid（必填）
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getFeedBackDetailInfo", method = RequestMethod.POST)
	public Map<String, Object> getFeedBackDetailInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			List<Map<String, Object>> list = this.personalService
					.getFeedBackListInfo(map);
			if (list != null && list.size() > 0) {
				Map<String, Object> feedbackmap = list.get(0);
				List<Map<String, Object>> feedbacklist = this.personalService
						.getFeedBackReplyListInfo(map);
				feedbackmap.put("replylist", feedbacklist);
				resultMap.put("data", feedbackmap);
				resultMap.put("status", 0);
				resultMap.put("message", "查询成功");
			} else {
				resultMap.put("data", "");
				resultMap.put("status", 1);
				resultMap.put("message", "查询成功,暂无数据");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("data", "");
			resultMap.put("status", 1);
			resultMap.put("message", "查询出错");
		}

		return resultMap;
	}
	
	/**
	 * 修改反馈信息是否已读 传入参数： companyid，createid，feedbackid,isread（必填）
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUpdateFeedBackDetailInfo", method = RequestMethod.POST)
	public Map<String, Object> getUpdateFeedBackDetailInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
					this.personalService.getUpdateFeedBackDetailInfo(map);
					resultMap.put("status", 0);
					resultMap.put("message", "修改成功");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "修改出错");
		
		}

		return resultMap;
	}

	/**
	 * 修改密码 new_password，userid，password ------>全部必填
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUserPassWordInfo", method = RequestMethod.POST)
	public Map<String, Object> updateUserPassWordInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (!map.containsKey("new_password") || !map.containsKey("password")
				|| "".equals(map.get("password"))
				|| "".equals(map.get("new_password"))) {
			resultMap.put("status", 1);
			resultMap.put("message", "密码不能为空");
			return resultMap;
		}

		try {
			map.put("new_password",
					Md5Util.getMD5(String.valueOf(map.get("new_password"))));
			map.put("password",
					Md5Util.getMD5(String.valueOf(map.get("password"))));
			// 验证密码是否正确
			Map<String, Object> vmap = this.personalService
					.getVerificationPwd(map);
			if (vmap.size() < 1 && vmap == null) {
				resultMap.put("status", 1);
				resultMap.put("message", "密码错误");
				return resultMap;
			}
			// 修改
			this.personalService.updateUserInfoPhoneAndPwd(map);
			resultMap.put("status", 0);
			resultMap.put("message", "密码修改成功");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "密码修改出错");
		}

		return resultMap;
	}

	/**
	 * 查询公司的基本信息 传入参数： companyid
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCompanyBasicInfo", method = RequestMethod.POST)
	public Map<String, Object> getCompanyBasicInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (!map.containsKey("companyid") || "".equals(map.get("companyid"))) {
			resultMap.put("status", 1);
			resultMap.put("data", "");
			resultMap.put("message", "未传入参数：companyid");
		}

		try {
			Map<String, Object> companymap = this.personalService
					.getCompanyBasicInfo(map);
			if (companymap != null && companymap.size() > 0) {
				resultMap.put("status", 0);
				resultMap.put("data", companymap);
				resultMap.put("message", "公司信息查询成功");
			} else {
				resultMap.put("status", 1);
				resultMap.put("data", "");
				resultMap.put("message", "公司信息查询成功,暂无数据");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("data", "");
			resultMap.put("message", "公司信息查询出错");
		}

		return resultMap;
	}

	/**
	 * 修改公司名称 传入参数： companyname，companyid
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateCompanyNameInfo", method = RequestMethod.POST)
	public Map<String, Object> updateCompanyNameInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			this.personalService.updateCompanyNameInfo(map);
			resultMap.put("status", 0);
			resultMap.put("message", "公司名称修改成功");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "公司名称修改出错");
		}

		return resultMap;
	}

	/**
	 * 查询组织架构列表信息 传入参数： companyid（必填），type，parentid(查询一级组织架构则不填写)，search
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrganizeListInfo", method = RequestMethod.POST)
	public Map<String, Object> getOrganizeListInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (!map.containsKey("companyid") || "".equals(map.get("companyid"))) {
			resultMap.put("status", 1);
			resultMap.put("data", "");
			resultMap.put("message", "未传入参数：companyid");
		}

		if (!map.containsKey("parentid") || "".equals(map.get("parentid"))) {
			map.put("parentid", 0);
		}
		try {
			List<Map<String, Object>> list = this.personalService
					.getOrganizeListInfo(map);
			if (list != null && list.size() > 0) {
				resultMap.put("status", 0);
				resultMap.put("data", list);
				resultMap.put("message", "公司组织架构查询成功");
			} else {
				resultMap.put("status", 1);
				resultMap.put("data", "");
				resultMap.put("message", "公司组织架构查询成功,暂无数据");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("data", "");
			resultMap.put("message", "公司组织架构查询失败");
		}

		return resultMap;
	}

	/**
	 * 查询组织架构详细信息 organizeid（必填），companyid
	 * 
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrganizeDetailInfo", method = RequestMethod.POST)
	public Map<String, Object> getOrganizeDetailInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (!map.containsKey("organizeid") || "".equals(map.get("organizeid"))) {
			resultMap.put("status", 1);
			resultMap.put("data", "");
			resultMap.put("message", "未传入参数：organizeid");
		}
		try {
			// 查询信息
			Map<String, Object> organizemap = this.personalService
					.getOrganizeDetailInfo(map);
			if (organizemap != null && organizemap.size() > 0) {
				resultMap.put("status", 0);
				resultMap.put("data", organizemap);
				resultMap.put("message", "组织详情查询成功");
			} else {
				resultMap.put("status", 1);
				resultMap.put("data", "");
				resultMap.put("message", "组织详情查询成功,暂无数据");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("data", "");
			resultMap.put("message", "组织详情查询出错");
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
	@RequestMapping(value = "/updateOrganizeInfo", method = RequestMethod.POST)
	public Map<String, Object> updateOrganizeInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(map.containsKey("updatadatacode") && map.get("updatadatacode") != ""){
			List<String> resourcetypes=new ArrayList<String>();
			String str =  map.get("updatadatacode").toString();
			for(int i=0;i<str.length()/3;i++){
				String code = str.substring(0,str.length()-(3*i));
				resourcetypes.add(code);
			}
			map.put("resourcetypes", resourcetypes);
		}
	
		
		try {
			
			// 查询下级的店面数
			int countTypelower;
			// 查询上级的店面数
			int countType =  0;
			
			if(map.containsKey("resourcetypes") && map.get("resourcetypes") != ""){
				countType =  this.personalService.selectOrganizeType(map);
			}
			int type = Integer.parseInt(map.get("updatatype").toString()) ;
			boolean flag  =false;
			/*
			 *1.判断修改的type是否等于3，如果等于则判断目标组织的上级type=3是否有数据
			 *2. 如果修改的type不等于3，那么判断当前组织下级和目标组织上级是否有店面
			 */
			if(type==3){
				if(countType==0){
					countTypelower = this.personalService.selectOrganizeTypelower(map);
					if(countType==0 && countTypelower==0){
						flag = true;
					} 
				}
			}else{
				flag = true;
			}
			if(flag){
				this.personalService.updateOrganizeInfo(map);
				resultMap.put("message", "修改成功");
			}else{
				resultMap.put("status", 1);
				resultMap.put("message", "店面不允许出现店面组织架构");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "修改出错");
		}

		return resultMap;
	}

	/**
	 * 删除组织架构
	 * 传入参数： companyid，organizeid,datacode
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateOrganizeDelete", method = RequestMethod.POST)
	public Map<String, Object> updateOrganizeDelete(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			map.put("delflag", 1);
			// 查询是否有子级 1等于没有
			int num =  this.personalService.selectDataCode(map);
			if(num == 1){
				// 删除当前组织架构
				this.personalService.updateOrganizeDelete(map);
				resultMap.put("message", "删除成功");
			}else{
				resultMap.put("message", "当前组织架构下有机构,不可删除");
			}
			resultMap.put("status", 0);
		
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "删除出错");
		}

		return resultMap;
	}

	/**
	 * 新增组织架构信息 传入参数：
	 * datacode，companyid（必填），organizename（必填），type，parentid，address |
	 * 如果该组织架构没有上级，则parentid不传值
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/insertOrganizeInfo", method = RequestMethod.POST)
	public Map<String, Object> insertOrganizeInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response,HttpServletRequest request) {
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
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
			
			String organizeid=UUID.randomUUID().toString().replaceAll("-", "");
			
			//生成qrcode
			String codeContent=Constants.PROJECT_PATH+"app/restaurant/appraise_add.html?organizeid="+organizeid;
			QRcode qr=new QRcode();
			String qrcode=qr.getQRcode(codeContent, request, organizeid);
			
			map.put("qrcode", qrcode);
			map.put("organizeid", organizeid);
			
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
	 * 查询组织架构下的人员信息 传入参数： organizeid，status（必填），search companyid（必填），parentid
	 * 
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrganizeUserListInfo", method = RequestMethod.POST)
	public Map<String, Object> getOrganizeUserListInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (!map.containsKey("companyid") || "".equals(map.get("companyid"))) {
			resultMap.put("status", 1);
			resultMap.put("data", "");
			resultMap.put("message", "未传入参数：companyid");
		}
		if (!map.containsKey("status") || "".equals(map.get("status"))) {
			resultMap.put("status", 1);
			resultMap.put("data", "");
			resultMap.put("message", "未传入参数：status");
		}
		if (!map.containsKey("parentid") || "".equals(map.get("parentid"))) {
			map.put("parentid", 0);
		}

		try {
			// 查询公司组织架构列表信息
			List<Map<String, Object>> organizelist = this.personalService
					.getOrganizeListInfo(map);

			// 如果该组织架构为一级且parentid=0，则organizeid=companyid
			if (map.containsKey("parentid") && "0".equals(map.get("parentid"))) {
				map.put("organizeid", map.get("companyid"));
			}
			// 查询组织架构下人员信息
			List<Map<String, Object>> organizeuserlist = this.personalService
					.getOrganizeUserListInfo(map);

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("organizelist", organizelist);
			paramMap.put("organizeuserlist", organizeuserlist);

			resultMap.put("status", 0);
			resultMap.put("data", paramMap);
			resultMap.put("message", "查询成功");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("data", "");
			resultMap.put("message", "查询出错");
		}

		return resultMap;
	}

	/**
	 * 查询组织人员信息详情 传入参数： manageid（必填），companyid
	 * 
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrganizeUserDetailInfo", method = RequestMethod.POST)
	public Map<String, Object> getOrganizeUserDetailInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			Map<String, Object> detailmap = this.personalService
					.getOrganizeUserDetailInfo(map);

			if (detailmap != null && detailmap.size() > 0) {
				resultMap.put("data", detailmap);
				resultMap.put("status", 0);
				resultMap.put("message", "查询成功");
			} else {
				resultMap.put("data", "");
				resultMap.put("status", 1);
				resultMap.put("message", "查询成功,暂无数据");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("data", "");
			resultMap.put("status", 1);
			resultMap.put("message", "查询失败");
		}

		return resultMap;
	}
	/**
	 * 新增组织下人员的基本信息 传入参数： 
	 *
	 * 
	 * userid，realname，position，phone,companyid,sex,birthday
	 * 
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/insertOrganizeUserInfo", method = RequestMethod.POST)
	public Map<String, Object> insertOrganizeUserInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response , HttpServletRequest request) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if(!map.containsKey("organizelist") || "".equals(map.get("organizelist"))){
			resultMap.put("status", 1);
			resultMap.put("message", "请选择组织架构");
			return resultMap;
		}
		
		try {
			int count = this.personalService.getUserInfoCount(map);
			if(count == 0 ){
				map.put("createtime", new Date());
				map.put("getRealPath", request.getSession().getServletContext().getRealPath(""));
				map.put("fileDirectory", Constants.FILEDIRECTORY);
				this.personalService.insertUserInfo(map);
				resultMap.put("status", 0);
				resultMap.put("message", "添加成功");
			}else{
				resultMap.put("status", 2);
				resultMap.put("message", "当前手机用户已存在");
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "添加出错");
		}

		return resultMap;
	}

	/**
	 * 修改组织和用户的基本信息 传入参数： 
	 * organizeid，manageid
	 *
	 * 
	 * userid，realname，position，phone,companyid,sex,birthday
	 * 
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateOrganizeUserInfo", method = RequestMethod.POST)
	public Map<String, Object> updateOrganizeUserInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response , HttpServletRequest request) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//修改当前部门的usernum-1,然后获取修改后的部门usernum+1并修改该用户的部门
			if(map.containsKey("organizelist") && !"".equals(map.get("organizelist"))){
				JSONObject json=JSONObject.fromObject(map.get("organizelist")+"");
				List<Map<String, Object>> organizelist=(List<Map<String, Object>>)json.get("organizelist");
				if(organizelist!= null && organizelist.size() > 0){
					//删除用户原有的组织
					this.personalService.deleteUserorganize(map);
					
					for(Map<String, Object> organize:organizelist){
						Map<String, Object> organizemap=new HashMap<String, Object>();
						organizemap.put("organizeid",organize.get("organizeid"));
						organizemap.put("userid",map.get("userid"));
						organizemap.put("companyid",map.get("companyid"));
						organizemap.put("createid", map.get("updateid"));
						this.companyService.insertOrganizeUser(organizemap);
						organizemap.put("usernum", 1);
						this.personalService.updateUserOrganizeAdd(organizemap);
					}
				}
			}
			
			/*map.put("usernum",1);
			map.put("updatetime", new Date());
			if(map.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
		
				//如果没传manageid  那么久获取当前的的manageid
				if(!map.containsKey("manageid") && !"".equals(map.get("manageid"))){
					String manageid = this.personalService.selectDataManageid(map);
					map.put("manageid", manageid);
				}
				this.personalService.updateUserOrganize(map);
			}
			if(map.containsKey("organizeidUpdate") && !"".equals(map.get("organizeidUpdate"))){
				String organizeid =  map.get("organizeidUpdate").toString();
				map.put("organizeid", organizeid);	
				int num = this.personalService.getAllOrganizelCount(map);
				if(num == 1){
						//如果没传manageid  那么久获取当前的的manageid
						if(!map.containsKey("manageid") && !"".equals(map.get("manageid"))){
							String manageid = this.personalService.selectDataManageid(map);
							map.put("manageid", manageid);
						}
						if(map.containsKey("manageid") && map.get("manageid")!= "" ){
							this.personalService.updateOrganizeUserInfo(map);
						}
				}else{
						this.personalService.insertOrganizeUserInfo(map);
				}
				this.personalService.updateUserOrganizeAdd(map);
				
			}*/
			
			if(map.containsKey("realname") && !"".equals(map.get("realname"))){
				map.put("getRealPath", request.getSession().getServletContext().getRealPath(""));
				map.put("fileDirectory", Constants.FILEDIRECTORY);
				map.put("headimgtype", 13);
			}
			
			this.personalService.updateUserBasicInfo(map);
			
			resultMap.put("status", 0);
			resultMap.put("message", "修改成功");
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "修改出错");
		}

		return resultMap;
	}

	/**
	 *	权限管理：为用户添加或修改权限
	 * @param map 输入参数：powerid, ownerid(userid),delflag 
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/insertUserPowerInfonew")
	@ResponseBody
	public Map<String,Object> insertUserPowerInfonew(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		//获取当前登录人的基本信息
		
		String userid=map.get("userid").toString();
		
		String ownerid=map.get("ownerid").toString();
		map.put("ownerid",ownerid);//人员id
		map.put("updateid", userid);//当前修改人id
		try {
			if(map != null && !"".equals(map)){
				if(map.get("listInsrt") != null && !"".equals(map.get("listInsrt"))){	
					List<Map> list = JSONArray.parseArray(map.get("listInsrt").toString(), Map.class);
					for (int i = 0; i < list.size(); i++) {
						//根据选中的首先查找改人员之前是否存在过此权限
						String powerid=list.get(i).get("powerid").toString();
						map.put("powerid", powerid);
						List<Map<String, Object>> datalist = this.personalService.getPowerByUserId(map);
						if(datalist != null && datalist.size()>0){
							//修改权限
							map.put("delflag", 0);
							this.personalService.getdeletePowerByUserId(map);
							map.remove("delflag");   
						}else{
							//新增用户权限
							map.put("createid", userid);//当前修改人id
							this.personalService.insertUserPowerInfo(map);
						}
					}
					
				}
				
				//修改为取消
				if(map.get("listDelete") != null && !"".equals(map.get("listDelete"))){
					List<Map> list = JSONArray.parseArray(map.get("listDelete").toString(), Map.class);	
					for (int i = 0; i < list.size(); i++) {
						//根据选中的首先查找改人员之前是否存在过此权限
						String powerid=list.get(i).get("powerid").toString();
						map.put("powerid", powerid);
						map.put("delflag", 0);
						List<Map<String, Object>> datalist = this.personalService.getPowerByUserId(map);
						if(datalist != null &&  datalist.size()>0){
							//取消他的权限权限
							map.put("delflag", 1);
							this.personalService.getdeletePowerByUserId(map);
						}
					}
				}	
				
				try {
					realTiemUpdateUserPower(ownerid);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				resultMap.put("status", 0);
				resultMap.put("success", "权限设置成功");	
			}		
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 0);
			resultMap.put("errors", "权限设置失败");
		}
		return resultMap;
	}
	
	/**
	 * 修改用户状态为：邀请
	 * companyid，userid，useridlist,phone，delflag，realname，status，isinvite
	 * ，invitetime，updatetime，updateid，position 传入参数：
	 * useridlist，companyid,updateid
	 * 
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUserInvitation", method = RequestMethod.POST)
	public Map<String, Object> updateUserInvitation(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int num =0 ;//判断邀请多少个
		try {
			//选中多个人员
			
			if(map.get("list") != null && !"".equals(map.get("list"))){
					//获取所选中的userid
					List<Map> list = JSONArray.parseArray(map.get("list").toString(), Map.class);
					List<String> useridlist=new ArrayList<String>();
					List<String> userPhoneList=new ArrayList<String>();
				
					for(int i=0;i<list.size();i++){  					
						//随机生成6位数密码
						Random random = new Random(); 
						int pass = 0;
						while(pass < 100000){
							pass = (int)(Math.random()*1000000);
						}
						String  password  = Md5Util.getMD5(String.valueOf(pass));
						
						String userid=list.get(i).get("userid").toString();
						String phone=list.get(i).get("phone").toString();	
						String username=list.get(i).get("username").toString();	
						//循环修改密码
						Map<String, Object> usermap = new HashMap<String, Object>();
						usermap.put("userid",userid);
						usermap.put("invitetime", new Date());
						usermap.put("isinvite", 1);
						usermap.put("password",password);
						this.personalService.updateUserBasicInfo(usermap);	
						useridlist.add(userid);
						//调用手机短信发送加密前密码	
						boolean bool = SDKTestSendTemplateSMS.sendBeInvitationUserMessage(phone, username, String.valueOf(map.get("updatename")), String.valueOf(map.get("organizename")), phone, String.valueOf(pass));
						if(bool){
							num++;
						}
					}
					resultMap.put("status", 0);
				
			}else{
				// 修改为邀请状态
				map.put("invitetime", new Date());
				map.put("isinvite", 1);
				Random random = new Random(); 
				int pass = 0;
				while(pass < 100000){
					pass = (int)(Math.random()*1000000);
				}
				String  password  = Md5Util.getMD5(String.valueOf(pass));
				map.put("password", password);
				this.personalService.updateUserBasicInfo(map);
				//发送邀请信息
				boolean bool = SDKTestSendTemplateSMS.sendBeInvitationUserMessage(map.get("phone").toString(), String.valueOf(map.get("username")), String.valueOf(map.get("updatename")), String.valueOf(map.get("organizename")), map.get("phone").toString(), String.valueOf(pass));
				if(bool){
					num++;
				}
				resultMap.put("status", 0);
			
			}
			resultMap.put("message", "邀请"+num+"个人员成功");
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "邀请出错,邀请成功"+num+"个人员");
		}

		return resultMap;
	}

	private boolean sendmsg() {
		return true;
	}

	/**
	 * 修改用户状态为：禁用/启用
	 * 
	 * 传入参数：
	 * companyid，useridlist，status，organizeid,usernum
	 * 
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUserDisable", method = RequestMethod.POST)
	public Map<String, Object> updateUserDisable(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int status =Integer.parseInt(map.get("status").toString());
		
		//选中多个人员
		if(map.get("list") != null && !"".equals(map.get("list"))){
				//获取所选中的userid 获取所选中的organizeid	
				List<Map> list = JSONArray.parseArray(map.get("list").toString(), Map.class);
				map.put("usernum", list.size());
				List<String> useridlist=new ArrayList<String>();

				for(int i=0;i<list.size();i++){  					
					String userid=list.get(i).get("userid").toString();
					useridlist.add(userid);
				}	
				map.put("useridlist", useridlist);
		}else{
			map.put("usernum",1);
		}
		try {
			//status为0说明是禁用   同时修改组织表的usernum
			if(map != null && !"".equals(map)){		
				if(status ==  0){
					resultMap.put("message", "禁用成功");
				}else{
					resultMap.put("message", "启用成功");
				}
				// 修改
				this.personalService.updateUserBasicInfo(map);
				resultMap.put("status", 0);
			}

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "操作失败");
		}

		return resultMap;
	}

	/**
	 * 查询功能权限列表信息 传入参数： parentid
	 * 
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAuthorityListInfo", method = RequestMethod.POST)
	public Map<String, Object> getAuthorityListInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			// 查询权限
			List<Map<String, Object>> list = this.personalService
					.getAuthorityListInfo(map);
			if (list.size() > 0 && list != null) {
				resultMap.put("data", list);
				resultMap.put("status", 0);
				resultMap.put("message", "查询权限成功");
			} else {
				resultMap.put("data", "");
				resultMap.put("status", 1);
				resultMap.put("message", "查询权限成功,暂无数据");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("data", "");
			resultMap.put("status", 1);
			resultMap.put("message", "查询权限成功,暂无数据");
		}

		return resultMap;
	}

	/**
	 * 添加用户权限信息 传入参数： powerid，ownerid，createid
	 * 
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/insertUserPowerInfo", method = RequestMethod.POST)
	public Map<String, Object> insertUserPowerInfo(
			@RequestParam Map<String, Object> map, HttpServletResponse response,HttpServletRequest request) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> userinfo=UserUtil.getUser(request);
		try {
			// 添加用户权限信息
			insertManageLog(userinfo.get("companyid")+"",2,"企业权限","添加人员权限",userinfo.get("userid")+"");
			this.personalService.insertUserPowerInfo(map);
			resultMap.put("status", 0);
			resultMap.put("message", "授权成功");

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "授权出错");
		}

		return resultMap;
	}
	
	/**
	 * 我的首页数据
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getUserIndexPage")
	@ResponseBody
	public Map<String, Object> getUserIndexPage(
			@RequestParam Map<String, Object> map, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			Map<String, Object> dataMap = this.personalService
					.getUserIndexPage(map);
			data.put("dataMap", dataMap);
			data.put("status", "0");
			data.put("msg", "查询成功");
		} catch (Exception e) {
			data.put("status", "1");
			data.put("msg", "查询失败");
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 得到系统公告列表
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getSystemMessageListByUser")
	@ResponseBody
	public Map<String, Object> getNoticeList(
			@RequestParam Map<String, Object> map, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			PageScroll page = new PageScroll();
			int remindnum = this.personalService
					.getSystemMessageCountByUserId(map);
			page.setTotalRecords(remindnum);
			page.initPage(map);
			List<Map<String, Object>> dataList = this.personalService
					.getSystemMessageListByUserId(map);
			data.put("dataList", dataList);
			data.put("page", page);
			data.put("status", "0");
			data.put("msg", "查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			data.put("status", "1");
			data.put("msg", "查询失败");
		}

		return data;
	}
	
	
	
	/**
	 * 得到系统公告详情
	 * 
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getSystemMessageNoticeById")
	@ResponseBody
	public Map<String, Object> getNoticeById(
			@RequestParam Map<String, Object> map, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> dataList = this.personalService
					.getSystemMessageNoticeById(map);
			data.put("dataList", dataList);
			data.put("status", "0");
			data.put("msg", "查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			data.put("status", "1");
			data.put("msg", "查询失败");
		}

		return data;
	}

	/**
	 * 修改系统公告
	 * 
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updataSystemMessageDetail", method = RequestMethod.POST)
	public Map<String, Object> updataSystemMessageDetail(
			@RequestParam Map<String, Object> map, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 创建返回的map对象
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			this.personalService.updataSystemMessageDetail(map);
			resultMap.put("status", 0);
			resultMap.put("message", "修改成功");

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			resultMap.put("status", 1);
			resultMap.put("message", "修改出错");
		}

		return resultMap;
	}
	/**
	 * 发送邮件
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getEmailVerificationCode" , method = RequestMethod.POST)
	public Map<String,Object> getEmailVerificationCode(@RequestParam Map<String,Object> map , 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data = new HashMap<String, Object>();
		
		try {
			if(map.containsKey("emailUrl") && !"".equals(map.get("emailUrl"))){
				//获取邮箱地址
				String emailurl = String.valueOf(map.get("emailUrl"));
				//生成验证码
				String vc = Generate_Verification_Code.generate_verification_code(6);
				//创建发送邮件的对象
				EmailUtil email = new EmailUtil();
				
				String title = "餐饮大师";
				String content = "您修改邮箱的验证码为："+vc;
				
				email.sendEmail(emailurl, title, content);
				
				data.put("status", 0);
				data.put("message", vc);
			}else{
				data.put("status", 1);
				data.put("message", "请输入邮箱地址");
			}
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "请输入邮箱验证码发送失败");
		}
		
		return data;
	}

	
	/**
	 * 查询用户是否属于当前登录用户组织区域下的人员
	 * 传入参数：resourceid  = userid (当前登录的用户id)  ;  userid = userid(查询的用户id)
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getPersonalByUserId")
	@ResponseBody
	public Map<String, Object> getPersonalReleaseByUserId(
			@RequestParam Map<String, Object> map, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> releOrganIdList = this.personalService
					.getPersonalReleaseByUserId(map);
			List<Map<String, Object>> userOrganIdLsit = this.personalService
					.getPersonalUsreByUserId(map);
			data.put("releOrganIdList", releOrganIdList);
			data.put("userOrganIdLsit", userOrganIdLsit);
			data.put("status", "0");
			data.put("msg", "查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			data.put("status", "1");
			data.put("msg", "查询失败");
		}

		return data;
	}
	
	
	/**
	 * 禁用/启用
	 * 传入参数：resourceid  = userid (当前登录的用户id)  ;  userid = userid(查询的用户id)
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getUpdataByUser")
	@ResponseBody
	public Map<String, Object> getUpdataByUser(
			@RequestParam Map<String, Object> map, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> releOrganIdList = this.personalService
					.getPersonalReleaseByUserId(map);
			List<Map<String, Object>> userOrganIdLsit = this.personalService
					.getPersonalUsreByUserId(map);
			data.put("releOrganIdList", releOrganIdList);
			data.put("userOrganIdLsit", userOrganIdLsit);
			data.put("status", "0");
			data.put("msg", "查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			data.put("status", "1");
			data.put("msg", "查询失败");
		}

		return data;
	}

	/**
	 * 权限管理：企业权限列表
	 * @param map 输入参数：powerid,
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getSelectPowerCompany")
	@ResponseBody
	public Map<String,Object> getSelectPowerCompany(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> resultMap = new HashMap<String, Object>();
//		Map<String, Object> userinfo=UserUtil.getUser(request);
//		map.put("companyid", userinfo.get("companyid"));
		try {			
			//查询出一级菜单
			List<Map<String, Object>> listone = this.personalService.getPowerOne(map);
			if(listone != null && listone.size() > 0){
				List<String> parentidlist=new ArrayList<String>();
	 			for (int i = 0; i < listone.size(); i++) {
	 				String parentid= listone.get(i).get("powerid").toString();
					parentidlist.add(parentid);
				}
				map.put("parentidlist", parentidlist);
				//查询出对应得二级菜单
				List<Map<String, Object>> datalist = this.personalService.getPowerAll(map);
				
				resultMap.put("listone", listone);
				resultMap.put("datalist", datalist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 权限管理：企业权限列表
	 * @param map 输入参数：powerid,
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getSelectPowerCompanynew")
	@ResponseBody
	public Map<String,Object> getSelectPowerCompanynew(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> resultMap = new HashMap<String, Object>();
//		Map<String, Object> userinfo=UserUtil.getUser(request);
//		map.put("companyid", userinfo.get("companyid"));
		try {			
			//查询出一级菜单
			List<Map<String, Object>> listone = this.personalService.getPowerOne(map);
			if(listone != null && listone.size() > 0){
				List<String> parentidlist=new ArrayList<String>();
	 			for (int i = 0; i < listone.size(); i++) {
	 				String parentid= listone.get(i).get("powerid").toString();
					parentidlist.add(parentid);
				}
				map.put("parentidlist", parentidlist);
				//查询出对应得二级菜单
				List<Map<String, Object>> datalist = this.personalService.getPowerAll(map);
				
				//查询当前用户所有的权限
				map.put("ownerid",map.get("userid"));
				map.put("delflag",0);
				List<Map<String, Object>> userdatalist = this.personalService.getPowerByUserId(map);
				if(datalist!=null && datalist.size()>0 && userdatalist!=null && userdatalist.size()>0){
					for(Map<String,Object> m:datalist){
						for(Map<String,Object> nonem:userdatalist){
							if(m.get("powerid").equals(nonem.get("powerid"))){
								m.put("isshow",1);
							}
						}
					}
					List<String> nonelist=new ArrayList<String>();
					for(Map<String,Object> nonem:userdatalist){
						if(nonem.get("powerid")!=null && "".equals(nonem.get("powerid"))){
							nonelist.add(nonem.get("powerid").toString());
						}
					}
					resultMap.put("listuser", nonelist);
				}
				
				
				resultMap.put("listone", listone);
				resultMap.put("datalist", datalist);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 权限管理：组织架构下企业权限中已有的人员
	 * @param map 输入参数：powerid,
	 * @param model
	 * @param request
	 * @return
	 * @author dsl
	 */
	@RequestMapping("/getSelectPowerByPowerid")
	@ResponseBody
	public Map<String,Object> getSelectPowerByPowerid(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> resultMap = new HashMap<String, Object>();
//		Map<String, Object> userinfo=UserUtil.getUser(request);
//		map.put("companyid", userinfo.get("companyid"));
		try {			
			//查询出一级菜单
			List<Map<String, Object>> listone = this.personalService.getPowerOne(map);
			if(listone != null && listone.size() > 0){
				List<String> parentidlist=new ArrayList<String>();
	 			for (int i = 0; i < listone.size(); i++) {
	 				String parentid= listone.get(i).get("powerid").toString();
					parentidlist.add(parentid);
				}
				map.put("parentidlist", parentidlist);
				//查询出对应得二级菜单
				List<Map<String, Object>> datalist = this.personalService.getPowerAll(map);
				
				
				resultMap.put("listone", listone);
				resultMap.put("datalist", datalist);
						
				List<String> poweridlist=new ArrayList<String>();
				List<String> organizeidlist=new ArrayList<String>();
				
				if(datalist != null && datalist.size()>0 ){
					for (int i = 0; i < datalist.size(); i++) {
						poweridlist.add(datalist.get(i).get("powerid").toString());
					}
					map.put("poweridlist", poweridlist);
					List<Map<String, Object>> organizelist=this.personalService.getOrganizeidAll(map);
					for (int i = 0; i < organizelist.size(); i++) {
						organizeidlist.add(organizelist.get(i).get("organizeid").toString());
					}
					map.put("organizeidlist", organizeidlist);
					//查询出所有权限管理下面的人员
					List<Map<String, Object>> poweruserlist = this.personalService.getPowerUserByPowerId(map);
					resultMap.put("poweruserlist", poweruserlist);
				}
			
				
				resultMap.put("listone", listone);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}


/**
 *	权限管理：申请企业权限
 * @param map 输入参数：powerid
 * @param model
 * @param request
 * @return
 * @author dsl
 */
@RequestMapping("/getPowerByCompany")
@ResponseBody
public Map<String,Object> getPowerByCompany(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
		HttpServletResponse response) {
	response.setHeader("Access-Control-Allow-Origin", "*");
	Map<String,Object> resultMap = new HashMap<String, Object>();
//	//获取当前登录人的基本信息
//	Map<String, Object> userinfo=UserUtil.getUser(request);
	String userid=map.get("userid").toString();
	
//	insertManageLog(userinfo.get("companyid")+"",2,"企业权限","申请权限",userinfo.get("userid")+"");
	try {
		if(map != null && !"".equals(map)){
//			map.put("companyid", userinfo.get("companyid"));
				List<Map<String, Object>> datalist = this.personalService.selectPowerCompany(map);
				if(datalist != null && datalist.size()>0){
					 map.put("updateid", userid);
					 map.put("status", 2);
					 map.put("delflag", 0);
					 map.put("powercompanyid", datalist.get(0).get("powercompanyid"));
					 this.personalService.getUpdatPowerCompanyByid(map);
				}else{
					map.put("createid", userid);
					map.put("status", 2);
					this.personalService.insertPowerCompany(map);
				}
				resultMap.put("status",0);
				resultMap.put("success", "权限申请成功");	
		}		
	} catch (Exception e) {
		e.printStackTrace();
		resultMap.put("status",1);
		resultMap.put("errors", "权限设申请失败");
	}
	return resultMap;
}



/**
 *	权限管理：为多个用户添加或修改权限
 * @param map 输入参数：powerid, ownerid(userid),delflag 
 * @param model
 * @param request
 * @return
 * @author dsl
 */
@RequestMapping("/updataUserByPowerId")
@ResponseBody
public Map<String,Object> updataUserByPowerId(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
		HttpServletResponse response) {
	response.setHeader("Access-Control-Allow-Origin", "*");
	Map<String,Object> resultMap = new HashMap<String, Object>();
//	//获取当前登录人的基本信息
//	Map<String, Object> userinfo=UserUtil.getUser(request);
	String userid=map.get("userid").toString();
	try {
		if(map != null && !"".equals(map)){
			if(map.get("useridlist") != null && !"".equals(map.get("useridlist"))){	
				List<Map> list = JSONArray.parseArray(map.get("useridlist").toString(), Map.class);
				for (int i = 0; i < list.size(); i++) {
					//根据选中的首先查找改人员之前是否存在过此权限
					String ownerid=list.get(i).get("userid").toString();
					map.put("ownerid", ownerid);
					List<Map<String, Object>> datalist = this.personalService.getPowerByUserId(map);
					if(datalist != null && datalist.size()>0){
						//修改权限
//						insertManageLog(userinfo.get("companyid")+"",2,"企业权限","修改权限给"+list.get(i).get("realname")+"",userinfo.get("userid")+"");
						map.put("delflag", 0);
						map.put("updateid", userid);//当前修改人id
						this.personalService.getdeletePowerByUserId(map);
						map.remove("delflag");   
						map.remove("updateid");   
					}else{
						//新增用户权限
//						insertManageLog(userinfo.get("companyid")+"",2,"企业权限","添加权限给"+list.get(i).get("realname")+"",userinfo.get("userid")+"");
						map.put("createid", userid);//当前修改人id
						this.personalService.insertUserPowerInfo(map);
						map.remove("createid");   
					}
				}
				
			}
			if(map.containsKey("poweruserid")){
				map.put("delflag", 1);
				map.put("updateid", userid);//当前修改人id
				this.personalService.getdeletePowerByUserId(map);
			}
			resultMap.put("status",0);
			resultMap.put("success", "权限设置成功");	
		}		
	} catch (Exception e) {
		e.printStackTrace();
		resultMap.put("status",1);
		model.addAttribute("errors", "权限设置失败");
	}
	return resultMap;
}
/**
 *	权限管理：查询权限下已有的权限人员
 * @param map 输入参数：powerid, ownerid(userid),delflag 
 * @param model
 * @param request
 * @return
 * @author dsl
 */
@RequestMapping("/getPowerUser")
@ResponseBody
public  Map<String, Object> getPowerUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
		HttpServletResponse response) {
	response.setHeader("Access-Control-Allow-Origin", "*");
	Map<String,Object> resultMap = new HashMap<String, Object>();
	try {
		List<Map<String, Object>> userlist = new ArrayList<Map<String,Object>>();
			if(map.get("powerid") != null && !"".equals(map.get("powerid"))){	
				userlist = this.personalService.getPowerUser(map);
			}
			resultMap.put("userlist",userlist);
			resultMap.put("status",0);
			resultMap.put("success", "权限设置成功");			
	} catch (Exception e) {
		e.printStackTrace();
		resultMap.put("status",1);
		resultMap.put("errors", "权限设置失败");
	}
	return resultMap;
}

/**
 *	权限管理：权限详情
 * @param map 输入参数：powerid,ownerid(userid)
 * @param model
 * @param request
 * @return
 * @author dsl
 */
@RequestMapping("/getPowerByPowerid")
@ResponseBody
public Map<String,Object> getPowerByPowerid(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request, 
		HttpServletResponse response) {
	response.setHeader("Access-Control-Allow-Origin", "*");
	Map<String,Object> resultMap = new HashMap<String, Object>();
	try {
		if(map != null && map.containsKey("powerid")){
			List<Map<String, Object>> datalist = this.personalService.getPowerAll(map);
			resultMap.put("datalist", datalist);
		}
		
	} catch (Exception e) {
		e.printStackTrace();
		model.addAttribute("errors", "查询失败");
	}
	return resultMap;
}

/**
 * 人员管理：查询组织下人员信息
  * @param map
 * @param model
 * @param request
 * @return
 * @author dsl
 */
@RequestMapping("/getNextOrgainizeById")
@ResponseBody
public Map<String,Object> getNextOrgainizeById(@RequestParam Map<String, Object> map,  HttpServletRequest request) {
	Map<String,Object> resultMap = new HashMap<String, Object>();
//	Map<String, Object> userinfo=UserUtil.getUser(request);
//	map.put("userid", userinfo.get("userid")+"");
//	map.put("companyid", userinfo.get("companyid")+"");
	try {
		PageHelper page = new PageHelper(request);
		int count = this.indexService.getUserListAllCount(map);
		page.setTotalCount(count);
		page.initPage(map);
		List<Map<String, Object>> userlist=this.indexService.getUserListAll(map);
		resultMap.put("status", 0);
		resultMap.put("userlist", userlist);
//		resultMap.put("userinfo", userinfo);
		resultMap.put("pager", page.JSCateringPage().toString());
	} catch (Exception e) {
		e.printStackTrace();
		resultMap.put("userlist", "");
		resultMap.put("status", 1);
	}
	return resultMap;
}

/**
 * 
 * 模糊查询
 * 传入参数{"realname":"李语然"}
 * 传出参数{"message":"查询成功","modulelist":[{"createtime":1468983040000,"companyid":"67702cc412264f4ea7d2c5f692070457","modulename":"公司新闻","moduleimage":"1.jpg","updatetime":1468983120000,"createid":"690fb669ed4d40219964baad7783abd4","moduleid":"fdd61606a640459081bce3d02dd8e01a"}],"status":0}
 * @param map
 * @param model
 * @param request
 * @return
 * @author silence
 */

@ResponseBody
@RequestMapping(value="/getSearchUserByName")
public Map<String,Object> getSearchUserByName(@RequestParam Map<String,Object> map , HttpServletRequest request){
	Map<String,Object> resultMap = new HashMap<String, Object>();
	try {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> userlist=this.userInfoService.getUserListByOrganize(map);
		resultMap.put("userlist", userlist);
		resultMap.put("status", 0);
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		resultMap.put("status", 1);
	}
	
	return resultMap;
}

/**
 * 查询用户组织架构信息
 * @param map
 * @param request
 * @return
 */
@ResponseBody
@RequestMapping(value="/getOrganize")
public Map<String,Object> getOrganize(@RequestParam Map<String,Object> map , HttpServletRequest request, 
		HttpServletResponse response){
	response.setHeader("Access-Control-Allow-Origin", "*");
	Map<String,Object> resultMap = new HashMap<String, Object>();
	try {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		JSONObject jsonObject = JSONObject.fromObject(map.get("userinfo"));
		Map<String, Object> userinfo=(Map)jsonObject;
		if(userinfo != null){
			if(userinfo.containsKey("managerole") && !"".equals(map.get("managerole"))){
				String managerole = String.valueOf(userinfo.get("managerole"));
				if("3".equals(managerole)){
					Map<String, Object> organizemap=new HashMap<String, Object>();
					organizemap.put("companyid", userinfo.get("companyid"));
					list = this.indexService.getOrganizeList(organizemap);
				}else if("2".equals(managerole)){
					map.put("userid", userinfo.get("userid"));
					map.put("companyid", userinfo.get("companyid"));
					list = this.indexService.getOrganizeByUserList(map);
				}
			}
		}
		resultMap.put("compandyname", userinfo.get("companyname"));
		resultMap.put("type", userinfo.get("managerole"));
		resultMap.put("organizelist", list);
		resultMap.put("status", 0);
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		resultMap.put("organizelist", "");
		resultMap.put("status", 1);
	}
	
	return resultMap;
}



	/**
	 * 查询用户所属的组织区域
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getOrganizeByUserid")
	public Map<String,Object> getOrganizeByUserid(@RequestParam Map<String,Object> map , HttpServletRequest request, 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			if(map.containsKey("userid")){
				List<Map<String,Object>> list_a = this.personalService.getOrganizeUserId(map);
				List<String> organizeidlist=new ArrayList<String>();
				for(int i=0;i<list_a.size();i++){
					organizeidlist.add(list_a.get(i).get("organizeid")+"");
				}
				map.put("organizeidlist",organizeidlist);
				List<Map<String,Object>> list_b = this.personalService.getDatacodeByOrgid(map);
				
				List<Map<String,Object>> list_c = this.personalService.getOrganizeByDataCodeList(map);
				List<Map<String,Object>> list_d = this.personalService.getOrganizeByDataCode(map);
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("organizelist", "");
			resultMap.put("status", 1);
		}
		
		return resultMap;
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

