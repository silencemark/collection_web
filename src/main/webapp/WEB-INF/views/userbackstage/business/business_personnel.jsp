<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>人员管理</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organize.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organizeCheckbox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organizeRadio.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>

<!-- 上传需要js -->      
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.ui.widget.js"></script>
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.iframe-transport.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload.js"></script>  
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-ui.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-process.js"></script>   
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-validate.js"></script>  
<%-- <script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>   --%>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>

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
	


var pageParam = new Object();
pageParam.companyid ='${userinfo.companyid}';
function pageHelper(page){
	pageParam.currentPage = page;
	queryUserAllList();
}

//回调函数
function callbackfunc(organizeid){
	
	$("#seach").show();
	if('${powerMap.power7001010}' == ''){
		$("#insert").hide();
	}else{
		$("#insert").show();
	}
	$("#retruns").hide();
	delete 	pageParam.searchname;
	pageParam.organizeid = organizeid;
	pageParam.currentPage = 1;
	if(	pageParam.organizeid== ''){
		delete 	pageParam.organizeid;
	}
	queryUserAllList();
	organizeDetailInfo(organizeid);
}
function queryUserAllList(){
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath() %>/userbackstage/getNextOrgainizeById",
		data:pageParam,
		success:function(data){
			$('#Pagination').html(data.pager);
			if(data.userlist.length>0){
				var temp="  <tr class=\"head_td\"><td width=\"2%\">&nbsp;</td><td width=\"2%\">&nbsp;</td><td width=\"10%\">姓名</td><td width=\"6%\">性别</td><td width=\"6%\">职务</td>"+
                "<td width=\"10%\">电话</td><td width=\"10%\">创建时间</td><td width=\"10%\">最后登录时间</td><td colspan='4' width=\"10%\" style=\"text-align: center;\" >操作</td></tr>";
				for(var i=0;i<data.userlist.length;i++){
// 					if(data.userinfo.userid != data.userlist[i].userid){
					if(true){
						temp += "<tr>"+
	                    	"<td><div id=\"peron\"  >"+
	                    	"<a href=\"javascript:void(0)\" name=\"peron\" class=\"check\" onclick=\"check(this)\" userid=\""+data.userlist[i].userid+"\" realname=\""+data.userlist[i].realname+"\" username=\""+data.userlist[i].username+"\"  phone=\""+data.userlist[i].phone+"\"  organizename=\""+data.userlist[i].organizename+"\"  organizeid=\""+data.userlist[i].organizeid+"\"   status=\""+data.userlist[i].status+"\"  manageid=\""+data.userlist[i].manageid+"\">选择</a></div></td>"+
	                        "<td class=\"img_td2\"><div class=\"img\">"+
	                        "<img src=\""+data.userlist[i].headimage+"\" width=\"30px\" height=\"30px\" /></div></td>";
	                        if('${powerMap.power7001010}' != ''){
	                        	 temp += "<td><a href=\"javascript:void(0)\" class=\"link\"  style='margin-right:10px;' onclick=\"checkUserById('"+data.userlist[i].userid+"')\">"+data.userlist[i].realname+"</a></td>";
	                        }else{
	                        	 temp += "<td>"+data.userlist[i].realname+"</td>";
	                        }
	                     
	                        if(data.userlist[i].sex == 0){
	                        	temp += "<td>女</td>";
	                        }else if(data.userlist[i].sex == 1){
	                        	temp += "<td>男</td>";
	                        }else{
	                        	temp += "<td> </td>"
	                        }
	                        temp +="<td>"+(data.userlist[i].position!=null && data.userlist[i].position!=undefined && data.userlist[i].position!=""?data.userlist[i].position:"")+"</td>";
	                        temp +="<td>"+data.userlist[i].phone+"</td>"+
	                        "<td>"+data.userlist[i].new_createtime+"</td>";
	                        if(data.userlist[i].new_logintime!=null){
	                        	temp += "<td>"+data.userlist[i].new_logintime+"</td>";
	                        }else{
	                        	temp +="<td></td>";
	                        }
	                       
	                        temp += "<td width=\"30px\"  style=\"padding-right: 3px;padding-left: 0px\" >";
	                        if('${powerMap.power7001010}' != ''){
	                        	temp += "<a href=\"javascript:void(0)\" class=\"start\" style='margin:0px 10px;' onclick=\"checkPower('"+data.userlist[i].userid+"')\">分配权限</a>";
	                        }
// 	                        temp += "</td>";
	                        if(data.userlist[i].isinvite==0){
	                        	temp +=  "<a href=\"javascript:void(0)\" class=\"link\" style='margin:0px 10px;' onclick=\"checkInvitation('"+data.userlist[i].userid+"','"+data.userlist[i].realname+"','"+data.userlist[i].phone+"','"+data.userlist[i].organizename+"')\">邀请</a>";
	                        }else{
	                        	temp +=  "<a href=\"javascript:void(0)\" class=\"link\" style='margin:0px 10px;' onclick=\"checkInvitation('"+data.userlist[i].userid+"','"+data.userlist[i].realname+"','"+data.userlist[i].phone+"','"+data.userlist[i].organizename+"')\">再次邀请</a>";
	                        }
	                        if(data.userlist[i].status != 1){
	                        	if('${powerMap.power7001010}' != ''){
	                        		temp += "<br/><a href=\"javascript:void(0)\" class=\"start\" style='margin:0px 23px;' onclick=\"checkStart(1,'"+data.userlist[i].userid+"','"+data.userlist[i].organizeid+"')\">启用</a>";
	                        	}
	                        	if('${powerMap.power7001010}' != ''){
	                        		temp +="<a href=\"javascript:void(0)\" class=\"del\" style='margin:0px 10px;' onclick=\"checkDelete('"+data.userlist[i].userid+"','"+data.userlist[i].manageid+"','"+data.userlist[i].organizeid+"')\">删除</a>";
	                        	}
	                        }else{
	                        	if('${powerMap.power7001010}' != ''){
	                        		temp += "<a href=\"javascript:void(0)\" class=\"del\"  style='margin:0px 10px;' onclick=\"checkStart(0,'"+data.userlist[i].userid+"','"+data.userlist[i].organizeid+"')\">禁用</a>";
	                        	}
	                        } 
	                        temp += "</td></tr>";
						}
					
					}
					temp +="<tr><td>"+
					"<a href=\"javascript:void(0)\" id=\"ckOut\" class=\"check\" onclick=\"checkOut()\" >选择</a></td>"+
					"<td colspan=\"7\"><i class=\"m_r30\">全选</i>"+
					"<a href=\"javascript:void(0)\" class=\"link\" onclick=\"checkInvitedOut()\">邀请</a>";
					if('${powerMap.power7001010}' != ''){
						temp +=	"<a href=\"javascript:void(0)\" class=\"start\" onclick =\"checkStartOut()\">启用</a>"+
						"<a href=\"javascript:void(0)\" class=\"del\"  onclick =\"checkStartedOut()\">禁用</a>";
					}
					if('${powerMap.power7001010}' != ''){
						temp +="<a href=\"javascript:void(0)\" class=\"del\" onclick=\"checkDeleteOut()\">删除</a></td></tr>";
					}
			
					$('#modulediv').html(temp);
				}else{
					$('#modulediv').html("暂无数据...");
				}
			}
	});
}
	
