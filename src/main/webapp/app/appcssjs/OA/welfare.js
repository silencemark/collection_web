
var obj = new Object();
var userid = "";
var userInfo = "";

var nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>当月没有员工过生日呢~</span><b><img src="'+notDataImageUrl_havepower+'"></b></div>';
$(function(){
	var mydate = new Date();
	var month=mydate.getMonth()+1;
	
	$('#month').text(month);
	
	//初始化信息
	JianKangCache.getGlobalData('userinfo',function(data){
		obj.companyid = data.companyid;
		userid = data.userid;
		userInfo = data;
	});
	
	//加载更多
	PageHelper({
		url:projectpath+"/app/getUserBirthdayList",
		data:obj,
		success:function(resultMap){
			if(resultMap != undefined && resultMap != null){
				var list = resultMap.userlist;
				if(list != null && list.length > 0){
					showWelfare(list);
				}
			}
		}
	});
	
	queryWelfare();
});

function changeMonth(num){
	var month  = parseInt($('#month').text()) + parseInt(num);
	if(month<=0){
		month = 12;
	}else if(month >= 13){
		month = 1;
	}
	obj.monthnum = month;
	$('#month').text(obj.monthnum);
	$('#welfare_list').empty();
	queryWelfare();
}

function queryWelfare(){
	obj.monthnum = $('#month').text();
	requestPost("/app/getUserBirthdayList",obj,"welfare",true,function(resultMap){
		$(".list_none").remove();
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == '0' || resultMap.status == 0){
				var userlist = resultMap.userlist;
				if(userlist != null && userlist.length > 0){
					showWelfare(userlist);
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

function showWelfare(list){
	if(list != null && list.length > 0){
		var dd = new Date(); 
		var m = dd.getMonth()+1;//获取当前月份的日期 
		var d = dd.getDate();
		$.each(list,function(i,map){
			var cls = 'style="color:gray;"';
			//var na = "";添加关注颜色对象
			if(d==map.bday && m==map.bmonth){
				cls = 'style="color:red;"';
				//na = 'style="color:red;"';添加关注颜色
			}
			if(parseInt(map.bday) > parseInt(d) && parseInt(m) <= parseInt(map.bmonth)){
				cls = '';
			}
			var welfarehtml_li = $('#'+map.userid+'_welfare');
			if(welfarehtml_li.length == 0){
				//添加到<li '+na+'>中显示
				var htm = '<li id="'+map.userid+'_welfare">'+
							'<div class="img"><img src="'+projectpath+map.headimage+'"></div>'+       
				            '<div class="name"><span>'+map.realname+'</span><i '+cls+'>'+map.birthday+'</i></div>'+
				            '<div class="txt"><span>'+((map.position)==undefined?"":map.position)+'</span><i>'+((map.organizename)==undefined?"":map.organizename)+'</i></div>'+
				        '</li>';
				$('#welfare_list').append(htm);
			}else{
				$(welfarehtml_li).find("div[class='txt'] i").append("，"+((map.organizename)==undefined?"":map.organizename));
			}
		});
	}else{$('#nodata').show();}
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