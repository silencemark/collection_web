<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无权限提示内容设置</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript">
	var pageWidth = window.innerWidth;
	var pageHeight = window.innerHeight;
	var minHeight;
	var usernameWidth;
	if (typeof pageWidth != "number")
	{
		if(document.compatMode == "CSS1Compat")
		{
			pageWidth = document.documentElement.clientWidth;
			pageHeight = document.documentElement.clientHeight;
		}
		else
		{
			pageWidth = document.body.clientWidth;
			pageHeight = document.body.clientHeight;
		}
	}
	minHeight = pageHeight - 77;
	$(document).ready(function(){		
		$(".main_page").css("min-height",minHeight+"px");
		
		usernameWidth = $(".top_username").width();		
		usernameWidth = (160 - usernameWidth)/2;
	    $(".top_username").css("margin-left",usernameWidth+"px");		
		$(".head_box .user_box").hover(
			  function () {
				$(".head_box .user_box .name").addClass("name_border"); //移入
				$(".head_box .user_box .box").show();
			  },
			  function () {
				$(".head_box .user_box .name").removeClass("name_border");//移除
				$(".head_box .user_box .box").hide();
			  }
			);			
});
	
	
	function checkInsert(){
		var list = [];
		var param = new Object();
		var td = $('td textarea[name="datacode"]');
		$.each(td,function(i,item){
			var datacode = $(item).attr("datacode");
			var remark = $(item).val();
			list[i] = {datacode:datacode, remark:remark};
		});
		param.list = JSON.stringify(list);
		$.ajax({
			type:'post',
			url:'<%=request.getContextPath() %>/managebackstage/updateSystemDictPower',
			data:param,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text :	"修改失败",
						type : "error",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						
					});
				}else{
					swal({
						title : "",
						text :	"修改成功",
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						location.href="/managebackstage/getSystemDictPower";
					});
					
				}
			}
		})
	}
	
	
$(document).ready(function(){
		$('#system').parent().parent().find("span").attr("class","bg_hidden");
		$('#system').attr('class','active li_active');
})
</script>
</head>

<body style="font-size: 12px;">
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">系统设置</a><i>/</i><span>无权限提示内容设置</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">无权限提示内容设置</span></div>
        <div class="tab_list" >
        	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">
        	<c:forEach items="${datalist}" var ="list">
        		  <tr class="noneborder" style="font-size: 12px;">
                    <td class="l_text" width="140" style="vertical-align:top"  >${list.cname} </td>
                    <td><textarea class="text_area" placeholder="请输入提示信息" name="datacode" datacode="${list.datacode }">${list.remark}</textarea></td>
                </tr>
        	</c:forEach>
                <tr class="foot_td">
                	<td>&nbsp;</td>
                    <td colspan="2"><a href="#" class="a_btn bg_yellow" onclick="checkInsert()">保存</a><a href="<%=request.getContextPath() %>/managebackstage/systemIndex" class="a_btn bg_gay2">取消</a></td>
                </tr>
            </table>
        </div>
    </div>
</div>
</body>
</html>
