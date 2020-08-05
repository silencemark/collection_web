<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单管理-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript">
	var pageWidth = window.innerWidth;
	var pageHeight = window.innerHeight;
	var minHeight;
	var usernameWidth;
	if (typeof pageWidth != "number")
	{
		if(document.compatMode == "CSS1Compat")
		{
			pageWidth = document.documentElement.clientWidth;
			pageHeight = document.documentElement.clientHeight;
		}
		else
		{
			pageWidth = document.body.clientWidth;
			pageHeight = document.body.clientHeight;
		}
	}
	minHeight = pageHeight - 77;
	$(document).ready(function(){		
		$(".main_page").css("min-height",minHeight+"px");
		
		usernameWidth = $(".top_username").width();		
		usernameWidth = (160 - usernameWidth)/2;
	    $(".top_username").css("margin-left",usernameWidth+"px");		
		$(".head_box .user_box").hover(
			  function () {
				$(".head_box .user_box .name").addClass("name_border"); //移入
				$(".head_box .user_box .box").show();
			  },
			  function () {
				$(".head_box .user_box .name").removeClass("name_border");//移除
				$(".head_box .user_box .box").hide();
			  }
			);			
});
	
$(document).ready(function(){
	$('#orderlist').parent().parent().find("span").attr("class","bg_hidden");
	$('#orderlist').attr('class','active li_active');
})
	
function examineOrder(orderid, status){
	$("input[name=orderid]").val(orderid);
	$("input[name=status]").val(status);
	swal({
		title : "",
		text : "确认审核拒绝？",
		type : "error",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$('#orderform').submit();a
	})
}



function returnOrder(orderid, status){
	swal({
		title : "",
		text : "确认将任务卡所在订单从买家扣除，退还给卖家用户？",
		type : "error",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath()%>/managebackstage/returnOrder",
			data:{"orderid":orderid},
			success:function(data){
				var message = "success";
				if(data.status == 1){
					var message = "error";
				}
				swal({
					title : "",
					text : data.message,
					type : message,
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.reload();
				})
			}
		})
	})
	
}

function frozenOrder(orderid, status){
	swal({
		title : "",
		text : "确认冻结买家/卖家双方用户？",
		type : "error",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath()%>/managebackstage/frozenOrder",
			data:{"orderid":orderid, "status":status},
			success:function(data){
				var message = "success";
				if(data.status == 1){
					var message = "error";
				}
				swal({
					title : "",
					text : data.message,
					type : message,
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.reload();
				})
			}
		})
	})
	
}

//显示隐藏窗口
function checkHide(rum,selltime,rushtime,buytime,duetime){
	if(rum == 1 ){
		$("#timediv").hide();	
		$(".div_mask").css("display","none");
		$("#selltime").text("");
		$("#rushtime").text("");
		$("#buytime").text("");
		$("#duetime").text("");
	}else{
		$("#timediv").show();	
		$(".div_mask").css("display","block");
		$("#selltime").text(selltime);
		$("#rushtime").text(rushtime);
		$("#buytime").text(buytime);
		$("#duetime").text(duetime);
	}
}

