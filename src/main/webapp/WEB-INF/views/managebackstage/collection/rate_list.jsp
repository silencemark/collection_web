如果成长值达到下一个值改变会员等级<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>抢购概率管理-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>  
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
	$('#ratelist').parent().parent().find("span").attr("class","bg_hidden");
	$('#ratelist').attr('class','active li_active');
})


//隐藏窗口
function checkHide(num){
	if(num == 1) {
		$('#updateform').submit();
	}else {
		$("#updatediv").hide();	
		$(".div_mask").css("display","none");
		$("#rateid").val("");
		$("#forder").val("");
		$("#minute").val("");
		$("#rate").val("");
		$("#lastflag").val("");
	}
}

//显示隐藏窗口
function update(rateid,forder,minute,rate,lastflag){
	$("#updatediv").show();	
	$(".div_mask").css("display","block");
	$("#rateid").val(rateid);
	$("#forder").val(forder);
	$("#minute").val(minute);
	$("#rate").val(rate);
	$("#lastflag").val(lastflag);
}

</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><span>抢购概率列表</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">抢购概率管理列表</span></div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
                	<td>结算排序数</td>
                    <td>抢购开始后几分钟开始结算</td>
                    <td>抢购成功几率</td>
                    <td>是否最后一次结算</td>
                    <td>创建时间</td>
                    <td>修改时间</td>
                    <td>操作</td>
                </tr>
                <c:forEach items="${list }" var="li">
                	<tr>
	                	<td>第${li.forder }次结算</td>
	                    <td>${li.minute }分钟</td>
	                    <td>${li.rate }%</td>
	                    <td>${li.lastflag == 1?'是':'否' }</td>
	                    <td>${li.createtime }</td>
	                    <td>${li.updatetime }</td>
	                    <td><a href="javascript:void(0)" onclick="update('${li.rateid}','${li.forder}','${li.minute}','${li.rate}','${li.lastflag}')" class="blue">修改</a></td>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div>
    </div>
</div>

<div class="div_mask" style="display:none;"></div>

<div class="tc_changetext"  id="updatediv"  style="display:none;top: 40%;left: 40%; width: 650px;">
	<div class="tc_title"><span>修改抢购概率信息</span><a href="#" onclick="checkHide(0)">×</a></div>
    <form action="<%=request.getContextPath() %>/managebackstage/insertOrUpdateRate" id="updateform" method="post">
    <input type="hidden" name="rateid"  id="rateid"/>
    <div class="box">
        <span>结算排序数</span>
        <input type="text" class="text2"  placeholder="请输入结算排序数"  name="forder" id="forder" />
        <div class="clear"></div>
        <span>几分钟开始结算</span>
        <input type="text" class="text2" placeholder="请输入分钟数" name="minute" id="minute"/>
        <div class="clear"></div>
        <span>抢购成功几率</span>
        <input type="text" class="text2" placeholder="请输入抢购成功几率(%)" name="rate" id="rate"/> 
        <div class="clear"></div>
        <span>是否最后一次结算</span>
        <select class="sel" name="lastflag" id="lastflag">
           	<option value="1">是</option>
           	<option value="0">否</option>
         </select>
        <div class="clear"></div>
    </div>
    </form>
    <div class="tc_btnbox"><a href="#" class="bg_gay2" onclick="checkHide(0)">取消</a><a href="#"  class="bg_yellow" onclick="checkHide(1)">确认</a></div>
</div>
</body>
</html>
