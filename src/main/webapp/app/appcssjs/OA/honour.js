var fangwen_url = "/app/getUserRewardList";
var userid = "";
var userInfo = "";
var obj = new Object();

var nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>有荣誉榜还没人呢~</span><b><img src="'+notDataImageUrl_havepower+'"></b></div>';
$(function(){
	$('#starttime').val(GetDateStr(-30));
	$('#endtime').val(GetDateStr(0));
	var currYear = (new Date()).getFullYear();	
	var opt={};
		opt.date = {preset : 'date', dateFormat: 'yy-mm-dd' };
		//opt.datetime = { preset : 'datetime', minDate: new Date(2012,3,10,9,22), maxDate: new Date(2014,7,30,15,44), stepMinute: 5  };
		opt.datetime = {preset : 'date', dateFormat: 'yy-mm-dd' }; 
		opt.time = {preset : 'time'};
		opt.default = { 
			theme: 'android-ics light', //皮肤样式  
	        display: 'modal', //显示方式  
	        mode: 'scroller', //日期选择模式
	        dateOrder : 'yymmdd',  
			lang:'zh',
	        startYear:currYear - 10, //开始年份
	        endYear:currYear + 10 //结束年份
		};
	//初始化时间插件
	$("#starttime").scroller('destroy').scroller($.extend(opt['datetime'], opt['default']));
	$("#endtime").scroller('destroy').scroller($.extend(opt['datetime'], opt['default']));
	
	//初始化信息
	JianKangCache.getGlobalData('userinfo',function(data){
		obj.companyid = data.companyid;
		userid = data.userid;
		userInfo = data;
		
		fuzzySearch();
	});
	
	//加载更多
	PageHelper({
		url:projectpath+fangwen_url,
		data:obj,
		success:function(resultMap){
			if(resultMap != undefined && resultMap != null){
				var list = resultMap.userlist;
				if(list != null && list.length > 0){
					showHonour(list);
				}
			}
		}
	}); 
});

function fuzzySearch(){
	obj.starttime = $('#starttime').val();
	obj.endtime = $('#endtime').val();
	$('#honour_list').empty();
	queryHonour();
}


function queryHonour(){
	requestPost(fangwen_url,obj,"honour",true,function(resultMap){
		$(".list_none").remove();
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == 0 || resultMap.status == '0'){
				var userlist = resultMap.userlist;
				if(userlist != null && userlist.length > 0){
					showHonour(userlist);
				}else{
					$("body").append(nodata);
				}
			}else{
				$("body").append(nodata);
			}
		}else{
			$("body").append(nodata);
		}
	});
}


function showHonour(list){
	if(list != null && list.length > 0){
		
		$.each(list,function(i,map){
			var htm = '<li onclick="location.href=\'../worksheet/reward_detail.html?rewardid='+map.rewardid+'&userid='+userid+'\'">'+
							'<div class="img"><img src="'+projectpath+map.headimage+'"></div>'+        
				            '<div class="name"><span>'+map.realname+'</span><i>'+map.createtime+'</i></div>'+
				            '<div class="txt">'+((map.rewardresult)==undefined?"":(map.rewardresult))+'</div>'
				        '</li>';
			$('#honour_list').append(htm);
		});
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

function GetDateStr(AddDayCount) { 
	var dd = new Date(); 
	dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期 
	var y = dd.getFullYear(); 
	var m = dd.getMonth()+1;//获取当前月份的日期 
	if(parseInt(m)<10){
		m="0"+m;
	}
	var d = dd.getDate();
	if(parseInt(d)<10){
		d="0"+d;
	}
	return y+"-"+m+"-"+d; 
} 