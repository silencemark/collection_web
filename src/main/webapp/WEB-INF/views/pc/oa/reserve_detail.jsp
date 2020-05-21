<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>备用金详情</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
	<!-- 上传图片需要js -->   
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>

	<!-- 上传文件需要js -->      
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.ui.widget.js"></script>
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.iframe-transport.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload.js"></script>  
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-ui.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-process.js"></script>   
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-validate.js"></script> 

<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/showcomment.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/relaycomment.js"></script>

<script type="text/javascript">
var relayiduserid = '${userInfo.userid}';
var relayidcompanyid ='${userInfo.companyid}';
var relayid = '${map.reserveamountid}';
var relaytype = 20;
$.ajax({
	type:'post',
	url:'/pc/getReserveAmountInfo?reserveamountid=${map.reserveamountid}',
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
			if(data.reserveamountinfo!=null){
				$("#reason").text(data.reserveamountinfo.reason);
				var urgentlevel="";
				if(data.reserveamountinfo.urgentlevel==1 || data.reserveamountinfo.urgentlevel == '1'){
					$("#urgentlevel").attr("class","green");
					urgentlevel="普通";
				}else if(data.reserveamountinfo.urgentlevel==2 || data.reserveamountinfo.urgentlevel == '2'){
					$("#urgentlevel").attr("class","yellow");
					urgentlevel="紧急";
				}else if(data.reserveamountinfo.urgentlevel==3| data.reserveamountinfo.urgentlevel == '3'){
					$("#urgentlevel").attr("class","red");
					urgentlevel="非常紧急";
				}
				$("#urgentlevel").text(urgentlevel);
				$("#amount").text("￥"+data.reserveamountinfo.amount);
				$("#usetime").text(data.reserveamountinfo.usetime);
				$("#returntime").text(data.reserveamountinfo.returntime);
				$("#remark").text(data.reserveamountinfo.remark);
				if(data.reserveamountinfo.result==1){
					$("#consent").attr("src","../userbackstage/images/pc_page/agree_img.png");
				}else if(data.reserveamountinfo.result==0){
					$("#consent").attr("src","../userbackstage/images/pc_page/agree_img2.png");
				}
				$("#createheadimage").attr("src",data.reserveamountinfo.createheadimage);
				$("#createname").text(data.reserveamountinfo.createname);
				$("#createtime").text(data.reserveamountinfo.createtime);
				$("#examineheadimage").attr("src",data.reserveamountinfo.examineheadimage);
				$("#examinename").text(data.reserveamountinfo.examinename);
				$("#opinion").text(data.reserveamountinfo.opinion);
				
				showCCuserInfo(data.reserveamountinfo.ccuserlist);
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
	param.orderid='${map.reserveamountid}';
	param.resourcetype= 20;
	pageList(param);
}

//评论列表
function onloadDate(){
	getSendName('${map.forwarduserid}',"#sendname");
	var param =new  Object();
	param.orderid='${map.reserveamountid}';
	param.resourcetype=20;
	addcommentAjax(param);//显示评论列表		
}

//新增评论
function checkComment(row){
	var param = new Object();
	param.userid = '${userInfo.userid}';
	param.resourceid = '${map.reserveamountid}';
	param.resourcetype =20;
	showComment(row,param,'/pc/getPcOaReserveDetail?reserveamountid=${map.reserveamountid}&resourcetype=20');//添加评论信息
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
		        url: "<%=request.getContextPath() %>/pc/exportReserveDetail?reserveamountid=${map.reserveamountid}",
		        success: function(data){
		        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
		        }
		    });
		});
		
	}
$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#reserveActive').attr('class','active');
});
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>备用金详情</span><a href="<%=request.getContextPath() %>/pc/getPcOaReserveList" class="back">返回</a>
    	<a href="#" onclick="exportexcel()">导出</a><a href="#" onclick="showForwardDiv()">转发</a></div>
        <div class="page_tab2"> 
        	<div class="check_state"><img src="" id="consent"/></div>
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">
                  	<tr>
                    	<td class="l_name2">事由</td>
                        <td ><span id="reason"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name2">紧急程度</td>
                        <td ><i class="green" id="urgentlevel"></i></td>
                    </tr>
                 	<tr>
                    	<td class="l_name">申请金额</td>
                        <td><span id="amount"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name">使用日期</td>
                        <td><span id="usetime"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name">归还日期</td>
                        <td><span id="returntime"></span></td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name2">备注</td>
                        <td>
                        	<span id="remark"></span>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">请示人</td>
                        <td class="img_td"><div class="img f"><img src="" id="createheadimage" width="30" height="30" /></div><i class="i_name" id="createname"></i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">抄送人</td>
                        <td class="img_td" id="CCusernames"></td>
                    </tr>
                    <tr>
                    	<td class="l_name">转发人</td>
                        <td class="img_td">
                        	<i class="i_name" id="sendname"></i>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">请示时间</td>
                        <td><span id="createtime"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name">审批人</td>
                        <td class="img_td"><div class="img f"><img src="" id="examineheadimage" width="30" height="30" /></div><i class="i_name" id="examinename"></i></td>
                    </tr>
                    <tr class="none_border">
                    	<td class="l_name2">审批意见</td>
                        <td><span id="opinion"></span></td>
                    </tr>
                  
                </table>
            </div>
        </div>
        <div class="comment">
       	<div class="name">共有<i class="yellow" id="num"></i>条评论</div>
            <div class="text_box" >
            	<b><textarea placeholder="请输入评论内容，最多允许输入800字符" maxlength="800" id="content_text1"></textarea></b>
                <span><input type="button" value="评论" onclick="checkComment(1)" /></span>
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
