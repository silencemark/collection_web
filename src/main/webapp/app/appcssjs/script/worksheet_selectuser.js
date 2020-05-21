/**
 * 
 */


var selecthtml="<link type=\"text/css\" rel=\"stylesheet\" href=\"../appcssjs/style/public.css\">"+
"<link type=\"text/css\" rel=\"stylesheet\" href=\"../appcssjs/style/page_public.css\">"+
"<link type=\"text/css\" rel=\"stylesheet\" href=\"../appcssjs/style/structure.css\">"+
"<div style=\"display: none;width:100%;height:100%\" id=\"worksheet_organizeiframe\">"+
		"<input type=\"hidden\" id=\"reward_userid\"/>"+
		"<input type=\"hidden\" id=\"reward_realname\"/>"+
		"<input type=\"hidden\" id=\"reward_organizename\"/>"+
		"<input type=\"hidden\" id=\"rewared_position\"/>"+
		"<input type=\"hidden\" id=\"organizechange_datacode\"/>"+
"<div class=\"head_box\">"+
"<a class=\"ico_back\" onclick=\"$('#worksheet_organizeiframe').hide();$('#htmlbody').show();\">返回</a>"+
	"<div class=\"name\">组织架构</div>"+
	"<div class=\"link_yellow\" id=\"confirmuserbtn\" style=\"display: none;\" onclick=\"confirmuser()\">确认</div>"+
	"</div>"+
"<div class=\"head_none\"></div>"+
"<div class=\"stru_list\">"+
	"<div class=\"search_box\"><input type=\"text\" class=\"bg\" placeholder=\"搜索\" oninput=\"rewardsearchuser(this)\"></div>"+
	"<div class=\"title\" id=\"usertext\" onclick=\"onloadSelectUser()\"></div>"+
"</div>"+
"</div>";

$('#htmlbody').after(selecthtml);
onloadSelectUser();

