<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增巡店日志</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/shopRadioPC.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/change_examineuserinfo.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/pc_chaosongren.js"></script>
	<!-- 上传图片需要js -->   
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<%-- <script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>   --%>

<!-- 上传需要js -->      
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.ui.widget.js"></script>
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.iframe-transport.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload.js"></script>  
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-ui.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-process.js"></script>   
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-validate.js"></script>  

</head>
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


$(document).ready(function(){
	$('.restaurant_li').find("li").attr('class','');
	$('#patrollog').attr('class','active');
	$('#homepage').parent().find("li").attr('class','');
	$('#homepage').attr('class','active');
	
	
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
})
function onloaddata(){
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/app/getShopListByUser?userid=${userInfo.userid}&companyid=${userInfo.companyid}',
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
					
				});
			}else{
				if(data.shoplist.length > 0 && data.shoplist != ''){
					var temp="<option value=''>请选择店面</option>";
					for(var i=0;i<data.shoplist.length;i++){
						temp+="<option value=\""+data.shoplist[i].organizeid+"\">"+data.shoplist[i].organizename+"</option>";
					}
					$('select[name=organizeid]').html(temp);
				}
			}
		}
	})
}
</script>
<body onload="onloaddata()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>新建巡店日志</span><a href="<%=request.getContextPath() %>/pc/getTourStoreList" class="back">返回</a></div>
        <div class="page_tab2">            
            <div class="tab_list">
<!--             <input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/> -->
<input type="file" style="display: none" name="myfiles" id="fileName" multiple/>
            <form action="" id="data_form">
            <input type="hidden" id="companyid" name="companyid" value="${userInfo.companyid}"/>
		    <input type="hidden" id="createid" name="createid" value="${userInfo.userid}"/>
		    <input type="hidden" id="examineuserid" name="copyuserid" />
		    <input type="hidden" id="filelist" name="filelist"/>
		           	
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="none_border">
                    <tr>
                    	<td class="l_name">所巡门店</td>
                    	<input type="hidden" id="organizeid" name="organizeid"/>
                        <td><input type="text" class="text" placeholder="请选择所巡门店" readonly="readonly" id="organizename" onclick="$('.tc_structure').show();$('.div_mask').show();"/></td>
                    </tr>
                    <tr>
                    	<td class="l_name">到店时间</td>
                        <td><input type="text" class="text" placeholder="请输入到店时间" id="arrivetime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" name="arrivetime"/></td>
                    </tr>
                    <tr>
                    	<td class="l_name">离店时间</td>
                        <td><input type="text" class="text" placeholder="请输入离店时间" id="leavetime"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  name="leavetime" /></td>
                    </tr>
                    <tr>
                    	<td class="l_name2">发现问题</td>
                        <td>
                        	<textarea class="text_area" placeholder="请输入发现问题，最多允许输入800字符" maxlength="800" name="findproblem"></textarea>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name2">需解决问题</td>
                        <td>
                        	<textarea class="text_area" placeholder="请输入需解决问题，最多允许输入800字符" maxlength="800" name="solveproblem"></textarea>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name2">门店意见</td>
                        <td>
                        	<textarea class="text_area" placeholder="请输入门店意见，最多允许输入800字符" maxlength="800" name="opinion"></textarea>
                            <div class="clear"></div>
                            <div class="img_list">
<!--                                 <div class="img_box"><b><img src="../images/public/img.png"></b><b><img src="../images/public/img.png"></b><div class="del">删除</div></div> -->
                                <div class="img_add" onclick="checkFileImg()" id="fileimg">添加图片</div>
                                <div class="clear"></div>
                            </div>
                        </td>
                    </tr>
                    <tr class="first_td">
                    	<td class="l_name">审核人</td>
                        <td class="img_td"><div class="img f"><img src="../userbackstage/images/pc_page/user_img2.png" id="examineheadimage" width="30" height="30" /></div><i class="i_name" id="examinename"></i><a href="javascript:void(0)" onclick="openOrCloseExamineDiv()" class="add_user">添加</a></td>
                    </tr>
                    <tr>
                    	<td class="l_name">抄送人</td>
                        <td class="img_td" id="CCusernames"><a href="javascript:void(0)" onclick="showCCuserOrganize()" class="add_user">添加</a></td>
                    </tr>
                    <tr>
                    	<td class="l_name">申请人</td>
                        <td class="img_td"><div class="img f"><img src="${userInfo.headimage}" width="30" height="30" /></div><i class="i_name">${userInfo.realname}</i></td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name">填写时间</td>
                    	<input type="hidden" name="createtime" value="${createtime}"/>
                        <td>${createtime}</td>
                    </tr>
                    <tr class="foot_td">
                    	<td>&nbsp;</td>
                        <td><a href="javascript:void(0)" class="a_btn bg_yellow" onclick="submitdata()">发送</a><a href="javascript:void(0)" onclick="goBackPage()"  class="a_btn bg_gay2">取消</a></td>
                    </tr>
                </table>
                </form>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>
