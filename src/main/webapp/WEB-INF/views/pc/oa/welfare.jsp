<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>员工关怀</title>
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
 		url:'/pc/getUserBirthdayList',
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
				                	"<td width=\"30%\">姓名</td>"+
				                    "<td width=\"25%\">生日</td>"+
				                    "<td width=\"25%\">职务</td>"+
				                    "<td>所属部门</td>"+
				                "</tr>";
 					for(var i= 0;i<data.userlist.length;i++){
 						var position = data.userlist[i].position;
 						if(position == undefined || position == 'undefined'){
 							position = "";
 						}
 						var birthday = data.userlist[i].birthday;
 						if(birthday == undefined || birthday == 'undefined'){
 							birthday = "";
 						}
 						temp+="<tr>"+
	 	                    	"<td><div class=\"img f\"><img src=\""+data.userlist[i].headimage+"\" width=\"30\" height=\"30\" /></div><i class=\"i_name\">"+data.userlist[i].realname+"</i></td>"+
	 	                        "<td>"+birthday+"</td>"+
	 	                       	"<td>"+position+"</td>"+
	 	                        "<td>"+data.userlist[i].organizename+"</td>";
	 	            }
 					$("#tableadd").html(temp);
 					$('#Pagination').html(data.pager);
 					
 				}
 			}
 		}
 })
}

var nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>当月没有员工过生日哦~</span><b><img src="/userbackstage/images/index/none_msg.gif" width="293" height="240"></b></div>';
function onloadDate(){
	$.ajax({
 		type:'post',
 		url:'/pc/getUserBirthdayList',
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
				                	"<td width=\"30%\">姓名</td>"+
				                    "<td width=\"25%\">生日</td>"+
				                    "<td width=\"25%\">职务</td>"+
				                    "<td>所属部门</td>"+
				                "</tr>";
				 if(data.userlist.length > 0){
					 $(".list_none").remove();
 					for(var i= 0;i<data.userlist.length;i++){
 						var position = data.userlist[i].position;
 						if(position == undefined || position == 'undefined'){
 							position = "";
 						}
 						var birthday = data.userlist[i].birthday;
 						if(birthday == undefined || birthday == 'undefined'){
 							birthday = "";
 						}
 						temp+="<tr>"+
	 	                    	"<td><div class=\"img f\"><img src=\""+data.userlist[i].headimage+"\" width=\"30\" height=\"30\" /></div><i class=\"i_name\">"+data.userlist[i].realname+"</i></td>"+
	 	                        "<td>"+birthday+"</td>"+
	 	                       	"<td>"+position+"</td>"+
	 	                        "<td>"+data.userlist[i].organizename+"</td>";
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
	param.monthnum = $('#month').val();
	//alert(param.monthnum);

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
		        url: "<%=request.getContextPath() %>/pc/exportUserBirthdayList",
		        data:param,
		        success: function(data){
		        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
		        }
		    });
		});
		
	}
$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#welfareActive').attr('class','active');
	var starttime =  getnowtime(0);
	var endtime =  getnowtime(1);
	$('#starttime').val(starttime);
	$('#endtime').val(endtime);
    param.starttime=$("#starttime").val(); 
	param.endtime=$("#endtime").val();
	
	//获取当前月份
	var mydate = new Date();
	var month=mydate.getMonth()+1;
	param.monthnum = month;
	$('#month option[vlaue="'+month+'"]').attr("selected","selected");
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
    	<div class="page_name"><span>员工关怀</span><a href="#" onclick="exportexcel()">导出</a></div>
    	  <div class="page_tab">
         <div class="sel_box">
<!--              <input onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="endtime"    id="starttime"  class="text_time" type="text"  readonly="readonly"  style="width: 135px;"/> -->
<!--              <span>-</span> -->
<!--          	 <input onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="endtime" 	id="endtime"  class="text_time m_r20" type="text"  readonly="readonly" style="width: 135px;"/> -->
				<select class="sel"  id="month">
					<option value="1">1&nbsp;月</option>
					<option value="2">2&nbsp;月</option>
					<option value="3">3&nbsp;月</option>
					<option value="4">4&nbsp;月</option>
					<option value="5">5&nbsp;月</option>
					<option value="6">6&nbsp;月</option>
					<option value="7">7&nbsp;月</option>
					<option value="8">8&nbsp;月</option>
					<option value="9">9&nbsp;月</option>
					<option value="10">10&nbsp;月</option>
					<option value="11">11&nbsp;月</option>
					<option value="12">12&nbsp;月</option>
				</select>
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
