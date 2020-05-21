<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>通知详情</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/showcomment.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/relaycomment.js"></script>

<script type/="text/javascript">
var relayiduserid = '${userInfo.userid}';
var relayidcompanyid ='${userInfo.companyid}';
var relayid = '${map.noticeid}';
var relaytype = 27;
$.ajax({
		type:'post',
		url:'/pc/getNoticeDetail?noticeid=${map.noticeid}',
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
					$("#title").text(data.data.title);
					$("#time").text(data.data.createtime);
					$("#toptime").text(data.data.createtime);
					$("#content").text(data.data.content);
					$("#realname").text(data.data.realname);
					$("#img").attr("src",data.data.headimage);
				}            
	            if(data.data.filelist!=""){
	            	var temp="";
	            	for(var i= 0 ; i<data.data.filelist.length;i++){
	            		if(data.data.filelist[i].type==1){
	            			temp+=" <div class=\"img_box\"><b><img onclick=\"showBigImagePC(this)\" src=\""+data.data.filelist[i].visiturl+"\"></b></div>";
	            		}else{
	            			temp+="<div class=\"talk\" onclick=\"PlayRecordPC(this,\'"+data.data.filelist[i].visiturl+"\')\"></div>";
	            			//temp+="<div class=\"talk\" onclick='checkMuisc()' style=\"cursor: pointer;\"></div>";
	            		
// 	            			temp+="<embed class=\"talk\" onclick='checkMuisc(this)' style=\"cursor: pointer;\" src=\""+data.data.filelist[i].visiturl+
// 	            			"\" loop=\"false\" autostart=\"false\"  hidden=\"true\" menu=\"false\" flashvars=\"autoplay=false&play=false\" play=\"false\" /></div>";
// 							$("#music").val(data.data.filelist[i].visiturl);
	            		}
	            	}
	            	temp+="<div class=\"clear\"></div>"
	            	$("#urldiv").append(temp);
           		}
			}
		}
})

function checkMuisc(obj){	
// 	$().controls.play(); //播放
// 	$("#mu").controls.pause();// 暂停
  
// 	$("#mu").pause();// 暂停
// 		var mu =  $("#mu")[0];
// 		alert(mu);
   $("#mu").attr("autoplay",true);
// $().controls.stop();//停止
}
//评论分页
function pageHelper(num){
	var param =new  Object();
	var currentPage = num;
	param.currentPage=currentPage;
	param.orderid='${map.noticeid}';
	param.resourcetype= 27;
	pageList(param);
}

//评论列表
function onloadDate(){
	getSendName('${map.forwarduserid}',"#sendname");
	var param =new  Object();
	param.orderid='${map.noticeid}';
	param.resourcetype=27;
	addcommentAjax(param);//显示评论列表		
}

//新增评论
function checkComment(row){
	var param = new Object();
	param.userid = '${userInfo.userid}';
	param.resourceid = '${map.noticeid}';
	param.resourcetype = 27;
	showComment(row,param,'/pc/oa_notice_detail?noticeid=${map.noticeid}&resourcetype=27');//添加评论信息
}

//删除
function checkDel(){
	$.ajax({
		type:'post',
		dataType:'json',
		url:'/pc/updatenotice?noticeid=${map.noticeid}',
		success:function(data){
			if(data.status==1){
				swal({
					title : "",
					text : data.msg,
					type : "erres",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){

				});
			}else{
				swal({
					title : "",
					text :  data.msg,
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href="<%=request.getContextPath() %>/pc/oa_notice_list";
				});
			}
		}
	})
}

$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#noticeActive').attr('class','active');
});
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>通知详情</span><a href="<%=request.getContextPath() %>/pc/oa_notice_list" class="back">返回</a>
    	<a href="#" onclick="checkDel()">删除</a><a href="javascript:void(0)" onclick="showForwardDiv()"  >转发</a></div>
        <div class="page_tab2">            
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="none_border" style ="font-size:12px;">
                    <tr>
                    	<td colspan="2">
                        	<div class="notice_xx">
                            	<div class="n_name">
                                	<span id="title"></span>
                                	<i id="toptime"></i>
                                    <div class="clear"></div>
                                </div>
                                <div class="p_box" style="font-size: 12px;"id="content">
                                	
                                </div>  
                                <div class="img_list" id="urldiv">
                                
                                </div>
                                <audio id="mu"  ><source src="" id="music"></audio>
                            </div>
                        </td>
                    </tr>
                    <tr class="first_td">
                    	<td class="l_name">发送人</td>
                        <td class="img_td"><div class="img f"><img src="" id="img" width="30" height="30" /></div><i class="i_name" id="realname"></i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">转发人</td>
                        <td class="img_td">
                        	<i class="i_name" id="sendname"></i>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">创建时间</td>
                        <td id="time"></td>
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
    <div class="clear"></div>
</div>

</body>
</html>
