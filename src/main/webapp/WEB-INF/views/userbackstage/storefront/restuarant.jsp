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
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organizeRadio.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organizeRange.js"></script>
<style type="text/css">
	.sel_xx li {
	    float: left;
	    border: 1px solid #eee;
	    height: 30px;
	    line-height: 30px;
	    padding: 0px 10px;
	    margin-bottom: 10px;
	    margin-right: 10px;
	    position: relative;
	}
	#addrangeul .del {
	    display: block;
	    width: 16px;
	    height: 16px;
	    position: absolute;
	    right: -38px;
	    top: -13px;
	    cursor: pointer;
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
	
	
var organizeid = "";

function callbackfunc(organizeid){
	this.organizeid = organizeid;
	$('#addeverydayrestuarant').show();
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath() %>/userbackstage/getReportModule",
		data:"organizeid="+organizeid,
		success:function(data){
			if(data.modulelist.length>0){
				$('#addeverydayrestuarant').hide();
				var temp="";
				for(var i=0;i<data.modulelist.length;i++){
					temp+="<tr>"+
                    	"<td width=\"75%\">"+data.modulelist[i].templatename+"</td>"+
                        "<td><a href=\"javascript:void(0)\" onclick=\"initupdate('"+data.modulelist[i].templateid+"')\" class=\"link\">修改</a><a href=\"javascript:void(0)\" class=\"del\" onclick=\"deletetemplate(this,'"+data.modulelist[i].templateid+"','"+data.modulelist[i].templatename+"')\" >删除</a></td>"+
                    "</tr>";
				}
				$('#modulediv').html(temp);
			}else{
				$('#modulediv').html("");
			}
		}
	});
	
	var param = new Object();
	param.organizeid = organizeid;
	$.ajax({
		url:"/userbackstage/getOrganizeTypeInfo",
		type:"post",
		data:param,
		success:function(data){
			if(data != '3'){
				$('#addeverydayrestuarant').hide();
			}
		}
	});
}

function queryorganizeuserlist(param){
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath() %>/userbackstage/getUserByOrganize",
		data:param,
		success:function(data){
			
			$('#usertable').html("<tr class=\"head_td\">"+
		        	"<td width=\"30\">&nbsp;</td>"+
		            "<td width=\"70\">姓名</td>"+
		            "<td width=\"50\">性别</td>"+
		            "<td width=\"100\">电话</td>"+
		            "<td>操作</td>"+
		        "</tr>");
			if(data.userlist.length>0){
				$('#userPagination').html(data.pager);
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

var param = new Object();
function callbackfunc1(organizeid){
	$('#searchrealname').val("");
	param.organizeid = organizeid;
	param.searchrealname = "";
	param.rowPage = 5;
	param.currentPage = 1;
	queryorganizeuserlist(param);
}
function adduser(obj,userid,realname){
	$('#rangeul').find("input[value="+userid+"]").parent().remove();
	var temp="<li><input type=\"hidden\" name=\"userid\" value=\""+userid+"\"/>"+realname+"<a class=\"del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\" onclick=\"deleteorganizeuser(this)\" /></a></li>";
	$('#rangeul').append(temp);
}

function searchuser(){
	var searchrealname=$('#searchrealname').val();
	param.currentPage = 1;
	param.searchrealname = searchrealname;
	queryorganizeuserlist(param);
}

function pageHelper2(num){
	param.currentPage = num;
	queryorganizeuserlist(param);
}

//修改的函数
function initupdate(templateid){
	if(organizeid == ""){
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
		return false;
	}
	
	$('#projectul').find("a[class=checked]").attr("class","check");
	$('input[value=10]').parent().attr("class","checked").attr("onclick","javascript:void(0)");
	$('input[value=11]').parent().attr("class","checked").attr("onclick","javascript:void(0)");
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/userbackstage/getTemplateInfo",
		data:"templateid="+templateid,
		success:function(data){
			var organizeid=data.organizeid;
			$('#templateid').val(data.templateid);
			if(data.extendlist.length>0){
				for(var i=0;i<data.extendlist.length;i++){
					$('#projectul').find("input[name=dataid][value="+data.extendlist[i].dataid+"]").parent().attr("class","checked");
				}
			}
			$('#li1_'+organizeid).find("a[class=check]").click();
			$('#li1_'+organizeid).parents("ul").show();
			
			if(data.rangelist.length>0){
				$('#addrangeul').html("");
				for(var i=0;i<data.rangelist.length;i++){
					if(data.rangelist[i].type==1){
						var temp="<li><input type=\"hidden\" name=\"userid\" value=\""+data.rangelist[i].userid+"\"/>"+data.rangelist[i].rangename+"<a class=\"del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\" onclick=\"deleteorganizeuser(this)\" /></a></li>";
						$('#addrangeul').append(temp);
					}else if(data.rangelist[i].type==2){
						var temp="<li><input type=\"hidden\" name=\"organizeid\" value=\""+data.rangelist[i].organizeid+"\"/>"+data.rangelist[i].rangename+"<a class=\"del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\" onclick=\"deleteorganizeuser(this)\" /></a></li>";
						$('#addrangeul').append(temp);
					}
					
				}
			}
			
			$('#addsubmitdata').hide();
			$('#updatesubmitdata').show();
			
			$('.tc_selrestaurant').show();
			$('.div_mask').show();
		}
	})
}

function updatedata(){
	
	var templateid=$('#templateid').val();
	swal({
		title : "",
		text : "确认提交",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	},function(){
		var alldata={"extendlist":[],"userlist":[]};
		$('#projectul').find("a[class=checked]").each(function(index){
			var extendlist={};
			extendlist['dataid']=$(this).find('input[name=dataid]').val();
			extendlist['typeid']=$(this).find('input[name=typeid]').val();
			alldata.extendlist.push(extendlist);
		})
		
		$('#addrangeul').find("input[name=organizeid]").each(function(index){
			var organizelist={};
			organizelist['organizeid']=$(this).val();
			alldata.userlist.push(organizelist); 
		})
		$('#addrangeul').find("input[name=userid]").each(function(index){
			var userlist={};
			userlist['userid']=$(this).val();
			alldata.userlist.push(userlist); 
		})
		if(alldata.userlist.length == 0){
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
			return false;
		}
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath() %>/userbackstage/updateTemplateReport",
			data:"organizeid="+organizeid+"&datalist="+JSON.stringify(alldata)+"&templateid="+templateid,
			success:function(data){
				location.reload();
			}
		})
	});
}

