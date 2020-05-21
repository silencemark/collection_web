<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>OA办公企业简报详情-使用方后台</title>
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
	
	function deleteBriefInfo(){
		swal({
		    title: "提示",
		    text: "确定要删除该简报信息吗！",
		    type: "warning",
		    showCancelButton: true,
		    confirmButtonColor: "#ff7922",
		    confirmButtonText: "确定",
		    cancelButtonText: "取消",
		    closeOnConfirm: true
		}, function(){
			location.href="/userbackstage/deleteBriefInfo?briefid=${briefmap.briefid }&moduleid=${briefmap.moduleid}&title=${briefmap.title }";
		});
	}
	$(document).ready(function(){
		$('#nav_brief').attr('class','active');$('#nav_brief').parent().parent().show();
	})
</script>
</head>

<body>
<jsp:include page="../top.jsp" flush="true"></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" flush="true"></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">OA办公</a><i>/</i><a href="/userbackstage/getCompanyModuleList">企业简报栏目</a><i>/</i><a href="javascript:void(0)" onclick="window.history.go(-1)">企业简报列表</a><i>/</i><span>企业简报详情</span></p></div>  
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1">企业简报详情</span><a href="javascript:void(0)" onclick="deleteBriefInfo()">删除</a><a href="/userbackstage/intoBriefingEditPage?briefid=${map.briefid}">修改</a></div>
        <div class="news_detail">
        	<div class="name">${briefmap.title }</div>
            <div class="span"><span>${briefmap.realname }</span><span>${briefmap.createtime }</span></div>
            <div class="p_box">
            	<p>${briefmap.content }</p>
            </div>
        </div>
    </div>
</div>

</body>
</html>
