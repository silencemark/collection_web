<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户信息列表-管理方后台</title>
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
		$('#userlist').parent().parent().find("span").attr("class","bg_hidden");
		$('#userlist').attr('class','active li_active');
	})
function updateUser(userid, status, delflag, sta){
	$("input[name=userid]").val(userid);
	var message="确认冻结？";
	if(sta == 1){
		message="确认解禁？";
		$("input[name=status]").val(status);
	} else if(sta == 2){
		message="确认解冻？";
		$("input[name=status]").val(status);
	} else {
		if(delflag == 1){
			message="确认删除？";
			$("input[name=delflag]").val(delflag);
		}
		if (status == 1) {
			message="确认禁用？";
			$("input[name=status]").val(status);
		}
		if (status == 2) {
			message="确认冻结？";
			$("input[name=status]").val(status);
		}
	}
	swal({
		title : "",
		text : message,
		type : "error",
		showCancelButton : true,
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
	<div class="page_nav"><p><span>用户信息列表</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">用户信息列表</span></div>
        <div class="sel_box">
        	<form action="<%=request.getContextPath()%>/managebackstage/getUserList" method="post">
        		<input type="text" class="text" placeholder="请输入手机号" name="phone" value="${map.phone}"/>
	            <input type="text" class="text" placeholder="请输入用户昵称" name="nickname" value="${map.nickname }"/>
	            <input type="text" class="text" placeholder="请输入用户真实姓名" name="realname" value="${map.realname }"/>
	            <select class="sel" name="status">
	            	<option>全部</option>
	            	<option value="0" <c:if test="${map.status == '0' }">selected="selected"</c:if>>正常</option>
	            	<option value="1" <c:if test="${map.status == '1' }">selected="selected"</c:if>>禁用</option>
	            	<option value="2" <c:if test="${map.status == '2' }">selected="selected"</c:if>>冻结</option>
	            </select>
	            <input type="submit" value="搜索" class="find_btn"  />
            </form>
            <div class="clear"></div>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
                	<td width="12%">昵称</td>
                    <td width="12%">联系手机</td>
                    <td width="12%">头像</td>
                    <td width="12%">可兑换资产</td>
                    <td width="10%">总资产收益 </td>
                    <td width="12%">真实姓名</td>
                    <td width="10%">填写时间</td>
                    <td width="12%">状态</td>
                    <td>操作</td>
                </tr>
                <c:forEach items="${list }" var="li">
                	<tr>
	                	<td>${li.nickname }</td>
	                    <td>${li.phone }</td>
	                    <td><img src="${li.headimage }" alt="" style="width:48px; height:48px; border-radius:48px; overflow:hidden; box-shadow:0px 0px 1px 0px #ddd; margin:0px auto;"  /></td>
	                    <td>${li.sumprofit }</td>
	                    <td>${li.sumassets }</td> 
	                    <td>${li.realname }</td>
	                    <td>${li.createtime }</td>
	                    <c:choose>
	                    	<c:when test="${li.status == 0 }">
	                    		<td><i class="red">正常 </i></td>
	                    		<td style="cursor: pointer;" ><a href="javascript:void(0)" onclick="updateUser('${li.userid}',1,0,0)"  class="blue">禁用</a>&nbsp;&nbsp;<a href="javascript:void(0)"  onclick="updateUser('${li.userid}',2,0,0)" class="blue">冻结</a>&nbsp;&nbsp;<a href="javascript:void(0)"  onclick="updateUser('${li.userid}','',1,0)" class="blue">删除</a></td>
	                    	</c:when>
	                    	<c:when test="${li.status == 1 }">
	                    		<td><i class="red"> 禁用</i></td>
	                    		<td style="cursor: pointer;" ><a href="javascript:void(0)"  onclick="updateUser('${li.userid}',0,0,1)" class="blue">解禁</a></td>
	                    	</c:when>
	                    	<c:otherwise>
	                    		<td><i class="greed"> 冻结</i></td>
	                    		<td style="cursor: pointer;" ><a href="javascript:void(0)"  onclick="updateUser('${li.userid}',0,0,2)"class="blue">解冻</a></td>
	                    	</c:otherwise>
	                    </c:choose>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div>
    </div>
</div>
<form action="<%=request.getContextPath() %>/managebackstage/updateUserStatus" id="userform" method="post">
     <input type="hidden" name="userid" />
     <input type="hidden" name="status" />
     <input type="hidden" name="delflag" />
</form>

</body>
</html>
