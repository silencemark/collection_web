<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>首页-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
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
                <span><i>市场总流水金额</i><em class="yellow">${indexInfo.sumprice}</em><i>元</i><div class="clear"></div></span>
                <span><i>系统出售总金额</i><em class="yellow">${indexInfo.sysprice}</em><i>元</i><div class="clear"></div></span>
            </div>
        </div>
    	<div class="wid_02">
            <div class="user_num">
                <span><i>总注册人数</i><em class="yellow">${indexInfo.usernum}</em><i>人</i><div class="clear"></div></span>
                <span><i>有效用户人数</i><em class="yellow">${indexInfo.effectiveusernum}</em><i>人</i></span>
            </div> 
        </div>
        <div class="clear"></div>
    </div>    
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">消息</span></div>
        <div class="sel_box">
        	<form action="<%=request.getContextPath()%>/managebackstage/index" method="post">
        		<input type="text" class="text" placeholder="请输入标题" name="title" value="${map.title}"/>
	            <input type="text" class="text" placeholder="请输入内容" name="message" value="${map.message }"/>
	            <select class="sel" name="status">
	            	<option>全部</option>
	            	<option value="1" <c:if test="${map.status == '1' }">selected="selected"</c:if>>成功</option>
	            	<option value="0" <c:if test="${map.status == '0' }">selected="selected"</c:if>>失败</option>
	            </select>
	            <input type="text" class="text" placeholder="请输入订单日期"  name="createtime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${map.createtime}"/></td>
	            <input type="submit" value="搜索" class="find_btn"  />
            </form>
            <div class="clear"></div>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td2">
                	<td>标题</td>
                    <td>内容</td>
                    <td>消息类别</td>
                    <td>时间</td>
                </tr>
                <c:forEach items="${noticeList}" var="item">
	                <tr>
	                	<td>${item.title}</td>
	                    <td>${item.message}</td>
	                    <td>${item.status == 1?'成功':'失败'}</td>
	                    <td>${item.createtime}</td>
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