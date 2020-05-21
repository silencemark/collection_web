/**
 * 
 */


var selecthtml="<link type=\"text/css\" rel=\"stylesheet\" href=\"../appcssjs/style/public.css\">"+
"<link type=\"text/css\" rel=\"stylesheet\" href=\"../appcssjs/style/page_public.css\">"+
"<link type=\"text/css\" rel=\"stylesheet\" href=\"../appcssjs/style/structure.css\">"+
"<div style=\"display: none;width:100%;height:100%\" id=\"organizeiframe\">"+
		"<input type=\"hidden\" id=\"userid\"/>"+
"<input type=\"hidden\" id=\"realname\"/>"+
"<div class=\"head_box\">"+
"<a class=\"ico_back\" onclick=\"$('#organizeiframe').hide();$('#htmlbody').show();\">返回</a>"+
	"<div class=\"name\">组织架构</div>"+
	"<div class=\"link_yellow\" id=\"confirmbtn\" style=\"display: none;\" onclick=\"confirmexanine()\">确认</div>"+
	"</div>"+
"<div class=\"head_none\"></div>"+
"<div class=\"stru_list\">"+
	"<div class=\"search_box\"><input type=\"text\" class=\"bg\" placeholder=\"搜索\" oninput=\"searchuser(this)\"></div>"+
	"<div class=\"title\" id=\"organizetext\" onclick=\"onloadSelect()\"></div>"+
"</div>"+
"</div>";

$('#htmlbody').after(selecthtml);
onloadSelect();

function onloadSelect(){
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getNextOrgainize?companyid='+userInfo.companyid,
		success:function(data){
			if(data.organizelist.length > 0){
				$('#organizetext').text(data.organizelist[0].organizename);
				$('#organizetext').nextAll().remove();
				getorganizelist(0,data.organizelist[0].organizeid,data.organizelist[0].organizename,'#organizetext');
			}
		}
	})
}
function getorganizelist(num,organizeid,organizename,obj){
	$('#userid').val("");
	$('#realname').val("");
	$('#confirmbtn').hide();
 	if(obj !='#organizetext' &&  $(obj).parent().next("ul[class=quyu]").length>0){
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
		async:false,
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
					temp+="id=\"organize_"+data.organizelist[i].datacode+"\" style=\"padding:0px 15px 0px "+(num*15+30)+"px;background-position:"+(num*15+15)+"px 50%;\"  onclick=\"getorganizelist("+(num+1)+",'"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"',this)\" ><i class=\""+changeorganizetype(data.organizelist[i].type)+"\">"+data.organizelist[i].organizename+"</i><div class=\"clear\"></div></span>";
				}
				
				if(data.userlist.length > 0){
					for(var j=0;j<data.userlist.length ;j++){
						if(data.userlist[j].userid != userInfo.userid){
							temp+="<div name=\"people\" onclick=\"changerealname(this,'"+data.userlist[j].userid+"','"+data.userlist[j].realname+"')\"><i class=\"ico_person\" style=\"padding:0px 15px 0px "+(num*15+35)+"px;background-position:"+(num*15+15)+"px 50%;\">"+data.userlist[j].realname+"</i><a class=\"radio\" >选择</a><div class=\"clear\"></div></div>";
						}
					}
				}
        		temp+="</li></ul>";
        		if(obj =='#organizetext'){
					$(obj).after(temp);
        		}else{
        			$(obj).parent().find("span").hide();
        			$(obj).parent().find("ul").hide();
        			$(obj).parent().find("div[name=people]").hide();
        			$(obj).show();
        			$(obj).attr("class","leve_a_s bg_show");
        			$(obj).after(temp);
        		}
        		if(obj !='#organizetext'){
 					$(obj).attr("class","leve_a_s bg_show");
        		}
			}else{
				if(obj !='#organizetext'){
 					$(obj).attr("class","leve_a_s");
        		}
			}
		}
	})
}
function changerealname(obj,userid,realname){
	$('a[class=radio_ed]').attr("class","radio");
	$(obj).find('a').attr("class","radio_ed");
	$('#userid').val(userid);
	$('#realname').val(realname);
	$('#confirmbtn').show();
}
function confirmexanine(){
	$('#examinename').text($('#realname').val());
	$('#examinename').attr("onclick","location.href='../member/homepage_info.html?userid="+$('#userid').val()+"'");
	$('#examineuserid').val($('#userid').val());
	$('#organizeiframe').hide();
	$('#htmlbody').show();
	logical();
}
function searchuser(obj){
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
							temp+="<div name=\"people\" onclick=\"changerealname(this,'"+data.userlist[j].userid+"','"+data.userlist[j].realname+"')\"><i class=\"ico_person\" style=\"padding:0px 15px 0px 30px;background-position:15px 50%;\">"+data.userlist[j].realname+"-"+data.userlist[j].organizename+"</i><a class=\"radio\">选择</a><div class=\"clear\"></div></div>";
						}
					}
	        		temp+="</li></ul>";
	        		$("#organizetext").nextAll().remove();
					$("#organizetext").after(temp);
				}
			}
		})
	}else{
		onloadSelect();
	}
}