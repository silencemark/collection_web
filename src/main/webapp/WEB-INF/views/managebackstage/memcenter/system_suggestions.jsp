<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>意见反馈</title>
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
				$(".head_box .user_box .name").removeClass("name_border");//移除
				$(".head_box .user_box .box").hide();
			  }
			);			
});
	
function checkSelect(){
	var status="";
	var realname="";
	status = $(".sel").val();
	realname= $("#realname").val();
	if(status != ""){
		location.href="/managebackstage/getSystemBacklist?status="+status+"&realname="+realname;
	}else{
		location.href="/managebackstage/getSystemBacklist?realname="+realname;
	}
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

	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">系统设置</a><i>/</i><span>意见反馈</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">意见反馈</span></div>
        <div class="sel_box">
            <input type="text" class="text" placeholder="请输入用户关键字" id="realname" />
            <select class="sel"><option value="">全部</option><option value="0">未处理</option><option value="1">已处理</option><option value="2">已忽略</option></select>
            <input type="button" value="搜索" class="find_btn"  onclick="checkSelect()" />
            <div class="clear"></div>
        </div>
      	<div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="border-top:1px solid #eee;font-size:12px;" >
                   <c:forEach var="list" items="${dataList}">
	               	<tr>
	                	<td width="20%"><i class="gray1">${list.realname}</i><br />${list.companyname}</td>
                   		<td width="65%">${list.description}</td>
	                    <td>
	                    	<c:choose>
	                    		<c:when test="${list.status == 0}"><i class="red m_r30">未处理</i></c:when>
	                    		<c:when test="${list.status == 1}"><i>已处理</i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</c:when>
	                    		<c:when test="${list.status == 2}"><i>已忽略</i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</c:when>
	                    	</c:choose>
		                    <a href="<%=request.getContextPath()%>/managebackstage/getSystemBackDetail?feedbackid=${list.feedbackid}" class="blue">查看</a>
		                    <br />${list.createtime}
	                    </td>
	                </tr>      
                </c:forEach>
            </table>
        </div>
       	 <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination-->
    </div>
</div>
</body>
</html>