function organizeDetailInfo(organizeid){
	
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath() %>/userbackstage/getOrganizeInfo",
		data:"organizeid="+organizeid,
		success:function(data){
			var addr = data.address;
			if(addr == undefined){
				data.address = "";
			}
			var temp = "<td width=\"25%\" style=\"text-overflow:ellipsis ; overflow:hidden; white-space:nowrap;\">组织名称：【"+data.organizename+"】</td>"+
		            	'<td width="15%">员工人数：'+data.usernum+' 人</td>';
	        if(data.type==3){
	        	temp += "<td width=\"20%\">二维码：<img src=\"../userbackstage/images/public/ico_ewm.png\" />&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"$('.tc_ewmxx').show();$('.div_mask').show();\" class=\"blue\">查看</a></td>"+
            			'<td width="25%" style=\"text-overflow:ellipsis ; overflow:hidden; white-space:nowrap;\">地址：'+data.address+'</td>';
            	if(organizeid != ""){
            		temp += "<td width=\"15%\"><a href=\"javascript:void(0)\" class=\"blue\" onclick=\"initupdate('"+data.organizeid+"','"+data.organizename+"','"+data.parentname+"','"+data.parentid+"',"+data.type+",'"+data.address+"','"+data.priority+"')\">修改</a></td>";
            	}
            	$('#qrcodeimage').attr("src",data.qrcode);
	        }else{
	        	if(organizeid != ""){
	        		temp += "<td width=\"15%\"><a href=\"javascript:void(0)\" class=\"blue\" onclick=\"initupdate('"+data.organizeid+"','"+data.organizename+"','"+data.parentname+"','"+data.parentid+"',"+data.type+",'"+data.address+"','"+data.priority+"')\">修改</a></td>";
	        	}
	        	temp += "<td width=\"15%\"></td>"+
    					'<td width="35%"></td>';
	        }
	        	
	        $('#organizedetailinfo_tr').html(temp);
	        
		}
	})
}

