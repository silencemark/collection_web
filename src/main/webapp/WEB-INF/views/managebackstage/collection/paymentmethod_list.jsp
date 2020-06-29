<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>收款方式管理-管理方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/hhutil.js"></script>
<script src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>  
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
	$('#paymentlist').parent().parent().find("span").attr("class","bg_hidden");
	$('#paymentlist').attr('class','active li_active');
})

//显示隐藏支付凭证
function checkPayOrderShowHide(rum,payorder){
	if(rum == 1 ){
		$("#payorderdiv").hide();	
		$(".div_mask").css("display","none");
	}else{
		$("#payorderdiv").show();	
		$(".div_mask").css("display","block");
		$("#payorder").attr("src", payorder);
	}
}

</script>
</head>

<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">收款方式管理</a><i>/</i><span>收款方式列表</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">收款方式管理列表</span></div>
        <div class="sel_box">
        	<form action="<%=request.getContextPath()%>/managebackstage/getPaymentMethodList" method="post">
        		<input type="text" class="text" placeholder="请输入收款人姓名" name="realname" value="${map.realname}"/>
	            <input type="submit" value="搜索" class="find_btn"  />
            </form>
            <div class="clear"></div>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
                	<td>用户昵称</td>
                    <td>微信号</td>
                    <td>微信收款码</td>
                    <td>微信收款人姓名</td>
                    <td>支付宝账号</td>
                    <td>支付宝收款码</td>
                    <td>支付宝收款人姓名</td>
                    <td>开户行</td>
                    <td>银行卡号</td>
                    <td>持卡人真实姓名</td>
                    <td>修改时间</td>
                    <td>操作</td>
                </tr>
                <c:forEach items="${list }" var="li">
                	<tr>
	                	<td>${li.realname }</td>
	                    <td>V${li.levelenum }</td>
	                    <td>${li.mingrowthvalue }</td>
	                    <td>${li.maxgrowthvalue }</td>
	                    <td>${li.interesttimes }倍</td>
	                    <td>${li.updatetime }</td>
	                    <td><a href="javascript:void(0)" onclick="update('${li.levelid}','${li.levelname}','${li.levelenum}','${li.mingrowthvalue}','${li.maxgrowthvalue}','${li.interesttimes}')" class="blue">修改</a></td>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div>
    </div>
</div>

<div class="div_mask" style="display:none;"></div>

<div class="tc_changetext"  id="updatediv"  style="display:none;top: 40%;left: 40%; width: 650px;">
	<div class="tc_title"><span>修改会员等级信息</span><a href="#" onclick="checkHide(0)">×</a></div>
    <form action="<%=request.getContextPath() %>/managebackstage/updateLevel" id="updateform" method="post">
    <input type="hidden" name="levelid"  id="levelid"/>
    <div class="box">
        <span>会员等级名称</span>
        <input type="text" class="text2"  placeholder="请输入会员等级名称"  name="levelname" id="levelname123" />
        <div class="clear"></div>
        <span>会员等级</span>
        <input type="text" class="text2" readonly="readonly" id="levelenum"/>
        <div class="clear"></div>
        <span>最小成长值</span>
        <input type="text" class="text2" placeholder="请输入最小成长值" name="mingrowthvalue" id="mingrowthvalue"/> 
        <div class="clear"></div>
        <span>最大成长值</span>
        <input type="text" class="text2" placeholder="请输入最大成长值" name="maxgrowthvalue" id="maxgrowthvalue"/> 
        <div class="clear"></div>
        <span>收益倍数</span>
        <input type="text" class="text2" placeholder="请输入收益倍数" name="interesttimes" id="interesttimes"/> 
        <div class="clear"></div>
    </div>
    </form>
    <div class="tc_btnbox"><a href="#" class="bg_gay2" onclick="checkHide(0)">取消</a><a href="#"  class="bg_yellow" onclick="checkHide(1)">确认</a></div>
</div>

<div class="tc_changetext"  id="imgdiv"  style="display:none;width: 560px;top:40%;">
	<div class="tc_title"><span>展示电影封面</span><a href="#" onclick="checkImageShowHide(0,'')">×</a></div>
    <div class="box">
    	<span>电影封面</span>
        <img id="imagesrc" width="400px" height="500px"></i>
        <div class="clear"></div>
    </div>
    <div class="tc_btnbox"><a href="#"  class="bg_yellow" onclick="checkImageShowHide(0,'')">确定</a></div>
</div>

<input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/>
<script type="text/javascript">
function ajaxFileUpload(id,Fileid,noimg){
	if(!Fileid){
		Fileid = "fileName";
	}
	hhutil.ajaxFileUpload("<%=request.getContextPath()%>/upload/manageheadimg",Fileid,function(data){
			if(data.imgkey){
				$("#coverimgurl").attr("src",data.imgkey);
				$("input[name=coverimg]").val(data.imgkey);
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
</script>
</body>
</html>
