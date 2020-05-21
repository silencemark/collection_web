<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登录</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/login.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/tab.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>

	<script type="text/javascript" src="<%=request.getContextPath()%>/dwr/engine.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dwr/util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dwr/interface/ScanLogin.js"></script>
	
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/qrcode.js"></script>

</head>

<body class="body_bg">
<div class="login_head">
	<div class="box">
        <div class="name">餐饮大师</div>
        <div class="link"><a href="<%=request.getContextPath()%>/pc/login">登录</a><span>|</span><a href="<%=request.getContextPath()%>/pc/reg1">注册</a></div>
    </div>
</div>
<div class="login_box" style="margin-top:-142px;">
	<div class="span"><span id="tb_11" onclick="HoverLi(1,1,2);">快捷登录</span><span class="active" id="tb_12" onclick="HoverLi(1,2,2)">账号密码登陆</span></div>    
    <div class="undis" id="tbc_11">
    	<div class="ewm_xx">
        	<b id="qrcode" style=" width: 200px; height: 200px; margin: auto; border: 5px solid #000; padding: 10px; "></b>
            <span>手机扫码 安全登录</span>
        </div>
    </div>
    <div class="dis" id="tbc_12">
        <div class="text_box">
            <span><i>账号</i><input type="text" placeholder="请输入手机号" id="username"></span>
            <span><i>密码</i><input type="password" placeholder="请输入长度6-16位密码" id="password"></span>
            <b><input type="button" onclick="login()" value="登录"/></b>
            <div class="link_xx"><i>没有账号？<a href="<%=request.getContextPath() %>/pc/reg1">立即注册</a></i><a href="<%=request.getContextPath() %>/pc/forgetpass" class="link_a">忘记密码?</a></div>
        </div>
    </div>
</div>
<script type="text/javascript">
var UUID = "<%=UUID.randomUUID().toString().replace("-", "")%>"
dwr.engine.setActiveReverseAjax(true);
//项服务端注册
ScanLogin.onLoad(UUID);
//接收服务端信息
function DwrLogin(data){
	if(data.status==0){ //扫描成功
		$("#qrcode").html('<span style="font-size: 14px; display: block;margin-top: 50px;color: #389E26;">扫描成功!<br>请在手机上确认是否授权登录</span>')
	}else if(data.status==1){//扫描成功
		$("#qrcode").html('<span style="font-size: 14px; display: block;margin-top: 50px;color: #389E26;">授权成功!<br>请稍受,正在登录中...</span>')
		var code = data.code;
		$.ajax({
			type:'post',
			dataType:'json',
			url:'<%=request.getContextPath()%>/pc/chooseCompany?userid='+code,
			success:function(data2){
				location.href="<%=request.getContextPath()%>/pc/index";
			}
		})
	}else if(data.status==2){//取消登录
		$("#qrcode").html('<span style="font-size: 14px; display: block;margin-top: 50px;color: red;">授权失败!<br>已经在手机端取消授权</span>')
	}
}
var loginToken = {
		RegId:UUID,
		scantype:"scanlogin"
}
function makeQrcode(){
	// 二维码对象
	var qrcode; 
	//默认设置
	var content = JSON.stringify(loginToken);
	var size = 200;  
	// 获取内容
	content = content.replace(/(^\s*)|(\s*$)/g, ""); 
	// 清除上一次的二维码
	/* if(qrcode){
	    qrcode.clear();
	} */ 
	// 创建二维码
	qrcode = new QRCode(document.getElementById("qrcode"), {
	    width : size,//设置宽高
	    height : size
	}); 
	qrcode.makeCode(content);
	document.getElementById("qrcode").title = "扫描登录"
}
makeQrcode();
function login(){
	var username=$('#username').val();
	var password=$('#password').val();
	if(username==""){
		swal({
			title : "",
			text : "用户名不能为空",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
		return false;
	}
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
			
		});
		return false;
	}
	var param = new Object();
	param.username = username;
	param.password = password;
	requestPost("<%=request.getContextPath()%>/pc/userlogin",param,function(data){
		
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
					$("#password").val("");
				});
			}else{
				if(data.companylist.length==1){
					$.ajax({
						type:'post',
						dataType:'json',
						url:'<%=request.getContextPath()%>/pc/chooseCompany?userid='+data.companylist[0].userid,
						success:function(data2){
							if(data2.status==1){
								swal({
									title : "",
									text : data2.message,
									type : "error",
									showCancelButton : false,
									confirmButtonColor : "#ff7922",
									confirmButtonText : "确认",
									cancelButtonText : "取消",
									closeOnConfirm : true
								}, function(){
									
								});
							}else{
								$.ajax({
									type:'post',
									dataType:'json',
									url:'<%=request.getContextPath()%>/pc/getShopListByUser?userid='+data.companylist[0].userid+'&companyid='+data.companylist[0].companyid,
									success:function(data1){
										if(data1.status==1){
											swal({
												title : "",
												text : data1.message,
												type : "error",
												showCancelButton : false,
												confirmButtonColor : "#ff7922",
												confirmButtonText : "确认",
												cancelButtonText : "取消",
												closeOnConfirm : true
											}, function(){
												
											});
										}else{
											if(data2.userInfo.isfristlogin==1){
												location.href="<%=request.getContextPath()%>/pc/resetPassword";
											}else{
												location.href="<%=request.getContextPath()%>/pc/index";
											}
										}
									}
								})
								
							}
						}
					})
				}else{
					JianKangCache.setGlobalData("companylist",data.companylist);
					location.href="<%=request.getContextPath()%>/pc/loginChangeCompany";
				}
				
			}
		});
	
}
</script>
</body>
</html>
