/**
 * 	显示数据
 */
function data_show(){
	
	//获取用户编号
	var userid=$("#userid").val();
	if(userid.length<=0){
		error_show();
		return;
	}
	//ajax请求数据
	$.ajax({
		url:'/interface/querycompanylot',
		type:'post',
		dataType:'json',
		data:'userid='+userid,
		success:function(data){
			if(data!=null && data.data!=null && data.data!=''){
				if(data.data.newmsg==1){
					$("#newmsg").append("<b class='point'>点</b>");
				}
				$("#center_img").attr("src",data.data.iconurl);
				$("#companyname_span").text(data.data.companyname!=null && data.data.companyname!=undefined && data.data.companyname!=''?data.data.companyname:'暂无');
				$("#center_img").attr("onerror","$(this).attr('src','/appcssjs/images/index/logo_img03.jpg')");
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
	$("#center_img").attr("src","/appcssjs/images/index/logo_img03.jpg");
	$("#companyname_span").text("暂无");
}

/**
 * 	点击企业资料
 */
function click_detail(){
	//获取用用户编号
    //var userid=$("#userid").val();
	location.href="/weixin/companycenter/mydetail";
}

function click_aboutWe(){
	location.href="/weixin/jumpPage/intoAboutWeInfo";
}

function click_feedBack(){
	location.href="/weixin/jumpPage/intoCompanyAdviceInfo";
}

function click_select(){
	location.href="/weixin/jumpPage/selectPage";
}

function headimg_click(){
	
	location.href="/weixin/my/myinfo?space=company&companyimg="+$("#center_img").attr("src");
}