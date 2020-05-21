<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>企业权限</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organize.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/powerorganize.js"></script>
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
var uid='${userinfo.userid}';
function onloadData(){

	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath()%>/userbackstage/getSelectPowerCompanyone?isone=isone",
		success:function(data){
			if(data.listone != null || data.listone.length != ""){
				var temp="<tr class=\"head_td\">"+"<td style=\"width:10%\">模块</td><td style=\"width:10%;\">权限状态</td><td style=\"width:15%;\">操作</td></tr>";
				for(var i= 0;i<data.listone.length;i++){
					temp += "<tr><td>"+data.listone[i].powername+"</td>";
					if(data.listone[i].edition==1){
						temp+="<td><i class=\"green\">已获得</i>(普通)</td><td>&nbsp;</td>";
					}else{
						if(data.listone[i].result==1){
								temp += "<td><i class=\"green\">已获得</i>（专业版)</td><td>&nbsp;</td>";
						}else if(data.listone[i].status ==2){
								temp+="<td>未获得</td><td><i class=\"green\">申请中</i></br>"+data.listone[i].createtime+"</td>";
						}else if(data.listone[i].status ==1){
							temp+="<td><i class=\"red\">已拒绝</i>（专业版)</td>";
							if(data.powerMap.power7001010 != null){
								temp+="<td><a href=\"#\" class=\"blue\" onclick=\"checkPweroCompany('"+data.listone[i].powerid+"')\" >申请</a></td>"
							}	
						}else{
							temp+="<td>未获得</td>";
							if(data.powerMap.power7001010 != null){
								temp+="<td><a href=\"#\" class=\"blue\" onclick=\"checkPweroCompany('"+data.listone[i].powerid+"')\" >申请</a></td>"
							}	
						}
					}
					//
				}
				$('#modeltabs').html(temp);
			}else{
				$('#modeltabs').html("");
			}		
				
		}
	})
}

