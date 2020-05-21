
var region_organizeid = "";

var type = "";
var managerole = "";
var typeid = "";
var pageParam = new Object();
$(function(){
	type = $('#type').val();
	typeid = $('#typeid').val();
	managerole = $('#managerole').val();
	
	queryInspectTemplate();
	
	$.ajax({
		url:"/userbackstage/getOrganizeByCompanyid",
		type:"post",
		data:"companyid="+$('#companyid').val(),
		success:function(data){
			if(data != ""){
				region_organizeid = data.data.organizeid;
			}
		}
	});
});

function pageHelper(num){
	pageParam.currentPage = num;
	queryInspectTemplate();
}

function callbackfunc(organizeid){
	var param = new Object();
	param.organizeid = organizeid;
	requestPost("/userbackstage/getOrganizeTypeInfo",param,function(data){
		if(data == "3"){
			$('#addTemplate_insert').show();
			$('#editorgangweimuban').show();
		}else{
			$('#addTemplate_insert').hide();
			$('#editorgangweimuban').hide();
		}
	});
	pageParam.currentPage = 1;
	region_organizeid = organizeid;
	pageParam.organizeid = organizeid;
	queryInspectTemplate();
}

function querydefaulttemplate(){
	var param = new Object();
	param.typeid = $('#typeid').val();
	param.companyid = $('#companyid').val();
	param.organizeid = region_organizeid;
	requestPost("/userbackstage/getOrganizeTemplateListInfo",param,function(resultMap){
		if(resultMap != null && resultMap != ''){
			$('#tempmode').empty();
			var templist = resultMap.templatelist;
			if(templist != null && templist != ''){
				var html = "";
				$.each(templist,function(i,map){
					var tempname = map.templatename;
					var cls = "";
					if(tempname != undefined && tempname.length > 8){
						cls = 'class="two"';
					}
					html += '<a '+cls+' id="'+map.templateid+'_template" onclick="querytempproject(this,\''+map.templateid+'\')">'+tempname+'</a>';
				});
				html += '<div class="clear"></div>';
				$('#tempmode').html(html);
			}
		}
	});
}

function querytempproject(obj,tempid){
	var pro = $('#addprojectlist').find('tr[name="addproject_'+tempid+'"]');
	if(pro.length == 0){
		$(obj).css("background-color","#ff9b30");
		$(obj).css("color","#fff");
		var templatename = $(obj).text();
		var temname = $('#add_templateName').val();
		if(temname == ""){
			$('#add_templateName').val($(obj).text());
		}
		queryprojecttemplate(tempid,templatename);
		$("#addTemplate").animate({ scrollTop: $('#addTemplate')[0].scrollHeight}, 500);
	}else{
		$(obj).css("background-color","#eee");
		$(obj).css("color","#666");
		$('#addprojectlist').find('tr[name="addproject_'+tempid+'"]').remove();
	}
}

function queryprojecttemplate(tempid,templatename){
	var param = new Object();
	param.templateid = tempid;
	requestPost("/userbackstage/getOrganizeTemplateProjectListInfo",param,function(resultMap){
		if(resultMap != null && resultMap != ''){
			var prolist = resultMap.projectlist;
			if(prolist != null && prolist != ''){
				var html = '<tr name="addproject_'+tempid+'">'+
								'<td colspan="2" style="text-align:left; font-weight:bold; font-size:15px;" name="add_pro_name" status="1" templateid="'+tempid+'">'+templatename+'</td>'+
				            '</tr>';
				$.each(prolist,function(i,map){
					html += '<tr name="addproject_'+tempid+'">'+
								'<td name="add_pro_name" status="0" templateid="'+tempid+'">'+map.projectname+'</td>'+
				                '<td class="t_r"><a href="javascript:void(0)" class="blue m_r30" onclick="showTemplateProject(this)">修改</a><a href="javascript:void(0)" onclick="deleteTemplateprojectInfo(this,\''+tempid+'\')" class="del">删除</a></td>'+
				            '</tr>';
				});
//				html += '<tr name="addproject_'+param.templateid+'">'+
//							'<th colspan="2" style="text-align:center; font-weight:bold; font-size:15px;">&nbsp;</th>'+
//				        '</tr>';
				$('#addprojectlist').append(html);
			}
		}
	});
}

