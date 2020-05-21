<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>备用金申请</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
	<!-- 上传图片需要js -->   
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<%-- <script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>   --%>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>

	<!-- 上传文件需要js -->      
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.ui.widget.js"></script>
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.iframe-transport.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload.js"></script>  
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-ui.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-process.js"></script>   
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-validate.js"></script> 

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/change_examineuserinfo.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/pc_chaosongren.js"></script>

<script type="text/javascript">

function checkFileImg(){
 	if($("#img_list img[name=fileimg]").length<3){
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

function checkAdd(){	
	var param = new Object();
	param.companyid ='${userInfo.companyid}';
	param.createid ='${userInfo.userid}';
	param.examineuserid = $('#examineuserid').val();
	
	param.reason = $('#reason').val();
	param.urgentlevel = urgentlevel;
	param.amount = $('#amount').val();
	param.usetime = $('#starttime').val();
	param.returntime = $('#endtime').val();
	param.remark = $('#remark').val();
	
	param.userlist = $("#CCuseridlist").val();
	param.CCusernames = $('#CCusernamelist').val();
	
	if(param.content == ""){
		swal({
			title : "",
			text : "事由不能为空！",
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
			text : "审批人不能为空！",
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
		url:"/pc/insertReserveAmount",
		type:"post",
		data:param,
		success:function(data){
			if(data.status == 0 || data.status == '0'){
				swal({
					title : "",
					text : "添加成功！",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href="<%=request.getContextPath()%>/pc/getPcOaReserveList";
				});
			}
		}
	});
}

var urgentlevel=1;
function checkRadio(row){
	urgentlevel=row;
	if(row==1){
		$("#green").attr("class","radio_ed_green");
		$("#yellow").attr("class","radio_yellow");
		$("#red").attr("class","radio_red");
	}else if(row==2){
		$("#green").attr("class","radio_green");
		$("#yellow").attr("class","radio_ed_yellow");
		$("#red").attr("class","radio_red");
	}else if(row==3){
		$("#green").attr("class","radio_green");
		$("#yellow").attr("class","radio_yellow");
		$("#red").attr("class","radio_ed_red");
	}
}
$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#reserveActive').attr('class','active');
	
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
        	$("<div class=\"img_box\" ><b><img name=\"fileimg\" src=\""+data.url+"\"></b><div class=\"del\" onclick=\"$(this).parent().remove();\">删除</div></div>").insertBefore("#fileimg");
        }
    });
});

</script>
</head>

<body onload="onloadData()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>备用金申请</span><a href="<%=request.getContextPath() %>/pc/getPcOaReserveList" class="back">返回</a></div>
        <div class="page_tab2">            
            <div class="tab_list">
<!--              <input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/>       	 -->
			 <input type="file" style="display: none" name="myfiles" id="fileName" multiple/>
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="none_border" style="font-size:12px;">
                    <tr>
                    	<td class="l_name2">事由</td>
                        <td>
                        	<textarea class="text_area" placeholder="请输入事由，最多允许输入800字符" maxlength="800" id="reason"></textarea>
                        </td>
                    </tr>
                     <tr>
                    	<td class="l_name">紧急度</td>
                        <td>
                        	<a href="javascript:void(0)" class="radio_ed_green" onclick="checkRadio(1)"  id="green">选择</a><i class="i_radio green" >普通</i><!--radio_green-->
                            <a href="javascript:void(0)" class="radio_yellow"  onclick="checkRadio(2)"  id="yellow"">选择</a><i class="i_radio yellow">紧急</i><!--radio_ed_yellow-->
                            <a href="javascript:void(0)" class="radio_red"  onclick="checkRadio(3)" id="red" >选择</a><i class="i_radio red">非常紧急</i><!--radio_ed_red-->
                        </td>
                    </tr>
                    <tr>
                        	<td class="l_name">申请金额</td>
                        <td><input type="text" class="text" placeholder="请输入申请金额" id="amount" /></td>
                    </tr>
                    <tr>
                    	<td class="l_name2">使用日期</td>
                        <td>
                        	<input  class="text"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="endtime" value="${createtime}"   id="starttime"  class="text_time" type="text"  readonly="readonly"  style="width: 135px;"/>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name2">归还日期</td>
                        <td>
                        	<input class="text"   placeholder="归还日期" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="endtime"    id="endtime"  class="text_time" type="text"  readonly="readonly"  style="width: 135px;"/>
                        </td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name2">备注</td>
                        <td>
                        	<textarea class="text_area" placeholder="请输入备注，最多允许输入800字符" maxlength="800" id="remark" maxlength="200"></textarea>
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
                    	<td class="l_name">申请人</td>
                        <td class="img_td"><div class="img f"><img src="${userInfo.headimage }" width="30" height="30" /></div><i class="i_name">${userInfo.realname }</i></td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name">填写时间</td>
                        <td>${createtime}</td>
                    </tr>
                    <tr class="foot_td">
                    	<td>&nbsp;</td>
                        <td><a href="javascript:void(0)" class="a_btn bg_yellow" onclick="checkAdd()">发送</a><a href="javascript:void(0)" onclick="goBackPage()" class="a_btn bg_gay2">取消</a></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>
