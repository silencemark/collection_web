

var userInfo = "";
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data;
});
$(function(){
	$('#createname').text(userInfo.realname);
	
	queryOrganizeList();
	
	$('#createtime').text(appBase.parseDateMinute(new Date()));
	
	$('#organize_option').delegate("li[name='organize_li']","click",function(){
		$("li[name='organize_li']").attr("class","");
		$(this).attr("class","active");
	});
	
	$('#sure_btn').click(function(){
		var param = $('#organize_option li[class="active"]');
		if(param.length > 0){
			var organizeid = $(param).attr("organizeid");
			$('#organizename').text($(param).attr("organizename"));
			$('#organizeid').val(organizeid);
		}
		
	});
	
	$("#organize_mask").click(function(){
		close_open_organizeDiv();
    });
	
	
	
	
	$('#dishestype_option').delegate('li[name="dishestype_li"]','click',function(){
		$("li[name='dishestype_li']").attr("class","");
		$(this).attr("class","active");
	});
	$('#dishestype_btn').click(function(){
		var param = $('#dishestype_option li[class="active"]');
		if(param.length > 0){
			var dishestypeid = $(param).attr("dishestypeid");
			$('#dishestypename').val($(param).attr("dishestypename"));
			$('#dishestypeid').val(dishestypeid);
		}
	});
	
	$("#dishestype_mask").click(function(){
		close_open_dishestypeDiv();
    });
});

function onPhotoDataSuccess(imageData,fileSize) {
	$('#imgslist').append('<div class="img" imgurl="'+imageData+'" filesize="'+fileSize+'"><b><img onclick=\"showbigimg(this)\" src="'+projectpath+imageData+'"></b><div class="del" onclick="$(this).parent().remove()">删除</div></div>');
}

function uploadimage(num){
	var imglist = $('#imgslist').find('div[class="img"]');
	if(imglist.length < num){
		getPhoto(pictureSource.PHOTOLIBRARY,{type:11});
	}else{
		swal({
			title : "",
			text : "最多只能上传"+num+"张图片！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		});
	}
}

//添加菜品成本
function addDishesInfo(){
	var imgs = "";
	var imglist = $('#imgslist').find('div[class="img"]');
	$.each(imglist,function(i,item){
		imgs += $(item).attr("imgurl")+",";
	});
	
	var param = new Object();
	param.companyid = userInfo.companyid;
	param.orgid = $('#organizeid').val();
	param.dishestype = $('#dishestypename').val();
	param.dishesname = $('#dishesname').val();
	param.feeding = $('#feeding').val();
	param.costprice = $('#costprice').val();
	param.price = $('#price').val();
	param.costrate = $('#costrate').val();
	param.description = $('#description').val();
	param.examineuserid = $('#examineuserid').val();
	param.createid = userInfo.userid;
	
	param.filelist = imgs;
	param.sound = "";
	
	param.userlist = $("#userlist").val();
	param.CCusernames = $('#releaserange').text();
	
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

	param.sound = localStorage.http_audio_foldname_url;
	$.ajax({
		url:projectpath+"/app/insertDishesInfo",
		beforeSend:function(a,b){
			ajaxbefore();
		},
		complete:function(a,b){
			ajaxcomplete();
		},
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
					goBackPage();
				});
			}
		}
	});
}

//查询区域信息
function queryOrganizeList(){
	var param = new Object();
	param.userid = userInfo.userid;
	param.companyid = userInfo.companyid;
	requestPost("/app/getShopListByUser",param,"organize",true,function(resultMap){
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
			html += '<li name="organize_li" organizeid="'+map.organizeid+'" organizename="'+map.organizename+'">'+map.organizename+'</li>';
		});
		$('#organize_option').append(html);
	}
}



//打开或者关闭区域选择层
function close_open_organizeDiv(){
	var disk = $('#organize_mask').css("display");
	var organize = $('#organize_div').css("display");
	if(disk == "none" && organize == "none"){
		$('#organize_mask').show();
		$('#organize_div').show();
	}else{
		$('#organize_mask').hide();
		$('#organize_div').hide();
	}
	$('#dishestype_div').hide();
}


function queryDishesTypeInfo(){ 
	var param = new Object();
	param.organizeid=$('#organizeid').val();
	$.ajax({		
		data:param,		
		type:"post",
		url:projectpath+'/app/getDishestypeListInfoByOrganizeId',
		success:function(resultMap){
			if(resultMap != undefined && resultMap != null){				
				var dishestypelist = resultMap.data;
				showDishesTyepeInfo(dishestypelist);				
			}
		},error:function(e){
			
		}
	});
}

function showDishesTyepeInfo(lists){
	if(lists.length > 0 && lists != null){
		var html = "";
		$.each(lists,function(i,list){			
			html+='<li onclick="chooseDishesType(this)">'+list.dishestype+'</li>';						
		});				
		$('#organize_optionk').html(html);
	}else{
		
	}
}

//关闭
function close_organizeDivk(){
	$('#organize_maskk').hide();
	$('#organize_divk').hide();
}

//类型输入
function typewrite(){
	close_organizeDivk();
	$("#dishestypename").focus();
}

function chooseDishesType(e){
	var text=$(e).html();
	$('#dishestypename').val(text);
	$('#organize_maskk').hide();
	$('#organize_divk').hide();
}


function close_open_dishestypeDiv(){
	$("#dishestypename").blur();
	var organizeid=$('#organizeid').val();
	if(organizeid!=''){
		
		queryDishesTypeInfo()
	}else{		
		
	}
	close_open_organizeDivk();
}

//类型选择康打开或者关闭区域选择层
function close_open_organizeDivk(){
	var disk = $('#organize_maskk').css("display");
	var organize = $('#organize_divk').css("display");
	if(disk == "none" && organize == "none"){
		$('#organize_maskk').show();
		$('#organize_divk').show();
	}else{
		$('#organize_divk').hide();
		$('#organize_maskk').hide();
	}
}

//公共的查询方法
function requestPost(url,data,identifying,issynchroniz,callback){
	$.ajax({
		url:projectpath+url,
		data:data,
		async:issynchroniz,
		type:"post",
		beforeSend:function(){
			$('#jiazaizhong').show();
		},
		success:function(resultMap){
			//保存到当前路径缓存
			JianKangCache.setData("worksheet_",resultMap);
			callback(resultMap);
		},error:function(e){
			//取出缓存数据
			JianKangCache.getData("worksheet_",function(resultMap){
				callback(resultMap);
			});
		},complete:function(){
			$('#jiazaizhong').hide();
		}
	});
}