
$(function(){
	$('#checkall').click(function(){
		var input = $('input[name="check_one"]');
		$.each(input,function(i,item){
			var check = item.checked;
			if(check){
				item.checked = false;
			}else{
				item.checked = true;
			}
		});
	});
	
});

//进入文件夹内部
function childFolder(fileid){
	$('#child_parentid').val(fileid);
	$('#child_form').submit();
}

//返回上一级
function returnToTheUpperLevel(){
	var param = new Object();
	param.parentid = $('#parentid').val();
	requestPost("/managebackstage/getFolderParentId",param,function(data){
		if(data.status == 0){
			var pid = data.parentid;
			if(pid == "" || pid == null || pid == undefined){
				location.href="/managebackstage/intoSkyDrivePage";
			}else{
				if(pid == 0 || pid == '0'){pid="";}
				//返回上一级
				childFolder(pid);
			}
		}else{
			window.history.go(-1);
		}
	});
}

//新建文件
function insertFile(){
	var param = new Object();
	param.filename = $('#filename').val();
	param.filetype = 1;
	param.fileurl = $('#fileurl').val();
	param.memory = $('#kb_memory').val();
	param.parentid = $('#parentid').val();
	param.type = $('#type').val();
	
	requestPost("/managebackstage/insertSystemCloudInfo",param,function(data){
		addFile_closeOrOpen();
		if(data.status == 0){
			location.reload();
		}else{
			swal("","新建失败","error");
		}
	});
}

//新建文件夹
function insertFolder(){
	var param = new Object();
	param.filename = $('#insert_filename').val();
	param.filetype = 2;
	param.parentid = $('#parentid').val();
	param.type = $('#type').val();
	
	if(param.filename == ""){
		swal("","请输入文件夹的名称","warning");
		return false;
	}
	
	$('#insert_filename').val("");
	
	folder_closeOrOpen();
	requestPost("/managebackstage/insertSystemCloudInfo",param,function(data){
		if(data.status == 0){
			location.reload();
		}else{
			swal("","新建失败","error");
		}
	});
}

//修改文件名称
function updateFolderName(){
	var param = new Object();
	param.filename = $('#update_filename').val();
	param.fileid = $('#update_fileid').val();
	
	if(param.filename == ""){
		swal("","文件名称不能为空！","warning");
		return false;
	}
	update_folder_closeOrOpen();
	requestPost("/managebackstage/updateSystemCloudInfo",param,function(data){
		if(data.status == 0){
			location.reload();
		}else{
			swal("","修改失败","error");
		}
	});
}

//下载
function downFolder(url){
	window.location.href="/upload/download?fileName="+url;
}

//批量删除文件
function deleteall(){
	var param = {"filelist":[]};
	var input = $('input[name="check_one"]');
	$.each(input,function(i,item){
		var check = item.checked;
		if(check){
			var obj = new Object();
			obj.parentid = $(item).attr("fileid");
			obj.fileid = $(item).attr("fileid");
			param.filelist[i] = obj;
		}
	});
	if(param.filelist.length <= 0){
		swal("","请选择文件或文件夹！","warning");
		return false;
	}
	swal({
	    title: "提示",
	    text: "确定要删除吗？",
	    type: "warning",
	    showCancelButton: true,
	    confirmButtonColor: "#ff7922",
	    confirmButtonText: "确定",
	    cancelButtonText: "取消",
	    closeOnConfirm: true
	}, function(){
		if(param.filelist.length > 0){
			var filemap = new Object();
			filemap.filelist = JSON.stringify(param);
			queryFolderChildCount(filemap);
		}
	});
}

//删除单个文件
function deleteFolerOne(fileid){
	var param = {"filelist":[]};
	var obj = new Object();
	obj.parentid = fileid;
	obj.fileid = fileid;
	param.filelist[0] = obj;
	
	var filemap = new Object();
	filemap.filelist = JSON.stringify(param);
	queryFolderChildCount(filemap);
}

//查询文件夹是否存在子文件夹
function queryFolderChildCount(param){
	$.ajax({
		url:"/managebackstage/getFolderChildCount",
		type:"post",
		data:param,
		success:function(data){
			if(data.status == 0){
				if(data.count == 0){
					deleteFolder(param);
				}else{
					swal("","抱歉，你删除的文件夹中存在子文件夹！","warning");
				}
			}else{
				swal("","对不起，暂时不能进行删除操作！","error");
			}
		}
	});
}

//删除文件
function deleteFolder(param){
	requestPost("/managebackstage/deleteSystemCloud",param,function(data){
		if(data.status == 0){
			//swal("","删除成功","success");
			location.reload();
		}
	});
}

//添加文件夹窗口
function folder_closeOrOpen(){
	var mask = $('#div_mask').css("display");
	var folder = $('#folder_div').css("display");
	if(mask == "none" || folder == "none"){
		$('#div_mask').show();
		$('#folder_div').show();
	}else{
		$('#div_mask').hide();
		$('#folder_div').hide();
	}
}

//修改文件/夹名字
function update_folder_closeOrOpen(fileid,filename){
	var mask = $('#div_mask').css("display");
	var folder = $('#update_folder_div').css("display");
	if(mask == "none" || folder == "none"){
		$('#update_filename').val(filename);
		$('#update_fileid').val(fileid);
		
		$('#div_mask').show();
		$('#update_folder_div').show();
	}else{
		$('#update_filename').val("");
		$('#update_fileid').val("");
		
		$('#div_mask').hide();
		$('#update_folder_div').hide();
	}
}


//上传文件
function addFile_closeOrOpen(){
	var mask = $('#div_mask').css("display");
	var folder = $('#addfile_div').css("display");
	if(mask == "none" || folder == "none"){
		$('#div_mask').show();
		$('#addfile_div').show();
	}else{
		$('#div_mask').hide();
		$('#addfile_div').hide();
	}
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