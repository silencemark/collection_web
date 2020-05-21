/**
 * 	消息详情列表的显示
 */
function msgdetail_data_show(){
	//获取参数
	var type=$("#type").val();
	if(type.trim().length<=0){
		$("#msg_list_ul").html("<li>暂无数据</li>");
		return;
	}
	var userid=$("#userid").val();
	if(userid.trim().length<=0){
		$("#msg_list_ul").html("<li>暂无数据</li>");
		return;
	}
	//ajax请求
	$.ajax({
		url:'/interface/getMessageList',
		type:'post',
		dataType:'json',
		data:'type='+type+'&userid='+userid,
		success:function(data){
			if(data!=null && data.data!=null && data.data!='' && data.data.length>0){
				var leng=data.data.length;
				var str="";
				for(var i=0;i<leng;i++){
			        str+="<li class='msgdetail_li' onclick='click_li(this)' type='hide' messageid='"+data.data[i].messageid+"' messageurl='"+data.data[i].messageurl+"'>";
			        if(data.data[i].status==0){
			        	str+="<div class='state_point'>未读</div>";
			        }
			        str+="<div class='name' onclick='click_title(this)' type='hide' messageid='"+data.data[i].messageid+"'><span>"+data.data[i].title+"</span><i>"+data.data[i].createtime+"</i></div>";
			        str+="<div class='txt msgdetail_div_"+data.data[i].messageid+"' style='width:auto;height:24px;line-height:24px; font-size:12px;overflow:hidden;text-overflow:ellipsis;'>"+data.data[i].content+"</div>";
			        str+="</li>";
				}
				if(str.length>0){
					$("#msg_list_ul").html(str);
				}else{
					$("#msg_list_ul").html("<li>暂无数据</li>");
				}
			}else{
				$("#msg_list_ul").html("<li>暂无数据</li>");
			}
		},
		error:function(){
			$("#msg_list_ul").html("<li>暂无数据</li>");
		}
	});
}

/**
 * 	点击li
 */
function click_li(id){
	var messageid=$(id).attr("messageid");
	if(messageid.trim().length>0){
		//ajax标记当前消息为已读
		var bool=false;
		$.ajax({
			url:'/interface/updatemsgstatus',
			type:'post',
			dataType:'json',
			async: false,
			data:'messageid='+messageid,
			success:function(data){
				if(data!=null && data.status!=null && data.status==0){
					bool=true;
				}
			},
			error:function(){
				swal("","请求错误");
			}
		});
		if(bool){
			$(id).children(".state_point").remove();
			$(".txt").css("height","24px");
			var type=$(id).attr("type");
			var messageurl=$(id).attr("messageurl");
			if(type=='hide'){
				$(id).attr("type","show");
				$(".msgdetail_div_"+messageid).css("height","auto");
				$(".msgdetail_div_"+messageid).attr("onclick",messageurl!=null && messageurl!='' && messageurl!='undefined'?('location.href=\"'+messageurl+'\"'):'');
			}
			$(".name").attr("type","hide");
			$(id).children(".name").attr("type","show");
			$(id).attr("class","");
		}
	}
}

/**
 * 	点击标题
 * @param id
 */
function click_title(id){
	var type=$(id).attr("type");
	var messageid=$(id).attr("messageid");
	if(type=='show'){
		$(id).parent("li").attr("class","msgdetail_li");
		$(id).parent("li").attr("type","hide");
		$(id).attr("type","hide");
		$(".msgdetail_div_"+messageid).attr("onclick","");
		$(".msgdetail_div_"+messageid).css("height","24px");
	}
}

/**
 * 	选中每条消息
 */
function click_msg(id){
	if($(id).children("a").attr("class")=="radio"){
		$(id).children("a").attr("class","radio_ed");
	}else{
		$(id).children("a").attr("class","radio");
	}
}

