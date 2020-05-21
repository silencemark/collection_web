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
        <div class="menu_name">采购管理</div>
        <ul class="sup_li">
        	<li class="active" id="apply"><a href="<%=request.getContextPath() %>/pc/getApplyOrder" class="bg_01">申购单</a></li>
            <li id="purchase"><a href="<%=request.getContextPath() %>/pc/getPurchaseOrder" class="bg_02">采购(入库)单</a></li>
            <li id="supplier"><a href="<%=request.getContextPath() %>/pc/getSupplierList"  class="bg_03">供应商</a></li>
            <li id="monthreport"><a href="javascript:void(0)" onclick="linkmonthly()" class="bg_04">采购月报表</a></li>
        </ul>
</div>
<%Map<String,Object> powerMap=RedisUtil.getMap(userInfo.get("userid")+"powerMap"); %>
<script type="text/javascript">
function linkmonthly(){
	var powerMapleft="<%=powerMap.get("power1001010")%>";
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
		location.href='<%=request.getContextPath() %>/pc/monthlyReport';
	}
}
</script>