function submitdata(){
	var range = $('#rangeul').html();
	$('#addrangeul').html(range);
	chagneRange();
	queryTemplateProject_div();
}

function addTemplateName(){
	var name = $('#add_templatename').val();
	if(name == ""){
		swal("","检查项目名称不能为空！","warning");
		return false;
	}
	var html = '<tr>'+
					'<td name="add_pro_name" status="0" templateid="" >'+name+'</td>'+
				    '<td class="t_r"><a href="javascript:void(0)" class="blue m_r30" onclick="showTemplateProject(this)">修改</a><a href="javascript:void(0)" onclick="$(this).parent().parent().remove()" class="del">删除</a></td>'+
				'</tr>';
	$('#addprojectlist').append(html);
	$('#add_templatename').val("");
	addTemplate_div();
	queryTemplateProject_div();
	$("#addTemplate").animate({ scrollTop: $('#addTemplate')[0].scrollHeight}, 500);
}

var td_param = "";
function showTemplateProject(obj){
	td_param = obj;
	var cname = $(td_param).parent().parent().find('td[name="add_pro_name"]').text();
	$('#update_projectname').val(cname);
	$('#updateTemplateProject').attr("onclick","updateTemplateProject()");
	queryTemplateProject_div();
	updateTemplateProjectName_div();
}

function updateTemplateProject(){
	var name = $('#update_projectname').val();
	if(name == ""){
		swal("","检查项目名称不能为空！","warning");
		return false;
	}
	$(td_param).parent().parent().find('td[name="add_pro_name"]').text(name);
	$('#update_projectname').val("");
	updateTemplateProjectName_div();
	queryTemplateProject_div();
}

/*-------------------------------------------------------检查模板-----------------------------------------------------*/

function queryInspectTemplate(){
	pageParam.organizeid = region_organizeid;
	pageParam.type = type;
	requestPost("/userbackstage/getInspectTemplateList",pageParam,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			$('#Pagination').html(resultMap.pager);
			$('#inspecttemplate_list').empty();
			if(resultMap.status == 0 || resultMap.status == '0'){
				$('#inspecttemplate_list').empty();
				var templatelist = resultMap.templatelist;
				if(templatelist != null && templatelist != undefined){
					showInspectTemplate(templatelist);
				}
			}
		}
	});
}

function showInspectTemplate(list){
	if(list.length > 0 && list != null){
		
		$.each(list,function(i,map){
			var htm = '<tr id="template_'+map.templateid+'">'+
		            	'<td width="60%" id="templatename_'+map.templateid+'">'+map.organizename+"-"+map.templatename+'</td>'+
		                '<td><a href="javascript:void(0)" onclick="queryTemplateProject(\''+map.templateid+'\',\''+map.templatename+'\')" class="link">修改</a>'+
		            	'<a href="javascript:void(0)" onclick="deleteInspectTemplate(\''+map.templateid+'\',\''+map.templatename+'\')" class="del">删除</a></td>'+
		            '</tr>';
			$('#inspecttemplate_list').append(htm);
		});
	}
}

//删除检查模板
function deleteInspectTemplate(templateid,templatename){
	swal({
	    title: "提示",
	    text: "确定要删除   模板："+templatename+" 吗！",
	    type: "warning",
	    showCancelButton: true,
	    confirmButtonColor: "#ff7922",
	    confirmButtonText: "确定",
	    cancelButtonText : "取消",
	    closeOnConfirm: true
	}, function(){
		var param = new Object();
		param.templateid = templateid;
		param.delflag = 1;
		param.type = type;
		updateInspectTemplate(param,function(resultMap){
			if(resultMap.status == 0 || resultMap.status == '0'){
				$('#template_'+templateid).remove();
				swal("", "删除成功!", "success");
			}
		});
	});
}

