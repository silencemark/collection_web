<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>日志详情</title>
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
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript">

var examine_createid = "";

$.ajax({
	type:'post',
	url:'/pc/getJournalDetaiInfo?logid=${map.pid}&jotype=${map.jotype}',
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
			if(data.data!=null){
				if(data.data.jotype==1){
					$("#gonzuo").hide();
					$("#jihua").text("未完成工作");
					$("#nextplan").text(data.data.worksummary);
				}else if(data.data.jotype==2){
					$("#jihua").text("下周工作计划");
					$("#worksummary").text(data.data.worksummary);
					$("#nextplan").text(data.data.nextplan);
				}else if(data.data.jotype==3){
					$("#jihua").text("下月工作计划");
					$("#worksummary").text(data.data.worksummary);
					$("#nextplan").text(data.data.nextplan);
				}
				$("#completedwork").text(data.data.completedwork);
				$("#needhelp").text(data.data.needhelp);
				
				if(data.data.rangelist!=null){
					var temp = "" ;
					for(var i=0;i<data.data.rangelist.length;i++){
						if(i>0){
							temp+=","	
						}	
						temp+=data.data.rangelist[i].rangename;
					}
					$("#orage").text(temp);
					
				}
				$("#time").text(data.data.time);
				
				$("#remark").text(data.data.remark);
				$("#createheadimage").attr("src",data.data.createheadimage);
				$("#createname").text(data.data.createname);
				$("#createtime").text(data.data.createtime);
				$("#commentheadimage").attr("src",data.data.commentheadimage);
				$("#commentusername").text(data.data.commentusername);
				$("#updatetime").text(data.data.updatetime);
				examine_createid = data.data.createid;
				
				showCCuserInfo(data.data.ccuserlist);
				
				var xing ="";
				if(data.data.commentuserid =='${userInfo.userid}' && data.data.status==0){
					for(var i=0;i<5;i++){
						if(i<3){
							$("#save").show();
							$('#commentcontent').hide();
							$('#commentbutton').hide();
							$('#zhuanfa').hide();
							xing += '<img onclick="xuanze('+(i+1)+')" id="img_'+(i+1)+'" name="img_xing" src="../userbackstage/images/pc_page/start2.png" width="20" height="20">';
						}else{
							xing += '<img onclick="xuanze('+(i+1)+')" id="img_'+(i+1)+'" name="img_xing" src="../userbackstage/images/pc_page/start3.png" width="20" height="20">';
						}
					}
				}else{
					for(var i=0;i<5;i++){
						if(i<parseInt(data.data.commentlevel)){
							xing += '<img src="../userbackstage/images/pc_page/start2.png" width="20" height="20">';
						}else{
							xing += '<img src="../userbackstage/images/pc_page/start3.png" width="20" height="20">';
						}
					}   	
				}
				
				$("#commentlevel").html(xing);
            if(data.data.filelist!=""){
            	var temp="";
            	for(var i= 0 ; i<data.data.filelist.length;i++){
            		if(data.data.filelist[i].type==1){
            			temp+=" <div class=\"img_box\"><b><img onclick=\"showBigImagePC(this)\" src=\""+data.data.filelist[i].visiturl+"\"></b></div>";
            		}else{
            			temp+="<div class=\"talk\" onclick=\"PlayRecordPC(this,\'"+data.data.filelist[i].visiturl+"\')\"></div>";
            		}
            	}
            	temp+="<div class=\"clear\"></div>"
            	$("#urldiv").append(temp);
       		}
		}
	}
	}
});

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

var num ;
function xuanze(i){
	$('img[name="img_xing"]').attr("src","../userbackstage/images/pc_page/start3.png");
	for(var j=1;j<=i;j++){
		$('#img_'+j).attr("src","../userbackstage/images/pc_page/start2.png");
	}
	num = i;
}

function sendMessage(){
	var param = new Object();
	param.commentlevel = num;
	param.updateid = '${userInfo.userid}';
	param.logid = '${map.pid}';
	param.jotype = jotype;
	$.ajax({
		url:"/pc/updateJournalInfo",
		type:"post",
		data:param,
		success:function(resultMap){
			if(resultMap.status == 0 || resultMap.status == '0'){
				changeIsread(examine_createid,'${map.pid}');
				var rizhi = "";
				if(jotype == '1'){
					rizhi = "日报";
				}else if(jotype == '2'){
					rizhi = "周报";
				}else if(jotype == '3'){
					rizhi = "月报";
				}
				var title=$("#commentusername").text()+"审批了您提交的"+rizhi+",请及时查看";
				var url="/oa/log_detail.html?pid=${map.pid}&userid="+examine_createid+"&jotype="+jotype;
				pushMessage(examine_createid,title,url);
				swal({
					title : "",
					text : "点评成功！",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href='/pc/getPcOaLogDetail?pid=${map.pid}&jotype='+jotype;
				});
			}
		}
	});
}

