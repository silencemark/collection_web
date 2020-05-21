<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>顾客满意度</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
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
var orgid = "";
var orgname = "";
function callbackfunc(orgid1,orgname1){
	orgid = orgid1;
	orgname = orgname1;
}

function changeshopp(){
	$('.tc_structure').hide();
	$('.div_mask').hide();
	$('#organizeid').val(orgid);
	$('#organizename').val(orgname);
}

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
                    	"<span class=\"bg_hidden\" onclick=\"changenextul(this,'"+list[i].isonlyread+"','')\"><i id=\"companyname\">"+list[i].organizename+"</i></span>"+
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
		var orgname = $(obj).find("i").text();
		callbackfunc(organizeid,orgname);
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

</script>
<body>
<jsp:include page="../top.jsp"></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList">企业信息列表</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyInfo?companyid=${map.companyid}">公司信息</a><i>/</i><span>顾客满意度</span></p></div>
        <div class="page_tab">
            <div class="sel_box">
            	<form action="<%=request.getContextPath() %>/managebackstage/getManageStoreEvaluateList" id="searchform" method="post">
                <input type="hidden" name="organizeid" value="${map.organizeid}" id="organizeid">
                <input type="hidden" name="companyid" value="${map.companyid}">
	            <input type="text" class="text" name="organizename" id="organizename" onclick="$('.tc_structure').show();$('.div_mask').show();" readonly="readonly" value="${map.organizename}"/>
                <input type="text" class="text_time" placeholder="请选择时间" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${map.createtime}" name="createtime"/>
                <input type="submit" value="" class="find_btn1"  />
                </form>
                <div class="clear"></div>
            </div>
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr class="head_td">
                        <td width="50%">评论时间</td>
                        <td width="50%">综合得分</td>
                    </tr>
           
                    <c:forEach items="${datalist }" var="item">
                    	<tr>
                    	<td colspan="2" class="appraise_td">
                        	<div class="appraise">
                            	<div class="xx"><span>${item.createtime}</span><span class="num">${item.averagescore}分</span><i></div>
                                <div class="box">
                                <div class="star_list">
                                	<div class="star"><span>口味得分</span><b>
                                	<c:forEach begin="0" end="${item.tastescore-1}" >
                                		<img src="../userbackstage/images/pc_page/start2.png" width="20" height="20" />
                                	</c:forEach>
                                	<c:forEach begin="${item.tastescore}" end="4">
                                		<img src="../userbackstage/images/pc_page/start3.png" width="20" height="20" />
                                	</c:forEach></b></div>
                                    <div class="star"><span>环境得分</span><b><c:forEach begin="0" end="${item.environmentscore-1}">
                                		<img src="../userbackstage/images/pc_page/start2.png" width="20" height="20" />
                                	</c:forEach>
                                	<c:forEach begin="${item.environmentscore}" end="4">
                                		<img src="../userbackstage/images/pc_page/start3.png" width="20" height="20" />
                                	</c:forEach></b></div>
                                    <div class="star"><span>服务得分</span><b><c:forEach begin="0" end="${item.servicescore-1}">
                                		<img src="../userbackstage/images/pc_page/start2.png" width="20" height="20" />
                                	</c:forEach>
                                	<c:forEach begin="${item.servicescore}" end="4">
                                		<img src="../userbackstage/images/pc_page/start3.png" width="20" height="20" />
                                	</c:forEach></b></div>
                                    <div class="star last"><span>价格得分</span><b><c:forEach begin="0" end="${item.pricescore-1}">
                                		<img src="../userbackstage/images/pc_page/start2.png" width="20" height="20" />
                                	</c:forEach>
                                	<c:forEach begin="${item.pricescore}" end="4">
                                		<img src="../userbackstage/images/pc_page/start3.png" width="20" height="20" />
                                	</c:forEach></b></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="span">
                                	<span class="user">${item.name}</span>
                                    <span class="tel">${item.phone}</span>
                                    <span class="desk">${item.seat}</span>
                                    <div class="clear"></div>
                                </div>
                                <div class="txt">${item.opinion}</div>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </c:forEach>
                </table>
            </div>
            <div id="Pagination" style="width:450px;">${pager }</div><!--动态的获取pagination的宽度赋值给Pagination-->
        </div>
    </div>
    <div class="clear"></div>
</div>
<div class="div_mask" style="display: none"></div>
<div class="tc_structure" style="display: none">
	<div class="tc_title"><span>选择组织架构店</span><a href="javascript:void(0);" onclick="$('.tc_structure').hide();$('.div_mask').hide();" >×</a></div>
    <div id="organizetree"  style="overflow:hidden;overflow-y:visible;"></div>
    <div class="tc_btnbox"><a href="javascript:void(0);" onclick="$('.tc_structure').hide();$('.div_mask').hide();"  class="bg_gay2">取消</a>
    <a href="javascript:void(0)" onclick="changeshopp()" class="bg_yellow">确定</a>
    </div>
</div>
</body>
</html>