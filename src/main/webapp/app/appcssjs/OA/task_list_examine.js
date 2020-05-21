var fangwen_url = "/app/getTaskExamine";
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
				var list = resultMap.tasklist;
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

//时间模糊查询
function fuzzySearch(){
	obj.keyword = $('#key').val();
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
			var list = resultMap.tasklist;
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
			var tasklist = item.tasklist;
			
			//得到日期格式的时间
			var createdate = item.createtime;
			
			var topdiv = '<div class="time_name" id="'+createdate+'_div" name="date_div" data="'+createdate+'"><span>'+createdate+'<i id="'+createdate+'_i">0</i><em>点</em></span><b id="'+createdate+'_b" class="up">隐藏</b></div><ul id="'+createdate+'_ul" style="display:none;"></ul>';
			$('#examinelist').append(topdiv);
			
			$.each(tasklist,function(i,map){
				var isread = '';
				//判断是否阅读
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
				var onclick_url = 'task_detail.html?taskid='+map.taskid+"&userid="+userid+"&cple=success&forwarduserid="+map.forwarduserid;
				htm = '<li class="li_del" style="height:127px;" onclick="intoDetail(\''+onclick_url+'\',\''+map.taskid+'\','+map.isread+',19)">'+
							'<div class="li_box" id="'+map.forwarduserid+'_libox">'+
								'<div class="xx_01"><span '+isread+'>'+map.createname+'发布的任务</span><em class="green">已完成</em></div>'+
					            '<div class="xx_02"><i class="fzr">负责人：<em class="gay">'+map.examinename+'</em></i><i class="xzr">协办人：<em class="gay">'+assi+'</em></i></div>'+
				            '</div>'+
				            '<a class="del_btn" style="width:0px;" onclick="deleteForwardUserInfo(this)" id="'+map.forwarduserid+'_delbtn">删除</a>'+
				        	'<div class="clear"></div>'+
				            '<div class="xx_02" style="padding:0px 15px;"><i class="xzr">截止时间：<em class="gay">'+appBase.parseDateMinute(map.endtime)+'</em></i></div>'+
				        '</li>';
				//显示
				$('#'+createdate+'_ul').append(htm);
			});
			//添加数据条数
			$('#'+createdate+'_i').text(tasklist.length);
			
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