<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>消息主页</title>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" >


<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/style/public.css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/style/page_public.css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/style/msg.css">
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/tab.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/page.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/appendjs.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script src="http://cdn.ronghub.com/RongIMLib-2.2.3.min.js"></script> 
<script type="text/javascript">
var userInfo;
if('${userInfo}' != ''){
	userInfo=${userInfo};
}


function getInfoList(){
	RongIMClient.getInstance().getConversationList({
		  onSuccess: function(list) {
		    // list => 会话列表集合。
		    console.log(list);
		    var groupid=[];
		    for(var i=0;i<list.length;i++){
		    	groupid[i]=list[i].targetId;
		    }
		    if(list.length>0){
			    $.ajax({
		    		type:"post",
		    		dataType:"json",
		    		url:"<%=request.getContextPath() %>/app/getGroupList",
		    		data:"groupids="+groupid,
		    		success:function(data){
		    			if(data.grouplist.length>0){
		    				var temp="";
		    				for(var j=0;j<data.grouplist.length;j++){
		    					temp+="<li onclick=\"window.parent.location.href='file:///D:/eclipseWorkspace/catering_web/src/main/webapp/app/message/talk.html?groupId="+data.grouplist[j].groupid+"'\">"+
		    			        	"<b><img src=\"<%=request.getContextPath() %>"+data.grouplist[j].groupurl+"\"></b>"+
		    			            "<span>"+data.grouplist[j].groupname+"</span><i>"+data.grouplist[j].lasttime+"</i>"+
		    			            "<div class=\"clear\"></div>";
		    			        if(list[j].unreadMessageCount > 0){
		    			        	if(data.grouplist[j].type==2){
		    			        		temp+="<p class=\"word_hidden unread\">[图片]</p></li>";
		    			        	}else if(data.grouplist[j].type==3){
		    			        		temp+="<p class=\"word_hidden unread\">[语音]</p></li>";
		    			        	}else{
		    			        		temp+="<p class=\"word_hidden unread\">"+data.grouplist[j].lastcontent+"</p></li>";
		    			        	}
		    			        	
		    			        }else{
		    			        	temp+="<p class=\"word_hidden\">"+data.grouplist[j].lastcontent+"</p></li>";
		    			        }
		    				}
		    				$('#infoul').html(temp);
		    			}
		    		}
		    	})
		    }
		  },
		  onError: function(error) {
		     // do something...
		  }
		},null);
}
</script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/RongIMClient.js"></script>
</head>

<body onload="gettoken();"  class="talk_bg">
<div class="head_box">
	<a class="ico_back" onclick="goBackPage();">返回</a>
    <div class="name" onclick="getInfoList()">消息</div>
</div>
<div class="head_none"></div>
<div class="msg_index">
	<div class="msg_nav">
    	<span class="active">消息列表</span>
        <span>组织架构</span>
        <span class="last">通讯录</span>
        <div class="clear"></div>
    </div>
	<div class="search_box" ><input type="text" class="bg" placeholder="搜索"></div>
    <ul id="infoul">
    	
    </ul>
</div>
<div class="bt_none"></div>
</body>
</html>

