<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" >
<title>测试地址</title>
<script type="text/javascript" src="appcssjs/script/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="appcssjs/script/cache.js"></script>
<script type="text/javascript" src="../../js/md5.js"></script>
</head>
<body>
<center>
	<table border="0">
		<tr>
			<td>方法名:</td>
			<td><input type="text" id="methodname" />(例:appLogin/userlogin)</td>
		</tr>
		<tr>
			<td>传入参数:</td>
			<td><textarea rows="8" cols="40" id="parameter"></textarea><br/>(例：phone=15000042335&password=123)</td>
		</tr>
		<tr>
			<td>传入参数json:</td>
			<td><textarea rows="8" cols="40" id="parameterjson" disabled="disabled"></textarea><br/></td>
		</tr>
		<tr>
			<th colspan="2" align="center"><input type="button" value="提交" id="subbtn"/><br/></th>
		</tr>
		<tr>
			<td>返回参数：</td>
			<td><textarea rows="18" cols="40" id="returnparameter"></textarea></td>
		</tr>
	</table>
	<input type="button" onclick="saveUserInfo()" value="保存用户信息到缓存"/>
	<input type="button" onclick="JianKangCache.getGlobalData('userinfo',function(data){alert(JSON.stringify(data))})" value="查看用户信息缓存"/>
	<form id="idform">
		<input type="hidden" name="type" value="fuck">
		<input type="hidden" name="name" value="shit">
	</form>
	<input type="button" onclick="idform()" value="测试form"/>
	
</center>
<script type="text/javascript">
function idform(){
	var materiallist={};
	materiallist['type']='fuck';
	materiallist['name']='shit';
	var materiallist1={};
	materiallist1['type1']='fuck1';
	materiallist1['name1']='shit1';
	var allMenu={  
            "materiallist":[ ]  
            };  
	allMenu.materiallist.push(materiallist);  
	allMenu.materiallist.push(materiallist1);  
	var url=projectpath+"/app/insertPurchase";
	$.ajax({
		type:'post',
		dataType:'json',
		data:"materiallist="+JSON.stringify(allMenu),
		url:url,
		success:function(data){
			if(typeof(data.userInfo)!="undefined"){
				JianKangCache.setGlobalData("userinfo",data.userInfo);
			}
		},error:function(text){
			alert("参数格式错误或者方法名不存在");
		}
	})
}
function saveUserInfo(){
	var url=projectpath+"/app/chooseCompany?userid=1";
	$.ajax({
		type:'post',
		dataType:'json',
		url:url,
		success:function(data){
			if(typeof(data.userInfo)!="undefined"){
				JianKangCache.setGlobalData("userinfo",data.userInfo);
			}
		},error:function(text){
			alert("参数格式错误或者方法名不存在");
		}
	})
}

$(function(){
	$('#parameter').change(function(){
		var parameter=$('#parameter').val();
		var array=parameter.split('&');
		var param="";
		for(var i=0;i<array.length;i++){
			var value=array[i].split('=');
			if(isNaN(value[1])){
				param+="\""+value[0]+"\":\""+value[1]+"\",";
			}else{
				param+="\""+value[0]+"\":"+value[1]+",";
			}
		}
		$('#parameterjson').val("{"+param.substring(0,param.length-1)+"}");
	})
	$('#subbtn').click(function(){
		var methodname=$('#methodname').val();
		var parameter=$('#parameter').val();
		var url=projectpath+"/"+methodname;
		$.ajax({
			type:'post',
			dataType:'json',
			url:url,
			data:parameter,
			success:function(data){
				$('#returnparameter').text(JSON.stringify(data));
			},error:function(text){
				alert("参数格式错误或者方法名不存在");
			}
		})
	})
})
</script>
</body>
</html>