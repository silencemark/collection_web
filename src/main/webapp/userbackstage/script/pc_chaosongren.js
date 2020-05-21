$(function(){
	 var chaosongren_html = '<link type="text/css" rel="stylesheet" href="/userbackstage/style/chaosongren.css">'+
		'<div class="tc_chaosongren" style="display:none">'+
		'<div class="tc_title"><span>抄送人</span><a href="javascript:void(0);" onclick="$(\'.tc_chaosongren\').hide();$(\'.div_mask\').hide();">×</a></div>'+
	    '<div class="range">'+
	    	'<div class="l_box" id="chaosongren_organize" style="width:280px;overflow:hidden;overflow-y:visible;">'+
	        '</div>'+
	        '<div class="r_box" style="width:440px;overflow:hidden;overflow-y:visible;">'+
	        	'<div class="sel_xx">'+
	                '<div class="sel_box">'+
	                    '<input type="text" class="text" placeholder="请输入姓名" id="searchchaosongrenrealname"/>'+
	                    '<input type="button" class="find_btn" value="" onclick="searchchaosongren()" />'+
	                    '<div class="clear"></div>'+
	                '</div>'+
	                '<input type="hidden" id="CCuseridlist"/>'+
	                '<input type="hidden" id="CCusernamelist"/>'+
	                '<ul id="chaosongren_rangeul">'+
	                '</ul>'+
	            '</div>'+
	            '<div class="tab_list">'+
	            	'<table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">'+
	                	'<tr class="head_td">'+
	                    	'<td width="30">&nbsp;</td>'+
	                        '<td width="70">姓名</td>'+
	                        '<td width="50">性别</td>'+
	                        '<td width="100">电话</td>'+
	                        '<td>操作</td>'+
	                    '</tr>'+
	                    '<tbody id="chaosongrenusertable"></tbody>'+
	                '</table>'+
	            '</div>'+
	            '<div id="Pagination" style="width:450px;"><div id="chaosongrenPagination"></div></div><!--动态的获取pagination的宽度赋值给Pagination-->'+
	            '<div class="tc_btnbox"><a  href="javascript:void(0);" onclick="$(\'.div_mask\').hide();$(\'.tc_chaosongren\').hide();"  class="bg_gay2">取消</a>'+
	            '<a href="javascript:void(0)" onclick="chaosongren_submitdata()"  class="bg_yellow">确定</a>'+
	            '</div>'+
	        '</div>'+
	        '<div class="clear"></div>'+
	    '</div>'+
	'</div>';

	 $("body").after(chaosongren_html);
});
function showCCuserOrganize(){
	
	$('.div_mask').show();$('.tc_chaosongren').show();
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/pc/getPCOrganize",
		success:function(data){
			if(data.status == 0){
				var organizelist = data.organizelist;
				showOrganize_chaosongren(organizelist,data.compandyname);
			}
		}
	})
}

var initorganizeid_chaosong="";
function showOrganize_chaosongren(list,compandyname){
	if(list.length > 0){
		for(var i=0;i<list.length;i++){
			if(list[i].parentid=="" || typeof(list[i].parentid)=="undefined"){
				var temp="<ul class=\"tree_list\">"+
            	"<li id=\"chaosong_li_"+list[i].organizeid+"\">"+                    	                    	
            		"<div class=\"gray_line\"></div>"+
                	"<span class=\"bg_show\" onclick=\"initorganizeid_chaosongren(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">"+
					"<i id=\"companyname_chaosong\">"+list[i].organizename+"</i></span>"+
                "</li>"+
                "</ul>";
				initorganizeid_chaosong=list[i].organizeid;
				$("#chaosongren_organize").html(temp);
				appendli_chaosong(list,list[i].organizeid);
			}
		}
	}
}

function appendli_chaosong(list,organizeid){
	var indexnum=0;
	for(var i=0;i<list.length;i++){
		if(list[i].parentid==organizeid){
			var ultemp="";
			if(organizeid==initorganizeid_chaosong){
				ultemp+="<ul style=\"display:block;\" id=\"chaosong_ul_"+list[i].parentid+"\"></ul>";
			}else{
				ultemp+="<ul style=\"display:none;\" id=\"chaosong_ul_"+list[i].parentid+"\"></ul>";
			}
			
			var temp="<li class=\"li_bg\" id=\"chaosong_li_"+list[i].organizeid+"\">";
			indexnum++;
			if(list[i].childnum>0){
				temp+="<div class=\"gray_line\" style=\"display:none\"></div><span class=\"bg_hidden\" onclick=\"initorganizeid_chaosongren(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">";
				
			}else{
				temp+="<span class=\"bg_last\" onclick=\"initorganizeid_chaosongren(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">";
			}
            temp+="<i id=\"companyname_chaosong\">"+list[i].organizename+"</i></span></li>";

            if($("#chaosong_ul_"+list[i].parentid).length>0){
            	$("#chaosong_ul_"+list[i].parentid).append(temp);
			}else{
				$("#chaosong_li_"+organizeid).append(ultemp);
				$("#chaosong_ul_"+list[i].parentid).append(temp);
			}
			$("ul li").find("div[class=white_line]").remove();
			$("ul li:last-child").append("<div class=\"white_line\" name=\"white_box\">");
			
			appendli_chaosong(list,list[i].organizeid);
			
			
		}
	}
	var whiteHeight=0;
	
}

