<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>OA办公企业简报-使用方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/sweetalert/dist/sweetalert-dev.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/oa/oa_briefinglist.js"></script>
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
	$(document).ready(function(){
		$('#nav_brief').attr('class','active');$('#nav_brief').parent().parent().show();
	})
</script>
</head>

<body>
<jsp:include page="../top.jsp" flush="true"></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" flush="true"></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">OA办公</a><i>/</i><a href="/userbackstage/getCompanyModuleList">企业简报栏目</a><i>/</i><span>企业简报列表</span></p></div>  
    <input type="hidden" id="moduleid" value="${map.moduleid }"/>
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1">企业简报列表</span><a href="/userbackstage/intoBriefingEditPage?moduleid=${map.moduleid }" class="wid_01">新建简报</a><input type="button" value="搜索" onclick="queryBriefList()" class="btn" /><input type="text" class="text" placeholder="请输入关键字" id="title"/></div>
            	<div class="briefing_list">
                	<ul id="briefing_list">
<!--                     	<li> -->
<!--                         	<div class="name"><a href="#"></a><i>2016-06-02 10:30</i></div> -->
<!--                             <div class="txt">企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情</div> -->
<!--                         </li> -->
                    </ul>
                <div id="Pagination" style="width:450px;">
<!--                 <div class="pagination"><a href="#" class="prev_first">首页</a><a href="#" class="prev">上一页</a><a href="#" class="sp">1</a><a href="#">2</a><a class="current">3</a><a href="#">4</a><a href="#">5</a><a href="#" class="next">下一页</a><a href="#" class="next_last">尾页</a></div> -->
                </div><!--动态的获取pagination的宽度赋值给Pagination-->
            <div class="clear"></div>
        </div>
    </div>
</div>

</body>
</html>
