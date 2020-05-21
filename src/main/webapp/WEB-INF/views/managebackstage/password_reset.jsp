<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>重置密码-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  

</head>
<script type="text/javascript">
function updatepass(){
	var password1=$('#password1').val();
	var password2=$('#password2').val();
	if(password1==""){
		swal({
			title : "",
			text : "新密码不能为空",
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
	if(password1!=password2){
		swal({
			title : "",
			text : "两次密码输入不一致",
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
	$('#updateform').submit();
}
</script>
<body>
<div class="login_box l_height">
	<div class="name">CateringMaster</div>
	<form action="<%=request.getContextPath() %>/managebackstage/updateUserInfo" method="post" id="updateform">
	<input type="hidden" name="userid" value="${userInfo.userid}"/>
    <div class="text"><input type="password" placeholder="新密码" name="password" id="password1"/></div>
    <div class="text"><input type="password" placeholder="确认密码" id="password2"/></div>
    <div class="btn"><input type="button" value="重置密码" onclick="updatepass()"/></div>  
    </form>
    <div class="link2"><a href="<%=request.getContextPath() %>/managebackstage/login">返回登陆？</a></div>
</div>
</body>
</html>
