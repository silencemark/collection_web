<%@ page language="java" contentType="text/html; charset=utf-8"
  pageEncoding="UTF-8"%>

<div class="left_menu">
    	<div class="user_xx">
    	   <div class="img"><img src="${user.headimage}"  width="128" height="128"  /></div>
            <div class="name"><span>${user.realname}</span><a href="<%=request.getContextPath() %>/pc/memcenter_infoedit">编辑</a></div>
		</div>
 		<div class="menu_name">KPI星级考核</div>
        <ul class="kpi_li">
        	<li id="jobstar"><a href="<%=request.getContextPath() %>/pc/getStarAssessList" class="bg_01">岗位星值</a></li>
            <li id="allsstar"><a href="<%=request.getContextPath() %>/pc/allstarList" class="bg_02">综合星值</a></li>
            <li id="rule"><a onclick="intoRangkingPage()" href="javascript:void(0)" class="bg_03">KPI星值排行</a></li>
        </ul>
        
        <script type="text/javascript">
        	function intoRangkingPage(){
        		var power = "${powerMap.power3001010}";
        		if(power == ""){
        			//查询提示框
        			$.ajax({
        				type:"post",
        				dataType:"json",
        				url:"<%=request.getContextPath() %>/app/getPromptInfo",
        				data:"datacode=personnopower",
        				success:function(data){
        					swal({
        						title : "",
        						text :  data.datamap.remark,
        						type : "warning",
        						showCancelButton : false,
        						confirmButtonColor : "#ff7922",
        						confirmButtonText : "确认",
        						cancelButtonText : "取消",
        						closeOnConfirm : true
        					}, function(){
        						
        					});
        					return;
        				}
        			})
        		}else{
        			location.href="<%=request.getContextPath() %>/pc/rankingList";
        		}
        	}
        </script>
</div>