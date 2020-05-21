

var userInfo = "";
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data;
});
$(function(){
	$('#starttime').val(appBase.parseDateMinute(new Date()));
//	$('#endtime').val(appBase.parseDateMinute(new Date()));
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
	$("#starttime").scroller('destroy').scroller($.extend(opt['datetime'], opt['default']));
	$("#endtime").scroller('destroy').scroller($.extend(opt['datetime'], opt['default']));
	
	$('#createname').text(userInfo.realname);
	
	queryOrganizeList();
	
	$('#createtime').text(appBase.parseDateMinute(new Date()));
	
	$('#organize_option').delegate("li[name='organize_li']","click",function(){
		$("li[name='organize_li']").attr("class","");
		$(this).attr("class","active");
	});
	
	$('#organize_option').delegate("#resulttext","click",function(){
		$("li[name='organize_li']").attr("class","");
	});
	$('#sure_btn').click(function(){
		var param = $('#organize_option li[class="active"]');
		var organizeid = $(param).attr("organizeid");
		$('#leavetype').val($(param).attr("organizename"));
		$('#leavetypeid').val(organizeid);
		
		
	});
	
	$("#organize_mask").click(function(){
		close_open_organizeDiv();
    });
	
	$('#hournum').keyup(function(){
		var hournum = $(this).val();
		if(parseInt(hournum) > 8){
			$(this).val("8");
		}
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

function sendRest(){
	var imgs = "";
	var imglist = $('#imgslist').find('div[class="img"]');
	$.each(imglist,function(i,item){
		imgs += $(item).attr("imgurl")+",";
	});
	var param = new Object();
	param.companyid = userInfo.companyid;
	param.leavetype = $('#leavetypeid').val();
	param.starttime = $('#starttime').val();
	param.endtime = $('#endtime').val();
	param.daynum = $('#daynum').val();
	param.hournum = $('#hournum').val();
	param.reason = $('#reason').val();
	param.examineuserid = $('#examineuserid').val();
	param.createid = userInfo.userid;
	param.filelist = imgs;
	param.sound = "";
	
	param.userlist = $("#userlist").val();
	param.CCusernames = $('#releaserange').text();
	
	if(param.leavetype == ""){
		swal({
			title : "",
			text : "请假类型不能为空！",
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
	if(param.starttime == ""){
		swal({
			title : "",
			text : "开始时间不能为空！",
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
	if(param.endtime == ""){
		swal({
			title : "",
			text : "结束时间不能为空！",
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
	if(param.reason == ""){
		swal({
			title : "",
			text : "请假事由不能为空！",
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
			text : "审批人不能为空！",
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
		url:projectpath+"/app/insertLeave",
		data:param,
		beforeSend:function(a,b){
			ajaxbefore();
		},
		complete:function(a,b){
			ajaxcomplete();
		},
		type:"post",
		success:function(resultMap){
			if(resultMap.status == 0 || resultMap.status == '0'){
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
					location.href='rest_list_noexamine.html';
				});
			}
		}
	});
}

function queryOrganizeList(){
	requestPost("/app/getLeaveTypeList","","leavetype",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == '0' || resultMap.status == 0){
				var leavetypelist = resultMap.leavetypelist;
				showOrganizeInfo(leavetypelist);
			}
		}
	});
}

function showOrganizeInfo(list){
	if(list.length > 0 && list != null){
		var html = '<li>请选择</li>';
		$.each(list,function(i,map){
			html += '<li name="organize_li" organizeid="'+map.dataid+'" onclick=\"getOptionValue(this)\" organizename="'+map.cname+'">'+map.cname+'</li>';
		});
		$('#organize_option').append(html);
	}
}

//取值
function getOptionValue(obj){
	var organizeid = $(obj).attr("organizeid");
	$('#leavetype').val($(obj).attr("organizename"));
	$('#leavetypeid').val(organizeid);
	close_open_organizeDiv();
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
			JianKangCache.setData("oa_",resultMap);
			callback(resultMap);
		},error:function(e){
			//取出缓存数据
			JianKangCache.getData("oa_",function(resultMap){
				callback(resultMap);
			});
		},complete:function(){
			$('#jiazaizhong').hide();
		}
	});
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