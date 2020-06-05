package com.collection.controller.userbackstage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.json.JSON;
import com.base.controller.BaseController;
import com.collection.redis.RedisUtil;
import com.collection.service.CompanyService;
import com.collection.service.IndexService;
import com.collection.service.UserInfoService;
import com.collection.util.CookieUtil;
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
@RequestMapping("/userbackstage")
public class UserIndexController extends BaseController{
	private transient static Log log = LogFactory.getLog(UserIndexController.class);
	
	@Resource private IndexService indexService;

	@Resource private UserInfoService userInfoService;
	
	@Resource private CompanyService companyService;
	
	/**
	 * 初始化用户登录
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/initlogin")
	public String initlogin(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		//查询权限菜单
		map.put("userid", map.get("pcuserid"));
		model.addAttribute("map", map);
		return "/userbackstage/login";
	}
	/**
	 * 用户登录
	 * @param map
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	public String adminLogin(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response,HttpServletRequest request){
		//查询权限菜单
		model.addAttribute("map", map);
		if(map.size() > 0){
			
			String password=Md5Util.getMD5(map.get("password")+""); 
			map.put("password", password);
			Map<String, Object> userMap=this.userInfoService.getUserInfo(map);  
			if(userMap != null && userMap.size()>0){
				 if(!map.containsKey("username") || String.valueOf(map.get("username")).equals("")){
					 model.addAttribute("errors", "用户名不能为空");
					return "/userbackstage/login";
				 }
				 if(userMap.containsKey("status") && String.valueOf(userMap.get("status")).equals("0")){
					 model.addAttribute("errors", "您的帐号已被禁用");
					return "/userbackstage/login";
				 }
				//登录成功修改最后登录时间
				Map<String, Object> loginmap=new HashMap<String, Object>();
				loginmap.put("logintime", new Date());
				loginmap.put("userid", userMap.get("userid"));
				loginmap.put("companyid", userMap.get("companyid"));
				this.userInfoService.updateLoginime(loginmap);
				
				Map<String, Object> shopmap=new HashMap<String, Object>();
				shopmap.put("userid", userMap.get("userid"));
				shopmap.put("type", 3);
				shopmap.put("companyid", userMap.get("companyid"));
				List<Map<String, Object>> organizelist=this.indexService.getOrganizeListByUser(shopmap);
				List<Map<String, Object>> shoplist=new ArrayList<Map<String,Object>>();
				for(Map<String, Object> shop:organizelist){
					if("3".equals(String.valueOf(shop.get("type")))){
						shoplist.add(shop);
					}
				}
				if(shoplist != null && shoplist.size()>0){
					userMap.put("organizeid",shoplist.get(0).get("organizeid"));
					userMap.put("organizename",shoplist.get(0).get("organizename"));
					userMap.put("datacode",shoplist.get(0).get("datacode"));
				}
				
				// 登陆成功生成唯一cookiesid
				String cookiesid = userMap.get("userid") + "_" + System.currentTimeMillis();
				// cookiesid存入cookies中,有效期30分钟
				CookieUtil.setCookie(response, UserUtil.USERINFO, cookiesid, "/", null);
				// 用户信息存入reids中,有效期30分钟
				RedisUtil.setMap(cookiesid, userMap);
				// 存储登录来源
				CookieUtil.setCookie(response, UserUtil.FROMINFO, "USER", "/",null);			
				
				//查询 该用户的角色对应的菜单
				List<Map<String, Object>> menuList=this.userInfoService.getMenuList(userMap);
				Map<String, Object> menuMap = new HashMap<String, Object>();
				try {
					menuMap.put("data", JSON.json(menuList));
				} catch (IOException e) {
					e.printStackTrace();
				}
				RedisUtil.setMap(cookiesid+"_menu",menuMap);;
				return "redirect:/userbackstage/index";
			} 
			else{
				model.addAttribute("errors", "用户名或密码输入错误");
				return "/userbackstage/login";
			}
		}else{
			return "/userbackstage/login";
		} 		
	}
	/**
	 * 
	 * 首页
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/index")
	public String index(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		//查询公司信息 （包括 总人数、未激活人数、店面数、 总内存、剩余空间）
		Map<String, Object> userinfo=UserUtil.getUser(request);
		Map<String, Object> indexInfo=this.userInfoService.getIndexInfo(userinfo);
		model.addAttribute("userinfo", userinfo);
		model.addAttribute("indexInfo", indexInfo);
		
		map.put("companyid", userinfo.get("companyid"));
		//初始化分页
		PageHelper pageHelper = new PageHelper(request);
		pageHelper.initPage(map);
		List<Map<String, Object>> userList = userInfoService.getNotuseUserList(map);
		int num = userInfoService.getNotuseUserListNum(map);
		pageHelper.setTotalCount(num);
		model.addAttribute("userList", userList);
		model.addAttribute("pager", pageHelper.cateringPage().toString());
		model.addAttribute("map", map);
		return "/userbackstage/index";
	}
	/**
	 * 
	 * 首页界面未使用人员搜索
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/searchUnusedPerson")
	@ResponseBody
	public Map<String, Object> indexSearch(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>();
		try {		
			//搜索信息 （包括 总人数、未激活人数、店面数、 总内存、剩余空间）
			Map<String, Object> userinfo=UserUtil.getUser(request);
			model.addAttribute("userinfo", userinfo);		
			map.put("companyid", userinfo.get("companyid"));
			//初始化分页
			PageHelper pageHelper = new PageHelper(request);
			pageHelper.initPage(map);
			List<Map<String, Object>> userList = userInfoService.getNotuseUserList(map);
			int num = userInfoService.getNotuseUserListNum(map);
			pageHelper.setTotalCount(num);
			data.put("userlist", userList);
			data.put("pager", pageHelper.cateringPage().toString());
			data.put("status", 0);
			data.put("message", "搜索成功");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "搜素错误");
		}
		return data;
	}
	
	/**
	 * 
	 * 再次邀请
	 * 传入参数{"userid":"67702cc412264f4ea7d2c5f692070457"}
	 * 传出参数{"message":"查询成功","modulelist":[{"createtime":1468983040000,"companyid":"67702cc412264f4ea7d2c5f692070457","modulename":"公司新闻","moduleimage":"1.jpg","updatetime":1468983120000,"createid":"690fb669ed4d40219964baad7783abd4","moduleid":"fdd61606a640459081bce3d02dd8e01a"}],"status":0}
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 * @author silence
	 */
	@RequestMapping("/inviteUser")
	@ResponseBody
	public Map<String, Object> inviteUser(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
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
				data.put("message", "发送邀请成功");
				
				userInfo.put("password", Md5Util.getMD5(code));
				userInfo.put("invitetime", new Date());
				userInfo.put("isinvite", 1);
				this.userInfoService.updateUserInfo(userInfo);
				Map<String, Object> user=UserUtil.getUser(request);
				
				insertManageLog(user.get("companyid")+"",2,"PC端首页","邀请了用户"+map.get("realname"),user.get("userid")+"");
				
			} catch (Exception e) {
				data.put("status", 1);
				data.put("message", "发送邀请错误");
				return data;
			}
		}else{
			data.put("status", 1);
			data.put("message", "用户不存在");
		}
		return data;
	}
	
	
	/**
	 * 修改用户信息
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @author silence
	 * @return
	 */
	@RequestMapping("/updateUserInfo")
	@ResponseBody
	public Map<String, Object> updateUserInfo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data=new HashMap<String, Object>(); 
		try {
			this.userInfoService.updateUserInfo(map);
			
			String userid = String.valueOf(map.get("userid"));
			if(map.containsKey("userid") && !"".equals(map.get("userid"))){
				if(map.containsKey("managerole") && !"".equals(map.get("managerole"))){
					try {
						//同步app端 权限
						realTiemUpdateUserPower(userid);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
			data.put("status", 0);
			data.put("message", "修改成功");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("status", 1);
			data.put("message", "修改错误");
		}
		return data;
	}
	
	/** 
	 * 利用开源组件POI3.0.2动态导出EXCEL文档 转载时请保留以下信息，注明出处！ 
	 *  
	 * @author leno 
	 * @version v1.0 
	 * @param <T> 
	 *            应用泛型，代表任意一个符合javabean风格的类 
	 *            注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx() 
	 *            byte[]表jpg格式的图片数据 
	 */  
	public static class ExportExcel<T>  
	{  
	    public void exportExcel(Collection<T> dataset, OutputStream out)  
	    {  
	        exportExcel("测试POI导出EXCEL文档", null, dataset, out, "yyyy-MM-dd");  
	    }  
	  
	    public void exportExcel(String[] headers, Collection<T> dataset,  
	            OutputStream out)  
	    {  
	        exportExcel("测试POI导出EXCEL文档", headers, dataset, out, "yyyy-MM-dd");  
	    }  
	  
	    public void exportExcel(String[] headers, Collection<T> dataset,  
	            OutputStream out, String pattern)  
	    {  
	        exportExcel("测试POI导出EXCEL文档", headers, dataset, out, pattern);  
	    }  
	  
	    /** 
	     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上 
	     *  
	     * @param title 
	     *            表格标题名 
	     * @param headers 
	     *            表格属性列名数组 
	     * @param dataset 
	     *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的 
	     *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据) 
	     * @param out 
	     *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中 
	     * @param pattern 
	     *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd" 
	     */  
	    @SuppressWarnings("unchecked")  
	    public void exportExcel(String title, String[] headers,  
	            Collection<T> dataset, OutputStream out, String pattern)  
	    {  
	        // 声明一个工作薄  
	        HSSFWorkbook workbook = new HSSFWorkbook();  
	        // 生成一个表格  
	        HSSFSheet sheet = workbook.createSheet(title);  
	        // 设置表格默认列宽度为15个字节  
	        sheet.setDefaultColumnWidth((short) 18);  
	        // 生成一个样式  
	        HSSFCellStyle style = workbook.createCellStyle();  
	        // 设置这些样式  
	        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);  
	        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
	        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
	        // 生成一个字体  
	        HSSFFont font = workbook.createFont();  
	        font.setColor(HSSFColor.BLACK.index);  
	        font.setFontHeightInPoints((short) 12);  
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
	        // 把字体应用到当前的样式  
	        style.setFont(font);  
	        // 生成并设置另一个样式  
	        HSSFCellStyle style2 = workbook.createCellStyle();  
	        style2.setFillForegroundColor(HSSFColor.WHITE.index);  
	        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
	        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
	        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
	        // 生成另一个字体  
	        HSSFFont font2 = workbook.createFont();
	        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
	        // 把字体应用到当前的样式  
	        style2.setFont(font2);  
	  
	        // 声明一个画图的顶级管理器  
	        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();  
	        // 定义注释的大小和位置,详见文档  
	        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,  
	                0, 0, 0, (short) 4, 2, (short) 6, 5));  
	        // 设置注释内容  
	        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));  
	        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.  
	        comment.setAuthor("leno");  
	  
	        // 产生表格标题行  
	        HSSFRow row = sheet.createRow(0);  
	        for (short i = 0; i < headers.length; i++)  
	        {  
	            HSSFCell cell = row.createCell(i);  
	            cell.setCellStyle(style);
	            HSSFRichTextString text = new HSSFRichTextString(headers[i]);  
	            cell.setCellValue(text);  
	        }
	  
	        List<Map<String, Object>> it=(List<Map<String, Object>>) dataset;
	        int index = 0;
	        for(Map<String, Object> data:it){
	        	index++;
	        	row = sheet.createRow(index);
	        	Iterator<String> iterator = data.keySet().iterator();
	        	int i=0;
	    		while (iterator.hasNext()) {
	    			HSSFCell cell = row.createCell(i);  
	                cell.setCellStyle(style2);  
	    			Object key = iterator.next();
	    			Object value=data.get(key);
	    			String textValue = null;
	    			if(!value.equals("")){
	    				textValue = value.toString();
	    			}else{
	    				textValue="";
	    			}
	    			if (textValue != null)
                    {
                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");  
                        Matcher matcher = p.matcher(textValue);  
                        if (matcher.matches())  
                        {  
                            // 是数字当作double处理  
                            cell.setCellValue(Double.parseDouble(textValue));  
                        }  
                        else  
                        {  
                            HSSFRichTextString richString = new HSSFRichTextString(  
                                    textValue);  
                            HSSFFont font3 = workbook.createFont();  
                            font3.setColor(HSSFColor.BLUE.index);
                            richString.applyFont(font3);
                            cell.setCellValue(richString);
                        }
                    }
	    			i++;
	    		}
	        } 
	        try
	        {  
	            workbook.write(out);  
	        }  
	        catch (IOException e)  
	        {  
	            e.printStackTrace();  
	        }  
	    }  
	  
	    public static void main(String[] args)  
	    {  
	        // 测试学生  
	        ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();  
	        String[] headers =  
	        { "学号", "姓名", "年龄", "性别", "出生日期" };  
	        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
	        TreeMap<String, Object> studentmap=new TreeMap<String, Object>();
	        studentmap.put("a", 10000001);
	        studentmap.put("b", "张三");
	        studentmap.put("c", "20");
	        studentmap.put("d",true);
	        studentmap.put("e", new Date());
	        dataset.add(studentmap);
//            List<Student> dataset = new ArrayList<Student>();  
//            dataset.add(new Student(10000001, "张三", 20, true, new Date()));  
//            dataset.add(new Student(20000002, "李四", 24, false, new Date()));  
//            dataset.add(new Student(30000003, "王五", 22, true, new Date()));  
	        // 测试图书  
	        ExportExcel<Map<String, Object>> ex2 = new ExportExcel<Map<String, Object>>();  
	        String[] headers2 =  
	        { ".", "图书名称", "图书作者", "图书价格", "图书ISBN", "图书出版社", "封面图片" };  
	        List<Map<String, Object>> dataset2 = new ArrayList<Map<String, Object>>();
	        try  
	        {
	        	TreeMap<String, Object> bookmap=new TreeMap<String, Object>();
	            bookmap.put("a", 1);
	            bookmap.put("b","java编程思想");
	            bookmap.put("c","leno");
	            bookmap.put("d","300.33f");
	            bookmap.put("e","1234567");
	            bookmap.put("f","清华出版社");
	            bookmap.put("g","ceshi");
	            dataset2.add(bookmap);
	            OutputStream out = new FileOutputStream("E://a.xls");  
	            OutputStream out2 = new FileOutputStream("E://b.xls");  
	            ex.exportExcel(headers, dataset, out);  
	            ex2.exportExcel(headers2, dataset2, out2);  
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
	    }
	    
	}
	@RequestMapping("/downloadexcel") 
	public ResponseEntity<byte[]> downloadexcel(@RequestParam Map<String,Object> map,HttpServletRequest request)
            throws IOException {
		String rspName=request.getSession().getServletContext().getRealPath("/upload/excel/");
		HttpHeaders headers = new HttpHeaders();  
		rspName = rspName+"/"+map.get("fileName");  
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  
		headers.setContentDispositionFormData("attachment", new String((map.get("fileName")+"").getBytes("gb2312"),  
		                "iso-8859-1"));  
		File file = new File(rspName);
		byte[] bytes = FileUtils.readFileToByteArray(file);  
		try {  
		    if (file.exists()){
		        file.delete();  
		    }
		} catch (Exception e){
		    e.printStackTrace();  
		}  
		return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
	}
	
	
	/**
	 * 查询某人员上级是否存在店面
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryUserUpIsExistsShop")
	public Map<String,Object> queryUserUpIsExistsShop(@RequestParam Map<String,Object> map , HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			List<Map<String,Object>> userorganizelist = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> orglist = this.companyService.getUserShops(map);
			if(orglist != null && orglist.size() > 0){
				for(Map<String,Object> orgmap : orglist){
					Map<String,Object> userorgmap = this.companyService.queryUserUpIsExistsShop(orgmap);
					if(userorgmap != null && userorgmap.size() > 0){
						userorganizelist.add(userorgmap);
					}
				}
			}
			
			if(userorganizelist != null && userorganizelist.size() > 1){
				for (int i = 0; i < userorganizelist.size() - 1; i++) {
					for (int j = userorganizelist.size() - 1; j > i; j--) {
						if (userorganizelist.get(j).get("organizeid").equals(userorganizelist.get(i).get("organizeid"))) {
							userorganizelist.remove(j);
						}
					}
				}
			}
			
			resultMap.put("datalist", userorganizelist);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultMap;
	}
}
