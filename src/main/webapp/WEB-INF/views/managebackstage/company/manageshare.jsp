<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>主页</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/appbase.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/page.js"></script>

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
$(document).ready(function(){
	$('#company').parent().parent().find("span").attr("class","bg_hidden");
	$('#company').attr('class','active li_active');
});





var userid = "";
var companyid = "";
var organizeid = "";
var realname = "";
var companyname = "";
var datacode = "";

var obj = new Object();

$(function(){
	
	userid = $('#userid').val();
	companyid = $('#companyid').val();
	realname = $('#realname').val();
	companyname = $('#companyname').val();
	$('#changed_orgname').text(companyname);
	
	obj.companyid = companyid;
	//查询分享信息
	queryShareInfo();
	
	//加载更多--分享信息
	PageHelper({
		url:"/managebackstage/getShareList",
		data:obj,
		success:function(resultMap){
			if(resultMap != undefined && resultMap != null){
				if(resultMap.status == 0 || resultMap.status == '0'){
					var sharelist = resultMap.sharelist;
					showShareInfo(sharelist);
				}
			}
		}
	});
	
});

/*----------------------------------------------------------------查询组织机构js------------------------------------------------------------------*/
//选择的组织的id
function callbackfunc1(orgid,orgname,datacode){
	$('#change_organizeid').val(orgid);
	$('#change_organizename').val(orgname);
	$('#change_datacode').val(datacode);
}

function changeshopp(){
	obj.datacode = $('#change_datacode').val();
	datacode = $('#change_datacode').val();
	if(obj.datacode.length == 4){
		obj.datacode = "";
	}
	$('#changed_orgname').text($('#change_organizename').val());
	
	$('#sharelist_ul').empty();
	queryShareInfo();
	close_open_organizeDiv();
}



$(function(){
	$.ajax({
		type:"post",
		dataType:"json",
		data:"companyid="+companyid,
		url:"/managebackstage/getOrganizeBySystem",
		success:function(data){
			var organizelist = data.organizelist;
			showOrganize2(organizelist,data.compandyname);
		}
	});
});
var initorganizeid2="";
function showOrganize2(list,compandyname){
	if(list.length > 0){
		for(var i=0;i<list.length;i++){
			if(list[i].parentid=="" || typeof(list[i].parentid)=="undefined"){
				var temp="<ul class=\"tree_list\">"+
            	"<li id=\"li2_"+list[i].organizeid+"\">"+                    	                    	
            		"<div class=\"gray_line\"></div>"+
                	"<span class=\"bg_show\" onclick=\"changenextul2(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"','"+list[i].organizename+"','"+list[i].datacode+"')\">"+
					"<i id=\"companyname\">"+list[i].organizename+"</i></span>"+
                "</li>"+
                "</ul>";
				initorganizeid2=list[i].organizeid;
				$("#organizetree2").html(temp);
				appendli2(list,list[i].organizeid);
			}
		}
	}
}

function appendli2(list,organizeid){
	var indexnum=0;
	for(var i=0;i<list.length;i++){
		if(list[i].parentid==organizeid){
			var ultemp="";
			if(organizeid==initorganizeid2){
				ultemp+="<ul style=\"display:block;\" id=\"ul2_"+list[i].parentid+"\"></ul>";
			}else{
				ultemp+="<ul style=\"display:none;\" id=\"ul2_"+list[i].parentid+"\"></ul>";
			}
			
			var temp="<li class=\"li_bg\" id=\"li2_"+list[i].organizeid+"\">";
			indexnum++;
			if(list[i].childnum>0){
				temp+="<div class=\"gray_line\" style=\"display:none\"></div><span class=\"bg_hidden\" onclick=\"changenextul2(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"','"+list[i].organizename+"','"+list[i].datacode+"')\">";
				
			}else{
				temp+="<span class=\"bg_last\" onclick=\"changenextul2(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"','"+list[i].organizename+"','"+list[i].datacode+"')\">";
			}
            temp+="<i id=\"companyname\">"+list[i].organizename+"</i></span></li>";

            if($("#ul2_"+list[i].parentid).length>0){
            	$("#ul2_"+list[i].parentid).append(temp);
			}else{
				$("#li2_"+organizeid).append(ultemp);
				$("#ul2_"+list[i].parentid).append(temp);
			}
			$("ul li").find("div[class=white_line]").remove();
			$("ul li:last-child").append("<div class=\"white_line\" name=\"white_box\">");
			
			appendli2(list,list[i].organizeid);
			
			
		}
	}
	var whiteHeight=0;
	
}

