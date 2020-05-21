
var evaluateid = "";
var userid = "";

var relayiduserid = '';
var relayidcompanyid ='';
var relayid = '';
var relaytype = 6;

$(function(){
	
	evaluateid = $('#evaluateid').val();
	userid = $('#userid').val();
	
	relayiduserid = $('#userid').val();
	relayidcompanyid = $('#companyid').val();
	relayid = $('#evaluateid').val();
	
	var obj = new Object();
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
										'<div class="star_box"></div>'+
									'</td>'+
								'</tr>';
					}else{
						var xing = "";
						var starlevel = map.starlevel;
						for(var g=0;g<5;g++){
							if(g<starlevel){
								xing += '<img src="/userbackstage/images/pc_page/start2.png"/>';
							}else{
								xing += '<img src="/userbackstage/images/pc_page/start3.png"/>';
							}
						}
						htm += '<tr>'+
									'<td>'+((map.projectname)==undefined?"":map.projectname)+'</td>'+
									'<td class="t_r">'+
										'<div class="star_box">'+
											xing+
											'<a style="margin-left:20px;">'+dengji(starlevel)+'</a>'+
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
				$('#gangwei').text(evaluateInfo.templatename);
				
				if(evaluateInfo.status == 1 || evaluateInfo.status == '1'){
					if(evaluateInfo.result == '1' || evaluateInfo.result == '1'){
						$('#agree').show();
					}else if(evaluateInfo.result == '2' || evaluateInfo.result == '2'){
						$('#agree').show();
						$('#agree').find('img').attr("src",projectpath+"/userbackstage/images/pc_page/agree_img2.png");
					}
					
				}
				
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
		userForword(userInfo.companyid,evaluateid,$('#examineuserid').val(),userInfo.userid,6);
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

