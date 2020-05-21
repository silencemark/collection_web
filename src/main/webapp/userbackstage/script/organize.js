$(function(){
	$.ajax({
		type:"post",
		dataType:"json",
		async:false,
		url:"/userbackstage/getOrganize",
		success:function(data){
			if(data.status == 0){
				var organizelist = data.organizelist;
				showOrganize(organizelist,data.compandyname);
			}
		}
	})
});

var initorganizeid="";
function showOrganize(list,compandyname){
	if(list.length > 0){
		for(var i=0;i<list.length;i++){
			if(list[i].parentid=="" || typeof(list[i].parentid)=="undefined"){
				var temp="<ul class=\"tree_list\">"+
                	"<li id=\"li_"+list[i].organizeid+"\">"+                    	                    	
                		"<div class=\"gray_line\"></div>"+
                    	"<span class=\"bg_show\" onclick=\"changenextul(this,'"+list[i].isonlyread+"','')\"><i id=\"companyname\" class=\"bg_yellow\">"+list[i].organizename+"</i></span>"+
                    "</li>"+
                "</ul>";
				initorganizeid=list[i].organizeid;
				$("#organizetree").html(temp);
				appendli(list,list[i].organizeid);
			}
		}
	}
}

function appendli(list,organizeid){
	var indexnum=0;
	for(var i=0;i<list.length;i++){
		if(list[i].parentid==organizeid){
			var ultemp="";
			if(organizeid==initorganizeid){
				ultemp+="<ul style=\"display:block;\" id=\"ul_"+list[i].parentid+"\"></ul>";
			}else{
				ultemp+="<ul style=\"display:none;\" id=\"ul_"+list[i].parentid+"\"></ul>";
			}
			
			var temp="<li class=\"li_bg\" id=\"li_"+list[i].organizeid+"\">";
			indexnum++;
			if(list[i].childnum>0){
				temp+="<div class=\"gray_line\" style=\"display:none\"></div><span class=\"bg_hidden\" onclick=\"changenextul(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">";
				
			}else{
				temp+="<span class=\"bg_last\" onclick=\"changenextul(this,'"+list[i].isonlyread+"','"+list[i].organizeid+"')\">";
			}
            temp+="<i id=\"companyname\">"+list[i].organizename+"</i></span>";
            temp+="</li>";

            if($("#ul_"+list[i].parentid).length>0){
            	$("#ul_"+list[i].parentid).append(temp);
			}else{
				$("#li_"+organizeid).append(ultemp);
				$("#ul_"+list[i].parentid).append(temp);
			}
			$("ul li").find("div[class=white_line]").remove();
			$("ul li:last-child").append("<div class=\"white_line\" name=\"white_box\">");
			
			appendli(list,list[i].organizeid);
			
			
		}
	}
	var whiteHeight=0;
	
}


function changenextul(obj,isonlyread,organizeid){
	if(isonlyread != '1'){
		//回调查询
		callbackfunc(organizeid);
		$('#organizetree').find("i").attr("class","");
		$(obj).find("i").attr("class","bg_yellow");
	}
	if($(obj).attr("class")=="bg_hidden"){
		$(obj).prev().show();
		$(obj).attr("class","bg_show");
		$(obj).nextAll("ul").show();
		changeheight();
	}else if($(obj).attr("class")=="bg_show"){
		$(obj).attr("class","bg_hidden");
		$(obj).nextAll("ul").hide();
		$(obj).prev().hide();
		changeheight();
	}
}
function changeheight(){
	var whiteHeight=0;
	$(".tree_list .white_line").each(function() {	
		whiteHeight = $(this).parent().height();
		whiteHeight = whiteHeight - 21 ;
	    $(this).height(whiteHeight) ;
	});
}
