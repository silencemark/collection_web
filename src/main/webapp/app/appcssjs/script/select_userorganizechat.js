

var htmlOrganize = "<div style=\"display: none;width:100%;height:100%\" id=\"morechange_organizeiframe\">"+
					'<div class="head_box">'+
						'<input type="hidden" id="userlist"/>'+
						/*'<a class="ico_back" onclick="$(\'#morechange_organizeiframe\').hide();$(\'#htmlbody\').show();">返回</a>'+*/
					    '<div class="name">组织架构</div>'+
					    '<div class="link_yellow" id="morechange_confirmbtn" style="display: block;color:gray;" onclick="">建群</div>'+
					'</div>'+
					'<div class="head_none"></div>'+
					
					"<div class=\"msg_index\">"+
					"<div class=\"msg_nav\">"+
					"<span onclick=\"$('#morechange_organizeiframe').hide();showsharenum();onloaddata();$('#organizeiframe').hide();$('#htmlbody').show();\">聊天</span>"+
					"<span class=\"active\">组织架构</span>"+
					"<span class=\"last\" onclick=\"$('#organizeiframe').show();showsharenum();onloaddata();$('#morechange_organizeiframe').hide();\">通讯录</span>"+
					    "<div class=\"clear\"></div>"+
					"</div>"+
					"</div>"+
					
					'<div class="stru_list">'+
						'<div class="search_box"><input type="text" class="bg" placeholder="搜索" oninput="morechange_searchinfo(this)"></div>'+
					    '<div class="search_list" id="morechange_orgainizediv">'+
					    	'<ul id="morechange_chooseul">'+
					        '</ul>'+
					        '<div class="clear"></div>'+
					    '</div>'+
					    '<div class="title" id="morechange_organizetext" onclick="onloadDataorg()"></div>'+
					'</div>'+
					$("#menu_bottom_hg").html()+
					    '</div>';
$('#htmlbody').after(htmlOrganize);

