<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>每日报表详情</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
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

var relayiduserid = '${userInfo.userid}';
var relayidcompanyid ='${userInfo.companyid}';
var relayid = '${reportInfo.reportid}';
var relaytype = 21;

$(document).ready(function(){
	$('.restaurant_li').find("li").attr('class','');
	$('#dailyreport').attr('class','active');
	$('#homepage').parent().find("li").attr('class','');
	$('#homepage').attr('class','active');
})

function onloadData(){
	onloadDate();
	//标记为已读
	readstatus('${reportInfo.reportid}','${userInfo.userid}');
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
function examineorder(){
	if($('#opinion').val()==""){
		swal({
			title : "",
			text : "请输入抄送意见",
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
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/app/copyEverydayReport?reportid=${reportInfo.reportid}&userid=${userInfo.userid}&opinion='+$('#opinion').val(),
		success:function(data){
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
					changeIsread('${reportInfo.createid}','${reportInfo.reportid}');
					var title="${reportInfo.examinename}对您发布的每日报表，填写了抄送意见,请及时查看";
					var url="/restaurant/dailyreport_detail.html?reportid=${reportInfo.reportid}";
					pushMessage('${reportInfo.createid}',title,url);
					goBackPage();
				});
			}
	})
}

</script>
<body onload="onloadData()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>每日报表详情</span><a href="javascript:void(0)" onclick="goBackPage()" class="back">返回</a>
    	<!-- <a href="javascript:void(0)" onclick="exportexcel();">导出</a> -->
   	  <c:if test="${tourstoreinfo.copyuserid!=userInfo.userid || tourstoreinfo.status!=0}">
   	  <a href="javascript:void(0)"  onclick="showForwardDiv()" >转发</a>
   	  </c:if>
    	</div>
    	
        <div class="page_tab2">            
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
        
        <div class="comment">
       	<div class="name">共有<i class="yellow" id="num"></i>条评论</div>
            <div class="text_box" >
            <c:if test="${reportInfo.examineuserid!=userInfo.userid || reportInfo.status!=0}">
            	<b><textarea placeholder="请输入评论内容，最多允许输入800字符" maxlength="800" id="content_text1"></textarea></b>
                <span><input type="button" value="评论" onclick="checkComment(1)" /></span>
            </c:if>
	           	<input type="hidden" id="parentid" />
				<input type="hidden" id="parentuserid" />
            </div>
            <div class="ul_list" id="ul_list">
            	
                
            </div>
            <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination-->
            <div class="clear"></div>
        </div>
        
    </div>
    <div class="clear"></div>
</div>
<script type="text/javascript">
//评论分页
function pageHelper(num){
	var param =new  Object();
	var currentPage = num;
	param.currentPage=currentPage;
	param.orderid='${reportInfo.reportid}';
	param.resourcetype= 21;
	pageList(param);
}

//评论列表
function onloadDate(){
	getSendName('${map.forwarduserid}',"#sendname");
	var param =new  Object();
	param.orderid='${reportInfo.reportid}';
	param.resourcetype=21;
	addcommentAjax(param);//显示评论列表		
}

//新增评论
function checkComment(row){
	var param = new Object();
	param.userid = '${userInfo.userid}';
	param.resourceid = '${reportInfo.reportid}';
	param.resourcetype =21;
	showComment(row,param,'/pc/getDailyreportInfo?reportid=${reportInfo.reportid}&resourcetype=21');//添加评论信息
}

</script>
</body>
</html>