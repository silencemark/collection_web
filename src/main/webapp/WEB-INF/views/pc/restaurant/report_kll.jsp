<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>每日报表统计-客流量</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/echarts-all.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organizeRadioPC.js"></script>
</head>
<script type="text/javascript">
$(document).ready(function(){
	$('.restaurant_li').find("li").attr('class','');
	$('#report').attr('class','active');
	$('#homepage').parent().find("li").attr('class','');
	$('#homepage').attr('class','active');
})
var organizeid='${userInfo.organizeid}'
var organizename='${userInfo.organizename}';
function getdata(){
	//查询统计数据
	$.ajax({
		type:'post',
		dataType:'json',
		data:"statisticsTime="+$("#statisticsTime").val(),
		url:projectpath+'/app/getReportFlowChart?organizeid='+organizeid,
		success:function(data){
			if(data.status==0){
				/* if(data.nowmonthlist.length>=15){
					$("#echarts_one").css({"width":"600px"});	
				} */
				var timedataArray = new Array() ;
				var lastdataArray = new Array() ;
				var nowdataArray = new Array() ;
				var nowsumprice = 0;
				var lastsumprice = 0;
				var nextcount=999;
				for(var i=data.nowmonthlist.length-1;i>0;i--){
					if(data.nowmonthlist[i].sumdata==0){
						nextcount=i;
					}else{
						break;
					}
				}
				var firstcount=-1;
				for(var i=0;i<data.nowmonthlist.length;i++){
					if(data.nowmonthlist[i].sumdata==0){
						firstcount=i;
					}else{
						break;
					}
				}
				
				for(var i=0;i<data.nowmonthlist.length;i++){
					timedataArray[i]=data.nowmonthlist[i].comparetime;
					if(firstcount<i && i<nextcount){
						nowdataArray[i]=data.nowmonthlist[i].sumdata;
						nowsumprice += parseInt(data.nowmonthlist[i].sumdata);
					}
					//nowdataArray[i]=data.nowmonthlist[i].sumdata;
				}
				
				var nextcount2=999;
				for(var i=data.lastmonthlist.length-1;i>0;i--){
					if(data.lastmonthlist[i].sumdata==0){
						nextcount2=i;
					}else{
						break;
					}
				}
				var firstcount2=-1;
				for(var i=0;i<data.lastmonthlist.length;i++){
					if(data.lastmonthlist[i].sumdata==0){
						firstcount2=i;
					}else{
						break;
					}
				}
				
				for(var i=0;i<data.lastmonthlist.length;i++){
					if(firstcount2<i && i<nextcount2){
						lastdataArray[i]=data.lastmonthlist[i].sumdata;
						lastsumprice += parseInt(data.lastmonthlist[i].sumdata);
					}
					//lastdataArray[i]=data.lastmonthlist[i].sumdata;
				}
// 				if((lastdataArray.length!=0 || nowdataArray.length!=0) && data.nowmonthlist.length>=15){
// 					$("#echarts_one").css({"width":"600px"});	
// 				}
				option = {
					    
					    title: {
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
					                 res += htm+"："+money+"人<br/>";
					            }; 
					            return date+res;  
					        } 
					    },
					    legend: {
					    	orient:'vertical',
					    	data:[data.lastmonth+"月，总客流人数："+lastsumprice+"人",data.nowmonth+"月，总客流人数："+nowsumprice+"人"]
					    },
					    toolbox: {
					        show: false,
					        feature: {
					            dataZoom: {
					                yAxisIndex: 'none'
					            },
					            dataView: {readOnly: false},
					            magicType: {type: ['line', 'bar']},
					            restore: {},
					            saveAsImage: {}
					        }
					    },
					   	grid: {
					   		x:50,y:60,
					        y2:40,x2:20
					    },
					    xAxis: {
					        type: 'category',
					        boundaryGap: false,
					        data: timedataArray,
					        splitLine: {
					            show: false
					        },
					        axisLabel: {
					            interval: 0,
// 					            rotate:40,
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
					    /* dataZoom: [
					        {
					            type: 'inside',
					            start:60,
					            end:100
					        },
					        {
					            show: false,
					            type: 'slider',
					            start:60,
					            end:100,
					        }
					    ], */
					    series: [
					        {
					            name:data.lastmonth+"月，总客流人数："+lastsumprice+"人",
					            type:'line',
					            data:lastdataArray,
					            symbol:'circle'
					        },
					        {
					            name:data.nowmonth+"月，总客流人数："+nowsumprice+"人",
					            type:'line',
					            data:nowdataArray,
					            symbol:'circle'
					        }
					    ],
// 					    color:['#c23531','#2f4554']
					    color:['#2f4554','#c23531']
					};
				
// 				$("#echarts_one").css("width",$(document).width());
				var myChart = echarts.init(document.getElementById("echarts_one"));
				myChart.setOption(option);
			}
		}
	})
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
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>每日报表统计</span></div>
        <div class="page_tab2">        	
            <div class="shop_sel"><span id="organizename"></span><a href="javascript:void(0)" onclick="$('.tc_structure').show();$('.div_mask').show();">切换店面</a><div class="clear"></div></div>
            <div class="mrbb_nav">
                <ul>
                	<li><a href="<%=request.getContextPath()%>/pc/getReportIncomeChart">营业额</a></li>
                    <li class="active"><a href="<%=request.getContextPath()%>/pc/getReportFlowChart">客流量</a></li>
                    <li><a href="<%=request.getContextPath()%>/pc/getReportConsumptionChart">人均消费</a></li>
                    <li><a href="<%=request.getContextPath()%>/pc/getReportAttendanceChart">出勤人数</a></li>
                </ul>
                <ul>
                    <li><a href="<%=request.getContextPath()%>/pc/getReportPreCheckPage">餐前检查</a></li>
                    <li><a href="<%=request.getContextPath()%>/pc/getReportKitChencheckPage">厨房检查</a></li>
                    <li><a href="<%=request.getContextPath()%>/pc/getReportLeavePage">离店报告</a></li>
                    <li><a href="<%=request.getContextPath()%>/pc/getReportSatisfyChart">满意度</a></li>
                </ul>
            </div>
            <div class="mrbb_chartbox">
            	<div class="time_sel">
                    <b style="background:#eee;"><i class="pre" onclick="preMonth()">前一天</i><input style="border:0px; background-color:#eee;" id="statisticsTime" onchange="getdata()" value="请选择时间"><i onclick="nextMonth()" class="next">后一天</i></b>
                </div>
            	<div id="echarts_one" style="width: 100%;height: 300px;"></div>
            </div>
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
</script>
</body>
</html>