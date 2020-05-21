
var overallvaluateid = appBase.getQueryString("overallvaluateid");
var userid = appBase.getQueryString("userid");
var userInfo = "";
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
	
	//样式切换
	$('.foot_btn').delegate('span[name="foot_span"]',"click",function(){
		$('span[name="foot_span"]').attr("class","");
		$(this).attr("class","active");
	});
	
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
		$('#shnpi_examinename').text(evaluateInfo.examinename);
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
