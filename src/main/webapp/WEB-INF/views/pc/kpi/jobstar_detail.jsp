<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>岗位星值详情</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/appbase.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/relaycomment.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/kpi/jobstar_detail.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/showcomment.js"></script>
<script type="text/javascript">
var userid;
userid='${user.userid}';
var param=new Object();
param.receiveid = '${user.userid}';
param.companyid ='${user.companyid}';
function exportexcel(){
	swal({	
		title : "",
		text : "是否导出",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$.ajax({
	        type: "POST",
	        url: "<%=request.getContextPath() %>/pc/exportEvaluateDetailList?evaluateid=${map.evaluateid}",
	       
	        success: function(data){
	        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
	        }
		 });
	    });
	}


$(document).ready(function(){
	$('.kpi_li').find("li").attr('class','');
	$('#jobstar').attr('class','active');
	getSendName('${map.forwarduserid}',"#sendname");
});
</script>
</head>

<body>
<input type="hidden" id="userid" value="${user.userid }"/>
<input type="hidden" id="companyid" value="${user.companyid }"/>
<input type="hidden" id="evaluateid" value="${map.evaluateid }"/>
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>岗位星值详情</span><a href="javascript:void(0)" onclick="window.history.go(-1)" class="back">返回</a>
    	<a href="javascript:void(0)" onclick="exportexcel()">导出</a><a href="javascript:void(0)" onclick="showForwardDiv()">转发</a></div>
        <div class="page_tab2">  
        	<div class="check_state" id="agree" style="display: none;"><img src="<%=request.getContextPath() %>/userbackstage/images/pc_page/agree_img.png" /></div>          
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                    	<td class="l_name">所属区域</td>
                        <td id="organizename"></td>
                    </tr>
                    <tr>
                    	<td class="l_name">岗位名称</td>
                        <td id="gangwei"></td>
                    </tr>
                    <tr>
                    	<td class="l_name">岗位星值</td>
                        <td class="mx_tab">
                        	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="mx_td">
                        		<tbody id="evaluate_list">
                        			
                        		</tbody>
<!--                                 <tr> -->
<!--                                 	<td>仪容仪表</td> -->
<!--                                     <td class="t_r"> -->
<!-- 	                                    <div class="star_box"> -->
<!-- 		                                    <img src="/userbackstage/images/pc_page/start2.png" /> -->
<!-- 		                                    <img src="../images/pc_page/start2.png" /> -->
<!-- 		                                    <img src="../images/pc_page/start2.png" /> -->
<!-- 		                                    <img src="../images/pc_page/start3.png" /> -->
<!-- 		                                    <img src="../images/pc_page/start3.png" /> -->
<!-- 	                                    </div> -->
<!--                                     </td> -->
<!--                                 </tr> -->
								<tr class="none_border">
                                	<td>&nbsp;</td>
                                    <td class="t_r">合计： <i class="yellow" id="sumstar">0</i> 星</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">申请人</td>
                        <td class="img_td"><div class="img f"><img src="<%=request.getContextPath() %>/userbackstage/images/pc_page/user_img2.png" width="30" height="30" /></div><i class="i_name" id="shenqing_name"></i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">填写时间</td>
                        <td id="shenqing_time"></td>
                    </tr>
                    <tr>
                    	<td class="l_name">审批人</td>
                        <td class="img_td"><div class="img f"><img src="<%=request.getContextPath() %>/userbackstage/images/pc_page/user_img2.png" width="30" height="30" /></div><i class="i_name" id="shenpi_name"></i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">抄送人</td>
                        <td class="img_td" id="CCusernames"></td>
                    </tr>
                    <tr>
                    	<td class="l_name">转发人</td>
                        <td class="img_td">
                        	<i class="i_name" id="sendname"></i>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">审批时间</td>
                        <td id="shenpi_time"></td>
                    </tr>
                    <tr class="none_border">
                    	<td class="l_name2">审批人意见</td>
                        <td id="yijian"></td>
                    </tr>
                </table>
            </div>
        </div>
        
        <div class="comment">
       		<div class="name">共有<i class="yellow" id="num"></i>条评论</div>
            <div class="text_box" >
            	<b><textarea placeholder="请输入评论内容，最多允许输入800字符" maxlength="800"id="content_text1"></textarea></b>
                <span><input type="button" value="评论" onclick="checkComment(1)" /></span>
	           	<input type="hidden" id="parentid" />
				<input type="hidden" id="parentuserid" />
            </div>
            <div class="ul_list" id="ul_list">
                
            </div>
            <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination-->
            <div class="clear"></div>
        </div>
    </div>
    <div class="clear"></div>
</div>
</body>
</html>
