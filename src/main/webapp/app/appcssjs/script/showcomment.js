var isforwarding;
function addcommentAjax(param){
	param.parentid = $('#parentid').val();
	if($('#parentuserid').val() == param.userid){
		param.parentid = "";
	}
	if(param.content == ""){
		swal({
			title : "",
			text : "评论信息不能为空！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
		return false;
	}
	$.ajax({
		url:projectpath+"/app/insertComment",
		data:param,
		type:"post",
		success:function(resultMap){
			comment_tishi(resultMap,param);
			//给所有的转发者推送信息
			changeIsReadAll(param.userid,param.resourceid,param.resourcetype);
		}
	});
}

function changeIsread(receiveid,resourceid){
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/updateIsread?receiveid='+receiveid+'&isread=0&resourceid='+resourceid,
		success:function(data){
		}
	})
}

//评论框的开关
function comment_opent_close(parentid,parentuserid){
	isforwarding=1;
	var screen = $('#screen').css("display");
	var comment = $('#commentdiv').css("display");
	if(comment == 'none' && screen == 'none'){
		$("#content").val("");
		$('#parentid').val(parentid);
		$('#parentuserid').val(parentuserid);
		$('#screen').show();
		$('#commentdiv').show();
		$("#content").focus();
	}else{
		$('#parentid').val("");
		$('#parentuserid').val("");
		$('#screen').hide();
		$('#commentdiv').hide();
		$("#content").val("");
	}
}

//转发的开关
function forwarding_opent_close(parentid,parentuserid){
	isforwarding=0;
	var screen = $('#screen').css("display");
	var comment = $('#commentdiv').css("display");
	if(comment == 'none' && screen == 'none'){
		$('#parentid').val(parentid);
		$('#parentuserid').val(parentuserid);
		$('#screen').show();
		$('#commentdiv').show();
		$("#content").focus();
	}else{
		$('#parentid').val("");
		$('#parentuserid').val("");
		$('#screen').hide();
		$('#commentdiv').hide();
	}
}



//展示品论信息
function showComment(list){
	if(list != null && list.length > 0){
		$.each(list,function(i,map){
			var replyname = "";
			if(map.replyname != ""){
				replyname = '<i>回复</i><em style="color:#666;">'+map.replyname+"</em>";
			}
			if(map.type == "2"){
				replyname = '<i>转发</i><em>'+map.forwardusernames+'</em>';
			}
			var htm = '<li onclick="comment_opent_close(\''+map.commentid+'\',\''+map.userid+'\')">'+
				           '<div class="xx"><b><img src="'+projectpath+map.headimage+'"></b><span>'+map.realname+'</span>'+replyname+'</div>'+
				           '<div class="txt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+map.content+'</div>'+
				           '<div class="time"><i>'+appBase.parseDateMinute(map.createtime)+'</i></div>'+
				       '</li>';
			$('#comment_list').append(htm);
		});
	}
}

function comment_tishi(resultMap,param){
	if(resultMap.status == 0 || resultMap.status == '0'){
		changeIsread(param.examineuserid,param.resourceid);	
		if(param.parentid!=""){
			changeIsread($('#parentuserid').val(),param.resourceid);
		}
//		swal({
//			title : "",
//			text : "操作成功",
//			type : "success",
//			showCancelButton : false,
//			confirmButtonColor : "#ff7922",
//			confirmButtonText : "确认",
//			cancelButtonText : "取消",
//			closeOnConfirm : true
//		}, function(){
//			
//		});	
//		if(isforwarding==1){
//			location.reload(false);
//			comment_opent_close();
//		}else{			
//			$('.div_mask').hide();
//			$('#commentdiv').hide();
//			$('#organizeiframe').show();
//			$('#htmlbody').hide();		
//		}
	}
}