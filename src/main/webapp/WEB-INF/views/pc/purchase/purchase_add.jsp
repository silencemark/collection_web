<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>采购(入库)单新建</title>
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
	$('.sup_li').find("li").attr('class','');
	$('#purchase').attr('class','active');
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
</script>
<body onload="onloaddata()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>新建采购(入库)单</span><a href="<%=request.getContextPath() %>/pc/getPurchaseOrder" class="back">返回</a></div>
        <div class="page_tab2">            
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="none_border">
                    <tr>
                    	<td class="l_name">采购(入库)单编号</td>
                    	<input type="hidden" name="orderno" value="${orderno}"/>
                        <td class="num_xx">${orderno}</td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name">店面</td>
                        <td><select class="text" name="organizeid"></select></td>
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
                                    <td width="10%">供应商</td>
                                    <td width="10%">数量</td>
                                    <td width="15%">规格</td>   
                                    <td width="10%">单价</td>
                                    <td width="10%">总价</td>     
                                                                
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
                    	<td class="l_name">实付金额</td>
                        <td>￥<input type="text" id="payAmount" class="text" value="0"/>元</td>
                    </tr>
                    <tr>
                    	<td class="l_name">审核人</td>
                    	<input type="hidden" id="examineuserid" >
                        <td class="img_td"><div class="img f"><img src="../userbackstage/images/pc_page/user_img2.png" id="examineheadimage" width="30" height="30" /></div><i class="i_name" id="examinename"></i><a href="javascript:void(0)" onclick="openOrCloseExamineDiv()" class="add_user">添加</a></td>
                    </tr>
                    <tr>
                    	<td class="l_name">抄送人</td>
                    	<input type="hidden" id="releaserange"/>
                        <td class="img_td" id="CCusernames"><a href="javascript:void(0)" onclick="showCCuserOrganize()" class="add_user">添加</a></td>
                    </tr>
                    <tr>
                    	<td class="l_name">入库时间</td>
                        <td><input type="text" id="purchasetime" class="text" readonly="readonly" placeholder="请选择采购入库时间"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/></td>
                    </tr>
                    <tr>
                    	<td class="l_name">采购人</td>
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
                <td>
                	<div class="have_position">
	                	<input type="text" class="text" name="type"/>
	                	<div class="sel_ul" id="purchasetype" style="display: none;">
	                    	<ul id="purchasetypul">
<!-- 	                        	<li class="active"><a>类型一</a></li> -->
<!-- 	                            <li><a>类型二</a></li> -->
	                        </ul>
	                    </div>
	                </div>
                </td>
            </tr>
            <tr>
            	<td class="l_name" width="40">名称</td>
                <td>
                	<div class="have_position">
	                	<input type="text" class="text"  name="name"/>
	                	<div class="sel_ul" id="purchasename" style="display: none;">
	                    	<ul id="purchasenameul">
<!-- 	                        	<li class="active"><a>类型一</a></li> -->
<!-- 	                            <li><a>类型二</a></li> -->
	                        </ul>
	                    </div>
	                </div>
                </td>
            </tr>
            <tr>
            	<td class="l_name" width="40">数量</td>
                <td><input type="text" class="text" name="num" oninput="jisuanprice()"/></td>
            </tr>
<!--             <tr>
            	<td class="l_name" width="40">单位</td>
                <td><input type="text" class="text"  name="unit"/></td>
            </tr> -->
            <tr>
            	<td class="l_name" width="40">供应商</td>
                <td>
                	<select class="text" name="suppliername">
                		<option value="">请选择</option>
                		<c:forEach items="${supplierlist}" var="item">
                			<option value="${item.supplierid}">${item.suppliername}</option>
                		</c:forEach>
                	</select>
                </td>
            </tr>
            <tr>
            	<td class="l_name" width="40">规格</td>
                <td><input type="text" class="text" name="specifications" style="width:72px" placeholder="规格数量"/>
                <select class="text" name="specificationsone" style="width:80px">
                	<c:forEach items="${dictlistone}" var="item">
                		<option value="${item.dataid}">${item.cname}</option>
                	</c:forEach>
                </select>
                <select class="text" name="specificationstwo" style="width:80px">
                	<option value="">请选择</option>
                	<c:forEach items="${dictlisttwo}" var="item">
                		<option value="${item.dataid}">${item.cname}</option>
                	</c:forEach>
                </select>
                </td>
            </tr>
            <tr>
            	<td class="l_name" width="40">单价</td>
                <td><input type="text" class="text" name="price" oninput="jisuanprice()"/></td>
            </tr>
            <tr>
            	<td class="l_name" width="40">总价</td>
                <td><input type="text" class="text" name="sumprice" disabled="disabled" style="background-color: white;"/></td>
            </tr>
        </table>
    </div>
    <div class="tc_btnbox"><a href="javascript:void(0)" onclick="$('.div_mask').hide();$('.tc_wlmxbox').hide();" class="bg_gay2">取消</a><a href="javascript:void(0)" onclick="addmaterial()" class="bg_yellow">确定</a></div>
</div>

