
var arealist={};
var refreshlist={};
var industrylist={};
var salarylist={};
var experiencelist={};
var educationlist={};

$(function(){
	//查询简历的筛选的类型信息
	$.ajax({
		url:"/interface/getResumeScreenType",
		type:"post",
		success:function(resultMap){
			//获取数据集合
			var list = resultMap.data;
			if(list != null && list != ""){
				
				$.each(list,function(i,map){
					var str='<li id="'+map.showtype+'_screentype" style="cursor: pointer;">'+
								'<span>'+map.name+'</span><i><font id="'+map.showtype+'_font">不限</font>&nbsp;</i>'+
								'<input type="hidden" name="'+map.showtype+'id" id="'+map.showtype+'id" value=""/>'+
								'<input type="hidden" name="'+map.showtype+'" id="'+map.showtype+'name" value=""/>'+
							'</li>';
					$('#screen_ul').append(str);
				});
			}
			
			//数据回显
			var searchMap=$('#querycondtion').val();
			var jsonmap = "";
			if(searchMap != null && searchMap != "" && searchMap != undefined){
				jsonmap = JSON.parse(searchMap);
			}
			if(jsonmap != ""){
				if(jsonmap.searchContent != "" && jsonmap.searchContent != undefined){
					$('#searchContent').val(jsonmap.searchContent);
					searchInput();
				}
				if(jsonmap.cityid != ""){
					$('#cityid').val(jsonmap.cityid);
					$('#cityname').val(jsonmap.city);
					$('#city_font').text(jsonmap.city);
				}
				
				if(jsonmap.industryid != ""){
					$('#industryid').val(jsonmap.industryid);
					$('#industryname').val(jsonmap.industry);
					$('#industry_font').text(jsonmap.industry);
				}
				
				if(jsonmap.experienceid != ""){
					$('#experienceid').val(jsonmap.experienceid);
					$('#experiencename').val(jsonmap.experience);
					$('#experience_font').text(jsonmap.experience);
				}
				if(jsonmap.educationid != ""){
					$('#educationid').val(jsonmap.educationid);
					$('#educationname').val(jsonmap.education);
					$('#education_font').text(jsonmap.education);
				}
				if(jsonmap.salaryid != ""){
					$('#salaryid').val(jsonmap.salaryid);
					$('#salaryname').val(jsonmap.salary);
					$('#salary_font').text(jsonmap.salary);
				}
				if(jsonmap.refreshtimeid != ""){
					$('#refreshtimeid').val(jsonmap.refreshtimeid);
					$('#refreshtimename').val(jsonmap.refreshtime);
					$('#refreshtime_font').text(jsonmap.refreshtime);
				}
			}
		},
		error:function(){}
	});
	
	
	//筛选类型和类型详情的页面的切换
	$('#city_screentype').click(function(){
		var cityid = $(this).find('#cityid').val();
		var cityname = $(this).find('#cityname').val();
		$('#saveCityInfo').attr("cityid",cityid);
		$('#saveCityInfo').attr("cityname",cityname);
		showAreaInfo(arealist,cityid);
		$('#find_show_hide').hide();
		$('#area_show_hide').show();
	});
	$('#screen_ul').delegate("#refreshtime_screentype","click",function(){
		var refreshtimeid = $(this).find('#refreshtimeid').val();
		var refreshtimename = $(this).find('#refreshtimename').val();
		$('#saveRefreshtimeInfo').attr("refreshtimeid",refreshtimeid);
		$('#saveRefreshtimeInfo').attr("refreshtimename",refreshtimename);
		showRefreshtimeInfo(refreshlist,refreshtimeid);
		$('#find_show_hide').hide();
		$('#refreshtime_show_hide').show();
	});
	$('#screen_ul').delegate("#industry_screentype","click",function(){
		var industryid = $(this).find('#industryid').val();
		var industryname = $(this).find('#industryname').val();
		$('#saveIndustryInfo').attr("industryid",industryid);
		$('#saveIndustryInfo').attr("industryname",industryname);
		showIndustryInfo(industrylist,industryid);
		$('#find_show_hide').hide();
		$('#industry_show_hide').show();
	});
	$('#screen_ul').delegate("#salary_screentype","click",function(){
		var salaryid = $(this).find('#salaryid').val();
		var salaryname = $(this).find("#salaryname").val();
		$('#saveSalaryInfo').attr("salaryid",salaryid);
		$('#saveSalaryInfo').attr("salaryname",salaryname);
		showSalaryInfo(salarylist,salaryid);
		$('#find_show_hide').hide();
		$('#salary_show_hide').show();
	});
	$('#screen_ul').delegate("#experience_screentype","click",function(){
		var experienceid = $(this).find('#experienceid').val();
		var experiencename = $(this).find('#experiencename').val();
		$('#saveExperienceInfo').attr("experienceid",experienceid);
		$('#saveExperienceInfo').attr("experiencename",experiencename);
		showExperienceInfo(experiencelist,experienceid);
		$('#find_show_hide').hide();
		$('#experience_show_hide').show();
	});
	$('#screen_ul').delegate("#education_screentype","click",function(){
		var educationid = $(this).find('#educationid').val();
		var educationname = $(this).find('#educationname').val();
		$('#saveEducationInfo').attr("educationid",educationid);
		$('#saveEducationInfo').attr("educationname",educationname);
		showEducationInfo(educationlist,educationid);
		$('#find_show_hide').hide();
		$('#education_show_hide').show();
	});
	//筛选类型，和简历页面的切换
	$('#click_searchInput').click(function(){
		getResumeScreenTypeDetail();
		$('#resume_list').hide();
		$('#find_show_hide').show();
	});
	$('#closeFind').click(function(){
		$('#resume_list').show();
		$('#find_show_hide').hide();
	});
	
	
	//选中信息的保存
	$('#saveCityInfo').click(function(){
		var cityid = $(this).attr("cityid");
		var cityname = $(this).attr("cityname");
		$('#cityid').val(cityid);
		$('#cityname').val(cityname);
		$('#city_font').text(cityname==""?"不限":cityname);
		$('#find_show_hide').show();
		$('#area_show_hide').hide();
	});
	$('#saveRefreshtimeInfo').click(function(){
		var refreshtimeid = $(this).attr("refreshtimeid");
		var refreshtimename = $(this).attr("refreshtimename");
		$('#refreshtimeid').val(refreshtimeid);
		$('#refreshtimename').val(refreshtimename);
		$('#refreshtime_font').text(refreshtimename==""?"不限":refreshtimename);
		$('#find_show_hide').show();
		$('#refreshtime_show_hide').hide();
	});
	$('#saveIndustryInfo').click(function(){
		var industryid = $(this).attr("industryid");
		var industryname = $(this).attr("industryname");
		$('#industryid').val(industryid);
		$('#industryname').val(industryname);
		$('#industry_font').text(industryname==""?"不限":industryname);
		$('#find_show_hide').show();
		$('#industry_show_hide').hide();
	});
	$('#saveSalaryInfo').click(function(){
		var salaryid = $(this).attr("salaryid");
		var salaryname = $(this).attr("salaryname");
		$('#salaryid').val(salaryid);
		$('#salaryname').val(salaryname);
		$('#salary_font').text(salaryname==""?"不限":salaryname);
		$('#find_show_hide').show();
		$('#salary_show_hide').hide();
	});
	$('#saveExperienceInfo').click(function(){
		var experienceid = $(this).attr("experienceid");
		var experiencename = $(this).attr("experiencename");
		$('#experienceid').val(experienceid);
		$('#experiencename').val(experiencename);
		$('#experience_font').text(experiencename==""?"不限":experiencename);
		$('#find_show_hide').show();
		$('#experience_show_hide').hide();
	});
	$('#saveEducationInfo').click(function(){
		var educationid = $(this).attr("educationid");
		var educationname = $(this).attr("educationname");
		$('#educationid').val(educationid);
		$('#educationname').val(educationname);
		$('#education_font').text(educationname==""?"不限":educationname);
		$('#find_show_hide').show();
		$('#education_show_hide').hide();
	});
	
	
	//模糊查询提交
	$('#search_screen').click(function(){
		$('#searchContent_form').submit();
	});
	
	//搜索条件查询
	$('#screen_input').click(function(){
		$('#screen_form').submit();
	});
	
	
//	$('#word_fixed').delegate("span[name='code_selected']","click",function(){
//		$('span[name="code_selected"]').attr("class","");
//		$(this).attr("class","active");
//	});
});

