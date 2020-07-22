<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>首页免费电影管理-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>  
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
	
$(document).ready(function(){
	$('#indexmovielist').parent().parent().find("span").attr("class","bg_hidden");
	$('#indexmovielist').attr('class','active li_active');
})


//隐藏窗口
function checkHide(num){
	if(num == 1) {
		$('#movieform').submit();
	}else {
		$("#moviediv").hide();	
		$(".div_mask").css("display","none");
		$("#movieid").val("");
		$("#coverimg").val("");
		$("#coverimgurl").attr("src", "");
		$("#title").val(title);
		$("#description").val("");
		$("#httpurl").val("");
		$("#type").val("");
		$("#status").val("");
	}
}

//显示隐藏窗口
function updatemovie(movieid, coverimg, title, description, httpurl, type, hotforder, status){
	$("#moviediv").show();	
	$(".div_mask").css("display","block");
	$("#movieid").val(movieid);
	$("#coverimg").val(coverimg);
	$("#coverimgurl").attr("src", coverimg);
	$("#title").val(title);
	$("#description").val(description);
	$("#httpurl").val(httpurl);
	$("#type").val(type);
	$("#hotforder").val(hotforder);
	$("#status").val(status);
}

//显示隐藏支付凭证
function checkImageShowHide(rum,imagesrc){
	if(rum == 1 ){
		$("#imgdiv").hide();	
		$(".div_mask").css("display","none");
	}else{
		$("#imgdiv").show();	
		$(".div_mask").css("display","block");
		$("#imagesrc").attr("src", imagesrc);
	}
}
</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页免费电影管理</a><i>/</i><span>首页电影列表</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">首页免费电影管理列表</span><a href="#" onclick="updatemovie('','','','','','','')">添加</a></div>
        <div class="sel_box">
        	<form action="<%=request.getContextPath()%>/managebackstage/getIndexMovieList" method="post">
        		<input type="text" class="text" placeholder="请输入电影名称" name="title" value="${map.title}"/>
	            <input type="text" class="text" placeholder="请输入电影描述" name="description" value="${map.description }"/>
	            <select class="sel" name="status">
	            	<option>全部</option>
	            	<option value="1" <c:if test="${map.status == '1' }">selected="selected"</c:if>>有效</option>
	            	<option value="0" <c:if test="${map.status == '0' }">selected="selected"</c:if>>无效</option>
	            </select>
	            <select class="sel" name="type">
	            	<option>全部类别</option>
	            	<option value="2" <c:if test="${map.type == '2' }">selected="selected"</c:if>>珍藏电影</option>
	            	<option value="3" <c:if test="${map.type == '3' }">selected="selected"</c:if>>推荐动漫</option>
	            	<option value="4" <c:if test="${map.type == '4' }">selected="selected"</c:if>>电视剧</option>
	            </select>
	            <input type="text" class="text" placeholder="请输入录入日期"  name="createtime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${map.createtime}"/>
	            <input type="submit" value="搜索" class="find_btn"  />
            </form>
            <div class="clear"></div>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
                	<td>电影名称</td>
                    <td>电影描述</td>
                    <td>电影封面</td>
                    <td>电影链接</td>
                    <td>电影类别</td>
                    <td>热度排序值</td>
                    <td>状态</td>
                    <td>创建时间</td>
                    <td>修改时间</td>
                    <td>操作</td>
                </tr>
                <c:forEach items="${list }" var="li">
                	<tr>
	                	<td>${li.title }</td>
	                    <td width="20%">${li.description }</td>
	                    <td onclick="checkImageShowHide(0,'${li.coverimg }');"><img src="${li.coverimg }" alt="" width="48px" height="48px" /></td>
	                    <td>${li.httpurl }</td>
	                    <td><c:choose>
	                    	<c:when test="${li.type == 1 }">
	                    		<i class="red">会员专享</i>
	                    	</c:when>
	                    	<c:when test="${li.type == 2 }">
	                    		<i class="red">珍藏电影</i>
	                    	</c:when>
	                    	<c:when test="${li.type == 3 }">
	                    		<i class="red">推荐动漫</i>
	                    	</c:when>
	                    	<c:when test="${li.type == 4 }">
	                    		<i class="red">电视剧</i>
	                    	</c:when>
	                    </c:choose></td>
	                    <td>${li.hotforder }</td>
	                    <td>${li.status ==1?'有效':'无效' }</td>
	                    <td>${li.createtime }</td>
	                    <td>${li.updatetime }</td>
	                    <td><a href="javascript:void(0)" onclick="updatemovie('${li.movieid}','${li.coverimg}','${li.title}','${li.description}','${li.httpurl}','${li.type}', '${li.hotforder }','${li.status}')" class="blue">修改</a></td>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div>
    </div>
