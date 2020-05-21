package com.collection.controller.pc.kpi;

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

import com.alibaba.dubbo.container.page.Page;
import com.collection.controller.userbackstage.UserIndexController.ExportExcel;
import com.collection.service.IndexService;
import com.collection.service.starassess.StarAssessService;
import com.collection.util.PageHelper;
import com.collection.util.PageScroll;


/**
 * KPI星值审核
 * @author silence
 *
 */
@Controller
@RequestMapping("/pc")
public class KpiJobStarController {
	private transient static Log log = LogFactory.getLog(KpiJobStarController.class);
	
	@Resource IndexService indexService;
	
	@Resource StarAssessService starAssessService;

	
	
	/**
	 * 岗位星值和综合星值的未读数量
	 * 传入参数{"receiveid":1,"companyid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传入参数{"generalstarnum":1,"selfstarnum":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getStarAssessNotreadNum")
	@ResponseBody
	public Map<String, Object> getPurchaseNotreadNum(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data =new HashMap<String, Object>();
		map.put("isread", 0);
		//岗位星值
		List<Integer> resourcetypes=new ArrayList<Integer>();
		resourcetypes.add(6);
		map.put("resourcetypes", resourcetypes);
		int selfstarnum=this.indexService.getForwordNotreadNum(map);
		data.put("selfstarnum", selfstarnum);
		//综合星值
		resourcetypes=new ArrayList<Integer>();
		resourcetypes.add(7);
		map.put("resourcetypes", resourcetypes);
		int generalstarnum=this.indexService.getForwordNotreadNum(map);
		data.put("generalstarnum", generalstarnum);
		return data;
	}
	
	
	/**
	 * 查询自评模板列表信息
	 * 传入参数：type,companyid,organizeid  (type,1:岗位星值，2：综合星值)
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getStarEvaluateTemplateList")
	public Map<String,Object> getStarEvaluateTemplateList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data =new HashMap<String, Object>();
		
		try {
			List<Map<String,Object>> list = this.starAssessService.appGetEvaluateTemplateList(map);
			if(list.size() > 0 && list != null){
				data.put("templatelist", list);
				data.put("status", 0);
			}else{
				data.put("templatelist", "");
				data.put("status", 1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			data.put("templatelist", "");
			data.put("status", 1);
		}
		
		return data;
	}
	
	/**
	 * 获取岗位项目列表（传type=1 为岗位星值项目  type=2为综合星值项目）
	 * 传入参数{"type":1,"companyid":"67702cc412264f4ea7d2c5f692070457","organizeid":5}
	 * 传出参数{"message":"查询成功","projectlist":[{"createtime":1468825689000,"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"1","organizeid":"5","projectid":"1","projectname":"衣服着装","type":1}],"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getStarProjectByOrganize")
	@ResponseBody
	public Map<String, Object> getStarProjectByOrganize(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data =new HashMap<String, Object>();
		
		try {
			List<Map<String, Object>> projectlist=this.starAssessService.getStarProjectList(map);
			if(projectlist!= null && projectlist.size()>0){
				data.put("projectlist", projectlist);
			}else{
				data.put("projectlist", "");
			}
			data.put("status", 0);
			data.put("message", "查询成功");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status",1);
		}
			
		return data;
	}
	/**
	 * 添加岗位自评 星级
	 * 传入参数{"sumstar":5,"companyid":"67702cc412264f4ea7d2c5f692070457","organizeid":5,"examineuserid":1,"createid":"690fb669ed4d40219964baad7783abd4","starlist":"{"starlist":[{"projectid":1,"starlevel":5}]}"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertEvaluate")
	@ResponseBody
	public Map<String, Object> insertEvaluate(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data =new HashMap<String, Object>();
		String evaluateid="";
		try {
			//添加岗位自评 星级
			evaluateid=this.starAssessService.insertEvaluate(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加错误");
			return data;
		}
		try {
			//添加到自评星级评分表
			JSONObject json=JSONObject.fromObject(map.get("starlist")+"");
			List<Map<String, Object>> starlist=(List<Map<String, Object>>)json.get("starlist");
			for(Map<String, Object> star:starlist){
				star.put("companyid", map.get("companyid"));
				star.put("evaluateid", evaluateid);
				star.put("createid", map.get("createid"));
				this.starAssessService.insertEvaluateStar(star);
			}
			data.put("status", 0);
			data.put("message", "添加成功");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加错误");
			return data;
		}
		return data;
	}
	
	/**
	 * 岗位星值查询
	 * 传入参数{"userid":1,"status":0}  status：0 未处理 1：已处理
	 * 传出参数{"num":10,"page":{"pageSize":10,"pageNo":1,"totalRecords":10,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0,"evaluatelist":[{"createtime":1468826204000,"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","status":0,"examineuserid":"1","organizeid":"5","evaluateid":"6341be0af4074e46a8335933821bcd25","createname":"silence","sumstar":5},{"createtime":1468826204000,"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","status":0,"examineuserid":"1","organizeid":"5","evaluateid":"6341be0af4074e46a8335933821bcd25","createname":"silence","sumstar":5}]}
 	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getEvaluateList")
	@ResponseBody
	public Map<String, Object> getEvaluateList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		try {
			map.put("delflag", 0);
			PageHelper page = new PageHelper(request);
			int num=this.starAssessService.getEvaluateListNum(map);		
			page.setTotalCount(num);
			page.initPage(map);
			List<Map<String, Object>> evaluatelist=this.starAssessService.getEvaluateList(map);
			
			data.put("num", num);
			data.put("evaluatelist", evaluatelist);
			data.put("status",0);
			data.put("pager",page.JSCateringPage().toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			data.put("status", 1);
		}
		return data;
	}
	
	/**
	 * 星值详情页面
	 * 传入参数{"userid":1,"evaluateid":"6341be0af4074e46a8335933821bcd25"}
	 * 传出参数{"message":"查询成功","status":0,"evaluateInfo":{"createtime":1468826204000,"companyid":"67702cc412264f4ea7d2c5f692070457","createid":"690fb669ed4d40219964baad7783abd4","examineuserid":"1","organizeid":"5","organizename":"浦东金桥分店","evaluateid":"6341be0af4074e46a8335933821bcd25","createname":"silence","examinename":"silence","sumstar":5},"evaluatelist":[{"createtime":1468826205000,"createid":"690fb669ed4d40219964baad7783abd4","starid":"ff6c8162da804a598a21ad977f055248","projectid":"1","projectname":"衣服着装","evaluateid":"6341be0af4074e46a8335933821bcd25","starlevel":5}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getEvaluateInfo")
	@ResponseBody
	public Map<String, Object> getEvaluateInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>(); 
		
		if(!map.containsKey("evaluateid") || "".equals(map.get("evaluateid"))){
			data.put("status", 1);
			data.put("message", "参数不正确");
			return data;
		}
		
		try {
			//自评详情
			Map<String, Object> evaluateInfo=this.starAssessService.getEvaluateInfo(map);
			//自评项目打分列表
			List<Map<String, Object>> evaluatelist=this.starAssessService.getEvaluateStarList(evaluateInfo);
			
			data.put("evaluateInfo", evaluateInfo);
			data.put("evaluatelist", evaluatelist);
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
	 * 审核岗位星值  同意或者拒绝
	 * 传入参数{"evaluateid":"6341be0af4074e46a8335933821bcd25","userid":1,"opinion":"ohmygod","result":1}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineEvaluate")
	@ResponseBody
	public Map<String, Object> examineEvaluate(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.starAssessService.updateEvaluate(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	
	/**
	 * 岗位星值列表导出
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportEvaluateList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportEvaluateList(@RequestParam Map<String, Object> map,
			HttpServletRequest request) {
		String importurl = request.getSession().getServletContext()
				.getRealPath("/upload/excel/岗位星值列表.xls");
	

		List<Map<String, Object>> evaluatelist = this.starAssessService
				.getEvaluateList(map);

		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		String[] headers = { "申请人","申请时间","总星值"	,"状态" };
	
		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> order : evaluatelist) {
			TreeMap<String, Object> datamap = new TreeMap<String, Object>();
			datamap.put("a",
					order.get("createname") == null ? "" : order.get("createname"));
			datamap.put(
					"b",
					order.get("createtime") == null ? "" : order
							.get("createtime"));
			datamap.put(
					"c",
					order.get("sumstar") == null ? "" : order
							.get("sumstar"));
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
		return "岗位星值列表.xls";
	}
	 
	@RequestMapping(value = "/exportEvaluateDetailList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportEvaluateDetailList(@RequestParam Map<String, Object> map,
			HttpServletRequest request) {
		String importurl = request.getSession().getServletContext()
				.getRealPath("/upload/excel/岗位星值详情.xls");
		//综合自评详情
		Map<String, Object> emap=this.starAssessService.getEvaluateInfo(map);
		//综合自评项目打分列表
		List<Map<String, Object>> evaluatelist=this.starAssessService.getEvaluateStarList(emap);
        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>(); 
        String[] headers = { "所属区域","岗位名称","合计","申请人","申请时间","状态","审批结果","审批人","审批意见"};
        
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
     
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a",emap.get("organizename")==null?"":""+emap.get("organizename"));
	        datamap.put("b",emap.get("identityname")==null?"":""+emap.get("identityname"));
	        datamap.put("d",emap.get("sumstar")==null?"":emap.get("sumstar"));
	    
	        datamap.put("f",emap.get("createname")==null?"":""+emap.get("createname"));
	        datamap.put("g",emap.get("createtime")==null?"":""+emap.get("createtime"));
	        datamap.put("h",Integer.parseInt(emap.get("status").toString())==0?"待处理":"已处理");
	        if(emap.containsKey("result")){
	        	datamap.put("i", Integer.parseInt(emap.get("result").toString())==1?"已同意":"已拒绝");
	        }
	        datamap.put("j",emap.get("examinename")==null?"":""+emap.get("examinename"));
	        datamap.put("k",emap.get("opinion")==null?"":""+emap.get("opinion"));
	        dataset.add(datamap);
	        TreeMap<String, Object> datamap3=new TreeMap<String, Object>();
	        datamap3.put("a","岗位名称");
	        datamap3.put("b","岗位星值");
	        dataset.add(datamap3);
             for(Map<String, Object> log:evaluatelist){
                TreeMap<String, Object> datamap2=new TreeMap<String, Object>();
		        datamap2.put("a",log.get("projectname")==null?"":log.get("projectname"));
		        datamap2.put("b",log.get("starlevel")==null?"":""+log.get("starlevel"));
		        dataset.add(datamap2);
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
		return "岗位星值详情.xls";
	}

	/*-------------------------------------------------综合星值--------------------------------------------------------------------*/
	