<script type="text/javascript">
$(function(){
	$('#addul input[name="type"]').click(function(){
		event.stopPropagation();
		$("#purchasetype").show();
		queryPurchaseTypeInfo();
	});
	
	$("#addul input[name='name']").click(function(){
		event.stopPropagation();
		$('#purchasename').show();
		open_purchasename_div();
	});
	
	$("#addul").parent().click(function(){
		event.stopPropagation();
		$("#purchasetype").hide();
		$('#purchasename').hide();
	});
	
	
	$("#purchasetime").val(GetDateStr(0));
});
function queryPurchaseTypeInfo(){
	var param = new Object();	
	param.companyid="${userInfo.companyid}";
	$.ajax({		
		data:param,		
		type:"post",
		url:projectpath+'/app/getPurchaseTypeList',
		success:function(resultMap){
			if(resultMap != undefined && resultMap != null){				
				var purchasetypelist = resultMap.data;
				showPurchaseTyepeInfo(purchasetypelist);				
			}
		},error:function(e){
			
		}
	});
}
function showPurchaseTyepeInfo(lists){
	if(lists.length > 0 && lists != null){
		var html = "";
		$.each(lists,function(i,list){			
			html+='<li onclick="choosePurchaseType(this)"><a>'+list.type+'</a></li>';						
		});				
		$('#purchasetypul').html(html);
	}else{
		
	}
}
function choosePurchaseType(e){
	var text=$(e).find("a").text();
	$('#addul input[name="type"]').val(text);
	$("#purchasetype").hide();
}


//打开采购单名称列表
function open_purchasename_div(){
	//取类别
	var typename=$("#addul input[name='type']").val();
	//查询数据
	$.ajax({
		type:'post',
		async:false,
		dataType:'json',
		url:projectpath+'/app/getPurchaseMaterial?type='+typename+'&companyid=${userInfo.companyid}&organizeid='+$('select[name="organizeid"]').val()+"&groupname=1",
		success:function(data){
			if(data.status==0){
				var temp1="";
				if(data.materialList.length>0){
					for(var j=0;j<data.materialList.length;j++){
						temp1+="<li onclick=\"optionpurchasename(this)\"><a>"+data.materialList[j].name+"</a></li>";
					}
					$('#purchasenameul').html(temp1);
				}
			}
		}
	});
}
//点击选项
function optionpurchasename(obj){
	$(obj).attr("class","active");
	$("#addul input[name='name']").val($(obj).find("a").text());
	$('#purchasename').hide();
}

