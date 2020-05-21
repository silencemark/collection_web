<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>查看管理员-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  

</head>
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
	$('#adminuser').parent().parent().find("span").attr("class","bg_hidden");
	$('#adminuser').attr('class','active li_active');
})
</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
    <div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">管理员管理</a><i>/</i><span>查看管理员</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">查看管理员</span><a href="javascript:void(0)" onclick="deleteuser('${userInfo.userid}')">删除</a><a href="<%=request.getContextPath()%>/managebackstage/initAddAdminUser?userid=${userInfo.userid}">修改</a></div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td class="l_text" width="140">头像</td>
                    <td width="30" class="img_td"><div class="img"><img src="${userInfo.headimage}" width="30" height="30" /></div></td>
                    <td class="gray_word">${userInfo.realname}</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">性别</td>
                    <c:choose>
                    	<c:when test="${userInfo.sex==1}">
                    		<td colspan="2" >男</td>
                    	</c:when>
                    	<c:when test="${userInfo.sex==0}">
                    		<td colspan="2" >女</td>
                    	</c:when>
                    	<c:otherwise>
                    		<td colspan="2" ></td>
                    	</c:otherwise>
                    </c:choose>
                    
                </tr>
                <tr>
                    <td class="l_text" width="140">出生年月</td>
                    <td colspan="2" >${userInfo.birthday}</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">手机号</td>
                    <td colspan="2" >${userInfo.phone}</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">电子邮件地址</td>
                    <td colspan="2" >${userInfo.email}</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">地址</td>
                    <td colspan="2" >${userInfo.address}</td>
                </tr>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
function deleteuser(userid){
	swal({
		title : "",
		text : "确定删除？",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath()%>/managebackstage/updateAdminUser",
			data:"userid="+userid+"&delflag=1&realname=${userInfo.realname}",
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text : data.message,
						type : "error",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
					})
				}else{
					swal({
						title : "",
						text : data.message,
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						location.href="<%=request.getContextPath()%>/managebackstage/getAdminUserList";
					})
				}
			}
		})
	})
}
</script>
</body>
</html>