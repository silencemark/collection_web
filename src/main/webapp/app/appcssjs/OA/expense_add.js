
var userInfo = "";
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data
});
$(function(){
	var obj = new Object();
	obj.companyid = userInfo.companyid;
	requestPost("/app/initInsertExpense",obj,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == '0' || resultMap.status == 0){
				$('#expenseno').text(resultMap.expenseno);
			}
		}
	});
	
	$('#createname').text(userInfo.realname);
	$('#createtime').text(appBase.parseDateMinute(new Date()));
	
	$('#detail_div_mask').click(function(){
		open_close_div();
	});
});


function sendExpense(){
	var detaillist = {"detaillist":[]};
	var list = $('input[name="detail_mingxi"]');
	$.each(list,function(i,item){
		var detail = $(item).val().replaceAll("'",'"');
		detaillist.detaillist[i] = JSON.parse(detail);
	});
	var param = new Object();
	param.companyid = userInfo.companyid;
	param.expenseno = $('#expenseno').text();
	param.examineuserid = $('#examineuserid').val();
	param.createid = userInfo.userid;
	param.detailprice = $('#detailprice').text();
	param.detailnum = $('#detailnum').text();
	param.detaillist = JSON.stringify(detaillist);
	
	param.userlist = $("#userlist").val();
	param.CCusernames = $('#releaserange').text();
	if(param.expenseno == ""){
		swal({
			title : "",
			text : "报销单编号请求失败，请重新进入该页面！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			goBackPage();
		});
		return false;
	}
	if(detaillist.detaillist == ""){
		swal({
			title : "",
			text : "报销明细不能为空！",
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
	if(param.examineuserid == ""){
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
		url:projectpath+'/app/insertExpense',
		type:'post',
		beforeSend:function(a,b){
			ajaxbefore();
		},
		complete:function(a,b){
			ajaxcomplete();
		},
		data:param,
		success:function(resultMap){
			if(resultMap.status == 0 || resultMap.status == '0'){
				swal({
					title : "",
					text : "报销单发送成功！",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href='expenseaccount_list_noexamine.html';
				});
			}
		},
		error:function(msg){
			
		}
	});
	/*requestPost('/app/insertExpense',param,function(resultMap){
		if(resultMap.status == 0 || resultMap.status == '0'){
			swal({
				title : "",
				text : "报销单发送成功！",
				type : "success",
				showCancelButton : false,
				confirmButtonColor : "#ff7922",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			}, function(){
				location.href='expenseaccount_list_noexamine.html';
			});
		}
	});*/
}

function addDetail(){
	var detailtype = $('#detailtype').val();
	var money = $('#money').val();
	var detail = $('#detail').val();
	
	if(detailtype == ""){
		swal({
			title : "",
			text : "类型不能为空！",
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
	if(money == ""){
		swal({
			title : "",
			text : "金额不能为空！",
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
	
	var param = new Object();
	param.type = detailtype;
	param.price = money;
	param.detail = detail;
	param = JSON.stringify(param).replaceAll('"',"'");
	
	var time = new Date().getTime();
	var htm = '<tr id="'+time+'"><input type="hidden" name="detail_mingxi" value="'+param+'"/>'+
			    	'<td class="first"">'+detailtype+'</td>'+
			        '<td>'+money+'</td>'+
			        '<td>'+detail+'</td>'+
			        '<td class="last"><a onclick="deleteDetail(\''+time+'\',\''+money+'\')">删除</a></td>'+
			    '</tr>';
	$('#baoxiaomingxi').append(htm);
	
	$('#detailprice').text(parseInt($('#detailprice').text())+parseInt(money));
	$('#detailnum').text(parseInt($('#detailnum').text())+1);
	open_close_div();
	$('#detailtype').val("");
	$('#money').val("");
	$('#detail').val("");
}

function deleteDetail(time,money){
	$('#'+time).remove();
	$('#detailprice').text(parseInt($('#detailprice').text())-parseInt(money));
	$('#detailnum').text(parseInt($('#detailnum').text())-1);
}

function open_close_div(){
	var mask = $('#detail_div_mask').css("display");
	var div = $('#detail_div').css("display");
	if(mask == "none" && div == "none"){
		$('#detail_div_mask').show();
		$('#detail_div').show();
	}else{
		$('#detail_div_mask').hide();
		$('#detail_div').hide();
	}
}

function requestPost(url,data,callback){
	$.ajax({
		url:projectpath+url,
		type:"post",
		data:data,
		success:function(resultMap){
			callback(resultMap);
		}
	});
}