function onloadSelectUser(){
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getNextOrgainize?companyid='+userInfo.companyid,
		success:function(data){
			if(data.organizelist.length > 0){
				$('#usertext').text(data.organizelist[0].organizename);
				$('#usertext').nextAll().remove();
				getuserlist(0,data.organizelist[0].organizeid,data.organizelist[0].organizename,'#usertext');
			}
		}
	})
}
function getuserlist(num,organizeid,organizename,obj){
	$('#reward_userid').val("");
	$('#reward_realname').val("");
	$('#confirmuserbtn').hide();
 	if(obj !='#usertext' &&  $(obj).parent().next("ul[class=quyu]").length>0){
		if($(obj).parent().attr("class")=="leve_a_s bg_hidden"){
			$(obj).parent().find("span").hide();
			$(obj).parent().find("ul").hide();
			$(obj).parent().find("div[name=people]").hide();
			$(obj).show();
			$(obj).attr("class","leve_a_s bg_show");
			$(obj).next("ul").show();
			$(obj).next("ul").find("span").show();
			$(obj).next("ul").find("span").each(function(index){
				if($(this).attr("class")=="leve_a_s bg_show"){
					$(this).attr("class","leve_a_s bg_hidden")
				}
			})
			$(obj).next("ul").find("div[name=people]").show();
		}else{
			$(obj).next("ul").hide();
			$(obj).attr("class","leve_a_s bg_hidden");
		}
		return;
	}
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getNextOrgainize?companyid='+userInfo.companyid+'&organizeid='+organizeid,
		success:function(data){
			if(data.organizelist.length > 0 || data.userlist.length >0){
				var temp="";
				temp+="<ul class=\"quyu\">";
				temp+="<li class=\"leve_a\" >";
				for(var i=0;i<data.organizelist.length ;i++){
					if(data.organizelist[i].usernum>0  || data.organizelist[i].childnum > 0){
						temp+="<span class=\"leve_a_s bg_hidden\"";
					}else{
						temp+="<span class=\"leve_a_s\"";
					}
					temp+="style=\"padding:0px 15px 0px "+(num*15+30)+"px;background-position:"+(num*15+15)+"px 50%;\"  onclick=\"getuserlist("+(num+1)+",'"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"',this)\" ><i class=\""+changeorganizetype(data.organizelist[i].type)+"\">"+data.organizelist[i].organizename+"</i><div class=\"clear\"></div></span>";
				}
				
				if(data.userlist.length > 0){
					for(var j=0;j<data.userlist.length ;j++){
						if(data.userlist[j].userid != userInfo.userid){
							temp+="<div name=\"people\" onclick=\"changeusername(this,'"+data.userlist[j].userid+"','"+data.userlist[j].realname+"','"+data.userlist[j].organizename+"','"+data.userlist[j].position+"','"+data.userlist[j].datacode+"')\"><i class=\"ico_person\" style=\"padding:0px 15px 0px "+(num*15+35)+"px;background-position:"+(num*15+15)+"px 50%;\">"+data.userlist[j].realname+"</i><a class=\"radio\" >选择</a><div class=\"clear\"></div></div>";
						}
					}
				}
        		temp+="</li></ul>";
        		if(obj =='#usertext'){
					$(obj).after(temp);
        		}else{
        			$(obj).parent().find("span").hide();
        			$(obj).parent().find("ul").hide();
        			$(obj).parent().find("div[name=people]").hide();
        			$(obj).show();
        			$(obj).attr("class","leve_a_s bg_show");
        			$(obj).after(temp);
        		}
        		if(obj !='#usertext'){
 					$(obj).attr("class","leve_a_s bg_show");
        		}
			}else{
				if(obj !='#usertext'){
 					$(obj).attr("class","leve_a_s");
        		}
			}
		}
	})
}
function changeusername(obj,userid,realname,organizename,position,datacode){
	$('a[class=radio_ed]').attr("class","radio");
	$(obj).find('a').attr("class","radio_ed");
	$('#reward_userid').val(userid);
	$('#organizechange_datacode').val(datacode);
	$('#reward_realname').val(realname);
	$('#reward_organizename').val(organizename);
	$('#rewared_position').val(position);
	$('#confirmuserbtn').show();
}
function confirmuser(){
	$('#resoucename').text($('#reward_realname').val());
	$('#resouceid').val($('#reward_userid').val());
	$('#organizename').val($('#reward_organizename').val());
	$('#identityname').val($('#rewared_position').val());
	$('#worksheet_organizeiframe').hide();
	$('#htmlbody').show();
	
	var datacode = $('#organizechange_datacode').val();
	changeorganizestore(datacode);
	
}
function rewardsearchuser(obj){
	var realname =$(obj).val();
	if(realname.trim()!=""){
		$.ajax({
			type:'post',
			dataType:'json',
			url:projectpath+'/app/getUserBySearch?companyid='+userInfo.companyid+'&searchname='+realname,
			success:function(data){
				if(data.userlist.length > 0){
					var temp="";
					temp+="<ul class=\"quyu\">";
					temp+="<li class=\"leve_a\" >";
					if(data.userlist.length > 0){
						for(var j=0;j<data.userlist.length ;j++){
							temp+="<div name=\"people\" onclick=\"changeusername(this,'"+data.userlist[j].userid+"','"+data.userlist[j].realname+"','"+data.userlist[j].datacode+"')\"><i class=\"ico_person\" style=\"padding:0px 15px 0px 30px;background-position:15px 50%;\">"+data.userlist[j].realname+"-"+data.userlist[j].organizename+"</i><a class=\"radio\">选择</a><div class=\"clear\"></div></div>";
						}
					}
	        		temp+="</li></ul>";
	        		$("#usertext").nextAll().remove();
					$("#usertext").after(temp);
				}
			}
		})
	}else{
		onloadSelectUser();
	}
}