//点击用户查询当前用户详细信息
function checkUserById(userid){
	$("#insert").hide();
	$("#retruns").show();
	$("#seach").hide();
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath() %>/userbackstage/getUserByUserid",
		data:"userid="+userid+"&companyid=${userinfo.companyid}",
		success:function(data){
			if(data.userinfo.length != null || data.userinfo.length != ""){
				var posit = data.userinfo.position;
				if(posit == "undefined" || posit == undefined){
					data.userinfo.position = "";
				}
				var organizenames=data.datalist[0].organizename;
				for(var i=1;i<data.datalist.length;i++){
					organizenames+=","+data.datalist[i].organizename;
				}
				var temp ="<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:12px;\">"+
            	"<tr class=\"head_td\">"+
	            	"<td width=\"120\">基本信息</td>"+
	                "<td colspan=\"2\" class=\"t_r\">";
	                if(data.userinfo.managerole != 3 && data.userinfo.managerole != 2){
	                	temp +="<a href=\"javascript:void(0)\" class=\"link\" onclick=\"checkPower('"+data.userinfo.userid+"')\">权限设置</a><a href=\"javascript:void(0)\" class=\"blue\" onclick=\"checkUpdatUser('"+data.userinfo.userid+"')\">修改</a></td>";
	                }
	            temp +="</tr>"+
	            "<tr>"+
	            	"<td>姓名</td>"+
	                "<td width=\"30\" class=\"img_td\"><div class=\"img\"><img src="+data.userinfo.headimage+" width=\"30\" height=\"30\" /></div></td>"+
	                "<td class=\"gray_word\">"+data.userinfo.realname+"</td>"+
            	"</tr>"+
           		"<tr>"+
	            	"<td>性别</td>";
	            	  if(data.userinfo.sex == 1){
		                	temp+= "<td colspan=\"2\" class=\"gray_word\"  id=\"sex\">男</td>";
		               }else if(data.userinfo.sex == 0){
		               		temp+= "<td colspan=\"2\" class=\"gray_word\" id=\"sex\">女</td>";
		               }else{
		            	   temp+="<td></td>"
		               }  
            	temp+="</tr>";
            	
            	
            	
            	temp+="<tr>"+
	            	"<td>出生年月</td>"+
	                "<td colspan=\"2\" class=\"gray_word\">"+(data.userinfo.birthday==undefined?"":data.userinfo.birthday)+"</td>"+
	            "</tr>"+
	            "<tr>"+
	            	"<td>手机号</td>";
            	if(data.userinfo.isshowphone == 1){
            		temp += "<td colspan=\"2\" class=\"gray_word\">"+data.userinfo.phone+"</td>";
            	}else{
            		 var phone = data.userinfo.phone;
            	     var mphone = phone.substr(3,4);
            	     var phones = phone.replace(mphone,"****");
            	     temp +="<td colspan=\"2\" class=\"gray_word\">"+phones+"</td>";
            	}
	           temp += "</tr>"+
	            "<tr>"+
	            	"<td>电子邮件地址</td>"+
	                "<td colspan=\"2\" class=\"gray_word\">"+(data.userinfo.email==undefined?"":data.userinfo.email)+"</td>"+
	            "</tr>"+
	            "<tr>"+
	            	"<td>部门</td>"+
	                "<td colspan=\"2\" class=\"gray_word\">"+organizenames+"</td>"+
	            "</tr>"+
	            "<tr>"+
	            	"<td>职位</td>"+
	                "<td colspan=\"2\" class=\"gray_word\">"+data.userinfo.position+"</td>"+
		        "</tr>"+
		        "</table>";
				$('#modulediv').html(temp);
			}else{
				$('#modulediv').html("暂无数据...");
			}
		}
	})
}



//点击修改人员信息
function checkUpdatUser(userid){
	$("#retruns").hide();
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath() %>/userbackstage/getUserByUserid",
		data:"userid="+userid+"&companyid=${userinfo.companyid}",
		success:function(data){
			if(data.userinfo != null || data.userinfo.length != ""){
				var posit = data.userinfo.position;
				if(posit == "undefined" || posit == undefined){
					data.userinfo.position = "";
				}
				var organizenames="";
				var organizelist = {"organizelist":[]};
				for(var i=0;i<data.datalist.length;i++){
					organizenames+=data.datalist[i].organizename+",";
					var orglist = {};
					orglist['organizeid'] = data.datalist[i].organizeid;
					organizelist.organizelist.push(orglist);
				}
				var temp ="<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:12px;\">"+
            	"<tr class=\"head_td\">"+
	            	"<td width=\"120\">基本信息修改</td>"+
	                "<td colspan=\"2\" class=\"t_r\"></td>"+
	            "</tr>"+
	            "<tr>"+
	            	"<td>姓名</td>"+
	                "<td width=\"30\" class=\"img_td\"><div class=\"img\"><img src=\""+data.userinfo.headimage+"\" width=\"30\" height=\"30\" /></div></td>"+
	                "<td class=\"gray_word\">"+data.userinfo.realname+"</td>"+
            	"</tr>"+
           		"<tr>"+
	            	"<td>性别</td>";
	   
	                if(data.userinfo.sex == 1){
	                	temp+= "<td colspan=\"2\" class=\"gray_word\"  id=\"sex\">男</td>";
	                }else if(data.userinfo.sex == 0){
	               		temp+= "<td colspan=\"2\" class=\"gray_word\" id=\"sex\">女</td>";
	                }else{
	                	temp+= "<td colspan=\"2\" class=\"gray_word\" ></td>";
	                }   
	          	temp+="</tr>"+
            	"<tr>"+
	            	"<td>出生年月</td>"+
	                "<td colspan=\"2\" class=\"gray_word\">"+(data.userinfo.birthday==undefined?"":data.userinfo.birthday)+"</td>"+
	            "</tr>"+
	            "<tr>"+
	            	"<td>手机号</td>";
	            	if(data.userinfo.isshowphone == 1){
	            		temp += "<td colspan=\"2\" class=\"gray_word\">"+data.userinfo.phone+"</td>";
	            	}else{
	            		 var phone = data.userinfo.phone;
	            	     var mphone = phone.substr(3,4);
	            	     var phones = phone.replace(mphone,"****");
	            	     temp +="<td colspan=\"2\" class=\"gray_word\">"+phones+"</td>";
	            	}
	                
	            temp +="</tr>"+
	            "<tr>"+
	            	"<td>电子邮件地址</td>"+
	                "<td colspan=\"2\" class=\"gray_word\">"+(data.userinfo.email==undefined?"":data.userinfo.email)+"</td>"+
	            "</tr>"+
	            "<tr>"+
	            	"<td>区域/部门/店面</td><input type=\"hidden\" id=\"updataorganizeid\" value='"+JSON.stringify(organizelist)+"'/>"+
	                "<td colspan=\"2\" class=\"gray_word\"><i class=\"m_r30\" id=\"updataorganizename\">"+organizenames.substring(0,(organizenames.length - 1))+"</i><a href=\"javascript:void(0)\" class=\"blue\" onclick=\"checkWindow_update()\">编辑</a></td>"+
	            "</tr>"+
	            "<tr>"+
	            	"<td>职位</td>"+
	                "<td colspan=\"2\" class=\"gray_word\"><input type=\"text\" class=\"text\" placeholder=\"请输入职位\" id=\"position\" value="+data.userinfo.position+"></td>"+
		        "</tr>"+
		        " <tr class=\"foot_td\">"+
	               " <td>&nbsp;</td>"+
	                "<td colspan=\"2\"><a href=\"javascript:void(0)\" class=\"a_btn bg_yellow\" onclick=\"UpdataUserInfo('"+data.userinfo.userid+"','"+data.userinfo.realname+"')\">保存</a><a href=\"javascript:void(0)\" class=\"a_btn bg_gay2\" onclick=\"checkUserById('"+data.userinfo.userid+"')\">取消</a></td>"+
          		 "</tr>"+
		        "</table>";
				$('#modulediv').html(temp);
			}else{
				$('#modulediv').html("暂无数据...");
			}
		}
	})
}

