var citylist = {};
var industrylist = {};
var salarylist = {};
var explist = {};
var eduList = {};
function initParam(){
	//初始化页面信息
	$.ajax({
		url:"/interface/getPositionDictionary",
		type:"post",
		success:function(resultMap){
			citylist = resultMap.citylist;
			industrylist = resultMap.industrylist;
			salarylist = resultMap.salarylist;
			explist = resultMap.explist;
			eduList = resultMap.eduList;
			markList = resultMap.markList;
			$('#tip_info').text('(系统会给出一些常用标签，用户也可以自定义标签,限制'+resultMap.paramMap.pvalue+'个)');
		},
		error:function(){}
	});
}

function getMyMarkList(call){
	$.ajax({
		url:"/interface/selectMarkList",
		type:"post",
		data:{userid:$('#memberid').val()},
		success:function(resultMap){
			if(resultMap.status+"" == "0"){
				call(resultMap.markList);
			} 
			
		},
		error:function(){}
	});
}
$(function(){
	initParam();
	if($('#operate').val() != ""){
		$.ajax({
			url:"/interface/initEditPosition",
			type:"post",
			data:{positionid:$("#positionid").val()},
			success:function(resultMap){
			   var dataList = resultMap.markList;
			   var data = resultMap.data
			   if(data != null){
				  $("#positionname").val(data.positionname);
				  $("#industry_click").text(data.worktypename);
				  $("#recruitnum").val(data.recruitnum);
				  $("#edu_click").text(data.eduname);
				  $("#experice_click").text(data.experiencename);
				  $("#salary_click").text(data.salayrname);
				  $("#area_click").text(data.cityname);
				  $("#address").val(data.address);
				  $("#requirement").val(data.requirement);
				  $("#worktype").val(data.worktype);
				  $("#salaryid").val(data.salaryid);
				  $("#experienceid").val(data.experienceid);
				  $("#cityid").val(data.cityid);
				  $("#educationid").val(data.educationid);
				  
			   }
			   if(dataList.length>0){
				   var markname = "";
				   $.each(dataList,function(i,item){
					   markname+=item.markname+" ";
					   $("#mark_select").append('<li markid="'+item.markid+'" markname="'+item.markname+'"><span>'+item.markname+'</span><a href="javascript:void(0)" onclick="cancelMark(this)"><img src="/appcssjs/images/public/close_02.png"></a></li>');
				   });
				   $("#specil_click").text(markname);
				   $("#count_num").text(dataList.length);
			   }
			},
			error:function(){}
		});
	}
	
	//初始化页面信息
/*	$.ajax({
		url:"/interface/getPositionDictionary",
		type:"post",
		data:{userid:$('#memberid').val()},
		success:function(resultMap){
			citylist = resultMap.citylist;
			industrylist = resultMap.industrylist;
			salarylist = resultMap.salarylist;
			explist = resultMap.explist;
			eduList = resultMap.eduList;
			markList = resultMap.markList;
		},
		error:function(){}
	});*/
	 
	//工作选择的切换
	$('#area_click').parent().click(function(){
		var areaid = $('#cityid').val();
		getWorkArea(citylist,areaid);
		$('#content_main').hide();
		$('#areaChange').show();
	});
	
	if($('#operate').val() == ""){
	//行业类别的选择切换
	$("#industry_click").parent().click(function(){
		var industid = $('#worktype').val();
		getIndustryInfo(industrylist,industid);
		$('#content_main').hide();
		$('#industryChange').show();
	});
   }
 
	//薪资范围选择的样式切换
	$("#salary_click").parent().click(function(){
		var salaryid = $('#salaryid').val();
		getSalaryInfo(salarylist,salaryid);
		$('#content_main').hide();
		$('#salaryChange').show();
	});
	
	//工作经验的样式切换
	$("#experice_click").parent().click(function(){
		var expid = $('#experienceid').val();
		getExperience(explist,expid);
		$('#content_main').hide();
		$('#experienceChange').show();
	});
	
	//学历要求的样式切换
	$("#edu_click").parent().click(function(){
		var eduid = $("#educationid").val();
		getEdu(eduList,eduid);
		$('#content_main').hide();
		$('#eduChange').show();
	});
	
	//学历要求的样式切换
	$("#specil_click").parent().click(function(){
		getMyMarkList(function(data){
			var selectMarkList = new Array();
			$.each($("#mark_select li"),function(i,item){
				selectMarkList.push($(item).attr("markid"));
			});
			getMark(data,selectMarkList);
			$('#content_main').hide();
			$('#mark_div').show();	
			
		});
	});
	
	
	//工作区域的样式--选择
	$("#city_ui").delegate("[name='areaspan']","click",function(){
		var obj = $(this).find("a[name='area_city']");
		var change = obj.attr("class");
		if(change == "radio"){
			$('[name="area_city"]').attr("class","radio");
			obj.attr("class","radio_ed");
			$('#saveArea').attr("cityid",obj.attr("cityId"));
			$('#saveArea').attr("cityname",obj.attr("cityName"));
		}else{
			obj.attr("class","radio");
			$('#saveArea').attr("cityid","");
			$('#saveArea').attr("cityname","");
		}
		
	});
	
	//工作区域选择后的保存
	$('#saveArea').click(function(){
		var cityid = $(this).attr("cityid");
		var cityname = $(this).attr("cityname");
		if(cityid != null && cityid != ""){
			$('#cityid').val(cityid);
			$("#area_click").text(cityname);
		} 
		closeArea();
	});
	
	//行业样式
	$("#industry_ul").delegate("[name='zhiyespan']","click",function(){
		var obj = $(this).find("a[name='zhiye']");
		var zhiye = obj.attr("class");
		if(zhiye == "radio"){
			$("[name='zhiye']").attr("class","radio");
			obj.attr("class","radio_ed");
			
			$('#saveIndustry').attr("industryid",obj.attr("dataid"));
			$('#saveIndustry').attr("industryname",obj.attr("cname"));
		}else{
			obj.attr("class","radio");
			$('#saveIndustry').attr("industryid","");
			$('#saveIndustry').attr("industryname","");
		}
	});
	
	//行业选择--保存
	$('#saveIndustry').click(function(){
		var dataid = $(this).attr("industryid");
		var cname = $(this).attr("industryname");
		if(dataid == null || dataid == ""){
			$('#worktype').val("");
		}else{
			$('#industry_click').text(cname);
			$('#worktype').val(dataid);
		}
		closeIndustry();
	});
	
	//薪资样式
	$("#salary_div").delegate("[name='xinzispan']","click",function(){
		var obj=$(this).find("a[name='xinzi']");
		var xinzi = obj.attr("class");
		if(xinzi == "radio"){
			$("[name='xinzi']").attr("class","radio");
			obj.attr("class","radio_ed");
			$('#saveSalary').attr("salaryid",obj.attr("dataid"));
			$('#saveSalary').attr("salaryname",obj.attr("cname"));
		}else{
			obj.attr("class","radio");
			$('#saveSalary').attr("salaryid","");
			$('#saveSalary').attr("salaryname","");
		}
	});
	//薪资样式保存
	$('#saveSalary').click(function(){
		var dataid = $(this).attr("salaryid");
		var cname = $(this).attr("salaryname");
		if(dataid == null || dataid == ""){
			$('#salaryid').val("");
		}else{
			$('#salaryid').val(dataid);
			$('#salary_click').text(cname);	
		}
		
		closeSalary();
	});
	 
	
	//工作经验保存
	$("#experience_div").delegate("[name='expspan']","click",function(){
		var obj=$(this).find("a[name='exp']");
		var xinzi = obj.attr("class");
		if(xinzi == "radio"){
			$("[name='exp']").attr("class","radio");
			obj.attr("class","radio_ed");
			$('#saveExp').attr("expid",obj.attr("dataid"));
			$('#saveExp').attr("expname",obj.attr("cname"));
		}else{
			obj.attr("class","radio");
			$('#saveExp').attr("expid","");
			$('#saveExp').attr("expname","");
		}
	});
	//工作经验样式保存
	$('#saveExp').click(function(){
		var dataid = $(this).attr("expid");
		var cname = $(this).attr("expname");
		if(dataid == null || dataid == ""){
			$('#experienceid').val("");
		}else{
			$('#experienceid').val(dataid);
			$('#experice_click').text(cname);
		}
		
		closeExper();
	});
	 
	//学历样式保存
	$("#edu_div").delegate("[name='eduspan']","click",function(){
		var obj=$(this).find("a[name='edu']");
		var xinzi = obj.attr("class");
		if(xinzi == "radio"){
			$("[name='edu']").attr("class","radio");
			obj.attr("class","radio_ed");
			$('#saveEdu').attr("eduid",obj.attr("dataid"));
			$('#saveEdu').attr("eduame",obj.attr("cname"));
		}else{
			obj.attr("class","radio");
			$('#saveEdu').attr("eduid","");
			$('#saveEdu').attr("eduame","");
		}
	});
	//保存学历
	$('#saveEdu').click(function(){
		var dataid = $(this).attr("eduid");
		var cname = $(this).attr("eduame");
		if(dataid == null || dataid == ""){
			$('#educationid').val("");
		}else{
			$('#educationid').val(dataid);
			$('#edu_click').text(cname);	
		}
		
		closeEdu();
	});
	
	$('#word_fixed').delegate("span[name='code_selected']","click",function(){
		var code = $(this).attr("code");
		window.location.href="#"+code+"_change";
		$(this).attr("class","active");
		$(this).siblings().attr("class","");
		
	});
	
	//保存 标签
	$('#saveMark').click(function(){
		closeMark();
	});
	
 
});

