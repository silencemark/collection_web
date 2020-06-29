<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>会员实名认证管理-管理方后台</title>
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
	$('#certificationlist').parent().parent().find("span").attr("class","bg_hidden");
	$('#certificationlist').attr('class','active li_active');
})


//隐藏窗口
function checkHide(num){
	if(num == 1) {
		$('#updateform').submit();
	}else {
		$("#updatediv").hide();	
		$(".div_mask").css("display","none");
		$("#certificationid").val("");
		$("#message").val("");
	}
}

//显示隐藏窗口
function update(certificationid){
	$("#updatediv").show();	
	$(".div_mask").css("display","block");
	$("#certificationid").val(certificationid);
}

</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">会员实名认证管理</a><i>/</i><span>会员实名认证列表</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">会员实名认证管理列表</span></div>
        <div class="sel_box">
        	<form action="<%=request.getContextPath()%>/managebackstage/getCertificationList" method="post">
        		<input type="text" class="text" placeholder="请输入会员昵称" name="nickname" value="${map.nickname}"/>
        		<input type="text" class="text" placeholder="请输入会员实名姓名" name="realname" value="${map.realname}"/>
        		<input type="text" class="text" placeholder="请输入会员身份证" name="idcard" value="${map.idcard}"/>
	            <input type="submit" value="搜索" class="find_btn"  />
            </form>
            <div class="clear"></div>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
                	<td>用户昵称</td>
                    <td>国籍</td>
                    <td>真实姓名</td>
                    <td>身份证号</td>
                    <td>审核原因</td>
                    <td>状态</td>
                    <td>修改时间</td>
                    <td>操作</td>
                </tr>
                <c:forEach items="${list }" var="li">
                	<tr>
	                	<td>${li.nickname }</td>
	                    <td>${li.nationality }</td>
	                    <td>${li.realname }</td>
	                    <td>${li.idcard }</td>
	                    <td>${li.message }</td>
	                    <c:choose>
	                    	<c:when test="${li.status == 0 }">
	                    		<td><i class="gray">待审核 </i></td>
	                    	</c:when>
	                    	<c:when test="${li.status == 1 }">
	                    		<td><i class="green">审核通过 </i></td>
	                    	</c:when>
	                    	<c:otherwise>
	                    		<td><i class="red">审核拒绝</i></td>
	                    	</c:otherwise>
	                    </c:choose>
	                    <td>${li.updatetime }</td>
	                    <td>
	                    <c:choose>
	                    	<c:when test="${li.status == 0 }">
	                    		<a href="javascript:void(0)" onclick="update('${li.certificationid}')" class="blue">审核</a>
	                    	</c:when>
	                    	<c:otherwise>
	                    		--
	                    	</c:otherwise>
	                    </c:choose></td>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div>
    </div>
</div>

<div class="div_mask" style="display:none;"></div>

<div class="tc_changetext"  id="updatediv"  style="display:none;top: 40%;left: 40%; width: 650px;">
	<div class="tc_title"><span>审核实名认证</span><a href="#" onclick="checkHide(0)">×</a></div>
    <form action="<%=request.getContextPath() %>/managebackstage/updateCertification" id="updateform" method="post">
    <input type="hidden" name="certificationid"  id="certificationid"/>
    <div class="box">
        <span>审核：</span>
        <select class="sel" name="status" name="status">
           	<option value="1">审核通过</option>
           	<option value="2">审核拒绝</option>
         </select>
        <div class="clear"></div>
        <span>拒绝原因</span>
        <textarea placeholder="请输入拒绝原因" maxlength="800" cols="43" style="border: 1px solid #eee;" rows="2" name="message" id="message"></textarea>
        <div class="clear"></div>
    </div>
    </form>
    <div class="tc_btnbox"><a href="#" class="bg_gay2" onclick="checkHide(0)">取消</a><a href="#"  class="bg_yellow" onclick="checkHide(1)">确认</a></div>
</div>
</body>
</html>
