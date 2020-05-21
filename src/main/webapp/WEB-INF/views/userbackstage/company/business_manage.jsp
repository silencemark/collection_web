<%@page import="com.collection.util.UserUtil"%>
<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>首页-使用方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organize.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organizeRangeManage.js"></script>
<%-- <script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organizeCheckbox.js"></script> --%>

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
function initadd(){
	$('.tc_selrange').show();
	$('.div_mask').show();
	$('#searchrealname').val("");
	var temp="<tr class=\"head_td\">"+
		"<td width=\"30\">&nbsp;</td>"+
	    "<td width=\"70\">姓名</td>"+
	    "<td width=\"70\">性别</td>"+
	    "<td width=\"130\">电话</td>"+
	    "<td>操作</td>"+
	"</tr>";
	$('#usertable').html(temp);
}
function callbackfunc(organizeid){
	$('#Pagination').hide();
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath() %>/userbackstage/getManageListByAjax",
		data:"organizeid="+organizeid+"&realname="+$('input[name=realname]').val(),
		success:function(data){
			var temp="<tr class=\"head_td none_tborder\">"+
	            "<td width=\"30\">&nbsp;</td>"+
	            "<td>姓名</td>"+
	            "<td>电话</td>"+
	            "<td>管理范围</td>"+
	            "<td>操作</td>"+
	        "</tr>";
			if(data.userlist.length>0){
				var mapmanagerole = "${map.managerole}";
				for(var i=0;i<data.userlist.length;i++){
					temp+="<tr>"+
                    	"<td class=\"img_td\"><div class=\"img\"><img src=\""+data.userlist[i].headimage+"\" width=\"30\" height=\"30\" /></div></td>"+
                        "<td>"+data.userlist[i].realname;
                    	if(data.userlist[i].managerole==3){
                    		temp+="<i class=\"red\">(超级管理员)</i>";
                        }
                    	temp+="</td><td>"+data.userlist[i].phone+"</td>";
                    	if(data.userlist[i].managerole==2){
                    		temp+="<td>"+data.userlist[i].organizenames+"</td>";
                    	}else{
                    		temp+="<td>全部店面</td>";
                    	}
                    	if(mapmanagerole == "3"){
                    		temp+="<td><a href=\"javascript:void(0)\" class=\"blue\"  onclick=\"deleteManage('"+data.userlist[i].userid+"','"+data.userlist[i].managerole+"')\">移除管理权限</a></td></tr>";
                    	}else if(mapmanagerole == "2"){
                    		if(data.userlist[i].managerole!=3){
                    			temp+="<td><a href=\"javascript:void(0)\" class=\"blue\"  onclick=\"deleteManage('"+data.userlist[i].userid+"','"+data.userlist[i].managerole+"')\">移除管理权限</a></td></tr>";
                    		}
                    	}
                    	
				}
			}
			$('#userdiv').html(temp);
		}
	})
}

var userParam = new Object();
userParam.rowPage = 5;
userParam.managerole = 1;

function pageHelper(num){
	userParam.currentPage = num;
	if(userParam.organizeid != "" && userParam.searchrealname == ""){
		callbackfunc1("");
	}else if(userParam.organizeid == "" && userParam.searchrealname != ""){
		searchuser("search");
	}
}


