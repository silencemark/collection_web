<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>查看管理日志-管理方后台</title>
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
function exportexcel(){
	$.ajax({
        type: "POST",
        url: "<%=request.getContextPath() %>/managebackstage/exportLogList",
        data: $('#searchform').serializeArray(),
        success: function(data){
        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
        }
    });
}
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
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">系统设置</a><i>/</i><span>查看管理日志</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">查看管理日志</span><a href="javascript:void(0)" onclick="exportexcel();">导出</a></div>
         <div class="sel_box">
            <form action="<%=request.getContextPath() %>/managebackstage/getLogList" method="post" id="searchform">
            <input type="text" class="text" placeholder="关键字（操作员/描述）" name="searchcontent" value="${map.searchcontent}"/>
             <select class="sel" name="model">
             	<option value="">请选择</option>
             	<c:forEach items="${modellist}" var="item">
             		<c:choose>
             			<c:when test="${map.model==item.model}">
             			<option value="${item.model}" selected="selected">${item.model}</option>
             			</c:when>
             			<c:otherwise>
             			<option value="${item.model}">${item.model}</option>
             			</c:otherwise>
             		</c:choose>
            		
            	</c:forEach>
             </select>
             <input onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="starttime" value="${map.starttime}"  class="text2" type="text" placeholder="开始时间">
             <input onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="endtime" value="${map.endtime}"  class="text2" type="text" placeholder="结束时间">
            <input type="submit" value="搜索" class="find_btn" />
            </form>
            <div class="clear"></div>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
                	<td width="15%">操作人员</td>
                    <td width="70%">操作描述</td>
                    <td>时间</td>
                </tr>
                <c:forEach items="${loglist}" var="item">
                	<tr>
	                	<td>${item.realname}</td>
	                    <td>【${item.model}】${item.content}</td>
	                    <td>${item.createtime}</td>
	                </tr>
                </c:forEach>
             </table>
         </div>
         <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination-->
    </div>
</div>
</body>
</html>