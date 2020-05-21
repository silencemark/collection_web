<%@page import="com.alibaba.fastjson.JSON"%>
<%@page import="java.util.List"%>
<%@page import="com.collection.util.CookieUtil"%>
<%@page import="java.util.Map"%>
<%@page import="com.collection.util.UserUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript">
function changemenu(obj){
	if($(obj).attr("class")=="bg_hidden"){
		$(obj).parent().parent().find("ul").hide();
		$(obj).parent().parent().find("span[class=bg_show]").attr("class","bg_hidden");
		$(obj).attr("class","bg_show");
		
		$(obj).next().next().show();
	}else{
		$(obj).attr("class","bg_hidden");
		$(obj).next().next().hide();
	}
}
</script>
<div class="left_menu">
		<%Map<String, Object> map = UserUtil.getMenu(request);
			String a=map.get("data").toString();
			List<Map<String, Object>> list=CookieUtil.getList(a);%>
    	<ul>
        	<li><span><a href="<%=request.getContextPath()%>/userbackstage/index" class="bg_01">首页</a></span><div class="line"></div></li>
            <%for(Map<String,Object> menu:list){
            	if(String.valueOf(menu.get("parentid")).equals("0")){%>
           			<li>
	 					<span class="bg_hidden" onclick="changemenu(this)"><a class="<%=menu.get("remark") %>" href="javascript:void(0)"><%=menu.get("name") %></a></span><div class="line"></div>
			 				<ul class="undis">
			 				<%for(Map<String,Object> me:list){
            					if(menu.get("functionid").equals(me.get("parentid"))){%>
          					 			<li>
										 	<span style="cursor: pointer;" id="<%=me.get("cssname")%>"><a href="<%=request.getContextPath()+me.get("linkurl")%>"><%=me.get("name")%></a></span><div class="line"></div>
										</li>
            					<%}}%>
			 				</ul>
		 			</li>
            	<%}}%>
        </ul>
	</div>