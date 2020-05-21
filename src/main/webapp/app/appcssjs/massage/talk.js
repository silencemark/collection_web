//用户信息
var userInfo = JSON.parse(localStorage.GlobalData).userinfo;
//页面传入参数
var groupId=getUrlParam("groupId");
var isGroud=getUrlParam("isGroud");
var realName = userInfo.realname;
//容云IM表情初始化
RongIMLib.RongIMEmoji.init();

var isReplaceBHTML = false;

function getinfodetail(){
	var targetId = groupId;
	var conversationType = RongIMLib.ConversationType.GROUP;
}

function reloademojis(){
	var clientWidth = document.body.clientWidth - 30;
	clientWidth = (clientWidth - (24*7))/17 + 1.5;
	var emojis = RongIMLib.RongIMEmoji.emojis;
	var temphtml="";
	for(var i=0;i<40;i++){
		var emojishtml = emojis[i].innerHTML;
		if(isReplaceBHTML){
			if($(emojishtml).find("b").length > 0){
				var str = RongIMLib.RongIMEmoji.symbolToEmoji($(emojishtml).attr("name"));
				str =$(emojishtml).html(str)[0].outerHTML;
				emojishtml = str;
			}
		}
		temphtml+="<span onclick=\"chooseemojis(this)\" style=\"margin:auto "+clientWidth.toFixed(1)+"px;\">"+emojishtml+"</span>";//
	}
	$('#emojisdiv').html(temphtml);
}
function chooseemojis(obj){
	var content=$('#content').text();
	var str = $(obj).children().attr("name")+"";
	if(isReplaceBHTML){
		str = RongIMLib.RongIMEmoji.symbolToEmoji(str);
	}
	$('#content').text(content+str);
	$("img",$(".add.send")).attr("src",'../appcssjs/images/massage/send.png');
}

	function onPhotoDataSuccess(imageData,fileSize) {
		$('#imageurl').val(imageData);
		imageData=projectpath+imageData;
		$("#adddiv").hide();
		$("#emojisdiv").hide();
		
		//发送消息到融云
		var base64Str="";
		convertImgToBase64(imageData, function(base64Img){
            // base64Img为转好的base64,放在img src直接前台展示(<img src="data:image/jpg;base64,/9j/4QMZRXh...." />)
            //console.log(base64Img);
            // base64转图片不需要base64的前缀data:image/jpg;base64
            console.log(base64Img.split(",")[1]);
            base64Str=base64Img;
            
            sendimagemessage(base64Str,imageData);
    		
          });
	}

	function convertImgToBase64(url, callback, outputFormat){ 
        var canvas = document.createElement('CANVAS'); 
        var ctx = canvas.getContext('2d'); 
        var img = new Image; 
        img.crossOrigin = 'Anonymous'; 
        img.onload = function(){
          var width = img.width;
          var height = img.height;
          // 按比例压缩2倍
          var rate = (width<height ? width/height : height/width)/2;
          canvas.width = width*rate; 
          canvas.height = height*rate; 
          ctx.drawImage(img,0,0,width,height,0,0,width*rate,height*rate); 
          var dataURL = canvas.toDataURL(outputFormat || 'image/png'); 
          callback.call(this, dataURL); 
          canvas = null; 
        };
        img.src = url;
    }
	
	function sendimagemessage(base64Str,imageUri){
		//var base64Str = "base64 格式缩略图";// 图片转为可以使用 HTML5 的 FileReader 或者 canvas 也可以上传到后台进行转换。
		//var imageUri = "图片 URL"; // 上传到自己服务器的 URL。
		 var msg = new RongIMLib.ImageMessage({content:base64Str,imageUri:imageUri});
		 var conversationtype = 3; 
		 var targetId = groupId; // 目标 Id
		 RongIMClient.getInstance().sendMessage(conversationtype, targetId, msg, {
		                onSuccess: function (message) {
		                    //message 为发送的消息对象并且包含服务器返回的消息唯一Id和发送消息时间戳
		                	
		                	var noneimg=new Image();
		                	noneimg.onload=function(){
		                		
		                		var nonewidth=noneimg.width;
								var noneheight=noneimg.height;
								var newheight=96;
								var newwidth=96;
								if(nonewidth!=0 && noneheight!=0 && noneheight>nonewidth){
									newwidth=48;
								}
		                		document.getElementById(noneimg.src).width=newwidth;
		            			document.getElementById(noneimg.src).height=newheight;
		            			document.getElementById(noneimg.src).style.display="block";
		                		
		                	};
		                	noneimg.src=base64Str;
							
							
		                    $('#infoul').append("<li>"+
		    			        	"<div class=\"user_r\" ><img  src=\""+projectpath+userInfo.headimage+"\" onerror=this.src='"+hrefurl+"/app/appcssjs/images/defaultheadimage.png'></div>"+
//		    			            "<div class=\"name_r\">"+userInfo.realname+"</div>"+
//		    			            "<div class=\"msg_r\" style=\"padding:0px;background:none;\"><em></em><img id=\""+base64Str+"\" style=\"display:none;\" width=\"96\" height=\"96\" src=\""+imageUri+"\" onclick=\"showbigimg(this)\"></div>"+
		    			        	"<br>"+
		    			            "<div class=\"msg_r\" style=\"padding:0px;background:none;\"><img id=\""+base64Str+"\" style=\"display:none;\" width=\"96\" height=\"96\" src=\""+imageUri+"\" onclick=\"showbigimg(this)\"></div>"+		    			        	
		    			            "<div class=\"clear\"></div>"+
		    			        "</li>");
		    			     $("body").scrollTop($("body")[0].scrollHeight);
		    			     $("#talkbody").scrollTop($("#talkbody ul")[0].scrollHeight);
		                    //保存数据库
		    			     
		                    $.ajax({
	    			    	 type:"post",
	    			    	 dataType:"json",
	    			    	 url:projectpath+"/app/insertChatRecord",
	    			    	 data:{
	    			    		 raelname:realName,
	    			    		 content:imageUri,
	    			    		 groupid:groupId,
	    			    		 type:2,
	    			    		 isgroud:isGroud,
	    			    		 createid:userInfo.userid
	    			    	 },
	    			    	 success:function(data){
	    			    		 
	    			    	 }
	    			     })
		                },
		                onError: function (errorCode,message) {
		                    var info = '';
		                    switch (errorCode) {
		                        case RongIMLib.ErrorCode.TIMEOUT:
		                            info = '超时';
		                            break;
		                        case RongIMLib.ErrorCode.UNKNOWN_ERROR:
		                            info = '未知错误';
		                            break;
		                        case RongIMLib.ErrorCode.REJECTED_BY_BLACKLIST:
		                            info = '在黑名单中，无法向对方发送消息';
		                            break;
		                        case RongIMLib.ErrorCode.NOT_IN_DISCUSSION:
		                            info = '不在讨论组中';
		                            break;
		                        case RongIMLib.ErrorCode.NOT_IN_GROUP:
		                            info = '不在群组中';
		                            break;
		                        case RongIMLib.ErrorCode.NOT_IN_CHATROOM:
		                            info = '不在聊天室中';
		                            break;
		                        default :
		                            info = x;
		                            break;
		                    }
		                    //console.log('发送失败:' + info);
		                    alert('发送失败:' + info);
		                }
		            }
		        );
		//更新缓存中的数据
			var grouplist = getGroupList();
			localStorage.GroupList = "{}";
			//得到需要更新的组的id
			var targetid = groupId;
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
	}

