<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>OA办公企业简报编辑-使用方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/sweetalert/dist/sweetalert-dev.js"></script> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/ueditor/themes/default/css/ueditor.css"/>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/ueditor/ueditor.all.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organizeRange.js"></script>
<style>
	#range_div li{
	    float: left;
    border: 1px solid #eee;
    height: 30px;
    line-height: 30px;
    padding: 0px 10px;
    margin-bottom: 10px;
    margin-right: 10px;
    position: relative;
    }
</style>
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
	$(document).ready(function(){
		$('#nav_brief').attr('class','active');$('#nav_brief').parent().parent().show();
	})
</script>
</head>

<body>
<jsp:include page="../top.jsp" flush="true"></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" flush="true"></jsp:include>
	<div class="page_nav"><p><a href="javascript:void(0)">首页</a><i>/</i><a href="javascript:void(0)">OA办公</a><i>/</i><a href="/userbackstage/getCompanyModuleList">企业简报栏目</a><i>/</i><a href="javascript:void(0)" onclick="window.history.go(-1)">企业简报列表</a><i>/</i><span>企业简报编辑</span></p></div>  
    <div class="page_tab m_top" style="min-width: 1100px;">
        <div class="tab_name"><span class="gray1">企业简报编辑</span></div>
        <div class="detail_edit">
        	<span class="name">标题</span>
            <input type="text" class="text" placeholder="请输入标题" style="width:400px;" id="title"/>
            <div class="clear"></div>
            <div style="height:320px;" id="myEditor">
            	
            </div>
            <div class="line"></div>
            <span class="name">发布范围</span>
            <div class="fbfw">
<!--             	<i>浦东店<a href="javascript:void(0)" class="del"><img src="/userbackstage/images/public/del.png" alt="删除" /></a></i> -->
<!--                 <i>浦东店<a href="javascript:void(0)" class="del"><img src="/userbackstage/images/public/del.png" alt="删除" /></a></i> -->
<!--                 <i>浦东店<a href="javascript:void(0)" class="del"><img src="/userbackstage/images/public/del.png" alt="删除" /></a></i> -->
				<div id="range_div"></div>
                <a href="javascript:void(0)" onclick="close_open_div()" class="add">添加</a>
                <div class="clear"></div>
            </div>
            <div class="clear"></div>
            <div class="tj_btnbox">
		            <a href="javascript:void(0)" onclick="addBriefInfo(this)" class="bg_yellow">保存</a>
		            <a href="javascript:void(0)" onclick="window.history.go(-1)" class="bg_gay2">取消</a><div class="clear"></div></div>
        </div>
    </div>
    
