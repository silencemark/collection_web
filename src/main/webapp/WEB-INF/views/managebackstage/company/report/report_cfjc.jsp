<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>每日报表统计-营业额</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/echarts-all.js"></script>
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

var organizeid='${map.organizeid}'
var organizename='${map.organizename}';
var companyid = '${map.companyid}';
var param = new Object();
function getdata(){
	
	//查询统计数据
	$.ajax({
		type:'post',
		dataType:'json',
		data:"statisticsTime="+$("#statisticsTime").val(),
		url:projectpath+'/app/getReportKitChencheckInfo?organizeid='+organizeid,
		success:function(data){
			if(data.status==0){
				
				var timedataArray = new Array() ;
				var lastdataArray = new Array() ;
				var nowdataArray = new Array() ;
				var nowsumprice=0;
				var lastsumprice=0;
				var nextcount=999;
				for(var i=data.nowmonthlist.length-1;i>0;i--){
					if(data.nowmonthlist[i].sumstar==0){
						nextcount=i;
					}else{
						break;
					}
				}
				var firstcount=-1;
				for(var i=0;i<data.nowmonthlist.length;i++){
					if(data.nowmonthlist[i].sumstar==0){
						firstcount=i;
					}else{
						break;
					}
				}
				
				for(var i=0;i<data.nowmonthlist.length;i++){
					timedataArray[i]=data.nowmonthlist[i].comparetime;
					if(firstcount<i && i<nextcount){
						nowdataArray[i]=data.nowmonthlist[i].sumstar;
						lastsumprice += parseInt(data.nowmonthlist[i].sumstar);
					}
					//nowdataArray[i]=data.nowmonthlist[i].price;
				}
				var nextcount2=999;
				for(var i=data.lastmonthlist.length-1;i>0;i--){
					if(data.lastmonthlist[i].sumstar==0){
						nextcount2=i;
					}else{
						break;
					}
				}
				var firstcount2=-1;
				for(var i=0;i<data.lastmonthlist.length;i++){
					if(data.lastmonthlist[i].sumstar==0){
						firstcount2=i;
					}else{
						break;
					}
				}
				
				for(var i=0;i<data.lastmonthlist.length;i++){
					if(firstcount2<i && i<nextcount2){
						lastdataArray[i]=data.lastmonthlist[i].sumstar;
						nowsumprice += parseInt(data.lastmonthlist[i].sumstar);
					} 
					//lastdataArray[i]=data.lastmonthlist[i].sumstar;
				}
				var nowreportnum = data.nowreportnum;
				var lastreportnum = data.lastreportnum;
				if(lastreportnum > 0){
					nowsumprice = (nowsumprice/lastreportnum).toFixed(1);
				}
				if(nowreportnum > 0){
					lastsumprice = (lastsumprice/nowreportnum).toFixed(1);
				}
				
				option = {
					    title: {
					    	/* text:"总营业额："+nowsumprice+"元\n\n总营业额："+lastsumprice+"元",
					    	textStyle:{
					    		color:'#666',
					    		fontStyle:'normal',
					    		fontWeight:'normal',
					    		fontSize:12,
					    	}, */
					    },
					    tooltip: {
					        trigger: 'axis',
					        axisPointer: {
					            type: 'none'
					        },
					        formatter: function(params,ticket,callback){
					            var res = '';  
					            var date = '';
					            for (var i = 0; i < params.length; i++) {  
					                 date = params[i][1]+"号<br/>";
					                 var firstnum = date.substring(0,1);
					                 if(firstnum == 0){
					                	 date = date.substring(1,date.length);
					                 }
					                 var htm = params[i][0];
					                 htm = htm.substring(0,htm.indexOf("，"));
					                 var money = (params[i][2]+"").replace("-",0);
					                 res += htm+"："+money+"分<br/>";
					            }; 
					            return date+res;  
					        } 
					    },
					    legend: {
					    	orient:'vertical',
					    	data:[data.lastmonth+"月，综合评分："+nowsumprice+"分",data.nowmonth+"月，综合评分："+lastsumprice+"分"],
// 					        data:[data.lastmonth+"月",data.nowmonth+"月"],
					        formatter: '{name}'
					    },
					    grid: {
					        x:50,
					        y:60,
					        y2:40,
					        x2:20,
					    },
					    toolbox: {
					    	show:false,
					        feature: {
					            saveAsImage: {}
					        }
					    },
					    xAxis: {
					        type: 'category',
					        boundaryGap: false,
					        data: timedataArray,
					        splitLine: {
					            show: false,
					        },
					        axisLabel: {
					        	interval: 0,
					            margin:10
		             		},
		             		axisLine:{
		             			lineStyle:{
		             				color:'#333',
		             				width:1,
		             			}
		             		}
					    },
					    yAxis: {
					        type: 'value',
					        splitLine: {
					            show: false
					        },
		             		axisLine:{
		             			lineStyle:{
		             				color:'#333',
		             				width:1,
		             			}
		             		}
					    },
					    series: [
					        {
					            name:data.lastmonth+"月，综合评分："+nowsumprice+"分",
					            type:'line',
					            data:lastdataArray,
					            symbol:'circle'
					        },
					        {
					            name:data.nowmonth+"月，综合评分："+lastsumprice+"分",
					            type:'line',
					            data:nowdataArray,
					            symbol:'circle'
					        }
					    ],
					    color:['#2f4554','#c23531']
					};
				var myChart = echarts.init(document.getElementById("echarts_one"));
				myChart.setOption(option);
			}
		}
	});
	param.currentPage = 1;
	getKitchenList();
}

