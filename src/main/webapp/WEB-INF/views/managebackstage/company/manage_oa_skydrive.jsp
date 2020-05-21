<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理方后台-企业云盘</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

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
		
		$('#company').parent().parent().find("span").attr("class","bg_hidden");
		$('#company').attr('class','active li_active');
});
	
</script>
</head>

<body>
<jsp:include page="../top.jsp"></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp"></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList">企业信息列表</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyInfo?companyid=${map.companyid}">公司信息</a><i>/</i><span>企业云盘</span></p></div>  
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1">企业云盘(${sy_memory }G/${maxmemory }G)</span></div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">
        		<c:forEach items="${modulelist }" var="ml">
        			<tr>
	                	<td class="img_td" width="30"><div class="img2"><img src="${ml.moduleimage }" width="30" height="30" /></div></td>
	                    <td width="70%" style="cursor: pointer;" onclick="location.href='<%=request.getContextPath()%>/managebackstage/intoManageCompanyCloudFilePage?moduleid=${ml.moduleid}&companyid=${map.companyid}'">${ml.modulename }</td>
	                    <td class="t_r"><a href="<%=request.getContextPath()%>/managebackstage/intoManageCompanyCloudFilePage?moduleid=${ml.moduleid}&companyid=${map.companyid}" class="link">查看</a></td>
	                </tr>
        		</c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div><!--动态的获取pagination的宽度赋值给Pagination-->
    </div>
</div>
</body>
</html>
