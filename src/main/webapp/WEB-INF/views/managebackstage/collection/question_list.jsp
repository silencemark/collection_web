<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>投诉与建议管理-管理方后台</title>
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
	$('#questionlist').parent().parent().find("span").attr("class","bg_hidden");
	$('#questionlist').attr('class','active li_active');
})


//隐藏窗口
function checkHide(num){
	if(num == 1) {
		$('#updateform').submit();
	}else {
		$("#updatediv").hide();	
		$(".div_mask").css("display","none");
		$("#questionid").val("");
		$("#replycontent").val("");
	}
}

//显示隐藏窗口
function update(questionid, questioncontent, replycontent){
	$("#updatediv").show();	
	$(".div_mask").css("display","block");
	$("#questionid").val(questionid);
	$("#questioncontent").val(questioncontent);
	$("#replycontent").val(replycontent);
}

</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">投诉与建议管理</a><i>/</i><span>投诉与建议列表</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">投诉与建议管理列表</span></div>
        <div class="sel_box">
        	<form action="<%=request.getContextPath()%>/managebackstage/getQuestionList" method="post">
        		<input type="text" class="text" placeholder="请输入会员昵称" name="nickname" value="${map.nickname}"/>
        		<select class="sel" name="qtype">
	            	<option>全部问题类型 </option>
	            	<option value="0" <c:if test="${map.qtype == '0' }">selected="selected"</c:if>>App使用问题</option>
	            	<option value="1" <c:if test="${map.qtype == '1' }">selected="selected"</c:if>>影片问题</option>
	            	<option value="2" <c:if test="${map.qtype == '2' }">selected="selected"</c:if>>交易问题</option>
	            	<option value="3" <c:if test="${map.qtype == '3' }">selected="selected"</c:if>>其他问题</option>
	            </select>
	            <input type="submit" value="搜索" class="find_btn"  />
            </form>
            <div class="clear"></div>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
                	<td>用户昵称</td>
                	<td>问题类型</td>
                    <td width="20%">提问内容</td>
                    <td>提问时间</td>
                    <td width="20%">回复内容</td>
                    <td>回复时间</td>
                    <td>操作</td>
                </tr>
                <c:forEach items="${list }" var="li">
                	<tr>
	                	<td>${li.nickname }</td>
	                	<td><c:choose>
	                    	<c:when test="${li.qtype == 0 }">
	                    		App使用问题
	                    	</c:when>
	                    	<c:when test="${li.qtype == 1 }">
	                    		影片问题
	                    	</c:when>
	                    	<c:when test="${li.qtype == 2 }">
	                    		交易问题
	                    	</c:when>
	                    	<c:when test="${li.qtype == 3 }">
	                    		其他问题
	                    	</c:when>
	                    </c:choose></td>
	                    <td>${li.questioncontent }</td>
	                    <td>${li.createtime }</td>
	                    <td>${li.replycontent }</td>
	                    <td>${li.updatetime }</td>
	                    <td>
	                    <c:choose>
	                    	<c:when test="${li.replycontent == null || li.replycontent == '' }">
	                    		<a href="javascript:void(0)" onclick="update('${li.questionid}','${li.questioncontent}', '')" class="blue">回复</a>
	                    	</c:when>
	                    	<c:otherwise>
	                    		<a href="javascript:void(0)" onclick="update('${li.questionid}','${li.questioncontent}', '${li.replycontent}')" class="blue">修改回复</a>
	                    	</c:otherwise>
	                    </c:choose></td>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div>
    </div>
</div>

<div class="div_mask" style="display:none;"></div>

<div class="tc_changetext"  id="updatediv"  style="display:none;top: 40%;left: 40%; width: 650px;">
	<div class="tc_title"><span>回复投诉与建议</span><a href="#" onclick="checkHide(0)">×</a></div>
    <form action="<%=request.getContextPath() %>/managebackstage/replyQuestion" id="updateform" method="post">
    <input type="hidden" name="questionid"  id="questionid"/>
    <div class="box">
        <span>提问内容：</span>
        <textarea  maxlength="800" cols="43" style="border: 1px solid #eee;" rows="3" id="questioncontent" readonly="readonly"></textarea>
        <div class="clear"></div>
        <span>回复内容：</span>
        <textarea placeholder="请输入回复内容" maxlength="800" cols="43" style="border: 1px solid #eee;" rows="5" name="replycontent" id="replycontent"></textarea>
        <div class="clear"></div>
    </div>
    </form>
    <div class="tc_btnbox"><a href="#" class="bg_gay2" onclick="checkHide(0)">取消</a><a href="#"  class="bg_yellow" onclick="checkHide(1)">确认</a></div>
</div>
</body>
</html>