function getKitchenList(){
	param.companyid = companyid;
	param.organizeid = organizeid;
	param.status = 1;
	param.statisticsTime = $("#statisticsTime").val();
	
	$.ajax({
		url:projectpath+"/app/getKitchenListOnlyOnePC",
		data:param,
		type:"post",
		success:function(resultMap){
			$('#cfjclist').html("");
			$('#Pagination').html(resultMap.pager);
			if(resultMap != undefined && resultMap != null){
				var list = resultMap.inspectlist;
				if(list != null && list.length > 0){
					showData(list);
				}
			}
		}
	});
}

function showData(list){
	if(list != null && list.length > 0){
		var html = "";
		$.each(list,function(i,map){
			html += '<tr>'+
		                '<td>'+map.createname+'</td>'+
		                '<td>'+map.templatename+'</td>'+
		                '<td>'+map.templatename+'</td>'+
						'<td class="green">已处理</td>'+
		                '<td><a href="/managebackstage/getKitchenInfo?inspectid='+map.inspectid+'&forwarduserid='+map.forwarduserid+'" class="link">查看</a></td>'+
		            '</tr>';
		});
		$('#cfjclist').append(html);
	}
}

function pageHelper(num){
	param.currentPage = num;
	getKitchenList();
}

function logical(){
	userInfo.organizeid=$('#organizeid').val();
	userInfo.organizename=$('#organizename').text();
	JianKangCache.setGlobalData("userinfo",userInfo);
	getdata();
}


$(function(){
	$('#organizename').html(organizename);
	
	$('#statisticsTime').val(GetDateStr());
});

function GetDateStr() { 
	var dd = new Date(); 
	var y = dd.getFullYear(); 
	var m = dd.getMonth()+1;//获取当前月份的日期 
	if(parseInt(m)<10){
		m="0"+m;
	}
	return y+"-"+m; 
}
function preMonth(){
	var time = $("#statisticsTime").val();
	var times = time.split("-");
	var month = parseInt(times[1]) - 1;
	var year = parseInt(times[0]);
	if(month <= 0){
		month = 12;
		year = year - 1;
	}else if( month < 10 && month >= 1){
		month = "0"+month;
	}
	time = year+"-"+month;
	$("#statisticsTime").val(time);
	getdata();
}
function nextMonth(){
	var time = $("#statisticsTime").val();
	var times = time.split("-");
	var month = parseInt(times[1]) + 1;
	var year = parseInt(times[0]);
	if(month >= 13){
		month = "0"+1;
		year = year + 1;
	}else if( month < 10 && month >= 1){
		month = "0"+month;
	}
	time = year+"-"+month;
	$("#statisticsTime").val(time);
	getdata();
}

