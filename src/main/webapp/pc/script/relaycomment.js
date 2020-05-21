var forwardhtml = '<div style="display:none; position: fixed;left: 0px;top: 0px;width: 100%;height: 100%;_height: 0px;z-index: 501;background: #000;FILTER: alpha(opacity:60);opacity: 0.6;" id="forward_mask_div"></div>'+
					'<div class="tc_selrange" style="display:none;" id="forwardSelrangeDiv">'+
						'<div class="tc_title"><span>选择转发人</span><a href="javascript:void(0);" onclick="$(\'#forwardSelrangeDiv\').hide();$(\'#forward_mask_div\').hide();">×</a></div>'+
						'<div class="range">'+
							'<div class="l_box" id="forwardOrganizeTree" style="overflow:hidden;overflow-y:visible;">'+
							
						    '</div>'+
						    '<div class="r_box">'+
						    	'<div class="sel_xx">'+
						            '<div class="sel_box">'+
						                '<input type="text" class="text" placeholder="请输入姓名" id="searchForwardRealname"/>'+
						                '<input type="button" class="find_btn" value="" onclick="searchForwardUser()" />'+
						                '<div class="clear"></div>'+
						            '</div>'+
						            '<div class="sel_bo" style="padding:5px 0px;">'+
						                '<textarea class="text" placeholder="请输入评论信息" id="comment_content" style="float:left; width:290px; height:30px; padding-left:12px; border:1px solid #eee; margin-right:20px;"></textarea>'+
						                '<div class="clear"></div>'+
						            '</div>'+
						            '<input type="hidden" id="forwardUserList"/>'+
						            '<ul id="forwardRangeUl">'+
						            '</ul>'+
						        '</div>'+
						        '<div class="tab_list">'+
						        	'<table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">'+
						            	'<tr class="head_td">'+
						                	'<td width="30">&nbsp;</td>'+
						                    '<td width="70">姓名</td>'+
						                    '<td width="70">性别</td>'+
						                    '<td width="130">电话</td>'+
						                    '<td>操作</td>'+
						                '</tr>'+
						                '<tbody id="forwardUserTable"><tbody>'+
						            '</table>'+
						        '</div>'+
						        '<div id="Pagination" style="width:450px;"><div id="forwardPagination"></div></div><!--动态的获取pagination的宽度赋值给Pagination-->'+
						        '<div class="tc_btnbox"><a  href="javascript:void(0);" onclick="$(\'#forward_mask_div\').hide();$(\'#forwardSelrangeDiv\').hide();"  class="bg_gay2">取消</a>'+
						        '<a href="javascript:void(0)" onclick="submitForwardUser()"  class="bg_yellow">确定</a>'+
						        '</div>'+
						    '</div>'+
						    '<div class="clear"></div>'+
						'</div>'+
					 '</div>';

$(function(){
	$('body').after(forwardhtml);
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/pc/getPCOrganize",
		success:function(data){
			if(data.status == 0){
				var organizelist = data.organizelist;
				showForwardOrganize(organizelist);
			}
		}
	})
});

function showForwardDiv(){
	$('#forward_mask_div').show();$('#forwardSelrangeDiv').show();
}

var initForwardOrganize="";
function showForwardOrganize(list){
	if(list.length > 0){
		for(var i=0;i<list.length;i++){
			if(list[i].parentid=="" || typeof(list[i].parentid)=="undefined"){
				var temp="<ul class=\"tree_list\">"+
            	"<li id=\"forward_li2_"+list[i].organizeid+"\">"+                    	                    	
            		"<div class=\"gray_line\"></div>"+
                	"<span class=\"bg_show\" onclick=\"changeForwardOrganize(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">"+
					"<i id=\"companyname\">"+list[i].organizename+"</i></span>"+
                "</li>"+
                "</ul>";
				initForwardOrganize=list[i].organizeid;
				$("#forwardOrganizeTree").html(temp);
				appendNextForwardOrganize(list,list[i].organizeid);
			}
		}
	}
}

function appendNextForwardOrganize(list,organizeid){
	var indexnum=0;
	for(var i=0;i<list.length;i++){
		if(list[i].parentid==organizeid){
			var ultemp="";
			if(organizeid==initForwardOrganize){
				ultemp+="<ul style=\"display:block;\" id=\"forward_ul2_"+list[i].parentid+"\"></ul>";
			}else{
				ultemp+="<ul style=\"display:none;\" id=\"forward_ul2_"+list[i].parentid+"\"></ul>";
			}
			
			var temp="<li class=\"li_bg\" id=\"forward_li2_"+list[i].organizeid+"\">";
			indexnum++;
			if(list[i].childnum>0){
				temp+="<div class=\"gray_line\" style=\"display:none\"></div><span class=\"bg_hidden\" onclick=\"changeForwardOrganize(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">";
				
			}else{
				temp+="<span class=\"bg_last\" onclick=\"changeForwardOrganize(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">";
			}
            temp+="<i id=\"companyname\">"+list[i].organizename+"</i></span></li>";

            if($("#forward_ul2_"+list[i].parentid).length>0){
            	$("#forward_ul2_"+list[i].parentid).append(temp);
			}else{
				$("#forward_li2_"+organizeid).append(ultemp);
				$("#forward_ul2_"+list[i].parentid).append(temp);
			}
			$("ul li").find("div[class=white_line]").remove();
			$("ul li:last-child").append("<div class=\"white_line\" name=\"white_box\">");
			
			appendNextForwardOrganize(list,list[i].organizeid);
			
			
		}
	}
	var whiteHeight=0;
	
}

