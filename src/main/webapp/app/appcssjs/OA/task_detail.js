
var taskid = appBase.getQueryString("taskid");
var userid = appBase.getQueryString("userid");
var cple = appBase.getQueryString("cple");
var forwarduserid = appBase.getQueryString("forwarduserid");
var userInfo = {};
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data;
});

var array = new Object();
array.orderid = taskid;
array.resourcetype = 19;
$(function(){
	var resourceType = appBase.getQueryString("type");
	//修改未读状态
	intoDetailUpdateRead(userid, taskid, resourceType);
	
	if(cple == "success"){
		$('#iscomplete').show();
		$('#fankui').show();
	}
	
	queryDetail();
	queryComment();
	
	//样式切换
	$('.foot_btn').delegate('span[name="foot_span"]',"click",function(){
		$('span[name="foot_span"]').attr("class","");
		var name = $(this).text();
		if(name=="转发"){
			$(this).attr("class","mid active");
		}else{
			$(this).attr("class","active");
		}
	});
	
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

function queryDetail(){
	var param = new Object();
	param.taskid = taskid;
	requestPost('/app/getTaskInfo',param,"task_detail",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == 0 || resultMap.status == '0'){
				var taskinfo =  resultMap.taskinfo;
				if(taskinfo != null){
					showDetail(taskinfo);
				}
			}
		}
	});
}
var examine_createid = "";
function showDetail(map){
	if(map != null){
		var filelist = map.filelist;
		if(filelist != null && filelist != ""){
			showimgs(filelist);
		}
		var assi = "";
		$.each(map.assistlist,function(k,item){
			if(item != null){
				assi += item.realname+"，";
			}
		});
		assi = assi.substring(0,(assi.length-1));
		$('#title').text(map.title);
		$('#endtime').text(appBase.parseDateMinute(map.endtime));
		$('#rw_content').text(map.content);
		$('#fz_examinename').text(map.examinename);
		$('#xieban').text(assi);
		$('#creaetname').text(map.createname);
		$('#createtime').text(appBase.parseDateMinute(map.createtime));
		$('#createname').attr("onclick","location.href='../member/homepage_info.html?userid="+map.createid+"'");
		$('#fz_examinename').attr("onclick","location.href='../member/homepage_info.html?userid="+map.examineuserid+"'");
		examine_createid = map.createid;
		if(map.examineuserid == userid && cple=="yes"){
			$('#fankui').show();
			$('#opinion').show().text(map.opinion);
			$('#complete').show();
		}else{
			$('#opinion_div').show().text(map.opinion);
			$('.fbtn_none').show();
			$('.foot_btn').show();
		}
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

function successComplete(){
	var obj = new Object();
	obj.taskid = taskid;
	obj.userid = userid;
	obj.opinion = $('#opinion').val();
	obj.createid = examine_createid;
	
	$.ajax({
		url:projectpath+"/app/examineTask",
		type:"post",
		beforeSend:function(a,b){
			ajaxbefore();
		},
		complete:function(a,b){
			ajaxcomplete();
		},
		data:obj,
		success:function(data){
			if(data.status == 0 || data.status == '0'){
				changeIsread(examine_createid,taskid);
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
		sendComment(2,$('#examinename').text(),$('#examineuserid').val());
		userForword(userInfo.companyid,taskid,$('#examineuserid').val(),userInfo.userid,19);
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
	param.resourceid = taskid;
	param.resourcetype = 19;
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