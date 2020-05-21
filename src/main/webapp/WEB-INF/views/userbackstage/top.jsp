<%@page import="com.collection.util.UserUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<% Map<String, Object> user = UserUtil.getUser(request);%>
<div class="head_box">
	<div class="logo_name" style="background:url(<%=request.getContextPath()+user.get("logourl")%>) no-repeat left; background-size:32px 27px;"><%=user.get("companyname")%></div>
    <div class="user_box">
    	<div class="name"><div class="top_username"><b><img src="<%=request.getContextPath() %><%=user.get("headimage")%>" width="26" height="26" /></b><span><%=user.get("realname")%></span></div></div>
        <div class="box">
        	<ul>
            	<li><a href="<%=request.getContextPath() %>/userbackstage/getOrganizeByUser" class="bg_01">个人中心</a></li>
                <li><a href="<%=request.getContextPath() %>/userbackstage/selectUpdataUserInfo" class="bg_02">修改个人信息</a></li>
                <li><a href="<%=request.getContextPath() %>/userbackstage/getPassword" class="bg_03">重置密码</a></li>
                <li class="last"><a href="<%=request.getContextPath()%>/pc/login" class="bg_04">退出登录</a></li>
            </ul>
        </div>
    </div>
    <div class="link">
    	<ul>
        	<!-- <li><a href="#" class="bg_01">分享圈</a></li>
            <li><a href="#" class="bg_02">备忘录</a></li> -->
            <li><a href="<%=request.getContextPath() %>/pc/index" class="bg_03">PC前端</a></li>
        </ul>
    </div>
</div>