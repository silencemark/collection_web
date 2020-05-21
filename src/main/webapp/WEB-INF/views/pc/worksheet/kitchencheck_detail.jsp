<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>厨房检查单</title>
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
	<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/relaycomment.js"></script>
</head>

<script type="text/javascript">
	$(document).ready(function() {
		$('.sup_li').find("li").attr('class', '');
		$('#kitchencheck').attr('class', 'active');
		$('#homepage').parent().find("li").attr('class', '');
		$('#homepage').attr('class', 'active');
	})
	function onloaddata() {
		getSendName('${map.forwarduserid}',"#sendname");
		readstatus('${KitchenInfo.inspectid}', '${userInfo.userid}');
	}
</script>

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
        url: "<%=request.getContextPath() %>/pc/exportKitchenDetailList",
        data: 'inspectid=${KitchenInfo.inspectid}',
        success: function(data){
        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
        }
    });
	});
}



</script>
<body onload="onloaddata()">
<jsp:include page="../top.jsp"></jsp:include>

<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>厨房检查单详情</span><a href="javascript:void(0)" onclick="window.history.go(-1)" class="back">返回</a>
    	<a href="javascript:void(0)" onclick="exportexcel()">导出</a>
    	<c:if test="${KitchenInfo.examineuserid!=userInfo.userid || KitchenInfo.status!=0 }">
    	<a href="javascript:void(0)" onclick="showForwardDiv()">转发</a>
    	</c:if>
    	</div>
        <div class="page_tab2">            
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="none_border">
                    <tr>
                    	<td class="l_name">所属架构</td>
                        <td>${KitchenInfo.organizename}</td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name">检查区域</td>
                        <td>${KitchenInfo.templatename}</td>
                    </tr>
                    <tr class="wlmx_detail">
                    	<td class="l_name">检查项目</td>
                        <td class="mx_tab">
                        	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="mx_td">
                           	
                                <tbody id="evaluate_list"></tbody>                                                                   
                                <tr class="none_border">
                                	<td>&nbsp;</td>
                                    <td class="t_r">
										<div class="star_box">
									    	<span><i style="display: none;" id="sumstar">0</i></span>
										    <li class="n_bor">
											    <b>
											    	<a id="showStarLevel"></a>
												    <font style="margin-left:10px;" id="star_level"></font>
												</b>
											    <div class="clear"></div>
										    </li>
									    </div>
									</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                    	<td class="l_name">检查人</td>
                        <td class="img_td"><div class="img f"><img src="${KitchenInfo.createheadimage}" width="30" height="30" /></div><i class="i_name">${KitchenInfo.createname}</i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">填写时间</td>
                        <td>${KitchenInfo.createtime}</td>
                     
                    </tr>
                    <tr>
                    	<td class="l_name">审核人</td>
                        <td class="img_td"><div class="img f"><img src="${KitchenInfo.examineheadimage}" width="30" height="30" /></div><i class="i_name">${KitchenInfo.examinename}</i></td>
                        </tr>
                        <tr>
                    	<td class="l_name">抄送人</td>
                        <td class="img_td">
                        	<c:forEach items="${KitchenInfo.ccuserlist }" var="map">
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
                    	<c:when test="${KitchenInfo.examineuserid==userInfo.userid && KitchenInfo.status==0 }">
	                    <tr class="none_border">
	                    	<td class="l_name2">审核人意见</td>
	                        <td><textarea class="text_area" placeholder="请输入抄送人意见，最多允许输入800字符" maxlength="800" id="opinion"></textarea></td>
	                    </tr>
	                    <tr>
	                    	<td>&nbsp;</td>
	                        <td><a href="javascript:void(0)" onclick="examineorder(1)" class="a_btn bg_yellow">发送</a>
	                        <a href="javascript:void(0)" onclick="examineorder(0)" class="a_btn bg_gay2">取消</a></td>
	                    </tr>
                    	</c:when>
                    	<c:otherwise>
                    	<tr class="none_border">
	                    	<td class="l_name2">审核人意见</td>
	                        <td>${KitchenInfo.opinion }</td>
	                    </tr>
                    	</c:otherwise>
                    </c:choose>
                </table>
            </div>
        </div>
        <div class="comment">
        	<div class="name">共有<i class="yellow" id="num"></i>条评论</div>
            <div class="text_box" >
            <c:if test="${KitchenInfo.examineuserid!=userInfo.userid || KitchenInfo.status!=0 }">
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

