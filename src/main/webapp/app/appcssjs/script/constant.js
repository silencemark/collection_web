//项目资源路径
var hrefurl = location.href.substring(0, location.href.indexOf("/app/"));
// 项目域名

//var projectpath="http://localhost:8080";
var projectpath="https://www.goaragekit.cn";

/**
 * android终端
 */
var isAndroid = (navigator.userAgent).indexOf('Android') > -1;

/**
 * ios终端
 */
var isiOS = !!(navigator.userAgent).match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);


/**
 * ios引用不同的样式
 */
if(isiOS){
	var links = $('link[type="text/css"]');
	$.each(links,function(i,item){
		var href = $(item).attr("href");
		if(href.indexOf("/appcssjs/style/") > 0){
			var cssname = href.substring(href.lastIndexOf("/")+1,href.length);
			$(item).attr("href","../appcssjs/style/ios_"+cssname);
		}
	});
}

document.write("<script language=javascript src='../../cordova.js'></script>");
document.write("<script language=javascript src='../../cordova_plugins.js'></script>");
document.write("<script>window['SCHEMETYPE'] = 'http';</script>");
document.write("<script language=javascript src='http://cdn.ronghub.com/RongIMLib-2.2.3.min.js'></script>");
document.write("<script language=javascript src='http://cdn.ronghub.com/RongEmoji-2.2.3.min.js'></script>");
document.write("<script language=javascript src='../appcssjs/script/RongIMClient.js'></script>");
document.write("<script language=javascript src='../appcssjs/script/index.js'></script>");
document.write("<script language=javascript src='../appcssjs/script/media.js'></script>");
/**
 * 图片压缩的js
 */
document.write("<script language=javascript src='../appcssjs/script/mobileFix.mini.js?v=ab833c8'></script>");
document.write("<script language=javascript src='../appcssjs/script/exif.js?v=b26c6d5'></script>");
document.write("<script language=javascript src='../appcssjs/script/lrz.js?v=7bc1191'></script>");
document.write("<script language=javascript src='../appcssjs/script/formdata.js'></script>");

var app_file_url = hrefurl;// "file:///sdcard/com.cateringmaster.app";

var pictureSource; // picture source

var destinationType; // sets the format of returned value
var checkinterval;
$(function() {
	//if(location.href.indexOf("/login/index.html")>0){
	if(false){
		$.ajax({
			type:'post',
			dataType:'json',
			data:{
				version:0
			},
			async: false,
			url:projectpath+'/app/getLaunched',
			success:function(data){
				if(data.success){
					console.log(data);
					var app_cache = localStorage.APP_CACHE;
					if(!app_cache){
						app_cache = "{}";
					}
					app_cache = JSON.parse(app_cache); 
					if(!app_cache.Launched || app_cache.Launched.version!=data.version){
						app_cache.Launched = data; 
						app_cache.Launched.isqidong = false;
						localStorage.APP_CACHE = JSON.stringify(app_cache);
						location.href=hrefurl+"/app/login/banner.html";
					}
					
				}
			}
		})
	}
	checklogintoken();
	//checkinterval = setInterval("checklogintoken()", 20000);
	if (isiOS) {
		console.log("This is IOS");
		// 首页
		$(".head_none2").css({
			"height" : "31px",
			"width" : "100%",
			"background" : "#fff",
			"margin" : "0px"
		});
		// 其他
		$(".head_box").css({
			"height" : "71px"
		});
		$(".head_box .ico_back,.ico_info,.ico_sys,.ico_cal").css({
			"top" : "31px"
		});
		$(".head_box .name").css({
			"margin-top" : "23px"
		});
		$(".head_box .link,.link_yellow").css({
			"top" : "30px"
		});
		$(".head_box .find").css({
			"margin-top" : "21px"
		});

		$(".head_none").css({
			"height" : "76px"
		});
	} else {
		console.log("This is Android");
	}
	
	
	$('input[class="bg"]').focus(function(){
		$(this).css({"background-position":"4% 50%","text-align":"left"});
		$(this).attr("placeholder","");
	});
	$('input[class="bg"]').blur(function(){
		if($(this).val() == ""){
			$(this).css({"background-position":"46% 50%","text-align":"center"});
			$(this).attr("placeholder","搜索");
		}
	});
	
	//监听每个页面的转发和评论按钮
	$("input[onclick='initaddcomment();'],input[onclick='comment_opent_close()']").click(function(){
		$("#content").attr("placeholder","");
		$("#commentdiv input[onclick='addComment();']").attr("value","发表");
		$("#commentdiv input[onclick='addcomment();']").attr("value","发表");
	});
	
	$(".zf[onclick='initforwarding();'],.zf[onclick='forwarding_opent_close()']").click(function(){
		$("#content").attr("placeholder","写评论.意见或建议");
		$("#commentdiv input[onclick='addComment();']").attr("value","转发");
		$("#commentdiv input[onclick='addcomment();']").attr("value","转发");
	});
})

document.addEventListener('deviceready', onDeviceReady, false);
document.addEventListener('resume', PushInto, false);
if (isiOS) {
	document.addEventListener("jpush.openNotification", PushInto, false);
	document.addEventListener("jpush.receiveNotification",onReceiveNotification, false);
}