</div>
<div class="div_mask" style="display: none;"></div>
	<div class="tc_selrange" style="display:none" id="tc_selrange">
			<div class="tc_title"><span>填写范围</span><a href="javascript:void(0);" onclick="close_open_div()">×</a></div>
		    <div class="range">
		    	<div class="l_box" id="organizetree2"  style="overflow:hidden;overflow-y:visible; width:280px;">
		    	
		        </div>
		        <div class="r_box" style="width:440px;overflow:hidden;overflow-y:visible;">
		        	<div class="sel_xx">
		                <div class="sel_box">
		                    <input type="text" class="text" placeholder="请输入姓名" id="searchrealname"/>
		                    <input type="button" class="find_btn" value="搜索" onclick="searchuser()" />
		                    <div class="clear"></div>
		                </div>
		                <ul id="rangeul">
		                </ul>
		            </div>
		            <div class="tab_list">
		            	<table width="100%" border="0" cellpadding="0" cellspacing="0" id="usertable">
		                	<tr class="head_td">
		                    	<td width="30">&nbsp;</td>
		                        <td width="70">姓名</td>
		                        <td width="50">性别</td>
		                        <td width="110">电话</td>
		                        <td>操作</td>
		                    </tr>
		                </table>
		            </div>
		            <div id="Pagination" style="width:450px;"><div id="userPagination"></div></div><!--动态的获取pagination的宽度赋值给Pagination-->
		            <div class="tc_btnbox"><a  href="javascript:void(0);" onclick="close_open_div()"  class="bg_gay2">取消</a>
		            <a href="javascript:void(0)" onclick="addRange()"  class="bg_yellow">确定</a>
		            </div>
		        </div>
		        <div class="clear"></div>
		    </div>
		</div>
    <script type="text/javascript">
	var user_param = new Object();
	function callbackfunc1(organizeid){
		if(organizeid != null && organizeid != ""){
			user_param.searchrealname = "";
			user_param.organizeid = organizeid;
			user_param.currentPage = 1;
			user_param.rowPage = 5;
		}
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath() %>/userbackstage/getUserByOrganize",
			data:user_param,
			success:function(data){
				showOrganizeUser(data);
			}
		})
	}
	function searchuser(type){
		if(type != "search"){
			var searchrealname=$('#searchrealname').val();
			if(searchrealname == ""){
				swal("","请输入用户名称···","warning");
				return false;
			}
			user_param.searchrealname = searchrealname;
			user_param.currentPage = 1;
			user_param.organizeid = "";
			user_param.rowPage = 5;
		}
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath() %>/userbackstage/getSearchUserByName",
			data:user_param,
			success:function(data){
				showOrganizeUser(data);
			}
		})
	}
	
	function pageHelper2(num){
		user_param.currentPage = num;
		if(user_param.searchrealname == "" && user_param.organizeid != ""){
			callbackfunc1("");
		}else if(user_param.searchrealname != "" && user_param.organizeid == ""){
			searchuser("search");
		}
	}
	
	function showOrganizeUser(data){
		var pager = data.pager2;
		$('#userPagination').html(pager);
		$('#usertable').html("<tr class=\"head_td\">"+
	        	"<td width=\"40\">&nbsp;</td>"+
	            "<td width=\"50\">姓名</td>"+
	            "<td width=\"50\">性别</td>"+
	            "<td width=\"100\">电话</td>"+
	            "<td>操作</td>"+
	        "</tr>");
		if(data.userlist.length>0){
			var temp="";
			for(var i=0;i<data.userlist.length;i++){
				temp+="<tr><td class=\"img_td\"><div class=\"img\"><img src=\"<%=request.getContextPath() %>"+data.userlist[i].headimage+"\" width=\"30\" height=\"30\" /></div></td>"+
                    "<td>"+data.userlist[i].realname+"</td>";
                    if(data.userlist[i].sex==1){
                   		temp+="<td>男</td>";
                    }else if(data.userlist[i].sex==0){
                    	temp+="<td>女</td>";
                    }else{
                    	temp+="<td></td>";
                    }
                    if(data.userlist[i].isshowphone==1){
                    	 temp+="<td>"+data.userlist[i].phone+"</td>";
                    }else{
                    	var phone=data.userlist[i].phone;
                    	temp+="<td>"+phone.substring(0,3)+"*****"+phone.substring(7,phone.length)+"</td>";
                    }
                    temp+="<td><a href=\"javascript:void(0)\" onclick=\"adduser(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"')\" class=\"link\">添加</a></td></tr>";
			}
			$('#usertable').append(temp);
		}
	}
	
	function adduser(obj,userid,realname){
		$('#rangeul').find("input[value="+userid+"]").parent().remove();
		var temp="<li><input type=\"hidden\" name=\"userid\" value=\""+userid+"\"/>"+realname+"<a class=\"del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\" onclick=\"deleteorganizeuser(this)\" /></a></li>";
		$('#rangeul').append(temp);
	}
</script>
<script>
	var editor = new baidu.editor.ui.Editor({initialFrameHeight:400,initialFrameWidth:1000}); 
	editor.render("myEditor");
</script>
<script>

$(function(){
	var ddd = "${map.briefid}";
	if(ddd != "" && ddd != null){
		queryBriefDetail();
	}
	
});

