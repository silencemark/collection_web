<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>字典管理</title>
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
$(document).ready(function(){
	$('#system').parent().parent().find("span").attr("class","bg_hidden");
	$('#system').attr('class','active li_active');
})

function checkDel(id){
	swal({
		title : "",
		text :	"是否删除",
		type : "error",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		functiondel(id);	
	});
}
function functiondel(id,cname){
	var parma = new Object ();
	parma.dataid = id;
	parma.delflag = 1;
	parma.cname=cname;
	$.ajax({
		type:'post',
		url:'<%=request.getContextPath() %>/managebackstage/updateSystemDict',
		data:parma,
		success:function(data){
			if(data.status==1){
				swal({
					title : "",
					text :	"删除失败",
					type : "error",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					
				});
			}else{
				
					$("#"+id+"").parent().parent().remove();
			
				
			}
		}
	})
}
var name="";
function checkSelect(cname,dataid,priority,typeid,templatename){
	name=name;
	$('.tc_changetext').show();$('.div_mask').show();
	$("#update").show();$("#insert").hide();
	if(typeid == "9" || typeid == "10" || typeid == "11" || typeid == "12" || typeid == "13"){
		$('[name="remark"]').show();
		$('#remark').val(templatename);
	}else{
		$('[name="remark"]').hide();
	}
	$("#cname").val(cname);$("#dataid").val(dataid);$("#priority").val(priority);
	
}
function checkupdate(){
	var parma = new Object();
	var cname ="" ,dataid="",priority="";
	cname=$("#cname").val();
	dataid=$("#dataid").val();
	priority=$("#priority").val();
	var mess="";
	if(cname==""){
		mess="请输入名称";
	}else if(priority == ""){
		mess = "请输入顺序";
	}
	parma.name=name;
	parma.cname= cname;
	parma.dataid= dataid;
	parma.priority= priority;
	parma.templatename = $('#remark').val();
	
	if(mess == ""){
		$.ajax({
			type:'post',
			url:'<%=request.getContextPath() %>/managebackstage/updateSystemDict',
			data:parma,
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
						showCancelButton : true,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						location.href="/managebackstage/getSystemDict";
					});
					
				}
			}
		})
	}else{
		swal({
			title : "",
			text :	mess,
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
	}
}
function checkAdd(typeid){
	$('.tc_changetext').show();$('.div_mask').show();
	$("#update").hide();$("#insert").show();
	if(typeid == "9" || typeid == "10" || typeid == "11" || typeid == "12" || typeid == "13"){
		$('[name="remark"]').show();
	}else{
		$('[name="remark"]').hide();
	}
	$("#cname").val("");$("#dataid").val(typeid);$("#priority").val("");$('#remark').val("");
}
function checkInsert(){
	var parma = new Object();
	var cname ="" ,priority="",typeid="";
	cname=$("#cname").val();
	typeid=$("#dataid").val();
	priority=$("#priority").val();
	var mess="";
	if(cname==""){
		mess="请输入名称";
	}else if(priority == ""){
		mess = "请输入顺序值";
	}
	parma.cname= cname;
	parma.priority= priority;
	parma.typeid=typeid;
	parma.templatename = $('#remark').val();
	
	if(mess == ""){
		var num = parseInt(typeid);
		if(num>=9 && num<=13){
			if(cname.length > 8){
				swal({
					title : "",
					text :	"名称不能超出8个字",
					type : "error",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
				});
				return false;
			}
		}
		$.ajax({
			type:'post',
			url:'<%=request.getContextPath() %>/managebackstage/insertSystemDict',
			data:parma,
			success:function(data){
				if(data.status==1){
					swal({
						title : "",
						text :	"新增失败",
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
						text :	"新增成功",
						type : "success",
						showCancelButton : true,
						confirmButtonColor : "#ff7922",
						confirmButtonText : "确认",
						cancelButtonText : "取消",
						closeOnConfirm : true
					}, function(){
						location.href="/managebackstage/getSystemDict";
					});
					
				}
			}
		})
	}else{
		swal({
			title : "",
			text :	mess,
			type : "error",
			showCancelButton : false,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			
		});
	}
}

</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="javascript:void(0)">首页</a><i>/</i><a href="javascript:void(0)">系统设置</a><i>/</i><span>字典管理</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">字典管理</span></div>
        <div class="detail_edit">
          	<c:forEach items="${dataList}" var="strlist"  varStatus="status">
          		<c:choose>
          			<c:when test="${status.index!=0}"><div class="line"></div></c:when>
          		</c:choose>	
          	 	<span class="name2">${strlist.cname}</span>
	          	 <div class="fbfw wid">
	          		  
	          	 
	          	 		<c:forEach items="${list}" var="listall" >
		          	 		<c:choose>
          						<c:when test="${listall.typeid==strlist.datatypeid}">
			           				<div><i><span style='cursor: pointer;' onclick="checkSelect('${listall.cname}','${listall.dataid}','${listall.priority}','${listall.typeid }','${listall.templatename }')" ><c:if test="${listall.templatename != undefined && listall.templatename != ''}">${listall.templatename}--</c:if> ${listall.cname}</span><a href="javascript:void(0)" class="del">
			           				<img src="../userbackstage/images/public/del.png" alt="删除" onclick="checkDel('${listall.dataid}','${listall.cname}')" id="${listall.dataid}"/></a></i></div>
          						</c:when>
          					</c:choose>	
	          	 		</c:forEach>
			           	<a href="javascript:void(0)" class="add" onclick="checkAdd('${strlist.datatypeid}')">添加</a> 
			     </div>
          		<div class="clear"></div>
          	</c:forEach>
        </div>
    </div>
</div>
<div class="div_mask" style="display:none;"></div>
<div class="tc_changetext" style="display:none;">
	<div class="tc_title"><span>字典管理</span><a href="javascript:void(0)" onclick="$('.tc_changetext').hide();$('.div_mask').hide();">×</a></div>
    <div class="box">
        <span >选项值</span>
        <input type="text" class="text2" placeholder="请输入名称" id="cname" />
        <input type="hidden" class="text2" id="dataid" />
        <div class="clear"></div>
        <span>顺序</span>
        <input type="text" class="text2" placeholder="请输入顺序" id="priority" />
        <div class="clear"></div>
        <span name="remark">检查模板名称</span>
        <input name="remark" type="text" class="text2" placeholder="请输入模板名称" id="remark" />
        <div name="remark" class="clear"></div>
    </div>
    <div class="tc_btnbox"><a href="javascript:void(0)" class="bg_gay2" onclick="$('.tc_changetext').hide();$('.div_mask').hide();">取消</a>
    <a href="javascript:void(0)"  class="bg_yellow" onclick="checkInsert()" id="insert">保存</a>
    <a href="javascript:void(0)"  class="bg_yellow" onclick="checkupdate()"  id="update">保存</a></div>
</div>
</body>
</html>
