var organizeid = "";
var companyid = "";
var userid = "";
var templateid = "";

var userInfo = "";
JianKangCache.getGlobalData('userinfo',function(data){
	companyid = data.companyid;
	userid = data.userid;
	userInfo = data;
});

$(function(){
	
	//查询区域信息
	var obj = new Object();
	obj.userid = userid;
	obj.companyid = companyid;
	obj.type = 1;
	queryOrganizeList(obj);
	
	$('#shenqinren').text(userInfo.realname);
	
	$('#shenqingshijian').text(appBase.parseDateMinute(new Date()));
	
	$('#organize_option').delegate("li[name='organize_li']","click",function(){
		$("li[name='organize_li']").attr("class","");
		$(this).attr("class","active");
		$('#sure_btn').click();
	});
	
	$('#sure_btn').click(function(){
		var param = $('#organize_option li[class="active"]');
		if(param.length > 0){
			var id = $(param).attr("organizeid");
			var datacode = $(param).attr("datacode");
			changeorganizestore(datacode);
			if(organizeid != id){
				organizeid = id;
				$('#organizename').val($(param).attr("organizename"));
				$('#identityname').val($(param).attr("identityname"));
				showTemplate();
			}
		}
	});
	
	$("#organize_screen").click(function(){
		close_open_organizeDiv();
    });
	
	
	
	
	$("#project_mask").click(function(){
		close_open_starProject();
	});
	
	$('#temple_option').delegate('li[name="template_li"]','click',function(){
		$('li[name="template_li"]').attr("class","");
		$(this).attr("class","active");
		$('#template_btn').click();
	});
	$('#template_btn').click(function(){
		var param = $('#temple_option li[class="active"]');
		var template = $(param).attr("templateid");
		$('#quyu').val($(param).attr("templatename"));
		var docm = $('#'+template+'_project_li').html();
		if(docm == undefined || docm == null){
			templateid = template;
			$('#ev_list').empty();
			$('#sumstarnum').text(0);
			if(template != "" && template != undefined){
				showStarProject(template);
			}
		}
	});
	
});

//查询自评项目
function showTemplate(){
	var obj = new Object();
	obj.companyid = companyid;
	obj.organizeid = organizeid;
	obj.userid = userid;
	obj.type = 1;
	requestPost('/app/getInspectTemplate',obj,"temple",true,function(resultMap){
		if(resultMap != null && resultMap != undefined){
			if(resultMap.status == 0 || resultMap.status == '0'){
				var templatelist = resultMap.templatelist;
				var htm ='<li>请选择</li>';
				$.each(templatelist,function(i,map){
					var cls = "";
					if(i == 0){
						templateid = map.templateid;
						$('#quyu').val(map.templatename);
						showStarProject(map.templateid);
						cls = 'class="active"';
					}
					htm += '<li '+cls+' name="template_li" templateid=\''+map.templateid+'\' templatename=\''+map.templatename+'\'>'+map.templatename+'</li>';
				});
				$('#temple_option').html(htm);
			}
		}
	});
}