function addBriefInfo(obj){
	$(obj).attr("onclick","");
	var rge_param = {"rangelist":[]}
	var range = $('#range_div').find('input[name=organizeid]');
	var user = $('#range_div').find('input[name=userid]');
	if(range.length<=0 && user.length<=0){
		$(obj).attr("onclick","addBriefInfo(this)");
		swal({
		    title: "提示",
		    text: "请选择发布范围！",
		    type: "warning",
		    showCancelButton: false,
		    confirmButtonColor: "#ff7922",
		    confirmButtonText: "确定",
		    closeOnConfirm: true
		}, function(){});
		return false;
	}
	var rge = "";
	$.each(range,function(r,rg){
		var obj = new Object();
		obj.organizeid = $(rg).val();
		rge_param.rangelist[r] = obj;
		rge=(r+1);
	});
	$.each(user,function(u,ur){
		var obj = new Object();
		obj.userid = $(ur).val();
		rge_param.rangelist[rge+u] = obj;
	});
	
	var param = new Object();
	
	var briefid = "${map.briefid}";
	var moduleid = "${map.moduleid}";
	var url = "";
	if(briefid == "" || moduleid != ""){
		url = "/userbackstage/insertBriefInfo";
		param.moduleid = moduleid;
	}else if(briefid != "" && moduleid == ""){
		url = "/userbackstage/updateBriefInfo";
		param.briefid = briefid;
	}
	
	param.rangelist = JSON.stringify(rge_param);
	param.title = $('#title').val();
	param.content = editor.getContent();
	param.companyid = "${map.companyid}";
	
	if(param.title == ""){
		$(obj).attr("onclick","addBriefInfo(this)");
		swal({
		    title: "提示",
		    text: "简报标题不能为空！",
		    type: "warning",
		    showCancelButton: false,
		    confirmButtonColor: "#ff7922",
		    confirmButtonText: "确定",
		    closeOnConfirm: true
		}, function(){
		});
		return false;
	}
	if(param.content == ""){
		$(obj).attr("onclick","addBriefInfo(this)");
		swal({
		    title: "提示",
		    text: "简报内容不能为空！",
		    type: "warning",
		    showCancelButton: false,
		    confirmButtonColor: "#ff7922",
		    confirmButtonText: "确定",
		    closeOnConfirm: true
		}, function(){
		});
		return false;
	}
	
	requestPost(url,param,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == 0 && resultMap.status == '0'){
				$(obj).attr("onclick","addBriefInfo(this)");
				swal({
				    title: "提示",
				    text: "操作成功！",
				    type: "success",
				    showCancelButton: false,
				    confirmButtonColor: "#ff7922",
				    confirmButtonText: "确定",
				    cancelButtonText: "取消",
				    closeOnConfirm: true
				}, function(){
					window.history.go(-1);
				});
			}else{
				$(obj).attr("onclick","addBriefInfo(this)");
				swal({
				    title: "提示",
				    text: "操作失败！",
				    type: "error",
				    showCancelButton: false,
				    confirmButtonColor: "#ff7922",
				    confirmButtonText: "确定",
				    cancelButtonText: "取消",
				    closeOnConfirm: true
				}, function(){
				});
			}
		}
	});
}

function queryBriefDetail(){
	var param = new Object();
	param.briefid = "${map.briefid}";
	requestPost("/userbackstage/getBriefDetailInfo",param,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == 0 && resultMap.status == '0'){
				var briefmap = resultMap.briefmap;
				$('#title').val(briefmap.title);
				
				var rangelist = briefmap.rangelist;
				if(rangelist != undefined && rangelist != null){
					$.each(rangelist,function(i,map){
						var htm = "";
						if(map.type == 1 || map.type == '1'){
							htm += "<li><input type=\"hidden\" name=\"userid\" value=\""+map.userid+"\"/>"+map.rangename+"<a class=\"del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\" onclick=\"deleteorganizeuser(this)\" /></a></li>";
						}else if(map.type == 2 || map.type == '2'){
							htm +="<li><input type=\"hidden\" name=\"organizeid\" value=\""+map.organizeid+"\"/>"+map.rangename+"<a class=\"del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\" onclick=\"deleteorganizeuser(this)\" /></a></li>";
						}
						$("#range_div").append(htm);
					});
				}
				editor.addListener("ready", function () {
			        // editor准备好之后才可以使用
					editor.setContent(briefmap.content);
				});
				
			}
		}
	});
}

function addRange(){
	var range = $('#rangeul').html();
	$('#range_div').html(range);
	close_open_div();
}

function delete_changed_range(obj){
	$(obj).parent('i').remove();
}

function close_open_div(){
	var mask = $('.div_mask').css("display");
	var range = $('.tc_selrange').css("display");
	if(mask == "none" && range == "none"){
		$('#myEditor').hide();
		var range = $('#range_div').html();
		$('#rangeul').html(range);
		$('.div_mask').show();
		$('.tc_selrange').show();
	}else{
		$('#myEditor').show();
		$('.div_mask').hide();
		$('.tc_selrange').hide();
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
</body>
</html>
