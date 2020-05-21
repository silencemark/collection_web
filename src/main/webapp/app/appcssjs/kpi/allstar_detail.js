
var overallvaluateid = appBase.getQueryString("overallvaluateid");
var userid = appBase.getQueryString("userid");
var forwarduserid = appBase.getQueryString("forwarduserid");
var userInfo = "";
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data
});
$(function(){
	var resourceType = appBase.getQueryString("type");
	//修改未读状态
	intoDetailUpdateRead(userid, overallvaluateid, resourceType);
	
	var data = new Object();
	data.overallvaluateid = overallvaluateid;
	data.userid = userid;
	requestPost('/app/getOverallvaluateInfo',data,'allstar_detail',true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			var evaluateInfo = resultMap.evaluateInfo;
			var evaluatelist = resultMap.evaluatelist;
			showDetail(evaluateInfo);
			showEvaluate(evaluatelist);
		}
	});
	
	//样式切换
	$('.foot_btn').delegate('span[name="foot_span"]',"click",function(){
		$('span[name="foot_span"]').attr("class","");
		$(this).attr("class","active");
	});
	
	getSendName(forwarduserid,"#sendname");
	
	//查询评论
	queryComment();
	
	var array = new Object();
	array.orderid = overallvaluateid;
	array.resourcetype = 7;
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

function dengji(level){
	level = parseInt(level);
	/*if(level == 1){
		return "很差";
	}else if(level == 2){
		return "不差";
	}else if(level == 3){
		return "一般";
	}else if(level == 4){
		return "&nbsp;&nbsp;&nbsp;好";
	}else if(level == 5){
		return "很好";
	}*/
	if(level == 1){
		return "一星";
	}else if(level == 2){
		return "二星";
	}else if(level == 3){
		return "三星";
	}else if(level == 4){
		return "四星";
	}else if(level == 5){
		return "五星";
	}
}

function logical(){
//	swal({   
//		title: "An input!",   
//		text: "Write something interesting:",   
//		type: "input",   
//		showCancelButton: true,   
//		closeOnConfirm: false,   
//		animation: "slide-from-top",   
//		inputPlaceholder: "转发评论" 
//	}, function(inputValue){   
//		if (inputValue === false) return false;
//		if (inputValue === "") {     
//			swal.showInputError("You need to write something!");     
//			return false   
//		}      
//		swal("Nice!", "You wrote: " + inputValue, "success"); 
//	});
	swal({
		title : "",
		text : "确认转发给"+$('#examinename').text()+"？",
		type : "success",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : false
	}, function(){
		sendComment(2,$('#examinename').text(),$('#examineuserid').val());
		userForword(userInfo.companyid,overallvaluateid,$('#examineuserid').val(),userInfo.userid,7);
		location.reload(false);
	});
}

//查询评论信息
function queryComment(){
	var array = new Object();
	array.orderid = overallvaluateid;
	array.resourcetype = 7;
	requestPost('/app/getOrderComment',array,'allstar_detail_comment',true,function(resultMap){
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
	param.resourceid = overallvaluateid;
	param.resourcetype = 7;
	param.type = type;
	param.forwardusernames = forwardusernames;
	param.forwarduserids = forwarduserids;
	
	addcommentAjax(param);
}

//显示基本信息
function showDetail(evaluateInfo){
	if(evaluateInfo != null && evaluateInfo != ''){
		$('#organizename').text(evaluateInfo.organizename);
		$('#createname').text(evaluateInfo.createname);
		$('#createtime').text(appBase.parseDateMinute(evaluateInfo.createtime));
		$('#shnpi_examinename').text(evaluateInfo.examinename);
		$('#opinion').text(evaluateInfo.opinion==undefined?"":evaluateInfo.opinion);
		if(evaluateInfo.updatetime == undefined){
			$('#exminetime').parent().hide();
		}
		$('#exminetime').text(appBase.parseDateMinute(evaluateInfo.updatetime)==null?"":appBase.parseDateMinute(evaluateInfo.updatetime));
		$('#sumstar').text(evaluateInfo.sumstar);
		$('#gangwei').text(evaluateInfo.templatename);
		$('#createname').attr("onclick","location.href='../member/homepage_info.html?userid="+evaluateInfo.createid+"'");
		$('#shnpi_examinename').attr("onclick","location.href='../member/homepage_info.html?userid="+evaluateInfo.examineuserid+"'");
		
		$('#CCusernames').text(evaluateInfo.CCusernames);
		if(evaluateInfo.status == '1' || evaluateInfo.status == 1){
			if(evaluateInfo.result == 1 || evaluateInfo.result == '1'){
				$('#agree').show();
			}else if(evaluateInfo.result == 2 || evaluateInfo.result == '2'){
				$('#agree').show();
				$('#agree').find('img').attr("src","../appcssjs/images/public/agree_img2.png");
			}
		}
	}
}

//显示项目信息
function showEvaluate(list){
	if(list.length > 0 && list != null){
		$.each(list,function(i,map){
			
			var xing = "";
			var starlevel = map.starlevel;
			var dj = dengji(starlevel);
			for(var i=0;i<5;i++){
				if(i<starlevel){
					xing += '<em><img src="../appcssjs/images/public/start2.png"></em>'
				}else{
					xing += '<em><img src="../appcssjs/images/public/start3.png"></em>'
				}
			}
			var htl ='<li>'+
			        	'<span>'+map.projectname+'</span>'+
			            '<b>'+
			            	xing+'<font style="font-weight:normal; font-size:12px;">&nbsp;&nbsp;'+dj+'</font>'+
			            '</b>'+
			            '<div class="clear"></div>'+
			            '<p>'+(map.description==undefined?"":map.description)+'</p>'+
			        '</li>';
			$('#evaluate_list').append(htl);
		})
	}
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
			JianKangCache.setData("kpi_",resultMap);
			callback(resultMap);
		},error:function(e){
			//取出缓存数据
			JianKangCache.getData("kpi_",function(resultMap){
				callback(resultMap);
			});
		},complete:function(){
			$('#jiazaizhong').hide();
		}
	});
}