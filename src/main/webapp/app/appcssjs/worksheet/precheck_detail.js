
var inspectid = appBase.getQueryString("inspectid");
var userid = appBase.getQueryString("userid");
var forwarduserid = appBase.getQueryString("forwarduserid");

var userInfo;
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data
});

var shenhe_examineid = "";
var shenhe_examinestatus = "";
var obj = new Object();
$(function(){
	var resourceType = appBase.getQueryString("type");
	//修改未读状态
	intoDetailUpdateRead(userid, inspectid, resourceType);
	
	beformealDetail();
	
	var param = new Object();
	param.resourcetype = 11;
	param.orderid = inspectid;
	//查询评论信息
	requestPost("/app/getOrderComment",param,"",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			var commentlist = resultMap.commentlist;
			var commentnum = resultMap.num;
			$('#comnum').text(commentnum);
			showComment(commentlist);
		}
	});
	getSendName(forwarduserid,"#sendname");
	//评论加载更多
	PageHelper({
		url:projectpath+"/app/getOrderComment",
		data:param,
		success:function(resultMap){
			if(resultMap != undefined && resultMap != null){
				var commentlist = resultMap.commentlist;
				showComment(commentlist);
			}
		}
	}); 
	
	//样式切换
	$('.foot_btn').delegate('span[name="foot_span"]',"click",function(){
		$('span[name="foot_span"]').attr("class","");
		$(this).attr("class","active");
	});
});


var examine_createid = "";
function beformealDetail(){
	obj.inspectid = inspectid;
	obj.userid = userid;
	requestPost('/app/getBeforeMealInfo',obj,'detail_'+inspectid,true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			var inspectinfo = resultMap.inspectinfo;
			var starlist = resultMap.inspectinfo.starlist;
			//显示审核项目
			if(starlist.length > 0 && starlist != null){
				var htm = "";
				var sumstar = 0;
				var addnum = 0;
				$.each(starlist,function(i,map){
					var status = map.status;
					if(status == 1 || status == "1"){
						htm += '<li>'+
									'<span style="font-weight:bold;">'+map.projectname+'</span>'+
									'<b></b>'+
								'</li>';
					}else{
						var xing = "";
						var starlevel = map.starlevel;
						for(var g=0;g<5;g++){
							if(g<starlevel){
								xing += '<em><img src="../appcssjs/images/public/start2.png"></em>';
							}else{
								xing += '<em><img src="../appcssjs/images/public/start3.png"></em>';
							}
						}
						htm += '<li>'+
									'<span>'+map.projectname+'</span>'+
									'<b>'+
										xing+
										'<font style="font-weight:normal; font-size:12px;">&nbsp;&nbsp;'+dengji(starlevel)+'</font>'+
									'</b>'+
								'</li>';
						sumstar += starlevel;
						addnum++;
					}
				});
				$('#sumstar').text(sumstar);
				$('#evaluate_list').append(htm);
				$('#showStarLevel').html(showStarLevel(sumstar,addnum));
			}
			//显示审核的基本信息
			if(inspectinfo != null){
				$('#createname').text(inspectinfo.createname);
				$('#createtime').text(appBase.parseDateMinute(inspectinfo.createtime));
				$('#cs_examinename').text(inspectinfo.examinename);
				if(inspectinfo.updatetime == undefined){
					$('#examinetime').parent().hide();
				}
				$('#examinetime').text(appBase.parseDateMinute(inspectinfo.updatetime));
				$('#opinion_textarea').text(inspectinfo.opinion==undefined?"":inspectinfo.opinion);
				$('#opinion_div').text(inspectinfo.opinion==undefined?"":inspectinfo.opinion);
				$('#organizename').text(inspectinfo.organizename);
				$('#templatename').text(inspectinfo.templatename);
				
				$('#createname').attr("onclick","location.href='../member/homepage_info.html?userid="+inspectinfo.createid+"'");
				$('#cs_examinename').attr("onclick","location.href='../member/homepage_info.html?userid="+inspectinfo.examineuserid+"'");
				
				examine_createid = inspectinfo.createid;
				shenhe_examineid = inspectinfo.examineuserid;
				shenhe_examinestatus = inspectinfo.status;
				
				$('#CCusernames').text(inspectinfo.CCusernames);
				if(inspectinfo.examineuserid == userid && inspectinfo.status == 0){
					$('#sendmessage').show();
					$('#opinion_div').hide();
					$('#opinion_textarea').show();
				}else{
					$('.fbtn_none').show();
					$('.foot_btn').show();
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

function showStarLevel(sumstar,addnum){
	var intnum = parseInt(sumstar/addnum);
	var floatnum = (sumstar%addnum)/addnum;
	/*var shownum = intnum;//显示的等级
	if(floatnum > 0){
		shownum++;
	}
	var html = "";
	for(var i=0 ; i<5 ; i++){
		if(i<intnum){
			html += '<em><img src="../appcssjs/images/public/start2.png"></em>';
		}else if(floatnum <= 0.2 && floatnum != 0 && i == intnum){
			html += '<em><img src="../appcssjs/images/public/start_02.png"></em>';
		}else if(floatnum <= 0.4 && floatnum != 0 && i == intnum){
			html += '<em><img src="../appcssjs/images/public/start_04.png"></em>';
		}else if(floatnum <= 0.6 && floatnum != 0 && i == intnum){
			html += '<em><img src="../appcssjs/images/public/start_06.png"></em>';
		}else if(floatnum <= 0.8 && floatnum != 0 && i == intnum){
			html += '<em><img src="../appcssjs/images/public/start_08.png"></em>';
		}else if(floatnum > 0.8 && floatnum != 0 && i == intnum){
			html += '<em><img src="../appcssjs/images/public/start2.png"></em>';
		}else{
			html += '<em><img src="../appcssjs/images/public/start3.png"></em>';
		}
	}*/
	$('#star_level').html((intnum+floatnum).toFixed(1)+"分");
	return "综合评价：";
}


//提交抄送意见
function sendBeforeMeal(chun){
	if(shenhe_examineid == userid && shenhe_examinestatus == 0){
		var param = new Object();
		param.opinion = $('#opinion_textarea').val();
		param.inspectid = inspectid;
		param.userid = userid;
		$.ajax({
			url:projectpath+"/app/examineBeforeMeal",
			type:"post",
			beforeSend:function(a,b){
				ajaxbefore();
			},
			complete:function(a,b){
				ajaxcomplete();
			},
			data:param,
			success:function(resultMap){
				if(resultMap.status == 0){
					changeIsread(examine_createid,inspectid);
					if(chun == 'chun'){
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

function logical(){
	sendBeforeMeal()
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
		userForword(userInfo.companyid,inspectid,$('#examineuserid').val(),userInfo.userid,11);
		location.reload(false);
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
	sendBeforeMeal();
	var param = new Object();
	param.content = $('#content').val();
	param.userid = userid;
	param.resourceid = inspectid;
	param.resourcetype = 11;
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