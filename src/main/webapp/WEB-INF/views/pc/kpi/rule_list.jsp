<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>KPI排行规则</title>
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

var param=new Object();
param.userid ='${user.userid}';
param.companyid = "${user.companyid}";

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
				 					" <td width=\"20%\">申请时间</td>"+
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
	 	                        	temp+= "<td class=\"green\">已处理</td>";
	 	                        }
	 	                       temp += "<td><a href=\"#\" class=\"link\" onclick=\"checklook('"+data.datalist[i].overallvaluateid+"','"+data.datalist[i].isread+"')\">查看</a>";
	 	                       if(data.datalist[i].status == 0){
	 	                    		if(data.datalist[i].examineuserid == '${user.userid}'){
	 	                      		 temp +="<a href=\"#\" class=\"yellow\" onclick=\"checkexamine('"+data.datalist[i].overallvaluateid+"','"+data.datalist[i].isread+"')\">审核</a>";
	 	                      		}	
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

var nodata = '<div class="list_none"><span><i class="yellow">Hi,我是大狮！</i><br>列表空空，KPI排行还没有人哦~</span><b><img src="/userbackstage/images/index/none_msg2.gif" width="293" height="240"></b></div>';
function onloadDate(){
	$.ajax({
 		type:'post',
 		url:'/pc/getStarRuleList',
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
 				if(data.rulelist!=null){
 					var temp="  <tr class=\"head_td\">"+
				 					"<td width=\"50%\">规则标题</td>"+
				                    "<td width=\"35%\">发布时间</td>"+
				                    "<td>操作</td>"+
				 				"   </tr>";
				 	if(data.rulelist.length > 0){
				 		$(".list_none").remove();
 					for(var i= 0;i<data.rulelist.length;i++){
 						temp +="<tr>"+
				 					" <td>"+data.rulelist[i].title+"</td>"+
		 							" <td>"+data.rulelist[i].createtime+"</td>"+
		 							" <td><a href=\"#\" onclick=\"selectfun('"+data.rulelist[i].ruleid+"')\" class=\"blue\">查看</a></td>"+
					  			"</tr>";
 					}
				 	}else{
				 		$(".list_none").remove();
				 		$("#nodata").parent().parent().append(nodata);
				 	}
 					$("#tableadd").html(temp);
 					$('#Pagination').html(data.pager);
 					
 				}
 			}
 		}
 })
}

function selectfun(ruleid){
	location.href="<%=request.getContextPath() %>/pc/ruleDetail?ruleid="+ruleid+"&organizeid=${map.organizeid}";
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
    	<div class="page_name"><span>KPI排行规则</span><a href="<%=request.getContextPath() %>/pc/rankingList"" class="back" >返回</a></div>
        <div class="page_tab">
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" id="tableadd" style="font-size:12px;">
                     <tr class="head_td">
                       	<td width="50%">规则标题</td>
                        <td width="35%">发布时间</td>
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
