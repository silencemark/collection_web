<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报损单新建</title>
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
	$('.stock_li').find("li").attr('class','');
	$('#break').attr('class','active');
	$('#homepage').parent().find("li").attr('class','');
	$('#homepage').attr('class','active');
})
function onloaddata(){
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/app/getShopListByUser?userid=${userInfo.userid}&companyid=${userInfo.companyid}',
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
				if(data.shoplist.length > 0 && data.shoplist != ''){
					var temp="<option value=''>请选择店面</option>";
					for(var i=0;i<data.shoplist.length;i++){
						temp+="<option value=\""+data.shoplist[i].organizeid+"\">"+data.shoplist[i].organizename+"</option>";
					}
					$('select[name=organizeid]').html(temp);
				}
			}
		}
	})
	initmaterial();
}

//清空当前对象后边一个以后所有的值
function removevalue(obj){
	$('#addul select[name='+obj+']').parent().parent().nextAll("tr").find("input").val("");
	$('#addul select[name='+obj+']').parent().parent().nextAll("tr").find("select").val("");
}
function choosetype(){
	var type =$('#addul select[name=type]').val();
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/app/getPurchaseMaterial?type='+type+'&companyid=${userInfo.companyid}&organizeid='+$('#organizeid').val()+'&groupname=1',
		success:function(data){
			if(data.status==0){
				var temp1="<option value=\"\">请选择名称</option>";
				if(data.materialList.length>0){
					for(var j=0;j<data.materialList.length;j++){
						temp1+="<option value=\""+data.materialList[j].name+"\">"+data.materialList[j].name+"</option>";
					}
					$('#addul select[name=name]').html(temp1);
				}else{
					$('#addul select[name=name]').html("");
				}
			}
			removevalue('type');
		}
	})
}
function choosename(){
	var type =$('#addul select[name=type]').val();
	var name =$('#addul select[name=name]').val();
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/app/getPurchaseMaterial?type='+type+'&companyid=${userInfo.companyid}&organizeid='+$('#organizeid').val()+'&name='+name+'&groupspecifications=1',
		success:function(data){
			if(data.status==0){
				var temp="<option value=\"\" onclick=\"optionguige(this)\">请选择规格</option>";
				if(data.materialList.length>0){
					for(var j=0;j<data.materialList.length;j++){
						temp+="<option value=\""+data.materialList[j].specificationsall+"\" unit=\""+data.materialList[j].unit+"\" onclick=\"optionguige(this)\">"+data.materialList[j].specificationsall+"</option>";
						if(j==0){
							//默认给后面的值
							$('#addul select[name=specifications]').html("<option value=\""+data.materialList[j].specificationsall+"\" unit=\""+data.materialList[j].unit+"\" onclick=\"optionguige(this)\">"+data.materialList[j].specificationsall+"</option>");
							/* $('#addul select[name=price]').html("<option value=\""+data.materialList[j].price+"\" materielid="+data.materialList[j].materielid+" stock="+data.materialList[j].stock+">"+data.materialList[j].price+"</option>"); */
							$("#unit").val(data.materialList[j].unit);
							$('#addul select[name=specifications]').val(data.materialList[j].specificationsall);
							/* $('#addul select[name=price]').val(data.materialList[j].price);
							
							$('#stock').val(data.materialList[j].stock);
							$('#materielid').val(data.materialList[j].materielid); */
						}
					}
					$('#addul select[name=specifications]').html(temp);
					$('#addul select[name=specifications]').val(data.materialList[0].specificationsall);
					choosespecifications();
				}else{
					$('#addul select[name=specifications]').html("");
				}
			}
		}
	})
}

function optionguige(obj){
	var unit=$(obj).attr("unit");
	if(unit==null || unit==undefined){
		unit="";
	}
	$("#unit").val(unit);
}

