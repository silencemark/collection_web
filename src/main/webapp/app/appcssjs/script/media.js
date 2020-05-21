/**
 * android终端
 */
var isAndroid = (navigator.userAgent).indexOf('Android') > -1;

/**
 * ios终端
 */
var isiOS = !!(navigator.userAgent).match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);

//生成初始录音文件路径
window.fileVideoSrc =null;
//var videoSrc = new Date().getTime()+".aac";
var videoSrc = "test.aac";
if(isAndroid){
	videoSrc = new Date().getTime()+".aac";
}
/**
 * 生成录音文件路径
 */
function getVideoSrc(){
	videoSrc = new Date().getTime()+".aac";
	if(isAndroid){
		videoSrc = new Date().getTime()+".aac";
	}
	if(isiOS){
		document.addEventListener("deviceready", function(){
			window.requestFileSystem(LocalFileSystem.PERSISTENT, 0,
					function(fileSystem) {
						var fileTransfer = new FileTransfer();
						fileTransfer.download(projectpath+"/test.aac",
						cordova.file.documentsDirectory + '/'+videoSrc, function(
								entry) {
							console("success:"+JSON.stringify(entry)); 
						}, function(error) {
							console("errir:"+error);
						});
					});
		}, true);
	}  
}
getVideoSrc();
var success = function(p){ };
var fail = function(){ }; 
 
	// 音频播放器
	var my_media = null;
	var mediaTimer = null;
	// 播放音频
	function playAudio(src,backCall) {
		/*if(typeof(Media) != "object"){
			alert("缺少插件");
			return;
		}*/
		// 从目标播放文件创建Media对象
		my_media = new Media(src, onSuccess, onError);
		
		// 播放音频
		my_media.play();

		// 每秒更新一次媒体播放到的位置
		if (mediaTimer == null) {
			mediaTimer = setInterval(function() {
				// 获取媒体播放到的位置
				my_media.getCurrentPosition(
					// 获取成功后调用的回调函数
					function(position) {
						if(position<0){
							clearInterval(mediaTimer);
						}
						if(typeof(backCall)=="function"){
							try {
								backCall(parseInt(position))
							} catch (e) {
								 
							}
						}
					},
					// 发生错误后调用的回调函数
					function(e) {
						console.log("Error getting pos=" + e);
						setAudioPosition("Error: " + e);
					}
				);
			}, 1000);
		}
	}

	function playAudiotwo(src) {
		// 播放音频
		my_media.play();

		// 每秒更新一次媒体播放到的位置
		if (mediaTimer == null) {
			mediaTimer = setInterval(function() {
				// 获取媒体播放到的位置
				my_media.getCurrentPosition(
					// 获取成功后调用的回调函数
					function(position) {
						if (position > -1) {
							setAudioPosition((position) + " sec");
						}
					},
					// 发生错误后调用的回调函数
					function(e) {
						console.log("Error getting pos=" + e);
						setAudioPosition("Error: " + e);
					}
				);
			}, 1000);
		}
	}
	// 暂停音频播放       
	function pauseAudio() {
		if (my_media) {
			my_media.pause();
		}
	}

	// 停止音频播放 
	function stopAudio() {
		if (my_media) {
			my_media.stop();
		}
		clearInterval(mediaTimer);
		mediaTimer = null;
	}

	// 创建Media对象成功后调用的回调函数       
	function onSuccess() {
		//alert("playAudio():Audio Success");
	}

	// 创建Media对象出错后调用的回调函数       
	function onError(error) {
		/*alert('code: '    + error.code    + '\n' + 
			'message: ' + error.message + '\n');*/
	}
 
	
	var recInterval =null;
	var mediaRec=null;
	// 录制音频
	function recordAudio(backCall) {
		try{
			clearInterval(recInterval);
			recInterval =null;
		}catch(e){} 
		window.fileVideoSrc =null;
		var rootDic = app_file_url+"/media/";
		window.fileVideoSrc = rootDic+videoSrc;
		if(isiOS){  
				window.fileVideoSrc = "documents://"+videoSrc;
				if(typeof(backCall) == "function"){
					backCall('start',window.fileVideoSrc);
				}
		    	mediaRec = new Media("documents://"+videoSrc);
				//录制开始
			    mediaRec.startRecord();
			    // 60秒钟后停止录制
				var recTime = 0;
				recInterval=setInterval(function(){
					recTime = recTime + 1;
					//setAudioPosition1(recTime + " sec");
					if (recTime >= 60) {
						if(typeof(backCall) == "function"){
							backCall('end',window.fileVideoSrc);
						}
						clearInterval(recInterval); 
						recInterval = null;
					}else{
						if(typeof(backCall) == "function"){
							backCall(recTime,window.fileVideoSrc);
						} 
					}
				}, 1000);
			 
		}else{
				if(typeof(backCall) == "function"){
					backCall('start',window.fileVideoSrc);
				}
				mediaRec= new Media(window.fileVideoSrc);
				// 开始录制音频
				mediaRec.startRecord(); 
				// 60秒钟后停止录制
				var recTime = 0;
				recInterval=setInterval(function(){
					recTime = recTime + 1; 
					if (recTime >= 60) {
						if(typeof(backCall) == "function"){
							backCall('end',window.fileVideoSrc);
						}
						clearInterval(recInterval); 
						recInterval = null;
					}else{
						if(typeof(backCall) == "function"){
							backCall(recTime,window.fileVideoSrc);
						} 
					}
				}, 1000);
			
		}	
		return window.fileVideoSrc;
	}
	
	// 录制音频
	function recordAudioIM(backCall) {
		var begin = false;
		try{
			clearInterval(recInterval);
		}catch(e){}
	
		recInterval_bg=setInterval(function(){
			if(begin){
				console.log(new Date())
				console.log(recInterval)
				if(recInterval==null){
					console.log("结束");
					$(".recordAudio_class").remove();
					clearInterval(recInterval_bg);
				} 
			}else{
				begin = true;
				console.log("开始");
				$("body").append('<div class="recordAudio_class" style=" bottom:100px;   position: fixed;    width: 100%;    margin: auto;"><div style="    width: 50%;    margin: auto;    border: 1px solid #ddd;   background-color: #ddd;    border-radius: 10px;  height: 125px;    padding-top: 15px;"><img src="../appcssjs/images/massage/ly.png" style="    margin: auto;    display: block;"><span style="    margin: auto;width: 100%;text-align: center;display: block;margin-top: 10px;" class="text">正在录制</span></div></div>')
			}  
		}, 500);
	    window.fileVideoSrc =null;  
		var rootDic = app_file_url+"/media/";
		window.fileVideoSrc = rootDic+videoSrc;
		if(isiOS){ 
		 
				window.fileVideoSrc = "documents://"+videoSrc;
				if(typeof(backCall) == "function"){
					backCall('start',window.fileVideoSrc);
				}
		    	mediaRec = new Media("documents://"+videoSrc);
				//录制开始
			    mediaRec.startRecord();
			    // 60秒钟后停止录制
				var recTime = 0;
				recInterval=setInterval(function(){
					recTime = recTime + 1;
					//setAudioPosition1(recTime + " sec");
					if (recTime >= 60) {
						if(typeof(backCall) == "function"){
							backCall('end',window.fileVideoSrc);
						}
						clearInterval(recInterval); 
						recInterval = null;
					}else{
						if(typeof(backCall) == "function"){
							backCall(recTime,window.fileVideoSrc);
						} 
					}
				}, 1000);
			 
		}else{
				if(typeof(backCall) == "function"){
					backCall('start',window.fileVideoSrc);
				}
				mediaRec= new Media(window.fileVideoSrc);
				// 开始录制音频
				mediaRec.startRecord(); 
				// 60秒钟后停止录制
				var recTime = 0;
				recInterval=setInterval(function(){
					recTime = recTime + 1; 
					if (recTime >= 60) {
						if(typeof(backCall) == "function"){
							backCall('end',window.fileVideoSrc);
						}
						clearInterval(recInterval); 
						recInterval = null;
					}else{
						if(typeof(backCall) == "function"){
							backCall(recTime,window.fileVideoSrc);
						} 
					}
				}, 1000);
			
		}	
		return window.fileVideoSrc;
	}
	 
	function stoprecording() {
		try {
			clearInterval(recInterval);
			recInterval = null;
		} catch (e) {
		}
		try {
			mediaRec.stopRecord();
		} catch (e) {
		}
	}

	// 设置音频播放位置
	function setAudioPosition1(position) {
		document.getElementById('audio_position1').innerHTML = position;
	}
	
	function uuid() {
		var s = [];
		var hexDigits = "0123456789abcdef";
		for (var i = 0; i < 36; i++) {
		s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
		}
		s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
		s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
		s[8] = s[13] = s[18] = s[23] = "-";

		var uuid = s.join("");
		return uuid;
		}
	 
	function uploadAudio(imageURI,type,backCall) {

		if(isiOS){
			var rootDic=cordova.file.documentsDirectory;
			imageURI= rootDic+"/"+videoSrc;
		} 
		var options = new FileUploadOptions();
		
		options.fileKey = "fileAddPic";//用于设置参数，对应form表单里控件的name属性，这是关键，废了一天时间，完全是因为这里，这里的参数名字，和表单提交的form对应

		options.fileName = imageURI.substr(imageURI.lastIndexOf('/') + 1);
		if(options.fileName!=null && options.fileName.lastIndexOf(".")>0){
			
		}else{
			if(isAndroid){
				options.fileName = imageURI.substr(imageURI.lastIndexOf('/') + 1)+".aac";
			}else{
				options.fileName = imageURI.substr(imageURI.lastIndexOf('/') + 1)+".aac";
			}
		}
		//如果是图片格式，就用image/jpeg，其他文件格式上官网查API

		//options.mimeType = "audio/mpeg";

		options.mimeType = "multipart/form-data";//这两个参数修改了，后台就跟普通表单页面post上传一样 enctype="multipart/form-data"

		//这里的uri根据自己的需求设定，是一个接收上传图片的地址

		var uri = encodeURI(projectpath+"/file/upload");

//		alert(imageURI);

//		alert(uri);

		options.chunkedMode = false;
		
		var params = new Object();

		var USER = JSON.parse(localStorage.GlobalData).userinfo;
		params.FileDirectory = USER.companyid;
		params.userid = USER.userid;
		params.type = type;

		 options.params = params;

		var ft = new FileTransfer();

		ft.upload(imageURI, uri, function(data){
			if(typeof(backCall) == "function"){
				backCall({success:true,data:{
					url:JSON.parse(data.response)[0].httpUrl,
					size:JSON.parse(data.response)[0].fileSize
				}});
			}
		}, function(data){
			if(typeof(backCall) == "function"){
				backCall({success:false,msgdata:data});
			}
		}, options);

	}
	
	
	//长按事件
	$.fn.longPress = function(fn) {
	    var timeout = fn("touchend");
	    var $this = this;
	    for(var i = 0;i<$this.length;i++){
	        $this[i].addEventListener('touchstart', function(event) {
	        	fn("start");
	        	event.preventDefault();
	            }, false);
	        $this[i].addEventListener('touchend', function(event) {
	        	fn("end");
	            event.preventDefault();
	            }, false);
	        $this[i].addEventListener('touchmove', function(event) {
	        	fn("move");
	        	event.preventDefault();
	            }, false);
	    }
	}