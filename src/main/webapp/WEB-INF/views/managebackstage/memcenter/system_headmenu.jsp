<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>使用方PC头部菜单设置</title>
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

	
	
function checkUpdate(id){
		var parma = new Object ();
		parma.codeid = id;
		$.ajax({
			type:'post',
			dataType:'json',
			url:"<%=request.getContextPath() %>/managebackstage/getSystemPctopDetails",
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
					$("#insert").hide();
					$("#update").show();
					$('.tc_changetext').show();
					$('.div_mask').show();
					if(data.dataMap!= null){
						if(data.dataMap.type == 1){
							$('#site').attr('class','radio');$('#modular').attr('class','radio_ed');
							$("#"+data.dataMap.modularid+"").attr("selected","selected");
							$(".sel").show();
							$("#sitetext").hide();
						}else{
							$('#site').attr('class','radio_ed');$('#modular').attr('class','radio');
							$("#sitetext").show();
							$(".sel").hide();
						}
					}
					$("#name").val(data.dataMap.name);
					$("#sitetext").val(data.dataMap.url);
					$("#sort").val(data.dataMap.sort);
					$("#codeid").val(data.dataMap.codeid);
				}
			}
		})
}	
function checkDel(id){
	var parma = new Object ();
	parma.codeid = id;
	parma.delflag = 1;
	$.ajax({
		type:'post',
		url:'<%=request.getContextPath() %>/managebackstage/updateSystemPctop',
		data:parma,
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
				swal({
					title : "",
					text :	"删除成功",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href="/managebackstage/getSystemPctop";
				});
				
			}
		}
	})
}

function checkSave(){
	var parma = new Object();
	parma.codeid = 	$("#codeid").val();
	parma.name = 	$("#name").val();
	parma.sort = 	$("#sort").val();
	var mess="";
	if($("#modular").attr("class") == "radio_ed"){
		var modular = $(".sel").val();
		var modularid = "",
			modularname=""; 
		var splitmodular=modular.split(":");
		if(splitmodular!= ""){
			modularid = splitmodular[0];
			modularname = splitmodular[1];
		}
		if(modularid == ""){
			mess="请选择一个系统模块";
		}else{
			parma.modularid = modularid;
			parma.modularname = modularname;
			parma.url = splitmodular[2];
			parma.type = 1;
		}
	}else{
		var url = "" ;
		url = $("#sitetext").val();
		if(url == "" ){
			mess="请输入网址";
		}else{
			parma.url = url;
			parma.type = 0;
		}
	}
	
	if(mess==""){
		$.ajax({
			type:'post',
			dataType:'json',
			url:'<%=request.getContextPath() %>/managebackstage/updateSystemPctop',
			data:parma,
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
						location.href="/managebackstage/getSystemPctop";
					});
					
				}
			}
		})
	}else{
		swal({
			title : "",
			text :	mess,
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
function checkRadio(row){
	if(row == 1){
		$('#site').attr('class','radio');$('#modular').attr('class','radio_ed');
		$(".sel").show();
		$("#sitetext").hide();
	}else{
		$('#modular').attr('class','radio');$('#site').attr('class','radio_ed');
		$(".sel").hide()	;
		$("#sitetext").show();
	}
}
function checkAdd(){
	$('#insert').show();$('#update').hide();$('.tc_changetext').show();$('.div_mask').show();
	$('#site').attr('class','radio');$('#modular').attr('class','radio_ed');
	$(".sel").show();
	$("#sitetext").hide();
	$("#name").val("");
	$("#sort").val("");
	$("#sitetext").val("");
	$("#start").attr("selected","selected");
}

function checkInsert(){
	var parma = new Object();
	parma.name = $("#name").val();
	parma.sort = $("#sort").val();
	var mess="";
	if($("#modular").attr("class") == "radio_ed"){
		var modular = $(".sel").val();
		var modularid = "",
			modularname=""; 
		var splitmodular=modular.split(":");
		if(splitmodular!= ""){
			modularid = splitmodular[0];
			modularname = splitmodular[1];
		}
		if(modularid == ""){
			mess="请选择一个系统模块";
		}else{
			parma.modularid = modularid;
			parma.modularname = modularname;
			parma.url = splitmodular[2];
			parma.type = 1;
		}
	}else{
		var url = "" ;
		url = $("#sitetext").val();
		if(url == "" ){
			mess="请输入网址";
		}else{
			parma.url = url;
			parma.type = 0;
		}
	}
	
	if(mess==""){
		$.ajax({
			type:'post',
			dataType:'json',
			url:'<%=request.getContextPath() %>/managebackstage/insertSystemPctop',
			data:parma,
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
						location.href="/managebackstage/getSystemPctop";
					});
					
				}
			}
		})
	}else{
		swal({
			title : "",
			text :	mess,
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
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">系统设置</a><i>/</i><span>使用方PC头部菜单设置</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">使用方PC头部菜单设置</span><a href="#" onclick ="checkAdd()">添加</a></div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">
                  <c:forEach var="list" items="${dataList}">
                  	<tr>
	                	<td width="50%">${list.name}</td>
	                	<td >${list.sort}</td>
	                    <td class="t_r"><a href="#" class="link" onclick="checkUpdate('${list.codeid}')">编辑</a><a href="#" class="red" onclick="checkDel('${list.codeid}','${list.name} ')">删除</a></td>
               		</tr>
                </c:forEach>
            </table>
        </div>
         <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination-->
    </div>
</div>
<div class="div_mask" style="display:none;"></div>

<div class="tc_changetext" style="display:none;">
	<div class="tc_title"><span>使用方PC头部菜单设置</span><a href="#" onclick="$('.tc_changetext').hide();$('.div_mask').hide();">×</a></div>
    <div class="box">
        <span >名称</span>
        <input type="text" class="text2" placeholder="请输入名称" id="name" />
        <input type="hidden" class="text2"  id="codeid" />
        <div class="clear"></div>
        <span>输入类型</span>
        <a href="#" class="radio_ed" id="modular"  onclick="checkRadio(1)">选择</a><i>系统模块</i>
        <a href="#" class="radio" id="site" onclick="checkRadio(2)">选择</a><i>网址</i>
        <div class="clear"></div>
        <span>网址</span>
        <select class="sel" >
       			<option value="" id="start" >全部</option>			
       	 		<c:forEach var="modl" items="${modularList}">
       	 				 <option value="${modl.modularid}:${modl.modularname}:${modl.url}" id="${modl.modularid}" >${modl.modularname}</option>
       	 		</c:forEach>
        </select>
         <input type="text" class="text2" placeholder="请输入网址" style="display:none;" id="sitetext"/>
        <div class="clear"></div>
        <span>排序</span>
        <input type="text" class="text2" placeholder="请输入" id="sort"  />
        <div class="clear"></div>
    </div>
    <div class="tc_btnbox"><a href="#" class="bg_gay2" onclick="$('.tc_changetext').hide();$('.div_mask').hide();">取消</a>
    <a href="#"  class="bg_yellow" onclick="checkInsert()" id="insert">保存</a>
    <a href="#"  class="bg_yellow" onclick="checkSave()"  id="update">保存</a></div>
    
</div>
</body>
</html>
 