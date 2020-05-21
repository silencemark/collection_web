

var userInfo = {};
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data;
});
$(function(){
	$('#endtime').val(appBase.parseDateMinute(new Date()));
	$('#createname').text(userInfo.realname);
	$('#createtime').text(appBase.parseDateMinute(new Date()));
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
	$("#endtime").scroller('destroy').scroller($.extend(opt['datetime'], opt['default']));
});

function clear_xiebanren(){
	$('#releaserange').text("");
	var alldata={"userlist":[]};
	$('#userlist').val(JSON.stringify(alldata));
}

function onPhotoDataSuccess(imageData,fileSize) {
	$('#imgslist').append('<div class="img" imgurl="'+imageData+'"><b><img onclick=\"showbigimg(this)\" src="'+projectpath+imageData+'"></b><div class="del" onclick="$(this).parent().remove()">删除</div></div>');
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

function addTaskInfo(){
	var imgs = "";
	var imglist = $('#imgslist').find('div[class="img"]');
	$.each(imglist,function(i,item){
		imgs += $(item).attr("imgurl")+",";
	});
	var param = new Object();
	param.companyid = userInfo.companyid;
	param.title = $('#title').val();
	param.content = $('#content').val();
	param.endtime = $('#endtime').val();
	param.examineuserid = $('#examineuserid').val();
	param.createid = userInfo.userid;
	param.userlist = $('#userlist').val();
	param.filelist = imgs;
	param.sound = "";
	
	if(param.endtime==""){
		swal({
			title : "",
			text : "截止时间不能为空！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		});
		return false;
	}else{
		var createtime = $("#createtime").text();
		var date_now = new Date(createtime).getTime();
		var date_end = new Date(param.endtime).getTime();
		if(date_end <= date_now){
			swal({
				title : "",
				text : "截止时间不能小于任务下达时间！",
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
	}
	
	if(param.title==""){
		swal({
			title : "",
			text : "任务标题不能为空！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		});
		return false;
	}if(param.content==""){
		swal({
			title : "",
			text : "任务内容不能为空！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		});
		return false;
	}if(param.examineuserid==""){
		swal({
			title : "",
			text : "任务负责人不能为空！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		});
		return false;
	}if(param.userlist!=""){
		var userlist = JSON.parse(param.userlist).userlist;
		var fag = false;
		$.each(userlist,function(i,item){
			if(param.examineuserid == item.userid){
				swal({
					title : "",
					text : "协办人不能是负责人！",
					type : "warning",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
				});
				fag = true;
			}
		});
		if(fag){
			return false
		}
	}
	
	param.sound = localStorage.http_audio_foldname_url;
	$.ajax({
		url:projectpath+"/app/insertTask",
		type:"post",
		beforeSend:function(a,b){
			ajaxbefore();
		},
		complete:function(a,b){
			ajaxcomplete();
		},
		data:param,
		success:function(data){
			if(data.status == 0 || data.status == '0'){
				swal({
					title : "",
					text : "发送成功！",
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
 