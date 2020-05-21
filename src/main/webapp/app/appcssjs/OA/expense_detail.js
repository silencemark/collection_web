
var expenseid = appBase.getQueryString("expenseid");
var userid = appBase.getQueryString("userid");
var forwarduserid = appBase.getQueryString("forwarduserid");
var userInfo = "";
JianKangCache.getGlobalData('userinfo',function(data){
	userInfo = data
});

var array = new Object();
array.orderid = expenseid;
array.resourcetype = 17;
$(function(){
	var resourceType = appBase.getQueryString("type");
	//修改未读状态
	intoDetailUpdateRead(userid, expenseid, resourceType);
	
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


function querydetail(){
	var obj = new Object();
	obj.expenseid = expenseid;
	requestPost('/app/getExpenseInfo',obj,"detail",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == '0' || resultMap.status == 0){
				var expenseinfo = resultMap.expenseinfo;
				
				showbasicInfo(expenseinfo);
			}
		}
	})
}

//显示奖励单的基本信息
function showbasicInfo(map){
	if(map != null && map != undefined && map != ""){
		$('#expenseno').text(map.expenseno);
		$('#totalprice').text(map.detailprice);
		$('#projectnum').text(map.detailnum);
		
		$('#createname').text(map.createname);
		$('#createtime').text(appBase.parseDateMinute(map.createtime));
		$('#sp_examinename').text(map.examinename);
		if(map.updatetime == undefined){
			$('#examinetime').parent().hide();
		}
		$('#examinetime').text(appBase.parseDateMinute(map.updatetime));
		$('#opinion').text(map.opinion);
		$('#createname').attr("onclick","location.href='../member/homepage_info.html?userid="+map.createid+"'");
		$('#sp_examinename').attr("onclick","location.href='../member/homepage_info.html?userid="+map.examineuserid+"'");
		var expensedetaillist = map.expensedetaillist;
		if(map.status == 1 || map.status == '1'){
			if(map.result == 1 || map.result == '1'){
				$('#agree').show();
			}else if(map.result == 2 || map.result == '2'){
				$('#agree').show();
				$('#agree').find('img').attr('src',"../appcssjs/images/public/agree_img2.png");
			}
		}
		
		$('#CCusernames').text(map.CCusernames);
		if(expensedetaillist != undefined && expensedetaillist != null){
			showDetail(expensedetaillist);
		}
		
		//查询评论信息
		queryComment();
	}
}

function showDetail(list){
	if(list != null && list.length > 0){
		var htm = "";
		$.each(list,function(i,map){
			htm += '<tr>'+
		            	'<td class="first">'+map.type+'</td>'+
		                '<td>'+map.price+'</td>'+
		                '<td class="last">'+map.detail+'</td>'+
		            '</tr>';
		});
		$('#baoxiaomingxi').append(htm);
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
		userForword(userInfo.companyid,expenseid,$('#examineuserid').val(),userInfo.userid,17);
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
	var param = new Object();
	param.content = $('#content').val();
	param.userid = userid;
	param.resourceid = expenseid;
	param.resourcetype = 17;
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