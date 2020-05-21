
var requestid = appBase.getQueryString("requestid");
var userid = appBase.getQueryString("userid");
var forwarduserid = appBase.getQueryString("forwarduserid");
var userInfo = "";
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data
});
var array = new Object();
array.orderid = requestid;
array.resourcetype = 16;
$(function(){
	var resourceType = appBase.getQueryString("type");
	//修改未读状态
	intoDetailUpdateRead(userid, requestid, resourceType);
	
	querydetail();
	
	//样式切换
	$('.foot_btn').delegate('span[name="foot_span"]',"click",function(){
		$('span[name="foot_span"]').attr("class","");
		$(this).attr("class","active");
	});
	getSendName(forwarduserid,"#sendname");
	//评论加载更多
	PageHelper({
		url:projectpath+"/app/getOrderComment",
		data:array,
		success:function(resultMap){
			if(resultMap != undefined && resultMap != null){
				var commentlist = resultMap.commentlist;
				showComment(commentlist);
			}
		}
	});
});


function querydetail(){
	var obj = new Object();
	obj.requestid = requestid;
	requestPost('/app/getRequestInfo',obj,"detail",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == '0' || resultMap.status == 0){
				var requestinfo = resultMap.requestinfo;
				
				showbasicInfo(requestinfo);
			}
		}
	})
}

//显示奖励单的基本信息
function showbasicInfo(map){
	if(map != null && map != undefined && map != ""){
		$('#reason').text(map.reason);
		$('#ju_content').text(map.content);
		$('#createname').text(map.createname);
		$('#createtime').text(appBase.parseDateMinute(map.createtime));
		$('#sp_examinename').text(map.examinename);
		if(map.updatetime == undefined){
			$('#examinetime').parent().hide();
		}
		$('#examinetime').text(appBase.parseDateMinute(map.updatetime));
		$('#opinion').text(map.opinion);
		$('#createname').attr("onclick","location.href='../member/homepage_info.html?userid="+map.createid+"'");
		$('#sp_examinename').attr("onclick","location.href='../member/homepage_info.html?userid="+map.examineuserid+"'");
		if(map.status == 1 || map.status == '1'){
			if(map.result == 1 || map.result == '1'){
				$('#agree').show();
			}else if(map.result == 2 || map.result == '2'){
				$('#agree').show();
				$('#agree').find('img').attr("src","../appcssjs/images/public/agree_img2.png");
			}
		}
		var jinji = "";
		if(parseInt(map.urgentlevel) == 1){
			jinji = '<i class="w_green">普通</i>';
		}
		if(parseInt(map.urgentlevel) == 2){
			jinji = '<i class="w_yellow">紧急</i>';		
		}
		if(parseInt(map.urgentlevel) == 3){
			jinji = '<i class="w_red">非常紧急</i>';
		}
		$('#jinji').html(jinji);
		
		$('#CCusernames').text(map.CCusernames);
		//查询评论信息
		queryComment();
		
		var filelist = map.filelist;
		
		showimgs(filelist);
		
	}
}

//显示发布的语音和图片
function showimgs(filelist){
	if(filelist.length > 0 && filelist != null){
		var imghtml = "";
		$.each(filelist,function(i,map){
			if(map.type == 1){
				imghtml += '<div class="img"><b><img onclick=\"showbigimg(this)\" src="'+projectpath+"/"+map.visiturl+'"></b></div>';
			}else if(map.type == 2){
				imghtml += '<div class="talk" recordAudio="'+map.visiturl+'" onclick="playRecordAudioSound(this)">语言文件</div>';
			}
		});
		imghtml += '<div class="clear"></div>';
		
		$('#files').html(imghtml);
	}
}

//转发
function logical(){
	swal({
		title : "",
		text : "确认转发给"+$('#examinename').text()+"？",
		type : "success",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		sendComment(2,$('#examinename').text(),$('#examineuserid').val());
		userForword(userInfo.companyid,requestid,$('#examineuserid').val(),userInfo.userid,16);
		location.reload(false);
	});
}

//查询评论信息
function queryComment(){
	
	requestPost('/app/getOrderComment',array,'comment',true,function(resultMap){
		if(resultMap != undefined && resultMap != ""){
			var commentlist = resultMap.commentlist;
			var commentnum = resultMap.num;
			$('#commentCount').text(commentnum);
			showComment(commentlist);
		}
	});
}

//添加评论信息
function addComment(){
	if(isforwarding==1){
		sendComment(1,"","");
		location.reload(false);
	}else{			
		$('.div_mask').hide();
		$('#commentdiv').hide();
		$('#organizeiframe').show();
		$('#htmlbody').hide();		
	}
}

function sendComment(type,forwardusernames,forwarduserids){
	var param = new Object();
	param.content = $('#content').val();
	param.userid = userid;
	param.resourceid = requestid;
	param.resourcetype = 16;
	param.type = type;
	param.forwardusernames = forwardusernames;
	param.forwarduserids = forwarduserids;
	addcommentAjax(param);
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