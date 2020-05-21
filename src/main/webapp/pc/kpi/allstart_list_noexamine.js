var fangwen_url = "/app/getOverallvaluateList";
var userid = "";
$(function(){
	
	var obj = new Object();
	//初始化信息
	JianKangCache.getGlobalData('userinfo',function(data){
		obj.receiveid = data.userid;
		obj.companyid = data.companyid;
		obj.status = 0;
		userid = data.userid;
		//
		queryNoExamineInfo(obj);
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

//查询信息
function queryNoExamineInfo(obj){
	requestPost(fangwen_url,obj,"noExamine",false,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			var nodata = '<div class="load_more"><a>暂无数据</a></div>';
			var list = resultMap.overallvaluatelist;
			if(list != null && list.length > 0){
				showData(list);
			}else{
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
			var onclick_url = 'allstar_detail.html?overallvaluateid='+map.overallvaluateid+"&userid="+userid;
			if(map.examineuserid == userid){
				onclick_url = 'allstar_check.html?overallvaluateid='+map.overallvaluateid+"&userid="+userid;
			}
			htm += '<li onclick="intoDetail(\''+onclick_url+'\',\''+map.overallvaluateid+'\',\''+map.isread+'\',7)">'+
			           '<div class="xx_01"><span '+isread+'>'+map.createname+'的岗位星级</span><em class="red">待处理</em></div>'+
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