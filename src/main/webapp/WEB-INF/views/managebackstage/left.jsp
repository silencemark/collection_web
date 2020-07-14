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
            <li><span class="bg_hidden" id="levellist"><a href="<%=request.getContextPath() %>/managebackstage/getLevelList" class="bg_04">用户会员等级管理</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="paymentlist"><a href="<%=request.getContextPath() %>/managebackstage/getPaymentMethodList" class="bg_02">收款方式管理</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="certificationlist"><a href="<%=request.getContextPath() %>/managebackstage/getCertificationList" class="bg_03">实名认证管理</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="questionlist"><a href="<%=request.getContextPath() %>/managebackstage/getQuestionList" class="bg_03">投诉与建议回复</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="notice"><a href="<%=request.getContextPath() %>/managebackstage/getSysNoticeList" class="bg_02">发送系统通知管理</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="ratelist"><a href="<%=request.getContextPath() %>/managebackstage/getRateList" class="bg_05">抢购概率管理</a></span><div class="line"></div></li>
            <li><span class="bg_hidden" id="exchangelist"><a href="<%=request.getContextPath() %>/managebackstage/getExchangeList" class="bg_08">兑换列表</a></span><div class="line"></div>
       		<li><span class="bg_hidden" id="statistics"><a href="<%=request.getContextPath() %>/managebackstage/statisticsIndex" class="bg_06">统计</a></span><div class="line"></div></li>
       </ul>
</div>