function onReceiveNotification(event) {
//	alert("registrationid 接收推送信息的监听");
	try {
		var alertContent;
		if (device.platform == "Android") {
			alertContent = event.alert;
		} else {
			alertContent = event.aps.alert;
		}
		location.href = hrefurl + "/app" + event.URL;
	} catch (exception) {
		console.log(exception)
	}
}

/**
 * 推送跳转
 */
function PushInto(data) {
	/**
	 * 跳转或处理相关业务
	 */
	function Business(data) {
		// 普通推送
		if (data.ORDINARY_PUSH == "ORDINARY_PUSH") {
			location.href = hrefurl + "/app" + data.URL;
			// 静默推送
		} else if (data.SILENCE_PUSH == "SILENCE_PUSH") {
			// alert("进入静默："+JSON.stringify(data));
			// 更新权限
			if (data.operation == "powerupdate") {
				// alert("调 查询权限方法");
				// 查询现在已有的权限信息
				powerUpdate(data.userid);
			}

		}
	}

	if (isAndroid) {
		/**
		 * 刷新页面才会触发，一个页面只触发一次，用户在启动应用的的情况下
		 */
		window.plugins.jPushPlugin.getPushData(function(data) {
			if (data != '{}' && location.href.indexOf("login/index.html") > 0) {
				Business(JSON.parse(data).extras["cn.jpush.android.EXTRA"]);
			}
		});
		/**
		 * 一个页面可以多次触发，用户在页面没有刷新的情况下
		 */
		window.plugins.jPushPlugin.openNotificationInAndroidCallback = function(
				data) {
			Business(data.extras);
		};

		/**
		 * 在接收到推送消息时触发这个方法
		 */
		window.plugins.jPushPlugin.receiveMessageInAndroidCallback = function(
				data) {
//			alert("接收到消息了" + JSON.stringify(data));
			if (data.extras.ORDINARY_PUSH == "ORDINARY_PUSH") {
				try {
//					alert("进入  接收推送消息时候推送的消息");
					// 更新代办事项
					getTodoList();
				} catch (e) {
					// TODO: handle exception
					console.log('not in index page');
				}
			}
		};

	} else if (isiOS) {
		Business(data);
	}

}

// 刷新用户所拥有的权限
function powerUpdate(userid, powertime) {
	$.ajax({
		url : projectpath + "/app/getUserPowerInfo",
		type : "post",
		data : "userid=" + userid,
		success : function(data) {
			if (data != null) {
				if (data.status == 0 || data.status == '0') {
					var powerMap = data.powerMap;
					var userInfo = {};
					// 取出缓存中的权限信息
					JianKangCache.getGlobalData('userinfo', function(data) {
						userInfo = data;
					});
					// 给用户权限重新赋值
					userInfo.powerMap = powerMap;
					userInfo.updatepowertime = powertime;
					// 放回到全局缓存中去
					JianKangCache.setGlobalData("userinfo", userInfo);
					// alert("权限刷新成功")
				}
			}
		}
	});
}
function onDeviceReady() {
	pictureSource = navigator.camera.PictureSourceType;
	destinationType = navigator.camera.DestinationType;
	try {
		JPush.init(); // 初始化
		// setJpushId(); //设置别名
		PushInto(); // 设置消息接收方法
	} catch (e) {
	}
}

// 设置userid 推送 jpushid：传入用户id
function setJpushId(jpushid) {
	if (!jpushid) {
		jpushid = JSON.parse(localStorage.GlobalData).userinfo.userid;
	}
	if (isAndroid || isiOS) {
     	try{
     		JPush.getRegistrationID(function(id) {
    			setRegistrationIdByUserId(jpushid, id);
    		})
     	}catch(e){console.log(e)}
//		JPush.setAlias("");
//		JPush.setAlias(jpushid);
	}
}

/**
 * 给用户关联设备id
 * 
 * @param userid
 * @param registrationid
 */
function setRegistrationIdByUserId(userid, registrationid) {
	$.ajax({
		url : projectpath + "/app/updateRegistrationIdByUserId",
		data : "userid=" + userid + "&registrationid=" + registrationid,
		type : "post",
		async : false,
		success : function(data) {
//			alert("设置RegistrationId：" + data);
		}
	});
}

var userinfo = {};

/**
 * 替换模板
 * 
 * @param tmpl
 * @param beanpatrollog_detail.html
 * @returns
 */
function replaceTemp(tmpl, bean) {
	for ( var i in bean) {// 用javascript的for/in循环遍历对象的属性
		if (bean.hasOwnProperty(i)) {
			var regExp = new RegExp("{[ ]*" + i + "[ ]*}", "gmi");
			while (true) {
				tmpl = tmpl.replace(regExp, bean[i]);
				if (!regExp.test(tmpl)) {
					break;
				}
			}

		}
	}
	return tmpl;
}

/**
 * 返回上一页
 */
function goBackPage() {
	window.history.go(-1);
}

