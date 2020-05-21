

var userInfo = "";
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data;
});
$(function(){
	$('#findtime').val(appBase.parseDateMinute(new Date()));
	var currYear = (new Date()).getFullYear();	
	var opt={};
		opt.date = {preset : 'date', dateFormat: 'yy-mm-dd' };
		//opt.datetime = { preset : 'datetime', minDate: new Date(2012,3,10,9,22), maxDate: new Date(2014,7,30,15,44), stepMinute: 5  };
		opt.datetime = {preset : 'datetime', dateFormat: 'yy-mm-dd' }; 
		opt.time = {preset : 'time'};
		opt.default = { 
			theme: 'android-ics light', //皮肤样式  
	        display: 'modal', //显示方式  
	        mode: 'scroller', //日期选择模式
	        dateOrder : 'yymmdd',  
			lang:'zh',
	        startYear:currYear - 10, //开始年份
	        endYear:currYear + 10 //结束年份
		};
	//初始化时间插件
	$("#findtime").scroller('destroy').scroller($.extend(opt['datetime'], opt['default']));
	
	$('#createname').text(userInfo.realname);
	$('#createtime').text(appBase.parseDateMinute(new Date()));
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
function addRepairInfo(){
	var imgs = "";
	var imglist = $('#imgslist').find('div[class="img"]');
	$.each(imglist,function(i,item){
		imgs += $(item).attr("imgurl")+",";
	});
	var param = new Object();
	param.companyid = userInfo.companyid;
	param.findtime = $('#findtime').val();
	param.description = $('#opinion').val();
	param.examineuserid = $('#examineuserid').val();
	param.createid = userInfo.userid;
	param.filelist = imgs;
	param.sound = "";
	
	param.userlist = $("#userlist").val();
	param.CCusernames = $('#releaserange').text();
	
	if(param.description == ""){
		swal({
			title : "",
			text : "地点描述不能为空！",
			type : "warning",
			showCancelButton : true,
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
			text : "抄送人不能为空！",
			type : "warning",
			showCancelButton : true,
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
		url:projectpath+"/app/insertRepairInfo",
		beforeSend:function(a,b){
			ajaxbefore();
		},
		complete:function(a,b){
			ajaxcomplete();
		},
		type:"post",
		data:param,
		success:function(resultMap){
			if(resultMap.status == 0 || resultMap.status == '0'){
				swal({
					title : "",
					text : "添加成功！",
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
