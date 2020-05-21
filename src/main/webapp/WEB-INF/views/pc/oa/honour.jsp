<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>荣誉榜</title>
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
var userid;
userid='${userInfo.userid}';
var param=new Object();
param.userid = '${userInfo.userid}';
param.companyid ='${userInfo.companyid}';

function pageHelper(num){
	var currentPage = num;
	param.currentPage=currentPage;
	$.ajax({
 		type:'post',
 		dataType:'json',
 		url:'/pc/getUserRewardList',
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
 				if(data.userlist!=null){
 					var temp=" <tr class=\"head_td\">"+
				                	"<td width=\"40%\">姓名</td>"+
				                   "<td width=\"40%\">奖励结果</td>"+
				                    "<td>获奖时间</td>"+
				                "</tr>";
 					for(var i= 0;i<data.userlist.length;i++){
 						temp+="<tr>"+
	 	                    	"<td>"+data.userlist[i].realname+"</td>"+
	 	                        "<td>"+data.userlist[i].rewardresult+"</td>"+
	 	                        "<td>"+data.userlist[i].createtime+"</td>";
	 	            }
 					$("#tableadd").html(temp);
 					$('#Pagination').html(data.pager);
 					
 				}
 			}
 		}
 })
}

var nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>荣誉榜还没有人哦~</span><b><img src="/userbackstage/images/index/none_msg.gif" width="293" height="240"></b></div>';
function onloadDate(){
	$.ajax({
 		type:'post',
 		url:'/pc/getUserRewardList',
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
 				if(data.userlist!=null){
 					var temp=" <tr class=\"head_td\">"+
				                	"<td width=\"40%\">姓名</td>"+
				                   "<td width=\"40%\">奖励结果</td>"+
				                    "<td>获奖时间</td>"+
				                "</tr>";
				    if(data.userlist.length > 0){
				    	$(".list_none").remove();
 					for(var i= 0;i<data.userlist.length;i++){
 						temp+="<tr>"+
	 	                    	"<td>"+data.userlist[i].realname+"</td>"+
	 	                        "<td>"+data.userlist[i].rewardresult+"</td>"+
	 	                        "<td>"+data.userlist[i].createtime+"</td>";
	 	            }
				    }else{
				    	$(".list_none").remove();
				    	$("#tableadd").parent().append(nodata);
				    }
 					$("#tableadd").html(temp);
 					$('#Pagination').html(data.pager);
 					
 				}
 			}
 		}
 })
}
// function checklook(taskid,isread){
// 	intoDetail("/pc/getPcOaTaskDetail?taskid="+taskid,taskid,isread,19);
// }



function checkSearch(){
	param.starttime=$("#starttime").val(); 
	param.endtime=$("#endtime").val(); 

	onloadDate();
}


//导出
function exportexcel(){
	 swal({
			title : "",
			text : "是否导出",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$.ajax({
		        type: "POST",
		        url: "<%=request.getContextPath() %>/pc/exportUserRewardList",
		        data:param,
		        success: function(data){
		        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
		        }
		    });
		});
		
	}
$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#honourActive').attr('class','active');
	var starttime =  getnowtime(0);
	var endtime =  getnowtime(1);
	$('#starttime').val(starttime);
	$('#endtime').val(endtime);
    param.starttime=$("#starttime").val(); 
	param.endtime=$("#endtime").val(); 
});

//获取当前时间,格式 2015-09-05 
function getnowtime(num) {
    var nowtime = new Date();
    var year = nowtime.getFullYear();
    var month = padleft0(nowtime.getMonth() + num)  ;
    var day = padleft0(nowtime.getDate());
    return year + "-" + month + "-" + day;

}
function padleft0(obj) {
    return obj.toString().replace(/^[0-9]{1}$/, "0" + obj);
}
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>荣誉榜</span><a href="#" onclick="exportexcel()">导出</a></div>
        <div class="page_tab">
            <div class="sel_box">
             <input onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="endtime"    id="starttime"  class="text_time" type="text"  readonly="readonly"  style="width: 135px;"/>
             <span>-</span>
         	 <input onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="endtime" 	id="endtime"  class="text_time m_r20" type="text"  readonly="readonly" style="width: 135px;"/>
				<input type="button" value="" class="find_btn"  onclick="checkSearch()" />
                <div class="clear"></div>
            </div>
             <div class="tab_list">
                 <table width="100%" border="0" cellpadding="0" cellspacing="0" id="tableadd" style="font-size: 12px;">
                   
               
                </table>
            </div>
            <div id="Pagination" style="width:450px;"></div>
            </div>
    </div>
    <div class="clear"></div>
</div>
</body>
</html>