$(document).ready(function(){
	$('#nav_report').attr('class','active');$('#nav_report').parent().parent().show();
})
</script>

</head>
<body>

<input type="hidden" id="templateid"/>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="<%=request.getContextPath() %>/userbackstage/index">首页</a><i>/</i><a href="#">店面管理</a><i>/</i><span>每日报表</span></p></div>  
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1">每日报表</span><a style="display: none;" id="addeverydayrestuarant" href="javascript:void(0)" onclick="initadd();">新建</a></div>
        <div class="worksheet">
        	<div class="l_tree" id="organizetree"  style="overflow:hidden;overflow-y:visible;">
        	
            </div>
            <div class="r_list">
            	<div class="tab_list">
                	<table border="0" cellpadding="0" cellspacing="0" width="100%" id="modulediv">
                		<c:forEach items="${modulelist}" var="module">
                    	<tr>
                        	<td width="75%">${module.templatename}</td>
                            <td><a href="javascript:void(0)" class="link" onclick="initupdate('${module.templateid}')">修改</a><a href="javascript:void(0)" onclick="deletetemplate(this,'${module.templateid}','${module.templatename}')" class="del">删除</a></td>
                        </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
            <div class="clear"></div>
        </div>
    </div>
</div>

<div class="div_mask" style="display:none;"></div>
<div class="tc_selrestaurant" style="display:none; max-height:600px; overflow:auto;">
	<div class="tc_title"><span>填写项目</span><a href="javascript:void(0);" onclick="$('.tc_selrestaurant').hide();$('.div_mask').hide();">×</a></div>
    <ul id="projectul">
    	<c:forEach items="${typelist}" var="type">
	    	<li>
	        	<span>${type.typename}</span>
	        	<div class="sel">
	            <c:forEach items="${type.dictlist}" var="dict">
		        <a class="check" onclick="changechoose(this)">
	            <input type="hidden" name="typeid" value="${type.typeid}"/>
	            <input type="hidden" name="dataid" value="${dict.dataid}"/>
	            	选择</a><i>${dict.dataname }</i>
	            </c:forEach>
	            </div>
	            <div class="clear"></div>
	        </li>
    	</c:forEach>
    </ul>
    <div class="tab_list">
       	<table border="0" cellpadding="0" cellspacing="0" width="100%">   
            <tr class="head_td">
            	<td>发布范围</td>
                <td width="80"><a href="javascript:void(0)" onclick="$('.tc_selrestaurant').hide();$('.tc_selrange').show();updatereleaserange();" class="add_btn">编辑范围</a></td>
            </tr>
            <tr>
            	<td colspan="2">
                <ul class="sel_xx li" id="addrangeul">
                	<!-- <li>SHUYI<a class="del"><img src="../images/public/del.png" alt="删除" /></a></li>
                    <li>SHUYI<a class="del"><img src="../images/public/del.png" alt="删除" /></a></li>
                    <li>浦东店<a class="del"><img src="../images/public/del.png" alt="删除" /></a></li> -->
                </ul>
                </td>
            </tr>
        </table>
    </div>
    <div class="tc_btnbox">
   		<a  href="javascript:void(0);" onclick="$('.tc_selrestaurant').hide();$('.div_mask').hide();"  class="bg_gay2">取消</a>
        <a href="javascript:void(0)" onclick="submitdata()"  class="bg_yellow" id="addsubmitdata">确定</a>
        <a href="javascript:void(0)" onclick="updatedata()"  class="bg_yellow" id="updatesubmitdata" style="display: none">确定</a>
    </div>
