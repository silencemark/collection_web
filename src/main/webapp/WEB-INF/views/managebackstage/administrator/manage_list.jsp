<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>首页-管理方后台</title>
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
function exportexcel(){
	$.ajax({
        type: "POST",
        url: "<%=request.getContextPath() %>/managebackstage/exportCompanyList",
        data: $('#searchform').serializeArray(),
        success: function(data){
        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
        }
    });
}
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
    <div class="page_nav"><p><a href="#">首页</a><i>/</i><span>管理员管理</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">管理员管理列表</span><a href="<%=request.getContextPath() %>/managebackstage/initAddAdminUser">添加</a></div>
        <div class="sel_box">
        	<form action="<%=request.getContextPath() %>/managebackstage/getAdminUserList" method="post">
            <input type="text" class="text" placeholder="请输入关键字" name="realname" value="${map.realname}"/>
            <input type="submit" value="搜索" class="find_btn"  />
            <div class="clear"></div>
            </form>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="border-top:1px solid #eee;">
                <c:forEach items="${userlist}" var="item">
                	<tr>
	                	<td class="img_td2" width="30"><div class="img"><img src="${item.headimage}" width="30" height="30" /></div></td>
	                    <td width="12%">${item.realname}</td>
	                    <td>${item.phone}</td>
	                    <td width="12%"><a href="<%=request.getContextPath() %>/managebackstage/getAdminUserInfo?userid=${item.userid}" class="blue">查看</a></td>
	                   
	                    <c:choose>
	                    	<c:when test="${item.status==1}">
	                    		<td width="12%"><a href="javascript:void(0)" class="red" onclick="changestatus(0,'${item.userid}','${item.realname}')">禁用</a></td>
	                    	</c:when>
	                    	<c:otherwise>
	                    		<td width="12%"><a href="javascript:void(0)" class="green" onclick="changestatus(1,'${item.userid}','${item.realname}')">启用</a></td>
	                    	</c:otherwise>
	                    </c:choose>
	                    <td width="60"><a href="javascript:void(0)" class="blue" onclick="resetpass('${item.userid}')">密码重置</a></td>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div><!--动态的获取pagination的宽度赋值给Pagination-->
    </div>
</div>
<script type="text/javascript">
function changestatus(status,userid,realname){
	var resultstr="确认启用?";
	if(status==0){
		resultstr="确认禁用?";
	}
	swal({
		title : "",
		text : resultstr,
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
			data:"userid="+userid+"&status="+status+"&realname="+realname,
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
					location.reload();
				}
			}
		})
	})
}

function resetpass(userid){
	swal({
		title : "",
		text : "是否重置密码",
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
			url:"<%=request.getContextPath()%>/managebackstage/resetAdminPass",
			data:"userid="+userid,
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
						text : "重置成功",
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						
					})
				}
			}
		})
	})
}
</script>
</body>
</html>