//点击保存修改人员信息
function UpdataUserInfo(userid,realname){
	var organizeid="",
	updataorganizeid="",
		organizename="",
		position="";
	organizeid = $("#updataorganizeid").val();//原部门id
	position = $("#position").val();
	updataorganizeid=$("#organizeid").val();//修改后的部门id
	
	position=$("#position").val();
	var htm = '<form action="/userbackstage/getUpdataUserByUserid" method="post" id="updateorganizeuserinfo_form">'+
				'<input type="hidden" name="userid" value="'+userid+'"/>'+
				'<input type="hidden" name="companyid" value="${userinfo.companyid}"/>'+
				'<input type="hidden" name="updateid" value="'+userid+'"/>'+
				'<input type="hidden" name="position" value="'+position+'"/>'+
				'<input type="hidden" name="organizelist" value=\''+updataorganizeid+'\'/>'+
			'</form>';
	//location.href="/userbackstage/getUpdataUserByUserid?organizeid="+organizeid+"&updataorganizeid="+updataorganizeid+"&position="+position+"&userid="+userid+"&realname="+realname;
	$('#updateorganizeuserinfo_form').remove();
	$("body").append(htm);
	
	$('#updateorganizeuserinfo_form').submit();
}



//权限设置
function checkPower(userid){
	location.href="/userbackstage/getPowerByUserId?userid="+userid;
}











//全选邀请
function checkInvitedOut(){
	var list = [];
	var i = 0;
	var  param = new Object();
	$("a[name=peron]").each(function(index,dom){
		if($(this).attr("class") == "checked"){
			list[i] = {userid:$(this).attr("userid"),phone:$(this).attr("phone"),username:$(this).attr("realname"),organizename:$(this).attr("organizename")};
			i++;
		}
	})	
	param.list = JSON.stringify(list);
	param.updatename = "${userinfo.realname}";
	
	if(list.length>0){
		swal({
			title : "",
			text : "是否邀请！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			location.href="/userbackstage/updateUserInvitation?list="+param.list;
		});		
	}else{
		swal({
			title : "",
			text : "请选择一个人员！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		
		});
	}
}

//全选删除
function checkDeleteOut(){
	var list = [];
	var i = 0;
	var  param = new Object();
	$("a[name=peron]").each(function(index,dom){
		if($(this).attr("class") == "checked"){
			list[i] = {userid:$(this).attr("userid"), manageid:$(this).attr("manageid"),organizeid:$(this).attr("organizeid")};
			i++;
		}
	})	
	param.list = JSON.stringify(list);
	if(list.length>0){
		swal({
			title : "",
			text : "是否删除！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			location.href="/userbackstage/getDeleteByUser?list="+param.list;
		});		
	}else{
		swal({
			title : "",
			text : "请选择一个人员！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		
		});
	}
	
}


//全选启用
function checkStartOut(){
	var list = [];
	var  param = new Object();
	var status = 1;
	var organizeid="";
	var i = 0;
	$("a[name=peron]").each(function(index,dom){
		if($(this).attr("class") == "checked"){
			list[i] = {userid:$(this).attr("userid")};
			organizeid= $(this).attr("organizeid");
			i++;
			
		}	
	})	
	param.list = JSON.stringify(list);
	if(list.length>0){
		swal({
			title : "",
			text : "是否启用！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			location.href="/userbackstage/getUpdataByUser?organizeid="+organizeid+"&list="+param.list+"&status="+status;
		});		
	}else{
		swal({
			title : "",
			text : "请选择一个人员！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		
		});
	}
	
	
}

//全选禁用
function checkStartedOut(){
	var list = [];
	var  param = new Object();
	var status = 0;
	var i = 0;
	$("a[name=peron]").each(function(index,dom){
		if($(this).attr("class") == "checked"){
			list[i] = {userid:$(this).attr("userid")}
			i++;
			organizeid= $(this).attr("organizeid");
		}	
	})	
	param.list = JSON.stringify(list);
	if(list.length>0){
		swal({
			title : "",
			text : "是否禁用！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			location.href="/userbackstage/getUpdataByUser?organizeid="+organizeid+"&list="+param.list+"&status="+status;
		});		
	}else{
		swal({
			title : "",
			text : "请选择一个人员！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		
		});
	}

}





//删除
function checkDelete(userid,manageid,organizeid){
	location.href="/userbackstage/getDeleteByUser?userid="+userid+"&manageid="+manageid+"&organizeid="+organizeid;
}

//禁用/启用
function checkStart(status,userid,organizeid){
	location.href="/userbackstage/getUpdataByUser?userid="+userid+"&status="+status+"&organizeid="+organizeid;
}

