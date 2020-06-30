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
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
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
				$(".head_box .user_box .name").addClass("name_border"); //ç§»å¥
				$(".head_box .user_box .box").show();
			  },
			  function () {
				$(".head_box .user_box .name").removeClass("name_border");//ç§»é¤\
				$(".head_box .user_box .box").hide();
			  }
			);			
});
var realname = "",
	position = "",
	birthday ="";
function onloadData(){
	 var sexs=${userinfo.sex!=null && userinfo.sex!=undefined && userinfo.sex!=''?userinfo.sex:'1'};
	 if(sexs == 1 ){
		 $("#sex").val(1);
		 $("#nan").attr("class","radio_ed");
		 $("#nv").attr("class","radio");
	 }else if(sexs == 0){
		 $("#sex").val(0);
		 $("#nan").attr("class","radio");
		 $("#nv").attr("class","radio_ed");
	 }
}
//切换性别
function checkUpdata(sexs){
	 if(sexs == 1){
		 $("#nan").attr("class","radio_ed");
		 $("#nv").attr("class","radio");
		 $("#sex").val(1);
	 }else if(sexs == 0){
		 $("#nan").attr("class","radio");
		 $("#nv").attr("class","radio_ed");
		 $("#sex").val(0);
	 }
}
//保存按钮   修改
function checkUpdataUserInfo(){
	var num = 1,
		mess= "";
	realname = $("#realname").val();
	address = $("#address").val();
	birthday = $("#birthday").val();
	var img = $("#img").attr("src");
	if(realname == "" ){
		mess="请输入姓名";
		num = 0;
	}else if(birthday == "" ){
		amess="请选择你的生日";
		num = 0;
	}else if(address == "" ){
		mess="请输入地址";
		num = 0;
	}else
	
	if(num == 1){
		$("#updateform").submit();
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
function uploadbanner(){
	$('#fileName').click();
}

function ajaxFileUpload(id,Fileid,noimg){
	if(!Fileid){
		Fileid = "fileName";
	}
	hhutil.ajaxFileUpload("<%=request.getContextPath()%>/upload/manageheadimg",Fileid,function(data){
			if(data.imgkey){
				$("#img").attr("src",data.imgkey);
				$("#headimage").val(data.imgkey);s
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
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">个人中心</a><i>/</i><a href="#">个人信息</a><i>/</i><span>修改</span></p></div>  
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">修改个人信息</span></div>
        <div class="tab_list">
        	<form action="<%=request.getContextPath()%>/managebackstage/updateSystemUserInfo" method="post" id="updateform">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">
                <tr class="noneborder">
                    <td class="l_text" width="140">头像</td>
                    <td width="48" class="img_td"><div class="img3"><img src="${userinfo.headimage}" width="48" height="48" id="img"/></div></td>
                    <td class="gray_word"><a href="#" class="yellow" onclick="uploadbanner()">修改</a></td>
                    <input type="hidden" name="headimage" id ="headimage"/>
                    <input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/>
                </tr>
                <tr class="noneborder">
                    <td class="l_text" width="140">姓名</td>
                    <td colspan="2" ><input type="text" class="text" placeholder="请输入姓名" name="realname" id="realname" value="${userinfo.realname}"/></td>
                </tr>
                <tr class="noneborder">
                	<input type="hidden" name="sex" id ="sex"/>
                    <td class="l_text" width="140">性别</td>
                    <td colspan="2" >
                    <a href="#" class="radio_ed" id="nan" onclick="checkUpdata(1)">选择</a>
                    <i class="i_dp">男</i>
                    <a href="#" class="radio" id="nv" onclick="checkUpdata(0)">选择</a>
                    <i class="i_dp">女</i></td>
                </tr>
                <tr class="noneborder">
                    <td class="l_text" width="140">出生年月</td>
                   <td colspan="2"><input onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="birthday" value="${userinfo.birthday}"  class="text" type="text" placeholder="出生年月" id="birthday"/></td>
                </tr>
                <tr class="noneborder">
                    <td class="l_text" width="140">地址</td>
                    <td colspan="2" ><input type="text" class="text" placeholder="请输入地址" name="address" value="${userinfo.address}" id="address" /></td>
                </tr>                
                <tr class="foot_td">
                	<td>&nbsp;</td>
                    <td colspan="2"><a href="#" class="a_btn bg_yellow" onclick="checkUpdataUserInfo()">保存</a><a href="<%=request.getContextPath() %>/managebackstage/getSystemUser" class="a_btn bg_gay2">取消</a></td>
                </tr>
            </table>
            </form>
        </div>
    </div>
</div>



</form>

</body>
</html>