//修改检查模板信息
function updateInspectTemplate(param,callback){
	requestPost("/userbackstage/updateInspectTemplateInfo",param,function(resultMap){
		callback(resultMap);
	});
}

function addProjectAndTemplate(){
	var name = $('#add_templateName').val();
	var templateid = $('#update_templateid_project').val();
	if(name != ""){
		var param = new Object();
		param.templatename = name;
		param.organizeid = region_organizeid;
		param.type = type;
		requestPost("/userbackstage/getTemplateNameIsExists",param,function(data){
			if(data == "error"){
				swal({
				    title: "提示",
				    text: "该组织机构下，岗位模板重复了！",
				    type: "warning",
				    showCancelButton: false,
				    confirmButtonColor: "#ff7922",
				    confirmButtonText: "确定",
				    closeOnConfirm: true
				}, function(){
				});
				return false;
			}else{

				var pro_param = {"prolist":[]};
				var project = $('#addprojectlist').find('td[name="add_pro_name"]');
				$.each(project,function(k,pro){
					var obj = new Object();
					obj.projectname = $(pro).text();
					obj.status = $(pro).attr("status");
					obj.resoucetemplateid = $(pro).attr("templateid");
					pro_param.prolist[k] = obj;
				});
				if(project.length == 0){
					swal({
					    title: "提示",
					    text: "请添加检查项目！",
					    type: "warning",
					    showCancelButton: false,
					    confirmButtonColor: "#ff7922",
					    confirmButtonText: "确定",
					    closeOnConfirm: true
					}, function(){});
					return false;
				}
				
				var templatename = $('#add_templateName').val();
				if(templatename == ""){
					swal({
					    title: "提示",
					    text: "请输入区域名称！",
					    type: "warning",
					    showCancelButton: false,
					    confirmButtonColor: "#ff7922",
					    confirmButtonText: "确定",
					    closeOnConfirm: true
					}, function(){});
					return false;
				}
				
				var org_param = {"orglist":[]};
				var range = $('#addrangeul').find('input[name=organizeid]');
				var user = $('#addrangeul').find('input[name=userid]');
				if(range.length<=0 && user.length<=0){
					swal({
					    title: "提示",
					    text: "请选择发布范围！",
					    type: "warning",
					    showCancelButton: false,
					    confirmButtonColor: "#ff7922",
					    confirmButtonText: "确定",
					    closeOnConfirm: true
					}, function(){});
					return false;
				}
				var rge = "";
				$.each(range,function(r,rg){
					var obj = new Object();
					obj.organizeid = $(rg).val();
					org_param.orglist[r] = obj;
					rge=(r+1);
				});
				$.each(user,function(u,ur){
					var obj = new Object();
					obj.userid = $(ur).val();
					org_param.orglist[rge+u] = obj;
				});
				
				var param = new Object();
				param.prolist = JSON.stringify(pro_param);
				param.orglist = JSON.stringify(org_param);
				param.templatename = templatename;
				param.type = type;
				param.organizeid = region_organizeid;//待处理
				
				queryTemplateProject_div();
				requestPost("/userbackstage/insertInspectTemplateInfo",param,function(resultMap){
					if(resultMap.status == 0 || resultMap.status == '0'){
						//location.reload();
						queryInspectTemplate();
					}else{
						swal("","新建失败！","error");
					}
				});
			}
		});
	}else{
		swal({
		    title: "提示",
		    text: "区域名称不能为空！",
		    type: "warning",
		    showCancelButton: false,
		    confirmButtonColor: "#ff7922",
		    confirmButtonText: "确定",
		    closeOnConfirm: true
		}, function(){
		});
		return false;
	}
//	alert(JSON.stringify(pro_param));
//	alert(JSON.stringify(org_param));
//	alert(JSON.stringify(templatename));
//	alert(JSON.stringify(range_param));
}


