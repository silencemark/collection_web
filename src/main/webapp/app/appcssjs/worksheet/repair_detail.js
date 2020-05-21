
var repairid = appBase.getQueryString("repairid");
var userid = appBase.getQueryString("userid");
var forwarduserid = appBase.getQueryString("forwarduserid");
var examineuserid = "";
var examinestatus = "";

var userInfo = "";
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data
});

var array = new Object();
array.orderid = repairid;
array.resourcetype = 13;
$(function(){
	var resourceType = appBase.getQueryString("type");
	//修改未读状态
	intoDetailUpdateRead(userid, repairid, resourceType);
	
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
	obj.repairid = repairid;
	requestPost('/app/getRepairDetailInfo',obj,"detail",true,function(resultMap){
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
	getSendName(forwarduserid,"#sendname");
	if(map != null && map != undefined && map != ""){
		$('#findtime').text(map.findtime);
		$('#description').text(map.description);
		$('#checkname').text(map.createname);
		$('#checktime').text(map.createtime);
		$('#cs_examinename').text(map.examinename);
		if(map.updatetime == undefined){
			$('#examinetime').parent().hide();
		}
		$('#examinetime').text(appBase.parseDateMinute(map.updatetime));
		$('#opinion_textarea').text(map.opinion);
		$('#opinion_div').text(map.opinion);
		
		$('#checkname').attr("onclick","location.href='../member/homepage_info.html?userid="+map.createid+"'");
		$('#cs_examinename').attr("onclick","location.href='../member/homepage_info.html?userid="+map.examineuserid+"'");
		
		examineuserid = map.examineuserid;
		examinestatus = map.status;
		examine_createid = map.createid;
		
		$('#CCusernames').text(map.CCusernames);
		if(map.examineuserid == userid && map.status == 0){
			$('#sendmessage').show();
			$('#opinion_textarea').show();
			$('#opinion_div').hide();
		}else{
			$('.fbtn_none').show();
			$('.foot_btn').show();
		}
		//查询评论信息
		queryComment();
		
		var filelist = map.filelist;
		
		if(filelist != undefined){
			showimgs(filelist);
		}
		
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

//提交审核信息
function updateRepairInfo(chun){
	if(examineuserid == userid && examinestatus == 0){
		var param = new Object();
		param.result = 1;
		param.opinion = $('#opinion_textarea').val();
		param.updateid = userid;
		param.repairid = repairid;
		param.companyid = userInfo.companyid;
		$.ajax({
			url:projectpath+"/app/updateRepairInfo",
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
					changeIsread(examine_createid,repairid);
					if(chun == "chun"){
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
					}
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
}

//转发
function logical(){
	updateRepairInfo();
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
		userForword(userInfo.companyid,repairid,$('#examineuserid').val(),userInfo.userid,13);
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
	updateRepairInfo();
	var param = new Object();
	param.content = $('#content').val();
	param.userid = userid;
	param.resourceid = repairid;
	param.resourcetype = 13;
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