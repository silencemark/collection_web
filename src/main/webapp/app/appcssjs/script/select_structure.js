/**
 * 
 */


var selecthtml="<link type=\"text/css\" rel=\"stylesheet\" href=\"../appcssjs/style/public.css\">"+
"<link type=\"text/css\" rel=\"stylesheet\" href=\"../appcssjs/style/page_public.css\">"+
"<link type=\"text/css\" rel=\"stylesheet\" href=\"../appcssjs/style/structure.css\">"+
"<div style=\"display: none;width:100%;height:100%\" id=\"organizeiframe\" >"+
		"<input type=\"hidden\" id=\"orgaid\"/>"+
		"<input type=\"hidden\" id=\"orgatype\"/>"+
		"<input type=\"hidden\" id=\"orgacompanyid\"/>"+
		"<input type=\"hidden\" id=\"orgaparentid\"/>"+
		"<input type=\"hidden\" id=\"orgacode\"/>"+
		"<input type=\"hidden\" id=\"organame\"/>"+
"<div class=\"head_box\">"+
"<a class=\"ico_back\" onclick=\"$('#organizeiframe').hide();$('#htmlbody').show();\">返回</a>"+
	"<div class=\"name\">组织架构</div>"+
	"<div class=\"link_yellow\" id=\"confirmbtn\" style=\"display: none;\" onclick=\"confirmexanine()\">确认</div>"+
	"</div>"+
"<div class=\"head_none\"></div>"+
"<div class=\"stru_list\">"+
	"<div class=\"search_box\"><input type=\"text\" class=\"bg\" placeholder=\"搜索\" oninput=\"searchOrganize(this)\"></div>"+
	"<div class=\"title\" id=\"organizetext\" onclick=\"onloadSelect()\"></div>"+
"</div>"+
"</div>";

$('#htmlbody').after(selecthtml);


