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
$(document).ready(function(){
	$('#company').parent().parent().find("span").attr("class","bg_hidden");
	$('#company').attr('class','active li_active');
})
function jisuanpercent(){
	var maxmemory=parseFloat('${companyInfo.maxmemory}');
	var shujudmemory=parseFloat('${companyInfo.shujudmemory}');
	var usedmemory=parseFloat('${companyInfo.usedmemory}');
	var remainmemory=parseFloat('${companyInfo.remainmemory}');
	
	if(maxmemory > 0){
		var shuju=((shujudmemory/maxmemory).toFixed(2))*500;
		var qywpd=((usedmemory/maxmemory).toFixed(2))*500;
		$('#shuju').css("width",shuju+"px");
		$('#qywpd').css("width",qywpd+"px");
	}
}
</script>
</head>

<body onload="jisuanpercent()">
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="<%=request.getContextPath() %>/managebackstage/index">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList">企业信息列表</a><i>/</i><span>公司信息</span></p></div>        
    <div class="page_tab">
        <div class="tab_name" style="height:110px;"><span class="gray1">${companyInfo.companyname}<i class="yellow"><c:choose>
        <c:when test="${companyInfo.edition==1}">
       	 普通版
        </c:when>
        <c:when test="${companyInfo.edition==2}">
       	 收费版
        </c:when>
        </c:choose></i></span><div class="clear"></div>
        
        	<a href="<%=request.getContextPath() %>/managebackstage/getManageStoreEvaluateList?companyid=${companyInfo.companyid}" class="wid_01">顾客满意度</a>
        	<a href="<%=request.getContextPath() %>/managebackstage/getManageCompanyCloudModuleList?companyid=${companyInfo.companyid}">企业网盘</a>
        	<a href="<%=request.getContextPath() %>/managebackstage/getManageCompanyModuleList?companyid=${companyInfo.companyid}" class="wid_01">企业简报</a>
        	<a href="<%=request.getContextPath() %>/managebackstage/intoManageRankingListPage?companyid=${companyInfo.companyid}">KPI排行</a>
        	<a href="<%=request.getContextPath() %>/managebackstage/getReportIncomeChart?companyid=${companyInfo.companyid}" class="wid_01">报表管理</a>
        	<a href="<%=request.getContextPath() %>/managebackstage/intoManageAnalysisPage?companyid=${companyInfo.companyid}" class="wid_01">仓库管理</a>
        	<a href="<%=request.getContextPath() %>/managebackstage/intoMonthlyReportPage?companyid=${companyInfo.companyid}" class="wid_01">采购管理</a>
        	<a href="<%=request.getContextPath() %>/managebackstage/getOrganizeList?companyid=${companyInfo.companyid}" class="wid_01">组织架构</a>
        	<a href="<%=request.getContextPath() %>/managebackstage/getManageStatisticsToday?companyid=${companyInfo.companyid}">日活统计</a>
        	<a href="<%=request.getContextPath() %>/managebackstage/intoCompanyMailListPage?companyid=${companyInfo.companyid}">通讯录</a>
        	<a href="<%=request.getContextPath() %>/managebackstage/intoCompanySharePage?companyid=${companyInfo.companyid}">分享圈</a>
	        <a href="<%=request.getContextPath() %>/managebackstage/getCompanyPower?companyid=${companyInfo.companyid}">权限</a>
	        
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr>
                    <td class="l_text" width="140">餐厅/公司</td>
                    <td>${companyInfo.companyname}</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">空间信息</td>
                    <td>
                    	<div class="kjxx_box">
                        	<em class="bg_white">白色</em><i>总空间:${companyInfo.maxmemory}G</i>
                            <em class="bg_blue">蓝色</em><i>数据空间:${companyInfo.shujudmemory}G</i>
                            <em class="bg_purple">紫色</em><i>云盘空间:${companyInfo.qywpdmemory}G</i>
                            <em class="bg_gray">灰色</em><i>剩余空间:${companyInfo.remainmemory}G</i>
                            <div class="clear"></div>
                            <p><span class="bg_blue"  id="shuju">蓝色</span>
                            <span class="bg_purple"  id="qywpd">紫色</span></p>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="l_text" width="140">通讯地址</td>
                    <td>${companyInfo.address}</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">负责人姓名</td>
                    <td>${companyInfo.contactperson}</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">负责人证件类型</td>
                    <td>${companyInfo.idcodetype}</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">负责人证件号</td>
                    <td>${companyInfo.idcode}</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">电话</td>
                    <td>${companyInfo.telephone}</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">手机号</td>
                    <td>${companyInfo.phone}</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">电子邮件</td>
                    <td>${companyInfo.email}</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">创建人</td>
                    <td><i class="m_r30">${companyInfo.createname}</i><a href="javascript:void(0)" onclick="resetpass('${companyInfo.createid}')" class="yellow">重置密码</a></td>
                </tr>
                <tr>
                    <td class="l_text" width="140">营业执照</td>
                    <td>
                    	<c:forEach items="${companyInfo.licenselist}" var="item">
                    	<img src="${item.imageurl}" height="72" style="margin-right:10px;" />
	                	</c:forEach>
                    </td>
                </tr>
                <tr>
                    <td class="l_text" width="140">备注</td>
                    <td>${companyInfo.info}</td>
                </tr>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
function resetpass(userid){
	swal({
		title : "",
		text : "是否重置密码",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath()%>/managebackstage/resetPass",
			data:"userid="+userid,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text : data.message,
						type : "error",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						
					})
				}else{
					swal({
						title : "",
						text : "重置成功",
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						
					})
				}
			}
		})
	})
}
</script>
</body>
</html>