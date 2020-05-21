
var userid = "";
var companyid = "";
var organizeid = "";
$(function(){
	
	userid = $('#userid').val();
	companyid = $('#companyid').val();
	organizeid = $('#organizeid').val();
	
	queryOrganizeList();
	queryDishesTypeInfo();
	
});

//添加菜品成本
function addDishesInfo(){
	var imgurls = "";
	var imglists = $('#imglist').find('img');
	$.each(imglists,function(i,item){
		imgurls += $(item).attr("url")+",";
	});
	var param = new Object();
	param.companyid = $('#companyid').val();
	param.orgid = $('#organize_option').val();
	param.dishestype = $('#dishestype_option').val();
	param.dishesname = $('#dishesname').val();
	param.feeding = $('#feeding').val();
	param.costprice = $('#costprice').val();
	param.price = $('#price').val();
	param.costrate = $('#costrate').val();
	param.description = $('#description').val();
	param.examineuserid = $('#examineuserid').val();
	param.createid = $('#userid').val();
	
	param.filelist = imgurls;
	
	param.userlist = $("#CCuseridlist").val();
	param.CCusernames = $('#CCusernamelist').val();
	
	var costrate = param.costrate;
	param.costrate = parseFloat(costrate)/100;
	if(param.orgid == ""){
		swal({
			title : "",
			text : "请选择区域。",
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
	if(param.dishestype == ""){
		swal({
			title : "",
			text : "请选择菜品类型。",
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
	if(param.dishesname == ""){
		swal({
			title : "",
			text : "请输入菜品名称。",
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
	if(param.feeding == ""){
		swal({
			title : "",
			text : "请输入投料标准。",
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
	if(param.costprice == ""){
		swal({
			title : "",
			text : "请输入成本价。",
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
			text : "请选择审核人。",
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
		url:projectpath+"/pc/insertDishesInfo",
		type:"post",
		data:param,
		success:function(resultMap){
			if(resultMap.status == 0 || resultMap == '0'){
				swal({
					title : "",
					text : "菜品成本单新增成功！",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					window.location = projectpath+"/pc/getcostListInfo";
				});
			}
		}
	});
}

//查询区域信息
function queryOrganizeList(){
	var param = new Object();
	param.userid = userid;
	param.companyid =companyid;
	requestPost("/pc/getShopListByUser",param,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == '0' || resultMap.status == 0){
				var rewardtypelist = resultMap.shoplist;
				showOrganizeInfo(rewardtypelist);
			}
		}
	});
}

//显示区域信息
function showOrganizeInfo(list){
	if(list.length > 0 && list != null){
		var html = "";
		$.each(list,function(i,map){
			html += '<option value="'+map.organizeid+'">'+map.organizename+'</option>';
		});
		$('#organize_option').append(html);
	}
}


function queryDishesTypeInfo(){
	requestPost("/pc/getDishesTypeListInfo","",function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == '0' || resultMap.status == 0){
				var rewardtypelist = resultMap.data;
				showDishesTyepeInfo(rewardtypelist);
			}
		}
	});
}

function showDishesTyepeInfo(list){
	if(list.length > 0 && list != null){
		var html = "";
		$.each(list,function(i,map){
			html += '<option value="'+map.dataid+'">'+map.cname+'</li>';
		});
		$('#dishestype_option').append(html);
	}
}

