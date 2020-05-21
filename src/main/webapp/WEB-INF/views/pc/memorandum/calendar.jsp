<%@page import="com.collection.util.UserUtil"%>
<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加备忘录</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
</head>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/style/calendar.css">

<style type="text/css">
a {
	text-decoration: none;
}
ul, ol, li {
	list-style: none;
	padding: 0;
	margin: 0;
}
#demo {
	width: 100%;
	margin: auto;
	background: white;
	margin-top:40px;
}
p {
	margin: 0;
}
#dt {
	margin: 30px auto;
	height: 28px;
	width: 500px;
	padding: 0 6px;
	border: 1px solid #ccc;
	outline: none;
}
</style>
<script type="text/javascript">
$(document).ready(function(){
	$('#memorandum').parent().find("li").attr('class','');
	$('#memorandum').attr('class','active');
})
var date1='${map.datetime}';
var timestr="";
var yearmonth="";
function onloadData(datetime){
	if(datetime != null && datetime != ''){
		timestr=datetime;
	}else{
		datetime="";
	}
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/pc/getMemorandumList?companyid=${userInfo.companyid}&createid=${userInfo.userid}&datetime='+datetime,
		success:function(data){
			if(data.memorandumlist.length > 0){
				var temp="";
				for(var i=0;i<data.memorandumlist.length;i++){
					temp+="<li><div class=\"point\"></div><i>"+data.memorandumlist[i].hourtime+"</i><span class=\"word_hidden\">"+data.memorandumlist[i].content+"</span></li>";
				}
				$('#memoul').html(temp);
			}else{
				$('#memoul').html("");
			}
			yearmonth=data.yearmonth;
		}
	})
	//加载所有的红点
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/app/getAllMemorandumTime?companyid=${userInfo.companyid}&createid=${userInfo.userid}',
		success:function(data){
			$('#ca').calendar({
				width: $('#demo').width(),
		        height: 350,
		        data: data.timelist,
		        onSelected: function (view, date, data) {
		            onloadData(date.format('yyyy-mm-dd'));
		        }
		    });

		    $('#dd').calendar({
		        trigger: '#dt',
		        zIndex: 999,
				format: 'yyyy-mm-dd',
		        onSelected: function (view, date, data) {
		            console.log('event: onSelected')
		        },
		        onClose: function (view, date, data) {
		            console.log('event: onClose')
		            console.log('view:' + view)
		            console.log('date:' + date)
		            console.log('data:' + (data || 'None'));
		        }
		    });
		}
	})
}
</script>
<body onload="onloadData('${map.datetime}')">
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
        <div class="right_page" style="background: red!important">
    	<div class="page_name"><span>日历</span><a href="javascript:void(0)"  onclick="goBackPage();" class="back">返回</a></div>
        <div class="page_tab2">
        	<div id="demo">
			  <div id="ca"></div>
			</div>
            <div class="memo_list">
                <div class="name">
                    <span>我的安排</span>
                    <a href="javascript:void(0)" onclick="location.href='<%=request.getContextPath() %>/pc/initInsertMemorandum?datetime='+timestr;">添加</a>
                </div>
                <div class="li_list">
                    <div class="ul_head"></div>
                    <ul id="memoul">
                    </ul>        
                    <div class="ul_foot"></div>
                </div>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>
<script src="<%=request.getContextPath() %>/app/appcssjs/script/jquery.js"></script> 
<script src="<%=request.getContextPath() %>/app/appcssjs/script/calendar.js"></script> 

</body>
</html>