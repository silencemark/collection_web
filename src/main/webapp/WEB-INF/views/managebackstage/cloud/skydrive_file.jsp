<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>餐饮大师云盘-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>

<!-- 上传需要js -->      
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.ui.widget.js"></script>
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.iframe-transport.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload.js"></script>  
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-ui.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-process.js"></script>   
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-validate.js"></script>  

<script src="<%=request.getContextPath() %>/managebackstage/skydrive_file.js"></script>
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
	$(document).ready(function(){
		$('#syscloud').parent().parent().find("span").attr("class","bg_hidden");
		$('#syscloud').attr('class','active li_active');
		
		
		$('#file_upload').fileupload({    
			url:'<%=request.getContextPath() %>/upload/manageUpload', 
			formData:{
				fileName:'myfiles'   
			},
			type:'post',
			maxNumberOfFiles:1,    
			autoUpload:true,
	        dataType: 'json',
// 	        acceptFileTypes:  /(\.|\/)(gif|jpe?g|png)$/i, 
	        maxFileSize: 419430400,  
	        done: function (e, data) { 
	        	$('#upload_progressall').hide();
	    		$('#zhangzaishangchuan').css("margin-top","");
	    		
	        	data = data.result;
	        	if(data.error == undefined || data.error == null){
	        		$('#addfilebutton').attr("onclick","insertFile()");
	        		
	        		//删除文件
	        		var oldfileurl = $('#fileurl').val();
	        		deletefile(oldfileurl);
	        		
	        		$('#filename').val(data.name);
	        		$('#fileurl').val(data.url);
	        		var kb = data.size;
	        		kb = ((kb*100)/1024)/100;
	        		var q = (kb+"").indexOf(".");
    				kb = (kb+"").substring(0,(q+3));
	        		$('#kb_memory').val(kb);
	        		
	        		var size = data.size;
	        		var bg = new Array(2);
	        		bg[1]="KB";
	        		bg[2]="MB";
	        		for(i=0;i<=2;i++){
	        			if(size>1024){
	        				if(i==2){
	        					var k = (size+"").indexOf(".");
		        				size = (size+"").substring(0,(k+3));
	        					size = size+" "+bg[i];
	        					$('#memory').text(size);
	        					return false;
	        				}
	        				size = ((size*100)/1024)/100;
	        			}else{
	        				var k = (size+"").indexOf(".");
	        				size = (size+"").substring(0,(k+3));
	        				size = size+" "+bg[i];
	        				$('#memory').text(size);
	        				return false;
	        			}
	        		}
	        	}else{
	        		$('#addfilebutton').attr("onclick","swal('','文件上传失败！','error')");
	        	}
	        },
	        progressall: function (e, data) {
	        	$('#addfilebutton').attr("onclick","swal('','上传中，请稍后···','warning')");
	        	$('#upload_progressall').show();
	    		$('#zhangzaishangchuan').css("margin-top","30px");
	           var progress = parseInt(data.loaded / data.total * 100, 10);
	            $('#xiantiao').css('width',progress + '%');
	            $('#zhangzaishangchuan').html('正在上传'+progress+'%/100%');
	        }
	    });
	});
	
	//删除文件
	function deletefile(fileurl){
		$.ajax({
			url:"<%=request.getContextPath()%>/upload/deleteDocumentFile",
			type:"post",
			data:"fileurl="+fileurl,
			success:function(){}
		});
	}
