<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>每日报表详情</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/showcomment.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/relaycomment.js"></script>

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
})



function onloadData(){
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getEverydayReportInfo?reportid=${reportInfo.reportid}',
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
				var temp="";
				if(data.reportInfo.extendlist.length > 0){
					//先得到所有的餐别id
					var meallist=new Array();
					for(var i=0;i < data.reportInfo.extendlist.length;i++){
						if(data.reportInfo.extendlist[i].type==1){
							var status=0;
							for(var j=0;j < meallist.length;j++){
								if(data.reportInfo.extendlist[i].mealdataname==meallist[j]){
									status=1;
									break;
								}
							}
							if(status==0){
								meallist[meallist.length]=data.reportInfo.extendlist[i].mealdataname;
							}
						}
					}
					//餐别显示
					var mealtemp="";
					for(var j=0;j < meallist.length;j++){
						mealtemp+="<tr>"+
                    	"<td class=\"l_name2\">"+meallist[j]+"</td>"+
                        "<td class=\"mx_tab\">"+
                        	"<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"mx_td\" mealname=\""+meallist[j]+"\">"+

                            "</table>"+
                        "</td>"+
                    "</tr>";
                    }
					$('#alldata').before(mealtemp);
					
					//得到今日统计
					for(var j=0;j < meallist.length;j++){
						var todaytemp="<tr class=\"gray_bg\">"+
                    	"<td colspan=\"3\"><i class=\"gray1\">今日统计</i></td>"+
                        "</tr>"+
                        "<tr>";
                        for(var i=0;i < data.reportInfo.extendlist.length;i++){
							if(data.reportInfo.extendlist[i].typeid==3){
								if(data.reportInfo.extendlist[i].mealdataname==meallist[j]){
									if(data.reportInfo.extendlist[i].dataid==10 || data.reportInfo.extendlist[i].dataid==11 ){
										todaytemp+="<td><i class=\"gray2\">"+data.reportInfo.extendlist[i].dataname+"：</i>￥"+data.reportInfo.extendlist[i].content+"元</td>";
									}else{
										todaytemp+="<td><i class=\"gray2\">"+data.reportInfo.extendlist[i].dataname+"：</i>"+data.reportInfo.extendlist[i].content+"</td>";
									}
								}
							}
							
						}
						todaytemp+="</tr>";
						$("table[mealname="+meallist[j]+"]").append(todaytemp);
					}
					
					
					//得到明细
					for(var j=0;j < meallist.length;j++){
						var todaytemp="<tr class=\"gray_bg\">"+
	                    	"<td colspan=\"3\"><i class=\"gray1\">收入详细</i></td>"+
	                        "</tr>"+
	                        "<tr>";
						for(var i=0;i < data.reportInfo.extendlist.length;i++){
							if(data.reportInfo.extendlist[i].typeid==2){
								if(data.reportInfo.extendlist[i].mealdataname==meallist[j]){
									if(typeof(data.reportInfo.extendlist[i].content)=="undefined"){
										todaytemp+="<td><i class=\"gray2\">"+data.reportInfo.extendlist[i].dataname+"：</i></td>";
									}else{
										todaytemp+="<td><i class=\"gray2\">"+data.reportInfo.extendlist[i].dataname+"：</i>￥"+data.reportInfo.extendlist[i].content+"</td>";
									}
								}
							}
							
						}
						todaytemp+="</tr>";
						$("table[mealname="+meallist[j]+"]").append(todaytemp);
					}
					

					var remarkList=getRemarkList(data.reportInfo.extendlist,2);
					
					//得到今日培训情况统计（例会）
					var todaytemp="<tr>"+
                    	"<td class=\"l_name2\">今日培训情况</td>"+
                        "<td class=\"mx_tab\">";
                        
                    for(var j=0;j<remarkList.length;j++){
						todaytemp+="<div class=\"div_pxqk2\">";
					    var num=1;
						for(var i=0;i < data.reportInfo.extendlist.length;i++){
							if(data.reportInfo.extendlist[i].remark==remarkList[j]  && data.reportInfo.extendlist[i].type==2){
								todaytemp+="<i><em>"+data.reportInfo.extendlist[i].dataname+"：</em>"+data.reportInfo.extendlist[i].content+"</i>";
								if(num%2==0){
									todaytemp+="<div class=\"clear\"></div>";
							    }
							    num++;
							}
						}
						if(num%2==0){
							todaytemp+="<div class=\"clear\"></div>";
						}
						todaytemp+="</div>";
					}
					todaytemp+="</td>"+
                	"</tr>";
					$('#alldata').before(todaytemp);
					
					remarkList=getRemarkList(data.reportInfo.extendlist,3);
					
					//得到今日出勤情况统计
					var attendancetemp="<tr>"+
	                	"<td class=\"l_name2\">今日出勤情况</td>"+
	                    "<td class=\"mx_tab\">";
					for(var j=0;j<remarkList.length;j++){
						attendancetemp+="<div class=\"div_pxqk2\">";
					    var num=1;
						for(var i=0;i < data.reportInfo.extendlist.length;i++){
							if(data.reportInfo.extendlist[i].remark==remarkList[j] && data.reportInfo.extendlist[i].type==3){
								attendancetemp+="<i><em>"+data.reportInfo.extendlist[i].dataname+"：</em>"+data.reportInfo.extendlist[i].content+"</i>";
								if(num%2==0){
									attendancetemp+="<div class=\"clear\"></div>";
							    }
							    num++;
							}
						}
						if(num%2==0){
							attendancetemp+="<div class=\"clear\"></div>";
						}
						attendancetemp+="</div>";
					}
					attendancetemp+="</td>"+
                	"</tr>";
					$('#alldata').before(attendancetemp);
					
					remarkList=getRemarkList(data.reportInfo.extendlist,4);
					//今日主要客情
					var todaycustomer="<tr>"+
                    	"<td class=\"l_name2\">今日主要客情</td>"+
                        "<td>";
					for(var j=0;j<remarkList.length;j++){
						for(var i=0;i < data.reportInfo.extendlist.length;i++){
							if(data.reportInfo.extendlist[i].remark==remarkList[j]  && data.reportInfo.extendlist[i].type==4){
								todaycustomer+="<span>"+data.reportInfo.extendlist[i].content+"</span>";
							}
						}
					}
					todaycustomer+="</td></tr>";
					$('#alldata').before(todaycustomer);
					
					remarkList=getRemarkList(data.reportInfo.extendlist,5);
					//需沟通事项
					var communication="<tr>"+
                	"<td class=\"l_name2\">需沟通事项</td>"+
                    "<td>";
                   	for(var j=0;j<remarkList.length;j++){
						for(var i=0;i < data.reportInfo.extendlist.length;i++){
							if(data.reportInfo.extendlist[i].remark==remarkList[j]  && data.reportInfo.extendlist[i].type==5){
								communication+="<span>"+data.reportInfo.extendlist[i].content+"</span>";
							}
						}
					}
					communication+="</td></tr>";
					$('#alldata').before(communication);
				}
				
			}
		}
	})
}

