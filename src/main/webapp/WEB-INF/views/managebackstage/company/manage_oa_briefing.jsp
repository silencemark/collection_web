<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>OA办公企业简报-使用方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/sweetalert/dist/sweetalert-dev.js"></script> 

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>

<!-- 上传需要js -->      
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.ui.widget.js"></script>
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.iframe-transport.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload.js"></script>  
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-ui.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-process.js"></script>   
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-validate.js"></script>  

<%-- <script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>  --%>
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
		$('#company').parent().parent().find("span").attr("class","bg_hidden");
		$('#company').attr('class','active li_active');
});
	
	function intobriefpage(id){
		location.href="/managebackstage/intoBriefListPage?moduleid="+id+"&companyid=${map.companyid}";
	}
</script>
</head>

<body>
<jsp:include page="../top.jsp" flush="true"></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" flush="true"></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList">企业信息列表</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyInfo?companyid=${map.companyid}">公司信息</a><i>/</i><span>企业简报栏目</span></p></div>
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1">企业简报栏目</span></div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
        		<c:forEach items="${modulelist}" var="item">
                <tr>
                	<td class="img_td" width="30"><div class="img2"><img src="${item.moduleimage}" width="30" height="30" /></div></td>
                    <td width="70%" style="cursor: pointer;" onclick="intobriefpage('${item.moduleid}')">${item.modulename}</td>
                    <td class="t_r"><a href="javascript:void(0)" onclick="intobriefpage('${item.moduleid}')" class="link">查看</a></td>
                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div><!--动态的获取pagination的宽度赋值给Pagination-->
    </div>
</div>
</body>
</html>