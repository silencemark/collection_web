
var userid = "";
var obj = new Object();
var nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>还没有分享信息，赶紧添加一条吧~</span><b><img src="'+notDataImageUrl_havepower+'"></b></div>';
var userInfo = "";
$(function(){
	if(isiOS){
		$('.s_name').css({
			"margin-top" : "23px"
		});
	}
	
	//初始化信息
	JianKangCache.getGlobalData('userinfo',function(data){
		$('#change_organizename').text(data.companyname);
		
		userInfo = data;
		userid = data.userid;
		//查询分享信息
		queryShareInfo();
	});
	
	
	//清空未读数量
	var par = new Object();
	par.createid = userid;
	par.num = 0;
	$.ajax({
		url:projectpath+"/app/clearShareNotReadNum",
		type:"post",
		data:par,
		success:function(){}
	});
	
	//加载更多--分享信息
	PageHelper({
		url:projectpath+"/app/getShareList",
		data:obj,
		success:function(resultMap){
			if(resultMap != undefined && resultMap != null){
				if(resultMap.status == 0 || resultMap.status == '0'){
					var sharelist = resultMap.sharelist;
					showShareInfo(sharelist);
				}
			}
		}
	});
	
	//查询组织架构信息
	onloadSelect();
	
});


/*--------------------------------------------------------------------查询分享信息相关js--------------------------------------------------------------------*/

//查询分享信息
function queryShareInfo(){
	obj.companyid = userInfo.companyid;
	
	requestPost("/app/getShareList",obj,"list",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			$(".list_none").remove();
			if(resultMap.status == 0 || resultMap.status == '0'){
				var sharelist = resultMap.sharelist;
				showShareInfo(sharelist);
			}
		}else{
			$("#sharelist_ul").parent().after(nodata);
		}
	});
}

function allShare(){
	$('#fenxiang_div').show();
	$('#personal_div').hide();
	$('#personal_fenxiang').text("分享圈");
	
	$('#sharelist_ul').empty();
	
	obj.datacode = $('#datacode').val();
	obj.userid = "";
	queryShareInfo();
}

//查询评论者发布的分享信息
function personal(p_userid,commentname){
	$('#fenxiang_div').hide();
	$('#personal_div').show();
	$('#personal_fenxiang').text(commentname+"的分享圈");
	
	$('#sharelist_ul').empty();
	
	obj.userid = p_userid;
	obj.datacode = "";
	
	queryShareInfo();
}

