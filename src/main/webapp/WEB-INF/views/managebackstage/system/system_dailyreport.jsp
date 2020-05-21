<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>每日报表-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>

</head>
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
$(document).ready(function(){
	$('#system').parent().parent().find("span").attr("class","bg_hidden");
	$('#system').attr('class','active li_active');
})
</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">系统设置</a><i>/</i><span>每日报表</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">每日报表</span></div>
        <div class="detail_edit">
        	<c:forEach items="${datatypelist}" var="type" varStatus="st">
        		<span class="name2">${type.typename}</span>
            	<div class="fbfw wid">
            		<c:forEach items="${type.datalist}" var="data">
            			<i><span onclick="initupdate('${type.typename}','${data.dataname}','${data.dataid}',${data.isnum})">${data.dataname}</span><a href="javascript:void(0)" class="del" onclick="deldata(this,'${data.dataid}','${data.dataname}')"><img src="../userbackstage/images/public/del.png" alt="删除" /></a></i>
            		</c:forEach>
            		<a href="javascript:void(0)" onclick="initadd('${type.typename}',this,'${type.typeid}')" class="add">添加</a>
            	</div>
	            <div class="clear"></div>
	            <c:if test="${st.index != (datatypelist.size()-1)}">
	            <div class="line"></div>
	            </c:if>
        	</c:forEach>
        </div>
    </div>
</div>

<div class="div_mask" style="display:none;"></div>
<div class="tc_changetext" style="display:none;">
	<div class="tc_title"><span id="title">每日报表</span><a href="#" onclick="$('.tc_changetext').hide();$('.div_mask').hide();">×</a></div>
    <div class="box">
    	<input type="hidden" id="dataid" />
    	<span>输入类型</span>
    	<input type="hidden" id="isnum" value="1"/>
        <a href="javascript:void(0)" class="radio_ed"  onclick="changetype(this,1)" id="num1">选择</a><i class="i_dp">数字</i>
	    <a href="javascript:void(0)" class="radio"  onclick="changetype(this,0)" id="num2">选择</a><i class="i_dp">非数字</i>
        <div class="clear"></div>
        <span>名称</span>
        <input type="text" class="text2" placeholder="请输入名称" id="dataname" />
        <input type="hidden" id="typeid" />
        <div class="clear"></div>
    </div>
    <div class="tc_btnbox"><a href="#" class="bg_gay2" onclick="$('.tc_changetext').hide();$('.div_mask').hide();">取消</a>
    <a href="javascript:void(0)"  class="bg_yellow" onclick="submitdata()" id="insert">保存</a></div>
</div>
<script type="text/javascript">
function deldata(obj,dataid,dataname){
	swal({
		title : "",
		text : "是否删除？",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$.ajax({
			type:"post",
			datatype:"json",
			url:"<%=request.getContextPath()%>/managebackstage/updateDailyReportData",
			data:"delflag=1&dataid="+dataid+"&dataname="+dataname,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text : data.message,
						type : "error",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						
					})
				}else{
					location.reload();
				}
			}
		})
	})
}
function initupdate(titlename,dataname,dataid,isnum){
	$('#title').text(titlename);
	$('#dataname').val(dataname);
	$('#dataid').val(dataid);
	$('#isnum').val(isnum);
	if(isnum==1){
		$('#num1').attr("class","radio_ed");
		$('#num2').attr("class","radio");
	}else{
		$('#num1').attr("class","radio");
		$('#num2').attr("class","radio_ed");
	}
	$('.tc_changetext').show();
	$('.div_mask').show();
}
function changetype(obj,type){
	$(obj).parent().find("a").attr("class","radio");
	$(obj).attr("class","radio_ed");
	$("#isnum").val(type);
}
function initadd(titlename,obj,typeid){
	$('#title').text(titlename);
	$('.tc_changetext').show();
	$('.div_mask').show();
	$('#typeid').val(typeid);
	$('#dataname').val("");
	$('#dataid').val("");
	$('#isnum').val(1);
	$('#num1').attr("class","radio_ed");
	$('#num2').attr("class","radio");
}
function submitdata(){
	var dataid=$('#dataid').val();
	var isnum=$('#isnum').val();
	var dataname=$('#dataname').val();
	var typeid=$('#typeid').val();
	if(dataname==""){
		swal({
			title : "",
			text : "名称不能为空",
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		})
		return;
	}
	if(dataid == ""){
		$.ajax({
			type:"post",
			datatype:"json",
			url:"<%=request.getContextPath()%>/managebackstage/insertDailyReportData",
			data:"isnum="+isnum+"&dataname="+dataname+"&typeid="+typeid,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text : data.message,
						type : "error",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						
					})
				}else{
					swal({
						title : "",
						text : data.message,
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						location.reload();
					})
				}
			}
		})
	}else{
		$.ajax({
			type:"post",
			datatype:"json",
			url:"<%=request.getContextPath()%>/managebackstage/updateDailyReportData",
			data:"isnum="+isnum+"&dataname="+dataname+"&dataid="+dataid,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text : data.message,
						type : "error",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						
					})
				}else{
					swal({
						title : "",
						text : data.message,
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						location.reload();
					})
				}
			}
		})
	}
}
</script>
</body>
</html>