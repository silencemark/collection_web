<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>收款方式管理-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>  
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript">
	var pageWidth = window.innerWidth;
	var pageHeight = window.innerHeight;
	var minHeight;
	var usernameWidth;
	if (typeof pageWidth != "number")
	{
		if(document.compatMode == "CSS1Compat")
		{
			pageWidth = document.documentElement.clientWidth;
			pageHeight = document.documentElement.clientHeight;
		}
		else
		{
			pageWidth = document.body.clientWidth;
			pageHeight = document.body.clientHeight;
		}
	}
	minHeight = pageHeight - 77;
	$(document).ready(function(){		
		$(".main_page").css("min-height",minHeight+"px");
		
		usernameWidth = $(".top_username").width();		
		usernameWidth = (160 - usernameWidth)/2;
	    $(".top_username").css("margin-left",usernameWidth+"px");		
		$(".head_box .user_box").hover(
			  function () {
				$(".head_box .user_box .name").addClass("name_border"); //移入
				$(".head_box .user_box .box").show();
			  },
			  function () {
				$(".head_box .user_box .name").removeClass("name_border");//移除
				$(".head_box .user_box .box").hide();
			  }
			);			
});
	
$(document).ready(function(){
	$('#paymentlist').parent().parent().find("span").attr("class","bg_hidden");
	$('#paymentlist').attr('class','active li_active');
})

//显示隐藏微信
function weixinShowHide(rum,imagesrc){
	if(rum == 1 ){
		$("#weixinimgdiv").hide();	
		$(".div_mask").css("display","none");
	}else{
		$("#weixinimgdiv").show();	
		$(".div_mask").css("display","block");
		$("#weixinimagesrc").attr("src", imagesrc);
	}
}
//显示支付宝
function alipayImageShowHide(rum,imagesrc){
	if(rum == 1 ){
		$("#alipayimgdiv").hide();	
		$(".div_mask").css("display","none");
	}else{
		$("#alipayimgdiv").show();	
		$(".div_mask").css("display","block");
		$("#alipayimagesrc").attr("src", imagesrc);
	}
}

</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">收款方式管理</a><i>/</i><span>收款方式列表</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">收款方式管理列表</span></div>
        <div class="sel_box">
        	<form action="<%=request.getContextPath()%>/managebackstage/getPaymentMethodList" method="post">
        		<input type="text" class="text" placeholder="请输入收款人姓名" name="realname" value="${map.realname}"/>
	            <input type="submit" value="搜索" class="find_btn"  />
            </form>
            <div class="clear"></div>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
                	<td>用户昵称</td>
                    <td>微信号</td>
                    <td>微信收款码</td>
                    <td>微信收款人姓名</td>
                    <td>支付宝账号</td>
                    <td>支付宝收款码</td>
                    <td>支付宝收款人姓名</td>
                    <td>开户行</td>
                    <td>银行卡号</td>
                    <td>持卡人真实姓名</td>
                    <td>修改时间</td>
                </tr>
                <c:forEach items="${list }" var="li">
                	<tr>
	                	<td>${li.nickname }</td>
	                    <td>${li.weixinnum }</td>
	                    <td onclick="weixinShowHide(0,'${li.weixinqrcode }');"><img src="${li.weixinqrcode }" width="48pxs" height="48px" alt="" /></td>
	                    <td>${li.weixinrealname }</td>
	                    <td>${li.alipaynum }</td>
	                    <td onclick="alipayImageShowHide(0,'${li.alipayqrcode }');"><img src="${li.alipayqrcode }" width="48pxs" height="48px" alt="" /></td>
	                    <td>${li.alipayrealname }</td>
	                    <td>${li.bank }</td>
	                    <td>${li.banknum }</td>
	                    <td>${li.bankrealname }</td>
	                    <td>${li.updatetime }</td>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div>
    </div>
</div>

<div class="div_mask" style="display:none;"></div>

<div class="tc_changetext"  id="weixinimgdiv"  style="display:none;width: 560px;top:40%;">
	<div class="tc_title"><span>展示微信收款码</span><a href="#" onclick="weixinShowHide(1,'')">×</a></div>
    <div class="box">
    	<span>微信收款码</span>
        <img id="weixinimagesrc" width="400px" height="500px"></i>
        <div class="clear"></div>
    </div>
    <div class="tc_btnbox"><a href="#"  class="bg_yellow" onclick="weixinShowHide(1,'')">确定</a></div>
</div>

<div class="tc_changetext"  id="alipayimgdiv"  style="display:none;width: 560px;top:40%;">
	<div class="tc_title"><span>展示支付宝收款</span><a href="#" onclick="alipayImageShowHide(1,'')">×</a></div>
    <div class="box">
    	<span>支付宝收款码</span>
        <img id="alipayimagesrc" width="400px" height="500px"></i>
        <div class="clear"></div>
    </div>
    <div class="tc_btnbox"><a href="#"  class="bg_yellow" onclick="alipayImageShowHide(1,'')">确定</a></div>
</div>
</body>
</html>
