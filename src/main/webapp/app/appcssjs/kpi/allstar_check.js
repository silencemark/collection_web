
var overallvaluateid = appBase.getQueryString("overallvaluateid");
var userid = appBase.getQueryString("userid");
var userInfo;
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
	queryComment();
	//样式切换
	$('.foot_btn').delegate('span[name="foot_span"]',"click",function(){
		$('span[name="foot_span"]').attr("class","");
		$(this).attr("class","active");
	});
	
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
		userForword(userInfo.companyid,overallvaluateid,$('#examineuserid').val(),userInfo.userid,7);
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
var examine_createtid = "";
//审核同意或者拒绝
function yongyi_jujue(result){
	var obj = new Object();
	
	var starlist = {"starlist":[]};
	var input = $('input[name="input_star"]');
	$.each(input,function(i,item){
		var star = new Object();
		star.starid = $(item).attr("starid");
		star.overallvaluateid = overallvaluateid;
		star.starlevel = $(item).attr("sumNum");
		star.description = $('#'+star.starid+'_textarea').val();
		starlist.starlist[i] = star;
	});
	
	obj.overallvaluateid = overallvaluateid;
	obj.userid = userid;
	obj.result = result;
	obj.opinion = $('#opinion').val();
	obj.sumstar = $('#sumstar').text();
	obj.starlist = JSON.stringify(starlist);
	
	if(obj.opinion == ""){
		swal({
			title : "",
			text : "审批意见不能为空",
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
		url:projectpath+"/app/examineOverallvaluate",
		type:"post",
		data:obj,
		beforeSend:function(a,b){
			ajaxbefore();
		},
		complete:function(a,b){
			ajaxcomplete();
		},
		success:function(resultMap){
			if(resultMap.status == '0' || resultMap.status == 0){
				changeIsread(examine_createtid,overallvaluateid);
				var title=$('#shenpi_examinename').text()+"审批了您的综合星值自评,请及时查看";
				var url="/kpi/allstar_detail.html?overallvaluateid="+overallvaluateid+"&userid="+examine_createtid;
				pushMessage(examine_createtid,title,url);
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

//添加评论信息
function addComment(){
	var param = new Object();
	param.content = $('#content').val();
	param.userid = userid;
	param.resourceid = overallvaluateid;
	param.resourcetype = 7;
	
	addcommentAjax(param);
}

//显示基本信息
function showDetail(evaluateInfo){
	if(evaluateInfo != null && evaluateInfo != ''){
		$('#organizename').text(evaluateInfo.organizename);
		$('#createname').text(evaluateInfo.createname);
		$('#createtime').text(appBase.parseDateMinute(evaluateInfo.createtime));
		$('#shenpi_examinename').text(evaluateInfo.examinename);
		$('#opinion').text(evaluateInfo.opinion==undefined?"":evaluateInfo.opinion);
		$('#exminetime').text(appBase.parseDateMinute(evaluateInfo.updatetime)==null?"":appBase.parseDateMinute(evaluateInfo.updatetime));
		$('#sumstar').text(evaluateInfo.sumstar);
		$('#gangwei').text(evaluateInfo.templatename);
		$('#ccusernames').text(evaluateInfo.CCusernames);
		examine_createtid = evaluateInfo.createid;
		$('#createname').attr("onclick","location.href='../member/homepage_info.html?userid="+evaluateInfo.createid+"'");
		$('#shenpi_examinename').attr("onclick","location.href='../member/homepage_info.html?userid="+evaluateInfo.examineuserid+"'");
	}
}

//显示项目信息
function showEvaluate(list){
	if(list.length > 0 && list != null){
		$.each(list,function(i,map){
			
			var xing = "";
			var starlevel = map.starlevel;
			for(var i=1;i<=5;i++){
				if(i<=starlevel){
					xing += '<em onclick="xuanze(\''+map.starid+'\',\''+i+'\')"><img id="'+map.starid+'_'+i+'" name="img_'+map.starid+'" src="../appcssjs/images/public/start2.png"></em>';
				}else{
					xing += '<em onclick="xuanze(\''+map.starid+'\',\''+i+'\')"><img id="'+map.starid+'_'+i+'" name="img_'+map.starid+'" src="../appcssjs/images/public/start3.png"></em>';
				}
			}
			var htl ='<li class="n_bor"><i></i>'+
			        	'<span>'+map.projectname+'</span>'+
			            '<b>'+
			            	'<input type="hidden" name="input_star" id="'+map.starid+'_input" starid="'+map.starid+'" sumNum="'+starlevel+'"/>'+
			            	xing+
			            	'<font style="font-weight:normal; font-size:12px;" id="'+map.starid+'_starlevel">&nbsp;&nbsp;'+dengji(starlevel)+'</font>'+
			            '</b>'+
				        '<div class="text"><textarea placeholder="请输入详细内容，不超过200字" id="'+map.starid+'_textarea">'+map.description+'</textarea></div>'+
			        '</li>';
			$('#evaluate_list').append(htl);
		})
	}
}

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

//选择项目星级```````````````````````````````````````````````````````````
function xuanze(i,k){
	$('img[name="img_'+i+'"]').attr("src","../appcssjs/images/public/start3.png");
	for(var j=1;j<=k;j++){
		$('#'+i+"_"+j).attr("src","../appcssjs/images/public/start2.png");
	}
	$('#'+i+'_input').attr("sumNum",k);
	$('#'+i+'_starlevel').html("&nbsp;&nbsp;"+dengji(k));
	
	var input = $('input[name="input_star"]');
	var sumstar = "0";
	$.each(input,function(k,item){
		sumstar = parseInt($(item).attr("sumNum")) + parseInt(sumstar);
	});
	$('#sumstar').text(sumstar);
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