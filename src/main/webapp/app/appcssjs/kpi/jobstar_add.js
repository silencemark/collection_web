var organizeid = "";
var companyid = "";
var userid = "";
var templateid = "";
$(function(){
	JianKangCache.getGlobalData('userinfo',function(data){
		companyid = data.companyid;
		userid = data.userid;
		//查询区域信息
		var obj = new Object();
		obj.userid = data.userid;
		obj.companyid = data.companyid;
		obj.type = 1;
		queryOrganizeList(obj);
		
		$('#shenqinren').text(data.realname);
	});
	$('#shenqingshijian').text(appBase.parseDateMinute(new Date()));
	
	
	
	$('#organize_option').delegate("li[name='organize_li']","click",function(){
		$("li[name='organize_li']").attr("class","");
		$(this).attr("class","active");
		$('#sure_btn').click();
	});
	
	$('#sure_btn').click(function(){
		var param = $('#organize_option li[class="active"]');
		if(param.length > 0){
			organizeid = $(param).attr("organizeid");
			$('#organizename').val($(param).attr("organizename"));
			queryTemplateList(organizeid);
		}
	});
	
	$("#organize_mask").click(function(){
		close_open_organizeDiv();
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
	obj.templateid = templateid;
	requestPost('/app/getStarProjectByOrganize',obj,"add",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			$('#evaluate_list').empty();
			$('#sumstarnum').text("0");
			var projectlist = resultMap.projectlist;
			if(projectlist != null && projectlist != ""){
				var htm = "";
				var cont = 0;
				$.each(projectlist,function(i,map){
					var status = map.status;
					if(status == "1" || status == 1){
						htm += '<li>'+
									'<span style="font-weight:bold;">'+map.projectname+'</span>'+
									'<b><input type="hidden" name="input_star" id="'+i+'_input" projectid="'+map.projectid+'" status="1" projectname="'+map.projectname+'" sumNum="0"/></b>'+
								'</li>';
					}else{
						htm += '<li>'+
									'<span>'+map.projectname+'</span>'+
									'<b>'+
										'<input type="hidden" name="input_star" id="'+i+'_input" projectid="'+map.projectid+'" status="0" projectname="'+map.projectname+'" sumNum="3"/>'+
										'<em onclick="xuanze(\''+i+'\',\'1\')"><img id="'+i+'_1" name="img_'+i+'" src="../appcssjs/images/public/start2.png"></em>'+
										'<em onclick="xuanze(\''+i+'\',\'2\')"><img id="'+i+'_2" name="img_'+i+'" src="../appcssjs/images/public/start2.png"></em>'+
										'<em onclick="xuanze(\''+i+'\',\'3\')"><img id="'+i+'_3" name="img_'+i+'" src="../appcssjs/images/public/start2.png"></em>'+
										'<em onclick="xuanze(\''+i+'\',\'4\')"><img id="'+i+'_4" name="img_'+i+'" src="../appcssjs/images/public/start3.png"></em>'+
										'<em onclick="xuanze(\''+i+'\',\'5\')"><img id="'+i+'_5" name="img_'+i+'" src="../appcssjs/images/public/start3.png"></em>'+
										'<font style="font-weight:normal; font-size:12px;" id="'+i+'_starlevel">&nbsp;&nbsp;三星</font>'+
									'</b>'+
								'</li>';
						cont +=3;
					}
				});
				$('#evaluate_list').append(htm);
				$('#sumstarnum').text(cont);
			}
		}
	});
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
			var cls="";
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
	param.type = 1;
	param.companyid = companyid;
	param.organizeid = organizeid;
	param.userid = userid;
	requestPost("/app/getStarEvaluateTemplateList",param,"organize",true,function(resultMap){
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

function dengji(level){
	level = parseInt(level);
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
	$.each(input,function(k,item){
		var bn = $(item).attr("sumNum");
		sumstar = parseInt(bn) + parseInt(sumstar);
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
	obj.templatename = $('#identityname').val();
	obj.organizename = $('#organizename').val();
	
	obj.starlist = JSON.stringify(param);
	
	obj.userlist = $("#userlist").val();
	obj.CCusernames = $('#releaserange').text();
	
	if(input.length <= 0){
		swal({
			title : "",
			text : "审核项目不能为空！",
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
		url:projectpath+"/app/insertEvaluate",
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