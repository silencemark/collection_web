<%@page import="com.collection.util.UserUtil"%>
<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>首页-使用方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<%-- <script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>   --%>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organize.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organizeRadio.js"></script>
<!-- 上传需要js -->      
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.ui.widget.js"></script>
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.iframe-transport.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload.js"></script>  
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-ui.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-process.js"></script>   
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-validate.js"></script>  

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

$(document).ready(function(){
	$('#nav_companyinfo').attr('class','active');$('#nav_companyinfo').parent().parent().show();
})
</script>
</head>
<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="<%=request.getContextPath() %>/userbackstage/index">首页</a><i>/</i><a href="#">企业管理</a><i>/</i><span>餐饮公司管理</span></p></div>  
    <div class="page_tab m_top">
    	<div id="detaildiv">
	        <div class="tab_name"><span class="gray1">餐饮公司管理</span><a href="javascript:void(0)" onclick="$('#editdiv').show();$('#detaildiv').hide();">修改</a></div>
	        <div class="tab_list">
	        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
	            	<tr>
	                    <td class="l_text" width="140">餐厅/公司</td>
	                    <td>${companymap.companyname}</td>
	                </tr>
	                <tr>
	                    <td class="l_text" width="140">公司logo</td>
	                    <td>
	                    	<img src="${companymap.logourl}" height="72" />
	                	</td>
	                </tr>
	                <tr>
	                    <td class="l_text" width="140">通讯地址</td>
	                    <td>${companymap.address}</td>
	                </tr>
	                <tr>
	                    <td class="l_text" width="140">负责人姓名</td>
	                    <td>${companymap.contactperson}</td>
	                </tr>
	                <tr>
	                    <td class="l_text" width="140">负责人证件类型</td>
	                    <td>${companymap.idcodetype}</td>
	                </tr>
	                <tr>
	                    <td class="l_text" width="140">负责人证件号</td>
	                    <td>${companymap.idcode}</td>
	                </tr>
	                <tr>
	                    <td class="l_text" width="140">电话</td>
	                    <td>${companymap.telephone}</td>
	                </tr>
	                <tr>
	                    <td class="l_text" width="140">手机号</td>
	                    <td>${companymap.phone}</td>
	                </tr>
	                <tr>
	                    <td class="l_text" width="140">电子邮件</td>
	                    <td>${companymap.email}</td>
	                </tr>
	                <tr>
	                    <td class="l_text" width="140">创建人</td>
	                    <td><i class="m_r30">${companymap.createname}</i><a href="javascript:void(0)" onclick="resetpass('${companyInfo.createid}')" class="yellow">重置密码</a></td>
	                </tr>
	                <tr>
	                    <td class="l_text" width="140">营业执照</td>
	                    <td>
	                    <c:forEach items="${companymap.licenselist}" var="item">
	                    	<img src="${item.imageurl}" height="72" style="margin-right:10px;" />
	                	</c:forEach>
	                	</td>
	                </tr>
	                
	                <tr>
	                    <td class="l_text" width="140">备注</td>
	                    <td>${companymap.info}</td>
	                </tr>
	            </table>
	        </div>
	        
        </div>
        <div id="editdiv" style="display: none">
	        <div class="tab_name"><span class="gray1">餐饮公司信息编辑</span><a onclick="$('#detaildiv').show();$('#editdiv').hide()">返回</a></div>
	        <input type="hidden" id="companyid" value="${companymap.companyid}"/>
	        <div class="tab_list">
	        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
	            	<tr class="none_border">
	                    <td class="l_text" width="140">餐厅/公司</td>
	                    <td><input type="text" class="text" placeholder="请输入" id="companyname" value="${companymap.companyname}"/></td>
	                </tr>
	                <tr class="none_border">
	                    <td class="l_text" width="140">公司logo</td>
	                    <td id="imagetd">
	                    	<div name="imagediv">
	                    	<input type="hidden" name="logourl" value="${companymap.logourl}"/>
	                   	 	<div class="img4"><img src="${companymap.logourl}" height="72" id="logourlshow" /></div>
	                		</div>
	                    <a class="a_btn bg_yellow" id="logouploadbtn" onclick="$('#logofileName').click();">上传</a></td>
	                </tr>
	                <tr class="none_border">
	                    <td class="l_text" width="140">通讯地址</td>
	                    <td><input type="text" class="text" placeholder="请输入" id="address" value="${companymap.address}"/></td>
	                </tr>
	                <tr class="none_border">
	                    <td class="l_text" width="140">负责人姓名</td>
	                    <td><input type="text" class="text" placeholder="请输入"  id="contactperson"   value="${companymap.contactperson}"/></td>
	                </tr>
	                <tr class="none_border">
	                    <td class="l_text" width="140">负责人证件类型</td>
	                    <td><input type="text" class="text" placeholder="请输入" id="idcodetype"   value="${companymap.idcodetype}"/></td>
	                </tr>
	                <tr class="none_border">
	                    <td class="l_text" width="140">负责人证件号</td>
	                    <td><input type="number" class="text" placeholder="请输入" id="idcode"  value="${companymap.idcode}"/></td>
	                </tr>
	                <tr class="none_border">
	                    <td class="l_text" width="140">电话</td>
	                    <td><input type="text" class="text" maxlength="20" placeholder="请输入" id="telephone"  value="${companymap.telephone}" /></td>
	                </tr>
	                <tr class="none_border">
	                    <td class="l_text" width="140">手机号</td>
	                    <td><input type="number" class="text" maxlength="11" placeholder="请输入" id="phone" value="${companymap.phone}"/></td>
	                </tr>
	                <tr class="none_border">
	                    <td class="l_text" width="140">电子邮件</td>
	                    <td><input type="text" class="text" placeholder="请输入" id="email"  value="${companymap.email}"/></td>
	                </tr>
	                <tr class="none_border">
	                    <td class="l_text" width="140">营业执照</td>
	                    <td id="imagetd">
	                    <c:forEach items="${companymap.licenselist}" var="item">
	                    	<div name="imagediv">
	                    	<input type="hidden" name="imageurl" value="${item.imageurl}"/>
	                   	 	<div class="img4"><img src="${item.imageurl}" height="72" /><a href="javascript:void(0)" class="a_del"><img src="../userbackstage/images/public/del.png" alt="删除" onclick="dellicense(this)"/></a></div>
	                		</div>
	                	</c:forEach>
	                    <a class="a_btn bg_yellow" id="uploadbtn" onclick="uploadimage()">上传</a></td>
	                </tr>
	                <tr class="none_border">
	                    <td class="l_text" width="140">备注</td>
	                    <td><textarea class="text_area" placeholder="请输入" id="info">${companymap.info}</textarea></td>
	                </tr>
	                <tr class="none_border">
	                    <td class="l_text" width="50"></td>
                    	<td><a href="javascript:void(0)" class="a_btn bg_yellow" onclick="submitdata()">提交</a>&nbsp;&nbsp;&nbsp;&nbsp;<a class="a_btn bg_gay1" onclick="$('#detaildiv').show();$('#editdiv').hide()">取消</a></td>
	                </tr>
	                
	            </table>
	        </div>
        </div>
    </div>
