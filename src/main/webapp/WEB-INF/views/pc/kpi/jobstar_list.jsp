<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>岗位星值列表</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/appbase.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/kpi/jobstart_list.js"></script>
<script type="text/javascript">
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
	        url: "<%=request.getContextPath() %>/pc/exportEvaluateList",
	        data: $('#searchform').serializeArray(),
	        success: function(data){
	        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
	        }
		 });
	    });
	}
	
$(document).ready(function(){
	$('.kpi_li').find("li").attr('class','');
	$('#jobstar').attr('class','active');
});
</script>
</head>

<body>
<input type="hidden" id="userid" value="${user.userid }"/>
<input type="hidden" id="companyid" value="${user.companyid }"/>
<input type="hidden" id="power3001010" value="${powerMap.power3001010 }"/>
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>岗位星值列表</span><a href="javascript:void(0)" onclick="exportexcel()">导出</a>
    	<c:if test="${powerMap.power3001010 != null}"><a href="<%=request.getContextPath()%>/pc/jobStarAdd">新建</a></c:if>
    	</div>
        <div class="page_tab">
            <div class="sel_box">
                <input onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="readonly" class="text_time" name="starttime" type="text" placeholder="开始日期" id="starttime"/>
                <span>-</span>
                <input onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="readonly" class="text_time" name="endtime" type="text" placeholder="结束日期" id="endtime"/>
                <select class="sel" id="status"><option value="-1">全部</option><option value="0">待处理</option><option value="1">已处理</option></select>
                <input type="button" value="" class="find_btn"  onclick="fuzzySearch()"/>
                <div class="clear"></div>
            </div>
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr class="head_td">
                        <td width="20%">申请人</td>
                        <td width="20%">填写时间</td>
                        <td width="20%">总星值</td>
                        <td width="20%">状态</td>
                        <td>操作</td>
                    </tr>
                    <tbody id="starlist">
                    	
                    </tbody>
                </table>
            </div>
            <div id="Pagination" style="width:450px;"></div><!--动态的获取pagination的宽度赋值给Pagination-->
        </div>
    </div>
    <div class="clear"></div>
</div>
<form action="<%=request.getContextPath()%>/pc/getStarAssessList"
						id="searchform" method="post"></form>
</body>
</html>