//邀请
function checkInvitation(userid,username,phone,organizename){
	location.href="/userbackstage/updateUserInvitation?userid="+userid+"&username="+username+"&phone="+phone+"&organizename="+organizename+"&updatename=${userinfo.realname}";
}

function check(obj){
	if($(obj).attr("class")=="check"){
		$(obj).attr("class","checked");
	}else{
		$(obj).attr("class","check");
	}
}

function checkOut(){
	if($("#ckOut").attr("class") == "check"){
		$("#peron a").attr("class","checked");
		$("#ckOut").attr("class","checked");
	}else{
		$("#peron a").attr("class","check");
		$("#ckOut").attr("class","check");
	}
	
}

function onloadData(){
	if("${userinfo.managerole}" != "3"){
		return false;
	}
	queryUserAllList();
	if('${powerMap.power7001010}' == ''){
		$("#insert").hide();
	}
}
$(document).ready(function(){
	$('#nav_userlist').attr('class','active');$('#nav_userlist').parent().parent().show();
	
	$('#file_upload').fileupload({    
		url:'<%=request.getContextPath() %>/upload/imageUpload?type=1', 
		formData:{
			fileName:'myfiles'   
		},
		type:'post',
		maxNumberOfFiles:1,    
		autoUpload:true,
        dataType: 'json',
	        acceptFileTypes:  /(\.|\/)(jpeg|tiff|psd|png|swf|svg|pcx|dxf|wmf|emf|lic|eps|tga|bmp|jpg|jpeg2000|gif)$/i, 
        maxFileSize: 419430400,  
        done: function (e, data) { 
        	data = data.result;
        	
        	$("#headimage").attr("src",data.url);
        		
        }
    });
})

</script>
</head>

<body onload="onloadData()">
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>	
	<div class="div_mask" style="display:none;"></div>
	<div class="page_nav"><p><a href="<%=request.getContextPath() %>/userbackstage/index">首页</a><i>/</i><a href="javascript:void(0)">企业管理</a><i>/</i><span>组织架构</span></p></div>  
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1">组织架构</span>
        <a href="javascript:void(0)" style="width:80px;" id="insert" onclick="$('#new_userdiv').hide();$('#userdiv').show();$('#insert').hide();$('#retruns').show();">新建人员</a>
        <a href="javascript:void(0)" style="width:80px;" onclick="initadd()">新建组织</a>
        <a href="<%=request.getContextPath()%>/userbackstage/getNextOrgainize" id="retruns" style="display:none;" >返回</a></div>
        <div class="personnel">
			<div class="l_tree" id="organizetree"  style="overflow:hidden;overflow-y:visible;">
        	
            </div>
            
             <div class="r_list"  style="display:none;" id="userdiv">
            	<div class="tab_list">
                	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size: 12px;">
                    	<tr class="head_td">
                        	<td width="120">基本信息</td>
                            <td colspan="2" class="t_r">&nbsp;</td>
                        </tr>
                        <tr class="none_border">
                        	<td >头像</td>
                            <td width="48" class="img_td">
                            <div class="img3"><img src="" width="48" height="48" id="headimage"/></div>
                            <input type="file" style="display: none" name="myfiles" id="file_upload" multiple/>