//显示隐藏支付凭证
function checkPayOrderShowHide(rum,payorder){
	if(rum == 1 ){
		$("#payorderdiv").hide();	
		$(".div_mask").css("display","none");
	}else{
		$("#payorderdiv").show();	
		$(".div_mask").css("display","block");
		$("#payorder").attr("src", payorder);
	}
}
</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">抢购/售出订单管理</a><i>/</i><span>订单列表</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">抢购/售出订单列表</span></div>
        <div class="sel_box">
        	<form action="<%=request.getContextPath()%>/managebackstage/getOrderList" method="post">
        		<input type="text" class="text" placeholder="请输入交易订单号" name="ordernum" value="${map.ordernum}"/>
	            <input type="text" class="text" placeholder="请输入买家用户昵称" name="buynickname" value="${map.buynickname }"/>
	            <input type="text" class="text" placeholder="请输入卖家用户昵称" name="sellnickname" value="${map.sellnickname }"/>
	            <input type="text" class="text" placeholder="请输入任务卡名称" name="typename" value="${map.typename }"/>
	            <select class="sel" name="type">
	            	<option>全部订单</option>
	            	<option value="1" <c:if test="${map.type == '1' }">selected="selected"</c:if>>是</option>
	            	<option value="0" <c:if test="${map.type == '0' }">selected="selected"</c:if>>否</option>
	            </select>
	            
	            <select class="sel" name="status">
	            	<option>全部状态</option>
	            	<option value="0" <c:if test="${map.status == '0' }">selected="selected"</c:if>>待出售</option>
	            	<option value="1" <c:if test="${map.status == '1' }">selected="selected"</c:if>>待支付</option>
	            	<option value="2" <c:if test="${map.status == '2' }">selected="selected"</c:if>>待审核</option>
	            	<option value="3" <c:if test="${map.status == '3' }">selected="selected"</c:if>>审核通过</option>
	            	<option value="-1" <c:if test="${map.status == '-1' }">selected="selected"</c:if>>审核失败</option>
	            	<option value="4" <c:if test="${map.status == '4' }">selected="selected"</c:if>>已到期(可出售)</option>
	            	<option value="5" <c:if test="${map.status == '5' }">selected="selected"</c:if>>已过期(无法出售)</option>
	            </select>
	            <input type="text" class="text" placeholder="请输入订单日期"  name="ordertime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${map.ordertime}"/>
	            <input type="submit" value="搜索" class="find_btn"  />
            </form>
            <div class="clear"></div>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
                	<td>交易订单号</td>
                    <td>买家昵称</td>
                    <td>卖家昵称</td>
                    <td>任务卡名称</td>
                    <td>任务卡价格</td>
                    <td>订单类型</td>
                    <td>是否系统订单</td>
                    <td>支付凭证</td>
                    <td>此订单收益价格</td>
                    <td>创建时间</td>
                   	<td>订单状态</td>
                    <td>操作</td>
                </tr>
                <c:forEach items="${list }" var="li">
                	<tr>
	                	<td>${li.ordernum }</td>
	                    <td>${li.buynickname }</td>
	                    <td>${li.sellnickname }</td>
	                    <td>${li.typename }</td>
	                    <td>${li.cardprice }元</td>
	                    <td>${li.ordertype ==1?'普通订单':'兑换订单' }</td>
	                    <td>${li.type==1?"是":"否" }</td>
	                    <td onclick="checkPayOrderShowHide(0,'${li.payorder }');"><img src="${li.payorder }" width="48pxs" height="48px" alt="" /></td>
	                    <td>${li.profitprice }元</td>
	                    <td>${li.createtime }</td>
	                    <c:choose>
	                    	<c:when test="${li.status == 0 }">
	                    		<td><i class="red">待出售</i></td>
	                    		<td><a href="javascript:void(0)" class="blue" onclick="checkHide(0,'${li.selltime}','${li.rushtime}','${li.buytime}','${li.duetime}');">查看时间</a></td>
	                    	</c:when>
	                    	<c:when test="${li.status == 1 }">
	                    		<td><i class="red">待支付</i></td>
	                    		<td><a href="javascript:void(0)" class="blue" onclick="checkHide(0,'${li.selltime}','${li.rushtime}','${li.buytime}','${li.duetime}');">查看时间</a></td>
	                    	</c:when>
	                    	<c:when test="${li.status == 2 }">
	                    		<td><i class="red">待审核</i></td>
	                    		<td style="cursor: pointer;" ><a href="javascript:void(0)" onclick="frozenOrder('${li.orderid}',-2)" class="blue">冻结买/卖家</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="returnOrder('${li.orderid}')" class="blue">订单退还卖家</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="blue" onclick="checkHide(0,'${li.selltime}','${li.rushtime}','${li.buytime}','${li.duetime}');">查看时间</a></td>
	                    		<%-- <td style="cursor: pointer;" ><a href="javascript:void(0)" onclick="examineOrder('${li.orderid}', -1)" class="blue">审核拒绝</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="blue" onclick="checkHide(0,'${li.selltime}','${li.rushtime}','${li.buytime}','${li.duetime}');">查看时间</a></td> --%>
	                    	</c:when>
	                    	<c:when test="${li.status == 3 }">
	                    		<td><i class="red">已审核通过</i></td>
	                    		<td style="cursor: pointer;" ><a href="javascript:void(0)" onclick="frozenOrder('${li.orderid}',-2)" class="blue">冻结买/卖家</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="returnOrder('${li.orderid}')" class="blue">订单退还卖家</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="blue" onclick="checkHide(0,'${li.selltime}','${li.rushtime}','${li.buytime}','${li.duetime}');">查看时间</a></td>
	                    	</c:when>
	                    	<c:when test="${li.status == 4 }">
	                    		<td><i class="red">已到期</i></td>
	                    		<td><a href="javascript:void(0)" class="blue" onclick="checkHide(0,'${li.selltime}','${li.rushtime}','${li.buytime}','${li.duetime}');">查看时间</a></td>
	                    	</c:when>
	                    	<c:when test="${li.status == 5 }">
	                    		<td><i class="red">已过期(历史订单)</i></td>
	                    		<td><a href="javascript:void(0)" class="blue" onclick="checkHide(0,'${li.selltime}','${li.rushtime}','${li.buytime}','${li.duetime}');">查看时间</a></td>
	                    	</c:when>
	                    	<c:when test="${li.status == -2 }">
	                    		<td><i class="red">已冻结双方用户</i></td>
	                    		<td><a href="javascript:void(0)" onclick="frozenOrder('${li.orderid}',3)" class="blue">解冻结买/卖家</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="blue" onclick="checkHide(0,'${li.selltime}','${li.rushtime}','${li.buytime}','${li.duetime}');">查看时间</a></td>
	                    	</c:when>
	                    	<c:otherwise>
	                    		<td><i class="greed">审核失败</i></td>
	                    		<td><a href="javascript:void(0)" class="blue" onclick="checkHide(0,'${li.selltime}','${li.rushtime}','${li.buytime}','${li.duetime}');">查看时间</a></td>
	                    	</c:otherwise>
	                    </c:choose>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div>
    </div>
