

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
		$('#organize_zidingyi').show();
		$('#organize_input').hide();
		$('#organize_input').html("");
	});
	
	$('#organize_option').delegate("#organize_zidingyi","click",function(){
		$("li[name='organize_li']").attr("class","");
		$(this).hide();
		$('#organize_input').show();
		$('#organize_input').html('<input type="text" style="border:1px solid #ddd; height:30px; color:#888; font-size:14px; text-align:center;" id="resulttext"/>');
	});
	$('#sure_btn').click(function(){
		var param = $('#organize_option li[class="active"]');
		if(param.length > 0){
			$('#resulttext').val("");
			$('#result_text').val("");
			var organizeid = $(param).attr("organizeid");
			$('#rewardresulttext').val($(param).attr("organizename"));
			$('#result').val(organizeid);
		}else{
			$('#result').val("");
			$('#result_text').val($('#resulttext').val());
			$('#rewardresulttext').val($('#resulttext').val());
		}
		
	});
	
	$("#organize_mask").click(function(){
		close_open_organizeDiv();
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

//添加奖励单信息
function addRewardInfo(){
	var imgs = "";
	var imglist = $('#imgslist').find('div[class="img"]');
	$.each(imglist,function(i,item){
		imgs += $(item).attr("imgurl")+",";
	});
	//var userlist = {"userlist":[{"userid":"1"},{"organizeid":"3"},{"organizeid":"1"},{"organizeid":"4"}]};
	var userlist = $('#userlist').val();
	var param = new Object();
	param.companyid = userInfo.companyid;
	param.rewarduserid= $('#resouceid').val();
	param.realname = $('#resoucename').text();
	param.organizename = $('#organizename').val();
	param.position = $('#identityname').val();
	param.reason = $('#opinion').val();
	param.rewardresult = $('#result').val();
	param.resulttext = $('#result_text').val();
	param.examineuserid = $('#examineuserid').val();
	param.createid = userInfo.userid;
	
	param.userlist = userlist;
	
	param.filelist = imgs;
	param.sound = "";

	param.CCuserlist = $("#CCuserlist").val();
	param.CCusernames = $('#CCusernames').text();
	
	if(param.rewarduserid == ""){
		swal({
			title : "",
			text : "请选择被奖励者。",
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
	if(param.rewardresult == "" && param.resulttext == ""){
		swal({
			title : "",
			text : "请选择奖励结果。",
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
	if(userlist == ""){
		swal({
			title : "",
			text : "请选择发布范围。",
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
	
	if(param.rewarduserid == param.examineuserid){
		swal({
			title : "",
			text : "奖励者和审核者不能是同一个人！",
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
		url:projectpath+"/app/insertRewardInfo",
		type:"post",
		data:param,
		success:function(resultMap){
			if(resultMap.status == 0 || resultMap == '0'){
				swal({
					title : "",
					text : "奖励单新增成功！",
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
	requestPost("/app/getRewardTypeListInfo","","organize",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == '0' || resultMap.status == 0){
				var rewardtypelist = resultMap.data;
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
			html += '<li name="organize_li" organizeid="'+map.dataid+'" organizename="'+map.cname+'">'+map.cname+'</li>';
		});
		html += '<li id="organize_zidingyi">自定义</li>';
		$('#organize_option').append(html+'<li style="display:none;" id="organize_input"></li>');
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