<!--                              <input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/> -->
                            </td>
                            <td class="gray_word"><a href="javascript:void(0)" class="yellow" onclick="uploadbanner()">上传</a></td>
                        </tr>
                        <tr class="none_border">
                        	<td>姓名<i style="color: red;">*<i></td>
                            <td colspan="2" class="gray_word"><input type="text" class="text" placeholder="请输入姓名" id="realname"/></td>
                        </tr>
                        <tr class="none_border">
                        	<td>性别<i style="color: red;">*<i></td>
                            <td colspan="2" class="gray_word">
	                            <a href="javascript:void(0)" class="radio_ed"  id="nan" onclick="$('#nan').attr('class','radio_ed');$('#nv').attr('class','radio');">选择</a><i class="i_dp">男</i>
	                            <a href="javascript:void(0)" class="radio" id="nv"  onclick="$('#nan').attr('class','radio');$('#nv').attr('class','radio_ed');">选择</a><i class="i_dp" >女</i>
                            </td>
                        </tr>
                        <tr class="none_border">
                        	<td>出生年月</td>  	
                            <td colspan="2" class="gray_word">
                            	<input  class="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" placeholder="请输入出生年月" name="endtime"    id="birthday"  class="text_time" type="text"  readonly="readonly"  style="width: 135px;"/>
                    		 </td>
                        </tr>
                        <tr class="none_border">
                        	<td>手机号<i style="color: red;">*<i></td>
                            <td colspan="2" class="gray_word"><input type="text" class="text" placeholder="请输入手机号" id="phone" maxlength="11"/></td>
                        </tr>
                        <tr class="none_border">
                        	<td>电子邮件地址</td>
                            <td colspan="2" class="gray_word"><input type="text" class="text" placeholder="请输入电子邮件地址" id="email"/></td>
                        </tr>
                        <tr class="none_border">
                        	<td>区域/部门/店面<i style="color: red;">*<i></td>
                            <td colspan="2" class="gray_word"><i class="m_r30" id="name"></i><a href="javascript:void(0)" onclick="checkWindow()">选择</a></td>
                        </tr>
                        <tr class="none_border">
                        	<td>职位</td>
                            <td colspan="2" class="gray_word"><input type="text" class="text" placeholder="请输入职位" id="positions"/></td>
                        </tr>
                        <tr class="foot_td">
	               <td>&nbsp;</td>
	                <td colspan="2"><a href="javascript:void(0)" class="a_btn bg_yellow" onclick="checkInsertUserInfo()">保存</a><a href="<%=request.getContextPath()%>/userbackstage/getNextOrgainize" class="a_btn bg_gay2" >取消</a></td>
          		 </tr>
                    </table>
                </div>               
            </div>
 <script type="text/javascript">
 function uploadbanner(){
	 $('#file_upload').click();
	}

 
 function checkInsertUserInfo(){
	 var myreg = /^1\d{10}$/; 
	 var sex = 1
	 	 headimage="",
	 	 realname="",
	 	 birthday="",
	 	 phone="",
	 	 email="",	
	 	 updataorganizename="",
	 	 organizeid="",
	 	 position="",
		 mess="";
	 if($('#nan').attr('class')=='radio_ed'){
		 sex = 1;
	 }else{
		 sex = 0 ;
	 }
	 headimage=$("#headimage").attr("src");
	 organizeid=$('#organizeid').val()
	 realname=$("#realname").val();
	 birthday=$("#birthday").val();
	 phone=$("#phone").val();
	 email=$("#email").val();
	 updataorganizename=$("#name").text();
	 position=$("#positions").val();
	 
	 var parma = new Object();
	 parma.organizelist=organizeid;
	 parma.headimage=headimage;
	 parma.realname=realname;
	 parma.birthday=birthday;
	 parma.username=phone;
	 parma.phone=phone;
	 parma.email=email;
	 parma.position=position;
	 parma.sex=sex;
	 parma.companyid='${userinfo.companyid}';
	 if(headimage == "" || headimage==null){
		 mess="";
	 }else if(realname =="" || realname == null){
		 mess="请输入姓名";
	 }else if(phone==null || phone==""){
		 mess="请输入手机号";
	 }else if(phone.length != 11 ||  !myreg.test(phone)){
		 mess="手机号格式错误,请重新输入";
	 }else if(email==null || email==""){
		 mess="请输入邮箱";
	 }else if(email.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) == -1){
		 mess="邮箱格式错误，请重新输入";
	 }else if(updataorganizename==null || updataorganizename=="" || organizeid == ""){
		 mess="请选择一个组织";
	 }
	 
	 if(mess != "" ){
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
				type:"post",
				dataType:"json",
				url:"<%=request.getContextPath() %>/app/insertOrganizeUserInfo",
				data:parma,
				success:function(data){	 
					 if(data.status == 1 ){
						 swal({
							title : "",
							text :  data.message,
							type : "error",
							showCancelButton : false,
							confirmButtonColor : "#ff7922",
							confirmButtonText : "确认",
							cancelButtonText : "取消",
							closeOnConfirm : true
						}, function(){	
						});	 
					}else if(data.status == 2 ){
						 swal({
								title : "",
								text : data.message,
								type : "warning",
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
								text : data.message,
								type : "success",
								showCancelButton : false,
								confirmButtonColor : "#ff7922",
								confirmButtonText : "确认",
								cancelButtonText : "取消",
								closeOnConfirm : true
							}, function(){
								location.href="/userbackstage/getNextOrgainize";		
							});
					}
					
				}
			})
	 }
 }
 
 function getSearchName(){
	 pageParam.searchname=$("#uname").val();
	 if(pageParam.searchname=="" || pageParam.searchname == undefined){
		 delete pageParam.searchname;
	 }
	 
	 var str = $("#strust_iss").val();
	 var  uname= $("#uname").val();
	 if(str==1){
		 pageParam.status=0;
		 delete pageParam.isinvite;
	 }else if(str==2){
		 pageParam.status=1;
		 delete pageParam.isinvite;
	 }else if(str==3){
		 pageParam.isinvite=1;
		 delete pageParam.status;
	 }else if(str==4){
		 pageParam.isinvite=0;
		 delete pageParam.status;
	 }else{
		 delete pageParam.status;
		 delete pageParam.isinvite;
	 }
	 queryUserAllList();
}
 
 
 function exportexcel(){
	 swal({
			title : "",
			text : "是否导出",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$.ajax({
		        type: "POST",
		        url: "<%=request.getContextPath() %>/userbackstage/exportUserList",
		        data:pageParam,
		        success: function(data){
		        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
		        }
		    });
		});
		
	}
 </script>           
            
            
            <div class="r_list" id="new_userdiv">
            	<div class="sel_box" id="seach">
                	<input type="text" class="text" placeholder="请输入关键字" id="uname" />
                    <select class="sel" id="strust_iss" ><option value="0">全部</option><option value="1">禁用</option><option value="2">启用</option><option value="3">已邀请</option><option value="4">未邀请</option></select>
                    <input type="button" value="导出" class="find_btn" onclick="exportexcel()" />
                    <input type="button" value="搜索" class="find_btn"   onclick="getSearchName()"/>
                    <div class="clear"></div>
                </div>
            	<div class="tab_list" >
            			<table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;">
            				<tr id="organizedetailinfo_tr">
		                        
                       		 </tr>
            			</table>
       					<table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;" >
	       					<tbody id="modulediv"></tbody>
       					</table>
                </div> 
                <div id="Pagination" style="width:450px;"></div><!--动态的获取pagination的宽度赋值给Pagination-->
                </div>
            <div class="clear"></div>
            <div class="div_mask" style="display:none;"></div>
        </div>
    </div>
</div>
</div>