function changenextul2(obj,isonlyread,organizeid,organizename,datacode){
	if(isonlyread != '1'){
		//回调查询
		callbackfunc1(organizeid,organizename,datacode);
		$('#organizetree2').find("i").attr("class","");
		$(obj).find("i").attr("class","bg_yellow");
	}
	if($(obj).attr("class")=="bg_hidden"){
		$(obj).prev().show();
		$(obj).attr("class","bg_show");
		$(obj).nextAll("ul").show();
		changeheight2();
	}else if($(obj).attr("class")=="bg_show"){
		$(obj).attr("class","bg_hidden");
		$(obj).nextAll("ul").hide();
		$(obj).prev().hide();
		changeheight2();
	}
}
function changeheight2(){
	var whiteHeight=0;
	$(".tree_list .white_line").each(function() {	
		whiteHeight = $(this).parent().height();
		whiteHeight = whiteHeight - 21 ;
	    $(this).height(whiteHeight) ;
	});
}


//打开或者关闭区域选择层
function close_open_organizeDiv(){
	var disk = $('#organize_mask').css("display");
	var organize = $('#organize_div').css("display");
	if(disk == "none" && organize == "none"){
		$('#organize_mask').show();
		$('#organize_div').show();
	}else{
		$('#organize_mask').hide();
		$('#organize_div').hide();
	}
}


/*--------------------------------------------------------------------查询分享信息相关js--------------------------------------------------------------------*/


//查询分享信息
function queryShareInfo(){
	requestPost("/managebackstage/getShareList",obj,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == 0 || resultMap.status == '0'){
				var sharelist = resultMap.sharelist;
				showShareInfo(sharelist);
			}
		}
	});
}

//展示分享信息
function showShareInfo(sharelist){
	if(sharelist != null && sharelist.length > 0){
		//循环显示所有的评论
		var htm = "";
		$.each(sharelist,function(i,map){
			//得到评论相关的图片list，评论list，点赞人list
			var commentlist = map.commentlist;
			var imagelist = map.imagelist;
			var praiselist = map.praiselist;
			
			//循环显示所有的图片
			var imghtml = "";
			if(imagelist.length > 0 && imagelist != null){
				$.each(imagelist,function(g,imgmap){
					imghtml += '<span><img src="'+projectpath+imgmap.visiturl+'"></span>';
				});
				imghtml += '<div class="clear"></div>';
			}
			//循环显示所有的评论信息
			commhtml = "";
			if(commentlist != null && commentlist.length > 0){
				$.each(commentlist,function(c,cmtmap){
					var reply = "";
					//判断是否是对某条评论信息的回复
					if(cmtmap.replyname != ""){
						reply = '&nbsp;回复&nbsp;<span>'+cmtmap.replyname+'</span>';
					}
					commhtml += '<p>'+
									'<span style="cursor: pointer;">'+cmtmap.createname+'</span>'+reply+': '+
									'<font style="cursor: pointer;">'+cmtmap.content+'</font>'+
								'</p>';
				});
			}
			//判断当前用户是否点赞
			var isparise = 'class="ico_zan"';
			//循环显示所有点赞人名称
			parisehtml = "";
			var ki = 0;
			if(praiselist != null && praiselist.length > 0){
				$.each(praiselist,function(p,praisemap){
					if(praisemap.createname != undefined){
						var dou = "，";
						if(ki==0){dou = "";}
						parisehtml += '<font style="cursor: pointer;" id="parseid_'+map.shareid+'_'+praisemap.createid+'">'+dou+praisemap.createname+'</font>';
						if(praisemap.createid == userid){
							isparise = 'class="ico_zaned"';
						}
						ki=1;
					}
				});
			}
			htm += '<li>'+
						   '<div class="txt">'+
					        	'<div style="cursor: pointer;" class="user_img" onclick="personal(\''+map.createid+'\',\''+map.createname+'\')"><img src="'+projectpath+map.headimage+'" width="52" height="52"></div>'+
					            '<div class="name"><span>'+map.createname+'</span><i>'+appBase.parseDateMinute(map.createtime)+'</i></div>'+
					            '<p>'+map.content+'</p>'+
					            '<div class="img">'+imghtml+'</div>'+
					        '</div>'+
					        '<div class="com_list">'+
					        	'<div class="zan_box" id="parise_list_'+map.shareid+'">'+parisehtml+'</div>'+
					            '<div class="li_list" id="comment_list_'+map.shareid+'">'+
					                commhtml+
					            '</div>'+
					        '</div>'+
					    '</li>';
		});
		$('#sharelist_ul').append(htm);
	}
}

</script>
</head>
<body>

<input type="hidden" id="companyid" value="${companyInfo.companyid }"/>
<input type="hidden" id="companyname" value="${companyInfo.companyname }"/>

<jsp:include page="../top.jsp"></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList">企业信息列表</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyInfo?companyid=${map.companyid}">公司信息</a><i>/</i><span>分享圈</span></p></div>  
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1" onclick="close_open_organizeDiv()" id="changed_orgname">${companyInfo.companyname }</span></div>
        <div class="page_tab2">
            <div class="share_list">
                <ul id="sharelist_ul">
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
</body>
</html>