<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>意见反馈</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  

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

function checkUpdata(str,userid){
	var status = str;
	var content=$(".text_area").val();
	var parma = new Object();
	var feedbackid='${dataMap.feedbackid}';
	
	var mess= "";
	parma.status = status;
	parma.feedbackid = feedbackid;
	parma.createid = '${dataMap.userid}';
	parma.userid = userid;
	if(content.length > 0){
		parma.content = content;
	}
	
	if(status == 1){
		if(content.length > 0){
			parma.content = content	;
		}else{
			mess ="请输入反馈信息";
		}
	}
	
	if(mess == ""){
		$.ajax({
			type:'post',
			dataType:'json',
			url:'<%=request.getContextPath() %>/managebackstage/updataSystemBack',
			data:parma,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text :	"操作失败",
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
						location.href="/managebackstage/getSystemBackDetail?feedbackid="+feedbackid;
					});
					
				}
			}
		})
	}else{
		swal({
			title : "",
			text :	"请输入反馈信息",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
	}
}
$(document).ready(function(){
	$('#system').parent().parent().find("span").attr("class","bg_hidden");
	$('#system').attr('class','active li_active');
})
</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">系统设置</a><i>/</i><span>意见反馈</span></p></div>        
    <div class="page_tab">
    <div class="tab_name"><span class="gray1">意见反馈</span><a href="/managebackstage/getSystemBacklist">返回</a></div>
    <div class="sug_detail">
    	<div class="name"><span>${dataMap.realname}</span>
    	<i>${dataMap.companyname}<em class="red">
    								<c:choose>
    									<c:when test="${dataMap.status==0}">
    										(未处理)
    									</c:when>
    									<c:when test="${dataMap.status==1}">
    										(已处理)
    									</c:when>
    									<c:when test="${dataMap.status==2}">
    										(已忽略)
    									</c:when>
    								</c:choose>
    							</em></i>
    	<i class="time">${dataMap.createtime}</i></div>
        <div class="p_box">
     		${dataMap.description}
		</div>
			<c:choose>
    			<c:when test="${dataMap.status==0}">
    										
					<div class="text_box">
							<span>回馈意见</span>
							<textarea class="text_area" placeholder="请输入反馈信息" style="height:150px;"></textarea>
					</div>	
					<div class="a_btn">
							<a href="#" class="bg_yellow" onclick="checkUpdata(1,'${dataMap.userid}')">处理</a>
							<a href="#" class="bg_yellow" onclick="checkUpdata(2,'${dataMap.userid}')">忽略</a>
            		</div>								
				</c:when>
    			<c:when test="${dataMap.status==1}">
    				<div class="tab_list">
						<div class="name"><span>回馈意见</span><i class="time">${data.createtime}</i></div>
					 	<div class="p_box">${data.content }</div>
					</div>
    			</c:when>
    			<c:when test="${dataMap.status==2}">
    					
    			</c:when>
    		</c:choose>
		</div>
		</div>
    </div>
</div>
</body>
</html>
