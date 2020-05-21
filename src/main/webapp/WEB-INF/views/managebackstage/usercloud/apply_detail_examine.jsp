<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>申请反馈信息-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
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
		$('#capacity').parent().parent().find("span").attr("class","bg_hidden");
		$('#capacity').attr('class','active li_active');
	})
</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath()%>/managebackstage/getCloudCapacityList">申请记录列表</a><i>/</i><span>申请反馈信息</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">申请反馈信息<i class="red">待处理</i></span></div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr>
                    <td class="l_text" width="140">公司名称</td>
                    <td>${cloudmap.companyname }</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">申请人</td>
                    <td>${cloudmap.realname }</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">联系电话</td>
                    <td>${cloudmap.phone }</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">申请大小</td>
                    <td><i class="yellow">${cloudmap.memory }</i></td>
                </tr>
                <tr>
                    <td class="l_text" width="140">填写时间</td>
                    <td>${cloudmap.createtime }</td>
                </tr>
                <tr class="noneborder">
                    <td class="l_text" width="140">实际大小</td>
                    <td><input type="text" style="width:70px;" class="text" id="sjmemory" maxlength="7" onkeyup="this.value=value.replace(/[^\d.]/g,'')"   onafterpaste="this.value=value.replace(/[^\d.]/g,'')">&nbsp;G</td>
                </tr>
                <tr class="noneborder">
                    <td class="l_text" width="140">备注</td>
                    <td><textarea class="text_area" placeholder="请输入拒绝或同意原因" id="reason"></textarea></td>
                </tr>
                <tr class="foot_td">
                	<td>&nbsp;</td>
                    <td><a href="javascript:void(0)" onclick="examine(2)" class="a_btn bg_green">同意</a><a href="javascript:void(0)" onclick="examine(0)" class="a_btn bg_red">拒绝</a></td>
                </tr>
            </table>
        </div>
    </div>
</div>

<script>
	function examine(num){
		var param = new Object();
		param.sjmemory = $('#sjmemory').val();
		param.refusereason = $('#reason').val();
		param.status = num;
		param.capacityid = "${cloudmap.capacityid}";
		param.cloudid = "${cloudmap.cloudid}";
		param.companyid = "${cloudmap.companyid}";
		if(parseFloat(param.sjmemory)>10000){
			swal({
			    title: "提示",
			    text: "每次扩容大小不能超过 10000 G！",
			    type: "warning",
			    showCancelButton: false,
			    confirmButtonColor: "#ff7922",
			    confirmButtonText: "确定",
			    closeOnConfirm: true
			}, function(){
			});
			return false;
		}
		if(param.sjmemory == ""){
			swal({
			    title: "提示",
			    text: "申请大小不能为空！",
			    type: "warning",
			    showCancelButton: false,
			    confirmButtonColor: "#ff7922",
			    confirmButtonText: "确定",
			    closeOnConfirm: true
			}, function(){
			});
			return false;
		}
		requestPost("<%=request.getContextPath()%>/managebackstage/updateCloudCapacity",param,function(data){
			if(data.status == 0){
				location.href="<%=request.getContextPath()%>/managebackstage/getCloudCapacityList";
			}else{
				swal("","审核失败！","error");
			}
		});
	}
	
	function requestPost(url,param,callback){
		$.ajax({
			url:url,
			type:"post",
			data:param,
			beforeSend:function(){
				var load = '<div id="load_mask" class="div_mask" style="opacity:0;"></div>'+
					      '<div id="load_loading" class="loading"><img src="../userbackstage/images/public/loading.gif" width="360" height="200" /></div>';
				$("body").after(load);
			},
			success:function(resultMap){
				callback(resultMap);
			},
			complete:function(){
				$('#load_mask').remove();
				$('#load_loading').remove();
			},
			error:function(e){
				
			}
		});
	}
</script>

</body>
</html>
