package com.collection.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {
	
	/**
	 * html标签转义
	 * @param content
	 * @return
	 */
	public static String htmlEscape(String content) {
		if (content == null)
			return "";
		String html = content;
		// TODO BUG!!! 替换空白的动作多余，会导致编辑器存显不一致！！！
		/* 修复BUG：页面样式被修改 */ // html = html.replaceAll("&nbsp;", " ");// 替换空格
		html = html.replaceAll("&", "&amp;");
		html = html.replaceAll("'", "&apos;");
		html = html.replaceAll("\"", "&quot;"); // "
		/* 修复BUG：页面样式被修改 */ // html = html.replaceAll("\t", "&nbsp;&nbsp;");// 替换跳格
		/* 修复BUG：页面样式被修改 */ // html = html.replaceAll(" ", "&nbsp;");// 替换空格
		html = html.replaceAll("<", "&lt;");
		html = html.replaceAll(">", "&gt;");
		return html;
	}
	
	
	 /**
	  * content 获取所有图片路径
	  * @param htmlStr
	  * @return
	  */
	 public static List<String> getImageSrc(String htmlStr){        
	        String img="";        
	        Pattern p_image;        
	        Matcher m_image;        
	        List<String> pics = new ArrayList<String>();     
	       
	        String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址        
	        p_image = Pattern.compile      
	                (regEx_img,Pattern.CASE_INSENSITIVE);        
	       m_image = p_image.matcher(htmlStr);      
	       while(m_image.find()){        
	            img = img + "," + m_image.group();        
	            Matcher m  = Pattern.compile(" src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src     
	            while(m.find()){     
	               pics.add(m.group(1));     
	            }     
	        } 
	       
	        return pics;        
	    }  
	
	 /**
	  * 替换htmlString内容中的a标签href值
	  * @param htmlStr
	  * @return
	  */
	 public static List<String> getAHref(String htmlStr){
		  String img="";        
	      Pattern p_a;        
	      Matcher m_a;        
	      List<String> pics = new ArrayList<String>();     
	      String regEx_img = "<a.*href=(.*?)[^>]*?>"; //a标签链接地址        
	      p_a = Pattern.compile      
	              (regEx_img,Pattern.CASE_INSENSITIVE);        
	     m_a = p_a.matcher(htmlStr);      
	     while(m_a.find()){        
	          img = img + "," + m_a.group();        
	          Matcher m  = Pattern.compile(" href=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配href     
	          while(m.find()){     
	             pics.add(m.group(1));     
	          }     
	      } 
	       return pics;
	 }
	 
	 
	 /**
	  * 返回图片路径和a标签替换成功的html内容
	  * @param htmlStr
	  * @param projectPath
	  * @return
	  */
	 public static String returnHtmlStr(String htmlStr,String projectPath){
		 List<String> imglist=HtmlUtil.getImageSrc(htmlStr);//原图片的集合
		 List<String> newimglist=new ArrayList<String>(imglist.size());//新图片集合
			//将不合法的图片路径加上项目域名
			if(imglist!=null){
				for(String s:imglist){
					if(s!=null && s.indexOf("http")==0){
						newimglist.add(s);
					}else{
						newimglist.add(projectPath+s);
					}
				}
			}
			//将内容中不合法的图片路径替换
			int leng=imglist.size();
			for(int i=0;i<leng;i++){
				
				System.out.println(imglist.get(i));
				htmlStr=htmlStr.replaceFirst(imglist.get(i).replace("?", "\\?"),newimglist.get(i));
			}
			
		 return htmlStr;
	 }
	 

  public static void main(String[] args) {
	String htmlStr = "<h1>2014年8月餐饮服务许可证发证信息公示</h1> <p><img border=\"0\" src=\"/editor/sysimage/file/xls.gif\" /><a href=\"/editor/Fileview?filename=2014090303181693.xls\" target=\"_blank\">餐饮201408.xls</a></p>";
	 String s = "<div class=\"\" id=\"content\"><h1>我所开展“学校卫生综合评价”卫生监督知识专题培训</h1>"; 
	 s+="<p></p><div>&nbsp;&nbsp;&nbsp; 2016年5月13日下午，我所举办了“学校卫生综合评价”卫生监督知识专题培训班，市所卫生监督员、区镇预防保健所负责学校卫生的监督员以及2016年拟开展学校卫生综合评价学校的校医共83人参加了培训。此次培训旨在提升我市学校卫生监管水平，加大学校传染病防控和医疗机构管理、饮用水卫生、学习环境卫生的监督执法力度，督促学校依法履行职责，规范管理行为。";
	 s+="</div><div>&nbsp;&nbsp;&nbsp; 培训班邀请了上海市学校卫生首席监督员杨艰萍来昆讲授，杨专家讲述了学校卫生综合评价标准出台的背景、目的和意义，并结合上海市学校卫生监督实践，从监督评分、监测评分、综合评价、信息填报等四个方面系统地讲授学校卫生综合评价标准的操作。通过培训，使我市卫生监督员和有关学校校医了解和掌握了《学校卫生综合评价》（GB/T 18205-2012）国标的具体内容和操作方法，对促使我市学校卫生监督管理工作向更科学、规范、有效的方向迈进具有积极的作用。";
	 s+="</div> <p align=\"center\"><img src=\"/editor/Picview?filename=20160518110031529\"></p> <p>&nbsp;</p></div>";
	 String str = HtmlUtil.returnHtmlStr(s, "http://www.kswj.js.cn/");
	 System.out.println(str);
}
	 
}