function updateReleaseRange(){
	var name = $('#add_templateName').val();
	var templateid = $('#update_templateid_project').val();
	if(name != ""){
		var param = new Object();
		param.templatename = name;
		param.organizeid = region_organizeid;
		param.type = type;
		param.templateid = templateid;
		requestPost("/userbackstage/getTemplateNameIsExists",param,function(data){
			if(data == "error"){
				swal({
				    title: "提示",
				    text: "该组织机构下，岗位模板重复了！",
				    type: "warning",
				    showCancelButton: false,
				    confirmButtonColor: "#ff7922",
				    confirmButtonText: "确定",
				    closeOnConfirm: true
				}, function(){
				});
				return false;
			}else{

				var range_param = {"rangelist":[]};
				var range = $('#addrangeul').find('li input[name="organizeid"]');
				var user = $('#addrangeul').find('li input[name="userid"]');
				if(range.length<=0 && user.length<=0){
					swal({
					    title: "提示",
					    text: "请选择发布范围！",
					    type: "warning",
					    showCancelButton: false,
					    confirmButtonColor: "#ff7922",
					    confirmButtonText: "确定",
					    closeOnConfirm: true
					}, function(){});
					return false;
				}
				var rge = "";
				$.each(range,function(r,rg){
					var obj = new Object();
					obj.organizeid = $(rg).val();
					range_param.rangelist[r] = obj;
					rge=(r+1);
				});
				$.each(user,function(u,ur){
					var obj = new Object();
					obj.userid = $(ur).val();
					range_param.rangelist[rge+u] = obj;
				});
				
				var pro_param = {"prolist":[]};
				var project = $('#addprojectlist').find('td[name="add_pro_name"]');
				$.each(project,function(k,pro){
					var obj = new Object();
					obj.projectname = $(pro).text();
					obj.status = $(pro).attr("status");
					obj.resoucetemplateid = $(pro).attr("templateid");
					pro_param.prolist[k] = obj;
				});
				if(project.length == 0){
					swal({
					    title: "提示",
					    text: "请添加检查项目！",
					    type: "warning",
					    showCancelButton: false,
					    confirmButtonColor: "#ff7922",
					    confirmButtonText: "确定",
					    closeOnConfirm: true
					}, function(){});
					return false;
				}
				
				var param = new Object();
				param.rangelist = JSON.stringify(range_param);
				param.prolist = JSON.stringify(pro_param);
				param.templatename = $('#add_templateName').val();//待处理
				param.templateid = $('#update_templateid_project').val();
				if(param.templatename == ""){
					swal({
					    title: "提示",
					    text: "请输入区域名称！",
					    type: "warning",
					    showCancelButton: false,
					    confirmButtonColor: "#ff7922",
					    confirmButtonText: "确定",
					    closeOnConfirm: true
					}, function(){});
					return false;
				}
				queryTemplateProject_div();
				requestPost("/userbackstage/updateRangeInfo",param,function(resultMap){
					if(resultMap.status == 0 || resultMap.status == '0'){
						swal("","修改成功！","success");
						queryInspectTemplate();
					}else{
						swal("","修改失败","error");
					}
				});
			}
		});
	}else{
		swal({
		    title: "提示",
		    text: "区域名称不能为空！",
		    type: "warning",
		    showCancelButton: false,
		    confirmButtonColor: "#ff7922",
		    confirmButtonText: "确定",
		    closeOnConfirm: true
		}, function(){
		});
		return false;
	}
}

/*-----------------------------------------------------------查询检查项目信息--------------------------------------------------*/

