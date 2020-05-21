var fangwen_url = "/app/getExamineExpenseList";
var userid = "";

var obj = new Object();
var readparam = new Object();

var nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>还没有报销单，赶紧添加一条吧~</span><b><img src="'+notDataImageUrl_havepower+'"></b></div>';
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
		
		if(typeof(data.powerMap.power5001210) != "undefined"){
			$('#linkadd').show();
		}
		
		obj.userid = data.userid;
		obj.companyid = data.companyid;
		
		readparam.userid = data.userid;
		readparam.companyid = data.companyid;
		
		obj.status = 1;
		userid = data.userid;
		//
	});
	
	//加载更多
	PageHelper({
		url:projectpath+fangwen_url,
		data:obj,
		success:function(resultMap){
			if(resultMap != undefined && resultMap != null){
				var list = resultMap.expenselist;
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
	
});

function initPage(){
	getNotIsRead(readparam);
	queryExamineInfo(obj);
	//默认打开第一栏
	$('#examinelist ul').first().show();
	$('#'+$('#examinelist div').first().attr("data")+'_b').attr('class','down');
}

function getNotIsRead(param){
	$.ajax({
		url:projectpath+"/app/getExpenseAllNotRead",
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

//时间模糊查询
function fuzzySearch(){
	obj.starttime = $('#starttime').val();
	obj.endtime = $('#endtime').val();
	$('#examinelist').empty();
	queryExamineInfo(obj);
	
	//默认打开第一栏
	$('#examinelist ul').first().show();
	$('#'+$('#examinelist div').first().attr("data")+'_b').attr('class','down');
}

//查询信息
function queryExamineInfo(obj){
	requestPost(fangwen_url,obj,"examine",false,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			var list = resultMap.expenselist;
			if(list != null && list.length > 0){
				$(".list_none").remove();
				showData(list);
			}else{
				$(".list_none").remove();
				$("body").append(nodata);
			}
		}
	});
}

//信息展示
function showData(list){
	if(list.length > 0 && list != null){
		var htm = '';
		$.each(list,function(k,item){
			var expenselist = item.expenselist;
			
			//得到日期格式的时间
			var createdate = item.createtime;
			
			var topdiv = '<div class="time_name" id="'+createdate+'_div" name="date_div" data="'+createdate+'"><span>'+createdate+'<i id="'+createdate+'_i">0</i><em>点</em></span><b id="'+createdate+'_b" class="up">隐藏</b></div><ul id="'+createdate+'_ul" style="display:none;"></ul>';
			$('#examinelist').append(topdiv);
			
			$.each(expenselist,function(i,map){
				var isread = '';
				//判断是否阅读
				if(map.isread == 0 || map.isread == '0'){
					isread = 'class="new"';
				}
				
				var onclick_url = 'expenseaccount_detail.html?expenseid='+map.expenseid+"&userid="+userid+'&forwarduserid='+map.forwarduserid;
				var result = map.result;
				var resulthtml = "";
				if(result == 1){
					resulthtml = '<em class="green">同意</em>';
				}else{
					resulthtml = '<em class="red">拒绝</em>';
				}
				var htm = '<li class="li_del" onclick="intoDetail(\''+onclick_url+'\',\''+map.expenseid+'\','+map.isread+',17)">'+
							'<div class="li_box" id="'+map.forwarduserid+'_libox">'+
					        	'<div class="xx_01"><span '+isread+' style="font-size:12px;">'+map.expenseno+'</span>'+resulthtml+'<i>'+map.createname+'</i></div>'+
		            			'<div class="xx_02"><span class="all">合计：<em>'+map.detailprice+'</em></span><span>共<em>'+map.detailnum+'</em>项</span><i>'+map.createtime+'</i></div>'+
	            			'</div>'+
				            '<a class="del_btn" style="width:0px;" onclick="deleteForwardUserInfo(this)" id="'+map.forwarduserid+'_delbtn">删除</a>'+
				        	'<div class="clear"></div>'+
	            		'</li>';
				//显示
				$('#'+createdate+'_ul').append(htm);
			});
			//添加数据条数
			$('#'+createdate+'_i').text(expenselist.length);
			
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