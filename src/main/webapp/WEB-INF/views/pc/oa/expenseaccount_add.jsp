<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报销单新建</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/change_examineuserinfo.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/pc_chaosongren.js"></script>

</head>
<script type="text/javascript">
$(document).ready(function(){
	$('.oa_li').find("li").attr('class','');
	$('#expenseActive').attr('class','active');
});
function onloaddata(){
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/pc/initInsertExpense?companyid=${userInfo.companyid}',
		success:function(data){
			if(data.status==0){
				$("#orderno").text(data.expenseno);
			}
		}
	});
}
</script>
<body onload="onloaddata()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>新建报销单</span><a href="<%=request.getContextPath() %>/pc/getPcOaExpenseList" class="back">返回</a></div>
        <div class="page_tab2">            
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="none_border">
                    <tr>
                    	<td class="l_name">报销单编号</td>
                        <td class="num_xx"> <span id="orderno" ></span></td>
                    </tr>
                    <tr class="wlmx_detail">
                    	<td class="l_name">物料明细</td>
                        <td class="mx_tab">
                        	<a href="javascript:void(0)" class="a_btn bg_gay1 m_bt10" onclick="initmaterial()" id="initmaterial">添加</a>
                            <div class="clear"></div>
                        	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="mx_td" id="materialul">
                            	<tr class="gray_bg">
                                	<td width="20%">类别</td>
                                    <td width="40%">明细</td>
                                    <td width="30%">金额</td>                           
                                    <td>操作</td>
                                </tr>
                                                           
                                <tr class="none_border" id="materall">
                                	<input type="hidden" name="maternum" value="0"/>
                                	<td colspan="3">共 <i class="yellow" id="maternum">0</i> 项</td>
                                	<input type="hidden" name="materialprice" value="0"/>
                                    <td colspan="3" class="t_r">合计￥<i class="yellow" id="materialprice">0</i></td>
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
                    	<td class="l_name">抄送人</td>
                        <td class="img_td" id="CCusernames"><a href="javascript:void(0)" onclick="showCCuserOrganize()" class="add_user">添加</a></td>
                    </tr>
                    <tr>
                    	<td class="l_name">申购人</td>
                    	<input type="hidden" name="createid" value="${userInfo.userid}"/>
                        <td class="img_td"><div class="img f"><img src="${userInfo.headimage}" width="30" height="30" /></div><i class="i_name">${userInfo.realname}</i></td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name">填写时间</td>
                    	<input type="hidden" name="createtime" value="${createtime}"/>
                        <td>${createtime}</td>
                    </tr>
                    <tr class="foot_td">
                    	<td>&nbsp;</td>
                        <td><a href="javascript:void(0)" class="a_btn bg_yellow" onclick="submitdata()">发送</a><a href="javascript:void(0)" onclick="goBackPage()"  class="a_btn bg_gay2">取消</a></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>
<div class="div_mask" style="display:none;"></div>
<div class="tc_wlmxbox" style="margin-top:-204px;display: none;">
	<div class="tc_title"><span>新增物料</span><a href="javascript:void" onclick="$('.div_mask').hide();$('.tc_wlmxbox').hide();">×</a></div>
    <div class="tab_list">
    	<table width="100%" border="0" cellpadding="0" cellspacing="0" id="addul">
        	<tr>
            	<td class="l_name" width="40">类型</td>
                <td><input type="text" class="text" name="type"/></td>
            </tr>
            <tr>
            	<td class="l_name" width="40">明细</td>
                <td><input type="text" class="text"  name="detail"/></td>
            </tr>
            <tr>
            	<td class="l_name" width="40">单价</td>
                <td><input type="text" class="text"  name="price" oninput="jisuanprice()"/></td>
            </tr>
        </table>
    </div>
    <div class="tc_btnbox"><a href="javascript:void(0)" onclick="$('.div_mask').hide();$('.tc_wlmxbox').hide();" class="bg_gay2">取消</a><a href="javascript:void(0)" onclick="addmaterial()" class="bg_yellow">确定</a></div>
