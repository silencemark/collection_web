
var expenseid = appBase.getQueryString("expenseid");
var userid = appBase.getQueryString("userid");
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

var examine_createid = "";
function sendExamine(result){
	var param = new Object();
	param.expenseid = expenseid;
	param.userid = userid;
	param.opinion = $('#opinion').val();
	param.result = result;
	
	$.ajax({
		url:projectpath+"/app/examineExpense",
		type:"post",
		beforeSend:function(a,b){
			ajaxbefore();
		},
		complete:function(a,b){
			ajaxcomplete();
		},
		data:param,
		success:function(resultMap){
			if(resultMap.status == '0' || resultMap.status == 0){
				changeIsread(examine_createid,expenseid);
				var title=$("#sp_examinename").text()+"审批了您的报销单,请及时查看";
				var url="/oa/expenseaccount_detail.html?expenseid="+expenseid+"&userid="+examine_createid;
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

//显示奖励单的基本信息
function showbasicInfo(map){
	if(map != null && map != undefined && map != ""){
		$('#expenseno').text(map.expenseno);
		$('#totalprice').text(map.detailprice);
		$('#projectnum').text(map.detailnum);
		
		$('#createname').text(map.createname);
		$('#createtime').text(appBase.parseDateMinute(map.createtime));
		$('#sp_examinename').text(map.examinename);
		$('#examinetime').text(map.updatetime);
		$('#opinion').text(map.opinion);
		
		$('#createname').attr("onclick","location.href='../member/homepage_info.html?userid="+map.createid+"'");
		$('#sp_examinename').attr("onclick","location.href='../member/homepage_info.html?userid="+map.examineuserid+"'");
		
		$('#CCusernames').text(map.CCusernames);
		examine_createid = map.createid;
		var expensedetaillist = map.expensedetaillist;
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
	var param = new Object();
	param.content = $('#content').val();
	param.userid = userid;
	param.resourceid = expenseid;
	param.resourcetype = 17;
	
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