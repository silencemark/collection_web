<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>OA办公扩容-使用方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/sweetalert/dist/sweetalert-dev.js"></script> 
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
				$(".head_box .user_box .name").removeClass("name_border");//移除\
				$(".head_box .user_box .box").hide();
			  }
			);			
});
	
	$(document).ready(function(){
		$('#nav_capacity').attr('class','active');$('#nav_capacity').parent().parent().show();
	})
</script>
</head>

<body>
<jsp:include page="../top.jsp" flush="true"></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" flush="true"></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">OA办公</a><i>/</i><span>扩容</span></p></div>  
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1">扩容</span><a href="/userbackstage/getCloudCapacity" class="wid_01">扩容记录</a></div>
        <div class="enlarge">
        	<input type="text" class="text" placeholder="申请大小" id="memory" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="5"/><span>G</span>
            <input type="button" value="提交" onclick="addMemory()" class="btn" />
            <div class="clear"></div>
        </div>
    </div>
</div>
</body>
<script>
	function addMemory(){
		var param = new Object();
		param.memory = $('#memory').val();
		
		if(parseInt(param.memory)>10000){
			swal({
			    title: "提示",
			    text: "每次扩容大小不能超过 10000 G！",
			    type: "warning",
			    showCancelButton: false,
			    confirmButtonColor: "#ff7922",
			    confirmButtonText: "确定",
			    closeOnConfirm: true
			}, function(){
			});
			return false;
		}
		if(param.memory == ""){
			swal({
			    title: "提示",
			    text: "申请大小不能为空！",
			    type: "warning",
			    showCancelButton: false,
			    confirmButtonColor: "#ff7922",
			    confirmButtonText: "确定",
			    closeOnConfirm: true
			}, function(){
			});
			return false;
		}
		
		$.ajax({
			url:"/userbackstage/insertCloudCapacity",
			type:"post",
			data:param,
			success:function(resultMap){
				if(resultMap.status == 0 || resultMap.status == '0'){
					swal({
					    title: "提示",
					    text: "申请成功！",
					    type: "success",
					    showCancelButton: true,
					    confirmButtonColor: "#ff7922",
					    confirmButtonText: "确定",
					    cancelButtonText: "取消",
					    closeOnConfirm: true
					}, function(){
						location.href="/userbackstage/getCloudCapacity";
					});
				}else{
					swal({
					    title: "提示",
					    text: "申请失败！",
					    type: "error",
					    showCancelButton: false,
					    confirmButtonColor: "#ff7922",
					    confirmButtonText: "确定",
					    closeOnConfirm: true
					}, function(){
					});
				}
			}
		});
		
	}
</script>
</html>