// 自动封装form
function getFormBean(formID) {
	var tempData = $('#' + formID).serializeArray();
	var item = new Object();
	if (null != tempData && '' != tempData && typeof (tempData) != 'undefined') {
		$.each(tempData, function(i, field) {
			item[field.name] = field.value;
		});
	}
	return item;
}
/**
 * 获取地址栏参数
 * 
 * @param name
 * @returns
 * @author silence
 */
function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); // 构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg); // 匹配目标参数
	if (r != null)
		return unescape(r[2]);
	return null; // 返回参数值
}

/**
 * 转发用户
 * 
 * @param name
 * @returns
 * @author silence
 */
function userForword(companyid, resourceid, receiveid, createid, resourcetype) {
	$.ajax({
		type : 'post',
		dataType : 'json',
		url : projectpath + '/app/forward',
		data : 'companyid=' + companyid + '&resourceid=' + resourceid
				+ '&receiveids=' + receiveid + '&createid=' + createid
				+ '&resourcetype=' + resourcetype,
		success : function(data) {
			if (data.status == 0) {
				// swal({
				// title : "",
				// text : "转发成功",
				// type : "success",
				// showCancelButton : false,
				// confirmButtonColor : "#ff7922",
				// confirmButtonText : "确认",
				// cancelButtonText : "取消",
				// closeOnConfirm : false
				// }, function(){
				// });
			}
		}
	})
}
/**
 * 转发用户
 * 
 * @param name
 * @returns
 * @author silence
 */
function readstatus(orderid, userid) {
	$.ajax({
		type : 'post',
		dataType : 'json',
		url : projectpath + '/app/updateIsread?resourceid=' + orderid
				+ '&isread=1&receiveid=' + userid,
		success : function(data) {
		}
	})
}

function Num(obj) {
	obj.value = obj.value.replace(/[^\d.]/g, ""); // 清除"数字"和"."以外的字符
	obj.value = obj.value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
	obj.value = obj.value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
	obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
			".");
	obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); // 只能输入两个小数
}

/**
 * 修改信息的阅读状态
 * 
 * @param url
 * @param resourceid
 * @param isread
 * @param type
 */
function intoDetail(url, resourceid, isread, type) {
	location.href = url + "&type=" + type;
}

function intoDetailUpdateRead(userid, resourceid, type) {
	if(userid == ""){
		userid = userInfo.userid;
	}
	var param = new Object();
	param.resourceid = resourceid;
	param.receiveid = userid;
//  param.resourcetype = type;
	param.isread = 1;
	// 标记为已读
	$.ajax({
		url : projectpath + "/app/updateIsread",
		type : "post",
		data : param,
		success : function(data) {
			// alert(JSON.stringify(data));
		}
	});
}

function getNowTime() {
	var date = new Date();
	this.year = date.getFullYear();
	this.month = date.getMonth() + 1;
	this.date = date.getDate();
	this.month = this.month < 10 ? "0" + this.month : this.month;
	this.date = this.date < 10 ? "0" + this.date : this.date;
	this.hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
	this.minute = date.getMinutes() < 10 ? "0" + date.getMinutes() : date
			.getMinutes();
	var currentTime = this.year + "-" + this.month + "-" + this.date + " "
			+ this.hour + ":" + this.minute;
	return currentTime;
}

// 公共的查询方法
function requestPost(url, param, callback) {
	$.ajax({
		url : url,
		type : "post",
		data : param,
		beforeSend : function() {
			loading();
		},
		success : function(resultMap) {
			$('#Pagination').html(resultMap.pager);
			callback(resultMap);
		},
		complete : function() {
			closeLoading();
		},
		error : function(e) {

		}
	});
}

// 显示加载更多
function loading() {
	var load = '<div id="load_mask" class="div_mask" style="opacity:0;"></div>'
			+ '<div id="load_loading" class="loading"><img src="../userbackstage/images/public/loading.gif" width="360" height="200" /></div>';
	$("body").after(load);
}

// 移除加载更多的样式
function closeLoading() {
	$('#load_mask').remove();
	$('#load_loading').remove();
}

/**
 * 保存pc使用方的组织
 * 
 * @param name
 * @returns
 * @author silence
 */
function changeOrganizeid(organizeid, organizename) {
	$.ajax({
		type : 'post',
		dataType : 'json',
		url : projectpath + '/pc/changeOrganizeUser',
		data : 'organizeid=' + organizeid + '&organizename=' + organizename,
		success : function(data) {
		}
	})
}
function changeIsReadAll(userid, resourceid, resourcetype) {
	$.ajax({
		type : 'post',
		dataType : 'json',
		url : projectpath + '/app/changeIsReadAll',
		data : 'userid=' + userid + '&resourcetype=' + resourcetype
				+ "&resourceid=" + resourceid,
		success : function(data) {

		}
	})
}
function pushMessage(userid, title, url) {
	$.ajax({
		type : 'post',
		dataType : 'json',
		url : projectpath + '/app/pushMessage',
		data : 'userid=' + userid + '&title=' + title + "&url=" + url,
		success : function(data) {
		}
	})
}