function queryTemplateProject(templateid,templatename){
	if(region_organizeid == ""){
		swal({
		    title: "提示",
		    text: "请选择区域！",
		    type: "warning",
		    showCancelButton: false,
		    confirmButtonColor: "#ff7922",
		    confirmButtonText: "确定",
		    closeOnConfirm: true
		}, function(){});
		return false;
	}
	querydefaulttemplate();
	$('#add_templateName').val(templatename);
	$('#update_templateid_project').val(templateid);
	var param = new Object();
	param.templateid = templateid;
	requestPost("/userbackstage/getTemplateProjectList",param,function(resultMap){
		if(resultMap != null && resultMap != ""){
			var projectlist = resultMap.projectlist;
			var rangelist = resultMap.rangelist;
			if(rangelist != null && rangelist.length > 0){
				var rangehtml = "";
				$.each(rangelist,function(i,map){
					if(map.type == 1 || map.type == '1'){
						rangehtml += "<li><input type=\"hidden\" name=\"userid\" value=\""+map.userid+"\"/>"+map.rangename+"<a class=\"del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\" onclick=\"deleteorganizeuser(this)\" /></a></li>";
					}else if(map.type == 2 && map.type == '2'){
						rangehtml += "<li><input type=\"hidden\" name=\"organizeid\" value=\""+map.organizeid+"\"/>"+map.rangename+"<a class=\"del\"><img src=\"../userbackstage/images/public/del.png\" alt=\"删除\" onclick=\"deleteorganizeuser(this)\" /></a></li>";
					}
				});
				$('#addrangeul').html(rangehtml);
			}
			if(projectlist != null &&　projectlist.length > 0){
				var html = "";
				$.each(projectlist,function(i,map){
					var status = map.status;
					if(status == "1" || status == 1){
						html += '<tr name="addproject_'+map.resoucetemplateid+'">'+
									'<td colspan="2" style="text-align:left; font-weight:bold; font-size:15px;" name="add_pro_name" status="1" templateid="'+map.resoucetemplateid+'">'+map.projectname+'</td>'+
					            '</tr>';
					}else{
						html += '<tr name="addproject_'+map.resoucetemplateid+'">'+
									'<td name="add_pro_name" status="0" templateid="'+map.resoucetemplateid+'">'+map.projectname+'</td>'+
								    '<td class="t_r"><a href="javascript:void(0)" class="blue m_r30" onclick="showTemplateProject(this)">修改</a><a href="javascript:void(0)" onclick="deleteTemplateprojectInfo(this,\''+map.resoucetemplateid+'\')" class="del">删除</a></td>'+
								'</tr>';
					}
				});
				$('#addprojectlist').html(html);
			}
		}
	});
	$('#updateTemplate_submit').show();
	$('#addTemplate_submit').hide();
	queryTemplateProject_div();
}


function deleteTemplateprojectInfo(obj,templateid){
	var pronum = $('#addprojectlist').find('tr[name="addproject_'+templateid+'"]').length;
	if(pronum > 2){
		$(obj).parent().parent().remove();
		$('#tempmode').find('#'+templateid+'_template').css("background-color","#eee");
		$('#tempmode').find('#'+templateid+'_template').css("color","#666");
	}else{
		$('#addprojectlist').find('tr[name="addproject_'+templateid+'"]').remove();
	}
}

function queryTemplateProject_div(item){
	var mask = $('#div_mask').css("display");
	var temp = $('#addTemplate').css("display");
	if(mask == "none" || temp == "none"){
		if(item == "first"){
			$('#update_templateid_project').val("");
			querydefaulttemplate();
			
			$('#updateTemplate_submit').hide();
			$('#addTemplate_submit').show();
			$('#addprojectlist').empty();
			$('#addrangeul').empty();
			$('#add_templateName').val("");
		}
		$('#div_mask').show();
		$('#addTemplate').show();
	}else{
		$('#div_mask').hide();
		$('#addTemplate').hide();
	}
}

function addTemplate_div(){
	var mask = $('#div_mask').css("display");
	var temp = $('#addTemplate_div').css("display");
	if(temp == "none" && mask == "none"){
		$('#div_mask').show();
		$('#addTemplate_div').show();
	}else{
		$('#add_templatename').val("");
		$('#div_mask').hide();
		$('#addTemplate_div').hide();
	}
}

function chagneRange(){
	var mask = $('#div_mask').css("display");
	var addtemp = $('#tc_selrange').css("display");
	if(mask == "none" && addtemp == "none"){
		var range = $('#addrangeul').html();
		$('#rangeul').html(range);
		$('#div_mask').show();
		$('#tc_selrange').show();
	}else{
		$('#div_mask').hide();
		$('#tc_selrange').hide();
	}
}

function updateTemplateProjectName_div(){
	var tempname = $('#updateprojectname_div').css("display");
	var mask = $('#div_mask').css("display");
	if(mask == "none" && tempname == "none"){
		$('#updateprojectname_div').show();
		$('#div_mask').show();
	}else{
		$('#update_projectname').val("");
		$('#div_mask').hide();
		$('#updateprojectname_div').hide();
	}
}



