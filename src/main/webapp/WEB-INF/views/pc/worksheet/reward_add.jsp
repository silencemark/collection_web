<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>奖励单</title>
<link
	href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css"
	type="text/css" rel="stylesheet" />
<link
	href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css"
	type="text/css" rel="stylesheet" />
<script type="text/javascript"
	src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet"
	href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css" />
<script
	src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/change_releaserange.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/change_examineuserinfo.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/change_rewarduserinfo.js"></script>
		<!-- 上传图片需要js -->   
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<%-- <script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>   --%>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/pc_chaosongren.js"></script>

<!-- 上传需要js -->      
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.ui.widget.js"></script>
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.iframe-transport.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload.js"></script>  
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-ui.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-process.js"></script>   
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-validate.js"></script>  

<script type="text/javascript">

function checkFileImg(){
 	if($("#imglist img[name=fileimg]").length<3){
 		$('#fileName').click();
 	}else{
 		swal({
			title : "",
			text : "图片只能上传3张",
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

$(document).ready(function() {
	$('.sup_li').find("li").attr('class', '');
	$('#reward').attr('class', 'active');
	$('#homepage').parent().find("li").attr('class', '');
	$('#homepage').attr('class', 'active');
	
	$('#fileName').fileupload({    
		url:'<%=request.getContextPath() %>/upload/imageUpload?type=3', 
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
        	var html = '<div class="img_box">'+
							'<b><img onclick="showBigImagePC(this)" name="fileimg" src="<%=request.getContextPath()%>'+data.url+'" url="'+data.url+'"></b>'+
							'<div class="del" onclick="$(this).parent().remove();">删除</div>'+
						'</div>';
				$('#imglist').append(html);
        }
    });
})

var number=1;
function checkAdd(row){
	
	$('#tc_selrange2').show();	$('.div_mask').show();
	if(row==1){
	    $('#title').text("请选择奖励人"); 
	}else if(row==2){ 
		$('#title').text("请选择审批人");
	}
	number = row;
}
function adduser2(obj,userid,realname,headimage,position,organizename){

	$('.div_mask').hide();
	$('.tc_selrange').hide();
	
	
	if(number==1){
		$("#resouceid").text(realname);
		$('#rewarduserid').val(userid);
		$("#organizename").val(organizename);
		$("#position").val(position!=null && position!=undefined && position!="undefined"?position:"");
		return ;
	}else if(number == 2){
		$('#examinename').text(realname);
		$('#examineuserid').val(userid);
		var htm = '<div class="img f">'+
						'<img src="<%=request.getContextPath() %>'+headimage+'" id="examineheadimage" width="30" height="30" />'+
					'</div>';
		$('#examineuser').html(htm);
		return ;
	
	}
	
}

$(function(){
	/* var img = "${userInfo.headimage}";
	var realname = "${userInfo.realname}"; */
	<%-- $('#createheadimage').attr("src",<%=request.getContextPath()%>+img);
	$('#createname').text(realname); --%>

	$.ajax({
		url:"/pc/getRewardTypeListInfo",
		type:"post",
		data:"",
		success:function(resultMap){
			if(resultMap != undefined && resultMap != null){
				if(resultMap.status == '0' || resultMap.status == 0){
					var rewardtypelist = resultMap.data;
					if(rewardtypelist != null && rewardtypelist.length > 0){
						var html = '';
						$.each(rewardtypelist,function(i,map){
							html += '<option value="'+map.dataid+'">'+map.cname+'</option>';
						});
						html += '<option value="">自定义</option>';
						$('#rewardtype').html(html);
					}
				}
			}
		}
	});
	
	$('#rewardtype').change(function(){
		var type = $(this).val();
		if(type == ""){
			$('#resultText').show();
			$('#resultText').val("");
		}else{
			$('#resultText').hide();
			$('#resultText').val("");
		}
	});
});
</script>
</head>


<body>
	<jsp:include page="../top.jsp"></jsp:include>
	<input type="hidden" id="userid" value="${userInfo.userid }">
   	<input type="hidden" id="realname" value="${userInfo.realname }"/>
   	<input type="hidden" id="companyid" value="${userInfo.companyid }"/>
	<input type="hidden" id="organizeid" value="${userInfo.organizeid }"/>
<!-- 	<input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/>    -->
<input type="file" style="display: none" name="myfiles" id="fileName" multiple/>
	<div class="page_main">
		<jsp:include page="left.jsp"></jsp:include>
		<div class="right_page">
			<div class="page_name">
				<span>新建奖励单</span><a
					href="<%=request.getContextPath() %>/pc/getRewardListInfo"
					class="back">返回</a>
			</div>
			<div class="page_tab2">
				<div class="tab_list">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="none_border">
						<tr>
							<td class="l_name">姓名</td>
								<input type="hidden" id="rewarduserid"/>
							<td class="num_xx"><div class="img f"><img src="../userbackstage/images/pc_page/user_img2.png" id="rewardheadimage" width="30" height="30" /></div><i class="i_name" id="resouceid"></i><a
								href="#" class="add_user" onclick="openOrCloseExamineDiv1()">添加</a></td>
						</tr>
						<tr>
							<td class="l_name">部门</td>
							<td><input class="text" name="organizename" readonly="readonly"
								id="organizename"></input></td>
						</tr>
						<tr>
							<td class="l_name">职务</td>
							<td><input class="text" name="position" id="position" readonly="readonly"></input></td>
						</tr>
						<tr>
							<td class="l_name">奖励结果</td>
							<td><select class="text" id="rewardtype">
								
							</select>&nbsp;<input class="text" id="resultText" style="display: none;" placeholder="自定义结果"/></td>
						</tr>
						<tr>
							<td class="l_name">发布范围</td>
							<td><i class="i_name" id="releaserange"></i><a
								href="javascript:void(0)"
								onclick="openOrCloseReleaseRangeDiv()"
								class="add_user">添加</a></td>
						</tr>
						<tr class="last_td">
							<td class="l_name2">奖励原因</td>
							<td><textarea id="reason" class="text_area"
									placeholder="请输入奖励原因，最多允许输入800字符" maxlength="800"></textarea>
								<div class="clear"></div>
								<div class="img_list">
									<div id="imglist">
										
									</div>
									<div class="img_add" onclick="checkFileImg()">添加图片</div>
									<div class="clear"></div>
								</div>
							</td>
						</tr>
						<tr class="first_td">
							<td class="l_name">审批人</td>
							<input type="hidden" id="examineuserid" >
                        	<td class="img_td"><div class="img f"><img src="../userbackstage/images/pc_page/user_img2.png" id="examineheadimage" width="30" height="30" /></div><i class="i_name" id="examinename"></i><a href="javascript:void(0)" onclick="openOrCloseExamineDiv()" class="add_user">添加</a></td>
						</tr>
						<tr>
                    	<td class="l_name">抄送人</td>
                        <td class="img_td" id="CCusernames"><a href="javascript:void(0)" onclick="showCCuserOrganize()" class="add_user">添加</a></td>
                    </tr>
						<tr>
							<td class="l_name">填写人</td>
							<td class="img_td"><div class="img f">
									<img id="createheadimage" src="<%=request.getContextPath() %>${userInfo.headimage}" width="30" height="30" />
								</div>
								<i id="createname" class="i_name">${userInfo.realname }</i>
							</td>
						</tr>
						<tr class="last_td">
							<td class="l_name">填写时间</td>
							<td id="createtime"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())%></td>
						</tr>
						<tr class="foot_td">
							<td>&nbsp;</td>
							<td><a href="javascript:void(0)" onclick="addreward()"
								class="a_btn bg_yellow">发送</a><a href="javascript:void(0)" onclick="window.history.go(1)" class="a_btn bg_gay2">取消</a></td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<div class="clear"></div>
	</div>
	<div class="div_mask" style="display: none"></div>

</body>
<script type="text/javascript">
	function addreward(){
		var imgurls = "";
		var imglists = $('#imglist').find('img');
		$.each(imglists,function(i,item){
			imgurls += $(item).attr("url")+",";
		});
		
		var userlist = $('#userlist').val();
		var param = new Object();
		param.companyid = "${userInfo.companyid}";
		param.rewarduserid= $('#rewarduserid').val();
		param.realname = $('#resouceid').text();
		param.organizename = $('#organizename').val();
		param.position = $('#position').val();
		param.reason = $('#reason').val();
		param.rewardresult = $('#rewardtype').val();
		param.resulttext = $('#resultText').val();
		param.examineuserid = $('#examineuserid').val();
		param.createid = "${userInfo.userid}";
		
		param.userlist = userlist;
		param.filelist = imgurls;
		
		param.CCuserlist = $("#CCuseridlist").val();
		param.CCusernames = $('#CCusernamelist').val();
		
		if(param.rewarduserid == ""){
			swal({
				title : "",
				text : "请选择被奖励者。",
				type : "warning",
				showCancelButton : false,
				confirmButtonColor : "#ff7922",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			}, function(){
			});
			return false;
		}
		if(param.rewardresult == "" && param.resulttext == ""){
			swal({
				title : "",
				text : "请选择奖励结果。",
				type : "warning",
				showCancelButton : false,
				confirmButtonColor : "#ff7922",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			}, function(){
			});
			return false;
		}
		if(userlist == ""){
			swal({
				title : "",
				text : "请选择发布范围。",
				type : "warning",
				showCancelButton : false,
				confirmButtonColor : "#ff7922",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			}, function(){
			});
			return false;
		}
		if(param.examineuserid == ""){
			swal({
				title : "",
				text : "请选择审核人。",
				type : "warning",
				showCancelButton : false,
				confirmButtonColor : "#ff7922",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			}, function(){
			});
			return false;
		}

		if(param.rewarduserid == param.examineuserid){
			swal({
				title : "",
				text : "奖励者和审核者不能是同一个人！",
				type : "warning",
				showCancelButton : false,
				confirmButtonColor : "#ff7922",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			}, function(){
			});
			return false;
		}

		$.ajax({
			url:projectpath+"/pc/insertRewardInfo",
			type:"post",
			data:param,
			success:function(resultMap){
				if(resultMap.status == 0 || resultMap == '0'){
					swal({
						title : "",
						text : "奖励单新增成功！",
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						location.href=projectpath+"/pc/getRewardListInfo";
					});
				}
			}
		});
		}

</script>


</html>