function getMessage(message){
	if(message.targetId==groupId){
	var senderUserIds=[];
	senderUserIds[0]=message.senderUserId;
		$.ajax({
			type:"post",
			dataType:"json",
			url:projectpath+"/app/getSendUserList?this_userid="+userInfo.userid,
			data:"senderUserIds="+senderUserIds+"&groupid="+groupId,
			success:function(data){
				signChatRecordStatus();
				if(data.userlist.length>0){
					var temp="";
					for(var j=0;j<data.userlist.length;j++){
						message.content.times = "";
						message.content.divonclick = "";
						if(userInfo.userid != data.userlist[j].userid){
							var ismsg=0;
							var scound=0;
							if(message.content.messageName=="ImageMessage"){//图片
								ismsg=1;
								var noneimg = new Image();
								noneimg.onload=function(){
									var newheight=96;
									var newwidth=96;
									var nonewidth=noneimg.width;
									var noneheight=noneimg.height;
									if(nonewidth!=0 && noneheight!=0 && noneheight>nonewidth){
										newwidth=48;
									}
									document.getElementById(noneimg.src).width=newwidth;
									document.getElementById(noneimg.src).height=newheight;
									document.getElementById(noneimg.src).style.display="block";
								}
								noneimg.src=message.content.content;
								
								message.content.content="<img id=\""+message.content.content+"\" style=\"display:none;\" width=\"96\" height=\"96\" src=\""+message.content.content+"\" onclick=\"showbigimg(this)\">";
		    			    }else{
		    			    	if(message.content.extra=="3"){
									scound=JSON.parse(message.content.content).times*1.6+40;
									/*if(scound>75){
										scound=75;
									}else if(scound<10){
										scound=10;
									}*/
								}
		    			    	if(message.content.extra=="3"){//语音
		    			    		var recordid = JSON.parse(message.content.content).recordid;
		    			    		message.content.times = "<div class='time_l'>"+JSON.parse(message.content.content).times+"″</div><div id="+recordid+" class=\"read_l\">未读</div>";
		    			    		message.content.content = JSON.parse(message.content.content).content;
		    			    		message.content.divonclick = 'onclick=play_voice_massage(this,\"'+message.content.content+'\",\"'+recordid+'\")';
		    			    		message.content.content='<img src="../appcssjs/images/msg/ico_sound1.png" width="20">';
		    			    	}else{//文字
		    			    		if(isReplaceBHTML){
		    			    			message.content.content=RongIMLib.RongIMEmoji.symbolToEmoji(message.content.content);
		    			    		}else{
		    			    			message.content.content=RongIMLib.RongIMEmoji.symbolToHTML(message.content.content);
		    			    		}
		    			    	}
		    			    }
							
							temp+="<li>"+
			    			        	"<div class=\"user_l\" ><img  src=\""+projectpath+data.userlist[j].headimage+"\" onerror=this.src='"+hrefurl+"/app/appcssjs/images/defaultheadimage.png'></div>"+
			    			            "<br><div class=\"msg_l\" "+message.content.divonclick+" style=\"top:-10px;"+(scound!=0?("width:"+scound+"px;"):"")+(ismsg==1?"padding:0px;background:none;":"")+"\">"+message.content.content+"</div>"+
			    			            message.content.times+
			    			            "<div class=\"clear\"></div></li>";
						}else{
							var ismsg=0;
							var scound=0;
							if(message.content.messageName=="ImageMessage"){//图片
								ismsg=1;
								
								var noneimg = new Image();
								noneimg.onload=function(){
									var newheight=96;
									var newwidth=96;
									var nonewidth=noneimg.width;
									var noneheight=noneimg.height;
									if(nonewidth!=0 && noneheight!=0 && noneheight>nonewidth){
										newwidth=48;
									}
									document.getElementById(noneimg.src).width=newwidth;
									document.getElementById(noneimg.src).height=newheight;
									document.getElementById(noneimg.src).style.display="block";
								}
								noneimg.src=message.content.content;
								
								
								message.content.content="<img id=\""+message.content.content+"\" style=\"display:none;\" width=\"96\" height=\"96\" src=\""+message.content.content+"\" onclick=\"showbigimg(this)\">";
		    			    }else{
		    			    	if(message.content.extra=="3"){
		    			    		scound=JSON.parse(message.content.content).times*1.6+40;
									/*if(scound>75){
										scound=75;
									}else if(scound<10){
										scound=10;
									}*/
								}
		    			    	if(message.content.extra=="3"){//语音
		    			    		message.content.times = "<div class='time_r'>"+JSON.parse(message.content.content).times+"″</div>";
		    			    		message.content.content = JSON.parse(message.content.content).content;
		    			    		message.content.divonclick = 'onclick=play_voice_massage(this,\"'+message.content.content+'\")';
		    			    		message.content.content='<img src="../appcssjs/images/msg/ico_sound2.png" width="20">';
		    			    	}else{//文字
		    			    		if(isReplaceBHTML){
		    			    			message.content.content=RongIMLib.RongIMEmoji.symbolToEmoji(message.content.content);
		    			    		}else{
		    			    			message.content.content=RongIMLib.RongIMEmoji.symbolToHTML(message.content.content);
		    			    		}
		    			    	}
		    			    }
							
    						temp+="<li>"+
		    			        	"<div class=\"user_r\" ><img  src=\""+projectpath+data.userlist[j].headimage+"\" onerror=this.src='"+hrefurl+"/app/appcssjs/images/defaultheadimage.png'></div>"+message.content.times+
		    			            "<br><div class=\"msg_r\" "+message.content.divonclick+" style=\"top:-15px;background:#5EAAF8;"+(scound!=0?("width:"+scound+"px;"):"")+(ismsg==1?"padding:0px;background:none;":"")+"\">"+message.content.content+"</div><div class=\"clear\"></div></li>";
						}
					}
					$('#infoul').append(temp);
					$("body").scrollTop($("body")[0].scrollHeight);
					$("#talkbody").scrollTop($("#talkbody ul")[0].scrollHeight);
				}
			}
		})
	}
	//更新缓存中的数据
	var grouplist = getGroupList();
	localStorage.GroupList = "{}";
	//得到需要更新的组的id
	var targetid = groupId;
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
}

