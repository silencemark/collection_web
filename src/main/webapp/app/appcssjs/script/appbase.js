	var appBase = {

		 		forward: function(url) {
		 			window.setTimeout(function() {
		 				window.location.href = url;
		 			}, 1);
		 		},
		 		removeHTMLTag: function(str) {
		 			str = str.replace(/<\/?[^>]*>/g, ''); //去除HTML tag
		 			str = str.replace(/[ | ]*\n/g, '\n'); //去除行尾空白
		 			//str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
		 			str = str.replace(/&nbsp;/ig, ''); //去掉&nbsp;
		 			return str;
		 		},
		 		removeHTMLTag2: function(str) {
		 			str = str.replace(/</g, ''); //去除HTML tag
		 			str = str.replace(/>/g, ''); //去除HTML tag
		 			str = str.replace(/\//g, ''); //去除HTML tag
		 			str = str.replace(/[ | ]*\n/g, '\n'); //去除行尾空白
		 			//str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
		 			str = str.replace(/&nbsp;/ig, ''); //去掉&nbsp;
		 			return str;
		 		},
		 		setData: function(key, value) {
		 			$(document).data(key, value);
		 		},
		 		getData: function(key) {
		 			return $(document).data(key);
		 		},
		 		arrayRemove: function(val) {
		 			var index = -1;
		 			for (var i = 0; i < this.length; i++) {
		 				if (this[i] == val) {
		 					index = i;
		 					break;
		 				}
		 			}
		 			if (index > -1) {
		 				this.splice(index, 1);
		 			}
		 		},
		 		encodeHtml: function(str) {
		 			var s = "";
		 			if (str.length == 0) return "";
		 			s = str.replace(/&/g, "&gt;");
		 			s = s.replace(/</g, "&lt;");
		 			s = s.replace(/>/g, "&gt;");
		 			s = s.replace(/ /g, "&nbsp;");
		 			s = s.replace(/\'/g, "&#39;");
		 			s = s.replace(/\"/g, "&quot;");
		 			s = s.replace(/\n/g, "<br>");
		 			return s;
		 		},
		 		decodeHtml: function(str) {
		 			var s = "";
		 			if (str.length == 0) return "";
		 			s = str.replace(/&gt;/g, "&");
		 			s = s.replace(/&lt;/g, "<");
		 			s = s.replace(/&gt;/g, ">");
		 			s = s.replace(/&nbsp;/g, " ");
		 			s = s.replace(/&#39;/g, "\'");
		 			s = s.replace(/&quot;/g, "\"");
		 			s = s.replace(/<br>/g, "\n");
		 			return s;
		 		},
		 		mid: function() {
		 			return (Math.random() * 1E18).toString(36).slice(0, 5).toUpperCase();
		 		},
		 		getQueryString: function(name) {
		 			return this.getQueryStringByUrl(name, window.location);
		 		},
		 		/**
		 		 * @description [getQueryStringByUrl 根据参数名称获取指定URL参数值]
		 		 * @param  {[String]} 参数名称
		 		 * @param  {[String]} 指定URL
		 		 * @return {[String]} 参数值
		 		 */
		 		getQueryStringByUrl: function(name, url) {
		 			var reg = new RegExp("(^|\\?|&)" + name + "=([^&]*)(\\s|&|$)", "i");
		 			try {
		 				if (reg.test(url)) return unescape(decodeURI(RegExp.$2.replace(/\+/g, " ")));
		 			} catch (e) {
		 				H[co].error(e);
		 			}
		 			return "";
		 		},
		 		getAllUrlParameter:function(){
		 			  var url = location.search; //获取url中"?"符后的字串
		 			   var theRequest = new Object();
		 			   if (url.indexOf("?") != -1) {
		 			      var str = url.substr(1);
		 			      strs = str.split("&");
		 			      for(var i = 0; i < strs.length; i ++) {
		 			         theRequest[strs[i].split("=")[0]]=(strs[i].split("=")[1]);
		 			      }
		 			   }
		 			 return theRequest;
		 		},
		 		getAnchor: function() {
		 			var url = window.location + "";
		 			var a = url.split("#");
		 			return a[1];
		 		},
		 		browser: {
		 			version: (navigator.userAgent.toLowerCase().match(/.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || [0, '0'])[1],
		 			safari: /webkit/.test(navigator.userAgent.toLowerCase()),
		 			opera: /opera/.test(navigator.userAgent.toLowerCase()),
		 			msie: /msie/.test(navigator.userAgent.toLowerCase()) && !/opera/.test(navigator.userAgent.toLowerCase()),
		 			mozilla: /mozilla/.test(navigator.userAgent.toLowerCase()) && !/(compatible|webkit)/.test(navigator.userAgent.toLowerCase())
		 		},
		 		storage: {
		 			setData: function(name, value) {
		 				try {
		 					localStorage.setItem(name, value);
		 				} catch (e) {
		 					console.log(e);
		 				}
		 			},
		 			getData: function(name) {
		 				try {
		 					return localStorage.getItem(name);
		 				} catch (e) {
		 					console.log(e);
		 				}
		 			}
		 		},
		 		 isEmpty : function(value){
					  		return  (null === value || "" === value || undefined === value);
				   },
				   getDataJson : function(formBean){
					  return JSON.stringify(formBean);
				   },
					 /**
					 * 将时间戳转换为“****年**月**日”
					 * **/
					parseDate_chinese : function(datetime){
						var parsedDate = this.parseDate(datetime);
						if (null != parsedDate) {
							return parsedDate.substring(0,4)+"年"+parsedDate.substring(5,7)+"月"+parsedDate.substring(8)+"日";
						}
						return "";
					},
					
					/**
					 * 将时间戳转换为日期
					 * **/
					parseDate : function(datetime){
						if(!this.isEmpty(datetime)){
							if(datetime.length > 19 || datetime.length == undefined){
								var date = new Date(datetime);
								return date.format("YYYY-MM-DD");
							}else if(datetime.length >= 10){
								return datetime.substring(0,10);
							}else{
								return datetime;
							}
						}
						return null;
					},
					
					/**
					 * 将时间戳转换为日期
					 * **/
					parseDateMinute : function(datetime){
						if(!this.isEmpty(datetime)){
							if(datetime.length > 19 || datetime.length == undefined){
								var date = new Date(datetime);
								return date.format("YYYY-MM-DD hh:mm");
							}else if(datetime.length >= 16){
								return datetime.substring(0,16);
							}else{
								return datetime;
							}
						}
						return null;
					},
					
					/**
					 * 将时间戳转换为日期
					 * **/
					parseDatetime : function(datetime){
						if(!this.isEmpty(datetime)){
							var date = new Date(datetime);
							return date.format("YYYY-MM-DD hh:mm:ss");
						}
						return null ;
					}


		 	};

		 	Date.prototype.format = function(fmt) { //author: meizz 
		 		var o = {
		 			"M+": this.getMonth() + 1, //月份 
		 			"D+": this.getDate(), //日 
		 			"h+": this.getHours(), //小时 
		 			"m+": this.getMinutes(), //分 
		 			"s+": this.getSeconds(), //秒 
		 			"q+": Math.floor((this.getMonth() + 3) / 3), //季度 
		 			"S": this.getMilliseconds() //毫秒 
		 		};
		 		if (/(Y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
		 		for (var k in o)
		 			if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		 		return fmt;
		 	};
		 	
		 	Date.prototype.formatday = function(pattern) {
		 	    /*初始化返回值字符串*/
		 	    var returnValue = pattern;
		 	    /*正则式pattern类型对象定义*/
		 	    var format = {
		 	        "y+": this.getFullYear(),
		 	        "M+": this.getMonth()+1,
		 	        "d+": this.getDate(),
		 	        "H+": this.getHours(),
		 	        "m+": this.getMinutes(),
		 	        "s+": this.getSeconds(),
		 	        "S": this.getMilliseconds(),
		 	        "h+": (this.getHours()%12),
		 	        "a": (this.getHours()/12) <= 1? "上午":"下午"
		 	    };
		 	    /*遍历正则式pattern类型对象构建returnValue对象*/
		 	    for(var key in format) {
		 	        var regExp = new RegExp("("+key+")");
		 	        if(regExp.test(returnValue)) {
		 	            var zero = "";
		 	            for(var i = 0; i < RegExp.$1.length; i++) { zero += "0"; }
		 	            var replacement = RegExp.$1.length == 1? format[key]:(zero+format[key]).substring(((""+format[key]).length));
		 	            returnValue = returnValue.replace(RegExp.$1, replacement);
		 	        }
		 	    }
		 	    return returnValue;
		 	};
		 	
		 	Array.prototype.remove=function(dx) {
		 	　　if(isNaN(dx)||dx>this.length){return false;}
		 	　　for(var i=0,n=0;i<this.length;i++) {
		 	　　　　if(this[i]!=this[dx]) {
		 	　　　　　　this[n++]=this[i];
		 	　　　　}
		 	　　}
		 	　　this.length-=1;
		 	};
		 	
		 	String.prototype.replaceAll = function(s1,s2) { 
		 	    return this.replace(new RegExp(s1,"gm"),s2); 
		 	};
