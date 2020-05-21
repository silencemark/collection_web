var fangwen_url = "/app/getExamineOverallvaluateList";
var userid = "";
var obj = new Object();
$(function(){
	
	$('#starttime').val(GetDateStr(-1));
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
		obj.receiveid = data.userid;
		obj.companyid = data.companyid;
		obj.status = 1;
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
				var list = resultMap.data;
				if(list != null && list.length > 0){
					showData(list);
				}
			}
		}
	}); 
	
	//点击展开/收拢 信息
	$('#examinelist').delegate('div[name="date_div"]','click',function(){
		var dateul = $(this).attr("data");
		var display = $('#'+dateul+'_ul').css("display");
		if(display == "none"){
			$('#'+dateul+'_ul').show();
			$('#'+dateul+'_b').attr("class","down");
		}else if(display == "block"){
			$('#'+dateul+'_ul').hide();
			$('#'+dateul+'_b').attr("class","up");
		}
	});
	
	//默认打开第一栏
	$('#examinelist ul').first().show();
	$('#'+$('#examinelist div').first().attr("data")+'_b').attr('class','down');
});

//时间模糊查询
function fuzzySearch(){
	obj.starttime = $('#starttime').val();
	obj.endtime = $('#endtime').val();
	$('#examinelist').empty();
	queryNoExamineInfo(obj);
}

//查询信息
function queryNoExamineInfo(obj){
	requestPost(fangwen_url,obj,"examine",false,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			var nodata = '<div class="load_more"><a>暂无数据</a></div>';
			var list = resultMap.data;
			if(list != null && list.length > 0){
				$('.load_more').remove();
				showData(list);
			}else{
				$('.load_more').remove();
				$('body').append(nodata);
			}
		}
	});
}

//信息展示
function showData(list){
	if(list.length > 0 && list != null){
		var htm = '';
		$.each(list,function(k,item){
			var overallvaluatelist = item.overallvaluatelist;
			
			//得到日期格式的时间
			var createdate = item.createtime;
			
			var topdiv = '<div class="time_name" id="'+createdate+'_div" name="date_div" data="'+createdate+'"><span>'+createdate+'<i id="'+createdate+'_i">0</i><em>点</em></span><b id="'+createdate+'_b" class="up">隐藏</b></div><ul id="'+createdate+'_ul" style="display:none;"></ul>';
			$('#examinelist').append(topdiv);
			
			$.each(overallvaluatelist,function(i,map){
				var isread = '';
				//判断是否阅读
				if(map.isread == 0 || map.isread == '0'){
					isread = 'class="new"';
				}
				var onclick_url = 'allstar_detail.html?overallvaluateid='+map.overallvaluateid+"&userid="+userid;
				//添加详细列表
				var htm = '<li onclick="intoDetail(\''+onclick_url+'\',\''+map.overallvaluateid+'\',\''+map.isread+'\',7)">'+
				        	'<div class="xx_01"><span '+isread+'>'+map.createname+'的岗位星级</span><em class="green">已处理</em></div>'+
				            '<div class="xx_02"><span class="all">合计：<em>'+map.sumstar+'</em>星</span><i>'+appBase.parseDateMinute(map.createtime)+'</i></div>'+
				        '</li>';
				//显示
				$('#'+createdate+'_ul').append(htm);
			});
			//添加数据条数
			$('#'+createdate+'_i').text(overallvaluatelist.length);
			
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