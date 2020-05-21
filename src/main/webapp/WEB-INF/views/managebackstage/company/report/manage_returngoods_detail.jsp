<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退货单详情</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

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
			$(".head_box .user_box .name").removeClass("name_border");//移除
			$(".head_box .user_box .box").hide();
		  }
		);			
});
$(document).ready(function(){
$('#company').parent().parent().find("span").attr("class","bg_hidden");
$('#company').attr('class','active li_active');
})

</script>
<body onload="onloaddata()">
<jsp:include page="../../top.jsp"></jsp:include>
<div class="main_page">
	<jsp:include page="../../left.jsp"></jsp:include>
    <div class="right_page">
   	  <div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList">企业信息列表</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyInfo?companyid=${map.companyid}">公司信息</a><i>/</i><a href="javascript:void(0)" onclick="window.history.go(-1)">库存分析</a></a><i>/</i><span>退货单详情</span></p></div>
   	  </div>
        <div class="page_tab2">  
          <c:if test="${returnInfo.result==1}">
          <div class="check_state"><img src="../userbackstage/images/pc_page/agree_img.png" /></div>        
          </c:if>  
          <c:if test="${returnInfo.result==0}">
          <div class="check_state"><img src="../userbackstage/images/pc_page/agree_img2.png" /></div>        
          </c:if>  
          <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                    	<td class="l_name">退货单编号</td>
                        <td>${returnInfo.orderno}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">店面</td>
                        <td>${returnInfo.organizename}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">物料明细</td>
                        <td class="mx_tab">
                        	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="mx_td">
                            	<tr class="gray_bg">
                                	<td width="15%">类别</td>
                                    <td width="15%">名称</td>
                                    <td width="15%">数量</td>
                                    <td width="15%">总价</td>
                                    <!-- <td width="15%">单位</td>   -->   
                                    <td width="20%">规格</td>    
                                </tr>
                                <c:forEach items="${materiallist}" var="item">
                                <tr>
                                	<td>${item.type}</td>
                                    <td>${item.name}</td>
                                    <td>${item.num}${item.unit}</td>
                                    <td>${item.sumprice}</td>
                                    <%-- <td>${item.unit}</td> --%> 
                                    <td>${item.specifications}</td>   
                                </tr>
                                </c:forEach>                                
                                <tr class="none_border">
                                	<td colspan="4">共 <i class="yellow">${returnInfo.maternum }</i> 项</td>
                                    <td colspan="3" class="t_r">合计<i class="yellow">￥${returnInfo.materialprice }</i>元</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">申请人</td>
                        <td class="img_td"><div class="img f"><img src="${returnInfo.createheadimage}" width="30" height="30" /></div><i class="i_name">${returnInfo.createname}</i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">审批人</td>
                        <td class="img_td"><div class="img f"><img src="${returnInfo.examineheadimage}" width="30" height="30" /></div><i class="i_name">${returnInfo.examinename }</i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">退货时间</td>
                        <td>${returnInfo.returntime }</td>
                    </tr>
                     <tr>
                    	<td class="l_name">填写时间</td>
                        <td>${returnInfo.createtime }</td>
                    </tr>
                    <tr>

                    	<td class="l_name">抄送人</td>
                        <td class="img_td" id="CCusernames">
                        	<c:forEach items="${returnInfo.ccuserlist }" var="map">
                        		<div class="img f"><img src="${map.headimage }" width="30" height="30" /></div><i class="i_name" >${map.realname }</i>
                        	</c:forEach>
                        </td>
                    </tr>
                    <tr>

                    	<td class="l_name">转发人</td>
                        <td class="img_td">
                        	<i class="i_name" id="sendname"></i>
                        </td>
                    </tr>
                    
                    <tr>

                    	<td class="l_name">审批时间</td>
                        <td>${returnInfo.updatetime }</td>
                    </tr>
                    <c:choose>
                    	<c:when test="${returnInfo.examineuserid==userInfo.userid && returnInfo.status==0}">
	                    <tr class="none_border">
	                    	<td class="l_name2">审批人意见</td>
	                        <td><textarea class="text_area" placeholder="请输入审批人意见，最多允许输入800字符" maxlength="800" id="opinion"></textarea></td>
	                    </tr>
	                    <tr>
	                    	<td>&nbsp;</td>
	                        <td><a href="javascript:void(0)" onclick="examineorder(1)" class="a_btn bg_yellow">同意</a>
	                        <a href="javascript:void(0)" onclick="examineorder(0)" class="a_btn bg_gay2">拒绝</a></td>
	                    </tr>
                    	</c:when>
                    	<c:otherwise>
                    	<tr class="none_border">
	                    	<td class="l_name2">审批人意见</td>
	                        <td>${returnInfo.opinion }</td>
	                    </tr>
                    	</c:otherwise>
                    </c:choose>
                    
                </table>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>

</body>
</html>