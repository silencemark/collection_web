<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>banner图管理-管理方后台</title>
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
	$('#bannerlist').parent().parent().find("span").attr("class","bg_hidden");
	$('#bannerlist').attr('class','active li_active');
})
	
function deleteBanner(bannerid){
	$("input[name=bannerid]").val(bannerid);
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
	<div class="page_nav"><p><a href="#">banner图管理</a><i>/</i><span>banner图列表</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">banner图列表列表</span><a href="<%=request.getContextPath() %>/managebackstage/initAddOrUpdateBanner">添加</a></div>
        <div class="sel_box">
        	<form action="<%=request.getContextPath()%>/managebackstage/getBannerList" method="post">
        		<select class="sel" name="type">
	            	<option>全部</option>
	            	<option value="1" <c:if test="${map.type == '1' }">selected="selected"</c:if>>首页</option>
	            	<option value="2" <c:if test="${map.type == '2' }">selected="selected"</c:if>>会员页面</option>
	            </select>
	            <select class="sel" name="status">
	            	<option>全部</option>
	            	<option value="1" <c:if test="${map.status == '1' }">selected="selected"</c:if>>正常</option>
	            	<option value="0" <c:if test="${map.status == '0' }">selected="selected"</c:if>>删除</option>
	            </select>
	            <input type="submit" value="搜索" class="find_btn"  />
            </form>
            <div class="clear"></div>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
                	<td>图片</td>
                    <td>链接地址</td>
                    <td>所属页面</td>
                    <td width="20%">描述</td>
                    <td>修改时间</td>
                    <td>创建时间 </td>
                    <td>状态</td>
                    <td>操作</td>
                </tr>
                <c:forEach items="${list }" var="li">
                	<tr>
	                	<td><img src="${li.imgurl}" alt="" width="480" height="270" /></td>
	                    <td>${li.httpurl }</td>
	                    <td><c:choose>
	                    	<c:when test="${li.type == 1 }">
	                    		首页banner图
	                    	</c:when>
	                    	<c:otherwise>
	                    		会员页面banner图
	                    	</c:otherwise>
	                    </c:choose></td>
	                    <td>${li.description }</td>
	                    <td>${li.updatetime }</td>
	                    <td>${li.createtime }</td>
	                    <c:choose>
	                    	<c:when test="${li.status == 1 }">
	                    		<td><i class="red">正常 </i></td>
	                    		<td style="cursor: pointer;" ><a href="javascript:void(0)" onclick="location.href='<%=request.getContextPath() %>/managebackstage/initAddOrUpdateBanner?bannerid=${li.bannerid }'" class="blue">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="deleteBanner('${li.bannerid}')" class="blue">删除</a></td>
	                    	</c:when>
	                    	<c:otherwise>
	                    		<td></td>
	                    		<td></td>
	                    	</c:otherwise>
	                    </c:choose>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div>
    </div>
</div>
<form action="<%=request.getContextPath() %>/managebackstage/deleteBanner" id="userform" method="post">
     <input type="hidden" name="bannerid" />
</form>

</body>
</html>
