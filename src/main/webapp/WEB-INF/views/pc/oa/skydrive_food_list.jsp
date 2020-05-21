<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>餐饮大师网盘</title>
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
var parentid ='${map.parentid}';
if(parentid!=""){
	param.parentid= parentid;
}
function pageHelper(num){
	var currentPage = num;
	param.currentPage=currentPage;
	$.ajax({
 		type:'post',
 		dataType:'json',
 		url:'/pc/getSystemCloudList',
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
 				$("#tableadd").html("");
 				if(data.datalist!=null){
 					var temp="  <tr class=\"head_td\">"+
				                    "<td width=\"50%\">文件名</td>"+
			                        "<td width=\"35%\">发布时间</td>"+
				                    "<td>操作</td>"+
				                "</tr>";
 					for(var i= 0;i<data.datalist.length;i++){
 						temp+="<tr>"+
	 	                    	"<td>"+data.datalist[i].filename+"</td>"+
	 	                        "<td>"+data.datalist[i].createtime+"</td>";
	 	                        if(data.datalist[i].filetype==2){
	 	                        	temp+="<td><a href=\"<%=request.getContextPath() %>/pc/getPcOaSkydriveFoodList?parentid="+data.datalist[i].fileid+"&type=${map.type}\" class=\"link\">打开</a></td></tr>";
	 	                        }else if(data.datalist[i].filetype==1){
	 	                        	temp+="<td><a href=\"#\" class=\"yellow\" onclick=\"downloadfile('"+data.datalist[i].fileurl+"')\">下载</a></td></tr>";
	 	                        }
		 	               		
 					}
 					$("#tableadd").html(temp);
 					$('#Pagination').html(data.pager);
 					
 				}
 			}
 		}
 })
}


function onloadDate(){
	if('${map.type}' == '1'){
		$("#type").text("文件");
	}else if('${map.type}' == '2'){
		$("#type").text("视频");
	}
	$.ajax({
 		type:'post',
 		url:'/pc/getSystemCloudList',
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
 				$("#tableadd").html("");
 				if(data.datalist!=null){
 					var temp="  <tr class=\"head_td\">"+
				                    "<td width=\"50%\">文件名</td>"+
			                        "<td width=\"35%\">发布时间</td>"+
				                    "<td>操作</td>"+
				                "</tr>";
 					for(var i= 0;i<data.datalist.length;i++){
 						temp+="<tr>"+
	 	                    	"<td>"+data.datalist[i].filename+"</td>"+
	 	                        "<td>"+data.datalist[i].createtime+"</td>";
	 	                        if(data.datalist[i].filetype==2){
	 	                        	temp+="<td><a href=\"<%=request.getContextPath() %>/pc/getPcOaSkydriveFoodList?parentid="+data.datalist[i].fileid+"&type=${map.type}\" class=\"link\">打开</a></td></tr>";
	 	                        }else if(data.datalist[i].filetype==1){
	 	                        	temp+="<td><a href=\"#\" class=\"yellow\" onclick=\"downloadfile('"+data.datalist[i].fileurl+"')\">下载</a></td></tr>";
	 	                        }
		 	               		
 					}
 					$("#tableadd").html(temp);
 					$('#Pagination').html(data.pager);
 					
 				}
 			}
 		}
 })
}
function downloadfile(name){
	window.location.href="<%=request.getContextPath() %>/upload/download?fileName="+name;
}

function checkSearch(){
	param.filename=$("#title").val(); 
	onloadDate();
}


$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#skydrivefoddActive').attr('class','active');
});
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>餐饮大师网盘列表—<i id="type"></i></span><a href="<%=request.getContextPath() %>/pc/getPcOaSkydriveFood" class="back">返回</a></div>
        <div class="page_tab">
            <div class="sel_box">
                <input type="text" class="text" placeholder="请输入关键字"  id="title"/>
                <input type="button" value="" class="find_btn" onclick="checkSearch()" />
                <div class="clear"></div>
            </div>
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" id="tableadd" style="font-size:12px;">
                
                </table>
            </div>
             <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination-->
        </div>
    </div>
    <div class="clear"></div>
</div>
</body>
</html>
