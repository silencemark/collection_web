<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>人员权限</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organize.js"></script>
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
	
function checkAll(obj){
	var check = $(obj).attr("class");
	if(check == "check"){
		var checkparam = $('#modeltabs').find('td a[class="check"]');
		$.each(checkparam,function(i,map){
			$(map).attr("class","checked");
		});
	}else if(check == "checked"){
		var checkparam = $('#modeltabs').find('td a[class="checked"]');
		$.each(checkparam,function(i,map){
			$(map).attr("class","check");
		});
	}
}
function onloadData(){
	var list = JSON.parse('${listmap}');
	var ownerid = list.ownerid;//需要修改权限的人员id
	$("#ownerid").val(ownerid);
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath()%>/userbackstage/getPowerAll",
		success:function(data){
			if(data.countPower!=null || data.countPower.length > 0){
					var temp="<tr class=\"head_td\">"+"<td >模块</td><td >权限</td><td >描述</td><td><div><a href=\"javascript:void(0)\" class=\"check\" onclick=\"checkAll(this)\"></a></div></td></tr>";
						for(var p = 0;p < data.countPower.length;p++){
							temp += "<tr>";
							for(var i= 0;i<data.listone.length;i++){
								if(data.listone[i].powerid==data.countPower[p].powerid){
									temp +="<td rowspan=\""+data.countPower[p].childnum+"\">"+data.listone[i].powername+"</td>";
								}
							}
							if(data.datalist.length != null || data.datalist.length != ""){
								var rumnumber = 0;
								var num = 0;
								for(var j= 0;j<data.datalist.length;j++){
									num = 0;//
									////一级td显示
									if(data.countPower[p].powerid == data.datalist[j].parentid){
										if(rumnumber == 0){
											var info="";
											if(data.datalist[j].info.length > 30 ){
												info = data.datalist[j].info.substring(0,30)+"......";
											}else{
												info = data.datalist[j].info+"  ";
											}
											rumnumber++;
											temp += "<td>"+data.datalist[j].powername+"</td>"+
											"<td>"+info+"<a href=\"javascript:void(0)\" class=\"blue\"  onclick=\"details('"+data.datalist[j].powerid+"')\">详情</a></td>";
											if(list!=null || list.datalist.length != "" && list!= null || list.length !=""){
												for(var k=0;k<list.datalist.length;k++){
													if(list.datalist[k].powerid==data.datalist[j].powerid){
														num=1;
													}
												}
												if(num == 1){
													temp += "<td><div id=\"checked\"><a href=\"javascript:void(0)\" class=\"checked\" onclick=\"checked(this)\" powerid='"+data.datalist[j].powerid+"' ></a></div></td></tr>";	
												}else{
													temp += "<td><div id=\"checked\"><a href=\"javascript:void(0)\" class=\"check\" onclick=\"checked(this)\" powerid='"+data.datalist[j].powerid+"'></a></div></td></tr>";	
												}
											}else{
												temp += "<td><div id=\"checked\"><a href=\"javascript:void(0)\" class=\"check\" onclick=\"checked(this)\" powerid='"+data.datalist[j].powerid+"'></a></div></td></tr>";	
											}			
										////	
										}else{
											var info = "";
											if(data.datalist[j].info.length > 30 ){
												info = data.datalist[j].info.substring(0,30)+"......";
											}else{
												info = data.datalist[j].info+"  ";
											}
											temp += "<tr><td>"+data.datalist[j].powername+"</td>"+
											"<td>"+info+"<a href=\"javascript:void(0)\" class=\"blue\" onclick=\"details('"+data.datalist[j].powerid+"')\">详情</a></td>";
											if(list!=null || list.datalist.length != ""){
												for(var k=0;k<list.datalist.length;k++){
													if(list.datalist[k].powerid==data.datalist[j].powerid){
														num=1
													}
												}
												if(num == 1){
													temp += "<td><div id=\"checked\"><a href=\"javascript:void(0)\" class=\"checked\" onclick=\"checked(this)\" powerid='"+data.datalist[j].powerid+"'></a></div></td></tr>";	
												}else{
													temp += "<td><div id=\"checked\"><a href=\"javascript:void(0)\" class=\"check\" onclick=\"checked(this)\" powerid='"+data.datalist[j].powerid+"'></a></div></td></tr>";	
												}
											}else{
													temp += "<td><div id=\"checked\"><a href=\"javascript:void(0)\" class=\"check\" onclick=\"checked(this)\" powerid='"+data.datalist[j].powerid+"'></a></div></td></tr>";	
											}
										}
									}
								}
							}
							
					}//countPower For
					temp += " <tr class=\"foot_td\">"+
			              	 " <td>&nbsp;</td>"+
			              	 " <td>&nbsp;</td>"+
		                	"<td colspan='2'><a href=\"javascript:void(0)\" class=\"a_btn bg_yellow\" onclick=\"checkSavePower('"+ownerid+"')\">保存</a>"+
		                	"<a href=\"javascript:void(0)\" class=\"a_btn bg_gay2\" onclick=\"checkRetrun()\">取消</a></td>"+
        		 		"</tr>";
					$('#modeltabs').html(temp);
			
			}else{
				$('#modeltabs').html("");
			}//countPower
			
				
		}
	})
}
//取消按钮
function checkRetrun(){
	location.href="<%=request.getContextPath()%>/userbackstage/getNextOrgainize";
}