<input type="hidden" id="organizeid" />
<input type="hidden" id="organizename" />
<div class="tc_structure" style="display:none;max-height:60%; overflow-x: hidden; overflow-y: visible;" id="organize_duoxuan">
		<div class="tc_title"><span>选择组织架构店</span><a href="javascript:void(0);" onclick="$('#organize_duoxuan').hide();$('.div_mask').hide();" >×</a></div>
	    <div id="checkboxdiv"></div>
	    <div class="tc_btnbox"><a href="javascript:void(0);" onclick="$('#organize_duoxuan').hide();$('.div_mask').hide();"  class="bg_gay2">取消</a>
	    <a href="javascript:void(0)" onclick="chooseorganizeuser()"  class="bg_yellow" id="addchooseorganizeuser">确定</a>
	    <a href="javascript:void(0)" onclick="chooseorganizeuser1()" class="bg_yellow" id="updatechooseorganizeuser" style="display: none">确定</a>
	    </div>
</div>

<!-- 二维码 -->
<div class="tc_ewmxx" style="display:none;">
	<div class="tc_title none_border"><a href="javascript:void(0);" onclick="$('.tc_ewmxx').hide();$('.div_mask').hide();">×</a></div>
    <div class="ewm_img"><img src="../userbackstage/images/public/ewm.jpg" width="262" height="262" id="qrcodeimage" /></div>
    <div class="ewm_txt"><span>客户满意度二维码</span><a href="javascript:void(0)" onclick="downloadewm()" class="yellow">下载</a></div>
</div>

<!-- 编辑组织架构信息 -->
<div class="tc_addstru" style="display:none;height:auto;">
	<div class="tc_title"><span>新建组织架构</span><a href="javascript:void(0)" onclick="$('.tc_addstru').hide();$('.div_mask').hide();">×</a></div>
    <div class="box">
    	<input type="hidden" id="organizeid1"/>
    	<span><input type="text" class="text" placeholder="请输入组织或职务" id="organizename1"/></span>
    	<input type="hidden" id="parentid"/>
        <span><input type="text" class="text" placeholder="请选择上级部门/区域/店面" id="parentname" readonly="readonly" onclick="$('.tc_addstru').hide();$('#organize_danxuan').show();checkRedioDanXuan();" /></span>
        <input type="hidden" id="type"/>
        <span><a href="javascript:void(0)" class="radio" id="quyu" onclick="checktype(this,1);">选择</a><i>区域</i><a href="javascript:void(0)"class="radio_ed" id="bumen" onclick="checktype(this,2);">选择</a><i>部门</i><a href="javascript:void(0)" class="radio" id="dianmian" onclick="checktype(this,3);">选择</a><i>店面</i></span>
    	<span><input type="text" class="text" placeholder="请安排排序号" id="priority"/></span>
    	<span><input type="text" class="text" placeholder="请输入详细地址" id="address" style="display: none; "/></span>
    </div>
    <div class="tc_btnbox"><a href="javascript:void(0)" onclick="$('.div_mask').hide();$('.tc_addstru').hide();" class="bg_gay2">取消</a>
    <a href="javascript:void(0)" onclick="submitdata()" class="bg_yellow">保存</a></div>
</div>

<!-- 选择组织信息 -->
<div class="tc_structure" style="display:none;max-height:60%;overflow:hidden;overflow-y:visible;" id="organize_danxuan">
	<div class="tc_title"><span>选择组织架构店</span><a href="javascript:void(0);" onclick="$('#organize_danxuan').hide();$('.div_mask').hide();" >×</a></div>
    <div id="organizetree1"  style="overflow:hidden;overflow-y:visible;"></div>
    <div class="tc_btnbox"><a href="javascript:void(0);" onclick="$('#organize_danxuan').hide();$('.tc_addstru').show();"  class="bg_gay2">取消</a>
    <a href="javascript:void(0)" onclick="chooseorganizeuser_organize()"  class="bg_yellow" id="addchooseorganizeuser">确定</a>
    </div>
</div>

<script type="text/javascript">

//组织架构模块js——start
function downloadewm(){
	var fileName=$('#qrcodeimage').attr('src');
	window.location.href="<%=request.getContextPath()%>/userbackstage/download?fileName="+fileName;
}
//----修改组织信息
function initupdate(organizeid,organizename,parentname,parentid,type,address,priority){
	$('#organizeid1').val(organizeid);
	$('#organizename1').val(organizename);
	$('#parentname').val(parentname);
	$('#parentid').val(parentid);
	$('#priority').val(priority);
	if(type==1){
		$('#quyu').attr("class","radio_ed");
		$('#bumen').attr("class","radio");
		$('#dianmian').attr("class","radio");
		$('#address').hide();
		$('#address').val("");
	}else if(type==2){
		$('#quyu').attr("class","radio");
		$('#bumen').attr("class","radio_ed");
		$('#dianmian').attr("class","radio");
		$('#address').hide();
		$('#address').val("");
	}else if(type==3){
		$('#quyu').attr("class","radio");
		$('#bumen').attr("class","radio");
		$('#dianmian').attr("class","radio_ed");
		$('#address').show();
		$('#address').val(address);
	}
	$('#type').val(type);
	$('.tc_addstru').show();
	$('.div_mask').show();
}


