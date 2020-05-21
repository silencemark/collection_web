<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报修单</title>
<link
	href="<%=request.getContextPath()%>/userbackstage/style/pc_public.css"
	type="text/css" rel="stylesheet" />
<link
	href="<%=request.getContextPath()%>/userbackstage/style/pc_page.css"
	type="text/css" rel="stylesheet" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/app/appcssjs/sweetalert/dist/sweetalert.css" />
<script
	src="<%=request.getContextPath()%>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
</head>
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
        url: "<%=request.getContextPath() %>/pc/exportRepairList",
        data: $('#searchform').serializeArray(),
        success: function(data){
        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
        }
    });
	});
}


</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('.sup_li').find("li").attr('class', '');
		$('#repair').attr('class', 'active');
		$('#homepage').parent().find("li").attr('class', '');
		$('#homepage').attr('class', 'active');
	})
</script>

<body>
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
		<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>报修单列表</span><a href="javascript:void(0)"
					onclick="exportexcel();">导出</a>
					<a href="<%=request.getContextPath() %>/pc/intoRepairAdd">新建</a></div>
        <div class="page_tab">
            <div class="sel_box">
            <form action="<%=request.getContextPath()%>/pc/getRepairListInfo"
						id="searchform" method="post">
               <input type="text" class="text_time"
						placeholder="开始时间" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"
						value="${map.begintime}" name="begintime" /> <span>-</span> <input
						type="text" class="text_time m_r20" placeholder="结束时间"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"
						value="${map.endtime}" name="endtime" /> <select class="sel"
						name="status">
						<option value="-1">全部</option>
						<c:choose>
							<c:when test="${map.status=='0'}">
								<option value="0" selected="selected">待处理</option>
								<option value="1">已处理</option>
							</c:when>
							<c:when test="${map.status=='1'}">
								<option value="0">待处理</option>
								<option value="1" selected="selected">已处理</option>
							</c:when>
							<c:otherwise>
								<option value="0">待处理</option>
								<option value="1">已处理</option>
							</c:otherwise>
						</c:choose>
                    </select>
                  <input type="submit" value="" class="find_btn" /> </form>
                <div class="clear"></div>
            </div>
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr class="head_td">
                        <td width="40%">描述</td>
                        <td width="18%">发现时间</td>
                        <td width="13%">填写人</td>
                        <td width="12%">状态</td>
                        <td>操作</td>
                    </tr>
                    <c:forEach var="item" items="${repairlist}">
                    <tr>
                        <td>${item.description}</td>
                        <td>${item.findtime}</td>
                        <td>${item.createname}</td>
                       <c:choose>
									<c:when test="${item.status=='1'}">
										<td class="green">已处理</td>
									</c:when>
									<c:otherwise>
										<td class="red">未处理</td>
									</c:otherwise>
								</c:choose>
		                  <td>	
		                  		<a href="<%=request.getContextPath() %>/pc/getRepairDetailInfo?repairid=${item.repairid}&forwarduserid=${item.id}" class="link">查看</a>
		                  		<c:if test="${item.status==1}"><a href="javascript:void(0)" onclick="deleteForwardUserInfo(this,'${item.id}')" class="link" style="color:red;">删除</a></c:if>
		                  </td>
                    </tr>
                  </c:forEach>
                </table>
                <c:if test="${fn:length(repairlist) == 0 }">
	                	<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>还没有报修单，赶紧添加一条吧~</span><b><img src="<%=request.getContextPath() %>/userbackstage/images/index/none_msg.gif" width="293" height="240"></b></div>
	                </c:if>
            </div>
          <div id="Pagination" style="width:450px;">${page}</div><!--动态的获取pagination的宽度赋值给Pagination-->
        </div>
    </div>
    <div class="clear"></div>
</div>
</body>
</html>