function closeArea(){
	$('#content_main').show();
	$('#areaChange').hide();
}
function closeIndustry(){
	$('#content_main').show();
	$('#industryChange').hide();
}
function closeExper(){
	$('#content_main').show();
	$('#experienceChange').hide();
}
function closeSalary(){
	$('#content_main').show();
	$('#salaryChange').hide();
}
function closeEdu(){
	$('#content_main').show();
	$('#eduChange').hide();
}
 
function closeMark(){
	var markname = "";
	$("#mark_select li").each(function(){
		markname+=$(this).attr("markname")+" ";
	});
	if(markname != ""){
		$('#specil_click').text(markname);	
	}else{
		$('#specil_click').html("&nbsp;");
	}
	$('#content_main').show();
	$('#mark_div').hide();
}

function showAddDiv(){
	$.ajax({
		url:"/interface/isExceed",
		type:"post",
		data:{userid:$('#memberid').val()},
		success:function(resultMap){
		  if(resultMap.status+"" == "0"){
			$('#div_mask').show();
			$('#input_div').show();
			$('#markname_input').focus();
		  }else{
			 swal({
					title : "",
					text : resultMap.msg,
					showCancelButton : false,
					confirmButtonColor : "#D83538",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				});	
		
		  }
		} 
	});
	
	
}

