package com.collection.controller.app;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.collection.service.IAppIndexService;


/**
 * 首页功能
 * @author silence
 *
 */
@Controller
@RequestMapping("/appIndex")
public class AppIndexController extends BaseController{
	@SuppressWarnings("unused")
	private transient static Log log = LogFactory.getLog(AppIndexController.class);
	
	@Resource private IAppIndexService appindexService;
	
	
	/**
	 * 首页banner图
	 * 传入参数{"type":"1"}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getBanner")
	@ResponseBody
	public List<Map<String, Object>> getBanner(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		return this.appindexService.getHomePageBanner(map);
	}
	
	/**
	 * 首页广告信息
	 * 传入参数{}
	 * 传出参数[
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getAdvertisement")
	@ResponseBody
	public List<Map<String, Object>> getAdvertisement(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		return this.appindexService.getAdvertisement(map);
	}
	
	
	/**
	 * 首页免费影片列表
	 * 传入参数{"type":1， "userid":"45651"}
	 * 传出参数
	 * @param type
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/getHomePageMovie")
	@ResponseBody
	public List<Map<String, Object>> getHomePageMovie(@RequestParam Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		return this.appindexService.getHomePageMovie(map);
	}
}