function checkPweroCompany(powercompanyid){
	location.href="/userbackstage/getPowerByCompany?powerid="+powercompanyid;
}
var funid ="";
//回调函数
function callbackfunc(organizeid){
	funid = organizeid;
	if(organizeid == '' || organizeid == null){
		onloadData();
	}else{
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath()%>/userbackstage/getSelectPowerByPowerid",
			data:"organizeid="+organizeid,
			success:function(data){
				if(data.listone != null || data.listone.length != ""){
					var temp="<tr class=\"head_td\">"+"<td style=\"width:10%\">模块</td><td style=\"width:15%;\">权限</td><td style=\"width:40%;\">描述</td><td style=\"width:10%;\">权限状态</td><td style=\"width:15%;\">操作</td></tr>";
					for(var i= 0;i<data.listone.length;i++){
						temp += "<tr><td rowspan=\""+data.listone[i].childnum+"\">"+data.listone[i].powername+"</td>";
						if(data.datalist.length != null || data.datalist.length != ""){
							var rumnumber = 0;
							for(var j= 0;j<data.datalist.length;j++){
								var info="";
								if(data.datalist[j].info.length > 30 ){
									info = data.datalist[j].info.substring(0,30)+"......";
								}else{
									info = data.datalist[j].info+"  ";
								}
								var falg= false;
								if(data.listone[i].powerid == data.datalist[j].parentid){
									if(rumnumber == 0){
										rumnumber++;
										temp += "<td>"+data.datalist[j].powername+"</td>"+
										"<td>"+info+"<a href=\"#\" class=\"blue\"  onclick=\"details('"+data.datalist[j].powerid+"')\">详情</a></td>";
										if(data.datalist[j].edition==1){
											temp+="<td><i class=\"green\">已获得</i>(普通)</td>";
											falg= true;
										}else{
											if(data.datalist[j].result==1){
												temp += "<td><i class=\"green\">已获得</i>（专业版)</td>";
												falg= true;
											}else if(data.datalist[j].status ==2){
												temp+="<td>未获得</td>";
											}else{
												temp+="<td>未获得</td>";
											}
										}	
										if(data.poweruserlist!=null || data.poweruserlist.length>0){
											temp+="<td>";
											var row = 0;
											for(var p =0; p < data.poweruserlist.length ;p++){
												if(data.datalist[j].powerid == data.poweruserlist[p].powerid){
													if(row < 3){
														row ++;
														temp+="<i class=\"i_user\">"+data.poweruserlist[p].realname+",</i>";
													}
												}
											}
											if(data.powerMap.power7001010 != null){
												if(falg){
													temp+="<a href=\"#\" class=\"add_user\" onclick=\"checkfunAdd('"+data.datalist[j].powerid+"','"+data.datalist[j].powername+"')\">添加</a></td></tr>";
												}else{
													temp+="<a href=\"#\" class=\"add_user\" onclick=\"checkNot()\">添加</a></td></tr>";
												}
											}
										}else{
											if(data.powerMap.power7001010 != null){	
												if(falg){
													temp+="<a href=\"#\" class=\"add_user\" onclick=\"checkfunAdd('"+data.datalist[j].powerid+"','"+data.datalist[j].powername+"')\">添加</a></td></tr>";
												}else{
													temp+="<a href=\"#\" class=\"add_user\" onclick=\"checkNot()\">添加</a></td></tr>";
												}
											}
										}
									}else{
										temp += "<tr><td>"+data.datalist[j].powername+"</td>"+
										"<td>"+info+"<a href=\"#\" class=\"blue\"  onclick=\"details('"+data.datalist[j].powerid+"')\">详情</a></td>";
										if(data.datalist[j].edition==1){
											temp+="<td><i class=\"green\">已获得</i>（(普通)</td>";
											falg= true;
										}else{
											if( data.datalist[j].result==1){
												temp += "<td><i class=\"green\">已获得</i>（专业版)</td>";
												falg= true;
											}else if(data.datalist[j].status ==2){
												temp+="<td>未获得</td>";
											}else{
												temp+="<td>未获得</td>";
											}
											
										}	
										if(data.poweruserlist!=null || data.poweruserlist.length>0){
											temp+="<td>";
											var row = 0;
											for(var p =0; p < data.poweruserlist.length ;p++){
												if(data.datalist[j].powerid == data.poweruserlist[p].powerid ){
													if(row < 3){
														row ++;
														temp+="<i class=\"i_user\">"+data.poweruserlist[p].realname+",</i>";
													}
												}
											}
											if(data.powerMap.power7001010 != null){
												if(falg){
													temp+="<a href=\"#\" class=\"add_user\" onclick=\"checkfunAdd('"+data.datalist[j].powerid+"','"+data.datalist[j].powername+"')\">添加</a></td></tr>";
												}else{
													temp+="<a href=\"#\" class=\"add_user\" onclick=\"checkNot()\">添加</a></td></tr>";
												}
											}
										}else{
											if(data.powerMap.power7001010 != null){
												if(falg){
													temp+="<a href=\"#\" class=\"add_user\" onclick=\"checkfunAdd('"+data.datalist[j].powerid+"','"+data.datalist[j].powername+"')\">添加</a></td></tr>";
												}else{
													temp+="<a href=\"#\" class=\"add_user\" onclick=\"checkNot()\">添加</a></td></tr>";
												}
											}
										}
										
									}
								}
							}
					
						}
					}
					$('#modeltabs').html(temp);
				}else{
					$('#modeltabs').html("");
				}		
					
			}
		})
	}

}
var userlist = {};
function checkfunAdd(powerid,powername){
	showOrgan();
	userlist = {};
	$('#rangeul').html('');
	$('#usertable').html('');
	$('.tc_selrange').show();
	$('.div_mask').show();
	$('#powerid').val(powerid);
	$('#powername').val(powername);
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath()%>/userbackstage/getPowerUser",
		data:"powerid="+powerid,
		success:function(data){
			if(data.userlist.length > 0){
				$.each(data.userlist,function(i,map){
					var usermap = new Object ();
					usermap.userid = map.ownerid;
					usermap.powerid = map.powerid;
					usermap.poweruserid = map.poweruserid;
					userlist[i]=usermap;
				});
			}
		}
	});
}
function checkNot(){
	swal({
		title : "",
		text : "暂无权限,请申请权限！",
		type : "warning",
		showCancelButton : false,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
	
	});
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
				$(".div_mask").show();
				$("#datails").show();
				if(data.datalist != null && data.datalist.length>0){
					$("#powername").html(data.datalist[0].powername);
					$("#info").html(data.datalist[0].info);
				}	
			}
		}
	})
	
}


$(document).ready(function(){
	$('#nav_power').attr('class','active');$('#nav_power').parent().parent().show();
})



