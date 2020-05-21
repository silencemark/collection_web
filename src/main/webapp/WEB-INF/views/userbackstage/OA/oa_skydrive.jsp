<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>OA办公扩容-使用方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/sweetalert/dist/sweetalert-dev.js"></script> 

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<%-- <script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>  --%>
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
		$('#nav_cloudmodule').attr('class','active');$('#nav_cloudmodule').parent().parent().show();
		
		$('#fileName').fileupload({    
			url:'<%=request.getContextPath() %>/upload/imageUpload?type=2', 
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
				$('#imgurl').val(data.url);
				var img = '<img src="<%=request.getContextPath()%>'+data.url+'" width="102" height="102"/>';
				$('#moduleimage').html(img);
	        		
	        }
	    });
	})
</script>
</head>

<body>
<jsp:include page="../top.jsp" flush="true"></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" flush="true"></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">OA办公</a><i>/</i><span>企业云盘</span></p></div>  
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1">企业云盘(${sy_memory }G/${maxmemory }G)</span><a href="javascript:void(0)" onclick="addmodule()" class="wid_01">新建栏目</a><a href="/userbackstage/intoAddCloudCapacity">扩容</a></div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
        		<c:forEach items="${modulelist }" var="ml">
        			<tr>
	                	<td class="img_td" width="30"><div class="img2"><img src="${ml.moduleimage }" width="30" height="30" /></div></td>
	                    <td width="70%" style="cursor: pointer;" onclick="location.href='/userbackstage/intoCompanyCloudFilePage?moduleid=${ml.moduleid}'">${ml.modulename }</td>
	                    <td class="t_r"><a href="javascript:void(0)" onclick="showModule('${ml.moduleid}','${ml.modulename }','${ml.moduleimage }')" class="link">修改</a><a href="javascript:void(0)" onclick="deleteModuleInfo('${ml.moduleid}')" class="red">删除</a></td>
	                </tr>
        		</c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div><!--动态的获取pagination的宽度赋值给Pagination-->
    </div>
</div>
<div class="div_mask" style="display: none;"></div>
<div class="tc_addbriefing" style="display: none;">
	<div class="tc_title"><span>栏目新增</span><a href="javascript:void(0)" onclick="close_open_div()">×</a></div>
    <div class="box">
    	<span>栏目标题</span>
    	<input type="hidden" id="moduleid"/>
        <input type="text" class="text" placeholder="请输入栏目名称" id="modulename"/>
        <div class="clear"></div>
        <span>栏目图片</span>
        <div class="img" id="moduleimage"></div>
        <input type="hidden" id="imgurl"/>
        <div class="txt"><input type="button" onclick="$('#fileName').click();" value="上传" class="sc_btn" /><p>上传尺寸比例<br />1:1</p></div>
        <div class="clear"></div>
    </div>
    <div class="tc_btnbox"><a href="javascript:void(0)" onclick="close_open_div()" class="bg_gay2">取消</a>
    <a href="javascript:void(0)" id="add_btn" onclick="addModuleInfo()" class="bg_yellow">确定</a>
    <a href="javascript:void(0)" id="update_btn" onclick="updateMoudleInfo();" style="display: none;" class="bg_yellow">确定</a></div>
</div>
<!-- <input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/> -->
<input type="file" style="display: none" name="myfiles" id="fileName" multiple/>

<input type="hidden" id="img" name="headimage" />
</body>
<script>
	function close_open_div(){
		var mask = $('.div_mask').css("display");
		var addpage = $('.tc_addbriefing').css("display");
		if(mask == "none" && addpage == "none"){
			$('.div_mask').show();
			$('.tc_addbriefing').show();
		}else{
			$('.div_mask').hide();
			$('.tc_addbriefing').hide();
		}
	}
	
	function addmodule(){
		$('#moduleid').val("");
		$('#modulename').val("");
		$('#imgurl').val("");
		$('#moduleimage').html("");
		
		$('#add_btn').show();
		$('#update_btn').hide();
		
		close_open_div();
	}
	
	function addModuleInfo(){
		var param = new Object();
		param.modulename = $('#modulename').val();
		param.moduleimage = $('#imgurl').val();
		
		requestPost("/userbackstage/insertCompanyCloudModuleInfo",param,function(resultMap){
			if(resultMap.status == 0){
				swal({
				    title: "提示",
				    text: "新建成功！",
				    type: "success",
				    showCancelButton: false,
				    confirmButtonColor: "#ff7922",
				    confirmButtonText: "确定",
				    closeOnConfirm: true
				}, function(){
					location.reload();
				});
			}
		});
	}
	
	function showModule(id,name,url){
		$('#moduleid').val(id);
		$('#modulename').val(name);
		$('#imgurl').val(url);
		$('#moduleimage').html('<img src="'+url+'" width="102" height="102"/>');
		
		$('#add_btn').hide();
		$('#update_btn').show();
		
		close_open_div();
	}
	
	function updateMoudleInfo(){
		var param = new Object();
		param.moduleid = $('#moduleid').val();
		param.modulename = $('#modulename').val();
		param.moduleimage = $('#imgurl').val();
		
		requestPost("/userbackstage/updateCompanyCloudModuleInfo",param,function(resultMap){
			if(resultMap.status == 0){
				close_open_div();
				swal({
				    title: "提示",
				    text: "修改成功！",
				    type: "success",
				    showCancelButton: false,
				    confirmButtonColor: "#ff7922",
				    confirmButtonText: "确定",
				    closeOnConfirm: true
				}, function(){
					location.reload();
				});
			}
		});
	}
	
	function deleteModuleInfo(id){
		swal({
		    title: "提示",
		    text: "您确定要删除吗！",
		    type: "warning",
		    showCancelButton: true,
		    confirmButtonColor: "#ff7922",
		    cancelButtonText: "取消",
		    confirmButtonText: "确定",
		    closeOnConfirm: true
		}, function(){
			var param = new Object();
			param.moduleid = id;
			param.delflag = 1;
			requestPost("/userbackstage/updateCompanyCloudModuleInfo",param,function(resultMap){
				if(resultMap.status == 0){
					swal({
					    title: "提示",
					    text: "删除成功！",
					    type: "success",
					    showCancelButton: false,
					    confirmButtonColor: "#ff7922",
					    confirmButtonText: "确定",
					    closeOnConfirm: true
					}, function(){
						location.reload();
					});
				}
			});
		});
	}
	
	//公共的查询方法
	function requestPost(url,param,callback){
		$.ajax({
			url:url,
			type:"post",
			data:param,
			beforeSend:function(){
				var load = '<div id="load_mask" class="div_mask" style="opacity:0;"></div>'+
			      '<div id="load_loading" class="loading"><img src="../userbackstage/images/public/loading.gif" width="360" height="200" /></div>';
				$("body").after(load);
			},
			success:function(resultMap){
				callback(resultMap);
			},
			complete:function(){
				$('#load_mask').remove();
				$('#load_loading').remove();
			},
			error:function(e){
				
			}
		});
	}
</script>
</html>
