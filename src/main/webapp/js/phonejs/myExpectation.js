
var citylist={};
var industrylist={};
var salarylist={};

$(function(){
		//初始化页面信息
		var userid = $('#userid').val();
		$.ajax({
			url:"/interface/initResumeTwo",
			data:"userid="+userid,
			type:"post",
			success:function(resultMap){
				//得到城市信息
				citylist = resultMap.data.citylist;
				
				//得到行业信息的集合
				industrylist = resultMap.data.industrylist;
				
				//得到薪资信息的集合
				salarylist = resultMap.data.salarylist;
				
			},
			error:function(){}
		});
		
/*-------------------------------------------------*/
		
		//行业选择，页面div的切换
		$('#expectIndustry').click(function(){
			var industryid = $('#industryid').val();
			var industryname = $('#industryname').text();
			
			$('#saveIndustry').attr("industryid",industryid);
			$('#saveIndustry').attr("industryname",industryname);
			
			showIndustryListInfo(industrylist,industryid);
			$('#resume_div').hide();
			$('#industryChange').show();
		});
		$('#closeIndustry').click(function(){
			$('#resume_div').show();
			$('#industryChange').hide();
		});
		//行业样式--选中
		$("#industry_ul").delegate("[name='zhiyespan']","click",function(){
			var obj = $(this).find('a[name="zhiye"]');
			var zhiye = obj.attr("class");
			var id = obj.attr("id");
			if(zhiye == "radio"){
				$("[name='zhiye']").attr("class","radio");
				obj.attr("class","radio_ed");
				
				$('#saveIndustry').attr("industryid",obj.attr("dataid"));
				$('#saveIndustry').attr("industryname",obj.attr("cname"));
			}else{
				obj.attr("class","radio");
				$('#saveIndustry').attr("industryid","");
				$('#saveIndustry').attr("industryname","无");
			}
			$('#saveIndustry').attr("typeid",id);
		});
		//行业选择--保存
		$('#saveIndustry').click(function(){
			var dataid = $(this).attr("industryid");
			var cname = $(this).attr("industryname");
			
			$('#industryid').val(dataid);
			$('#industryname').text(cname);
			
			$('#resume_div').show();
			$('#industryChange').hide();
		});
		
		
		//地址城市，页面div的切换
		$('#expectCity').click(function(){
			var cityid = $('#cityid').val();
			var cityname = $('#cityname').text();
			
			$('#saveArea').attr("cityid",cityid);
			$('#saveArea').attr("cityname",cityname);
			
			showCityListInfo(citylist,cityid);
			$('#resume_div').hide();
			$('#areaChange').show();
		});
		$('#closeArea').click(function(){
			$('#resume_div').show();
			$('#areaChange').hide();
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
				$('#saveArea').attr("cityname","无");
			}
			
		});
		//工作区域选择后的保存
		$('#saveArea').click(function(){
			var cityid = $(this).attr("cityid");
			var cityname = $(this).attr("cityname");
			
			$('#cityid').val(cityid);
			$('#cityname').text(cityname);
			
			$('#resume_div').show();
			$('#areaChange').hide();
		});
		
		
		//地址薪资，页面div的切换
		$('#expectSalary').click(function(){
			var salaryid = $('#salaryid').val();
			var salaryname = $('#salaryname').text();
			
			$('#saveSalary').attr("salaryid",salaryid);
			$('#saveSalary').attr("salaryname",salaryname);
			
			showSalaryListInfo(salarylist,salaryid);
			$('#resume_div').hide();
			$('#salaryChange').show();
		});
		$('#closeSalary').click(function(){
			$('#resume_div').show();
			$('#salaryChange').hide();
		});
		//薪资样式
		$("#salary_div").delegate("[name='xinzispan']","click",function(){
			var obj = $(this).find('a[name="xinzi"]');
			var xinzi = obj.attr("class");
			var id = obj.attr("id");
			if(xinzi == "radio"){
				$("[name='xinzi']").attr("class","radio");
				obj.attr("class","radio_ed");
				$('#saveSalary').attr("salaryid",obj.attr("dataid"));
				$('#saveSalary').attr("salaryname",obj.attr("cname"));
			}else{
				obj.attr("class","radio");
				$('#saveSalary').attr("salaryid","");
				$('#saveSalary').attr("salaryname","无");
			}
			$('#saveDistance').attr("typeid",id);
		});
		//薪资样式保存
		$('#saveSalary').click(function(){
			var dataid = $(this).attr("salaryid");
			var cname = $(this).attr("salaryname");
			
			$('#salaryid').val(dataid);
			$('#salaryname').text(cname);
			
			$('#resume_div').show();
			$('#salaryChange').hide();
		});
		
		$('#word_fixed').delegate("span[name='code_selected']","click",function(){
			$('span[name="code_selected"]').attr("class","");
			$(this).attr("class","active");
		});
		
	});
	
	//我的期望的完成
	function saveMyExpect(){
		$.ajax({
			url:"/interface/addResumeTwo",
			data:$('#expectForm').serialize(),
			type:"post",
			success:function(map){
				if(map.status == 0){
					location.href="/weixin/jumpPage/getMyResume?resumeid="+map.data.resumeid;
				}
			},
			error:function(){}
		});
	}
	
	function tiaoguo(){
		$.ajax({
			url:"/interface/addResumeTwo",
			data:"userid="+$('#userid').val()+"&resumeid="+$('#resumeid').val(),
			type:"post",
			success:function(map){
				if(map.status == 0){
					location.href="/weixin/jumpPage/getMyResume?resumeid="+map.data.resumeid;
				}
			},
			error:function(){}
		});
	}
	
	

	function showCityListInfo(citylist,cityid){
		//判断citylist是否为空
		if(citylist != null){
			$("#city_ui").empty();
			$('#word_fixed').empty();
			for(var i=0 ; i< citylist.length ; i++){
				//得到每条数据
				var map = citylist[i];
				
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
				if(map.areaid == cityid){
					$('#'+map.datacode+"_div").append('<span style="cursor: pointer;" name="areaspan"><a class="radio_ed" name="area_city" cityId="'+map.areaid+'" cityName="'+map.cname+'">选择</a><i>'+map.cname+'</i></span>');
				}else{
					$('#'+map.datacode+"_div").append('<span style="cursor: pointer;" name="areaspan"><a class="radio" name="area_city" cityId="'+map.areaid+'" cityName="'+map.cname+'">选择</a><i>'+map.cname+'</i></span>');
				}
			}
			$('span[name="code_selected"]:first').attr("class","active");
		}
	}
	
	function showIndustryListInfo(industrylist,industryid){
		//判断industrylist是否为空
		if(industrylist != null){
			$('#industry_ul').empty();	
			for(var i=0; i<industrylist.length; i++){
				
				//得到每条显示的信息
				var map = industrylist[i];
				
				if(map.parentid == null){
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
					if(map.dataid == industryid){
						$("#"+map.parentid+"_div").append('<span style="cursor: pointer;" name="zhiyespan"><a class="radio_ed" id="'+map.typeid+'" name="zhiye" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a><i>'+map.cname+'</i></span>');
					}else{
						$("#"+map.parentid+"_div").append('<span style="cursor: pointer;" name="zhiyespan"><a class="radio" id="'+map.typeid+'" name="zhiye" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a><i>'+map.cname+'</i></span>');
					}
				}
			}
		}
	}
	
	
	function showSalaryListInfo(salarylist,salaryid){
		//判断salarylist是否为空
		if(salarylist != null){
			$('#salary_div').empty();	
			for(var i=0; i<salarylist.length; i++){
					
				//得到每条显示的信息
				var map = salarylist[i];
				//添加数据
				if(map.dataid == salaryid){
					$('#salary_div').append('<span style="cursor: pointer;" name="xinzispan"><i>'+map.cname+'</i><a class="radio_ed" id="'+map.typeid+'" name="xinzi" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a></span>');
				}else{
					$('#salary_div').append('<span style="cursor: pointer;" name="xinzispan"><i>'+map.cname+'</i><a class="radio" id="'+map.typeid+'" name="xinzi" dataid="'+map.dataid+'" cname="'+map.cname+'">选择</a></span>');
				}
			}
			
		}
	}