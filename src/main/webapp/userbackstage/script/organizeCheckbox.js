$(function(){
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/userbackstage/getOrganize",
		success:function(data){
			if(data.status == 0){
				var organizelist = data.organizelist;
				showOrganize3(organizelist,data.compandyname);
			}
		}
	})
});
var initorganizeid3="";
function showOrganize3(list,compandyname){
	if(list.length > 0){
		for(var i=0;i<list.length;i++){
			if(list[i].parentid=="" || typeof(list[i].parentid)=="undefined"){
				var temp="<ul class=\"tree_list\">"+
                	"<li id=\"li3_"+list[i].organizeid+"\">"+                    	                    	
                		"<div class=\"gray_line\"></div>"+
                    	"<span class=\"bg_show\" onclick=\"changenextul3(this,'"+list[i].organizeid+"')\">";
						if(list[i].isonlyread != 1){
							temp+="<a href=\"javascript:void(0)\" class=\"check\" id=\"checkboxdiv_"+list[i].organizeid+"\" onclick=\"changeorganize1(this,'"+list[i].organizeid+"','"+list[i].organizename+"')\">" +
									"<input type=\"hidden\" name=\"organizeid\" value=\""+list[i].organizeid+"\"/><input type=\"hidden\" name=\"organizename\" value=\""+list[i].organizename+"\"/>选择</a>";
						}
						temp+="<i id=\"companyname\">"+list[i].organizename+"</i></span>"+
                    "</li>"+
                "</ul>";
				initorganizeid3=list[i].organizeid;
				$("#checkboxdiv").html(temp);
				appendli3(list,list[i].organizeid);
			}
		}
	}
}

function appendli3(list,organizeid){
	var indexnum=0;
	for(var i=0;i<list.length;i++){
		if(list[i].parentid==organizeid){
			var ultemp="";
			if(organizeid==initorganizeid3){
				ultemp+="<ul style=\"display:block;\" id=\"ul3_"+list[i].parentid+"\"></ul>";
			}else{
				ultemp+="<ul style=\"display:none;\" id=\"ul3_"+list[i].parentid+"\"></ul>";
			}
			
			var temp="<li class=\"li_bg\" id=\"li3_"+list[i].organizeid+"\">";
			indexnum++;
			if(list[i].childnum>0){
				temp+="<div class=\"gray_line\" style=\"display:none\"></div><span class=\"bg_hidden\" onclick=\"changenextul3(this,'"+list[i].organizeid+"')\">";
				
			}else{
				temp+="<span class=\"bg_last\" onclick=\"changenextul3(this,'"+list[i].organizeid+"')\">";
			}
			
			if(list[i].isonlyread != 1){
				temp+="<a href=\"javascript:void(0)\" id=\"checkboxdiv_"+list[i].organizeid+"\" class=\"check\" onclick=\"changeorganize1(this,'"+list[i].organizeid+"','"+list[i].organizename+"')\">" +
						"<input type=\"hidden\" name=\"organizeid\" value=\""+list[i].organizeid+"\"/><input type=\"hidden\" name=\"organizename\" value=\""+list[i].organizename+"\"/>选择</a>";
			}
            temp+="<i id=\"companyname\">"+list[i].organizename+"</i></span>";
            temp+="</li>";

            if($("#ul3_"+list[i].parentid).length>0){
            	$("#ul3_"+list[i].parentid).append(temp);
			}else{
				$("#li3_"+organizeid).append(ultemp);
				$("#ul3_"+list[i].parentid).append(temp);
			}
			$("ul li").find("div[class=white_line]").remove();
			$("ul li:last-child").append("<div class=\"white_line\" name=\"white_box\">");
			
			appendli3(list,list[i].organizeid);
			
			
		}
	}
	var whiteHeight=0;
	
}

function changeorganize1(obj,organizeid,organizename){
	if($(obj).attr("class")=="check"){
		$(obj).attr("class","checked");
	}else{
		$(obj).attr("class","check");
	}
}
function changenextul3(obj,organizeid){
	if($(obj).attr("class")=="bg_hidden"){
		$(obj).prev().show();
		$(obj).attr("class","bg_show");
		$(obj).nextAll("ul").show();
		changeheight3();
	}else if($(obj).attr("class")=="bg_show"){
		$(obj).attr("class","bg_hidden");
		$(obj).nextAll("ul").hide();
		$(obj).prev().hide();
		changeheight3();
	}
}
function changeheight3(){
	var whiteHeight=0;
	$(".tree_list .white_line").each(function() {	
		whiteHeight = $(this).parent().height();
		whiteHeight = whiteHeight - 21 ;
	    $(this).height(whiteHeight) ;
	});
}
