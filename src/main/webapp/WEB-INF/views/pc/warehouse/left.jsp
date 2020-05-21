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
        <div class="menu_name">仓库管理</div>
        <ul class="stock_li">
        	<li class="active" id="use"><a href="<%=request.getContextPath() %>/pc/getMaterialOrder" class="bg_01">用料单</a></li>
            <li id="return"><a href="<%=request.getContextPath() %>/pc/getReturnOrder" class="bg_02">退货单</a></li>
            <li id="break"><a href="<%=request.getContextPath() %>/pc/getReportlossOrder"  class="bg_03">报损单</a></li>
            <li id="stock"><a href="javascript:void(0)" onclick="linkstock()" class="bg_04">库存分析</a></li>
        </ul>
</div>

<%Map<String,Object> powerMap=RedisUtil.getMap(userInfo.get("userid")+"powerMap"); %>
<script type="text/javascript">
function linkstock(){
	var powerMapleft="<%=powerMap.get("power2001010")%>";
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
				return;
			}
		})
	}else{
		location.href='<%=request.getContextPath() %>/pc/getAnalysis';
	}
}
</script>