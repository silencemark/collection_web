<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>供应商详情</title>
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
        url: "<%=request.getContextPath() %>/pc/exportSupplierInfo",
        data: 'supplierid=${supplierInfo.supplierid}',
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
    	<div class="page_name"><span>供应商详情</span><a href="<%=request.getContextPath() %>/pc/getSupplierList" class="back">返回</a><a href="javascript:void(0)" onclick="exportexcel();">导出</a><c:if test="${powerMap.power1001010 != null}"><a href="<%=request.getContextPath()%>/pc/initEditSupplier?supplierid=${supplierInfo.supplierid}">修改</a></c:if></div>
        <div class="page_tab2">            
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                    	<td class="l_name">供应商编号</td>
                        <td>${supplierInfo.supplierno}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">供应商名称</td>
                        <td>${supplierInfo.suppliername}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">供应商类型</td>
                        <td>${supplierInfo.suppliertype}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">联系人</td>
                        <td>${supplierInfo.contactsname}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">联系电话</td>
                        <td>${supplierInfo.phone}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">职务</td>
                        <td>${supplierInfo.position}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">联系地址</td>
                        <td>${supplierInfo.provincename}${supplierInfo.cityname}${supplierInfo.districtname}${supplierInfo.address}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">纳税人识别号</td>
                        <td>${supplierInfo.taxpayernum}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">开户行</td>
                        <td>${supplierInfo.bankname}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">银行账号</td>
                        <td>${supplierInfo.bankaccount}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">简介</td>
                        <td>${supplierInfo.introduction}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">创建人</td>
                        <td class="img_td"><div class="img f"><c:if test="${supplierInfo.createheadimage != ''}"><img src="${supplierInfo.createheadimage}" width="30" height="30" /></c:if></div><i class="i_name">${supplierInfo.createname }</i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">创建时间</td>
                        <td>${supplierInfo.createtime.substring(0,16) }</td>
                    </tr>
                    <tr>
                    	<td class="l_name">修改人</td>
                        <td class="img_td"><div class="img f"><c:if test="${supplierInfo.updateheadimage != ''}"><img src="${supplierInfo.updateheadimage}" width="30" height="30" /></c:if></div><i class="i_name">${supplierInfo.updatename }</i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">修改时间</td>
                        <td>${supplierInfo.updatetime!=null && supplierInfo.updatetime!=''?supplierInfo.updatetime.substring(0,16):'' }</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>
</body>
</html>