function companydetail_show(){
	
	//ajax请求数据
	$.ajax({
		url:'/interface/getcompanydetail',
		type:'post',
		dataType:'json',
		data:'companyid='+$("#companyid").val(),
		success:function(data){
			if(data!=null){
				$("#iconurl").html("<img src='"+data.iconurl+"' width='96' height='96' id='company_img'>");
				$("#company_img").attr("onerror","$(this).attr('src','/appcssjs/images/index/logo_img03.jpg')");
				if(data.companyname!=null && data.companyname!=''){
					$("#companyname").html(data.companyname);
				}else{
					$("#companyname").html("无");
				}
				if(data.natureid!=null && data.natureid!=''){
					$("#natureid").html(data.natureid);
				}else{
					$("#natureid").html("无");
				}
				if(data.scaleid!=null && data.scaleid!=''){
					$("#scaleid").html(data.scaleid);
				}else{
					$("#scaleid").html("无");
				}
				if(data.industryid!=null && data.industryid!=''){
					$("#industryid").html(data.industryid);
				}else{
					$("#industryid").html("无");
				}
				if(data.address!=null && data.address!=''){
					$(".address").html("<a href='#'><span class='word_hidden'>"+data.address+"</span></a>");
				}else{
					$(".address").html("<span class='word_hidden'>无</span>");
				}
				if(data.about!=null && data.about!=''){
					$("#pbox_height").html("<p>"+data.about+"</p>");
				}else{
					$("#pbox_height").html("暂无数据");
				}
				$("#company_job_more").attr("onclick","location.href='/jumpPage/getJobList?companyid="+data.companyid+"'");
				if(data.datalist!=null && data.datalist.length>0){
					var leng=data.datalist.length;
					var str="";
					for(var i=0;i<leng;i++){
						var path="/jumpPage/getJobDetail?positionId="+data.datalist[i].positionid;
				        str+="<li onclick='location.href=\""+path+"\"'>";
				        str+="<div class='xx_01'><span class='word_hidden'>"+data.datalist[i].positionname+"</span><i>"+data.datalist[i].createtime+"</i></div>";
				        str+="<div class='xx_02'>";
				        str+="<span class='time'>"+data.datalist[i].salary+"</span>";
				        str+="<span class='study'>"+data.datalist[i].experience+"</span>";
				        str+="<span class='pay'>"+data.datalist[i].education+"</span>";
				        str+="</div>";
				        str+="</li>";
					}
					if(str.length>0){
						$("#job_ul").html(str);
					}else{
						$("#job_ul").html("<li>暂无数据</li>");
					}
				}else{
					$("#job_ul").html("<li>暂无数据</li>");
				}
			}else{
				error_detail_show();
			}
		},
		error:function(){
			error_detail_show();
		}
	});
	
}

/**
 * 	错误时显示的数据
 */
function error_detail_show(){
	$("#companyname").html("无");
    $("#natureid").html("无");
    $("#scaleid").html("无");
    $("#industryid").html("无");
	$(".address").html("<span class='word_hidden'>无</span>");
	$("#pbox_height").html("暂无数据");
	$("#job_ul").html("<li>暂无数据</li>");
}

$(function(){
	
	//滑到底部自动分页
	PageHelper({
		url:"/interface/getpagecompanypositionlist",
		success:function(data){
			if(data!=null && data.datalist!=null){
				alert(data.datalist.length);
			}else{
				alert("请求成功，但是没有苏剧");
			}
		},
		error:function(){
			alert("请求错误");
		}
	}); 
	
});