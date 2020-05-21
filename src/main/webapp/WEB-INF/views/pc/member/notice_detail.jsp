<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统公告</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$('.link ul').find("li").attr('class','');
	$('#usercenter').attr('class','active');
});
</script>
</head>

<body>
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<div class="left_menu">
    <jsp:include page="../user.jsp"></jsp:include><!-- 头像and编辑 -->
        <div class="menu_name">个人中心</div>
        <ul class="member_li">
        	<li><a href="<%=request.getContextPath() %>/pc/memcenter_info" class="bg_01">个人信息</a></li>
            <li class="active"><a href="<%=request.getContextPath() %>/pc/notice_list" class="bg_02">系统公告</a></li>
            <li><a href="<%=request.getContextPath() %>/pc/password" class="bg_03">重置密码</a></li>
        </ul>
    </div>
    <div class="right_page">
    	<div class="page_name"><span>系统公告详情</span><a class="back"  href="<%=request.getContextPath() %>/pc/notice_list">返回</a></div>
        <div class="page_tab2">     
        	<div class="sky_detail">
            	<div class="name"><span>${dataMap.title}</span><i>${dataMap.createtime}</i><div class="clear"></div></div>
                <div class="p_box">
              		${dataMap.content}	
                </div>
            </div>                 
            
        </div>
    </div>  
    <div class="clear"></div>
</div>
</body>
</html>