//保存
function checkSavePower(ownerid){
	var listInsrt = [];
	var i = 0;
	var  param = new Object();
	param.ownerid=ownerid;
	$("#checked a[class=checked]").each(function(index,dom){
		listInsrt[i] = {powerid:$(this).attr("powerid")};
		i++;
	})	
	param.listInsrt = JSON.stringify(listInsrt);
	
	var listDelete = [];
	var j = 0;
	$("#checked a[class=check]").each(function(index,dom){
		listDelete[j] = {powerid:$(this).attr("powerid")};
		j++;
	})	
	param.listDelete = JSON.stringify(listDelete);
	
	if(param != null){
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath()%>/userbackstage/insertUserPowerInfo",
			data:param,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text : data.errors,
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
						text : data.success,
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						window.history.go(-1);
					});	
				}
			}
		})
	}	
}

//详情
function details(powerid){
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath()%>/userbackstage/getPowerByPowerid",
		data:"powerid="+powerid,
		success:function(data){
			if(data.status==1){
				swal({
					title : "",
					text : data.errors,
					type : "error",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
				
				});
			}else{
				$("#pagediv").css("display","block");
				$("#datails").show();
				if(data.datalist != null && data.datalist.length>0){
					$("#powername").html(data.datalist[0].powername);
					$("#info").html(data.datalist[0].info);
				}	
			}
		}
	})
	
}

function checkput(){
	$("#pagediv").css("display","block");
	$('.tc_selrange').show();
}



//选中
function checked(obj){
	if($(obj).attr("class") == "checked"){
		$(obj).attr("class","check")
	}else{
		$(obj).attr("class","checked")
	}
}

function checkRtun(rum){
	$("#pagediv").css("display","none");
	$("#datails").css("display","none");
	if(rum = 0){
		$("#datails").hide();
	}else{
		$("#userdiv").hide();
	}
}

var userParam = new Object();
userParam.rowPage = 5;

function pageHelper(num){
	userParam.currentPage = num;
	if(userParam.searchrealname == "" && userParam.organizeid != ""){
		callbackfunc("");
	}else if(userParam.searchrealname != "" && userParam.organizeid == ""){
		searchuser("search");
	}
}

function callbackfunc(organizeid){
	if(organizeid != ""){
		userParam.currentPage = 1;
		userParam.organizeid = organizeid;
		userParam.searchrealname = "";
	}
	var ownerid ="";
	ownerid = $("#ownerid").val()
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath() %>/userbackstage/getNextOrgainizeById",
		data:userParam,
		success:function(data){
			$('#userPagination').html(data.pager);
			$('#usertable').html("<tr class=\"head_td\">"+
		        	"<td width=\"30\">&nbsp;</td>"+
		            "<td width=\"70\">姓名</td>"+
		            "<td width=\"50\">性别</td>"+
		            "<td width=\"100\">电话</td>"+
		            "<td>操作</td>"+
		        "</tr>");
			if(data.userlist.length>0){
				var temp="";
				for(var i=0;i<data.userlist.length;i++){
					if(ownerid !=data.userlist[i].userid ){
							temp+="<tr><td class=\"img_td\"><div class=\"img\"><img src=\""+data.userlist[i].headimage+"\" width=\"30\" height=\"30\" /></div></td>"+
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
		                        temp+="<td><a href=\"javascript:void(0)\" onclick=\"checkCopy('"+data.userlist[i].userid+"')\" class=\"link\">复制权限</a></td></tr>";
					    	}
					}
				$('#usertable').append(temp);
			}else{
				$('#usertable').html("");
			}
		}
	})
}


