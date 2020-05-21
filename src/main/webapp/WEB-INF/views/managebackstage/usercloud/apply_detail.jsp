<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>申请反馈信息-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
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
		$('#capacity').parent().parent().find("span").attr("class","bg_hidden");
		$('#capacity').attr('class','active li_active');
	})
</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath()%>/managebackstage/getCloudCapacityList">申请记录列表</a><i>/</i><span>申请反馈信息</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">申请反馈信息<i class="green">已处理</i></span></div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr>
                    <td class="l_text" width="140">公司名称</td>
                    <td>${cloudmap.companyname }</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">申请人</td>
                    <td>${cloudmap.realname }</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">联系电话</td>
                    <td>${cloudmap.phone }</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">申请大小</td>
                    <td><i class="yellow">${cloudmap.memory }</i></td>
                </tr>
                <tr>
                    <td class="l_text" width="140">填写时间</td>
                    <td>${cloudmap.createtime }</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">实际大小</td>
                    <td><i class="yellow">${cloudmap.sjmemory }</i></td>
                </tr>
                <tr>
                    <td class="l_text" width="140">备注</td>
                    <td>${cloudmap.refusereason }</td>
                </tr>
            </table>
        </div>
    </div>
</div>
</body>
</html>