function choosespecifications(){
	var type =$('#addul select[name=type]').val();
	var name =$('#addul select[name=name]').val();
	var specifications =$('#addul select[name=specifications]').val();
	if(specifications !=""){
		$.ajax({
			type:'post',
			dataType:'json',
			url:'<%=request.getContextPath()%>/app/getPurchaseMaterial?type='+type+'&companyid=${userInfo.companyid}&organizeid='+$('#organizeid').val()+'&name='+name+'&specificationsall='+specifications,
			success:function(data){
				if(data.status==0){
					var temp="";
					if(data.materialList.length>0){
						for(var j=0;j<data.materialList.length;j++){
							temp+="<option value=\""+data.materialList[j].price+"\" materielid="+data.materialList[j].materielid+" stock="+data.materialList[j].stock+">"+data.materialList[j].price+"</option>";
							if(j==0){
								//默认给后面的值
								/* $('#addul select[name=price]').val(data.materialList[j].price);
								
								$('#stock').val(data.materialList[j].stock);
								$('#materielid').val(data.materialList[j].materielid); */
							}
						}
						$('#addul select[name=price]').html(temp);
					}else{
						$('#addul select[name=price]').html("");
					}
				}
			}
		})
	}
}

function changenum(obj){
	
	var materielid="";
	var stocknum=0;
	var stock1=0;
	
	var nonecount=$('#addul input[name=num]').val();
	//var reg=/^\d+(\.\d{0,2}?)?$/;
	//var ex = /^\d+$/;
	var ex = /^\+?(\d*\.{0,1}\d{0,2})$/;
	if(nonecount!="" && (!ex.test(nonecount) || parseFloat(nonecount) < 0)){
		swal({
			title : "",
			text : "数量输入有误",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$('#addul input[name=num]').val("");
		});
		return;
	}
	
	var num1=parseFloat($('#addul input[name=num]').val());
	$('#addul select[name=price] option').each(function(index){
		stocknum+=parseFloat($(this).attr("stock"));
		
		stock1=parseFloat($(this).attr("stock"));
		if(index ==0){
			materielid+=$(this).attr("materielid");
		}else{
			if(num1 > stock1 ){
				materielid+="_"+$(this).attr("materielid");
				num1=num1-stock1;
			}else if(num1 > 0){
				materielid+="_"+$(this).attr("materielid");
				num1=0;
			}
		}
	})
	$('#materielid').val(materielid);
	$('#stock').val(stocknum);
	
	var num=0;
	if($('#materialul input[value='+materielid+']').parent().parent().find('em[name=num]').length>0){
		$('#materialul input[value='+materielid+']').parent().parent().find('em[name=num]').each(function(index){
			num+=parseFloat($(this).text());
		})
	}
	
	if(parseFloat($(obj).val()) > (stocknum-num)){
		if(stocknum-num > 0){
			$(obj).val(stocknum-num);
		}else{
			$(obj).val(0);
		}
	}
	
	var sumprice=0;
	var stock=0;
	var nownum=parseFloat($('#addul input[name=num]').val());
	
	
	
	$('#addul select[name=price] option').each(function(index){
		stock=parseFloat($(this).attr("stock"));
		if(num > stock){
			num=num-stock;
		}else{
			if(num > 0 && nownum > (stock-num)){
				var price=parseFloat($(this).text());
				sumprice+=((stock-num)*100*price*100)/10000;
				nownum=nownum-(stock-num);
				num=0;
			}else{
				if(nownum > stock ){
					var price=parseFloat($(this).text());
					sumprice+=(stock*100*price*100)/10000;
					nownum=nownum-stock;
				}else if(nownum > 0){
					var price=parseFloat($(this).text());
					sumprice+=(nownum*100*price*100)/10000;
					nownum=0;
				}
			}
		}
	})
	
	$('#addul input[name=sumprice]').val(sumprice);
	
}

