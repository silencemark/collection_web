<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>综合星值-审核</title>
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
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organizeRangeRedioPC.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/relaycomment.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript">
var relayiduserid = '${user.userid}';
var relayidcompanyid ='${user.companyid}';
var relayid = '${map.overallvaluateid}';
var relaytype =  7;

var examine_createid = "";
$.ajax({
	type:'post',
	url:'/pc/getOverallvaluateInfo?overallvaluateid=${map.overallvaluateid}',
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
			if(data.datamap!=null){
				$("#organizename").text(data.datamap.organizename);
				$("#identityname").text(data.datamap.identityname);
				if(data.datamap.result==1){
					$("#consent").attr("src","../userbackstage/images/pc_page/agree_img.png");
				}else if(data.datamap.result==2){
					$("#consent").attr("src","../userbackstage/images/pc_page/agree_img2.png");
				}
				if(data.datalist!=null){
					var temp ="";
					for(var i=0;i<data.datalist.length;i++){
						temp+="  <tr>"+
		                        	"<td>"+data.datalist[i].projectname+"</td>";
		                var xing = "";
		                var starlevel = data.datalist[i].starlevel;
						for(var j=1;j<=5;j++){
							if(j<=starlevel){
								xing += '<em onclick="xuanze(\''+data.datalist[i].starid+'\',\''+j+'\')"><img id="'+data.datalist[i].starid+'_'+j+'" name="img_'+data.datalist[i].starid+'" src="../userbackstage/images/pc_page/start2.png"></em>';
							}else{
								xing += '<em onclick="xuanze(\''+data.datalist[i].starid+'\',\''+j+'\')"><img id="'+data.datalist[i].starid+'_'+j+'" name="img_'+data.datalist[i].starid+'" src="../userbackstage/images/pc_page/start3.png"></em>';
							}
						}

						temp+="<td class=\"t_r\"><div class=\"star_box\">"+
								'<input type="hidden" name="input_star" id="'+data.datalist[i].starid+'_input" starid="'+data.datalist[i].starid+'" sumNum="'+starlevel+'"/>'+
								xing+
								'<a style="margin-left:20px;" id="'+data.datalist[i].starid+'_starlevel">'+dengji(starlevel)+'</a>'+
								"</div></td></tr>"+
		                        "<tr><td colspan=\"2\"><textarea class=\"text_area\" placeholder=\"请输入详细内容...\" id=\""+data.datalist[i].starid+"_textarea\">"+data.datalist[i].description+"</textarea></td></tr>";
		                 if(i==data.datalist.length-1){
		                	 temp+="<tr class=\"none_border\">"+
		                	 			"<td>&nbsp;</td>"+
			                            "<td class=\"t_r\">合计<i class=\"yellow\" id=\"sumstarnum\">"+data.datamap.sumstar+"星</i></td>"+
			                        "</tr>";
		                 }
					}
					$("#mx_td").html(temp);
				}
				$("#createheadimage").attr("src",data.datamap.createheadimage);
				$("#createname").text(data.datamap.createname);
				$("#createtime").text(data.datamap.createtime);
				$("#examineheadimage").attr("src",data.datamap.examineheadimage);
				$("#examinename").text(data.datamap.examinename);
				$("#opinion").text(data.datamap.opinion);
				examine_createid = data.createid;
				
				showCCuserInfo(data.datamap.ccuserlist);
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

function dengji(level){
	level = parseInt(level);
	/* if(level == 1){
		return "很差";
	}else if(level == 2){
		return "不差";
	}else if(level == 3){
		return "一般";
	}else if(level == 4){
		return "&nbsp;&nbsp;&nbsp;好";
	}else if(level == 5){
		return "很好";
	} */
	if(level == 1){
		return "一星";
	}else if(level == 2){
		return "二星";
	}else if(level == 3){
		return "三星";
	}else if(level == 4){
		return "四星";
	}else if(level == 5){
		return "五星";
	}
}

//选择项目星级
function xuanze(i,k){
	$('img[name="img_'+i+'"]').attr("src","/userbackstage/images/pc_page/start3.png");
	for(var j=1;j<=k;j++){
		$('#'+i+"_"+j).attr("src","/userbackstage/images/pc_page/start2.png");
	}
	$('#'+i+'_input').attr("sumNum",k);
	$('#'+i+'_starlevel').html(dengji(k));
	
	var input = $('input[name="input_star"]');
	var sumstar = "0";
	$.each(input,function(k,item){
		var bn = $(item).attr("sumNum");
		sumstar = parseInt(bn) + parseInt(sumstar);
	});
	$('#sumstarnum').text(sumstar);
}

//评论分页
function pageHelper(num){
	var param =new  Object();
	var currentPage = num;
	param.currentPage=currentPage;
	param.orderid='${map.overallvaluateid}';
	param.resourcetype=  7;
	pageList(param);
}

//评论列表
function onloadDate(){
	var param =new  Object();
	param.orderid='${map.overallvaluateid}';
	param.resourcetype= 7;
	addcommentAjax(param);//显示评论列表		
}

//新增评论
function checkComment(row){
	var param = new Object();
	param.userid = '${user.userid}';
	param.resourceid = '${map.overallvaluateid}';
	param.resourcetype = 7;
	showComment(row,param,'');//添加评论信息
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
		        url: "<%=request.getContextPath() %>/pc/exportOverallDetail?overallvaluateid=${map.overallvaluateid}",
		        success: function(data){
		        	window.location.href="<%=request.getContextPath() %>/userbackstage/downloadexcel?fileName="+data;
		        }
		    });
		});
		
	}
	

