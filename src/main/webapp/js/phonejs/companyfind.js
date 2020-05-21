
/**
 * 	页面数据显示
 */
function data_show(){
	
	var realname=$("#search_div input").val();
	if(realname!=null && realname.length>0){
		queryInputKeyup("#search_div input");
	}
	
	$.ajax({
		url:"/interface/showcompanytype",
		type:"post",
		success:function(resultMap){
			//获取筛选列表信息
			var list = resultMap.data;
			
			//判断筛选列表信息是否为空
			if(list != null && list !=undefined && list != ''){
				
				//循环显示信息
				for(var i=0; i<list.length; i++){
					
					//得到每条数据
					var map = list[i];
					
					var id=$("#industryType input[han='"+map.ename+"']").val();
					var name=$("#industryType input[han='"+map.ename+"name']").val();
					if(map.datatypeid !=null && map.datatypeid=='7' || map.datatypeid==7){
						var lat=$("#latitude").val();
						var lng=$("#longitude").val();
						if(lat.trim().length<=0 || lng.trim().trim().length<=0){
							continue;
						}
					}
					//<input type="hidden" name="'+map.ename+'name" id="'+map.datatypeid+'id2" value="'+name+'">
					$('#industryType').append('<li class="dict_type" onclick="click_type(\''+map.datatypeid+'\',\''+map.name+'\')" name="'+map.name+'" datatypeid="'+map.datatypeid+'"><span>'+map.name+'</span><i><span id="'+map.datatypeid+'name">'+name+'</span>&nbsp;</i></li><input type="hidden" dataid="'+id+'" name="'+map.ename+'" id="'+map.datatypeid+'id" value="'+id+'"><input type="hidden" id="'+map.datatypeid+'_name" name="'+map.ename+'name" value="'+name+'" />');
				}
			}
			$("#search_div").children("input").focus();
		},
		error:function(){}
	});
}


/**
 * 	点击类型
 */
function click_type(id,typename){
	var typeid=id;
	var name=typename;
	if(typeid.trim().length<=0){
		return;
	}
	
	//ajax请求数据
	$.ajax({
		url:'/interface/querydictbytypeid',
		type:'post',
		dataType:'json',
		data:'typeid='+typeid,
		success:function(data){
			$("#findType").hide();
			$("#saveIndustry").attr("datatypeid",typeid);
			$("#industry_ul").html("");
			$("#industryChange").show();
			var changeid= $('#'+typeid+'id').val();
			if(typeid==10 || typeid=='10'){
				var str="";
				
				if(data!=null && data.data!=null && data.data!='' && data.data.length>0){
					var len=data.data.length;
					for(var j=0;j<len;j++){
						if(data.data[j].parentid==null || data.data[j].parentid==undefined || data.data[j].parentid.length<=0){
							str+="<li><div class='name'>"+data.data[j].cname+"</div>";
							var bool=false;
							var leng=data.data.length;
							str+="<div class='span_box'>";
							for(var i=0;i<leng;i++){
								if(data.data[j].dataid==data.data[i].parentid){
									if(changeid == data.data[i].dataid){
										str+="<span class='sp_radio' onclick='click_sp_radio(this)'><a class='radio_ed' dataid='"+data.data[i].dataid+"' cname='"+data.data[i].cname+"'>选择</a><i>"+data.data[i].cname+"</i></span>";
									}else{
										str+="<span class='sp_radio' onclick='click_sp_radio(this)'><a class='radio' dataid='"+data.data[i].dataid+"' cname='"+data.data[i].cname+"'>选择</a><i>"+data.data[i].cname+"</i></span>";
									}
									bool=true;
								}
							}
							if(!bool){
								str+="<span class='sp_radio' onclick='click_sp_radio(this)'><a class='radio' dataid='"+data.data[j].dataid+"' cname='"+data.data[j].cname+"'>选择</a><i>"+data.data[j].cname+"</i></span>";
							}
							str+="</div>";
							
							str+="</li>";
						}
					}
				}else{
					str+="<li><div class='name'>"+name+"</div>";
					str+="<div class='span_box'>暂无数据</div>";
					str+="</li>";
				}
				$("#industry_ul").html(str);
			}else{
				var str="";
				str+="<li><div class='name'>"+name+"</div>";
				if(data!=null && data.data!=null && data.data!='' && data.data.length>0){
					var leng=data.data.length;
					str+="<div class='span_box'>";
					for(var i=0;i<leng;i++){
						if(changeid == data.data[i].dataid){
							str+="<span class='sp_radio' onclick='click_sp_radio(this)'><a class='radio_ed' dataid='"+data.data[i].dataid+"' cname='"+data.data[i].cname+"'>选择</a><i>"+data.data[i].cname+"</i></span>";
						}else{
							str+="<span class='sp_radio' onclick='click_sp_radio(this)'><a class='radio' dataid='"+data.data[i].dataid+"' cname='"+data.data[i].cname+"'>选择</a><i>"+data.data[i].cname+"</i></span>";
						}
					}
					str+="</div>";
				}else{
					str+="<div class='span_box'>暂无数据</div>";
				}
				str+="</li>";
				$("#industry_ul").html(str);
			}
		},
		error:function(){
			//弹窗显示
			swal("","请求错误");
		}
	});
}