/**********------------------------------------------------编辑模板------------------------------------------------------*/

function organizeTemplateList(){
	var param = new Object();
	param.companyid = $('#companyid').val();
	param.organizeid = region_organizeid;
	param.typeid = typeid;
	requestPost("/userbackstage/getOrganizeTemplateListInfo",param,function(resultMap){
		if(resultMap != null && resultMap != ""){
			var templist = resultMap.templatelist;
			if(templist != null && templist != ""){
				var html = "";
				$.each(templist,function(i,map){
					var tempname = map.templatename;
					var cls = "";
					if(tempname != undefined && tempname.length > 8){
						cls = 'class="two"';
					}
					html += '<a '+cls+'><div style="width:100%; height:100%;" onclick="update_querytempproject(this,\''+map.templateid+'\')">'+tempname+'</div><i class="del" onclick="deletetemplate_a(this,\''+tempname+'\',\''+map.templateid+'\')"><img src="/userbackstage/images/public/del.png" alt="删除"></i></a>';
				});
				html += '<div class="clear"></div>';
				$('#update_tempmode').html(html);
			}
		}
	});
}

var templateid = "";
function update_querytempproject(obj,tempid){
	var pro = $('#updateprojectlist').find('tr[name="updateproject_'+tempid+'"]');
	if(pro.length == 0){
		var ahtm = $('#update_tempmode').find('a');
		$.each(ahtm,function(i,item){
			$(item).css("background-color","#eee");
			$(item).css("color","#666");
		});
		templateid = tempid;
		$(obj).parent().css("background-color","#ff9b30");
		$(obj).parent().css("color","#fff");
			$('#update_templateName').val($(obj).text());
		$('#updateprojectlist').empty();
		organizeTemplateProject(tempid);
	}else{
		templateid = "";
		$(obj).parent().css("background-color","#eee");
		$(obj).parent().css("color","#666");
		$('#updateprojectlist').find('tr[name="updateproject_'+tempid+'"]').remove();
	}
}


function organizeTemplateProject(templateid){
	var param = new Object();
	param.templateid = templateid;
	requestPost("/userbackstage/getOrganizeTemplateProjectListInfo",param,function(resultMap){
		if(resultMap != null && resultMap != ""){
			var prolist = resultMap.projectlist;
			if(prolist != null && prolist != ""){
				var html = "";
				$.each(prolist,function(i,map){
					html += '<tr name="updateproject_'+templateid+'">'+
								'<td name="add_pro_name">'+map.projectname+'</td>'+
				                '<td class="t_r"><a href="javascript:void(0)" class="blue m_r30" onclick="updateshowTemplateProject(this,\''+map.projectid+'\')">修改</a><a href="javascript:void(0)" onclick="update_deleteProject(this,\''+map.projectid+'\',\''+map.projectname+'\')" class="del">删除</a></td>'+
				            '</tr>';
				});
				$('#updateprojectlist').append(html);
			}
		}
	});
}

function updateshowTemplateProject(obj,projectid){
	td_param = obj;
	var cname = $(td_param).parent().parent().find('td[name="add_pro_name"]').text();
	$('#update_projectname').val(cname);
	
	$('#updateTemplateProject').attr("onclick","updatetemplateprojectname('"+projectid+"')");
	updateTemplateCloseOrOpenDiv();
	updateTemplateProjectName_div();
}

function updatetemplateprojectname(projectid){
	if(projectid == "undefined"){
		var name = $('#update_projectname').val();
		if(name == ""){
			swal("","检查项目名称不能为空！","warning");
			return false;
		}
		$(td_param).parent().parent().find('td[name="add_pro_name"]').text(name);
		$('#update_projectname').val("");
		updateTemplateProjectName_div();
		updateTemplateCloseOrOpenDiv();
	}else{
		var param = new Object();
		var name = $('#update_projectname').val();
		param.projectid = projectid;
		param.projectname = name;
		requestPost("/userbackstage/updateOrganizeTemplateProjectInfo",param,function(data){
			if(data == "success"){
				swal("","修改成功！","success");
				$(td_param).parent().parent().find('td[name="add_pro_name"]').text(name);
				$('#update_projectname').val("");
			}else{
				swal("","修改失败···","error");
			}
			updateTemplateProjectName_div();
			updateTemplateCloseOrOpenDiv();
		});
	}
}