function callbackfunc1(organizeid){
	if(organizeid != ""){
		userParam.organizeid = organizeid;
		userParam.currentPage = 1;
		userParam.searchrealname = "";
	}
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath() %>/userbackstage/getUserByOrganize",
		data:userParam,
		success:function(data){
			$('#userPage').html(data.pager);
			$('#usertable').html("<tr class=\"head_td\">"+
		        	"<td width=\"30\">&nbsp;</td>"+
		            "<td width=\"70\">姓名</td>"+
		            "<td width=\"70\">性别</td>"+
		            "<td width=\"130\">电话</td>"+
		            "<td>操作</td>"+
		        "</tr>");
			if(data.userlist.length>0){
				var temp="";
				for(var i=0;i<data.userlist.length;i++){
					temp+="<tr>"+
                    	"<td class=\"img_td\"><div class=\"img\"><img src=\"<%=request.getContextPath() %>"+data.userlist[i].headimage+"\" width=\"30\" height=\"30\" /></div></td>"+
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
                        temp+="<td><a href=\"javascript:void(0)\" onclick=\"adduser(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"')\" class=\"link\">添加</a></td>"+
                    "</tr>";
				}
				$('#usertable').append(temp);
			}
		}
	})
}
function adduser(obj,userid,realname){
	$('#userid').val(userid);
	$('.tc_powerxx').show();
	$('.tc_selrange').hide();
}
function searchuser(type){
	if(type != "search"){
		var searchrealname=$('#searchrealname').val();
		if(searchrealname == ""){
			swal("","请输入用户名称···","warning");
			return false;
		}
		userParam.organizeid = "";
		userParam.currentPage = 1;
		userParam.searchrealname = searchrealname;
	}
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath() %>/userbackstage/getSearchUserByName",
		data:"searchrealname="+searchrealname,
		success:function(data){
			$('#userPage').html(data.pager);
			$('#usertable').html("<tr class=\"head_td\">"+
		        	"<td width=\"30\">&nbsp;</td>"+
		            "<td width=\"70\">姓名</td>"+
		            "<td width=\"70\">性别</td>"+
		            "<td width=\"130\">电话</td>"+
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
	})
}
$(document).ready(function(){
	$('#nav_manage').attr('class','active');$('#nav_manage').parent().parent().show();
});
function chooserole(managerole){
	var userid=$("#userid").val();
	$('#managerole').val(managerole);
	if(managerole==2){
		$.ajax({
			url:"<%=request.getContextPath()%>/userbackstage/queryUserUpIsExistsShop?userid="+userid,
			type:"post",
			success:function(data){
				var datalist = data.datalist;
				if(datalist != null && datalist.length > 0){
					var temp = "";
					$.each(datalist,function(i,map){
						temp+="<a href=\"javascript:void(0)\" class=\"checked\" onclick=\"changeorganize1(this,'"+map.organizeid+"','"+map.organizename+"')\">" +
								"<input type=\"hidden\" name=\"organizeid\" value=\""+map.organizeid+"\"/><input type=\"hidden\" name=\"organizename\" value=\""+map.organizename+"\"/>选择</a>";
					});
					$("#checkboxdiv").html(temp);
					
					submitdata();
				}else{
					swal({   
						title: "",   
						text: "<font style=\"color:#ff7922; font-size:28px; margin-left:20px;\">该人员不在店面下面！</font>",
						type:"warning",
						timer: 2000,
						html:true,
						showConfirmButton: false 
					});
				}
			}
		});
		
// 		$('#selectfunction').show();
// 		$('.tc_powerxx').hide();
	}else{
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath()%>/userbackstage/updateUserInfo",
			data:"userid="+userid+"&managerole="+managerole,
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
						
					});
				}else{
					swal({
						title : "",
						text : "操作成功",
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
function submitdata(){
	if($('#checkboxdiv').find("a[class=checked]").length==0){
		swal({
			title : "",
			text : "请选择管理范围",
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
	var managerole=2;
	var userid=$('#userid').val();
	var alldata={"functionlist":[],"organizelist":[]};
	$('#functiondiv').find("a[class=checked]").each(function(){
		var functionmap={};
		functionmap["functionid"]=$(this).find("input[name=functionid]").val();
		alldata.functionlist.push(functionmap);
	})
	$('#checkboxdiv').find("a[class=checked]").each(function(){
		var organizemap={};
		organizemap["organizeid"]=$(this).find("input[name=organizeid]").val();
		alldata.organizelist.push(organizemap);
	})
	swal({
		title : "",
		text : "是否提交？",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath()%>/userbackstage/updateUserInfoRole",
			data:"userid="+userid+"&managerole="+managerole+"&datalist="+JSON.stringify(alldata),
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
						
					});
				}else{
					location.reload();
				}
			}
		})
	});
}
function deleteManage(userid,managerole){
	swal({
		title : "",
		text : "是否移除？",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath()%>/userbackstage/deleteManageRole",
			data:"userid="+userid+"&managerole="+managerole,
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
						
					});
				}else{
					location.reload();
				}
			}
		})
	});
}
function changenextul4(obj){
	if($(obj).attr("class")=="bg_hidden"){
		$(obj).prev().show();
		$(obj).attr("class","bg_show");
		$(obj).nextAll("ul").show();
		changeheight4();
	}else if($(obj).attr("class")=="bg_show"){
		$(obj).attr("class","bg_hidden");
		$(obj).nextAll("ul").hide();
		$(obj).prev().hide();
		changeheight4();
	}
}
function changeheight4(){
	var whiteHeight=0;
	$(".tree_list .white_line").each(function() {	
		whiteHeight = $(this).parent().height();
		whiteHeight = whiteHeight - 21 ;
	    $(this).height(whiteHeight) ;
	});
}
function changefunction(obj,type){
	if(type==0){
		if($(obj).attr("class")=="check"){
			$(obj).attr("class","checked");
			$(obj).parent().next().find("a[class=check]").attr("class","checked");
		}else{
			$(obj).attr("class","check");
			$(obj).parent().next().find("a[class=checked]").attr("class","check");
		}
	}else if(type==1){
		if($(obj).attr("class")=="check"){
			$(obj).attr("class","checked");
			$(obj).parent().parent().parent().prev().find("a[class=check]").attr("class","checked");
		}else{
			$(obj).attr("class","check");
			if($(obj).parent().parent().parent().find("a[class=checked]").length==0){
				$(obj).parent().parent().parent().prev().find("a[class=checked]").attr("class","check");
			}
		}
	}
}
</script>
</head>
<body>