</script>
</head>

<body onload="onloadData();" >

<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page"  >
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">企业管理</a></p></div>  
	<input type="hidden" id="ownerid"/>
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1">企业权限</span></div>
        <div class="personnel">
			<div class="l_tree" id="organizetree"  style="overflow:hidden;overflow-y:visible;" >
        	
            </div>
            
             <div class="r_list"  id="userdiv">
            	<div class="tab_list">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0" id="modeltabs" style="font-size: 12px;">
                       
                    </table>
                  
                </div> 
              </div>    
      
            <div class="clear"></div>
        </div>
    </div>
    <div class="div_mask"  style="display:none"></div>
</div>


<div class="tc_detail" style="display:none" id="datails">
	<div class="tc_title"><span  id="powername"></span><a href="#" onclick="$('.div_mask').hide();$('#datails').hide();">×</a></div>
    <div class="p_box">
        <p id="info"></p>
    </div>
    <div class="clear"></div>
</div>


<div class="tc_selrange" style="display:none">
	<div class="tc_title"><span>企业权限</span><a href="javascript:void(0);" onclick="$('.tc_selrange').hide();$('.div_mask').hide();">×</a></div>
    <div class="range">
    	<div class="l_box" id="new_organizetree" style=" width:280px;max-height: 100%; overflow-x: hidden; overflow-y: visible;">
    	
        </div>
        <div class="r_box" style=" width:440px; overflow-x: hidden; overflow-y: visible;">
        	<div class="sel_xx">
                <div class="sel_box">
                    <input type="text" class="text" placeholder="请输入姓名" id="searchrealname"/>
                    <input type="button" class="find_btn" value="搜索" onclick="searchuser()" />
                    <div class="clear"></div>
                </div>
                <ul id="rangeul">
                </ul>
            </div>
            <div class="tab_list" >
           	<input type="hidden" id="powerid"/>
           	<input type="hidden" id="powername"/>
            	<table width="100%" border="0" cellpadding="0" cellspacing="0" id="usertable" style="font-size: 12px;">
                	<tr class="head_td">
                    	<td width="30">&nbsp;</td>
                        <td width="70">姓名</td>
                        <td width="50">性别</td>
                        <td width="100">电话</td>
                        <td>操作</td>
                    </tr>
                </table>
            </div>
            <div id="Pagination" style="width:450px;"><div id="userPagination"></div></div><!--动态的获取pagination的宽度赋值给Pagination-->
            <div class="tc_btnbox"> <a href="javascript:void(0)" onclick="submitdata()"  class="bg_yellow" id="addsubmitdata">确定</a></div>
            
        </div>
        <div class="clear"></div>
    </div>
</div>
<script type="text/javascript">
function checkput(powerid){
	$("#powerid").val(powerid);
	$('.tc_selrange').show();
	$('.div_mask').show();
}

function submitdata(){
	swal({
		title : "",
		text : "确认提交",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$('.tc_selrange').hide();$('.div_mask').hide();
		var alldata={"userlist":[]};
		$('#rangeul').find("input[name=userid]").each(function(index){
			var userlist={};
			userlist['userid']=$(this).val();
			userlist['realname']=$(this).attr("realname");
			alldata.userlist.push(userlist); 
		})
		var userlist=JSON.stringify(alldata.userlist);
		var powerid = $("#powerid").val();
		var powername = $("#powername").val();
		$.ajax({
			type:'post',
			dataType:'json',
			url:'/userbackstage/updataUserByPowerId',
			data:"powerid="+powerid+"&useridlist="+userlist,
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
					callbackfunc(funid);
				}
			}
		});	
	});
}

function canceluser(poweruserid,powerid){
	swal({
		title : "",
		text : "确认取消",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$.ajax({
			type:'post',
			dataType:'json',
			url:'/userbackstage/updataUserByPowerId',
			data:"poweruserid="+poweruserid,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text :"取消权限失败",
						type : "error",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
					
					});
				}else{
					$('.tc_selrange').hide();
					$('.div_mask').hide();
					callbackfunc(funid);
				}
			}
		});	
	});
}


function pageHelper(num){
	power_param.currentPage = num;
	if(power_param.organizeid != "" && power_param.searchrealname == ""){
		callback("");
	}else if(power_param.organizeid == "" && power_param.searchrealname != ""){
		searchuser("search");
	}
}

