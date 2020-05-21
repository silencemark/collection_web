/**
 * 	显示数据
 */
function data_show(){
	//获取编号
	var userid=$("#userid").val();
	if(userid.length<=0){
		error_show();
		return;
	}
	$.ajax({
		url:'/interface/getcompanycenterdetailjson',
		type:'post',
		dataType:'json',
		data:'userid='+userid,
		success:function(data){
			if(data!=null && data.data!=null && data.data!=''){
				if(data.data.companyname!=null && data.data.companyname!=undefined && data.data.companyname!=''){
					$("#companyname_i").html(data.data.companyname);
					$("#companyname").val(data.data.companyname);
				}else{
					$("#companyname_i").html("&nbsp;");
					$("#companyname").val("");
				}
				if(data.data.contacts!=null && data.data.contacts!=undefined && data.data.contacts!=''){
					$("#contacts_i").html(data.data.contacts);
					$("#contacts").val(data.data.contacts);
				}else{
					$("#contacts_i").html("&nbsp;");
					$("#contacts").val("");
				}
				if(data.data.phonenum!=null && data.data.phonenum!=undefined && data.data.phonenum!=''){
					$("#phonenum_i").html(data.data.phonenum);
					$("#phonenum").val(data.data.phonenum);
				}else{
					$("#phonenum_i").html("&nbsp;");
					$("#phonenum").val("");
				}
				if(data.data.companyemail!=null && data.data.companyemail!=undefined && data.data.companyemail!=''){
					$("#companyemail_i").html(data.data.companyemail);
					$("#companyemail").val(data.data.companyemail);
				}else{
					$("#companyemail_i").html("&nbsp;");
					$("#companyemail").val("");
				}
				if(data.data.scaleid!=null && data.data.scaleid!=undefined && data.data.scaleid!=''){
					$("#scaleid_i").html(data.data.scaleidname);
					$("#scaleid").val(data.data.scaleid);
				}else{
					$("#scaleid_i").html("&nbsp;");
					$("#scaleid").val("");
				}
				if(data.data.natureid!=null && data.data.natureid!=undefined && data.data.natureid!=''){
					$("#natureid_i").html(data.data.natureidname);
					$("#natureid").val(data.data.natureid);
				}else{
					$("#natureid_i").html("&nbsp;");
					$("#natureid").val("");
				}
				if(data.data.industryid!=null && data.data.industryid!=undefined && data.data.industryid!=''){
					$("#industryid_i").html(data.data.industryidname);
					$("#industryid").val(data.data.industryid);
				}else{
					$("#industryid_i").html("&nbsp;");
					$("#industryid").val("");
				}
				if(data.data.cityid!=null && data.data.cityid!=undefined && data.data.cityid!=''){
					$("#cityid_i").html(data.data.cityidname);
					$("#cityid").val(data.data.cityid);
				}else{
					$("#cityid_i").html("&nbsp;");
					$("#cityid").val("");
				}
				if(data.data.address!=null && data.data.address!=undefined && data.data.address!=''){
					$("#address_i").html(data.data.address);
					$("#address").val(data.data.address);
				}else{
					$("#address_i").html("&nbsp;");
					$("#address").val("");
				}
				if(data.data.description!=null && data.data.description!=undefined && data.data.description!=''){
					$("#description_i").html(data.data.description);
					$("#description").val(data.data.description);
				}else{
					$("#description_i").html("&nbsp;");
					$("#description").val("");
				}
				if(data.data.companywebsite!=null && data.data.companywebsite!=undefined && data.data.companywebsite!=''){
					$("#companywebsite_i").html(data.data.companywebsite);
					$("#companywebsite").val(data.data.companywebsite);
				}else{
					$("#companywebsite_i").html("&nbsp;");
					$("#companywebsite").val("");
				}
			}else{
				error_show();
			}
		},
		error:function(){
			error_show();
		}
	});
}

/**
 * 	错误时显示的数据
 */
function error_show(){
	$("i").html("&nbsp;");
	$("#companydetail_from input[type='hidden']").val("");
}

/**
 * 	点击可进行文本编辑的字段
 */
function text_click(object){
	var id=$(object).attr("name");
	var name=$("#"+id).val();
	//赋值
	$("#save_text").attr("name",id);
	$("#text_content").val(name);
	//显示交替
	$("#body_content").hide();
	$("#body_text").show();
}

