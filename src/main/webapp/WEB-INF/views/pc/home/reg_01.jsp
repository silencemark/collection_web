<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>注册1</title>
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
function getvalidecode(){
	$('#phone').attr("disabled",true);
	var phone=$('#phone').val();
	if(phone==""){
		swal({
			title : "",
			text : "请输入手机号码",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
		return;
	}
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/pc/getVerificationCode?phone='+phone,
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
				$('input[name=phone]').val(phone);
				$('#phoneform').submit();
			}
		}
	})
}
</script>
<body class="body_bg">
<form action="<%=request.getContextPath() %>/pc/reg2" method="post" id="phoneform">
	<input type="hidden" name="phone" />
</form>
<div class="login_head">
	<div class="box">
        <div class="name">餐饮大师</div>
        <div class="link"><a  class="a_back" href="javascript:void(0)" onclick="goBackPage();">返回</a><span>|</span><a href="<%=request.getContextPath() %>/pc/login">登录</a></div>
    </div>
</div>
<div class="reg_page" style="margin-top:-120px;">
	<div class="reg_nav">
        <span class="active"><img src="../userbackstage/images/login/ico_a.png"></span>
        <i>line</i>
        <span><img src="../userbackstage/images/login/ico_b.png"></span>
        <i>line</i>
        <span><img src="../userbackstage/images/login/ico_c.png"></span>
        <i>line</i>
        <span><img src="../userbackstage/images/login/ico_d.png"></span>
        <div class="clear"></div>
    </div>
    <div class="reg_box">
        <span class="bg03"><input type="text" placeholder="请输入手机号"  id="phone"></span>
        <b><input type="button" value="下一步" class="btn_next" onclick="getvalidecode()" style="background: #ff9b30"></b>
        <em>点击下一步意味着您同意餐饮大师的<a href="<%=request.getContextPath() %>/pc/agreement">《用户协议》</a></em>
    </div>
</div>
</body>
</html>