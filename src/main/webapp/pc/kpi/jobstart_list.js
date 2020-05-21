var fangwen_url = "/pc/getEvaluateList";
var userid = "";
var companyid = "";

var obj = new Object();

$(function(){
	userid = $('#userid').val();
	companyid = $('#companyid').val();
	
	//初始化信息
	obj.receiveid = userid;
	obj.companyid = companyid;
	//
	queryNoExamineInfo(obj);
});

function fuzzySearch(){
	obj.currentPage=1;
	obj.status = $('#status').val();
	obj.beforetime = $('#starttime').val();
	obj.aftertime = $('#endtime').val();
	
	queryNoExamineInfo(obj);
}

function pageHelper(num){
	obj.currentPage = num;
	queryNoExamineInfo(obj);
}

//查询信息
function queryNoExamineInfo(obj){
	requestPost(fangwen_url,obj,function(resultMap){
		$('#starlist').empty();
		if(resultMap != undefined && resultMap != null){
			var nodata = '<div class="list_none"><span><i class="yellow">Hi,我是大狮！</i><br>列表空空，还没有岗位星值呢~</span><b><img src="/userbackstage/images/index/none_msg2.png" width="293" height="240"></b></div>';
			if($("#power3001010").val() != ""){
				nodata = '<div class="list_none"><span><i class="yellow">Hello,我是大狮！</i><br>还没有岗位星值，赶紧添加一条吧~</span><b><img src="/userbackstage/images/index/none_msg.png" width="293" height="240"></b></div>';
			}
			var list = resultMap.evaluatelist;
			if(list != null && list.length > 0){
				$(".list_none").remove();
				showData(list);
			}else{
				$(".list_none").remove();
				$("#starlist").parent().parent().append(nodata);
			}
		}
	});
}
//信息展示
function showData(list){
	if(list != null && list.length > 0){
		var htm = '';
		$.each(list,function(i,map){
			var status="";
			var shenhe="";
			if(map.status==0 || map.status=='0'){
				status='<td class="red">待处理</td>';
				if(map.examineuserid == userid){
					shenhe='<a href="'+projectpath+'/pc/jobStarCheck?evaluateid='+map.evaluateid+'" class="yellow">审核</a></td>';
				}
			}if(map.status==1 || map.status=='1'){
				if(map.result == 1){
					status = "<td class=\"green\">同意</td>";
             	}else{
             		status = "<td class=\"red\">拒绝</td>";
             	}
				shenhe +="<a href=\"javascript:void(0)\" class=\"red\" onclick=\"deleteForwardUserInfo(this,'"+map.forwarduserid+"')\">删除</a></td>";
			}
			var onclick_url = '?evaluateid='+map.evaluateid+"&userid="+userid+'&forwarduserid='+map.forwarduserid;
			htm += ' <tr>'+
		                '<td onclick="intoDetail(\''+onclick_url+'\',\''+map.evaluateid+'\',\''+map.isread+'\',6)">'+map.createname+'</td>'+
		                '<td>'+appBase.parseDateMinute(map.createtime)+'</td>'+
		                '<td>'+map.sumstar+'星</td>'+
		                status+
		                '<td><a href="'+projectpath+'/pc/jobStarDetail?evaluateid='+map.evaluateid+'&forwarduserid='+map.forwarduserid+'" class="link">查看</a>'+shenhe+
		            '</tr>';       
		});
		//显示
		$('#starlist').append(htm);
	}
}


//导出
function exportexcel(){
	 swal({
			title : "",
			text : "是否导出",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#ff7922",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(){
			$.ajax({
		        type: "POST",
		        url: "/pc/exportnotice",
		        data:param,
		        success: function(data){
		        	window.location.href="/userbackstage/downloadexcel?fileName="+data;
		        }
		    });
		});
		
	}