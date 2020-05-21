package com.collection.controller.memorandum;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collection.service.IndexService;
import com.collection.service.memorandum.MemorandumService;
import com.collection.service.purchase.PurchaseService;
import com.collection.util.PageScroll;


/**
 * 备忘录 / 分享圈
 * @author silence
 *
 */
@Controller
@RequestMapping("/app")
public class MemorandumController {
	private transient static Log log = LogFactory.getLog(MemorandumController.class);
	@Resource
	private MemorandumService memorandumService;
	@Resource
	private PurchaseService purchaseService;
	@Resource
	private IndexService indexService;
	
	/**
	 * 查询今天的备忘录
	 * 传入参数{"createid":"690fb669ed4d40219964baad7783abd4","companyid":"67702cc412264f4ea7d2c5f692070457","datetime":"2016-07-18"}  datetime不传表示今天
	 * 传出参数{"num":1,"memorandumlist":[{"content":"回家洗衣服","createtime":1468657439000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468832901000,"createid":"690fb669ed4d40219964baad7783abd4","updateid":"","hourtime":"20:00","memorandumid":"2","datetime":"2016-07-18","ifexpired":0}],"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getMemorandumList")
	@ResponseBody
	public Map<String, Object> getMemorandumList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		if(!map.containsKey("datetime") || "".equals(map.get("datetime"))){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
			SimpleDateFormat sdf1=new SimpleDateFormat("MM");
			SimpleDateFormat sdf2=new SimpleDateFormat("dd");
			Date date=new Date();
			map.put("datetime", sdf.format(date)+"-"+Integer.parseInt(sdf1.format(date))+"-"+Integer.parseInt(sdf2.format(date)));
		}
		PageScroll page = new PageScroll();
		int num=this.memorandumService.getMemorandumListNum(map);		
		page.setTotalRecords(num);
		page.initPage(map);
		List<Map<String, Object>> memorandumlist=this.memorandumService.getMemorandumList(map);
		data.put("num", num);
		String a=map.get("datetime")+"";
		data.put("yearmonth",a.substring(0,a.indexOf("-", 5)));
		data.put("memorandumlist", memorandumlist);
		data.put("status",0);
		data.put("page",page);
		return data;
	}
	
	/**
	 * 查询今天的备忘录
	 * 传入参数{"createid":"690fb669ed4d40219964baad7783abd4","companyid":"67702cc412264f4ea7d2c5f692070457","datetime":"2016-07-18"}  datetime不传表示今天
	 * 传出参数{"num":1,"memorandumlist":[{"content":"回家洗衣服","createtime":1468657439000,"companyid":"67702cc412264f4ea7d2c5f692070457","updatetime":1468832901000,"createid":"690fb669ed4d40219964baad7783abd4","updateid":"","hourtime":"20:00","memorandumid":"2","datetime":"2016-07-18","ifexpired":0}],"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getAllMemorandumTime")
	@ResponseBody
	public Map<String, Object> getAllMemorandumTime(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		List<Map<String, Object>> timelist=this.memorandumService.getAllMemorandumTime(map);
		for(Map<String, Object> time:timelist){
			String value=time.get("value")+"";
			if(value.length()>10){
				time.put("value", value.substring(0, 10)+"...");
			}
		}
		data.put("timelist", timelist);
		data.put("status",0);
		return data;
	}
	
	/**
	 * 添加备忘录
	 * 传入参数{"createid":"690fb669ed4d40219964baad7783abd4","companyid":"67702cc412264f4ea7d2c5f692070457","datetime":"2016-07-18","hourtime":"21:00","content":"俯卧撑100个"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertMemorandum")
	@ResponseBody
	public Map<String, Object> insertMemorandum(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		this.memorandumService.insertMemorandum(map);
		data.put("status",0);
		data.put("message","添加成功");
		return data;
	}
	
	/**
	 * 查询备忘录信息
	 * 传入参数{"memorandumid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@ResponseBody
	@RequestMapping("/getMemorandumInfo")
	public Map<String, Object> getMemorandumInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		Map<String, Object>  memoranduminfo=this.memorandumService.getMemorandumInfo(map);
		data.put("memoranduminfo", memoranduminfo);
		data.put("status",0);
		data.put("message","查询成功");
		return data;
	}
	/**
	 * 修改备忘录信息
	 * 传入参数{"memorandumid":"67702cc412264f4ea7d2c5f692070457","delflag":1,"userid":"1"}
	 * 传出参数
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@ResponseBody
	@RequestMapping("/updateMemorandum")
	public Map<String, Object> updateMemorandum(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		map.put("updateid", map.get("userid"));
		map.put("updatetime", new Date());
		this.memorandumService.updateMemorandumInfo(map);
		data.put("status",0);
		data.put("message","修改成功");
		return data;
	}
	
	
	/**
	 * 添加分享圈
	 * 传入参数{"createid":"690fb669ed4d40219964baad7783abd4","companyid":"67702cc412264f4ea7d2c5f692070457","content":"真是子啊","images":"sss,aaa,aaa"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertShare")
	@ResponseBody
	public Map<String, Object> insertShare(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		String [] images=null;
		if(map.containsKey("images") && !"".equals(map.get("images"))){
			images=(map.get("images")+"").split(",");
		}
		map.put("images", images);
		this.memorandumService.insertShare(map);
		data.put("status",0);
		data.put("message","添加成功");
		return data;
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
	 * 分享圈点赞/取消点赞
	 * 传入参数{"shareid":"bf15672191674a6a8f46ce0c4b885c3b","createid":1,"ispraise":1} praiseid不传 ispraise为 新增 
	 * 传出参数{"message":"修改成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertPraise")
	@ResponseBody
	public Map<String, Object> insertPraise(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		Map<String, Object> praisemap =new HashMap<String, Object>();
		praisemap.put("praiseid", map.get("praiseid"));
		praisemap=this.memorandumService.getPraiseInfo(praisemap);
		if(praisemap != null && praisemap.size()>0 && map.containsKey("praiseid") && !"".equals(map.get("praiseid"))){
			try {
				Map<String, Object> praiseinfo=new HashMap<String, Object>();
				praiseinfo.put("praiseid", map.get("praiseid"));
				praiseinfo.put("ispraise", map.get("ispraise"));
				this.memorandumService.updatePraise(praiseinfo);
				data.put("status",0);
				data.put("shareid", praisemap.get("shareid"));
				data.put("message","修改成功");
			
			} catch (Exception e) {
				// TODO: handle exception
				data.put("status",1);
				data.put("message","传入参数错误");
			}
		}else{
			String praiseid = UUID.randomUUID().toString().replaceAll("-", "");
			map.put("praiseid", praiseid);
			this.memorandumService.insertPraise(map);
			this.memorandumService.insertShareNotread(map);
			data.put("status",0);
			data.put("praiseid", praiseid);
			data.put("message","添加成功");
		}
		
		return data;
	}
	
	
	/**
	 * 分享圈 添加 评论
	 * 传入参数{"content":"你这个搞笑么","userid":1,"resourceid":"bf15672191674a6a8f46ce0c4b885c3b","resourcetype":26}
	 * 传出参数{"message":"评论成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertShareComment")
	@ResponseBody
	public Map<String, Object> insertShareComment(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>(); 
		//添加到评论表
		String commentid = this.purchaseService.insertComment(map);
		//添加到分享圈未读数表
		Map<String, Object> notreadmap=new HashMap<String, Object>();
		notreadmap.put("createid", map.get("userid"));
		notreadmap.put("shareid", map.get("resourceid"));
		notreadmap.put("num", 0);
		this.memorandumService.insertShareNotread(notreadmap);
		data.put("status",0);
		data.put("commentid", commentid);
		data.put("message","评论成功");
		return data;
	}
	
	/**
	 * 清空未读数量
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/clearShareNotReadNum")
	public String clearShareNotReadNum(@RequestParam Map<String,Object> map , HttpServletRequest request){
		String msg = "success";
		try {
			this.memorandumService.shareNotReadClearNum(map);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}
	
	/**
	 * 查询用户分享圈的未读数量
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getShareNotReadNumByUser")
	public Map<String,Object> getShareNotReadNumByUser(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			int readnum = this.memorandumService.getShareNotReadSumNum(map);
			resultMap.put("num", readnum);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("num", 0);
		}
		return resultMap;
	}
}
