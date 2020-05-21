
var companyid = "";
var userid = "";
var organizeid = "";
var userInfo = "";
var data = new Object();
var obj = new Object();
//初始化信息
$(function(){
	
	//初始化 yearul 年份 
	var nowdate = new Date();
	nowyear=nowdate.getFullYear();
	var yeartemp="";
	for(var k=0;k < 100 ;k++){
		if(k==0){
			yeartemp+="<li year=\""+(nowyear-k)+"\" class=\"active\"  onclick=\"clickSelect(this)\">"+(nowyear-k)+"年</li>";
		}else{
			yeartemp+="<li year=\""+(nowyear-k)+"\"  onclick=\"clickSelect(this)\">"+(nowyear-k)+"年</li>";
		}
	}
	$('#yearul').html(yeartemp);
	
	var mydate = new Date();
	yearnum=mydate.getFullYear();
	var month=mydate.getMonth()+1; //获取当前月份(0-11,0代表1月)
	if(parseInt(month)<10){
		monthnum="0"+month;
	}else{
		monthnum=month;
	}
	$('#yearnum').val(yearnum);
	$('#monthnum').val(monthnum);
	$('#change_time').val(yearnum+"年-"+monthnum+"月");
	$('#monthul').find('li[name="'+monthnum+'"]').attr("class","active");
	
	JianKangCache.getGlobalData('userinfo',function(data){
		companyid = data.companyid;
		userid = data.userid;
		organizeid = data.organizeid;
		obj.userid = data.userid;
		obj.companyid = data.companyid;
		userInfo = data;
	});
	//设置初始的机构和时间
	
	queryOrganizeList(obj)
	//加载更多
	PageHelper({
		url:projectpath+"/app/getRankingList",
		data:data,
		success:function(resultMap){
			if(resultMap != null && resultMap != ""){
				var userlist = resultMap.userlist;
				var page = resultMap.page;
				showRanking(userlist,page);
			}
		}
	});
	
	
	$('#organize_option').delegate("li[name='organize_li']","click",function(){
		$("li[name='organize_li']").attr("class","");
		$(this).attr("class","active");
	});
	
	$('#sure_btn').click(function(){
		var param = $('#organize_option li[class="active"]');
		if(param.length > 0){
			organizeid = $(param).attr("organizeid");
			$('#organize').val($(param).attr("organizename"));
			$('#organize').attr("organizeid",$(param).attr("organizeid"));
			//queryRanking();
		}
	});
	
	$("#organize_mask").click(function(){
		close_open_organizeDiv();
    });
	
	
	$('#time_mask').click(function(){
		changetimediv();
	});
})

function checkall(check){
	if(check == "yes"){
		$('#organize').attr("organizeid","");
		$('#organize').val(userInfo.companyname);
		$('#change_time').val("");
		$('#yearnum').val("");
		$('#monthnum').val("");
	}else if(check == "no"){
		$('#organize').attr("organizeid",userInfo.organizeid);
		$('#organize').val(userInfo.organizename);
		var mydate = new Date();
		yearnum=mydate.getFullYear();
		var month=mydate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		if(parseInt(month)<10){
			monthnum="0"+month;
		}else{
			monthnum=month;
		}
		$('#yearnum').val(yearnum);
		$('#monthnum').val(monthnum);
		$('#change_time').val(yearnum+"年-"+monthnum+"月");
		$('#monthul').find('li[name="'+monthnum+'"]').attr("class","active");
	}
	changetimediv();
}

//查询区域信息
function queryOrganizeList(obj){
	requestPost("/app/getOrganizeListByUser",obj,"organize",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == '0' || resultMap.status == 0){
				var organizelist = resultMap.organizelist;
				showOrganizeInfo(organizelist);
			}
		}
	});
}

//显示区域信息
function showOrganizeInfo(list){
	if(list.length > 0 && list != null){
		$('.load_more').remove();
		var html = "";
		var fag = false;
		var organizename = "";
		$.each(list,function(i,map){
			var active = "";
			if(i==0){
				$('#organize').val(map.organizename);
				$('#organize').attr("organizeid",map.organizeid)
			}
			if(map.organizeid==organizeid){
				//
				fag = true;
				organizename = map.organizename;
				active = "class='active'";
			}
			html += '<li '+active+' name="organize_li" organizeid="'+map.organizeid+'" organizename="'+map.organizename+'" organizeid="'+map.organizeid+'">'+map.organizename+'</li>';
		});
		$('#organize_option').append(html);
		if(fag){
			$('#organize').val(organizename);
			$('#organize').attr("organizeid",organizeid)
		}
		//查询
		queryRanking();
	}
}
//
function queryRanking(){
	var organizeid = $('#organize').attr("organizeid");
	var year = $('#yearnum').val();
	var month = $('#monthnum').val();
	
	data.year = year;
	data.monthnum = month;
	data.organizeid = organizeid;
	data.companyid = companyid;
	//查询排行信息
	requestPost("/app/getRankingList",data,"ranking",true,function(resultMap){
		if(resultMap != null && resultMap != ""){
			$('#rank_list').empty();
			var userlist = resultMap.userlist;
			var page = resultMap.page;
			showRanking(userlist,page);
		}
	});
}

//显示排行信息
function showRanking(list,page){
	var nodata = '<div class="load_more"><a>暂无数据</a></div>';
	if(list.length > 0 && list != null){
		$('.load_more').remove();
		var pageno = page.pageNo;
		var pagesize = page.pageSize;
		$.each(list,function(i,map){
			var yangshi = "";
			if(i<3 && pageno==1){
				yangshi = "yellow";
			}
			var html = '<li>'+
				        	'<div class="num bg">'+((parseInt(pageno)-1)*parseInt(pagesize)+1+i)+'</div>'+
				            '<div class="name"><b><img src="'+map.headimage+'"></b><span>'+map.realname+'</span></div>'+
				            '<div class="start '+yangshi+'">'+map.allstar+'</div>'+
				        '</li>';
			$('#rank_list').append(html);
		});
	}else{
		$('.load_more').remove();
		$('body').append(nodata);
	}
}

//打开或者关闭区域选择层
function close_open_organizeDiv(){
	var disk = $('#organize_mask').css("display");
	var organize = $('#organize_div').css("display");
	if(disk == "none" && organize == "none"){
		$('#organize_mask').show();
		$('#organize_div').show();
	}else{
		$('#organize_mask').hide();
		$('#organize_div').hide();
	}
}

function changetimediv(){
	var mask = $('#time_mask').css("display");
	var timediv = $('#timediv').css("display");
	if(mask == "none" && timediv == "none"){
		$('#time_mask').show();
		$('#timediv').show();
	}else{
		$('#time_mask').hide();
		$('#timediv').hide();
	}
}

function changetime(){
	var year =$('#yearul li[class=active]').attr('year');
	var month =$('#monthul li[class=active]').attr('month');
	if(month==""){
		$('#change_time').val(year+"年（全年）");
	}else{
		$('#change_time').val(year+"年-"+month+"月");
	}
	$('#yearnum').val(year);
	$('#monthnum').val(month);
	
	changetimediv();
}
function clickSelect(obj){
	$(obj).parent().find("li").attr("class","");
	$(obj).attr("class","active");
}

function intorulelist(){
	location.href="rule_list.html?organizeid="+$('#organize').attr("organizeid");
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