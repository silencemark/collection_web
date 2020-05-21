<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新建通知</title>
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

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/change_releaserange.js"></script>

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


function checkInsert(){
	var param = new Object();
	param.userid ='${userInfo.userid}';
	param.companyid = '${userInfo.companyid}';
	param.title = $('#title').val();
	param.content = $('#content').val();
	param.userlist = $('#userlist').val();
	
	var imgurl = $('img[name="fileimg"]');
	var filelist="";
	$.each(imgurl,function(i,item){
		filelist += $(item).attr("src")+",";
	});
	filelist = filelist.substring(0,(filelist.length-1));
	param.filelist = filelist;

	if(param.title == ""){
		swal({
			title : "",
			text : "请输入标题！",
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
	if(param.content == ""){
		swal({
			title : "",
			text : "请输入内容！",
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
	if(param.userlist == ""){
		swal({
			title : "",
			text : "请选择发布范围！",
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
		url:"/pc/insertNotice",
		type:"post",
		data:param,
		success:function(data){
			if(data.status == "0"){
				swal({
					title : "",
					text : "通知信息发送成功！",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href="<%=request.getContextPath() %>/pc/oa_notice_list";
				});
			}
		}
	});
	
}

$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#noticeActive').attr('class','active');
	
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
        	$("<div class=\"img_box\" ><b><img name=\"fileimg\" onclick=\"showBigImagePC(this)\" src=\""+data.url+"\"></b><div class=\"del\" onclick=\"$(this).parent().remove();\">删除</div></div>").insertBefore("#fileimg");
        }
    });
});

</script>
</head>

<body >
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>新建通知</span><a href="<%=request.getContextPath() %>/pc/oa_notice_list" class="back">返回</a></div>
        <div class="page_tab2">            
            <div class="tab_list">
<!--              <input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/>       	 -->
			 <input type="file" style="display: none" name="myfiles" id="fileName" multiple/>
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="none_border" style="font-size:12px;">
                    <tr>
                    	<td class="l_name">标题</td>
                        <td><input type="text" class="text" placeholder="请输入标题"  id="title"/></td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name2" >内容</td>
                        <td>
                        	<textarea class="text_area" placeholder="请输入内容，最多允许输入800字符" maxlength="800"id="content"></textarea>
                            <div class="clear"></div>
                            <div class="img_list" id="img_list">
                                <div class="img_add"  onclick="checkFileImg()" id="fileimg">添加图片</div>
                                <div class="clear"></div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">发布范围</td>
                        <td><i class="i_name" id="releaserange"></i><a href="javascript:void(0)" onclick="openOrCloseReleaseRangeDiv()" class="add_user">添加</a></td>
                    </tr>
                    <tr class="foot_td">
                    	<td>&nbsp;</td>
                        <td><a href="#" class="a_btn bg_yellow" onclick="checkInsert()">发送</a><a href="#" class="a_btn bg_gay2" onclick="goBackPage();">取消</a></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>

<div class="div_mask" style="display:none;"></div>

</body>
</html>
