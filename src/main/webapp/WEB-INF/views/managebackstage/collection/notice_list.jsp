<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>发送系统通知管理页面-管理方后台</title>
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
	$('#notice').parent().parent().find("span").attr("class","bg_hidden");
	$('#notice').attr('class','active li_active');
})


//隐藏窗口
function checkHide(num){
	if(num == 1) {
		$('#updateform').submit();
	}else {
		$("#updatediv").hide();	
		$(".div_mask").css("display","none");
	}
}

//显示隐藏窗口
function updatemovie(){
	$("#updatediv").show();	
	$(".div_mask").css("display","block");
}

</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><span>发送系统通知管理列表</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">发送系统通知管理列表</span><a href="#" onclick="updatemovie()">发布通知</a></div>
        <div class="sel_box">
        	<form action="<%=request.getContextPath()%>/managebackstage/getSysNoticeList" method="post">
        		<input type="text" class="text" placeholder="请输入标题" name="title" value="${map.title}"/>
        		<input type="text" class="text" placeholder="请输入内容" name="message" value="${map.message}"/>
        		<input type="text" class="text" placeholder="请输入录入日期"  name="createtime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${map.createtime}"/>
	            <input type="submit" value="搜索" class="find_btn"  />
            </fo
            rm>
            <div class="clear"></div>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
                	<td>标题</td>
                    <td>内容</td>
                    <td>发送时间</td>
                </tr>
                <c:forEach items="${list }" var="li">
                	<tr>
	                	<td>${li.title }</td>
	                    <td>${li.message }</td>
	                    <td>${li.createtime }</td>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div>
    </div>
</div>

<div class="div_mask" style="display:none;"></div>

<div class="tc_changetext"  id="updatediv"  style="display:none;top: 40%;left: 40%; width: 650px;">
	<div class="tc_title"><span>发送系统通知</span><a href="#" onclick="checkHide(0)">×</a></div>
    <form action="<%=request.getContextPath() %>/managebackstage/sendSysNotice" id="updateform" method="post">
    <div class="box">
        <span>标题</span>
        <input type="text" class="text2"  placeholder="请输入标题"  name="title" />
        <div class="clear"></div>
        <span>通知内容</span>
        <textarea placeholder="请输入通知内容，最多允许输入800字符" maxlength="800" cols="43" style="border: 1px solid #eee;" rows="5" name="message"></textarea>
        <div class="clear"></div>
    </div>
    </form>
    <div class="tc_btnbox"><a href="#" class="bg_gay2" onclick="checkHide(0)">取消</a><a href="#"  class="bg_yellow" onclick="checkHide(1)">确认</a></div>
</div>
</body>
</html>