	/**
	 * 添加岗位综合自评 星级
	 * 传入参数{"sumstar":4,"companyid":"67702cc412264f4ea7d2c5f692070457","organizeid":5,"examineuserid":1,"createid":"690fb669ed4d40219964baad7783abd4","starlist":"{"starlist":[{"projectid":2,"starlevel":4,"description":"111111"}]}"}
	 * 传出参数{"message":"添加成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/insertOverallvaluate")
	@ResponseBody
	public Map<String, Object> insertOverallvaluate(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data =new HashMap<String, Object>();
		String overallvaluateid="";
		try {
			//添加岗位综合自评 星级
			overallvaluateid=this.starAssessService.insertOverallvaluate(map);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加错误");
			return data;
		}
		try {
			//添加到综合自评星级评分表
			JSONObject json=JSONObject.fromObject(map.get("starlist")+"");
			List<Map<String, Object>> starlist=(List<Map<String, Object>>)json.get("starlist");
			for(Map<String, Object> star:starlist){
				star.put("companyid", map.get("companyid"));
				star.put("overallvaluateid", overallvaluateid);
				star.put("createid", map.get("createid"));
				this.starAssessService.insertOverallvaluateStar(star);
			}
			data.put("status", 0);
			data.put("message", "添加成功");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "添加错误");
			return data;
		}
		return data;
	}
	
	/**
	 * 查询综合星值列表信息
	 * 传入参数：
	 * companyid，receiveid,status，starttime，endtime
	 * @param map
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getOverallvaluateList")
	public Map<String,Object> getOverallvaluateList(@RequestParam Map<String,Object> map , 
			HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> data =new HashMap<String, Object>();
		
		try {
			map.put("delflag", 0);
			PageHelper page = new PageHelper(request);
			int count = this.starAssessService.getOverallvaluateListNum(map);
			page.setTotalCount(count);
			page.initPage(map);
			
			List<Map<String,Object>> list = this.starAssessService.getOverallvaluateList(map);
			
			data.put("num", count);
			data.put("datalist", list);
			data.put("status",0);
			data.put("pager",page.JSCateringPage().toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			data.put("overallvaluatelist", "");
			data.put("status",1);
		}
		
		return data;
	}
	
	
	/**
	 * 星值详情页面
	 * 传入参数{"userid":1,"overallvaluateid":"fd2372de523a42e1b6e9275867de454b"}
	 * 传出参数{"message":"查询成功","status":0,"evaluateInfo":{"createtime":1468830093000,"companyid":"67702cc412264f4ea7d2c5f692070457","overallvaluateid":"fd2372de523a42e1b6e9275867de454b","createid":"690fb669ed4d40219964baad7783abd4","examineuserid":"1","organizeid":"5","organizename":"浦东金桥分店","createname":"silence","examinename":"silence","sumstar":4},"evaluatelist":[{"createtime":1468830093000,"overallvaluateid":"fd2372de523a42e1b6e9275867de454b","createid":"690fb669ed4d40219964baad7783abd4","description":"111111","starid":"16680709c40c4b59ae494fb841720788","projectid":"2","projectname":"为人态度","starlevel":4}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getOverallvaluateInfo")
	@ResponseBody
	public Map<String, Object> getOverallvaluateInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		
		try {
			//综合自评详情
			Map<String, Object> evaluateInfo=this.starAssessService.getOverallvaluateInfo(map);
			//综合自评项目打分列表
			List<Map<String, Object>> evaluatelist=this.starAssessService.getOverallvaluateStarList(evaluateInfo);
			
			data.put("datamap", evaluateInfo);
			data.put("datalist", evaluatelist);
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
	 * 审核岗位星值  同意或者拒绝
	 * 传入参数{"userid":1,"overallvaluateid":"fd2372de523a42e1b6e9275867de454b","opinion":"balala","result":1}
	 * 传出参数{"message":"操作成功","status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/examineOverallvaluate")
	@ResponseBody
	public Map<String, Object> examineOverallvaluate(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> data=new HashMap<String, Object>(); 
		map.put("status", 1);
		map.put("updatetime", new Date());
		map.put("updateid", map.get("userid"));
		this.starAssessService.updateOverallvaluate(map);
		data.put("status", 0);
		data.put("message", "操作成功");
		return data;
	}
	/**
	 * 
	 * 导出综合星值列表
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportOverallList",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogRequestList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/综合星值列表.xls");
		List<Map<String, Object>> list=this.starAssessService.getOverallvaluateList(map);

        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
        String[] headers = { "申请人","申请时间","总星值","状态","审批结果"};
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> log:list){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a",log.get("createname")==null?"":""+log.get("createname"));
	        datamap.put("b",log.get("createname")==null?"":""+log.get("createname"));
	        datamap.put("c",  log.get("sumstar")==null?"":log.get("sumstar"));
	        datamap.put("d", Integer.parseInt(log.get("status").toString())==0?"待处理":"已处理");
	        if(log.containsKey("result")){
	        	datamap.put("e", Integer.parseInt(log.get("result").toString())==1?"已同意":"已拒绝");
	        }
	        dataset.add(datamap);
        }
        try  
        {
            OutputStream out = new FileOutputStream(importurl);  
            ex.exportExcel(headers, dataset, out);
            out.close();  
            System.out.println("excel导出成功！");
        }  
        catch (FileNotFoundException e)  
        {
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }
		return "综合星值列表.xls";
	}
	
	/**
	 * 
	 * 导出审批详情
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping(value="/exportOverallDetail",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportLogRequestDetail(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		String importurl=request.getSession().getServletContext().getRealPath("/upload/excel/综合星值详情.xls");
		//综合自评详情
		Map<String, Object> emap=this.starAssessService.getOverallvaluateInfo(map);
		//综合自评项目打分列表
		List<Map<String, Object>> evaluatelist=this.starAssessService.getOverallvaluateStarList(emap);
        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>(); 
        String[] headers = { "所属区域","岗位名称","星值模板","模板描述","星值","合计","申请人","申请时间","状态","审批结果","审批人","审批意见"};
        
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> log:evaluatelist){
	        TreeMap<String, Object> datamap=new TreeMap<String, Object>();
	        datamap.put("a",emap.get("organizename")==null?"":""+emap.get("organizename"));
	        datamap.put("b",emap.get("identityname")==null?"":""+emap.get("identityname"));
	        datamap.put("c",log.get("projectname")==null?"":log.get("projectname"));
	        datamap.put("d",log.get("description")==null?"":""+log.get("description"));
	        datamap.put("e",log.get("starlevel")==null?"":""+log.get("starlevel"));
	        datamap.put("f",emap.get("sumstar")==null?"":emap.get("sumstar"));
	    
	        datamap.put("g",emap.get("createname")==null?"":""+emap.get("createname"));
	        datamap.put("h",emap.get("createtime")==null?"":""+emap.get("createtime"));
	        datamap.put("i",Integer.parseInt(emap.get("status").toString())==0?"待处理":"已处理");
	        if(emap.containsKey("result")){
	        	datamap.put("j", Integer.parseInt(emap.get("result").toString())==1?"已同意":"已拒绝");
	        }
	        datamap.put("k",emap.get("examinename")==null?"":""+emap.get("examinename"));
	        datamap.put("l",emap.get("opinion")==null?"":""+emap.get("opinion"));
	        dataset.add(datamap);
        }
     
        
        try  
        {
            OutputStream out = new FileOutputStream(importurl);  
            ex.exportExcel(headers, dataset, out);
            out.close();  
            System.out.println("excel导出成功！");
        }  
        catch (FileNotFoundException e)  
        {
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }
		return "综合星值详情.xls";
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
	 * 星值规则列表
	 * 传入参数{"organizeid":5}
	 * 传出参数{"num":1,"page":{"pageSize":10,"pageNo":1,"totalRecords":1,"totalPages":1,"lastPageNo":1,"previousPageNo":1,"nextPageNo":1,"indexPageNo":1},"status":0,"rulelist":[{"content":"测测测测","createtime":1468831924000,"companyid":"67702cc412264f4ea7d2c5f692070457","title":"测试规则","updatetime":1468831921000,"createid":"1","ruleid":"1"}]}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getStarRuleList")
	@ResponseBody
	public Map<String, Object> getStarRuleList(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageHelper page = new PageHelper(request);
		int num=this.starAssessService.getStarRuleListNum(map);		
		page.setTotalCount(num);
		page.initPage(map);
		List<Map<String, Object>> rulelist=this.starAssessService.getStarRuleList(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("num", num);
		data.put("rulelist", rulelist);
		data.put("status",0);
		data.put("pager",page.JSCateringPage().toString());
		return data;
	}
	
	/**
	 * 星值规则详情
	 * 传入参数{"ruleid":1}
	 * 传出参数{"ruleInfo":{"content":"测测测测","createtime":1468831924000,"companyid":"67702cc412264f4ea7d2c5f692070457","title":"测试规则","updatetime":1468831921000,"createid":"1","ruleid":"1"},"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getStarRuleInfo")
	@ResponseBody
	public Map<String, Object> getStarRuleInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> ruleInfo=this.starAssessService.getStarRuleInfo(map);
		Map<String, Object> data=new HashMap<String, Object>();
		data.put("ruleInfo", ruleInfo);
		data.put("status",0);
		return data;
	}
}
