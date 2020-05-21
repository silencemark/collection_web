var organizeid = "";
var companyid = "";
var userid = "";
var templateid = "";
$(function(){
	
	companyid = $('#companyid').val();
	userid = $('#userid').val();
	
	var obj = new Object();
	obj.userid = userid;
	obj.companyid = companyid;
	obj.type = 1;
	queryOrganizeList(obj);
	
	$('#shenqing_time').text(appBase.parseDateMinute(new Date()));
	
	$('#organize_option').change(function(){
		organizeid = $(this).val();
		$('#store_ul').empty();
		$('#evaluate_list').html("");
		$('#sumstarnum').text("0");
		queryTemplateList(organizeid);
	});
	
	
	$('#store_ul').change(function(){
		var id = $(this).val();
		templateid = id;
		showStarProject();
	});
});

//查询自评项目
function showStarProject(){
	var obj = new Object();
	obj.companyid = companyid;
	obj.templateid = templateid;
	requestPost('/pc/getStarProjectByOrganize',obj,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			$('#evaluate_list').empty();
			$('#sumstarnum').text("0");
			var projectlist = resultMap.projectlist;
			if(projectlist != null && projectlist != ""){
				var htm = "";
				var cont = 0;
				$.each(projectlist,function(i,map){
					var status = map.status;
					if(status == 1 || status == "1"){
						htm +='<tr>'+
									'<td style="font-weight:bold; font-size:15px;">'+map.projectname+'</td>'+
									'<td class="t_r"><div class="star_box">'+
										'<input type="hidden" name="input_star" id="'+i+'_input" projectid="'+map.projectid+'" projectname="'+map.projectname+'" status="1" sumNum="0"/>'+
									'</div></td>'+
								'</tr>';
					}else{
						htm += '<tr>'+
									'<td>'+map.projectname+'</td>'+
									'<td class="t_r"><div class="star_box">'+
										'<input type="hidden" name="input_star" id="'+i+'_input" projectid="'+map.projectid+'" projectname="'+map.projectname+'" status="0" sumNum="3"/>'+
										'<a class="a_star" onclick="xuanze(\''+i+'\',\'1\')"><img id="'+i+'_1" name="img_'+i+'" src="/userbackstage/images/pc_page/start2.png"></a>'+
										'<a class="a_star" onclick="xuanze(\''+i+'\',\'2\')"><img id="'+i+'_2" name="img_'+i+'" src="/userbackstage/images/pc_page/start2.png"></a>'+
										'<a class="a_star" onclick="xuanze(\''+i+'\',\'3\')"><img id="'+i+'_3" name="img_'+i+'" src="/userbackstage/images/pc_page/start2.png"></a>'+
										'<a class="a_star" onclick="xuanze(\''+i+'\',\'4\')"><img id="'+i+'_4" name="img_'+i+'" src="/userbackstage/images/pc_page/start3.png"></a>'+
										'<a class="a_star" onclick="xuanze(\''+i+'\',\'5\')"><img id="'+i+'_5" name="img_'+i+'" src="/userbackstage/images/pc_page/start3.png"></a>'+
										'<a style="margin-left:20px;" id="'+i+'_starlevel">三星</a>'+
									'</div></td>'+
								'</tr>';
						cont +=3;
					}
				});
				$('#evaluate_list').prepend(htm);
				$('#sumstarnum').text(cont);
			}
		}
	});
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

//查询区域信息
function queryOrganizeList(obj){
	requestPost("/pc/getOrganizeListByReleaseRange",obj,function(resultMap){
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
		var html = "";
		$.each(list,function(i,map){
			var cls="";
			if(i==0){
				//
				organizeid = map.organizeid;
				queryTemplateList(organizeid);
				cls='selected="selected"';
			}
			html += '<option '+cls+' value="'+map.organizeid+'">'+map.organizename+'</option>';
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
	requestPost("/pc/getStarEvaluateTemplateList",param,function(resultMap){
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
		var html="";
		$.each(list,function(i,map){
			var cls="";
			if(i==0){
				//
				templateid = map.templateid;
				cls='selected="selected"';
				showStarProject();
			}
			html += '<option '+cls+' value="'+map.templateid+'">'+map.templatename+'</option>';
		});
		$('#store_ul').html(html);
	}
}

//选择项目星级
function xuanze(i,k){
	$('img[name="img_'+i+'"]').attr("src","/userbackstage/images/pc_page/start3.png");
	for(var j=1;j<=k;j++){
		$('#'+i+"_"+j).attr("src","/userbackstage/images/pc_page/start2.png");
	}
	$('#'+i+'_input').attr("sumNum",k);
	$('#'+i+'_starlevel').html(dengji(k));
	
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
	obj.templatename = $('#store_ul').text();
	obj.organizename = $('#organize_option').text();
	
	obj.createid = userid;
	obj.starlist = JSON.stringify(param);
	obj.templateid =  templateid;
	
	obj.userlist = $("#CCuseridlist").val();
	obj.CCusernames = $('#CCusernamelist').val();
	
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
		url:"/pc/insertEvaluate",
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
					window.history.go(-1);
				});
			}
		}
	});
}

