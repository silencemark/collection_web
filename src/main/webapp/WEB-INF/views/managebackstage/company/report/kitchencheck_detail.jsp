<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>厨房检查单</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/app/appcssjs/sweetalert/dist/sweetalert.css" />
<script src="<%=request.getContextPath()%>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
</head>

<script type="text/javascript">

var pageWidth = window.innerWidth;
var pageHeight = window.innerHeight;
var minHeight;
var usernameWidth;
if (typeof pageWidth != "number")
{
	if(document.compatMode == "CSS1Compat")
	{
		pageWidth = document.documentElement.clientWidth;
		pageHeight = document.documentElement.clientHeight;
	}
	else
	{
		pageWidth = document.body.clientWidth;
		pageHeight = document.body.clientHeight;
	}
}
minHeight = pageHeight - 77;
$(document).ready(function(){		
	$(".main_page").css("min-height",minHeight+"px");
	
	usernameWidth = $(".top_username").width();		
	usernameWidth = (160 - usernameWidth)/2;
    $(".top_username").css("margin-left",usernameWidth+"px");		
	$(".head_box .user_box").hover(
		  function () {
			$(".head_box .user_box .name").addClass("name_border"); //移入
			$(".head_box .user_box .box").show();
		  },
		  function () {
			$(".head_box .user_box .name").removeClass("name_border");//移除
			$(".head_box .user_box .box").hide();
		  }
		);			
});
$(document).ready(function(){
$('#company').parent().parent().find("span").attr("class","bg_hidden");
$('#company').attr('class','active li_active');
})
	function onloaddata() {
		getSendName('${map.forwarduserid}',"#sendname");
	}
</script>

<body onload="onloaddata()">
<jsp:include page="../../top.jsp"></jsp:include>
<div class="main_page">
	<jsp:include page="../../left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList">企业信息列表</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyInfo?companyid=${map.companyid}">公司信息</a><i>/</i><a href="javascript:void(0)" onclick="window.history.go(-1)">报表管理/营业额</a><i>/</i><span>厨房检查单详情</span></p></div>
    	</div>
        <div class="page_tab">            
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
    </div>
    <div class="clear"></div>
</div>

</body>
<script type="text/javascript">
	
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

</script>
</html>
