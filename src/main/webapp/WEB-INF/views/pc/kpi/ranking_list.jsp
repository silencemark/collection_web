<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>个人中心—个人信息</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
	<!-- 日期 -->
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>


<script type="text/javascript">

var param=new Object();
param.organizeid = '${user.organizeid}';
param.companyid ='${user.companyid}';
function pageHelper(num){
	var currentPage = num;
	param.currentPage=currentPage;
	$.ajax({
 		type:'post',
 		url:'/pc/getRankingList',
 		data:param,
 		success:function(data){
 			if(num > 1){
 				allorderNum = (num - 1)*10 - tongnum;
 			}else{
 				allorderNum = (num - 1)*10;
 			}
 			//显示排行信息
 			showRankingInfo(data);
 		}
 })
}


function onloadDate(){
	ranking();
}

function ranking(){
	$.ajax({
 		type:'post',
 		url:'/pc/getRankingList',
 		data:param,
 		success:function(data){
 			//显示排行信息
 			allorderNum = 0;
 			showRankingInfo(data);
 		}
 	});
}

var nodata = '<div class="list_none"><span><i class="yellow">Hi,我是大狮！</i><br>列表空空，KPI排行还没有人哦~</span><b><img src="/userbackstage/images/index/none_msg2.gif" width="293" height="240"></b></div>';

var allorderNum = 0;
var tongnum = 0;
function showRankingInfo(data){
	if(data.status==1){
		swal({
			title : "",
			text : "查询失败",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
	}else{
		$("#tableadd").html("");
		if(data.userlist!=null){
			var temp="";
			var allstarNum = 0;
		if(data.userlist.length > 0){
			$(".list_none").remove();
			for(var i= 0;i<data.userlist.length;i++){
				if(allstarNum > data.userlist[i].allstar || allstarNum == 0){
					
					allorderNum = allorderNum + 1;
					
					temp += KPIRankingDetaiShow(data,allorderNum,i);
					
					allstarNum = data.userlist[i].allstar;
					
				}else if(allstarNum == data.userlist[i].allstar){
					
					if(allorderNum < 4){
						tongnum ++;
						temp += KPIRankingDetaiShow(data,allorderNum,i);
					}else{
						
						allorderNum = allorderNum + 1;
						
						temp += KPIRankingDetaiShow(data,allorderNum,i);
					}
				}
			}
			$('#Pagination').html(data.pager);
		}else{
			$(".list_none").remove();
			$("#tableadd").parent().parent().append(nodata);
		}
			$("#tableadd").html(temp);
		}
	}
}

function KPIRankingDetaiShow(data,allorderNum,i){
	var temp = "";
	var img = "../userbackstage/images/pc_page/start2.png" ;
	if(allorderNum <= 3){
		temp +="<tr><td class=\"rang_num\"><span class=\"rank_bg\"><img src=\"/userbackstage/images/kpi/mun_0"+(allorderNum)+".gif\" height=\"30\"></span></td>";
	}else{
		img = "../userbackstage/images/pc_page/start1.png" ;
		temp +="<tr><td><span style=\"margin-left:15px;\">"+(allorderNum)+"</span></td>";
	}
	temp +="<td><div class=\"img f\"><img src=\""+data.userlist[i].headimage+"\" width=\"30\" height=\"30\" /></div><i class=\"i_name\">"+data.userlist[i].realname+"</i></td>"+
				" <td> &nbsp;&nbsp;<img src=\""+img+"\" width=\"20\" height=\"20\"/> <span class=\"f\">"+data.userlist[i].allstar+"星</span></td>"+
				" <td> &nbsp; &nbsp;<img src=\""+img+"\" width=\"20\" height=\"20\"/> <span class=\"f\">"+data.userlist[i].selfstar+"星</span></td>"+
				" <td> &nbsp;&nbsp;<img src=\""+img+"\" width=\"20\" height=\"20\"/> <span class=\"f\">"+data.userlist[i].overallstar+"星</span></td>"+
		   "</tr>";
	  return temp;
}

function checkSearch(){
	param.organizeid=$("#organizeid").val(); 
	param.monthnum=$("#month").val(); 
	param.year=$("#year").val(); 
	if(param.monthnum==""){
		delete param.monthnum;
	}
	param.currentPage = 1;
	onloadDate();
}

function checkRule(){
	location.href="<%=request.getContextPath() %>/pc/ruleList?organizeid="+param.organizeid;
}

$(document).ready(function(){
	$('.kpi_li').find("li").attr('class','');
	$('#rule').attr('class','active');
	  var nowtime = new Date();
	  var year = nowtime.getFullYear();
	  var month = padleft0(nowtime.getMonth() + 1) ;
	  
	  param.monthnum=month; 
	  param.year=year; 
	  
	  
	  var numberYear = year-100;
	  var temp = "";
	  for(year;year > numberYear;year--){
		  temp += "<option value=\""+year+"\">"+year+"年</option>";
	  }
	  $("#year").html(temp);
	  var tempmonth="";
	  for(var i= 1 ;i <= 12;i++){
		  if(i==month){
			  tempmonth += "<option selected=\"selected\" value=\""+padleft0(i)+"\">"+i+"月</option>";
		  }else{
			  tempmonth += "<option value=\""+padleft0(i)+"\">"+i+"月</option>";
		  }
		 
	  }
	  $("#month").append(tempmonth);
	  
	  
});


/*----------------------------------------组织架构-----------------------------------------------*/

$.ajax({
	type:"post",
	dataType:"json",
	url:"/pc/getPCOrganize",
	success:function(data){
		if(data.status == 0){
			var organizelist = data.organizelist;
			showOrganize2(organizelist,data.compandyname);
		}
	}
})
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

//选择的组织的id
function callbackfunc1(orgid,orgname,datacode){
	$('#change_organizeid').val(orgid);
	$('#change_organizename').val(orgname);
	$('#change_datacode').val(datacode);
}

function changeshopp(){
	$('#organizename').text($('#change_organizename').val());
	$('#organizeid').val($('#change_organizeid').val());
	close_open_organizeDiv();
}

function padleft0(obj) {
    return obj.toString().replace(/^[0-9]{1}$/, "0" + obj);
}
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>KPI星值排行</span><a href="javascript:void(0)"  onclick="checkRule()">规则</a></div>
        <div class="page_tab">
            <div class="sel_box">
            	<input type="hidden" id="organizeid" value="${user.organizeid }"/>
            	 <span class="sel" style="float:left;padding-left:0px; margin-right:5px;" id="organizename">${user.organizename }</span>
            	 <span class="sel" onclick="close_open_organizeDiv()" style="float:left; width:70px; padding-left:0px; background-color:#ff9b30; color:#fff;">选择组织</span>
            	 <select class="sel" id="year"><option value="">年</option></select>
            	  <select class="sel" id="month"><option value="">全年</option></select>
                 <input type="button" value="" class="find_btn"  onclick="checkSearch()" />
                <div class="clear"></div>
            </div>
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">
                      <tr class="head_td">
                        <td width="22%">排名</td>
                        <td width="22%">姓名</td>
                        <td width="22%">总星值</td>
                        <td width="22%">岗位星值</td>
                        <td>综合星值</td>
                    </tr>
                    <tbody id="tableadd">
                    	
                    </tbody>
                </table>
            </div>
                	 <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination-->
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