var power_param = new Object();
power_param.rowPage = 5;

function callback(organizeid){
	if(organizeid != ""){
		power_param.organizeid = organizeid;
		power_param.searchrealname = "";
		power_param.currentPage = 1;
	}
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/userbackstage/getNextOrgainizeById",
		data:power_param,
		success:function(data){
			$('#userPagination').html(data.pager);
			if(data.userlist!= null && data.userlist.length >0 ){
				$('#usertable').html("<tr class=\"head_td\">"+
			        	"<td width=\"30\">&nbsp;</td>"+
			            "<td width=\"70\">姓名</td>"+
			            "<td width=\"50\">性别</td>"+
			            "<td width=\"100\">电话</td>"+
			            "<td>操作</td>"+
			        "</tr>");
			}
			if(data.userlist.length>0){
				var temp="";
				for(var i=0;i<data.userlist.length;i++){
					if(uid !=data.userlist[i].userid ){
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
                        if(userlist != null && data.userlist[i].managerole != 3 && data.userlist[i].managerole != 2){
                        	var powerid = $("#powerid").val();
                        	var falg = true ;
                        	var poweruserid="";
                        	$.each(userlist,function(u,map){
                        		if(powerid == map.powerid){
                        			if(data.userlist[i].userid == map.userid){
                        				falg = false;
                        				poweruserid=map.poweruserid;
                        				powerid = map.powerid;
                        			}
                            	}
                        	});
                        	if(falg){
                        		temp+="<td><a href=\"javascript:void(0)\" onclick=\"adduser(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"')\" class=\"link\">添加</a></td></tr>";
                        	}else{
                        		temp+="<td><a href=\"javascript:void(0)\" onclick=\"canceluser('"+poweruserid+"','"+powerid+"')\" class=\"link\">取消</a></td></tr>";
                        	}
                        }else{
                        	temp+="<td><a href=\"javascript:void(0)\" onclick=\"adduser(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"')\" class=\"link\">添加</a></td></tr>";
                        }
                        
					}
				
				}
				$('#usertable').append(temp);
			}else{
				$('#usertable').append("");
			}
		}
	})
}

function searchuser(type){
	var ownerid ="";
	ownerid = $("#ownerid").val()
	if(type == "search"){
		var searchrealname=$('#searchrealname').val();
		if(searchrealname == ""){
			swal("","请输入用户名称","warning");
			return false;
		}
		power_param.organizeid = "";
		power_param.searchrealname = searchrealname;
		power_param.currentPage = 1;
	}
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/userbackstage/getSearchUserByName",
		data:power_param,
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
					if(uid !=data.userlist[i].userid ){
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
                        if(userlist != null  && data.userlist[i].managerole != 3 && data.userlist[i].managerole != 2){
                        	var powerid = $("#powerid").val();
                        	var falg = true ;
                        	var poweruserid="";
                        	$.each(userlist,function(u,map){
                        		if(powerid == map.powerid){
                        			if(data.userlist[i].userid == map.userid){
                        				falg = false;
                        				poweruserid=map.poweruserid;
                        				powerid = map.powerid;
                        			}
                            	}
                        	});
                        	if(falg){
                        		temp+="<td><a href=\"javascript:void(0)\" onclick=\"adduser(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"')\" class=\"link\">添加</a></td></tr>";
                        	}else{
                        		temp+="<td><a href=\"javascript:void(0)\" onclick=\"canceluser('"+poweruserid+"','"+powerid+"')\" class=\"link\">取消</a></td></tr>";
                        	}
                        }else{
                        	temp+="<td><a href=\"javascript:void(0)\" onclick=\"adduser(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"')\" class=\"link\">添加</a></td></tr>";
                        }
                        
					}
				
				}
				$('#usertable').append(temp);
			}
		}
	})
}

function adduser(obj,userid,realname){
	$('#rangeul').find("input[value="+userid+"]").parent().remove();
	var temp="<li><input type=\"hidden\" name=\"userid\" realname='"+realname+"' value=\""+userid+"\"/>"+realname+"<a class=\"del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\" onclick=\"deleteorganizeuser(this)\" /></a></li>";
	$('#rangeul').append(temp);
}

function deleteorganizeuser(obj){
	$(obj).parent().parent().remove();
}
</script>

</body>
</html>
