function index_show_data(){
	var cityid=$('#cityid').val();
	$.ajax({
		url:'/interface/getHomeBanner',
		type:'post',
		dataType:'json',
		data:'lat='+$("#latitude").val()+'&lng='+$("#longitude").val()+'&userid='+$('#userid').val()+'&cityid='+cityid,
		success:function(data){
			
			$("body").show();
			//显示是否存在新消息
			if(data!=null && data.data.newmsg!=null && data.data.newmsg==1){
				$(".index_msg").append("<b class='point'>点</b>");
			}
			var banner = data.data.banner;
			//显示banner
			if(data!=null && data.data!=null && banner!=null && banner.length>0){
				var leng=banner.length;
				var bannerstr="";
				var bannernum="";
				for(var i=0;i<leng;i++){
					bannerstr+="<li style='display:block'><a href='"+banner[i].linkurl+"'><img src='"+banner[i].imgurl+"'  alt='' width='100%'></a>";
					if(i==0){
						bannernum+="<a href='javascript:void(0);' class='active'>1</a>";
					}else{
						bannernum+="<a href='javascript:void(0);'>"+(i+1)+"</a>";
					}
				}
				$("#slider").html(bannerstr);
				$("#pagenavi").html(bannernum);
				$("body").append("<script type='text/javascript' src='/appcssjs/script/touchScroll.js'></script>");
				$("body").append("<script type='text/javascript' src='/appcssjs/script/touchslider.dev.js'></script>");
				$("body").append("<script type='text/javascript' src='/appcssjs/script/run.js'></script>");
			}
			$.ajax({
				url:'/interface/getHomeHotCompany',
				type:'post',
				dataType:'json',
				data:'lat='+$("#latitude").val()+'&lng='+$("#longitude").val()+'&cityid='+cityid,
				success:function(data){
					//显示热门企业
					if(data!=null && data.data!=null &&  data.data.hotcompany!=null && data.data.hotcompany.length>0){
						var leng=data.data.hotcompany.length;
						var str="";
						for(var i=0;i<leng;i++){
							var path="/weixin/msg/tocompanydetailjsp?companyid="+data.data.hotcompany[i].companyid;
							str+="<li onclick='location.href=\""+path+"\"'>";
							str+="<div class='img_logo'><span><img src='/appcssjs/images/index/tip_hot.png' alt='hot'></span><b><img src='"+data.data.hotcompany[i].iconurl+"' width='74' height='74' class='hot_company_img'></b></div>";
							str+="<div class='xx_01 word_hidden'>"+data.data.hotcompany[i].companyname+"</div>";
							str+="<div class='xx_02'><span>"+data.data.hotcompany[i].industry+"</span></div>";
							str+="<div class='xx_03'><span class='address'>"+data.data.hotcompany[i].address+"</span><span class='tnum'>"+data.data.hotcompany[i].companysize+"</span></div>";
							str+="</li>";
						}
						if(str.length<=0){
							$("#hot_enterprise_ul").html("<li>暂无数据</li>");
						}else{
							$("#hot_enterprise_ul").html(str);
							$(".hot_company_img").attr("onerror","$(this).attr('src','/appcssjs/images/index/logo_img03.jpg')");
						}
					}else{
						$("#hot_enterprise_ul").html("<li>暂无数据</li>");
					}
					$('.index_list').show();
					$.ajax({
						url:'/interface/getHomeNearCompany',
						type:'post',
						dataType:'json',
						data:'lat='+$("#latitude").val()+'&lng='+$("#longitude").val()+'&cityid='+cityid,
						success:function(data){
							//加载附近企业
							if(data!=null && data.data!=null && data.data.nearbybusiness!=null && data.data.nearbybusiness.length>0){
								var leng=data.data.nearbybusiness.length;
								var str="";
								for(var i=0;i<leng;i++){
									var path="/weixin/msg/tocompanydetailjsp?companyid="+data.data.nearbybusiness[i].companyid;
									str+="<li onclick='location.href=\""+path+"\"'>";
						            str+="<div class='img_logo'><b><img src='"+data.data.nearbybusiness[i].iconurl+"' width='74' height='74' class='near_company_img'></b></div>";
						            str+="<div class='xx_01 word_hidden'>"+data.data.nearbybusiness[i].companyname+"</div>";
						            str+="<div class='xx_02'><span>"+data.data.nearbybusiness[i].industry+"</span><i>"+(data.data.nearbybusiness[i].juli!=null && data.data.nearbybusiness[i].juli!='undefined'?(data.data.nearbybusiness[i].juli<1000?(data.data.nearbybusiness[i].juli+' m'):((parseFloat(data.data.nearbybusiness[i].juli/1000).toFixed(1))+' km')):'')+"</i></div>";
						            str+="<div class='xx_03'><span class='address'>"+data.data.nearbybusiness[i].address+"</span><span class='tnum'>"+data.data.nearbybusiness[i].companysize+"</span></div>";
						            str+="</li>";
								}
								if(str.length<=0){
									$("#near_enterprise_ul").html("<li>暂无数据</li>");
								}else{
									$("#near_enterprise_ul").html(str);
									$(".near_company_img").attr("onerror","$(this).attr('src','/appcssjs/images/index/logo_img03.jpg')");
								}
							}else{
								$("#near_enterprise_ul").html("<li>暂无数据</li>");
							}
							$.ajax({
								url:'/interface/getHomeHotPosition',
								type:'post',
								dataType:'json',
								data:'lat='+$("#latitude").val()+'&lng='+$("#longitude").val()+'&cityid='+cityid,
								success:function(data){
									//显示热门职位
									if(data!=null && data.data!=null && data.data.hotposition!=null && data.data.hotposition.length>0){
										var leng=data.data.hotposition.length;
										var str="";
										for(var i=0;i<leng;i++){
											var path="/weixin/jumpPage/getJobDetail?positionId="+data.data.hotposition[i].positionid;
											str+="<li onclick='location.href=\""+path+"\"'>";
											str+="<div class='img_logo'><b><img src="+data.data.hotposition[i].iconurl+" width='74' height='74' class='hot_job_img'></b></div>";
											str+="<div class='xx_01'><span class='word_hidden'>"+data.data.hotposition[i].positionname+"</span><i>"+data.data.hotposition[i].createtime+"</i></div>";
											str+="<div class='xx_02'><span>"+data.data.hotposition[i].companyname+"</span><i>"+data.data.hotposition[i].salary+"</i></div>";
											str+="<div class='xx_03'><span class='address'>"+data.data.hotposition[i].address+"</span><span class='pin'>招聘人数 "+data.data.hotposition[i].recruitnum+"</span></div>";
											str+="</li>";
										}
										if(str.length<=0){
											$("#hot_job_ul").html("<li>暂无数据</li>");
										}else{
											$("#hot_job_ul").html(str);
											$(".hot_job_img").attr("onerror","$(this).attr('src','/appcssjs/images/index/logo_img05.jpg')");
										}
									}else{
										$("#hot_job_ul").html("<li>暂无数据</li>");
									}
								}
							})
							
						}
					})
				}
			})
		},
		error:function(){
			//显示暂无数据
			$("#hot_enterprise_ul").html("<li>暂无数据</li>");
			$("#near_enterprise_ul").html("<li>暂无数据</li>");
			$("#hot_job_ul").html("<li>暂无数据</li>");
		}
	});
	
	$('#word_fixed').delegate("span[name='code_selected']","click",function(){
		$('span[name="code_selected"]').attr("class","");
		$(this).attr("class","active");
	});
	
}


