<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>首页-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  

</head>
<script type="text/javascript">
function onloaddata(){
	if('${errors}' != ''){
		swal({
			title : "",
			text : "${errors}",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
	}
}
</script>
<body onload="onloaddata()">
<form action="<%=request.getContextPath() %>/managebackstage/login" method="post">
<div class="login_box">
	<div class="name">CateringMaster</div>
    <div class="text"><input type="text" placeholder="用户名" name="username" value="${map.username}"/></div>
    <div class="text"><input type="password" placeholder="密码" name="password" /></div>
    <div class="text">
    	<input type="text" placeholder="验证码" class="wid_01"  name="code"/>
    	<div class="ymz_img"><img id="vcode2" src="<%=request.getContextPath() %>/vcode/getSystemVcode?" +new
							Date().getTime() onclick='this.src="<%=request.getContextPath() %>/vcode/getSystemVcode?"+new Date().getTime()'
							width="130" height="34p" /></div>
    	<div class="clear"></div>
    </div>
    <div class="btn"><input type="submit" value="登 录" /></div>    
    <div class="link"><a href="<%=request.getContextPath() %>/managebackstage/initforget">忘记密码？</a></div>
</div>
</form>
</body>
</html>