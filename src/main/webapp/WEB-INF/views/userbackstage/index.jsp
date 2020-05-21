<%@page import="com.collection.util.UserUtil"%>
<%@page import="com.collection.util.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>首页-使用方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/app/appcssjs/sweetalert/dist/sweetalert-dev.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/organizeRadio.js"></script>
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
function inviteuser(userid,realname,e){
	swal({
		title : "",
		text : "是否邀请"+realname+"？",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$.ajax({
			type:"post",
			dataType:"json",
			url:"<%=request.getContextPath()%>/userbackstage/inviteUser",
			data:"userid="+userid+"&realname="+realname,
			success:function(data){
				if(data.status==0){					
					$(e).parent().prev().text(getnowtime());					
				}
			}
		})
	});
	
}
function getnowtime() {
    var nowtime = new Date();
    var year = nowtime.getFullYear();
    var month = padleft0(nowtime.getMonth() + 1);
    var day = padleft0(nowtime.getDate());
    var hour = padleft0(nowtime.getHours());
    var minute = padleft0(nowtime.getMinutes());
    var second = padleft0(nowtime.getSeconds());
    var millisecond = nowtime.getMilliseconds(); millisecond = millisecond.toString().length == 1 ? "00" + millisecond : millisecond.toString().length == 2 ? "0" + millisecond : millisecond;
    return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
}
//补齐两位数
function padleft0(obj) {
    return obj.toString().replace(/^[0-9]{1}$/, "0" + obj);
}
function chooseorganize(){
	$('.tc_structure').hide();
	$('.div_mask').hide();
	$('input[name=organizeid]').val($('#organizeid').val());
	$('input[name=organizename]').val($('#organizename').val());
}

function searchUnusedPerson(){
	var pageParam = new Object();
	var realname=$('#unusedUserName').val();
	var organizeid=$('input[name=organizeid]').val();
	pageParam.realname=realname;
	pageParam.organizeid=organizeid;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/userbackstage/searchUnusedPerson",
		data:pageParam,
		success:function(data){
			var temp="<table width=\"100\%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr class=\"head_td\"><td width=\"10%\">姓名</td><td width=\"10%\">职位</td><td width=\"15%\">联系电话</td><td width=\"25%\">区域/店面/部门</td><td width=\"16%\">上次邀请时间</td><td>操作</td></tr>";
			if(data.userlist.length>0){
				for(var i=0;i<data.userlist.length;i++){
					temp+="<tr><td>"+(data.userlist[i].realname==undefined?'':data.userlist[i].realname)+"</td><td>"+(data.userlist[i].position==undefined?'':data.userlist[i].position)+"</td><td>"+(data.userlist[i].phone==undefined?'':data.userlist[i].phone)+"</td><td>"+(data.userlist[i].organizename==undefined?'':data.userlist[i].organizename)+"</td><td>"+(data.userlist[i].invitetime==undefined?'':data.userlist[i].invitetime)+"</td><td><a href=\"javascript:void(0)\" class=\"link\" onclick=\"inviteuser(\'"+data.userlist[i].userid+"\',\'"+data.userlist[i].realname+"\',this)\">邀请</a></td></tr>";
				}
				temp+="</table><div id=\"Pagination\" style=\"width:315px;\">"+data.pager+"</div>";
				$('#searchResult').html(temp); 
			}else{
				//搜索无数据状态
				$('#searchResult').html("没有符合条件的信息");
			}
		}
	});
}
function deleteorganize(){
	$('input[name=organizename]').val('');
	$('input[name=organizeid]').val('');
	$('#unusedUserName').val('');	
}
</script>
</head>
<body>
<jsp:include page="top.jsp" ></jsp:include>
<div class="main_page">
	<jsp:include page="left.jsp" ></jsp:include>
	<div class="page_nav"><p><span>首页</span></p></div>
    <div class="index_top">
    	<div class="wid_01">
            <div class="user_num">
                <span><i>员工总人数</i><em class="yellow">${indexInfo.allusernum }</em><i>人</i><div class="clear"></div>
                </span>
                <span><i>未激活人员</i><em class="yellow">${indexInfo.notusernum }</em><i>人</i></span>
            </div>
        </div>
        <div class="wid_02">
            <div class="cp_num">
                <span><i>店面数</i><em class="yellow">${indexInfo.shopnum }</em><i>家</i><div class="clear"></div></span>
            </div>
        </div>
        <div class="wid_03">
        	<div class="capacity_num">
            	<span><i class="i_wid">云盘总空间</i><em class="yellow">${indexInfo.maxmemory }</em><i>G</i><div class="clear"></div></span>
                <span><i class="i_wid">剩余空间</i><em class="yellow">${indexInfo.maxmemory - indexInfo.usedmemory }</em><i>G</i></span>
            </div>
			<div class="link"><a href="<%=request.getContextPath() %>/userbackstage/intoAddCloudCapacity">扩容</a></div>
        </div>
        <div class="clear"></div>
    </div>    
    <div class="page_tab">
        <div class="tab_name"><span class="gray1">未使用系统人员</span></div>
        <div class="sel_box">
        	<input type="text" class="text" placeholder="请输入姓名" id="unusedUserName"  name="realname" value="${map.realname}"/>
        	<input type="hidden" name="organizeid" value="${map.organizeid}">
            <input type="text" class="text" placeholder="所在组织结构" name="organizename" value="${map.organizename}" onclick="$('.tc_structure').show();$('.div_mask').show();" readonly="readonly"/>
            <input type="button" class="find_btn" style="float:left;" value="清除" onclick="deleteorganize()"/>
            <input type="button" class="find_btn" value="搜索" onclick="searchUnusedPerson()"/>            
            <div class="clear"></div>
        </div>
        <div class="tab_list" id="searchResult">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            	<tr class="head_td">
                	<td width="10%">姓名</td>
                    <td width="10%">职位</td>
                    <td width="15%">联系电话</td>
                    <td width="25%">区域/店面/部门</td>
                    <td width="16%">上次邀请时间</td>
                    <td>操作</td>
                </tr>
                <c:forEach items="${userList}" var="item">
                <tr>
                	<td>${item.realname}</td>
                    <td>${item.position}</td>
                    <td>${item.phone}</td>
                    <td>${item.organizename}</td>
                    <td>${item.invitetime}</td>
                    <td><a href="javascript:void(0)" class="link" onclick="inviteuser('${item.userid}','${item.realname}',this)">邀请</a></td>
                </tr>	
                </c:forEach>
            </table>
            <div id="Pagination" style="width:450px;">${pager}</div><!--动态的获取pagination的宽度赋值给Pagination-->
        </div>        
    </div>
</div>
<div class="div_mask" style="display:none;"></div>
<div class="tc_structure" style="display:none;">
	<div class="tc_title"><span>选择组织架构店</span><a href="javascript:void(0);" onclick="$('.tc_structure').hide();$('.div_mask').hide();" >×</a></div>
    <input type="hidden" id="organizeid" />
	<input type="hidden" id="organizename" />
    <div id="organizetree1"  style="overflow:hidden;overflow-y:visible;"></div>
    <div class="tc_btnbox"><a href="javascript:void(0);" onclick="$('.tc_structure').hide();$('.div_mask').hide();"  class="bg_gay2">取消</a>
    <a href="javascript:void(0)"  onclick="chooseorganize();" class="bg_yellow">确定</a>
    </div>
</div>

</body>
</html>