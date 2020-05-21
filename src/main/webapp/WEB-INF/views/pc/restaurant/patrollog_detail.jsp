<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>巡店日志详情</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/showcomment.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/relaycomment.js"></script>

</head>
<script type="text/javascript">

var relayiduserid = '${userInfo.userid}';
var relayidcompanyid ='${userInfo.companyid}';
var relayid = '${tourstoreinfo.tourstoreid}';
var relaytype = 22;

function exportexcel(){
	$.ajax({
        type: "POST",
        url: "<%=request.getContextPath() %>/pc/exportPatrollogInfo",
        data: 'tourstoreid=${tourstoreinfo.tourstoreid}',
        success: function(data){
        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
        }
    });
}
$(document).ready(function(){
	$('.restaurant_li').find("li").attr('class','');
	$('#patrollog').attr('class','active');
	$('#homepage').parent().find("li").attr('class','');
	$('#homepage').attr('class','active');
})
function onloaddata(){
	readstatus('${tourstoreinfo.tourstoreid}','${userInfo.userid}');
	onloadDate();
}

</script>
<body onload="onloaddata()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
   	  <div class="page_name"><span>巡店日志详情</span><a href="javascript:void(0)" onclick="goBackPage()" class="back">返回</a>
   	  <a href="javascript:void(0)" onclick="exportexcel();">导出</a>
   	  <c:if test="${tourstoreinfo.copyuserid!=userInfo.userid || tourstoreinfo.status!=0}">
   	  <a href="javascript:void(0)"  onclick="showForwardDiv()" >转发</a>
   	  </c:if>
   	  </div>
        <div class="page_tab2">
          <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                    	<td class="l_name">所巡门店</td>
                        <td>${tourstoreinfo.organizename}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">到店时间</td>
                        <td>${tourstoreinfo.arrivetime}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">离店时间</td>
                        <td>${tourstoreinfo.leavetime}</td>
                    </tr>
                    <tr>
                    	<td class="l_name2">发现问题</td>
                        <td>${tourstoreinfo.findproblem}</td>
                    </tr>
                    <tr>
                    	<td class="l_name2">需解决问题</td>
                        <td>${tourstoreinfo.solveproblem}</td>
                    </tr>
                    <tr>
                    	<td class="l_name2">门店意见</td>
                        <td>${tourstoreinfo.opinion}
                        	<div class="clear"></div>
                        	
                            <div class="img_list">
                            	<c:forEach items="${tourstoreinfo.filelist}" var="item">
                            		<c:if test="${item.type==1}">
                            			<div class="img_box"><b><img onclick="showBigImagePC(this)" src="${item.visiturl}"></b></div>
                            		</c:if>
                            	</c:forEach>
                                <c:forEach items="${tourstoreinfo.filelist}" var="item">
                            		<c:if test="${item.type==2}">
                            			<div class="talk" onclick="PlayRecordPC(this,'${item.visiturl}')"></div>
                            		</c:if>
                            	</c:forEach>
                                <div class="clear"></div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">创建人</td>
                        <td class="img_td"><div class="img f"><img src="${tourstoreinfo.createheadimage }" width="30" height="30" /></div><i class="i_name">${tourstoreinfo.createname }</i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">填写时间</td>
                        <td>${tourstoreinfo.createtime}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">审核人</td>
                        <td class="img_td"><div class="img f"><img src="${tourstoreinfo.copyheadimage }" width="30" height="30" /></div><i class="i_name">${tourstoreinfo.copyname }</i></td>
                    </tr>
                     <tr>
                    	<td class="l_name">抄送人</td>
                        <td class="img_td">
                        	<c:forEach items="${tourstoreinfo.ccuserlist }" var="map">
                        		<div class="img f"><img src="${map.headimage }" width="30" height="30" /></div><i class="i_name" >${map.realname }</i>
                        	</c:forEach>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">转发人</td>
                        <td class="img_td">
                        	<i class="i_name" id="sendname"></i>
                        </td>
                    </tr>
					<c:choose>
                    	<c:when test="${tourstoreinfo.copyuserid!=userInfo.userid || tourstoreinfo.status!=0}">
	                    <tr class="none_border">
	                    	<td class="l_name2">审核人意见</td>
	                        <td>${tourstoreinfo.copyopinion }</td>
	                    </tr>
                    	</c:when>
                    	<c:otherwise>
                    	<tr class="none_border">
	                    	<td class="l_name2">审核人意见</td>
	                        <td><textarea class="text_area" placeholder="请输入抄送人意见，最多允许输入800字符" maxlength="800" id="copyopinion"></textarea></td>
	                    </tr>
	                    <tr class="none_border">
	                    	<td>&nbsp;</td>
	                        <td>
	                        	<a  href="javascript:void(0)"  onclick="examineorder()" class="a_btn bg_yellow">发送</a><a href="<%=request.getContextPath() %>/pc/getTourStoreList" class="a_btn bg_gay2">取消</a>
	                        </td>
	                    </tr>
                    	</c:otherwise>
                    </c:choose>
                </table>
            </div>
        </div>
        
        <div class="comment">
       	<div class="name">共有<i class="yellow" id="num"></i>条评论</div>
            <div class="text_box" >
            <c:if test="${tourstoreinfo.copyuserid!=userInfo.userid || tourstoreinfo.status!=0}">
            	<b><textarea placeholder="请输入评论内容 ，最多允许输入800字符" maxlength="800" id="content_text1"></textarea></b>
                <span><input type="button" value="评论" onclick="checkComment(1)" /></span>
                </c:if>
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

