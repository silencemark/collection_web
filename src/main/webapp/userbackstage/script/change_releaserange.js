
var releaserangehtml = '<div class="tc_selrange" style="display:none" id="releaserangediv">'+
						'<div class="tc_title"><span>发布范围</span><a href="javascript:void(0);" onclick="$(\'#releaserangediv\').hide();$(\'.div_mask\').hide();">×</a></div>'+
					    '<div class="range">'+
					    	'<div class="l_box" id="releaserangetree"  style="width:280px;overflow:hidden;overflow-y:visible;">'+
					    	
					        '</div>'+
					        '<div class="r_box" style="width:440px;overflow:hidden;overflow-y:visible;">'+
					        	'<div class="sel_xx">'+
					                '<div class="sel_box">'+
					                    '<input type="text" class="text" placeholder="请输入姓名" id="releaserangesearchrealname"/>'+
					                    '<input type="button" class="find_btn" value="" onclick="searchReleaseRangeUser()" />'+
					                    '<div class="clear"></div>'+
					                '</div>'+
					                '<input type="hidden" id="userlist"/>'+
					                '<ul id="releaseRangeUl">'+
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
					                    '<tbody id="releaseRangeTable"></tbody>'+
					                '</table>'+
					            '</div>'+
					            '<div id="Pagination" style="width:450px;"><div id="relaseRangePagination"></div></div><!--动态的获取pagination的宽度赋值给Pagination-->'+
					            '<div class="tc_btnbox"><a  href="javascript:void(0);" onclick="$(\'.div_mask\').hide();$(\'#releaserangediv\').hide();"  class="bg_gay2">取消</a>'+
					            '<a href="javascript:void(0)" onclick="releaseRangeSubmit()"  class="bg_yellow">确定</a>'+
					            '</div>'+
					        '</div>'+
					        '<div class="clear"></div>'+
					    '</div>'+
					'</div>';



$(function(){
	$('body').after(releaserangehtml);
	
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/pc/getPCOrganize",
		success:function(data){
			if(data.status == 0){
				var organizelist = data.organizelist;
				showReleaseRangeOrganize(organizelist,data.compandyname);
			}
		}
	})
})

function openOrCloseReleaseRangeDiv(){
	$('#releaserangediv').show();$('.div_mask').show();
}

var initReleaseRangeOrganizeid="";
function showReleaseRangeOrganize(list,compandyname){
	if(list.length > 0){
		for(var i=0;i<list.length;i++){
			if(list[i].parentid=="" || typeof(list[i].parentid)=="undefined"){
				var temp="<ul class=\"tree_list\">"+
            	"<li id=\"li1_"+list[i].organizeid+"\">"+                    	                    	
            		"<div class=\"gray_line\"></div>"+
                	"<span class=\"bg_show\" onclick=\"changeNextReleaseRangeOrganizeUl(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">"+
					"<i id=\"companyname\">"+list[i].organizename+"</i>";
					if(list[i].isonlyread != 1){
						temp+="<a href=\"javascript:void(0)\" onclick=\"addReleaseRangeOrganize(this,'"+list[i].organizeid+"','"+list[i].organizename+"')\" class=\"add\">添加</a>";
					}
					temp+="</span>"+
                "</li>"+
                "</ul>";
				initReleaseRangeOrganizeid=list[i].organizeid;
				$("#releaserangetree").html(temp);
				queryNextRealeaseRangeOrganize(list,list[i].organizeid);
			}
		}
	}
}

function queryNextRealeaseRangeOrganize(list,organizeid){
	var indexnum=0;
	for(var i=0;i<list.length;i++){
		if(list[i].parentid==organizeid){
			var ultemp="";
			if(organizeid==initReleaseRangeOrganizeid){
				ultemp+="<ul style=\"display:block;\" id=\"ul1_"+list[i].parentid+"\"></ul>";
			}else{
				ultemp+="<ul style=\"display:none;\" id=\"ul1_"+list[i].parentid+"\"></ul>";
			}
			
			var temp="<li class=\"li_bg\" id=\"li1_"+list[i].organizeid+"\">";
			indexnum++;
			if(list[i].childnum>0){
				temp+="<div class=\"gray_line\" style=\"display:none\"></div><span class=\"bg_hidden\" onclick=\"changeNextReleaseRangeOrganizeUl(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">";
				
			}else{
				temp+="<span class=\"bg_last\" onclick=\"changeNextReleaseRangeOrganizeUl(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">";
			}
            temp+="<i id=\"companyname\">"+list[i].organizename+"</i>";
            if(list[i].isonlyread != 1){
				temp+="<a href=\"javascript:void(0)\" onclick=\"addReleaseRangeOrganize(this,'"+list[i].organizeid+"','"+list[i].organizename+"')\" class=\"add\">添加</a>";
			}
            temp+="</span></li>";

            if($("#ul1_"+list[i].parentid).length>0){
            	$("#ul1_"+list[i].parentid).append(temp);
			}else{
				$("#li1_"+organizeid).append(ultemp);
				$("#ul1_"+list[i].parentid).append(temp);
			}
			$("ul li").find("div[class=white_line]").remove();
			$("ul li:last-child").append("<div class=\"white_line\" name=\"white_box\">");
			
			queryNextRealeaseRangeOrganize(list,list[i].organizeid);
			
			
		}
	}
	var whiteHeight=0;
	
}
function addReleaseRangeOrganize(obj,organizeid,organizename){
	$('#releaseRangeUl').find("input[value="+organizeid+"]").parent().remove();
	var temp="<li><input type=\"hidden\" name=\"releaseRangeOrganizeid\" value=\""+organizeid+"\" organizename='"+organizename+"'/>"+organizename+"<a class=\"del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\" onclick=\"deletReleaseRangeOrganizeUser(this)\" /></a></li>";
	$('#releaseRangeUl').append(temp);
}