function searchuser(type){
	var ownerid ="";
	ownerid = $("#ownerid").val()
	if(type != "search"){
		var searchrealname=$('#searchrealname').val();
		if(searchrealname == ""){
			swal("","请输入用户名称···","warning");
			return false;
		}
		userParam.currentPage = 1;
		userParam.organizeid = "";
		userParam.searchrealname = searchrealname;
	}
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/userbackstage/getSearchUserByName",
		data:userParam,
		success:function(data){
			$('#userPagination').html(data.pager);
			$('#usertable').html("<tr class=\"head_td\">"+
		        	"<td width=\"30\">&nbsp;</td>"+
		            "<td width=\"70\">姓名</td>"+
		            "<td width=\"50\">性别</td>"+
		            "<td width=\"100\">电话</td>"+
		            "<td>操作</td>"+
		        "</tr>");
			if(data.userlist.length>0){
				var temp="";
				for(var i=0;i<data.userlist.length;i++){
					if(ownerid !=data.userlist[i].userid ){
						temp+="<tr><td class=\"img_td\"><div class=\"img\"><img src=\""+data.userlist[i].headimage+"\" width=\"30\" height=\"30\" /></div></td>"+
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
                        temp+="<td><a href=\"javascript:void(0)\" onclick=\"checkCopy('"+data.userlist[i].userid+"')\" class=\"link\">复制权限</a></td></tr>";
					}
				
				}
				$('#usertable').append(temp);
			}
		}
	})
}

//复制权限
function checkCopy(ownerid){
	var userid ="";
	userid = $("#ownerid").val();
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/userbackstage/getPowerCopyByUserid",
		data:"userid="+userid+"&ownerid="+ownerid,
		success:function(data){
			if(data.status==1){
				swal({
					title : "",
					text : data.errors,
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
					text : data.success,
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href="/userbackstage/getPowerByUserId?userid="+userid;
				});
			}
		}
	})
	
}
$(document).ready(function(){
	$('#nav_userlist').attr('class','active');$('#nav_userlist').parent().parent().show();
})

</script>
</head>

<body onload="onloadData()" >

<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page"  >
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">企业管理</a><i>/</i><a href="#">人员管理</a><i>/</i><span>分配权限</span></p></div>  
	<input type="hidden" id="ownerid"/>
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1">企业权限</span><a href="#" class="wid_01" onclick="checkput()">复制权限</a></div>
        <div class="personnel">

           
            	<div class="tab_list">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0" id="modeltabs" style="font-size: 12px;">
                       
                    </table>
                  
                </div>     
      
            <div class="clear"></div>
        </div>
    </div>
    <div class="div_mask" id="pagediv" style="display:none"></div>
</div>

<div class="tc_detail" style="display:none" id="datails">
	<div class="tc_title"><span  id="powername"></span><a href="javascript:void(0)" onclick="checkRtun(1)">×</a></div>
    <div class="p_box">
        <p id="info"></p>
    </div>
    <div class="clear"></div>
</div>


<div class="tc_selrange" style="display:none">
	<div class="tc_title"><span>企业权限</span><a href="javascript:void(0);" onclick="$('.tc_selrange').hide();$('.div_mask').hide();">×</a></div>
    <div class="range">
    	<div class="l_box" id="organizetree"  style="width:280px;overflow:hidden;overflow-y:visible;">
    	
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
            	<table width="100%" border="0" cellpadding="0" cellspacing="0" id="usertable" style="font-size:12px;">
                	<tr class="head_td">
                    	<td width="30">&nbsp;</td>
                        <td width="70">姓名</td>
                        <td width="70">性别</td>
                        <td width="130">电话</td>
                        <td>操作</td>
                    </tr>
                </table>
            </div>
            <div id="Pagination" style="width:450px;"><div id="userPagination"></div></div><!--动态的获取pagination的宽度赋值给Pagination-->
        </div>
        <div class="clear"></div>
    </div>
</div>


</body>
</html>
