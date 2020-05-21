package com.collection.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import net.sf.json.JSONObject;

public class heweather {
	public static void main(String[] args) {
	
		/*String httpUrl = "https://api.heweather.com/x3/weather?cityid=CN101040100&key=c862473b80a14309ad456cae531375b4";
		//httpUrl = "https://api.heweather.com/x3/citylist?search=allworld&key=c862473b80a14309ad456cae531375b4";
		String jsonResult = request(httpUrl);
		System.out.println(jsonResult);*/
		System.out.println(getTemperature("CN101020100"));
	}
	public static String getpm25(String cityid){
		String httpUrl = "https://api.heweather.com/x3/weather?cityid="+cityid+"&key=c862473b80a14309ad456cae531375b4";
		//httpUrl = "https://api.heweather.com/x3/citylist?search=allworld&key=c862473b80a14309ad456cae531375b4";
		String jsonResult = request(httpUrl);
		JSONObject json=JSONObject.fromObject(jsonResult);
		String pm25="";
		try {
			List<Map<String, Object>> weatherlist=(List<Map<String, Object>>) json.get("HeWeather data service 3.0");
			Map<String, Object> aqi=(Map<String, Object>) weatherlist.get(0).get("aqi");
			Map<String, Object> city=(Map<String, Object>) aqi.get("city");
			pm25=city.get("pm25")+"";
		} catch (Exception e) {
			System.out.println("天气获取失败");
			System.out.println("----------"+jsonResult);
			pm25="0";
		}
		return pm25;
	}
	
	public static String gethumity(String cityid){
		String httpUrl = "https://api.heweather.com/x3/weather?cityid="+cityid+"&key=c862473b80a14309ad456cae531375b4";
		//httpUrl = "https://api.heweather.com/x3/citylist?search=allworld&key=c862473b80a14309ad456cae531375b4";
		String jsonResult = request(httpUrl);
		JSONObject json=JSONObject.fromObject(jsonResult);
		String hum="";
		try {
			List<Map<String, Object>> weatherlist=(List<Map<String, Object>>) json.get("HeWeather data service 3.0");
			List<Map<String, Object>> daily_forecast=(List<Map<String, Object>>) weatherlist.get(0).get("daily_forecast");
			hum=daily_forecast.get(0).get("hum")+"";
		} catch (Exception e) {
			System.out.println("天气获取失败");
			System.out.println("----------"+jsonResult);
			hum="0";
		}
		return hum;
	}
	//获取温度
	public static String getTemperature(String cityid){
		String httpUrl = "https://api.heweather.com/x3/weather?cityid="+cityid+"&key=c862473b80a14309ad456cae531375b4";
		//httpUrl = "https://api.heweather.com/x3/citylist?search=allworld&key=c862473b80a14309ad456cae531375b4";
		String jsonResult = request(httpUrl);
		JSONObject json=JSONObject.fromObject(jsonResult);
		String temperature="";
		try {
			List<Map<String, Object>> weatherlist=(List<Map<String, Object>>) json.get("HeWeather data service 3.0");
			Map<String, Object> now=(Map<String, Object>) weatherlist.get(0).get("now");
			temperature=now.get("tmp")+"℃ ";
		} catch (Exception e) {
			System.out.println("天气获取失败");
			System.out.println("----------"+jsonResult);
			temperature="0";
		}
		return temperature;
	}
	//获取天气状况
	public static String getweathername(String cityid){
		String httpUrl = "https://api.heweather.com/x3/weather?cityid="+cityid+"&key=c862473b80a14309ad456cae531375b4";
		//httpUrl = "https://api.heweather.com/x3/citylist?search=allworld&key=c862473b80a14309ad456cae531375b4";
		String jsonResult = request(httpUrl);
		JSONObject json=JSONObject.fromObject(jsonResult);
		String weathername="";
		try {
			List<Map<String, Object>> weatherlist=(List<Map<String, Object>>) json.get("HeWeather data service 3.0");
			List<Map<String, Object>> daily_forecast=(List<Map<String, Object>>) weatherlist.get(0).get("daily_forecast");
			Map<String, Object> cond=(Map<String, Object>) daily_forecast.get(0).get("cond");
			weathername=cond.get("txt_d")+"";
		} catch (Exception e) {
			System.out.println("天气获取失败");
			System.out.println("----------"+jsonResult);
			weathername="0";
		}
		return weathername;
	}
	
	public static String request(String httpUrl) {
		   BufferedReader reader = null;String result = null;StringBuffer sbf = new StringBuffer();
		   try {
		   URL url = new URL(httpUrl);
		  HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		   connection.setRequestMethod("GET");
		   connection.connect();
		   InputStream is = connection.getInputStream();
		   reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		   String strRead = null;
		   while ((strRead = reader.readLine()) != null) {
		   sbf.append(strRead); sbf.append("\r\n");
		   }
		   reader.close();
		   result = sbf.toString();
		   } catch (Exception e) { e.printStackTrace(); }
		   return result;
		 }
}