function sendmessage(){
	var content=$('#content').text();
	if(content==null || content.trim()==""){
		return false;
	}
	$("#emojisdiv").hide();
	//content = RongIMLib.RongIMEmoji.symbolToEmoji(content);
	var msg = new RongIMLib.TextMessage({content:content,extra:"1"});
	 var conversationtype = 3; // 私聊,其他会话选择相应的消息类型即可。
	 var targetId = groupId; // 目标 Id
	 RongIMClient.getInstance().sendMessage(conversationtype, targetId, msg, { 
	                onSuccess: function (message) {
	                    //message 为发送的消息对象并且包含服务器返回的消息唯一Id和发送消息时间戳
	                    //console.log("Send successfully");
	                    var contentemji = "";
	                    if(isReplaceBHTML){
	                    	contentemji = content;
	                    }else{
	                    	contentemji =  RongIMLib.RongIMEmoji.symbolToHTML(content);
	                    }
	                    $('#infoul').append("<li>"+
	    			        	"<div class=\"user_r\" ><img  src=\""+projectpath+userInfo.headimage+"\" onerror=this.src='"+hrefurl+"/app/appcssjs/images/defaultheadimage.png'></div>"+
	    			            "<br>"+
	    			            "<div class=\"msg_r\" style=\"top:-15px;color:white;background:#5EAAF8;\">"+(contentemji)+"</div>"+
	    			            "<div class=\"clear\"></div>"+
	    			        "</li>");
	                    	$('#content').text("");
	                    	$('#content').blur();
	                    	$("body").scrollTop($("body")[0].scrollHeight);
	    			        $("#talkbody").scrollTop($("#talkbody ul")[0].scrollHeight);
	    			        $("img",$(".add.send")).attr("src",'../appcssjs/images/msg/add.png');
	    			        if(isReplaceBHTML){
	    			        	content = RongIMLib.RongIMEmoji.emojiToSymbol(content);
	    			        }
//	    			        alert("___保存到数据的内容content："+content);
	    			     //保存到数据库
	    			     $.ajax({
	    			    	 type:"post",
	    			    	 dataType:"json",
	    			    	 url:projectpath+"/app/insertChatRecord",
	    			    	 data:{
	    			    		 raelname:realName,
	    			    		 content:content,
	    			    		 groupid:groupId,
	    			    		 type:1,
	    			    		 isgroud:isGroud,
	    			    		 createid:userInfo.userid
	    			    	 },success:function(data){
//	    			    		 alert("保存数据成功:  "+JSON.stringify(data));
	    			    	 }
	    			     })
	    			   	 return false;
	                },
	                onError: function (errorCode,message) {
	                    var info = '';
	                    switch (errorCode) {
	                        case RongIMLib.ErrorCode.TIMEOUT:
	                            info = '超时';
	                            break;
	                        case RongIMLib.ErrorCode.UNKNOWN_ERROR:
	                            info = '未知错误';
	                            break;
	                        case RongIMLib.ErrorCode.REJECTED_BY_BLACKLIST:
	                            info = '在黑名单中，无法向对方发送消息';
	                            break;
	                        case RongIMLib.ErrorCode.NOT_IN_DISCUSSION:
	                            info = '不在讨论组中';
	                            break;
	                        case RongIMLib.ErrorCode.NOT_IN_GROUP:
	                            info = '不在群组中';
	                            break;
	                        case RongIMLib.ErrorCode.NOT_IN_CHATROOM:
	                            info = '不在聊天室中';
	                            break;
	                        default :
	                            info = x;
	                            break;
	                    }
	                    console.log('发送失败:' + info);
	                }
	            }
	        );
	 
	 	//更新缓存中的数据
		var grouplist = getGroupList();
		localStorage.GroupList = "{}";
		//得到需要更新的组的id
		var targetid = groupId;
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
	 return false;
}