</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	
	<input type="hidden" id="parentid" value="${map.parentid }"/>
	<input type="hidden" id="type" value="${map.type }"/>
	<input type="file" style="display: none" name="myfiles" id="file_upload" multiple/>
	<input type="hidden" id="kb_memory"/>
	<input type="hidden" id="fileurl"/>
	
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/intoSkyDrivePage">餐饮大师云盘</a><i>/</i><span>文件</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">文件</span><a href="javascript:void(0)" onclick="returnToTheUpperLevel()" style="background-color: #aaa;">返回</a><a href="javascript:void(0)" onclick="folder_closeOrOpen()" class="wid_02">新建文件夹</a><a href="javascript:void(0)" onclick="addFile_closeOrOpen()">上传</a></div>
        <div class="sel_box">
            <input type="button" value="删除" style="float:left;" class="find_btn" onclick="deleteall()"/> 
           <form action="<%=request.getContextPath() %>/managebackstage/getSystemCloudFileList" method="post" id="fuzzy_form">
            	<input type="hidden" name="parentid" value="${map.parentid }"/>
	            <input type="submit" value="搜索" class="find_btn"/>
	  			<input type="text" style="float:right;" id="keywork" class="text" value="${map.filename }" name="filename" placeholder="请输入关键字"/>
            </form>
            <div class="clear"></div>
        </div>
      	<div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
            		<td style="width:20px;"><input type="checkbox" id="checkall"/></td>
                	<td width="50">文件名</td>
                    <td width="30%">&nbsp;</td>
                    <td width="15%">大小</td>                    
                    <td width="18%">修改日期</td>
                    <td>操作</td>
                </tr>
                <tbody>
                	<c:forEach items="${filelist }" var="fl">
                		<tr>
                			<td><input type="checkbox" name="check_one" fileid="${fl.fileid }" filename="${fl.filename }"/></td>
                			<c:if test="${fl.filetype == 2 || fl.filetype == '2' }">
                				<td style="cursor: pointer;" class="img_td" onclick="childFolder('${fl.fileid}')"><img src="<%=request.getContextPath() %>/userbackstage/images/public/file.png" width="45" height="30" alt="文件夹" /></td>
                				<td style="cursor: pointer;" onclick="childFolder('${fl.fileid}')">${fl.filename }</td>
                			</c:if>
                			<c:if test="${fl.filetype == 1 || fl.filetype == '1' }">
                				<td class="img_td"><img src="<%=request.getContextPath() %>${fl.file_icon }" width="45" height="45" alt="文件" /></td>
                				<td>${fl.filename }</td>
                			</c:if>
		                    <td>${fl.memory }</td>
		                    <td>${fl.updatetime }</td>
		                    <td>
		                    	<a href="javascript:void(0)" onclick="update_folder_closeOrOpen('${fl.fileid }','${fl.filename}')" class="link">修改</a>
		                    	<a href="javascript:void(0)" onclick="deleteFolerOne('${fl.fileid}')" class="link">删除</a>
			                   	<c:if test="${fl.filetype == 1 }"> 
			                   		<a href="javascript:void(0)" onclick="downFolder('${fl.fileurl}')" class="blue">下载</a>
			                   	</c:if>
		                    </td>
		                </tr>
                	</c:forEach>
                </tbody>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div><!--动态的获取pagination的宽度赋值给Pagination-->
    </div>
</div>

<form action="<%=request.getContextPath()%>/managebackstage/getSystemCloudFileList" id="child_form" method="post">
	<input type="hidden" name="parentid" id="child_parentid"/>
</form>

<div class="div_mask" style="display: none;" id="div_mask"></div>
<div class="tc_worksheet2" style="display:none;" id="folder_div">
	<div class="text"><input type="text" placeholder="请输入文件夹名称" id="insert_filename"/></div>
    <div class="tc_btnbox"><a href="javascript:void(0)" onclick="folder_closeOrOpen()" class="bg_gay2">取消</a>
    <a href="javascript:void(0)" onclick="insertFolder()" class="bg_yellow">确定</a></div>
</div>

<div class="tc_worksheet2" style="display:none;" id="update_folder_div">
	<div class="text"><input type="text" placeholder="请输入 文件/夹 名称" id="update_filename"/></div>
	<input type="hidden" id="update_fileid"/>
    <div class="tc_btnbox"><a href="javascript:void(0)" onclick="update_folder_closeOrOpen()" class="bg_gay2">取消</a>
    <a href="javascript:void(0)" onclick="updateFolderName()" class="bg_yellow">确定</a></div>
</div>

<div class="tc_addfile" style="display: none;" id="addfile_div">
	<div class="tc_title"><span>上传文件</span><a href="javascript:void(0)" onclick="addFile_closeOrOpen()">×</a></div>
    <div class="box">
    	<input type="text" class="text" id="filename" placeholder="请输入文件名称" />
        <input type="button" class="btn" onclick="$('#file_upload').click();" value="上传" />
    	<div class="clear"></div>
    	<div class="tc_progress_bar" style="display: none;" id="upload_progressall"><span id="xiantiao">线条</span></div>
        <span id="zhangzaishangchuan" >文件大小不能超过400M</span>
    </div>
    <div class="tc_btnbox"><a href="javascript:void(0)" onclick="addFile_closeOrOpen()" class="bg_gay2">取消</a><a href="javascript:void(0)" id="addfilebutton" onclick="" class="bg_yellow">确认</a></div>
</div>

</body>
</html>
