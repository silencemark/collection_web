<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手办专区信息管理-管理方后台</title>
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
	$('#membercard').parent().parent().find("span").attr("class","bg_hidden");
	$('#membercard').attr('class','active li_active');
})


//隐藏窗口
function checkHide(num){
	if(num == 1) {
		$('#memberform').submit();
	}else {
		$("#updatediv").hide();	
		$(".div_mask").css("display","none");
		$("#cardid").text("");
		$("#typename").text("");
		$("#cardicon").text("");
		$("#cardiconurl").attr("src", "");
		$("#cardimg").text("");
		$("#cardimgurl").attr("src", "");
		$("#minprice").text("");
		$("#maxprice").text("");
		$("#xgocoin").text("");
		$("#starttime").text("");
		$("#endtime").text("");
		$("#commentstartdays").text("");
		$("#watchdays").text("");
		$("#commentcount").text("");
		$("#yield").text("");
		$("#introduction").text("");
		$("#description").val("");
		$("#status").val("");
	}
}

//显示隐藏窗口
function update(cardid,typename,cardicon,cardimg,minprice,maxprice,xgocoin,starttime,endtime,commentstartdays,watchdays,commentcount,yield,introduction,description,status){
	$("#updatediv").show();	
	$(".div_mask").css("display","block");
	$("#cardid").val(cardid);
	$("#typename").val(typename);
	$("#cardicon").val(cardicon);
	$("#cardiconurl").attr("src", cardicon);
	$("#cardimg").val(cardimg);
	$("#cardimgurl").attr("src", cardimg);
	$("#minprice").val(minprice);
	$("#maxprice").val(maxprice);
	$("#xgocoin").val(xgocoin);
	$("#starttime").val(starttime);
	$("#endtime").val(endtime);
	$("#commentstartdays").val(commentstartdays);
	$("#watchdays").val(watchdays);
	$("#commentcount").val(commentcount);
	$("#yield").val(yield);
	$("#introduction").val(introduction);
	$("#description").val(description);
	$("#status").val(status);
}

</script>
</head>
<style>
	.box {
	  overflow: auto;
	  height: 400px;
	}
	.box::-webkit-scrollbar {
		border-width:1px;
	}
</style>
<body>
<jsp:include page="../top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" ></jsp:include>
	<div class="page_nav"><p><a href="#">手办专区信息管理</a><i>/</i><span>手办专区信息列表</span></p></div>        
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">手办专区信息管理列表</span></div>
        <div class="sel_box">
        	<form action="<%=request.getContextPath()%>/managebackstage/getMemberCardList" method="post">
        		<input type="text" class="text" placeholder="请输入手办专区名称" name="typename" value="${map.typename}"/>
	            <select class="sel" name="status">
	            	<option>全部</option>
	            	<option value="1" <c:if test="${map.status == '1' }">selected="selected"</c:if>>有效</option>
	            	<option value="0" <c:if test="${map.status == '0' }">selected="selected"</c:if>>无效</option>
	            </select>
	            <input type="submit" value="搜索" class="find_btn"  />
            </form>
            <div class="clear"></div>
        </div>
        <div class="tab_list">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
                	<td>手办专区名称</td>
                    <td>手办专区图标</td>
                    <td>手办专区价格范围</td>
                    <td>购买需xgo币</td>
                    <td>购买开始结束时分</td>
                    <td>需要几天能出售</td>
                    <td>总观看期限(天)</td>
                    <td>需好评次数</td>
                    <td>收益率</td>
                    <td>修改时间</td>
                    <td>是否有效</td>
                    <td>操作</td>
                </tr>
                <c:forEach items="${list }" var="li">
                	<tr>
	                	<td>${li.typename }</td>
	                    <td><img src="${li.cardicon }" alt="" width="80px" height="60px" /></td>
	                    <td>${li.minprice }元 - ${li.maxprice }元</td>
	                    <td>${li.xgocoin}</td>
	                    <td>${li.starttime}:00 - ${li.endtime}:00</td>
	                    <td>${li.commentstartdays}</td>
	                    <td>${li.watchdays}</td>
	                    <td>${li.commentcount}</td>
	                    <td>${li.yield}%</td>
	                    <td>${li.updatetime }</td>
	                    <td>${li.status ==1?'有效':'无效' }</td>
	                    <td><a href="javascript:void(0)" onclick="update('${li.cardid}','${li.typename}','${li.cardicon}','${li.cardimg}','${li.minprice}','${li.maxprice}','${li.xgocoin}','${li.starttime}','${li.endtime}','${li.commentstartdays}','${li.watchdays}','${li.commentcount}','${li.yield}','${li.introduction}','${li.description}','${li.status}')" class="blue">修改</a>
	                    	<a href="<%=request.getContextPath()%>/managebackstage/getGarageKitList?cardid=${li.cardid}" class="blue">编辑专区手办信息</a>
	                    	<a href="<%=request.getContextPath()%>/managebackstage/getRateList?cardid=${li.cardid}" class="blue">编辑专区抢购概率 </a>
	                    </td>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <div id="Pagination" style="width:450px;">${pager }</div>
    </div>
