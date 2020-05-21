<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>综合星值</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/pc/script/organizeRange.js"></script>
	<!-- 上传图片需要js -->   
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>

	<!-- 上传文件需要js -->      
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.ui.widget.js"></script>
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.iframe-transport.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload.js"></script>  
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-ui.js"></script> 
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-process.js"></script>   
<script src="<%=request.getContextPath() %>/js/jqueryfile/jquery.fileupload-validate.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/change_examineuserinfo.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/pc_chaosongren.js"></script>

<script type="text/javascript">


function onloadDate(){
	var param = new Object();
	param.userid = "${user.userid}";
	param.companyid = "${user.companyid}";
	param.type =2;
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/pc/getOrganizeListByReleaseRange',
		data:param,
		success:function(data){
			if(data.status==0){
				if(data.organizelist.length > 0 && data.organizelist != ''){
					var temp="";
					for(var i=0;i<data.organizelist.length;i++){
						if(i==0){
							queryTemplateList(data.organizelist[i].organizeid);
						}
						temp+="<option   change=\"queryTemplateList("+data.organizelist[i].organizeid+")\" value=\""+data.organizelist[i].organizeid+"\" selected=\"selected\">"+data.organizelist[i].organizename+"</option>";
					}
					$('select[name=organizeid]').html(temp);
				}
			}
		}
	});
}
var stationtab="<tr class=\"none_border\"  id=\"stationtab\">"+
					"<td colspan=\"2\"><a href=\"javascript:void(0)\" class=\"a_btn bg_yellow\" onclick=\"$('#divadd').show();$('.div_mask').show();\">添加</a></td>"+
				    "<td class=\"t_r\">合计： <i class=\"yellow\" id=\"sumstarnum\" >0</i> 星</td>"+
				"</tr>";
function queryTemplateList(organizeid){
	var param = new Object();
	param.type = 2;
	param.companyid = '${user.companyid}';
	param.organizeid = organizeid;
	param.userid = "${user.userid}";
	$.ajax({
		type:'post',
		url:'<%=request.getContextPath()%>/pc/getStarEvaluateTemplateList',
		data:param,
		success:function(data){
			$('select[name=templateid]').html("");
			$('#stationt').html(stationtab);
			$("#Modulartab").html("");
			if(data.status==0){
				if(data.templatelist.length > 0 && data.templatelist != ''){
			
					var temp="";
					for(var i=0;i<data.templatelist.length;i++){
						if(i==0){
							showStarProject(data.templatelist[i].templateid);
						}
						temp+="<option value=\""+data.templatelist[i].templateid+"\" selected=\"selected\">"+data.templatelist[i].templatename+"</option>";
					}
					$('select[name=templateid]').html(temp);
				}
			}
		}
	});
}

function showStarProject(templateid){
	var param = new Object();
	param.companyid = '${user.companyid}';
	param.templateid = templateid;
	$.ajax({
		type:'post',
		url:'<%=request.getContextPath()%>/pc/getStarProjectByOrganize',
		data:param,
		success:function(data){
			$('#stationt').html(stationtab);
			$("#Modulartab").html("");
			if(data.status==0){
				if(data.projectlist.length > 0 && data.projectlist != ''){
					var temp = ""; 
					$.each(data.projectlist,function(i,map){
						var status = map.status;
						if(status == 1 || status == "1"){
							
						}else{
							temp += "	<tr>"+
							            	"<td>"+map.projectname+"</td>"+
							                "<td width=\"40\"><a href=\"javascript:void(0)\" class=\"blue\" onclick=\"checkAddModle('"+map.projectid+"','"+map.projectname+"')\">添加</a></td>"+
							            "</tr>";
						}
					});
					$("#Modulartab").html(temp);
				}
				
			}
		}
	});
}


