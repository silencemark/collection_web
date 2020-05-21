<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>通知列表</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script>
var userid;
userid='${userInfo.userid}';
var param=new Object();
	param.receiveid=userid;
	param.companyid='${userInfo.companyid}';
	
function pageHelper(num){
	var currentPage = num;
	param.currentPage=currentPage;
	$.ajax({
 		type:'post',
 		dataType:'json',
 		url:'/pc/getNoticeList',
 		data:param,
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
 				$("#tableadd").html("");
 				if(data.ntList!=null ){
 					var temp="<tr class=\"head_td\">"+
			                    " <td width=\"50%\">标题</td>"+
		                        "<td width=\"20%\">发布时间</td>"+
								"<td width=\"10%\">状态</td>"+
		                        "<td>操作</td>"+
                				"</tr>";
 					for(var i= 0;i<data.ntList.length;i++){
 						temp+="<tr>"+
		 						"<td>"+data.ntList[i].title+"</td>"+
		 	                     "<td>"+data.ntList[i].createtime+"</td>";
		 	                     if(data.ntList[i].isread==0){
		 	                    	 temp+=" <td class=\"red\">未读</td>";
		 	                     }else{
		 	                    	 temp+=" <td class=\"green\">已读</td>";
		 	                     }
		 	                    temp+="<td><a href=\"#\" class=\"blue\" onclick=\"checklook('"+data.ntList[i].resourceid+"','"+data.ntList[i].isread+"','"+data.ntList[i].id+"')\">查看</a><a href=\"javascript:void(0)\" style=\"margin-left:30px;\" class=\"red\" onclick=\"deleteForwardUserInfo(this,'"+data.ntList[i].id+"')\">删除</a></td></tr>";
 					}
 					$("#tableadd").append(temp);
 					$('#Pagination').html(data.pager);
 				}
 			}
 		}
 })
}
var nodata = '<div class="list_none"><span><i class="yellow">Hi,我是大狮！</i><br>列表空空，还没有通知呢~</span><b><img src="/userbackstage/images/index/none_msg2.gif" width="293" height="240"></b></div>';
if("${powerMap.power5001010}" != "null"){
	nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>还没有通知，赶紧添加一条吧~</span><b><img src="/userbackstage/images/index/none_msg.gif" width="293" height="240"></b></div>';
}
function onloadDate(){
	$.ajax({
 		type:'post',
 		url:'/pc/getNoticeList',
 		data:param,
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
 				$("#tableadd").html("");
 				if(data.ntList!=null){
 					var temp="<tr class=\"head_td\">"+
                    " <td width=\"50%\">标题</td>"+
                    "<td width=\"20%\">发布时间</td>"+
					"<td width=\"10%\">状态</td>"+
                    "<td>操作</td>"+
    				"</tr>";
    				if(data.ntList.length > 0){
    					$(".list_none").remove();
 					for(var i= 0;i<data.ntList.length;i++){
 						temp+="<tr>"+
		 						"<td>"+data.ntList[i].title+"</td>"+
		 	                     "<td>"+data.ntList[i].createtime+"</td>";
		 	                     if(data.ntList[i].isread==0){
		 	                    	 temp+=" <td class=\"red\">未读</td>";
		 	                     }else{
		 	                    	 temp+=" <td class=\"green\">已读</td>";
		 	                     }
		 	                    
		 	                    temp+="<td><a href=\"#\" class=\"blue\" onclick=\"checklook('"+data.ntList[i].noticeid+"','"+data.ntList[i].isread+"','"+data.ntList[i].id+"')\">查看</a><a href=\"javascript:void(0)\" style=\"margin-left:30px;\" class=\"red\" onclick=\"deleteForwardUserInfo(this,'"+data.ntList[i].id+"')\">删除</a></td></tr>";
 					}
    				}else{
    					$(".list_none").remove();
    					$("#tableadd").parent().append(nodata);
    				}
 					$("#tableadd").html(temp);
 					$('#Pagination').html(data.pager);
 					
 				}
 			}
 		}
 })
}

function checklook(noticeid,isread,forwarduserid){
	intoDetail("/pc/oa_notice_detail?noticeid="+noticeid+"&forwarduserid="+forwarduserid,noticeid,isread,27);
}

function checkSave(){
	var title =$("#toptitle").val();
	param.title=title;
	onloadDate();
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
		        url: "<%=request.getContextPath() %>/pc/exportnotice",
		        data:param,
		        success: function(data){
		        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
		        }
		    });
		});
		
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
    	<div class="page_name"><span>通知列表</span><a href="#" onclick="exportexcel()">导出</a>
    	<c:if test="${powerMap.power5001010 != null}"><a href="<%=request.getContextPath()%>/pc/oa_notice_add">新建</a></c:if>
    	</div>
        <div class="page_tab">
            <div class="sel_box">
                <input type="text" class="text" placeholder="请输入关键字" id="toptitle"/>
                <input type="button" value="" class="find_btn"   onclick="checkSave()"/>
                <div class="clear"></div>
            </div>
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" id="tableadd" style ="font-size:12px;">
                </table>
            </div>
            <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination-->
        </div>
    </div>
    <div class="clear"></div>
</div>
</body>
</html>