</div>

<div class="div_mask" style="display:none;"></div>

<div class="tc_changetext"  id="moviediv"  style="display:none;top: 40%;left: 40%; width: 650px;">
	<div class="tc_title"><span>新增/修改免费电影信息</span><a href="#" onclick="checkHide(0)">×</a></div>
    <form action="<%=request.getContextPath() %>/managebackstage/insertOrupdateIndexMovie" id="movieform" method="post">
    <input type="hidden" name="movieid"  id="movieid"/>
    <div class="box">
    	<span>封面图</span>
    	<input type="hidden" name="coverimg" id="coverimg" />
        <div class="img3"><img src="" onclick="$('#fileName').click();" width="150px" height="200px" id="coverimgurl"/></div>
        <div class="clear"></div>
        <span>电影名称</span>
        <input type="text" class="text2"  placeholder="请输入电影名称"  name="title" id="title"/>
        <div class="clear"></div>
        <span>电影描述</span>
        <textarea placeholder="请输入电影描述，最多允许输入800字符" maxlength="800" cols="43" style="border: 1px solid #eee;" rows="2" name="description" id="description"></textarea>
        <div class="clear"></div>
        <span>电影链接</span>
        <input type="text" class="text2" placeholder="请输入电影链接" name="httpurl" id="httpurl"/> 
        <div class="clear"></div>
        <span>电影类型</span>
        <select class="sel" name="type" id="type">
           	<option value="2">珍藏电影</option>
           	<option value="3">推荐动漫</option>
           	<option value="4">电视剧</option>
         </select>
        <div class="clear"></div>
        <span>排序值</span>
        <input type="number" class="number"  placeholder="请输入排序值"  name="hotforder" value="1" id="hotforder"/>
        <div class="clear"></div>
        <span>是否有效</span>
        <select class="sel" name="status" name="status">
           	<option value="1">有效</option>
           	<option value="0">无效</option>
         </select>
        <div class="clear"></div>
    </div>
    </form>
    <div class="tc_btnbox"><a href="#" class="bg_gay2" onclick="checkHide(0)">取消</a><a href="#"  class="bg_yellow" onclick="checkHide(1)">确认</a></div>
</div>

<div class="tc_changetext"  id="imgdiv"  style="display:none;width: 560px;top:40%;">
	<div class="tc_title"><span>展示电影封面</span><a href="#" onclick="checkImageShowHide(1,'')">×</a></div>
    <div class="box">
    	<span>电影封面</span>
        <img id="imagesrc" width="400px" height="500px"></i>
        <div class="clear"></div>
    </div>
    <div class="tc_btnbox"><a href="#"  class="bg_yellow" onclick="checkImageShowHide(1,'')">确定</a></div>
</div>

<input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/>
<script type="text/javascript">
function ajaxFileUpload(id,Fileid,noimg){
	if(!Fileid){
		Fileid = "fileName";
	}
	hhutil.ajaxFileUpload("<%=request.getContextPath()%>/upload/manageheadimg",Fileid,function(data){
			if(data.imgkey){
				$("#coverimgurl").attr("src",data.imgkey);
				$("input[name=coverimg]").val(data.imgkey);
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
