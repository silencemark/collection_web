<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>统计日活统计-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
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
</script>
</head>

<body onload="onloaddata();">
<jsp:include page="../../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList">企业信息列表</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyInfo?companyid=${map.companyid}">公司信息</a><i>/</i><span>日活统计</span></p></div>     
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">日活统计</span></div>
        <div class="sel_box">       
        	<form action="<%=request.getContextPath() %>/managebackstage/getManageStatisticsToday" method="post">
        		<input name="companyid" value="${map.companyid}" style="display: none;"/>
	            <input type="text" class="text" placeholder="开始时间" name="starttime" value="${map.starttime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />            
	            <input type="text" class="text" placeholder="结束时间" name="endtime" value="${map.endtime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
	            <input type="submit" value="统计" class="find_btn"  />
	            <div class="clear"></div>
            </form>
        </div>
      	<div class="chart_imgbox">
      		<div class="chart_img" id="echarts_two" style="height:350px; width:100%;"></div>
        </div>
    </div>
</div>
<script type="text/javascript">
function onloaddata(){
	<c:if test="${usercountlist.size()>0}">
		var timedataArray1 = new Array();
		var nowdataArray1 = new Array();
		var count = "${count}";
		<c:forEach items="${usercountlist }" var="user" varStatus="st">
			timedataArray1[${st.index}]='${user.comparetime}';
			nowdataArray1[${st.index}]='${user.count}';
		</c:forEach>
		option = {
			    
			    title: {
			    	text: '使用人数：'+count+"人",
			    },
			    tooltip: {
			        trigger: 'axis',
			        formatter: function(params,ticket,callback){
// 			        	console.log(params);
			            var res = '';  
			            var date = '';
			            for (var i = 0; i < params.length; i++) {  
			                 date = params[i][1]+" 号<br/>";
			                 var money = (params[i][2]+"").replace("-",0);
			                 res += "活跃人数："+money+"人<br/>";
			                 res += "日活率："+((money/count).toFixed(2))+"%";
			            }; 
			            return date+res;  
			        } 
			    },
			    legend: {
			    	data:["人员"]
			    },
			    grid: {
			        x:50,
			        y:60,
			        y2:40,
			        x2:40,
			    },
			    toolbox: {
			        show: true,
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
			    xAxis: {
			        type: 'category',
			        boundaryGap: false,
			        data: timedataArray1
			    },
			    yAxis: {
			        type: 'value'
			    },
			    series: [
			        {
			        	name:"人员",
			            type:'line',
			            data:nowdataArray1
			        }
			    ]
			};
		
		$("#echarts_two").css("width",$(document).width()*70/100+"px");
		var myChart1 = echarts.init(document.getElementById("echarts_two"));
		myChart1.setOption(option);
	</c:if>
}
</script>
</body>
</html>