function submitdata(){
	var organizeid=$('#organizeid1').val();
	
	var organizename=$('#organizename1').val();
	var parentid=$('#parentid').val();
	var type=$('#type').val();
	var address=$('#address').val();
	if(organizename==""){
		swal({
			title : "",
			text : "请先填写组织名称",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		});
		return;
	}
	if(parentid==""){
		swal({
			title : "",
			text : "请先选择上级组织",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		});
		return;
	}
	if(type==3 && address==""){
// 		swal({
// 			title : "",
// 			text : "请输入店面详细地址",
// 			type : "error",
// 			showCancelButton : false,
// 			confirmButtonColor : "#ff7922",
// 			confirmButtonText : "确认",
// 			cancelButtonText : "取消",
// 			closeOnConfirm : true
// 		}, function(){
// 		});
// 		return;
	}
	
	if(organizeid != ""){
		var param = new Object();
		//需要修改的数据
/* 		param.lastdatacode = datacode;
		param.lastcompanyid = companyid; */
		param.lastorganizeid =organizeid;
		param.organizename = organizename;
		
		//需要修改到当前组织下
		param.updataorganizeid =parentid;
/* 		param.updatadatacode = $("#datacode").val();
		param.parentid = parentid; */

		param.updatatype = type;
		param.address = address;
		param.priority = $('#priority').val();

		$.ajax({
			type:'post',
			dataType:'json',
			url:'<%=request.getContextPath()%>/userbackstage/updateOrganize',
			data:param,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text : data.message,
						type : "error",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						//location.reload();
					});
				}else{
					swal({
						title : "",
						text : data.message,
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						location.reload();
					});
				}
				
			}
		})
	}else{
		//新增
		var param = new Object();
		param.updataorganizeid =parentid;//parentid
		param.address=address;
		param.organizename = organizename;//公司名称
		param.type = type;//type
		param.priority = $('#priority').val();
		
		$.ajax({
			type:'post',
			dataType:'json',
			url:'<%=request.getContextPath()%>/userbackstage/insertOrganize',
			data:param,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text : data.message,
						type : "error",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						//location.reload();
					});
				}else{
					swal({
						title : "",
						text : data.message,
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						location.reload();
					});
				}
				
			}
		})
	}
}

function checkRedioDanXuan(){
	$('#organizetree1').find('a[class="checked"]').attr("class","check");
	var orgid = $('#parentid').val();
	$('#organizetree1').find('#organizeRadio_'+orgid).attr("class","checked");
}

function chooseorganizeuser_organize(){
	if($('#organizeid').val()==""){
		swal({
			title : "",
			text : "请选择组织架构",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
		return;
	}
	$('#parentid').val($('#organizeid').val());
	$('#parentname').val($('#organizename').val());
	$('.tc_addstru').show();
	$('#organize_danxuan').hide();
	
	var param = new Object();
	param.organizeid = $('#organizeid').val();
	$.ajax({
		url:"/userbackstage/getParentOrganizeMaxPriority",
		type:"post",
		data:param,
		async:false,
		success:function(data){
			if(data != ""){
				$('#priority').val(parseInt(data)+1);
			}
		}
	});
}
function initadd(){
	$('#organizetree1').find('a[class=checked]').attr("class","check");
	$('#organizeid1').val("");
	$('#organizename1').val("");
	$('#parentname').val("");
	$('#type').val(1);
	$('#address').hide();
	$('#quyu').attr("class","radio");
	$('#bumen').attr("class","radio_ed");
	$('#dianmian').attr("class","radio");
	$('#address').val("");
	$('#priority').val("");
	$('.tc_addstru').show();
	$('.div_mask').show();
}
function checktype(obj,type){
	$(obj).parent().find("a").attr("class","radio");
	$(obj).attr("class","radio_ed");
	$('#type').val(type);
	if(type==3){
		$('#address').show();
	}else{
		$('#address').hide();
	}
}

//组织架构模块js——end

//修改人员资料,编辑部门
function checkWindow(){
		$('#organize_duoxuan').show();
		$('.div_mask').show();
		$('#checkboxdiv').find('a[class="checked"]').attr("class","check");
		var organizelist = $('#organizeid').val();
		organizelist = JSON.parse(organizelist).organizelist;
		$.each(organizelist,function(i,item){
			$('#checkboxdiv').find('#checkboxdiv_'+item.organizeid).attr("class","checked");
		});
}
function checkWindow_update(){
	$('#organize_duoxuan').show();
	$('.div_mask').show();
	$('#checkboxdiv').find('a[class="checked"]').attr("class","check");
	var organizelist = $('#updataorganizeid').val();
	organizelist = JSON.parse(organizelist).organizelist;
	$.each(organizelist,function(i,item){
		$('#checkboxdiv').find('#checkboxdiv_'+item.organizeid).attr("class","checked");
	});
}

function chooseorganizeuser(){
	if($('#checkboxdiv').find("a[class=checked]").length==0){
		swal({
			title : "",
			text : "请选择组织架构",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		})
		return;
	}else{
		var alldata={"organizelist":[]};
		var checkbox = $('#checkboxdiv').find("a[class=checked]");
		var orgid = "";
		var orgname = "";
		$.each(checkbox,function(i,map){
			orgname += $(map).find("input[name=organizename]").val()+",";
			var organizemap={};
			organizemap["organizeid"]=$(map).find("input[name=organizeid]").val();
			alldata.organizelist.push(organizemap);
		});
		$('#updataorganizename').text(orgname.substring(0,(orgname.length - 1)));
		$('#name').text(orgname.substring(0,(orgname.length - 1)));
		$('#organizeid').val(JSON.stringify(alldata));
	}
	$('#organize_duoxuan').hide();
	$('.div_mask').hide();
}
</script>
</body>
</html>