function update_deleteProject(obj,projectid,projectname){
	swal({
	    title: "提示",
	    text: "确定要删除 "+projectname+" 吗？<br/>（删除之后无法恢复）",
	    type: "warning",
	    html: true,
	    showCancelButton: true,
	    confirmButtonColor: "#ff7922",
	    confirmButtonText: "确定",
	    cancelButtonText : "取消",
	    closeOnConfirm: true
	}, function(){
		var param = new Object();
		param.delflag = 1;
		param.projectid = projectid;
		requestPost("/userbackstage/updateOrganizeTemplateProjectInfo",param,function(data){
			if(data == "success"){
				$(obj).parent().parent().remove();
			}else{
				swal("",projectname+"删除失败···","error");
			}
		});
	});
}


function updatetemplatename(){
	var name = $('#update_templateName').val();
	if(name != ""){
		var param = new Object();
		param.templatename = name;
		param.organizeid = region_organizeid;
		param.typeid = typeid;
		param.templateid = templateid;
		requestPost("/userbackstage/getOrganizeTemplateNameIsExists",param,function(data){
			if(data == "exists"){
				swal({
				    title: "提示",
				    text: "该组织机构下，模板名称重复了！",
				    type: "warning",
				    showCancelButton: false,
				    confirmButtonColor: "#ff7922",
				    confirmButtonText: "确定",
				    closeOnConfirm: true
				}, function(){
				});
				return false;
			}else if(data == "error"){
				swal("","验证出错","error");
				return false;
			}else{

				if(templateid == ""){
					var pro_param = {"prolist":[]};
					var project = $('#updateprojectlist').find('td[name="add_pro_name"]');
					$.each(project,function(k,pro){
						var obj = new Object();
						obj.projectname = $(pro).text();
						obj.status = $(pro).attr("status");
						pro_param.prolist[k] = obj;
					});
					
					var param = new Object();
					param.templatename = $('#update_templateName').val();
					param.typeid = typeid;
					param.organizeid = region_organizeid;
					param.companyid = $('#companyid').val();
					param.prolist = JSON.stringify(pro_param);
					if(param.templatename == ""){
						swal({
						    title: "提示",
						    text: "请输入模板名称！",
						    type: "warning",
						    showCancelButton: false,
						    confirmButtonColor: "#ff7922",
						    confirmButtonText: "确定",
						    closeOnConfirm: true
						}, function(){});
						return false;
					}
					requestPost("/userbackstage/insertOrganizeTemplateInfo",param,function(data){
						if(data == "success"){
							swal("","添加成功！","success");
							updateTemplateCloseOrOpenDiv();
						}else{
							swal("","添加失败！","error");
						}
					});
				}else{
					var param = new Object();
					param.templatename = $('#update_templateName').val();
					param.templateid = templateid;
					requestPost("/userbackstage/updateOrganizeTemplateInfo",param,function(data){
						if(data == "success"){
							swal("","修改成功！","success");
							organizeTemplateList();
							templateid = "";
							$('#updateprojectlist').empty();
							$('#update_templateName').val("");
						}else{
							swal("","修改失败！","error");
						}
					});
				}
			}
		});
	}else{
		swal({
		    title: "提示",
		    text: "模板名称不能为空！",
		    type: "warning",
		    showCancelButton: false,
		    confirmButtonColor: "#ff7922",
		    confirmButtonText: "确定",
		    closeOnConfirm: true
		}, function(){
		});
		return false;
	}
}






