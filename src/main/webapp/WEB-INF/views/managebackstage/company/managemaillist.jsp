<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>通讯录</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>

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
	                    	"<span class=\"bg_hidden\" onclick=\"changenextul(this,'"+list[i].isonlyread+"','')\"><i class='bg_yellow' id=\"companyname\">"+list[i].organizename+"</i></span>"+
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
pageParam.companyid ='${map.companyid}';
function pageHelper(page){
	pageParam.currentPage = page;
	queryUserAllList();
}

//回调函数
function callbackfunc(organizeid){
	pageParam.organizeid = organizeid;
	pageParam.currentPage = 1;
	queryUserAllList();
}
function queryUserAllList(){
	$.ajax({
		type:"post",
		url:'<%=request.getContextPath() %>/managebackstage/getNextOrgainizenew',
		data:pageParam,
		success:function(data){
			$('#Pagination').html(data.pager);
			if(data.userlist.length>0){
				var temp = "";
				$.each(data.userlist,function(i,map){
					var sex = "";
					if(map.sex == ""){
						sex = "保密";
					}else if(map.sex == "1"){
						sex = "男";
					}else if(map.sex == "0"){
						sex = "女";
					}
					temp += '<tr>'+
		                        '<td class="img_td2"><div class="img"><img src="'+map.headimage+'" width="30" height="30" /></div></td>'+
		                        '<td>'+map.realname+'</td>'+
		                        '<td>'+sex+'</td>'+
		                        '<td>'+map.phone+'</td>'+
		                    '</tr>';
				});
				$('#modulediv').html(temp);
			}else{
				$('#modulediv').html("暂无数据...");
			}
		}
	});
}
	
</script>
</head>

<body onload="queryUserAllList()">
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>	
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList">企业信息列表</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyInfo?companyid=${map.companyid}">公司信息</a><i>/</i><span>通讯录</span></p></div>  
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1">${companyInfo.companyname }</span></div>
        <div class="worksheet">
        	<div class="l_tree" id="organizetree"  style="overflow:hidden;overflow-y:visible;">
        	
            </div>
            <div class="r_list">
            	<div class="tab_list">
                	<table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;" >
       						<tr class="head_td">
		       					<td width="10%">&nbsp;</td>
		       					<td width="30%">姓名</td>
		       					<td width="20%">性别</td>
	       						<td>联系方式</td>
       						</tr>
	       					<tbody id="modulediv">
	       					
	       					</tbody>
       					</table>
                </div>
                <div id="Pagination" style="width:315px;"></div><!--动态的获取pagination的宽度赋值给Pagination-->                
            </div>
            <div class="clear"></div>
        </div>
    </div>
</div>

</body>
</html>