<script type="text/javascript">
//评论分页
function pageHelper(num){
	var param =new  Object();
	var currentPage = num;
	param.currentPage=currentPage;
	param.orderid='${tourstoreinfo.tourstoreid}';
	param.resourcetype= 22;
	pageList(param);
}

//评论列表
function onloadDate(){
	getSendName('${map.forwarduserid}',"#sendname");
	var param =new  Object();
	param.orderid='${tourstoreinfo.tourstoreid}';
	param.resourcetype=22;
	addcommentAjax(param);//显示评论列表		
}

//新增评论
function checkComment(row){
	var param = new Object();
	param.userid = '${userInfo.userid}';
	param.resourceid = '${tourstoreinfo.tourstoreid}';
	param.resourcetype =22;
	showComment(row,param,'/pc/getTourStoreInfo?tourstoreid=${tourstoreinfo.tourstoreid}&resourcetype=22');//添加评论信息
}


function examineorder(result){
	if($("#copyopinion").val().trim() == ""){
   	 swal({
				title : "",
				text : "抄送意见不能为空",
				type : "error",
				showCancelButton : false,
				confirmButtonColor : "#ff7922",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			}, function(){
				$("#copyopinion").focus();
			});
   	 return false;
    } 
    
    $.ajax({
		  url:"<%=request.getContextPath()%>/app/examineTourStore",
		  type:"post",
		  data:"tourstoreid=${tourstoreinfo.tourstoreid}&userid=${userInfo.userid}&copyopinion="+$("#copyopinion").val().trim(),
		  success:function(data){
			  if(data.status+"" == "0"){
				 swal({
	  				title : "",
	  				text : "意见填写成功",
	  				type : "success",
	  				showCancelButton : false,
	  				confirmButtonColor : "#ff7922",
	  				confirmButtonText : "确认",
	  				cancelButtonText : "取消",
	  				closeOnConfirm : true
	  			}, function(){
	  				
	  				changeIsread('${tourstoreinfo.createid}','${tourstoreinfo.tourstoreid}');
					var title="${tourstoreinfo.copyname}对您发布的巡店日志，填写了抄送意见,请及时查看";
					var url="/restaurant/patrollog_detail.html?tourstoreid=${tourstoreinfo.tourstoreid}";
					pushMessage('${tourstoreinfo.createid}',title,url);
					
	  				goBackPage();
	  			});
			  }
		  },error:function(e){
			 swal({
				title : "",
				text : "意见填写失败，请稍后再试",
				type : "error",
				showCancelButton : false,
				confirmButtonColor : "#ff7922",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			}, function(){
				 
			});
		  }
   });
}
</script>
</body>
</html>