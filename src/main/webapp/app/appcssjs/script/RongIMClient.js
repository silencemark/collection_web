var usermap = JSON.parse(localStorage.GlobalData).userinfo;
//容云IM初始化
//RongIMClient.init("qd46yzrf44bkf");
RongIMClient.init("kj7swf8o77ux2");
RongIMClient.setConnectionStatusListener({
    onChanged: function (status) {
        switch (status) {
            //链接成功
            case RongIMLib.ConnectionStatus.CONNECTED:
                console.log('链接成功');
                try {
					onloaddata();
				} catch (e) {
					 
				}
                break;
            //正在链接
            case RongIMLib.ConnectionStatus.CONNECTING:
                console.log('正在链接');
                break;
            //重新链接
            case RongIMLib.ConnectionStatus.DISCONNECTED:
                console.log('断开连接');
                break;
            //其他设备登录
            case RongIMLib.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT:
                console.log('其他设备登录');
                break;
              //网络不可用
            case RongIMLib.ConnectionStatus.NETWORK_UNAVAILABLE:
              console.log('网络不可用');
              break;
            }
    }});

 // 消息监听器
 RongIMClient.setOnReceiveMessageListener({
    // 接收到的消息
    onReceived: function (message){
    	/*//更新底部新消息数量
    	RongIMClient.getInstance().getTotalUnreadCount({
    		  onSuccess:function(count){
    			  if(parseInt(count)>0){
    				  $('#messagecount').text(count);
    				  $('#messagecount').show();
    			  }
    		  },
    		  onError:function(error){
    		      // error => 获取总未读数错误码。
    		  }
    	});*/
    	$.ajax({
    		type:"post",
    		dataType:"json",
    		url:projectpath+"/app/getUserGroup",
    		data:"userid="+usermap.userid+"&groupid="+message.targetId,
    		success:function(data){
    			if(data.isdisturb==0){
    				if(isiOS){
    					//载入声音文件 
        				$('<audio id="chatAudio"><source src="../appcssjs/voice/sm.mp3" type="audio/mpeg"></audio>').appendTo('body');
    			    	//播放
    			    	$('#chatAudio')[0].play();
    				} 
    			}
    		}
    	})
        // 判断消息类型
        switch(message.messageType){
            case RongIMClient.MessageType.TextMessage:
                   // 发送的消息内容将会被打印
            	
            	 	try {
            	 		getMessage(message);					
					} catch (e) {
						try {
							onloaddata();
						} catch (e) {
							 
						}
					}
                console.log(message.content.content);
                break;
            case RongIMClient.MessageType.VoiceMessage:
                // 对声音进行预加载                
                // message.content.content 格式为 AMR 格式的 base64 码
                RongIMLib.RongIMVoice.preLoaded(message.content.content);
                break;
            case RongIMClient.MessageType.ImageMessage:
            	getMessage(message);
                // do something...
                break;
            case RongIMClient.MessageType.DiscussionNotificationMessage:
                // do something...
                break;
            case RongIMClient.MessageType.LocationMessage:
                // do something...
                break;
            case RongIMClient.MessageType.RichContentMessage:
                // do something...
                break;
            case RongIMClient.MessageType.DiscussionNotificationMessage:
                // do something...
                break;
            case RongIMClient.MessageType.InformationNotificationMessage:
                // do something...
                break;
            case RongIMClient.MessageType.ContactNotificationMessage:
                // do something...
                break;
            case RongIMClient.MessageType.ProfileNotificationMessage:
                // do something...
                break;
            case RongIMClient.MessageType.CommandNotificationMessage:
                // do something...
                break;
            case RongIMClient.MessageType.CommandMessage:
                // do something...
                break;
            case RongIMClient.MessageType.UnknownMessage:
                // do something...
                break;
            default:
                // 自定义消息
                // do something...
        }
    }
});
 
 
 
function getToken(){
	$.ajax({
		type:"post",
		dataType:"json",
		url:projectpath+"/app/getToken",
		data:"userid="+usermap.userid+"&realname="+usermap.realname+"&headimage="+usermap.headimage,
		success:function(data){
			// 初始化。
			var token = data.tokenid;

			// 连接融云服务器。
			RongIMClient.connect(token, {
		        onSuccess: function(userId) {
		          console.log("Login successfully." + userId);
		          try {
		        	  getinfodetail();						
				} catch (e) {
					try {
						onloaddata();
					} catch (e) {
						 
					}
				}
		        },
		        onTokenIncorrect: function(e) {
		          console.log('token无效'+e);
		          //setTimeout(getToken,1000);
		        },
		        onError:function(errorCode){
		              var info = '';
		              switch (errorCode) {
		                case RongIMLib.ErrorCode.TIMEOUT:
		                  info = '超时';
		                  break;
		                case RongIMLib.ErrorCode.UNKNOWN_ERROR:
		                  info = '未知错误';
		                  break;
		                case RongIMLib.ErrorCode.UNACCEPTABLE_PaROTOCOL_VERSION:
		                  info = '不可接受的协议版本';
		                  break;
		                case RongIMLib.ErrorCode.IDENTIFIER_REJECTED:
		                  info = 'appkey不正确';
		                  break;
		                case RongIMLib.ErrorCode.SERVER_UNAVAILABLE:
		                  info = '服务器不可用';
		                  break;
		              }
		              console.log(errorCode);
		            }
		      });      
			     
		}
	})
}
getToken();
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
				$('#messagecount').text(data.count);
				$('#messagecount').show(); 
			}else{
				$('#messagecount').hide(); 
			}
		}
	})
}