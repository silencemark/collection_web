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


function pageHelper(num){
	var currentPage = num;
	$.ajax({
 		type:'post',
 		dataType:'json',
 		url:'/pc/getSystemMessageListByUser?currentPage='+currentPage,
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
 				if(data.dataList!=null ){
 					var temp="<tr class=\"head_td\">"+
			                    "<td width=\"50%\">标题</td>"+
			                    "<td width=\"40%\">发布时间</td>"+
			                    "<td>操作</td>"+
                				"</tr>";
 					for(var i= 0;i<data.dataList.length;i++){
 						temp+="<tr>"+
		 						"<td>"+data.dataList[i].title+"</td>"+
		 	                     "<td>"+data.dataList[i].createtime+"</td>"+
		 	                     "<td><a href=\"#\" class=\"blue\" onclick=\"checkByid('"+data.dataList[i].messageid+"')\">查看</a></td>"+
 	                   		 "</tr>";
 					}
 					$("#tableadd").append(temp);
 					$('#Pagination').html(data.pager);
 				}
 			}
 		}
 })
}
function onloadDate(){
 	$.ajax({
	 		type:'post',
	 		dataType:'json',
	 		url:'/pc/getSystemMessageListByUser',
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
	 				if(data.dataList!=null){
	 					var temp="";
	 					for(var i= 0;i<data.dataList.length;i++){
	 						temp+="<tr>"+
			 						"<td>"+data.dataList[i].title+"</td>"+
			 	                     "<td>"+data.dataList[i].createtime+"</td>"+
			 	                    "<td><a href=\"#\" class=\"blue\" onclick=\"checkByid('"+data.dataList[i].messageid+"')\">查看</a></td>"+
	 	                   		 "</tr>";
	 					}
	 					$("#tableadd").append(temp);
	 					$('#Pagination').html(data.pager);
	 				}
	 			}
	 		}
	 })
}

function checkByid(messageid){
	location.href="<%=request.getContextPath() %>/pc/getSystemMessageNoticeById?messageid="+messageid;
}
$(document).ready(function(){
	$('.link ul').find("li").attr('class','');
	$('#usercenter').attr('class','active');
});
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main" >
	<div class="left_menu">
    <jsp:include page="../user.jsp"></jsp:include><!-- 头像and编辑 -->
        <div class="menu_name">个人中心</div>
        <ul class="member_li">
        	<li><a href="<%=request.getContextPath() %>/pc/memcenter_info" class="bg_01">个人信息</a></li>
            <li class="active"><a href="<%=request.getContextPath() %>/pc/notice_list" class="bg_02">系统公告</a></li>
            <li><a href="<%=request.getContextPath() %>/pc/password" class="bg_03">重置密码</a></li>
        </ul>
    </div>
    <div class="right_page">
    	<div class="page_name"><span>系统公告</span></div>
        <div class="page_tab2">
            <div class="tab_list" >
                <table width="100%" border="0" cellpadding="0" cellspacing="0" id="tableadd" style="font-size:12px;">
                    <tr class="head_td">
                        <td width="50%">标题</td>
                        <td width="40%">发布时间</td>
                        <td>操作</td>
                    </tr>
                   
                </table>
            </div>
           <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination-->
        </div>
    </div>
    <div class="clear"></div>
</div>


</body>
</html>