//展示分享信息
function showShareInfo(sharelist){
	if(sharelist != null && sharelist.length > 0){
		$(".list_none").remove();
		//循环显示所有的评论
		var htm = "";
		$.each(sharelist,function(i,map){
			//得到评论相关的图片list，评论list，点赞人list
			var commentlist = map.commentlist;
			var imagelist = map.imagelist;
			var praiselist = map.praiselist;
			
			//循环显示所有的图片
			var imghtml = "";
			if(imagelist.length > 0 && imagelist != null){
				$.each(imagelist,function(g,imgmap){
					imghtml += '<span class=\"img_span_\"><img src="'+projectpath+imgmap.visiturl+'" onclick=\"showbigimg(this)\"></span>';
				});
			}
			//循环显示所有的评论信息
			commhtml = "";
			if(commentlist != null && commentlist.length > 0){
				$.each(commentlist,function(c,cmtmap){
					var reply = "";
					//判断是否是对某条评论信息的回复
					if(cmtmap.replyname != ""){
						reply = '&nbsp;回复&nbsp;<span>'+cmtmap.replyname+'</span>';
					}
					commhtml += '<p>'+
									'<span onclick="personal(\''+cmtmap.userid+'\',\''+cmtmap.createname+'\')">'+cmtmap.createname+'</span>'+reply+': '+
									'<font onclick="showcomment_div(\'huifu\',\''+map.shareid+'\',\''+cmtmap.commentid+'\','+
									'\''+cmtmap.userid+'\',\''+cmtmap.createname+'\')">'+cmtmap.content+'</font>'+
								'</p>';
				});
			}
			//判断当前用户是否点赞
			var isparise = 'class="ico_zan" onclick="addPraise(\''+map.shareid+'\',this)"';
			//循环显示所有点赞人名称
			parisehtml = "";
			if(praiselist != null && praiselist.length > 0){
				$.each(praiselist,function(p,praisemap){
					if(praisemap.createname != undefined){
						var dou = "，";
						if(p==0){dou = "";}
						parisehtml += '<font id="parseid_'+map.shareid+'_'+praisemap.createid+'" onclick="personal(\''+praisemap.createid+'\',\''+praisemap.createname+'\')">'+dou+praisemap.createname+'</font>';
						if(praisemap.createid == userid){
							isparise = 'class="ico_zaned" onclick="cancelPraise(\''+praisemap.praiseid+'\',this)"';
						}
					}
				});
			}
			htm += '<li>'+
						   '<div class="txt">'+
					        	'<div class="user_img" onclick="personal(\''+map.createid+'\',\''+map.createname+'\')"><img src="'+projectpath+map.headimage+'" style=\"min-height:38px;\"></div>'+
					            '<div class="name"><span>'+map.createname+'</span><i>'+appBase.parseDateMinute(map.createtime)+'</i></div>';
								if(map.content!=null && map.content!=undefined && map.content.length>123){
									htm+='<p>'+map.content.substring(0,120)+'...</p><a style=\"color:blue;\" onclick=\"showallcontent(this)\" content=\"'+map.content+'\">全文</a>';
								}else{
									htm+='<p>'+map.content+'</p>';
								}
					            htm+='<div class="img">'+imghtml+'<div class="clear"></div></div>'+
					            '<div class="ico_box"><a class="ico_pl" onclick="showcomment_div(\'pinglun\',\''+map.shareid+'\',\'\',\'\',\'\')">评论</a><a '+isparise+'>赞</a></div>'+
					        '</div>'+
					        '<div class="com_list">'+
					        	'<div class="zan_box" id="parise_list_'+map.shareid+'">'+parisehtml+'</div>'+
					            '<div class="li_list" id="comment_list_'+map.shareid+'">'+
					                commhtml+
					            '</div>'+
					        '</div>'+
					        '<div class="com_textbox" style="display:none;" id="comment_'+map.shareid+'">'+
				            	'<textarea class="text_area" placeholder="请输入评论信息" id="textarea_'+map.shareid+'"></textarea>'+
				                '<input type="button" class="btn" onclick="addComment(\''+map.shareid+'\')" value="评论">'+
				            	'<input type="button" class="btn" onclick="showcomment_div(\'\',\''+map.shareid+'\')" value="取消" style="background-color:gray; margin-right:20px;">'+
				                '<div class="clear"></div>'+
				            '</div>'+
					    '</li>';
		});
		$('#sharelist_ul').append(htm);
		var kuan=$(".img_span_").css("width");
		$(".img_span_").css("height",kuan);
		if((kuan+"").indexOf("px")>=0){
			kuan=kuan.replace("px","")*1;
		}
		//var img = new Image();
		$(".img_span_ img").each(function(i,item){
			//img.src =$(item).attr("src"); 
			//var w = img.width;
			var h = $(item)[0].height;
			if(h<kuan){
				$(item).attr("height",kuan);
				$(item).attr("width",kuan);
			}
		});
	}else{
		$("#sharelist_ul").parent().after(nodata);
	}
}

//全文
function showallcontent(obj){
	$(obj).prev("p").text($(obj).attr("content"));
	$(obj).text("收起");
	$(obj).attr("onclick","hideallcontent(this)");
}

//收起
function hideallcontent(obj){
	var content=$(obj).attr("content");
	if(content!=null && content!=undefined && content.length>123){
		content=content.substring(0,120);
	}
	$(obj).prev("p").text(content+'...');
	$(obj).text("全文");
	$(obj).attr("onclick","showallcontent(this)");
}

