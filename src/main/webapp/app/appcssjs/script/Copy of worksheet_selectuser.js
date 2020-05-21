/**
 * 
 */


var selecthtml_work="<link type=\"text/css\" rel=\"stylesheet\" href=\"../appcssjs/style/public.css\">"+
"<link type=\"text/css\" rel=\"stylesheet\" href=\"../appcssjs/style/page_public.css\">"+
"<link type=\"text/css\" rel=\"stylesheet\" href=\"../appcssjs/style/structure.css\">"+
"<div style=\"display: none;width:100%;height:100%\" id=\"worksheet_organizeiframe\">"+
		"<input type=\"hidden\" id=\"resouceid\"/>"+
"<input type=\"hidden\" id=\"resoucename\"/>"+
"<input type=\"hidden\" id=\"organizename\"/>"+
"<input type=\"hidden\" id=\"identityname\"/>"+
"<div class=\"head_box\">"+
"<a class=\"ico_back\" onclick=\"$('#worksheet_organizeiframe').hide();$('#htmlbody').show();\">返回</a>"+
	"<div class=\"name\">组织架构</div>"+
	"<div class=\"link_yellow\" id=\"confirmbtn_worksheet\" style=\"display: none;\" onclick=\"confirmexanine_work()\">确认</div>"+
	"</div>"+
"<div class=\"head_none\"></div>"+
"<div class=\"stru_list\">"+
	"<div class=\"search_box\"><input type=\"text\" class=\"bg\" placeholder=\"搜索\" oninput=\"searchuser_work(this)\"></div>"+
	"<div class=\"sel_classa\" id=\"orgainizediv_work\">"+
		"<ul id=\"organizelist_work\">"+
		"</ul>"+
	"</div>"+
"</div>"+
"</div>";

$('#htmlbody').after(selecthtml_work);
onloadSelect_work();
function onloadSelect_work(){
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getNextOrgainize?companyid='+userInfo.companyid,
		success:function(data){
			if(data.organizelist.length > 0){
				var temp="<div class=\"title\" id=\"companydiv_work\"><span onclick=\"getorganizelist_work('"+data.organizelist[0].organizeid+"','"+data.organizelist[0].organizename+"')\" name="+data.organizelist[0].organizeid+">"+data.organizelist[0].organizename+"</span></div>";
				$('#orgainizediv_work').prev().after(temp);
				
				getorganizelist_work(data.organizelist[0].organizeid,data.organizelist[0].organizename);
			}
			
		}
	})
}
function getorganizelist_work(organizeid,organizename){
	$('#resouceid').val("");
	$('#resoucename').val("");
	$('#confirmbtn_worksheet').hide();
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getNextOrgainize?companyid='+userInfo.companyid+'&organizeid='+organizeid,
		success:function(data){
			if(data.organizelist.length > 0){
				var temp="";
				for(var i=0;i<data.organizelist.length ;i++){
					temp+="<li onclick=\"getorganizelist_work('"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"')\"><span>"+data.organizelist[i].organizename+"</span></li>";
				}
				$('#organizelist_work').html(temp);
				var strorga="<em>&gt;</em><span onclick=\"getorganizelist_work('"+organizeid+"','"+organizename+"')\" name="+organizeid+">"+organizename+"</span>";
				var companyhtml=$('#companydiv_work').html();
				if(companyhtml.indexOf(organizename) == -1){
					$('#companydiv_work').append(strorga);
				}else{
					$('#companydiv_work').find('span[name='+organizeid+']').nextAll().remove();
				}
			}else{
				$('#organizelist_work').html("");
			}
			
			if(data.userlist.length > 0){
				var temp="";
				for(var i=0;i<data.userlist.length ;i++){
					if(data.userlist[i].userid != userInfo.userid){
						temp+="<li><i>"+data.userlist[i].realname+"</i><a class=\"radio\" onclick=\"changerealname_work(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"','"+data.userlist[i].organizename+"','"+data.userlist[i].identityname+"')\">选择</a></li>";
					}
				}
				$('#organizelist_work').append(temp);
			}
		}
	})
}
function changerealname_work(obj,userid,realname,organizename,identityname){
	$('a[class=radio_ed]').attr("class","radio");
	$(obj).attr("class","radio_ed");
	$('#resouceid').val(userid);
	$('#resoucename').val(realname);
	$('#organizename').val(organizename);
	$('#identityname').val(identityname);
	$('#confirmbtn_worksheet').show();
}
function confirmexanine_work(){
	$('#resoucename').text($('#resoucename').val());
	$('#resouceid').val($('#resouceid').val());
	$('#organizename').val($('#organizename').val());
	$('#identityname').val($('#identityname').val());
	$('#worksheet_organizeiframe').hide();
	$('#htmlbody').show();
}
function searchuser_work(obj){
	var realname =$(obj).val();
	if(realname.trim()!=""){
		$.ajax({
			type:'post',
			dataType:'json',
			url:projectpath+'/app/getUserBySearch?companyid='+userInfo.companyid+'&searchname='+realname,
			success:function(data){
				if(data.userlist.length > 0){
					var temp="";
					for(var i=0;i<data.userlist.length ;i++){
						temp+="<li><i>"+data.userlist[i].realname+"</i><a class=\"radio\" onclick=\"changerealname_work(this,'"+data.userlist[i].userid+"','"+data.userlist[i].realname+"')\">选择</a></li>";
					}
					$('#organizelist_work').html(temp);
				}
			}
		})
	}
}
