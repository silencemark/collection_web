<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>添加修改管理员-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>  


</head>
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
function submitdata(){
	var userid=$('input[name=userid]').val();
	var headimage=$('input[name=headimage]').val();
	if(headimage==""){
		swal({
			title : "",
			text : "请上传您的头像",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		})
		return;
	}
	var realname=$('input[name=realname]').val();
	if(realname==""){
		swal({
			title : "",
			text : "请填写您的姓名",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		})
		return;
	}
	var birthday=$('input[name=birthday]').val();
	var phone=$('input[name=phone]').val();
	if(phone==""){
		swal({
			title : "",
			text : "请填写您的手机号码",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		})
		return;
	}
	var email=$('input[name=email]').val();
	if(phone != ""){
		if(!(/^1[3|4|5|7|8]\d{9}$/.test(phone))){
			swal({
				title : "",
				text : "手机号码有误，请重填",
				type : "error",
				showCancelButton : false,
				confirmButtonColor : "#ff7922",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			}, function(){
			})
			return; 
		}
	}
	if(email != ""){
		var patten = new RegExp(/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/); 
		if(!patten.test(email)){
			swal({
				title : "",
				text : "请输入正确的邮箱",
				type : "error",
				showCancelButton : false,
				confirmButtonColor : "#ff7922",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			}, function(){
			})
			return;
		}
	}
	var address=$('input[name=address]').val();
	if(userid!=""){
		//修改
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath()%>/managebackstage/updateAdminUser",
			data:$('#userform').serialize(),
			success:function(data){
				var typename="success";
				if(data.status==1){
					typename="error";
				}
				swal({
					title : "",
					text : data.message,
					type : typename,
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
	}else{
		//添加
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath()%>/managebackstage/insertAdminUser",
			data:$('#userform').serialize(),
			success:function(data){
				var typename="success";
				if(data.status==1){
					typename="error";
				}
				swal({
					title : "",
					text : data.message,
					type : typename,
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
	}
}
$(document).ready(function(){
	$('#adminuser').parent().parent().find("span").attr("class","bg_hidden");
	$('#adminuser').attr('class','active li_active');
})
</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
    <div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">管理员管理</a><i>/</i><span>添加修改管理员</span></p></div>        
    <div class="page_tab">
        <div class="tab_name">
        <c:choose>
        	<c:when test="${userInfo==null}">
        	<span class="gray1">添加管理员</span></div>
        	</c:when>
        	<c:otherwise>
        	<span class="gray1">修改管理员</span></div>
        	</c:otherwise>
        </c:choose>
        <form action="" id="userform">
        <input type="hidden" name="userid" value="${userInfo.userid}"/>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr class="noneborder">
                    <td class="l_text" width="140">头像</td>
                    <input type="hidden" name="headimage" value="${userInfo.headimage}"/>
                    <td width="48" class="img_td"><div class="img3"><img src="${userInfo.headimage}" onclick="$('#fileName').click();" width="48" height="48" id="headimageurl"/></div></td>
                    <td class="gray_word"><a href="javascript:void(0)" onclick="$('#fileName').click();" class="yellow">修改</a></td>
                </tr>
                <tr class="noneborder">
                    <td class="l_text" width="140">姓名</td>
                    <td colspan="2" ><input type="text" class="text" placeholder="请输入姓名"  name="realname" value="${userInfo.realname}"/></td>
                </tr>
                <tr class="noneborder">
                    <td class="l_text" width="140">性别</td>
                    <input type="hidden" name="sex" value="${userInfo.sex==null?1:userInfo.sex}"/>
                    <td colspan="2" >
                    <c:choose>
                    	<c:when test="${userInfo.sex==0}">
	                    	<a href="javascript:void(0)" class="radio"  onclick="changesex(this,1)">选择</a><i class="i_dp">男</i>
	                    	<a href="javascript:void(0)" class="radio_ed"  onclick="changesex(this,0)">选择</a><i class="i_dp">女</i>
                    	</c:when>
                    	<c:otherwise>
	                    	<a href="javascript:void(0)" class="radio_ed" onclick="changesex(this,1)">选择</a><i class="i_dp"  >男</i>
	                    	<a href="javascript:void(0)" class="radio"  onclick="changesex(this,0)">选择</a><i class="i_dp" >女</i>
                    	</c:otherwise>
                    </c:choose>
                    </td>
                </tr>
                <tr class="noneborder">
                    <td class="l_text" width="140">出生年月</td>
                    <td colspan="2" ><input type="text" class="text" placeholder="请输入出生年月"  name="birthday" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${userInfo.birthday}"/></td>
                </tr>
                <tr class="noneborder">
                    <td class="l_text" width="140">手机号</td>
                    <td colspan="2" ><input type="text" class="text" name="phone" placeholder="请输入手机号" value="${userInfo.phone}"/></td>
                </tr>
                <tr class="noneborder">
                    <td class="l_text" width="140">电子邮件地址</td>
                    <td colspan="2" ><input type="text" class="text" name="email" placeholder="请输入电子邮件地址" value="${userInfo.email}"/></td>
                </tr>
                <tr class="noneborder">
                    <td class="l_text" width="140">地址</td>
                    <td colspan="2" ><input type="text" class="text2" name="address" placeholder="请输入地址" value="${userInfo.address}"/></td>
                </tr>
                <tr class="foot_td">
                	<td>&nbsp;</td>
                    <td colspan="2"><a href="javascript:void(0)" onclick="submitdata()" class="a_btn bg_yellow">保存</a><a href="javascript:void(0)" onclick="window.history.go(-1);" class="a_btn bg_gay2">取消</a></td>
                </tr>
            </table>
        </div>
        </form>
    </div>
</div>
<input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/>
<script type="text/javascript">
function changesex(obj,sex){
	$(obj).parent().find("a").attr("class","radio");
	$(obj).attr("class","radio_ed");
	$("input[name=sex]").val(sex);
}
function ajaxFileUpload(id,Fileid,noimg){
	if(!Fileid){
		Fileid = "fileName";
	}
	hhutil.ajaxFileUpload("<%=request.getContextPath()%>/upload/manageheadimg",Fileid,function(data){
			if(data.imgkey){
				$("#headimageurl").attr("src",data.imgkey);
				$("input[name=headimage]").val(data.imgkey);
			}else{
				swal({
					title : "",
					text : "图片上传失败",
					type : "error",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
				})
				return;
			}
		
	});
}
</script>
</body>
</html>