//城市ABCD...选中的样式的切换
function changeWordFixed(obj){
	$('span[name="code_selected"]').attr("class","");
	$(obj).attr("class","active");
}
//输入文本框输入内容的事发后出发
function searchInput(){
	var text = $('#searchContent').val().replace(/\s/gi,'');
	if(text.length > 0){
		$('.find_page').hide();
	}else{
		$('#searchContent').val("");
		$('.find_page').show();
	}
}

//关闭筛选类型的详情页面
function closeScreenPage(type){
	$('#find_show_hide').show();
	$('#'+type+'_show_hide').hide();
}
//获取所有的筛选的具体信息
function getResumeScreenTypeDetail(){
	$.ajax({
		url:"/interface/getResumeScreenTypeDetail",
		type:"post",
		success:function(resultMap){
			arealist=resultMap.data.arealist;
			refreshlist=resultMap.data.refreshlist;
			industrylist=resultMap.data.industrylist;
			salarylist=resultMap.data.salarylist;
			experiencelist=resultMap.data.experiencelist;
			educationlist=resultMap.data.educationlist;
		},
		error:function(){}
	});
}

//显示城市信息
function showAreaInfo(list,areaid){
	//判断list是否为空
	if(list != null){
		$("#city_ul").empty();
		$('#word_fixed').empty();
		var code="";
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
				$('#word_fixed').append('<span name="code_selected" id="'+map.datacode+'_span" onclick="changeWordFixed(this)"><a href="#'+map.datacode+'_change">'+map.datacode+'</a></span>');
				$("#city_ul").append(hhutil.replace(html_li,map)).trigger("create");
			}
			if(areaid == map.areaid){
				code=map.datacode;
				$('#'+map.datacode+"_div").append('<span cityId="'+map.areaid+'" cityName="'+map.cname+'" onclick="changeArea(this)" style="cursor: pointer;"><a class="radio_ed" name="area_a">选择</a><i>'+map.cname+'</i></span>');
			}else{
				$('#'+map.datacode+"_div").append('<span cityId="'+map.areaid+'" cityName="'+map.cname+'" onclick="changeArea(this)" style="cursor: pointer;"><a class="radio" name="area_a">选择</a><i>'+map.cname+'</i></span>');
			}
			
		}
		if(code==""){
			$('span[name="code_selected"]:first').attr("class","active");
		}else{
			window.location.href='#'+code+'_change';
			$('#'+code+'_span').attr("class","active");
		}
	}
}
function changeArea(obj){
	var checked = $(obj).find('a[name="area_a"]').attr("class");
	if(checked == "radio"){
		var cityid = $(obj).attr("cityId");
		var cityname = $(obj).attr("cityName");
		$('#saveCityInfo').attr("cityid",cityid);
		$('#saveCityInfo').attr("cityname",cityname);
		$('a[name="area_a"]').attr("class","radio");
		$(obj).find('a[name="area_a"]').attr("class","radio_ed");
	}else{
		$(obj).find('a[name="area_a"]').attr("class","radio");
		$('#saveCityInfo').attr("cityid","");
		$('#saveCityInfo').attr("cityname","不限");
	}
}