</div>

<div class="tc_selrange" style="display:none">
	<div class="tc_title"><span>发布范围</span><a href="javascript:void(0);" onclick="$('.tc_selrange').hide();$('.tc_selrestaurant').show();">×</a></div>
    <div class="range">
    	<div class="l_box" id="organizetree2"  style=" width:280px;overflow:hidden;overflow-y:visible;">
    	
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
                        <td width="100">电话</td>
                        <td>操作</td>
                    </tr>
                </table>
            </div>
            <div id="Pagination" style="width:450px;"><div id="userPagination"></div></div><!--动态的获取pagination的宽度赋值给Pagination-->
            <div class="tc_btnbox"><a  href="javascript:void(0);" onclick="$('.tc_selrestaurant').show();$('.tc_selrange').hide();"  class="bg_gay2">取消</a>
            <a href="javascript:void(0)" onclick="addreleaserange()"  class="bg_yellow">确定</a>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>

<script type="text/javascript">
function addreleaserange(){
	var relea = $('#rangeul').html();
	$('#addrangeul').html(relea);
	$('.tc_selrange').hide();
	$('.tc_selrestaurant').show();
}

function updatereleaserange(){
	var relea = $('#addrangeul').html();
	$('#rangeul').html(relea);
	$('.tc_selrestaurant').hide();
	$('.tc_selrange').show();
}

function deletetemplate(obj,templateid,templatename){
	swal({
		title : "",
		text : "确认删除",
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
			url:"<%=request.getContextPath() %>/userbackstage/updateTemplate",
			data:"templateid="+templateid+"&delflag=1&templatename="+templatename,
			success:function(data){
				
			}
		})
		$(obj).parent().parent().remove();
	})
	
}
function submitdata(){
	var alldata = checkSubmitInfo()
	if(alldata.userlist.length > 0){
		saveEverydayReportTemplate(alldata)
	}else{
		alertErrorInfo();
	}
}
function checkSubmitInfo(){
	var alldata={"extendlist":[],"userlist":[]};
	$('#projectul').find("a[class=checked]").each(function(index){
		var extendlist={};
		extendlist['dataid']=$(this).find('input[name=dataid]').val();
		extendlist['typeid']=$(this).find('input[name=typeid]').val();
		alldata.extendlist.push(extendlist); 
	})
	
	$('#addrangeul').find("input[name=organizeid]").each(function(index){
		var organizelist={};
		organizelist['organizeid']=$(this).val();
		alldata.userlist.push(organizelist); 
	})
	$('#addrangeul').find("input[name=userid]").each(function(index){
		var userlist={};
		userlist['userid']=$(this).val();
		alldata.userlist.push(userlist); 
	})
	return alldata;
}
function saveEverydayReportTemplate(alldata){
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath() %>/userbackstage/insertTemplateReport",
		data:"organizeid="+organizeid+"&datalist="+JSON.stringify(alldata),
		success:function(data){
			location.reload();
		}
	})
}
function alertErrorInfo(){
	swal({
		title : "",
		text : "请选择组织架构",
		type : "warning",
		showCancelButton : false,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
	});
	return false;
}
function initadd(){
	if(organizeid == ""){
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
		return false;
	}
	$('#projectul').find("a[class=checked]").attr("class","check");
	
	$('input[value=10]').parent().attr("class","checked").attr("onclick","javascript:void(0)");
	$('input[value=11]').parent().attr("class","checked").attr("onclick","javascript:void(0)");
	$('.tc_selrestaurant').show();
	$('.div_mask').show();
	$('#addrangeul').html("");
	$('#addsubmitdata').show();
	$('#updatesubmitdata').hide();
}
function changechoose(obj){
	if($(obj).attr("class")=="check"){
		$(obj).attr("class","checked");
	}else{
		$(obj).attr("class","check");
	}
}
function chooseorganize(){
	$('.tc_selrestaurant').hide();
	$('.tc_structure').show();
	$('#organizetree1').find('a[class=checked]').attr("class","check");
}


function chooseorganizeuser(){
	var organizeid=$('#organizeid').val();
	if(organizeid==""){
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
	$('#rangeul').html("");
	$('.tc_structure').hide();
	$('.tc_selrange').show();
}
function chooseorganizeuser1(){
	var organizeid=$('#organizeid').val();
	if(organizeid==""){
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
	$('.tc_structure').hide();
	$('.tc_selrange').show();
}
</script>
</body>
</html>
