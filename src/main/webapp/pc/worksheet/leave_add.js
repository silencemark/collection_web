var organizeid = "";
var companyid = "";
var userid = "";
var templateid = "";

$(function(){
	
	companyid = $('#companyid').val();
	userid = $('#userid').val();
	organizeid = $('#organizeid').val();
	
	//查询区域信息
	var obj = new Object();
	obj.userid = userid;
	obj.companyid = companyid;
	obj.type = 1;
	queryOrganizeList(obj);
	
	$('#organize_option').change(function(){
		organizeid = $(this).val();
		$('#ev_list').empty();
		showTemplate();
	});
	
	$('#temple_option').change(function(){
		var template = $(this).val();
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
	requestPost('/pc/getInspectTemplate',obj,function(resultMap){
		if(resultMap != null && resultMap != undefined){
			if(resultMap.status == 0 || resultMap.status == '0'){
				var templatelist = resultMap.templatelist;
				var htm = "";
				$.each(templatelist,function(i,map){
					var cls = "";
					if(i == 0){
						templateid = map.templateid;
						showStarProject(map.templateid);
						cls = 'selected="selected"';
					}
					htm += '<option '+cls+' value=\''+map.templateid+'\'>'+map.templatename+'</option>';
				});
				$('#temple_option').empty().html(htm);
			}
		}
	});
}

function showStarProject(templateid){
	var param = new Object();
	param.templateid = templateid;
	requestPost('/pc/getTemplateProjectList',param,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			var projectlist = resultMap.projectlist;
			if(projectlist != null && projectlist != ""){
				$.each(projectlist,function(i,map){
					var status = map.status;
					if(status == 1 || status == "1"){
						var project = '<tr class="n_bor" id="'+map.projectid+'_project_li">'+
									    	'<td style="font-weight:bold;">'+map.projectname+'</td>'+
									    	'<td class="t_r"><div class="star_box">'+
												'<input type="hidden" name="input_star" id="'+map.projectid+'_input" projectid="'+map.projectid+'" projectname="'+map.projectname+'" status="1" sumNum="0"/>'+
											'</div></td>'+
									    '</tr>';
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
	var project = '<tr class="n_bor" id="'+id+'_project_li">'+
				    	'<td>'+name+'</td>'+
				    	'<td class="t_r"><div class="star_box">'+
							'<input type="hidden" name="input_star" id="'+id+'_input" projectid="'+id+'" projectname="'+name+'" status="0" sumNum="3"/>'+
							'<a onclick="xuanze(\''+id+'\',\'1\')"><img id="'+id+'_1" name="img_'+id+'" src="'+projectpath+'/userbackstage/images/pc_page/start2.png"></a>'+
							'<a onclick="xuanze(\''+id+'\',\'2\')"><img id="'+id+'_2" name="img_'+id+'" src="'+projectpath+'/userbackstage/images/pc_page/start2.png"></a>'+
							'<a onclick="xuanze(\''+id+'\',\'3\')"><img id="'+id+'_3" name="img_'+id+'" src="'+projectpath+'/userbackstage/images/pc_page/start2.png"></a>'+
							'<a onclick="xuanze(\''+id+'\',\'4\')"><img id="'+id+'_4" name="img_'+id+'" src="'+projectpath+'/userbackstage/images/pc_page/start3.png"></a>'+
							'<a onclick="xuanze(\''+id+'\',\'5\')"><img id="'+id+'_5" name="img_'+id+'" src="'+projectpath+'/userbackstage/images/pc_page/start3.png"></a>'+
							'<a style="margin-left:20px;" id="'+id+'_starlevel">一般</a>'+
						'</div></td>'+
				    '</tr>';
	$('#ev_list').append(project);
	$('#sumstarnum').text(parseInt($('#sumstarnum').text())+3);
}

//查询区域信息
function queryOrganizeList(obj){
	requestPost("/pc/getShopListByReleaseRange",obj,function(resultMap){
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
			html += '<em><img src="/app/appcssjs/images/public/start2.png"></em>';
		}else if(floatnum <= 0.2 && floatnum != 0 && i == intnum){
			html += '<em><img src="/app/appcssjs/images/public/start_02.png"></em>';
		}else if(floatnum <= 0.4 && floatnum != 0 && i == intnum){
			html += '<em><img src="/app/appcssjs/images/public/start_04.png"></em>';
		}else if(floatnum <= 0.6 && floatnum != 0 && i == intnum){
			html += '<em><img src="/app/appcssjs/images/public/start_06.png"></em>';
		}else if(floatnum <= 0.8 && floatnum != 0 && i == intnum){
			html += '<em><img src="/app/appcssjs/images/public/start_08.png"></em>';
		}else if(floatnum > 0.8 && floatnum != 0 && i == intnum){
			html += '<em><img src="/app/appcssjs/images/public/start2.png"></em>';
		}else{
			html += '<em><img src="/app/appcssjs/images/public/start3.png"></em>';
		}
	}*/
	$('#star_level').html((intnum+floatnum).toFixed(1)+"分");
	$('#avgstarlevel').text((intnum+floatnum).toFixed(1));
	return "综合评价：";
}

//显示区域信息
function showOrganizeInfo(list){
	if(list.length > 0 && list != null){
		var html = "";
		$.each(list,function(i,map){
			var cls = "";
			if(i==0){
				organizeid = map.organizeid;
				$('#organizename').val(map.organizename);
				cls = 'selected="selected"';
			}
			html += '<option '+cls+' value="'+map.organizeid+'">'+map.organizename+'</option>';
		});
		
		$('#organize_option').append(html);
		showTemplate();
	}
}


//选择项目星级
function xuanze(i,k){
	$('img[name="img_'+i+'"]').attr("src",projectpath+"/userbackstage/images/pc_page/start3.png");
	for(var j=1;j<=k;j++){
		$('#'+i+"_"+j).attr("src",projectpath+"/userbackstage/images/pc_page/start2.png");
	}
	$('#'+i+'_input').attr("sumNum",k);
	$('#'+i+'_starlevel').html(dengji(k));
	
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
	obj.starlist = JSON.stringify(param);
	
	obj.userlist = $("#CCuseridlist").val();
	obj.CCusernames = $('#CCusernamelist').val();
	obj.avgstarlevel = $('#avgstarlevel').text();
	
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
		url:projectpath+"/pc/insertLeaveshop",
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