//发送语音消息
function sendVoiceMessage(voiceUrl,times){ 
	var recordid = uuid().replace(new RegExp("-","gm"),"");
	var content=JSON.stringify({
		content:voiceUrl,
		times:times,
		recordid:recordid
	}); 
	var msg = new RongIMLib.TextMessage({content:content,extra:"3"});
	 var conversationtype = 3; // 私聊,其他会话选择相应的消息类型即可。
	 var targetId = groupId; // 目标 Id
	 RongIMClient.getInstance().sendMessage(conversationtype, targetId, msg, {
	                onSuccess: function (message) {
	                    //message 为发送的消息对象并且包含服务器返回的消息唯一Id和发送消息时间戳
	                    //console.log("Send successfully");
	                    var contentstr = content;
	                    var times = JSON.parse(content).times;
	                    var recordid = JSON.parse(content).recordid;
	                    content = JSON.parse(content).content;
	                    
	                    var scound=0;
						scound=times*1.6+40;
						/*if(scound>75){
							scound=75;
						}else if(scound<10){
							scound=10;
						}*/
						
	                    $('#infoul').append("<li>"+
	    			        	"<div class=\"user_r\" ><img  src=\""+projectpath+userInfo.headimage+"\" onerror=this.src='"+hrefurl+"/app/appcssjs/images/defaultheadimage.png'></div>"+
//	    			            "<div class=\"name_r\">"+userInfo.realname+"</div>"+
//	    			            "<div class=\"msg_r\" style=\""+(scound!=0?("width:"+scound+"px;"):"")+"background:#7FBE29;\"><em></em>"+'<img src="../appcssjs/images/msg/ico_sound2.png" width="20" onclick=play_voice_massage(this,\"'+content+'\",\"'+recordid+'\")>'+"</div>"+
	    			            "<br>"+
	    			            "<div class=\"msg_r\" onclick=\"play_voice_massage(this,'"+content+"','"+recordid+"')\" style=\"top:-15px;color:white;"+(scound!=0?("width:"+scound+"px;"):"")+"background:#5EAAF8;\">"+'<img src="../appcssjs/images/msg/ico_sound2.png" width="20">'+"</div>"+	    			        	
	    			            "<div class='time_r'>"+times+"″</div>"+
	    			            "<div class=\"clear\"></div>"+
	    			        "</li>");
	    			        $("body").scrollTop($("body")[0].scrollHeight);
	    			        $("#talkbody").scrollTop($("#talkbody ul")[0].scrollHeight);
	    			        $('#content').text("");
	    			        $("img",$(".add.send")).attr("src",'../appcssjs/images/msg/add.png');
	    			     //保存到数据库
	    			     $.ajax({
	    			    	 type:"post",
	    			    	 dataType:"json",
	    			    	 url:projectpath+"/app/insertChatRecord",
	    			    	 data:{
	    			    		 raelname:realName,
	    			    		 content:contentstr,
	    			    		 groupid:groupId,
	    			    		 type:3,
	    			    		 isgroud:isGroud,
	    			    		 createid:userInfo.userid,
	    			    		 recordid:recordid
	    			    	 },
	    			    	 success:function(data){
	    			    		 $.ajax({
	    								type:"post",
	    								dataType:"json",
	    								url:projectpath+"/app/signVoiceChatRecordStatus",
	    								data:{ 
	    									userid:userInfo.userid,
	    									recordid:recordid,
	    									groupid:groupId
	    								},
	    								success:function(data){
	    									if(data.success){
	    										$("#"+recordid).remove();
	    									}else{
	    										
	    									}
	    								}
	    							})
	    			    	 }
	    			     })
	    			   	 return false;
	                },
	                onError: function (errorCode,message) {
	                    var info = '';
	                    switch (errorCode) {
	                        case RongIMLib.ErrorCode.TIMEOUT:
	                            info = '超时';
	                            break;
	                        case RongIMLib.ErrorCode.UNKNOWN_ERROR:
	                            info = '未知错误';
	                            break;
	                        case RongIMLib.ErrorCode.REJECTED_BY_BLACKLIST:
	                            info = '在黑名单中，无法向对方发送消息';
	                            break;
	                        case RongIMLib.ErrorCode.NOT_IN_DISCUSSION:
	                            info = '不在讨论组中';
	                            break;
	                        case RongIMLib.ErrorCode.NOT_IN_GROUP:
	                            info = '不在群组中';
	                            break;
	                        case RongIMLib.ErrorCode.NOT_IN_CHATROOM:
	                            info = '不在聊天室中';
	                            break;
	                        default :
	                            info = x;
	                            break;
	                    }
	                    console.log('发送失败:' + info);
	                }
	            }
	        );
	//更新缓存中的数据
	var grouplist = getGroupList();
	localStorage.GroupList = "{}";
	//得到需要更新的组的id
	var targetid = groupId;
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
	 return false;
}



