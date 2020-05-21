<%@page import="com.collection.util.UserUtil"%>
<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>主页</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script src="http://api.map.baidu.com/api?v=2.0&ak=wBYHoaC0rzxp8zqGCdx9WXxa" type="text/javascript"></script>
<script type="text/javascript" src="http://developer.baidu.com/map/jsdemo/demo/convertor.js"></script>
</head>

<script type="text/javascript">

var latitude; // 纬度，浮点数，范围为90 ~ -90
var longitude; // 经度，浮点数，范围为180 ~ -180。
function getbaidu(){
	var geolocation = new BMap.Geolocation();
	geolocation.getCurrentPosition(function(r){
		if(this.getStatus() == BMAP_STATUS_SUCCESS){
			longitude = r.point.lng;
			latitude = r.point.lat; 
		}else{
			longitude = 26.629907;
			latitude = 106.709177;
		}
		$.ajax({
			type:'post',
			dataType:'json',
			url:'<%=request.getContextPath()%>/app/getcity?lat='+latitude+'&lng='+longitude+'&userid=${userInfo.userid}',
			success:function(data){
				$('#weatherurl').attr("src","../userbackstage/images/pc_page/weather_"+data.weathermap.weatherurl+".png");
				$('#weathername').html(data.weathermap.cityname+"<br>"+data.weathermap.weathername);
				$('#temperature').text(data.weathermap.temperature);
				$('#weekdate').html(data.weathermap.week+"&nbsp;&nbsp;&nbsp;&nbsp;"+data.weathermap.datetime);
			}
		})
	},{enableHighAccuracy: true});
}
$(document).ready(function(){
	$('#memorandum').parent().find("li").attr('class','');
	$('#memorandum').attr('class','active');
})
function onloadData(){
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/app/getWeather?userid=${userInfo.userid}',
		success:function(data){
			if(data.status==1){
				getbaidu();
			}else{
				$('#weatherurl').attr("src","../userbackstage/images/pc_page/weather_"+data.weathermap.weatherurl+".png");
				$('#weathername').html(data.weathermap.cityname+"<br>"+data.weathermap.weathername);
				$('#temperature').text(data.weathermap.temperature);
				$('#weekdate').html(data.weathermap.week+"&nbsp;&nbsp;&nbsp;&nbsp;"+data.weathermap.datetime);
			}
		}
	})
}
</script>
<body onload="onloadData()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
    <%Map<String,Object> userInfo=UserUtil.getPCUser(request); %>
	<div class="left_menu">
    	<div class="user_xx">
        	<div class="img"><img src="<%=userInfo.get("headimage") %>" width="128" height="128" /></div>
            <div class="name"><span><%=userInfo.get("realname") %></span><a href="#">编辑</a></div>
        </div>
        <div class="menu_name">备忘录</div>
        <ul class="memo_li">
        	<li class="active"><a href="<%=request.getContextPath()%>/pc/getMemorandumListToday" class="bg_01">备忘录</a></li>
        </ul>
    </div>
    <div class="right_page">
    	<div class="page_name"><span>备忘录</span><a href="<%=request.getContextPath()%>/pc/calendar" class="ico_calendar"><img src="../userbackstage/images/pc_page/ico_cal.png" width="24" height="24" alt="日历" /></a></div>
        <div class="page_tab2">
        	<div class="memo_top">
                <div class="box">
                    <div class="xx_l">
                        <span id="temperature"></span>
                        <i id="weathername"></i>
                        <div class="clear"></div>
                        <em id="weekdate"></em>
                    </div>
                    <div class="xx_r"><img src="" id="weatherurl"></div>
                    <div class="clear"></div>
                </div>
            </div>
            <div class="memo_list">
                <div class="name">
                    <span>今日安排</span>
                    <a href="<%=request.getContextPath() %>/pc/initInsertMemorandum">添加</a>
                </div>
                <div class="li_list">
                    <div class="ul_head"></div>
                    <ul>
                    	<c:forEach items="${memorandumlist}" var="item">
                    		<li><div class="point"></div><i>${item.hourtime }</i><span class="word_hidden">${item.content}</span></li>
                    	</c:forEach>
                    </ul>        
                    <div class="ul_foot"></div>
                </div>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>
</body>
</html>