<script type="text/javascript">
function callbackfunc1(organizeid){
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath() %>/userbackstage/getUserByOrganize",
		data:"organizeid="+organizeid,
		success:function(data){
			
			$('#usertable').html("<tr class=\"head_td\">"+
		        	"<td width=\"30\">&nbsp;</td>"+
		            "<td width=\"70\">姓名</td>"+
		            "<td width=\"70\">性别</td>"+
		            "<td width=\"130\">电话</td>"+
		            "<td>操作</td>"+
		        "</tr>");
			if(data.userlist.length>0){
				var temp="";
				for(var i=0;i<data.userlist.length;i++){
					if(data.userlist[i].userid != '${userInfo.userid}'){
					temp+="<tr>"+
                    	"<td class=\"img_td\"><div class=\"img\"><img src=\"<%=request.getContextPath() %>"+data.userlist[i].headimage+"\" width=\"30\" height=\"30\" /></div></td>"+
                        "<td>"+data.userlist[i].realname+"</td>";
                        if(data.userlist[i].sex==1){
                       		temp+="<td>男</td>";
                        }else if(data.userlist[i].sex==0){
                        	temp+="<td>女</td>";
                        }else{
                        	temp+="<td></td>";
                        }
                        if(data.userlist[i].isshowphone==1){
                        	 temp+="<td>"+data.userlist[i].phone+"</td>";
                        }else{
                        	var phone=data.userlist[i].phone;
                        	temp+="<td>"+phone.substring(0,3)+"*****"+phone.substring(7,phone.length)+"</td>";
                        }
                        temp+="<td><a href=\"javascript:void(0)\" onclick=\"adduser(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"','"+data.userlist[i].headimage+"')\" class=\"link\">选择</a></td>"+
                    "</tr>";
					}
				}
				$('#usertable').append(temp);
			}
		}
	})
}
function searchuser(){
	var searchrealname=$('#searchrealname').val();
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath() %>/userbackstage/getSearchUserByName",
		data:"searchrealname="+searchrealname,
		success:function(data){
			$('#usertable').html("<tr class=\"head_td\">"+
		        	"<td width=\"30\">&nbsp;</td>"+
		            "<td width=\"70\">姓名</td>"+
		            "<td width=\"70\">性别</td>"+
		            "<td width=\"130\">电话</td>"+
		            "<td>操作</td>"+
		        "</tr>");
			if(data.userlist.length>0){
				var temp="";
				for(var i=0;i<data.userlist.length;i++){
					if(data.userlist[i].userid != '${userInfo.userid}'){
						temp+="<tr><td class=\"img_td\"><div class=\"img\"><img src=\"<%=request.getContextPath() %>"+data.userlist[i].headimage+"\" width=\"30\" height=\"30\" /></div></td>"+
	                        "<td>"+data.userlist[i].realname+"</td>";
	                        if(data.userlist[i].sex==1){
	                       		temp+="<td>男</td>";
	                        }else if(data.userlist[i].sex==0){
	                        	temp+="<td>女</td>";
	                        }else{
	                        	temp+="<td></td>";
	                        }
	                        if(data.userlist[i].isshowphone==1){
	                        	 temp+="<td>"+data.userlist[i].phone+"</td>";
	                        }else{
	                        	var phone=data.userlist[i].phone;
	                        	temp+="<td>"+phone.substring(0,3)+"*****"+phone.substring(7,phone.length)+"</td>";
	                        }
	                        temp+="<td><a href=\"javascript:void(0)\" onclick=\"adduser(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"','"+data.userlist[i].headimage+"')\" class=\"link\">选择</a></td></tr>";
					}
				}
				$('#usertable').append(temp);
			}
		}
	})
}
function adduser(obj,userid,realname,headimage){
	$('.div_mask').hide();
	$('.tc_selrange').hide();
	$('#examinename').text(realname);
	$('#examineuserid').val(userid);
	$('#examineheadimage').attr("src",headimage);
}
</script>

<div class="div_mask" style="display:none;"></div>
<div class="tc_selrange" style="display:none;">
	<div class="tc_title"><span>选择转发人</span><a href="javascript:void(0);" onclick="$('.tc_selrange').hide();$('.div_mask').hide();">×</a></div>
    <div class="range">
    	<div class="l_box" id="organizetree2" style="overflow:hidden;overflow-y:visible;">
    	
        </div>
        <div class="r_box">
        	<div class="sel_xx">
                <div class="sel_box">
                    <input type="text" class="text" placeholder="请输入姓名" id="searchrealname"/>
                    <input type="button" class="find_btn" value="" onclick="searchuser()" />
                    <div class="clear"></div>
                </div>
            </div>
            <div class="tab_list">
            	<table width="100%" border="0" cellpadding="0" cellspacing="0" id="usertable" style="font-size:12px;">
                	<tr class="head_td">
                    	<td width="30">&nbsp;</td>
                        <td width="70">姓名</td>
                        <td width="70">性别</td>
                        <td width="130">电话</td>
                        <td>操作</td>
                    </tr>
                </table>
            </div>
            <%-- <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination--> --%>
            <div class="tc_btnbox"><a  href="javascript:void(0);" onclick="$('.div_mask').hide();$('.tc_selrange').hide();"  class="bg_gay2">取消</a>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>

</body>
</html>
