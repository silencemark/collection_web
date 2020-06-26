<%@ page language="java" contentType="text/html; charset=utf-8"
  pageEncoding="UTF-8"%>
<div class="left_menu">
    	<ul>
        	<li><span class="active li_active"><a href="<%=request.getContextPath() %>/managebackstage/index" class="bg_01">首页</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="userlist"><a href="<%=request.getContextPath() %>/managebackstage/getUserList" class="bg_04">用户信息管理</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="orderlist"><a href="<%=request.getContextPath() %>/managebackstage/getOrderList" class="bg_07">订单管理</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="bannerlist"><a href="<%=request.getContextPath() %>/managebackstage/getBannerList" class="bg_02">banner图管理</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="indexmovielist"><a href="<%=request.getContextPath() %>/managebackstage/getIndexMovieList" class="bg_01">首页电影管理</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="membercard"><a href="<%=request.getContextPath() %>/managebackstage/getMemberCardList" class="bg_05">VIP会员卡管理</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="syscloud"><a href="<%=request.getContextPath() %>/managebackstage/getAdminUserList" class="bg_04">用户会员等级管理</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="userlist"><a href="<%=request.getContextPath() %>/managebackstage/getUserList" class="bg_02">收款方式管理</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="capacity"><a href="<%=request.getContextPath() %>/managebackstage/getCloudCapacityList" class="bg_03">实名认证管理</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="statistics"><a href="<%=request.getContextPath() %>/managebackstage/statisticsIndex" class="bg_06">统计</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="syscloud"><a href="<%=request.getContextPath() %>/managebackstage/getAdminUserList" class="bg_03">联系客服及建议回复</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="system"><a href="<%=request.getContextPath() %>/managebackstage/systemIndex" class="bg_08">兑换列表</a></span><div class="line"></div>
       </ul>
</div>