function deleteuser_chaosong(obj){
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
function initorganizeid_chaosongren(obj,isonlyread,organizeid){
	if(isonlyread != '1'){
		//回调查询
		callbackfunc_chaosong(organizeid);
		$('#chaosongren_organize').find("i").attr("class","");
		$(obj).find("i").attr("class","bg_yellow");
	}
	if($(obj).attr("class")=="bg_hidden"){
		$(obj).prev().show();
		$(obj).attr("class","bg_show");
		$(obj).nextAll("ul").show();
		changeheight_chaosong();
	}else if($(obj).attr("class")=="bg_show"){
		$(obj).attr("class","bg_hidden");
		$(obj).nextAll("ul").hide();
		$(obj).prev().hide();
		changeheight_chaosong();
	}
}
function changeheight_chaosong(){
	var whiteHeight=0;
	$(".tree_list .white_line").each(function() {	
		whiteHeight = $(this).parent().height();
		whiteHeight = whiteHeight - 21 ;
	    $(this).height(whiteHeight) ;
	});
}

function chaosongren_submitdata(){
	var alldata={"userlist":[]};
	var td = "";
	var ccusernames = "";
	$('#chaosongren_rangeul input[name=chaosongren_userid]').each(function(index){
		var userlist={};
		userlist['userid']=$(this).val();
		ccusernames += $(this).attr("realname")+"，";
		td += "<div class=\"img f\"><img src=\""+projectpath+$(this).attr("headimage")+"\" width=\"30\" height=\"30\" /></div>"+
									"<i class=\"i_name\" >"+$(this).attr("realname")+"</i>";
		alldata.userlist.push(userlist); 
	})
	$('#CCuseridlist').val(JSON.stringify(alldata));
	$('#CCusernamelist').val(ccusernames.substring(0,(ccusernames.length)-1));
	td += '<a href="javascript:void(0)" onclick="showCCuserOrganize()" class="add_user">添加</a>';
	$('#CCusernames').html(td);
	$('.tc_chaosongren').hide();$('.div_mask').hide();
}
function adduser_chaosong(obj,userid,realname,headimage){
	$('#chaosongren_rangeul').find("input[value="+userid+"]").parent().remove();
	var temp="<li><input type=\"hidden\" name=\"chaosongren_userid\" value=\""+userid+"\" realname='"+realname+"' headimage='"+headimage+"'/>"+realname+"<a class=\"del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\" onclick=\"deleteuser_chaosong(this)\" /></a></li>";
	$('#chaosongren_rangeul').append(temp);
}

var chaosongrenParam = new Object();

function pageHelper2(num){
	chaosongrenParam.currentPage = num;
	if(chaosongrenParam.searchrealname == "" && chaosongrenParam.organizeid != ""){
		callbackfunc_chaosong("");
	}else if(chaosongrenParam.searchrealname != "" && chaosongrenParam.organizeid == ""){
		searchchaosongren("search");
	}
}

function callbackfunc_chaosong(organizeid){
	if(organizeid != ""){
		chaosongrenParam.searchrealname = "";
		chaosongrenParam.organizeid = organizeid;
		chaosongrenParam.currentPage = 1;
		chaosongrenParam.rowPage = 5;
	}
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/userbackstage/getUserByOrganize",
		data:chaosongrenParam,
		success:function(data){
			showchaosongreninfo(data);
		}
	})
}
function searchchaosongren(type){
	if(type != "search"){
		var searchrealname=$('#searchchaosongrenrealname').val();
		if(searchrealname == ""){
			swal("","请输入用户名称···","warning");
			return false;
		}
		chaosongrenParam.searchrealname = searchrealname;
		chaosongrenParam.organizeid = "";
		chaosongrenParam.currentPage = 1;
		chaosongrenParam.rowPage = 5;
	}
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/userbackstage/getSearchUserByName",
		data:chaosongrenParam,
		success:function(data){
			showchaosongreninfo(data);
		}
	})
}


function showchaosongreninfo(data){
	$('#chaosongrenPagination').html(data.pager2);
	$('#chaosongrenusertable').empty();
	if(data.userlist.length>0){
		var temp="";
		for(var i=0;i<data.userlist.length;i++){
			temp+="<tr><td class=\"img_td\"><div class=\"img\"><img src=\""+data.userlist[i].headimage+"\" width=\"30\" height=\"30\" /></div></td>"+
                "<td>"+data.userlist[i].realname+"</td>";
                if(data.userlist[i].sex==1){
               		temp+="<td>男</td>";
                }else if(data.userlist[i].sex==0){
                	temp+="<td>女</td>";
                }else{
                	temp+="<td></td>";
                }
                if(data.userlist[i].isshowphone==1){
                	 temp+="<td>"+data.userlist[i].phone+"</td>";
                }else{
                	var phone=data.userlist[i].phone;
                	temp+="<td>"+phone.substring(0,3)+"*****"+phone.substring(7,phone.length)+"</td>";
                }
            if(data.userlist[i].userid != '${userInfo.userid}' && $('#examineuserid').val()!=data.userlist[i].userid){
                temp+="<td><a href=\"javascript:void(0)\" onclick=\"adduser_chaosong(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"','"+data.userlist[i].headimage+"')\" class=\"link\">添加</a></td></tr>";
			}
		}
		$('#chaosongrenusertable').html(temp);
	}
}
