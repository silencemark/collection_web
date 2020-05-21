<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>人事轨迹</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organizeRangeRedioPC.js"></script>
</head>
<script type="text/javascript">
function exportexcel(){
	$.ajax({
        type: "POST",
        url: "<%=request.getContextPath() %>/pc/exportEmployeeKpi",
        data: $('#searchform').serializeArray(),
        success: function(data){
        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
        }
    });
}
$(document).ready(function(){
	$('.restaurant_li').find("li").attr('class','');
	$('#employee').attr('class','active');
	$('#homepage').parent().find("li").attr('class','');
	$('#homepage').attr('class','active');
})
function callbackfunc1(organizeid){
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath() %>/userbackstage/getUserByOrganize",
		data:"organizeid="+organizeid,
		success:function(data){
			
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
					if(data.userlist[i].userid != '${userInfo.userid}'){
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
                        temp+="<td><a href=\"javascript:void(0)\" onclick=\"adduser(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"','"+data.userlist[i].headimage+"')\" class=\"link\">选择</a></td>"+
                    "</tr>";
					}
				}
				$('#usertable').append(temp);
			}
		}
	})
}
function searchuser(){
	var searchrealname=$('#searchrealname').val();
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath() %>/userbackstage/getSearchUserByName",
		data:"searchrealname="+searchrealname,
		success:function(data){
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
					if(data.userlist[i].userid != '${userInfo.userid}'){
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
	                        temp+="<td><a href=\"javascript:void(0)\" onclick=\"adduser(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"','"+data.userlist[i].headimage+"')\" class=\"link\">选择</a></td></tr>";
					}
				}
				$('#usertable').append(temp);
			}
		}
	})
}
function adduser(obj,userid,realname,headimage){
	$('.div_mask').hide();
	$('.tc_selrange').hide();
	$('#searchrealname').val(realname);
	$('input[name=headimage]').val(headimage);
	$('input[name=userid]').val(userid);
}
function onloadData(){
	//初始化 yearul 年份 
	var nowdate = new Date();
	nowyear=nowdate.getFullYear();
	var yeartemp="<option value=\"\"  >请选择</option>";
	for(var k=0;k < 100 ;k++){
			yeartemp+="<option value=\""+(nowyear-k)+"\"  >"+(nowyear-k)+"年</option>";
	}
	$('select[name=year]').html(yeartemp);
	
	$("select[name=year]").find("option[value=${map.year}]").attr("selected",true);
	$("select[name=monthnum]").find("option[value=${map.monthnum}]").attr("selected",true);
	
}
function locationurl(url){
	$('#searchform').attr("action",url);
	$('#searchform').submit();
}
</script>
<body onload="onloadData()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>人事轨迹</span><a href="javascript:void(0)" onclick="exportexcel();">导出</a></div>
        <div class="page_tab">  
        	<form action="<%=request.getContextPath() %>/pc/getPathKpiStarList" method="post" id="searchform">     	
	            <div class="sel_box">
	            	<input type="hidden" name="userid" value="${map.userid}">
	            	<input type="hidden" name="headimage" value="${map.headimage}">
	                <input type="text" class="text" name="searchrealname" id="searchrealname" onclick="$('.tc_selrange').show();$('.div_mask').show()" readonly="readonly" value="${map.searchrealname}"/>
	                
	                <select class="sel" name="year">
	                	<option></option>
	                </select>
	            	<select class="sel" name="monthnum">
	            		<option value="">全年</option>
	                	<option value="01">01月</option>
	                	<option value="02">02月</option>
	                	<option value="03">03月</option>
	                	<option value="04">04月</option>
	                	<option value="05">05月</option>
	                	<option value="06">06月</option>
	                	<option value="07">07月</option>
	                	<option value="08">08月</option>
	                	<option value="09">09月</option>
	                	<option value="10">10月</option>
	                	<option value="11">11月</option>
	                	<option value="12">12月</option>
	                </select>
	                <input type="submit" class="find_btn" value="" />
	                <div class="clear"></div>
	            </div>
            </form>
            <div class="rsgj_nav">
                <ul>
                    <li class="active"><a href="javascript:void(0)" onclick="locationurl('<%=request.getContextPath() %>/pc/getPathKpiStarList');">KPI星级考核</a></li>
                    <li><a href="javascript:void(0)" onclick="locationurl('<%=request.getContextPath() %>/pc/getPathRewardList');">奖励记录</a></li>
                    <li><a href="javascript:void(0)" onclick="locationurl('<%=request.getContextPath() %>/pc/getPathPunishList');">处罚记录</a></li>
                </ul>
            </div>
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr class="head_td">
                    	<td class="20%">姓名</td>
                        <td width="20%">考核时间</td>
                        <td width="20%">总星值</td>
                        <td width="20%">岗位星值</td>
                        <td>综合星值</td>
                    </tr>
                    <c:forEach items="${datalist}" var="item">
	                    <tr>
	                    	<td><div class="img f"><img src="${map.headimage}" width="30" height="30" /></div><i class="i_name">${map.searchrealname}</i></td>
	                    	<td>${item.createtime}</td>
	                        <td>${item.sumstar}</td>
	                        <td>${item.selfstar}</td>
	                        <td>${item.overallstar}</td>
	                    </tr>
                    </c:forEach>
                 </table>
                 <c:if test="${fn:length(datalist) == 0 }">
                		<div class="list_none"><span><i class="yellow">Hi,我是大狮！</i><br>列表空空，还没有KPI星级考核单呢~</span><b><img src="<%=request.getContextPath() %>/userbackstage/images/index/none_msg2.gif" width="293" height="240"></b></div>
                </c:if>
            </div>
            <div id="Pagination" style="width:450px;">${pager}</div>
        </div>
    </div>
    <div class="clear"></div>
</div>
<div class="div_mask" style="display:none;"></div>
<div class="tc_selrange" style="display:none;">
	<div class="tc_title"><span>选择审批人</span><a href="javascript:void(0);" onclick="$('.tc_selrange').hide();$('.div_mask').hide();">×</a></div>
    <div class="range">
    	<div class="l_box" id="organizetree2" style="overflow:hidden;overflow-y:visible;">
    	
        </div>
        <div class="r_box">
        	<div class="sel_xx">
                <div class="sel_box">
                    <input type="text" class="text" placeholder="请输入姓名" id="searchrealname"/>
                    <input type="button" class="find_btn" value="" onclick="searchuser()" />
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
            <%-- <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination--> --%>
            <div class="tc_btnbox"><a  href="javascript:void(0);" onclick="$('.div_mask').hide();$('.tc_selrange').hide();"  class="bg_gay2">取消</a>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>

</body>
</html>