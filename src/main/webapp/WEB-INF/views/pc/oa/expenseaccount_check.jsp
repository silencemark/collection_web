<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报销单审批</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
	<!-- 日期 -->
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/showcomment.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/relaycomment.js"></script>
<script type="text/javascript">
var relayiduserid = '${userInfo.userid}';
var relayidcompanyid ='${userInfo.companyid}';
var relayid = '${map.expenseid}';
var relaytype = 17;

var examine_createid = "";
$.ajax({
	type:'post',
	url:'/pc/getExpenseInfo?expenseid=${map.expenseid}',
	success:function(data){
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
			if(data.expenseinfo!=null){
				$("#expenseno").text(data.expenseinfo.expenseno);
				if(data.expenseinfo.result==1){
					$("#consent").attr("src","../userbackstage/images/pc_page/agree_img.png");
				}else if(data.expenseinfo.result==2){
					$("#consent").attr("src","../userbackstage/images/pc_page/agree_img2.png");
				}
				if(data.expenseinfo.expensedetaillist!=null){
					var temp ="";
					for(var i=0;i<data.expenseinfo.expensedetaillist.length;i++){
						temp+="  <tr>"+
		                        	"<td>"+data.expenseinfo.expensedetaillist[i].type+"</td>"+
		                            "<td>"+data.expenseinfo.expensedetaillist[i].detail+"</td>"+
		                            "<td>"+data.expenseinfo.expensedetaillist[i].price+"</td></tr>";
		                 if(i==data.expenseinfo.expensedetaillist.length-1){
		                	 temp+="<tr class=\"none_border\">"+
			                         	"<td colspan=\"2\">共 <i class=\"yellow\">"+data.expenseinfo.detailnum+"</i> 项</td>"+
			                            "<td colspan=\"1\" >合计<i class=\"yellow\">￥"+data.expenseinfo.detailprice+"</i></td>"+
			                        "</tr>";
		                 }
					}
					$("#mx_td").append(temp);
				}
				$("#createheadimage").attr("src",data.expenseinfo.createheadimage);
				$("#createname").text(data.expenseinfo.createname);
				$("#createtime").text(data.expenseinfo.createtime);
				$("#examineheadimage").attr("src",data.expenseinfo.examineheadimage);
				$("#examinename").text(data.expenseinfo.examinename);
				$("#opinion").text(data.expenseinfo.opinion);
				examine_createid = data.expenseinfo.createid;
				
				showCCuserInfo(data.expenseinfo.ccuserlist);
			}            
            
		}
	}
})

/**
 * 显示抄送人
 */
function showCCuserInfo(ccuserlist){
	if(ccuserlist != null && ccuserlist != "" && ccuserlist.length > 0){
		var htm = "";
		$.each(ccuserlist,function(i,map){
			htm += "<div class=\"img f\"><img src=\""+projectpath+map.headimage+"\" width=\"30\" height=\"30\" /></div>"+
				   "<i class=\"i_name\" >"+map.realname+"</i>";
		});
		$('#CCusernames').html(htm);
	}
}

//评论分页
function pageHelper(num){
	var param =new  Object();
	var currentPage = num;
	param.currentPage=currentPage;
	param.orderid='${map.requestid}';
	param.resourcetype= 17;
	pageList(param);
}

//评论列表
function onloadDate(){
	var param =new  Object();
	param.orderid='${map.requestid}';
	param.resourcetype=17;
	addcommentAjax(param);//显示评论列表		
}

//新增评论
function checkComment(row){
	var param = new Object();
	param.userid = '${userInfo.userid}';
	param.resourceid = '${map.expenseid}';
	param.resourcetype =17;
	showComment(row,param,'/pc/getPcOaExpenseDetail?expenseid=${map.expenseid}&resourcetype=17');//添加评论信息
}

//导出
function exportexcel(){
	 swal({
			title : "",
			text : "是否导出",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$.ajax({
		        type: "POST",
		        url: "<%=request.getContextPath() %>/pc/exportExpenseDetail?expenseid=${map.expenseid}",
		        success: function(data){
		        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
		        }
		    });
		});
		
	}
	
function examineInfo(result){
	var param = new Object();
	param.expenseid ='${map.expenseid}';
	param.userid = '${userInfo.userid}';
	param.opinion = $('#opinion').val();
	param.result = result;
	
	$.ajax({
		url:"/pc/examineExpense",
		type:"post",
		data:param,
		success:function(resultMap){
			if(resultMap.status == 0 || resultMap.status == '0'){
				changeIsread(examine_createid,'${map.expenseid}');
				var title=$("#examinename").text()+"审批了您的报销单,请及时查看";
				var url="/oa/expenseaccount_detail.html?expenseid=${map.expenseid}&userid="+examine_createid;
				pushMessage(examine_createid,title,url);
				swal({
					title : "",
					text : "审批成功！",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href='<%=request.getContextPath() %>/pc/getPcOaExpenseList';
				});
			}
		}
	});
}
$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#expenseActive').attr('class','active');
});
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>报销单审批</span><a href="<%=request.getContextPath()%>/pc/getPcOaExpenseList" class="back">返回</a>
    	<a href="#" onclick="exportexcel()">导出</a>
<!--     	<a href="#" onclick="showForwardDiv()">转发</a> -->
    	</div>
        <div class="page_tab2">  
        	<div class="check_state"><img src="" id="consent"/></div>          
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">
                    <tr>
                    	<td class="l_name">报销单编号</td>
                        <td><span id="expenseno"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name">物料明细</td>
                        <td class="mx_tab">
                        	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="mx_td" id="mx_td" style="font-size:12px;">
                            	<tr class="gray_bg">
                                	<td width="20%">类别</td>
                                    <td width="40%">明细</td>
                                    <td width="40%">金额</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                      <tr>
                    	<td class="l_name">申请人</td>
                        <td class="img_td"><div class="img f"><img src="" id="createheadimage" width="30" height="30" /></div><i class="i_name" id="createname"></i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">填写时间</td>
                        <td><span id="createtime"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name">审批人</td>
                        <td class="img_td"><div class="img f"><img src="" id="examineheadimage" width="30" height="30" /></div><i class="i_name" id="examinename"></i>
                    </td>
                    <tr>
                    	<td class="l_name">抄送人</td>
                        <td class="img_td" id="CCusernames"></td>
                    </tr>
                    </tr>
                        <tr class="none_border">
                    	<td class="l_name2">审批意见</td>
                    	<td><textarea class="text_area" placeholder="请输入审批意见，最多允许输入800字符" maxlength="800" id="opinion"></textarea></td>
                    </tr>
                   <tr class="none_border">
                    	<td>&nbsp;</td>
                        <td><a href="#" class="a_btn bg_yellow" onclick="examineInfo(1)">同意</a><a href="#" class="a_btn bg_gay2" onclick="examineInfo(2)">拒绝</a></td>
                    </tr>
                </table>
            </div>
        </div>
        
         <div class="comment">
       	<div class="name">共有<i class="yellow" id="num"></i>条评论</div>
            <div class="text_box" >
<!--             	<b><textarea placeholder="请输入评论内容，最多允许输入800字符" maxlength="800"id="content_text1"></textarea></b> -->
<!--                 <span><input type="button" value="评论" onclick="checkComment(1)" /></span> -->
	           	<input type="hidden" id="parentid" />
				<input type="hidden" id="parentuserid" />
            </div>
            <div class="ul_list" id="ul_list">
            	
                
            </div>
            <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination-->
            <div class="clear"></div>
        </div>
        </div>
    </div>
    <div class="clear"></div>
</div>

</body>
</html>