//显示行业职位信息
function showIndustryInfo(list,industryid){
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
				if(industryid == map.dataid){
					$("#"+map.parentid+"_div").append('<span dataid="'+map.dataid+'" cname="'+map.cname+'" onclick="changeIndustry(this)" style="cursor: pointer;"><a class="radio_ed" name="industry_a">选择</a><i>'+map.cname+'</i></span>');
				}else{
					$("#"+map.parentid+"_div").append('<span dataid="'+map.dataid+'" cname="'+map.cname+'" onclick="changeIndustry(this)" style="cursor: pointer;"><a class="radio" name="industry_a">选择</a><i>'+map.cname+'</i></span>');
				}
			}
		}
	}
}
function changeIndustry(obj){
	var checked = $(obj).find('a[name="industry_a"]').attr("class");
	if(checked == "radio"){
		var industryid = $(obj).attr("dataid");
		var industryname = $(obj).attr("cname");
		$('#saveIndustryInfo').attr("industryid",industryid);
		$('#saveIndustryInfo').attr("industryname",industryname);
		$('a[name="industry_a"]').attr("class","radio");
		$(obj).find('a[name="industry_a"]').attr("class","radio_ed");
	}else{
		$(obj).find('a[name="industry_a"]').attr("class","radio");
		$('#saveIndustryInfo').attr("industryid","");
		$('#saveIndustryInfo').attr("industryname","不限");
	}
}


