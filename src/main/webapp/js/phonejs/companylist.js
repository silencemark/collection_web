/**
 * 	数据显示
 */
function data_show(isfirst){
	$(".load_more").show();
	//ajax请求数据
	$.ajax({
		url:'/interface/querypagecompany',
		type:'post',
		dataType:'json',
		data:$("#myform").serialize(),
		success:function(data){
			$(".load_more").hide();
			if(data!=null && data.data.data!=null && data.data.data!='' && data.data.data.length>0){
				if(data.data.newmsg!=null && data.data.newmsg==1){
					$(".msg").html("<img src='/appcssjs/images/public/t_msg.png'><b class='point'>点</b>");
				}
				
				var leng=data.data.data.length;
				var str="";
				for(var i=0;i<leng;i++){
					var path="/weixin/msg/tocompanydetailjsp?companyid="+data.data.data[i].companyid;
					str+="<li onclick='location.href=\""+path+"\"'>";
		            var img="";
		            if(data.data.data[i].ishot==1){
		            	img="<span><img src='/appcssjs/images/index/tip_hot.png' alt='hot'></span>";
		            }
		            str+="<div class='img_logo'>"+img+"<b><img src='"+data.data.data[i].iconurl+"' width='74' height='74' class='company_list_img'></b></div>";
		            str+="<div class='xx_01 word_hidden ico_v'>"+(data.data.data[i].companyname!=null && data.data.data[i].companyname!=undefined && data.data.data[i].companyname!=''?data.data.data[i].companyname:'')+"</div>";
		            str+="<div class='xx_02'><span>"+(data.data.data[i].industry!=null && data.data.data[i].industry!=undefined && data.data.data[i].industry!=''?data.data.data[i].industry:'')+"</span><i>"+(data.data.data[i].juli!=null && data.data.data[i].juli!='undefined'?(parseFloat(data.data.data[i].juli)<1000?(data.data.data[i].juli+' m'):((parseFloat(data.data.data[i].juli/1000).toFixed(1))+' km')):'')+"</i></div>";
		            str+="<div class='xx_03'><span class='address'>"+(data.data.data[i].address!=null && data.data.data[i].address!=undefined && data.data.data[i].address!=''?data.data.data[i].address:'')+"</span><span class='tnum'>"+(data.data.data[i].companysize!=null && data.data.data[i].companysize!=undefined && data.data.data[i].companysize!=''?data.data.data[i].companysize:'')+"</span></div>";
		            str+="</li>";
				}
				if(str.length>0){
					$("#company_list_ul").append(str);
					$(".company_list_img").attr("onerror","$(this).attr('src','/appcssjs/images/index/logo_img03.jpg')");
				}else{
					error_show(isfirst);	
				}
			}else{
				error_show(isfirst);
			}
		},
		error:function(){
			error_show(isfirst);
		}
	});
}

$(function(){
	var data1 ={
			userid:$("#userid").val(),
			ishot:$("#ishot").val(),
			isnear:$("#isnear").val(),
			natureid:$("#natureid").val(),
			scaleid:$("#scaleid").val(),
			industryid:$("#industryid").val(),
			distance:$("#distance").val(),
			realname:$("#realname").val(),
			cityid:$("#cityid").val(),
			lat:$("#lat").val(),
			lng:$("#lng").val()
	};
	//滑到底部自动分页
	PageHelper({
		url:"/interface/querypagecompany",
		data:data1,
		success:function(data){
			if(data!=null && data.data.data!=null && data.data.data!='' && data.data.data.length>0){
				if(data.data.newmsg!=null && data.data.newmsg==1){
					$(".msg").html("<img src='/appcssjs/images/public/t_msg.png'><b class='point'>点</b>");
				}
				
				var leng=data.data.data.length;
				var str="";
				for(var i=0;i<leng;i++){
					var path="/weixin/msg/tocompanydetailjsp?companyid="+data.data.data[i].companyid;
					str+="<li onclick='location.href=\""+path+"\"'>";
		            var img="";
		            if(data.data.data[i].ishot==1){
		            	img="<span><img src='/appcssjs/images/index/tip_hot.png' alt='hot'></span>";
		            }
		            str+="<div class='img_logo'>"+img+"<b><img src='"+data.data.data[i].iconurl+"' width='74' height='74' class='company_list_img'></b></div>";
		            str+="<div class='xx_01 word_hidden ico_v'>"+(data.data.data[i].companyname!=null && data.data.data[i].companyname!=undefined && data.data.data[i].companyname!=''?data.data.data[i].companyname:'')+"</div>";
		            str+="<div class='xx_02'><span>"+(data.data.data[i].industry!=null && data.data.data[i].industry!=undefined && data.data.data[i].industry!=''?data.data.data[i].industry:'')+"</span><i>"+(data.data.data[i].juli!=null && data.data.data[i].juli!='undefined'?(parseFloat(data.data.data[i].juli)<1000?(data.data.data[i].juli+' m'):((parseFloat(data.data.data[i].juli/1000).toFixed(1))+' km')):'')+"</i></div>";
		            str+="<div class='xx_03'><span class='address'>"+(data.data.data[i].address!=null && data.data.data[i].address!=undefined && data.data.data[i].address!=''?data.data.data[i].address:'')+"</span><span class='tnum'>"+(data.data.data[i].companysize!=null && data.data.data[i].companysize!=undefined && data.data.data[i].companysize!=''?data.data.data[i].companysize:'')+"</span></div>";
		            str+="</li>";
				}
				if(str.length>0){
					$("#company_list_ul").append(str);
					$(".company_list_img").attr("onerror","$(this).attr('src','/appcssjs/images/index/logo_img03.jpg')");
				}
			}
		}
	});
});

/**
 * 	错误时显示的数据
 * @param isfirst
 */
function error_show(isfirst){
	if(isfirst==1){
		$("#company_list_ul").html("<li>暂无数据</li>");
	}
	$(".load_more").hide();
}

$(function(){
	
	//点击企业查询
	$("#company_find").click(function(){
		
		$("#myform").submit();
	});
});