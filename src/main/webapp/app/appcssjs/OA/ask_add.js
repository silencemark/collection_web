
var userInfo = "";
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data
});
$(function(){
	$('#createname').text(userInfo.realname);
	$('#createtime').text(appBase.parseDateMinute(new Date()));
	
	
});

function changeUrgent(obj){
	$('a[name="urgent"]').attr("class","check");
	$(obj).find('a[name="urgent"]').attr("class","check_ed");
	var level = $(obj).find('a[name="urgent"]').attr("urgentlevel");
	$('#level').val(level);
}

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

function sendMessage(){
	var imgs = "";
	var imglist = $('#imgslist').find('div[class="img"]');
	$.each(imglist,function(i,item){
		imgs += $(item).attr("imgurl")+",";
	});
	var param = new Object();
	param.companyid = userInfo.companyid;
	param.reason = $('#reason').val();
	param.content = $('#content').val();
	param.examineuserid = $('#examineuserid').val();
	param.urgentlevel = $('#level').val();
	param.createid = userInfo.userid;
	param.userlist = $('#userlist').val();
	param.filelist = imgs;
	param.sound = "";
	
	param.userlist = $("#userlist").val();
	param.CCusernames = $('#releaserange').text();
	
	if(param.reason == ""){
		swal({
			title : "",
			text : "请输入事由！",
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
	if(param.content == ""){
		swal({
			title : "",
			text : "请输入具体内容！",
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
			text : "请选择审批人！",
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
		url:projectpath+"/app/insertRequest",
		type:"post",
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
					text : "请示信息发送成功！",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href='ask_list_noexamine.html';
				});
			}
		}
	});
}