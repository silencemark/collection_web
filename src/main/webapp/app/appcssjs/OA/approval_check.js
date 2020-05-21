
var currencyexamineid = appBase.getQueryString("currencyexamineid");
var userid = appBase.getQueryString("userid");
var userInfo = "";
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data
});

var array = new Object();
array.orderid = currencyexamineid;
array.resourcetype = 15;
$(function(){
	var resourceType = appBase.getQueryString("type");
	//修改未读状态
	intoDetailUpdateRead(userid, currencyexamineid, resourceType);
	
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
	obj.currencyexamineid = currencyexamineid;
	requestPost('/app/getCurrencyExamineDetailInfo',obj,"detail",true,function(resultMap){
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
		$('#detail').text(map.details);
		$('#ju_content').text(map.content);
		
		$('#createname').text(map.createname);
		$('#createtime').text(map.createtime);
		$('#shenpi_examinename').text(map.examinename);
		$('#examinetime').text(map.updatetime);
		$('#opinion').text(map.opinion);
		examine_createid = map.createid;
		
		$('#createname').attr("onclick","location.href='../member/homepage_info.html?userid="+map.createid+"'");
		$('#shenpi_examinename').attr("onclick","location.href='../member/homepage_info.html?userid="+map.examineuserid+"'");
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

function sendExamine(result){
	var param = new Object();
	param.currencyexamineid = currencyexamineid;
	param.userid = userid;
	param.opinion = $('#opinion').val();
	param.result = result;
	
	$.ajax({
		url:projectpath+"/app/updateCurrencyExamineInfo",
		type:"post",
		beforeSend:function(a,b){
			ajaxbefore();
		},
		complete:function(a,b){
			ajaxcomplete();
		},
		data:param,
		success:function(resultMap){
			if(resultMap.status == '0' || resultMap.status == 0){
				changeIsread(examine_createid,currencyexamineid);
				var title=$("#shenpi_examinename").text()+"审批了您的通用审批申请,请及时查看";
				var url="/oa/approval_detail.html?currencyexamineid="+currencyexamineid+"&userid="+examine_createid;
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
		userForword(userInfo.companyid,currencyexamineid,$('#examineuserid').val(),userInfo.userid,15);
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
	param.resourceid = currencyexamineid;
	param.resourcetype = 15;
	
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