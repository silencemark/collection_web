
var citylist = {};
var industrylist = {};
var salarylist = {};
var distancelist = {};

$(function(){
	var searchMap=$('#searchMap').val();
	var jsonmap = JSON.parse(searchMap);
	
	var latitude = $("#latitude").val();
	
	//初始化页面信息
	$.ajax({
		url:"/interface/initEditScreenFind",
		type:"post",
		success:function(resultMap){
			//获取筛选列表信息
			var list = resultMap.data.screentypelist;
			
			citylist = resultMap.data.citylist;
			industrylist = resultMap.data.industrylist;
			salarylist = resultMap.data.salarylist;
			distancelist = resultMap.data.distancelist;
			
			//判断筛选列表信息是否为空
			if(list != null){
				//循环显示信息
				for(var i=0; i<list.length; i++){
					
					//得到每条数据
					var map = list[i];
					
					var id="";
					var name="";
					var method="";
					if(map.ename ==  "industryid"){
						method="industry_click()";
						id= jsonmap.industryid;
						name =jsonmap.industryname;
						if(name== null || name == ""){
							name="不限";
						}
//						$('#saveIndustry').attr("industryid",id);
//						$('#saveIndustry').attr("industryname",name);
					}
					if(map.ename ==  "salaryid"){
						method="salary_click()";
						id= jsonmap.salaryid;
						name =jsonmap.salaryname;
						if(name== null || name == ""){
							name="不限";
						}
//						$('#saveSalary').attr("salaryid",id);
//						$('#saveSalary').attr("salaryname",name);
					}
					if(map.ename ==  "distance"){
						method="distance_click()";
						id= jsonmap.distanceid;
						name =jsonmap.distancename;
						if(name== null || name == ""){
							name="不限";
						}
//						$('#saveDistance').attr("distanceid",id);
//						$('#saveDistance').attr("distancename",name);
					}
					
					$('#screentype').append('<li onclick="'+method+'" id="'+map.showtype+'" datatypeid="'+map.datatypeid+'" style="cursor: pointer;"><span>'+map.name+'</span><i id="show_'+map.showtype+'name">'+name+'</i><input type="hidden" id="'+map.showtype+'id" name="'+map.showtype+'id" value="'+id+'"><input type="hidden" id="'+map.showtype+'name" name="'+map.showtype+'name" value="'+name+'"></li>');
					
				}
				
				if(latitude == ""){
					$('#distance').hide();
				}
			}
		},
		error:function(){}
	});
	
	
	
	var cid = jsonmap.cityid;
	var cname = jsonmap.cityname;
	$('#areaname').val(cname);
	if(cname== null || cname == ""){
		cname="不限";
	}
	$('#show_areaname').text(cname);
	$('#areaid').val(cid);
	$('#saveArea').attr("cityid",cid);
	$('#saveArea').attr("cityname",cname);
	
	$('#seachInput').val(jsonmap.keyword);
	var searchtext = jsonmap.keyword;
	if(searchtext.length > 0){
		$('.find_page').hide();
	}else{
		$('.find_page').show();
	}
	
	$('#ishot_input').val(jsonmap.ishot);
	$('#companyid_input').val(jsonmap.companyid);
	
	$('#ishot').val(jsonmap.ishot);
	$('#companyid').val(jsonmap.companyid);
	
	//页面样式切换
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	//自定义筛选条件查询
	$('#saveInfo').click(function(){
		$('#tiaojiansousuo').submit();
	});
	
	$('#searchbutton').click(function(){
		$('#screenTypeform').submit();
	});

	//点击搜索
	$('#word_fixed').delegate("span[name='code_selected']","click",function(){
		$('span[name="code_selected"]').attr("class","");
		$(this).attr("class","active");
	});
	
});

