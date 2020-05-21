<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<title>OA办公企业简报-使用方后台</title>
<link href="<%=request.getContextPath() %>/userbackstage/style/public2.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/userbackstage/style/page2.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/userbackstage/script/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/sweetalert/dist/sweetalert.css">
<script src="<%=request.getContextPath() %>/sweetalert/dist/sweetalert-dev.js"></script> 
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
				$(".head_box .user_box .name").removeClass("name_border");//移除\
				$(".head_box .user_box .box").hide();
			  }
			);	
		
		$('#company').parent().parent().find("span").attr("class","bg_hidden");
		$('#company').attr('class','active li_active');
});

	
	
	
	
	
	var param = new Object();
	$(function(){
		queryBriefList();
	});

	function pageHelper(num){
		param.currentPage = num;
//		$('#briefing_list').empty();
		queryBriefList();
	}

	function queryBriefList(){
		
		param.moduleid = "${map.moduleid}";
		param.title= $('#title').val();
		if(param.title != ""){
			param.currentPage = 1;
		}
		requestPost("/managebackstage/getManageBriefList",param,function(resultMap){
			if(resultMap != undefined && resultMap != null){
				$('#Pagination').html(resultMap.pager);
				if(resultMap.status == 0 || resultMap.status == '0'){
					var brieflist = resultMap.brieflist;
					$('#briefing_list').empty();
					if(brieflist != undefined && brieflist != null){
						showBriefInfo(brieflist);
					}
				}
			}
		});
	}

	function showBriefInfo(list){
		if(list != null && list.length > 0){
			$.each(list,function(i,map){
				var htm = '<li  style="cursor: pointer;" onclick="location.href=\'/managebackstage/intoManageBriefingDetialPage?briefid='+map.briefid+'\'">'+
				               '<div class="name"><a href="javascript:void(0)">'+map.title+'</a><i>'+map.createtime+'</i></div>'+
				               '<div class="txt" style="width:800px; text-overflow:ellipsis ; overflow:hidden; white-space:nowrap;"></div>'+
				           '</li>';
				$('#briefing_list').append(htm);
			});
		}
	}

	//公共的查询方法
	function requestPost(url,param,callback){
		$.ajax({
			url:url,
			type:"post",
			data:param,
			beforeSend:function(){
				var load = '<div id="load_mask" class="div_mask" style="opacity:0;"></div>'+
					      '<div id="load_loading" class="loading"><img src="../userbackstage/images/public/loading.gif" width="360" height="200" /></div>';
				$("body").after(load);
			},
			success:function(resultMap){
				callback(resultMap);
			},
			complete:function(){
				$('#load_mask').remove();
				$('#load_loading').remove();
			},
			error:function(e){
				
			}
		});
	}
</script>
</head>

<body>
<jsp:include page="../top.jsp" flush="true"></jsp:include>
<div class="main_page">
	<jsp:include page="../left.jsp" flush="true"></jsp:include>
   <div class="page_nav"><p><a href="#">首页</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyList">企业信息列表</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getCompanyInfo?companyid=${map.companyid}">公司信息</a><i>/</i><a href="<%=request.getContextPath() %>/managebackstage/getManageCompanyModuleList?companyid=${map.companyid}">企业简报栏目</a><i>/</i><span>企业简报列表</span></p></div>
    <input type="hidden" id="moduleid" value="${map.moduleid }"/>
    <div class="page_tab m_top">
        <div class="tab_name"><span class="gray1">企业简报列表</span><input type="button" value="搜索" onclick="queryBriefList()" class="btn" /><input type="text" class="text" placeholder="请输入关键字" id="title"/></div>
            	<div class="briefing_list">
                	<ul id="briefing_list">
<!--                     	<li> -->
<!--                         	<div class="name"><a href="#"></a><i>2016-06-02 10:30</i></div> -->
<!--                             <div class="txt">企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情企业简报详情</div> -->
<!--                         </li> -->
                    </ul>
                <div id="Pagination" style="width:450px;">
<!--                 <div class="pagination"><a href="#" class="prev_first">首页</a><a href="#" class="prev">上一页</a><a href="#" class="sp">1</a><a href="#">2</a><a class="current">3</a><a href="#">4</a><a href="#">5</a><a href="#" class="next">下一页</a><a href="#" class="next_last">尾页</a></div> -->
                </div><!--动态的获取pagination的宽度赋值给Pagination-->
            <div class="clear"></div>
        </div>
    </div>
</div>

</body>
</html>
