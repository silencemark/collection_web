<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>个人中心—个人信息</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
	<!-- 日期 -->
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>


<script type="text/javascript">
var userid;
userid='${user.userid}';
var param=new Object();
param.receiveid = '${user.userid}';
param.companyid ='${user.companyid}';

function pageHelper(num){
	var currentPage = num;
	param.currentPage=currentPage;
	$.ajax({
 		type:'post',
 		url:'/pc/getOverallvaluateList',
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
				 					" <td width=\"20%\">申请人</td>"+
				 					" <td width=\"20%\">填写时间</td>"+
				 					" <td width=\"20%\">总星值</td>"+
				 					"  <td width=\"20%\">状态</td>"+
				 					" <td>操作</td>"+
				 				"   </tr>";
 					for(var i= 0;i<data.datalist.length;i++){
 						temp+="<tr>"+
		 						"<td>"+data.datalist[i].createname+"</td>"+
		 						"<td>"+data.datalist[i].createtime+"</td>"+
	 	                    	"<td>"+data.datalist[i].sumstar+"星</td>";
	 	                        if(data.datalist[i].status == 0){
	 	                        	temp+= "<td class=\"red\">待处理</td>";
	 	                        }else{
	 	                        	if(data.datalist[i].result == 1){
	 	                        		temp+= "<td class=\"green\">同意</td>";
	 	                        	}else{
	 	                        		temp+= "<td class=\"red\">拒绝</td>";
	 	                        	}
	 	                        }
	 	                       temp += "<td><a href=\"#\" class=\"link\" onclick=\"checklook('"+data.datalist[i].overallvaluateid+"','"+data.datalist[i].isread+"','"+data.datalist[i].forwarduserid+"')\">查看</a>";
	 	                       if(data.datalist[i].status == 0){
	 	                    		if(data.datalist[i].examineuserid == '${user.userid}'){
	 	                      		 temp +="<a href=\"#\" class=\"yellow\" onclick=\"checkexamine('"+data.datalist[i].overallvaluateid+"','"+data.datalist[i].isread+"')\">审核</a>";
	 	                      		}	
	 	                       }else{
	 	                    	  temp +="<a href=\"javascript:void(0)\" class=\"red\" onclick=\"deleteForwardUserInfo(this,'"+data.datalist[i].forwarduserid+"')\">删除</a>";
	 	                       }
	 	                      temp+="</td></tr>";
 					}
 					$("#tableadd").html(temp);
 					$('#Pagination').html(data.pager);
 					
 				}
 			}
 		}
 })
}

var nodata = '<div class="list_none"><span><i class="yellow">Hi,我是大狮！</i><br>列表空空，还没有综合星值呢~</span><b><img src="/userbackstage/images/index/none_msg2.gif" width="293" height="240"></b></div>';
if("${powerMap.power3001010}" != ""){
	nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>还没有综合星值，赶紧添加一条吧~</span><b><img src="/userbackstage/images/index/none_msg.gif" width="293" height="240"></b></div>';
}
function onloadDate(){
	$.ajax({
 		type:'post',
 		url:'/pc/getOverallvaluateList',
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
				 					" <td width=\"20%\">申请人</td>"+
				 					" <td width=\"20%\">填写时间</td>"+
				 					" <td width=\"20%\">总星值</td>"+
				 					"  <td width=\"20%\">状态</td>"+
				 					" <td>操作</td>"+
				 				"   </tr>";
				 if(data.datalist.length > 0){
					 $(".list_none").remove();
 					for(var i= 0;i<data.datalist.length;i++){
 						temp+="<tr>"+
		 						"<td>"+data.datalist[i].createname+"</td>"+
		 						"<td>"+data.datalist[i].createtime+"</td>"+
	 	                    	"<td>"+data.datalist[i].sumstar+"星</td>";
	 	                        if(data.datalist[i].status == 0){
	 	                        	temp+= "<td class=\"red\">待处理</td>";
	 	                        }else{
	 	                        	if(data.datalist[i].result == 1){
	 	                        		temp+= "<td class=\"green\">同意</td>";
	 	                        	}else{
	 	                        		temp+= "<td class=\"red\">拒绝</td>";
	 	                        	}
	 	                        }
	 	                       temp += "<td><a href=\"#\" class=\"link\" onclick=\"checklook('"+data.datalist[i].overallvaluateid+"','"+data.datalist[i].isread+"','"+data.datalist[i].forwarduserid+"')\">查看</a>";
	 	                       if(data.datalist[i].status == 0){
	 	                    		if(data.datalist[i].examineuserid == '${user.userid}'){
	 	                      		 temp +="<a href=\"#\" class=\"yellow\" onclick=\"checkexamine('"+data.datalist[i].overallvaluateid+"','"+data.datalist[i].isread+"')\">审核</a>";
	 	                      		}	
	 	                       }else{
	 	                    	  temp +="<a href=\"javascript:void(0)\" class=\"red\" onclick=\"deleteForwardUserInfo(this,'"+data.datalist[i].forwarduserid+"')\">删除</a>";
	 	                       }
	 	                      temp+="</td></tr>";
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

function checklook(overallvaluateid,isread,forwarduserid){
	intoDetail("/pc/allstarDetail?overallvaluateid="+overallvaluateid+"&forwarduserid="+forwarduserid,overallvaluateid,isread,7);
}

function checkexamine(overallvaluateid,isread){
	intoDetail("/pc/allstarCheck?overallvaluateid="+overallvaluateid,overallvaluateid,isread,7);
}

function checkSearch(){
	param.beforetime=$("#starttime").val(); 
	param.aftertime=$("#endtime").val(); 
	if($("#sel").val()==""){
		delete param.status;
	}else{
		param.status=$("#sel").val(); 	
	}
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
		        url: "<%=request.getContextPath() %>/pc/exportOverallList",
		        data:param,
		        success: function(data){
		        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
		        }
		    });
		});
		
	}

$(document).ready(function(){
	$('.kpi_li').find("li").attr('class','');
	$('#allsstar').attr('class','active');
});
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>综合星值列表</span><a href="#" onclick="exportexcel()">导出</a>
    	<c:if test="${powerMap.power3001010 != null}"><a href="<%=request.getContextPath() %>/pc/allstarAdd">新建</a></c:if>
    	</div>
        <div class="page_tab">
            <div class="sel_box">
             <input onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="endtime"  placeholder="开始时间"   id="starttime"  class="text_time" type="text"  readonly="readonly"  style="width: 135px;"/>
             <span>-</span>
         	 <input onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="endtime" placeholder="结束时间" 	id="endtime"  class="text_time m_r20" type="text"  readonly="readonly" style="width: 135px;"/>
         	 
                <select class="sel" id="sel"><option value="">全部</option><option value="0">未处理</option><option value="1">已处理</option></select>
                <input type="button" value="" class="find_btn"  onclick="checkSearch()" />
                <div class="clear"></div>
            </div>
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" id="tableadd" style="font-size:12px;">
                     <tr class="head_td">
                        <td width="20%">申请人</td>
                        <td width="20%">填写时间</td>
                        <td width="20%">总星值</td>
                        <td width="20%">状态</td>
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
