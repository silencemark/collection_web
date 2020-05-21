<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>公告详情</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/ueditor/themes/default/css/ueditor.css"/>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/ueditor/ueditor.all.js"></script>
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
	
function onloadData(){
	$("#title").val('${dataMap.title}');
	editor.addListener("ready", function () {
        // editor准备好之后才可以使用
		editor.setContent($("#noneid").html());
	});
}

function checkUpdata(){
	var title = "" ,
		content = "",
		mess="";
	var parma = new Object();
	title = $("#title").val();
	var messageid ='${dataMap.messageid}';
	content = editor.getContent();
	parma.messageid = messageid;
	parma.title = title;
	parma.content =content;
	if(title == "" ){
		mess="请输入标题";
	}else if(title.length > 20){
		mess= "标题不能过长";
	}else if(content =="" ){
		mess="请输入内容";
	}else if(content.length > 2000 ){
		mess="内容过长";
	}
	
	if(mess != ""){
		swal({
			title : "",
			text : mess,
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
	}else{
		$.ajax({
			type:'post',
			dataType:'json',
			url:'<%=request.getContextPath() %>/managebackstage/updataSystemMessageDetail',
			data:parma,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text :	"修改失败",
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
						text :	"修改成功",
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						location.href="/managebackstage/getSystemMessageDetail?messageid="+messageid;
					});
					
				}
			}
		})
	}
}

$(document).ready(function(){
	$('#system').parent().parent().find("span").attr("class","bg_hidden");
	$('#system').attr('class','active li_active');
})
</script>
</head>

<body onload="onloadData()">
<div style="display:none;" id="noneid">${dataMap.content}</div>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>

	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">系统设置</a><i>/</i><a href="#">系统公告</a><i>/</i><span>公告详情</span></p></div>        
    <div class="notice_add">
    	<div class="box">
        	<div class="name_text"><input type="text" class="text" placeholder="请输入标题,最多可输入20字"  maxlength="20" id="title"/></div>
            <div class="edit_box" style="height:400px;"id="myEditor" >
           
            </div>
              <div class="add_btn">
            	<a href="#" class="bg_yellow" onclick="checkUpdata()">保存</a>
            	<a href="/managebackstage/getSystemMessageDetail?messageid=${dataMap.messageid}" class="bg_gay2">取消</a>
            </div>
        </div>
    </div>
</div>
<script>
	var editor = new baidu.editor.ui.Editor({initialFrameHeight:700,initialFrameWidth:1000}); 
	editor.render("myEditor");
	
	
</script>
</body>
</html>
