<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统公告</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
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

function selectTitle(){
	var title = $("#title").val();
	location.href="/managebackstage/getSystemMessage?title="+title;
	
}

	
$(document).ready(function(){
	$('#system').parent().parent().find("span").attr("class","bg_hidden");
	$('#system').attr('class','active li_active');
})

</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">个人中心</a><i>/</i><span>系统公告</span></p></div>  
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1">系统公告列表</span><a href="/managebackstage/noticeadd">新建</a></div>
        <div class="sel_box">
            <input type="text" class="text" placeholder="请输入关键字"  id="title"/>
            <input type="button" value="搜索" class="find_btn"  onclick="selectTitle()"/>
            <div class="clear"></div>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0"  id="messageTable" style="font-size:12px;">
                <tr class="head_td">
                	<td width="25%">标题</td>
                    <td>简介</td>
                    <td width="16%">发布时间</td>
                    <td width="40">操作</td>
                </tr>
                <c:forEach var="list" items="${dataList}">
	               	<tr>
	                	<td>${list.title}</td>
	                    <td>${list.content}</td>
	                    <td>${list.createtime}</td>
	                    <td><a href="<%=request.getContextPath()%>/managebackstage/getSystemMessageDetail?messageid=${list.messageid}" class="blue">查看</a></td>
	                </tr>      
                </c:forEach>
               
            </table>
        </div>
 	 <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination-->
    </div>
</div>
<div class="div_mask" style="display:none;"></div>


</body>
</html>
