<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>企业信息权限</title>
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

</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="<%=request.getContextPath() %>/managebackstage/index">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList">企业信息列表</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyInfo?companyid=${companyInfo.companyid}">公司信息</a><i>/</i><span>公司权限</span></p></div>  
    <div class="page_tab m_top">
        <div class="tab_name noneborder"><span class="gray1">${companyInfo.companyname}权限管理</span></div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
                	<td width="5%">模块</td>
                    <td width="10%">权限状态</td>
                    <td width="15%">操作</td>
                </tr>
                <c:forEach items="${powerlist}" var="power">
                	<c:if test="${power.parentid==null || power.parentid==''}">
                			<tr>
		                	<td >${power.powername}</td>
		                	
		                				
						              
						                    <c:choose>
						                    	<c:when test="${power.edition==1}">
						                    		<td><i class="green">已获得</i>（普通版）</td>
						                    	</c:when>
						                    	<c:otherwise>
						                    		<c:choose>
						                    			<c:when test="${power.result==1}">
						                    				<td><i class="green">已获得</i>（专业版）</td>
						                    			</c:when>
						                    			<c:otherwise>
						                    				<td><i class="red">未获得</i>（专业版）</td>
						                    			</c:otherwise>
						                    		</c:choose>
						                    	</c:otherwise>
						                    </c:choose>
						                    <c:choose>
				                    			<c:when test="${power.status==2}">
				                    				<td><a href="javascript:void(0)" class="green m_r10" onclick="updatepower(1,'${power.powerid}','${power.powername}','${power.companyid}')">同意</a>
				                    				<a href="javascript:void(0)" class="red m_r10" onclick="updatepower(0,'${power.powerid}','${power.powername}','${power.companyid}')">拒绝</a><br/>
				                    				<i>${power.createtime}</i></td>
				                    			</c:when>
				                    			<c:when test="${power.status==1}">
				                    				<c:choose>
						                    			<c:when test="${power.result==1}">
						                    				<td><i class="green">已同意</td>
						                    			</c:when>
						                    			<c:otherwise>
						                    				<td><i class="red">已拒绝</td>
						                    			</c:otherwise>
						                    		</c:choose>
				                    			</c:when>
				                    			<c:otherwise>
				                    				<td>&nbsp;</td>
				                    			</c:otherwise>
				                    		</c:choose>
						                </tr>
		                			
                	</c:if>
                </c:forEach>
            </table>
        </div>
  </div>
</div>
<div class="div_mask" style="display:none;"></div>
<div class="tc_detail" style="display:none;">
	<div class="tc_title"><span id="powername"></span><a href="javascrpit:void(0)" onclick="$('.div_mask').hide();$('.tc_detail').hide();">×</a></div>
    <div class="p_box">
        <p id="info"></p>
    </div>
</div>
<script type="text/javascript">
function detailshow(powername,info){
	$('#powername').text(powername);
	$('#info').text(info);
	$('.div_mask').show();
	$('.tc_detail').show()
}
function updatepower(result,powerid,powername,companyid){
	var resultstr="确认同意?";
	if(result==0){
		resultstr="确认拒绝?";
	}
	
	swal({
		title : "",
		text : resultstr,
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
			url:"<%=request.getContextPath()%>/managebackstage/updateCompanyPower",
			data:"powerid="+powerid+"&result="+result+"&powername="+powername+"&companyname=${companyInfo.companyname}&companyid="+companyid,
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
					location.reload();
				}
			}
		})
	})
}
</script>
</body>
</html>