<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><c:choose><c:when test="${supplierInfo != null }">编辑</c:when><c:otherwise>添加</c:otherwise></c:choose>供应商</title>
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
    	<div class="page_name"><span><c:choose><c:when test="${supplierInfo != null }">编辑</c:when><c:otherwise>添加</c:otherwise></c:choose>供应商</span><a href="javascript:void(0)" onclick="goBackPage()" class="back">返回</a></div>
        <div class="page_tab2">            
            <div class="tab_list">
            	<form action="<%=request.getContextPath() %>/pc/editSupplier" method="post" id="editform">
            	<input type="hidden" name="supplierid" value="${supplierInfo.supplierid}"/>
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="none_border">
                    <tr>
                    	<td class="l_name">供应商编号</td>
                    	<input type="hidden" name="supplierno" value="${supplierInfo.supplierno==null?supplierno:supplierInfo.supplierno}">
                        <td class="num_xx">${supplierInfo.supplierno==null?supplierno:supplierInfo.supplierno}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">供应商名称</td>
                        <td><input type="text" class="text" placeholder="请输供应商名称入" name="suppliername" value="${supplierInfo.suppliername }" /></td>
                    </tr>
                    <tr>
                    	<td class="l_name">供应商类型</td>
                        <td><input type="text" class="text" placeholder="请输入供应商类型" name="suppliertype" value="${supplierInfo.suppliertype }" /></td>
                    </tr>
                    <tr>
                    	<td class="l_name">联系人</td>
                        <td><input type="text" class="text" placeholder="请输入联系人" name="contactsname" value="${supplierInfo.contactsname }" /></td>
                    </tr>
                    <tr>
                    	<td class="l_name">联系电话</td>
                        <td><input type="text" class="text" maxlength="11" oninput="this.value=this.value.replace(/\D/g,'')" placeholder="请输入联系电话" name="phone" value="${supplierInfo.phone }" /></td>
                    </tr>
                    <tr>
                    	<td class="l_name">职务</td>
                        <td><input type="text" class="text" placeholder="请输入职务" name="position" value="${supplierInfo.position }" /></td>
                    </tr>
                    <tr>
                    	<td class="l_name">联系地址</td>
                        <td><input type="text" class="text2" placeholder="请输入详细联系地址" name="address" value="${supplierInfo.address }" /></td>
                    </tr>
                    <tr>
                    	<td class="l_name">纳税人识别号</td>
                        <td><input type="text" class="text" placeholder="请输入纳税人识别号" name="taxpayernum" value="${supplierInfo.taxpayernum }" /></td>
                    </tr>
                    <tr>
                    	<td class="l_name">开户行</td>
                        <td><input type="text" class="text" placeholder="请输入开户行" name="bankname" value="${supplierInfo.bankname }" /></td>
                    </tr>
                    <tr>
                    	<td class="l_name">银行账号</td>
                        <td><input type="text" class="text" placeholder="请输入银行账号" name="bankaccount" value="${supplierInfo.bankaccount }" /></td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name2">简介</td>
                        <td><textarea class="text_area" placeholder="请输入简介，最多允许输入800字符" maxlength="800" name="introduction" >${supplierInfo.introduction }</textarea></td>
                    </tr>
                    <tr class="foot_td">
                    	<td class="l_name">&nbsp;</td>
                        <td class="img_td"><a href="javascript:void(0)" onclick="submitdata()" class="a_btn bg_yellow">保存</a><a href="javascript:void(0)" onclick="goBackPage()" class="a_btn bg_gay2">取消</a></td>
                </table>
                </form>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>
<script type="text/javascript">
function submitdata(){
	if($("input[name=suppliername]").val().trim() == ""){
		swal({
			title : "",
			text : "供应商名称不能为空",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$("input[name=suppliername]").focus();
		});
		
	 return false;
 }
 
 if($("input[name=contactsname]").val().trim() == ""){
	swal({
			title : "",
			text : "联系人姓名不能为空",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			 $("input[name=contactsname]").focus();
		});
	 return false;
 }
 
 if( $("input[name=phone]").val().trim() == ""){
	 swal({
			title : "",
			text : "联系人电话不能为空",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			 $("input[name=phone]").focus();
		});
	 
	 return false;
 }
 
 if(!(/^1[3|4|5|7|8]\d{9}$/.test($("input[name=phone]").val()))){
	 swal({
			title : "",
			text : "手机号不合法",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$("input[name=phone]").focus();
		});
	 return false;
	 
 }
swal({
	title : "",
	text : "是否提交？",
	type : "warning",
	showCancelButton : true,
	confirmButtonColor : "#ff7922",
	confirmButtonText : "确认",
	cancelButtonText : "取消",
	closeOnConfirm : true
}, function(){
	$('#editform').submit();
});
	
}
</script>
</body>
</html>