function closeArea(){
	$('#findType').show();
	$('#areaChange').hide();
}
function closeIndustry(){
	$('#findType').show();
	$('#industryChange').hide();
}
function closeDistance(){
	$('#findType').show();
	$('#distanceChange').hide();
}
function closeSalary(){
	$('#findType').show();
	$('#salaryChange').hide();
}
//输入文本框输入内容的事发后出发
function searchInput(){
	var text = $('#seachInput').val().replace(/\s/gi,'');
	if(text.length > 0){
		$('.find_page').hide();
	}else{
		$('#seachInput').val("");
		$('.find_page').show();
	}
}

//查询省份信息
function getWorkArea(list,areaid){
			
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
				$('#word_fixed').append('<span name="code_selected"><a href="#'+map.datacode+'_change">'+map.datacode+'</a></span>');
				$("#city_ui").append(hhutil.replace(html_li,map)).trigger("create");
			}
			if(areaid == map.areaid){
				$('#'+map.datacode+"_div").append('<span name="areaspan" onclick="areaspan_click(this)"><a class="radio_ed" name="area_city" cityId="'+map.areaid+'" cityName="'+map.cname+'">选择</a><i>'+map.cname+'</i></span>');
			}else{
				$('#'+map.datacode+"_div").append('<span name="areaspan" onclick="areaspan_click(this)"><a class="radio" name="area_city" cityId="'+map.areaid+'" cityName="'+map.cname+'">选择</a><i>'+map.cname+'</i></span>');
			}
			
		}
		$('span[name="code_selected"]:first').attr("class","active");
	}
		
}

	
	//行业信息
	function getIndustryInfo(list,selectid){
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
					if(selectid == map.dataid){
						$("#"+map.parentid+"_div").append('<span name="zhiyespan" onclick="zhiyespan_click(this)"><a class="radio_ed" id="'+map.typeid+'" name="zhiye" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a><i>'+map.cname+'</i></span>');
					}else{
						$("#"+map.parentid+"_div").append('<span name="zhiyespan" onclick="zhiyespan_click(this)"><a class="radio" id="'+map.typeid+'" name="zhiye" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a><i>'+map.cname+'</i></span>');
					}
				}
			}
		}
	}
	
	
	//距离信息
	function getDistanceInfo(list,selectid){
		//判断list是否为空
		if(list != null){
			$('#distance_div').empty();
			for(var i=0; i<list.length; i++){
					
				//得到每条显示的信息
				var map = list[i];
				//添加数据
				if(selectid == map.dataid){
					$('#distance_div').append('<span name="julispan" onclick="julispan_click(this)"><i>'+map.cname+'</i><a class="radio_ed" id="'+map.typeid+'" name="juli" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a></span>');
				}else{
					$('#distance_div').append('<span name="julispan" onclick="julispan_click(this)"><i>'+map.cname+'</i><a class="radio" id="'+map.typeid+'" name="juli" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a></span>');
				}
			}
		}
	}
	
	//薪资信息
	function getSalaryInfo(list,selectid){
		//判断list是否为空
		if(list != null){
			$('#salary_div').empty();
			for(var i=0; i<list.length; i++){
					
				//得到每条显示的信息
				var map = list[i];
				//添加数据
				if(selectid == map.dataid){
					$('#salary_div').append('<span name="xinzispan" onclick="xinzispan_click(this)"><i>'+map.cname+'</i><a class="radio_ed" name="xinzi" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a></span>');
				}else{
					$('#salary_div').append('<span name="xinzispan" onclick="xinzispan_click(this)"><i>'+map.cname+'</i><a class="radio" name="xinzi" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a></span>');
				}
			}
		}
	}
	
	/**
	 * 	工作选择的切换
	 */
	function area_click(){
		var areaid = $('#areaid').val();
		var areaname = $('#areaname').val();
		
		$('#saveArea').attr("cityid",areaid);
		$('#saveArea').attr("cityname",areaname);
		
		getWorkArea(citylist,areaid);
		$('#findType').hide();
		$('#areaChange').show();
	}
	
	/**
	 * 	行业类别的选择切换
	 */
	function industry_click(){
		var typeid = $('#industryid').val();
		var indsutryname = $('#industryname').val();
		
		$('#saveIndustry').attr("industryid",typeid);
		$('#saveIndustry').attr("industryname",indsutryname);
		
		getIndustryInfo(industrylist,typeid);
		$('#findType').hide();
		$('#industryChange').show();
	}
	
	/**
	 * 	距离范围选择的样式切换
	 */
	function distance_click(){
		var typeid = $('#distanceid').val();
		var distancename = $('#distancename').val();
		
		$('#saveDistance').attr("distanceid",typeid);
		$('#saveDistance').attr("distancename",distancename);
		
		getDistanceInfo(distancelist,typeid);
		$('#findType').hide();
		$('#distanceChange').show();
	}
	
	/**
	 * 	薪资范围选择的样式切换
	 */
	function salary_click(){
		var typeid = $('#salaryid').val();
		var salaryname = $('#salaryname').val();
		
		$('#saveSalary').attr("salaryid",typeid);
		$('#saveSalary').attr("salaryname",salaryname);
		
		getSalaryInfo(salarylist,typeid);
		$('#findType').hide();
		$('#salaryChange').show();
	}
	
	/**
	 * 	工作区域的样式--选择
	 */
	function areaspan_click(object){
		var obj = $(object).find("a[name='area_city']");
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
	}

	/**
	 * 	工作区域选择后的保存
	 */
	function saveArea_click(){
		var cityid = $("#saveArea").attr("cityid");
		var cityname = $("#saveArea").attr("cityname");
		
		if(cityname == ""){
			$('#show_areaname').text("不限");
		}else{
			$('#show_areaname').text(cityname);
		}
		if(cityid != null && cityid != ""){
			$('#areaid').val(cityid);
		}else{
			$('#areaid').val("");
		}
		$('#areaname').val(cityname);
		closeArea();
	}
	
	
	/**
	 * 	行业样式
	 * @param object
	 */
	function zhiyespan_click(object){
		var obj = $(object).find("a[name='zhiye']");
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
	}
	
	/**
	 * 	行业选择--保存
	 */
	function saveIndustry_click(){
		var dataid = $("#saveIndustry").attr("industryid");
		var cname = $("#saveIndustry").attr("industryname");
		if(cname == null || cname == ""){
			cname="不限";
		}
		$('#show_industryname').text(cname);
		
		$('#industryname').val(cname);
		$('#industryid').val(dataid);
		closeIndustry();
	}
	
	/**
	 * 	薪资样式
	 */
	function xinzispan_click(object){
		var obj=$(object).find("a[name='xinzi']");
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
	}
	
	/**
	 * 	薪资样式保存
	 */
	function saveSalary_click(){
		var dataid = $("#saveSalary").attr("salaryid");
		var cname = $("#saveSalary").attr("salaryname");
		
		$('#salaryid').val(dataid);
		$('#salaryname').val(cname);
		if(cname == null || cname == ""){
			cname="不限";
		}
		$('#show_salaryname').text(cname);
		
		closeSalary();
	}
	
	/**
	 * 	距离信息样式选择
	 */
	function julispan_click(object){
		var obj = $(object).find("a[name='juli']");
		var check = obj.attr("class");
		if(check == "radio"){
			$('[name="juli"]').attr("class","radio");
			obj.attr("class","radio_ed");
			var dataid = obj.attr("dataid");
			var cname = obj.attr("cname");
			$('#saveDistance').attr("distanceid",dataid);
			$('#saveDistance').attr("distancename",cname);
		}else{
			obj.attr("class","radio");
			$('#saveDistance').attr("distanceid","");
			$('#saveDistance').attr("distancename","");
		}
	}
	
	/**
	 * 	距离信息保存
	 */
	function saveDistance_click(){
		var id = $("#saveDistance").attr("distanceid");
		var name = $("#saveDistance").attr("distancename");
		$('#distanceid').val(id);
		$('#distancename').val(name);
		if(name == "" || name == null){
			name="不限";
		}
		$('#show_distancename').text(name);
		closeDistance();
	}
	
	