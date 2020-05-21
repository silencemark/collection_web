<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>忘记密码</title>
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
function getVaildeCode(obj){
	
	var phone=$('#phone').val();
	if(phone==""){
		swal({
			title : "",
			text : "请输入手机号",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$('#phone').focus();
		});
		return;
	}
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/pc/getVerificationCode?type=1&phone='+phone,
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
				swal({
					title : "",
					text : "获取验证码成功",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					$('#validecode').focus();
				});
				sendSms(60,$(obj)); 
			}
		}
	})
}
function sendSms(i,dom){
  	if(i==0){
  		$(dom).val("获取验证码");
  		$(dom).css("background-color","#ff9b30");
  		$(dom).attr("disabled",false);
  		return;
  	}else{ 
  		$(dom).val(i+"s后再试");
  		$(dom).css("background-color","#ffc383");
  		$(dom).attr("disabled",true);
  	}
  	setTimeout(function(){
  		sendSms(i-1,dom);
  	},1000);
  }
function nextstep(){
	var phone=$('#phone').val();
	var validecode=$('#validecode').val();
	if(phone==""){
		swal({
			title : "",
			text : "请输入手机号",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$('#phone').focus();
		});
		return;
	}
	if(validecode==""){
		swal({
			title : "",
			text : "请输入验证码",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$('#validecode').focus();
		});
		return;
	}
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/pc/getCompanyByPhone?code='+validecode+'&phone='+phone,
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
				if(data.companylist.length==1){
					$('input[name=userid]').val(data.companylist[0].userid);
					$('#userform').submit();
				}else{
					JianKangCache.setGlobalData("findcompanylist",data.companylist);
					location.href="<%=request.getContextPath()%>/pc/findcompanylist";
				}
			}
		}
	})
}
</script>
<body class="body_bg">
<form action="<%=request.getContextPath() %>/pc/resetpass" method="post" id="userform">
	<input type="hidden" name="userid" />
</form>
<div class="login_head">
	<div class="box">
        <div class="name">餐饮大师</div>
        <div class="link"><a  class="a_back" href="javascript:void(0)" onclick="goBackPage();">返回</a><span>|</span><a href="<%=request.getContextPath() %>/pc/login">登录</a></div>
    </div>
</div>
<div class="login_box login_password">
	<div class="name">忘记密码</div>
    <div class="text_box">
        <span><i>手&nbsp;&nbsp;&nbsp;机</i><input type="text" placeholder="请输入手机号" id="phone"></span>
        <span><i>验证码</i><input type="text" placeholder="请输入验证码"  id="validecode"><input type="button" value="获取验证码" class="yzm_btn" onclick="getVaildeCode(this);" style="background: #ff9b30;"></span>
        <b><input type="button" value="下一步" onclick="nextstep()"></b>
    </div>
</div>
</body>
</html>