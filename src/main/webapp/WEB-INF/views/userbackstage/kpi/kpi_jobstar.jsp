<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>KPI星级考核岗位星值-使用方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/sweetalert/dist/sweetalert-dev.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/kpi/kpi_star.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organize2.js"></script>
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
		$('#nav_star${map.type}').attr('class','active');$('#nav_star${map.type}').parent().parent().show();
	})
</script>
</head>

<body>
<jsp:include page="../top.jsp" flush="true"></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" flush="true"></jsp:include>
	<input id="type" type="hidden" value="${map.type }"/>
	<input id="typeid" type="hidden" value="${map.typeid }"/>
	<input id="managerole" type="hidden" value="${map.managerole }"/>
	<input id="userid" type="hidden" value="${user.userid }"/>
	<input id="companyid" type="hidden" value="${user.companyid }"/>
	
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">KPI星级考核</a><i>/</i><span>${map.typename }</span></p></div>  
    <div class="page_tab m_top">
        <div class="tab_name">
        	<span class="gray1">${map.typename }</span>
        	<a href="javascript:void(0)" onclick="queryTemplateProject_div('first');">新建</a>
        	<a href="javascript:void(0)" onclick="updateTemplateCloseOrOpenDiv('first');organizeTemplateList();">编辑模板</a>
        </div>
        <div class="worksheet">
        	<div class="l_tree" style="width:30%; max-height:460px; overflow:auto;" id="new_organizetree">
            	
            </div>
            <div class="r_list" style="width:69%; min-height:460px;">
            	<div class="tab_list">
                	<table border="0" cellpadding="0" cellspacing="0" width="100%" id="template_list">
<!--                     	<tr> -->
<!--                         	<td width="76%">徐家汇店-岗位星值</td> -->
<!--                             <td><a href="#" class="link">修改</a><a href="#" class="del">删除</a></td> -->
<!--                         </tr> -->
                    </table>
                </div>
                <div id="Pagination" style="width:450px;"></div>
            </div>
            <div class="clear"></div>
        </div>
    </div>
</div>

<div class="div_mask" style="display: none;" id="div_mask"></div>

<div class="tc_worksheet" style="display:none; max-height:550px; overflow:auto; width:750px;" id="addTemplate">
	<input type="hidden" id="update_templateid_project" />
	<div class="tc_title"><span>${map.typename }模板</span><a href="javascript:void(0)" onclick="queryTemplateProject_div()">×</a></div>
   	<div class="mode" id="tempmode">
    	<!-- <a>保安岗位星级模板</a>
        <a>保安岗位星级模板</a>
        <a>保安岗位星级模板</a>
        <a>保安岗位星级模板</a>
        <a>保安岗位星级模板</a>
        <a>保安岗位星级模板</a>
        <a class="two" style="background-color:#ff9b30;color:#fff;">保安岗位保安岗位星级模板</a>
        <div class="clear"></div> -->
    </div>
    <div class="tab_list">
    	<table border="0" cellpadding="0" cellspacing="0" width="100%">
        	<tr class="head_td">
            	<td>检查项目</td>
                <td width="100"><a href="javascript:void(0)" onclick="queryTemplateProject_div();addTemplate_div();" class="add_btn">添加检查项</a></td>
            </tr>
         </table>
        <div>
        	<table border="0" cellpadding="0" cellspacing="0" width="100%">
            	<tbody  id="addprojectlist">
            		
            	</tbody>
        	</table>
        </div>
       	<table border="0" cellpadding="0" cellspacing="0" width="100%">   
            <tr>
            	<td><span class="gray2">岗位名称</span> <input type="text" class="text" placeholder="请输入岗位名称" id="add_templateName" maxlength="8"/></td>
                <td></td>
            </tr>
            <tr class="head_td">
            	<td>发布范围</td>
                <td width="80"><a href="javascript:void(0)" onclick="queryTemplateProject_div();chagneRange();" class="add_btn">编辑范围</a></td>
            </tr>
            <tr>
            	<td colspan="2">
                <ul class="sel_xx" id="addrangeul">
                	<!-- <li>SHUYI<a class="del"><img src="../images/public/del.png" alt="删除" /></a></li>
                    <li>SHUYI<a class="del"><img src="../images/public/del.png" alt="删除" /></a></li>
                    <li>浦东店<a class="del"><img src="../images/public/del.png" alt="删除" /></a></li> -->
                </ul>
                </td>
            </tr>
        </table>
    </div>
    <div class="tc_btnbox" id="sureAndClose"><a href="javascript:void(0)" onclick="queryTemplateProject_div();" class="bg_gay2">取消</a>
    	<a href="javascript:void(0)" onclick="addProjectAndTemplate()" id="addTemplate_submit" class="bg_yellow">确定</a>
    	<a href="javascript:void(0)" onclick="updateReleaseRange()" style="display: none;" id="updateTemplate_submit" class="bg_yellow">确定</a>
    </div>