$(function(){
	//滑到底部自动分页
	PageHelper({
		url:"/interface/getMessageList",
		data:{type:$("#type").val(),userid:$("#userid").val()},
		pageNo:2,
		pageSize:10,
		success:function(data){
			if(data!=null && data.data!=null && data.data!='' && data.data.length>0){
				var leng=data.data.length;
				var str="";
				for(var i=0;i<leng;i++){
					
					if($(".edit").attr("onclick")=="edit_hide()"){
						str+="<li class='msgdetail_edit_li' onclick='click_msg(this)' type='hide' messageid='"+data.data[i].messageid+"' messageurl='"+data.data[i].messageurl+"'>";
						str+="<a class='radio'>选择</a>";
				        if(data.data[i].status==0){
				        	str+="<div class='state_point' style='display:none;'>未读</div>";
				        }
					}else{
						str+="<li class='msgdetail_li' onclick='click_li(this)' type='hide' messageid='"+data.data[i].messageid+"' messageurl='"+data.data[i].messageurl+"'>";
				        if(data.data[i].status==0){
				        	str+="<div class='state_point'>未读</div>";
				        }
					}
			        str+="<div class='name' onclick='click_title(this)' type='hide' messageid='"+data.data[i].messageid+"'><span>"+data.data[i].title+"</span><i>"+data.data[i].createtime+"</i></div>";
			        str+="<div class='txt msgdetail_div_"+data.data[i].messageid+"' style='width: auto; height: 24px; line-height: 24px; font-size: 12px; overflow: hidden; text-overflow: ellipsis;'>"+data.data[i].content+"</div>";
			        str+="</li>";
				}
				if(str.length>0){
					$("#msg_list_ul").append(str);
				}
			}
		}
	});
});

/**
 * 	批量已读
 */
function pl_read(){
	var str="";
	$.each($(".radio_ed"),function(i,n){
		
		str+=($(n).parent("li").attr("messageid")!=null && $(n).parent("li").attr("messageid")!='undefined' && $(n).parent("li").attr("messageid")!=''?$(n).parent("li").attr("messageid"):'')+",";
	});
	if(str.trim().length<=1){
		swal("","请选择需要操作的消息");
		return;
	}
	$.ajax({
		url:'/interface/updatemsgstatuspl',
		type:'post',
		dataType:'json',
		data:'msgstr='+str+'&method=update',
		success:function(data){
			if(data!=null && data.status!=null && data.status=="0"){
				location.href="/weixin/companymsg/companymessagedetail?type="+$("#type").val();
			}else{
				swal("","操作失败");
			}
		},
		error:function(){
			swal("","请求错误");
		}
	});
}

/**
 * 	批量删除
 */
function pl_delete(){
	var str="";
	$.each($(".radio_ed"),function(i,n){
		
		str+=($(n).parent("li").attr("messageid")!=null && $(n).parent("li").attr("messageid")!='undefined' && $(n).parent("li").attr("messageid")!=''?$(n).parent("li").attr("messageid"):'')+",";
	});
	if(str.trim().length<=1){
		swal("","请选择需要操作的消息");
		return;
	}
	swal({
		  title : "",
			text : "是否确认删除所选消息？",
			type : "",
			showCancelButton : true,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$.ajax({
				url:'/interface/updatemsgstatuspl',
				type:'post',
				dataType:'json',
				data:'msgstr='+str+'&method=delete',
				success:function(data){
					if(data!=null && data.status!=null && data.status=="0"){
						location.href="/weixin/companymsg/companymessagedetail?type="+$("#type").val();
					}else{
						swal("","删除失败");
					}
				},
				error:function(){
					swal("","请求错误");
				}
			});
		});	
}

/**
 * 	点击编辑按钮 修改本页面为编辑页面
 */
function edit_show(){
	if($("#msg_list_ul .msgdetail_li").length<=0){
		return;
	}
	
	$(".edit").attr("onclick","edit_hide()");
	$(".edit").html("取消");
	$(".txt").attr("onclick","");
	$(".txt").css("height","24px");
	$(".name").attr("type","hide");
	$("#msg_list_ul li").attr("type","hide");
	$("#msg_list_ul li").attr("onclick","click_msg(this)");
	$("#msg_list_ul li").prepend("<a class='radio'>选择</a>");
	$(".state_point").hide();
	$("#msg_list_ul li").attr("class","msgdetail_edit_li");
	$("#buttom_span").hide();
	
	var str="<div class='msg_none'></div>";
	str+="<div class='msg_btbox'>";
	str+="<a class='link_a' onclick='pl_read()'>已读</a>";
	str+="<a class='link_b' onclick='pl_delete()'>删除</a>";
	str+="</div>";
	$("body").append(str);
}

/**
 * 	恢复编辑页面为数据页面
 */
function edit_hide(){
	$(".edit").attr("onclick","edit_show()");
	$(".edit").html("<img src='/appcssjs/images/public/t_edit.png'>");
	$(".msgdetail_edit_li").children("a").remove();
	$(".state_point").show();
	$(".msgdetail_edit_li").attr("class","msgdetail_li");
	
	$(".msg_none").remove();
	$(".msg_btbox").remove();

	$("#buttom_span").show();
}