function deleteForwardUser(obj){
	swal({
		title : "",
		text : "是否删除？",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$(obj).parent().parent().remove();
	});
}
function changeForwardOrganize(obj,isonlyread,organizeid){
	if(isonlyread != '1'){
		//回调查询
		queryForwardUserList(organizeid);
		$('#forwardOrganizeTree').find("i").attr("class","");
		$(obj).find("i").attr("class","bg_yellow");
	}
	if($(obj).attr("class")=="bg_hidden"){
		$(obj).prev().show();
		$(obj).attr("class","bg_show");
		$(obj).nextAll("ul").show();
		forward_changeHeight();
	}else if($(obj).attr("class")=="bg_show"){
		$(obj).attr("class","bg_hidden");
		$(obj).nextAll("ul").hide();
		$(obj).prev().hide();
		forward_changeHeight();
	}
}
function forward_changeHeight(){
	var whiteHeight=0;
	$(".tree_list .white_line").each(function() {	
		whiteHeight = $(this).parent().height();
		whiteHeight = whiteHeight - 21 ;
	    $(this).height(whiteHeight) ;
	});
}

var forwardParam = new Object();
forwardParam.rowPage = 4;

function pageHelper3(num){
	forwardParam.currentPage = num;
	if(forwardParam.searchrealname == "" && forwardParam.organizeid != ""){
		queryForwardUserList("");
	}else if(forwardParam.searchrealname != "" && forwardParam.organizeid == ""){
		searchForwardUser("search");
	}
}

function queryForwardUserList(organizeid){
	if(organizeid != ""){
		forwardParam.currentPage = 1;
		forwardParam.organizeid = organizeid;
		forwardParam.searchrealname = "";
	}
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/userbackstage/getUserByOrganize",
		data:forwardParam,
		success:function(data){
			showFowardUserListInfo(data);
		}
	})
}
function searchForwardUser(type){
	if(type != "search"){
		var searchrealname=$('#searchForwardRealname').val();
		if(searchrealname == ""){
			swal("","请输入用户名称···","warning");
			return false;
		}
		forwardParam.currentPage = 1;
		forwardParam.organizeid = "";
		forwardParam.searchrealname = searchrealname;
	}
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/userbackstage/getSearchUserByName",
		data:forwardParam,
		success:function(data){
			showFowardUserListInfo(data);
		}
	})
}

function showFowardUserListInfo(data){
	$('#forwardPagination').html(data.pager3);
	$('#forwardUserTable').empty();
	if(data.userlist.length>0){
		var temp="";
		for(var i=0;i<data.userlist.length;i++){
			temp+="<tr><td class=\"img_td\"><div class=\"img\"><img src=\""+projectpath+data.userlist[i].headimage+"\" width=\"30\" height=\"30\" /></div></td>"+
                "<td>"+data.userlist[i].realname+"</td>";
                if(data.userlist[i].sex==1){
               		temp+="<td>男</td>";
                }else{
                	temp+="<td>女</td>";
                }
                if(data.userlist[i].isshowphone==1){
                	 temp+="<td>"+data.userlist[i].phone+"</td>";
                }else{
                	var phone=data.userlist[i].phone;
                	temp+="<td>"+phone.substring(0,3)+"*****"+phone.substring(7,phone.length)+"</td>";
                }
            if(data.userlist[i].userid != relayiduserid){
                temp+="<td><a href=\"javascript:void(0)\" onclick=\"addForwardUser(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"','"+data.userlist[i].headimage+"')\" class=\"link\">添加</a></td></tr>";
			}
		}
		$('#forwardUserTable').append(temp);
	}
}

function addForwardUser(obj,userid,realname,headimage){
	
	$('#forwardRangeUl').find("input[value="+userid+"]").parent().remove();
	var temp="<li><input type=\"hidden\" name=\"userid\" value=\""+userid+"\" realname='"+realname+"'/>"+realname+"<a class=\"del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\" onclick=\"deleteForwardUser(this)\" /></a></li>";
	$('#forwardRangeUl').append(temp);
}

//点击确定
function submitForwardUser(){
	var ids="";
	var forwardnames = "";
	$('#forwardRangeUl input[name=userid]').each(function(index){
		ids+=$(this).val()+",";
		forwardnames += $(this).attr("realname")+"，";
	});
	if(ids==""){
		swal({
			title : "",
			text : "转发人不能为空",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			closeOnConfirm : true
		}, function(){
			
		}); 
		return;
	}
	ids = ids.substring(0,(ids.length - 1));
	forwardnames = forwardnames.substring(0,(forwardnames.length - 1));
	
	$('#forward_mask_div').hide();
	$('#forwardSelrangeDiv').hide();
	swal({
		title : "",
		text : "确定要转发给："+forwardnames+" 吗？",
		type : "success",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		userForword(relayidcompanyid,relayid,ids,relayiduserid,relaytype);
		var param = new Object();
		param.userid = relayiduserid;
		param.resourceid =relayid;
		param.resourcetype =relaytype;
		param.type = 2;
		param.forwardusernames = forwardnames;
		param.forwarduserids = ids;
		showCommentpc(param,"");
	}); 
}