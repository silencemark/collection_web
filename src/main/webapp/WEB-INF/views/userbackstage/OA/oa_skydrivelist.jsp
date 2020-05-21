<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>OA办公企业简报-使用方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/sweetalert/dist/sweetalert-dev.js"></script> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/ueditor/themes/default/css/ueditor.css"/>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/ueditor/ueditor.all.js"></script>
<script src="<%=request.getContextPath() %>/userbackstage/oa/oa_skydrivelist.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organize.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organizeRange.js"></script>

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
		
		
		
		
		$('#file_upload').fileupload({    
			url:'<%=request.getContextPath() %>/upload/documentUpload?type=7', 
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
	        	data = data.result;
	        	if(data.error == undefined || data.error == null){
	        		//删除文件
	        		var oldfileurl = $('#fileurl').val();
	        		deletefile(oldfileurl);
	        		
	        		$('#file_div').show();
	        		$('#filename').text(data.name);
	        		$('#download_div').attr("onclick","downloadfile('"+data.url+"')");
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
	        	}
	        },
	        progressall: function (e, data) {
	           var progress = parseInt(data.loaded / data.total * 100, 10);
	            $('#progress .bar').css(
	                'width',
	                progress + '%'
	            );
	            $('#progress .bar').html('正在上传'+progress+'%/100%');
	        }
	    });
	});
	
	//删除文件
	function deletefile(fileurl){
		$.ajax({
			url:"/upload/deleteDocumentFile",
			type:"post",
			data:"fileurl="+fileurl,
			success:function(){}
		});
	}
	
	function downloadfile(name){
		window.location.href="<%=request.getContextPath() %>/upload/download?fileName="+name;
	}
</script>
<style>
	#range_div li{
	    float: left;
    border: 1px solid #eee;
    height: 30px;
    line-height: 30px;
    padding: 0px 10px;
    margin-bottom: 10px;
    margin-right: 10px;
    position: relative;
    }
</style>
</head>

<body>
<jsp:include page="../top.jsp" flush="true"></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" flush="true"></jsp:include>
	<div id="cloudfile_div">
		<input type="hidden" id="moduleid" value="${map.moduleid }"/>
		<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">OA办公</a><i>/</i><a href="/userbackstage/getCompanyCloudModuleList">企业云盘</a><i>/</i><span>企业云盘文件</span></p></div>  
	    <div class="page_tab m_top">
	        <div class="tab_name"><span class="gray1">企业云盘文件</span><a href="javascript:void(0)" onclick="editor_close_open()" class="wid_01">新建文件</a><input type="button" value="搜索" onclick="searchKey()" class="btn" /><input type="text" id="title" class="text" placeholder="请输入关键字" /></div>
	        <div class="worksheet">
	        	<div class="l_tree" style="width:30%; max-height:460px; overflow:auto;" id="organizetree">
	            	
	            </div>
	            <div class="r_list" style="width:69%; min-height:460px;">
	            	<div class="briefing_list">
	                	<ul id="file_list">
	<!--                     	<li> -->
	<!--                         	<div class="name"><a href="#">文件名称文件名称文件名称</a><i>2016-06-02 10:30</i></div> -->
	<!--                             <div class="txt">文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述</div> -->
	<!--                         </li> -->
	                    </ul>
	                </div>
	                <div id="Pagination" style="width:450px;"></div><!--动态的获取pagination的宽度赋值给Pagination-->
	            </div>
	            <div class="clear"></div>
	        </div>
	    </div>
	</div>
	
	<div id="editor_div" style="display: none;">

	<script>
		var editor = new baidu.editor.ui.Editor({initialFrameHeight:400,initialFrameWidth:1000}); 
		editor.render("myEditor");
	</script>
	<input type="file" style="display: none" name="myfiles" id="file_upload" multiple/>
		<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">OA办公</a><i>/</i><a href="/userbackstage/getCompanyCloudModuleList">企业云盘</a><i>/</i><a href="#" onclick="editor_close_open()">企业云盘文件</a><i>/</i><span>企业云盘文件编辑</span></p></div>  
	    <div class="page_tab m_top">
	        <div class="tab_name"><span class="gray1">企业云盘文件编辑</span></div>
	        <div class="detail_edit">
	        	<span class="name">标题</span>
	            <input type="text" class="text" placeholder="请输入标题" id="add_title"/>
	            <div class="clear"></div>