</body>
<script type="text/javascript">
	//显示发布范围

	$(function() {
		onloadDate();

	})

	

	//评论分页
	function pageHelper(num) {
		var param = new Object();
		var currentPage = num;
		param.currentPage = currentPage;
		param.orderid = '${map.inspectid}';
		param.resourcetype = 12;
		pageList(param);
	}

	//评论列表
	function onloadDate() {
		var param = new Object();
		param.orderid = '${map.inspectid}';
		param.resourcetype = 12;
		addcommentAjax(param);//显示评论列表		
	} 

	//新增评论
	function checkComment(row) {
		var param = new Object();
		param.userid = '${userInfo.userid}';
		param.resourceid = '${map.inspectid}';
		param.resourcetype = 12;
		showComment(row, param,
				'/pc/getKitchenInfo?inspectid=${KitchenInfo.inspectid}&resourcetype=12');//添加评论信息
	}

$(function(){
	var starlist = '${KitchenInfo.starlist}';
	if(starlist != null && starlist != ''){
		starlist = JSON.parse(starlist);
		showtemplate(starlist);
	}
});

function showtemplate(starlist){
	//显示审核项目
	if(starlist.length > 0 && starlist != null){
		
		var sumstar = 0;
		var addnum = 0;
		$.each(starlist,function(i,map){
			var status = map.status;
			if(status == 1 || status == "1"){
				var htm = '<tr>'+
								'<td style="font-weight:bold;">'+(map.projectname?map.projectname:"&nbsp;")+'</td>'+
								'<td class="t_r">'+
									'<div class="star_box"></div>'+
								'</td>'+
							'</tr>';
					$('#evaluate_list').append(htm);
			}else{
				var xing = "";
				var starlevel = map.starlevel;
				for(var g=0;g<5;g++){
					if(g<starlevel){
						xing += '<img src="../userbackstage/images/pc_page/start2.png"/>';
					}else{
						xing += '<img src="../userbackstage/images/pc_page/start3.png"/>';
					}
				}
				var htm = '<tr>'+
							'<td>'+(map.projectname?map.projectname:"&nbsp;")+'</td>'+
							'<td class="t_r">'+
								'<div class="star_box">'+xing+'<a style="margin-left:20px;">'+dengji(starlevel)+'</a></div>'+
							'</td>'+
						'</tr>';
				sumstar += starlevel;
				addnum++;
				$('#evaluate_list').append(htm);
			}
		});
		$('#sumstar').text(sumstar);
		$('#showStarLevel').html(showStarLevel(sumstar,addnum));
	}
}

function dengji(level){
	level = parseInt(level);
	if(level == 1){
		return "很差";
	}else if(level == 2){
		return "不差";
	}else if(level == 3){
		return "一般";
	}else if(level == 4){
		return "&nbsp;&nbsp;&nbsp;好";
	}else if(level == 5){
		return "很好";
	}
}

function showStarLevel(sumstar,addnum){
	var intnum = parseInt(sumstar/addnum);
	var floatnum = (sumstar%addnum)/addnum;
	/* var shownum = intnum;//显示的等级
	if(floatnum > 0){
		shownum++;
	}
	var html = "";
	for(var i=0 ; i<5 ; i++){
		if(i<intnum){
			html += '<em><img src="/app/appcssjs/images/public/start2.png"></em>';
		}else if(floatnum <= 0.2 && floatnum != 0 && i == intnum){
			html += '<em><img src="/app/appcssjs/images/public/start_02.png"></em>';
		}else if(floatnum <= 0.4 && floatnum != 0 && i == intnum){
			html += '<em><img src="/app/appcssjs/images/public/start_04.png"></em>';
		}else if(floatnum <= 0.6 && floatnum != 0 && i == intnum){
			html += '<em><img src="/app/appcssjs/images/public/start_06.png"></em>';
		}else if(floatnum <= 0.8 && floatnum != 0 && i == intnum){
			html += '<em><img src="/app/appcssjs/images/public/start_08.png"></em>';
		}else if(floatnum > 0.8 && floatnum != 0 && i == intnum){
			html += '<em><img src="/app/appcssjs/images/public/start2.png"></em>';
		}else{
			html += '<em><img src="/app/appcssjs/images/public/start3.png"></em>';
		}
	} */
	$('#star_level').html((intnum+floatnum).toFixed(1)+"分");
	return "综合评价：";
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
			url:'<%=request.getContextPath()%>/pc/examineKitchen',
			data:'inspectid=${KitchenInfo.inspectid}&userid=${userInfo.userid}&result='+result+'&opinion='+$('#opinion').val(),
			success:function(data){
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
						location.href='<%=request.getContextPath() %>/pc/getKitchenList';
					});
				}
		})
	}


var relayiduserid = '${userInfo.userid}';
var relayidcompanyid ='${userInfo.companyid}';
var relayid = '${map.inspectid}';
var relaytype = 12;
</script>
</html>
