<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>KPI星级考核规则详情-使用方后台</title>
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
	$(document).ready(function(){
		$('#nav_kpirule').attr('class','active');$('#nav_kpirule').parent().parent().show();
	})
</script>
</head>

<body>
<jsp:include page="../top.jsp" flush="true"></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" flush="true"></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">KPI星级考核</a><i>/</i><a href="/userbackstage/intoRuleListPage">KPI规则</a><i>/</i><span>KPI规则详情</span></p></div>    
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1">规则详情</span><a href="/userbackstage/deleteStarRankingRuleInfo?ruleid=${map.ruleid }">删除</a><a href="/userbackstage/intoRuleEditor?ruleid=${map.ruleid }">修改</a></div>
        <div class="news_detail">
        	<div class="name" id="title"></div>
            <div class="span"><span id="realname"></span><span id="createtime"></span></div>
            <div class="p_box">
            	<p id="content"></p>
            </div>
        </div>
    </div>
</div>

</body>
<script>
	$(function(){
		queryDetail();
	});
	function queryDetail(){
		var param = new Object();
		param.ruleid = "${map.ruleid}";
		requestPost("/userbackstage/getStarRankingRuleDetailInfo",param,function(resultMap){
			if(resultMap != undefined && resultMap != null){
				if(resultMap.status == 0 && resultMap.status == '0'){
					var rulemap = resultMap.rulemap;
					
					$('#title').text(rulemap.title);
					$('#realname').text(rulemap.realname);
					$('#createtime').text(rulemap.createtime);
					$('#content').html(rulemap.content);
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
