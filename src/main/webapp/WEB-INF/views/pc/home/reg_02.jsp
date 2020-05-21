<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>注册2</title>
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
function sendSms(i,dom){
  	if(i==0){
  		$(dom).val("获取验证码");
  		$(dom).css("background-color","#ff9b30");
  		$(dom).attr("disabled",false);
  		return;
  	}else{ 
  		$(dom).val(i+"秒后重新发送");
  		$(dom).css("background-color","#ccc");
  		$(dom).attr("disabled",true);
  	}
  	setTimeout(function(){
  		sendSms(i-1,dom);
  	},1000);
  }
function getvalidecode(){
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
				sendSms(60,$('#sendbtn'));
			}
		}
	})
}
function nextstep(){
	var code=$('#code').val();
	if(code==""){
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
			
		});
		return;
	}
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/pc/checkValide?phone='+phone+'&code='+code,
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
<body class="body_bg"  onload="sendSms(60,$('#sendbtn'));">
<form action="<%=request.getContextPath() %>/pc/reg3" method="post" id="phoneform">
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
        <i class="active">line</i>
        <span class="active"><img src="../userbackstage/images/login/ico_b.png"></span>
        <i>line</i>
        <span><img src="../userbackstage/images/login/ico_c.png"></span>
        <i>line</i>
        <span><img src="../userbackstage/images/login/ico_d.png"></span>
        <div class="clear"></div>
    </div>
    <div class="reg_box">
        <div class="yzm_box"><span class="bg04"><input type="text" placeholder="请输入短信验证码" id="code"></span><input type="button" value="60秒后重新发送" class="yzm_btn" id="sendbtn" onclick="getvalidecode()"></div>
    <b><input type="button" value="下一步" class="btn_next" onclick="nextstep()"  style="background: #ff9b30"></b>
    </div>
</div>
</body>
</html>