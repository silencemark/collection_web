<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>企业简报详情</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
	<!-- 日期 -->
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/showcomment.js"></script>
<script type="text/javascript">
var param=new Object();
param.userid = '${userInfo.userid}';
param.companyid ='${userInfo.companyid}';
var moduleid='${map.moduleid}';
param.moduleid=moduleid;
param.briefid='${map.briefid}';
function onloadDate(){
	$.ajax({
 		type:'post',
 		url:'/pc/getBriefInfo',
 		data:param,
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
 				if(data.briefInfo!=null){
 					$("#title").text(data.briefInfo.title);
 					$("#createtime").text(data.briefInfo.createtime);
 					$("#content").html(data.briefInfo.content);
 				}
 			}
 		}
 })
}

$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#briefingActive').attr('class','active');
});
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>企业简报详情</span><a href="<%=request.getContextPath() %>/pc/getPcOaBriefingList?moduleid=${map.moduleid}" class="back">返回</a></div>
        <div class="page_tab2">            
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="none_border" style="font-size:12px;">
                    <tr>
                    	<td colspan="2">
                        	<div class="notice_xx">
                            	<div class="n_name">
                                	<span id="title"></span>
                                    <i id="createtime"></i>
                                    <div class="clear"></div>
                                </div>
                                <div class="p_box" id="content">
                                
                                </div>
                            </div>
                        </td>
                    </tr>                    
                </table>
            </div>
        </div>        
    </div>
    <div class="clear"></div>
</div>
</body>
</html>
