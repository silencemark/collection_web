var userInfo = JSON.parse(localStorage.GlobalData).userinfo;

//新消息
function getMessage(message){
	onloaddata();
	if($('#li_'+message.targetId).length>0){
		$("[name='lastcontent']",$('#li_'+message.targetId)).addClass("unread");
		var date = new Date(message.sentTime);
		var sendtime=date.formatday("a hh:mm");
		$('#li_'+message.targetId).find("i[name=lasttime]").text(sendtime);
		if(message.content.messageName=="ImageMessage"){//图片
			message.content.content = "[图片]";
	    }else{
	    	if(message.content.extra=="3"){//语音
	    		message.content.content = "[语音]";
	    	} 
	    }
		$('#li_'+message.targetId).find("p[name=lastcontent]").text(message.content.content);
		var lihtml=$('#li_'+message.targetId).html();
		$('#infoul').prepend($('#li_'+message.targetId));
		
		//更新缓存中的数据
		var grouplist = getGroupList();
		localStorage.GroupList = "{}";
		//得到需要更新的组的id
		var targetid = message.targetId;
		//将最新的组放在第一个
		for(var item in grouplist){
			if(targetid == item){
				setGroupList(targetid , grouplist[item]);
			}
		}
		//将其他的组一次追加上去
		for(var it in grouplist){
			if(targetid != it){
				setGroupList(grouplist[it].groupid , grouplist[it]);
			}
		}
	}else{
		$.ajax({
    		type:"post",
    		dataType:"json",
    		url:projectpath+"/app/getMyAllCartList",
    		data:{
    			groupid:message.targetId,
    			userid:userInfo.userid
    		},
    		success:function(resultMap){
    			if(resultMap.status == 0 && resultMap.status == '0'){
    				localStorage.GroupList = "{}";
    				var dataList = resultMap.dataList;
    				for(var k=dataList.length-1;k>=0;k--){
    					var datalistlength = dataList.length - 1;
    					setGroupList(dataList[datalistlength-k].groupid, jQuery.extend({}, {}, dataList[datalistlength-k]));
//    					setGroupList(dataList[k].groupid, dataList[k]);
    					if($("li[groupid='"+dataList[k].groupid+"']",$('#infoul')).length==0){
    						var temp= getShowHtml(dataList[k]);
    			        	$('#infoul').prepend(temp);
    					}else{
    						getShowHtml2(dataList[k]);
    					}
    				}
    			}
    		}
    	})
	}
}
//群聊
function creategroup(userlist){
	//查询所有用户 去重
	$.ajax({
		type:"post",
		dataType:"json",
		url:projectpath+"/app/createManyGroup",
		data:"userlist="+userlist+"&createid="+userInfo.userid,
		success:function(data){
			//进入聊天会话窗口 或者群聊窗口
			location.href="talk.html?isGroud="+data.isgroup+"&groupId="+data.groupId;
		}
	})
}
//单聊
function logical(userid){
	//创建单聊
	$.ajax({
		type:"post",
		dataType:"json",
		url:projectpath+"/app/createOneGroup",
		data:"userid="+userid+"&createid="+userInfo.userid,
		success:function(data){
			//进入聊天会话窗口 或者群聊窗口
			location.href="talk.html?isGroud=2&groupId="+data.groupId;
		}
	})
}
//加载更多
function getinfodetail(){
	onloaddata();
	//加载更多
	PageHelper({
		url:projectpath+"/app/getMyAllCartList",
		pageNo:1,
		pageSize:30,
		isload:true,
		beforefun:function(data){
			$(".bt_none").before($("#"+data.fresh_id));
		},
		data:{userid:userInfo.userid},
		success:function(resultMap){
			 
			if(resultMap.status == 0 && resultMap.status == '0'){
				if(this.pageNo==1){ 
					localStorage.GroupList = "{}"
				} 
				var dataList = resultMap.dataList;
				for(var k=dataList.length-1;k>=0;k--){
					var datalistlength = dataList.length - 1;
					setGroupList(dataList[datalistlength-k].groupid, dataList[datalistlength-k]);
					if($("li[groupid='"+dataList[k].groupid+"']",$('#infoul')).length==0){
						var temp= getShowHtml(jQuery.extend({}, {}, dataList[k]));
						if(this.pageNo==1){
							$('#infoul').prepend(temp);
						}else{
							$('#infoul').append(temp);
						}
					}else{
						getShowHtml2(jQuery.extend({}, {}, dataList[k]));
					}
				}
			}
			
			if(this.pageNo==1){
				//置顶
				if($("[nomsg=0][istop=1]",$('#infoul')).length>0){
					for(var i=$("[istop=1]",$('#infoul')).length-1;i>=0;i--){
						$('#infoul').prepend($("[istop=1]",$('#infoul')).eq(i));
					}
				}
			}
		}
	}); 
		
	
}

