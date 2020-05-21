
var noticeid = appBase.getQueryString("noticeid");
var userid = appBase.getQueryString("userid");

var userInfo = {};
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data;
});

var array = new Object();
array.orderid = noticeid;
array.resourcetype = 27;
$(function(){
	var resourceType = appBase.getQueryString("type");
	//修改未读状态
	intoDetailUpdateRead(userid, noticeid, resourceType);
	
	queryNoticeDetail();
	
	queryComment();
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

function queryNoticeDetail(){
	var obj = new Object();
	obj.noticeid =  noticeid;
	obj.receiveid = userid;
	requestPost('/app/getNoticeDetail',obj,'notice_detail',true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == 0 || resultMap.status == '0'){
				var noticemap = resultMap.data;
				
				$('#title').text(noticemap.title);
				$('#content_div').html(noticemap.content);
				$('#createtime').html(noticemap.createtime);
				$('#createname').html(noticemap.realname);
				
				var filelist = noticemap.filelist;
				if(filelist != undefined && filelist != null){
					showimgs(filelist);
				}
			}
		}
	});
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
	}else{
		$('#files').parent().remove();
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
		userForword(userInfo.companyid,noticeid,$('#examineuserid').val(),userInfo.userid,27);
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
	param.resourceid = noticeid;
	param.resourcetype = 27;
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