function changeIsread(receiveid, resourceid) {
	$.ajax({
		type : 'post',
		dataType : 'json',
		url : projectpath + '/app/updateIsread?receiveid=' + receiveid
				+ '&isread=0&resourceid=' + resourceid,
		success : function(data) {
		}
	})
}

function capturePhoto() {

	navigator.camera.getPicture(uploadPhoto, onFail, {

		quality : 50,

		destinationType : navigator.camera.DestinationType.FILE_URI,// 这里要用FILE_URI，才会返回文件的URI地址

		// destinationType : Camera.DestinationType.DATA_URL,

		sourceType : Camera.PictureSourceType.CAMERA,

		allowEdit : true,

		encodingType : Camera.EncodingType.JPEG,

		popoverOptions : CameraPopoverOptions,

		saveToPhotoAlbum : true

	});

}

function capturePhotoEdit() {

	navigator.camera.getPicture(uploadPhoto, onFail, {

		quality : 50,

		allowEdit : true,

		destinationType : destinationType.DATA_URL,

		saveToPhotoAlbum : true

	});

}

function getPhoto(source,data) {
	
	function uploadPhoto2(imageURI){
		uploadPhoto(imageURI,data);
	}
	
	navigator.camera.getPicture(uploadPhoto2, onFail, {

		quality : 50,

		destinationType : destinationType.FILE_URI,// 这里要用FILE_URI，才会返回文件的URI地址

		sourceType : source

	});

}

function onFail(message) {

	// alert('Failed because: ' + message);

}

/**
 * ajax提交form表单，做了图片压缩了
 * @param imageURI
 * @param data
 */
function uploadPhoto(imageURI,data) {
	// 也可以传入图片路径：lrz('../demo.jpg', ...
    lrz(imageURI, {
        before: function() {
            console.log('压缩开始');
        },
        fail: function(err) {
            console.error(err);
        },
        always: function() {
            console.log('压缩结束');
        },
        done: function (results) {
	        // 你需要的数据都在这里，可以以字符串的形式传送base64给服务端转存为图片。
	        //console.log(results);

        	data.fileName = imageURI.substr(imageURI.lastIndexOf('/') + 1);
        	if (data.fileName != null && data.fileName.lastIndexOf(".") > 0) {
        	} else {
        		data.fileName = imageURI.substr(imageURI.lastIndexOf('/') + 1)+ ".jpeg";
        	}
	        FormDataAjax(results.base64,data,function(data1){
	        	var imageURI = JSON.parse(data1)[0].httpUrl;
	        	var realurl = imageURI.substring(imageURI.indexOf(":80") + 3 , imageURI.length);
	        	var fileSize = JSON.parse(data1)[0].fileSize;
	        	onPhotoDataSuccess(realurl, fileSize);
	            //console.log(data1); 
	        }) 
        }
    });
}

/**
 * 调用原生的上传方式——未做图片压缩
 * @param imageURI
 * @param data
 */
function uploadPhoto_old(imageURI,data) {

	var options = new FileUploadOptions();

	options.fileKey = "fileAddPic";// 用于设置参数，对应form表单里控件的name属性，这是关键，废了一天时间，完全是因为这里，这里的参数名字，和表单提交的form对应

	options.fileName = imageURI.substr(imageURI.lastIndexOf('/') + 1);
	if (options.fileName != null && options.fileName.lastIndexOf(".") > 0) {

	} else {
		options.fileName = imageURI.substr(imageURI.lastIndexOf('/') + 1)
				+ ".jpeg";
	}

	// 如果是图片格式，就用image/jpeg，其他文件格式上官网查API

	options.mimeType = "image/jpeg";

	// options.mimeType = "multipart/form-data";//这两个参数修改了，后台就跟普通表单页面post上传一样
	// enctype="multipart/form-data"

	// 这里的uri根据自己的需求设定，是一个接收上传图片的地址

	var uri = encodeURI(projectpath + "/file/upload");

	// alert(imageURI);

	// alert(uri);

	options.chunkedMode = false;

	var params = new Object();
	var USER = JSON.parse(localStorage.GlobalData).userinfo;
	
	params.FileDirectory = USER.companyid;
	params.userid = USER.userid;
	params.type = data.type;

	options.params = params;

	var ft = new FileTransfer();

	ft.upload(imageURI, uri, win, fail, options);

}
function downLoadQrcode(downloadUrl, callback) {
	// alert("download");
	var filename = downloadUrl.substring(downloadUrl.lastIndexOf("/"),
			downloadUrl.length);
	var relativeFilePath = "cangpinzhencang" + filename; // using an absolute
														// path also does not
														// work
	if (downloadUrl && downloadUrl.indexOf(projectpath) != 0) {
		downloadUrl = projectpath + downloadUrl;
	}
	window.requestFileSystem(LocalFileSystem.PERSISTENT, 0,
			function(fileSystem) {
				// alert(JSON.stringify(fileSystem));
				var fileTransfer = new FileTransfer();
				fileTransfer.download(downloadUrl,
				// The correct path!
				fileSystem.root.toURL() + '/' + relativeFilePath, function(
						entry) {
					// alert("success:"+entry);
					callback({
						success : true,
						entry : entry
					});
				}, function(error) {
					callback({
						success : false,
						entry : entry
					});
				});
			});
}
function downLoadImg(downloadUrl, callback) {
	var filename = downloadUrl.substring(projectpath.length);
	var relativeFilePath = filename; // using an absolute path also does not
										// work
	callback(hrefurl + relativeFilePath);
	try {
		var fileTransfer = new FileTransfer();
		fileTransfer.download(downloadUrl, hrefurl + relativeFilePath);
	} catch (e) {
		// TODO: handle exception
	}
}
function win(r) {
	$("#value").val(r.response);
	var imageURI = JSON.parse(r.response)[0].httpUrl;
	var fileSize = JSON.parse(r.response)[0].fileSize;

	var realurl = imageURI.substring(imageURI.indexOf(":80") + 3,
			imageURI.length);

	onPhotoDataSuccess(realurl, fileSize);

}