function getRemarkList(extendlist,type){
	//先得到所有的今日培训 增添remark列表
	var todaylist=new Array();
	for(var i=0;i < extendlist.length;i++){
		if(extendlist[i].type==type){
			var status=0;
			for(var j=0;j < todaylist.length;j++){
				if(extendlist[i].remark==todaylist[j]){
					status=1;
					break;
				}
			}
			if(status==0){
				todaylist[todaylist.length]=extendlist[i].remark;
			}
		}
	}
	return todaylist;
}
</script>
<body onload="onloadData()">
<jsp:include page="../../top.jsp"></jsp:include>
<div class="main_page">
	<jsp:include page="../../left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList">企业信息列表</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyInfo?companyid=${map.companyid}">公司信息</a><i>/</i><a href="javascript:void(0)" onclick="window.history.go(-1)">报表管理/营业额</a><i>/</i><span>每日报表详情</span></p></div>
    	</div>
    	
        <div class="page_tab">            
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr class="last_td">
                    	<td class="l_name">店面</td>
                        <td>${reportInfo.organizename }</td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name">收入统计</td>
                        <td>今日营业额&nbsp;&nbsp;:&nbsp;&nbsp;￥<i style="margin-right: 20px" id="turnoveri">${reportInfo.turnover}元</i>今日客流量&nbsp;&nbsp;:&nbsp;&nbsp;<i style="margin-right: 20px" id="guestflowi">${reportInfo.guestflow}</i>今日人均消费&nbsp;&nbsp;:&nbsp;&nbsp;￥<i id="consumptioni">${reportInfo.consumption}元</i></td>
                    </tr>
                    <tr id="alldata">
                    	<td class="l_name">创建人</td>
                        <td class="img_td"><div class="img f"><img src="${reportInfo.createheadimage}" width="30" height="30" /></div><i class="i_name">${reportInfo.createname }</i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">统计时间</td>
                        <td>${reportInfo.statisticaltime}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">填写时间</td>
                        <td>${reportInfo.createtime}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">审核人</td>
                        <td class="img_td"><div class="img f"><img src="${reportInfo.copyheadimage}" width="30" height="30" /></div><i class="i_name">${reportInfo.examinename}</i></td>
                    </tr>
                     <tr>
                    	<td class="l_name">抄送人</td>
                        <td class="img_td">
                        	<c:forEach items="${reportInfo.ccuserlist }" var="map">
                        		<div class="img f"><img src="${map.headimage }" width="30" height="30" /></div><i class="i_name" >${map.realname }</i>
                        	</c:forEach>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">转发人</td>
                        <td class="img_td">
                        	<i class="i_name" id="sendname"></i>
                        </td>
                    </tr>
                    <c:choose>
                    	<c:when test="${reportInfo.examineuserid==userInfo.userid && reportInfo.status==0}">
	                    <tr class="none_border">
	                    	<td class="l_name2">审核人意见</td>
	                        <td><textarea class="text_area" placeholder="请输入抄送人意见，最多允许输入800字符" maxlength="800"id="opinion"></textarea></td>
	                    </tr>
	                    <tr>
	                    	<td>&nbsp;</td>
	                        <td><a href="javascript:void(0)" onclick="examineorder()" class="a_btn bg_yellow">发送</a>
	                        <a href="<%=request.getContextPath() %>/pc/getEverydayReportList" class="a_btn bg_gay2">取消</a></td>
	                    </tr>
                    	</c:when>
                    	<c:otherwise>
                    	<tr class="none_border">
	                    	<td class="l_name2">审核人意见</td>
	                        <td>${reportInfo.opinion}</td>
	                    </tr>
                    	</c:otherwise>
                    </c:choose>
                </table>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>
</script>
</body>
</html>