<input type="hidden" id="userid"/>
<input type="hidden" id="managerole"/>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="<%=request.getContextPath() %>/userbackstage/index">首页</a><i>/</i><a href="#">企业管理</a><i>/</i><span>管理员管理</span></p></div>  
    <div class="page_tab m_top">
        <form action="<%=request.getContextPath() %>/userbackstage/getManageList" method="post">
        <input type="hidden" name="organizeid" value="${map.organizeid}"/>
        <div class="tab_name"><span class="gray1">管理员管理</span><a href="javascript:void(0)" onclick="initadd();">添加</a><input type="submit" value="搜索" class="btn" /><input type="text" class="text" name="realname" placeholder="请输入关键字" value="${map.realname }"/></div>
        </form>
        <div class="personnel">
        	<div class="l_tree" id="organizetree"  style="overflow:hidden;overflow-y:visible;">
            </div>
            <div class="r_list">
            	<div class="tab_list">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0" id="userdiv">
                        <tr class="head_td none_tborder">
                            <td width="30">&nbsp;</td>
                            <td>姓名</td>
                            <td>电话</td>
                            <td>管理范围</td>
                            <td>操作</td>
                        </tr>
                        <c:forEach items="${userlist}" var="user">
                        <tr>
                        	<td class="img_td"><div class="img"><img src="${user.headimage}" width="30" height="30" /></div></td>
                            <td>${user.realname}<c:if test="${user.managerole==3}"><i class="red">(超级管理员)</i></c:if></td>
                            <td>${user.phone}</td>
                            <td>
                            <c:choose>
                            	<c:when test="${user.managerole==2}">${user.organizenames}</c:when>
                            	<c:otherwise>全部店面</c:otherwise>
                            </c:choose></td>
                            <td>
                            	<c:choose>
                            		<c:when test="${map.managerole==3}">
                            			<a href="javascript:void(0)" class="blue" onclick="deleteManage('${user.userid}','${user.managerole}')">移除管理权限</a>
                            		</c:when>
                            		<c:when test="${map.managerole == 2 }">
                            			<c:if test="${user.managerole!=3 }">
                            				<a href="javascript:void(0)" class="blue" onclick="deleteManage('${user.userid}','${user.managerole}')">移除管理权限</a>
                            			</c:if>
                            		</c:when>
                            	</c:choose>
                            </td>
                        </tr>
                        </c:forEach>
                    </table>
                </div>  
                <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination--> 
            </div>
            <div class="clear"></div>
        </div>
    </div>
