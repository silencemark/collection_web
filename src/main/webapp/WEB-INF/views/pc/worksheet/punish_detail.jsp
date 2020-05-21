<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>处罚单</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/relaycomment.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/pc/script/showcomment.js"></script>
</head>

<script type="text/javascript">
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
        url: "<%=request.getContextPath() %>/pc/exportPunishDetailList",
        data: 'punishid=${punishinfo.punishid}',
        success: function(data){
        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
        }
    });
	});
}

</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('.sup_li').find("li").attr('class', '');
		$('#punish').attr('class', 'active');
		$('#homepage').parent().find("li").attr('class', '');
		$('#homepage').attr('class', 'active');
	})
	function onloaddata() {
		getSendName('${map.forwarduserid}',"#sendname");
		readstatus('${punishinfo.punishid}', '${userInfo.userid}');
	}
</script>
<body onload="onloaddata()">
	<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>处罚单详情</span><a href="/pc/getPunishListInfo" class="back">返回</a>
    	<a href="javascript:void(0)" onclick="exportexcel()">导出</a>
    	<c:if test="${punishinfo.examineuserid!=userInfo.userid || punishinfo.status!=0}">
    	<a href="javascript:void(0)" onclick="showForwardDiv()">转发</a>
    	</c:if>
    	</div>
        <div class="page_tab2">   
                  <c:if test="${punishinfo.result==1}">
					<div class="check_state">
						<img src="../userbackstage/images/pc_page/agree_img.png" />
					</div>
				</c:if>
				<c:if test="${punishinfo.result==0}">
					<div class="check_state">
						<img src="../userbackstage/images/pc_page/agree_img2.png" />
					</div>
				</c:if>         
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                    	<td class="l_name">姓名</td>
                        <td class="num_xx">${punishinfo.realname}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">部门</td>
                        <td>${punishinfo.organizename}</td>
                    </tr>
                   <tr>
                    	<td class="l_name">职务</td>
                        <td>${punishinfo.position}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">处罚结果</td>
                        <td>${punishinfo.punishtype}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">发布范围</td>
                        <td id="releaserange"></td>
                    </tr>
                    <tr>
                    	<td class="l_name2">处罚原因</td>
                        <td>
                        ${punishinfo.reason}
                            <div class="clear"></div>
                            <div class="img_list" id="files">
                               <!--  <div class="img_box"><b><img src="../images/public/img.png"></b></div>
                                <div class="img_box"><b><img src="../images/public/img.png"></b></div>
                                <div class="talk">语言文件</div>
                                <div class="clear"></div> -->
                            </div>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">填写人</td>
                        <td class="img_td"><div class="img f"><img src="${punishinfo.createheadimage}" width="30" height="30" /></div><i class="i_name">${punishinfo.createname}</i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">填写时间</td>
                        <td>${punishinfo.createtime}</td>
                    </tr>
                    <tr>
                    	<td class="l_name">审批人</td>
                        <td class="img_td"><div class="img f"><img src="${punishinfo.examineheadimage}" width="30" height="30" /></div><i class="i_name">${punishinfo.examinename}</i></td>
                    </tr>
 <tr>
                    	<td class="l_name">抄送人</td>
                        <td class="img_td" id="CCusernames">
                        	<c:forEach items="${punishinfo.ccuserlist }" var="map">
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
							<c:when test="${punishinfo.examineuserid==userInfo.userid && punishinfo.status==0}">
								<tr class="none_border">
									<td class="l_name2">审批人意见</td>
									<td><textarea class="text_area" placeholder="请输入审批人意见，最多允许输入800字符" maxlength="800"
											id="opinion"></textarea></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td><a href="javascript:void(0)" onclick="examineorder(1)"
										class="a_btn bg_yellow">同意</a> <a href="javascript:void(0)"
										onclick="examineorder(0)" class="a_btn bg_gay2">拒绝</a></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr class="none_border">
									<td class="l_name2">审批人意见</td>
								
								</tr>
							</c:otherwise>
						</c:choose>
                   
                </table>
            </div>
        </div>
        <div class="comment">
        	<div class="name">共有<i class="yellow" id="num"></i>条评论</div>
            <div class="text_box" >
            <c:if test="${punishinfo.examineuserid==userInfo.userid && punishinfo.status==0}">
            	<b><textarea placeholder="请输入评论内容，最多允许输入800字符" maxlength="800" id="content_text1"></textarea></b>
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
//显示发布范围
$(function() {
	var rangelist = '${punishinfo.rangelist}';
	rangelist = JSON.parse(rangelist);
	if (rangelist.length > 0 && rangelist != null) {
		var rangeStr = "";
		$.each(rangelist, function(i, map) {
			var dou = "，";
			if ((i + 1) == rangelist.length) {
				dou = "";
			}
			rangeStr += map.rangename + dou;
		});
		$('#releaserange').text(rangeStr);
	}
	onloadDate();
	showimgs();

})

