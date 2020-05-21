var fangwen_url = "/app/getNoExmaineDishesListInfo";
var userid = "";
var obj = new Object();
var readparam = new Object();
var nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>还没有菜品成本单，赶紧添加一条吧~</span><b><img src="'+notDataImageUrl_havepower+'"></b></div>';
$(function(){
	
	//初始化信息
	JianKangCache.getGlobalData('userinfo',function(data){
		
		if(typeof(data.powerMap.power4001310) != "undefined"){
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
		url:projectpath+"/app/getDishesAllNotRead",
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
		$(".list_none").remove();
		if(resultMap != undefined && resultMap != null){
			var list = resultMap.data;
			if(list != null && list.length > 0){
				showData(list);
			}else{
				$("body").append(nodata);
			}
		}else{
			$(".list_none").remove();
			$("body").append(nodata);
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
			var onclick_url = 'cost_detail.html?dishesid='+map.dishesid+"&userid="+userid+'&forwarduserid='+map.id;
			htm += '<li onclick="intoDetail(\''+onclick_url+'\',\''+map.dishesid+'\',\''+map.isread+'\',14)">'+
			        	'<div class="xx_01"><span '+isread+'><span class="name">'+map.dishesname+'</span></span><em class="red">待处理</em></div>'+
			            '<div class="xx_02"><span class="lb">'+(map.dishestype!=null && map.dishestype!=undefined && map.dishestype!=""?map.dishestype:"")+'</span><span class="cb">成本 <em>￥'+map.costprice+'</em></span><span class="sj">售价 <em>￥'+map.price+'</em></span></div>'+
			        	'<div class="xx_02"><span>'+map.createname+'</span><i>'+map.createtime+'</i></div>'+
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