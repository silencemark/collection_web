
var companyid = "";
var userid = "";
var organizeid = "";
var userInfo = "";
var data = new Object();

var orgaidNum = 1;
var pagehelper = null;
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
		$('#organizename').text(data.organizename);
		$('#organizeid').val(data.organizeid);
		userInfo = data;
	});
	//设置初始的机构和时间
	
	queryRanking();
//	queryOrganizeList(obj)
	onloadSelect();
	
	//加载更多
	pagehelper = PageHelper({
		url:projectpath+"/app/getRankingList",
		data:data,
		success:function(resultMap){
			if(resultMap != null && resultMap != ""){
				var userlist = resultMap.userlist;
				if(userlist.length > 0){
					var page = resultMap.page;
					showRanking(userlist,page);
				}
			}
		}
	});
	
	
	$('#time_mask').click(function(){
		changetimediv();
	});
})

function logical(){
	$('#organizename').text($('#organame').val());
	$('#organizeid').val($('#orgaid').val());
}

function checkall(check){
	if(check == "yes"){
		$('#organize').attr("organizeid","");
		$('#organizename').text(userInfo.companyname);
		$('#change_time').val("");
		$('#yearnum').val("");
		$('#monthnum').val("");
	}else if(check == "no"){
		$('#organize').attr("organizeid",userInfo.organizeid);
		$('#organizename').text(userInfo.organizename);
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


function queryRanking(){
	var organizeid = $('#organizeid').val();
	var year = $('#yearnum').val();
	var month = $('#monthnum').val();
	
	data.year = year;
	data.monthnum = month;
	data.organizeid = organizeid;
	data.companyid = companyid;
	data.pageNo = 1;
	
	//查询排行信息
	requestPost("/app/getRankingList",data,"ranking",true,function(resultMap){
		if(resultMap != null && resultMap != ""){
			pagehelper.s.isok = true;
			pagehelper.s.pageNo = 1;
			$('#rank_list').empty();
			allorderNum = 0;
			var userlist = resultMap.userlist;
			var page = resultMap.page;
			showRanking(userlist,page);
		}
	});
}
var nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>排行还没有人，赶紧自评一条吧~</span><b><img src="'+notDataImageUrl_havepower+'"></b></div>';
var allorderNum = 0;
//显示排行信息
function showRanking(list,page){
	if(list != null && list.length > 0){
		$(".list_none").remove();
		var pageno = page.pageNo;
		var pagesize = page.pageSize;
		
		var allstarNum = 0;
		$.each(list,function(i,map){
			
			if(allstarNum > map.allstar || allstarNum == 0){
				var ordernum = allorderNum+1;
				allorderNum = ordernum;
				allstarNum = map.allstar;
				//显示排行人员详情
				showRankIngDetail(map,pageno,pagesize,ordernum,i);
			}else if(allstarNum == map.allstar){
				if(allorderNum < 4){
					showRankIngDetail(map,pageno,pagesize,allorderNum,i);
				}else{
					allorderNum = allorderNum+1;
					showRankIngDetail(map,pageno,pagesize,allorderNum,i);
				}
			}
		});
	}else{
		$(".list_none").remove();
		$('#htmlbody').append(nodata);
	}
}

function showRankIngDetail(map,pageno,pagesize,ordernum,i){
	var yangshi = "";
	if(ordernum<=3){
		yangshi = "yellow";
	}
	if(ordernum == 1){
		ordernum = '<img src="../appcssjs/images/kpi/mun_01.gif" height="30">';
	}else if(ordernum == 2){
		ordernum = '<img src="../appcssjs/images/kpi/mun_02.gif" height="30">';
	}else if(ordernum == 3){
		ordernum = '<img src="../appcssjs/images/kpi/mun_03.gif" height="30">';
	}else{
		ordernum = '<span style="margin-left:15px;">'+ordernum+'</span>';
	}
	var html = '<li id="kpi_paihang'+((parseInt(pageno)-1)*parseInt(pagesize)+1+i)+'" onclick="showxingzhi(this,'+((parseInt(pageno)-1)*parseInt(pagesize)+1+i)+')" style="height:62px; padding-top:15px;">'+
		        	'<div class="num">'+ordernum+'</div>'+
		            '<div class="name"><b><img style="min-height:38px;" src="'+projectpath+map.headimage+'"></b><span>'+map.realname+'</span></div>'+
		            '<div class="start '+yangshi+'">'+map.allstar+'</div>'+
		        '</li>'+
		        '<li style="display:none;" id="kpi_xingzhi'+((parseInt(pageno)-1)*parseInt(pagesize)+1+i)+'"><div style="line-height:30px; height:31px;><span style=" text-align:center; display:block; color:#757575; width:50%; float:left;">综合星值：<em class="yellow">'+map.overallstar+'</em></span><span style="text-align:center; display:block; color:#757575; width:50%; float:left;">岗位星值：<em class="yellow">'+map.selfstar+'</em></span></div></li>';
	$('#rank_list').append(html);
}

function showxingzhi(obj,i){
	var disp = $('#kpi_xingzhi'+i).css("display");
	if(disp == "none"){
		$('#kpi_xingzhi'+i).show();
		$(obj).css({"height":"","padding-top":""});
	}else{
		$(obj).css({"height":"62px","padding-top":"15px"});
		$('#kpi_xingzhi'+i).hide();
	}
}

//打开或者关闭区域选择层
function close_open_organizeDiv(){
	var organize = $('#organizeiframe').css("display");
	if(organize == "none" && organize == "none"){
		$('#organizeiframe').show();
		$('#htmlbody').hide();
	}else{
		$('#organizeiframe').hide();
		$('#htmlbody').show();
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