<!-- 	            <span class="name">上传文件</span> -->
<!-- 	            <div class="fiel_box"> -->
<!-- 	                <div id="file_div" style="display: none;"> -->
<!-- 		               	<div class="fiel_name" id="filename"></div> -->
<!-- 		                <div class="fiel_num" id="memory"></div> -->
<!-- 		                <input type="hidden" id="kb_memory"/> -->
<!-- 		                <input type="hidden" id="fileurl"/> -->
<!-- 		                <div class="fiel_link" style="cursor: pointer;" id="download_div">下载</div> -->
<!-- 	                </div>    -->
<!-- 	                <div class="clear"></div>    -->
<!-- 	                <div class="fiel_scbtn"><input type="button" onclick="$('#file_upload').click();" value="上传文件" /><em>文件不能超过400M</div> -->
<!-- 	            </div> -->
<!-- 	            <div class="clear"></div>             -->
	            <div style="height:320px;" id="myEditor">
            		
           		</div>
	            <div class="line"></div>
	            <span class="name">发布范围</span>
	            <div class="fbfw">
	            	<div id="range_div"></div>
	                <a href="javascript:void(0)" onclick="close_open_div()" class="add">添加</a>
	                <div class="clear"></div>
	            </div>
	            <div class="clear"></div>
	            <div class="tj_btnbox"><a href="javascript:void(0)" onclick="addCloudFile(this)" class="bg_yellow">保存</a><a href="#" onclick="editor_close_open()" class="bg_gay2">取消</a><div class="clear"></div></div>
	        </div>
	    </div>
	<div class="div_mask" style="display:none;"></div>
		<div class="tc_selrange" style="display:none" id="tc_selrange">
			<div class="tc_title"><span>填写范围</span><a href="javascript:void(0);" onclick="close_open_div()">×</a></div>
		    <div class="range">
		    	<div class="l_box" id="organizetree2"  style="overflow:hidden;overflow-y:visible; width:280px;">
		    	
		        </div>
		        <div class="r_box" style="width:440px;overflow:hidden;overflow-y:visible;">
		        	<div class="sel_xx">
		                <div class="sel_box">
		                    <input type="text" class="text" placeholder="请输入姓名" id="searchrealname"/>
		                    <input type="button" class="find_btn" value="搜索" onclick="searchuser()" />
		                    <div class="clear"></div>
		                </div>
		                <ul id="rangeul">
		                </ul>
		            </div>
		            <div class="tab_list">
		            	<table width="100%" border="0" cellpadding="0" cellspacing="0" id="usertable">
		                	<tr class="head_td">
		                    	<td width="30">&nbsp;</td>
		                        <td width="70">姓名</td>
		                        <td width="50">性别</td>
		                        <td width="110">电话</td>
		                        <td>操作</td>
		                    </tr>
		                </table>
		            </div>
		            <div id="Pagination" style="width:450px;"><div id="userPagination"></div></div><!--动态的获取pagination的宽度赋值给Pagination-->
		            <div class="tc_btnbox"><a  href="javascript:void(0);" onclick="close_open_div()"  class="bg_gay2">取消</a>
		            <a href="javascript:void(0)" onclick="addRange()"  class="bg_yellow">确定</a>
		            </div>
		        </div>
		        <div class="clear"></div>
		    </div>
		</div>
	</div>
    <script type="text/javascript">
	var user_param = new Object();
	user_param.rowPage = 5;
	function callbackfunc1(organizeid){
		if(organizeid != null && organizeid != ""){
			user_param.organizeid = organizeid;
			user_param.currentPage = 1;
			user_param.searchrealname = "";
		}
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath() %>/userbackstage/getUserByOrganize",
			data:user_param,
			success:function(data){
				showOrganizeUser(data);
			}
		})
	}
	function searchuser(type){
		if(type!="search"){
			var searchrealname=$('#searchrealname').val();
			if(searchrealname == ""){
				swal("","请输入用户名称！","warning");
				return false;
			}
			user_param.searchrealname = searchrealname;
			user_param.currentPage = 1;
			user_param.organizeid = "";
		}
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath() %>/userbackstage/getSearchUserByName",
			data:user_param,
			success:function(data){
				showOrganizeUser(data);
			}
		})
	}
	
	function pageHelper1(num){
		user_param.currentPage = num;
		if(user_param.organizeid != "" && user_param.searchrealname == ""){
			callbackfunc1("");
		}else if(user_param.organizeid == "" && user_param.searchrealname != ""){
			searchuser("search")
		}
	}
	
	function showOrganizeUser(data){
		var pager = data.pager1;
		$('#userPagination').html(pager);
		$('#usertable').html("<tr class=\"head_td\">"+
	        	"<td width=\"40\">&nbsp;</td>"+
	            "<td width=\"50\">姓名</td>"+
	            "<td width=\"50\">性别</td>"+
	            "<td width=\"100\">电话</td>"+
	            "<td>操作</td>"+
	        "</tr>");
		if(data.userlist.length>0){
			var temp="";
			for(var i=0;i<data.userlist.length;i++){
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
                    temp+="<td><a href=\"javascript:void(0)\" onclick=\"adduser(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"')\" class=\"link\">添加</a></td></tr>";
			}
			$('#usertable').append(temp);
		}
	}
	
	function adduser(obj,userid,realname){
		$('#rangeul').find("input[value="+userid+"]").parent().remove();
		var temp="<li><input type=\"hidden\" name=\"userid\" value=\""+userid+"\"/>"+realname+"<a class=\"del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\" onclick=\"deleteorganizeuser(this)\" /></a></li>";
		$('#rangeul').append(temp);
	}
</script>
</div>

</body>
</html>
