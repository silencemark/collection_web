<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>企业网盘</title>
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

var nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>列表空空，还没有网盘栏目呢</span><b><img src="/userbackstage/images/index/none_msg.gif" width="293" height="240"></b></div>';
function onloadDate(){
	$.ajax({
 		type:'post',
 		url:'/pc/getCompanyCloudModuleList',
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
 				if(data.modulelist!=null){
 					var temp=" <tr class=\"head_td\">"+
				                   "<td colspan=\"2\">文件名</td>"+
				                   "<td>更新时间</td>"+
				                   "<td>操作</td>"+
				              " </tr>";
				 if(data.modulelist.length > 0){
					 $(".list_none").remove();
 					for(var i= 0;i<data.modulelist.length;i++){
 						temp+=" <tr>"+
		 	                    	"<td class=\"img_td\" width=\"48\"><div class=\"img5\">"+
		 	                    	"<img src=\""+data.modulelist[i].moduleimage+"\" width=\"48\" height=\"48\" /></div>"+
		 	                    	"</td>"+
		 	                        "<td width=\"45%\">"+data.modulelist[i].modulename+"</td>"+
		 	                        "<td width=\"30%\">"+data.modulelist[i].updatetime+"</td>"+
		 	                        "<td><a href=\"<%=request.getContextPath() %>/pc/getPcOaSkydriveList?moduleid="+data.modulelist[i].moduleid+"\" class=\"blue\">查看</a></td>"+
		 	                    "</tr>";
 					}
				 }else{
					 $(".list_none").remove();
					 $("#tableadd").parent().append(nodata);
				 }
 					$("#tableadd").html(temp);
 					
 				}
 			}
 		}
 })
}

$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#skydriveActive').attr('class','active');
});
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>企业网盘</span></div>
        <div class="page_tab2">
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;" id="tableadd">
                   
                </table>
            </div>
            
        </div>
    </div>
    <div class="clear"></div>
</div>
</body>
</html>
