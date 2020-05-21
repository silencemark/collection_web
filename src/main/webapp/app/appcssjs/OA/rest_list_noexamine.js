var fangwen_url = "/app/getLeaveList";
var userid = "";
var obj = new Object();
var readparam = new Object();

var nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>还没有请假单，赶紧添加一条吧~</span><b><img src="'+notDataImageUrl_havepower+'"></b></div>';
$(function(){
	
	
	//初始化信息
	JianKangCache.getGlobalData('userinfo',function(data){
		
		if(typeof(data.powerMap.power5002110) != "undefined"){
			$('#linkadd').show();
		}
		
		obj.userid = data.userid;
		obj.companyid = data.companyid;
		
		readparam.userid = data.userid;
		readparam.companyid = data.companyid;
		
		obj.status = 0;
		userid = data.userid;
		//
	});
	
	//加载更多
	PageHelper({
		url:projectpath+fangwen_url,
		data:obj,
		success:function(resultMap){
			if(resultMap != undefined && resultMap != null){
				var list = resultMap.leavelist;
				if(list != null && list.length > 0){
					showData(list);
				}
			}
		}
	}); 
});

function initPage(){
	getNotIsRead(readparam);
	queryNoExamineInfo();
}

function getNotIsRead(param){
	$.ajax({
		url:projectpath+"/app/getLeaveAllNotRead",
		type:"post",
		data:param,
		success:function(data){
			if(data.status == 0 || data.status == '0'){
				var noexamiennum = data.noexaminenum;
				var examinenum = data.examinenum;
				if(noexamiennum > 0){
					$('.span_nav span:first').prepend(redPoint);
				}
				if(examinenum > 0){
					$('.span_nav span:last').prepend(redPoint);
				}
			}
		}
	});
}

//查询信息
function queryNoExamineInfo(){
	requestPost(fangwen_url,obj,"noExamine",false,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			var list = resultMap.leavelist;
			if(list != null && list.length > 0){
				$(".list_none").remove();
				showData(list);
			}else{
				$(".list_none").remove();
				$('body').append(nodata);
			}
		}
	});
}

//信息展示
function showData(list){
	if(list.length > 0 && list != null){
		var htm = '';
		$.each(list,function(i,map){
			var isread = '';
			if(map.isread == 0 || map.isread == '0'){
				isread = 'class="new"';
			}
			var onclick_url = 'rest_detail.html?leaveid='+map.leaveid+"&userid="+userid+'&forwarduserid='+map.forwarduserid;
			if(map.examineuserid == userid){
				onclick_url = 'rest_check.html?leaveid='+map.leaveid+'&userid='+userid+'&forwarduserid='+map.forwarduserid;
			}
			htm += '<li onclick="intoDetail(\''+onclick_url+'\',\''+map.leaveid+'\','+map.isread+',18)">'+
			        	'<div class="xx_01"><span '+isread+'>'+map.createname+'的请假单</span><em class="red">待处理</em></div>'+
			            '<div class="txt">'+map.reason+'</div>'+
			            '<div class="xx_02"><span class="qj">'+map.leavetypename+'</span><span><em>'+map.daynum+'</em>天<em>'+map.hournum+'</em>时</span></span><i>'+map.createtime+'</i></div>'+
			        '</li>';
		});
		//显示
		$('#noexaminelist').append(htm);
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