/**
 * 	点击单选按钮
 * @param id
 */
function click_sp_radio(id){
	if($(id).children("a").attr("class")=="radio"){
		$(".sp_radio").children("a").attr("class","radio");
		$(id).children("a").attr("class","radio_ed");
	}else{
		$(".sp_radio").children("a").attr("class","radio");
	}
}

/**
 * 	工作选择的切换
 */
function area_click(){
	var areaid = $('#areaid').val();
	var areaname = $('#areaid2').val();
	$('#saveArea').attr("cityid",areaid);
	$('#saveArea').attr("cityname",areaname);
	getWorkArea(areaid);
	$('#findType').hide();
	$('#areaChange').show();
}

/**
 * 	工作区域选择后的保存
 */
function saveArea_click(){
	var cityid = $("#saveArea").attr("cityid");
	var cityname = $("#saveArea").attr("cityname");
	
	if(cityname == ""){
		$('#areaname').text("不限");
	}else{
		$('#areaname').text(cityname);
	}
	if(cityid != null && cityid != ""){
		$('#areaid').val(cityid);
	}else{
		$('#areaid').val("");
	}
	$('#areaid2').val(cityname);
	closeArea();
}

/**
 * 	点击保存
 */
function saveIndustry_click(){
	var id="";
	var name="";
	var datatypeid=$("#saveIndustry").attr("datatypeid");
	$.each($(".sp_radio"),function(i,n){
		if($(n).children("a").attr("class")=="radio_ed"){
			id=$(n).children("a").attr("dataid")!=null && $(n).children("a").attr("dataid")!=undefined?$(n).children("a").attr("dataid"):'';
			name=$(n).children("a").attr("cname")!=null && $(n).children("a").attr("cname")!=undefined?$(n).children("a").attr("cname"):'';;
		}
	});
	
	$("#industryChange").hide();
	$("#findType").show();
	$("#saveIndustry").attr("datatypeid","");
	$("#industry_ul").html("");
	$("#"+datatypeid+"id").val(id);
	$("#"+datatypeid+"name").text(name);
	$("#"+datatypeid+"_name").val(name);
}

$(function(){	
	
	//点击锚点
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

//查询省份信息
function getWorkArea(areaid){
	$.ajax({
		url:"/interface/getWorkAreaInfo",
		data:"",
		type:"post",
		success:function(resultMap){

			$("#city_ui").html("");
			$("#word_fixed").html("");
			//得到省份信息
			var list = resultMap.data;
			
			//判断list是否为空
			if(list != null){
				$("#city_ui").empty();
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
						$('#'+map.datacode+"_div").append('<span onclick="click_address(this)"><a class="radio_ed" name="area_city" cityId="'+map.areaid+'" cityName="'+map.cname+'">选择</a><i>'+map.cname+'</i></span>');
					}else{
						$('#'+map.datacode+"_div").append('<span onclick="click_address(this)"><a class="radio" name="area_city" cityId="'+map.areaid+'" cityName="'+map.cname+'">选择</a><i>'+map.cname+'</i></span>');
					}
					
				}
				$('span[name="code_selected"]:first').attr("class","active");
			}
		},
		error:function(){}
	});
}

/**
 * 	点击地区选中
 * @param id
 */
function click_address(id){
	var change = $(id).find('a[name="area_city"]').attr("class");
	if(change == "radio"){
		$('[name="area_city"]').attr("class","radio");
		$(id).find('a[name="area_city"]').attr("class","radio_ed");
		$('#saveArea').attr("cityid",$(id).find('a[name="area_city"]').attr("cityId"));
		$('#saveArea').attr("cityname",$(id).find('a[name="area_city"]').attr("cityName"));
	}else{
		$(id).find('a[name="area_city"]').attr("class","radio");
		$('#saveArea').attr("cityid","");
		$('#saveArea').attr("cityname","");
	}
}


/**
 * 	输入查询框
 */
function queryInputKeyup(id){
	
	var txt=$(id).val();
	if(txt.length<=0){
		$("#cancelInfo").attr("onclick","javascript:history.go(-1)");
		$("#cancelInfo").children("span").text("取消");
		$("#cancelInfo").attr("type","show");
		$(".find_page").show();
	}else{
		$("#cancelInfo").attr("onclick","$('#find_form').submit()");
		$("#cancelInfo").children("span").text("搜索");
		$("#cancelInfo").attr("type","hide");
		$(".find_page").hide();
	}
	
}

/**
 * 	表单提交之前
 */
function form_submit(){
	if($("#cancelInfo").attr("type")=="hide"){
		$(".find_page").remove();
	}
	return true;
}