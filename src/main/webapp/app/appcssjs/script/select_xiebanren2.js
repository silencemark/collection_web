

var htmlOrganize = "<link type=\"text/css\" rel=\"stylesheet\" href=\"../appcssjs/style/structure.css\">"+
					"<div style=\"display: none;width:100%;height:100%\" id=\"CCmorechange_organizeiframe\">"+
					'<div class="head_box">'+
						'<input type="hidden" id="CCuserlist"/>'+
						'<a class="ico_back" onclick="$(\'#CCmorechange_organizeiframe\').hide();$(\'#htmlbody\').show();">返回</a>'+
					    '<div class="name">组织架构</div>'+
					    '<div class="link_yellow" id="CCmorechange_confirmbtn" style="display: none;" onclick="CCmorechange_confirmexanine()">确认</div>'+
					'</div>'+
					'<div class="head_none"></div>'+
					'<div class="stru_list">'+
						'<div class="search_box"><input type="text" class="bg" placeholder="搜索" oninput="CCmorechange_searchinfo(this)"></div>'+
					    '<div class="search_list" id="CCmorechange_orgainizediv">'+
					    	'<ul id="CCmorechange_chooseul">'+
					        '</ul>'+
					        '<div class="clear"></div>'+
					    '</div>'+
					    '<div class="title" id="CCmorechange_organizetext" onclick="CConloadDataorg()">上海餐饮集团有限公司</div>'+
					'</div></div>';
$('#htmlbody').after(htmlOrganize);

CConloadDataorg();
function CConloadDataorg(){
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getNextOrgainize?companyid='+userInfo.companyid,
		success:function(data){
			if(data.organizelist.length > 0){
				$('#CCmorechange_organizetext').text(data.organizelist[0].organizename);
				$('#CCmorechange_organizetext').nextAll().remove();
				CCmorechange_getorganizelist(0,data.organizelist[0].organizeid,data.organizelist[0].organizename,'#CCmorechange_organizetext');
			}
		}
	})
}
function CCmorechange_getorganizelist(num,organizeid,organizename,obj){
	if(obj !='#CCmorechange_organizetext' &&  $(obj).parent().next("ul").length>0){
		if($(obj).parent().find("em").attr("class")=="em_down"){
			$(obj).parent().next("ul").show();
			$(obj).parent().find("em").attr("class","em_up");
			$(obj).parent().find("em").text("隐藏");
		}else{
			$(obj).parent().next("ul").hide();
			$(obj).parent().find("em").attr("class","em_down");
			$(obj).parent().find("em").text("显示");
		}
		return;
	}
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getNextOrgainize?companyid='+userInfo.companyid+'&organizeid='+organizeid,
		success:function(data){
			if(data.organizelist.length > 0 || data.userlist.length >0){
				$(obj).parent().find("em").attr("class","em_up");
				$(obj).parent().find("em").text("隐藏");
				var temp="";
				temp+="<ul class=\"quyu\">";
				temp+="<li class=\"leve_a\" >";
				for(var i=0;i<data.organizelist.length ;i++){
					temp+="<span class=\"leve_a_s\" style=\"padding:0px 15px 0px "+(num*15+15)+"px;background-position:"+(num*15+15)+"px 50%;\"><em class=\"em_down\"  onclick=\"CCmorechange_getorganizelist("+(num+1)+",'"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"',this)\" >显示</em><i class=\""+changeorganizetype(data.organizelist[i].type)+"\" onclick=\"CCmorechange_getorganizelist("+(num+1)+",'"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"',this)\" >"+data.organizelist[i].organizename+"</i><div class=\"clear\"></div></span>";
				}
				if(data.userlist.length > 0){
					for(var j=0;j<data.userlist.length ;j++){
						if(data.userlist[j].userid != userInfo.userid){
							temp+="<span class=\"leve_a_s\" style=\"padding:0px 15px 0px "+(num*15+20)+"px;background-position:"+(num*15+15)+"px 50%;\"><a class=\"check\"  onclick=\"CCchooseuser('"+data.userlist[j].userid+"','"+data.userlist[j].realname+"',this)\" name=\"choose\">选择</a><i class=\"ico_person\">"+data.userlist[j].realname+"</i><div class=\"clear\"></div></span>";
						}
					}
				}
        		temp+="</li></ul>";
        		if(obj =='#CCmorechange_organizetext'){
					$(obj).after(temp);
        		}else{
        			$(obj).parent().after(temp);
        		}
			}
		}
	})
}
function CCchooseuser(userid,realname,obj){
	if($(obj).attr("class")=="check"){
		var userli="<li><input type=\"hidden\" name=\"CCuserid\" value=\""+userid+"\" realname='"+realname+"'/><span>"+realname+"</span><i onclick=\"deletechoose(this)\">×</i></li>";
		$('#CCmorechange_chooseul').append(userli);
		$(obj).attr("class","check_ed");
	}else{
		$('#CCmorechange_chooseul').find("input[value="+userid+"]").parent().remove();
		$(obj).attr("class","check");
	}
	if($('#CCmorechange_chooseul').children("li").length>0){
		$('#CCmorechange_confirmbtn').show();
	}else{
		$('#CCmorechange_confirmbtn').hide();
	}
	event.stopPropagation();
}
function deletechoose(obj){
	$("i:contains('"+$(obj).prev().text()+"')").prev().click();
	$(obj).parent().remove();
}
function CCmorechange_confirmexanine(){
	var alldata={"userlist":[]};
	var organizename_realname = "";
	var userparam = $('input[name=CCuserid]');
	$.each(userparam,function(i,index){
		var userlist={};
		userlist['userid']=$(index).val();
		organizename_realname += $(index).attr("realname")+"，";
		alldata.userlist.push(userlist); 
	})
	$('#CCuserlist').val(JSON.stringify(alldata));
	var names = organizename_realname.substring(0,(organizename_realname.length - 1))
	$('#CCusernames').text(names);
	
	$('#CCmorechange_organizeiframe').hide();
	$('#htmlbody').show();
}