function onloadSelect(){
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getNextOrgainize?companyid='+userInfo.companyid,
		success:function(data){
			if(data.organizelist.length > 0){
				$('#organizetext').text(data.organizelist[0].organizename);
				$('#organizetext').nextAll().remove();
				if(orgaidNum != -1){
					var temp="";
					temp+="<ul class=\"quyu\">";
					temp+="<li class=\"leve_a\" >";
					if(data.organizelist[0].childnum > 0){
						temp+="<span class=\"leve_a_s bg_hidden\"";
					}else{
						temp+="<span name=\"radios\"  class=\"leve_a_s\"";
					}
					temp+=" style=\"padding:0px 15px 0px 30px;background-position:15px 50%;\" ><i class=\"ico_cp\" onclick=\"getorganizelist(1,'"+
					data.organizelist[0].organizeid+"','"+data.organizelist[0].organizename+"',this)\">"+data.organizelist[0].organizename+"</i><a class=\"radio\" onclick=\"changerealname(this,'"+data.organizelist[0].organizeid+"','"+data.organizelist[0].organizename+"','"+data.organizelist[0].datacode+"','"+
					data.organizelist[0].type+"','"+data.organizelist[0].companyid+"','"+data.organizelist[0].parentid+"')\">选择</a><div class=\"clear\"></div></span>";
					temp+="</li></ul>";
					$("#organizetext").after(temp);
	//				
	//				getorganizelist(0,data.organizelist[0].organizeid,data.organizelist[0].organizename,'#organizetext');
				}else{
					getorganizelist(0,data.organizelist[0].organizeid,data.organizelist[0].organizename,"#organizetext")
				}
			}
		}
	})
}
function getorganizelist(num,organizeid,organizename,obj){
 	if(obj !='#organizetext' &&  $(obj).parent().next("ul[class=quyu]").length>0){
		if($(obj).parent().attr("class")=="leve_a_s bg_hidden"){
			$(obj).parent().parent().find("span").hide();
			$(obj).parent().parent().find("ul").hide();

			$(obj).parent().show();
			$(obj).parent().attr("class","leve_a_s bg_show");
			$(obj).parent().next("ul").show();
			$(obj).parent().next("ul").find("span").show();
			$(obj).parent().next("ul").find("span").attr("class","leve_a_s bg_hidden");
			$(obj).parent().next("ul").find("span[name=radios]").attr("class","leve_a_s");
		}else{
			$(obj).parent().next("ul").hide();
			$(obj).parent().attr("class","leve_a_s bg_hidden");
		}
		return;
	}
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getNextOrgainize?companyid='+userInfo.companyid+'&organizeid='+organizeid,
		success:function(data){
			if(data.organizelist.length > 0){
				var temp="";
				temp+="<ul class=\"quyu\">";
				temp+="<li class=\"leve_a\" >";
				for(var i=0;i<data.organizelist.length ;i++){
					if(orgaidNum != data.organizelist[i].organizeid){
						if(data.organizelist[i].childnum > 0){
							temp+="<span class=\"leve_a_s bg_hidden\"";
						}else{
							temp+="<span name=\"radios\"  class=\"leve_a_s\"";
						}
						temp+=" style=\"padding:0px 15px 0px "+(num*15+30)+"px;background-position:"+(num*15+15)+"px 50%;\" ><i class=\""+changeorganizetype(data.organizelist[i].type)+"\" onclick=\"getorganizelist("+(num+1)+",'"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"',this)\">"+data.organizelist[i].organizename+"</i><a class=\"radio\" onclick=\"changerealname(this,'"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"','"+data.organizelist[i].datacode+"','"+data.organizelist[i].type+"','"+data.organizelist[i].companyid+"','"+data.organizelist[i].parentid+"')\">选择</a><div class=\"clear\"></div></span>";
					}
				}
				
			
        		temp+="</li></ul>";
        		
        		if(obj =='#organizetext'){
					$(obj).after(temp);
        		}else{
        			$(obj).parent().parent().find("span").hide();
        			$(obj).parent().parent().find("ul").hide();
        			$(obj).parent().parent().find("div[name=people]").hide();
        			$(obj).parent().show();
        			$(obj).parent().attr("class","leve_a_s bg_show");
        			$(obj).parent().after(temp);
        		}
        		if(obj !='#organizetext'){
 					$(obj).parent().attr("class","leve_a_s bg_show");
        		}
			}else{
				if(obj !='#organizetext'){
 					$(obj).parent().attr("class","leve_a_s");
        		}
			}
		}
	})
}
function changerealname(obj,organizeid,organizename,orgacode,type,orgacompanyid,parentid){
	$('a[class=radio_ed]').attr("class","radio");
	$(obj).attr("class","radio_ed");
	$('#orgaid').val(organizeid);
	$('#orgatype').val(type);
	$('#organame').val(organizename);
	$('#orgacode').val(orgacode);
	$("#orgaparentid").val(parentid);
	$('#orgacompanyid').val(orgacompanyid);
	$('#confirmbtn').show();
	
	
	
}
function confirmexanine(){
	$("#parentid").val($("#orgaparentid").val());
	$('#organizename').val($('#organame').val());
	$('#organizeid').val($('#orgaid').val());
	$('#datacode').val($('#orgacode').val());
	$('#type').val($('#orgatype').val());
	$('#companyid').val($('#orgacompanyid').val());
	$('#organizeiframe').hide();
	$('#htmlbody').show();
	logical();

}
function searchOrganize(obj){
	var organizename =$(obj).val();
	if(organizename.trim()!=""){
		$.ajax({
			type:'post',
			dataType:'json',
			url:projectpath+'/app/getOrganizeBySearch?companyid='+userInfo.companyid+'&organizename='+organizename,
			success:function(data){
				if(data.organizelist.length > 0){
					var temp="";
					temp+="<ul class=\"quyu\">";
					temp+="<li class=\"leve_a\" >";
					for(var i=0;i<data.organizelist.length ;i++){
						if(orgaidNum != data.organizelist[i].organizeid){
							if(data.organizelist[i].childnum > 0){
								temp+="<span class=\"leve_a_s bg_hidden\"";
							}else{
								temp+="<span name=\"radios\"  class=\"leve_a_s\"";
							}
							temp+=" style=\"padding:0px 15px 0px 30 px;background-position:30px 50%;\" ><i class=\""+changeorganizetype(data.organizelist[i].type)+"\" onclick=\"getorganizelist(1,'"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"',this)\">"+data.organizelist[i].organizename+"</i><a class=\"radio\" onclick=\"changerealname(this,'"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"','"+data.organizelist[i].datacode+"','"+data.organizelist[i].type+"','"+data.organizelist[i].parentid+"')\">选择</a><div class=\"clear\"></div></span>";
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