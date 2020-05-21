<%@page import="com.collection.util.UserUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<% Map<String, Object> user = UserUtil.getSystemUser(request);%>
<div class="head_box">
	<div class="logo_name">CateringMaster</div>
    <div class="user_box">
    	<div class="name" style="border:0px;"><div class="top_username"><b><img src="<%=user.get("headimage") %>" width="26" height="26" /></b><span><%=user.get("realname") %></span></div></div>
        <div class="box">
        	<ul>
            	<li><a href="<%=request.getContextPath()%>/managebackstage/getSystemUser" class="bg_01">个人中心</a></li>
                <li><a href="<%=request.getContextPath()%>/managebackstage/getSystemUpdataUser" class="bg_02">修改个人信息</a></li>
                <li><a href="<%=request.getContextPath()%>/managebackstage/getPassword" class="bg_03">重置密码</a></li>
                <li class="last"><a href="<%=request.getContextPath()%>/managebackstage/login" class="bg_04">退出登录</a></li>
            </ul>
        </div>
    </div>
</div>