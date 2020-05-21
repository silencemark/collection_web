
var rewardid = appBase.getQueryString("rewardid");
var userid = appBase.getQueryString("userid");
var forwarduserid = appBase.getQueryString("forwarduserid");
var userInfo = "";
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data
});

var array = new Object();
array.orderid = rewardid;
array.resourcetype = 9;
$(function(){
	var resourceType = appBase.getQueryString("type");
	//修改未读状态
	intoDetailUpdateRead(userid, rewardid, resourceType);
	
	querydetail();
	
	//样式切换
	$('.foot_btn').delegate('span[name="foot_span"]',"click",function(){
		$('span[name="foot_span"]').attr("class","");
		$(this).attr("class","active");
	});
	
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

//查询奖励单详情
function querydetail(){
	var obj = new Object();
	obj.rewardid = rewardid;
	requestPost('/app/getRewardDetailInfo',obj,"detail",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == '0' || resultMap.status == 0){
				var rewardmap = resultMap.data;
				
				showbasicInfo(rewardmap);
			}
		}
	});
}

//显示奖励单的基本信息
function showbasicInfo(map){
	getSendName(forwarduserid,"#sendname");
	if(map != null && map != undefined && map != ""){
		$('#rewardname').text(map.realname);
		$('#rewardeptname').text(map.organizename);
		$('#rewardposition').text(map.position);
		$('#rewardresulttext').text(map.resulttext);
		$('#reason').text(map.reason);
		$('#createname').text(map.createname);
		$('#createtime').text(map.createtime);
		$('#shenpi_examinename').text(map.examineusername);
		if(map.examinetime == undefined){
			$('#examinetime').parent().hide();
		}
		$('#examinetime').text(map.examinetime);
		$('#opinion').text(map.opinion);
		
		$('#createname').attr("onclick","location.href='../member/homepage_info.html?userid="+map.createid+"'");
		$('#shenpi_examinename').attr("onclick","location.href='../member/homepage_info.html?userid="+map.examineuserid+"'");
		
		if(map.status == '1' || map.status == 1){
			if(map.result == 1 || map.result == '1'){
				$('#agree').show();
			}else if(map.result == 0 || map.result == '0'){
				$('#agree').show();
				$('#agree').find('img').attr("src","../appcssjs/images/public/agree_img2.png");
			}
		}
		$('#CCusernames').text(map.CCusernames);
		
		//查询评论信息
		queryComment();
		
		var rangelist = map.rangelist;
		var filelist = map.filelist;
		
		showrange(rangelist);
		showimgs(filelist);
		
	}
}

//显示发布范围
function showrange(rangelist){
	if(rangelist.length > 0 && rangelist != null){
		var rangeStr = "";
		$.each(rangelist,function(i,map){
			var dou = "，";
			if((i+1) == rangelist.length){
				dou = "";
			}
			rangeStr += map.rangename+dou;
		});
		$('#releaserange').html("<nobr>"+rangeStr+"</nobr>");
	}
}

//显示发布的语音和图片
function showimgs(filelist){
	if(filelist.length > 0 && filelist != null){
		var imghtml = "";
		$.each(filelist,function(i,map){
			if(map.type == 1){
				imghtml += '<div class="img"><b><img onclick=\"showbigimg(this)\" src="'+projectpath+map.visiturl+'"></b></div>';
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
		userForword(userInfo.companyid,rewardid,$('#examineuserid').val(),userInfo.userid,9);
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
	param.resourceid = rewardid;
	param.resourcetype = 9;
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