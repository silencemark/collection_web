<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>首页任务卡手办管理-管理方后台</title>
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
	$('#garagekit').parent().parent().find("span").attr("class","bg_hidden");
	$('#garagekit').attr('class','active li_active');
})


//隐藏窗口
function checkHide(num){
	if(num == 1) {
		$('#updateform').submit();
	}else {
		$("#updatediv").hide();	
		$(".div_mask").css("display","none");
		$("#kitid").val("");
		$("#iconimg").val("");
		$("#iconimgurl").attr("src", "");
		$("#coverimg").val("");
		$("#coverimgurl").attr("src", "");
		$("#title").val(title);
		$("#description").val("");
		$("#status").val("");
		$("#cardid123").val("");
		$("#forder").val("1");
	}
}

//显示隐藏窗口
function update(kitid, iconimg,coverimg, title, description,status, cardid, forder, avgprice, likenum){
	$("#updatediv").show();	
	$(".div_mask").css("display","block");
	$("#kitid").val(kitid);
	$("#iconimg").val(iconimg);
	$("#iconimgurl").attr("src", iconimg);
	$("#coverimg").val(coverimg);
	$("#coverimgurl").attr("src", coverimg);
	$("#title").val(title);
	$("#description").val(description);
	$("#status").val(status);
	$("#cardid123").val(cardid);
	$("#forder").val(forder);
	$("#avgprice").val(avgprice);
	$("#likenum").val(likenum);
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
	<div class="page_nav"><p><a href="<%=request.getContextPath() %>/managebackstage/getMemberCardList">VIP任务卡管理</a><i>/</i><span>任务卡手办管理</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">任务卡手办管理列表</span><a href="#" onclick="update('','','','','','','${map.cardid}','1','','')">添加</a></div>
        <div class="sel_box">
        	<form action="<%=request.getContextPath()%>/managebackstage/getGarageKitList" method="post">
        		<input type="hidden" class="text"  name="cardid" value="${map.cardid}"/>
        		<input type="text" class="text" placeholder="请输入名称" name="title" value="${map.title}"/>
	            <input type="text" class="text" placeholder="请输入描述" name="description" value="${map.description }"/>
	            <select class="sel" name="status">
	            	<option>全部</option>
	            	<option value="1" <c:if test="${map.status == '1' }">selected="selected"</c:if>>有效</option>
	            	<option value="0" <c:if test="${map.status == '0' }">selected="selected"</c:if>>无效</option>
	            </select>
	            <input type="text" class="text" placeholder="请输入录入日期"  name="createtime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${map.createtime}"/>
	            <input type="submit" value="搜索" class="find_btn"  />
            </form>
            <div class="clear"></div>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
            		<td>任务卡名称</td>
                	<td>名称</td>
                    <td width="20%">描述</td>
                    <td>手办图标</td>
                    <td>手办大图</td>
                    <td>状态</td>
                    <td>均价</td>
                    <td>被喜爱人数</td>
                    <td>排序值</td>
                    <td>创建时间</td>
                    <td>修改时间</td>
                    <td>操作</td>
                </tr>
                <c:forEach items="${list }" var="li">
                	<tr>
                		<td>${li.typename }</td>
	                	<td>${li.title }</td>
	                    <td>${li.description }</td>
	                    <td onclick="checkImageShowHide(0,'${li.iconimg }');"><img src="${li.iconimg }" alt="" width="48px" height="48px" /></td>
	                    <td onclick="checkImageShowHide(0,'${li.coverimg }');"><img src="${li.coverimg }" alt="" width="48px" height="48px" /></td>
	                    <td>${li.status ==1?'有效':'无效' }</td>
	                    <td>${li.avgprice}</td>
	                    <td>${li.likenum}</td>
	                    <td>${li.forder}</td>
	                    <td>${li.createtime }</td>
	                    <td>${li.updatetime }</td>
	                    <td><a href="javascript:void(0)" onclick="update('${li.kitid}','${li.iconimg }','${li.coverimg}','${li.title}','${li.description}','${li.status}','${map.cardid}','${li.forder}','${li.avgprice}','${li.likenum}')" class="blue">修改</a></td>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div>
    </div>
</div>

<div class="div_mask" style="display:none;"></div>

<div class="tc_changetext"  id="updatediv"  style="display:none;top: 20%;left: 40%; width: 650px;">
	<div class="tc_title"><span>新增/修改任务卡手办信息</span><a href="#" onclick="checkHide(0)">×</a></div>
    <form action="<%=request.getContextPath() %>/managebackstage/insertOrupdateGarageKit" id="updateform" method="post">
    <input type="hidden" name="kitid"  id="kitid" />
    <input type="hidden" name="cardid"  id="cardid123" />
    <div class="box">
    	<span>手办图标</span>
    	<input type="hidden" name="iconimg" id="iconimg" />
        <div class="img3"><img src="" onclick="$('#fileName1').click();" width="200px" height="200px" id="iconimgurl"/></div>
        <div class="clear"></div>
    	<span>手办大图</span>
    	<input type="hidden" name="coverimg" id="coverimg" />
        <div class="img3"><img src="" onclick="$('#fileName').click();" width="350px" height="200px" id="coverimgurl"/></div>
        <div class="clear"></div>
        <span>名称</span>
        <input type="text" class="text2"  placeholder="请输入名称"  name="title" id="title"/>
        <div class="clear"></div>
        <span>描述</span>
        <textarea placeholder="请输入描述，最多允许输入800字符" maxlength="800" cols="43" style="border: 1px solid #eee;" rows="2" name="description" id="description"></textarea>
        <div class="clear"></div>
        <span>均价</span>
        <input type="text" class="text2" placeholder="请输入均价" name="avgprice" id="avgprice"/> 
        <div class="clear"></div>
        <span>喜爱数量</span>
        <input type="number" class="number" placeholder="请输入喜爱的默认数量" name="likenum" id="likenum"/> 
        <div class="clear"></div>
        <span>排序值</span>
        <input type="number" class="number" placeholder="请输入排序值" name="forder" id="forder"/> 
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
	<div class="tc_title"><span>展示大图</span><a href="#" onclick="checkImageShowHide(1,'')">×</a></div>
    <div class="box">
    	<span>手办图片</span>
        <img id="imagesrc" width="600px" height="500px"></i>
        <div class="clear"></div>
    </div>
    <div class="tc_btnbox"><a href="#"  class="bg_yellow" onclick="checkImageShowHide(1,'')">确定</a></div>
</div>

<input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/>
<input type="file" name="myfiles" style="display: none" id="fileName1" T="file_headimg" onchange="ajaxFileUpload1('img')"/>

<script type="text/javascript">
function ajaxFileUpload(id,Fileid,noimg){
	if(!Fileid){
		Fileid = "fileName";
	}
	hhutil.ajaxFileUpload("<%=request.getContextPath()%>/upload/managecoverimg",Fileid,function(data){
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
function ajaxFileUpload1(id,Fileid,noimg){
	if(!Fileid){
		Fileid = "fileName1";
	}
	hhutil.ajaxFileUpload("<%=request.getContextPath()%>/upload/manageiconimg",Fileid,function(data){
			if(data.imgkey){
				$("#iconimgurl").attr("src",data.imgkey);
				$("input[name=iconimg]").val(data.imgkey);
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
