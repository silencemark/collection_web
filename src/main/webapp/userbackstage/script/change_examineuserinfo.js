
var examinehtml = '<div class="tc_selrange" style="display:none;" id="changeexaminediv">'+
					'<div class="tc_title"><span>审批人</span><a href="javascript:void(0);" onclick="$(\'#changeexaminediv\').hide();$(\'.div_mask\').hide();">×</a></div>'+
			    '<div class="range">'+
			    	'<div class="l_box" id="examineOrganize" style="width:280px;overflow:hidden;overflow-y:visible;">'+
			    	
			        '</div>'+
			        '<div class="r_box" style="width:440px;overflow:hidden;overflow-y:visible;">'+
			        	'<div class="sel_xx">'+
			                '<div class="sel_box">'+
			                    '<input type="text" class="text" placeholder="请输入姓名" id="searchexaminerealname"/>'+
			                    '<input type="button" class="find_btn" value="" onclick="searchexamine()"/>'+
			                    '<div class="clear"></div>'+
			                '</div>'+
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
			                    '<tbody id="examineusertable"></tbody>'+
			                '</table>'+
			            '</div>'+
			            '<div id="Pagination" style="width:450px;"><div id="userPagination"></div></div><!--动态的获取pagination的宽度赋值给Pagination-->'+
			            '<div class="tc_btnbox"><a  href="javascript:void(0);" onclick="$(\'.div_mask\').hide();$(\'#changeexaminediv\').hide();"  class="bg_gay2">取消</a>'+
			            '</div>'+
			        '</div>'+
			        '<div class="clear"></div>'+
			    '</div>'+
			'</div>';


$(function(){
	$('body').after(examinehtml);
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/pc/getPCOrganize",
		success:function(data){
			if(data.status == 0){
				var organizelist = data.organizelist;
				showExamineOrganize(organizelist);
			}
		}
	})
});

function openOrCloseExamineDiv(){
	$('#changeexaminediv').show();
	$('.div_mask').show();
}

var initExamineOrganizeid="";
function showExamineOrganize(list){
	if(list.length > 0){
		for(var i=0;i<list.length;i++){
			if(list[i].parentid=="" || typeof(list[i].parentid)=="undefined"){
				var temp="<ul class=\"tree_list\">"+
            	"<li id=\"li2_"+list[i].organizeid+"\">"+                    	                    	
            		"<div class=\"gray_line\"></div>"+
                	"<span class=\"bg_show\" onclick=\"changeNextexamineUl(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">"+
					"<i id=\"companyname\">"+list[i].organizename+"</i></span>"+
                "</li>"+
                "</ul>";
				initExamineOrganizeid=list[i].organizeid;
				$("#examineOrganize").html(temp);
				queryNextOrganize(list,list[i].organizeid);
			}
		}
	}
}

function queryNextOrganize(list,organizeid){
	var indexnum=0;
	for(var i=0;i<list.length;i++){
		if(list[i].parentid==organizeid){
			var ultemp="";
			if(organizeid==initExamineOrganizeid){
				ultemp+="<ul style=\"display:block;\" id=\"ul2_"+list[i].parentid+"\"></ul>";
			}else{
				ultemp+="<ul style=\"display:none;\" id=\"ul2_"+list[i].parentid+"\"></ul>";
			}
			
			var temp="<li class=\"li_bg\" id=\"li2_"+list[i].organizeid+"\">";
			indexnum++;
			if(list[i].childnum>0){
				temp+="<div class=\"gray_line\" style=\"display:none\"></div><span class=\"bg_hidden\" onclick=\"changeNextexamineUl(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">";
				
			}else{
				temp+="<span class=\"bg_last\" onclick=\"changeNextexamineUl(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">";
			}
            temp+="<i id=\"companyname\">"+list[i].organizename+"</i></span></li>";

            if($("#examineOrganize #ul2_"+list[i].parentid).length>0){
            	$("#examineOrganize #ul2_"+list[i].parentid).append(temp);
			}else{
				$("#examineOrganize #li2_"+organizeid).append(ultemp);
				$("#examineOrganize #ul2_"+list[i].parentid).append(temp);
			}
			$("ul li").find("div[class=white_line]").remove();
			$("ul li:last-child").append("<div class=\"white_line\" name=\"white_box\">");
			
			queryNextOrganize(list,list[i].organizeid);
			
			
		}
	}
	var whiteHeight=0;
	
}

function changeNextexamineUl(obj,isonlyread,organizeid){
	if(isonlyread != '1'){
		//回调查询
		queryOrganizeUser(organizeid);
		$('#examineOrganize').find("i").attr("class","");
		$(obj).find("i").attr("class","bg_yellow");
	}
	if($(obj).attr("class")=="bg_hidden"){
		$(obj).prev().show();
		$(obj).attr("class","bg_show");
		$(obj).nextAll("ul").show();
		changeheight_examine();
	}else if($(obj).attr("class")=="bg_show"){
		$(obj).attr("class","bg_hidden");
		$(obj).nextAll("ul").hide();
		$(obj).prev().hide();
		changeheight_examine();
	}
}
function changeheight_examine(){
	var whiteHeight=0;
	$(".tree_list .white_line").each(function() {	
		whiteHeight = $(this).parent().height();
		whiteHeight = whiteHeight - 21 ;
	    $(this).height(whiteHeight) ;
	});
}

var examineParam = new Object();
function pageHelper(num){
	examineParam.currentPage = num;
	
	if(examineParam.searchrealname == "" && examineParam.organizeid != ""){
		queryOrganizeUser("");
	}else if(examineParam.searchrealname != "" && examineParam.organizeid == ""){
		searchexamine("search");
	}
}

function queryOrganizeUser(organizeid){
	if(organizeid != ""){
		examineParam.searchrealname = "";
		examineParam.organizeid = organizeid;
		examineParam.currentPage = 1;
		examineParam.rowPage = 5;
	}
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/userbackstage/getUserByOrganize",
		data:examineParam,
		success:function(data){
			showExamineUserList(data);
		}
	})
}
function searchexamine(type){
	if(type != "search"){
		var searchrealname=$('#searchexaminerealname').val();
		if(searchrealname == ""){
			swal("","请输入用户名称···","warning");
			return false;
		}
		examineParam.organizeid = "";
		examineParam.currentPage = 1;
		examineParam.rowPage = 5;
		examineParam.searchrealname = searchrealname;
	}
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/userbackstage/getSearchUserByName",
		data:examineParam,
		success:function(data){
			showExamineUserList(data);
		}
	})
}

function showExamineUserList(data){
	$('#userPagination').html(data.pager);
	$('#examineusertable').html("");
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
                    temp+="<td><a href=\"javascript:void(0)\" onclick=\"addExamineUser(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"','"+data.userlist[i].headimage+"')\" class=\"link\">选择</a></td></tr>";
			}
		}
		$('#examineusertable').html(temp);
	}
}

function addExamineUser(obj,userid,realname,headimage){
	$('.div_mask').hide();
	$('#changeexaminediv').hide();
	$('#examinename').text(realname);
	$('#examineuserid').val(userid);
	$('#examineheadimage').attr("src",headimage);
}