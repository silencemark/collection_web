<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>申购单详情</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/showcomment.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/relaycomment.js"></script>

</head>
<script type="text/javascript">

var relayiduserid = '${userInfo.userid}';
var relayidcompanyid ='${userInfo.companyid}';
var relayid = '${applyInfo.orderid}';
var relaytype = 2;

function exportexcel(){
	$.ajax({
        type: "POST",
        url: "<%=request.getContextPath() %>/pc/exportApplyInfo",
        data: 'orderid=${applyInfo.orderid}',
        success: function(data){
        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
        }
    });
}
$(document).ready(function(){
	$('.sup_li').find("li").attr('class','');
	$('#apply').attr('class','active');
	$('#homepage').parent().find("li").attr('class','');
	$('#homepage').attr('class','active');
	
})
function onloaddata(){
	readstatus('${applyInfo.orderid}','${userInfo.userid}');
	onloadDate();
}

</script>
<body onload="onloaddata()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
   	  <div class="page_name"><span>申购单详情</span><a href="javascript:void(0)" onclick="goBackPage()" class="back">返回</a>
   	  <a href="javascript:void(0)" onclick="exportexcel();">导出</a>
   	  <c:if test="${applyInfo.examineuserid!=userInfo.userid || applyInfo.status!=0}">
   	  <a href="javascript:void(0)" onclick="showForwardDiv()" >转发</a>
   	  </c:if>
   	  </div>
        <div class="page_tab2">
          <c:if test="${applyInfo.result==1}">
          <div class="check_state"><img src="../userbackstage/images/pc_page/agree_img.png" /></div>        
          </c:if>
          <c:if test="${applyInfo.result==0}">
          <div class="check_state"><img src="../userbackstage/images/pc_page/agree_img2.png" /></div>        
          </c:if>  
          <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                    	<td class="l_name">申购单编号</td>
                        <td>${applyInfo.orderno}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">店面</td>
                        <td>${applyInfo.organizename}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">物料明细</td>
                        <td class="mx_tab">
                        	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="mx_td">
                            	<tr class="gray_bg">
                                	<td width="10%">类别</td>
                                    <td width="10%">名称</td>
                                    
                                    <td width="10%">数量</td>
                                    <td width="15%">规格</td>  
                                    <td width="10%">单价</td>
                                    <td width="10%">总价</td>     
                                      
                                </tr>
                                <c:forEach items="${applyInfo.materiallist}" var="item">
                                <tr>
                                	<td>${item.type}</td>
                                    <td>${item.name}</td>
                                    
                                    <td>${item.num}</td>
                                    <td>${item.specificationsall}</td>   
                                    <td>
                                    	<c:if test="${item.price!=null && item.price!='' && item.price!=0}">
                                    		${item.price}
                                    	</c:if>
                                    </td>
                                    <td>
                                    	<c:if test="${item.sumprice!=null && item.sumprice!='' && item.sumprice!=0}">
                                    		${item.sumprice}
                                    	</c:if>
                                    </td> 
                                    
                                </tr>
                                </c:forEach>                                
                                <tr class="none_border">
                                	<td colspan="4">共 <i class="yellow">${applyInfo.maternum }</i> 项</td>
                                    <td colspan="3" class="t_r">合计<i class="yellow">￥${applyInfo.materialprice }</i>元</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">申购人</td>
                        <td class="img_td"><div class="img f"><img src="${applyInfo.createheadimage}" width="30" height="30" /></div><i class="i_name">${applyInfo.createname}</i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">填写时间</td>
                        <td>${applyInfo.createtime.substring(0,16) }</td>
                    </tr>
                    <tr>
                    	<td class="l_name">审批人</td>
                        <td class="img_td"><div class="img f"><img src="${applyInfo.examineheadimage}" width="30" height="30" /></div><i class="i_name">${applyInfo.examinename }</i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">抄送人</td>
                        <td class="img_td">
                        	<c:forEach items="${applyInfo.ccuserlist }" var="map">
                        		<div class="img f"><img src="${map.headimage }" width="30" height="30" /></div><i class="i_name" >${map.realname }</i>
                        	</c:forEach>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">转发人</td>
                        <td class="img_td">
                        	<i class="i_name" id="sendname"></i>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">审批时间</td>
                        <td>${applyInfo.updatetime.substring(0,16) }</td>
                    </tr>
                    <c:choose>
                    	<c:when test="${applyInfo.examineuserid==userInfo.userid && applyInfo.status==0}">
	                    <tr class="none_border">
	                    	<td class="l_name2">审批人意见</td>
	                        <td><textarea class="text_area" placeholder="请输入审批人意见，最多允许输入800字符" maxlength="800" id="opinion"></textarea></td>
	                    </tr>
	                    <tr>
	                    	<td>&nbsp;</td>
	                        <td><a href="javascript:void(0)" onclick="examineorder(1)" class="a_btn bg_yellow">同意</a>
	                        <a href="javascript:void(0)" onclick="examineorder(0)" class="a_btn bg_gay2">拒绝</a></td>
	                    </tr>
                    	</c:when>
                    	<c:otherwise>
                    	<tr class="none_border">
	                    	<td class="l_name2">审批人意见</td>
	                        <td>${applyInfo.opinion }</td>
	                    </tr>
                    	</c:otherwise>
                    </c:choose>
                    
                </table>
            </div>
        </div>
        
        <div class="comment">
       	<div class="name">共有<i class="yellow" id="num"></i>条评论</div>
            <div class="text_box" >
            <c:if test="${applyInfo.examineuserid!=userInfo.userid || applyInfo.status!=0}">
            	<b><textarea placeholder="请输入评论内容 ，最多允许输入800字符" maxlength="800"id="content_text1"></textarea></b>
                <span><input type="button" value="评论" onclick="checkComment(1)" /></span>
             </c:if>
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

<script type="text/javascript">
//评论分页
function pageHelper(num){
	var param =new  Object();
	var currentPage = num;
	param.currentPage=currentPage;
	param.orderid='${applyInfo.orderid}';
	param.resourcetype= 2;
	pageList(param);
}

//评论列表
function onloadDate(){
	getSendName('${map.forwarduserid}',"#sendname");
	var param =new  Object();
	param.orderid='${applyInfo.orderid}';
	param.resourcetype=2;
	addcommentAjax(param);//显示评论列表		
}

//新增评论
function checkComment(row){
	var param = new Object();
	param.userid = '${userInfo.userid}';
	param.resourceid = '${applyInfo.orderid}';
	param.resourcetype =2;
	showComment(row,param,'/pc/getApplyInfo?orderid=${applyInfo.orderid}&resourcetype=2');//添加评论信息
}

function examineorder(result){
	if($('#opinion').val()==""){
		swal({
			title : "",
			text : "请输入审批意见",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
		return;
	}
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/app/examineApplyOrder',
		data:'orderid=${applyInfo.orderid}&userid=${userInfo.userid}&result='+result+'&opinion='+$('#opinion').val(),
		success:function(data){
				swal({
					title : "",
					text : "操作成功",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					changeIsread('${applyInfo.createid}','${applyInfo.orderid}');
					var title="${applyInfo.examinename}审批了您的申购单,请及时查看";
					var url="/purchase/apply_detail.html?orderid=${applyInfo.orderid}";
					pushMessage('${applyInfo.createid}',title,url);
					goBackPage();
				});
			}
	})
}
</script>
</body>
</html>