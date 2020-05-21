var fangwen_url = "/app/getTaskList";
var userid = "";
var obj = new Object();
var readparam = new Object();

var nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>还没有任务单，赶紧添加一条吧~</span><b><img src="'+notDataImageUrl_havepower+'"></b></div>';
$(function(){
	
	
	//初始化信息
	JianKangCache.getGlobalData('userinfo',function(data){
		
		if(typeof(data.powerMap.power5001510) != "undefined"){
			$('#linkadd').show();
		}
		
		obj.userid = data.userid;
		obj.companyid = data.companyid;
		
		readparam.userid = data.userid;
		readparam.companyid = data.companyid;
		
		obj.nooverdue = 1;
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
				var list = resultMap.tasklist;
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
		url:projectpath+"/app/getTaskAllNotRead",
		type:"post",
		data:param,
		success:function(data){
			if(data.status == 0 || data.status == '0'){
				var noexamiennum = data.noexaminenum;
				var examinenum = data.examinenum;
				var overnum = data.overnum;
				if(noexamiennum > 0){
					$('.span_nav2 span:first').prepend(redPoint);
				}
				if(overnum > 0){
					$('.span_nav2 span[class="mid"]').prepend(redPoint);
				}
				if(examinenum > 0){
					$('.span_nav2 span:last').prepend(redPoint);
				}
			}
		}
	});
}

//模糊查询
function fuzzySearch(){
	obj.keyword = $('#key').val();
	$('#noexaminelist').empty();
	queryNoExamineInfo();
}

//查询信息
function queryNoExamineInfo(){
	requestPost(fangwen_url,obj,"noExamine",false,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			var list = resultMap.tasklist;
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
			var assi = "";
			$.each(map.assistlist,function(k,item){
				if(item != null){
					assi += item.realname+"，";
				}
			});
			assi = assi.substring(0,(assi.length-1));
			var onclick_url = 'task_detail.html?taskid='+map.taskid+"&userid="+userid+"&cple=yes&forwarduserid="+map.forwarduserid;
			htm += '<li onclick="intoDetail(\''+onclick_url+'\',\''+map.taskid+'\','+map.isread+',19)">'+
			        	'<div class="xx_01"><span '+isread+'>'+map.createname+'发布的任务</span><em class="red">待处理</em></div>'+
			            '<div class="xx_02"><i class="fzr">负责人：<em class="gay">'+map.examinename+'</em></i><i class="xzr">协办人：<em class="gay">'+assi+'</em></i></div>'+
			            '<div class="xx_02"><i class="xzr">截止时间：<em class="gay">'+appBase.parseDateMinute(map.endtime)+'</em></i></div>'+
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
			JianKangCache.setData("worksheet_",resultMap);
			callback(resultMap);
		},error:function(e){
			//取出缓存数据
			JianKangCache.getData("worksheet_",function(resultMap){
				callback(resultMap);
			});
		},complete:function(){
			$('#jiazaizhong').hide();
		}
	});
}