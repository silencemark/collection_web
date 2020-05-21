var fangwen_url = "/app/getOverallvaluateList";
var userid = "";
var obj = new Object();
var readparam = new Object();

var nodata = '<div class="list_none"><span><i class="yellow">Hi,我是大狮！</i><br>列表空空，还没有综合星值呢~</span><b><img src="'+notDataImageUrl_nopower+'"></b></div>';

$(function(){
	
	//初始化信息
	JianKangCache.getGlobalData('userinfo',function(data){
		
		if(typeof(data.powerMap.power3001010) != "undefined"){
			$('#linkadd').show();
			nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>还没有综合星值，赶紧添加一条吧~</span><b><img src="'+notDataImageUrl_havepower+'"></b></div>';
		}
		
		obj.receiveid = data.userid;
		obj.companyid = data.companyid;
		
		readparam.receiveid = data.userid;
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
				var list = resultMap.overallvaluatelist;
				if(list != null && list.length > 0){
					showData(list);
				}
			}
		}
	}); 
});

function initPage(){
	getAllstatNotIsRead(readparam);
	queryNoExamineInfo(obj);
}

function getAllstatNotIsRead(param){
	$.ajax({
		url:projectpath+"/app/getOverallvaluateAllNotIsRead",
		type:"post",
		data:param,
		success:function(data){
			if(data.status == 0 || data.status == '0'){
				var overnum = data.overnum;
				var overtimenum = data.overtimenum;
				if(overnum > 0){
					$('.span_nav span:first').prepend(redPoint);
				}
				if(overtimenum > 0){
					$('.span_nav span:last').prepend(redPoint);
				}
			}
		}
	});
}

//查询信息
function queryNoExamineInfo(obj){
	requestPost(fangwen_url,obj,"noExamine",false,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			var list = resultMap.overallvaluatelist;
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
			var onclick_url = 'allstar_detail.html?overallvaluateid='+map.overallvaluateid+"&userid="+userid+'&forwarduserid='+map.forwarduserid;
			if(map.examineuserid == userid){
				onclick_url = 'allstar_check.html?overallvaluateid='+map.overallvaluateid+"&userid="+userid;
			}
			htm += '<li onclick="intoDetail(\''+onclick_url+'\',\''+map.overallvaluateid+'\',\''+map.isread+'\',7)">'+
			           '<div class="xx_01"><span '+isread+'>'+map.createname+'的综合星值</span><em class="red">待处理</em></div>'+
			           '<div class="xx_02"><span class="all">合计：<em>'+map.sumstar+'</em>星</span><i>'+appBase.parseDateMinute(map.createtime)+'</i></div>'+
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