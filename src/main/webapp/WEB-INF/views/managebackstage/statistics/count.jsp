<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>统计-管理方后台</title>
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
	$('#statistics').parent().parent().find("span").attr("class","bg_hidden");
	$('#statistics').attr('class','active li_active');
})
</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><span>统计</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">统计</span></div>
      	<div class="skydrive_ico">
        	<ul>
            	<li><a href="<%=request.getContextPath()%>/managebackstage/getStatisticsToday"><b class="bg_green2"><img src="../userbackstage/images/page/total_ico01.png" width="32" height="32" /></b></a><span>日活统计</span></li>
                <li><a href="<%=request.getContextPath()%>/managebackstage/getNewUserStatistics"><b class="bg_purple"><img src="../userbackstage/images/page/total_ico02.png" width="32" height="32" /></b></a><span>新增用户统计</span></li>
                <li><a href="<%=request.getContextPath()%>/managebackstage/getOrderStatistics"><b class="bg_yellow"><img src="../userbackstage/images/page/total_ico03.png" width="32" height="32" /></b></a><span>订单统计</span></li>
                <li><a href="<%=request.getContextPath()%>/managebackstage/getWaitSellOrderStatistics"><b class="bg_blue"><img src="../userbackstage/images/page/total_ico04.png" width="32" height="32" /></b></a><span>待出售订单统计</span></li>
            </ul>
            <div class="clear"></div>
        </div>
    </div>
</div>
</body>
</html>