function changeorganize(obj){
	var organizeid=$(obj).val();
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/pc/getMaterialTypeNameList',
		data:'companyid=${userInfo.companyid}&organizeid='+organizeid,
		success:function(data){
				var temp="<option value=\"\">请选择类型</option>";
				for(var i=0;i<data.materialTypeList.length;i++){
					temp+="<option value=\""+data.materialTypeList[i].type+"\">"+data.materialTypeList[i].type+"</option>";
				}
				$('select[name=type]').html(temp);
				var temp1="<option value=\"\">请选择名称</option>";
				for(var j=0;j<data.materialNameList.length;j++){
					temp1+="<option value=\""+data.materialNameList[j].name+"\">"+data.materialNameList[j].name+"</option>";
				}
				$('select[name=name]').html(temp1);
		}
	})
}
</script>
<body onload="onloaddata()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>新建报损单</span><a href="#" class="back">返回</a></div>
    	<input type="hidden" id="stock">
		<input type="hidden" id="materielid">
        <div class="page_tab2">            
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="none_border">
                    <tr>
                    	<td class="l_name">报损单编号</td>
                    	<input type="hidden" name="orderno" value="${orderno}"/>
                        <td class="num_xx">${orderno}</td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name">店面</td>
                        <td><select class="text" name="organizeid" id="organizeid" onchange="changeorganize(this)"></select></td>
                    </tr>
                    <tr class="wlmx_detail">
                    	<td class="l_name">物料明细</td>
                        <td class="mx_tab">
                        	<a href="javascript:void(0)" class="a_btn bg_gay1 m_bt10" onclick="initmaterial()" id="initmaterial">添加</a>
                            <div class="clear"></div>
                        	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="mx_td" id="materialul">
                            	<tr class="gray_bg">
                                	<td width="10%">类别</td>
                                    <td width="10%">名称</td>
                                    <td width="10%">数量</td>
                                    <td width="10%">总价</td>  
                                    <td width="10%">单位</td>     
                                    <td width="15%">规格</td>                               
                                    <td>操作</td>
                                </tr>
                                                           
                                <tr class="none_border" id="materall">
                                	<input type="hidden" name="maternum" value="0"/>
                                	<td colspan="3">共 <i class="yellow" id="maternum">0</i> 项</td>
                                	<input type="hidden" name="materialprice" value="0"/>
                                    <td colspan="3" class="t_r">合计￥<i class="yellow" id="materialprice">0</i>元</td>
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
                    	<td class="l_name">报损时间</td>
                        <td><input type="text" class="text" id="reportlosstime"  readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/></td>
                    </tr>
                    <tr>
                    	<td class="l_name">申请人</td>
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
                <td><select class="text" name="type" onchange="choosetype()">
                	<option value="">请选择类型</option>
                	<c:forEach items="${materialTypeList}" var="item">
                		<option value="${item.type}">${item.type}</option>
                	</c:forEach>
                </select></td>
            </tr>
            <tr>
            	<td class="l_name" width="40">名称</td>
                <td><select class="text" name="name" onchange="choosename()">
                	<option value="">请选择名称</option>
                	<c:forEach items="${materialNameList}" var="item">
                		<option value="${item.name}">${item.name}</option>
                	</c:forEach>
                </select></td>
            </tr>
            <tr>
            	<td class="l_name" width="40">规格</td>
                <td><select class="text" name="specifications" onchange="choosespecifications()">
                <option value="">请选择规格</option>
                </select>
                </td>
            </tr>
            <tr style="display: none;">
            	<td class="l_name" width="40">单价</td>
                <td><select class="text" name="price" >
                </select></td>
            </tr>
            <tr>
            	<td class="l_name" width="40">数量</td>
                <td><input type="text" class="text" maxlength="11" name="num" oninput="changenum(this);" /></td>
            </tr>
            <tr>
            	<td class="l_name" width="40">总价</td>
                <td><input type="text" class="text" name="sumprice" disabled="disabled" style="background-color: white;"/></td>
            </tr>
        </table>
    </div>
    <div class="tc_btnbox"><a href="javascript:void(0)" onclick="$('.div_mask').hide();$('.tc_wlmxbox').hide();" class="bg_gay2">取消</a><a href="javascript:void(0)" onclick="addmaterial()" class="bg_yellow">确定</a></div>
</div>
<input type="hidden" id="unit" value=""/>

