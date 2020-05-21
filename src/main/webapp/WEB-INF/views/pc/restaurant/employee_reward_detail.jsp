<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>奖励单</title>
<link
	href="<%=request.getContextPath()%>/userbackstage/style/pc_public.css"
	type="text/css" rel="stylesheet" />
<link
	href="<%=request.getContextPath()%>/userbackstage/style/pc_page.css"
	type="text/css" rel="stylesheet" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/app/appcssjs/sweetalert/dist/sweetalert.css" />
<script
	src="<%=request.getContextPath()%>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/pc/script/showcomment.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/userbackstage/script/organizeRangeRedioPC.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/relaycomment.js"></script>
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
        url: "<%=request.getContextPath() %>/pc/exportRewardDetailList",
        data: 'rewardid=${rewardinfo.rewardid}',
        success: function(data){
        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
        }
	});
    });
}
	$(document).ready(function() {
		$('.sup_li').find("li").attr('class', '');
		$('#reward').attr('class', 'active');
		$('#homepage').parent().find("li").attr('class', '');
		$('#homepage').attr('class', 'active');
	})
	function onloaddata() {
		readstatus('${rewardinfo.rewardid}', '${userInfo.userid}');
	}
</script>
<body onload="onloaddata()">
	<jsp:include page="../top.jsp"></jsp:include>
	<div class="page_main">
		<jsp:include page="left.jsp"></jsp:include>
		<div class="right_page">
			<div class="page_name">
				<span>奖励单详情</span><a href="javascript:void(0)" onclick="linkemployee()" class="back">返回</a><a
					href="javascript:void(0)" onclick="exportexcel()">导出</a>
					
			</div>
			<div class="page_tab2">
				<c:if test="${rewardinfo.result==1}">
					<div class="check_state">
						<img src="../userbackstage/images/pc_page/agree_img.png" />
					</div>
				</c:if>
				<c:if test="${rewardinfo.result==0}">
					<div class="check_state">
						<img src="../userbackstage/images/pc_page/agree_img2.png" />
					</div>
				</c:if>
				<div class="tab_list">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td class="l_name">姓名</td>
							<td class="num_xx">${rewardinfo.realname}</td>
						</tr>
						<tr>
							<td class="l_name">部门</td>
							<td>${rewardinfo.organizename}</td>
						</tr>
						<tr>
							<td class="l_name">职务</td>
							<td>${rewardinfo.position}</td>
						</tr>
						<tr>
							<td class="l_name">奖励结果</td>
							<td>${rewardinfo.resulttext}</td>

						</tr>
						<tr>
							<td class="l_name">发布范围</td>
							<td id="releaserange"></td>
						</tr>
						<tr>
							<td class="l_name2">奖励原因</td>
							<td>${rewardinfo.reason}
								<div class="clear"></div>
								<div class="img_list" id="files">
									<!-- 									 <div class="img_box"> -->
									<!-- 										<b><img src=""></b> -->
									<!-- 									</div> -->
									<!-- 									<div class="img_box"> -->
									<!-- 										<b><img src="../images/public/img.png"></b> -->
									<!-- 									</div> -->
									<!-- 									<div  class="talk">语言文件</div> -->
									<!-- 									<div class="clear"></div> -->
								</div>
							</td>
						</tr>
						<tr>
							<td class="l_name">填写人</td>
							<td class="img_td"><div class="img f">

									<img src="${rewardinfo.createheadimage}" width="30" height="30" />
								</div> <i class="i_name">${rewardinfo.createname}</i></td>
						</tr>
						<tr>
							<td class="l_name">填写时间</td>
							<td>${rewardinfo.createtime}</td>
						</tr>

						<tr>
							<td class="l_name">审批人</td>
							<td class="img_td"><div class="img f">
									<img src="${rewardinfo.examineheadimage}" width="30"
										height="30" />
								</div> <i class="i_name">${rewardinfo.examineusername}</i></td>
						</tr>
						<c:choose>
							<c:when
								test="${rewardinfo.examineuserid==userInfo.userid && rewardinfo.status==0}">
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
									<td>${rewardinfo.opinion}</td>
								</tr>
							</c:otherwise>
						</c:choose>
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
	<div class="div_mask" style="display: none"></div>
	<div class="tc_structure" style="display: none;">
		<div class="tc_title">
			<span>选择发布范围</span><a href="#">×</a>
		</div>
		<div class="str_box">
			<ul class="tree_list">
				<li>
					<div class="gray_line"></div> <span class="bg_show"><a
						href="#" class="check">选择</a><i>餐厅公司</i></span>
					<ul>
						<li class="li_bg"><span class="bg_hidden"><a href="#"
								class="check">选择</a><i>普陀区</i></span></li>
						<li class="li_bg"><span class="bg_hidden"><a href="#"
								class="checked">选择</a><i>嘉定区</i></span></li>
						<li class="li_bg">
							<div class="gray_line"></div> <span class="bg_show"><a
								href="#" class="checked">选择</a><i>浦东区</i></span>
							<ul>
								<li class="li_bg"><span class="bg_last"><a href="#"
										class="checked">选择</a><i>浦东店</i></span></li>
								<li class="li_bg"><span class="bg_last"><a href="#"
										class="checked">选择</a><i>申江路店</i></span></li>
								<li class="li_bg"><span class="bg_last"><a href="#"
										class="checked">选择</a><i>普降路店</i></span>
									<div class="white_line" name="white_box"></div></li>
							</ul>
						</li>
						<li class="li_bg"><span class="bg_hidden"><a href="#"
								class="check">选择</a><i>普陀区</i></span>
							<div class="white_line" name="white_box"></div></li>
					</ul>
				</li>
			</ul>
		</div>
		<div class="tc_btnbox">
			<a href="#" class="bg_gay2">取消</a><a href="#" class="bg_yellow">确定</a>
		</div>
	</div>
