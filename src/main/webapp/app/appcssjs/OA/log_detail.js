
var pid = appBase.getQueryString("pid");
var userid = appBase.getQueryString("userid");
var jotype = appBase.getQueryString("jotype");
var forwarduserid = appBase.getQueryString("forwarduserid");

var userInfo = {};
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data;
});

var resoucetype = "";

var array = new Object();
array.orderid = pid;

$(function(){
	var resourceType = appBase.getQueryString("type");
	//修改未读状态
	intoDetailUpdateRead(userid, pid, resourceType);
	
	var title="";
	var summary = "";
	var complete = "";
	var nextplan = "";
	if(jotype==1 || jotype == '1'){
		title = "日报详情";
		complete = "今日完成工作";
		summary = "未完成工作";
		$('#nextplan').hide();
		resoucetype = 23;
	}else if(jotype==2 || jotype == '2'){
		title = "周报详情";
		complete = "本周完成工作";
		summary = "本周工作总结";
		nextplan = "下周工作计划";
		resoucetype = 24;
	}else if(jotype==3 || jotype == '3'){
		title = "月报详情";
		complete = "本月完成工作";
		summary = "本月工作总结";
		nextplan = "下周工作计划";
		resoucetype = 25;
	}
	$('#journaltitle').text(title);
	$('#completename').text(complete);
	$('#summaryname').text(summary);
	$('#nextplanname').text(nextplan);
	
	queryDetail();
	
	queryComment();
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
var examine_createid = "";
var examine_commentid = "";
function queryDetail(){
	var obj = new Object();
	obj.jotype =  jotype;
	obj.logid = pid;
	requestPost('/app/getJournalDetaiInfo',obj,'log_detail',true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == 0 || resultMap.status == '0'){
				var jomap = resultMap.data;
				
				$('#time').val(jomap.time);
				$('#completecontent').text(jomap.completedwork);
				$('#summarycontent').text(jomap.worksummary);
				$('#nextplancontent').text(jomap.nextplan);
				$('#helpcontent').text(jomap.needhelp);
				$('#remark').text(jomap.remark);
				
				$('#createname').text(jomap.createname);
				$('#createtime').text(jomap.createtime);
				$('#commentrealname').text(jomap.commentusername);
				if(jomap.updatetime == undefined){
					$('#commenttime').parent().hide();
				}
				$('#commenttime').text(jomap.updatetime);
				$('#createname').attr("onclick","location.href='../member/homepage_info.html?userid="+jomap.userid+"'");
				$('#commentrealname').attr("onclick","location.href='../member/homepage_info.html?userid="+jomap.commentuserid+"'");
				examine_createid = jomap.createid;
				examine_commentid = jomap.commentuserid;
				
				$('#CCusernames').text(jomap.CCusernames);
				
				var xing = "";
				if(jomap.status == 0 || jomap.status == '0'){
					if(jomap.commentuserid == userid){
						$('#sendmessage').show();
						for(var i=0;i<5;i++){
							if(i<3){
								xing += '<img onclick="xuanze('+(i+1)+')" id="img_'+(i+1)+'" name="img_xing" src="../appcssjs/images/public/start2.png">';
							}else{
								xing += '<img onclick="xuanze('+(i+1)+')" id="img_'+(i+1)+'" name="img_xing" src="../appcssjs/images/public/start3.png">';
							}
						}
						$('#star_level').html(dengji(3));
						$('#sumstarnum').val(3);
					}else{
						$('#examine').hide();
						$('.fbtn_none').show();
						$('.foot_btn').show();
					}
				}else{
					for(var i=0;i<5;i++){
						if(i<parseInt(jomap.commentlevel)){
							xing += '<img src="../appcssjs/images/public/start2.png">';
						}else{
							xing += '<img src="../appcssjs/images/public/start3.png">';
						}
					}
					$('#star_level').html(dengji(jomap.commentlevel));
					$('#sumstarnum').val(jomap.commentlevel);
					$('.fbtn_none').show();
					$('.foot_btn').show();
				}
				$('#opinion').html(xing);
				
				var filelist = jomap.filelist;
				if(filelist != undefined && filelist != null){
					showimgs(filelist);
				}
				var rangelist = jomap.rangelist;
				if(rangelist != undefined && rangelist != null){
					showrange(rangelist);
				}
			}
		}
	});
}

function dengji(level){
	level = parseInt(level);
	if(level == 1){
		return "很差";
	}else if(level == 2){
		return "不差";
	}else if(level == 3){
		return "一般";
	}else if(level == 4){
		return "&nbsp;&nbsp;&nbsp;好";
	}else if(level == 5){
		return "很好";
	}
}

function sendMessage(){
	var param = new Object();
	param.commentlevel = $('#sumstarnum').val();
	param.updateid = userid;
	param.logid = pid;
	param.jotype = jotype;
	$.ajax({
		url:projectpath+"/app/updateJournalInfo",
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
				changeIsread(examine_createid,pid);
				var rizhi = "";
				if(jotype == '1'){
					rizhi = "日报";
				}else if(jotype == '2'){
					rizhi = "周报";
				}else if(jotype == '3'){
					rizhi = "月报";
				}
				var title=$("#commentrealname").text()+"审批了您提交的"+rizhi+",请及时查看";
				var url="/oa/log_detail.html?pid="+pid+"&userid="+examine_createid+"&jotype="+jotype;
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

//选择项目星级
function xuanze(i){
	$('img[name="img_xing"]').attr("src","../appcssjs/images/public/start3.png");
	for(var j=1;j<=i;j++){
		$('#img_'+j).attr("src","../appcssjs/images/public/start2.png");
	}
	$('#sumstarnum').val(i);
	$('#star_level').html(dengji(i));
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
		userForword(userInfo.companyid,pid,$('#examineuserid').val(),userInfo.userid,resoucetype);
		location.reload(false);
	});
}


//查询评论信息
function queryComment(){
	array.resourcetype = resoucetype;
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
	if(examine_commentid == userid){
		sendMessage();
	}
	var param = new Object();
	param.content = $('#content').val();
	param.userid = userid;
	param.resourceid = pid;
	param.resourcetype = resoucetype;
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