var organizeid = "";
var companyid = "";
var userid = "";
var templateid = "";
$(function(){
	JianKangCache.getGlobalData('userinfo',function(data){
		companyid = data.companyid;
		userid = data.userid;
		
		var obj = new Object();
		obj.userid = data.userid;
		obj.companyid = companyid;
		obj.type = 2;
		queryOrganizeList(obj);
		$('#shenqingren').text(data.realname);
	});
	$('#shenqing_time').text(appBase.parseDateMinute(new Date()));
	
	$('#organize_option').delegate("li[name='organize_li']","click",function(){
		$("li[name='organize_li']").attr("class","");
		$(this).attr("class","active");
		$('#sure_btn').click();
	});
	
	$('#sure_btn').click(function(){
		var param = $('#organize_option li[class="active"]');
		organizeid = $(param).attr("organizeid");
		$('#organizename').val($(param).attr("organizename"));
		queryTemplateList(organizeid);
		$('#ev_list').empty();
		$('#sumstarnum').text("0");
	});
	
	$("#organize_mask").click(function(){
		close_open_organizeDiv();
    });
	
	$("#project_mask").click(function(){
		close_open_starProject();
	});
	
	$('#evaluate_list').delegate("li[name='project_li']","click",function(){
		$("li[name='project_li']").attr("class","");
		$(this).attr("class","active");
		$('#starProject_btn').click();
	});
	$('#starProject_btn').click(function(){
		var param = $('#evaluate_list li[class="active"]');
		
		var projectid = $(param).attr("projectid");
		var projectname = $(param).attr("projectname");
		if(projectid != undefined && projectname != undefined){
			var htl = $('#'+projectid+'_project_li').html();
			if(htl == null || htl == undefined){
				addproject(projectid,projectname);
			}
		}
	});
	
	
	
	$('#store_mask').click(function(){
		storeDiv_closeOrOpen();
	});
	$('#store_ul').delegate("li[name='store_li']","click",function(){
		$('li[name="store_li"]').attr("class","");
		$(this).attr("class","active");
		$('#store_sure_btn').click();
	});
	$('#store_sure_btn').click(function(){
		var param = $('#store_ul li[class="active"]');
		if(param.length > 0){
			var id = $(param).attr("templateid");
			if(templateid != id){
				$('#ev_list').empty();
				$('#sumstarnum').text(0);
			}
			var templatename = $(param).attr("templatename");
			$('#identityname').val(templatename);
			templateid = id;
			showStarProject();
		}
	});
});

//查询自评项目
function showStarProject(){
	var obj = new Object();
	obj.companyid = companyid;
	obj.templateid = templateid;//--------------------
	requestPost('/app/getStarProjectByOrganize',obj,"add",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			$('#evaluate_list').empty();
			var projectlist = resultMap.projectlist;
			if(projectlist != null && projectlist != ""){
				var htm ='<li>请选择</li>';
				$.each(projectlist,function(i,map){
					var status = map.status;
					if(status == 1 || status == "1"){
						
					}else{
						htm += '<li name="project_li" projectid=\''+map.projectid+'\' projectname=\''+map.projectname+'\'>'+map.projectname+'</li>';
					}
				});
				$('#evaluate_list').append(htm);
			}
		}
	});
}
//添加项目
function addproject(id,name){
	var project = '<li class="n_bor" id="'+id+'_project_li">'+
				    	'<i onclick="deleproject(\''+id+'\')"><img src="../appcssjs/images/public/ico_del.png"></i>'+
				    	'<span>'+name+'</span>'+
				    	'<b>'+
							'<input type="hidden" name="input_star" id="'+id+'_input" projectid="'+id+'" status="0" projectname="'+name+'" sumNum="3"/>'+
							'<em onclick="xuanze(\''+id+'\',\'1\')"><img id="'+id+'_1" name="img_'+id+'" src="../appcssjs/images/public/start2.png"></em>'+
							'<em onclick="xuanze(\''+id+'\',\'2\')"><img id="'+id+'_2" name="img_'+id+'" src="../appcssjs/images/public/start2.png"></em>'+
							'<em onclick="xuanze(\''+id+'\',\'3\')"><img id="'+id+'_3" name="img_'+id+'" src="../appcssjs/images/public/start2.png"></em>'+
							'<em onclick="xuanze(\''+id+'\',\'4\')"><img id="'+id+'_4" name="img_'+id+'" src="../appcssjs/images/public/start3.png"></em>'+
							'<em onclick="xuanze(\''+id+'\',\'5\')"><img id="'+id+'_5" name="img_'+id+'" src="../appcssjs/images/public/start3.png"></em>'+
							'<font style="font-weight:normal; font-size:12px;" id="'+id+'_starlevel">&nbsp;&nbsp;三星</font>'+
						'</b>'+
				        '<div class="clear"></div>'+
				        '<div class="text"><textarea placeholder="请输入详细内容，不超过200字" id="'+id+'_textarea"></textarea></div>'+
				    '</li>';
	$('#ev_list').append(project);
	$('#sumstarnum').text(parseInt($('#sumstarnum').text())+3);
}