//上传
function uploadAudioSuccess(){
	$("#content2").css({"background-color":"#ededed"});
	$("#content2").val("按住 说话");
	uploadAudio(localStorage.voice_im_filename_url,10,function(data){
		sendVoiceMessage(data.data.url,localStorage.voice_times);
		localStorage.voice_im_filename_url = null;
		getVideoSrc();
		//发送 
	});
}

//播放语音
function play_voice_massage(dom,url,recordid){
	stopAudio();
	playAudio(url);
	/*$('#chatAudio').remove();
	//载入声音文件 
	$('<audio id="chatAudio"><source src="'+url+'" type="audio/mpeg"></audio>').appendTo('body');
	//播放
	$('#chatAudio')[0].play();*/
	
	$.ajax({
		type:"post",
		dataType:"json",
		url:projectpath+"/app/signVoiceChatRecordStatus",
		data:{ 
			userid:userInfo.userid,
			recordid:recordid,
			groupid:groupId
		},
		success:function(data){
			if(data.success){
				$("#"+recordid).remove();
			}else{
				
			}
		}
	})
	
}

var default_input = "keyboard";
//语音和文字切换
function toggle_keyboard_voice(){
	//content2   语音录入
	//content  文字录入
	if(default_input == "keyboard"){   //设置为文字录入
		$("#content2").hide();
		$("#content").show().text("");
		$(".talk_tool .a_box").css({"padding-right":"84px"});
		$(".bq").show();
		default_input = "voice";
		$(".keyboard_voice").find("img").attr("src","../appcssjs/images/msg/yy.png");
	}else{                             //设置为语音录入
		$("#content2").show();
		$("#content").hide().text("");
		$(".talk_tool .a_box").css({"padding-right":"42px"});
		$("#emojisdiv").hide();
		$(".bq").hide();
		default_input = "keyboard";
		$(".keyboard_voice").find("img").attr("src","../appcssjs/images/massage/keyboard.png");
	}
	$("img",$(".add.send")).attr("src",'../appcssjs/images/msg/add.png');
}


