<%@ page language="java" contentType="text/html; charset=utf-8"
  pageEncoding="UTF-8"%>
<div class="user_xx">
    	   <div class="img"><img src="${userInfo.headimage}" id="imgleft" width="128" height="128"  /></div>
            <div class="name"><span>${userInfo.realname}</span><a href="<%=request.getContextPath() %>/pc/memcenter_infoedit">编辑</a></div>
</div>