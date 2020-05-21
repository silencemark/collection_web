var fangwen_url = "/app/getExamineJournalList";
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

//时间模糊查询
function fuzzySearch(){
	obj.searchContent = $('#key').val();
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
			var list = resultMap.data;
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
			var journallist = item.journallist;
			
			//得到日期格式的时间
			var createdate = item.createtime;
			
			var topdiv = '<div class="time_name" id="'+createdate+'_div" name="date_div" data="'+createdate+'"><span>'+createdate+'<i id="'+createdate+'_i">0</i><em>点</em></span><b id="'+createdate+'_b" class="up">隐藏</b></div><ul id="'+createdate+'_ul" style="display:none;"></ul>';
			$('#examinelist').append(topdiv);
			
			$.each(journallist,function(i,map){
				var isread = '';
				//判断是否阅读
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
				var xing = "";
				for(var i=0 ; i<5 ; i++){
					if(i<parseInt(map.commentlevel)){
						xing += '<img src="../appcssjs/images/public/start2.png">';
					}else{
						xing += '<img src="../appcssjs/images/public/start3.png">';
					}
				}
				if(map.worksummary == undefined){
					map.worksummary = "无";
				}
				if(map.needhelp == undefined){
					map.needhelp = "无";
				}
				var onclick_url = 'log_detail.html?pid='+map.pid+"&userid="+userid+"&jotype="+map.jotype+'&forwarduserid='+map.id;
				//添加详细列表
				var htm = '<li class="li_del" style="height:163px;" onclick="intoDetail(\''+onclick_url+'\',\''+map.pid+'\',\''+map.isread+'\','+jotype+')">'+
							'<div class="xx_01" style="padding:0px 15px;"><span '+isread+'>'+map.realname+'</span><b>'+xing+'</b></div>'+
							'<div class="li_box" style="padding:0px 15px;" id="'+map.id+'_libox">'+
				        		'<div class="span_list">'+
					                '<div class="span"><span>完成工作: </span><i class="word_hidden">'+map.completedwork+'</i></div>'+
					                '<div class="span"><span>'+worktype+': </span><i class="word_hidden">'+map.worksummary+'</i></div>'+
					                '<div class="span"><span>需协调工作: </span><i class="word_hidden">'+map.needhelp+'</i></div>'+
				                '</div>'+
				            '</div>'+
				            '<a class="del_btn" style="width:0px; height:77px;" onclick="deleteForwardUserInfo(this)" id="'+map.id+'_delbtn">删除</a>'+
				        	'<div class="clear"></div>'+
				            '<div class="xx_02" style="margin:0px 15px;"><i>'+appBase.parseDateMinute(map.createtime)+'</i></div>'+
				        '</li>';
				//显示
				$('#'+createdate+'_ul').append(htm);
			});
			//添加数据条数
			$('#'+createdate+'_i').text(journallist.length);
			
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