function fail(error) {

	console.log("An error has occurred: Code = " + error.code);

	console.log("upload error source " + error.source);

	console.log("upload error target " + error.target);

	console.log("upload error source " + error.source);

	console.log("upload error target " + error.target);

}
function fomartDate(datastr) {
	if (datastr == null || datastr == "undefined") {
		return "";
	} else {
		return datastr.substring(0, 16);
	}

}

function fomatFloatFloor(src, pos) {
	return Math.floor(src * Math.pow(10, pos)) / Math.pow(10, pos);
}

/**
 * 图片的点击方法
 */
function showbigimg(obj) {
	var imgutil = {
		FDIMG : function(dom) {
			var src = $(dom).attr("src");
			var img = new Image();
			img.src = src;
			var width = img.width;
			var height = img.height;// 图片大小
			if (width == 0 && height == 0) {
				return;
			}
			imgutil
					.isPC(function(flg) {
						imgutil
								.alt(function(data) {
									var winHeight = data.height;
									var winWidth = data.width;
									if (flg) {
										imgutil.AutoResizeImage(width, height,
												winWidth, winHeight * 0.98,
												function(o) {

													width = o.w;
													height = o.h;

												});

									} else {
										imgutil.AutoResizeImage(width, height,
												winWidth, winHeight,
												function(o) {

													width = o.w;
													height = o.h;

												});
									}

									var top = ((winHeight - height) / 2);
									var left = ((winWidth - width) / 2);

									var img_model = new Date().getTime()
											+ "img";
									var html = '<div id="win_PMG">'
											+ '<div onclick="$(\'#win_PMG\').remove();" style="width: 100%; height: 100%; opacity:0.8; position: fixed; top:0px; background-color: #333; z-index: 9000">'
											+ '</div>'
											+ '<img onclick="$(\'#win_PMG\').remove();" id="'
											+ img_model
											+ '" src="'
											+ src
											+ '"    align="center" style="position: fixed; top:'
											+ top + 'px; left:' + left
											+ 'px; z-index: 9001; width: '
											+ width + 'px;height: ' + height
											+ 'px">' + '</div>';
									$("body").append(html);

								});

					});

		},
		AutoResizeImage : function(w, h, maxWidth, maxHeight, call) {
			var hRatio;
			var wRatio;
			var Ratio = 1;
			wRatio = maxWidth / w;
			hRatio = maxHeight / h;
			if (maxWidth == 0 && maxHeight == 0) {
				Ratio = 1;
			} else if (maxWidth == 0) {//
				if (hRatio < 1)
					Ratio = hRatio;
			} else if (maxHeight == 0) {
				if (wRatio < 1)
					Ratio = wRatio;
			} else if (wRatio < 1 || hRatio < 1) {
				Ratio = (wRatio <= hRatio ? wRatio : hRatio);
			}
			if (Ratio < 1) {
				w = w * Ratio;
				h = h * Ratio;
			}
			var co = {
				w : w,
				h : h
			};
			call(co);

		},
		alt : function(call) {
			var s = "";
			s += " 网页可见区域宽：" + document.body.clientWidth + "\n";
			s += " 网页可见区域高：" + document.body.clientHeight + "\n";
			s += " 网页可见区域宽：" + document.body.offsetWidth + " (包括边线和滚动条的宽)"
					+ "\n";
			s += " 网页可见区域高：" + document.body.offsetHeight + " (包括边线的宽)" + "\n";
			s += " 网页正文全文宽：" + document.body.scrollWidth + "\n";
			s += " 网页正文全文高：" + document.body.scrollHeight + "\n";
			s += " 网页被卷去的高(ff)：" + document.body.scrollTop + "\n";
			s += " 网页被卷去的高(ie)：" + document.documentElement.scrollTop + "\n";
			s += " 网页被卷去的左：" + document.body.scrollLeft + "\n";
			s += " 网页正文部分上：" + window.screenTop + "\n";
			s += " 网页正文部分左：" + window.screenLeft + "\n";
			s += " 屏幕分辨率的高：" + window.screen.height + "\n";
			s += " 屏幕分辨率的宽：" + window.screen.width + "\n";
			s += " 屏幕可用工作区高度：" + window.screen.availHeight + "\n";
			s += " 屏幕可用工作区宽度：" + window.screen.availWidth + "\n";
			s += " 你的屏幕设置是 " + window.screen.colorDepth + " 位彩色" + "\n";
			s += " 你的屏幕设置 " + window.screen.deviceXDPI + " 像素/英寸" + "\n";
			s += " window的页面可视部分实际高度(ff) " + window.innerHeight + " ";
			/* alert (s); */
			var data = {
				width : window.innerWidth,
				height : window.innerHeight
			}
			call(data);
		},
		isPC : function(call) {// true pc
			var userAgentInfo = navigator.userAgent;
			var Agents = new Array("Android", "iPhone", "SymbianOS",
					"Windows Phone", "iPad", "iPod");
			var flag = true;
			for (var v = 0; v < Agents.length; v++) {
				if (userAgentInfo.indexOf(Agents[v]) > 0) {
					flag = false;
					break;
				}
			}
			call(flag);
		}

	}
	imgutil.FDIMG(obj);
}

