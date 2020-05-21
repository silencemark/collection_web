<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>OA办公企业简报-使用方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/sweetalert/dist/sweetalert-dev.js"></script> 

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
			$(".head_box .user_box .name").removeClass("name_border");//移除\
			$(".head_box .user_box .box").hide();
		  }
		);			
	});
	
	
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/managebackstage/getOrganizeBySystem",
		data:"companyid=${map.companyid}",
		success:function(data){
			var organizelist = data.organizelist;
			showOrganize(organizelist,data.compandyname);
		}
	})

	var initorganizeid="";
	function showOrganize(list,compandyname){
		if(list.length > 0){
			for(var i=0;i<list.length;i++){
				if(list[i].parentid=="" || typeof(list[i].parentid)=="undefined"){
					var temp="<ul class=\"tree_list\">"+
	                	"<li id=\"li_"+list[i].organizeid+"\">"+                    	                    	
	                		"<div class=\"gray_line\"></div>"+
	                    	"<span class=\"bg_hidden\" onclick=\"changenextul(this,'"+list[i].isonlyread+"','')\"><i id=\"companyname\">"+list[i].organizename+"</i></span>"+
	                    "</li>"+
	                "</ul>";
					initorganizeid=list[i].organizeid;
					$("#organizetree").html(temp);
					appendli(list,list[i].organizeid);
				}
			}
		}
	}

	function appendli(list,organizeid){
		var indexnum=0;
		for(var i=0;i<list.length;i++){
			if(list[i].parentid==organizeid){
				var ultemp="";
				if(organizeid==initorganizeid){
					ultemp+="<ul style=\"display:block;\" id=\"ul_"+list[i].parentid+"\"></ul>";
				}else{
					ultemp+="<ul style=\"display:none;\" id=\"ul_"+list[i].parentid+"\"></ul>";
				}
				
				var temp="<li class=\"li_bg\" id=\"li_"+list[i].organizeid+"\">";
				indexnum++;
				if(list[i].childnum>0){
					temp+="<div class=\"gray_line\" style=\"display:none\"></div><span class=\"bg_hidden\" onclick=\"changenextul(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">";
					
				}else{
					temp+="<span class=\"bg_last\" onclick=\"changenextul(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">";
				}
	            temp+="<i id=\"companyname\">"+list[i].organizename+"</i></span>";
	            temp+="</li>";

	            if($("#ul_"+list[i].parentid).length>0){
	            	$("#ul_"+list[i].parentid).append(temp);
				}else{
					$("#li_"+organizeid).append(ultemp);
					$("#ul_"+list[i].parentid).append(temp);
				}
				$("ul li").find("div[class=white_line]").remove();
				$("ul li:last-child").append("<div class=\"white_line\" name=\"white_box\">");
				
				appendli(list,list[i].organizeid);
				
				
			}
		}
		var whiteHeight=0;
		
	}


	function changenextul(obj,isonlyread,organizeid){
		if(isonlyread != '1'){
			//回调查询
			callbackfunc(organizeid);
			$('#organizetree').find("i").attr("class","");
			$(obj).find("i").attr("class","bg_yellow");
		}
		if($(obj).attr("class")=="bg_hidden"){
			$(obj).prev().show();
			$(obj).attr("class","bg_show");
			$(obj).nextAll("ul").show();
			changeheight();
		}else if($(obj).attr("class")=="bg_show"){
			$(obj).attr("class","bg_hidden");
			$(obj).nextAll("ul").hide();
			$(obj).prev().hide();
			changeheight();
		}
	}
	function changeheight(){
		var whiteHeight=0;
		$(".tree_list .white_line").each(function() {	
			whiteHeight = $(this).parent().height();
			whiteHeight = whiteHeight - 21 ;
		    $(this).height(whiteHeight) ;
		});
	}
	
	
	
	
	
	
	
	var pageParam = new Object();

	$(function(){
		pageParam.moduleid = $('#moduleid').val();
		pageParam.companyid = "${map.companyid}"
		queryCloudFile();
	});

	function pageHelper(num){
		pageParam.currentPage = num;
		$('#file_list').empty();
		queryCloudFile();
	}

	function callbackfunc(organizeid){
		pageParam.organizeid = organizeid;
		pageParam.currentPage = 1;
		$('#file_list').empty();
		queryCloudFile();
	}

	function searchKey(){
		pageParam.title = $('#title').val();
		pageParam.currentPage = 1;
		$('#file_list').empty();
		queryCloudFile();
	}

	function queryCloudFile(){
		requestPost("/managebackstage/getManageCompanyCloudFileList",pageParam,function(resultMap){
			if(resultMap != undefined && resultMap != null){
				if(resultMap.status == 0 || resultMap.status == '0'){
					$('#Pagination').html(resultMap.page);
					var filelist = resultMap.filelist;
					if(filelist != undefined && filelist != null){
						showCloudFile(filelist);
					}
				}
			}
		});
	}

	function showCloudFile(list){
		if(list != null && list.length > 0){
			
			$.each(list,function(i,item){
				var htm = '<li onclick="location.href=\'/managebackstage/intoManageCloudFileDetail?moduleid='+item.moduleid+'&fileid='+item.fileid+'&companyid=${map.companyid}\'">'+
			            	'<div class="name">'+item.title+'</a><i>'+item.createtime+'</i></div>'+
			                '<div class="txt" style="width:500px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;"></div>'+
			              '</li>';
				$('#file_list').append(htm);
			});
		}
	}
	//公共的查询方法
	function requestPost(url,param,callback){
		$.ajax({
			url:url,
			type:"post",
			data:param,
			beforeSend:function(){
				var load = '<div id="load_mask" class="div_mask" style="opacity:0;"></div>'+
					      '<div id="load_loading" class="loading"><img src="../userbackstage/images/public/loading.gif" width="360" height="200" /></div>';
				$("body").after(load);
			},
			success:function(resultMap){
				callback(resultMap);
			},
			complete:function(){
				$('#load_mask').remove();
				$('#load_loading').remove();
			},
			error:function(e){
				
			}
		});
	}
</script>
</head>

<body>
<jsp:include page="../top.jsp" flush="true"></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" flush="true"></jsp:include>
	<div id="cloudfile_div">
		<input type="hidden" id="moduleid" value="${map.moduleid }"/>
		<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList">企业信息列表</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyInfo?companyid=${map.companyid}">公司信息</a><i>/</i><a href="/managebackstage/getManageCompanyCloudModuleList?companyid=${map.companyid }">企业云盘</a><i>/</i><span>企业云盘文件</span></p></div>  
	    <div class="page_tab m_top">
	        <div class="tab_name"><span class="gray1">企业云盘文件</span><input type="button" value="搜索" onclick="searchKey()" class="btn" /><input type="text" id="title" class="text" placeholder="请输入关键字" /></div>
	        <div class="worksheet">
	        	<div class="l_tree" style="width:30%; max-height:460px; overflow:auto;" id="organizetree">
	            	
	            </div>
	            <div class="r_list" style="width:69%; min-height:460px;">
	            	<div class="briefing_list">
	                	<ul id="file_list">
	<!--                     	<li> -->
	<!--                         	<div class="name"><a href="#">文件名称文件名称文件名称</a><i>2016-06-02 10:30</i></div> -->
	<!--                             <div class="txt">文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述文件描述</div> -->
	<!--                         </li> -->
	                    </ul>
	                </div>
	                <div id="Pagination" style="width:450px;"></div><!--动态的获取pagination的宽度赋值给Pagination-->
	            </div>
	            <div class="clear"></div>
	        </div>
	    </div>
	</div>
</div>
</body>
</html>
