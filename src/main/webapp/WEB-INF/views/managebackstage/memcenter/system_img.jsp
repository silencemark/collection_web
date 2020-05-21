<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>轮播图设置</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
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
				$(".head_box .user_box .name").removeClass("name_border");//移除
				$(".head_box .user_box .box").hide();
			  }
			);			
});
	
	
	
	$(document).ready(function(){
			$('#system').parent().parent().find("span").attr("class","bg_hidden");
			$('#system').attr('class','active li_active');
	})
	
	function checkShow(obj){
		$(".name").find("a[name='showname']").text("展开");	
		if(	$(obj).parent().next().attr("class") == "img dis"){
			$(".name").next().attr("class","img undis");
			$(obj).parent().next().attr("class","img undis");	
		}else{
			$(".name").next().attr("class","img undis");
			$(obj).parent().next().attr("class","img dis");	
			$(obj).text("收起");
		}
		
	}
	function checkmodel(model){
		location.href="/managebackstage/getManageBannerDetail?model="+model;
	}
</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">系统设置</a><i>/</i><span>轮播图查看</span></p></div>        
    <div class="flash_img">
    	<ul>
        	<li>
            	<div class="name"><span>首页</span><a href="#" class="blue" onclick="checkmodel(1)">编辑</a><a href="#" class="blue" onclick="checkShow(this)" name="showname">展开</a></div>
                <div class="img undis" >
                	<c:forEach items="${datalist }"  var ="list">
                		<c:choose>
                			<c:when test="${list.model == 1 }">
                				<span><img src="${list.imgurl}" width="232" height="104" /></span>
                			</c:when>
                		</c:choose>
                	</c:forEach>
                    <div class="clear"></div>
                </div>
            </li>

            <li>
            	<div class="name"><span>采购管理</span><a href="#" class="blue" onclick="checkmodel(2)">编辑</a><a href="#" class="blue" onclick="checkShow(this)" name="showname">展开</a></div>
                <div class="img undis">
                		<c:forEach items="${datalist }"  var ="list">
                		<c:choose>
                			<c:when test="${list.model == 2 }">
                				<span><img src="${list.imgurl}" width="232" height="104" /></span>
                			</c:when>
                		</c:choose>
                	</c:forEach>
                    <div class="clear"></div>
                </div>
            </li>
            <li>
            	<div class="name"><span>仓库管理</span><a href="#" class="blue" onclick="checkmodel(3)">编辑</a><a href="#" class="blue" onclick="checkShow(this)" name="showname">展开</a></div>
                <div class="img undis">
                	<c:forEach items="${datalist }"  var ="list">
                		<c:choose>
                			<c:when test="${list.model == 3 }">
                				<span><img src="${list.imgurl}" width="232" height="104" /></span>
                			</c:when>
                		</c:choose>
                	</c:forEach>
                    <div class="clear"></div>
                </div>
            </li>
            <li>
            	<div class="name"><span>工作表单</span><a href="#" class="blue" onclick="checkmodel(4)">编辑</a><a href="#" class="blue" onclick="checkShow(this)" name="showname">展开</a></div>
                <div class="img undis">
                	<c:forEach items="${datalist }"  var ="list">
                		<c:choose>
                			<c:when test="${list.model == 4 }">
                				<span><img src="${list.imgurl}" width="232" height="104" /></span>
                			</c:when>
                		</c:choose>
                	</c:forEach>
                    <div class="clear"></div>
                </div>
            </li>
            <li>
            	<div class="name"><span>OA办公</span><a href="#" class="blue" onclick="checkmodel(5)">编辑</a><a href="#" class="blue" onclick="checkShow(this)" name="showname">展开</a></div>
                <div class="img undis">
                	<c:forEach items="${datalist }"  var ="list">
                		<c:choose>
                			<c:when test="${list.model == 5 }">
                				<span><img src="${list.imgurl}" width="232" height="104" /></span>
                			</c:when>
                		</c:choose>
                	</c:forEach>
                	 <div class="clear"></div>
                </div>
            </li>
            <li>
            	<div class="name"><span>店面管理</span><a href="#" class="blue" onclick="checkmodel(6)">编辑</a><a href="#" class="blue" onclick="checkShow(this)" name="showname">展开</a></div>
                <div class="img undis">
                	<c:forEach items="${datalist }"  var ="list">
                		<c:choose>
                			<c:when test="${list.model == 6 }">
                				<span><img src="${list.imgurl}" width="232" height="104" /></span>
                			</c:when>
                		</c:choose>
                	</c:forEach>
                    <div class="clear"></div>
                </div>
            </li>
            <li>
            	<div class="name"><span>KPI星级考核</span><a href="#" class="blue" onclick="checkmodel(7)">编辑</a><a href="#" class="blue" onclick="checkShow(this)" name="showname">展开</a></div>
                <div class="img undis">
                	<c:forEach items="${datalist }"  var ="list">
                		<c:choose>
                			<c:when test="${list.model == 7 }">
                				<span><img src="${list.imgurl}" width="232" height="104" /></span>
                			</c:when>
                		</c:choose>
                	</c:forEach>
                 <div class="clear"></div>
                </div>
            </li>
        </ul>
    </div>
</div>
</body>
</html>