/**
 * 关闭大图
 */
function removebigimg() {
	if ($("#show_big_div").length > 0) {
		$("#show_big_div").remove();
	}
	if ($("#mask_show_big_img").length > 0) {
		$("#mask_show_big_img").remove();
	}
}

function changeorganizestore(datacode) {
	if (datacode != undefined && datacode != "undefined" && datacode != "") {
		var paramlist = [];
		var num = datacode.length;
		var j = 0;
		for (var i = 0; i < num; i += 3) {
			if ((num - i) >= 3) {
				var param = new Object();
				param.datacode = datacode.substring(0, (num - i));
				paramlist[j] = param;
				j++;
			}
		}

		if (paramlist.length > 0) {
			var num = paramlist.length - 1;
			for (i = num; i >= 0; i--) {
				$('#organize_' + paramlist[i].datacode).click();
			}
		}
	}
}

var redPoint = '<img src="../appcssjs/images/public/red_point.png" style="width:10px; height:10px; margin-right:8px;">';

// 校验登录
function checklogintoken() {
	var cuurhtml = location.href;
	if (cuurhtml.indexOf('/login/') != -1) {
		if (cuurhtml.indexOf('/login/index.html') == -1) {
			//clearInterval(checkinterval);
			return;
		}
	}
	if (cuurhtml.indexOf('/appraise_add.html') > 0) {
		//clearInterval(checkinterval);
		return;
	}
	var userInfo = {};
	var userid =
	// 取出缓存中的权限信息
	JianKangCache.getGlobalData('userinfo', function(data) {
		// u判断缓存数据是否为空，为空则跳转到登录页面。
		if (data == undefined || data == null || data == "null") {
			//location.href = '../login/login.html';
		}
		userInfo = data;
	});
	var loginToken = userInfo.LOGIN_TOKENKEY;
	$
			.ajax({
				url : projectpath + "/app/checklogintoken",
				type : "post",
				data : {
					userid : userInfo.userid,
					LOGIN_TOKENKEY : loginToken,
					powerlastupdatetime : userInfo.updatepowertime
				},
				success : function(data) {
					// console.log('data='+data);
					if (data != null) {
						if (data.status == 1 || data.status == '1') {
							JianKangCache.setGlobalData("userinfo", null);
							swal(
									{
										title : "",
										text : "<font style=\"color:#ff7922; font-size:18px; margin-left:20px;\">登录失效，请重新登录！</font>",
										type : "warning",
										timer : 500,
										html : true,
										showConfirmButton : false
									}, function() {
										location.href = '../login/login.html';
									});
						} else {
							if (cuurhtml.indexOf('/login/index.html') != -1) {
								if (typeof(isSetRegId)!="boolean" || !isSetRegId) {
									// 设置getRegistrationID
									try{
										JPush.getRegistrationID(function(id) {
											setRegistrationIdByUserId(userInfo.userid, id);
											isSetRegId = true;
										});
									}catch(e){console.log(e)}
								}
							}
							if (data.power == 0 || data.power == '0') {
								// 刷新权限
								powerUpdate(userInfo.userid, data.powertime);
							}
						}
					}
				},
				error : function(e) {
					console.log(e);
				}
			});

}

