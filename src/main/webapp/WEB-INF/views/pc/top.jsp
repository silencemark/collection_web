<%@page import="java.util.Map"%>
<%@page import="com.collection.util.UserUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
  pageEncoding="UTF-8"%>
<%-- <script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script> --%>
<div class="head_box">
	<div class="box">
	     <%
           //统一设置当前用户信息
           Map<String,Object> userInfo=UserUtil.getPCUser(request); 
           request.setAttribute("userInfo", userInfo);
         %>
    	<div class="logo_name" style="background:url(<%=request.getContextPath()+userInfo.get("logourl")%>) no-repeat left; background-size:32px 27px;"><%=userInfo.get("companyname")%></div>
        <div class="out_btn"><a href="<%=request.getContextPath()%>/pc/login">退出</a></div>
        <div class="link">
        	<ul>
            	<li class="active" id="homepage"><a href="<%=request.getContextPath()%>/pc/index" class="bg_home">首页</a></li>
                <li id="share"><a href="<%=request.getContextPath() %>/pc/intoSharePage" class="bg_share">分享圈</a></li>
                <li id="memorandum"><a href="<%=request.getContextPath()%>/pc/getMemorandumListToday" class="bg_meno">备忘录</a></li>
                <li ><a href="javascript:void(0)" onclick="$('#userlogin').submit();" class="bg_sys">后台管理</a></li>
                <li id="usercenter"><a href="<%=request.getContextPath()%>/pc/memcenter_info" class="bg_user">用户中心</a></li>
            </ul>
        </div>

		<form action="<%=request.getContextPath()%>/userbackstage/initlogin" id="userlogin" method="post">
        	<input type="hidden" name="pcuserid" value="<%=userInfo.get("userid") %>">
        	<input type="hidden" name="username" value="<%=userInfo.get("username") %>"/>
        	<input type="hidden" name="realname" value="<%=userInfo.get("realname") %>"/>
        	<input type="hidden" name="phone" value="<%=userInfo.get("phone") %>"/>
        </form>
    </div>
</div>