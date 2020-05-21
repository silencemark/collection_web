var fangwen_url = "/app/getNoExamineRepairListInfo";
var userid = "";
var nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>还没有报修单，赶紧添加一条吧~</span><b><img src="'+notDataImageUrl_havepower+'"></b></div>';
var obj = new Object();
var readparam = new Object();
$(function(){
	
	//初始化信息
	JianKangCache.getGlobalData('userinfo',function(data){
		
		if(typeof(data.powerMap.power4001210) != "undefined"){
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
				var list = resultMap.data;
				if(list != null && list.length > 0){
					showData(list);
				}
			}
		}
	}); 
});

function initPage(){
	getNotIsRead(readparam);
	queryNoExamineInfo(obj);
}

function getNotIsRead(param){
	$.ajax({
		url:projectpath+"/app/getRepairAllNotRead",
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
function queryNoExamineInfo(obj){
	requestPost(fangwen_url,obj,"noExamine",false,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			var list = resultMap.data;
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
			var onclick_url = 'repair_detail.html?repairid='+map.repairid+"&userid="+userid+'&forwarduserid='+map.id;
			
			htm += '<li onclick="intoDetail(\''+onclick_url+'\',\''+map.repairid+'\',\''+map.isread+'\',13)">'+
			        	'<div class="xx_01"><span '+isread+'><span class="name">'+map.createname+'的报修单</span></span><em class="red">待处理</em></div>'+
			            '<div class="txt">'+(map.description).substring(0,30)+'</div>'+
			        	'<div class="xx_02"><i>'+map.createtime+'</i></div>'+
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