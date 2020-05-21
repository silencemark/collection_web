<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>任务详情</title>
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
var relayid = '${map.taskid}';
var relaytype = 19;

function taskfun(){
	$.ajax({
		type:'post',
		url:'/pc/getTaskInfo?taskid=${map.taskid}',
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
				if(data.taskinfo!=null){
					$("#title").text(data.taskinfo.title);
					$("#endtime").text(data.taskinfo.endtime);
					if(data.taskinfo.result==1){
						$("#consent").attr("src","../userbackstage/images/pc_page/agree_img.png");
					}else if(data.taskinfo.result==2){
						$("#consent").attr("src","../userbackstage/images/pc_page/agree_img2.png");
					}
					$("#createheadimage").attr("src",data.taskinfo.createheadimage);
					$("#createname").text(data.taskinfo.createname);
					$("#createtime").text(data.taskinfo.createtime);
					$("#examineheadimage").attr("src",data.taskinfo.examineheadimage);
					$("#examinename").text(data.taskinfo.examinename);
					var assi = "";
	        		$.each(data.taskinfo.assistlist,function(k,item){
	        			if(item!=null){
	        				assi += "<div class=\"img f\" ><img src=\""+item.headimage+"\" width=\"30\" height=\"30\" /></div><i class=\"i_name\">"+item.realname+"</i>";
	        			}
	        			
	        		});
	        		$("#imgdiv").html(assi);
	        	     if(data.taskinfo.status == 0 && data.taskinfo.examineuserid == "${userInfo.userid}"){
	        	    	$("#wei1").show();
	        	    	$("#wei1").show();
	        	     	$("#yi").hide();
	        	     	$('#commentcontent').hide();
	        	     	$('#commentbutton').hide();
	        	     	$('#zhuanfa').hide();
	                  }else{
	                	$("#wei1").hide();  
	                	$("#wei2").hide(); 
	                  	$("#yi").show();
	                  	$("#opinion2").text(data.taskinfo.opinion);
	                  }
				}     
				
	            if(data.taskinfo.filelist!=""){
	            	var temp="";
	            	for(var i= 0 ; i<data.taskinfo.filelist.length;i++){
	            		if(data.taskinfo.filelist[i].type==1){
	            			temp+=" <div class=\"img_box\"><b><img onclick=\"showBigImagePC(this)\" src=\""+data.taskinfo.filelist[i].visiturl+"\"></b></div>";
	            		}else{
	            			temp+="<div class=\"talk\" onclick=\"PlayRecordPC(this,\'"+data.taskinfo.filelist[i].visiturl+"\'></div>";
	            		}
	            	}
	            	temp+="<div class=\"clear\"></div>"
	            	$("#urldiv").append(temp);
	       		}
			}
		}
	})

}

//评论分页
function pageHelper(num){
	var param =new  Object();
	var currentPage = num;
	param.currentPage=currentPage;
	param.orderid='${map.taskid}';
	param.resourcetype= 19;
	pageList(param);
}

//评论列表
function onloadDate(){
	getSendName('${map.forwarduserid}',"#sendname");
	var param =new  Object();
	param.orderid='${map.taskid}';
	param.resourcetype=19;
	taskfun();
	addcommentAjax(param);//显示评论列表		
}

//新增评论
function checkComment(row){
	var param = new Object();
	param.userid = '${userInfo.userid}';
	param.resourceid = '${map.taskid}';
	param.resourcetype =19;
	showComment(row,param,'/pc/getPcOaTaskDetail?taskid=${map.taskid}&resourcetype=19');//添加评论信息
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
		        url: "<%=request.getContextPath() %>/pc/exportTaskDetail?taskid=${map.taskid}",
		        success: function(data){
		        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
		        }
		    });
		});
		
	}
	
function successComplete(){
	var obj = new Object();
	obj.taskid = '${map.taskid}';
	obj.userid ='${userInfo.userid}' ;
	obj.opinion = $('#opinion1').val();
	
	$.ajax({
		url:"/pc/examineTask",
		type:"post",
		data:obj,
		success:function(data){
			if(data.status == 0 || data.status == '0'){
				swal({
					title : "",
					text : "任务已经完成！",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href='<%=request.getContextPath()%>/pc/getPcOaTaskList';
				});
			}
		}
	});
}
$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#taskActive').attr('class','active');
});
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>任务详情</span><a href="<%=request.getContextPath()%>/pc/getPcOaTaskList" class="back">返回</a>
    	<a href="#"  onclick="exportexcel()">导出</a>
    	<a href="#" id="zhuanfa" onclick="showForwardDiv()">转发</a>
    	</div>
        <div class="page_tab2"> 
        	<div class="check_state"><img src="" id="consent"/></div>
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size: 12px;">
                  	<tr>
                    	<td class="l_name2">主题</td>
                        <td><span id="title"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name2">截止时间</td>
                        <td><span id="endtime"></span></td>
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
                    	<td class="l_name">任务下达人</td>
                        <td class="img_td"><div class="img f"><img src="" id="createheadimage" width="30" height="30" /></div><i class="i_name" id="createname"></i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">任务下达时间</td>
                        <td><span id="createtime"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name">责任人</td>
                        <td class="img_td"><div class="img f"><img src="" id="examineheadimage" width="30" height="30" /></div><i class="i_name" id="examinename"></i></td>
                    </tr>
                     <tr>
                    	<td class="l_name">协办人</td>
                        <td class="img_td" id="imgdiv"></td>
                    </tr>
                    <tr>
                    	<td class="l_name">转发人</td>
                        <td class="img_td">
                        	<i class="i_name" id="sendname"></i>
                        </td>
                    </tr>
                     <tr class="none_border"   id="wei1">
	                    	<td class="l_name2">反馈内容</td>
	                        <td><textarea class="text_area" placeholder="请输入反馈内容，最多允许输入800字符" maxlength="800" id="opinion1"></textarea></td>
	                    </tr>
	                    <tr class="none_border" id="wei2">
	                    	<td>&nbsp;</td>
	                       <td><a href="#" class="a_btn bg_yellow" onclick="successComplete()">完成</a><a href="<%=request.getContextPath()%>/pc/getPcOaTaskList" class="a_btn bg_gay2" >取消</a></td>
	                    </tr>               
	                     <tr class="none_border" id="yi">
	                    	<td class="l_name2">反馈内容</td>
	                        <td><span id="opinion2"></span></td>
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
    </div>
    <div class="clear"></div>
</div>

</body>
</html>
