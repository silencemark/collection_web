<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>申请记录-管理方后台</title>
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
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><span>申请记录列表</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">申请记录列表</span></div>
        <div class="sel_box">
        	<form action="<%=request.getContextPath()%>/managebackstage/getCloudCapacityList" method="post">
	            <input type="text" class="text" placeholder="请输入公司名称" name="companyname" value="${map.companyname }"/>
	            <input type="text" class="text" placeholder="请输入申请人姓名" name="realname" value="${map.realname }"/>
	            <select class="sel" name="status">
	            	<option>全部</option>
	            	<option value="in" <c:if test="${map.status == 'in' }">selected="selected"</c:if>>待处理</option>
	            	<option value="end" <c:if test="${map.status == 'end' }">selected="selected"</c:if>>已处理</option>
	            </select>
	            <input type="submit" value="搜索" class="find_btn"  />
            </form>
            <div class="clear"></div>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
                	<td width="22%">公司名称</td>
                    <td width="12%">申请人</td>
                    <td width="15%">联系电话</td>
                    <td width="12%">申请大小</td>
                    <td width="18%">填写时间</td>
                    <td width="12%">状态</td>
                    <td>操作</td>
                </tr>
                <c:forEach items="${list }" var="li">
                	<tr>
	                	<td>${li.companyname }</td>
	                    <td>${li.realname }</td>
	                    <td>${li.phone }</td>
	                    <td>${li.memory }</td>
	                    <td>${li.createtime }</td>
	                    <c:choose>
	                    	<c:when test="${li.status == 1 }">
	                    		<td><i class="red">待处理</i></td>
	                    		<td style="cursor: pointer;" onclick="location.href='<%=request.getContextPath() %>/managebackstage/getUpdateCloudCapacityDetail?capacityid=${li.capacityid }'"><a href="javascript:void(0)" class="blue">查看</a></td>
	                    	</c:when>
	                    	<c:otherwise>
	                    		<td><i class="greed">已处理</i></td>
	                    		<td style="cursor: pointer;" onclick="location.href='<%=request.getContextPath() %>/managebackstage/getCloudCapacityDetail?capacityid=${li.capacityid }'"><a href="javascript:void(0)" class="blue">查看</a></td>
	                    	</c:otherwise>
	                    </c:choose>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div><!--动态的获取pagination的宽度赋值给Pagination-->
    </div>
</div>
</body>
</html>
