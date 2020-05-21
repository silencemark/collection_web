<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>重置密码</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript">

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
		 $.ajax({
		 		type:'post',
		 		dataType:'json',
		 		url:"<%=request.getContextPath() %>/pc/updateUserPassWordInfo?userid=${userInfo.userid}&password="+password+"&new_password="+new_password,
		 		success:function(data){
		 			if(data.status==1){
		 				swal({
		 					title : "",
		 					text : "修改失败",
		 					type : "error",
		 					showCancelButton : false,
		 					confirmButtonColor : "#ff7922",
		 					confirmButtonText : "确认",
		 					cancelButtonText : "取消",
		 					closeOnConfirm : true
		 				}, function(){
		 					
		 				});
		 			}else{
		 				swal({
		 					title : "",
		 					text : "修改成功",
		 					type : "success",
		 					showCancelButton : false,
		 					confirmButtonColor : "#ff7922",
		 					confirmButtonText : "确认",
		 					cancelButtonText : "取消",
		 					closeOnConfirm : true
		 				}, function(){
		 					location.href="<%=request.getContextPath() %>/pc/login";
		 				});
		 			}
		 		}
		 });
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
$(document).ready(function(){
	$('.link ul').find("li").attr('class','');
	$('#usercenter').attr('class','active');
});
</script>
</head>

<body >
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<div class="left_menu">
    <jsp:include page="../user.jsp"></jsp:include><!-- 头像and编辑 -->
        <div class="menu_name">个人中心</div>
        <ul class="member_li">
        	<li><a href="<%=request.getContextPath() %>/pc/memcenter_info" class="bg_01">个人信息</a></li>
            <li><a href="<%=request.getContextPath() %>/pc/notice_list" class="bg_02">系统公告</a></li>
            <li class="active"><a href="<%=request.getContextPath() %>/pc/password" class="bg_03">重置密码</a></li>
        </ul>
    </div>
    <div class="right_page">
    	<div class="page_name"><span>重置密码</span></div>
        <div class="page_tab2">
        	<div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">
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
                        <td colspan="2"><a href="#" class="a_btn wid_01 bg_yellow" onclick="updataPassword()">重置密码</a><a  href="<%=request.getContextPath() %>/pc/memcenter_info"  class="a_btn bg_gay2">取消</a></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>
</body>
</html>
