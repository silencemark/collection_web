<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>OA办公扩容-使用方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/sweetalert/dist/sweetalert-dev.js"></script> 
<script type="text/javascript">
	var pageWidth = window.innerWidth;
	var pageHeight = window.innerHeight;
	var minHeight;
	var usernameWidth;
	if (typeof pageWidth != "number")
	{
		if(document.compatMode == "CSS1Compat")
		{
			pageWidth = document.documentElement.clientWidth;
			pageHeight = document.documentElement.clientHeight;
		}
		else
		{
			pageWidth = document.body.clientWidth;
			pageHeight = document.body.clientHeight;
		}
	}
	minHeight = pageHeight - 77;
	$(document).ready(function(){		
		$(".main_page").css("min-height",minHeight+"px");
		
		usernameWidth = $(".top_username").width();		
		usernameWidth = (160 - usernameWidth)/2;
	    $(".top_username").css("margin-left",usernameWidth+"px");		
		$(".head_box .user_box").hover(
			  function () {
				$(".head_box .user_box .name").addClass("name_border"); //移入
				$(".head_box .user_box .box").show();
			  },
			  function () {
				$(".head_box .user_box .name").removeClass("name_border");//移除\
				$(".head_box .user_box .box").hide();
			  }
			);			
	});
</script>
</head>

<body>
<jsp:include page="../top.jsp" flush="true"></jsp:include>
	<div class="main_page">
	<jsp:include page="../left.jsp" flush="true"></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList">企业信息列表</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyInfo?companyid=${map.companyid}">公司信息</a><i>/</i><a href="/managebackstage/getManageCompanyCloudModuleList?companyid=${map.companyid }">企业云盘</a><i>/</i><a href="/managebackstage/intoManageCompanyCloudFilePage?moduleid=${map.moduleid }&companyid=${map.companyid}">企业云盘文件</a><i>/</i><span>企业云盘文件详情</span></p></div>  
    <div class="sug_detail page_tab m_top">
    <form id="deleteform" action="<%=request.getContextPath() %>/userbackstage/deleteCompanyCloudFileInfo" method="post">
	    <input type="hidden" id="moduleid" name="moduleid" value="${map.moduleid }"/>
	    <input type="hidden" id="fileid" name="fileid" value="${map.fileid }"/>
	    <input type="hidden" id="usedmemory" name="usedmemory"/>
	    <input type="hidden" id="oldfileurl" name="oldfileurl"/>
    </form>
    	<div class="tab_name"><span id="title"></span></div>
        <div class="span"><span id="realname"></span><span id="createtime"></span></div>
<!--         <div class="fiel_box"> -->
<!--             <div class="fiel_name" id="filename"></div> -->
<!--             <div class="fiel_num" id="memory"></div> -->
<!--             <div class="fiel_link" id="down_div" style="cursor: pointer;" onclick="">下载</div> -->
<!--             <div class="clear"></div>        -->
<!--         </div> -->
        <div class="span2">发布范围：<font id="range"></font></div>
        <div class="p_box" id="content">
<!--         <p>象征权威、高雅、低调、创意；也意味着执着、冷漠、防御，端视服饰的款式 与风格而定。黑色为大多数主管或白领专业人士所喜爱，当你需要极度权威、表现专业、展现品味、不想引人注目或想专心处理事情时，例如高级主管的日常穿著、 主持演示文稿、在公开场合演讲、写企划案、创作、从事跟“美”、“设计”有关的工作时，可以穿黑色。</p> -->
<!--         <p>象征诚恳、沉稳、考究。其中的铁灰、炭灰、暗灰，在无形中散发出智能、成 功、强烈权威等强烈讯息；中灰与淡灰色则带有哲学家的沉静。当灰色服饰质感不佳时，整个人看起来会黯淡无光、没精神，甚至造成邋遢、不干净的错觉。灰色在 权威中带着精确，特别受金融业人士喜爱；当你需要表现智能、成功、权威、诚恳、认真、沉稳等场合时，可穿著灰色衣服现身。</p> -->
		</div>
    </div>
</div>

</body>

<script>
$(function(){
	queryCloudfileDetail();
});
function queryCloudfileDetail(){
	var param = new Object();
	param.moduleid = $('#moduleid').val();
	param.fileid = $('#fileid').val();
	requestPost("/managebackstage/getManageCompanyCloudFileInfo",param,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == 0 && resultMap.status == '0'){
				var filemap = resultMap.filemap;
				if(filemap != undefined && filemap != null){
					$('#title').text(filemap.title);
					$('#realname').text(filemap.realname);
					$('#createtime').text(filemap.createtime);
					$('#filename').text(filemap.filename);
					$('#memory').text(filemap.memory);
					$('#oldfileurl').val(filemap.fileurl);
					$('#usedmemory').val(filemap.kb_memory);
					$('#down_div').attr("onclick","downloadfile('"+filemap.fileurl+"')");
					var rangelist = filemap.rangelist;
					if(rangelist != undefined && rangelist != null){
						var htm = "";
						$.each(rangelist,function(i,map){
							htm += map.rangename+"，";
						});
						$("#range").append(htm.substring(0,(htm.length - 1)));
					}
					$('#content').html(filemap.content);
				}
			}
		}
	});
}
//公共的查询方法
function requestPost(url,param,callback){
	$.ajax({
		url:url,
		type:"post",
		data:param,
		beforeSend:function(){
			var load = '<div id="load_mask" class="div_mask" style="opacity:0;"></div>'+
				      '<div id="load_loading" class="loading"><img src="../userbackstage/images/public/loading.gif" width="360" height="200" /></div>';
			$("body").after(load);
		},
		success:function(resultMap){
			callback(resultMap);
		},
		complete:function(){
			$('#load_mask').remove();
			$('#load_loading').remove();
		},
		error:function(e){
			
		}
	});
}
</script>
</html>
