<%@page import="com.collection.util.UserUtil"%>
<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>首页-使用方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organize.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organizeRadio.js"></script>

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
				$(".head_box .user_box .name").removeClass("name_border");//移除\
				$(".head_box .user_box .box").hide();
			  }
			);			
});
function callbackfunc(organizeid){
	
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=request.getContextPath() %>/userbackstage/getOrganizeInfo",
		data:"organizeid="+organizeid,
		success:function(data){
			var addr = data.address;
			if(addr == undefined){
				data.address = "";
			}
			temp="<tr class=\"head_td\">"+
		        	"<td width=\"160\">"+data.organizename+"</td>"+
		            "<td class=\"t_r\"><a href=\"javascript:void(0)\" class=\"blue\" onclick=\"initupdate('"+data.organizeid+"','"+data.organizename+"','"+data.parentname+"','"+data.parentid+"',"+data.type+",'"+data.address+"','"+data.priority+"')\">修改</a></td>"+
		        "</tr>"+
		        "<tr>"+
		        	"<td>员工数</td>"+
		            "<td class=\"gray_word\">"+data.usernum+"人</td>"+
		        "</tr>";
	        if(data.type==3){
	        	temp+="<tr>"+
		        	"<td>地址</td>"+
		            "<td class=\"gray_word\">"+data.address+"</td>"+
		        "</tr>"+
		        "<tr>"+
		        	"<td>二维码</td>"+
		            "<td><img src=\"../userbackstage/images/public/ico_ewm.png\" />&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"$('.tc_ewmxx').show();$('.div_mask').show();\" class=\"blue\">查看</a></td>"+
		        "</tr>";
	       		$('#qrcodeimage').attr("src",data.qrcode);
	        }
	        $('#organizediv').html(temp);
	        
		}
	})
}

 

$(document).ready(function(){
	$('#nav_organize').attr('class','active');$('#nav_organize').parent().parent().show();
})
</script>
</head>
<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="<%=request.getContextPath() %>/userbackstage/index">首页</a><i>/</i><a href="#">企业管理</a><i>/</i><span>组织架构</span></p></div>  
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1">组织架构</span><a href="javascript:void(0)" onclick="initadd()">新建</a></div>
        <div class="worksheet">
        	<div class="l_tree" id="organizetree"  style="overflow:hidden;overflow-y:visible;">
            </div>
            <div class="r_list">
            	<div class="tab_list">
                	<table width="100%" border="0" cellpadding="0" cellspacing="0" id="organizediv">
                    </table>
                </div>               
            </div>
            <div class="clear"></div>
        </div>
    </div>
</div>
<div class="div_mask" style="display:none;"></div>
<div class="tc_addstru" style="display:none;height:auto;">
	<div class="tc_title"><span>新建组织架构</span><a href="javascript:void(0)" onclick="$('.tc_addstru').hide();$('.div_mask').hide();">×</a></div>
    <div class="box">
    	<input type="hidden" id="organizeid1"/>
    	<span><input type="text" class="text" placeholder="请输入组织或职务" id="organizename1"/></span>
    	<input type="hidden" id="parentid"/>
        <span><input type="text" class="text" placeholder="请选择上级部门/区域/店面" id="parentname" readonly="readonly" onclick="$('.tc_addstru').hide();$('.tc_structure').show();" /></span>
        <input type="hidden" id="type"/>
        <span><a href="javascript:void(0)" class="radio" id="quyu" onclick="checktype(this,1);">选择</a><i>区域</i><a href="javascript:void(0)"class="radio_ed" id="bumen" onclick="checktype(this,2);">选择</a><i>部门</i><a href="javascript:void(0)" class="radio" id="dianmian" onclick="checktype(this,3);">选择</a><i>店面</i></span>
    	<span><input type="text" class="text" placeholder="请安排排序号" id="priority"/></span>
    	<span><input type="text" class="text" placeholder="请输入详细地址" id="address" style="display: none; "/></span>
    </div>
    <div class="tc_btnbox"><a href="javascript:void(0)" onclick="$('.div_mask').hide();$('.tc_addstru').hide();" class="bg_gay2">取消</a>
    <a href="javascript:void(0)" onclick="submitdata()" class="bg_yellow">保存</a></div>
</div>

<div class="tc_structure" style="display:none;max-height:60%;overflow:hidden;overflow-y:visible;">
	<div class="tc_title"><span>选择组织架构店</span><a href="javascript:void(0);" onclick="$('.tc_structure').hide();$('.div_mask').hide();" >×</a></div>
	<input type="hidden" id="organizeid" />
	<input type="hidden" id="organizename" />
    <div id="organizetree1"  style="overflow:hidden;overflow-y:visible;"></div>
    <div class="tc_btnbox"><a href="javascript:void(0);" onclick="$('.tc_structure').hide();$('.tc_addstru').show();"  class="bg_gay2">取消</a>
    <a href="javascript:void(0)" onclick="chooseorganizeuser()"  class="bg_yellow" id="addchooseorganizeuser">确定</a>
    </div>
</div>

