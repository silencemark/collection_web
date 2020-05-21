var param = new Object();
$(function(){
	queryBriefList();
});

function pageHelper(num){
	param.currentPage = num;
//	$('#briefing_list').empty();
	queryBriefList();
}

function queryBriefList(){
	
	param.moduleid = $('#moduleid').val();
	param.title= $('#title').val();
	if(param.title != ""){
		param.currentPage = 1;
	}
	requestPost("/userbackstage/getBriefList",param,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			$('#Pagination').html(resultMap.pager);
			if(resultMap.status == 0 || resultMap.status == '0'){
				var brieflist = resultMap.brieflist;
				$('#briefing_list').empty();
				if(brieflist != undefined && brieflist != null){
					showBriefInfo(brieflist);
				}
			}
		}
	});
}

function showBriefInfo(list){
	if(list != null && list.length > 0){
		$.each(list,function(i,map){
			var htm = '<li  style="cursor: pointer;" onclick="location.href=\'/userbackstage/intoBriefingDetialPage?briefid='+map.briefid+'\'">'+
			               '<div class="name"><a href="javascript:void(0)">'+map.title+'</a><i>'+map.createtime+'</i></div>'+
			               '<div class="txt" style="width:800px; text-overflow:ellipsis ; overflow:hidden; white-space:nowrap;"></div>'+
			           '</li>';
			$('#briefing_list').append(htm);
		});
	}
}

//公共的查询方法
function requestPost(url,param,callback){
	$.ajax({
		url:url,
		type:"post",
		data:param,
		beforeSend:function(){
			var load = '<div id="load_mask" class="div_mask" style="opacity:0;"></div>'+
				      '<div id="load_loading" class="loading"><img src="../userbackstage/images/public/loading.gif" width="360" height="200" /></div>';
			$("body").after(load);
		},
		success:function(resultMap){
			callback(resultMap);
		},
		complete:function(){
			$('#load_mask').remove();
			$('#load_loading').remove();
		},
		error:function(e){
			
		}
	});
}