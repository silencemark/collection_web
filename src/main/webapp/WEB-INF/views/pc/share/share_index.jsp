<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>主页</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<!-- 强制让文档的宽度与设备的宽度保持1:1，并且文档最大的宽度比例是1.0，且不允许用户点击屏幕放大浏览 -->
<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width, minimal-ui">
<!-- iphone设备中的safari私有meta标签，它表示：允许全屏模式浏览 -->
<meta name="apple-mobile-web-app-capable" content="yes">
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/style/artEditor.css">

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/appbase.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/page.js"></script>
<!-- 上传需要js -->      
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.ui.widget.js"></script>
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.iframe-transport.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload.js"></script>  
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-ui.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-process.js"></script>   
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-validate.js"></script>  
<script src="<%=request.getContextPath() %>/app/appcssjs/script/artEditor.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/share/share_index.js"></script>

</head>
<body>
<script type="text/javascript">
$(function(){
	$('#file_upload').fileupload({    
		url:'<%=request.getContextPath() %>/upload/imageUpload?type=8', 
		formData:{
			fileName:'myfiles'   
		},
		type:'post',
		maxNumberOfFiles:1,    
		autoUpload:true,
	    dataType: 'json',
	    acceptFileTypes:  /(\.|\/)(gif|jpe?g|png)$/,
	    maxFileSize: 419430400,  
	    done: function (e, data) {
	    	data = data.result;
	    	if(data.error == undefined || data.error == null){
	    		var temp="<img src='<%=request.getContextPath()%>"+data.url+"' url='"+data.url+"'/>";
	    		$('#img_list').append('<div class="img_box"><b>'+temp+'</b><div class="del" onclick="deleteImage(this)">删除</div></div>');
	    	}else{
	    		swal('','文件上传失败！','error');
	    	}
	    }
	});
});
$(document).ready(function(){
	$('.link ul').find("li").attr('class','');
	$('#share').attr('class','active');
});
</script>
<input type="hidden" id="userid" value="${user.userid }"/>
<input type="hidden" id="companyid" value="${user.companyid }"/>
<input type="hidden" id="organizeid" value="${user.organizeid }"/>
<input type="hidden" id="realname" value="${user.realname }"/>
<input type="hidden" id="companyname" value="${user.companyname }"/>

<input type="file" style="display: none" name="myfiles" id="file_upload" multiple/>

<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<div class="left_menu">
    	<div class="user_xx">
        	<div class="img"><img src="<%=request.getContextPath() %>${user.headimage }" width="128" height="128" /></div>
            <div class="name"><span>${user.realname }</span><a href="#">编辑</a></div>
        </div>
        <div class="menu_name">分享圈</div>
        <ul class="share_li">
        	<li class="active"><a href="<%=request.getContextPath() %>/pc/intoSharePage" class="bg_01">分享圈</a></li>
        </ul>
    </div>
    <div class="right_page">
    	<div class="page_name" id="fenxiang_div"><span>分享圈</span><a href="javascript:void(0)" onclick="close_open_organizeDiv()" class="ico_shop" id="changed_orgname">浦东店</a></div>
    	<div class="page_name" style="display: none;" id="personal_div"><span style='cursor: pointer;' onclick="allShare()">《 返回</span><a href="javascript:void(0)" class="ico_shop" id="personal_fenxiang"></a></div>
        <div class="page_tab2">
            <div class="share_text">
<!--                 <textarea placeholder="说点什么..." id="content"></textarea> -->
				<div>
			        <div class="publish-article-content" style="margin-bottom:0px; border:0px; background-color:#FAFAFA;">
			            <div class="article-content" style="height:60px;" id="content"></div>
			        </div>
			    </div>
                <div class="img_list">
                	<span id="img_list"></span>
                    <div class="img_add" onclick="$('#file_upload').click();">添加图片</div>
                    <div class="clear"></div>
                </div>
            </div>
            <div class="share_btn"><input style="cursor: pointer;" type="button" value="分享" onclick="addShare()"/></div>
            <div class="share_list">
                <ul id="sharelist_ul">
<!--                     <li> -->
<!--                         <div class="txt"> -->
<!--                             <div class="user_img"><img src="../images/pc_page/user_img2.png" width="52" height="52"></div> -->
<!--                             <div class="name"><span>唐昭仪</span><i>2016-06-20 11:30</i></div> -->
<!--                             <p>最近的业绩非常好希望大家再接再厉，争取本月有新突破</p> -->
<!--                             <div class="img"><span><img src="../images/pc_page/share_img01.png" height="220"></span><span><img src="../images/pc_page/share_img01.png" height="220"></span><div class="clear"></div></div> -->
<!--                             <div class="ico_box"><a class="ico_pl">评论</a><a class="ico_zan">赞</a></div> -->
<!--                         </div> -->
<!--                         <div class="com_list"> -->
<!--                             <div class="zan_box">Amy, 湛蓝天空，大头后厨，shuyi2，Amy, 湛蓝天空，大头后厨，shuyi2</div> -->
<!--                             <div class="li_list"> -->
<!--                                 <p><span>大头后厨</span>: 今天天气不错，客流量还是挺多的</p> -->
<!--                                 <p><span>湛蓝天空</span>: 最近的业绩非常好希望大家再接再厉，争取本月有新突破,加油加油</p> -->
<!--                             </div> -->
<!--                         </div> -->
<!--                         <div class="com_textbox"> -->
<!--                             <textarea class="text_area" placeholder="评论"></textarea> -->
<!--                             <input type="button" class="btn" value="评论"> -->
<!--                             <div class="clear"></div> -->
<!--                         </div> -->
<!--                     </li> -->
                </ul>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>


<div class="div_mask" style="display:none" id="organize_mask"></div>
<div class="tc_structure" style="display:none; max-height: 600px; overflow:hidden;overflow-y:visible;" id="organize_div">
	<div class="tc_title"><span>选择组织</span><a href="javascript:void(0)" onclick="close_open_organizeDiv()">×</a></div>
	<input type="hidden" id="change_organizeid"/>
	<input type="hidden" id="change_organizename"/>
	<input type="hidden" id="change_datacode"/>
    <div class="str_box" id="organizetree2" style="overflow: hidden; overflow-y: visible;">
    	
    </div>
    <div class="tc_btnbox"><a href="javascript:void(0)" onclick="close_open_organizeDiv()" class="bg_gay2">取消</a>
    <a href="javascript:void(0)" onclick="changeshopp()" class="bg_yellow">确定</a></div>
</div>
<script type="text/javascript">
$(function(){
	"use strict";
    $('#content').artEditor({
        imgTar: '#imageUpload',
        limitSize: 5,   // 兆
        showServer: false,
        uploadUrl: '',
        data: {},
        uploadField: 'image',
        placeholader: '<p>说点什么····</p>',
        validHtml: ["br"],
        uploadSuccess: function(res) {
            return res.path;
        },
        uploadError: function(res) {
            console.log(res);
        }
    });
});
</script>
</body>
</html>