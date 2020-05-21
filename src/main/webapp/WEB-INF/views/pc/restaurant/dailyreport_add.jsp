<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>每日报表</title>
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
	$('.restaurant_li').find("li").attr('class','');
	$('#dailyreport').attr('class','active');
	$('#homepage').parent().find("li").attr('class','');
	$('#homepage').attr('class','active');
	
	$('#statisticaltime').val(GetDateStr(0));
})
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

var organizeid="";
var createtime="";
var keqing=null;
var goutong=null;
function onloadData(){
	keqing=document.getElementById("todaycustomerdivid").innerHTML;
	goutong=document.getElementById("communicationdivid").innerHTML;
	document.getElementById("todaycustomerdivid").innerHTML="";
	document.getElementById("communicationdivid").innerHTML="";
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/app/getOrganizeListByDailyReport?userid=${userInfo.userid}&companyid=${userInfo.companyid}',
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
}
function chooseorganize(){
	var name =$('#organizeid option:selected').text();
	var id =$('#organizeid').val();
	organizeid=id;
	//查询模版
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/app/getEverydayReportModule?userid=${userInfo.userid}&organizeid='+organizeid+'&companyid=${userInfo.companyid}',
		success:function(data){
			if(data.modulelist.length > 0){
				var temp="";
				for(var i=0;i<data.modulelist.length;i++){
					temp+="<option value=\""+data.modulelist[i].templateid+"\">"+data.modulelist[i].templatename+"</option>";
				}
				$('#templateid').html(temp);
/* 				$('#module').show(); */
				//如果只有一个模版直接查询出内容
				choosetemplate();
			}
		}
	})
	$("input[name=content]").val("");
}
function choosetemplate(){
	var name =$('#templateid option:selected').text();
	var templateid =$('#templateid').val();
	//查询内容
	$.ajax({
		type:'post',
		dataType:'json',
		url:'<%=request.getContextPath()%>/app/getEverydayReportDictList?templateid='+templateid,
		success:function(data){
			if(data.datalist.length > 0){
				
				$('tr[class=wlmx_detail]').remove();
				//餐别
				var tempmeal="";
				for(var i=0;i<data.datalist.length;i++){
					if(data.datalist[i].typeid==1){
					tempmeal+="<tr class=\"wlmx_detail\" name=\"mealdaily\">"+
                		"<td class=\"l_name2\">"+data.datalist[i].dataname+"</td>"+
                    	"<td class=\"mx_tab\">"+
                    	"<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"mx_td\">"+
                        "<tr class=\"none_border\" name=\"mealdiv\"><input type=\"hidden\" name=\"mealdataid\" value=\""+data.datalist[i].dataid+"\" />"+
                        "</tr></table></td></tr>";
					}
				}
				$('#alldata').before(tempmeal);
				
				//今日统计
				var todaytemp="<td>今日统计<br/>";
				var todaynum=0;
				for(var k=0;k<data.datalist.length;k++){
					if(data.datalist[k].typeid==3){
						todaytemp+="<span name=\"datadiv\">"+data.datalist[k].dataname+"&nbsp;&nbsp;:&nbsp;&nbsp;";
						if(data.datalist[k].dataid==10 || data.datalist[k].dataid==11 || data.datalist[k].dataid==12){
							todaytemp+="<input type=\"text\" class=\"text m_top10\" placeholder=\"\" name=\"content\" oninput=\"calculate()\" />";
						}else{
							todaytemp+="<input type=\"text\" class=\"text m_top10\" placeholder=\"\" name=\"content\"/>";
						}
						todaytemp+="<input type=\"hidden\" name=\"dataid\" value=\""+data.datalist[k].dataid+"\"/><br /></span>";
						todaynum++;
					}
				}
				todaytemp+="</td>";
				if(todaynum > 0){
					$('tr[name=mealdiv]').append(todaytemp);
				}
				
				//明细
				var detailtemp="<td>收入详细<br/>";
				var detailnum=0;
				for(var j=0;j<data.datalist.length;j++){
					if(data.datalist[j].typeid==2){
						detailtemp+="<span name=\"datadiv\">"+data.datalist[j].dataname+"&nbsp;&nbsp;:&nbsp;&nbsp;<input type=\"text\" class=\"text m_top10\" placeholder=\"\" name=\"content\"/>"+
						"<input type=\"hidden\" name=\"dataid\" value=\""+data.datalist[j].dataid+"\"/><br /></span>";
						detailnum++;
					}
				}
				detailtemp+="</td>";
				if(detailnum > 0){
					$('tr[name=mealdiv]').append(detailtemp);
				}
				
				
				
			    
				//今日培训
				var todaytraintemp="";
				for(var i=0;i<data.datalist.length;i++){
					if(data.datalist[i].typeid==4){
						todaytraintemp+="<tr>"+
							            	"<td class=\"l_name\" width=\"50\">"+data.datalist[i].dataname+"</td>"+
							                "<td><input type=\"text\" placeholder=\"请输入"+data.datalist[i].dataname+"\" class=\"text\" name=\"content\" /><input type=\"hidden\" name=\"dataid\" value=\""+data.datalist[i].dataid+"\"/></td>"+
							            "</tr>";
					}
				}
				
				$('#todaytrainul').html(todaytraintemp);
				var todaytrain="<tr class=\"wlmx_detail\" name=\"todaytraindaily\">"+
	            	"<td class=\"l_name2\">今日培训情况</td>"+
	                "<td class=\"mx_tab\" id=\"todaytrain\">";
	                todaytrain+="<a href=\"javascript:void(0)\" name=\"addbtn\" class=\"a_btn bg_gay1 m_bt10\" onclick=\"$('#todaytraindiv').show();$('.div_mask').show();$('#todaytrainul').find('input[name=content]').val('');\">添加</a>";
                	//todaytrain+=$("#todaytraindiv").html();
                	todaytrain+="<div class=\"clear\"></div>"+
                    "</td>"+
                    "</tr>";
                    //$("#todaytraindiv").remove()
				$('#alldata').before(todaytrain);
				
				//今日出勤
				var attendancetemp="<div class=\"tab_list\"><table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" id=\"attendanceul\">";
		        
				for(var i=0;i<data.datalist.length;i++){
					if(data.datalist[i].typeid==5){
						attendancetemp+="<tr>"+
							            	"<td class=\"l_name\" width=\"50\">"+data.datalist[i].dataname+"</td>"+
							                "<td><input type=\"text\" placeholder=\"请输入"+data.datalist[i].dataname+"\" class=\"text\" name=\"content\" /><input type=\"hidden\" name=\"dataid\" value=\""+data.datalist[i].dataid+"\"/></td>"+
							            "</tr>";
					}
				}
				attendancetemp+="</table></div>";
				$('#attendancediv').html(attendancetemp);
				var attendance="<tr class=\"wlmx_detail\" name=\"attendancedaily\">"+
            	"<td class=\"l_name2\">今日出勤情况</td>"+
                "<td class=\"mx_tab\"  id=\"attendance\">";
            	//"<a href=\"javascript:void(0)\" name=\"addbtn\" class=\"a_btn bg_gay1 m_bt10\" onclick=\"$('#attendancediv').show();$('.div_mask').show();$('#attendanceul').find('input[name=content]').val('');\">添加</a>"+
            	attendance+=$('#attendancediv').html();
            	attendance+="<div class=\"clear\"></div>"+
                "</td>"+
                "</tr>";
                $('#attendancediv').html("")
				$('#alldata').before(attendance);
				
				
				
				//今日主要客情
				var todaycustomer="<tr class=\"wlmx_detail\" name=\"todaycustomerdaily\">"+
            	"<td class=\"l_name2\">今日主要客情</td>"+
                "<td class=\"mx_tab\"  id=\"todaycustomer\">";
					for(var i=0;i<data.datalist.length;i++){
						if(data.datalist[i].typeid==6){
							$('#todaycustomerdiv').find('input[name=dataid]').val(data.datalist[i].dataid);
							//todaycustomer+="<a href=\"javascript:void(0)\" name=\"addbtn\" class=\"a_btn bg_gay1 m_bt10\" onclick=\"$('#todaycustomerdiv').show();$('.div_mask').show();$('#todaycustomerdiv').find('textarea[name=content]').val('');\">添加</a>"+
							todaycustomer+=keqing;
			                "<div class=\"clear\"></div>";
			                
						}
					}
				todaycustomer+="</td>"+
                "</tr>";
				$('#alldata').before(todaycustomer);
				
				//需沟通事项
				var communication="<tr class=\"wlmx_detail\" name=\"communicationdaily\">"+
            	"<td class=\"l_name2\">需沟通事项</td>"+
                "<td class=\"mx_tab\"  id=\"communication\">";
					for(var i=0;i<data.datalist.length;i++){
						if(data.datalist[i].typeid==7){
							$('#communicationdiv').find('input[name=dataid]').val(data.datalist[i].dataid);
							//communication+="<a href=\"javascript:void(0)\" name=\"addbtn\" class=\"a_btn bg_gay1 m_bt10\" onclick=\"$('#communicationdiv').show();$('.div_mask').show();$('#communicationdiv').find('textarea[name=content]').val('')\">添加</a>"+
							communication+=goutong;
			                "<div class=\"clear\"></div>";
			                
						}
					}
					communication+="</td>"+
	                "</tr>";
				$('#alldata').before(communication);
				
			}
			
			//$("input[value=18]").prev().val(data.lastnum);
		}
	})
}
function calculate(){
	var yin=0;
	$("input[value=11]").prev().each(function(index){
		if($(this).val() != ""){
			yin+=parseFloat($(this).val());
		}
	})
	
	var flow=0;
	$("input[value=12]").prev().each(function(index){
		if($(this).val() != ""){
			flow+=parseInt($(this).val());
		}
	})
	var shi=0;
	$("input[value=10]").prev().each(function(index){
		if($(this).val() != ""){
			shi+=parseFloat($(this).val());
		}
	})
	/* var shi=0;
	var yin=0;
	var flow=0;
	if(keliu=="" || typeof(keliu)=="undefined"){
		flow=0;
	}else{
		flow=parseInt($("input[value=12]").prev().val());
	}
	if(shipin=="" || typeof(shipin)=="undefined"){
		shi=0;
	}else{
		shi=parseInt($("input[value=10]").prev().val());
	}
	if(yinpin=="" || typeof(yinpin)=="undefined"){
		yin=0;
	}else{
		yin=parseInt($("input[value=11]").prev().val());
	}
	 */
	$('#turnoveri').text((parseFloat(yin)+parseFloat(shi))+"元");
	$('#guestflowi').text(flow);
	if(flow==0){
		$('#consumptioni').text("0元");
		$('#consumption').val(0);
	}else{
		var price=((parseFloat(yin)+parseFloat(shi))*100/flow*100)/10000+"";
		if(price.length > price.indexOf(".")+2 ){
			price=price.substring(0,price.indexOf(".")+3);
		}
		$('#consumptioni').text(price+"元");
		$('#consumption').val(price);
	}
	
	$('#turnover').val(yin+shi);
	$('#guestflow').val(flow);
	
}
function submitdata(){
	
	var alldata={"extendlist":[]}; 
	//餐别
	var status=0;
	$('tr[name=mealdaily]').find("span[name=datadiv]").each(function(index){
		if($(this).find('input[name=dataid]').val()==10 && $(this).find('input[name=content]').val()==""){
			swal({
				title : "",
				text : "请填写食品金额后再提交",
				type : "error",
				showCancelButton : false,
				confirmButtonColor : "#ff7922",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			}, function(){
				$(this).find('input[name=content]').focus();
			});
			status=1;
			return false;
		}else if($(this).find('input[name=dataid]').val()==11 && $(this).find('input[name=content]').val()==""){
			swal({
				title : "",
				text : "请填写饮品金额后再提交",
				type : "error",
				showCancelButton : false,
				confirmButtonColor : "#ff7922",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			}, function(){
				$(this).find('input[name=content]').focus();
			});
			status=1;
			return false;
		}
		var extendlist={};
		extendlist['content']=$(this).find('input[name=content]').val();
		extendlist['dataid']=$(this).find('input[name=dataid]').val();
		extendlist['mealdataid']=$(this).parent().parent().find('input[name=mealdataid]').val();
		extendlist['type']=1;
		alldata.extendlist.push(extendlist); 
	})
	if(status==1){
		return;
	}
	if($('#organizeid').val()==""){
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
/* 	if($('tr[name=todaytraindaily] .div_pxqk').length==0){
		swal({
			title : "",
			text : "请添加今日培训情况",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		});
		return;
	} */
	//今日培训情况
	$('tr[name=todaytraindaily] .div_pxqk').find('i').each(function(index){
		var extendlist={};
		extendlist['content']=$(this).find('input[name=content]').val();
		extendlist['dataid']=$(this).find('input[name=dataid]').val();
		extendlist['remark']=$(this).find('input[name=remark]').val();
		extendlist['type']=2;
		alldata.extendlist.push(extendlist); 
	})
/* 	if($('tr[name=attendancedaily] .div_pxqk').length==0){
		swal({
			title : "",
			text : "请添加今日出勤情况",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		});
		return;
	} */
	//今日出勤情况
	$('#attendanceul tbody').find('tr').each(function(index){
		var extendlist={};
		extendlist['content']=$(this).find('input[name=content]').val();
		extendlist['dataid']=$(this).find('input[name=dataid]').val();
		extendlist['remark']=$(this).find('input[name=remark]').val();
		extendlist['type']=3;
		alldata.extendlist.push(extendlist); 
	})
/* 	if($('tr[name=todaycustomerdaily] .div_pxqk').length==0){
		swal({
			title : "",
			text : "请添加今日主要客情",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		});
		return;
	} */
	//今日主要客情
	$('#todaycustomerdiv').each(function(index){
		var extendlist={};
		extendlist['content']=$(this).find('textarea[name=content]').val();
		extendlist['dataid']=$(this).find('input[name=dataid]').val();
		extendlist['remark']=$(this).find('input[name=remark]').val();
		extendlist['type']=4;
		alldata.extendlist.push(extendlist); 
	})
/* 	if($('tr[name=communicationdaily] .div_pxqk').length==0){
		swal({
			title : "",
			text : "请添加需要沟通事项",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		});
		return;
	} */
	//需要沟通事项
	$('#communicationdiv').each(function(index){
		var extendlist={};
		extendlist['content']=$(this).find('textarea[name=content]').val();
		extendlist['dataid']=$(this).find('input[name=dataid]').val();
		extendlist['remark']=$(this).find('input[name=remark]').val();
		extendlist['type']=5;
		alldata.extendlist.push(extendlist); 
	})
	
	if($('#copyuserid').val()==""){
		swal({
			title : "",
			text : "请选择抄送人",
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
	if($('#statisticaltime').val()==""){
		swal({
			title : "",
			text : "请选择统计时间",
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
	
	var datetime=new Date($('#statisticaltime').val());
	if(datetime > new Date()){
		swal({
			title : "",
			text : "统计时间只能选择从今天以前",
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
	param.extendlist = JSON.stringify(alldata);
	param.organizeid = organizeid;
	param.companyid = "${userInfo.companyid}";
	param.createid = "${userInfo.userid}";
	param.examineuserid = $('#examineuserid').val();
	param.turnover = $('#turnover').val();
	param.guestflow = $('#guestflow').val();
	param.consumption = $('#consumption').val();
	param.statisticaltime = $('#statisticaltime').val();
	
	param.userlist = $("#CCuseridlist").val();
	param.CCusernames = $('#CCusernamelist').val();
	
	
	var url='<%=request.getContextPath()%>/app/insertEverydayReport';
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
					location.href="<%=request.getContextPath() %>/pc/getEverydayReportList";
				});
			}
		},error:function(text){
			alert("添加失败");
		}
	})
}
function addtodaytrain(){
	var addnum=$('#todaytrain').find("div[class=div_pxqk]").length;
	var status=0;
	var temp="<div class=\"div_pxqk\"><a href=\"javascript:void(0)\" class=\"w_del\" onclick=\"deletediv(this)\">删除</a>";
		var num=1;
		$('#todaytrainul tr').each(function(index){
			if($(this).find("input[name=content]").val()==""){
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
				status=1;
				return false;
			}
			temp+="<i><em>"+$(this).find("td[class=l_name]").text()+"：</em>"+$(this).find("input[name=content]").val()+
			"<input type=\"hidden\" name=\"content\" value=\""+$(this).find("input[name=content]").val()+"\" />"+
			"<input type=\"hidden\" name=\"dataid\" value=\""+$(this).find("input[name=dataid]").val()+"\" />"+
			"<input type=\"hidden\" name=\"remark\" value=\""+(addnum+1)+"\" /></i>";
		    if(num%2==0){
		    	temp+="<div class=\"clear\"></div>";
		    }
		    num++;
		})
		if(num%2==0){
			temp+="<div class=\"clear\"></div>";
		}
		if(status==1){
			return;
		}
		temp+="</div>";
		$("#todaytrain").append(temp);
		$('#todaytraindiv').hide();
		$('.div_mask').hide();
}
function addattendance(){
	var addnum=$('#attendance').find("div[class=div_pxqk]").length;
	var status=0;
	var temp="<div class=\"div_pxqk\"><a href=\"javascript:void(0)\" class=\"w_del\" onclick=\"deletediv(this)\">删除</a>";
		var num=1;
		$('#attendanceul tr').each(function(index){
			if($(this).find("input[name=content]").val()==""){
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
				status=1;
				return false;
			}
			temp+="<i><em>"+$(this).find("td[class=l_name]").text()+"：</em>"+$(this).find("input[name=content]").val()+
			"<input type=\"hidden\" name=\"content\" value=\""+$(this).find("input[name=content]").val()+"\" />"+
			"<input type=\"hidden\" name=\"dataid\" value=\""+$(this).find("input[name=dataid]").val()+"\" />"+
			"<input type=\"hidden\" name=\"remark\" value=\""+(addnum+1)+"\" /></i>";
		    if(num%2==0){
		    	temp+="<div class=\"clear\"></div>";
		    }
		    num++;
		})
		if(status==1){
			return;
		}
		if(num%2==0){
			temp+="<div class=\"clear\"></div>";
		}
		temp+="</div>";
		$("#attendance").append(temp);
		$('#attendancediv').hide();
		$('.div_mask').hide();
		
		$('tr[name=attendancedaily]').find('a[name=addbtn]').hide();
}
function addtodaycustomer(){
	if($('#todaycustomerdiv').find("textarea[name=content]").val()==""){
		swal({
			title : "",
			text : "请输入内容再提交",
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
	var addnum=$('#todaycustomer').find("div[class=div_pxqk]").length;
	var temp="<div class=\"div_pxqk\">"+
				"<a href=\"javascript:void(0)\"  onclick=\"deletediv(this)\" class=\"w_del\">删除</a>"+
			    "<span>"+$('#todaycustomerdiv').find("textarea[name=content]").val()+"</span>"+
			    "<input type=\"hidden\" name=\"content\" value=\""+$('#todaycustomerdiv').find("textarea[name=content]").val()+"\" />"+
				"<input type=\"hidden\" name=\"dataid\" value=\""+$('#todaycustomerdiv').find("input[name=dataid]").val()+"\" />"+
				"<input type=\"hidden\" name=\"remark\" value=\""+(addnum+1)+"\" />";
			    "<div class=\"clear\"></div>"+
			"</div>";
	$("#todaycustomer").append(temp);
	$('#todaycustomerdiv').hide();
	$('.div_mask').hide();
	
	$('tr[name=todaycustomerdaily]').find('a[name=addbtn]').hide();
}
function addcommunication(){
	if($('#communicationdiv').find("textarea[name=content]").val()==""){
		swal({
			title : "",
			text : "请输入内容再提交",
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
	var addnum=$('#communication').find("div[class=div_pxqk]").length;
	var temp="<div class=\"div_pxqk\">"+
		"<a href=\"javascript:void(0)\"  onclick=\"deletediv(this)\" class=\"w_del\">删除</a>"+
	    "<span>"+$('#communicationdiv').find("textarea[name=content]").val()+"</span>"+
	    "<input type=\"hidden\" name=\"content\" value=\""+$('#communicationdiv').find("textarea[name=content]").val()+"\" />"+
		"<input type=\"hidden\" name=\"dataid\" value=\""+$('#communicationdiv').find("input[name=dataid]").val()+"\" />"+
		"<input type=\"hidden\" name=\"remark\" value=\""+(addnum+1)+"\" />";
	    "<div class=\"clear\"></div>"+
	"</div>";
	$("#communication").append(temp);
	$('#communicationdiv').hide();
	$('.div_mask').hide();
	
	$('tr[name=communicationdaily]').find('a[name=addbtn]').hide();
}
function deletediv(obj){
	swal({
		title : "",
		text : "确认删除？删除之后无法恢复",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		
		if($(obj).parent().parent().attr("id")=="attendance"){
			$('tr[name=attendancedaily]').find('a[name=addbtn]').show();
		}else if($(obj).parent().parent().attr("id")=="todaycustomer"){
			$('tr[name=todaycustomerdaily]').find('a[name=addbtn]').show();
		}else if($(obj).parent().parent().attr("id")=="communication"){
			$('tr[name=communicationdaily]').find('a[name=addbtn]').show();
		}
		$(obj).parent().remove();
	});
	
}

</script>
<body onload="onloadData()">
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
        <div class="right_page">
    	<div class="page_name"><span>新建每日报表</span><a href="javascript:void(0)" onclick="window.history.go(-1)" class="back">返回</a></div>
        <div class="page_tab2">            
            <div class="tab_list">
            <input type="hidden" id="turnover" value="0"/>
            <input type="hidden" id="guestflow" value="0"/>
            <input type="hidden" id="consumption" value="0"/>
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="none_border">
                    <tr class="last_td">
                    	<td class="l_name">店面</td>
                        <td><select class="text" name="organizeid" id="organizeid" onchange="chooseorganize()">
                        <option  value="">请选择店面</option>
                        </select></td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name">收入统计</td>
                        <td>今日营业额&nbsp;&nbsp;:&nbsp;&nbsp;￥<i style="margin-right: 20px" id="turnoveri">0元</i>今日客流量&nbsp;&nbsp;:&nbsp;&nbsp;<i style="margin-right: 20px" id="guestflowi">0</i>今日人均消费&nbsp;&nbsp;:&nbsp;&nbsp;￥<i id="consumptioni">0元</i></td>
                    </tr>
                    <tr class="last_td" id="module" style="display: none;">
                    	<td class="l_name">模版</td>
                        <td><select class="text" name="templateid" id="templateid" onchange="choosetemplate()">
                        <option>请选择模版</option>
                        </select></td>
                    </tr>
                    <tr class="first_td" id="alldata">
                    	<td class="l_name">审核人</td>
                    	<input type="hidden" id="examineuserid" >
                        <td class="img_td"><div class="img f"><img src="../userbackstage/images/pc_page/user_img2.png" id="examineheadimage" width="30" height="30" /></div><i class="i_name" id="examinename"></i><a href="javascript:void(0)" onclick="openOrCloseExamineDiv()" class="add_user">添加</a></td>
                    </tr>
                    <tr>
                    	<td class="l_name">抄送人</td>
                        <td class="img_td" id="CCusernames"><a href="javascript:void(0)" onclick="showCCuserOrganize()" class="add_user">添加</a></td>
                    </tr>
                     <tr class="last_td">
                    	<td class="l_name">统计时间</td>
                        <td><input type="text" class="text" placeholder="请选择统计时间"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  id="statisticaltime"/></td>
                    </tr>
                    <tr>
                    	<td class="l_name">填写人</td>
                        <td class="img_td"><div class="img f"><img src="${userInfo.headimage}" width="30" height="30" /></div><i class="i_name">${userInfo.realname}</i></td>
                    </tr>
                    <tr class="last_td">
                    	<td class="l_name">填写时间</td>
                        <td>${createtime}</td>
                    </tr>
                    <tr class="foot_td">
                    	<td>&nbsp;</td>
                        <td><a href="javascript:void(0)" onclick="submitdata()" class="a_btn bg_yellow">发送</a><a href="<%=request.getContextPath() %>/pc/getEverydayReportList" class="a_btn bg_gay2">取消</a></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>
<div class="div_mask" style="display: none"></div>
<div class="tc_wlmxbox" style="margin-top:-238px; display:none;"  id="todaytraindiv">
	<div class="tc_title"><span>新增今日培训情况</span><a  href="javascript:void(0)" onclick="$('#todaytraindiv').hide();$('.div_mask').hide();" >×</a></div>
    <div class="tab_list">
    	<table width="100%" border="0" cellpadding="0" cellspacing="0"  id="todaytrainul">
        </table>
    </div>
   <div class="tc_btnbox"><a href="javascript:void(0)" onclick="$('#todaytraindiv').hide();$('.div_mask').hide();" class="bg_gay2">取消</a><a href="javascript:void(0)"  onclick="addtodaytrain()" class="bg_yellow">确定</a></div>
</div>
<div class="tc_wlmxbox" style="margin-top:-204px; display:none;" id="attendancediv">
	<!-- <div class="tc_title"><span>新增今日出勤情况</span><a href="javascript:void(0)" onclick="$('#attendancediv').hide();$('.div_mask').hide();">×</a></div> -->
    <div class="tab_list">
    	<table width="100%" border="0" cellpadding="0" cellspacing="0" id="attendanceul">
        </table>
    </div>
    <!-- <div class="tc_btnbox"><a href="javascript:void(0)" onclick="$('#attendancediv').hide();$('.div_mask').hide();" class="bg_gay2">取消</a><a href="javascript:void(0)" onclick="addattendance()" class="bg_yellow">确定</a></div> -->
</div>
<div id="todaycustomerdivid" style=" display:none;">
<div class="tc_textare" style="width:435px; background:#fff; " id="todaycustomerdiv">
	<!-- <div class="tc_title"><span>新增今日主要客情</span><a href="javascript:void(0)" onclick="$('#todaycustomerdiv').hide();$('.div_mask').hide();">×</a></div> -->
	<input type="hidden" name="dataid" />
    <div class="text_box" style="padding:25px 30px;"><textarea placeholder="请输入今日主要客情，最多允许输入800字符" maxlength="800" name="content" style="border:1px solid #eee; width:98%; padding:4px 10px; resize:none; height:120px; line-height:20px;"></textarea></div>
    <!-- <div class="tc_btnbox"><a href="javascript:void(0)" onclick="$('#todaycustomerdiv').hide();$('.div_mask').hide();" class="bg_gay2">取消</a><a href="javascript:void(0)" onclick="addtodaycustomer();" class="bg_yellow">确定</a></div> -->
</div>
</div>
<div id="communicationdivid" style=" display:none;">
<div class="tc_textare" style="width:435px; background:#fff; " id="communicationdiv">
	<!-- <div class="tc_title"><span>新增需要沟通事项</span><a href="javascript:void(0)" onclick="$('#communicationdiv').hide();$('.div_mask').hide();">×</a></div> -->
	<input type="hidden" name="dataid" />
    <div class="text_box" style="padding:25px 30px;"><textarea placeholder="请输入需要沟通事项，最多允许输入800字符" maxlength="800" name="content" style="border:1px solid #eee; width:98%; padding:4px 10px; resize:none; height:120px; line-height:20px;"></textarea></div>
    <!-- <div class="tc_btnbox"><a href="javascript:void(0)" onclick="$('#communicationdiv').hide();$('.div_mask').hide();" class="bg_gay2">取消</a><a href="javascript:void(0)" onclick="addcommunication();" class="bg_yellow">确定</a></div> -->
</div>
</div>
</body>
</html>