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
</script>
</head>

<body>
<jsp:include page="top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="left.jsp" ></jsp:include>
	<div class="page_nav"><p><span>首页</span></p></div>
    <div class="index_top">    	
        <div class="wid_01">
            <div class="cp_num">
                <span><i>目前市场金额</i><em class="yellow">200000</em><i>元</i><div class="clear"></div></span>
                <span><i>多余市场金额</i><em class="yellow">50000</em><i>元</i><div class="clear"></div></span>
            </div>
        </div>
    	<div class="wid_02">
            <div class="user_num">
                <span><i>已登录人员</i><em class="yellow">500</em><i>人</i><div class="clear"></div></span>
                <span><i>未登陆人员</i><em class="yellow">200</em><i>人</i></span>
            </div> 
        </div>
        <div class="clear"></div>
    </div>    
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">消息</span></div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td2">
                	<td width="60%">摘要</td>
                    <td width="30%">时间</td>
                    <td>操作</td>
                </tr>
                <c:forEach items="${noticeList}" var="item">
	                <tr>
	                	<td>${item.content}</td>
	                    <td>${item.createtime}</td>
	                    <td><a href="javascript:void(0)" class="link">查看</a></td>
	                    <%-- <c:choose>
	                    	<c:when test="${item.type==1}">
	                    		<td><a href="javascript:void(0)" onclick="linkurlpower(1,'${item.companyid}')" class="link">查看</a></td>
	                    	</c:when>
	                    	<c:otherwise>
	                    		<td><a href="javascript:void(0)" onclick="linkurlpower(2,'${item.reasonid}')" class="link">查看</a></td>
	                    	</c:otherwise>
	                    </c:choose> --%>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination-->
    </div>
</div>
<form action="" id="linkform" method="post">
	
</form>
<script type="text/javascript">
function linkurlpower(type,id){
	if(type==1){
		$("#linkform").attr("action","<%=request.getContextPath() %>/managebackstage/getCompanyPower");
		$("#linkform").html("<input type=\"hidden\" name=\"companyid\" value=\""+id+"\"/>");
		$("#linkform").submit();
	}else{
		$("#linkform").attr("action","<%=request.getContextPath() %>/managebackstage/getUpdateCloudCapacityDetail");
		$("#linkform").html("<input type=\"hidden\" name=\"capacityid\" value=\""+id+"\"/>");
		$("#linkform").submit();
	}
}
</script>
</body>
</html>