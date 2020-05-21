
var userInfo = {};
var userid = "";
var companyid = "";
JianKangCache.getGlobalData('userinfo',function(data){
		companyid = data.companyid;
		userid = data.userid;
		userInfo = data;
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

function addNoticeInfo(){
	var imgs = "";
	var imglist = $('#imgslist').find('div[class="img"]');
	$.each(imglist,function(i,item){
		imgs += $(item).attr("imgurl")+",";
	});
	var param = new Object();
	param.companyid = companyid;
	param.title = $('#title').val();
	param.content = $('#content').val();
	param.userid = userid;
	param.userlist = $('#userlist').val();
	param.filelist = imgs;
	param.sound = "";
	
	if(param.title == ""){
		swal({
			title : "",
			text : "请输入标题！",
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
			text : "请输入内容！",
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
	if(param.userlist == ""){
		swal({
			title : "",
			text : "请选择发布范围！",
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
		url:projectpath+"/app/insertNotice",
		beforeSend:function(a,b){
			ajaxbefore();
		},
		complete:function(a,b){
			ajaxcomplete();
		},
		type:"post",
		data:param,
		success:function(data){
			if(data.status == "0"){
				swal({
					title : "",
					text : "通知信息发送成功！",
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