<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>重置密码</title>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" >
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
function restpass(){
	var pass1=$('#pass1').val();
	var pass2=$('#pass2').val();
	if(pass1==""){
		swal({
			title : "",
			text : "请输入长度6-16位新密码",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$('#pass1').focus();
		});
		return;
	}
	if(pass1.length < 6){
		swal({
			title : "",
			text : "密码需要在6-16位之间",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$('#pass1').focus();
		});
		return;
	}
	if(pass1!=pass2){
		swal({
			title : "",
			text : "两次密码输入不一致，请重新输入",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$('#pass1').focus();
		});
		return;
	}
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/pc/firstLoginUpdatePass',
		data:'userid=${userInfo.userid}&password='+pass1,
		success:function(data){
			if(data.status==1){
				swal({
					title : "",
					text : data.message,
					type : "error",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					
				});
			}else{
				location.href="<%=request.getContextPath()%>/pc/index";
			}
		}
	})
} 
</script>
<body class="body_bg">
<div class="login_head">
	<div class="box">
        <div class="name">餐饮大师</div>
        <div class="link"><a  class="a_back" href="javascript:void(0)" onclick="goBackPage();">返回</a><span>|</span><a href="<%=request.getContextPath() %>/pc/login">登录</a></div>
    </div>
</div>
<div class="login_box login_password">
	<div class="name">重置密码</div>
    <div class="text_box">
        <span><i>新 密 码</i><input type="password" placeholder="请输入长度6-16位新密码" id="pass1"></span>
        <span><i>确定密码</i><input type="password" placeholder="请确认新密码" id="pass2"></span>
        <b><input type="button" value="重置密码" onclick="restpass();"></b>
    </div>
</div>
</body>
</html>
