<%@ page language="java" contentType="text/html; charset=utf-8"
  pageEncoding="UTF-8"%>
<div class="left_menu">
    	<ul>
        	<li><span class="active li_active"><a href="<%=request.getContextPath() %>/managebackstage/index" class="bg_01">首页</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="company"><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList" class="bg_02">企业信息</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="capacity"><a href="<%=request.getContextPath() %>/managebackstage/getCloudCapacityList" class="bg_03">申请记录</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="adminuser"><a href="<%=request.getContextPath() %>/managebackstage/getAdminUserList" class="bg_04">管理员管理</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="syscloud"><a href="<%=request.getContextPath() %>/managebackstage/intoSkyDrivePage" class="bg_05">餐饮大师网盘</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="statistics"><a href="<%=request.getContextPath() %>/managebackstage/statisticsIndex" class="bg_06">统计</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="systemuser"><a href="<%=request.getContextPath() %>/managebackstage/getSystemUser" class="bg_07">个人中心</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="system"><a href="<%=request.getContextPath() %>/managebackstage/systemIndex" class="bg_08">系统设置</a></span><div class="line"></div>
       </ul>
</div>