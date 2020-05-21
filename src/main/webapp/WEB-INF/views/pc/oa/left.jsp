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
    	   <div class="img"><img src="${userInfo.headimage}"  width="128" height="128"  /></div>
            <div class="name"><span>${userInfo.realname}</span><a href="<%=request.getContextPath() %>/pc/memcenter_infoedit">编辑</a></div>
		</div>
        <div class="menu_name">OA办公</div>
        <ul class="oa_li">
         	<li id="noticeActive"><a href="<%=request.getContextPath() %>/pc/oa_notice_list" class="bg_01">通知</a></li>
            <li id="logActive"><a href="<%=request.getContextPath() %>/pc/getPcOaLogList" class="bg_02">日志</a></li>
            <li id="taskActive"><a href="<%=request.getContextPath() %>/pc/getPcOaTaskList" class="bg_03">任务</a></li>
            <li id="askActive"><a href="<%=request.getContextPath() %>/pc/getPcOaAskList" class="bg_04">请示</a></li>
            <li id="restActive"><a href="<%=request.getContextPath() %>/pc/getPcOaRestList" class="bg_05">请假</a></li>
            <li id="expenseActive"><a href="<%=request.getContextPath() %>/pc/getPcOaExpenseList" class="bg_06">报销</a></li>
            <li id="approvaActive"><a href="<%=request.getContextPath() %>/pc/getPcOaApprovaList" class="bg_07">通用审批</a></li>
            <li id="honourActive"><a href="javascript:void(0)" onclick="intohonourpage()" class="bg_08">荣誉榜</a></li>
            <li id="welfareActive"><a href="javascript:void(0)" onclick="intowelfarepage()" class="bg_09">员工关怀</a></li>
            <li id="briefingActive"><a href="<%=request.getContextPath() %>/pc/getPcOaBriefing" class="bg_10">企业简报</a></li>
            <li id="skydriveActive"><a href="<%=request.getContextPath() %>/pc/getPcOaSkydrive" class="bg_11">企业网盘</a></li>
            <li id="reserveActive"><a href="<%=request.getContextPath() %>/pc/getPcOaReserveList" class="bg_12">备用金申请</a></li>
            <li id="skydrivefoddActive"><a href="<%=request.getContextPath() %>/pc/getPcOaSkydriveFood" class="bg_13">餐饮大师网盘</a></li>
        </ul>
        
        <script type="text/javascript">
        	function intohonourpage(){
        		location.href='<%=request.getContextPath() %>/pc/getPcOaHonourList';
        	}
        	
        	function intowelfarepage(){
        		location.href='<%=request.getContextPath() %>/pc/getPcOaWelfareList';
        	}
        
        </script>
</div>