// 长按事件
$.fn.longPressConstant = function(fn) {
	var timeout = fn("touchend");
	var $this = this;
	for (var i = 0; i < $this.length; i++) {
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
// 录制语音本地地址
localStorage.audio_foldname_url = "";
//录制的语音的外网的地址
localStorage.http_audio_foldname_url = "";
$(function() {
	$(".talk_add")
			.longPressConstant(
					function(type) {
						// 保存录制 的时间
						var recordAudio_time = 0;
						if (type == "start") {
							//添加语音录制样式
							var recordhtml = '<div class="tc_talk" id="appSoundRecord_div">'+
												'<b><img style="margin:0px 25px;" src="../appcssjs/images/public/talk.png">'+
											    '<span class="">正在录音中···</span>'+
											'</div>';
							$('body').after(recordhtml);
							// 录制语音，限时60s
							localStorage.audio_foldname_url = recordAudio(function(
									time, url) {
								// 录制的 语音 本地赋值
								localStorage.audio_foldname_url = url;
								if (time == 'end') {
									// 录制结束之后，保存到页面
									var sound = '<div class="talk" id="recordAudioSound_div_first" recordAudio="" onclick="playRecordAudioSound(this)">语言文件<div class="del" onclick="removeRecordAudioSound(this)">删除</div></div>';
									$(sound).insertBefore(".talk_add");
									// 隐藏语音的添加按钮
									$(".talk_add").hide();
									
									//删除语音录制样式
									$('#appSoundRecord_div').remove();
									
									//上传语音
									uploadAudio(localStorage.audio_foldname_url,9,function(data){
										if(data.success){
											//给语音图标赋值路径
											$('#recordAudioSound_div_first').attr("recordAudio",data.data.url);
											localStorage.http_audio_foldname_url = data.data.url;
										}
									});
									
									event.stopPropagation();
								} else {
									// 录制时间 赋值
									recordAudio_time = time;
								}
							});
						} else if (type == "end") {
							// 停止录制
							stoprecording();
							
							// 录制结束之后，保存到页面
							var sound = '<div class="talk" id="recordAudioSound_div_first" recordAudio="" onclick="playRecordAudioSound(this)">语言文件<div class="del" onclick="removeRecordAudioSound(this)">删除</div></div>';
							$(sound).insertBefore(".talk_add");
							// 隐藏语音的添加按钮
							$(".talk_add").hide();
							
							//删除语音录制样式
							$('#appSoundRecord_div').remove();
							
							//上传语音
							uploadAudio(localStorage.audio_foldname_url,9,function(data){
								if(data.success){
									//给语音图标赋值路径
									$('#recordAudioSound_div_first').attr("recordAudio",data.data.url);
									localStorage.http_audio_foldname_url = data.data.url;
								}
							});
							
							//防止时间冒泡
							event.stopPropagation();
							
						} else if (type == "move") {
							// $("#content").val("move");
						}
					});
})
// 播放 语音
function playRecordAudioSound(item) {
	stopAudio();
	var sound_url = $(item).attr("recordAudio");
	if (sound_url != "") {
		playAudio(sound_url);
	}else{
		swal({   
			title: "",   
			text: "<font style=\"color:#ff7922; font-size:18px; margin-left:20px;\">您录制的语音正在处理中，请稍后···！</font>",
			type:"warning",
			html:true,
			showConfirmButton: true 
		});
	}
}
// 删除录制的语音信息
function removeRecordAudioSound(obj) {
	localStorage.audio_foldname_url = "";
	$(obj).parent().remove();
	$('.talk_add').show();
	event.stopPropagation();
}

// 下载文件
function downLoadFileApp(src) {
	// alert("downLoadFileApp");
	downLoadQrcode(src, function(data) {
		if (data.success) {
			swal({
				title : "",
				text : "文件下载成功",
				type : "success",
				showCancelButton : false,
				confirmButtonColor : "#ff7922",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			}, function() {
			});
		} else {
			swal({
				title : "",
				text : "文件下载失败！",
				type : "error",
				showCancelButton : false,
				confirmButtonColor : "#ff7922",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : true
			}, function() {
			});
		}
	});
}

//查询指定转发人
function getSendName(forwarduserid,obj){
	if(forwarduserid!=null && forwarduserid!=undefined && forwarduserid!=""){
		$.ajax({
			url:projectpath+'/app/getsendname',
			type:'post',
			dataType:'json',
			data:'forwarduserid='+forwarduserid,
			success:function(data){
				if(data.sendinfo!=null && data.sendinfo!=undefined && data.sendinfo!=""){
					if(data.sendinfo.realname!=null && data.sendinfo.realname!=undefined && data.sendinfo.realname!=""){
						$(obj).text(data.sendinfo.realname);	
					}else{
						$(obj).parent("span").parent("div").hide();
					}
				}else{
					$(obj).parent("span").parent("div").hide();
				}
			},
			error:function(msg){
				$(obj).parent("span").parent("div").hide();
			}
		});
	}else{
		$(obj).parent("span").parent("div").hide();
	}
}

//ajax before
function ajaxbefore(){
	var bf = '<div style="position: fixed; left: 0px; top: 0px; width: 100%; height: 100%; z-index: 501; background: #000;opacity: 0.0;" id="ajaxBeforeSendMaskDiv"></div>';
	$("body").after(bf);
}

//ajax complete
function ajaxcomplete(){
	$('#ajaxBeforeSendMaskDiv').remove();
}

//选择组织架构的类型样式
function changeorganizetype(type){
	if(type==1){
		return "ico_org3";
	}else if(type==2){
		return "ico_org";
	}else if(type == 3){
		return "ico_org2";
	}
}


//删除用户关联的数据
function deleteForwardUserInfo(item){
	//防止事件冒泡
	event.stopPropagation();
	
	var fuid = $(item).attr("id").replace("_delbtn","");
	if(fuid != undefined && fuid != ""){
		$.ajax({
			url:projectpath+"/app/deleteForwardUserInfo",
			data:{"forwarduserid":fuid},
			type:"post",
			success:function(data){
				if(data.status == 0){
					var parent_ul = $(item).parent().parent();
					var length = $(parent_ul).find("li[class='li_del']").length;
					if(length > 1){
						var parent_ul_id = $(parent_ul).attr("id");
						var parent_div_id = parent_ul_id.replace("ul","div");
						$("#"+parent_div_id).find("span i").text(length - 1);
						$(item).parent().remove();
					}else{
						var parent_ul_id = $(parent_ul).attr("id");
						$('#'+parent_ul_id).remove();
						var parent_div_id = parent_ul_id.replace("ul","div");
						$('#'+parent_div_id).remove();
					}
				}else{
					console.log("删除失败");
				}
			}
		});
	}
}

//已处理列表的滑动删除
$(function(){
	var page_list_id = $('.page_list,.pur_list').attr("id");
//	alert(page_list_id);
	if(page_list_id != undefined && page_list_id != ""){
		//滑动删除
		var startX,startY,endX,endY,id,tiems=0,touchtype=-1,deletwidth,fag=false;
		var scrollTopVal=0;
		document.getElementById(page_list_id).addEventListener("touchstart", touchStart, false);
		document.getElementById(page_list_id).addEventListener("touchmove", touchMove, false);
		document.getElementById(page_list_id).addEventListener("touchend", touchEnd, false);
		function touchStart(event){
			 var touch = event.touches[0]; 
			 //获取最外层的id
			 var tar_id = touch.target.offsetParent.id;
			 if(tar_id == ""){
				 tar_id = touch.target.parentElement.offsetParent.id;
			 }
			 if(tar_id == ""){
				 tar_id = $(touch.target).parent().attr('id') == null?'':$(touch.target).parent().attr('id');
			 }
			 var elementhtml =  $("#"+tar_id);
			 if(elementhtml.length <= 0 || tar_id.indexOf("_libox") == -1){
				 tar_id = "";
			 }
			 var _libox = $('#'+tar_id);
			 var _delbtn = $('#'+tar_id.replace("_libox","")+"_delbtn");
			 if(_libox.length <= 0 || _delbtn.length <= 0){
				 tar_id = "";
			 }
			 //判断滑动的位置是否在指定的层里面
			 if(tar_id == undefined || tar_id == ""){
				 fag = true;
				 return;
			 }else{
				 fag = false;
				 id = tar_id.replace("_libox","");
			 }
			 startY = touch.pageY;   
			 startX = touch.pageX;
			 deletwidth = 81;
		}
		function touchMove(event){
			var touch = event.touches[0];
			endY = touch.pageY;
			endX = touch.pageX;
			
			if(Math.abs(startY - endY) > Math.abs(startX - endX)){
//				console.log("上下滑动-----------");
				fag = true;
			}else{
//				console.log("左右滑动-------------");
				//防止页面上下滚动
				event.preventDefault();
			}
			
			//滑动的位置，不在删除的那个层里面，不执行以下操作
			if(fag){return;}
			
			if(tiems == 0){
				scrollTopVal = endX;
			}
			
			if((scrollTopVal - endX) < 81 && (scrollTopVal - endX) > 0){
				touchtype = 0;
				var libox = $('#'+id+"_libox").css("margin-left").replace("px","");
				if(libox != -81){
					$('#'+id+"_libox").css("margin-left",-(scrollTopVal - endX));
					$('#'+id+"_delbtn").css("width",scrollTopVal - endX);
				}
			}else if((endX - scrollTopVal) < 81 && (endX - scrollTopVal) > 0 && touchtype != 0){
				touchtype = 1;
				var delet = $('#'+id+"_delbtn").css("width").replace("px","");
				if(delet > 0){
					$('#'+id+"_libox").css("margin-left",(endX - scrollTopVal) - deletwidth);
					$('#'+id+"_delbtn").css("width",(deletwidth - (endX - scrollTopVal)));
				}
			}
			tiems++;
		}
		function touchEnd(event){ 
			tiems = 0;
			
			//滑动的位置，不在删除的那个层里面，不执行以下操作
			if(fag){return;}
			
			var deletewidth = $('#'+id+"_delbtn").css("width").replace("px","");
			if(touchtype == 0){
				if(deletewidth >= 41){
					$('#'+id+"_libox").css("margin-left",-81);
					$('#'+id+"_delbtn").css("width",81);
					touchtype = 1;
				}else{
					$('#'+id+"_libox").css("margin-left",0);
					$('#'+id+"_delbtn").css("width",0);
				}
			}else if(touchtype == 1){
				if(deletewidth <= 41){
					$('#'+id+"_libox").css("margin-left",0);
					$('#'+id+"_delbtn").css("width",0);
				}else{
					$('#'+id+"_libox").css("margin-left",-81);
					$('#'+id+"_delbtn").css("width",81);
				}
			}
		}
	}
});

var notDataImageUrl_havepower = '../appcssjs/images/index/none_msg.gif';
var notDataImageUrl_nopower = '../appcssjs//images/index/none_msg2.gif';