//显示发布的语音和图片
function showimgs() {
	var filelist = '${punishinfo.filelist}';
	filelist = JSON.parse(filelist);
	if (filelist.length > 0 && filelist != null) {
		var imghtml = "";
		$
				.each(
						filelist,
						function(i, map) {
							if (map.type == 1) {
								imghtml += '<div class="img_box"><b><img onclick=\"showBigImagePC(this)\" src="'+projectpath+"/"+map.visiturl+'"></b></div>';
							} else if (map.type == 2) {
								imghtml += '<div class="talk" yuyin_url="'+projectpath+"/"+map.visiturl+'">语言文件</div>';
							}
						});
		imghtml += '<div class="clear"></div>';

		$('#files').html(imghtml);
	}
}

//评论分页
function pageHelper(num) {
	var param = new Object();
	var currentPage = num;
	param.currentPage = currentPage;
	param.orderid = '${map.punishid}';
	param.resourcetype = 8;
	pageList(param);
}

//评论列表
function onloadDate() {
	var param = new Object();
	param.orderid = '${map.punishid}';
	param.resourcetype = 8;
	addcommentAjax(param);//显示评论列表		
}

//新增评论
function checkComment(row) {
	var param = new Object();
	param.userid = '${userInfo.userid}';
	param.resourceid = '${map.punishid}';
	param.resourcetype = 8;
	showComment(row, param,
			'/pc/getPunishDetailInfo?punishid=${punishinfo.punishid}&resourcetype=8');//添加评论信息
}

	function examineorder(result){
		if($('#opinion').val()==""){
			swal({
				title : "",
				text : "请输入审批意见",
				type : "error",
				showCancelButton : false,
				confirmButtonColor : "#ff7922",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			}, function(){
				
			});
			return;
		}
		$.ajax({
			type:'post',
			dataType:'json',
			url:'<%=request.getContextPath()%>/pc/updatePunishInfo',
			data:'punishid=${punishinfo.punishid}&userid=${userInfo.userid}&result='+result+'&opinion='+$('#opinion').val(),
			success:function(data){
				changeIsread("${punishinfo.createid}","${punishinfo.punishid}");
				var title="${punishinfo.examinename}审批了对你的处罚信息,请及时查看";
				var url="/worksheet/punish_detail.html?punishid=${punishinfo.punishid}&userid=${punishinfo.createid}";
				pushMessage("${punishinfo.createid}",title,url);
					swal({
						title : "",
						text : "操作成功",
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						location.href='<%=request.getContextPath()%>/pc/getPunishDetailInfo?punishid=${punishinfo.punishid}';
					});
					}
				})
	}
</script> 
<script type="text/javascript">
var relayiduserid = '${userInfo.userid}';
var relayidcompanyid ='${userInfo.companyid}';
var relayid = '${map.punishid}';
var relaytype = 8;
</script>



</body>
</html>
