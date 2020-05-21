
var dishesid = appBase.getQueryString("dishesid");
var userid = appBase.getQueryString("userid");
var forwarduserid = appBase.getQueryString("forwarduserid");
var examineuserid = "";
var examinestatus = "";

var userInfo = "";
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data
});

var array = new Object();
array.orderid = dishesid;
array.resourcetype = 14;
$(function(){
	var resourceType = appBase.getQueryString("type");
	//修改未读状态
	intoDetailUpdateRead(userid, dishesid, resourceType);
	
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

//查询奖励单详情
function querydetail(){
	var obj = new Object();
	obj.dishesid = dishesid;
	requestPost('/app/getDishesDetailInfo',obj,"detail",true,function(resultMap){
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
		$('#organizename').text(map.organizename);
		$('#dishestypename').text(map.dishestype!=null && map.dishestype!=undefined && map.dishestype!=""?map.dishestype:"");
		$('#dishesname').text(map.dishesname);
		$('#costprice').text(map.costprice);
		$('#price').text(map.price);
		$('#costrate').text(map.costrate);
		$('#feeding').text(map.feeding);
		$('#description').text(map.description);
		$('#createname').text(map.createname);
		$('#createtime').text(map.createtime);
		$('#cs_examinename').text(map.examinename);
		if(map.updatetime == undefined){
			$('#examinetime').parent().hide();
		}
		$('#examinetime').text(appBase.parseDateMinute(map.updatetime));
		$('#opinion_textarea').text(map.opinion);
		$('#opinion_div').text(map.opinion);
		
		$('#createname').attr("onclick","location.href='../member/homepage_info.html?userid="+map.createid+"'");
		$('#cs_examinename').attr("onclick","location.href='../member/homepage_info.html?userid="+map.examineuserid+"'");
		
		examine_createid =  map.createid;
		examineuserid = map.examineuserid;
		examinestatus = map.status;
		
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
				imghtml += '<div class="img"><b><img onclick=\"showbigimg(this)\" src="'+projectpath+"/"+map.visiturl+'"></b></div>';
			}else if(map.type == 2){
				imghtml += '<div class="talk" recordAudio="'+map.visiturl+'" onclick="playRecordAudioSound(this)">语言文件</div>';
			}
		});
		imghtml += '<div class="clear"></div>';
		
		$('#files').html(imghtml);
	}
}

//提交审核信息
function updateDishesInfo(chun){
	if(examineuserid == userid && examinestatus == 0){
		var param = new Object();
		param.result = 1;
		param.opinion = $('#opinion_textarea').val();
		param.updateid = userid;
		param.dishesid = dishesid;
		param.companyid = userInfo.companyid;
		$.ajax({
			url:projectpath+"/app/updateDishesInfo",
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
					changeIsread(examine_createid,dishesid);
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
	updateDishesInfo();
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
		userForword(userInfo.companyid,dishesid,$('#examineuserid').val(),userInfo.userid,14);
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
	updateDishesInfo();
	
	var param = new Object();
	param.content = $('#content').val();
	param.userid = userid;
	param.resourceid = dishesid;
	param.resourcetype = 14;
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