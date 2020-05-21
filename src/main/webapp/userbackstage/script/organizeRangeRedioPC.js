$(function(){
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/pc/getPCOrganize",
		success:function(data){
			if(data.status == 0){
				var organizelist = data.organizelist;
				showOrganize2(organizelist);
			}
		}
	})
});
var initorganizeid2="";
function showOrganize2(list){
	if(list.length > 0){
		for(var i=0;i<list.length;i++){
			if(list[i].parentid=="" || typeof(list[i].parentid)=="undefined"){
				var temp="<ul class=\"tree_list\">"+
            	"<li id=\"li2_"+list[i].organizeid+"\">"+                    	                    	
            		"<div class=\"gray_line\"></div>"+
                	"<span class=\"bg_show\" onclick=\"changenextul2(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">"+
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
				temp+="<div class=\"gray_line\" style=\"display:none\"></div><span class=\"bg_hidden\" onclick=\"changenextul2(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">";
				
			}else{
				temp+="<span class=\"bg_last\" onclick=\"changenextul2(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">";
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
function addorganize(obj,organizeid,organizename){
	$('#rangeul').find("input[value="+organizeid+"]").parent().remove();
	var temp="<li><input type=\"hidden\" name=\"organizeid\" value=\""+organizeid+"\"/>"+organizename+"<a class=\"del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\" onclick=\"deleteorganizeuser(this)\" /></a></li>";
	$('#rangeul').append(temp);
}
function deleteorganizeuser(obj){
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
function changenextul2(obj,isonlyread,organizeid){
	if(isonlyread != '1'){
		//回调查询
		callbackfunc1(organizeid);
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