/**
 * 	查询省份信息
 * @param areaid
 */
function getWorkArea(){
	var id=$("#cityid").val();
	$.ajax({
		url:"/interface/getWorkAreaInfo",
		data:"",
		type:"post",
		success:function(resultMap){
			$('#body_content').hide();
			$("#city_ui").html("");
			$("#word_fixed").html("");
			$('#areaChange').show();
			//得到省份信息
			var list = resultMap.data;
			
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
	var name="";
	//获取预存的id
	var id_=$(object).attr("name");
	$.each($("#city_ui .sp_radio"),function(i,n){
		if($(n).children("a").attr("class")=="radio_ed"){
			id=$(n).children("a").attr("cityid")!=null && $(n).children("a").attr("cityid")!=undefined?$(n).children("a").attr("cityid"):'';
			name=$(n).children("a").attr("cityidname")!=null && $(n).children("a").attr("cityidname")!=undefined && $(n).children("a").attr("cityidname")!=''?$(n).children("a").attr("cityidname"):'';
		}
	});
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/interface/savecity?areaid="+id+"&cname="+name+"&userid="+$('#userid').val(),
		success:function(data){
			$('#cityid').val(id);
			$('#cityidname').val(name);
			$("#address_index").text(name);
			index_show_data();
			closeArea();
		}
	})
}

/**
 * 点击热门企业更多
 */
function more_hotcompany(path){
	$("#more_form").attr("action",path);
	$("#more_form").submit();
}