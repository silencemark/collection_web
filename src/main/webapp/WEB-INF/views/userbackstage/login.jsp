<%@page import="com.collection.util.UserUtil"%>
<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>首页-使用方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
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
<form action="<%=request.getContextPath() %>/userbackstage/login" method="post">
<div class="login_box">
		<%
           //统一设置当前用户信息
           Map<String,Object> userInfo=UserUtil.getPCUser(request); 
           request.setAttribute("userInfo", userInfo);
         %>
	<div class="name" style="background:url(<%=request.getContextPath()+userInfo.get("logourl")%>) no-repeat 42px 50%; background-size:32px 27px;"><%=userInfo.get("companyname")%></div>
	<input type="hidden" name="userid" value="${map.userid}"/>
	<input type="hidden" name="username" value="${map.username}"/>
	<input type="hidden" name="realname" value="${map.realname}"/>
	<input type="hidden" name="phone" value="${map.phone}"/>
    <div class="xx_01"><span>用户名：</span><i>${map.realname}</i></div>
    <div class="xx_01"><span>手机：</span><i>${map.phone}</i></div>
<!--<input type="hidden" name="username" value="15000042335"/>
	<input type="hidden" name="realname" value="李语然"/>
	<input type="hidden" name="phone" value="15000042335"/>
    <div class="xx_01"><span>用户名：</span><i>李语然</i></div>
    <div class="xx_01"><span>手机：</span><i>15000042335</i></div> -->
    <div class="text"><input type="password" placeholder="密码" name="password" value="123456"/></div>
    <div class="btn"><input type="submit" value="登 录" /></div>
</div>
</form>
</body>
</html>