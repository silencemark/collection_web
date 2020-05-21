<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>供应商</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
</head>
<script type="text/javascript">
function exportexcel(){
	$.ajax({
        type: "POST",
        url: "<%=request.getContextPath() %>/pc/exportSupplierList",
        data: $('#searchform').serializeArray(),
        success: function(data){
        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
        }
    });
}

$(document).ready(function(){
	$('.sup_li').find("li").attr('class','');
	$('#supplier').attr('class','active');
	$('#homepage').parent().find("li").attr('class','');
	$('#homepage').attr('class','active');
})
</script>
<body>
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>供应商列表</span><a href="javascript:void(0)" onclick="exportexcel();">导出</a><c:if test="${powerMap.power1001010 != null}"><a href="<%=request.getContextPath()%>/pc/initEditSupplier" >新建</a></c:if></div>
        <div class="page_tab">
        	<form action="<%=request.getContextPath() %>/pc/getSupplierList" method="post" id="searchform">
            <div class="sel_box">
                <input type="text" class="text" placeholder="请输入供应商名称" name="suppliername" value="${map.suppliername}"/>
                <input type="text" class="text" placeholder="请输入联系人姓名" name="contactsname" value="${map.contactsname}"/>
                <input type="submit" value="" class="find_btn"  />
                <div class="clear"></div>
            </div>
            </form>
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr class="head_td">
                        <td width="22%">供应商编号</td>
                        <td width="22%">供应商名称</td>
                        <td width="22%">联系人</td>
                        <td width="22%">联系电话</td>
                        <td>操作</td>
                    </tr>
                    <c:forEach items="${supplierlist}" var="item">
                    <tr>
                    	<td>${item.supplierno}</td>
                        <td>${item.suppliername}</td>
                        <td>${item.contactsname}（${item.position}）</td>
                        <td>${item.phone}</td>
                        <td><a href="<%=request.getContextPath() %>/pc/getSupplierInfo?supplierid=${item.supplierid}" class="blue">查看</a></td>
                    </tr>
                    </c:forEach>
                </table>
            </div>
		    <c:if test="${fn:length(supplierlist) == 0 }">
                	<c:if test="${powerMap.power1001010 != null}">
                		<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>还没有供应商，赶紧添加一个吧~</span><b><img src="<%=request.getContextPath() %>/userbackstage/images/index/none_msg.gif" width="293" height="240"></b></div>
                	</c:if>
                	<c:if test="${powerMap.power1001010 == null}">
                		<div class="list_none"><span><i class="yellow">Hi,我是大狮！</i><br>列表空空，还没有供应商呢~</span><b><img src="<%=request.getContextPath() %>/userbackstage/images/index/none_msg2.gif" width="293" height="240"></b></div>
                	</c:if>
                </c:if>
            <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination-->
        </div>
    </div>
    <div class="clear"></div>
</div>
<!-- <div class="div_mask"></div>
<div class="tc_tsxx">
	<div class="tc_title"><span>提示信息</span><a href="#">×</a></div>
    <div class="txt"><span>无此权限，请联系管理员开通！</span></div>
    <div class="tc_btnbox"><a href="#" class="bg_yellow">确认</a></div>
</div> -->
</body>
</html>