//显示工作经验信息
function showExperienceInfo(list,experienceid){
	//判断list是否为空
	if(list != null){
		$('#experience_div').empty();
		for(var i=0; i<list.length; i++){
				
			//得到每条显示的信息
			var map = list[i];
			//添加数据
			if(experienceid == map.dataid){
				$('#experience_div').append('<span dataid="'+map.dataid+'" cname="'+map.cname+'" onclick="changeEXperience(this)" style="cursor: pointer;"><i>'+map.cname+'</i><a class="radio_ed" name="experience_a">选择</a></span>');
			}else{
				$('#experience_div').append('<span dataid="'+map.dataid+'" cname="'+map.cname+'" onclick="changeEXperience(this)" style="cursor: pointer;"><i>'+map.cname+'</i><a class="radio" name="experience_a">选择</a></span>');
			}
		}
	}
}
function changeEXperience(obj){
	var checked = $(obj).find('a[name="experience_a"]').attr("class");
	if(checked == "radio"){
		var experienceid = $(obj).attr("dataid");
		var experiencename = $(obj).attr("cname");
		$('#saveExperienceInfo').attr("experienceid",experienceid);
		$('#saveExperienceInfo').attr("experiencename",experiencename);
		$('a[name="experience_a"]').attr("class","radio");
		$(obj).find('a[name="experience_a"]').attr("class","radio_ed");
	}else{
		$(obj).find('a[name="experience_a"]').attr("class","radio");
		$('#saveExperienceInfo').attr("experienceid","");
		$('#saveExperienceInfo').attr("experiencename","不限");
	}
}


//显示教育经历信息
function showEducationInfo(list,educationid){
	//判断list是否为空
	if(list != null){
		$('#education_div').empty();
		for(var i=0; i<list.length; i++){
				
			//得到每条显示的信息
			var map = list[i];
			//添加数据
			if(educationid == map.dataid){
				$('#education_div').append('<span dataid="'+map.dataid+'" cname="'+map.cname+'" onclick="changeEducation(this)" style="cursor: pointer;"><i>'+map.cname+'</i><a class="radio_ed" name="education_a">选择</a></span>');
			}else{
				$('#education_div').append('<span dataid="'+map.dataid+'" cname="'+map.cname+'" onclick="changeEducation(this)" style="cursor: pointer;"><i>'+map.cname+'</i><a class="radio" name="education_a">选择</a></span>');
			}
		}
	}
}
function changeEducation(obj){
	var checked = $(obj).find('a[name="education_a"]').attr("class");
	if(checked == "radio"){
		var educationid = $(obj).attr("dataid");
		var educationname = $(obj).attr("cname");
		$('#saveEducationInfo').attr("educationid",educationid);
		$('#saveEducationInfo').attr("educationname",educationname);
		$('a[name="education_a"]').attr("class","radio");
		$(obj).find('a[name="education_a"]').attr("class","radio_ed");
	}else{
		$(obj).find('a[name="education_a"]').attr("class","radio");
		$('#saveEducationInfo').attr("educationid","");
		$('#saveEducationInfo').attr("educationname","不限");
	}
}