function updateTemplateCloseOrOpenDiv(item){
	var temp = $('#updateTemplate').css("display");
	var mask = $('#div_mask').css("display");
	if(mask == "none" && temp == "none"){
		if(item == "first"){
			var ahtm = $('#update_tempmode').find('a');
			$.each(ahtm,function(i,item){
				$(item).css("background-color","#eee");
				$(item).css("color","#666");
			});
			$('#update_tempmode').empty();
			$('#updateprojectlist').empty();
			$('#update_templateName').val("");
		}
		$('#updateTemplate').show();
		$('#div_mask').show();
	}else{
		$('#div_mask').hide();
		$('#updateTemplate').hide();
	}
}

function updateaddtemplateprojectCloseOrOpen(){
	var temp = $('#updateTemplate_div').css("display");
	var mask = $('#div_mask').css("display");
	if(mask == "none" && temp == "none"){
		$('#updateTemplate_div').show();
		$('#div_mask').show();
		$('#update_addtemplatename').focus();
	}else{
		$('#div_mask').hide();
		$('#updateTemplate_div').hide();
	}
}

function update_addcheckProject(){
	var name = $('#update_addtemplatename').val();
	if(name == ""){
		swal("","检查项目名称不能为空！","warning");
		return false;
	}
	if(templateid != "" && templateid != undefined){
		var param = new Object();
		param.projectname = name;
		param.templateid = templateid;
		requestPost("/userbackstage/insertOrganizeTemplateProjectInfo",param,function(data){
			if(data != "error"){
				var html = '<tr name="updateproject_'+templateid+'">'+
								'<td name="add_pro_name">'+name+'</td>'+
				                '<td class="t_r"><a href="javascript:void(0)" class="blue m_r30" onclick="updateshowTemplateProject(this,\''+data+'\')">修改</a><a href="javascript:void(0)" onclick="update_deleteProject(this,\''+data+'\',\''+name+'\')" class="del">删除</a></td>'+
				            '</tr>';
				$('#updateprojectlist').append(html);
				swal({   
					title: "",   
					text: "<font style=\"color:#ff7922; font-size:28px; margin-left:20px;\">添加成功！</font>",
					type:"success",
					timer: 1000,
					html:true,
					showConfirmButton: false 
				});
			}
		});
	}else{
		var html = '<tr>'+
						'<td name="add_pro_name">'+name+'</td>'+
					    '<td class="t_r"><a href="javascript:void(0)" class="blue m_r30" onclick="updateshowTemplateProject(this)">修改</a><a href="javascript:void(0)" onclick="$(this).parent().parent().remove()" class="del">删除</a></td>'+
					'</tr>';
		$('#updateprojectlist').append(html);
	}
	$('#update_addtemplatename').val("");
	updateaddtemplateprojectCloseOrOpen();
	updateTemplateCloseOrOpenDiv();
}

function deletetemplate_a(obj,templatename,tempid){
	swal({
	    title: "提示",
	    text: "确定要删除模板  "+templatename+"  吗？<br/>（删除之后无法恢复）",
	    type: "warning",
	    html: true,
	    showCancelButton: true,
	    confirmButtonColor: "#ff7922",
	    confirmButtonText: "确定",
	    cancelButtonText : "取消",
	    closeOnConfirm: true
	}, function(){
		var param = new Object();
		param.delflag = 1;
		param.templateid = tempid;
		requestPost("/userbackstage/updateOrganizeTemplateInfo",param,function(data){
			if(data == "success"){
				$(obj).parent().remove();
				swal("","模板删除成功！","success");
			}else{
				swal("","模板删除失败！","error");
			}
		});
	});
}



//公共的查询方法
function requestPost(url,param,callback){
	$.ajax({
		url:url,
		type:"post",
		data:param,
		beforeSend:function(){
			var load = '<div id="load_mask" class="div_mask" style="opacity:0;"></div>'+
				      '<div id="load_loading" class="loading"><img src="../userbackstage/images/public/loading.gif" width="360" height="200" /></div>';
			$("body").after(load);
		},
		success:function(resultMap){
			callback(resultMap);
		},
		complete:function(){
			$('#load_mask').remove();
			$('#load_loading').remove();
		},
		error:function(e){
			
		}
	});
}