<div class="tc_ewmxx" style="display:none;">
	<div class="tc_title none_border"><a href="javascript:void(0);" onclick="$('.tc_ewmxx').hide();$('.div_mask').hide();">×</a></div>
    <div class="ewm_img"><img src="../userbackstage/images/public/ewm.jpg" width="262" height="262" id="qrcodeimage" /></div>
    <div class="ewm_txt"><span>客户满意度二维码</span><a href="javascript:void(0)" onclick="downloadewm()" class="yellow">下载</a></div>
</div>
<script type="text/javascript">
function initupdate(organizeid,organizename,parentname,parentid,type,address,priority){
	$('#organizeid1').val(organizeid);
	$('#organizename1').val(organizename);
	$('#parentname').val(parentname);
	$('#parentid').val(parentid);
	$('#priority').val(priority);
	if(type==1){
		$('#quyu').attr("class","radio_ed");
		$('#bumen').attr("class","radio");
		$('#dianmian').attr("class","radio");
		$('#address').hide();
		$('#address').val("");
	}else if(type==2){
		$('#quyu').attr("class","radio");
		$('#bumen').attr("class","radio_ed");
		$('#dianmian').attr("class","radio");
		$('#address').hide();
		$('#address').val("");
	}else if(type==3){
		$('#quyu').attr("class","radio");
		$('#bumen').attr("class","radio");
		$('#dianmian').attr("class","radio_ed");
		$('#address').show();
		$('#address').val(address);
	}
	$('#type').val(type);
	$('.tc_addstru').show();
	$('.div_mask').show();
}
function downloadewm(){
	var fileName=$('#qrcodeimage').attr('src');
	window.location.href="<%=request.getContextPath()%>/userbackstage/download?fileName="+fileName;
}
function submitdata(){
	var organizeid=$('#organizeid1').val();
	
	var organizename=$('#organizename1').val();
	var parentid=$('#parentid').val();
	var type=$('#type').val();
	var address=$('#address').val();
	if(organizename==""){
		swal({
			title : "",
			text : "请先填写组织名称",
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
	if(parentid==""){
		swal({
			title : "",
			text : "请先选择上级组织",
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
	if(type==3 && address==""){
// 		swal({
// 			title : "",
// 			text : "请输入店面详细地址",
// 			type : "error",
// 			showCancelButton : false,
// 			confirmButtonColor : "#ff7922",
// 			confirmButtonText : "确认",
// 			cancelButtonText : "取消",
// 			closeOnConfirm : true
// 		}, function(){
// 		});
// 		return;
	}
	
	if(organizeid != ""){
		var param = new Object();
		//需要修改的数据
/* 		param.lastdatacode = datacode;
		param.lastcompanyid = companyid; */
		param.lastorganizeid =organizeid;
		param.organizename = organizename;
		
		//需要修改到当前组织下
		param.updataorganizeid =parentid;
/* 		param.updatadatacode = $("#datacode").val();
		param.parentid = parentid; */

		param.updatatype = type;
		param.address = address;
		param.priority = $('#priority').val();

		$.ajax({
			type:'post',
			dataType:'json',
			url:'<%=request.getContextPath()%>/userbackstage/updateOrganize',
			data:param,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text : data.message,
						type : "error",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						//location.reload();
					});
				}else{
					swal({
						title : "",
						text : data.message,
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						location.reload();
					});
				}
				
			}
		})
	}else{
		//新增
		var param = new Object();
		param.updataorganizeid =parentid;//parentid
		param.address=address;
		param.organizename = organizename;//公司名称
		param.type = type;//type
		param.priority = $('#priority').val();
		
		$.ajax({
			type:'post',
			dataType:'json',
			url:'<%=request.getContextPath()%>/userbackstage/insertOrganize',
			data:param,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text : data.message,
						type : "error",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						//location.reload();
					});
				}else{
					swal({
						title : "",
						text : data.message,
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						location.reload();
					});
				}
				
			}
		})
	}
}
function chooseorganizeuser(){
	if($('#organizeid').val()==""){
		swal({
			title : "",
			text : "请选择组织架构",
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
	$('#parentid').val($('#organizeid').val());
	$('#parentname').val($('#organizename').val());
	$('.tc_addstru').show();
	$('.tc_structure').hide();
	
	var param = new Object();
	param.organizeid = $('#organizeid').val();
	$.ajax({
		url:"/userbackstage/getParentOrganizeMaxPriority",
		type:"post",
		data:param,
		async:false,
		success:function(data){
			if(data != ""){
				$('#priority').val(parseInt(data)+1);
			}
		}
	});
}
function initadd(){
	$('#organizetree1').find('a[class=checked]').attr("class","check");
	$('#organizeid1').val("");
	$('#organizename1').val("");
	$('#parentname').val("");
	$('#type').val(1);
	$('#address').hide();
	$('#quyu').attr("class","radio");
	$('#bumen').attr("class","radio_ed");
	$('#dianmian').attr("class","radio");
	$('#address').val("");
	$('#priority').val("");
	$('.tc_addstru').show();
	$('.div_mask').show();
}
function checktype(obj,type){
	$(obj).parent().find("a").attr("class","radio");
	$(obj).attr("class","radio_ed");
	$('#type').val(type);
	if(type==3){
		$('#address').show();
	}else{
		$('#address').hide();
	}
}
</script>
</body>
</html>
