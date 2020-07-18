<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>广告管理-管理方后台</title>
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
	$('#advertlist').parent().parent().find("span").attr("class","bg_hidden");
	$('#advertlist').attr('class','active li_active');
})
	
function deleteAdvert(advertid){
	$("input[name=advertid]").val(advertid);
	swal({
		title : "",
		text : "确认删除？",
		type : "error",
		showCancelButton : false,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$('#userform').submit();
	})
}
</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><span>广告管理列表</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">广告图列表管理</span><a href="<%=request.getContextPath() %>/managebackstage/initAddOrUpdateAdvert">添加</a></div>
        <div class="sel_box">
        	<form action="<%=request.getContextPath()%>/managebackstage/getAdvertList" method="post">
        		<input type="text" class="text" placeholder="请输入广告内容" name="advertcontent" value="${map.advertcontent }"/>
	            <input type="submit" value="搜索" class="find_btn"  />
            </form>
            <div class="clear"></div>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
            		<td>广告图</td>
                    <td>链接地址</td>
                    <td>广告内容</td>
                    <td>创建时间 </td>
                    <td>操作</td>
                </tr>
                <c:forEach items="${list }" var="li">
                	<tr>
	                	<td width ="20%"><img src="${li.imgurl}" alt=""/></td>
	                    <td width ="20%">${li.httpurl }</td>
	                    <td width ="20%">${li.advertcontent }</td>
	                    <td width ="20%">${li.createtime }</td>
	                    <td  style="cursor: pointer;" ><a href="javascript:void(0)" onclick="location.href='<%=request.getContextPath() %>/managebackstage/initAddOrUpdateAdvert?advertid=${li.advertid }'" class="blue">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="deleteAdvert('${li.advertid}')" class="blue">删除</a>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div>
    </div>
</div>
<form action="<%=request.getContextPath() %>/managebackstage/deleteAdvert" id="userform" method="post">
     <input type="hidden" name="advertid" />
</form>

</body>
</html>
