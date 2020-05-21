<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>OA办公扩容记录-使用方后台</title>
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
		$('#nav_capacity').attr('class','active');$('#nav_capacity').parent().parent().show();
	})
</script>
</head>

<body>
<jsp:include page="../top.jsp" flush="true"></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" flush="true"></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">OA办公</a><i>/</i><a href="/userbackstage/intoAddCloudCapacity">扩容</a><i>/</i><span>扩容记录</span></p></div>  
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1">扩容记录</span><a href="/userbackstage/intoAddCloudCapacity">扩容</a></div>
        <div class="ts_txt">以下是您企业空间扩容的记录&nbsp;&nbsp;&nbsp;&nbsp;共计：<span class="yellow"> ${memoryCount } G（包含平台赠送的${feedmemory }G）</span></div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
                	<td width="30%">申请大小</td>
                    <td width="30%">实际大小</td>
                    <td width="30%">申请时间</td>
                    <td class="t_r">状态</td>
                </tr>
                <c:forEach items="${cloudlist }" var="cl">
                	<tr>
	                	<td>${cl.memory }</td>
	                    <td>${cl.sjmemory }</td>
	                    <td>${cl.createtime }</td>
	                    <td class="t_r">
	                    	<c:choose>
	                    		<c:when test="${cl.status == 1 }"><i class="red">申请中</i></c:when>
	                    		<c:when test="${cl.status == 2 }"><i class="green">申请成功</i></c:when>
	                    		<c:when test="${cl.status == 0 }"><i style="color: black;">申请失败</i></c:when>
	                    	</c:choose>
	                    </td>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div><!--动态的获取pagination的宽度赋值给Pagination-->
    </div>
</div>

</body>
</html>
