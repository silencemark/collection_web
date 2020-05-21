<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>个人中心—个人信息</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript">

function checkUpdata(isNum){
	var isshowphone;
	if(isNum == 1){
		isshowphone=1;
		 $("#kai").attr("class","radio_ed");
		 $("#bu").attr("class","radio");
		 var phone ='${userInfo.phone}';
	     $("#phone").html(phone);
	    
	}else{
		isshowphone=0;
		 $("#kai").attr("class","radio");
		 $("#bu").attr("class","radio_ed");
		 var phone = '${userInfo.phone}';
	     var mphone = phone.substr(3,4);
	     var phones = phone.replace(mphone,"****");
	     $("#phone").html(phones);
	}
	 $.ajax({
	 		type:'post',
	 		dataType:'json',
	 		url:'/pc/updateOrganizeUserInfo?isshowphone='+isshowphone+"&userid=${userInfo.userid}",
	 	})
}


//打开手机窗口
function checkOpen(rum){
	if(rum == 1 ){
		$("#new_phone").text($("#phone").text());
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
		phone = $("#phe").val();
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
	u_phone =$("#phe").val();
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
		var param =new Object();
		param.userid='${userInfo.userid}';
		param.u_phone=u_phone;
		param.V_Code=V_Code;
		param.phone=phone;
		param.C_Code=C_Code;
		$.ajax({
			type:'post',
			dataType:'json',
			url:'/pc/updataPhoen',
			data:param,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text : data.error,
						type : "error",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						
					});
				}else{
					swal({
						title : "",
						text : "更换手机号成功",
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						location.href="<%=request.getContextPath() %>/pc/memcenter_info";
					});
				}
			}
		})
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
		var param =new Object();
		param.userid='${userInfo.userid}';
		param.email=email;
		$.ajax({
			type:'post',
			dataType:'json',
			url:'/pc/updataEmail',
			data:param,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text : data.error,
						type : "error",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						
					});
				}else{
					swal({
						title : "",
						text : "更换邮箱号成功",
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						location.href="<%=request.getContextPath() %>/pc/memcenter_info";
					});
				}
			}
		})
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

function onloadDate(){

	 $.ajax({
	 		type:'post',
	 		dataType:'json',
	 		url:'/pc/getOrganizeByUserAll?userid=${userInfo.userid}',
	 		success:function(data){
	 			if(data.status==1){
	 				swal({
	 					title : "",
	 					text : "查询失败",
	 					type : "error",
	 					showCancelButton : false,
	 					confirmButtonColor : "#ff7922",
	 					confirmButtonText : "确认",
	 					cancelButtonText : "取消",
	 					closeOnConfirm : true
	 				}, function(){
	 					
	 				});
	 			}else{
	 				$("#img").attr("src",data.userinfo.headimage);
	 				$("#imgleft").attr("src",data.userinfo.headimage);
	 				$("#realname").text(data.userinfo.realname);
	 				if(data.userinfo.sex == 1){
	 					$("#sex").text("男");
	 				}else if(data.userinfo.sex == 0){
	 					$("#sex").text("女");
	 				}
	 				$("#birthday").text(data.userinfo.birthday);
	 				 if(data.userinfo.isshowphone== 0){
	 					 $("#kai").attr("class","radio");
	 					 $("#bu").attr("class","radio_ed");
	 					   var phone = data.userinfo.phone;
	 				       var mphone = phone.substr(3,4);
	 				       var phones = phone.replace(mphone,"****");
	 				       $("#phone").text(phones);
	 				       
	 				 }else{
	 					$("#phone").text(data.userinfo.phone);
	 				 }
	 				$("#phe").val(data.userinfo.phone);
	 				$("#email").text(data.userinfo.email);	
	 				$("#position").text(data.userinfo.position);
	 				if(data.datalist!=null &&data.datalist.length>0){
	 					var temp="";
	 					for(var i = 0; i<data.datalist.length;i++){
	 						if(i > 0){
								temp +=",";
							}
							temp += data.datalist[i].organizename;
	 						
	 					}
	 					$("#organizename").text(temp);		
	 				}else{
	 					$("#organizename").text(data.userinfo.companyname);	
	 				}
	 				
	 			}
	 		}
	 	})
}
$(document).ready(function(){
	$('.link ul').find("li").attr('class','');
	$('#usercenter').attr('class','active');
});
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<div class="left_menu">
    	<jsp:include page="../user.jsp"></jsp:include><!-- 头像and编辑 -->
        <div class="menu_name">个人中心</div>
        <ul class="member_li">
        	<li class="active"><a href="<%=request.getContextPath() %>/pc/memcenter_info" class="bg_01">个人信息</a></li>
            <li><a href="<%=request.getContextPath() %>/pc/notice_list" class="bg_02">系统公告</a></li>
            <li><a href="<%=request.getContextPath() %>/pc/password" class="bg_03">重置密码</a></li>
        </ul>
    </div>
    <div class="right_page">
    	<div class="page_name"><span>个人信息</span><a href="<%=request.getContextPath() %>/pc/memcenter_infoedit">修改</a></div>
        <div class="page_tab2">
            <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">
                <tr>
                    <td class="l_text" width="140">姓名</td>
                    <td width="30" class="img_td"><div class="img"><img src="" id="img" width="30" height="30" /></div></td>
                    <td class="gray_word"><span id="realname" ></span></td>
                </tr>
                <tr>
                    <td class="l_text" width="140">性别</td>
                    <td colspan="2" id="sex" ></td>
                </tr>
                <tr>
                    <td class="l_text" width="140">出生年月</td>
                    <td colspan="2"  id="birthday"></td>
                </tr>
                <tr>
                  <td class="l_text" width="140">手机号</td>
                  <td colspan="2" class="img_td">
                  <i class="info_wid"  id="phone"></i><input type ="hidden" id="phe"/>
                  <a href="#" class="info_btn" onclick="checkOpen(1)">更换手机号</a>
                  <div class="info_xx">
                  <a href="#" class="radio_ed" onclick="checkUpdata('1')"  id="kai">选择</a><i class="i_dp">公开</i>
                  <a href="#" class="radio" onclick="checkUpdata('0')"  id="bu">选择</a><i class="i_dp">隐藏</i></div>
                  </td>
                </tr>
                <tr>
                    <td class="l_text" width="140">电子邮件地址</td>
                    <td colspan="2" ><i class="info_wid" id="email"></i><a href="#" class="info_btn" onclick="checkOpen(2)">更换邮箱</a></td>
                </tr>
                <tr>
                    <td class="l_text" width="140">部门</td>
                    <td colspan="2" ><i class="info_wid" id="organizename" ></i></td>
                </tr>
                <tr>
                    <td class="l_text" width="140">职务</td>
                    <td colspan="2" id="position" ></td>
                </tr>
            </table>
        </div>
        </div>
    </div>
    <div class="clear"></div>
</div>

<div class="div_mask" style="display:none;"></div>
<div class="tc_changetext" id="phonediv" style="display:none;"><!--修改手机号-->
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
        <input type="text" class="text2" placeholder="请输入新手机号" id="new_phones" maxlength="11"/>
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
