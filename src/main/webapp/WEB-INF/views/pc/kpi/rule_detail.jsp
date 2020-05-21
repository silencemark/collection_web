<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统公告</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript">

function onloadDate(){
	$.ajax({
 		type:'post',
 		url:'/pc/getStarRuleInfo?ruleid=${map.ruleid}',
 		success:function(data){
 			if(data.status==1){
 				swal({
 					title : "",
 					text : "查询失败",
 					type : "error",
 					showCancelButton : false,
 					confirmButtonColor : "#ff7922",
 					confirmButtonText : "确认",
 					cancelButtonText : "取消",
 					closeOnConfirm : true
 				}, function(){
 					
 				});
 			}else{
 				$("#tableadd").html("");
 				if(data.ruleInfo!=null){
 					$("#title").text(data.ruleInfo.title);
 					$("#createtime").text(data.ruleInfo.createtime);
 					$("#content").html(data.ruleInfo.content);
 				}
 			}
 		}
 })
}

$(document).ready(function(){
	$('.kpi_li').find("li").attr('class','');
	$('#rule').attr('class','active');
});
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>规则详情</span><a class="back"  href="<%=request.getContextPath() %>/pc/ruleList?organizeid=${map.organizeid}"> 返回</a></div>
        <div class="page_tab2">     
        	<div class="sky_detail">
            	<div class="name"><span id="title"></span><i id="createtime"></i><div class="clear"></div></div>
                <div class="p_box" id="content">
              		
                </div>
            </div>                 
            
        </div>
    </div>  
    <div class="clear"></div>
</div>
</body>
</html>
