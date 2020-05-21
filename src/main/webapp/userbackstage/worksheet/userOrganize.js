
$(function(){
	queryOrganzieList();
});

function queryOrganzieList(){
	requestPostOrganize("/userbackstage/getOrganizeListInfo","",function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == 0 && resultMap.status == '0'){
				var organizelist = resultMap.organizelist;
				if(organizelist != null && organizelist != ""){
					showOrganize(organizelist);
				}
			}
		}
	});
}

function showOrganize(list){
	if(list != null && list.length > 0){
		var white = '<div class="white_line" name="white_box"></div>';
		
		$.each(list,function(i,map){
			var display = "block";
			var cla= "bg_show";
			if(i!=0){
				display = "none";
				cla= "bg_hidden";
			}
			var htm_li = $('#orgUl_'+map.parentid).html();
			var li_bg = "li_bg";
			if(htm_li == null || htm_li == undefined){
				li_bg = "";
			}
			var show_organizeid = "";
			if(map.parentid != undefined && map.parentid != ""){
				show_organizeid = map.organizeid;
			}
			var bg_style = '<div class="gray_line" style="display:'+display+';" id="grayLine_'+map.organizeid+'"></div>'+
					    	'<span class="'+cla+'" id="region_organize_'+map.organizeid+'" onclick="showorganizeList(\''+show_organizeid+'\',this)"><i name="org_organizename">'+map.organizename+'</i></span>'+
					        '<ul style="display:'+display+';" id="orgUl_'+map.organizeid+'"></ul>';
			if(parseInt(map.childnum) <= 0){
				bg_style='<span class="bg_last" id="region_organize_'+map.organizeid+'" onclick="showorganizeList(\''+show_organizeid+'\',this,\'last\')"><i name="org_organizename">'+map.organizename+'</i></span>';
			}
			var htm = '<li class="'+li_bg+'">'+
				        	bg_style+
				        	'<div id="white_'+map.parentid+'" class="white_line"  name="white_box"></div>'+
				        '</li>';
			
			if(htm_li != null && htm_li != undefined){
				$('#white_'+map.parentid).remove();
				$('#orgUl_'+map.parentid).append(htm);
			}else{
				$('#tree_list').append(htm);
			}
		});
	}
}

function showorganizeList(organizeid,obj,last){
	var grayline = $('#grayLine_'+organizeid).css("display");
	var orgul = $('#orgUl_'+organizeid).css("display");
	if(grayline == "none" && orgul ==  "none"){
		$(obj).attr("class","bg_show");
		$('#grayLine_'+organizeid).show();
		$('#orgUl_'+organizeid).show();
	}else{
		if(last != "last"){
			$(obj).attr("class","bg_hidden");
		}
		$('#grayLine_'+organizeid).hide();
		$('#orgUl_'+organizeid).hide();
	}
	graylineauto();
	$('i[ name="org_organizename"]').attr("class","");
	$(obj).find('i[name="org_organizename"]').attr("class","bg_yellow");
	
	changedOrganize(organizeid);
//	queryInspectTemplate(organizeid);
}

//公共的查询方法
function requestPostOrganize(url,param,callback){
	$.ajax({
		url:url,
		type:"post",
		data:param,
		beforeSend:function(){},
		success:function(resultMap){
			callback(resultMap);
		},
		complete:function(){},
		error:function(e){
			
		}
	});
}


function graylineauto(){
	$(".tree_list .white_line").each(function() {		
		whiteHeight = $(this).parent().height();
		whiteHeight = whiteHeight - 21;
        $(this).height(whiteHeight) ;
    });
}