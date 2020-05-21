<%@page import="com.collection.util.CookieUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" >
<title>绑定手机</title>
<link href="<%=request.getContextPath() %>/appcssjs/style/public.css" type="text/css" rel="stylesheet">
<link href="<%=request.getContextPath() %>/appcssjs/style/membercenter.css" type="text/css" rel="stylesheet">
<link href="<%=request.getContextPath() %>/sweetalert/dist/sweetalert.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=request.getContextPath() %>/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.8.0.min.js"></script>
</head>
<script type="text/javascript">
function getverification(obj){
	var phone=$('#phone').val();
	var oldcode="";
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/interface/getTestCode',
		success:function(data){
			oldcode=data.testcode;
			var newcode=$('#newcode').val();
			if(newcode.toLowerCase()!=oldcode.toLowerCase()){
				swal("","验证码输入不正确");
				return;
			}
			
			if(phone==""){
				swal("","请先填写手机号再获取");
			}else{
				xians(60,$(obj));
				$.ajax({
					type:'post',
					dataType:'json',
					url:'<%=request.getContextPath()%>/interface/getVerificationCode?phone='+phone+'&type=1',
					success:function(data){
						if(data.status==1){
							swal("","获取验证码失败");
						}
					}
				})
			}
		}
	})
	
}

function xians(i,dom){
  	if(i==0){
  		$(dom).val("获取验证码");
  		$(dom).css("color","#ff7922");
  		$(dom).css("background-color","");
  		$(dom).attr("disabled",false);
  		return;
  	}else{ 
  		$(dom).val(i+"s后再试");
  		$(dom).css("color","black");
  		$(dom).css("background-color","#e9e9e9");
  		$(dom).attr("disabled",true);
  	}
  	setTimeout(function(){
  		xians(i-1,dom);
  	},1000);
  }
  
function subupdate(){
	var phone=$('#phone').val();
	var verification=$('#verification').val();
	
	var newcode=$('#newcode').val();
	if(phone==""){
		swal("","请输入手机号");
		return;
	}
	if(newcode==""){
		swal("","请输入图形码");
		return;
	}
	if(verification==""){
		swal("","请输入验证码");
		return;
	}
}
</script>
<body name="admin">
<!--头部开始-->
<div class="head_box">
	<c:if test="${isWeiXinFrom==null || isWeiXinFrom=='false'}">
		<a class="back" onclick="javascript:history.go(-1);"><img src="<%=request.getContextPath() %>/appcssjs/images/public/t_back.png"></a>
	</c:if>
    <div class="word f-20">绑定手机</div>
    <a class="msg" href="/weixin/msg/messagelist"><img src="<%=request.getContextPath() %>/appcssjs/images/public/t_msg.png"><b class="point" style="display: none;">点</b></a>
</div>
<div class="head_none"></div>
<!--头部结束-->
<div class="password">
	<ul>
    	<li><span>手 机 号</span><input type="text" class="text" id="phone" placeholder="请输入手机号"></li>
    	<li><span></span><img id="vcode2" src="<%=request.getContextPath() %>/vcode/getestVcode?" +new
							Date().getTime() onclick='this.src="<%=request.getContextPath() %>/vcode/getestVcode?"+new Date().getTime()'
							width="76" height="32" /></li>
    	<li><span>图形码</span><input type="text" id="newcode"  value=""  placeholder="请输入图形码"/></li>
    	
        <li><span>验 证 码</span><input type="text" class="text" id="verification" placeholder="请输入"><input type="button" value="获取验证码" class="yzm_btn" onclick="getverification(this)"></li>
    	
    </ul>
    <input type="hidden" value="<%=request.getSession().getAttribute("testcode")%>"/>
</div>
<div class="main_bigbtn"><input type="button" value="确定" onclick="subupdate()"></div>
<form action="<%=request.getContextPath()%>/file/upload" method="post" enctype="multipart/form-data" name="upload_form">
  <label>选择图片文件</label>
  <input name="imgfile" type="file" accept="image/gif, image/jpeg"/>
  <input name="FileDirectory" value="12345lkflksdlkfls" type="hidden"/>
  <input name="type" value="4" type="hidden"/>
  <input name="userid" value="12346" type="hidden"/>
  <input name="upload" type="submit" value="上传" />
</form>
<!--底部导航-->
<%-- <jsp:include page="/public/job_bottom.jsp"></jsp:include> --%>
<!--底部导航-->
</body>
</html>