function onloaddata(){
	//更新底部新消息数量
	$.ajax({
		type:"post",
		dataType:"json",
		url:projectpath+"/app/getChatRecordStatusCount",
		data:{ 
			userid:userInfo.userid
		},
		success:function(data){
			if(data.success && data.count>0){
				$('.messagecount').text(data.count);
				$('.messagecount').show(); 
			}else{
				$('.messagecount').hide(); 
			}
		}
	})
}

function setGroupList(groupid,data){
	var GroupList = {};
	if(localStorage.GroupList){
		GroupList = JSON.parse(localStorage.GroupList);
		if(!GroupList){
			GroupList = {};
		}
	} 
	GroupList[groupid] = data;
	localStorage.GroupList = JSON.stringify(GroupList);
}

function getGroupList(){
	if(localStorage.GroupList){
		return JSON.parse(localStorage.GroupList);
	} 
	return {};
}

function getShowHtml(data){ 
	var errorimg = projectpath+"/app/appcssjs/images/massage/group.png";
	if(data.isgroup==2){
		errorimg = projectpath+"/app/appcssjs/images/defaultheadimage.png";
	}
	if(!data.createtime){
		data.createtime = data.lastdate;
	}
	var headimg = projectpath+"/default/img/group/"+data.groupid;
	if(data.isgroup==2){
		headimg=projectpath+data.groupurl;
	}
	if(data.lasttime==null){
		data.lasttime = data.createtime;
	}
	if(data.lasttime==null){
		data.lasttime = "";
	}else if(isNaN(data.lasttime)){
		
	}else{
		if(data.lasttime && new Date(data.lasttime).format("YYYY-MM-DD")!=new Date().format("YYYY-MM-DD")){
			data.lasttime = new Date(data.lasttime).format("MM月DD日");
		}else{
			data.lasttime = new Date(data.lasttime).formatday("a hh:mm");
		}
	}  
	var temp="";
	temp+="<li nomsg=0 istop="+data.istop+" groupid="+data.groupid+" onclick=\"location.href='talk.html?isGroud="+data.isgroup+"&groupId="+data.groupid+"'\" id=\"li_"+data.groupid+"\">"+
    	"<div class=\"li_box\" id=\""+data.groupid+"_box\">" +
    		"<b class=\"big bg_color"+GetRandomNum(1,4)+"\" style='border-radius: 0px'><img src=\""+headimg+"\" onerror=this.src='"+errorimg+"'></b>"+
	        "<span>"+data.groupname+"</span><i name=\"lasttime\">"+data.lasttime+"</i>"+
	        "<div class=\"clear\"></div>";
    data.unread = "";
    if(data.status == 0){
    	data.unread = " unread";
    }
    if(data.content==null || data.content==""){
		data.content = "&nbsp;";
	}
    if(data.type==2){
		temp+="<p class=\"word_hidden "+data.unread+"\" name=\"lastcontent\">[图片]</p>";
	}else if(data.type==3){
		temp+="<p class=\"word_hidden "+data.unread+"\" name=\"lastcontent\">[语音]</p>";
	}else if(data.type==1){
		temp+="<p class=\"word_hidden "+data.unread+"\" name=\"lastcontent\">"+data.content+"</p>";
	}else{
		temp+="<p class=\"word_hidden \" name=\"lastcontent\">"+data.content+"</p>";
	}
    temp += '</div>'+
    		'<a class="del_btn" id="'+data.groupid+'_del" style="width:0px;" onclick="event.stopPropagation();deleteGroup(this)">删除</a>'+
    		'<div class="clear"></div>';
    temp += "</li>";
	return temp;
}