//点赞
function addPraise(shareid,obj){
	var param = new Object();
	param.shareid = shareid;
	param.createid = userid;
	param.ispraise = 1;
	
	praise(param,function(resultMap){
		if(resultMap.status == 0 || resultMap.status == '0'){
			$(obj).attr("class","ico_zaned");
			$(obj).attr("onclick","cancelPraise('"+resultMap.praiseid+"',this)");
			var pl = $('#parise_list_'+shareid).html();
			if(pl == ""){
				$('#parise_list_'+shareid).append('<font id="parseid_'+shareid+'_'+userid+'">'+userInfo.realname+'</font>');
			}else{
				$('#parise_list_'+shareid).append('<font id="parseid_'+shareid+'_'+userid+'">，'+userInfo.realname+'</font>');
			}
		}
	});
}
//取消点赞
function cancelPraise(praiseid,obj){
	var param = new Object();
	param.praiseid = praiseid;
	param.ispraise = 0;
	
	praise(param,function(resultMap){
		if(resultMap.status == 0 || resultMap.status == '0'){
			$(obj).attr("class","ico_zan");
			$(obj).attr("onclick","addPraise('"+resultMap.shareid+"',this)");
			$('#parseid_'+resultMap.shareid+'_'+userid).remove();
		}
	});
}

//点赞操作
function praise(param,callback){
	$.ajax({
		url:projectpath+"/app/insertPraise",
		type:"post",
		data:param,
		beforeSend:function(a,b){
			ajaxbefore();
		},
		complete:function(a,b){
			ajaxcomplete();
		},
		success:function(resultMap){
			callback(resultMap);
		}
	});
}

var param = new Object();
//评论div的开关
function showcomment_div(active,shareid,commentid,commentuserid,commentname){
	var dis = $('#comment_'+shareid).css("display");
	param = new Object();
	//判断评论框是否打开
	if(dis == "none"){
		if(active == "pinglun"){
			$('#textarea_'+shareid).attr("placeholder","请输入评论信息");
			param.resourceid = shareid;
		}else if(active == "huifu"){
			$('#textarea_'+shareid).attr("placeholder","回复："+commentname);
			param.resourceid = shareid;
			if(commentuserid != userid){
				param.parentid = commentid;
				param.commentname = commentname;
			}
		}
		$('#comment_'+shareid).show();
		//评论框获取焦点
		$('#textarea_'+shareid).focus();
	}else{
		
		$('#comment_'+shareid).hide();
	}
	
}

//添加评论信息
function addComment(shareid){
	param.content = $('#textarea_'+param.resourceid).val();
	param.userid = userid;
	param.resourcetype = 26;
	
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
		url:projectpath+"/app/insertShareComment",
		data:param,
		type:"post",
		beforeSend:function(a,b){
			ajaxbefore();
		},
		complete:function(a,b){
			ajaxcomplete();
		},
		success:function(resultMap){
			if(resultMap.status == 0 || resultMap.status == '0'){
				var reply = "";
				//判断是否是对某条评论信息的回复
				if(param.commentname != "" && param.commentname != undefined){
					reply = '&nbsp;回复&nbsp;<span>'+param.commentname+'</span>';
				}
				var comm = '<p>'+
								'<span onclick="personal(\''+userid+'\')">'+userInfo.realname+'</span>'+reply+': '+
								'<font onclick="showcomment_div(\'huifu\',\''+param.resourceid+'\',\''+resultMap.commentid+'\','+
								'\''+userInfo.userid+'\',\''+userInfo.realname+'\')">'+param.content+'</font>'+
							'</p>';
				$('#comment_list_'+param.resourceid).prepend(comm);
			}
			$('#textarea_'+param.resourceid).val("");
			showcomment_div('',param.resourceid);
		}
	});
}

//跳转到分享添加页面
function intoAddPage(){
	location.href='share_add.html?organizeid='+$('#organizeid').val()+"&pagetype=index";
}

/*----------------------------------------------------------------查询组织机构js------------------------------------------------------------------*/
var orgaidNum = 1;
function logical(){
	obj.datacode = $('#datacode').val();
	if(obj.datacode.length == 4){
		obj.datacode = "";
	}
	$('#change_organizename').text($('#organizename').val());
	$('#sharelist_ul').empty();
	queryShareInfo();
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
			JianKangCache.setData("share_",resultMap);
			callback(resultMap);
		},error:function(e){
			//取出缓存数据
			JianKangCache.getData("share_",function(resultMap){
				callback(resultMap);
			});
		},complete:function(){
			$('#jiazaizhong').hide();
		}
	});
}