(function() {
	/**
	 * 临时缓存
	 */
	JianKangCache = {
		/**
		 * 设置全局缓存
		 */
		setGlobalData : function(key, value, callback) {
			if ("object" != typeof value) {
				var value_1 = value;
				value = {};
				value[key] = value_1;
			}
			JianKangCache.set(key, value, true);
			if (JianKangCache.isLocalStorage()) {
				if ("function" == typeof callback) {
					callback(true);
				}
				return;
			}
		},
		/**
		 * 得到全局缓存
		 */
		getGlobalData : function(key, callback) {
			JianKangCache.get(key, true, callback);
		},
		/**
		 * 删除全局缓存
		 */
		delGlobalData : function(key, callback) {
			JianKangCache.del(key, true, callback);
		},
		/**
		 * 保存
		 */
		setData : function(key, value, callback) {
			JianKangCache.set(key, value);
			if (JianKangCache.isLocalStorage()) {
				if ("function" == typeof callback) {
					callback(true);
				}
				return;
			}
		},
		/**
		 * 得到
		 */
		getData : function(key, callback) {
			JianKangCache.get(key, false, callback);
		},

		/**
		 * 删除
		 */
		deleteData : function(key, callback) {
			var data = {
				KeyUrl : location.href,
				Key : key,
				deleteType : "key"
			}
			JianKangCache.del(key);
			if (JianKangCache.isLocalStorage()) {
				if ("function" == typeof callback) {
					callback(true);
				}
			}
		},

		/**
		 * 清空
		 */
		clearData : function(key, status, callback) {
			JianKangCache.del(key, status);
			if (JianKangCache.isLocalStorage()) {
				if ("function" == typeof callback) {
					callback(true);
				}
			}
		},
		set : function(key, value, status) {
			/**
			 * 全局缓存
			 */
			if (status) {
				var Data = localStorage.GlobalData;
				try {
					Data = JSON.parse(Data);
				} catch (e) {
				}
				if (Data == null || "object" != typeof Data) {
					Data = {};
				}
				Data[key] = value;
				localStorage.GlobalData = JSON.stringify(Data);
			} else {
				var locationhref = location.href;
				var Data = localStorage.Data;
				try {
					Data = JSON.parse(Data);
				} catch (e) {
				}
				if (Data == null || "object" != typeof Data) {
					Data = {};
				}
				if (Data[locationhref] == null) {
					Data[locationhref] = {};
				}
				Data[locationhref][key] = value;
				localStorage.Data = JSON.stringify(Data);
			}
		},
		del : function(key, status) {
			/**
			 * 全局
			 */
			if (status) {
				var Data = localStorage.GlobalData;
				try {
					Data = JSON.parse(Data);
				} catch (e) {
				}
				if (Data == null || "object" != typeof Data) {
					Data = {};
				}
				if (key == null) {

				} else {
					if (Data == null) {
						Data = {};
					}
					delete Data[key];
				}
				localStorage.GlobalData = JSON.stringify(Data);
			} else {
				var locationhref = location.href;
				var Data = localStorage.Data;
				try {
					Data = JSON.parse(Data);
				} catch (e) {
				}
				if (Data == null || "object" != typeof Data) {
					Data = {};
				}
				if (key == null) {
					delete Data[locationhref];
				} else {
					if (Data[locationhref] == null) {
						Data[locationhref] = {};
					}
					delete Data[locationhref][key];
				}
				localStorage.Data = JSON.stringify(Data);
			}

		},
		get : function(key, status, callback) {
			/**
			 * 全局
			 */
			if (status) {
				var value = null;
				var Data = localStorage.GlobalData;
				try {
					Data = JSON.parse(Data);
				} catch (e) {
				}
				if (Data == null || "object" != typeof Data) {
					Data = {};
				}
				localStorage.GlobalData = JSON.stringify(Data);
				value = Data[key];
				// 不存在者存在window中
		if ("function" == typeof callback) {
			callback(value);
		}
	} else {
		var locationhref = location.href;
		var value = null;
		var Data = localStorage.Data;
		try {
			Data = JSON.parse(Data);
		} catch (e) {
		}
		if (Data == null || "object" != typeof Data) {
			Data = {};
		}
		if (Data[locationhref] == null) {
			Data[locationhref] = {};
		}
		localStorage.Data = JSON.stringify(Data);
		localStorage.DataLen = Object.keys(Data).length;
		value = Data[locationhref][key];
		// 不存在者存在window中
		if ("function" == typeof callback) {
			callback(value);
		}
	}
},
isLocalStorage : function() {
	if ("object" == typeof localStorage) {
		return true;
	} else {
		return false;
	}
}
	}

	window.JianKangCache = JianKangCache;
})()