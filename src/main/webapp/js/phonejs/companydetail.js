function companydetail_show(){
	
	//ajax请求数据
	$.ajax({
		url:'/interface/getcompanydetail',
		type:'post',
		dataType:'json',
		data:'companyid='+$("#companyid").val(),
		success:function(data){
			if(data!=null){
				$("#iconurl").html("<img src='"+data.data.iconurl+"' width='96' height='96' id='company_img'>");
				$("#company_img").attr("onerror","$(this).attr('src','/appcssjs/images/index/logo_img03.jpg')");
				if(data.data.companyname!=null && data.data.companyname!=''){
					$("#companyname").html(data.data.companyname);
				}else{
					$("#companyname").html("无");
				}
				if(data.data.natureid!=null && data.data.natureid!=''){
					$("#natureid").html(data.data.natureid);
				}else{
					$("#natureid").html("无");
				}
				if(data.data.scaleid!=null && data.data.scaleid!=''){
					$("#scaleid").html(data.data.scaleid);
				}else{
					$("#scaleid").html("无");
				}
				if(data.data.industryid!=null && data.data.industryid!=''){
					$("#industryid").html(data.data.industryid);
				}else{
					$("#industryid").html("无");
				}
				
				if(data.data.address!=null && data.data.address!=''){
					$(".address").html("<a href=\"/public/developer.jsp?longitude="+$("#longitude").val()+"&latitude="+$("#latitude").val()+"&suppliername="+data.data.address+"\"><span class=\"word_hidden\">"+data.data.address+"</span></a>");
				}else{
					$(".address").html("<span class='word_hidden'>无</span>");
				}
				if(data.data.about!=null && data.data.about!=''){
					$("#pbox_height").html("<p>"+data.data.about+"</p>");
				}else{
					$("#pbox_height").html("暂无数据");
				}
				$("#company_job_more").attr("onclick","location.href='/weixin/jumpPage/getJobList?companyid="+data.data.companyid+"'");
				if(data.data.datalist!=null && data.data.datalist.length>0){
					var leng=data.data.datalist.length;
					var str="";
					for(var i=0;i<leng;i++){
						var path="/weixin/jumpPage/getJobDetail?positionId="+data.data.datalist[i].positionid;
				        str+="<li onclick='location.href=\""+path+"\"'>";
				        str+="<div class='xx_01'><span class='word_hidden'>"+data.data.datalist[i].positionname+"</span><i>"+data.data.datalist[i].createtime+"</i></div>";
				        str+="<div class='xx_02'>";
				        str+="<span class='time'>"+data.data.datalist[i].salary+"</span>";
				        str+="<span class='study'>"+data.data.datalist[i].experience+"</span>";
				        str+="<span class='pay'>"+data.data.datalist[i].education+"</span>";
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
		data:{companyid:$("#companyid").val()},
		pageSize:3,
		success:function(data){
			if(data!=null && data.data!=null && data.data!='' && data.data.length>0){
				var leng=data.data.length;
				var str="";
				for(var i=0;i<leng;i++){
					var path="/jumpPage/getJobDetail?positionId="+data.data[i].positionid;
			        str+="<li onclick='location.href=\""+path+"\"'>";
			        str+="<div class='xx_01'><span class='word_hidden'>"+data.data[i].positionname+"</span><i>"+data.data[i].createtime+"</i></div>";
			        str+="<div class='xx_02'>";
			        str+="<span class='time'>"+data.data[i].salary+"</span>";
			        str+="<span class='study'>"+data.data[i].experience+"</span>";
			        str+="<span class='pay'>"+data.data[i].education+"</span>";
			        str+="</div>";
			        str+="</li>";
				}
				if(str.length>0){
					$("#job_ul").append(str);
				}
			}
		},
		error:function(){
			
		}
	}); 
	
});