</div>
<script type="text/javascript">
function initmaterial(){
	$(".div_mask").show();
	$(".tc_wlmxbox").show();
	$('#addul input[name=type]').val("");
	$('#addul input[name=detail]').val("");
	$('#addul input[name=price]').val("");
}
function addmaterial(){
	if($('#addul input[name=type]').val()=="" ||
			$('#addul input[name=detail]').val()=="" ||
			$('#addul input[name=price]').val()=="" ){
			swal({
				title : "",
				text : "请填写完整后再提交",
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
		var temp="<tr>"+
				"<td><em name='type'>"+$('#addul input[name=type]').val()+"</em></td>"+
			    "<td><em name='detail'>"+$('#addul input[name=detail]').val()+"</em></td>"+
			    "<td><em name='price'>"+$('#addul input[name=price]').val()+"</em></td>"+
			    "<td><a href=\"javascript:void(0)\" class=\"red\" onclick=\"deleteorder(this,'"+$('#addul input[name=price]').val()+"')\">删除</a></td>"+
			"</tr>";
		
	     var materialprice=parseFloat($('#materialprice').text())+parseFloat($('#addul input[name=price]').val());
	     $('#materialprice').text(materialprice);
	     $('input[name=materialprice]').val(materialprice);
	     $('#materall').before(temp);
	     
	     var maternum=$('#materialul tr').length-2;
	     $('#maternum').text(maternum);
	     $('input[name=maternum]').val(maternum);
	     $(".div_mask").hide();
	 	 $(".tc_wlmxbox").hide();
}
function deleteorder(obj,price){
	swal({
		title : "",
		text : "确定要删除？",
		type : "error",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$(obj).parent().parent().remove();
		 var materialprice=parseFloat($('#materialprice').text())-parseFloat(price);
	     $('#materialprice').text(materialprice);
	     $('input[name=materialprice]').val(materialprice);
	     
	     var maternum=$('#materialul tr').length-2;
	     $('#maternum').text(maternum);
	     $('input[name=maternum]').val(maternum);
	});
	
}
function jisuanprice(){
	var price=$('#addul input[name=price]').val();
	var reg=/^\d+(\.\d{1,2})?$/;
	var ex = /^\d+$/;

	if(price != "" && !reg.test(price)){
		swal({
			title : "",
			text : "价格请输入数字",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$('#addul input[name=price]').val(0);
		});
		return;
	}
}

function submitdata(){
	var alldata={"detaillist":[]};  
	$('#materialul tr').each(function(index){
		if($(this).attr("class") != "gray_bg" && $(this).attr("class") != "none_border"){
			var detaillist={};
			detaillist['type']=$(this).find('em[name=type]').text();
			detaillist['detail']=$(this).find('em[name=detail]').text();
			detaillist['price']=$(this).find('em[name=price]').text();
			alldata.detaillist.push(detaillist); 
		}
	})
	if(alldata.detaillist.length==0){
		swal({
			title : "",
			text : "请添加物料明细",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$('#initmaterial').click();
		});
		return;
	}
	if($('#examineuserid').val()==""){
		swal({
			title : "",
			text : "请选择审批人",
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
	var param = new Object();
	param.companyid ='${userInfo.companyid}' ;
	param.expenseno = $('#orderno').text();
	param.examineuserid = $('#examineuserid').val();
	param.createid = '${userInfo.userid}';
	param.detailprice = $('#materialprice').text();
	param.detailnum = $('#maternum').text();
	param.detaillist = JSON.stringify(alldata);
	
	param.userlist = $("#CCuseridlist").val();
	param.CCusernames = $('#CCusernamelist').val();
	
	var url='<%=request.getContextPath()%>/pc/insertExpense';
	$.ajax({
		type:'post',
		dataType:'json',
		data:param,
		url:url,
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
					
				});
			}else{
				swal({
					title : "",
					text : "添加成功",
					type : "success",
					showCancelButton : true,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href="<%=request.getContextPath() %>/pc/getPcOaExpenseList";
				});
			}
		},error:function(text){
			alert("添加失败");
		}
	})
}
</script>
</body>
</html>