<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>库存分析</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/echarts-all.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organizeRadioPC.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/tab.js"></script>
</head>
<script type="text/javascript">
var stime="";
var etime="";
var organizename='${userInfo.organizename}';
var organizeid='${userInfo.organizeid}';

function getnotice(starttime,endtime){
	stime=starttime+" 00:00:00";
	etime=endtime+" 23:59:59";
	$('#organizename').html(organizename);
	//查询统计数据
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getMaterialGroupList?organizeid='+organizeid+'&starttime='+starttime+'&endtime='+endtime+'&companyid=${userInfo.companyid}',
		success:function(data){
			if(data.status==0){
				$('#starttime').val(data.map.starttime);
				$('#endtime').val(data.map.endtime);
				var dataArray = new Array();
				var sumprice=0;
				for(var i=0;i<data.typelist.length;i++){
					dataArray[i]=data.typelist[i].name;
					if(data.typelist[i].value != 'NaN'){
					sumprice+=parseFloat(data.typelist[i].value);
					}
				}
				
				var summaterialprice = data.purchaseMap.summaterialprice;
				var sumpayamount = data.purchaseMap.sumpayamount;
				
				var nonecolors=['#7e6fe1','#fff299', '#ee5c5c', '#64cccb', '#f7a44b','#6ac868',  '#36adff', '#9484fc','#ffce99', '#ee715c', '#64ccba','#f7bccb','#85e299','#5aace4','#9d6fe1','#e3eb64','#b13b3b','#51a4cf','#b9b866','#95db5c','#1ebfee'];
				var str="<div style=\"position:absolute;z-index:500;width:355px;padding-top:8px;height:320px;background-color:white;margin-bottom:-200px;\">";
				for(var i=0;i<data.typelist.length;i++){
					var nonenum=i%nonecolors.length;
					str+="<a onclick=\"materialDetail1('"+data.typelist[i].name+"',"+data.typelist[i].type+","+sumprice+")\"><span style=\"width:10px;height:5px;background-color:"+nonecolors[nonenum]+";color:"+nonecolors[nonenum]+";\">___</span>"+data.typelist[i].name+"</a><br/><div style=\"margin-top:5px;\"></div>";
				}
				str+="</div>";
				var allprice="";
				if(parseInt(sumprice)!=0){
					var payamountsumprice = sumprice*(sumpayamount/summaterialprice);
					allprice="实际金额\n￥"+payamountsumprice.toFixed(2)+"元";
				}
					option = {
						    legend: {
						        orient : 'vertical',
						        x : 'left',
						        data:dataArray,
						        selectedMode:false
						    },
						    toolbox: {
						        show : true,
						      
						    },
						    calculable : false,
						    series : [
						        { 
						            type:'pie', 
						            radius : [0,0], 
						            itemStyle : {
						                normal : {
						                    label : {
						                        position : 'inner',
						                        show : true, 
						                        textStyle:{
						                            color:'red'
						                        }
						                    },
						                    labelLine : {
						                        show : false
						                    },color:'#FFFFFF'
						                }
						            }, 
						            data:[
						                {
						                  value:335, name:allprice
						                }  
						            ]
						        },
						        {
						            name:'访问来源',
						            type:'pie',
						            radius : [65, 100],
						            selectedMode: 'single',
						            x: '60%',
						            width: '35%',
						            funnelAlign: 'left',
						            max: 1048,
						            
						            data:data.typelist, itemStyle : {
						                normal : {
						                    label : {
						                        position : 'inner',
						                        show : false, 
						                        textStyle:{
						                            color:'red'
						                        }
						                    },
						                    labelLine : {
						                        show : false
						                    }
						                }
						            }
						        }
						    ],
						    color:nonecolors,
						};
					
					var myChart = echarts.init(document.getElementById("echarts_one"));
					myChart.setOption(option);
					myChart.on('click', function (params) {
					    // 控制台打印数据的名称
					    $('#typename').text(params.name);
					    if(params.data.type==1){
					    	$('#tb_12').text(params.name+"单");
					    }else if(params.data.type==2){
					    	$('#tb_12').text(params.name+"单");
					    }else if(params.data.type==3){
					    	$('#tb_12').text(params.name+"单");
					    }else{
					    	$('#tb_12').text("库存采购(入库)单");
					    }
					    materialDetail(params.data.type,sumprice);
					});
					if(data.typelist.length>0){
						$('#typename').text(data.typelist[0].name);
					    
					    if(data.typelist[0].type==1){
					    	$('#tb_12').text(data.typelist[0].name+"单");
					    }else if(data.typelist[0].type==2){
					    	$('#tb_12').text(data.typelist[0].name+"单");
					    }else if(data.typelist[0].type==3){
					    	$('#tb_12').text(data.typelist[0].name+"单");
					    }else{
					    	$('#tb_12').text("库存采购(入库)单");
					    }
						materialDetail(data.typelist[0].type,sumprice);
					}else{
						$('#martailtable').html("<tr class=\"head_td\"><td width=\"50%\">类别</td><td width=\"50%\">金额</td></tr>");
						$('#purchaseul').html("");
					}
					$("#nonespan").html(str);
			}
		}
	})
}
var typenow="";
function materialDetail1(name,type,allsumprice){
	
	// 控制台打印数据的名称
    $('#typename').text(name);
    if(type==1){
    	$('#tb_12').text(name+"单");
    }else if(type==2){
    	$('#tb_12').text(name+"单");
    }else if(type==3){
    	$('#tb_12').text(name+"单");
    }else{
    	$('#tb_12').text("库存采购(入库)单");
    }
    materialDetail(type,allsumprice);
	
}
function materialDetail(type,allsumprice){
	
	typenow=type;
	//查询统计数据
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getStockMaterialDetailByStatistics?organizeid='+organizeid+'&type='+type+'&starttime='+stime+'&endtime='+etime,
		success:function(data){
			if(data.status==0){
				var temp="";
				var sumprice=0.0;
				for(var i=0;i<data.materiallist.length;i++){
					temp+="<tr><td>"+data.materiallist[i].type+"</td><td>￥"+data.materiallist[i].sumprice+"元</td></tr>";
					sumprice+=parseFloat(data.materiallist[i].sumprice);
				}
				$('#martailtable').html("<tr class=\"head_td\"><td width=\"50%\">类别</td><td width=\"50%\">金额</td></tr>");
				$('#martailtable').append(temp);
				
				$('#sumprice').text("￥"+sumprice.toFixed(2)+"元");
			    if(sumprice==0){
			    	$('#special').text(0);
			    }else{
			    	$('#special').text((sumprice/(allsumprice)*100).toFixed(2));
			    }
				
			}
		}
	})
	
	
	
	//查询各种单统计数据
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getStockOrderByStatistics?organizeid='+organizeid+'&type='+type+'&starttime='+stime+'&endtime='+etime,
		success:function(data){
			if(data.status==0){
				var temp="";
				for(var i=0;i<data.orderlist.length;i++){
					if(type==1){
						temp+="<li onclick=\"location.href=\'<%=request.getContextPath() %>/pc/getMaterialInfo?orderid="+data.orderlist[i].orderid+"\'\">";
				    }else if(type==2){
				    	temp+="<li onclick=\"location.href=\'<%=request.getContextPath() %>/pc/getReturnInfo?orderid="+data.orderlist[i].orderid+"\'\">";
				    }else if(type==3){
				    	temp+="<li onclick=\"location.href=\'<%=request.getContextPath() %>/pc/getReportlossInfo?orderid="+data.orderlist[i].orderid+"\'\">";
				    }else{
				    	temp+="<li onclick=\"location.href=\'<%=request.getContextPath() %>/pc/getPurchaseInfo?orderid="+data.orderlist[i].orderid+"\'\">";
				    }
					temp+="<span>"+data.orderlist[i].orderno+"</span></li>";
				}
				$('#purchaseul').html(temp);
			}
		}
	})
}

