
var companyid = "";
var userid = "";
var userInfo = "";
$(function(){
	//初始化信息
	JianKangCache.getGlobalData('userinfo',function(data){
		companyid = data.companyid;
		userid = data.userid;
		userInfo = data;
		
		//查询banner
		queryBanner();
		
		queryOANotReadNum();
	});
});

//查询banner信息
function queryBanner(){
	var banner = new Object();
	banner.model = 5;
	requestPost("/app/getBannerList",banner,"banner",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			var bannerhtml = '';
			var banner_ahtml = "";
			$.each(resultMap,function(i,map){
				var dis = "none";
				var active = "";
				if(i == 0){
					dis = "block";
					active = 'class="active"';
				}
				bannerhtml += ' <li style="display:'+dis+'"><img src="'+projectpath+"/"+map.imgurl+'"  alt="" width="100%"></li>';
				banner_ahtml += '<a href="javascript:void(0);" '+active+'>'+(i+1)+'</a>';
			});
			var windowheight=(parseInt($(window).height())*24)/100;
			$('#slider').css("max-height",windowheight+"px");
			$('#slider').html(bannerhtml);
			$('#pagenavi').html(banner_ahtml);
			var active=0,
			as=document.getElementById('pagenavi').getElementsByTagName('a');
			
			for(var i=0;i<as.length;i++){
				(function(){
					var j=i;
					as[i].onclick=function(){
						t2.slide(j);
						return false;
					}
				})();
			}

			var t1=new TouchScroll({id:'wrapper','width':5,'opacity':0.7,color:'#555',minLength:20});
			var t2=new TouchSlider({id:'slider', speed:600, timeout:6000, before:function(index){
					as[active].className='';
					active=index;
					as[active].className='active';
			}});
		}
	 	var pagediv=$('#pagenavi a');
		for(var i=0;i<pagediv.length;i++){
			if(pagediv[i].text==userInfo.bannerid){
				t2.slide(i);
				return false;
			}
		}
	});
}

//查询信息为读数量
function queryOANotReadNum(){
	var param = new Object();
	param.companyid = companyid;
	param.receiveid = userid;
	requestPost('/app/getOANoReadNum',param,"noread",true,function(resultMap){
		if(resultMap != undefined && resultMap != null){
			if(resultMap.status == 0 || resultMap.status == '0'){
				resultMap = resultMap.data;
				if(parseInt(resultMap.tongzhi) > 0){
					$('#tongzhi').show().text(resultMap.tongzhi);
				}
				var rizhi = (parseInt(resultMap.ribao) + parseInt(resultMap.zhoubao) + parseInt(resultMap.yuebao));
				if(rizhi > 0){
					$('#rizhi').show().text(rizhi);
				}
				if(parseInt(resultMap.renwu) > 0){
					$('#renwu').show().text(resultMap.renwu);
				}
				if(parseInt(resultMap.qingshi) > 0){
					$('#qingshi').show().text(resultMap.qingshi);
				}
				if(parseInt(resultMap.qingjia) > 0){
					$('#qingjia').show().text(resultMap.qingjia);
				}
				
				if(parseInt(resultMap.baoxiao) > 0){
					$('#baoxiao').show().text(resultMap.baoxiao);
				}
				if(parseInt(resultMap.shenpi) > 0){
					$('#shenpi').show().text(resultMap.shenpi);
				}
				if(parseInt(resultMap.jianbao) > 0){
					$('#jianbao').show().text(resultMap.jianbao);
				}
				if(parseInt(resultMap.beiyongjin) > 0){
					$('#beiyongjin').show().text(resultMap.beiyongjin);
				}
			}
		}
	});
}


//公共的查询方法
function requestPost(url,data,identifying,issynchroniz,callback){
	$.ajax({
		url:projectpath+url,
		data:data,
		async:issynchroniz,
		type:"post",
		beforeSend:function(){
			$('#jiazaizhong').show();
		},
		success:function(resultMap){
			//保存到当前路径缓存
			JianKangCache.setData("oa_",resultMap);
			callback(resultMap);
		},error:function(e){
			//取出缓存数据
			JianKangCache.getData("oa_",function(resultMap){
				callback(resultMap);
			});
		},complete:function(){
			$('#jiazaizhong').hide();
		}
	});
}