</div>
<div class="div_mask" style="display:none;"></div>
<!-- <input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/> -->
<input type="file" style="display: none" name="myfiles" id="fileName" multiple/>

<!-- <input type="file" name="myfiles" style="display: none" id="logofileName" T="file_headimg" onchange="ajaxFileUploadlogo('img')"/> -->
<input type="file" style="display: none" name="myfiles" id="logofileName" multiple/>

<script type="text/javascript">
$(function(){
	$('#logofileName').fileupload({    
		url:'<%=request.getContextPath() %>/upload/imageUpload?type=5', 
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
        	$('#logourlshow').attr('src',data.url);
			$('input[name=logourl]').val(data.url);
        }
    });

	
	$('#fileName').fileupload({    
		url:'<%=request.getContextPath() %>/upload/imageUpload?type=6', 
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
        	var temp="<div name=\"imagediv\">"+
        	"<input type=\"hidden\" name=\"imageurl\" value=\""+data.url+"\"/>"+
       	 	"<div class=\"img4\"><img src=\""+data.url+"\" height=\"72\" /><a href=\"javascript:void(0)\" class=\"a_del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\" onclick=\"dellicense(this)\"/></a></div>"+
    		"</div>";
    		$('#uploadbtn').before(temp);
        		
        }
    });

});


function submitdata(){
	var companyid=$('#companyid').val();
	var companyname=$('#companyname').val();
	var address=$('#address').val();
	var contactperson=$('#contactperson').val();
	var idcodetype=$('#idcodetype').val();
	var idcode=$('#idcode').val();
	var telephone=$('#telephone').val();
	var phone=$('#phone').val();
	var email=$('#email').val();
	var info=$('#info').val();
	var logourl=$('input[name=logourl]').val();
	
	
	if(companyname==""){
		swal({
			title : "",
			text : "请填写公司名称",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		})
		return
	}
	if(logourl==""){
		swal({
			title : "",
			text : "请上传公司logo",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		})
		return
	}
	
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
				$('#phone').focus();
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
				$('#email').focus();
			})
			return;
		}
	}
	var imagedata={"imagelist":[]};
	$('input[name=imageurl]').each(function(index){
		var imagemap={};
		imagemap["imageurl"]=$(this).val();
		imagedata.imagelist.push(imagemap);
	})
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath()%>/userbackstage/updateCompany",
		data:"imagelist="+JSON.stringify(imagedata)+"&companyid="+companyid+"&companyname="+companyname
		+"&address="+address+"&contactperson="+contactperson+"&idcodetype="+idcodetype
		+"&idcode="+idcode+"&telephone="+telephone+"&phone="+phone+"&email="+email+"&info="+info+"&logourl="+logourl,
		success:function(data){
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
				location.reload();
			})
		}
	})
}
function dellicense(obj){
	swal({
		title : "",
		text : "确认删除？",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$(obj).parent().parent().parent().remove();
	})
}
function uploadimage(){
	if($('#imagetd').find("div[name=imagediv]").length>=2){
		swal({
			title : "",
			text : "营业执照图片最多上传两张",
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
	$('#fileName').click();
}


function resetpass(userid){
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath()%>/managebackstage/resetPass",
		data:"userid="+userid,
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
					
				})
			}else{
				swal({
					title : "",
					text : "重置成功",
					type : "error",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					
				})
			}
		}
	})
}
</script>
</body>
</html>