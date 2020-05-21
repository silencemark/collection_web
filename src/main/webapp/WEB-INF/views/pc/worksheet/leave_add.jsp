<%@page import="com.collection.util.Constants"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>离店检查单</title>
<link
	href="<%=request.getContextPath()%>/userbackstage/style/pc_public.css"
	type="text/css" rel="stylesheet" />
<link
	href="<%=request.getContextPath()%>/userbackstage/style/pc_page.css"
	type="text/css" rel="stylesheet" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/app/appcssjs/sweetalert/dist/sweetalert.css" />
<script
	src="<%=request.getContextPath()%>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
			<!-- 上传图片需要js -->   
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>  
<script type="text/javascript"
	src="<%=request.getContextPath() %>/pc/script/organizeRange.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/worksheet/leave_add.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/change_examineuserinfo.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/pc_chaosongren.js"></script>
</head>
<script type="text/javascript">
	$(document).ready(function() {
		$('.sup_li').find("li").attr('class', '');
		$('#leave').attr('class', 'active');
		$('#homepage').parent().find("li").attr('class', '');
		$('#homepage').attr('class', 'active');
	})
</script>

<body>
<jsp:include page="../top.jsp"></jsp:include>
		<input type="hidden" id="userid" value="${userInfo.userid }"/>
   		<input type="hidden" id="realname" value="${userInfo.realname }"/>
		<input type="hidden" id="companyid" value="${userInfo.companyid }"/>
		<input type="hidden" id="organizeid" value="${userInfo.organizeid }"/>
<div class="page_main">
		<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>新建离店报告单</span><a href="javascript:void(0)" onclick="window.history.go(-1)" class="back">返回</a></div>
        <div class="page_tab2">            
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="none_border">
                    <tr>
                    	<td class="l_name">所属架构</td>
                        <td><select class="text" id="organize_option"></select></td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name">检查区域</td>
                        <td><select class="text" id="temple_option"></select></td>
                    </tr>
                    <tr class="wlmx_detail">
                    	<td class="l_name">检查项目</td>
                        <td class="mx_tab">
                       	  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="mx_td">
                       	  		<tbody id="ev_list">
                       	  			
                       	  		</tbody>
<!--                                 <tr> -->
<!--                                 	<td>餐具清洁</td> -->
<!--                                     <td class="t_r"><div class="star_box"><a class="a_star"><img src="../images/pc_page/start2.png" /></a><a class="a_star"><img src="../images/pc_page/start2.png" /></a><a class="a_star"><img src="../images/pc_page/start2.png" /></a><a class="a_star"><img src="../images/pc_page/start3.png" /></a><a class="a_star"><img src="../images/pc_page/start3.png" /></a></div></td> -->
<!--                                 </tr>                                                        -->
                            <tr class="none_border">
                                	<td>&nbsp;</td>
                                    <td class="t_r">
                                    	<div class="star_box">
									    	<span><i style="display: none;" id="sumstarnum">0</i><i style="display: none;" id="avgstarlevel">0</i></span>
										    <li class="n_bor">
											    <b>
											    	<a id="showStarLevel"></a>
												    <font style="margin-left:10px;" id="star_level"></font>
												</b>
											    <div class="clear"></div>
										    </li>
									    </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr class="first_td">
                    	<td class="l_name">审核人</td>
                    	<input type="hidden" id="examineuserid"/>
                        <td class="img_td"><div class="img f"><img src="../userbackstage/images/pc_page/user_img2.png" id="examineheadimage" width="30" height="30" /></div><i class="i_name" id="examinename"></i><a href="javascript:void(0)" onclick="openOrCloseExamineDiv()" class="add_user">添加</a></td>
                    </tr>
                    <tr>
                    	<td class="l_name">抄送人</td>
                        <td class="img_td" id="CCusernames"><a href="javascript:void(0)" onclick="showCCuserOrganize()" class="add_user">添加</a></td>
                    </tr>
                    <tr>
                    	<td class="l_name">检查人</td>
                        <td class="img_td">
                        	<div class="img f"><img src="<%=request.getContextPath() %>${userInfo.headimage }" width="30" height="30" /></div>
							<i id="createname" class="i_name">${userInfo.realname }</i>
                        </td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name">检查时间</td>
                        <td><%=new SimpleDateFormat("yyyy-MM-dd H:m").format(new Date())%></td>
                    </tr>
                    <tr class="foot_td">
                    	<td>&nbsp;</td>
                        <td><a href="javascript:void(0)" onclick="addEvaluate()" class="a_btn bg_yellow">发送</a>
                        <a href="javascript:void(0)" onclick="window.history.go(-1)" class="a_btn bg_gay2">取消</a></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>

<div class="div_mask" style="display:none"></div>
</body>
</html>
