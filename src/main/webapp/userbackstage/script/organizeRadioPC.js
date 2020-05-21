$(function(){
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/pc/getPCOrganize",
		success:function(data){
			if(data.status == 0){
				var organizelist = data.organizelist;
				showOrganize1(organizelist,data.compandyname);
			}
		}
	})
});
var initorganizeid1="";
function showOrganize1(list,compandyname){
	if(list.length > 0){
		for(var i=0;i<list.length;i++){
			if(list[i].parentid=="" || typeof(list[i].parentid)=="undefined"){
				var temp="<ul class=\"tree_list\">"+
                	"<li id=\"li1_"+list[i].organizeid+"\">"+                    	                    	
                		"<div class=\"gray_line\"></div>"+
                    	"<span class=\"bg_show\" onclick=\"changenextul1(this,'"+list[i].organizeid+"')\">";
						if(list[i].isonlyread != 1){
							temp+="<a href=\"javascript:void(0)\" class=\"check\" onclick=\"changeorganize(this,'"+list[i].organizeid+"','"+list[i].organizename+"')\">选择</a>";
						}
						temp+="<i id=\"companyname\">"+list[i].organizename+"</i></span>"+
                    "</li>"+
                "</ul>";
				initorganizeid1=list[i].organizeid;
				$("#organizetree1").html(temp);
				appendli1(list,list[i].organizeid);
			}
		}
	}
}

function appendli1(list,organizeid){
	var indexnum=0;
	for(var i=0;i<list.length;i++){
		if(list[i].parentid==organizeid){
			var ultemp="";
			if(organizeid==initorganizeid1){
				ultemp+="<ul style=\"display:block;\" id=\"ul1_"+list[i].parentid+"\"></ul>";
			}else{
				ultemp+="<ul style=\"display:none;\" id=\"ul1_"+list[i].parentid+"\"></ul>";
			}
			
			var temp="<li class=\"li_bg\" id=\"li1_"+list[i].organizeid+"\">";
			indexnum++;
			if(list[i].childnum>0){
				temp+="<div class=\"gray_line\" style=\"display:none\"></div><span class=\"bg_hidden\" onclick=\"changenextul1(this,'"+list[i].organizeid+"')\">";
				
			}else{
				temp+="<span class=\"bg_last\" onclick=\"changenextul1(this,'"+list[i].organizeid+"')\">";
			}
			
			if(list[i].isonlyread != 1){
				temp+="<a href=\"javascript:void(0)\" class=\"check\" onclick=\"changeorganize(this,'"+list[i].organizeid+"','"+list[i].organizename+"')\">选择</a>";
			}
            temp+="<i id=\"companyname\">"+list[i].organizename+"</i></span>";
            temp+="</li>";

            if($("#ul1_"+list[i].parentid).length>0){
            	$("#ul1_"+list[i].parentid).append(temp);
			}else{
				$("#li1_"+organizeid).append(ultemp);
				$("#ul1_"+list[i].parentid).append(temp);
			}
			$("ul li").find("div[class=white_line]").remove();
			$("ul li:last-child").append("<div class=\"white_line\" name=\"white_box\">");
			
			appendli1(list,list[i].organizeid);
			
			
		}
	}
	var whiteHeight=0;
	
}

function changeorganize(obj,organizeid,organizename){
	$('#organizetree1').find('a[class=checked]').attr("class","check");
	$(obj).attr("class","checked");

	//回调方法
	chooseorganize(organizeid,organizename);
}
function changenextul1(obj,organizeid){
	if($(obj).attr("class")=="bg_hidden"){
		$(obj).prev().show();
		$(obj).attr("class","bg_show");
		$(obj).nextAll("ul").show();
		changeheight1();
	}else if($(obj).attr("class")=="bg_show"){
		$(obj).attr("class","bg_hidden");
		$(obj).nextAll("ul").hide();
		$(obj).prev().hide();
		changeheight1();
	}
}
function changeheight1(){
	var whiteHeight=0;
	$(".tree_list .white_line").each(function() {	
		whiteHeight = $(this).parent().height();
		whiteHeight = whiteHeight - 21 ;
	    $(this).height(whiteHeight) ;
	});
}
