<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>注册4</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/login.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/tab.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
</head>

<body class="body_bg">
<div class="login_head">
	<div class="box">
        <div class="name">餐饮大师</div>
        <div class="link"><a  class="a_back" href="javascript:void(0)" onclick="goBackPage();">返回</a><span>|</span><a href="<%=request.getContextPath() %>/pc/login">登录</a></div>
    </div>
</div>
<div class="reg_page" style="margin-top:-120px;">
	<div class="reg_nav">
        <span class="active"><img src="../userbackstage/images/login/ico_a.png"></span>
        <i class="active">line</i>
        <span class="active"><img src="../userbackstage/images/login/ico_b.png"></span>
        <i class="active">line</i>
        <span class="active"><img src="../userbackstage/images/login/ico_c.png"></span>
        <i class="active">line</i>
        <span class="active"><img src="../userbackstage/images/login/ico_d.png"></span>
        <div class="clear"></div>
    </div>
    <div class="reg_box">
        <div class="succes">恭喜您成功注册餐饮大师！</div>
   		<b><input type="button" value="立即登录" class="btn_login" onclick="location.href='<%=request.getContextPath()%>/pc/login';"></b>
    </div>
</div>
</body>
</html>