function getShowHtml2(data){
	
	if(data.status == 0){
		$("[name='lastcontent']",$("li[groupid='"+data.groupid+"']",$('#infoul'))).addClass("unread");
    }else{
    	$("[name='lastcontent']",$("li[groupid='"+data.groupid+"']",$('#infoul'))).removeClass("unread");
    }
	if(data.content==null || data.content==""){
		data.content = "&nbsp;";
	}
	if(data.type==2){
		$("[name='lastcontent']",$("li[groupid='"+data.groupid+"']",$('#infoul'))).text("[图片]");
	}else if(data.type==3){
		$("[name='lastcontent']",$("li[groupid='"+data.groupid+"']",$('#infoul'))).text("[语音]");
	}else if(data.type==1){
		$("[name='lastcontent']",$("li[groupid='"+data.groupid+"']",$('#infoul'))).text(data.content);
	}
	if(!data.lasttime){
		data.lasttime = data.createtime;
	}
	if(data.lasttime && new Date(data.lasttime).format("YYYY-MM-DD")!=new Date().format("YYYY-MM-DD")){
		data.lasttime = new Date(data.lasttime).format("MM月DD日");
	}else{
		data.lasttime = new Date(data.lasttime).formatday("a hh:mm");
	}
	if(data.lasttime==null){
		data.lasttime = "";
	}
	$("[name='lasttime']",$("li[groupid='"+data.groupid+"']",$('#infoul'))).text(data.lasttime);
	$('#infoul').prepend($("li[groupid='"+data.groupid+"']",$('#infoul')).attr("nomsg","0"));
}

window.alert =  function(obj){
	console.log(obj);
}
/**
 * 
 * 
 * 退出讨论组
 * @param obj
 */
function deleteGroup(obj){
	var groupid = $(obj).attr("id").replace("_del","");
	//删除我们自己的服务器中的数据
	if(userInfo.userid != undefined && groupid != ""){
		//删除页面数据
		$(obj).parent().remove();
		
		//更新缓存中的数据
		var grouplist = getGroupList();
		localStorage.GroupList = "{}";
		//得到需要更新的组的id
		var targetid = groupid;
		//将最新的组放在第一个
		for(var item in grouplist){
			if(targetid != item){
				setGroupList(item , grouplist[item]);
			}
		}
		//alert(JSON.stringify(getGroupList()));
		$.ajax({
			url:projectpath+"/app/userBackGroup",
			type:"post",
			data:{userid:userInfo.userid,groupid:groupid},
			success:function(data){
				if(data.status == 1 || data.status == '1'){
					swal("","网络异常，请稍后重试···","warning");
				}else if(data.status == 0 || data.status == '0'){
					if(data.groupid != null && data.groupid != "" && data.groupid != "null"){
						//退出群
						RongIMClient.getInstance().quitGroup(data.groupid, { 
							onSuccess: function() {
							    console.log("quitGroup Successfully"); 
							}, onError: function(error) {
						         console.log("quitGroup:errorcode:" + error); 
						    } 
						});
					}
				}
			},error:function(e){
				swal("","网络异常，请稍后重试···","warning");
			}
		});
		
		
	}else{
		swal("","网络异常，请稍后重试···","warning");
	}
}