function checkemojisdiv(){
	if($("#emojisdiv").is(':visible')){
		$("#emojisdiv").hide();
	}else{
		$("#emojisdiv").show();
		$("#adddiv").hide();
		$("body").scrollTop($("body")[0].scrollHeight);
		$("#talkbody").scrollTop($("#talkbody ul")[0].scrollHeight);
	}
}

function checkalldiv(){
	if($("#content").text()==null || $("#content").text()==""){//选择
		if($("#adddiv").is(':visible')){
			$("#adddiv").hide();
		}else{
			$("#adddiv").show();
			$("#emojisdiv").hide();
			$("body").scrollTop($("body")[0].scrollHeight);
			$("#talkbody").scrollTop($("#talkbody ul")[0].scrollHeight);
		}
	}else{ //发送
		sendmessage();
	}
}
function showThisGroupInfo(){
	if(localStorage.GroupList){
		 var data = JSON.parse(localStorage.GroupList);
		 if(!isGroud){
			 isGroud = data[groupId].isgroud;			 
		 }
		 if(data[groupId]){
			 $("#groupname").text(data[groupId].groupname);
		 }
	}  
}

//标记已读状态
function signChatRecordStatus(){
	$.ajax({
		type:"post",
		dataType:"json",
		url:projectpath+"/app/signChatRecordStatus",
		data:{
			groupid:groupId,
			userid:userInfo.userid
		},
		success:function(data){
			if(data.success){
				console.log("消息清楚成功！");
			}else{
				console.log("消息清楚失败！");
			}
		}
	})
}
pageNo = 1;
pageSize = 30;
function loadMsg(){
	$.ajax({
		type:"post",
		dataType:"json",
		url:projectpath+"/app/getChatRecordList",
		data:{
			groupid:groupId,
			userid:userInfo.userid,
			pageNo:pageNo,
			pageSize:pageSize,
			
		},
		beforeSend:function(XMLHttpRequest){
		      $("#historybtn").html("<a>信息加载中</a>").removeAttr("onclick");
		},
		complete:function(XMLHttpRequest, textStatus){
			$("#historybtn").html("<a>加载历史记录</a>").attr("onclick","loadMsg()");
		},
		success:function(resultMap){
			if(resultMap.status == 0 && resultMap.status == '0'){
				var dataList = resultMap.dataList;
				for(var k=0;k<dataList.length;k++){
					var temp= getShowHtml(dataList[k]);
		        	$('#infoul').prepend(temp);
				}
				if(dataList.length!=pageSize || pageNo==resultMap.page.totalPages){
					$("#historybtn").hide();
				}
				pageNo+=1;
			} 
			$("body").scrollTop($("body")[0].scrollHeight);
			$("#talkbody").scrollTop($("#talkbody ul")[0].scrollHeight);
		}
	})
}

