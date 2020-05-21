<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>通用审批</title>
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
 		url:'/pc/getCurrencyExamineList',
 		data:param,
 		success:function(data){
 			$("#tableadd").html("");
				if(data.dataList!=null){
					var temp="   <tr class=\"head_td\">"+
			                    "<td width=\"15%\">申请人</td>"+
			                    "<td width=\"35%\">申请内容</td>"+
			                    "<td width=\"15%\">填写时间</td>"+
			                   " <td width=\"15%\">状态</td>"+
			                    "<td>操作</td>"+
			               " </tr>";
					for(var i= 0;i<data.dataList.length;i++){
						temp+="<tr>"+
 	                    	"<td>"+data.dataList[i].createname+"</td>"+
 	                        "<td>"+data.dataList[i].content+"</td>"+
 	                        "<td>"+data.dataList[i].createtime+"</td>";
 	                        if(data.dataList[i].status == 0){
 	                        	temp+= "<td class=\"red\">待处理</td>";
 	                        }else{
 	                        	 if(data.dataList[i].result == 1){
 	                           		temp += "<td class=\"green\">同意</td>";
 	                           	}else{
 	                           		temp += "<td class=\"red\">拒绝</td>";
 	                           	}
 	                        }
 	                       temp += "<td><a href=\"#\" class=\"link\" onclick=\"checklook('"+data.dataList[i].currencyexamineid+"','"+data.dataList[i].isread+"','"+data.dataList[i].forwarduserid+"')\">查看</a>";
 	                       if(data.dataList[i].status == 0){
 	                    		if(data.dataList[i].examineuserid == '${userInfo.userid}'){
 	                      		 temp +="<a href=\"#\" class=\"yellow\" onclick=\"checkexamine('"+data.dataList[i].currencyexamineid+"','"+data.dataList[i].isread+"')\">审批</a>";
 	                      		}	
 	                       }else{
 	                    	  temp +="<a href=\"javascript:void(0)\" class=\"red\" onclick=\"deleteForwardUserInfo(this,'"+data.dataList[i].forwarduserid+"')\">删除</a>";
 	                       }
 	                      temp+="</td></tr>";
					}
					$("#tableadd").html(temp);
					$('#Pagination').html(data.pager);
					
				}
 		}
 })
}

var nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>还没有通用审批单，赶紧添加一条吧~</span><b><img src="/userbackstage/images/index/none_msg.gif" width="293" height="240"></b></div>';
function onloadDate(){
	$.ajax({
 		type:'post',
 		url:'/pc/getCurrencyExamineList',
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
 				if(data.dataList!=null){
 					var temp="   <tr class=\"head_td\">"+
				                    "<td width=\"15%\">申请人</td>"+
				                    "<td width=\"35%\">申请内容</td>"+
				                    "<td width=\"15%\">填写时间</td>"+
				                   " <td width=\"15%\">状态</td>"+
				                    "<td>操作</td>"+
				               " </tr>";
				    if(data.dataList.length > 0){
				    	$(".list_none").remove();
 					for(var i= 0;i<data.dataList.length;i++){
 						temp+="<tr>"+
	 	                    	"<td>"+data.dataList[i].createname+"</td>"+
	 	                        "<td>"+data.dataList[i].content+"</td>"+
	 	                        "<td>"+data.dataList[i].createtime+"</td>";
	 	                        if(data.dataList[i].status == 0){
	 	                        	temp+= "<td class=\"red\">待处理</td>";
	 	                        }else{
	 	                        	 if(data.dataList[i].result == 1){
	 	                           		temp += "<td class=\"green\">同意</td>";
	 	                           	}else{
	 	                           		temp += "<td class=\"red\">拒绝</td>";
	 	                           	}
	 	                        }
	 	                       temp += "<td><a href=\"#\" class=\"link\" onclick=\"checklook('"+data.dataList[i].currencyexamineid+"','"+data.dataList[i].isread+"','"+data.dataList[i].forwarduserid+"')\">查看</a>";
	 	                       if(data.dataList[i].status == 0){
	 	                    		if(data.dataList[i].examineuserid == '${userInfo.userid}'){
	 	                      		 temp +="<a href=\"#\" class=\"yellow\" onclick=\"checkexamine('"+data.dataList[i].currencyexamineid+"','"+data.dataList[i].isread+"')\">审批</a>";
	 	                      		}	
	 	                       }else{
	 	                    	  temp +="<a href=\"javascript:void(0)\" class=\"red\" onclick=\"deleteForwardUserInfo(this,'"+data.dataList[i].forwarduserid+"')\">删除</a>";
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

function checklook(currencyexamineid,isread,forwarduserid){
	intoDetail("/pc/getPcOaApprovaDetail?currencyexamineid="+currencyexamineid+"&forwarduserid="+forwarduserid,currencyexamineid,isread,15);
}

function checkexamine(currencyexamineid,isread){
	intoDetail("/pc/getPcOaApprovaCheck?currencyexamineid="+currencyexamineid,currencyexamineid,isread,15);
}

function checkSearch(){
	param.starttime=$("#starttime").val(); 
	param.endtime=$("#endtime").val(); 
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
		        url: "<%=request.getContextPath() %>/pc/exportCurrencyList",
		        data:param,
		        success: function(data){
		        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
		        }
		    });
		});
		
	}
$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#approvaActive').attr('class','active');
});
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>通用审批</span><a href="#" onclick="exportexcel()">导出</a>
    	<a href="<%=request.getContextPath() %>/pc/getPcOaApprovaAdd">新建</a>
    	</div>
        <div class="page_tab">
            <div class="sel_box">
             	 <input onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="endtime"    id="starttime"  class="text_time" type="text"  readonly="readonly"  style="width: 135px;"/>
             <span>-</span>
         	 <input onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="endtime" 	id="endtime"  class="text_time m_r20" type="text"  readonly="readonly" style="width: 135px;"/>
         	 
             <select class="sel" id="sel"><option value="">全部</option><option value="0">未处理</option><option value="1">已处理</option></select>
                <input type="button" value="" class="find_btn" onclick="checkSearch()" />
                <div class="clear"></div>
            </div>
	            <div class="tab_list">
	                <table width="100%" border="0" cellpadding="0" cellspacing="0" id="tableadd" style="font-size: 12px;">
	                   
	               
	                </table>
	            </div>
	      	 <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination-->
        </div>
    </div>
    <div class="clear"></div>
</div>
</body>
</html>