function chooseorganize(orgid,orgname){
	$('.tc_structure').hide();
	$('.div_mask').hide();
	organizeid=orgid;
	organizename=orgname;
	getnotice($('#starttime').val(),$('#endtime').val());
	changeOrganizeid(orgid,orgname);
}
$(document).ready(function(){
	$('.stock_li').find("li").attr('class','');
	$('#stock').attr('class','active');
	$('#homepage').parent().find("li").attr('class','');
	$('#homepage').attr('class','active');
})

function GetDateStr(AddDayCount) { 
	var dd = new Date(); 
	dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期 
	var y = dd.getFullYear(); 
	var m = dd.getMonth()+1;//获取当前月份的日期 
	if(parseInt(m)<10){
		m="0"+m;
	}
	var d = dd.getDate();
	if(parseInt(d)<10){
		d="0"+d;
	}
	return y+"-"+m+"-"+d; 
} 
</script>
<body onload="getnotice(GetDateStr(-30),GetDateStr(0))">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>库存分析</span></div>
        <div class="page_tab">
            <div class="sel_box">
                <input type="text" class="text_time" id="starttime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  placeholder="2016-05-10" />
                <span>-</span>
                <input type="text" class="text_time m_r20" id="endtime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  placeholder="2016-05-10" />
                <input type="button" value="" class="find_btn"  onclick="getnotice($('#starttime').val(),$('#endtime').val())" />
                <div class="clear"></div>
            </div>
            <div class="shop_sel"><span id="organizename"></span><a href="javascript:void(0)" onclick="$('.tc_structure').show();$('.div_mask').show();">切换店面</a><div class="clear"></div></div>
            <span id="nonespan"></span>
            <div class="chart_imgbox" id="echarts_one" style="width: 100%;height: 300px;"></div>  
        </div>
        <div class="charttab_box">
            <div class="jt_top">箭头</div>
            <div class="xx">
                <span class="class" id="typename"></span>
		        <span>金额:<i id="sumprice"></i></span>
		        <span class="num">占比:<i id="special"></i>%</span>
		        <div class="clear"></div>
            </div>
            <div class="tab">
                <div class="tab_name"><span class="active" id="tb_11" onClick="HoverLi(1,1,2)">明细</span><span id="tb_12" onClick="HoverLi(1,2,2)">采购(入库)单</span></div>
                <div class="dis" id="tbc_11">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0"  id="martailtable">
                        <tr class="head_td">
                            <td width="50%">类别</td>
                            <td>金额</td>
                        </tr>
                    </table>
                </div>
                <div class="undis" id="tbc_12">
                    <ul id="purchaseul">
                    </ul>
                </div>
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
</body>
</html>