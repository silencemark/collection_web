<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>忘记密码-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  

</head>
<script type="text/javascript">
function sendSms(i,dom){
  	if(i==0){
  		$(dom).val("获取验证码");
  		$(dom).css("background-color","#bdd484");
  		$(dom).attr("disabled",false);
  		return;
  	}else{ 
  		$(dom).val(i+"秒后重新发送");
  		$(dom).css("background-color","#ccc");
  		$(dom).attr("disabled",true);
  	}
  	setTimeout(function(){
  		sendSms(i-1,dom);
  	},1000);
  }
function getvalidecode(){
	var phone=$('#phone').val();
	if(!(/^1[3|4|5|7|8]\d{9}$/.test(phone))){ 
		swal({
			title : "",
			text : "请输入正确的手机号码",
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
		url:'<%=request.getContextPath() %>/managebackstage/getVerificationCode',
		data:'phone='+phone,
		success:function(data){
			if(data.status==1){
				swal({
					title : "",
					text : data.message,
					type : "error",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					
				});
			}else{
				sendSms(60,$('#sendbtn'));
			}
		}
	})
}
function nextstep(){
	var phone=$('#phone').val();
	var code=$('#code').val();
	if(phone==""){
		swal({
			title : "",
			text : "请输入手机号码",
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
	if(code==""){
		swal({
			title : "",
			text : "请输入验证码",
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
		url:'<%=request.getContextPath() %>/managebackstage/checkValide',
		data:'phone='+phone+'&code='+code,
		success:function(data){
			if(data.status==1){
				swal({
					title : "",
					text : data.message,
					type : "error",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					
				});
			}else{
				$('input[name=phone]').val(phone);
				$('#resetform').submit();
			}
		}
	})
}
</script>
<body>
<form action="<%=request.getContextPath() %>/managebackstage/initreset" method="post" id="resetform">
<input type="hidden" name="phone" />
</form>
<div class="login_box l_height">
	<div class="name">CateringMaster</div>
    <div class="text"><input type="text" placeholder="管理员手机号" id="phone"/></div>
    <div class="text">
    	<input type="text" placeholder="短信验证码" class="wid_01" id="code"/>
    	<input type="button" value="获取验证码" class="yzm_btn" id="sendbtn" onclick="getvalidecode()"/>
    	<div class="clear"></div>
    </div>
    <div class="btn"><input type="button" value="下一步" onclick="nextstep()"  /></div>    
    <div class="link2"><a href="<%=request.getContextPath() %>/managebackstage/login">返回登陆？</a></div>
</div>
</body>
</html>