function initmaterial(){
	$(".div_mask").show();
	$(".tc_wlmxbox").show();
	$('#addul input[name=type]').val("");
	$('#addul input[name=name]').val("");
	$('#addul input[name=sumprice]').val("");
	$('#addul input[name=num]').val("");
	$('#addul input[name=price]').val("");
/* 	$('#addul input[name=unit]').val(""); */
	$('#addul input[name=specifications]').val("");
}
function addmaterial(){
	if($('#addul input[name=type]').val()=="" ||
			$('#addul input[name=name]').val()=="" ||
			$('#addul input[name=sumprice]').val()=="" ||
			$('#addul input[name=num]').val()=="" ||
			$('#addul input[name=price]').val()=="" || 
			$('#addul select[name=specificationsone]').val()=="" ){
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
	if($('#addul input[name=specifications]').val()=="" && $('#addul select[name=specificationstwo]').val() !="" ){
		swal({
			title : "",
			text : "请填写规格数量",
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
	if($('#addul input[name=specifications]').val() !="" && $('#addul select[name=specificationstwo]').val() =="" ){
		swal({
			title : "",
			text : "请选择规格",
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
	var noneprice=$('#addul input[name=price]').val();
	if(noneprice.indexOf(".")==noneprice.length-1){
		noneprice=noneprice.substring(0,noneprice.length-1);
	}
		var temp="<tr>"+
				"<td><em name='type'>"+$('#addul input[name=type]').val()+"</em></td>"+
			    "<td><em name='name'>"+$('#addul input[name=name]').val()+"</em></td>"+
			    "<td><em>"+($('#addul select[name=suppliername] option:selected').text()=="请选择" || $('#addul select[name=suppliername] option:selected').text()==""?"":$('#addul select[name=suppliername] option:selected').text())+"</em><input type=\"hidden\" name=\"suppliername\" value=\""+$('#addul select[name=suppliername]').val()+"\"></td>"+
/* 			    "<td><em name='unit'>"+$('#addul input[name=unit]').val()+"</em></td>"+ */
			    "<td><em name='num'>"+$('#addul input[name=num]').val()+"</em></td>"+
			    "<td><em name='specifications'>"+$('#addul input[name=specifications]').val()+"</em>&nbsp;"+
			    "<em>"+$('#addul select[name=specificationsone] option:selected').text()+"</em><input type=\"hidden\" name=\"specificationsone\" value=\""+$('#addul select[name=specificationsone]').val()+"\">"+
		        "<em>"+($('#addul select[name=specificationstwo] option:selected').text()=="请选择" || $('#addul select[name=specificationstwo] option:selected').text()==""?"":"/"+$('#addul select[name=specificationstwo] option:selected').text())+"</em>&nbsp;<input type=\"hidden\" name=\"specificationstwo\" value=\""+$('#addul select[name=specificationstwo]').val()+"\"></td>"+
			    "<td><em name='price'>"+noneprice+"</em></td>"+
			    "<td><em name='sumprice'>"+$('#addul input[name=sumprice]').val()+"</em></td>"+
			    
			    "<td><a href=\"javascript:void(0)\" class=\"red\" onclick=\"deleteorder(this,'"+$('#addul input[name=sumprice]').val()+"')\">删除</a></td>"+
			"</tr>";
		
	     var materialprice=parseFloat($('#materialprice').text())+parseFloat($('#addul input[name=sumprice]').val());
	     $('#materialprice').text(materialprice);
	     
	     var payAmount = $('#payAmount').val();
	     if(payAmount == ""){
	    	 payAmount = 0;
	     }
	     payAmount = parseFloat(payAmount)+parseFloat($('#addul input[name=sumprice]').val());
	     $('#payAmount').val(payAmount);
	     
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
	     
	     var payAmount = parseFloat($('#payAmount').val())-parseFloat(price);
	     $('#payAmount').val(payAmount);
	     
	     $('input[name=materialprice]').val(materialprice);
	     
	     var maternum=$('#materialul tr').length-2;
	     $('#maternum').text(maternum);
	     $('input[name=maternum]').val(maternum);
	});
	
}
function jisuanprice(){
	var price=$('#addul input[name=price]').val();
	var num=$('#addul input[name=num]').val();
	var reg=/^(0|[1-9][0-9]{0,9})(\.[0-9]{0,2})?$/;
		///^\d+(\.\d{1,2})?$/;
	//var ex = /^\d+$/;
	var ex = /^\+?(\d*\.{0,1}\d{0,2})$/;

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
			$('#addul input[name=price]').val("");
		});
		return;
	}
	if(num!="" && (!ex.test(num) || parseFloat(num) < 0)){
		swal({
			title : "",
			text : "数量精确到小数点后两位",
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
	if(price==""){
		$('#addul input[name=sumprice]').val(0);
		return;
	}
	if(num==""){
		$('#addul input[name=sumprice]').val(0);
		return;
	}
	$('#addul input[name=sumprice]').val(parseFloat(price)*parseFloat(num));
}
function accMul(arg1,arg2){  
	var m=0,s1=arg1.toString(),
	s2=arg2.toString();  
	try{
	m+=s1.split(".")[1].length}catch(e){}  
	try{
	m+=s2.split(".")[1].length}catch(e){}  
	return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
}
function submitdata(){
	var alldata={"materiallist":[]};  
	$('#materialul tr').each(function(index){
		if($(this).attr("class") != "gray_bg" && $(this).attr("class") != "none_border"){
			var specifications=$(this).find('em[name=specifications]').text();
			var price=$(this).find('em[name=price]').text();
			var materiallist={};
			materiallist['type']=$(this).find('em[name=type]').text();
			materiallist['name']=$(this).find('em[name=name]').text();
			/* materiallist['unit']=$(this).find('em[name=unit]').text(); */
			materiallist['realprice']=$(this).find('em[name=price]').text();
			
			if(specifications!=""){
				materiallist['price']=price/specifications;
				materiallist['specifications']=$(this).find('em[name=specifications]').text();
				
				materiallist['num']=accMul($(this).find('em[name=num]').text(),$(this).find('em[name=specifications]').text());
				materiallist['realnum']=$(this).find('em[name=num]').text();
			}else{
				materiallist['price']=price;
				
				materiallist['num']=$(this).find('em[name=num]').text();
				materiallist['realnum']=$(this).find('em[name=num]').text();
			}
			
			materiallist['sumprice']=$(this).find('em[name=sumprice]').text();
			materiallist['specificationsone']=$(this).find('input[name=specificationstwo]').val();
			materiallist['unit']=$(this).find('input[name=specificationsone]').prev().text();
			materiallist['specificationstwo']=$(this).find('input[name=specificationsone]').val();
			materiallist['supplierid']=$(this).find('input[name=suppliername]').val();
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
	var url='<%=request.getContextPath()%>/app/insertPurchase';
	var param = new Object();
	param.materiallist = JSON.stringify(alldata);
	param.orderno = orderno;
	param.organizeid = organizeid;
	param.companyid = "${userInfo.companyid}";
	param.createid = "${userInfo.userid}";
	param.datacode = "${userInfo.datacode}";
	param.examineuserid = $('#examineuserid').val();
	param.materialprice = $('#materialprice').text();
	param.maternum = $('#maternum').text();
	param.userlist = $("#CCuseridlist").val();
	param.CCusernames = $('#CCusernamelist').val();
	param.purchasetime = $("#purchasetime").val();
	param.payamount = $('#payAmount').val();
	
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
					location.href="<%=request.getContextPath() %>/pc/getPurchaseOrder";
				});
			}
		},error:function(text){
			alert("添加失败");
		}
	})
}

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
</script>
</body>
</html>