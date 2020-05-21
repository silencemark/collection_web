<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>请假审核</title>
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
var relayid = '${map.leaveid}';
var relaytype = 18;

var examine_createid = "";
$.ajax({
	type:'post',
	url:'/pc/getLeaveInfo?leaveid=${map.leaveid}',
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
			if(data.leaveinfo!=null){
				$("#leavetypename").text(data.leaveinfo.leavetypename);
				$("#starttime").text(data.leaveinfo.starttime);
				$("#endtime").text(data.leaveinfo.endtime);
				$("#long").text(data.leaveinfo.daynum+"天"+data.leaveinfo.hournum+"小时");
				$("#reason").text(data.leaveinfo.reason);
				$("#createheadimage").attr("src",data.leaveinfo.createheadimage);
				$("#createname").text(data.leaveinfo.createname);
				$("#createtime").text(data.leaveinfo.createtime);
				$("#examineheadimage").attr("src",data.leaveinfo.examineheadimage);
				$("#examinename").text(data.leaveinfo.examinename);
				$("#opinion").text(data.leaveinfo.opinion);
				examine_createid = data.leaveinfo.createid;
				
				showCCuserInfo(data.leaveinfo.ccuserlist);
			}            
            if(data.leaveinfo.filelist!=""){
            	var temp="";
            	for(var i= 0 ; i<data.leaveinfo.filelist.length;i++){
            		if(data.leaveinfo.filelist[i].type==1){
            			temp+=" <div class=\"img_box\"><b><img onclick=\"showBigImagePC(this)\" src=\""+data.leaveinfo.filelist[i].visiturl+"\"></b></div>";
            		}else{
            			temp+="<div class=\"talk\" onclick=\"PlayRecordPC(this,\'"+data.leaveinfo.filelist[i].visiturl+"\')\"></div>";
            		}
            	}
            	temp+="<div class=\"clear\"></div>"
            	$("#urldiv").append(temp);
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
	param.orderid='${map.leaveid}';
	param.resourcetype= 18;
	pageList(param);
}

//评论列表
function onloadDate(){
	var param =new  Object();
	param.orderid='${map.leaveid}';
	param.resourcetype=18;
	addcommentAjax(param);//显示评论列表		
}

//新增评论
function checkComment(row){
	var param = new Object();
	param.userid = '${userInfo.userid}';
	param.resourceid = '${map.leaveid}';
	param.resourcetype =18;
	showComment(row,param,'/pc/getPcOaRestDetail?leaveid=${map.leaveid}&resourcetype=18');//添加评论信息
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
		        url: "<%=request.getContextPath() %>/pc/exportLogLeaveDetail?leaveid=${map.leaveid}",
		        success: function(data){
		        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
		        }
		    });
		});
		
}
	
function examineInfo(result){
	var param = new Object();
	param.leaveid ='${map.leaveid}';
	param.userid = '${userInfo.userid}';
	param.opinion = $('#opinion').val();
	param.result = result;
	
	$.ajax({
		url:"/pc/examineLeave",
		type:"post",
		data:param,
		success:function(resultMap){
			if(resultMap.status == 0 || resultMap.status == '0'){
				changeIsread(examine_createid,"${map.leaveid}");
				var title=$("#examinename").text()+"审批了您的请假单,请及时查看";
				var url="/oa/rest_check.html?leaveid=${map.leaveid}&userid="+examine_createid;
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
					location.href='<%=request.getContextPath() %>/pc/getPcOaRestList';
				});
			}
		}
	});
}

$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#restActive').attr('class','active');
});
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>请假审核</span><a href="<%=request.getContextPath() %>/pc/getPcOaRestList" class="back">返回</a>
    	<a href="#" onclick="exportexcel()">导出</a>
<!--     	<a href="#" onclick="showForwardDiv()">转发</a> -->
    	</div>
        <div class="page_tab2"> 
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">
                  	<tr>
                    	<td class="l_name2">请假类型</td>
                        <td ><span id="leavetypename"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name2">开始时间</td>
                        <td ><span id="starttime"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name2">结束时间</td>
                        <td ><span id="endtime"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name2">请假时长</td>
                        <td ><span id="long"></span></td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name2">具体内容</td>
                        <td>
                        	<span id="reason"></span>
                            <div class="clear"></div>
                            <div class="img_list" id="urldiv">
                             
                            </div>
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
                        <td class="img_td"><div class="img f"><img src="" id="examineheadimage" width="30" height="30" /></div><i class="i_name" id="examinename"></i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">抄送人</td>
                        <td class="img_td" id="CCusernames"></td>
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
<!--             	<b><textarea placeholder="请输入评论内容，最多允许输入800字符" maxlength="800" id="content_text1"></textarea></b> -->
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