/**
 * 	点击文本框的保存
 */
function text_save(){
	var id=$("#save_text").attr("name");
	var name=$("#text_content").val();
	
	//验证手机号
	if(id=="phonenum"){
		if(!hhutil.checkMobile(name)){
			swal("","请输入正确格式的联系方式");
			return;
		}
	}
	
	//验证邮箱
	if(id=="companyemail"){
		if(!hhutil.checkEmail(name)){
			swal("","请输入正确格式邮箱");
			return;
		}
	}
	if(name.trim().length<=0){
		name="&nbsp;"
	}
	$("#"+id).val(name);
	$("#"+id+"_i").html(name);
	text_reset();
}

/**
 * 	文本框的取消
 */
function text_reset(){
	$("#save_text").attr("name","");
	$("#text_content").val("");
	//显示交替
	$("#body_text").hide();
	$("#body_content").show();
}

/**
 * 	点击企业规模丶企业性质丶企业行业
 */
function type_click(object){
	var id=$(object).attr("name");
	var name=$(object).attr("n");
	var typeid=$(object).attr("typeid");
	var id_value=$("#"+id).val();
	if(typeid.length<=0){
		return;
	}
	//ajax请求数据
	$.ajax({
		url:'/interface/querydictbytypeid',
		type:'post',
		dataType:'json',
		data:'typeid='+typeid,
		success:function(data){
			$("#body_content").hide();
			$("#saveIndustry").attr("name",id);
			$("#industry_ul").html("");
			$("#industryChange").show();
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
									var cla="radio";
									if(data.data[i].dataid==id_value){
										cla="radio_ed";
									}
									str+="<span class='sp_radio' onclick='click_sp_radio(this)'><a class='"+cla+"' dataid='"+data.data[i].dataid+"' cname='"+data.data[i].cname+"'>选择</a><i>"+data.data[i].cname+"</i></span>";
									bool=true;
								}
							}
							if(!bool){
								var cla="radio";
								if(data.data[j].dataid==id_value){
									cla="radio_ed";
								}
								str+="<span class='sp_radio' onclick='click_sp_radio(this)'><a class='"+cla+"' dataid='"+data.data[j].dataid+"' cname='"+data.data[j].cname+"'>选择</a><i>"+data.data[j].cname+"</i></span>";
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
						var cla="radio";
						if(data.data[i].dataid==id_value){
							cla="radio_ed";
						}
						str+="<span class='sp_radio' onclick='click_sp_radio(this)'><a class='"+cla+"' dataid='"+data.data[i].dataid+"' cname='"+data.data[i].cname+"'>选择</a><i>"+data.data[i].cname+"</i></span>";
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
 * 	类型选择取消
 */
function closeIndustry(){
	$("#saveIndustry").attr("name","");
	$("#industry_ul").html("");
	$('#industryChange').hide();
	$('#body_content').show();
}

/**
 * 	点击类型保存
 */
function saveIndustry_click(object){
	var id="";
	var name="&nbsp;";
	//获取预存的id
	var id_=$(object).attr("name");
	$.each($("#industry_ul .sp_radio"),function(i,n){
		if($(n).children("a").attr("class")=="radio_ed"){
			id=$(n).children("a").attr("dataid")!=null && $(n).children("a").attr("dataid")!=undefined?$(n).children("a").attr("dataid"):'';
			name=$(n).children("a").attr("cname")!=null && $(n).children("a").attr("cname")!=undefined && $(n).children("a").attr("cname")!=''?$(n).children("a").attr("cname"):'&nbsp;';
		}
	});
	$("#"+id_).val(id);
	$("#"+id_+"_i").html(name);
	$("#saveIndustry").attr("name","");
	$("#industry_ul").html("");
	$("#industryChange").hide();
	$("#body_content").show();
}

$(function(){
	$('#word_fixed').delegate("span[name='code_selected']","click",function(){
		$('span[name="code_selected"]').attr("class","");
		$(this).attr("class","active");
	});
});

/**
 * 	查询省份信息
 * @param areaid
 */
function getWorkArea(object){
	
	var id=$("#"+$(object).attr("name")).val();
	
	$.ajax({
		url:"/interface/getWorkAreaInfo",
		data:"",
		type:"post",
		success:function(resultMap){
			$("#saveArea").attr("name",$(object).attr("name"));
			$('#body_content').hide();
			$("#city_ui").html("");
			$("#word_fixed").html("");
			$('#areaChange').show();
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
					var cla="radio";
					if(map.areaid==id){
						cla="radio_ed";
					}
					$('#'+map.datacode+"_div").append('<span class="sp_radio" onclick="click_sp_radio(this)"><a class="'+cla+'" name="area_city" cityid="'+map.areaid+'" cityidname="'+map.cname+'">选择</a><i>'+map.cname+'</i></span>');
				}
				$('span[name="code_selected"]:first').attr("class","active");
			}
		},
		error:function(){
			
		}
	});
}

/**
 * 	点击右侧字母定位
 */
function abc_click(object){
	$('span[name="code_selected"]').attr("class","");
	$(object).attr("class","active");
}

/**
 * 	关闭地区查询
 */
function closeArea(){
	$('#areaChange').hide();
	$('#body_content').show();
}

/**
 * 	点击选择地区的保存
 * @param object
 */
function saveArea_click(object){
	var id="";
	var name="&nbsp;";
	//获取预存的id
	var id_=$(object).attr("name");
	$.each($("#city_ui .sp_radio"),function(i,n){
		if($(n).children("a").attr("class")=="radio_ed"){
			id=$(n).children("a").attr("cityid")!=null && $(n).children("a").attr("cityid")!=undefined?$(n).children("a").attr("cityid"):'';
			name=$(n).children("a").attr("cityidname")!=null && $(n).children("a").attr("cityidname")!=undefined && $(n).children("a").attr("cityidname")!=''?$(n).children("a").attr("cityidname"):'&nbsp;';
		}
	});
	$("#"+id_).val(id);
	$("#"+id_+"_i").html(name);
	$(object).attr("name","");
	$("#city_ui").html("");
	$("#areaChange").hide();
	$("#body_content").show();
}

/**
 * 	点击修改资料保存
 */
function sub_click(){
	
	var companyname=$("#companyname").val();
	if(companyname!=null && companyname.trim().length<=0){
		swal("","企业名称不能为空");
		return;
	}
	var contacts=$("#contacts").val();
	if(contacts!=null && contacts.trim().length<=0){
		swal("","联系人不能为空");
		return;
	}
	var phonenum=$("#phonenum").val();
	if(phonenum!=null && phonenum.trim().length<=0){
		swal("","联系方式不能为空");
		return;
	}
	var companyemail=$("#companyemail").val();
	if(companyemail!=null && companyemail.trim().length<=0){
		swal("","企业邮箱不能为空");
		return;
	}
	var scaleid=$("#scaleid").val();
	if(scaleid!=null && scaleid.trim().length<=0){
		swal("","企业规模不能为空");
		return;
	}
	var natureid=$("#natureid").val();
	if(natureid!=null && natureid.trim().length<=0){
		swal("","企业性质不能为空");
		return;
	}
	var industryid=$("#industryid").val();
	if(industryid!=null && industryid.trim().length<=0){
		swal("","企业行业不能为空");
		return;
	}
	var cityid=$("#cityid").val();
	if(cityid!=null && cityid.trim().length<=0){
		swal("","城市不能为空");
		return;
	}
	var address=$("#address").val();
	if(address!=null && address.trim().length<=0){
		swal("","企业地址不能为空");
		return;
	}
	var description=$("#description").val();
	if(description!=null && description.trim().length<=0){
		swal("","企业介绍不能为空");
		return;
	}
	
	//ajax修改信息
	$.ajax({
		url:'/interface/updatecompanydetail',
		type:'post',
		dataType:'json',
		data:$("#companydetail_from").serialize(),
		success:function(data){
			if(data.status==0){
				swal({
					title: "",
					text: "保存成功！",
					showCancelButton: false,
					confirmButtonColor : "red",
					confirmButtonText : "确认",
				},function(){
					location.href="/weixin/companycenter/center";
				});
				
			}else{
				swal("","修改失败");
			}
		},
		error:function(){
			swal("","请求错误,请检查网络或其他原因");
		}
	});
	//$("#companydetail_from").submit();
}
