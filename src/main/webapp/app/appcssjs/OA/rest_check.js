
var leaveid = appBase.getQueryString("leaveid");
var userid = appBase.getQueryString("userid");
var userInfo = "";
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data
});

var array = new Object();
array.orderid = leaveid;
array.resourcetype = 18;
$(function(){
	var resourceType = appBase.getQueryString("type");
	//修改未读状态
	intoDetailUpdateRead(userid, leaveid, resourceType);
	
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


function querydetail(){
	var obj = new Object();
	obj.leaveid = leaveid;
	requestPost('/app/getLeaveInfo',obj,"detail",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == '0' || resultMap.status == 0){
				var leaveinfo = resultMap.leaveinfo;
				
				showbasicInfo(leaveinfo);
			}
		}
	})
}

var examine_createid = "";
function examineInfo(result){
	var param = new Object();
	param.leaveid = leaveid;
	param.userid = userid;
	param.opinion = $('#opinion').val();
	param.result = result;
	
	$.ajax({
		url:projectpath+"/app/examineLeave",
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
				changeIsread(examine_createid,leaveid);
				var title=$("#shenpi_examinename").text()+"审批了您的请假单,请及时查看";
				var url="/oa/rest_detail.html?leaveid="+leaveid+"&userid="+examine_createid;
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

//显示奖励单的基本信息
function showbasicInfo(map){
	if(map != null && map != undefined && map != ""){
		$('#reason').text(map.reason);
		$('#typename').text(map.leavetypename);
		$('#starttime').text(map.starttime);
		$('#endtime').text(map.endtime);
		
		$('#hournum').text(map.hournum);
		$('#daynum').text(map.daynum);
		$('#createname').text(map.createname);
		$('#createtime').text(map.createtime);
		$('#shenpi_examinename').text(map.examinename);
		$('#examinetime').text(map.updatetime);
		$('#opinion').text(map.opinion);
		
		$('#createname').attr("onclick","location.href='../member/homepage_info.html?userid="+map.createid+"'");
		$('#shenpi_examinename').attr("onclick","location.href='../member/homepage_info.html?userid="+map.examineuserid+"'");
		
		examine_createid = map.createid;
		$("#CCusernames").text(map.CCusernames);
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
		userForword(userInfo.companyid,leaveid,$('#examineuserid').val(),userInfo.userid,18);
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
	var param = new Object();
	param.content = $('#content').val();
	param.userid = userid;
	param.resourceid = leaveid;
	param.resourcetype = 18;
	
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