<div class="div_mask" style="display:none;"></div>
<div class="tc_selrange" style="display:none;">
	<div class="tc_title"><span>选择转发人</span><a href="javascript:void(0);" onclick="$('.tc_selrange').hide();$('.div_mask').hide();">×</a></div>
    <div class="range">
    	<div class="l_box" id="organizetree2" style="overflow:hidden;overflow-y:visible;">
    	
        </div>
        <div class="r_box">
        	<div class="sel_xx">
                <div class="sel_box">
                    <input type="text" class="text" placeholder="请输入姓名" id="searchrealname"/>
                    <input type="button" class="find_btn" value="" onclick="searchuser()" />
                    <div class="clear"></div>
                </div>
            </div>
            <div class="tab_list">
            	<table width="100%" border="0" cellpadding="0" cellspacing="0" id="usertable" style="font-size:12px;">
                	<tr class="head_td">
                    	<td width="30">&nbsp;</td>
                        <td width="70">姓名</td>
                        <td width="70">性别</td>
                        <td width="130">电话</td>
                        <td>操作</td>
                    </tr>
                </table>
            </div>
            <%-- <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination--> --%>
            <div class="tc_btnbox"><a  href="javascript:void(0);" onclick="$('.div_mask').hide();$('.tc_selrange').hide();"  class="bg_gay2">取消</a>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>
</body>
<script type="text/javascript">
	//显示发布范围

	$(function() {
		var rangelist = '${rewardinfo.rangeliststr}';
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

		showimgs();
		onloadDate();

	})

	//显示发布的语音和图片
	function showimgs() {
		var filelist = '${rewardinfo.filelist}';
		filelist = JSON.parse(filelist);
		if (filelist.length > 0 && filelist != null) {
			var imghtml = "";
			$.each(filelist,function(i, map) {
				if (map.type == 1) {
					imghtml += '<div class="img_box"><b><img onclick=\"showBigImagePC(this)\" src="'+projectpath+"/"+map.visiturl+'"></b></div>';
				} else if (map.type == 2) {
					imghtml += '<div class="talk" onclick=\"PlayRecordPC(this,\''+projectpath+"/"+map.visiturl+'\')\"></div>';
				}
			});
			imghtml += '<div class="clear"></div>';

			$('#files').html(imghtml);
		}
	}
	</script>
<script type="text/javascript">

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
			url:'<%=request.getContextPath()%>/pc/updateRewardInfo',
			data:'rewardid=${rewardinfo.rewardid}&userid=${userInfo.userid}&result='+result+'&opinion='+$('#opinion').val(),
			success:function(data){
				changeIsread("${rewardinfo.createid}","${rewardinfo.rewardid}");
				var title="${rewardinfo.examineusername}审批了对你的奖励信息,请及时查看";
				var url="/worksheet/reward_detail.html?rewardid=${rewardinfo.rewardid}&userid=${rewardinfo.createid}";
				pushMessage("${rewardinfo.createid}",title,url);
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
						location.href='<%=request.getContextPath()%>/pc/getRewardDetailInfo?rewardid=${rewardinfo.rewardid}';
								});
					}
				})
	}
</script> 

<script type="text/javascript">

//评论分页
function pageHelper(num) {
	var param = new Object();
	var currentPage = num;
	param.currentPage = currentPage;
	param.orderid = '${map.rewardid}';
	param.resourcetype = 9;
	pageList(param);
}

//评论列表
function onloadDate() {
	var param = new Object();
	param.orderid = '${map.rewardid}';
	param.resourcetype = 9;
	addcommentAjax(param);//显示评论列表		
}

//新增评论

function checkComment(row) {
	var param = new Object();
	param.userid = '${userInfo.userid}';
	param.resourceid = '${map.rewardid}';
	param.resourcetype = 9;
	showComment(row, param,
			'/pc/getRewardDetailInfo?rewardid=${rewardinfo.rewardid}&resourcetype=9');//添加评论信息
}


</script>
<script type="text/javascript">
var relayiduserid = '${userInfo.userid}';
var relayidcompanyid ='${userInfo.companyid}';
var relayid = '${map.rewardid}';
var relaytype = 9;
</script>
</html>
