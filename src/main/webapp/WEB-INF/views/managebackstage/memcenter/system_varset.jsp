<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>APP参数设置</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  

	<!-- 上传图片需要js -->   
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>

	<!-- 上传文件需要js -->      
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
				$(".head_box .user_box .name").removeClass("name_border");//移除
				$(".head_box .user_box .box").hide();
			  }
			);			
});
var rownumber=0;
function ajaxFileUpload(id,Fileid,noimg){
		if(!Fileid){
			Fileid = "fileName";
		}
		hhutil.ajaxFileUpload("<%=request.getContextPath()%>/upload/manageheadimg",Fileid,function(data){
		if(data.imgkey){
			if(rownumber==1){
				checkUpdate(data.imgkey);
			}else if(rownumber==2 ){
				checkInsert(data.imgkey);
			}else if(rownumber==3 ){
				$("#androidApplyImg").attr("src",data.imgkey);
			}
			
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




function checkBanner(codeid){
	rownumber=1;
	$("#codeid").val(codeid);
	$("#fileName").click();
}	

function checkUpdate(imgkey){
	var codeid = "";
	codeid = $("#codeid").val();
	var param = new Object();
	param.url = imgkey;
	param.codeid = codeid;
	$.ajax({
		type:'post',
		url:'<%=request.getContextPath() %>/managebackstage/updateManageAppconfig',
		data:param,
		success:function(data){
			if(data.status==1){
				swal({
					title : "",
					text :	data.message,
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
					text :	data.message,
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					$("#"+codeid+"").attr("src",imgkey);	
				});
				
			}
		}
	})
}
function checkDel(codeid){
	swal({
		title : "",
		text :	"是否删除",
		type : "error",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		updateDel(codeid);
	});
}

function updateDel(codeid){
	var param = new Object();
	param.codeid = codeid;
	param.delflag = 1;
	$.ajax({
		type:'post',
		url:'<%=request.getContextPath() %>/managebackstage/updateManageAppconfig',
		data:param,
		success:function(data){
			if(data.status==1){
				swal({
					title : "",
					text :	"删除失败",
					type : "error",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					
				});
			}else{
					$("#"+codeid+"").parent().remove();	
					
			}
		}
	})
}

function checkSave(){
	if($('#tdimg div').length>5){
		swal({
			title : "",
			text :	"引导图最大只能上传6张",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
	}else{
		rownumber=2;
		$("#fileName").click();
	}
}
function checkInsert(imgkey){
	var param = new Object();
	param.url=imgkey;
	if(rownumber == 2){
		param.model="leadmodel";
	}else if(rownumber == 3){
		var content ="";
		content = $("#androidApplycontext").val();
		param.content=content;
		param.model="applymodel";
	}
	$.ajax({
		type:'post',
		url:'<%=request.getContextPath() %>/managebackstage/insertManageAppconfigr',
		data:param,
		success:function(data){
			if(data.status==1){
				swal({
					title : "",
					text :	"新增失败",
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
					text :	"新增成功",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href="<%=request.getContextPath() %>/managebackstage/getManageaAppconfig";
				});
			}
		}
	})
}

function checkAppconfig(){
	var param = new Object();
	var ioscontent="",iosurl="",androidcontent="";
	ioscontent = $("#ioscontent").val();
	iosurl = $("#iosurl").val();
	androidcontent = $("#androidcontent").val();
	
	param.ioscontent = ioscontent;
	param.iosurl = iosurl;
	param.androidcontent = androidcontent;
	$.ajax({
		type:'post',
		url:'<%=request.getContextPath() %>/managebackstage/updateManageAppconfig',
		data:param,
		success:function(data){
			if(data.status==1){
				swal({
					title : "",
					text :	data.message,
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
					text :	data.message,
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href="<%=request.getContextPath() %>/managebackstage/getManageaAppconfig";
				});
				
			}
		}
	})
}

function checkAndroidImg(){
	rownumber=3;
	$("#fileName").click();
}
function checkadd(){
	$('.div_mask').show();$('.tc_addbanner').show()
	$("#androidApplyImg").attr("src","");
	$("#androidApplycontext").val("");
}

function checkApply(){
	var img = $("#androidApplyImg").attr("src");
	checkInsert(img);
}
$(document).ready(function(){
		$('#system').parent().parent().find("span").attr("class","bg_hidden");
		$('#system').attr('class','active li_active');
		
		$('#file_upload').fileupload({    
			url:'<%=request.getContextPath() %>/upload/manageUploadApk', 
			formData:{
				fileName:'myfiles'   
			},
			type:'post',
			maxNumberOfFiles:1,    
			autoUpload:true,
	        dataType: 'json',
	        acceptFileTypes:  /(\.|\/)(apk|app|ipa|pxl|deb)$/i, 
	        maxFileSize: 419430400,  
	        done: function (e, data) { 
	        	data = data.result;
	        	$("#feedback").val(data.url);
				var url="<%= Constants.PROJECT_PATH%>/"+data.url;
				checkUpdataUrl(url);
	        },
	       
	    });
});

function checkUpdataUrl(url){
	var param = new Object();
	param.model = "androidmodel";
	param.url = url;
	$.ajax({
		type:'post',
		url:'<%=request.getContextPath() %>/managebackstage/updateManageAppconfig',
		data:param,
		success:function(data){
			if(data.status==1){
				swal({
					title : "",
					text :	"上传失败",
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
					text :	"上传成功",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					$("#url").text(url);
				});
			}
		}
	})
}

</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">系统设置</a><i>/</i><span>APP参数设置</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">APP参数设置</span></div>
        <div class="tab_list">
         <input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/>       	
		<input type="file" style="display: none" name="myfiles" id="file_upload" multiple/>
          <input type="hidden" id="codeid" />
          
        	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size: 12px;">
        	  
    			<tr>    	
    			    <td class="l_text" width="180"><div class="app_title"><span>启动图</span></div></td>	
	        		<c:forEach items="${datalist}" var="list">
	        		 	<c:if test="${list.model != ''}"> 
		        			<c:if test="${list.model=='startmodel'}">
			                   		<td><div class="app_img"><img src="${list.url}" width="80" height="128" onclick="checkBanner('${list.codeid}')" id="${list.codeid}"/></div></td>
		        			</c:if>       
		        		</c:if>	
	        		</c:forEach>
        		</tr>
                   
                <tr>
                    <td class="l_text" width="180"><div class="app_title"><span>引导图</span><a href="#" onclick="checkSave()">上传引导页</a><i>上传尺寸：1080*720</i></div></td>
                    <td id="tdimg">
                    <c:forEach items="${datalist}" var="list">
                     	<c:if test="${list.model != ''}"> 
		        			<c:if test="${list.model == 'leadmodel'}">
			                   	<div class="app_img">
			                    	<img src="${list.url}" width="80" height="128"  onclick="checkBanner('${list.codeid}')" id="${list.codeid}"/>
			                    	<a href="#" class="a_del" onclick="checkDel('${list.codeid}')"><img src="../userbackstage/images/public/del.png" alt="删除" /></a>
	                    		</div>
		        			</c:if> 
		        		</c:if>      	
	        		</c:forEach>
                    </td>
                </tr>
                
                <tr class="noneborder">
              	<td class="l_text" width="180">IOS应用版本号</td>
                    <c:forEach items="${datalist}" var="list">
                    	 <c:if test="${list.model != ''}"> 
		        			<c:if test="${list.model == 'iosmodel'}">
	                   				 <td><input type="text" class="text" placeholder="2.0.1" value="${list.content}" id="ioscontent"/></td>
		        			</c:if>    
		        		</c:if>   	
	        		</c:forEach>  
                </tr>
                
                <tr class="noneborder">
                <td class="l_text" width="180">IOS AppStore下载地址</td>
                     <c:forEach items="${datalist}" var="list">
                     	<c:if test="${list.model != ''}"> 
		        			<c:if test="${list.model == 'iosmodel'}">
	                   				 <td><input type="text" class="text" placeholder="2.0.1"  value="${list.url}" id="iosurl"/></td>
		        			</c:if> 
		        		</c:if>      	
	        		</c:forEach>
                </tr>
                
                <tr class="noneborder">
                   	 <td class="l_text" width="180">Android应用版本号</td>
                	 <c:forEach items="${datalist}" var="list">
                	 	<c:if test="${list.model != ''}"> 
		        			<c:if test="${list.model == 'androidmodel'}">    
	                   				 <td><input type="text" class="text" placeholder="2.0.1" value="${list.content}" id="androidcontent"/></td>
		        			</c:if> 
		        		</c:if>      	
	        		</c:forEach>
                </tr>
                
                <tr class="noneborder">
                	<td class="l_text" width="180">上传Adroid应用</td>
               		<td><input type="text" class="text f" placeholder="选择一个上传的应用" readonly="readonly"  id="feedback"/>
               		<input type="button" class="sc_btn" value="上传"  onclick="$('#file_upload').click();" /></td>  
                </tr>
                
                <tr class="noneborder">
                 <td class="l_text" width="180">Adroid应用的下载地址</td>
                   <c:forEach items="${datalist}" var="list">
                   		<c:if test="${list.model != ''}"> 
		        			<c:if test="${list.model == 'androidmodel'}">  	
	                   				 <td><span id="url">${list.url}</span></td>
		        			</c:if>      
		        		</c:if> 	
	        		</c:forEach>
                </tr>
                <tr class="noneborder">
                    <td class="l_text" width="180">Android上传的应用市场</td>
                    <td>
                        <c:forEach items="${datalist}" var="list">
	                   		<c:if test="${list.model != ''}"> 
			        			<c:if test="${list.model == 'applymodel'}">  	
		                   			<div class="app_yysd">
								          <i style="background-image:url(${list.url});">${list.content}<a href="#" class="a_del"  onclick="checkDel('${list.codeid}')"  id="${list.codeid}">
								          <img src="../userbackstage/images/public/del.png" alt="删除"/></a></i>
							        </div>
			        			</c:if>      
			        		</c:if> 	
	        			</c:forEach>   
	                <a href="#" class="add_yyb" onclick="checkadd()">添加</a>
                    </td>
                </tr>
                <tr class="foot_td">
                	<td>&nbsp;</td>
                    <td><a href="#" class="a_btn bg_yellow" onclick="checkAppconfig()">保存</a><a href="<%=request.getContextPath()%>/managebackstage/systemIndex" class="a_btn bg_gay2">取消</a></td>
                </tr>
            </table>
        </div>
    </div>
</div>

<div class="div_mask" style="display:none;"></div>
<div class="tc_addbanner" style="display:none;">
	<div class="tc_title"><span>添加Android上传的应用市场</span><a href="#" onclick="$('.div_mask').hide();$('.tc_addbanner').hide()">×</a></div>
    <div class="tab_list">
    	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="none_border">
        	<tr>
            	<td>应用市场名称</td>
                <td><input type="text" class="text"  id="androidApplycontext"/></td>
            </tr>
            <tr>
            	<td>图标</td>
                <td>
                	<div class="pic"><img src="" width="102" id="androidApplyImg"  /></div>
                    <div class="xx">
                    	<a href="#" onclick="checkAndroidImg()">上传</a>
                    </div>
                </td>
            </tr>
        </table>
    </div>
    <div class="tc_btnbox"><a href="#" class="bg_gay2" onclick="$('.div_mask').hide();$('.tc_addbanner').hide()">取消</a><a href="#" class="bg_yellow" onclick="checkApply()">确定</a></div>
</div>
</body>
</html>
