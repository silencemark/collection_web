<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>处罚单</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
</head>

<body>
	<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>处罚单审核审核</span><a href="#" class="back">返回</a><a href="#">导出</a><a href="#">转发</a></div>
        <div class="page_tab2">      
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="none_border">
                    <tr>
                    	<td class="l_name">姓名</td>
                       <span id="rewardeptname"></span>
                    </tr>
                    <tr>
                    	<td class="l_name">部门</td>
                       <span id="departmentname"></span>
                    </tr>
                   <tr>
                    	<td class="l_name">职务</td>
                    <span id="position"></span>

                    </tr>
                    <tr>
                    	<td class="l_name">处罚结果</td>
                       <span id="punishresulttext"></span>
                    </tr>
                    <tr>
                    	<td class="l_name">发布范围</td>
                        <td>区域一、区域二</td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name2">处罚原因</td>
                        <td>
                       <span id="reason"></span>
                            <div class="clear"></div>
                            <div class="img_list">
                                <div class="img_box"><b><img src="../images/public/img.png"></b></div>
                                <div class="img_box"><b><img src="../images/public/img.png"></b></div>
                                <div class="talk">语言文件</div>
                                <div class="clear"></div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">填写人</td>
                        <td class="img_td"><div class="img f"><img src="../images/pc_page/user_img2.png" width="30" height="30" /></div><i class="i_name" id="createname"></i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">填写时间</td>
                        <td><span id="createtime"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name">审批人</td>
                        <td class="img_td"><div class="img f"><img src="../images/pc_page/user_img2.png" width="30" height="30" /></div><i class="i_name" id="shenpi_examinename"></i></td>
                    </tr>
                    <tr class="none_border">
                    	<td class="l_name2">审批人意见</td>
                        <td><textarea class="text_area" placeholder="请输入审批人意见，最多允许输入800字符" maxlength="800" id="opinion"></textarea></td>
                    </tr>
                    <tr>
                    	<td>&nbsp;</td>
                        <td><a href="#" class="a_btn bg_yellow">同意</a><a href="#" class="a_btn bg_gay2">拒绝</a></td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="comment">
        	<div class="name">共有<i class="yellow">3</i>条评论</div>
            <div class="text_box">
            	<b><textarea placeholder="请输入评论内容，最多允许输入800字符" maxlength="800"></textarea></b>
                <span><input type="button" value="评论" /></span>
            </div>
            <div class="ul_list">
            	<li>
                	<div class="user"><b><img src="../images/pc_page/user_img.jpg" width="30" height="30" /></b><span>shuyi</span></div>
                    <div class="txt">企业采购部门向原材料、燃料、零部件、办公用品等的供应者发出的是采购(入库)单，企业采购部门向原材料这张采购(入库)单对于供应商来说就是他们的订单。</div>
                    <div class="time">2016-06-02 12:20</div>
                    <div class="clear"></div>
                </li>
                <li>
                	<div class="user"><b><img src="../images/pc_page/user_img.jpg" width="30" height="30" /></b><span>shuyi</span></div>
                    <div class="txt">企业采购部门向原材料、燃料、零部件、办公用品等的供应者发出的是采购(入库)单，企业采购部门向原材料这张采购(入库)单对于供应商来说就是他们的订单。</div>
                    <div class="time">2016-06-02 12:20</div>
                    <div class="clear"></div>
                </li>
                <li>
                	<div class="user"><b><img src="../images/pc_page/user_img.jpg" width="30" height="30" /></b><span>shuyi</span></div>
                    <div class="txt">企业采购部门向原材料、燃料、零部件、办公用品等的供应者发出的是采购(入库)单，企业采购部门向原材料这张采购(入库)单对于供应商来说就是他们的订单。</div>
                    <div class="time">2016-06-02 12:20</div>
                    <div class="clear"></div>
                </li>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>
<div class="div_mask" style="display:none"></div>
<div class="tc_structure" style="display:none;">
	<div class="tc_title"><span>选择发布范围</span><a href="#">×</a></div>
    <div class="str_box">
    <ul class="tree_list">
        <li>                    	                    	
            <div class="gray_line"></div>
            <span class="bg_show"><a href="#" class="check">选择</a><i>餐厅公司</i></span>
            <ul>
                <li class="li_bg"><span class="bg_hidden"><a href="#" class="check">选择</a><i>普陀区</i></span></li>
                <li class="li_bg"><span class="bg_hidden"><a href="#" class="checked">选择</a><i>嘉定区</i></span></li>
                <li class="li_bg">
                    <div class="gray_line"></div>
                    <span class="bg_show"><a href="#" class="checked">选择</a><i>浦东区</i></span>
                    <ul>
                        <li class="li_bg"><span class="bg_last"><a href="#" class="checked">选择</a><i>浦东店</i></span></li>
                        <li class="li_bg"><span class="bg_last"><a href="#" class="checked">选择</a><i>申江路店</i></span></li>
                        <li class="li_bg"><span class="bg_last"><a href="#" class="checked">选择</a><i>普降路店</i></span><div class="white_line" name="white_box"></div></li>
                    </ul>
                </li>
                <li class="li_bg">
                    <span class="bg_hidden"><a href="#" class="check">选择</a><i>普陀区</i></span>
                    <div class="white_line" name="white_box"></div>
                </li>
            </ul>
        </li>
    </ul>
    </div>
    <div class="tc_btnbox"><a href="#" class="bg_gay2">取消</a><a href="#"  class="bg_yellow">确定</a></div>
</div>
</body>
</html>