function CCmorechange_searchinfo(obj){
	var searchname =$(obj).val();
	if(searchname.trim()!=""){
		$.ajax({
			type:'post',
			dataType:'json',
			url:projectpath+'/app/getOrganizeUserBySearch?companyid='+userInfo.companyid+'&searchname='+searchname,
			success:function(data){
				if(data.organizelist.length > 0 || data.userlist.length >0){
					var temp="";
					temp+="<ul class=\"quyu\">";
					temp+="<li class=\"leve_a\" >";
					for(var i=0;i<data.organizelist.length ;i++){
						temp+="<span class=\"leve_a_s\" style=\"padding:0px 15px 0px 15px;background-position:15px 50%;\"><em class=\"em_down\"  onclick=\"CCmorechange_getorganizelist(1,'"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"',this)\" >显示</em><i class=\""+changeorganizetype(data.organizelist[i].type)+"\" onclick=\"CCmorechange_getorganizelist(1,'"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"',this)\" >"+data.organizelist[i].organizename+"</i><div class=\"clear\"></div></span>";
					}
					if(data.userlist.length > 0){
						for(var j=0;j<data.userlist.length ;j++){
							temp+="<span class=\"leve_a_s\" style=\"padding:0px 15px 0px 15px;background-position:15px 50%;\"><a class=\"check\"  onclick=\"CCchooseuser('"+data.userlist[j].userid+"','"+data.userlist[j].realname+"',this)\" name=\"choose\">选择</a><i class=\"ico_person\">"+data.userlist[j].realname+"-"+data.userlist[j].organizename+"</i><div class=\"clear\"></div></span>";
						}
					}
	        		temp+="</li></ul>";
	        		$('#CCmorechange_organizetext').nextAll().remove();
					$('#CCmorechange_organizetext').after(temp);
				}
				else{
					$('#CCmorechange_organizetext').nextAll().remove();
				}
			}
		})
	}else{
		CConloadDataorg();
	}
}