function showStarProject(templateid){
	var param = new Object();
	param.templateid = templateid;
	requestPost('/app/getTemplateProjectList',param,"project",true,function(resultMap){
		$('#ev_list').empty();
		$('#sumstarnum').text("0");
		if(resultMap != undefined && resultMap != null){
			var projectlist = resultMap.projectlist;
			if(projectlist != null && projectlist != ""){
				$.each(projectlist,function(i,map){
					var status = map.status;
					if(status == 1 || status == "1"){
						var project = '<li class="n_bor" id="'+map.projectid+'_project_li">'+
									    	'<span style="font-weight:bold;">'+map.projectname+'</span>'+
									    	'<b>'+
												'<input type="hidden" name="input_star" id="'+map.projectid+'_input" projectid="'+map.projectid+'" projectname="'+map.projectname+'" status="1" sumNum="0"/>'+
											'</b>'+
									        '<div class="clear"></div>'+
									    '</li>';
						$('#ev_list').append(project);
					}else{
						addproject(map.projectid,map.projectname);
					}
				});
				$('#star_level').text("一般");
				$('#showStarLevel').empty().html(showStarLevel(3,1));
			}
		}
	});
}
//添加项目
function addproject(id,name){
	var project = '<li class="n_bor" id="'+id+'_project_li">'+
				    	'<span>'+name+'</span>'+
				    	'<b>'+
							'<input type="hidden" name="input_star" id="'+id+'_input" projectid="'+id+'" projectname="'+name+'" status="0" sumNum="3"/>'+
							'<em onclick="xuanze(\''+id+'\',\'1\')"><img id="'+id+'_1" name="img_'+id+'" src="../appcssjs/images/public/start2.png"></em>'+
							'<em onclick="xuanze(\''+id+'\',\'2\')"><img id="'+id+'_2" name="img_'+id+'" src="../appcssjs/images/public/start2.png"></em>'+
							'<em onclick="xuanze(\''+id+'\',\'3\')"><img id="'+id+'_3" name="img_'+id+'" src="../appcssjs/images/public/start2.png"></em>'+
							'<em onclick="xuanze(\''+id+'\',\'4\')"><img id="'+id+'_4" name="img_'+id+'" src="../appcssjs/images/public/start3.png"></em>'+
							'<em onclick="xuanze(\''+id+'\',\'5\')"><img id="'+id+'_5" name="img_'+id+'" src="../appcssjs/images/public/start3.png"></em>'+
							'<font style="font-weight:normal; font-size:12px;" id="'+id+'_starlevel">&nbsp;&nbsp;一般</font>'+
						'</b>'+
				        '<div class="clear"></div>'+
				    '</li>';
	$('#ev_list').append(project);
	$('#sumstarnum').text(parseInt($('#sumstarnum').text())+3);
}

function dengji(level){
	level = parseInt(level);
	if(level == 1){
		return "很差";
	}else if(level == 2){
		return "不差";
	}else if(level == 3){
		return "一般";
	}else if(level == 4){
		return "&nbsp;&nbsp;&nbsp;好";
	}else if(level == 5){
		return "很好";
	}
}

function showStarLevel(sumstar,addnum){
	var intnum = parseInt(sumstar/addnum);
	var floatnum = (sumstar%addnum)/addnum;
	/*var shownum = intnum;//显示的等级
	if(floatnum > 0){
		shownum++;
	}
	var html = "";
	for(var i=0 ; i<5 ; i++){
		if(i<intnum){
			html += '<em><img src="../appcssjs/images/public/start2.png"></em>';
		}else if(floatnum <= 0.2 && floatnum != 0 && i == intnum){
			html += '<em><img src="../appcssjs/images/public/start_02.png"></em>';
		}else if(floatnum <= 0.4 && floatnum != 0 && i == intnum){
			html += '<em><img src="../appcssjs/images/public/start_04.png"></em>';
		}else if(floatnum <= 0.6 && floatnum != 0 && i == intnum){
			html += '<em><img src="../appcssjs/images/public/start_06.png"></em>';
		}else if(floatnum <= 0.8 && floatnum != 0 && i == intnum){
			html += '<em><img src="../appcssjs/images/public/start_08.png"></em>';
		}else if(floatnum > 0.8 && floatnum != 0 && i == intnum){
			html += '<em><img src="../appcssjs/images/public/start2.png"></em>';
		}else{
			html += '<em><img src="../appcssjs/images/public/start3.png"></em>';
		}
	}*/
	$('#star_level').html((intnum+floatnum).toFixed(1)+"分");
	$('#avgstarlevel').text((intnum+floatnum).toFixed(1));
	return "综合评价：";
}

//查询区域信息
function queryOrganizeList(obj){
	requestPost("/app/getShopListByReleaseRange",obj,"organize",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == '0' || resultMap.status == 0){
				var shoplist = resultMap.shoplist;
				if(shoplist != null && shoplist != undefined){
					showOrganizeInfo(shoplist);
				}
			}
		}
	});
}

function close_open_starProject(){
	var disk = $('#project_mask').css("display");
	var organize = $('#starProject_div').css("display");
	if(disk == "none" && organize == "none"){
		$('#project_mask').show();
		$('#starProject_div').show();
	}else{
		$('#project_mask').hide();
		$('#starProject_div').hide();
	}
}