</div>


<div class="tc_worksheet2" style="display:none;" id="addTemplate_div">
	<div class="text"><input type="text" placeholder="请输入检查项目名称" id="add_templatename" maxlength="10"/></div>
    <div class="tc_btnbox"><a href="javascript:void(0)" onclick="addTemplate_div();queryTemplateProject_div();" class="bg_gay2">取消</a>
    <a href="javascript:void(0)" onclick="addTemplateName()"  class="bg_yellow">确定</a></div>
</div>

<div class="tc_worksheet2" style="display:none;" id="updateprojectname_div">
	<div class="text"><input type="text" placeholder="请输入检查项目名称" id="update_projectname" maxlength="10"/></div>
    <div class="tc_btnbox"><a href="javascript:void(0)" onclick="updateTemplateProjectName_div();queryTemplateProject_div();" class="bg_gay2">取消</a>
    <a href="javascript:void(0)" onclick="" id="updateTemplateProject" class="bg_yellow">确定</a></div>
</div>

<div class="tc_selrange" style="display:none" id="tc_selrange">
	<div class="tc_title"><span>填写范围</span><a href="javascript:void(0);" onclick="chagneRange();queryTemplateProject_div();">×</a></div>
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
            <div class="tc_btnbox"><a  href="javascript:void(0);" onclick="chagneRange();queryTemplateProject_div();"  class="bg_gay2">取消</a>
            <a href="javascript:void(0)" onclick="submitdata()"  class="bg_yellow" id="addsubmitdata">确定</a>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>


<!-- 编辑模板信息 -->
<div class="tc_worksheet" style="display:none; max-height:600px; overflow:auto; width:750px;" id="updateTemplate">
	<input type="hidden" id="update_templateid_project" />
	<div class="tc_title"><span>编辑${map.typename }模板</span><a href="javascript:void(0)" onclick="updateTemplateCloseOrOpenDiv()">×</a></div>
   	<div class="mode" id="update_tempmode">
    	
    </div>
    <div class="tab_list">
    	<table border="0" cellpadding="0" cellspacing="0" width="100%">   
            <tr>
            	<td><span class="gray2">模板名称</span> <input type="text" class="text" placeholder="请输入模板名称" id="update_templateName" maxlength="8"/></td>
                <td></td>
            </tr>
        </table>
    	<table border="0" cellpadding="0" cellspacing="0" width="100%">
        	<tr class="head_td">
            	<td>检查项目</td>
                <td width="100"><a href="javascript:void(0)" onclick="updateTemplateCloseOrOpenDiv();updateaddtemplateprojectCloseOrOpen();" class="add_btn" style="background-color:#ff9b30;">添加检查项</a></td>
            </tr>
         </table>
        <div>
        	<table border="0" cellpadding="0" cellspacing="0" width="100%">
            	<tbody  id="updateprojectlist">
            		
            	</tbody>
        	</table>
        </div>
    </div>
    <div class="tc_btnbox"><a href="javascript:void(0)" onclick="updateTemplateCloseOrOpenDiv()" class="bg_gay2">退出</a>
    	<a href="javascript:void(0)" onclick="updatetemplatename()" class="bg_yellow">保存</a>
    </div>
</div>

<div class="tc_worksheet2" style="display:none;" id="updateTemplate_div">
	<div class="text"><input type="text" placeholder="请输入检查项目名称" id="update_addtemplatename" maxlength="10"/></div>
    <div class="tc_btnbox"><a href="javascript:void(0)" onclick="updateaddtemplateprojectCloseOrOpen();updateTemplateCloseOrOpenDiv();" class="bg_gay2">取消</a>
    <a href="javascript:void(0)" onclick="update_addcheckProject()"  class="bg_yellow">确定</a></div>
</div>

<script type="text/javascript">
	/*树状菜单的js*/
	var whiteHeight=0;
	$(document).ready(function(){	
		
		$(".tree_list .white_line").each(function() {		
			whiteHeight = $(this).parent().height();
			whiteHeight = whiteHeight - 21;
            $(this).height(whiteHeight) ;
        });
	});

	
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
</body>
</html>
