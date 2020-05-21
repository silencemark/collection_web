<%@page import="com.alibaba.fastjson.JSON"%>
<%@page import="com.collection.redis.RedisUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.collection.util.CookieUtil"%>
<%@page import="java.util.Map"%>
<%@page import="com.collection.util.UserUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
    <% 
    	Map<String,Object> userInfo=UserUtil.getPCUser(request); 
    	Map<String,Object> powerMap=RedisUtil.getMap(userInfo.get("userid")+"powerMap"); 
    	request.setAttribute("powerMap",powerMap);
    %>
<div class="left_menu">
    	<div class="user_xx">
        	<div class="img"><img src="<%=userInfo.get("headimage") %>" width="128" height="128" /></div>
            <div class="name"><span><%=userInfo.get("realname") %></span><a href="<%=request.getContextPath() %>/pc/memcenter_infoedit">编辑</a></div>
        </div>
       <div class="menu_name">工作表单</div>
        <ul class="worksheet_li">
        	<li id="reward"><a href="<%=request.getContextPath()%>/pc/getRewardListInfo" class="bg_01">奖励单</a></li>
            <li id="punish"><a href="<%=request.getContextPath()%>/pc/getPunishListInfo" class="bg_02">处罚单</a></li>
            <li id="repair"><a href="<%=request.getContextPath() %>/pc/getRepairListInfo" class="bg_03">报修单</a></li>
            <li id="cost"><a href="<%=request.getContextPath()%>/pc/getcostListInfo" class="bg_04">菜品成本单</a></li>
            <li id="precheck"><a href="<%=request.getContextPath()%>/pc/getBeforeMealList" class="bg_05">餐前检查单</a></li>
            <li id="kitchencheck"><a href="<%=request.getContextPath()%>/pc/getKitchenList" class="bg_06">厨房检查单</a></li>
            <li id="leave"><a href="<%=request.getContextPath()%>/pc/getLeaveShopListInfo" class="bg_07">离店报告单</a></li>
        </ul>
</div>