function examineInfo(result){
	var param = new Object();
	
	var starlist = {"starlist":[]};
	var input = $('input[name="input_star"]');
	$.each(input,function(i,item){
		var star = new Object();
		star.starid = $(item).attr("starid");
		star.overallvaluateid = overallvaluateid;
		star.starlevel = $(item).attr("sumNum");
		star.description = $('#'+star.starid+'_textarea').val();
		starlist.starlist[i] = star;
	});
	
	param.overallvaluateid ='${map.overallvaluateid}';
	param.userid = '${user.userid}';
	param.opinion = $('#opinion').val();
	param.result = result;
	param.opinion = $('#opinion').val();
	
	obj.sumstar = $('#sumstarnum').text();
	obj.starlist = JSON.stringify(starlist);
	
	if(param.opinion == ""){
		swal({
			title : "",
			text : "审批意见不能为空",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
		return false;
	}
	$.ajax({
		url:"/pc/examineOverallvaluate",
		type:"post",
		data:param,
		success:function(resultMap){
			if(resultMap.status == 0 || resultMap.status == '0'){
				changeIsread(examine_createid,param.overallvaluateid);
				var title=$('#examinename').text()+"审批了您的综合星值自评,请及时查看";
				var url="/kpi/allstar_detail.html?overallvaluateid=${map.overallvaluateid}&userid="+examine_createid;
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
					location.href='<%=request.getContextPath() %>/pc/allstarList';
				});
			}
		}
	});
}
$(document).ready(function(){
	$('.kpi_li').find("li").attr('class','');
	$('#allsstar').attr('class','active');
});
</script>
</head>

<body onload="onloadDate()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>综合星值详情</span>
    	<a href="<%=request.getContextPath()%>/pc/allstarList" class="back">返回</a>
    	<a href="javascript:void(0)" onclick="exportexcel()">导出</a>
<!--     	<a href="javascript:void(0)" onclick="showForwardDiv()">转发</a> -->
    	</div>
        <div class="page_tab2">  
        	<div class="check_state"><img src="" id="consent"/></div>          
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">
                   <tr>
                    	<td class="l_name">所属区域</td>
                        <td><span id="organizename"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name">岗位名称</td>
                        <td><span id="identityname"></span></td>
                    </tr>
                    <tr>
                    	<td class="l_name">综合星值</td>
                        <td class="mx_tab">
                        	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="mx_td" id="mx_td" style="font-size:12px;">
                          	
                            </table>
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
                    	<td><textarea class="text_area" placeholder="请输入审批意见，最多允许输入800字符" maxlength="800"id="opinion"></textarea></td>
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
