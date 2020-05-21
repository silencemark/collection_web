package com.collection.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.collection.util.Constants;
import com.utils.GetWxOrderno;
import com.utils.RequestHandler;
import com.utils.Sha1Util;
import com.utils.TenpayUtil;

public class paymoneyutil {
	private static final Logger LOGGER = Logger.getLogger(paymoneyutil.class);
	public Map<String, Object> payMoney(HttpServletRequest request,HttpServletResponse response,Map<String, Object> orderMap){
		
		
		String appid=Constants.APPID;
		String appsecret =Constants.APPSECRET;
		String partner = Constants.PARTNER;
		String partnerkey = Constants.PARTNERKEY;
		String price=orderMap.get("price")+"";
		float sessionmoney = Float.parseFloat(price);
		String finalmoney = String.format("%.2f", sessionmoney);
		finalmoney = finalmoney.replace(".","");
		LOGGER.debug(sessionmoney);
		int one=price.indexOf(".");
		if(one>0){
				finalmoney=Integer.parseInt(finalmoney)+"";
		}
		String openId =orderMap.get("openid")+"";
		
		//获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder
				String currTime = TenpayUtil.getCurrTime();
				//8位日期
				String strTime = currTime.substring(8, currTime.length());
				//四位随机数
				String strRandom = TenpayUtil.buildRandom(4) + "";
				//10位序列号,可以自行调整。
				String strReq = strTime + strRandom;
				
				
				//商户号
				String mch_id = partner;
				//子商户号  非必输
				//String sub_mch_id="";
				//设备号   非必输
				String device_info="";
				//随机数 
				String nonce_str = strReq;
				//商品描述
				//String body = describe;
				
				//商品描述根据情况修改
				String body = "理财邦订阅理财周刊支付金额";
				//附加数据
				String attach = "支付金额";
				//商户订单号
				String out_trade_no = appid+Sha1Util.getTimeStamp();
				int intMoney = Integer.parseInt(finalmoney);
				//总金额以分为单位，不带小数点
//				int total_fee = intMoney;
				//订单生成的机器 IP
				String spbill_create_ip = request.getRemoteAddr();
				//订 单 生 成 时 间   非必输
//				String time_start ="";
				//订单失效时间      非必输
//				String time_expire = "";
				//商品标记   非必输
//				String goods_tag = "";
				
				//这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
				//"http://zx.hoheng.cn/weixin/tender/queryTenderList"
				String notify_url =orderMap.get("notuify_url")+"";
				String trade_type ="";
				if(openId!=null && !openId.equals("") && !openId.equals("null")){ 
					trade_type = "JSAPI";
				}else{
					trade_type = "APP";
				}
				String openid = openId;
				//非必输
//				String product_id = "";
				SortedMap<String, String> packageParams = new TreeMap<String, String>();
				packageParams.put("appid", appid);  
				packageParams.put("mch_id", mch_id);  
				packageParams.put("nonce_str", nonce_str);  
				packageParams.put("body", body);  
				packageParams.put("attach", attach);  
				packageParams.put("out_trade_no", out_trade_no);  
				
				
				//这里写的金额为1 分到时修改
				packageParams.put("total_fee", finalmoney);  
				packageParams.put("spbill_create_ip", spbill_create_ip);  
				packageParams.put("notify_url", notify_url);  
				
				packageParams.put("trade_type", trade_type);
				if(openId!=null && !openId.equals("") && !openId.equals("null")){ 
					packageParams.put("openid", openid);  
				}
				RequestHandler reqHandler = new RequestHandler(request, response);
				reqHandler.init(appid, appsecret, partnerkey);
				String sign = reqHandler.createSign(packageParams);
				String xml="<xml>"+
						"<appid>"+appid+"</appid>"+
						"<mch_id>"+mch_id+"</mch_id>"+
						"<nonce_str>"+nonce_str+"</nonce_str>"+
						"<sign>"+sign+"</sign>"+
						"<body><![CDATA["+body+"]]></body>"+
						"<attach>"+attach+"</attach>"+
						"<out_trade_no>"+out_trade_no+"</out_trade_no>"+
						"<total_fee>"+finalmoney+"</total_fee>"+
						"<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
						"<notify_url>"+notify_url+"</notify_url>"+
						"<trade_type>"+trade_type+"</trade_type>";
				if(openId!=null && !openId.equals("") && !openId.equals("null")){ 
					xml+="<openid>"+openid+"</openid></xml>";
				}else{
					xml+="</xml>";
				}
						
				LOGGER.debug(xml);
				
				String allParameters = "";
				try {
					allParameters =  reqHandler.genPackage(packageParams);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
				String prepay_id="";
				try {
					prepay_id = new GetWxOrderno().getPayNo(createOrderURL, xml);
					if(prepay_id.equals("")){
						request.setAttribute("ErrorMsg", "统一支付接口获取预支付订单出错");
						LOGGER.debug("统一支付接口获取预支付订单出错");
						return new HashMap<String, Object>();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				SortedMap<String, String> finalpackage = new TreeMap<String, String>();
				String appid2 = appid;
				String timestamp = Sha1Util.getTimeStamp();
				String nonceStr2 = nonce_str;
				String prepay_id2 = "prepay_id="+prepay_id;
				String packages = prepay_id2;
				finalpackage.put("appId", appid2);  
				finalpackage.put("timeStamp", timestamp);  
				finalpackage.put("nonceStr", nonceStr2);  
				finalpackage.put("package", packages);  
				finalpackage.put("signType", "MD5");
				String finalsign = reqHandler.createSign(finalpackage);
				LOGGER.debug(request.getContextPath()+"/pay.jsp?appid="+appid2+"&timeStamp="+timestamp+"&nonceStr="+nonceStr2+"&package="+packages+"&sign="+finalsign);
				Map<String, Object> paymap=new HashMap<String, Object>();
				paymap.put("appid", appid2);
				paymap.put("timeStamp", timestamp);
				paymap.put("nonceStr", nonceStr2);
				paymap.put("packagevalue", packages);
				paymap.put("signvalue", finalsign);
				paymap.put("out_trade_no", out_trade_no);
				paymap.put("prepay_id", prepay_id);
				return paymap;
	}
}