</script>

<body onload="getdata()">
<jsp:include page="../../top.jsp"></jsp:include>
<div class="main_page">
	<jsp:include page="../../left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList">企业信息列表</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyInfo?companyid=${map.companyid}">公司信息</a><i>/</i><span>报表管理/厨房检查</span></p></div>
        <div class="page_tab2">        	
            <div class="shop_sel"><span id="organizename"></span><a href="javascript:void(0)" onclick="$('.tc_structure').show();$('.div_mask').show();">切换店面</a><div class="clear"></div></div>
            <div class="mrbb_nav">
                 <ul>
                    <li><a href="<%=request.getContextPath()%>/managebackstage/getReportIncomeChart?companyid=${map.companyid}">营业额</a></li>
                    <li><a href="<%=request.getContextPath()%>/managebackstage/getReportFlowChart?companyid=${map.companyid}">客流量</a></li>
                    <li><a href="<%=request.getContextPath()%>/managebackstage/getReportConsumptionChart?companyid=${map.companyid}">人均消费</a></li>
                    <li><a href="<%=request.getContextPath()%>/managebackstage/getReportAttendanceChart?companyid=${map.companyid}">出勤人数</a></li>
                </ul>
                <ul>
                    <li><a href="<%=request.getContextPath()%>/managebackstage/getReportPreCheckPage?companyid=${map.companyid}">餐前检查</a></li>
                    <li class="active"><a href="<%=request.getContextPath()%>/managebackstage/getReportKitChencheckPage?companyid=${map.companyid}">厨房检查</a></li>
                    <li><a href="<%=request.getContextPath()%>/managebackstage/getReportLeavePage?companyid=${map.companyid}">离店报告</a></li>
                    <li><a href="<%=request.getContextPath()%>/managebackstage/getReportSatisfyChart?companyid=${map.companyid}">满意度</a></li>
                </ul>
            </div>
            <div class="mrbb_chartbox">
            	<div class="time_sel">
                    <b style="background:#eee;"><i class="pre" onclick="preMonth()">前一天</i><input style="border:0px; background-color:#eee;" readonly="readonly" id="statisticsTime" onchange="getdata()" value="请选择时间"><i onclick="nextMonth()" class="next">后一天</i></b>
                </div>
            	<div id="echarts_one" style="width: 100%;height: 300px;"></div>
            </div>
            
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr class="head_td">
                        <td width="20%">检查人</td>
                        <td width="20%">区域</td>
                        <td width="20%">检查时间</td>
                        <td width="20%">状态</td>
                        <td>操作</td>
                    </tr>
                    <tbody id="cfjclist"></tbody>
                </table>
            </div>
            <div id="Pagination" style="width:450px;"></div><!--动态的获取pagination的宽度赋值给Pagination-->
        </div>
    </div>
    <div class="clear"></div>
</div>

<div class="div_mask" style="display: none"></div>
<div class="tc_structure" style="display: none">
	<div class="tc_title"><span>选择组织架构店</span><a href="javascript:void(0);" onclick="$('.tc_structure').hide();$('.div_mask').hide();" >×</a></div>
	<input type="hidden" id="organizeid" />
	<input type="hidden" id="organizename" />
    <div id="organizetree1"  style="overflow:hidden;overflow-y:visible;"></div>
    <div class="tc_btnbox"><a href="javascript:void(0);" onclick="$('.tc_structure').hide();$('.div_mask').hide();"  class="bg_gay2">取消</a>
    </div>
</div>
<script type="text/javascript">
function chooseorganize(orgid,orgname){
	$('.tc_structure').hide();
	$('.div_mask').hide();
	organizeid=orgid;
	organizename=orgname;
	getdata();
	changeOrganizeid(organizeid,organizename);
}


