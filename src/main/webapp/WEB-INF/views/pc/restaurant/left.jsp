<%@page import="com.collection.redis.RedisUtil"%>
<%@page import="com.alibaba.fastjson.JSON"%>
<%@page import="java.util.List"%>
<%@page import="com.collection.util.CookieUtil"%>
<%@page import="java.util.Map"%>
<%@page import="com.collection.util.UserUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
    <%Map<String,Object> userInfo=UserUtil.getPCUser(request); %>
<div class="left_menu">
    	<div class="user_xx">
        	<div class="img"><img src="<%=userInfo.get("headimage") %>" width="128" height="128" /></div>
            <div class="name"><span><%=userInfo.get("realname") %></span><a href="#">编辑</a></div>
        </div>
        <div class="menu_name">店面管理</div>
        <ul class="restaurant_li">
        	<li id="report"><a href="javascript:void(0)" onclick="linkreport()" class="bg_01">每日报表统计</a></li>
            <li id="appraise"><a href="javascript:void(0)" onclick="linkappraise()" class="bg_02">顾客满意度</a></li>
            <li id="dailyreport"><a href="<%=request.getContextPath() %>/pc/getEverydayReportList" class="bg_03">每日报表</a></li>
            <li id="employee"><a href="javascript:void(0)" onclick="linkemployee()" class="bg_04">人事轨迹</a></li>
            <li id="patrollog"><a href="<%=request.getContextPath() %>/pc/getTourStoreList" class="bg_05">巡店日志</a></li>
        </ul>
</div>
<%Map<String,Object> powerMap=RedisUtil.getMap(userInfo.get("userid")+"powerMap"); %>
<script type="text/javascript">
function linkreport(){
	var powerMapleft="<%=powerMap.get("power6001310")%>";
	if(powerMapleft == "null"){
		//查询提示框
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath() %>/app/getPromptInfo",
			data:"datacode=personnopower",
			success:function(data){
				swal({
					title : "",
					text :  data.datamap.remark,
					type : "warning",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					
				});
				return false;
			}
		})
	}else{
		location.href='<%=request.getContextPath() %>/pc/getReportIncomeChart';
	}
}

function linkemployee(){
	location.href='<%=request.getContextPath() %>/pc/getPathKpiStarList';
}

function linkappraise(){
	location.href='<%=request.getContextPath() %>/pc/getStoreEvaluateList';
}
</script>