<%@page import="com.collection.util.UserUtil"%>
<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加备忘录</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
</head>

<script type="text/javascript">
$(document).ready(function(){
	$('#memorandum').parent().find("li").attr('class','');
	$('#memorandum').attr('class','active');
})
function onloadData(){
	if('${map.datetime}' != ''){
		var datehour='${map.datetime}';
		var datestr=datehour.split(" ");
		$('#datetime').val(datestr[0]);
		$('#hourtime').val(datestr[1]);	
	}
}

function savememorandum(){
	var datetime=$('#datetime').val();
	var hourtime=$('#hourtime').val();
	var content=$('#content').val();
	if(datetime=="" || hourtime==""){
		swal({
			title : "",
			text : "请选择提醒时间",
			type : "error",
			showCancelButton : true,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$('#datehour').click();
		});
		return;
	}
	if(content==""){
		swal({
			title : "",
			text : "请填写提醒内容",
			type : "error",
			showCancelButton : true,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$('#content').focus();
		});
		return;
	}
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/app/insertMemorandum',
		data:'companyid=${userInfo.companyid}&createid=${userInfo.userid}&datetime='+datetime+'&hourtime='+hourtime+'&content='+content,
		success:function(data){
			if(data.status==0){
				swal({
					title : "",
					text : "添加成功",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href='<%=request.getContextPath()%>/pc/calendar?datetime='+$('#datetime').val();
				});
			}
		}
	})
}
function changedate(){
	var datehour=$('#datehour').val();
	var datestr=datehour.split(" ");
	$('#datetime').val(datestr[0]);
	$('#hourtime').val(datestr[1]);	
}
</script>
<body onload="onloadData()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
    <%Map<String,Object> userInfo=UserUtil.getPCUser(request); %>
	<div class="left_menu">
    	<div class="user_xx">
        	<div class="img"><img src="<%=userInfo.get("headimage") %>" width="128" height="128" /></div>
            <div class="name"><span><%=userInfo.get("realname") %></span><a href="#">编辑</a></div>
        </div>
        <div class="menu_name">备忘录</div>
        <ul class="memo_li">
        	<li class="active"><a href="<%=request.getContextPath()%>/pc/getMemorandumListToday" class="bg_01">备忘录</a></li>
        </ul>
    </div>
    <div class="right_page">
    	<div class="page_name"><span>新建备忘录</span><a href="<%=request.getContextPath()%>/pc/calendar" class="ico_calendar"><img src="../userbackstage/images/pc_page/ico_cal.png" width="24" height="24" alt="日历" /></a></div>
        <div class="page_tab2">
        	<div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="none_border">
                    <tr>
                    	<td class="l_name">提醒时间</td>
                    	<input type="hidden" id="datetime"/>
						<input type="hidden" id="hourtime"/>
                        <td><input type="text" class="text" placeholder="请选择时间"  id="datehour" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" onchange="changedate()" value="${map.datetime}"/></td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name2">事由</td>
                        <td>
                        	<textarea class="text_area" placeholder="请输入事由，最多允许输入800字符" maxlength="800" id="content" maxlength="200"></textarea>
                        </td>
                    </tr>
                    <tr class="foot_td">
                    	<td>&nbsp;</td>
                        <td><a href="javascript:void(0)" class="a_btn bg_yellow" onclick="savememorandum()">保存</a><a href="javascript:void(0)"  onclick="goBackPage();" class="a_btn bg_gay2">取消</a></td>
                    </tr>
               </table>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>
</body>
</html>