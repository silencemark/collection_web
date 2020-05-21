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
function initedit(){
	$('#showdiv').hide();
	$('#editdiv').show();
}
$(document).ready(function(){
	$('#nav_banner').attr('class','active');$('#nav_banner').parent().parent().show();
	
	$('#file_upload').fileupload({    
		url:'<%=request.getContextPath() %>/upload/imageUpload?type=4', 
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
        	
        	$("#img").val(data.url);
			var temp="<span><img src=\"<%=request.getContextPath()%>"+data.url+"\" width=\"232\" height=\"104\" />"+
    		"<input type=\"hidden\" value=\""+data.url+"\" name=\"imageurl\">"+
    		"<a href=\"javascript:void(0)\" class=\"del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\"   onclick=\"deletebanner(this,'')\"/></a></span>";
    		$('#lastdiv').before(temp);
        		
        }
    });
})
</script>
</head>
<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
    <div class="page_nav"><p><a href="<%=request.getContextPath() %>/userbackstage/index">首页</a><i>/</i><a href="#">企业管理</a><i>/</i><span>APP首页图片配置</span></p></div>  
    <div class="page_tab m_top">
    	<div id="showdiv">
	        <div class="tab_name"><span class="gray1">APP首页图片配置</span><a href="javascript:void(0)" onclick="initedit()">编辑</a></div>
	        <div class="flash_imgedit">
	        	<c:forEach items="${bannerlist}" var="banner">
	        		<span id="banner${banner.id}"><img src="${banner.imgurl}" width="232" height="104" /></span>
	        	</c:forEach>
	            <div class="clear"></div>
	        </div>
        </div>
        <div id="editdiv" style="display: none">
	        <div class="tab_name"><span class="gray1">APP首页</span><a href="javascript:void(0)" onclick="uploadbanner()" class="wid_01">上传Banner</a></div>
	        <div class="flash_imgedit" id="bannerdiv">
	        	<c:forEach items="${bannerlist}" var="banner">
	        		<span><img src="<%=request.getContextPath()%>${banner.imgurl}" width="232" height="104" />
	        		<input type="hidden" value="${banner.imgurl}" name="imageurl">
	        		<input type="hidden" value="${banner.id}" name="id">
	        		<a href="javascript:void(0)" class="del"><img src="../userbackstage/images/public/del.png" alt="删除" onclick="deletebanner(this,'${banner.id}')"/></a></span>
	        	</c:forEach>
	            <div class="clear" id="lastdiv"></div>
	            <div class="a_btn">
	            	<a href="javascript:void(0)" class="bg_yellow" onclick="submitbanner()">保存</a>
	                <a href="javascript:void(0)" class="bg_gay2" onclick="cancal()">取消</a>
	            </div>
	        </div>
        </div>
        
<%--         <form action="<%=request.getContextPath() %>/userbackstage/uploadimage" method="post" enctype="multipart/form-data" id="imageform" style="display: none">
        	<input type="file" name="file" onchange="submitdata(this)" id="filebtn">
        </form> --%>
        
<!--         <input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/> -->
		<input type="file" style="display: none" name="myfiles" id="file_upload" multiple/>
        <input type="hidden" id="img" name="headimage" />
    </div>
</div>
<div class="div_mask" style="display:none;"></div>
<script type="text/javascript">
function submitbanner(){
	var alldata={"bannerlist":[]};
	$('#bannerdiv').find('input[name=imageurl]').each(function(index){
		var banner={};
		if($(this).next("input[name=id]").length>0){
			banner["id"]=$(this).next("input[name=id]").val();
		}
		banner["type"]=2;
		banner["model"]=1;
		banner["imgurl"]=$(this).val();
		banner["priority"]=index+1;
		alldata.bannerlist.push(banner);
	})
	if($('#bannerdiv').find('input[name=imageurl]').length==0){
		swal({
			title : "",
			text : "图片至少上传一张",
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
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/userbackstage/editbanner",
		data:"bannerlist="+JSON.stringify(alldata),
		success:function(data){
			location.reload();
		}
	})
}
function deletebanner(obj,id){
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
		if(id != ""){
			$.ajax({
				type:"post",
				dataType:"json",
				url:"/userbackstage/deletebanner",
				data:"id="+id+"&delflag=1",
				success:function(data){
					
				}
			})
		}
		$(obj).parent().parent().remove();
		$('#banner'+id).remove();
	})
	
	
}
function uploadbanner(){
	if($('#bannerdiv').find("span").length>=2){
		swal({
			title : "",
			text : "首页banner图片最多上传两张",
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
	$('#file_upload').click();
}
/* function submitdata(obj){
	if($(obj).val()!=""){
		$('#imageform').submit();
	}
} */


</script>
</body>
</html>
