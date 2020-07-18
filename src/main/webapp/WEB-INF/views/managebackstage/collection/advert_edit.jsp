<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>添加修改管理员-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>

</head>
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
function submitdata(){
	var advertid=$('input[name=advertid]').val();
	var imgurl=$('input[name=imgurl]').val();
	if(imgurl==""){
		swal({
			title : "",
			text : "请上传您的广告图",
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
	var httpurl=$('input[name=httpurl]').val();
	if(httpurl==""){
		swal({
			title : "",
			text : "请填写您的广告图链接地址",
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
	if(advertid != ""){
		//修改
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath()%>/managebackstage/updateAdvert",
			data:$('#userform').serialize(),
			success:function(data){
				var typename="success";
				if(data.status==1){
					typename="error";
				}
				swal({
					title : "",
					text : data.message,
					type : typename,
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href="<%=request.getContextPath() %>/managebackstage/getAdvertList";
				})
			}
		})
	}else{
		//添加
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath()%>/managebackstage/insertAdvert",
			data:$('#userform').serialize(),
			success:function(data){
				var typename="success";
				if(data.status==1){
					typename="error";
				}
				swal({
					title : "",
					text : data.message,
					type : typename,
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href="<%=request.getContextPath() %>/managebackstage/getAdvertList";
				})
			}
		})
	}
}
$(document).ready(function(){
	$('#advertlist').parent().parent().find("span").attr("class","bg_hidden");
	$('#advertlist').attr('class','active li_active');
})
</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
    <div class="page_nav"><p><a href="#">广告图管理</a><i>/</i><span>添加修改广告图</span></p></div>        
    <div class="page_tab">
        <div class="tab_name">
        <c:choose>
        	<c:when test="${advertInfo==null}">
        	<span class="gray1">添加广告图</span></div>
        	</c:when>
        	<c:otherwise>
        	<span class="gray1">修改广告图</span></div>
        	</c:otherwise>
        </c:choose>
        <form action="" id="userform">
        <input type="hidden" name="advertid" value="${advertInfo.advertid}"/>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr class="noneborder">
                    <td class="l_text" width="140">广告图</td>
                    <input type="hidden" name="imgurl" value="${advertInfo.imgurl}"/>
                    <td width="248" class="img_td"><div class="img3"><img src="${advertInfo.imgurl}" onclick="$('#fileName').click();" width="240" height="148" id="imageurl"/></div></td>
                    <td class="gray_word"><a href="javascript:void(0)" onclick="$('#fileName').click();" class="yellow">修改</a></td>
                </tr>
                <tr class="noneborder">
                    <td class="l_text" width="140">链接地址</td>
                    <td colspan="2" ><input type="text" class="text" placeholder="请输入链接地址"  name="httpurl" value="${advertInfo.httpurl}"/></td>
                </tr>
                <tr class="noneborder">
                    <td class="l_text" width="140">描述</td>
                    <td colspan="2" ><textarea placeholder="请输入描述" maxlength="800" cols="43" style="border: 1px solid #eee;" rows="2" name="advertcontent" >${advertInfo.advertcontent}</textarea></td>
                </tr>
                <tr class="foot_td">
                	<td>&nbsp;</td>
                    <td colspan="2"><a href="javascript:void(0)" onclick="submitdata()" class="a_btn bg_yellow">保存</a><a href="javascript:void(0)" onclick="window.history.go(-1);" class="a_btn bg_gay2">取消</a></td>
                </tr>
            </table>
        </div>
        </form>
    </div>
</div>
<input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/>
<script type="text/javascript">
function ajaxFileUpload(id,Fileid,noimg){
	if(!Fileid){
		Fileid = "fileName";
	}
	hhutil.ajaxFileUpload("<%=request.getContextPath()%>/upload/manageheadimg",Fileid,function(data){
			if(data.imgkey){
				$("#imageurl").attr("src",data.imgkey);
				$("input[name=imgurl]").val(data.imgkey);
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
</script>
</body>
</html>