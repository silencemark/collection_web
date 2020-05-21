<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新建周报</title>
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
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/change_releaserange.js"></script>
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
	param.range_resourcetype = 5;
	param.userid ='${userInfo.userid}';
	param.companyid = '${userInfo.companyid}';
	param.commentuserid = $('#examineuserid').val();
	
	param.time = $('#createtime').val()+" 至 "+$('#endtime').val();
	param.completedwork = $('#completedwork').val();
	param.worksummary = $('#worksummary').val();
	param.nextplan = $('#nextplan').val();
	param.needhelp = $('#needhelp').val();
	param.remark = $('#remark').val();
	
	param.userlist = $('#userlist').val();

	param.CCuserlist = $("#CCuseridlist").val();
	param.CCusernames = $('#CCusernamelist').val();
	
	var imgurl = $('img[name="fileimg"]');
	var filelist="";
	$.each(imgurl,function(i,item){
		filelist += $(item).attr("src")+",";
	});
	filelist = filelist.substring(0,(filelist.length-1));
	param.filelist = filelist;

	if(param.createtime == ""){
		swal({
			title : "",
			text : "请输入开始时间！",
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
	if(param.endtime == ""){
		swal({
			title : "",
			text : "请输入结束时间！",
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
	if(param.completedwork == ""){
		swal({
			title : "",
			text : "请填写已完成的工作！",
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
	if(param.worksummary == ""){
		swal({
			title : "",
			text : "工作总结不能为空！",
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
	if(param.nextplan == ""){
		swal({
			title : "",
			text : "工作计划不能为空！",
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
	if(param.commentuserid == ""){
		swal({
			title : "",
			text : "点评人不能为空！",
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
		url:"/pc/addtWeekly",
		type:"post",
		data:param,
		success:function(data){
			if(data.status == "0"){
				swal({
					title : "",
					text : "周报提交成功！",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href="<%=request.getContextPath() %>/pc/getPcOaLogList";
				});
			}
		}
	});
	
}


$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#logActive').attr('class','active');
	
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
    	<div class="page_name"><span>新建周报</span><a href="<%=request.getContextPath() %>/pc/getPcOaLogList" class="back">返回</a></div>
        <div class="page_tab2">      
        	<div class="rsgj_nav">
                <ul>
                    <li ><a href="<%=request.getContextPath() %>/pc/getPcOaLogAddDay">日报</a></li>
                    <li	class="active"><a href="<%=request.getContextPath() %>/pc/getPcOaLogAddWeek">周报</a></li>
                    <li><a href="<%=request.getContextPath() %>/pc/getPcOaLogAddMonth">月报</a></li>
                </ul>
            </div>                 
            <div class="tab_list">
<!--              <input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/>       	 -->
<input type="file" style="display: none" name="myfiles" id="fileName" multiple/>
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="none_border" style="font-size:12px;">
                   <tr>
                    	<td class="l_name">时间</td>
                          <td>
                        	<input  class="text" placeholder="开始时间" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="createtime" value="${createdate}"   id="createtime"  class="text_time" type="text"  readonly="readonly"  style="width: 135px;"/>
                        	<span>-</span>
                        	<input  class="text" placeholder="结束时间" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="endtime" value="${enddate}"   id="endtime"  class="text_time" type="text"  readonly="readonly"  style="width: 135px;"/>
                        </td>
                    </tr>
                   
                    <tr>
                    	<td class="l_name2">完成工作</td>
                        <td>
                        	<textarea class="text_area" placeholder="请输入完成工作，最多允许输入800字符" maxlength="800" id="completedwork"></textarea>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name2">工作总结</td>
                        <td>
                        	<textarea class="text_area" placeholder="请输入工作总结，最多允许输入800字符" maxlength="800"" id="worksummary"></textarea>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name2">下周工作计划</td>
                        <td>
                        	<textarea class="text_area" placeholder="请输入下周工作计划，最多允许输入800字符" maxlength="800" id="nextplan"></textarea>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name2">需协调工作</td>
                        <td>
                        	<textarea class="text_area" placeholder="请输入需协调工作，最多允许输入800字符" maxlength="800" id="needhelp"></textarea>
                        </td>
                    </tr>
           			 <tr>
                    	<td class="l_name">发布范围</td>
                        <td><i class="i_name" id="releaserange"></i><a href="javascript:void(0)" onclick="openOrCloseReleaseRangeDiv()" class="add_user">添加</a></td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name2" >备注</td>
                        <td>
                        	<textarea class="text_area" placeholder="请输入备注，最多允许输入800字符" maxlength="800"id="remark"></textarea>
                            <div class="clear"></div>
                            <div class="img_list" id="img_list">
                                <div class="img_add"  onclick="checkFileImg()" id="fileimg">添加图片</div>
                                
                                <div class="clear"></div>
                            </div>
                        </td>
                    </tr>
                  </tr>
                       <tr class="first_td">
                    	<td class="l_name">点评人</td>
                    	<input type="hidden" id="examineuserid" >
                        <td class="img_td"><div class="img f"><img src="../userbackstage/images/pc_page/user_img2.png" id="examineheadimage" width="30" height="30" /></div><i class="i_name" id="examinename"></i><a href="javascript:void(0)" onclick="openOrCloseExamineDiv()" class="add_user">添加</a></td>
                    </tr>
                    <tr>
                    	<td class="l_name">抄送人</td>
                        <td class="img_td" id="CCusernames"><a href="javascript:void(0)" onclick="showCCuserOrganize()" class="add_user">添加</a></td>
                    </tr>
                    <tr>
                    	<td class="l_name">填写人</td>
                        <td class="img_td"><div class="img f"><img src="${userInfo.headimage }" width="30" height="30" /></div><i class="i_name">${userInfo.realname }</i></td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name">填写时间</td>
                        <td>${createtime}</td>
                    </tr>
                    <tr class="foot_td">
                    	<td>&nbsp;</td>
                        <td><a href="#" class="a_btn bg_yellow" onclick="checkAdd()">提交</a><a href="javascript:void(0)" onclick="goBackPage();" class="a_btn bg_gay2">取消</a></td>
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