</div>
<form action="<%=request.getContextPath() %>/managebackstage/updateOrderStatus" id="orderform" method="post">
     <input type="hidden" name="orderid" />
     <input type="hidden" name="status" />
</form>

<div class="div_mask" style="display:none;"></div>

<div class="tc_changetext"  id="timediv"  style="display:none;"><!--修改手机号-->
	<div class="tc_title"><span>相关卖出买出时间</span><a href="#" onclick="checkHide(1,'','','','')">×</a></div>
    <div class="box">
    	<span>预计售出时间</span>
        <i id="selltime"></i>
        <div class="clear"></div>
        <span>抢购时间</span>
        <i id="rushtime"></i>
        <div class="clear"></div>
        <span>购买时间</span>
        <i id="selltime"></i>
        <div class="clear"></div>
        <span>到期时间</span>
        <!-- <input type="text" class="text2" placeholder="请输入新手机号" id="new_phones"/> -->
        <i id="selltime"></i>
        <div class="clear"></div>
    </div>
    <div class="tc_btnbox"><a href="#"  class="bg_yellow" onclick="checkHide(1,'','','','')">确定</a></div>
</div>

<div class="tc_changetext"  id="payorderdiv"  style="display:none;width: 560px;top:40%;"><!--修改手机号-->
	<div class="tc_title"><span>展示支付凭证</span><a href="#" onclick="checkPayOrderShowHide(1,'')">×</a></div>
    <div class="box">
    	<span>支付凭证</span>
        <img id="payorder" width="400px" height="500px"></i>
        <div class="clear"></div>
    </div>
    <div class="tc_btnbox"><a href="#"  class="bg_yellow" onclick="checkPayOrderShowHide(1,'')">确定</a></div>
</div>
</body>
</html>