function addReleaseRangeUser(obj,userid,realname){
	$('#releaseRangeUl').find("input[value="+userid+"]").parent().remove();
	var temp="<li><input type=\"hidden\" name=\"releaseRangeUserid\" value=\""+userid+"\" realname='"+realname+"'/>"+realname+"<a class=\"del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\" onclick=\"deletReleaseRangeOrganizeUser(this)\" /></a></li>";
	$('#releaseRangeUl').append(temp);
}

function deletReleaseRangeOrganizeUser(obj){
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
function changeNextReleaseRangeOrganizeUl(obj,isonlyread,organizeid){
	if(isonlyread != '1'){
		//回调查询
		queryOrganizeDownUserInfo(organizeid);
		$('#releaserangetree').find("i").attr("class","");
		$(obj).find("i").attr("class","bg_yellow");
	}
	if($(obj).attr("class")=="bg_hidden"){
		$(obj).prev().show();
		$(obj).attr("class","bg_show");
		$(obj).nextAll("ul").show();
		releaseRangeChangeHeight();
	}else if($(obj).attr("class")=="bg_show"){
		$(obj).attr("class","bg_hidden");
		$(obj).nextAll("ul").hide();
		$(obj).prev().hide();
		releaseRangeChangeHeight();
	}
}
function releaseRangeChangeHeight(){
	var whiteHeight=0;
	$(".tree_list .white_line").each(function() {	
		whiteHeight = $(this).parent().height();
		whiteHeight = whiteHeight - 21 ;
	    $(this).height(whiteHeight) ;
	});
}

var releaseRangeParam = new Object();
function pageHelper3(num){
	releaseRangeParam.currentPage = num;
	if(releaseRangeParam.searchrealname == "" && releaseRangeParam.organizeid != ""){
		queryOrganizeDownUserInfo("");
	}else if(releaseRangeParam.searchrealname != "" && releaseRangeParam.organizeid == ""){
		earchReleaseRangeUser("search");
	}
}

function queryOrganizeDownUserInfo(organizeid){
	if(organizeid != ""){
		releaseRangeParam.searchrealname = "";
		releaseRangeParam.organizeid = organizeid;
		releaseRangeParam.currentPage = 1;
		releaseRangeParam.rowPage = 5;
	}
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/userbackstage/getUserByOrganize",
		data:releaseRangeParam,
		success:function(data){
			showReleaseRangeUserListInfo(data);
		}
	})
}

function searchReleaseRangeUser(type){
	if(type != "search"){
		var searchrealname=$('#releaserangesearchrealname').val();
		if(searchrealname == ""){
			swal("","请输入用户名称···","warning");
			return false;
		}
		releaseRangeParam.searchrealname = searchrealname;
		releaseRangeParam.organizeid = "";
		releaseRangeParam.currentPage = 1;
		releaseRangeParam.rowPage = 5;
	}
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/userbackstage/getSearchUserByName",
		data:releaseRangeParam,
		success:function(data){
			showReleaseRangeUserListInfo(data);
		}
	})
}

function showReleaseRangeUserListInfo(data){
	$('#relaseRangePagination').html(data.pager3);
	$('#releaseRangeTable').empty();
	if(data.userlist.length>0){
		var temp="";
		for(var i=0;i<data.userlist.length;i++){
			if(data.userlist[i].userid != '${userInfo.userid}'){
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
                    temp+="<td><a href=\"javascript:void(0)\" onclick=\"addReleaseRangeUser(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"')\" class=\"link\">添加</a></td></tr>";
			}
		}
		$('#releaseRangeTable').html(temp);
	}
}


function releaseRangeSubmit(){
	var alldata={"userlist":[]};
	var organizename_realname = "";
	$('#releaseRangeUl input[name=releaseRangeUserid]').each(function(index){
		var userlist={};
		userlist['userid']=$(this).val();
		organizename_realname += $(this).attr("realname")+"，";
		alldata.userlist.push(userlist); 
	})
	$('#releaseRangeUl input[name=releaseRangeOrganizeid]').each(function(index){
		var organizelist={};
		organizelist['organizeid']=$(this).val();
		organizename_realname += $(this).attr("organizename")+"，";
		alldata.userlist.push(organizelist); 
	})
	$('#userlist').val(JSON.stringify(alldata));
	var names = organizename_realname.substring(0,(organizename_realname.length - 1))
	$('#releaserange').text(names);
	$('#releaserangediv').hide();$('.div_mask').hide();
}