<div class="div_mask" style="display:none;"></div>
<div class="tc_structure" style="display: none">
	<div class="tc_title"><span>选择所巡门店</span><a href="javascript:void(0);" onclick="$('.tc_structure').hide();$('.div_mask').hide();" >×</a></div>
    <div id="organizetree1"  style="overflow:hidden;overflow-y:visible;"></div>
    <div class="tc_btnbox"><a href="javascript:void(0);" onclick="$('.tc_structure').hide();$('.div_mask').hide();"  class="bg_gay2">取消</a>
    </div>
</div>
<script type="text/javascript">
function chooseorganize(orgid,orgname){
	$('.tc_structure').hide();
	$('.div_mask').hide();
	$('#organizeid').val(orgid);
	$('#organizename').val(orgname);
}
function submitdata(){
	var time1 = $("#arrivetime").val();
	var time2 = $("#leavetime").val();
	
	var imgurl = $('img[name="fileimg"]');
	if(imgurl.length > 0){
		var alldata={"filelist":[]};  
		$.each(imgurl,function(i,index){
			var imagemap={};
			imagemap['visiturl']=$(index).attr("src");
			imagemap['resourcetype']=22;
			imagemap['type']=1;
			imagemap['size']=0;
			alldata.filelist.push(imagemap); 
		})
		$('#filelist').val(JSON.stringify(alldata));
	}
	
	if($("#organizeid").val() == ""){
		 swal({
				title : "",
				text : "请选择门店",
				type : "error",
				showCancelButton : false,
				confirmButtonColor : "#ff7922",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			}, function(){
				 
			});
		 return false;
	}
	
	if(time1 == ""){
		 swal({
			title : "",
			text : "请选择到店时间",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			 
		});
		 return false;
	    }
	
	
	if(time2 == ""){
		 swal({
			title : "",
			text : "请选择离店时间",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			 
		});
		 return false;
	}
	
	if(compareTime(time1,time2)){
	   swal({
			title : "",
			text : "到店时间不能大于离店时间",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		});
	   return false;
	}
	var jsonData = getFormBean("data_form");
	jsonData.userlist = $("#CCuseridlist").val();
	jsonData.CCusernames = $('#CCusernamelist').val();
	var flg = 0;
	if(flg == 0){
		flg = 1;
		 $.ajax({
               url:"<%=request.getContextPath()%>/app/insertTourStore",
               type:"post",
               data:jsonData,
               success:function(data){
            	   if(data.status+"" == "0"){
            		   swal({
   	       				title : "",
   	       				text : "保存成功！",
   	       				type : "success",
   	       				showCancelButton : false,
   	       				confirmButtonColor : "#ff7922",
   	       				confirmButtonText : "确认",
   	       				cancelButtonText : "取消",
   	       				closeOnConfirm : true
   	       			}, function(){
   	       				 location.href='<%=request.getContextPath() %>/pc/getTourStoreList';
   	       			}); 
            	   }else{
            		   swal({
   	       				title : "",
   	       				text : "保存失败，请稍后再试",
   	       				type : "error",
   	       				showCancelButton : false,
   	       				confirmButtonColor : "#ff7922",
   	       				confirmButtonText : "确认",
   	       				cancelButtonText : "取消",
   	       				closeOnConfirm : true
   	       			}, function(){
   	       				 
   	       			}); 
            	   }
               },error:function(e){
            	   swal({
       				title : "",
       				text : "保存失败，请稍后再试",
       				type : "error",
       				showCancelButton : false,
       				confirmButtonColor : "#ff7922",
       				confirmButtonText : "确认",
       				cancelButtonText : "取消",
       				closeOnConfirm : true
       			}, function(){
       				 
       			}); 
               }
		 });
	}
}
function compareTime(time1,time2){
	var date1 = new Date(time1.replace(/-/g,'/')).getTime();
	var date2 = new Date(time2.replace(/-/g,'/')).getTime();
	return date1>date2;
}
</script>
</body>
</html>