</div>
<div class="div_mask" style="display:none;"></div>
<div class="tc_selrange" style="display:none">
	<div class="tc_title"><span></span><a href="javascript:void(0);" onclick="$('.tc_selrange').hide();$('.div_mask').hide();">×</a></div>
    <div class="range">
    	<div class="l_box" id="organizetree2"  style="overflow-y:overlay;">
    		
        </div>
        <div class="r_box">
        	<div class="sel_xx">
                <div class="sel_box">
                    <input type="text" class="text" placeholder="请输入姓名" id="searchrealname"/>
                    <input type="button" class="find_btn" value="搜索" onclick="searchuser()" />
                    <div class="clear"></div>
                </div>
            </div>
            <div class="tab_list">
            	<table width="100%" border="0" cellpadding="0" cellspacing="0" id="usertable">
                	<tr class="head_td">
                    	<td width="30">&nbsp;</td>
                        <td width="70">姓名</td>
                        <td width="70">性别</td>
                        <td width="130">电话</td>
                        <td>操作</td>
                    </tr>
                </table>
            </div>
            <div id="Pagination" style="width:450px;"><div id="userPage"></div></div><!--动态的获取pagination的宽度赋值给Pagination-->
            <!-- <div class="tc_btnbox"><a  href="javascript:void(0);" onclick="$('.div_mask').hide();$('.tc_selrange').hide();"  class="bg_gay2">取消</a>
            <a href="javascript:void(0)" onclick="submitdata()"  class="bg_yellow" id="addsubmitdata">确定</a> -->
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>
<div class="tc_powerxx" style="display:none;">
	<div class="tc_title"><span>分配角色</span><a href="javascript:void(0);" onclick="$('.tc_powerxx').hide();$('.div_mask').hide();">×</a></div>
    <div class="box">
    	<c:if test="${map.managerole==3}"><span><a href="javascript:void(0)" onclick="chooserole(3)">超级管理员</a></span></c:if>
        <span><a href="javascript:void(0)" onclick="chooserole(2)">单店管理员</a></span>
    </div>
</div>

<div class="tc_structure" style="display:none;max-height:60%;overflow:hidden;overflow-y:visible;" id="selectfunction">
	<div class="tc_title"><span>管理员权限设置</span><a href="javascript:void(0);" onclick="$('#selectfunction').hide();$('.div_mask').hide();">×</a></div>
    <ul class="tree_list" id="functiondiv">
    	<c:forEach items="${functionlist}" var="item">
    		<c:if test="${item.parentid==0}">
    			<li>
    			<div class="gray_line" style="display: none;"></div>
            	<span class="bg_hidden" onclick="changenextul4(this)"><a href="javascript:void(0)" class="checked" onclick="changefunction(this,0)">
            	<input type="hidden" name="functionid" value="${item.functionid}"/>
            	<input type="hidden" name="functionname" value="${item.name}"/>
            	选择</a><i>${item.name}</i></span>
            	<ul style="display:none">
            	<c:forEach items="${functionlist}" var="item1">
            		<c:if test="${item1.parentid==item.functionid}">
            			<li class="li_bg"><span class="bg_show" ><a href="javascript:void(0)" class="checked" onclick="changefunction(this,1)">
            			<input type="hidden" name="functionid" value="${item1.functionid}"/>
            			<input type="hidden" name="functionname" value="${item1.name}"/>
            			选择</a><i>${item1.name}</i></span></li>
            		</c:if>
            	</c:forEach>
            	</ul>
            	</li>
    		</c:if>
    	</c:forEach>
    </ul>
    <div class="tc_btnbox"><a href="javascript:void(0);" onclick="$('#selectfunction').hide();$('.tc_powerxx').show();" class="bg_gay2">取消</a>
    <a href="javascript:void(0);"  class="bg_yellow" onclick="submitmanage()">确定</a></div>
</div>

<div class="tc_structure" style="display:none;" id="checkboxselect"  >
	<div class="tc_title"><span>管理范围</span><a href="javascript:void(0);" onclick="$('#checkboxselect').hide();$('#selectfunction').show();">×</a></div>
    <ul class="tree_list" id="checkboxdiv">
    	
    </ul>
    <div class="tc_btnbox"><a href="javascript:void(0);" onclick="$('#selectfunction').show();$('#checkboxselect').hide();" class="bg_gay2">取消</a>
    <a href="javascript:void(0);"  class="bg_yellow" onclick="submitdata();">确定</a></div>
</div>
<script type="text/javascript">
function submitmanage(){
	if($('#functiondiv').find("a[class=checked]").length==0){
		swal({
			title : "",
			text : "请至少勾选一个菜单",
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
	$('#selectfunction').hide();$('#checkboxselect').show();
}
</script>
</body>
</html>