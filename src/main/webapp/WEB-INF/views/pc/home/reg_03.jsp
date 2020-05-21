<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>注册3</title>
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
var phone='${map.phone}';
if(phone==''){
	'<%=request.getContextPath() %>/pc/reg1';
}
$('#phone').text(phone);
function regUser(){
	var password=$('#password').val();
	var password1=$('#password1').val();
	var companyname=$('#companyname').val();
	var realname=$('#realname').val();
	if(password==""){
		swal({
			title : "",
			text : "请输入长度6-16位密码",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$('#password').focus();
		});
		return;
	}
	if(password != password1){
		swal({
			title : "",
			text : "两次密码不一致，请重新输入",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$('#password').focus();
		});
		return;
	}
	if(companyname==""){
		swal({
			title : "",
			text : "请输入餐厅/公司名称",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$('#companyname').focus();
		});
		return;
	}
	if(realname==""){
		swal({
			title : "",
			text : "请输入管理员姓名",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$('#realname').focus();
		});
		return;
	}
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/pc/register?phone='+phone+'&realname='+realname+'&companyname='+companyname+'&password='+password,
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
				location.href='<%=request.getContextPath() %>/pc/reg4';
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
<div class="reg_page" style="margin-top:-205px;">
	<div class="reg_nav">
        <span class="active"><img src="../userbackstage/images/login/ico_a.png"></span>
        <i class="active">line</i>
        <span class="active"><img src="../userbackstage/images/login/ico_b.png"></span>
        <i class="active">line</i>
        <span class="active"><img src="../userbackstage/images/login/ico_c.png"></span>
        <i>line</i>
        <span><img src="../userbackstage/images/login/ico_d.png"></span>
        <div class="clear"></div>
    </div>
    <div class="reg_box">
        <span class="bg03" id="phone"></span>
        <span class="bg05"><div class="must">*</div><input type="password" placeholder="请输入长度6-16位密码" id="password" maxlength="16"></span>
        <span class="bg06"><div class="must">*</div><input type="password" placeholder="请确认密码" id="password1" maxlength="16"></span>
        <span class="bg01"><div class="must">*</div><input type="text" placeholder="请输入餐厅/公司名称"  id="companyname"></span>
        <span class="bg02"><input type="text" placeholder="请输入管理员姓名" id="realname"></span>
        <b><input type="button" value="同意《用户协议》并注册" class="btn_next" onclick="regUser()"  style="background: #ff9b30"></b>
    </div>
</div>
</body>
</html>