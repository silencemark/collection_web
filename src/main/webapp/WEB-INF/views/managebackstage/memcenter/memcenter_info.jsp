<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>个人中心—个人信息</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
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
				$(".head_box .user_box .name").removeClass("name_border");//移除\
				$(".head_box .user_box .box").hide();
			  }
			);			
});
	
	
function onloadData(){
	if('${error}' != ''){
		swal({
			title : "",
			text : "${error}",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
	}

}

function checkUpdata(isNum){
	var isshowphone;
	if(isNum == 1){
		isshowphone=1;
		 $("#kai").attr("class","radio_ed");
		 $("#bu").attr("class","radio");
		 var phone = '${userinfo.phone}';
	     $("#phone").html(phone);
	    
	}else{
		isshowphone=0;
		 $("#kai").attr("class","radio");
		 $("#bu").attr("class","radio_ed");
		 var phone = '${userinfo.phone}';
	     var mphone = phone.substr(3,4);
	     var phones = phone.replace(mphone,"****");
	     $("#phone").html(phones);
	}
	 $.ajax({
	 		type:'post',
	 		dataType:'json',
	 		url:'/managebackstage/updateSystemUserInfo?isshowphone='+isshowphone+"&userid=${userinfo.userid}",
	 		success:function(data){
	 			if(data.status==1){
	 				
	 			}else{
	 				
	 			}
	 		}
	 	})
}


//打开手机窗口
function checkOpen(rum){
	if(rum == 1 ){
		$("#new_phone").text("${userinfo.phone}");
		$("#phonediv").show();	
		$(".div_mask").css("display","block");
	}else{
		$("#new_email").text($("#email").text());
		$("#emaildiv").show();	
		$(".div_mask").css("display","block");
	}
	
}

//隐藏窗口
function checkHide(rum){
	if(rum == 1 ){
		
		$("#phonediv").hide();	
		$(".div_mask").css("display","none");
	}else{
		$("#emaildiv").hide();	
		$(".div_mask").css("display","none");
	}
}

//验证码
function sendSms(i,dom){
  	if(i==0){
  		$(dom).val("获取验证码");
  		$(dom).css("background-color","#a3a3a3");
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

//手机号格式验证
var myreg = /^1\d{10}$/; 
function checkUpdatPhone(rum){
	//rum为1是 发送验证码给原来手机号
	var phone="";
	if(rum == 1){
		phone = $("#new_phone").html();
	}else{
		phone = $("#new_phones").val();
	}
	if(phone.length!= 11 || !myreg.test(phone)){
		swal({
			title : "",
			text : "手机格式不正确",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
	}else{
		$.ajax({
			type:'post',
			dataType:'json',
			url:'/app/getVerificationCode?phone='+phone,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text : "获取验证码失败",
						type : "error",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						
					});
				}else{
					if(rum ==  1){
						sendSms(60,$('#phonebut_a'));
					}else{
						sendSms(60,$('#phonebut_b'));
					}
				}
			}
		})
		
	}
	
}

//修改手机
function checkUpdataP(){
	var phone = "",
	 	C_Code = "" ,
	 	V_Code = "",
	 	mess;
	var num = 1;
	u_phone =$("#new_phone").html();
	phone =$("#new_phones").val();
	V_Code =$("#V_Code").val();//原手机验证码
	C_Code =$("#C_Code").val();//新手机验证码
	if(V_Code == ""){
		mess="验证码不能为空";
		num = 0 ;
	}else if(phone == "" ){
		mess="手机不能为空";
		num = 0 ;
	}else if(phone.length!= 11 || !myreg.test(phone)){
		mess = "手机格式不正确";
		num  = 0;
	}else if(C_Code == ""){
		mess="验证码不能为空";
		num = 0 ;
	}
	
	if(num == 1){
		location.href='/managebackstage/updataPhoen?u_phone='+u_phone+'&V_Code='+V_Code+'&phone='+phone+'&C_Code='+C_Code;
	}else{
		swal({
			title : "",
			text : mess,
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
	}	
}


function checkUpdataE(){
	var email = "",
 	mess;
	var num = 1;
	email =$("#new_emails").val();
	if(email == "" ){
		mess="邮箱不能为空";
		num = 0 ;
	}else if(email.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) == -1){
		mess = "邮箱格式不正确";
		num  = 0;
	}
	
	if(num == 1){
		location.href='/managebackstage/updataEmail?email='+email;
	}else{
		swal({
			title : "",
			text : mess,
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
	}	

}
$(document).ready(function(){
	$('#systemuser').parent().parent().find("span").attr("class","bg_hidden");
	$('#systemuser').attr('class','active li_active');
})

</script>
</head>

<body onload="onloadData()">
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">个人中心</a><i>/</i><span>个人信息</span></p></div>  
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">个人信息</span><a href="<%=request.getContextPath() %>/managebackstage/getPassword" class="wid_01">重置密码</a><a href="<%=request.getContextPath()%>/managebackstage/getSystemUpdataUser">修改</a></div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">
                <tr>
                    <td class="l_text" width="140">姓名</td>
                    <td width="30" class="img_td"><div class="img"><img src="${userinfo.headimage}" width="30" height="30" /></div></td>
                    <td class="gray_word">${userinfo.realname}</td>
                </tr>
                <tr>
                    <td class="l_text" width="140">性别</td>
                    <td colspan="2" id="sex" >
      					  <c:choose>
      					  	 	 <c:when test="${userinfo.sex!=null && userinfo.sex!=undefined && userinfo.sex!='' && userinfo.sex ==1}">
			                            		男
			                      </c:when>
			                       <c:when test="${userinfo.sex!=null && userinfo.sex!=undefined && userinfo.sex!='' && userinfo.sex ==0}">
			                            		女
			                      </c:when>
			                     <c:otherwise>
                    				<td colspan="2" ></td>
                    			</c:otherwise>

      					  </c:choose>
                    </td>
                </tr>
                <tr>
                    <td class="l_text" width="140">出生年月</td>
                    <td colspan="2" >${userinfo.birthday}</td>
                </tr>	
                <tr>
                    <td class="l_text" width="140">手机号</td>
                  <td colspan="2" class="img_td">
                  <i class="info_wid">${userinfo.phone}</i>
                  <a href="#" class="info_btn"  onclick="checkOpen(1)">更换手机号</a><div class="info_xx">
                </tr>
                <tr>
                    <td class="l_text" width="140">电子邮件地址</td>
                    <td colspan="2" ><i class="info_wid" id="email">${userinfo.email}</i>
                    <a href="#" class="info_btn"   onclick="checkOpen(2)">更换邮箱</a></td>
                </tr>
                <tr>
                    <td class="l_text" width="140">地址</td>
                    <td colspan="2" >${userinfo.address}</td>
                </tr>
            </table>
        </div>
    </div>
</div>
<div class="div_mask" style="display:none;"></div>

<div class="tc_changetext"  id="phonediv"  style="display:none;"><!--修改手机号-->
	<div class="tc_title"><span>修改手机号</span><a href="#" onclick="checkHide(1)">×</a></div>
    <div class="box">
    	<span>原手机号</span>
        <i id="new_phone"></i>
        <div class="clear"></div>
        <span>验证码</span>
        <input type="text" class="text" placeholder="请输入短信验证码" id="V_Code"/>
        <input type="button" class="yzm_btn" value="获取验证码" id="phonebut_a" onclick="checkUpdatPhone(1)"/>
        <div class="clear"></div>
        <span>新手机号</span>
        <input type="text" class="text2" placeholder="请输入新手机号" id="new_phones"/>
        <div class="clear"></div>
        <span>验证码</span>
        <input type="text" class="text" placeholder="请输入短信验证码" id="C_Code" />
        <input type="button" class="yzm_btn" value="获取验证码"  id="phonebut_b" onclick="checkUpdatPhone(2)"/>
        <div class="clear"></div>
    </div>
    <div class="tc_btnbox"><a href="#" class="bg_gay2" onclick="checkHide(1)">取消</a><a href="#"  class="bg_yellow" onclick="checkUpdataP()">保存</a></div>
</div>

<div class="tc_changetext"  id="emaildiv"  style="display:none;"><!--邮箱-->
	<div class="tc_title"><span>修改邮箱</span><a href="#" onclick="checkHide(2)">×</a></div>
    <div class="box">
    	<span>原邮箱</span>
        <i id="new_email"></i>
        <div class="clear"></div>
        <span>新邮箱</span>
        <input type="text" class="text2" placeholder="请输入新邮箱号" id="new_emails"/>
         <div class="clear"></div>
    </div>
    <div class="tc_btnbox"><a href="#" class="bg_gay2" onclick="checkHide(2)">取消</a><a href="#"  class="bg_yellow" onclick="checkUpdataE()">保存</a></div>
</div>

</body>
</html>