<script type="text/javascript">
$(function(){
	$("#reportlosstime").val(GetDateStr(0));
});
function GetDateStr(AddDayCount) { 
	var dd = new Date(); 
	dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期 
	var y = dd.getFullYear(); 
	var m = dd.getMonth()+1;//获取当前月份的日期 
	if(parseInt(m)<10){
		m="0"+m;
	}
	var d = dd.getDate();
	if(parseInt(d)<10){
		d="0"+d;
	}
	return y+"-"+m+"-"+d; 
}
function initmaterial(){
	$(".div_mask").show();
	$(".tc_wlmxbox").show();
	$('#addul select[name=type]').val("");
	$('#addul select[name=name]').val("");
	$('#addul input[name=sumprice]').val("");
	$('#addul input[name=num]').val("");
/* 	$('#addul select[name=price]').val("");
	$('#addul select[name=unit]').val(""); */
	$('#addul select[name=specifications]').val("");
}
function addmaterial(){
	if($('#addul select[name=type]').val()=="" ||
		$('#addul select[name=name]').val()=="" ||
		$('#addul input[name=sumprice]').val()=="" ||
		$('#addul input[name=num]').val()=="" || 
		$('#addul select[name=specifications]').val()==""){
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
	if(parseInt($('#addul input[name=num]').val())==0){
			swal({
				title : "",
				text : "数量为0不能添加",
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
		"<td><em name='type'>"+$('#addul select[name=type]').val()+"</em></td>"+
	    "<td><em name='name'>"+$('#addul select[name=name]').val()+"</em></td>"+
/* 	    "<td><em name='unit'>"+$('#addul select[name=unit]').val()+"</em></td>"+ */
	    "<td><em name='num'>"+$('#addul input[name=num]').val()+"</em></td>"+
/* 	    "<td><em name='price'>"+$('#addul select[name=price]').val()+"</em></td>"+ */
	    "<td><em name='sumprice'>"+$('#addul input[name=sumprice]').val()+"</em></td>"+
	    "<td><em name='unit'>"+$('#unit').val()+"</em></td>"+
	    "<td><em name='specifications'>"+$('#addul select[name=specifications]').val()+"</em></td>"+
	    "<td><a href=\"javascript:void(0)\" class=\"red\" onclick=\"deleteorder(this,'"+$('#addul input[name=sumprice]').val()+"')\">删除</a>"+
	    "<input type=\"hidden\" name=\"stock\" value="+$('#stock').val()+" /><input type=\"hidden\" name=\"materielid\" value="+$('#materielid').val()+" /></td>"+
	"</tr>";
	
	var materialprice=parseFloat($('#materialprice').text())+parseFloat($('#addul input[name=sumprice]').val());
	$('#materialprice').text(materialprice);
	$('input[name=materialprice]').val(materialprice);
	$('#materall').before(temp);
	
	var maternum=$('#materialul tr').length-2;
	$('#maternum').text(maternum);
	$('input[name=maternum]').val(maternum);
	
	$(".div_mask").hide();
	$('.tc_wlmxbox').hide();
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
		$(obj).parent().remove();
		 var materialprice=parseFloat($('#materialprice').text())-parseFloat(price);
	     $('#materialprice').text(materialprice);
	     var maternum=$('#materialul li').length;
	     $('#maternum').text(maternum);
	});
	
}
function submitdata(){
	var alldata={"materiallist":[]};  
	$('#materialul tr').each(function(index){
		if($(this).attr("class") != "gray_bg" && $(this).attr("class") != "none_border"){
			var materiallist={};
			materiallist['type']=$(this).find('em[name=type]').text();
			materiallist['name']=$(this).find('em[name=name]').text();
 			materiallist['unit']=$(this).find('em[name=unit]').text(); 
			materiallist['num']=$(this).find('em[name=num]').text();
/* 			materiallist['price']=$(this).find('em[name=price]').text(); */
			materiallist['sumprice']=$(this).find('em[name=sumprice]').text();
			materiallist['specifications']=$(this).find('em[name=specifications]').text();
			materiallist['purchasematerielid']=$(this).find('input[name=materielid]').val();
			alldata.materiallist.push(materiallist); 
		}
	})
	var organizeid=$('select[name=organizeid]').val();
	if(organizeid==""){
		swal({
			title : "",
			text : "请选择店面",
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
	if(alldata.materiallist.length==0){
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
	var orderno='${orderno}';
	var organizeid=$('select[name=organizeid]').val();
	var url='<%=request.getContextPath()%>/app/insertReportloss';
	
	var param = new Object();
	param.materiallist = JSON.stringify(alldata);
	param.orderno = orderno;
	param.organizeid = organizeid;
	param.companyid = "${userInfo.companyid}";
	param.createid = "${userInfo.userid}";
	param.datacode = "${serInfo.datacode}";
	param.examineuserid = $('#examineuserid').val();
	param.materialprice = $('#materialprice').text();
	param.maternum = $('#maternum').text();
	
	param.userlist = $("#CCuseridlist").val();
	param.CCusernames = $('#CCusernamelist').val();
	param.reportlosstime = $('#reportlosstime').val();
	
	$.ajax({
		type:'post',
		dataType:'json',
		data: param,
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
					location.href="<%=request.getContextPath() %>/pc/getReportlossOrder";
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