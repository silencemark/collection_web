<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>采购(入库)单列表</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
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
			$(".head_box .user_box .name").removeClass("name_border");//移除\
			$(".head_box .user_box .box").hide();
		  }
		);			
});
$(document).ready(function(){
	$('#company').parent().parent().find("span").attr("class","bg_hidden");
	$('#company').attr('class','active li_active');
})
</script>
<body>
<jsp:include page="../top.jsp"></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp"></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList">企业信息列表</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyInfo?companyid=${map.companyid}">公司信息</a><i>/</i><span>采购(入库)单列表</span></p></div>
    <div class="right_page">
        <div class="page_tab">
            <div class="sel_box">
            	<form action="<%=request.getContextPath() %>/managebackstage/getManagePurchaseOrder" id="searchform">
            	<input type="hidden" name="companyid" value="${map.companyid }"/>
                <input type="text" class="text_time" placeholder="开始时间" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${map.starttime}" name="starttime"/>
                <span>-</span>
                <input type="text" class="text_time m_r20" placeholder="结束时间" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${map.endtime}" name="endtime"/>
                <select class="sel" name="status">
	                <option value="">全部</option>
	                <c:choose>
	                	<c:when test="${map.status=='0'}">
	                		<option value="0" selected="selected">待处理</option>
	                		<option value="1">已处理</option>
	                	</c:when>
	                	<c:when test="${map.status=='1'}">
	                		<option value="0">待处理</option>
	                		<option value="1" selected="selected">已处理</option>
	                	</c:when>
	                	<c:otherwise>
		                	<option value="0">待处理</option>
		                	<option value="1">已处理</option>
	                	</c:otherwise>
	                </c:choose>
                </select>
                <input type="submit" value="" class="find_btn"  />
                </form>
                <div class="clear"></div>
            </div>
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr class="head_td">
                        <td width="20%">采购(入库)单编号</td>
                        <td width="12%">采购人</td>
                        <td width="18%">填写时间</td>
                        <td width="12%">项数</td>
                        <td width="12%">金额</td>
                        <td width="12%">状态</td>
                        <td>操作</td>
                    </tr>
                    <c:forEach items="${orderlist }" var="item">
                    	<tr>
	                    	<tr>
	                    	<td>${item.orderno}</td>
	                        <td>${item.createname}</td>
	                        <td>${item.createtime.substring(0,16)}</td>
	                        <td>${item.maternum}项</td>
	                        <td>￥${item.materialprice}</td>
	                        <c:choose>
	                        	<c:when test="${item.status==1}">
	                        		<c:choose>
	                        			<c:when test="${item.result == 1 }"><td class="green">同意</td></c:when>
	                        			<c:otherwise><td class="red">拒绝</td></c:otherwise>
	                        		</c:choose>
	                        	</c:when>
	                        	<c:otherwise>
	                        		<td class="red">待处理</td>
	                        	</c:otherwise>
	                        </c:choose>
	                        <td>
		                        <a href="<%=request.getContextPath() %>/pc/getPurchaseInfo?orderid=${item.orderid}&forwarduserid=${item.forwarduserid}" class="link">查看</a>
	                        </td>
	                    </tr>
                    </c:forEach>
                </table>
            </div>
            <div id="Pagination" style="width:450px;">${pager }</div><!--动态的获取pagination的宽度赋值给Pagination-->
        </div>
    </div>
    <div class="clear"></div>
</div>
</body>
</html>