//显示薪资信息
function showSalaryInfo(list,salaryid){
	//判断list是否为空
	if(list != null){
		$('#salary_div').empty();
		for(var i=0; i<list.length; i++){
				
			//得到每条显示的信息
			var map = list[i];
			//添加数据
			if(salaryid == map.dataid){
				$('#salary_div').append('<span dataid="'+map.dataid+'" cname="'+map.cname+'" onclick="changeSalary(this)" style="cursor: pointer;"><i>'+map.cname+'</i><a class="radio_ed" name="salary_a">选择</a></span>');
			}else{
				$('#salary_div').append('<span dataid="'+map.dataid+'" cname="'+map.cname+'" onclick="changeSalary(this)" style="cursor: pointer;"><i>'+map.cname+'</i><a class="radio" name="salary_a">选择</a></span>');
			}
		}
	}
}
function changeSalary(obj){
	var checked = $(obj).find('a[name="salary_a"]').attr("class");
	if(checked == "radio"){
		var salaryid = $(obj).attr("dataid");
		var salaryname = $(obj).attr("cname");
		$('#saveSalaryInfo').attr("salaryid",salaryid);
		$('#saveSalaryInfo').attr("salaryname",salaryname);
		$('a[name="salary_a"]').attr("class","radio");
		$(obj).find('a[name="salary_a"]').attr("class","radio_ed");
	}else{
		$(obj).find('a[name="salary_a"]').attr("class","radio");
		$('#saveSalaryInfo').attr("salaryid","");
		$('#saveSalaryInfo').attr("salaryname","不限");
	}
}


//显示刷新时间信息
function showRefreshtimeInfo(list,refreshtimeid){
	//判断list是否为空
	if(list != null){
		$('#refresh_div').empty();
		for(var i=0; i<list.length; i++){
				
			//得到每条显示的信息
			var map = list[i];
			//添加数据
			if(refreshtimeid == map.dataid){
				$('#refresh_div').append('<span dataid="'+map.dataid+'" cname="'+map.cname+'" onclick="changeRefreshtime(this)" style="cursor: pointer;"><i>'+map.cname+'</i><a class="radio_ed" name="refreshtime_a">选择</a></span>');
			}else{
				$('#refresh_div').append('<span dataid="'+map.dataid+'" cname="'+map.cname+'" onclick="changeRefreshtime(this)" style="cursor: pointer;"><i>'+map.cname+'</i><a class="radio" name="refreshtime_a">选择</a></span>');
			}
		}
	}
}
function changeRefreshtime(obj){
	var checked = $(obj).find('a[name="refreshtime_a"]').attr("class");
	if(checked == "radio"){
		var refreshtimeid = $(obj).attr("dataid");
		var refreshtimename = $(obj).attr("cname");
		$('#saveRefreshtimeInfo').attr("refreshtimeid",refreshtimeid);
		$('#saveRefreshtimeInfo').attr("refreshtimename",refreshtimename);
		$('a[name="refreshtime_a"]').attr("class","radio");
		$(obj).find('a[name="refreshtime_a"]').attr("class","radio_ed");
	}else{
		$(obj).find('a[name="refreshtime_a"]').attr("class","radio");
		$('#saveRefreshtimeInfo').attr("refreshtimeid","");
		$('#saveRefreshtimeInfo').attr("refreshtimename","不限");
	}
}

