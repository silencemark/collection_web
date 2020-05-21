<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>讨论组</title>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" >
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/style/public.css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/style/page_public.css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/style/msg.css">
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/tab.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/page.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/appendjs.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>

<script src="http://cdn.ronghub.com/RongIMLib-2.2.3.min.js"></script> 
<script type="text/javascript">

function gettalkinfo(){
	RongIMClient.getInstance().getHistoryMessages(3, '${map.groupId}', null, 10, {
	    onSuccess: function(list, hasMsg) {
	    	console.log(list);
	    },
	    onError: function(error) {
	    	console.log("error");
	    }
	  });
}
</script>
</head>

<body onload="gettalkinfo()"  class="talk_bg">
<!-- <a onclick="sendmessage()">发送消息</a> -->
<div class="head_box">
	<a class="ico_back">返回</a>
    <div class="name">浦东店群聊组</div>
    <div class="ico_info">群组信息</div>
</div>
<div class="head_none"></div>
<div class="talk_page">
	<div class="load_history"><a>加载历史记录</a></div>
    <ul>
    	<li>
        	<div class="user_l"><img src="<%=request.getContextPath() %>/app/appcssjs/images/public/user_img.png"></div>
            <div class="name_l">大堂经理张三</div>
            <div class="msg_l"><em></em>最近的业绩非常好希望大家再接再厉，争取本月有新突破</div>
        </li>
        <li>
        	<div class="user_r"><img src="<%=request.getContextPath() %>/app/appcssjs/images/public/user_img.png"></div>
            <div class="name_r">大堂经理张三</div>
            <div class="msg_r"><em></em>最近的业绩非常好希望大家再接再厉，争取本月有新突破</div>
        </li>
        <li>
        	<div class="user_l"><img src="<%=request.getContextPath() %>/app/appcssjs/images/public/user_img.png"></div>
            <div class="name_l">前台张丽丽</div>
            <div class="msg_l"><em></em>最近的业绩非常好希望大家再接再厉，争取本月有新突破</div>
        </li>
        <li>
        	<div class="user_l"><img src="<%=request.getContextPath() %>/app/appcssjs/images/public/user_img.png"></div>
            <div class="name_l">前台张丽丽</div>
            <div class="msg_l"><em></em>加油，加油~~~~~</div>
        </li>        
        <li>
        	<div class="user_r"><img src="<%=request.getContextPath() %>/app/appcssjs/images/public/user_img.png"></div>
            <div class="name_r">大堂经理张三</div>
            <div class="msg_r"><em></em>最近的业绩非常好希望大家再接再厉，争取本月有新突破</div>
        </li>
    </ul>
</div>
<div class="talk_none"></div>
<div class="talk_tool">
	<div class="a_box">
        <a class="yy"><img src="<%=request.getContextPath() %>/app/appcssjs/images/msg/yy.png"></a>
        <input type="text" class="text">
        <a class="bq"><img src="<%=request.getContextPath() %>/app/appcssjs/images/msg/bq.png"></a>
        <a class="add"><img src="<%=request.getContextPath() %>/app/appcssjs/images/msg/add.png"></a>
    </div>
</div>
</body>
</html>

