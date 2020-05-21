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
	$('#company').parent().parent().find("span").attr("class","bg_hidden");
	$('#company').attr('class','active li_active');
})
</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><span>企业信息列表</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">企业信息列表</span><a href="javascript:void(0)" onclick="exportexcel();">导出</a></div>
        <div class="sel_box">
        	<form action="<%=request.getContextPath() %>/managebackstage/getCompanyList" method="post" id="searchform">
            <input type="text" class="text" name="companyname" placeholder="请输入关键字" value="${map.companyname}"/>
            <select class="sel" name="condition">
	            <option value="">--请选择查询条件--</option>
	            <option value="professional" <c:if test="${map.condition=='professional'}">selected="selected"</c:if>>专业版</option>
	            <option value="ordinary" <c:if test="${map.condition=='ordinary'}">selected="selected"</c:if>>普通版</option>
	            <option value="notjoinin" <c:if test="${map.condition=='notjoinin'}">selected="selected"</c:if>>普通版未参加</option>
	            <option value="joinin" <c:if test="${map.condition=='joinin'}">selected="selected"</c:if>>普通版已参加</option>
            </select>
            <select class="sel" name="priority">
	            <option value="">--请选择排序方式--</option>
	            <option value="time"<c:if test="${map.priority=='time'}">selected="selected"</c:if>>时间</option>
	            <option value="num" <c:if test="${map.priority=='num'}">selected="selected"</c:if>>人数</option>
            </select>
            <input type="submit" value="搜索" class="find_btn"  />
            <div class="clear"></div>
            </form>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
            		<td width="8%"></td>
                	<td width="25%">公司名称</td>
                    <td width="15%">人数/人</td>
                    <td width="10%">联系人</td>
                    <td width="15%">联系电话</td>
                    <td width="15%">注册时间</td>
                    <td>操作</td>
                </tr>
                <c:forEach items="${companylist}" var="item">
                	<tr>
                		<td>
		               		<c:choose>
		               			<c:when test="${item.edition==1 and item.ispromote==0 }"><div class="off" onclick="updatecompanyispromote(this,1,'${item.companyid}')"><em>未参加</em></div></c:when>
		               			<c:when test="${item.edition==1 and item.ispromote==1 }"><div class="on" onclick="updatecompanyispromote(this,0,'${item.companyid}')"><em>已参加</em></div></c:when>
		               		</c:choose>
                		</td>
	                	<td>${item.companyname}</td>
	                    <td>${item.usernum}</td>
	                    <td>${item.contactperson}</td>
	                    <td>${item.phone}</td>
	                    <td>${item.createtime}</td>
	                    <td><a href="<%=request.getContextPath() %>/managebackstage/getCompanyInfo?companyid=${item.companyid}" class="link">查看</a>
	                    <c:choose>
	                    	<c:when test="${item.status==1}">
	                    		<a href="javascript:void(0)" class="red" onclick="changestatus(0,'${item.companyid}','${item.companyname}')">禁用</a>
	                    	</c:when>
	                    	<c:otherwise>
	                    		<a href="javascript:void(0)" class="green" onclick="changestatus(1,'${item.companyid}','${item.companyname}')">启用</a>
	                    	</c:otherwise>
	                    </c:choose>
	                    </td>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination-->
    </div>
</div>
<script type="text/javascript">
function changestatus(status,companyid,companyname){
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
			url:"<%=request.getContextPath()%>/managebackstage/updateCompany",
			data:"companyid="+companyid+"&status="+status+"&companyname="+companyname,
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

function updatecompanyispromote(obj,ispromote,companyid){
	$.ajax({
		url:"/managebackstage/updateCompanyIsPromote",
		type:"post",
		data:"ispromote="+ispromote+"&companyid="+companyid,
		success:function(data){
			if(data.status == 0){
				if(ispromote == 0){
					$(obj).attr("class","off");
					$(obj).find("em").text("未参加");
					$(obj).attr("onclick","updatecompanyispromote(this,1,'"+companyid+"')");
				}else if(ispromote == 1){
					$(obj).attr("class","on");
					$(obj).find("em").text("已参加");
					$(obj).attr("onclick","updatecompanyispromote(this,0,'"+companyid+"')");
				}
			}
		}
	});
}
</script>
</body>
</html>