$(function(){
	var GroupList =  getGroupList();
	if (typeof (GroupList) == "object") {
        for ( var i in GroupList) {// 用javascript的for/in循环遍历对象的属性
            if (GroupList.hasOwnProperty(i)) {
                if(GroupList[i]){
                	$('#infoul').append(getShowHtml(GroupList[i]));
                }
            }
        }
        //置顶
		if($("[nomsg=0][istop=1]",$('#infoul')).length>0){
			for(var i=$("[istop=1]",$('#infoul')).length-1;i>=0;i--){
				$('#infoul').prepend($("[istop=1]",$('#infoul')).eq(i));
			}
		}
    }
	
	//滑动删除
	var startX,startY,endX,endY,id,tiems=0,touchtype=0,deletwidth,fag=false;
	var scrollTopVal=0;
	document.getElementById("infoul").addEventListener("touchstart", touchStart, false);
	document.getElementById("infoul").addEventListener("touchmove", touchMove, false);
	document.getElementById("infoul").addEventListener("touchend", touchEnd, false);
	function touchStart(event){
		 var touch = event.touches[0]; 
		 var tar_id = $(touch.target).parent().attr('id') == null?'':$(touch.target).parent().attr('id');
		 id = tar_id.replace("_box","").replace("li_",""); 
		 var boxhtml = $('#'+id+"_box");
		 var delhtml = $('#'+id+"_del");
		 if(boxhtml.lenght<=0 || delhtml.length<=0){
			 fag=true;
		 }else{
			 fag = false;
		 }
		 startY = touch.pageY;   
		 startX = touch.pageX;
		 
		 deletwidth = 64;
	}
	function touchMove(event){
		var touch = event.touches[0];
		endY = touch.pageY;
		endX = touch.pageX;
		
		if(Math.abs(endY - startY) > Math.abs(endX - startX)){
			fag = true;
		}else{
			//防止页面上下滚动
			event.preventDefault();
		}
		
		if(fag){
			return;
		}
		
		if(tiems == 0){
			scrollTopVal = endX;
		}
		
		if((scrollTopVal - endX) < 64 && (scrollTopVal - endX) > 0 ){
			touchtype = 0;
			var delet = $('#'+id+"_del").css("width").replace("px","");
			if(delet != 64){
				$('#'+id+"_box").css("margin-left",-(scrollTopVal - endX));
				$('#'+id+"_del").css("width",scrollTopVal - endX);
			}
		}else if((endX - scrollTopVal) < 64 && (endX - scrollTopVal) > 0 && touchtype != 0){
			touchtype = 1;
			var delet = $('#'+id+"_del").css("width").replace("px","");
			if(delet > 0){
				$('#'+id+"_box").css("margin-left",(endX - scrollTopVal) - deletwidth);
				$('#'+id+"_del").css("width",(deletwidth - (endX - scrollTopVal)));
			}
		}
		tiems++;
	}
	function touchEnd(event){ 
		
		tiems = 0;
		var deletewidth = $('#'+id+"_del").css("width").replace("px","");
		if(touchtype == 0){
			if(deletewidth >= 32){
				$('#'+id+"_box").css("margin-left",-64);
				$('#'+id+"_del").css("width",64);
				touchtype = 1;
			}else{
				$('#'+id+"_box").css("margin-left",0);
				$('#'+id+"_del").css("width",0);
			}
		}else if(touchtype == 1){
			if(deletewidth <= 32){
				$('#'+id+"_box").css("margin-left",0);
				$('#'+id+"_del").css("width",0);
			}else{
				$('#'+id+"_box").css("margin-left",-64);
				$('#'+id+"_del").css("width",64);
			}
		}
	} 
})
function GetRandomNum(Min,Max)
{   
	var Range = Max - Min;   
	var Rand = Math.random();   
	return(Min + Math.round(Rand * Range));   
}   