</div>

<div class="div_mask" style="display:none;"></div>

<div class="tc_changetext"  id="updatediv"  style="display:none;top: 40%;left: 40%; width: 650px;">
	<div class="tc_title"><span>修改手办专区信息</span><a href="#" onclick="checkHide(0)">×</a></div>
    <form action="<%=request.getContextPath() %>/managebackstage/updateMemberCard" id="memberform" method="post">
    <input type="hidden" name="cardid" id="cardid" />
    <div class="box">
    	<span>手办专区名称</span>
        <input type="text" class="text2"  placeholder="请输入会员VIP名称"  name="typename" id="typename"/>
        <div class="clear"></div>
    	<span>手办专区图标</span>
    	<input type="hidden" name="cardicon" id="cardicon" />
        <div class="img3"><img src="" onclick="$('#fileName').click();" width="150px" height="80px" id="cardiconurl"/></div>
        <div class="clear"></div>
        <span>手办专区大图</span>
    	<input type="hidden" name="cardimg" id="cardimg"/>
        <div class="img3"><img src="" onclick="$('#fileName1').click();" width="150px" height="80px" id="cardimgurl"/></div>
        <div class="clear"></div>
        <span>简介</span>
        <input type="text" class="text2"  placeholder="请输入简介"  name="introduction" id="introduction"/>
        <div class="clear"></div>
        <span>详细描述</span>
        <textarea placeholder="请输入详细描述，最多允许输入800字符" maxlength="800" cols="43" style="border: 1px solid #eee;" rows="2" name="description" id="description"></textarea>
        <div class="clear"></div>
        <span>最小价格</span>
        <input type="text" class="text2"  placeholder="请输入手办专区最小价格"  name="minprice" id="minprice"/>
        <div class="clear"></div>
        <span>最大价格</span>
        <input type="text" class="text2"  placeholder="请输入手办专区最大价格"  name="maxprice"  id="maxprice"/>
        <div class="clear"></div>
        <span>所需XGO币</span>
        <input type="text" class="text2"  placeholder="请输入所需XGO币"  name="xgocoin"  id="xgocoin" />
        <div class="clear"></div>
        <span>购买开始时分</span>
        <input type="text" class="text2"  placeholder="请输入购买开始时分"  name="starttime" id="starttime"/>
        <div class="clear"></div>
        <span>购买结束时分</span>
        <input type="text" class="text2"  placeholder="请输入购买结束时分"  name="endtime" id="endtime"/>
        <div class="clear"></div>
        <span>几天后可出售</span>
        <input type="text" class="text2"  placeholder="请输入几天才开始出售"  name="commentstartdays" id="commentstartdays"/>
        <div class="clear"></div>
        <span>观看期限(天)</span>
        <input type="text" class="text2"  placeholder="请输入观看期限（天）"  name="watchdays"  id="watchdays"/>
        <div class="clear"></div>
        <span>所需好评次数</span>
        <input type="text" class="text2"  placeholder="请输入所需好评次数"  name="commentcount" id="commentcount"/>
        <div class="clear"></div>
        <span>收益率</span>
        <input type="text" class="text2"  placeholder="请输入收益率(%)"  name="yield" id="yield" />
        <div class="clear"></div>
        <span>是否有效</span>
        <select class="sel" name="status" id="status">
           	<option value="1">有效</option>
           	<option value="0">无效</option>
         </select>
        <div class="clear"></div>
    </div>
    </form>
    <div class="tc_btnbox"><a href="#" class="bg_gay2" onclick="checkHide(0)">取消</a><a href="#"  class="bg_yellow" onclick="checkHide(1)">确认</a></div>
</div>


<input type="file" name="myfiles" style="display: none" id="fileName" T="file_headimg" onchange="ajaxFileUpload('img')"/>
<input type="file" name="myfiles" style="display: none" id="fileName1" T="file_headimg" onchange="ajaxFileUpload1('img')"/>
<script type="text/javascript">
function ajaxFileUpload(id,Fileid,noimg){
	if(!Fileid){
		Fileid = "fileName";
	}
	hhutil.ajaxFileUpload("<%=request.getContextPath()%>/upload/managebannerimg",Fileid,function(data){
			if(data.imgkey){
				$("#cardiconurl").attr("src",data.imgkey);
				$("input[name=cardicon]").val(data.imgkey);
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

function ajaxFileUpload1(id,Fileid,noimg){
	if(!Fileid){
		Fileid = "fileName1";
	}
	hhutil.ajaxFileUpload("<%=request.getContextPath()%>/upload/managebannerimg",Fileid,function(data){
			if(data.imgkey){
				$("#cardimgurl").attr("src",data.imgkey);
				$("input[name=cardimg]").val(data.imgkey);
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
