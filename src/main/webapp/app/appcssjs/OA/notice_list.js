
var fangwen_url = '/app/getNoticeList';
var obj = new Object();
var nodata = '<div class="list_none"><span><i class="yellow">Hi,我是大狮！</i><br>列表空空，还没有通知呢~</span><b><img src="'+notDataImageUrl_nopower+'"></b></div>';
var userid = "";
var companyid = "";
$(function(){
	//初始化信息
	JianKangCache.getGlobalData('userinfo',function(data){
		
		if(typeof(data.powerMap.power5001010) != "undefined"){
			$('#linkadd').show();
			nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>还没有通知，赶紧添加一条吧~</span><b><img src="'+notDataImageUrl_havepower+'"></b></div>';
		}
		
		companyid = data.companyid;
		userid = data.userid;
	});
	
	//加载更多
	PageHelper({
		url:projectpath+fangwen_url,
		data:obj,
		success:function(resultMap){
			if(resultMap != undefined && resultMap != null){
				if(resultMap.status == 0 && resultMap.status == '0'){
					var noticelist = resultMap.ntList;
					if(noticelist.length > 0){
						showNoticeList(noticelist);
					}
				}
			}
		}
	});
	
	
	//滑动删除
	var startX,startY,endX,endY,id,tiems=0,touchtype=-1,deletwidth,fag=false;
	var scrollTopVal=0;
	document.getElementById("notice_list").addEventListener("touchstart", touchStart, false);
	document.getElementById("notice_list").addEventListener("touchmove", touchMove, false);
	document.getElementById("notice_list").addEventListener("touchend", touchEnd, false);
	function touchStart(event){
		 var touch = event.touches[0]; 
		 //获取最外层的id
		 var tar_id = touch.target.offsetParent.id;
		 if(tar_id == ""){
			 tar_id = $(touch.target).parent().attr('id') == null?'':$(touch.target).parent().attr('id');
		 }
		 //判断滑动的位置是否在指定的层里面
		 if(tar_id == undefined || tar_id == ""){
			 fag = true;
			 return;
		 }else{
			 fag = false;
			 id = tar_id.replace("_libox","");
		 }
		 startY = touch.pageY;   
		 startX = touch.pageX;
		 deletwidth = 72;
	}
	function touchMove(event){
		var touch = event.touches[0];
		endY = touch.pageY;
		endX = touch.pageX;
		
		if(Math.abs(endY - startY) > Math.abs(endX - startX)){
			fag = true;
		}else{
			//防止页面上下滚动
			event.preventDefault();
		}
		//滑动的位置，不在删除的那个层里面，不执行以下操作
		if(fag){return;}
		
		if(tiems == 0){
			scrollTopVal = endX;
		}
		
		if((scrollTopVal - endX) < 72 && (scrollTopVal - endX) > 0){
			touchtype = 0;
			var libox = $('#'+id+"_libox").css("margin-left").replace("px","");
			if(libox != -72){
				$('#'+id+"_libox").css("margin-left",-(scrollTopVal - endX));
				$('#'+id+"_delbtn").css("width",scrollTopVal - endX);
			}
		}else if((endX - scrollTopVal) < 72 && (endX - scrollTopVal) > 0 && touchtype != 0){
			touchtype = 1;
			var delet = $('#'+id+"_delbtn").css("width").replace("px","");
			if(delet > 0){
				$('#'+id+"_libox").css("margin-left",(endX - scrollTopVal) - deletwidth);
				$('#'+id+"_delbtn").css("width",(deletwidth - (endX - scrollTopVal)));
			}
		}
		tiems++;
	}
	function touchEnd(event){ 
		tiems = 0;
		
		//滑动的位置，不在删除的那个层里面，不执行以下操作
		if(fag){return;}
		
		var deletewidth = $('#'+id+"_delbtn").css("width").replace("px","");
		if(touchtype == 0){
			if(deletewidth >= 41){
				$('#'+id+"_libox").css("margin-left",-72);
				$('#'+id+"_delbtn").css("width",72);
				touchtype = 1;
			}else{
				$('#'+id+"_libox").css("margin-left",0);
				$('#'+id+"_delbtn").css("width",0);
			}
		}else if(touchtype == 1){
			if(deletewidth <= 41){
				$('#'+id+"_libox").css("margin-left",0);
				$('#'+id+"_delbtn").css("width",0);
			}else{
				$('#'+id+"_libox").css("margin-left",-72);
				$('#'+id+"_delbtn").css("width",72);
			}
		}
	}
});

function initPage(){
	queryNotice();
}

//模糊查询
function fuzzySearch(){
	$('#notice_list').empty();
	obj.title = $('#search_input').val();
	queryNotice();
}

//查询通知信息
function queryNotice(){
	obj.receiveid = userid;
	obj.companyid = companyid;
	requestPost(fangwen_url,obj,"notice_list",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			$(".list_none").remove();
			if(resultMap.status == 0 && resultMap.status == '0'){
				var noticelist = resultMap.ntList;
				showNoticeList(noticelist);
			}else{
				$("body").append(nodata);
			}
		}else{
			$(".list_none").remove();
			$("body").append(nodata);
		}
	});
}

//显示信息
function showNoticeList(list){
	if(list != undefined && list != null && list.length > 0){
//		$('#nodata').hide();
		var htm = "";
		$.each(list,function(i,map){
			var isread = '';
			if(map.isread == 0 || map.isread == '0'){
				isread = 'class="new"';
			}
			var intourl = 'intoDetail(\'notice_detail.html?noticeid='+map.noticeid+'&userid='+userid+'\',\''+map.noticeid+'\','+map.isread+',\'27\')';
			htm += '<li class="li_del" onclick="'+intourl+'">'+
						'<div class="li_box" id="'+map.id+'_libox">'+
				        	'<div class="name"><span '+isread+'>'+map.title+'</span><i>'+map.createtime+'</i></div>'+
				            '<div class="txt" style="width:98%;text-overflow:ellipsis ; overflow:hidden; white-space:nowrap;">'+map.content+'</div>'+
			            '</div>'+
			            '<a class="del_btn" style="width:0px;" onclick="deleteForwardUserInfo(this)" id="'+map.id+'_delbtn">删除</a>'+
			        	'<div class="clear"></div>'+
			        '</li>';
		});
		$('#notice_list').append(htm);
	}else{
		$("body").append(nodata);
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