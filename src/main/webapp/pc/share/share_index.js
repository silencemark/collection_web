

var userid = "";
var companyid = "";
var organizeid = "";
var realname = "";
var companyname = "";
var datacode = "";

var obj = new Object();

$(function(){
	//清空未读数量
	var par = new Object();
	par.createid = userid;
	par.num = 0;
	requestPost("/pc/clearShareNotReadNum",par,function(){});
	
	userid = $('#userid').val();
	companyid = $('#companyid').val();
	realname = $('#realname').val();
	companyname = $('#companyname').val();
	$('#changed_orgname').text(companyname);
	
	obj.companyid = companyid;
	//查询分享信息
	queryShareInfo();
	
	//加载更多--分享信息
	PageHelper({
		url:"/pc/getShareList",
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
	
});

//选择的组织的id
function callbackfunc1(orgid,orgname,datacode){
	$('#change_organizeid').val(orgid);
	$('#change_organizename').val(orgname);
	$('#change_datacode').val(datacode);
}

function changeshopp(){
	obj.datacode = $('#change_datacode').val();
	datacode = $('#change_datacode').val();
	if(obj.datacode.length == 4){
		obj.datacode = "";
	}
	$('#changed_orgname').text($('#change_organizename').val());
	
	$('#sharelist_ul').empty();
	queryShareInfo();
	close_open_organizeDiv();
}

function deleteImage(obj){
	$(obj).parent().remove();
}
/*------------------------------------------------------------------------添加分享信息---------------------------------------------------------------------------------*/

function addShare(){
	var imgs = $('#img_list').find('img');
	var imgurls = "";
	$.each(imgs,function(i,item){
		imgurls += $(item).attr("url")+",";
	});
	var param = new Object();
	param.content = $('#content').html();
	param.images = imgurls;
	param.createid = userid;
	param.companyid = companyid;
	
	if(param.content == ""){
		swal({
			title : "",
			text : "分享信息不能为空！",
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
		url:"/pc/insertShare",
		type:"post",
		data:param,
		success:function(resultMap){
			if(resultMap.status == 0 || resultMap.status == '0'){
				swal({
					title : "",
					text : "分享成功！",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.reload();
				});
			}
		}
	});
}

/*----------------------------------------------------------------查询组织机构js------------------------------------------------------------------*/

$.ajax({
	type:"post",
	dataType:"json",
	url:"/pc/getPCOrganize",
	success:function(data){
		if(data.status == 0){
			var organizelist = data.organizelist;
			showOrganize2(organizelist,data.compandyname);
		}
	}
})
var initorganizeid2="";
function showOrganize2(list,compandyname){
	if(list.length > 0){
		for(var i=0;i<list.length;i++){
			if(list[i].parentid=="" || typeof(list[i].parentid)=="undefined"){
				var temp="<ul class=\"tree_list\">"+
            	"<li id=\"li2_"+list[i].organizeid+"\">"+                    	                    	
            		"<div class=\"gray_line\"></div>"+
                	"<span class=\"bg_show\" onclick=\"changenextul2(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"','"+list[i].organizename+"','"+list[i].datacode+"')\">"+
					"<i id=\"companyname\">"+list[i].organizename+"</i></span>"+
                "</li>"+
                "</ul>";
				initorganizeid2=list[i].organizeid;
				$("#organizetree2").html(temp);
				appendli2(list,list[i].organizeid);
			}
		}
	}
}

function appendli2(list,organizeid){
	var indexnum=0;
	for(var i=0;i<list.length;i++){
		if(list[i].parentid==organizeid){
			var ultemp="";
			if(organizeid==initorganizeid2){
				ultemp+="<ul style=\"display:block;\" id=\"ul2_"+list[i].parentid+"\"></ul>";
			}else{
				ultemp+="<ul style=\"display:none;\" id=\"ul2_"+list[i].parentid+"\"></ul>";
			}
			
			var temp="<li class=\"li_bg\" id=\"li2_"+list[i].organizeid+"\">";
			indexnum++;
			if(list[i].childnum>0){
				temp+="<div class=\"gray_line\" style=\"display:none\"></div><span class=\"bg_hidden\" onclick=\"changenextul2(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"','"+list[i].organizename+"','"+list[i].datacode+"')\">";
				
			}else{
				temp+="<span class=\"bg_last\" onclick=\"changenextul2(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"','"+list[i].organizename+"','"+list[i].datacode+"')\">";
			}
            temp+="<i id=\"companyname\">"+list[i].organizename+"</i></span></li>";

            if($("#ul2_"+list[i].parentid).length>0){
            	$("#ul2_"+list[i].parentid).append(temp);
			}else{
				$("#li2_"+organizeid).append(ultemp);
				$("#ul2_"+list[i].parentid).append(temp);
			}
			$("ul li").find("div[class=white_line]").remove();
			$("ul li:last-child").append("<div class=\"white_line\" name=\"white_box\">");
			
			appendli2(list,list[i].organizeid);
			
			
		}
	}
	var whiteHeight=0;
	
}

function changenextul2(obj,isonlyread,organizeid,organizename,datacode){
	if(isonlyread != '1'){
		//回调查询
		callbackfunc1(organizeid,organizename,datacode);
		$('#organizetree2').find("i").attr("class","");
		$(obj).find("i").attr("class","bg_yellow");
	}
	if($(obj).attr("class")=="bg_hidden"){
		$(obj).prev().show();
		$(obj).attr("class","bg_show");
		$(obj).nextAll("ul").show();
		changeheight2();
	}else if($(obj).attr("class")=="bg_show"){
		$(obj).attr("class","bg_hidden");
		$(obj).nextAll("ul").hide();
		$(obj).prev().hide();
		changeheight2();
	}
}
function changeheight2(){
	var whiteHeight=0;
	$(".tree_list .white_line").each(function() {	
		whiteHeight = $(this).parent().height();
		whiteHeight = whiteHeight - 21 ;
	    $(this).height(whiteHeight) ;
	});
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

/*--------------------------------------------------------------------查询分享信息相关js--------------------------------------------------------------------*/


//查询分享信息
function queryShareInfo(){
	requestPost("/pc/getShareList",obj,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == 0 || resultMap.status == '0'){
				var sharelist = resultMap.sharelist;
				showShareInfo(sharelist);
			}
		}
	});
}

function allShare(){
	$('#fenxiang_div').show();
	$('#personal_div').hide();
	$('#personal_fenxiang').text("分享圈");
	
	$('#sharelist_ul').empty();
	
	obj.userid = "";
	obj.companyid = companyid;
	obj.datacode = datacode;
	
	queryShareInfo();
}

//查询评论者发布的分享信息
function personal(p_userid,commentname){
	$('#fenxiang_div').hide();
	$('#personal_div').show();
	$('#personal_fenxiang').text(commentname+"的分享圈");
	
	$('#sharelist_ul').empty();
	
	obj.userid = p_userid;
	obj.companyid = "";
	obj.datacode = "";
	queryShareInfo();
}

//展示分享信息
function showShareInfo(sharelist){
	if(sharelist != null && sharelist.length > 0){
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
					imghtml += '<span><img src="'+projectpath+imgmap.visiturl+'"></span>';
				});
				imghtml += '<div class="clear"></div>';
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
									'<span style="cursor: pointer;" onclick="personal(\''+cmtmap.userid+'\',\''+cmtmap.createname+'\')">'+cmtmap.createname+'</span>'+reply+': '+
									'<font style="cursor: pointer;" onclick="showcomment_div(\'huifu\',\''+map.shareid+'\',\''+cmtmap.commentid+'\','+
									'\''+cmtmap.userid+'\',\''+cmtmap.createname+'\')">'+cmtmap.content+'</font>'+
								'</p>';
				});
			}
			//判断当前用户是否点赞
			var isparise = 'class="ico_zan" onclick="addPraise(\''+map.shareid+'\',this)"';
			//循环显示所有点赞人名称
			parisehtml = "";
			var ki = 0;
			if(praiselist != null && praiselist.length > 0){
				$.each(praiselist,function(p,praisemap){
					if(praisemap.createname != undefined){
						var dou = "，";
						if(ki==0){dou = "";}
						parisehtml += '<font style="cursor: pointer;"  onclick="personal(\''+praisemap.createid+'\',\''+praisemap.createname+'\')" id="parseid_'+map.shareid+'_'+praisemap.createid+'">'+dou+praisemap.createname+'</font>';
						if(praisemap.createid == userid){
							isparise = 'class="ico_zaned" onclick="cancelPraise(\''+praisemap.praiseid+'\',this)"';
						}
						ki=1;
					}
				});
			}
			htm += '<li>'+
						   '<div class="txt">'+
					        	'<div style="cursor: pointer;" class="user_img" onclick="personal(\''+map.createid+'\',\''+map.createname+'\')"><img src="'+projectpath+map.headimage+'" width="52" height="52"></div>'+
					            '<div class="name"><span>'+map.createname+'</span><i>'+appBase.parseDateMinute(map.createtime)+'</i></div>'+
					            '<p>'+map.content+'</p>'+
					            '<div class="img">'+imghtml+'</div>'+
					            '<div class="ico_box"><a class="ico_pl" href="javascript:void(0)" onclick="showcomment_div(\'pinglun\',\''+map.shareid+'\',\'\',\'\',\'\')">评论</a><a href="javascript:void(0)" '+isparise+'>赞</a></div>'+
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
	}
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
				$('#parise_list_'+shareid).append('<font style="cursor: pointer;" id="parseid_'+shareid+'_'+userid+'">'+realname+'</font>');
			}else{
				$('#parise_list_'+shareid).append('<font style="cursor: pointer;" id="parseid_'+shareid+'_'+userid+'">，'+realname+'</font>');
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
		url:"/pc/insertPraise",
		type:"post",
		data:param,
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
	}else{
		
		$('#comment_'+shareid).hide();
	}
	
}

//添加评论信息
function addComment(){
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
		url:"/pc/insertShareComment",
		data:param,
		type:"post",
		success:function(resultMap){
			if(resultMap.status == 0 || resultMap.status == '0'){
				var reply = "";
				//判断是否是对某条评论信息的回复
				if(param.commentname != "" && param.commentname != undefined){
					reply = '&nbsp;回复&nbsp;<span>'+param.commentname+'</span>';
				}
				var comm = '<p>'+
								'<span style="cursor: pointer;" onclick="personal(\''+userid+'\')">'+realname+'</span>'+reply+': '+
								'<font style="cursor: pointer;" onclick="showcomment_div(\'huifu\',\''+param.resourceid+'\',\''+resultMap.commentid+'\','+
								'\''+userid+'\',\''+realname+'\')">'+param.content+'</font>'+
							'</p>';
				$('#comment_list_'+param.resourceid).prepend(comm);
			}
			$('#textarea_'+param.resourceid).val("");
			showcomment_div('',param.resourceid);
		}
	});
}


