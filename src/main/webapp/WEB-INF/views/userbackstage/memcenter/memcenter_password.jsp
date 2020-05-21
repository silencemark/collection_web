<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>个人中心—重置密码</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
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
				$(".head_box .user_box .name").removeClass("name_border");//移除\
				$(".head_box .user_box .box").hide();
			  }
			);			
});
	
	
function updataPassword(){
	var  password="",
		 new_password="",
		 new_passwords="",
		 mess="",
		 num = 1;
	password = $("#password").val();
	new_password = $("#new_password").val();
	new_passwords = $("#new_passwords").val();
	if(password == ""){
		mess="原密码不能为空";
		num = 0;
	}else if(new_password == ""){
		mess="新密码不能为空";
		num = 0;
	}else if(new_passwords == ""){
		mess="新密码不能为空";
		num = 0;
	}else if(new_password != new_passwords){
		mess="两次密码不一致";
		num = 0;
	}
	
	if(num == 1){
		location.href="/userbackstage/updataPassword?password="+password+"&new_password="+new_password;
	}else{
		swal({
			title : "",
			text : mess,
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
	}
}

function onloadData(){
	if('${errors}' != ''){
		swal({
			title : "",
			text : "${errors}",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
	}
}
$(document).ready(function(){
	$('#nav_password').attr('class','active');$('#nav_password').parent().parent().show();
})
</script>
</head>

<body onload="onloadData()">
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">个人中心</a><i>/</i><span>重置密码</span></p></div>  
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">重置密码</span></div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size: 12px;">
                <tr class="noneborder">
                    <td class="l_text" width="140">原密码</td>
                    <td colspan="2" ><input type="text" class="text" placeholder="请输入原密码" id="password"/></td>
                </tr>  
                <tr class="noneborder">
                    <td class="l_text" width="140">新密码</td>
                    <td colspan="2" ><input type="password" class="text" placeholder="请输入长度6-16位新密码" id="new_password"/></td>
                </tr>  
                <tr class="noneborder">
                    <td class="l_text" width="140">确认密码</td>
                    <td colspan="2" ><input type="password" class="text" placeholder="请确认新密码" id="new_passwords"/></td>
                </tr>    
                <tr class="foot_td">
                	<td>&nbsp;</td>
                    <td colspan="2"><a href="#" class="a_btn wid_01 bg_yellow" onclick="updataPassword()">重置密码</a><a href="/userbackstage/getOrganizeByUser" class="a_btn bg_gay2">取消</a></td>
                </tr>
            </table>
        </div>
    </div>
</div>


</body>
</html>
