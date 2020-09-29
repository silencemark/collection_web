
//var projectpath="http://localhost:8080";
var projectpath="https://www.goaragekit.cn";

var userinfo={};


/**
 * 替换模板
 * @param tmpl
 * @param bean
 * @returns
 */
function replaceTemp(tmpl, bean){
		for(var i in bean){//用javascript的for/in循环遍历对象的属性 
			if(bean.hasOwnProperty(i)){
				var regExp=new RegExp("{[ ]*"+i+"[ ]*}","gmi");
				while(true){
					tmpl = tmpl.replace(regExp,bean[i]);
					if(!regExp.test(tmpl)){
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
function goBackPage(){
	window.history.go(-1);
}

 //自动封装form
 function getFormBean(formID){
	var tempData = $('#'+formID).serializeArray();
	var item = new Object() ;
	if(null!=tempData&&''!=tempData&&typeof(tempData)!='undefined'){
		$.each(tempData, function(i, field){
			item[field.name]=field.value;
		});
	}
	return item;
}
 /**
  * 获取地址栏参数
  * @param name
  * @returns
  * @author silence
  */
	function getUrlParam(name)
	{
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
		var r = window.location.search.substr(1).match(reg);  //匹配目标参数
		if (r!=null) return unescape(r[2]); return null; //返回参数值
	}
	
	
 /**
  * 转发用户
  * @param name
  * @returns
  * @author silence
  */
	function userForword(companyid,resourceid,receiveid,createid,resourcetype)
	{
		$.ajax({
			type:'post',
			dataType:'json',
			url:projectpath+'/app/forward',
			data:'companyid='+companyid+'&resourceid='+resourceid+'&receiveids='+receiveid+'&createid='+createid+'&resourcetype='+resourcetype,
			success:function(data){
				if(data.status==0){
//					swal({
//						title : "",
//						text : "转发成功",
//						type : "success",
//						showCancelButton : false,
//						confirmButtonColor : "#ff7922",
//						confirmButtonText : "确认",
//						cancelButtonText : "取消",
//						closeOnConfirm : false
//					}, function(){
//					});
				}
			}
		})
	}
	 /**
	  * 转发用户
	  * @param name
	  * @returns
	  * @author silence
	  */
	function readstatus(orderid,userid){
		$.ajax({
			type:'post',
			dataType:'json',
			url:projectpath+'/app/updateIsread?resourceid='+orderid+'&isread=1&receiveid='+userid,
			success:function(data){
			}
		})
	}
 
	function Num(obj){
		obj.value = obj.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
		obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字而不是
		obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
		obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
	}
	
	
	
	/**
	 * 修改信息的阅读状态
	 * @param url
	 * @param resourceid
	 * @param isread
	 * @param type
	 */
	function intoDetail(url,resourceid,isread,type){
		if(isread == '0' || isread == 0){
			var param = new Object();
			param.resourceid = resourceid;
			param.receiveid = userid;
			param.resourcetype = type;
			//标记为已读
			$.ajax({
				url:projectpath+"/app/upateUserNoReadStatus",
				type:"post",
				data:param,
				success:function(data){
					if(data != undefined && data != ""){
						if(data.status == 0 || data.status == '0'){
							location.href=url;
						}
					}
				}
			});
		}else{
			location.href=url;
		}
	}
	
	function getNowTime(){
		  var date = new Date();
		  this.year = date.getFullYear();
		  this.month = date.getMonth() + 1;
		  this.date = date.getDate();
		  this.month=this.month<10?"0"+this.month:this.month;
		  this.date=this.date<10?"0"+this.date:this.date;
		  this.hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
		  this.minute = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
		  var currentTime = this.year + "-" + this.month + "-" + this.date + " " + this.hour + ":" + this.minute;
	      return currentTime;
	}
	
	//公共的查询方法
	function requestPost(url,param,callback){
		$.ajax({
			url:url,
			type:"post",
			data:param,
			beforeSend:function(){
				loading();
			},
			success:function(resultMap){
				$('#Pagination').html(resultMap.pager);
				callback(resultMap);
			},
			complete:function(){
				closeLoading();
			},
			error:function(e){
				
			}
		});
	}
	
	//显示加载更多
	function loading(){
		var load = '<div id="load_mask" class="div_mask" style="opacity:0;"></div>'+
			'<div id="load_loading" class="loading"><img src="../userbackstage/images/public/loading.gif" width="360" height="200" /></div>';
			$("body").after(load);
	}
	
	//移除加载更多的样式
	function closeLoading(){
		$('#load_mask').remove();
		$('#load_loading').remove();
	}
	
	/**
	  * 保存pc使用方的组织
	  * @param name
	  * @returns
	  * @author silence
	  */
		function changeOrganizeid(organizeid,organizename)
		{
			$.ajax({
				type:'post',
				dataType:'json',
				url:projectpath+'/pc/changeOrganizeUser',
				data:'organizeid='+organizeid+'&organizename='+organizename,
				success:function(data){
				}
			})
		}
	function changeIsReadAll(userid,resourceid,resourcetype){
		$.ajax({
			type:'post',
			dataType:'json',
			url:projectpath+'/app/changeIsReadAll',
			data:'userid='+userid+'&resourcetype='+resourcetype+"&resourceid="+resourceid,
			success:function(data){
				
			}
		})
	}
	function pushMessage(userid,title,url){
		$.ajax({
			type:'post',
			dataType:'json',
			url:projectpath+'/app/pushMessage',
			data:'userid='+userid+'&title='+title+"&url="+url,
			success:function(data){
			}
		})
	}
	
	function changeIsread(receiveid,resourceid){
		$.ajax({
			type:'post',
			dataType:'json',
			url:projectpath+'/app/updateIsread?receiveid='+receiveid+'&isread=0&resourceid='+resourceid,
			success:function(data){
			}
		})
	}
	
	function fomatFloatFloor(src,pos){       
        return Math.floor(src*Math.pow(10, pos))/Math.pow(10, pos);       
   }
	
	
	//pc图片放大
	function showBigImagePC(obj){
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
										 var html = '<div id="win_PMG">'+
													'<div onclick="$(\'#win_PMG\').remove();" style="width: 100%; height: 100%; opacity:0.8; position: fixed; top:0px; background-color: #333; z-index: 9000">'+
													'</div>'+
													'<img onclick="$(\'#win_PMG\').remove();" id="'+img_model+'" src="'+src+'" align="center" style="position: fixed; top:'+top+'px; left:'+left+'px; z-index: 9001; width: '+width+'px;height: '+height+'px">'+
													'</div>';
										$("body").after(html);

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

	//播放音乐
	function PlayRecordPC(item,url){
		var playnexthtml = $(item).html();
		if(playnexthtml == "" || playnexthtml == "语言文件"){
			
			var ua = navigator.userAgent.toLowerCase(); 
			var recordhmtl = "";
			//判断浏览器 类型
			if(ua.match(/msie ([\d.]+)/)){ 
				recordhmtl = '<object classid="clsid:22D6F312-B0F6-11D0-94AB-0080C74C7E95">'+
								'<param name="AutoStart" value="1" />'+
								'<param name="Src" value="'+url+'" />'+
							'</object>'; 
			} 
			else if(ua.match(/firefox\/([\d.]+)/)){ 
				recordhmtl = '<embed src="'+url+'" type="audio/mp3" hidden="true" loop="false" mastersound></embed>'; 
			} 
			else if(ua.match(/chrome\/([\d.]+)/)){ 
				recordhmtl = '<audio src="'+url+'" type="audio/mp3" autoplay="autoplay" hidden="true"></audio>'; 
			} 
			else if(ua.match(/opera.([\d.]+)/)){ 
				recordhmtl = '<embed src="'+url+'" hidden="true" loop="false"><noembed><bgsounds src="'+url+'"></noembed></embed>'; 
			} 
			else if(ua.match(/version\/([\d.]+).*safari/)){ 
				recordhmtl = '<audio src="'+url+'" type="audio/mp3" autoplay="autoplay" hidden="true"></audio>'; 
			} 
			else { 
				recordhmtl = '<embed src="'+url+'" type="audio/mp3" hidden="true" loop="false" mastersound></embed>'; 
			}
			$(item).html(recordhmtl);
		}else{
			$(item).html("");
		}
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
							$(obj).parent("td").parent("tr").hide();
						}
					}else{
						$(obj).parent("td").parent("tr").hide();
					}
				},
				error:function(msg){
					$(obj).parent("td").parent("tr").hide();
				}
			});
		}else{
			$(obj).parent("td").parent("tr").hide();
		}
	}
	
	//删除用户关联的数据
	function deleteForwardUserInfo(item,fuid){
		swal({   
			title: "你确定要删除这条信息吗？",   
			text: "删除之后无法恢复！",   
			type: "warning",   
			showCancelButton: true,   
			confirmButtonColor: "#ff9b30",   
			confirmButtonText: "确定",   
			cancelButtonText: "取消",
			closeOnConfirm: false
		}, function(){ 
			if(fuid != undefined && fuid != ""){
				$.ajax({
					url:projectpath+"/app/deleteForwardUserInfo",
					data:{"forwarduserid":fuid},
					type:"post",
					async:false,
					success:function(data){
						if(data.status == 0){
							swal("删除成功！", "", "success"); 
							$(item).parent().parent().remove();
						}else{
							swal("网络异常，请稍后重试···！", "", "warning"); 
							console.log("删除失败");
						}
					}
				});
			}else{
				swal("网络异常，请稍后重试···！", "", "warning"); 
			}
		});
	}
	