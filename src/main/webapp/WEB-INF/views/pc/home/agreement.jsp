<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>使用条款及隐私政策</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/login.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/tab.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
</head>

<body class="body_bg">
<div class="login_head">
	<div class="box">
        <div class="name">餐饮大师</div>
        <div class="link"><a  class="a_back" href="javascript:void(0)" onclick="goBackPage();">返回</a></div>
    </div>
</div>
<div class="agreement">
	<div class="name">使用条款及隐私政策</div>
	<p>我们致力于保护您在使用我们网站时所提供的隐私、私人资料以及个人的资料（统称“个人资料”），使用我们在收集、使用存储和传送个人资料方面符合（与个人资料隐私有关的法律法规）消费者保护方面的最高标准。</p>
    <p>为确保您对我们在处理个人资料上有充分信心，您切要详细阅读及理解隐私政策的文条。特别是您一旦使用我们的网站，将视为接受、同意、承诺和确认：</p>
    <p>您在资源下连同所需的同意向我们披露个人资料；</p>
    <p>您会遵守隐私政策的全部条款和限制；</p>
    <p>您在我们网站上作登记、资料会被收集；</p>
    <p>您同意日后我们对隐私政策的任何修改；</p>
    <p>您同意我们的分公司、附属公司、雇员就您可能会感兴趣的产品和服务与您联络（除非您已经表示不想收到盖等讯息）。</p>
</div>
</body>
</html>