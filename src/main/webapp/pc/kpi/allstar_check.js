
var overallvaluateid = appBase.getQueryString("overallvaluateid");
var userid = appBase.getQueryString("userid");
var userInfo;
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data
});
$(function(){
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
	$('.foot_btn2').delegate('span[name="foot_span"]',"click",function(){
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
//审核同意或者拒绝
function yongyi_jujue(result){
	var obj = new Object();
	obj.overallvaluateid = overallvaluateid;
	obj.userid = userid;
	obj.result = result;
	obj.opinion = $('#opinion').val();
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
		success:function(resultMap){
			if(resultMap.status == '0' || resultMap.status == 0){
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
					goBackPage();
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
		$('#gangwei').text(evaluateInfo.identityname)
	}
}

//显示项目信息
function showEvaluate(list){
	if(list.length > 0 && list != null){
		$.each(list,function(i,map){
			
			var xing = "";
			var starlevel = map.starlevel;
			for(var i=0;i<5;i++){
				if(i<starlevel){
					xing += '<em><img src="../appcssjs/images/public/start2.png"></em>'
				}else{
					xing += '<em><img src="../appcssjs/images/public/start3.png"></em>'
				}
			}
			var htl ='<li>'+
			        	'<span>'+map.projectname+'</span>'+
			            '<b>'+xing+'</b>'+
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