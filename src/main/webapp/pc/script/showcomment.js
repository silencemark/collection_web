//评论分页
function pageList(param){
	$.ajax({
 		type:'post',
 		url:'/pc/getOrderComment',
 		data:param,
 		success:function(data){
 			if(data.status==1){
 				swal({
 					title : "",
 					text : "查询失败",
 					type : "error",
 					showCancelButton : false,
 					confirmButtonColor : "#ff7922",
 					confirmButtonText : "确认",
 					cancelButtonText : "取消",
 					closeOnConfirm : true
 				}, function(){
 					
 				});
 			}else{
 				$("#ul_list").text("");
 				if(data.commentlist!=null){
					var temp = "";
					for(var i=0;i<data.commentlist.length;i++){
						temp+="<li >"+
			            	"<div class=\"user\" onclick=\"comment_opent_close('"+data.commentlist[i].commentid+"','"+data.commentlist[i].userid+"',this)\"><b><img src=\""+data.commentlist[i].headimage+"\" width=\"30\" height=\"30\" /></b>";
			            	if(data.commentlist[i].type == "2"){
			            		temp+= "<span>"+data.commentlist[i].realname+"</span><i>转发</i><em>"+data.commentlist[i].forwardusernames+"</em></div>";
			            	}else{
			            		if(data.commentlist[i].replyname==null || data.commentlist[i].replyname==""){
				            		temp+= "<span>"+data.commentlist[i].realname+"</span></div>";
				            	}else{
				            		temp+= "<span>"+data.commentlist[i].realname+"</span><i>回复</i><em style='color:#666;'>"+data.commentlist[i].replyname+"</em></div>";
				            	}
			            	}
			            	temp+=  "<div class=\"time\">"+data.commentlist[i].createtime+"</div>"+
					                "<div class=\"clear\"></div>"+
					                "<div class=\"txt\">"+data.commentlist[i].content+"</div>"+
					                "<div class=\"clear\"></div>"+
					           		"</li>";
					}
					$("#ul_list").html(temp);
					$('#Pagination').html(data.pager);
				}
 			}
 		}
 })
}
//传入的参数：orderid resourcetype类型
function addcommentAjax(param){
	$.ajax({
		type:'post',
		url:'/pc/getOrderComment',
		data:param,
		success:function(data){
			if(data.status==1){
				swal({
					title : "",
					text : "查询失败",
					type : "error",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					
				});
			}else{
				if(data.num>0){
					$("#num").text(data.num);
				}else{
					$("#num").text('0');
				}

				if(data.commentlist!=null){
					var temp = "";
					for(var i=0;i<data.commentlist.length;i++){
						temp+="<li >"+
							"<div class=\"user\" onclick=\"comment_opent_close('"+data.commentlist[i].commentid+"','"+data.commentlist[i].userid+"',this)\"><b><img src=\""+data.commentlist[i].headimage+"\" width=\"30\" height=\"30\" /></b>";
			            	if(data.commentlist[i].type == "2"){
			            		temp+= "<span>"+data.commentlist[i].realname+"</span><i>转发</i><em>"+data.commentlist[i].forwardusernames+"</em></div>";
			            	}else{
			            		if(data.commentlist[i].replyname==null || data.commentlist[i].replyname==""){
				            		temp+= "<span>"+data.commentlist[i].realname+"</span></div>";
				            	}else{
				            		temp+= "<span>"+data.commentlist[i].realname+"</span><i>回复</i><em style='color:#666;'>"+data.commentlist[i].replyname+"</em></div>";
				            	}
			            	}
			            	temp+=  "<div class=\"time\">"+data.commentlist[i].createtime+"</div>"+
					                "<div class=\"clear\"></div>"+
					                "<div class=\"txt\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+data.commentlist[i].content+"</div>"+
					                "<div class=\"clear\"></div>"+
					           		"</li>";
					}
					$("#ul_list").html(temp);
					$('#Pagination').html(data.pager);
				}
			}
		}
	});
}
//评论框的开关
function comment_opent_close(parentid,parentuserid,obj){
		$('#parentid').val(parentid);
		$('#parentuserid').val(parentuserid);
		$("#ul_list div[name=contentBox]").remove();
		$(obj).parent().append(" <div class=\"text_box\"  style=\"margin-left:160px;\" name=\"contentBox\">"+
						"<b><textarea placeholder=\"请输入回复信息...\" maxlength=\"200\" id=\"content_text2\"></textarea></b>"+
					    "<span><input style='background-color:gray; margin-right:10px;' type=\"button\" value=\"取消\"  onclick=\"$(this).parent().parent().remove();\"/><input type=\"button\" value=\"回复\"  onclick=\"checkComment(2)\"/></span>"+
						"</div>");
}
	


//添加评论信息
function showComment(row,param,url){
	if(row==1){
		param.content=$("#content_text1").val();
	}else{
		param.content=$("#content_text2").val();
	}

	param.type = 1;
	param.forwardusernames = "";
	param.forwarduserids = "";
	
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
		url:"/pc/insertComment",
		data:param,
		type:"post",
		success:function(resultMap){
				param.parentid = $('#parentid').val();
				comment_tishi(resultMap,param,url);
				//给所有的转发者推送信息
				changeIsReadAll(param.userid,param.resourceid,param.resourcetype);
		}
	});
}

//pc端转发评论
function showCommentpc(param,url){
	param.content=$("#comment_content").val();

	if(param.content == ""){
		
		return false;
	}
	$.ajax({
		url:"/pc/insertComment",
		data:param,
		type:"post",
		success:function(resultMap){
				param.parentid = $('#parentid').val();
				comment_tishipc(resultMap,param,url);
				
				//给所有的转发者推送信息
				changeIsReadAll(param.userid,param.resourceid,param.resourcetype);
				
		}
	});
	
}

function comment_tishipc(resultMap,param,url){
	changeIsReadAll(param.userid,param.resourceid,param.resourcetype);
	if(resultMap.status == 0 || resultMap.status == '0'){
		if(param.parentid!=""){
			changeIsread($('#parentuserid').val(),param.resourceid);
		}else{
			if(param.userid != $('#parentuserid').val()){
				changeIsread($('#parentuserid').val(),param.resourceid);//判断是否属于他自己
			}
			
		}
		
		location.reload();
	}
}

function comment_tishi(resultMap,param,url){
	changeIsReadAll(param.userid,param.resourceid,param.resourcetype);
	if(resultMap.status == 0 || resultMap.status == '0'){
		if(param.parentid!=""){
			changeIsread($('#parentuserid').val(),param.resourceid);
		}else{
			if(param.userid != $('#parentuserid').val()){
				changeIsread($('#parentuserid').val(),param.resourceid);//判断是否属于他自己
			}
			
		}
		swal({
			title : "",
			text : "操作成功",
			type : "success",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
/*			if(url != ""){
				location.href=url;//刷新当前详情页面
			}else{*/
				location.reload();
			//}
		});
	}
}

function changeIsread(receiveid,resourceid){
	$.ajax({
		type:'post',
		dataType:'json',
		url:'/pc/updateIsread?receiveid='+receiveid+'&isread=0&resourceid='+resourceid,
		success:function(data){
			
		}
	})
}