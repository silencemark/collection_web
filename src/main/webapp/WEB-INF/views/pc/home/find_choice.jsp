<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登录-选择公司</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/login.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/tab.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
</head>
<script type="text/javascript">
function onloadhtml(){
	var companylist;
	JianKangCache.getGlobalData('findcompanylist',function(data){
		companylist=data;
	})
	var temp="";
	for(var i=0;i<companylist.length;i++){
		temp+="<li><a  onclick=\"choosecompany('"+companylist[i].userid+"');\" style=\"cursor: pointer;\">"+companylist[i].companyname+"</a></li>";
	}
	$('#companyul').html(temp);
}
function choosecompany(userid){
	$('input[name=userid]').val(userid);
	$('#userform').submit();
}
</script>
<body class="body_bg" onload="onloadhtml()">
<form action="<%=request.getContextPath() %>/pc/resetpass" method="post" id="userform">
	<input type="hidden" name="userid" />
</form>
<div class="login_head">
	<div class="box">
        <div class="name">餐饮大师</div>
        <div class="link"><a href="javascript:void(0)" class="a_back" onclick="goBackPage();">返回</a></div>
    </div>
</div>
<div class="choice_cp">
	<div class="name">选择公司</div>
	<ul id="companyul" style="overflow: hidden;overflow-y: visible;height: 380px;">
    </ul>
</div>
</body>
</html>
