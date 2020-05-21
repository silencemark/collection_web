<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>轮播图-修改</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
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
	
var decide = 0;//0是修改,1是新增
function checkBanner(){
	$("#fileName").click();
}	
function ajaxFileUpload(id,Fileid,noimg){
	if(!Fileid){
		Fileid = "fileName";
	}
	hhutil.ajaxFileUpload("<%=request.getContextPath()%>/upload/manageheadimg",Fileid,function(data){
	if(data.imgkey){
		if(decide==0){
			$("#imgurl").attr("src",data.imgkey);
		}else{
			var param = new Object();
			param.imgurl = data.imgkey;
			param.model = '${model}';
			$.ajax({
				type:'post',
				url:'<%=request.getContextPath() %>/managebackstage/insertManageBanner',
				data:param,
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
							showCancelButton : false,
							confirmButtonColor : "#ff7922",
							confirmButtonText : "确认",
							cancelButtonText : "取消",
							closeOnConfirm : true
						}, function(){
							location.href="/managebackstage/getManageBannerDetail?model=${model}";
						});
						
					}
				}
			})
		}
		
	}else{
		swal({
			title : "",
			text : "图片上传失败",
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
	});
}

function checkUpdate(id){
	 decide = 0;
	var param = new Object();
	param.id=id;
	$.ajax({
		type:'post',
		url:'<%=request.getContextPath() %>/managebackstage/getBannerDetail',
		data:param,
		success:function(data){
			if(data.status==1){
				swal({
					title : "",
					text :	"查询失败",
					type : "error",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					
				});
			}else{
				$(".div_mask").show();$(".tc_addbanner").show();
				if(data.datalist!= "" && data.datalist.length>0) {
					$("#httpurl").val(data.datalist[0].httpurl);
					$("#imgurl").attr("src",data.datalist[0].imgurl);
					$("#bannerid").val(data.datalist[0].id);
				}
				
			}
		}
	})
}
	
function checkDel(id){
	var param = new Object();
	param.id=id;
	param.delflag=1;
	$.ajax({
		type:'post',
		url:'<%=request.getContextPath() %>/managebackstage/updateManageBanner',
		data:param,
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
				swal({
					title : "",
					text :	"删除成功",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					location.href="/managebackstage/getManageBannerDetail?model=${model}";
				});
			}
		}
	})
}	
	

	
	
	
	
function checkupload(){
	decide = 1;
	var index = 2;
	if('${model}'==1){
		index = 4;
	}
	if($(".flash_imgedit img[name='imgurl']").length < index){
		checkBanner();
		$("#httpurl").attr("src","");
		$("#imgurl").attr("src","");
	}else{
		swal({
			title : "",
			text :	"图片最大上传"+index+"张",
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
	
function funUpdata(){
	var httpurl="",imgurl="",id="";
	httpurl =$("#httpurl").val();
	imgurl = $("#imgurl").attr("src");
	id = $("#bannerid").val();
	
	var param = new Object();
	param.id=id;
	param.httpurl=httpurl;
	param.imgurl=imgurl;
	$.ajax({
		type:'post',
		url:'<%=request.getContextPath() %>/managebackstage/updateManageBanner',
		data:param,
		success:function(data){
			if(data.status==1){
				swal({
					title : "",
					text :	"修改失败",
					type : "success",
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
					location.href="/managebackstage/getManageBannerDetail?model=${model}";
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

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="#">企业管理</a><i>/</i><a href="#">轮播图</a><i>/</i><span>图片编辑</span></p></div>  
    <div class="page_tab m_top">
 		
    
     	<div class="tab_name">

   			 <c:choose>
                <c:when test="${ model == 1 }">
                 	<span class="gray1">首页</span>
                </c:when>
                <c:when test="${model == 2 }">
                 <span class="gray1">采购管理</span>
                </c:when>
               <c:when test="${model == 3 }">
                <span class="gray1">仓库管理</span>
                </c:when>
                 <c:when test="${model == 4 }">
                 <span class="gray1">工作表单</span>
                </c:when>
                  <c:when test="${model == 5 }">
                 <span class="gray1">OA办公</span>
                </c:when>
                
                  <c:when test="${model == 6 }">
                 <span class="gray1">店面管理</span>
                </c:when>
                  <c:when test="${model ==7 }">
                 <span class="gray1">KPI星级考核</span>
                </c:when>
            </c:choose>
      			<a href="#" class="wid_01" onclick="checkupload()">上传Banner</a><a href="<%=request.getContextPath()%>/managebackstage/getManageBanner" class="wid_01" >返回</a></div>
      	
      	
        <div class="flash_imgedit" >
        <div id="flash_imgedit">
            <c:forEach items="${datalist}"  var ="list">
                    <span ><img src="${list.imgurl}" width="232" height="104"  onclick="checkUpdate('${list.id}')" name="imgurl" />
                    <a href="#" class="del"><img src="/userbackstage/images/public/del.png" alt="删除"   onclick="checkDel('${list.id}')" /></a></span>
           </c:forEach>
         </div>
           <input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/>
       
       	<div class="clear"></div>
       
        <!--
            <div class="a_btn">
            	<a href="#" class="bg_yellow">保存</a>
                <a href="<%=request.getContextPath()%>/managebackstage/getManageBanner" class="bg_gay2" >取消</a>
            </div>
             -->
        </div>
         
    </div>
</div>
<div class="div_mask" style="display:none;"></div>
<div class="tc_addbanner" style="display:none;font-size: 12px;">
	<div class="tc_title"><span>上传Banner图片</span><a href="#" onclick="$('.div_mask').hide();$('.tc_addbanner').hide();">×</a></div>
    <div class="tab_list">
    	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="none_border">
        	<tr>
        		<input type="hidden" class="text" id="bannerid"/>
            	<td>Banner地址</td>
                <td><input type="text" class="text" id="httpurl"/></td>
            </tr>
            <tr>
            	<td>Banner图片</td>
                <td>
                	<div class="pic"><img src="" width="102" id="imgurl"/></div>
                    <div class="xx">
                    	<a href="#" onclick="checkBanner()">上传</a>
                        <span>上传尺寸：4:3</span>
                    </div>
                </td>
            </tr>
        </table>
    </div>
    <div class="tc_btnbox"><a href="#"  onclick="$('.div_mask').hide();$('.tc_addbanner').hide();" class="bg_gay2">取消</a><a href="#" class="bg_yellow" onclick ="funUpdata()">保存</a></div>
</div>

</body>
</html>
