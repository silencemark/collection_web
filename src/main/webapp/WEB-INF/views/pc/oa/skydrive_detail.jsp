<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文件详情</title>
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
var moduleid='${map.moduleid}';
param.moduleid=moduleid;
param.fileid='${map.fileid}';
function onloadDate(){
	$.ajax({
 		type:'post',
 		url:'/pc/getCompanyCloudFileInfo',
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
 				if(data.datamap!=null){
 					$("#title").text(data.datamap.title);
 					$("#realname").text(data.datamap.realname);
 					$("#createtime").text(data.datamap.createtime);
 					$("#filename").text(data.datamap.filename);
 					$("#memory").text(data.datamap.memory);
 					$("#content").html(data.datamap.content);
 					$('#down_div').attr("onclick","downloadfile('"+data.datamap.fileurl+"')");
 				}
 			}
 		}
 })
}
function downloadfile(name){
	window.location.href="<%=request.getContextPath() %>/upload/download?fileName="+name;
}
$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#taskActive').attr('class','active');
});
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>文件详情</span><a href="<%=request.getContextPath() %>/pc/getPcOaSkydriveList?moduleid=${map.moduleid}" class="back">返回</a></div>
        <div class="page_tab2">
            <div class="sky_detail">
            	<div class="name" id="title"></div>
                <div class="span"><span >上传人:<span id="realname"></span></span><span>上传时间：<span id="createtime"></span></span></div>
<!--                 <div class="fiel_box"> -->
<!--                     <div class="fiel_name" id="filename" ></div> -->
<!--                     <div class="fiel_num" id="memory" ></div> -->
<!--                     <div class="fiel_link" id="down_div" onclick="" style="cursor: pointer;">下载</div> -->
<!--                     <div class="clear"></div>        -->
<!--                 </div> -->
                <div class="p_box" id="content" >
                
                </div>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>
</body>
</html>