function dengji(level){
	level = parseInt(level);
	/*if(level == 1){
		return "很差";
	}else if(level == 2){
		return "不差";
	}else if(level == 3){
		return "一般";
	}else if(level == 4){
		return "&nbsp;&nbsp;&nbsp;好";
	}else if(level == 5){
		return "很好";
	}*/
	if(level == 1){
		return "一星";
	}else if(level == 2){
		return "二星";
	}else if(level == 3){
		return "三星";
	}else if(level == 4){
		return "四星";
	}else if(level == 5){
		return "五星";
	}
}

//删除栏目
function deleproject(id){
	$('#'+id+'_project_li').remove();
	
	var input = $('input[name="input_star"]');
	var sumstar = "0";
	$.each(input,function(k,item){
		sumstar = parseInt($(item).attr("sumNum")) + parseInt(sumstar);
	});
	$('#sumstarnum').text(sumstar);
}

//查询区域信息
function queryOrganizeList(obj){
	requestPost("/app/getOrganizeListByReleaseRange",obj,"organize",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == '0' || resultMap.status == 0){
				var organizelist = resultMap.organizelist;
				showOrganizeInfo(organizelist);
			}
		}
	});
}

//显示区域信息
function showOrganizeInfo(list){
	if(list.length > 0 && list != null){
		var html ='<li>请选择</li>';
		$.each(list,function(i,map){
			var cls = "";
			if(i==0){
				//
				organizeid = map.organizeid;
				$('#organizename').val(map.organizename);
				queryTemplateList(organizeid);
				cls='class="active"';
			}
			html += '<li '+cls+' name="organize_li" organizeid="'+map.organizeid+'" organizename="'+map.organizename+'">'+map.organizename+'</li>';
		});
		$('#organize_option').append(html);
	}
}

function queryTemplateList(organizeid){
	var param = new Object();
	param.type = 2;
	param.companyid = companyid;
	param.organizeid = organizeid;
	param.userid = userid;
	requestPost("/app/getStarEvaluateTemplateList",param,"organize",true,function(resultMap){
		$('#store_ul').empty();
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == '0' || resultMap.status == 0){
				var templatelist = resultMap.templatelist;
				showStoreList(templatelist);
			}
		}
	});
}


function showStoreList(list){
	if(list.length > 0 && list != null){
		var html='<li>请选择</li>';
		$.each(list,function(i,map){
			var cls="";
			if(i==0){
				//
				templateid = map.templateid;
				$('#identityname').val(map.templatename);
				cls='class="active"';
				showStarProject();
			}
			html += '<li '+cls+' name="store_li" templateid="'+map.templateid+'" templatename="'+map.templatename+'">'+map.templatename+'</li>';
		});
		$('#store_ul').html(html);
	}
}

//打开或者关闭区域选择层
function close_open_organizeDiv(){
	var disk = $('#organize_mask').css("display");
	var organize = $('#organize_div').css("display");
	if(disk == "none" && organize == "none"){
		$('#organize_mask').show();
		$('#organize_div').show();
	}else{
		$('#organize_mask').hide();
		$('#organize_div').hide();
	}
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

function storeDiv_closeOrOpen(){
	var disk = $('#store_mask').css("display");
	var store_div = $('#store_div').css("display");
	if(disk == "none" && store_div == "none"){
		$('#store_mask').show();
		$('#store_div').show();
	}else{
		$('#store_mask').hide();
		$('#store_div').hide();
	}
}

//选择项目星级```````````````````````````````````````````````````````````
function xuanze(i,k){
	$('img[name="img_'+i+'"]').attr("src","../appcssjs/images/public/start3.png");
	for(var j=1;j<=k;j++){
		$('#'+i+"_"+j).attr("src","../appcssjs/images/public/start2.png");
	}
	$('#'+i+'_input').attr("sumNum",k);
	$('#'+i+'_starlevel').html("&nbsp;&nbsp;"+dengji(k));
	
	var input = $('input[name="input_star"]');
	var sumstar = "0";
	$.each(input,function(k,item){
		sumstar = parseInt($(item).attr("sumNum")) + parseInt(sumstar);
	});
	$('#sumstarnum').text(sumstar);
}

//新增自评
function addEvaluate(){
	var param = {"starlist":[]}
	var input = $('input[name="input_star"]');
	$.each(input,function(k,item){
		var par = new Object();
		par.projectid = $(item).attr("projectid");
		par.starlevel = $(item).attr("sumNum");
		par.description = $('#'+par.projectid+'_textarea').val();
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
	obj.templateid =templateid;
	obj.templatename = $('#identityname').val();
	obj.organizename = $('#organizename').val();
	obj.userlist = $("#userlist").val();
	obj.CCusernames = $('#releaserange').text();
	
	obj.starlist = JSON.stringify(param);
	
	if(input.length <= 0){
		swal({
			title : "",
			text : "自评项目不能为空！",
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
			text : "审核人不能为空！",
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
		url:projectpath+"/app/insertOverallvaluate",
		type:"post",
		data:obj,
		beforeSend:function(a,b){
			ajaxbefore();
		},
		complete:function(a,b){
			ajaxcomplete();
		},
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
			JianKangCache.setData("kpi_",resultMap);
			callback(resultMap);
		},error:function(e){
			//取出缓存数据
			JianKangCache.getData("kpi_",function(resultMap){
				callback(resultMap);
			});
		},complete:function(){
			$('#jiazaizhong').hide();
		}
	});
}