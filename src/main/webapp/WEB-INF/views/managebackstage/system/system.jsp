<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>系统设置-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>

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
	$('#system').parent().parent().find("span").attr("class","bg_hidden");
	$('#system').attr('class','active li_active');
})
</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><span>系统设置</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">系统设置</span></div>
      	<div class="sys_ico">
        	<ul>
            	<li><a href="<%=request.getContextPath()%>/managebackstage/getManageaAppconfig"><b class="bg_01"><img src="../userbackstage/images/page/sys_ico01.png" width="62" height="62" /></b></a><span>APP应用参数设置</span></li>
                <li><a href="<%=request.getContextPath()%>/managebackstage/getManageBanner"><b class="bg_02"><img src="../userbackstage/images/page/sys_ico02.png" width="62" height="62" /></b></a><span>轮播图设置</span></li>
                <li><a href="<%=request.getContextPath()%>/managebackstage/getSystemBacklist"><b class="bg_03"><img src="../userbackstage/images/page/sys_ico03.png" width="62" height="62" /></b></a><span>意见反馈</span></li>
                <li><a href="<%=request.getContextPath()%>/managebackstage/systemDailyReport"><b class="bg_04"><img src="../userbackstage/images/page/sys_ico04.png" width="62" height="62" /></b></a><span>每日报表</span></li>
                <li><a href="<%=request.getContextPath()%>/managebackstage/getSystemMessage"><b class="bg_05"><img src="../userbackstage/images/page/sys_ico05.png" width="62" height="62" /></b></a><span>系统公告</span></li>
                <li><a href="<%=request.getContextPath()%>/managebackstage/getSystemDict"><b class="bg_06"><img src="../userbackstage/images/page/sys_ico06.png" width="62" height="62" /></b></a><span>字典管理</span></li>
                <li><a href="<%=request.getContextPath()%>/managebackstage/getSystemPctop"><b class="bg_07"><img src="../userbackstage/images/page/sys_ico07.png" width="62" height="62" /></b></a><span>使用方PC头部菜单色设置</span></li>
                <li><a href="<%=request.getContextPath()%>/managebackstage/getLogList"><b class="bg_08"><img src="../userbackstage/images/page/sys_ico08.png" width="62" height="62" /></b></a><span>查看管理日志</span></li>
                <li><a href="<%=request.getContextPath() %>/managebackstage/getSystemDictPower"><b class="bg_09"><img src="../userbackstage/images/page/sys_ico09.png" width="62" height="62" /></b></a><span>设置无权限提示内容</span></li>
            </ul>
            <div class="clear"></div>
        </div>
    </div>
</div>
</body>
</html>