function checkAddModle(projectid,projectname){
	if($('#'+projectid+'_project_li').html() == null || $('#'+projectid+'_project_li').html()==undefined){
		var temp="  <tr id=\""+projectid+"_project_li\">"+
    	"<td width=\"30\"><a href=\"javascript:void(0)\" onclick=\"checkRemove('"+projectid+"')\" class=\"ico_del\"><img src='<%=request.getContextPath() %>/userbackstage/images/pc_page/ico_del.png' /></a></td>"+
    	"<td class=\"l_name\">"+projectname+"</td>"+
        "<td class=\"t_r\"><div class=\"star_box\"><input type='hidden' id=\""+projectid+"_div\" projectid=\""+projectid+"\" projectname=\""+projectname+"\" status=\"0\" name=\"input_star\" sumNum=\"3\" />"+
	       " <a class=\"a_star\" onclick=\"xuanze('"+projectid+"','1')\"><img name=\"img_"+projectid+"\" id=\""+projectid+"_1\"  src=\"../userbackstage/images/pc_page/start2.png\" /></a>"+
	        "<a class=\"a_star\" onclick=\"xuanze('"+projectid+"','2')\"><img  name=\"img_"+projectid+"\" id=\""+projectid+"_2\" src=\"../userbackstage/images/pc_page/start2.png\" /></a>"+
	       " <a class=\"a_star\" onclick=\"xuanze('"+projectid+"','3')\"><img  name=\"img_"+projectid+"\" id=\""+projectid+"_3\" src=\"../userbackstage/images/pc_page/start2.png\" /></a>"+
	       " <a class=\"a_star\" onclick=\"xuanze('"+projectid+"','4')\"><img  name=\"img_"+projectid+"\" id=\""+projectid+"_4\" src=\"../userbackstage/images/pc_page/start3.png\" /></a>"+
	       "<a class=\"a_star\" onclick=\"xuanze('"+projectid+"','5')\"><img   name=\"img_"+projectid+"\" id=\""+projectid+"_5\" src=\"../userbackstage/images/pc_page/start3.png\" /></a>"+
	       '<a style="margin-left:20px;" id="'+projectid+'_starlevel">三星</a>'+
	    "</div></td>"+
	    "</tr>  "+ 
	     "<tr>"+
	       "<td>&nbsp;</td>"+
	       " <td colspan=\"2\"><textarea class=\"text_area\" placeholder=\"请输入详细内容...\" id=\""+projectid+"_textarea\"></textarea></td>"+
	   " </tr> " ;
		$('#stationtab').before(temp);
		var input = $('input[name="input_star"]');
		var sumstar = "0";
		$.each(input,function(k,item){
			sumstar = parseInt($(item).attr("sumNum")) + parseInt(sumstar);
		});
		$('#sumstarnum').text(sumstar);
	}
    $('.div_mask').hide();$('#divadd').hide();
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

//星级
function xuanze(i,k){
	$('img[name="img_'+i+'"]').attr("src","../userbackstage/images/pc_page/start3.png");
	for(var j=1;j<=k;j++){
		$('#'+i+"_"+j).attr("src","../userbackstage/images/pc_page/start2.png");
	}
	$('#'+i+'_div').attr("sumNum",k);
	$('#'+i+'_starlevel').html(dengji(k));
	
	var input = $('input[name="input_star"]');
	var sumstar = "0";
	$.each(input,function(k,item){
		sumstar = parseInt($(item).attr("sumNum")) + parseInt(sumstar);
	});
	$('#sumstarnum').text(sumstar);
}

//删除
function checkRemove(projectid){
	$('#'+projectid+'_project_li').next().remove();
	$('#'+projectid+'_project_li').remove();
	
	var input = $('input[name="input_star"]');
	var sumstar = "0";
	$.each(input,function(k,item){
		sumstar = parseInt($(item).attr("sumNum")) + parseInt(sumstar);
	});
	$('#sumstarnum').text(sumstar);
}

function checkRegion(obj){
	queryTemplateList($(obj).val());
}

function checkTemplate(obj){
	showStarProject($(obj).val());
}
//新增自评
function addEvaluate(){
	var param = {"starlist":[]}
	var input = $('input[name="input_star"]');
	$.each(input,function(k,item){
		var par = new Object();
		par.projectid = $(item).attr("projectid");
		par.starlevel = $(item).attr("sumNum");
		par.description = $('#'+par.projectid+'_textarea').val();
		par.projectname = $(item).attr("projectname");
		par.type = $(item).attr("status");
		param.starlist[k]=par;
	});
	
	var obj = new Object();
	obj.sumstar = $('#sumstarnum').text();
	obj.companyid = '${user.companyid}';
	obj.organizeid = $("#organizeidtext").val();
	obj.examineuserid = $('#examineuserid').val();
	obj.createid = '${user.userid}';
	obj.starlist = JSON.stringify(param);
	obj.templateid = $('#templateidtext').val();
	obj.templatename = $('#templateidtext').text();
	obj.organizename = $('#organizeidtext').text();
	
	obj.userlist = $("#CCuseridlist").val();
	obj.CCusernames = $('#CCusernamelist').val();
	
	if(input.length <= 0){
		swal({
			title : "",
			text : "自评项目不能为空！",
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
	
	if(obj.examineuserid == "" || obj.examineuserid == null){
		swal({
			title : "",
			text : "审核人不能为空！",
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
		url:"/pc/insertOverallvaluate",
		type:"post",
		data:obj,
		success:function(data){
			if(data.status == 0 || data.status == '0'){
				swal({
					title : "",
					text : "新增成功",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href="<%=request.getContextPath() %>/pc/allstarList";
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
    	<div class="page_name"><span>综合星值自评</span><a href="<%=request.getContextPath() %>/pc/allstarAdd" class="back">返回</a></div>
        <div class="page_tab2">            
            <div class="tab_list">
             <input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/>       	
			 <input type="file" style="display: none" name="myfiles" id="file_upload" multiple/>
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="none_border" style="font-size:12px;">
                    <tr>
                    	<td class="l_name">所属区域</td>
                        <td><select class="text" id="organizeidtext"  name="organizeid"  readonly="readonly" onchange="checkRegion(this)"><option>请选择所属区域</select></td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name">岗位名称</td>
                        <td><select class="text" id="templateidtext"  name="templateid" readonly="readonly" onchange="checkTemplate(this)"><option>请选择岗位</select></td>
                    </tr>
                        <tr class="wlmx_detail">
                    	<td class="l_name">岗位星值</td>
                        <td class="mx_tab">
                        	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="mx_td"  style="font-size:12px;" id="stationt">
                              	
                                <tr class="none_border"  id="stationtab">
                                  	<td colspan="2"><a href="javascript:void(0)" class="a_btn bg_yellow" onclick="$('#divadd').show();$('.div_mask').show();">添加</a></td>
                                    <td class="t_r">合计： <i class="yellow" id="sumstarnum">0</i> 星</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                       <tr class="first_td">
                    	<td class="l_name">审批人</td>
                    	<input type="hidden" id="examineuserid" >
                        <td class="img_td"><div class="img f"><img src="../userbackstage/images/pc_page/user_img2.png" id="examineheadimage" width="30" height="30" /></div><i class="i_name" id="examinename"></i><a href="javascript:void(0)" onclick="openOrCloseExamineDiv()" class="add_user">添加</a></td>
                    </tr>
                    <tr>
                    	<td class="l_name">申请人</td>
                        <td class="img_td"><div class="img f"><img src="${user.headimage }" width="30" height="30" /></div><i class="i_name">${user.realname }</i></td>
                    </tr>
                    <tr>
                    	<td class="l_name">抄送人</td>
                        <td class="img_td" id="CCusernames"><a href="javascript:void(0)" onclick="showCCuserOrganize()" class="add_user">添加</a></td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name">填写时间</td>
                        <td>${createtime}</td>
                    </tr>
                    <tr class="foot_td">
                    	<td>&nbsp;</td>
                        <td><a href="javascript:void(0)" class="a_btn bg_yellow" onclick="addEvaluate()">发送</a><a href="<%=request.getContextPath()%>/pc/allstarList" class="a_btn bg_gay2">取消</a></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>

<div class="div_mask" style="display:none;"></div>
<div class="tc_worksheet" style="display:none; margin-top:-150px;" id="divadd">
	<div class="tc_title"><span>综合评分项目</span><a href="javascript:void(0)"  onclick="$('.div_mask').hide();$('#divadd').hide();">×</a></div>
    <div class="tab_list">
    	<table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;" id="Modulartab">

        </table>
    </div>
</div>
</body>
</html>