//显示区域信息
function showOrganizeInfo(list){
	if(list.length > 0 && list != null){
		var html ='<li>请选择</li>';
		$.each(list,function(i,map){
			var cls = "";
			if(i==0){
				organizeid = map.organizeid;
				$('#organizename').val(map.organizename);
				cls = 'class="active"';
			}
			if(map.organizeid == userInfo.organizeid){
				changeorganizestore(map.datacode);
			}
			html += '<li '+cls+' name="organize_li" organizeid="'+map.organizeid+'" organizename="'+map.organizename+'" identityname="'+map.identityname+'" datacode="'+map.datacode+'">'+map.organizename+'</li>';
		});
		
		$('#organize_option').append(html);
		showTemplate();
	}
}



//打开或者关闭区域选择层
function close_open_organizeDiv(){
	var disk = $('#organize_screen').css("display");
	var organize = $('#organize_div').css("display");
	if(disk == "none" && organize == "none"){
		$('#organize_screen').show();
		$('#organize_div').show();
	}else{
		$('#organize_screen').hide();
		$('#organize_div').hide();
	}
}

//选择项目星级
function xuanze(i,k){
	$('img[name="img_'+i+'"]').attr("src","../appcssjs/images/public/start3.png");
	for(var j=1;j<=k;j++){
		$('#'+i+"_"+j).attr("src","../appcssjs/images/public/start2.png");
	}
	$('#'+i+'_input').attr("sumNum",k);
	$('#'+i+'_starlevel').html("&nbsp;&nbsp;"+dengji(k));
	
	var input = $('input[name="input_star"]');
	var sumstar = "0";
	var addnum = 0;
	$.each(input,function(k,item){
		var bn = $(item).attr("sumNum");
		sumstar = parseInt(bn) + parseInt(sumstar);
		if(parseInt(bn) != 0){
			addnum++;
		}
	});
	$('#sumstarnum').text(sumstar);
	
	$('#showStarLevel').html(showStarLevel(sumstar,addnum));
}

//新增餐前检查信息
function addEvaluate(){
	var param = {"starlist":[]}
	var input = $('input[name="input_star"]');
	$.each(input,function(k,item){
		var par = new Object();
		par.projectid = $(item).attr("projectid");
		par.starlevel = $(item).attr("sumNum");
		par.projectname = $(item).attr("projectname");
		par.type = $(item).attr("status");
		param.starlist[k]=par;
	});
	
	var obj = new Object();
	obj.sumstar = $('#sumstarnum').text();
	obj.companyid = companyid;
	obj.organizeid = organizeid;
	obj.examineuserid = $('#examineuserid').val();
	obj.createid = userid;
	obj.templateid = templateid;
	obj.templatename = $('#quyu').val();
	obj.organizename = $('#organizename').val();
	
	obj.userlist = $("#userlist").val();
	obj.CCusernames = $('#releaserange').text();
	obj.avgstarlevel = $('#avgstarlevel').text();
	obj.starlist = JSON.stringify(param);
	if(input.length <= 0){
		swal({
			title : "",
			text : "检查项目不能为空！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		});
		return false;
	}
	if(obj.examineuserid == "" || obj.examineuserid == null){
		swal({
			title : "",
			text : "抄送人不能为空！",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
		});
		return false;
	}
	$.ajax({
		url:projectpath+"/app/insertLeaveshop",
		beforeSend:function(a,b){
			ajaxbefore();
		},
		complete:function(a,b){
			ajaxcomplete();
		},
		type:"post",
		data:obj,
		success:function(data){
			if(data.status == 0 || data.status == '0'){
				swal({
					title : "",
					text : "新增成功",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					goBackPage();
				});
			}
		}
	});
}

//公共的查询方法
function requestPost(url,data,identifying,issynchroniz,callback){
	$.ajax({
		url:projectpath+url,
		data:data,
		async:issynchroniz,
		type:"post",
		beforeSend:function(){
			$('#jiazaizhong').show();
		},
		success:function(resultMap){
			//保存到当前路径缓存
			JianKangCache.setData("worksheet_",resultMap);
			callback(resultMap);
		},error:function(e){
			//取出缓存数据
			JianKangCache.getData("worksheet_",function(resultMap){
				callback(resultMap);
			});
		},complete:function(){
			$('#jiazaizhong').hide();
		}
	});
}