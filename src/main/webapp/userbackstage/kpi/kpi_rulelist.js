
var pageParam = new Object();

$(function(){
	queryCloudFile();
});

function pageHelper(num){
	pageParam.currentPage = num;
	$('#file_list').empty();
	queryCloudFile();
}

function callbackfunc(organizeid){
	pageParam.organizeid = organizeid;
	pageParam.currentPage = 1;
	$('#file_list').empty();
	queryCloudFile();
}

function searchKey(){
	pageParam.title = $('#title').val();
	pageParam.currentPage = 1;
	$('#file_list').empty();
	queryCloudFile();
}

function queryCloudFile(){
	requestPost("/userbackstage/getStarRankingRule",pageParam,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == 0 || resultMap.status == '0'){
				$('#Pagination').html(resultMap.pager);
				var rulelist = resultMap.rulelist;
				if(rulelist != undefined && rulelist != null){
					showCloudFile(rulelist);
				}
			}
		}
	});
}

function showCloudFile(list){
	if(list != null && list.length > 0){
		
		$.each(list,function(i,item){
			var htm = '<li style="cursor: pointer;" onclick="location.href=\'/userbackstage/intoRuleDetail?ruleid='+item.ruleid+'\'">'+
		            	'<div class="name">'+item.title+'</a><i>'+item.createtime+'</i></div>'+
		                '<div class="txt" style="width:500px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;"></div>'+
		              '</li>';
			$('#file_list').append(htm);
		});
	}
}

/*---------------------------------------------编辑----------------------------------------------*/

function editor_close_open(){
	var ed = $('#editor_div').css("display");
	var cd = $('#cloudfile_div').css("display");
	if(ed == "none" && cd == "block"){
		$('#editor_div').show();
		$('#cloudfile_div').hide();
	}else{
		$('#editor_div').hide();
		$('#cloudfile_div').show();
	}
	
}


function addCloudFile(obj){
	$(obj).attr("onclick","");
	var rge_param = {"rangelist":[]}
	var range = $('#range_div').find('input[name=organizeid]');
	var user = $('#range_div').find('input[name=userid]');
	if(range.length<=0 && user.length<=0){
		swal({
		    title: "提示",
		    text: "请选择发布范围！",
		    type: "warning",
		    showCancelButton: false,
		    confirmButtonColor: "#ff7922",
		    confirmButtonText: "确定",
		    closeOnConfirm: true
		}, function(){});
		return false;
	}
	var rge = "";
	$.each(range,function(r,rg){
		var obj = new Object();
		obj.organizeid = $(rg).val();
		rge_param.rangelist[r] = obj;
		rge=(r+1);
	});
	$.each(user,function(u,ur){
		var obj = new Object();
		obj.userid = $(ur).val();
		rge_param.rangelist[rge+u] = obj;
	});
	
	var param = new Object();
	
	param.rangelist = JSON.stringify(rge_param);
	param.title = $('#add_title').val();
	param.content = editor.getContent();
	
	if(param.title == ""){
		$(obj).attr("onclick","addCloudFile(this)");
		swal({
		    title: "提示",
		    text: "规则标题不能为空！",
		    type: "warning",
		    showCancelButton: false,
		    confirmButtonColor: "#ff7922",
		    confirmButtonText: "确定",
		    closeOnConfirm: true
		}, function(){
		});
		return false;
	}
	if(param.content == ""){
		$(obj).attr("onclick","addCloudFile(this)");
		swal({
		    title: "提示",
		    text: "规则内容不能为空！",
		    type: "warning",
		    showCancelButton: false,
		    confirmButtonColor: "#ff7922",
		    confirmButtonText: "确定",
		    closeOnConfirm: true
		}, function(){
		});
		return false;
	}
	$('#myEditor').hide();
	requestPost("/userbackstage/insertStarRankingRuleInfo",param,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			$('#myEditor').show();
			if(resultMap.status == 0 && resultMap.status == '0'){
				$(obj).attr("onclick","addCloudFile(this)");
				swal({
				    title: "提示",
				    text: "添加成功！",
				    type: "success",
				    showCancelButton: true,
				    confirmButtonColor: "#ff7922",
				    confirmButtonText: "确定",
				    cancelButtonText: "取消",
				    closeOnConfirm: true
				}, function(){
					editor_close_open();
					location.reload();
				});
			}
		}
	});
}

function addRange(){
	var range = $('#rangeul').html();
	$('#range_div').html(range);
	close_open_div();
}

function delete_changed_range(obj){
	$(obj).parent('i').remove();
}

function close_open_div(){
	var mask = $('.div_mask').css("display");
	var range = $('#tc_selrange').css("display");
	if(mask == "none" && range == "none"){
		$('#myEditor').hide();
		
		var range = $('#range_div').html();
		$('#rangeul').html(range);
		
		$('.div_mask').show();
		$('#tc_selrange').show();
	}else{
		$('#myEditor').show();
		$('.div_mask').hide();
		$('#tc_selrange').hide();
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