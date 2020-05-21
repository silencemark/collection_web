var fangwen_url = "/app/getJournalList";
var userid = "";
var obj = new Object();
var readparam = new Object();

var nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>还没有日志，赶紧添加一条吧~</span><b><img src="'+notDataImageUrl_havepower+'"></b></div>';
$(function(){
	
	
	//初始化信息
	JianKangCache.getGlobalData('userinfo',function(data){
		
		if(typeof(data.powerMap.power5001410) != "undefined"){
			$('#linkadd').show();
		}
		
		obj.receiveid = data.userid;
		obj.companyid = data.companyid;
		
		readparam.receiveid = data.userid;
		readparam.companyid = data.companyid;
		
		userid = data.userid;
		//
	});
	
	//加载更多
	PageHelper({
		url:projectpath+fangwen_url,
		data:obj,
		success:function(resultMap){
			if(resultMap != undefined && resultMap != null){
				var list = resultMap.dataList;
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
		url:projectpath+"/app/getJournalAllNotRead",
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

//模糊查询
function fuzzySearch(){
	obj.searchContent = $('#key').val();
	$('#noexaminelist').empty();
	queryNoExamineInfo();
}

//查询信息
function queryNoExamineInfo(){
	requestPost(fangwen_url,obj,"noExamine",false,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			var list = resultMap.dataList;
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
			var jotype = "";
			var worktype = "工作总结";
			if(map.jotype == 1 || map.jotype == '1'){
				jotype = 23;
				worktype = "未完成工作";
				map.realname = map.realname+"的日报";
			}else if(map.jotype == 2 || map.jotype == '2'){
				jotype = 24;
				map.realname = map.realname+"的周报";
			}else if(map.jotype == 3 || map.jotype == '3'){
				jotype = 25;
				map.realname = map.realname+"的月报";
			}
			
			if(map.worksummary == undefined){
				map.worksummary = "无";
			}
			if(map.needhelp == undefined){
				map.needhelp = "无";
			}
			var onclick_url = 'log_detail.html?pid='+map.pid+"&userid="+userid+"&jotype="+map.jotype+'&forwarduserid='+map.id;
			htm += '<li onclick="intoDetail(\''+onclick_url+'\',\''+map.pid+'\','+map.isread+','+jotype+')">'+
			        	'<div class="name"><span '+isread+'>'+map.realname+'</span><i>'+appBase.parseDateMinute(map.createtime)+'</i></div>'+
			            '<div class="span_list">'+
			                '<div class="span"><span>完成工作: </span><i class="word_hidden">'+map.completedwork+'</i></div>'+
			                '<div class="span"><span>'+worktype+': </span><i class="word_hidden">'+(map.worksummary)+'</i></div>'+
			                '<div class="span"><span>需协调工作: </span><i class="word_hidden">'+(map.needhelp)+'</i></div>'+
			            '</div>'+
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