onloadDataorg();
function onloadDataorg(){
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getNextOrgainize?companyid='+userInfo.companyid,
		success:function(data){
			if(data.organizelist.length > 0){
				$('#morechange_organizetext').text(data.organizelist[0].organizename);
				$('#morechange_organizetext').nextAll().remove();
				morechange_getorganizelist(0,data.organizelist[0].organizeid,data.organizelist[0].organizename,'#morechange_organizetext');
			}
			
		}
	})
}
function morechange_getorganizelist(num,organizeid,organizename,obj){
	if(obj !='#morechange_organizetext' &&  $(obj).parent().next("ul").length>0){
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
					temp+="<span class=\"leve_a_s\" style=\"padding:0px 15px 0px "+(num*30+15)+"px;background-position:"+(num*30+15)+"px 50%;\"><em class=\"em_down\"  onclick=\"morechange_getorganizelist("+(num+1)+",'"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"',this)\" >显示</em><a class=\"check\" onclick=\"chooseorganize('"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"',this)\" name=\"choose\">选择</a><i class=\""+changeorganizetype(data.organizelist[i].type)+"\" onclick=\"morechange_getorganizelist("+(num+1)+",'"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"',this)\" >"+data.organizelist[i].organizename+"</i><div class=\"clear\"></div></span>";
				}
				if(data.userlist.length > 0){
					for(var j=0;j<data.userlist.length ;j++){
						if(data.userlist[j].userid != userInfo.userid){
							temp+="<span class=\"leve_a_s\" style=\"padding:0px 15px 0px "+(num*30+20)+"px;background-position:"+(num*30+15)+"px 50%;\"><a class=\"check\"  onclick=\"chooseuser('"+data.userlist[j].userid+"','"+data.userlist[j].realname+"',this)\" name=\"choose\">选择</a><i class=\"ico_person\">"+data.userlist[j].realname+"-"+data.userlist[j].organizename+"</i><div class=\"clear\"></div></span>";
						}
					}
				}
        		temp+="</li></ul>";
        		if(obj =='#morechange_organizetext'){
					$(obj).after(temp);
        		}else{
        			$(obj).parent().after(temp);
        		}
			}
		}
	})
}
function chooseorganize(organizeid,organizename,obj){
	if($(obj).attr("class")=="check"){
		var organizeli="<li><input type=\"hidden\" name=\"organizeid\" value=\""+organizeid+"\" organizename='"+organizename+"'/><span>"+organizename+"</span><i onclick=\"deletechoose(this)\">×</i></li>";
		$('#morechange_chooseul').append(organizeli);
		$(obj).attr("class","check_ed");
		
		if($(obj).parent().next("ul").length > 0){
			$.each($(obj).parent().next("ul").find("a[name=choose]"), function(i,val){
				if($(this).attr("class")=="check"){
					$(this).click();
				}
			});
		}
	}else{
		if($(obj).parents("ul").prev().length > 0){
			$.each($(obj).parents("ul").prev().find("a[name=choose]"), function(i,val){
				if($(this).attr("class")=="check_ed"){
					$(this).click();
				}
			});
		}
		$('#morechange_chooseul').find("input[value="+organizeid+"]").parent().remove();
		$(obj).attr("class","check");
	}
	
	if($('#morechange_chooseul').children("li").length>0){
		//morechange_confirmexanine()
		$('#morechange_confirmbtn').attr("onclick","morechange_confirmexanine()");
		$('#morechange_confirmbtn').css({
			"color":"#ff9b30"
		});
		
	}else{
		$('#morechange_confirmbtn').attr("onclick","");
		$('#morechange_confirmbtn').css({
			"color":"gray"
		});
		//$('#morechange_confirmbtn').hide();
	}
	event.stopPropagation();
}
function chooseuser(userid,realname,obj){
	if($(obj).attr("class")=="check"){
		var userli="<li><input type=\"hidden\" name=\"userid\" value=\""+userid+"\" realname='"+realname+"'/><span>"+realname+"</span><i onclick=\"deletechoose(this)\">×</i></li>";
		$('#morechange_chooseul').append(userli);
		$(obj).attr("class","check_ed");
		
		if($(obj).parent().next("ul").length > 0){
			$.each($(obj).parent().next("ul").find("a[name=choose]"), function(i,val){
				if($(this).attr("class")=="check"){
					$(this).click();
				}
			});
		}
	}else{
		if($(obj).parents("ul").prev().length > 0){
			$.each($(obj).parents("ul").prev().find("a[name=choose]"), function(i,val){
				if($(this).attr("class")=="check_ed"){
					$(this).click();
				}
			});
		}
		$('#morechange_chooseul').find("input[value="+userid+"]").parent().remove();
		$(obj).attr("class","check");
	}
	if($('#morechange_chooseul').children("li").length>0){
		//$('#morechange_confirmbtn').show();
		$('#morechange_confirmbtn').attr("onclick","morechange_confirmexanine()");
		$('#morechange_confirmbtn').css({
			"color":"#ff9b30"
		});
	}else{
		$('#morechange_confirmbtn').attr("onclick","");
		$('#morechange_confirmbtn').css({
			"color":"gray"
		});
		//$('#morechange_confirmbtn').hide();
	}
	event.stopPropagation();
}
function deletechoose(obj){
	$("i:contains('"+$(obj).prev().text()+"')").prev().click();
	$(obj).parent().remove();
}
function morechange_confirmexanine(){
	var alldata={"userlist":[]};
	var organizename_realname = "";
	$('input[name=userid]').each(function(index){
		var userlist={};
		userlist['userid']=$(this).val();
		organizename_realname += $(this).attr("realname")+"，";
		alldata.userlist.push(userlist); 
	})
	$('input[name=organizeid]').each(function(index){
		var organizelist={};
		organizelist['organizeid']=$(this).val();
		organizename_realname += $(this).attr("organizename")+"，";
		alldata.userlist.push(organizelist);
	})
	$('#userlist').val(JSON.stringify(alldata));
	
/*	$('#morechange_organizeiframe').hide();
	$('#htmlbody').show();*/
	//回调 创建群组的方法
	creategroup(JSON.stringify(alldata));
}

function morechange_searchinfo(obj){
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
						temp+="<span class=\"leve_a_s\" style=\"padding:0px 15px 0px 15px;background-position:15px 50%;\"><em class=\"em_down\"  onclick=\"morechange_getorganizelist(1,'"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"',this)\" >显示</em><a class=\"check\" onclick=\"chooseorganize('"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"',this)\" name=\"choose\">选择</a><i class=\""+changeorganizetype(data.organizelist[i].type)+"\" onclick=\"morechange_getorganizelist(1,'"+data.organizelist[i].organizeid+"','"+data.organizelist[i].organizename+"',this)\" >"+data.organizelist[i].organizename+"</i><div class=\"clear\"></div></span>";
					}
					if(data.userlist.length > 0){
						for(var j=0;j<data.userlist.length ;j++){
							temp+="<span class=\"leve_a_s\" style=\"padding:0px 15px 0px 15px;background-position:15px 50%;\"><a class=\"check\"  onclick=\"chooseuser('"+data.userlist[j].userid+"','"+data.userlist[j].realname+"',this)\" name=\"choose\">选择</a><i class=\"ico_person\">"+data.userlist[j].realname+"-"+data.userlist[j].organizename+"</i><div class=\"clear\"></div></span>";
						}
					}
	        		temp+="</li></ul>";
	        		$('#morechange_organizetext').nextAll().remove();
					$('#morechange_organizetext').after(temp);
				}
				else{
					$('#morechange_organizetext').nextAll().remove();
				}
			}
		})
	}else{
		onloadDataorg();
	}
}