var jotype='${map.jotype}';
var resourcetype = "";
//评论分页
function pageHelper(num){
	var param =new  Object();
	var currentPage = num;
	param.currentPage=currentPage;
	param.orderid='${map.pid}';
	param.resourcetype= resourcetype;
	pageList(param);
}


//评论列表
function onloadDate(){
	getSendName('${map.forwarduserid}',"#sendname");
	if(jotype==1){
		resourcetype=23;
		relaytype = 23;
	}else if(jotype==2){
		resourcetype=24;
		relaytype = 24;
	}else if(jotype==3){
		resourcetype=25;
		relaytype = 25;
	}
	var param =new  Object();
	param.orderid='${map.pid}';
	param.resourcetype=resourcetype;
	addcommentAjax(param);//显示评论列表		
}

//新增评论
function checkComment(row){
	var param = new Object();
	param.userid = '${userInfo.userid}';
	param.resourceid = '${map.pid}';
	param.resourcetype =resourcetype;
	showComment(row,param,'/pc/getPcOaLogDetail?pid=${map.pid}&jotype='+jotype);//添加评论信息
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
		        url: "<%=request.getContextPath() %>/pc/exportLogDetail?logid=${map.pid}&jotype="+jotype,
		        success: function(data){
		        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
		        }
		    });
		});
		
	}
$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#logActive').attr('class','active');
});



var relayiduserid = '${userInfo.userid}';
var relayidcompanyid ='${userInfo.companyid}';
var relayid = '${map.pid}';
var relaytype = "";
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>日志详情</span><a href="<%=request.getContextPath() %>/pc/getPcOaLogList" class="back">返回</a>
    	<a href="#" onclick="exportexcel()">导出</a>
    	<a href="#" id="zhuanfa" onclick="showForwardDiv()">转发</a>
    	<a href="#" id="save" onclick="sendMessage()" style="display:none;">发送</a>
    	</div>
        <div class="page_tab2">
        	<div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0"  style="font-size:12px;">
                    <tr>
                    	<td class="l_name2">完成工作</td>
                        <td><span id="completedwork"></span></td>
                    </tr>
                      <tr>
                    	<td class="l_name2">时间</td>
                        <td><span id="time"></span></td>
                    </tr>
                    <tr id="gonzuo" >
                    	<td class="l_name2"  >工作总结</td>
                        <td><span id="worksummary"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name2" id="jihua">未完成工作</td>
                        <td><span id="nextplan"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name2">需要帮助与协调</td>
                        <td><span id="needhelp"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name">发布范围</td>
                        <td><span id="orage"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name2">备注</td>
                        <td>	
                            <span id="remark"></span>
                            <div class="clear"></div>
                            <div class="img_list" id="urldiv">
                             
                            </div>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">创建人</td>
                        <td class="img_td"><div class="img f"><img src="" width="30" height="30" id="createheadimage" /></div><i class="i_name" id="createname"></i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">创建时间</td>
                        <td><span id="createtime"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name">点评人</td>
                        <td class="img_td"><div class="img f"><img src="" width="30" height="30" id="commentheadimage"  /></div><i class="i_name" id="commentusername"></i></td>
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
                    	<td class="l_name">点评时间</td>
                        <td><span id="updatetime"></span></td>
                    </tr>
                    <tr class="none_border">
                    	<td class="l_name2">点评人意见</td>
                        <td><div class="star" id="commentlevel"></div></td>
                    </tr>
                </table>
            </div>
        </div>
         <div class="comment">
		      	 	<div class="name">共有<i class="yellow" id="num"></i>条评论</div>
		            <div class="text_box" >
		            	<b id="commentcontent"><textarea placeholder="请输入评论内容 ，最多允许输入800字符" maxlength="800" id="content_text1"></textarea></b>
		                <span id="commentbutton"><input type="button" value="评论" onclick="checkComment(1)" /></span>
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
</body>
</html>