$(function(){
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/managebackstage/getOrganizeBySystem",
		data:"companyid=${map.companyid}",
		success:function(data){
			var organizelist = data.organizelist;
			showOrganize1(organizelist,data.compandyname);
		}
	})
});
var initorganizeid1="";
function showOrganize1(list,compandyname){
	if(list.length > 0){
		for(var i=0;i<list.length;i++){
			if(list[i].parentid=="" || typeof(list[i].parentid)=="undefined"){
				var temp="<ul class=\"tree_list\">"+
                	"<li id=\"li1_"+list[i].organizeid+"\">"+                    	                    	
                		"<div class=\"gray_line\"></div>"+
                    	"<span class=\"bg_show\" onclick=\"changenextul1(this,'"+list[i].organizeid+"')\">";
						if(list[i].isonlyread != 1){
							temp+="<a href=\"javascript:void(0)\" class=\"check\" onclick=\"changeorganize(this,'"+list[i].organizeid+"','"+list[i].organizename+"')\">选择</a>";
						}
						temp+="<i id=\"companyname\">"+list[i].organizename+"</i></span>"+
                    "</li>"+
                "</ul>";
				initorganizeid1=list[i].organizeid;
				$("#organizetree1").html(temp);
				appendli1(list,list[i].organizeid);
			}
		}
	}
}

function appendli1(list,organizeid){
	var indexnum=0;
	for(var i=0;i<list.length;i++){
		if(list[i].parentid==organizeid){
			var ultemp="";
			if(organizeid==initorganizeid1){
				ultemp+="<ul style=\"display:block;\" id=\"ul1_"+list[i].parentid+"\"></ul>";
			}else{
				ultemp+="<ul style=\"display:none;\" id=\"ul1_"+list[i].parentid+"\"></ul>";
			}
			
			var temp="<li class=\"li_bg\" id=\"li1_"+list[i].organizeid+"\">";
			indexnum++;
			if(list[i].childnum>0){
				temp+="<div class=\"gray_line\" style=\"display:none\"></div><span class=\"bg_hidden\" onclick=\"changenextul1(this,'"+list[i].organizeid+"')\">";
				
			}else{
				temp+="<span class=\"bg_last\" onclick=\"changenextul1(this,'"+list[i].organizeid+"')\">";
			}
			
			if(list[i].isonlyread != 1){
				temp+="<a href=\"javascript:void(0)\" class=\"check\" onclick=\"changeorganize(this,'"+list[i].organizeid+"','"+list[i].organizename+"')\">选择</a>";
			}
            temp+="<i id=\"companyname\">"+list[i].organizename+"</i></span>";
            temp+="</li>";

            if($("#ul1_"+list[i].parentid).length>0){
            	$("#ul1_"+list[i].parentid).append(temp);
			}else{
				$("#li1_"+organizeid).append(ultemp);
				$("#ul1_"+list[i].parentid).append(temp);
			}
			$("ul li").find("div[class=white_line]").remove();
			$("ul li:last-child").append("<div class=\"white_line\" name=\"white_box\">");
			
			appendli1(list,list[i].organizeid);
			
			
		}
	}
	var whiteHeight=0;
	
}

function changeorganize(obj,organizeid,organizename){
	$('#organizetree1').find('a[class=checked]').attr("class","check");
	$(obj).attr("class","checked");

	//回调方法
	chooseorganize(organizeid,organizename);
}
function changenextul1(obj,organizeid){
	if($(obj).attr("class")=="bg_hidden"){
		$(obj).prev().show();
		$(obj).attr("class","bg_show");
		$(obj).nextAll("ul").show();
		changeheight1();
	}else if($(obj).attr("class")=="bg_show"){
		$(obj).attr("class","bg_hidden");
		$(obj).nextAll("ul").hide();
		$(obj).prev().hide();
		changeheight1();
	}
}
function changeheight1(){
	var whiteHeight=0;
	$(".tree_list .white_line").each(function() {	
		whiteHeight = $(this).parent().height();
		whiteHeight = whiteHeight - 21 ;
	    $(this).height(whiteHeight) ;
	});
}

</script>
</body>
</html>