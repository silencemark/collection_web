
var evaluateid = "";
var userid = "";

var relayiduserid = '';
var relayidcompanyid ='';
var relayid = '';
var relaytype = 6;

var obj = new Object();


var examine_createid = "";
$(function(){
	
	evaluateid = $('#evaluateid').val();
	userid = $('#userid').val();
	
	relayiduserid = $('#userid').val();
	relayidcompanyid = $('#companyid').val();
	relayid = $('#evaluateid').val();
	
	obj.evaluateid = evaluateid;
	obj.userid = userid;
	requestPost('/pc/getEvaluateInfo',obj,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			var evaluateInfo = resultMap.evaluateInfo;
			var evaluatelist = resultMap.evaluatelist;
			//显示审核项目
			if(evaluatelist != null && evaluatelist.length > 0 ){
				var htm = "";
				$.each(evaluatelist,function(i,map){
					var status = map.status;
					if(status == 1 || status == "1"){
						htm += '<tr>'+
									'<td style="font-weight:bold;font-size:15px;">'+((map.projectname)==undefined?"":map.projectname)+'</td>'+
									'<td class="t_r">'+
										'<div class="star_box"><input type="hidden" name="input_star" id="'+map.starid+'_input" starid="'+map.starid+'" sumNum="0"/></div>'+
									'</td>'+
								'</tr>';
					}else{
						var xing = "";
						var starlevel = map.starlevel;
						for(var g=1;g<=5;g++){
							if(g<=starlevel){
								xing += '<a class="a_star" onclick="xuanze(\''+map.starid+'\',\''+g+'\')"><img id="'+map.starid+'_'+g+'" name="img_'+map.starid+'" src="/userbackstage/images/pc_page/start2.png"/></a>';
							}else{
								xing += '<a class="a_star" onclick="xuanze(\''+map.starid+'\',\''+g+'\')"><img id="'+map.starid+'_'+g+'" name="img_'+map.starid+'" src="/userbackstage/images/pc_page/start3.png"/></a>';
							}
						}
						htm += '<tr>'+
									'<td>'+((map.projectname)==undefined?"":map.projectname)+'</td>'+
									'<td class="t_r">'+
										'<div class="star_box">'+
											'<input type="hidden" name="input_star" id="'+map.starid+'_input" starid="'+map.starid+'" sumNum="'+starlevel+'"/>'+
											xing+
											'<a style="margin-left:20px;" id="'+i+'_starlevel">'+dengji(starlevel)+'</a>'+
										'</div>'+
									'</td>'+
								'</tr>';
					}
				});
				$('#evaluate_list').append(htm);
			}
			//显示审核的基本信息
			if(evaluateInfo != null){
				$('#shenqing_name').text(evaluateInfo.createname);
				$('#shenqing_time').text(appBase.parseDateMinute(evaluateInfo.createtime));
				$('#shenpi_name').text(evaluateInfo.examinename);
				$('#shenpi_time').text(appBase.parseDateMinute(evaluateInfo.updatetime)==null?"":appBase.parseDateMinute(evaluateInfo.updatetime));
				$('#yijian').text(evaluateInfo.opinion==undefined?"":evaluateInfo.opinion);
				$('#organizename').text(evaluateInfo.organizename);
				$('#sumstar').text(evaluateInfo.sumstar);
				$('#gangwei').text(evaluateInfo.templatename)
				examine_createid = evaluateInfo.createid;
				
				showCCuserInfo(evaluateInfo.ccuserlist);
			}
		}
	});
	
	//加载评论信息
	onloadDate();
});

/**
 * 显示抄送人
 */
function showCCuserInfo(ccuserlist){
	if(ccuserlist != null && ccuserlist != "" && ccuserlist.length > 0){
		var htm = "";
		$.each(ccuserlist,function(i,map){
			htm += "<div class=\"img f\"><img src=\""+projectpath+map.headimage+"\" width=\"30\" height=\"30\" /></div>"+
				   "<i class=\"i_name\" >"+map.realname+"</i>";
		});
		$('#CCusernames').html(htm);
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

//选择项目星级
function xuanze(i,k){
	$('img[name="img_'+i+'"]').attr("src","/userbackstage/images/pc_page/start3.png");
	for(var j=1;j<=k;j++){
		$('#'+i+"_"+j).attr("src","/userbackstage/images/pc_page/start2.png");
	}
	$('#'+i+'_input').attr("sumNum",k);
	$('#'+i+'_starlevel').html(dengji(k));
	
	var input = $('input[name="input_star"]');
	var sumstar = "0";
	$.each(input,function(k,item){
		var bn = $(item).attr("sumNum");
		sumstar = parseInt(bn) + parseInt(sumstar);
	});
	$('#sumstar').text(sumstar);
}

function tongyi(num){
	var obj = new Object();
	
	var starlist = {"starlist":[]};
	var input = $('input[name="input_star"]');
	$.each(input,function(i,item){
		var star = new Object();
		star.starid = $(item).attr("starid");
		star.evaluateid = evaluateid;
		star.starlevel = $(item).attr("sumNum");
		starlist.starlist[i] = star;
	});
	
	obj.opinion = $('#adivce').val();
	obj.result = num;
	obj.evaluateid = evaluateid;
	obj.starlist = JSON.stringify(starlist);
	obj.sumstar = $('#sumstar').text();
	
	if(obj.opinion == ""){
		swal({
			title : "",
			text : "审批意见不能为空",
			type : "error",
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
		url:projectpath+"/pc/examineEvaluate",
		type:"post",
		data:obj,
		success:function(resultMap){
			if(resultMap.status == '0' || resultMap.status == 0){
				changeIsread(examine_createid,evaluateid);
				var title=$('#shenpi_name').text()+"审批了您的岗位自评,请及时查看";
				var url="/kpi/jobstar_detail.html?evaluateid="+evaluateid+"&userid="+examine_createid;
				pushMessage(examine_createid,title,url);
				swal({
					title : "",
					text : "操作成功",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					window.history.go(-1);
				});
			}
		}
	});
}

//评论分页
function pageHelper(num){
	var param =new  Object();
	var currentPage = num;
	param.currentPage=currentPage;
	param.orderid=evaluateid;
	param.resourcetype= 6;
	pageList(param);
}

//评论列表
function onloadDate(){
	var param =new  Object();
	param.orderid=evaluateid;
	param.resourcetype=6;
	addcommentAjax(param);//显示评论列表		
}

//新增评论
function checkComment(row){
	var param = new Object();
	param.userid = userid;
	param.resourceid = evaluateid;
	param.resourcetype =6;
	showComment(row,param,'');//添加评论信息
}

