/**
 * 	加载消息类型列表
 */
function msg_data_show(){
	//获取编号
	var userid=$("#userid").val();
	if(userid.length<=0){
		error_data_show();
		return;
	}
	//ajax请求数据
	$.ajax({
		url:'/interface/getConpanyMessageType',
		type:'post',
		dataType:'json',
		data:'userid='+userid,
		success:function(data){
			if(data!=null && data.data!=null && data.data.length>0){
				var leng=data.data.length;
				var str="";
				for(var i=0;i<leng;i++){
					var path="/weixin/companymsg/companymessagedetail?type="+data.data[i].type;
					if(i==2){
						str+="<li class='last' onclick='location.href=\""+path+"\"'>";
					    str+="<div class='img'><img src='"+data.data[i].iconurl+"' class='msg_type_img'></div>";
					    str+="<div class='name'>"+data.data[i].typename+"<i class='red'>"+(data.data[i].count!=null && data.data[i].count!='' && data.data[i].count!='undefined'?(parseInt(data.data[i].count)<100?'('+data.data[i].count+')':'('+data.data[i].count+'+)'):'')+"</i></div>";
					    str+="<div class='txt word_hidden'>"+(data.data[i].newmsg!=null && data.data[i].newmsg!='' && data.data[i].newmsg!='undefined'?data.data[i].newmsg:'无')+"</div>";
					    str+="</li>";
					}else{
						str+="<li class='last' onclick='location.href=\""+path+"\"'>";
					    str+="<div class='img'><img src='"+data.data[i].iconurl+"' class='msg_type_img'></div>";
					    str+="<div class='name'>"+data.data[i].typename+"<i class='red'>"+(data.data[i].count!=null && data.data[i].count!='' && data.data[i].count!='undefined'?(parseInt(data.data[i].count)<100?'('+data.data[i].count+')':'('+data.data[i].count+'+)'):'')+"</i></div>";
					    str+="<div class='txt word_hidden'>"+(data.data[i].newmsg!=null && data.data[i].newmsg!='' && data.data[i].newmsg!='undefined'?data.data[i].newmsg:'无')+"</div>";
					    str+="</li>";
					    
					}
				}
				if(str.length>0){
					$("#msg_type_ul").html(str);
					$(".msg_type_img").attr("onerror","$(this).attr('src','/appcssjs/images/membercenter/msg_ico01.png')");
				}else{
					error_data_show();
				}
			}else{
				error_data_show();
			}
		},
		error:function(){
			error_data_show();
		}
	});
}

function error_data_show(){
	var str="";
    str+="<li>";
    str+="<div class='img'><img src='/appcssjs/images/membercenter/msg_ico01.png'></div>";
    str+="<div class='name'>系统消息</div>";
    str+="<div class='txt word_hidden'>无</div>";
    str+="</li>";
    str+="<li>";
    str+="<div class='img'><img src='/appcssjs/images/membercenter/msg_ico02.png'></div>";
    str+="<div class='name'>申请岗位消息</div>";
    str+="<div class='txt word_hidden'>无</div>";
    $("#msg_type_ul").html(str);
}