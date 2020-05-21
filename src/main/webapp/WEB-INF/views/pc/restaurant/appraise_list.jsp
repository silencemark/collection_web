<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>顾客满意度</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/pc_page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css"/>
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/constantpc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/app/appcssjs/script/cache.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organizeRadioPC.js"></script>
</head>
<script type="text/javascript">
$(document).ready(function(){
	$('.restaurant_li').find("li").attr('class','');
	$('#appraise').attr('class','active');
	$('#homepage').parent().find("li").attr('class','');
	$('#homepage').attr('class','active');
})
</script>
<body>
<jsp:include page="../top.jsp"></jsp:include>
<div class="page_main">
	<jsp:include page="left.jsp"></jsp:include>
    <div class="right_page">
    	<div class="page_name"><span>顾客满意度</span><a href="<%=request.getContextPath() %>/pc/getMyStoreEvaluate" class="wid_01">我的关注</a></div>
        <div class="page_tab">
            <div class="sel_box">
            	<form action="<%=request.getContextPath() %>/pc/getStoreEvaluateList" id="searchform" method="post">
                <input type="hidden" name="organizeid" value="${map.organizeid}" id="organizeid">
	            <input type="text" class="text" name="organizename" id="organizename" onclick="$('.tc_structure').show();$('.div_mask').show();" readonly="readonly" value="${map.organizename}"/>
                <input type="text" class="text_time" placeholder="请选择时间" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${map.createtime}" name="createtime"/>
                <input type="submit" value="" class="find_btn"  />
                </form>
                <div class="clear"></div>
            </div>
            <div class="tab_list">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr class="head_td">
                        <td width="426">评论时间</td>
                        <td width="416">综合得分</td>
                        <td>操作</td>
                    </tr>
           
                    <c:forEach items="${datalist }" var="item">
                    	<tr>
                    	<td colspan="3" class="appraise_td">
                        	<div class="appraise">
                            	<div class="xx"><span>${item.createtime}</span><span class="num">${item.averagescore}分</span><i>
                            	<c:choose>
                            		<c:when test="${item.isfw==0 }">
                            		<a href="javascript:void(0)" onclick="attention('${userInfo.userid}','${item.evaluateid}','${item.companyid}','${item.isfw}',this)" class="green">关注</a></i>
                            		</c:when>
                            		<c:otherwise>
                            		<a href="javascript:void(0)" onclick="attention('${userInfo.userid}','${item.evaluateid}','${item.companyid}','${item.isfw}',this)" class="red">取消关注</a></i>
                            		</c:otherwise>
                            	</c:choose>
                            	</div>
                                <div class="box">
                                <div class="star_list">
                                	<div class="star"><span>口味得分</span><b>
                                	<c:forEach begin="0" end="${item.tastescore-1}" >
                                		<img src="../userbackstage/images/pc_page/start2.png" width="20" height="20" />
                                	</c:forEach>
                                	<c:forEach begin="${item.tastescore}" end="4">
                                		<img src="../userbackstage/images/pc_page/start3.png" width="20" height="20" />
                                	</c:forEach></b></div>
                                    <div class="star"><span>环境得分</span><b><c:forEach begin="0" end="${item.environmentscore-1}">
                                		<img src="../userbackstage/images/pc_page/start2.png" width="20" height="20" />
                                	</c:forEach>
                                	<c:forEach begin="${item.environmentscore}" end="4">
                                		<img src="../userbackstage/images/pc_page/start3.png" width="20" height="20" />
                                	</c:forEach></b></div>
                                    <div class="star"><span>服务得分</span><b><c:forEach begin="0" end="${item.servicescore-1}">
                                		<img src="../userbackstage/images/pc_page/start2.png" width="20" height="20" />
                                	</c:forEach>
                                	<c:forEach begin="${item.servicescore}" end="4">
                                		<img src="../userbackstage/images/pc_page/start3.png" width="20" height="20" />
                                	</c:forEach></b></div>
                                    <div class="star last"><span>价格得分</span><b><c:forEach begin="0" end="${item.pricescore-1}">
                                		<img src="../userbackstage/images/pc_page/start2.png" width="20" height="20" />
                                	</c:forEach>
                                	<c:forEach begin="${item.pricescore}" end="4">
                                		<img src="../userbackstage/images/pc_page/start3.png" width="20" height="20" />
                                	</c:forEach></b></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="span">
                                	<span class="user">${item.name}</span>
                                    <span class="tel">${item.phone}</span>
                                    <span class="desk">${item.seat}</span>
                                    <div class="clear"></div>
                                </div>
                                <div class="txt">${item.opinion}</div>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </c:forEach>
                </table>
                <c:if test="${fn:length(datalist) == 0 }">
                	<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>还没有顾客满意度信息的~</span><b><img src="<%=request.getContextPath() %>/userbackstage/images/index/none_msg2.gif" width="293" height="240"></b></div>
                </c:if>
            </div>
            <div id="Pagination" style="width:450px;">${pager }</div><!--动态的获取pagination的宽度赋值给Pagination-->
        </div>
    </div>
    <div class="clear"></div>
</div>
<div class="div_mask" style="display: none"></div>
<div class="tc_structure" style="display: none">
	<div class="tc_title"><span>选择组织架构店</span><a href="javascript:void(0);" onclick="$('.tc_structure').hide();$('.div_mask').hide();" >×</a></div>
    <div id="organizetree1"  style="overflow:hidden;overflow-y:visible;"></div>
    <div class="tc_btnbox"><a href="javascript:void(0);" onclick="$('.tc_structure').hide();$('.div_mask').hide();"  class="bg_gay2">取消</a>
    </div>
</div>
<script type="text/javascript">
function chooseorganize(orgid,orgname){
	$('.tc_structure').hide();
	$('.div_mask').hide();
	$('#organizeid').val(orgid);
	$('#organizename').val(orgname);
	changeOrganizeid(orgid,orgname);
}
//关注
function attention(createid,evaluateid,companyid,isfollow,obj){
	var opt = {
			createid:createid,
			evaluateid:evaluateid,
			companyid:companyid,
			isattention:isfollow
	}
	$.ajax({
		url:"<%=request.getContextPath()%>/app/followStoreEvaluate",
		type:"post",
		data:opt,
		success:function(data){
          	//console.log(data);	
          	if(data.status+"" == "0"){
          		var txt = "";
          		if(data.isfollow+"" == "0"){
          			$(obj).attr("class","green");
          		    txt = "关注";
          		}else if(data.isfollow+"" == "1"){
          			txt = "取消关注";
          			$(obj).attr("class","red");
          		}
          		$(obj).attr("onclick","attention(\'"+createid+"\',\'"+evaluateid+"\',\'"+companyid+"\',\'"+data.isfollow+"\',this)").text(txt);
          	}else{
          		swal({
					title : "",
					text : "操作失败，请稍后再试",
					type : "error",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					 
				});
          	}
			
		},error:function(data){
			   swal({
					title : "",
					text : "操作失败，请稍后再试",
					type : "error",
					showCancelButton : false,
					confirmButtonColor : "#ff7922",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : true
				}, function(){
					 
				});
		}
	});
}
</script>
</body>
</html>