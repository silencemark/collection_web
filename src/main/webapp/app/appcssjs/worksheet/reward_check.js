
var rewardid = appBase.getQueryString("rewardid");
var userid = appBase.getQueryString("userid");
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
	})
}

var examine_createid = "";
//显示奖励单的基本信息
function showbasicInfo(map){
	if(map != null && map != undefined && map != ""){
		$('#rewardname').text(map.realname);
		$('#rewardeptname').text(map.organizename);
		$('#rewardposition').text(map.position);
		$('#rewardresulttext').text(map.resulttext);
		$('#reason').text(map.reason);
		$('#createname').text(map.createname);
		$('#createtime').text(map.createtime);
		$('#shenpi_examinename').text(map.examineusername);
		$('#examinetime').text(map.examinetime);
		$('#opinion').val(map.opinion);
		
		$('#createname').attr("onclick","location.href='../member/homepage_info.html?userid="+map.createid+"'");
		$('#shenpi_examinename').attr("onclick","location.href='../member/homepage_info.html?userid="+map.examineuserid+"'");
		
		examine_createid = map.createid;
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
		$('#releaserange').text(rangeStr);
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

//奖励单的同意或者拒绝,result，opinion，updateid，rewardid,companyid
function updateRewardInfo(result){
	var param = new Object();
	param.result = result;
	param.opinion = $('#opinion').val();
	param.updateid = userid;
	param.rewardid = rewardid;
	param.companyid = userInfo.companyid;
	if(param.opinion == ""){
		swal({
			title : "",
			text : "审批意见不能为空！",
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
		url:projectpath+"/app/updateRewarddInfo",
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
				changeIsread(examine_createid,rewardid);
				var title=$("#shenpi_examinename").text()+"审批了对你的奖励信息,请及时查看";
				var url="/worksheet/reward_detail.html?rewardid="+rewardid+"&userid="+examine_createid;
				pushMessage(examine_createid,title,url);
				swal({   
					title: "",   
					text: "<font style=\"color:#ff7922; font-size:28px; margin-left:20px;\">操作成功！</font>",
					type:"success",
					timer: 1000,
					html:true,
					showConfirmButton: false 
				}, function(){
					window.history.go(-1);
 				});
			}else{
				swal({
					title : "",
					text : "网络异常，请稍后重试···",
					type : "error",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
				});
			}
		}
	});
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
		userForword(userInfo.companyid,rewardid,$('#examineuserid').val(),userInfo.userid,9);
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
	var param = new Object();
	param.content = $('#content').val();
	param.userid = userid;
	param.resourceid = rewardid;
	param.resourcetype = 9;
	
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