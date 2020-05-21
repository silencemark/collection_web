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
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<%-- <script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>   --%>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>

<!-- 上传需要js -->      
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.ui.widget.js"></script>
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.iframe-transport.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload.js"></script>  
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-ui.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-process.js"></script>   
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-validate.js"></script>  

<script type="text/javascript">
var sex;

function uploadbanner(){
	$('#file_upload').click();
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
	 				$("#realname").val(data.userinfo.realname);
	 				if(data.userinfo.sex == 1){
	 						 sex = 1;
	 						 $("#nan").attr("class","radio_ed");
	 						 $("#nv").attr("class","radio");
	 				 }else if(data.userinfo.sex  == 0){
	 						 sex = 0;
	 						 $("#nan").attr("class","radio");
	 						 $("#nv").attr("class","radio_ed");
	 				}
	 				$("#birthday").val(data.userinfo.birthday);
	 				$("#position").val(data.userinfo.position);
 					$("#img").attr("src",data.userinfo.headimage);
 					$("#imgleft").attr("src",data.userinfo.headimage);
	 			}
	 		}
	 	})
}

//切换性别
function checkUpdata(sexs){
	 if(sexs == 1){
		 $("#nan").attr("class","radio_ed");
		 $("#nv").attr("class","radio");
		 sex = 1;
	 }else if(sexs == 0){
		 sex = 0;
		 $("#nan").attr("class","radio");
		 $("#nv").attr("class","radio_ed");
	 }
}

//保存按钮   修改
function checkUpdataUserInfo(){
	var mess= "";
	var realname = "",position = "",birthday ="";
	realname = $("#realname").val();
	position = $("#position").val();
	birthday = $("#birthday").val();
	var img = $("#img").attr("src");
	if(realname == "" ){
		swal({
			title : "",
			text : "请输入姓名",
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
	if(birthday == "" ){
		swal({
			title : "",
			text : "请选择你的生日",
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
// 	if(position == "" ){
// 		swal({
// 			title : "",
// 			text : "请输入你的职务",
// 			type : "error",
// 			showCancelButton : false,
// 			confirmButtonColor : "#ff7922",
// 			confirmButtonText : "确认",
// 			cancelButtonText : "取消",
// 			closeOnConfirm : true
// 		}, function(){
			
// 		});
// 		return;
// 	}
		if(position == "" ){
			position=" ";
		}
		var param = new Object();
		param.userid='${userInfo.userid}';
		param.realname=realname;
		param.position=position;
		param.birthday=birthday;
		param.sex=sex;
		param.headimage=img;
		$.ajax({
			type:'post',
			url:'/pc/updateOrganizeUserInfo',
			data:param,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text : "修改失败",
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
						text : "修改成功",
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
}
$(document).ready(function(){
	$('.link ul').find("li").attr('class','');
	$('#usercenter').attr('class','active');
	
	$('#file_upload').fileupload({    
		url:'<%=request.getContextPath() %>/upload/imageUpload?type=1', 
		formData:{
			fileName:'myfiles'   
		},
		type:'post',
		maxNumberOfFiles:1,    
		autoUpload:true,
        dataType: 'json',
	        acceptFileTypes:  /(\.|\/)(jpeg|tiff|psd|png|swf|svg|pcx|dxf|wmf|emf|lic|eps|tga|bmp|jpg|jpeg2000|gif)$/i, 
        maxFileSize: 419430400,  
        done: function (e, data) { 
        	data = data.result;
        	$("#img").attr("src",data.url);
        }
    });
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
        	<li	class="active"><a href="<%=request.getContextPath() %>/pc/memcenter_info" class="bg_01">个人信息</a></li>
            <li ><a href="<%=request.getContextPath() %>/pc/notice_list" class="bg_02">系统公告</a></li>
            <li><a href="<%=request.getContextPath() %>/pc/password" class="bg_03">重置密码</a></li>
        </ul>
    </div>
    <div class="right_page">
    	<div class="page_name"><span>修改个人信息</span></div>
        <div class="page_tab2">
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">
                    <tr class="noneborder">
<!--                         <input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/> -->
<input type="file" style="display: none" name="myfiles" id="file_upload" multiple/>
                        <td class="l_text" width="140">头像</td>
                        <td width="48" class="img_td"><div class="img3"><img src="" width="48" height="48" id="img"/></div></td>
                        <td class="gray_word"><a href="#" class="yellow" onclick="uploadbanner()">修改</a></td>
                    </tr>
                    <tr class="noneborder">
                        <td class="l_text" width="140">姓名</td>
                        <td colspan="2" ><input type="text" class="text" placeholder="请输入姓名"  id="realname"/></td>
                    </tr>
                      <tr class="noneborder">
		                    <td class="l_text" width="140">性别</td>
		                    <td colspan="2" >
		                    <a href="#" class="radio_ed" id="nan" onclick="checkUpdata(1)">选择</a>
		                    <i class="i_dp">男</i>
		                    <a href="#" class="radio" id="nv" onclick="checkUpdata(0)">选择</a>
		                    <i class="i_dp">女</i></td>
                		</tr>
                    <tr class="noneborder">
                        <td class="l_text" width="140">出生年月</td>
                         <td colspan="2">
                         <input onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="endtime" value="${userinfo.birthday}" id="birthday"  class="text" type="text" placeholder="请输入出生年月" readonly="readonly"/>
                         </td>
                    </tr>
                    <tr class="noneborder">
                        <td class="l_text" width="140">职务</td>
                        <td colspan="2" ><input type="text" class="text" placeholder="请输入职务" id="position" ></td>
                    </tr>                
                    <tr class="foot_td">
                        <td>&nbsp;</td>
                        <td colspan="2"><a href="#" class="a_btn bg_yellow" onclick="checkUpdataUserInfo()">保存</a><a href="<%=request.getContextPath() %>/pc/memcenter_info" class="a_btn bg_gay2">取消</a></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>


</body>
</html>
