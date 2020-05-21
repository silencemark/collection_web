<%@page import="com.collection.redis.RedisUtil"%>
<%@page import="com.alibaba.fastjson.JSON"%>
<%@page import="java.util.List"%>
<%@page import="com.collection.util.CookieUtil"%>
<%@page import="java.util.Map"%>
<%@page import="com.collection.util.UserUtil"%>
<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%
    	Map<String,Object> userInfo=UserUtil.getPCUser(request); 
    	Map<String,Object> powerMap=RedisUtil.getMap(userInfo.get("userid")+"powerMap");
    %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>首页</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/index.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/jquery.mCustomScrollbar.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery.SuperSlide.2.1.1.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
</head>
<script type="text/javascript">
function getnotice(){
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getHomePageBanner?model=1&companyid=${userInfo.companyid}',
		success:function(data){
			if(data.length>0){
				var slidertemp="";
				for(var i=0;i<data.length;i++){
					slidertemp+="<li style=\"display: block;\"><a href=\"#\"><img src=\""+projectpath+"/"+data[i].imgurl+"\"  alt=\"\" width=\"750\" height=\"320\"></a></li>";
				}
				$('#bannerul').html(slidertemp);
				
				$(".prev,.next").hover(function(){
					$(this).stop(true,false).fadeTo("show",0.9);
				},function(){
					$(this).stop(true,false).fadeTo("show",0.4);
				});
				
				$(".banner-box").slide({
					titCell:".hd ul",
					mainCell:".bd ul",
					effect:"fold",
					interTime:3500,
					delayTime:500,
					autoPlay:true,
					autoPage:true, 
					trigger:"click" 
				});
			}
		}
	})
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getRemindList?userid=${userInfo.userid}',
		success:function(data){
			if(data.remindlist.length>0){
				var temp="";
				for(var i=0;i<data.remindlist.length;i++){
					temp+="<li class=\"bg2\" onclick=\"location.href=\'"+data.remindlist[i].pclinkurl+"\'\"><span>【"+data.remindlist[i].title+"】"+data.remindlist[i].content+"</span><i>"+data.remindlist[i].createtime+"</i></li>";
				}
				$('#remindnum').html(data.remindnum);
				$('#noticeul').html(temp);
			}else{
				$('#remindnum').html(0);
				$('#yesremind').hide();
				$('#noremind').show();
			}
		}
	})
	
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getHomeNotreadNum?companyid=${userInfo.companyid}&receiveid=${userInfo.userid}',
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
					
				});
			}else{
				if(parseInt(data.purchasenum)> 0){
					$('#purchasenum').show();
					$('#purchasenum').html(data.purchasenum);
				}
				if(parseInt(data.warehousenum)> 0){
					$('#warehousenum').show();
					$('#warehousenum').html(data.warehousenum);
				}
				if(parseInt(data.worksheetnum)> 0){
					$('#worksheetnum').show();
					$('#worksheetnum').html(data.worksheetnum);
				}
				if(parseInt(data.OAofficenum)> 0){
					$('#OAofficenum').show();
					$('#OAofficenum').html(data.OAofficenum);
				}
				if(parseInt(data.storefrontnum)> 0){
					$('#storefrontnum').show();
					$('#storefrontnum').html(data.storefrontnum);
				}
				if(parseInt(data.starassessnum)> 0){
					$('#starassessnum').show();
					$('#starassessnum').html(data.starassessnum);
				}
			}
		}
	})
	
} 

function restaurant(){
	var powerMapleft="<%=powerMap.get("power6001310")%>";
	if(powerMapleft == "null"){
		location.href='<%=request.getContextPath() %>/pc/getStoreEvaluateList';
	}else{
		location.href='<%=request.getContextPath() %>/pc/getReportIncomeChart';
	}
}
</script>
<body onload="getnotice()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="index_main">
	<div class="l_box">
        <div class="banner-box">
            <div class="bd">
                <ul id="bannerul">
                </ul>
            </div>
            <div class="banner-btn">
                <a class="prev" style="display:none" href="javascript:void(0);"></a>
                <a class="next" style="display:none" href="javascript:void(0);"></a>
                <div class="hd"><ul></ul></div>
            </div>
        </div>
        <div class="index_menu">
            <ul>
                <li class="bg1"><a href="<%=request.getContextPath()%>/pc/getApplyOrder"><b><img src="../userbackstage/images/index/menu_01.png"><i id="purchasenum" style="display: none">0</i></b><span>采购管理</span></a></li>
                <li class="mid bg2"><a href="<%=request.getContextPath() %>/pc/getMaterialOrder"><b><img src="../userbackstage/images/index/menu_02.png"><i id="warehousenum" style="display: none">0</i></b><span>仓库管理</span></a></li>
                <li class="bg3"><a href="<%=request.getContextPath()%>/pc/getRewardListInfo"><b><img src="../userbackstage/images/index/menu_03.png"><i id="worksheetnum" style="display: none">0</i></b><span>工作表单</span></a></li>
                <li class="bg4"><a href="<%=request.getContextPath()%>/pc/oa_notice_list"><b><img src="../userbackstage/images/index/menu_04.png"><i id="OAofficenum" style="display: none">0</i></b><span>OA办公</span></a></li>
                <li class="mid bg5"><a href="javascript:void(0)" onclick="restaurant()"><b><img src="../userbackstage/images/index/menu_05.png"><i id="storefrontnum" style="display: none">0</i></b><span>店面管理</span></a></li>
                <li class="bg6"><a href="<%=request.getContextPath() %>/pc/getStarAssessList"><b><img src="../userbackstage/images/index/menu_06.png"><i id="starassessnum" style="display: none">0</i></b><span class="kpi">KPI<br />星级考核</span></a></li>
            </ul>
            <div class="clear"></div>
        </div>
   </div>
   <div class="db_list">
	<div class="name"><span>待办事项<i style="color:white;border:0px;background: #fa302e;font-weight: normal;" id="remindnum"></i></span></div>
    	<div class="ul_list mCustomScrollbar light _mCS_1 mCS-autoHide" data-mcs-theme="minimal-dark" id="yesremind">
	        <ul id="noticeul">
	        </ul>
	    </div>
    	<div class="list_none" style="display:none" id="noremind"><b><img src="../userbackstage/images/index/none_msg.gif" width="293" height="240"></b><span>您暂时还没有通知以及待审批的事项<br>（请先休息会儿吧）</span></div>
    
    </div>
   <div class="clear"></div>
</div>

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery.mCustomScrollbar.concat.min.js"></script>
</body>
</html>