function getShowHtml(data){
	data.times = "";
	data.divonclick = "";
	if(data.createid==userInfo.userid){
		var ismsg=0;
		var scound=0;
		data.voicestatus = '';
		if(data.type=="1"){//文字
			if(isReplaceBHTML){
				data.content=RongIMLib.RongIMEmoji.symbolToEmoji(data.content);
			}else{
				data.content=RongIMLib.RongIMEmoji.symbolToHTML(data.content);
			}
		}else if(data.type=="2"){//图片
			ismsg=1;
			
			var noneimg = new Image();
			noneimg.onload=function(){
				var newheight=96;
				var newwidth=96;
				
				var nonewidth=noneimg.width;
				var noneheight=noneimg.height;
				if(nonewidth!=0 && noneheight!=0 && noneheight>nonewidth){
					newwidth=48;
				}
				document.getElementById(noneimg.src).width=newwidth;
				document.getElementById(noneimg.src).height=newheight;
				document.getElementById(noneimg.src).style.display="block";
			}
			noneimg.src=data.content;
			data.content="<img id=\""+data.content+"\" style=\"display:none;\" width=\"96\" height=\"96\" src=\""+data.content+"\" onclick=showbigimg(this)>";
		}else if(data.type=="3"){//语音
			
			if(data.type=="3"){
				scound=JSON.parse(data.content).times*1.6+40;
				/*if(scound>75){
					scound=75;
				}else if(scound<10){
					scound=10;
				}*/
			}
			
			data.times = JSON.parse(data.content).times;
			data.content = JSON.parse(data.content).content; 
			data.times = "<div class='time_r'>"+data.times+"″</div>"+data.voicestatus;
			data.divonclick = 'onclick=play_voice_massage(this,\"'+data.content+'\",\"'+data.recordid+'\")';
			data.content='<img src="../appcssjs/images/msg/ico_sound2.png" width="20">';
		}
		temp="<li>"+
	        	"<div class=\"user_r\" ><img  src=\""+projectpath+data.createheadimage+"\"  onerror=this.src='"+hrefurl+"/app/appcssjs/images/defaultheadimage.png'></div>"+
	            "<br><div class=\"msg_r\" "+data.divonclick+" style=\"top:-15px;color:white;background:#5EAAF8;"+(scound!=0?("width:"+scound+"px;"):"")+(ismsg==1?"padding:0px;background:none;":"")+"\">"+data.content+"</div>"+data.times+"<div class=\"clear\"></div></li>";
		return temp;
	}
	var ismsg=0;
	var scound=0;
	if(data.voicestatus==0){
		data.voicestatus = '<div class="read_l" id="'+data.recordid+'">未读</div>';
	}else{
		data.voicestatus = '';
	}
	if(data.type=="1"){//文字
		if(isReplaceBHTML){
			data.content=RongIMLib.RongIMEmoji.symbolToEmoji(data.content);
		}else{
			data.content=RongIMLib.RongIMEmoji.symbolToHTML(data.content);
		}
	}else if(data.type=="2"){//图片
		ismsg=1;
		var noneimg = new Image();
		
		
		noneimg.onload=function(){
			var newheight=96;
			var newwidth=96;
			
			var nonewidth=noneimg.width;
			var noneheight=noneimg.height;
			if(nonewidth!=0 && noneheight!=0 && noneheight>nonewidth){
				newwidth=48;
			}
			document.getElementById(noneimg.src).width=newwidth;
			document.getElementById(noneimg.src).height=newheight;
			document.getElementById(noneimg.src).style.display="block";
			
		}
		noneimg.src=data.content;
		data.content="<img id=\""+data.content+"\" style=\"display:none;\" width=\"96\" height=\"96\" src=\""+data.content+"\" onclick=showbigimg(this)>";
	}else if(data.type=="3"){//语音 
		
		if(data.type=="3"){
			scound=JSON.parse(data.content).times*1.6+40;
			/*if(scound>75){
				scound=75;
			}else if(scound<10){
				scound=10;
			}*/
		}
		
		data.times = JSON.parse(data.content).times;
		data.content = JSON.parse(data.content).content; 
		data.times = "<div class='time_l'>"+data.times+"″</div>"+data.voicestatus;
		data.divonclick = 'onclick=play_voice_massage(this,\"'+data.content+'\",\"'+data.recordid+'\")';
		data.content='<img src="../appcssjs/images/msg/ico_sound1.png" width="20">';
	}
	
	
	temp="<li>"+
        	"<div class=\"user_l\" ><img  src=\""+projectpath+data.createheadimage+"\"  onerror=this.src='"+hrefurl+"/app/appcssjs/images/defaultheadimage.png'></div>"+
            "<br><div class=\"msg_l\" "+data.divonclick+" style=\"top:-10px;"+(scound!=0?("width:"+scound+"px;"):"")+(ismsg==1?"padding:0px;background:none;":"")+"\">"+data.content+"</div>"+data.times+"<div class=\"clear\"></div></li>";
	return temp;
}
$(function(){
	loadMsg();
	//显示聊天界面信息
	showThisGroupInfo();
	signChatRecordStatus();
	 
	/*var content_height = $(window).height() - $(".tool_box").height() - $(".head_box").height()-5;
	$("#talkbody").css({"height":content_height+"px"})*/
	
	$("#content").keyup(function(){
		//console.log($(this).text());
		if($(this).text()==null || $(this).text()==""){//选择
			$("img",$(".add.send")).attr("src",'../appcssjs/images/msg/add.png');
		}else{ //发送
			$("img",$(".add.send")).attr("src",'../appcssjs/images/massage/send.png');
		}
	}).change(function(){
		//console.log($(this).text());
		if($(this).text()==null || $(this).text()==""){//选择
			$("img",$(".add.send")).attr("src",'../appcssjs/images/msg/add.png');
		}else{ //发送
			$("img",$(".add.send")).attr("src",'../appcssjs/images/massage/send.png');
		}
	})
	
	document.onkeydown=function(event){
        var e = event || window.event || arguments.callee.caller.arguments[0];
         if(e && e.keyCode==13){ // enter 键
        	 if($("#content").text()==null || $("#content").text()==""){//选择
        		}else{ //发送
        			sendmessage();
        		}
         }
    }; 
	
	toggle_keyboard_voice();
	
	$("#content2").longPress(function(type){
	    if(type=="start"){ //开始录制
	    	stoprecording();
	    	localStorage.voice_times=0;
	    	localStorage.voice_im_filename_url = recordAudioIM(function(time,url){
	    		localStorage.voice_im_filename_url = url;
	    		if(time=="start"){
	    			//正在录制中
	    			$("#content2").css({"background-color":"#D6D6D6"});
	    			$("#content2").attr("m","start");
	    			setTimeout(function(){
	    				if($("#content2").attr("m")=="start"){
	    					stoprecording();
	    				}
	    			},1500)
	    		}else if(time=='end'){
	    			$("#content2").attr("m","end");
	    			//提交
	    			uploadAudioSuccess();
	    		}else if(time<=60){
	    			$("#content2").attr("m","end");
	    			//正在录制中
	    			$("#content2").css({"background-color":"#D6D6D6"});
	    			$("#content2").val("正在 录制");
	    			localStorage.voice_times = time;
	    		}
	    	})
	    }else if(type=="end"){//结束录制
	    	$("#content2").attr("m","end");
	    	$("#content2").css({"background-color":"#ededed"});
	    	stoprecording();
	    	if(localStorage.voice_times>0){
		    	uploadAudioSuccess();
	    	}
	    }
	});
	
	if(isAndroid){
		$("#content").focus(function(){
			setTimeout(function(){
				$("body").scrollTop($("body")[0].scrollHeight);
		        $("#talkbody").scrollTop($("#talkbody ul")[0].scrollHeight);
			},300);
		});
	}
})


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