function cancelAddMark(){
	$('#div_mask').hide();
	$('#input_div').hide();
}



//查询省份信息
function getWorkArea(list,areaid){
	var code = "";		
	//判断list是否为空
	if(list != null){
		$("#city_ui").empty();
		$('#word_fixed').empty();
		
		for(var i=0 ; i< list.length ; i++){
			//得到每条数据
			var map = list[i];
			
			//获取显示城市li
			var city_li = $('#'+map.datacode+"_div").attr("id");
			  
			//判断该li是否存在
			if(city_li == undefined || city_li== null){
				//得到模板信息
				var html_li = $("#area_li").html();
				//如果不存在
				$('#word_fixed').append('<span name="code_selected" code="'+map.datacode+'"><a code="'+map.datacode+'" href="javascript:void(0);">'+map.datacode+'</a></span>');
				$("#city_ui").append(hhutil.replace(html_li,map)).trigger("create");
			}
		      if(areaid != null && areaid == map.areaid){
		    	  code = map.datacode;
		    	  $('#'+map.datacode+"_div").append('<span name="areaspan"><a class="radio_ed" name="area_city" cityId="'+map.areaid+'" cityName="'+map.cname+'">选择</a><i>'+map.cname+'</i></span>');  
		      }else{
		    	  $('#'+map.datacode+"_div").append('<span name="areaspan"><a class="radio" name="area_city" cityId="'+map.areaid+'" cityName="'+map.cname+'">选择</a><i>'+map.cname+'</i></span>');
		      }
		}
		if(code != ""){
			window.location.href="#"+code+"_change";
			
			var h = "a[code="+code+"]";
			$("span[name='code_selected']").find(h).parent().attr("class","active");	
		}else{
			$("span[name='code_selected']").eq(0).attr("class","active");
		}
		
	}
		
}
 
	//行业信息
	function getIndustryInfo(list,industid){
		//判断list是否为空
		if(list != null){
			$('#industry_ul').empty();
			for(var i=0; i<list.length; i++){
				
				//得到每条显示的信息
				var map = list[i];
				
				if(map.parentid == null){
					$("#"+map.dataid+"_div").empty();
					//获取大的类型
					var industry_li = $("#"+map.dataid+"_div").attr("id");
					//判断父级类型是否存在
					if(industry_li == undefined || industry_li == null){
						//得到模板信息
						var html_li = $("#industry_li").html();
						//如果不存在
						$("#industry_ul").append(hhutil.replace(html_li,map)).trigger("create");
					}
				}
				//判断信息是二级信息
				if(map.parentid != null){
					//添加数据
					  if(industid != null && industid == map.dataid){
						  $("#"+map.parentid+"_div").append('<span  name="zhiyespan"><a class="radio_ed" id="'+map.typeid+'" name="zhiye" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a><i>'+map.cname+'</i></span>');
					  }else{
						  $("#"+map.parentid+"_div").append('<span name="zhiyespan"><a class="radio" id="'+map.typeid+'" name="zhiye" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a><i>'+map.cname+'</i></span>');  
					  }
						
					
				}
			}
		}
	}
	 
	
	//薪资信息
	function getSalaryInfo(list,salaryid){
		//判断list是否为空
		if(list != null){
			$('#salary_div').empty();
			for(var i=0; i<list.length; i++){
					
				//得到每条显示的信息
				var map = list[i];
				//添加数据
				if(salaryid != null && salaryid == map.dataid ){
					$('#salary_div').append('<span name="xinzispan"><i>'+map.cname+'</i><a class="radio_ed" name="xinzi" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a></span>');
				}else{
					$('#salary_div').append('<span name="xinzispan"><i>'+map.cname+'</i><a class="radio" name="xinzi" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a></span>');
				}
					
			}
		}
	}
	
	//工作经验
	function getExperience(list,expid){
		//判断list是否为空
		if(list != null){
			$('#experience_div').empty();
			for(var i=0; i<list.length; i++){
					
				//得到每条显示的信息
				var map = list[i];
				//添加数据
				if(expid != null && expid == map.dataid){
					$('#experience_div').append('<span name="expspan"><i>'+map.cname+'</i><a class="radio_ed" name="exp" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a></span>');
			
				}else{
					$('#experience_div').append('<span name="expspan"><i>'+map.cname+'</i><a class="radio" name="exp" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a></span>');
				}
			}	
		}
	}
	
	
	//学历要求
	function getEdu(list,eduid){
		//判断list是否为空
		if(list != null){
			$('#edu_div').empty();
			for(var i=0; i<list.length; i++){
				//得到每条显示的信息
				var map = list[i];
				//添加数据
				if(eduid != null && eduid == map.dataid){
					$('#edu_div').append('<span name="eduspan"><i>'+map.cname+'</i><a class="radio_ed" name="edu" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a></span>');
				}else{
					$('#edu_div').append('<span name="eduspan"><i>'+map.cname+'</i><a class="radio" name="edu" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a></span>');
			   }
			}
		}
	}
	
	/**
	 * 标签集合
	 * @param list
	 */
   function getMark(list,selectMarkList){
	   if(list != null){
		   $("#mark_list").html("");
		   $.each(list,function(i,item){
			   var flg = false;
			   if(selectMarkList.length>0){
			   for(var j in selectMarkList){
				  if(selectMarkList[j] == item.markid){
					   flg = true; 
				  }
			     }
			   }
			   if(flg){
				   if(item.type+"" == "1"){
					   $("#mark_list").append('<li class="active" markid="'+item.markid+'" markname="'+item.markname+'"><span onclick="chooseMark(this)">'+item.markname+'</span><div onclick="delMark(this,\''+item.markid+'\')" class="del">×</div></li>');
				   }else{
					   $("#mark_list").append('<li class="active" markid="'+item.markid+'" markname="'+item.markname+'"><span onclick="chooseMark(this)">'+item.markname+'</span></li>'); 
				   }
				   
			   }else{
                   if(item.type+"" == "1"){
                	   $("#mark_list").append('<li markid="'+item.markid+'" markname="'+item.markname+'"><span onclick="chooseMark(this)">'+item.markname+'</span><div onclick="delMark(this,\''+item.markid+'\')" class="del">×</div></li>');   
				   }else{
					   $("#mark_list").append('<li markid="'+item.markid+'" markname="'+item.markname+'"><span onclick="chooseMark(this)">'+item.markname+'</span></li>');
				   }
				      
			   }
		   });
	   }
	   
   }	
	
   function delMark(dom,markid){
	   swal({
				title : "",
				text : "您确认要删除当前标签？",
				showCancelButton : true,
				confirmButtonColor : "#D83538",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			},function(){
				$.ajax({
					url:'/interface/delPositionMark',
					type:'post',
					data:{markid:markid},
					success:function(data){
						if(data.status+"" == "0"){
							$(dom).parent().remove();
							$.each($("#mark_select li"),function(i,item){
								 if($(this).attr("markid") == markid){
									 $(this).remove();
								 }
							});
							$("#count_num").text($("#mark_select li").length);
						}else{
							  swal({
									title : "",
									text : data.msg,
									showCancelButton : false,
									confirmButtonColor : "#D83538",
									confirmButtonText : "确认",
									cancelButtonText : "取消",
									closeOnConfirm : true
								});	
						}
					}
					
				});
				
				
			});	
   }
   
   /**
    * 点击li 选中标签
    * @param obj
    */
   function chooseMark(obj){
	   if(!$(obj).parent().hasClass("active")){
		  var len = $("#mark_select").find("li").length+1;
		  if(len > 8){
			  return;
		  } 
		  var markname = $(obj).parent().attr("markname")
		  var markid = $(obj).parent().attr("markid")
		  $("#mark_select").append('<li markid="'+markid+'" markname="'+markname+'"><span>'+markname+'</span><a href="javascript:void(0)" onclick="cancelMark(this)"><img width="12px" height="12px" src="/appcssjs/images/public/close_02.png"></a></li>');
		  $(obj).parent().attr("class","active");
		  $('#count_num').text(len);
	   }
   }
   
   /**
    * 取消标签
    * @param obj
    */
   function cancelMark(obj){ 
	   var markid = $(obj).parent().attr("markid");
	   $(obj).parent().remove();
	   $("#mark_list").find("li[markid='"+markid+"']").attr("class"," ");
	   $('#count_num').text(parseInt($('#count_num').text())-1);
   }
    
   
   function addMark(){
	   var markname = $('#markname_input').val();
	   if(markname == ""){
		   swal({
				title : "",
				text : "标签名称不能为空！",
				showCancelButton : false,
				confirmButtonColor : "#D83538",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			});	
		return;
	   }
	   var data = new Object();
	   data.markname = markname;
	   data.userid = $('#memberid').val();
		$.ajax({
			type:"post",
			data:data,
			url:"/interface/addPositionMark",
			success:function(da){
				if(da.markid != null){
					$("#mark_list").append('<li  markid="'+da.markid+'" markname="'+da.markname+'"><span onclick="chooseMark(this)">'+da.markname+'</span><div onclick="delMark(this,\''+da.markid+'\')" class="del">×</div></li>');
					//$("#mark_select").append('<li markid="'+da.markid+'" markname="'+da.markname+'"><span>'+da.markname+'</span><a href="javascript:void(0)" onclick="cancelMark(this)"><img src="/appcssjs/images/public/close_02.png"></a></li>');
					$('#markname_input').val("");
					cancelAddMark();
					
				}
			},error:function(e){
				
			}
			
		});
	   
   }
   
   
   function subData(){
	if($("#positionname").val() == ""){
		 swal({
				title : "",
				text : "岗位名称不能为空！",
				showCancelButton : false,
				confirmButtonColor : "#D83538",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			});	
		return;
	}
	
	if($("#worktype").val() == ""){
		 swal({
				title : "",
				text : "请选择工种！",
				showCancelButton : false,
				confirmButtonColor : "#D83538",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			});	
		return;
	}
	
	
	
	if($("#worktype").val() == ""){
		 swal({
				title : "",
				text : "请选择工种！",
				showCancelButton : false,
				confirmButtonColor : "#D83538",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			});	
		return;
	}
	
	if($("#recruitnum").val() == ""){
		 swal({
				title : "",
				text : "招聘人数不能为空！",
				showCancelButton : false,
				confirmButtonColor : "#D83538",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			});	
		return;
	}
	
	if($("#educationid").val() == ""){
		 swal({
				title : "",
				text : "请选择学历！",
				showCancelButton : false,
				confirmButtonColor : "#D83538",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			});	
		return;
	}

	if($("#experienceid").val() == ""){
		 swal({
				title : "",
				text : "请选择经验要求！",
				showCancelButton : false,
				confirmButtonColor : "#D83538",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			});	
		return;
	}
	
	if($("#salaryid").val() == ""){
		 swal({
				title : "",
				text : "请选择薪水范围！",
				showCancelButton : false,
				confirmButtonColor : "#D83538",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			});	
		return;
	}
	
	if($("#cityid").val() == ""){
		 swal({
				title : "",
				text : "请选择城市！",
				showCancelButton : false,
				confirmButtonColor : "#D83538",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			});	
		return;
	}
	
	if($("#address").val() == ""){
		 swal({
				title : "",
				text : "详细地址不能为空！",
				showCancelButton : false,
				confirmButtonColor : "#D83538",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			});	
		return;
	}
	
	if($("#address").val().length>500){
		 swal({
				title : "",
				text : "详细地址长度不能操过500个字符！",
				showCancelButton : false,
				confirmButtonColor : "#D83538",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			});	
		return;
	}
	
	if($("#requirement").val() == ""){
		 swal({
				title : "",
				text : "岗位描述不能为空！",
				showCancelButton : false,
				confirmButtonColor : "#D83538",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			});	
		return;
	}
	
	if($("#requirement").val().length >1500 ){
		 swal({
				title : "",
				text : "岗位描述长度不能大于1500个字符！",
				showCancelButton : false,
				confirmButtonColor : "#D83538",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			});	
		return;
	}
	
	var mk = "";
	$("#mark_select li").each(function(){
		mk+=$(this).attr("markid")+",";
	});
	
	var data = hhutil.getFormBean("p_form");
	    data.mk = mk.substring(0,mk.length-1);
	    console.log(data);
	    return;
	$.ajax({
		type:"post",
		data:data,
		url:"/interface/addPosition",
		success:function(da){
			if(da.flg){
				if($('#operate').val() != ""){
					swal({
						title : "",
						text : "修改成功！",
						showCancelButton : false,
						confirmButtonColor : "#D83538",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
						},function(){
							window.location.href='/weixin/myposition'
					});	
					
				}else{
					swal({
						title : "",
						text : "新增成功！",
						showCancelButton : false,
						confirmButtonColor : "#D83538",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
						},function(){
							window.location.href='/weixin/myposition'
					});	
					
				}
				
				 
			}
			
		},error:function(e){
			
		}
		
	});
	
}
	
	