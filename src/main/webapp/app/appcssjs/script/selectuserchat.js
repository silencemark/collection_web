/**
 * 
 */


var selecthtml="<link type=\"text/css\" rel=\"stylesheet\" href=\"../appcssjs/style/public.css\">"+
"<link type=\"text/css\" rel=\"stylesheet\" href=\"../appcssjs/style/page_public.css\">"+
"<link type=\"text/css\" rel=\"stylesheet\" href=\"../appcssjs/style/msg.css\">"+
"<div style=\"display: none;width:100%;height:100%;\" id=\"organizeiframe\">"+
		"<input type=\"hidden\" id=\"userid\"/>"+
"<input type=\"hidden\" id=\"realname\"/>"+
"<div class=\"head_box\">"+
/*"<a class=\"ico_back\" onclick=\"$('#organizeiframe').hide();$('#htmlbody').show();\">返回</a>"+*/
	"<div class=\"name\">企业通讯录</div>"+
	"</div>"+
"<div class=\"head_none\"></div>"+

"<div class=\"msg_index\">"+
"<div class=\"msg_nav\">"+
"<span onclick=\"$('#morechange_organizeiframe').hide();showsharenum();onloaddata();$('#organizeiframe').hide();$('#htmlbody').show();\">聊天</span>"+
"<span onclick=\"$('#organizeiframe').hide();showsharenum();onloaddata();$('#morechange_organizeiframe').show();\">组织架构</span>"+
"<span class=\"last active\">通讯录</span>"+
    "<div class=\"clear\"></div>"+
"</div>"+
"</div>"+

"<div class=\"address_list\">"+
	"<div class=\"search_box\"><input id=\"organizetext\" type=\"text\" class=\"bg\" placeholder=\"搜索\" oninput=\"searchuser(this)\"></div>"+
"</div>"+
"<div class=\"address_word\" style=\"top:108px;\">"+
	"<a href=\"#A\"><span>A</span></a><a href=\"#B\"><span>B</span></a><a href=\"#C\"><span>C</span></a><a href=\"#D\"><span>D</span></a><a href=\"#E\"><span>E</span></a><a href=\"#F\"><span>F</span></a>"+
    "<a href=\"#G\"><span>G</span></a><a href=\"#H\"><span>H</span></a><a href=\"#I\"><span>I</span></a><a href=\"#J\"><span>J</span></a><a href=\"#K\"><span>K</span></a><a href=\"#L\"><span>L</span></a><a href=\"#M\"><span>M</span></a>"+
    "<a href=\"#N\"><span>N</span></a><a href=\"#O\"><span>O</span></a><a href=\"#P\"><span>P</span></a><a href=\"#Q\"><span>Q</span></a><a href=\"#R\"><span>R</span></a><a href=\"#S\"><span>S</span></a>"+
    "<a href=\"#T\"><span>T</span></a><a href=\"#U\"><span>U</span></a><a href=\"#V\"><span>V</span></a><a href=\"#W\"><span>W</span></a><a href=\"#X\"><span>X</span></a><a href=\"#Y\"><span>Y</span></a><a href=\"#Z\"><span>Z</span></a>"+
"</div>"+
$("#menu_bottom_hg").html()+
/*"<div class=\"bt_none\"></div>"+
"<div class=\"bt_menu\">"+
"<ul>"+
"<li><a href=\"../login/index.html\"><b class=\"bg_home\"></b><span>首页</span></a></li>"+
"<li class=\"dq\"><a ><b class=\"bg_msg\"></b><span>消息</span></a><em id=\"messagecount\" style=\"display:none\"></em></li>"+
"<li><a href=\"../memorandum/memorandum_index.html\"><b class=\"bg_memo\"></b><span>备忘录</span></a></li>"+
"<li><a href=\"../share/index.html\"><b class=\"bg_share\"></b><span>分享圈</span></a><em id=\"sharenum\" style=\"display: none;\"></em></li>"+
"<li><a href=\"../member/member_index.html\"><b class=\"bg_user\"></b><span>我的</span></a></li>"+
"</ul>"+
"</div>"+*/

"</div>";

$('#htmlbody').after(selecthtml);
onloadSelect();
function onloadSelect(){
	/*$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getNextOrgainize?companyid='+userInfo.companyid,
		success:function(data){
			if(data.organizelist.length > 0){
				$('#organizetext').text(data.organizelist[0].organizename);
				$('#organizetext').nextAll().remove();
				getorganizelist(0,data.organizelist[0].organizeid,data.organizelist[0].organizename,'#organizetext');
			}
		}
	})*/
	//查询当前条件下的人员信息
	var searchname=$("#organizetext").val()!=null && $("#organizetext").val()!=undefined?$("#organizetext").val():"";
	var companyid=userInfo.companyid;
	getorganizelist(companyid,searchname);
}
function getorganizelist(companyid,searchname){
	$(".address_list .search_box").nextAll().remove();
	$.ajax({
		type:'post',
		dataType:'json',
		url:projectpath+'/app/getNextOrgainizenew?companyid='+companyid+'&searchname='+searchname,
		success:function(data){
			$(".address_list .search_box").nextAll().remove();
			if(data!=null && data!=undefined && data!="" && data.userlist!=null && data.userlist!=undefined){
				var str="";
				var len=data.userlist.length;
				for(var i=0;i<len;i++){
					if(data.userlist[i].realnamelist!=null &&　data.userlist[i].realnamelist!=undefined && data.userlist[i].realnamelist.length>0){
						if(data.userlist[i].english!="empty"){
							str+="<div class=\"ul_name\"><a id=\""+data.userlist[i].english+"\" name=\""+data.userlist[i].english+"\"></a>"+(data.userlist[i].english!=null && data.userlist[i].english!=undefined && data.userlist[i].english!="empty"?data.userlist[i].english:"")+"</div>";	
						}
						str+="<ul>";
						var leng=data.userlist[i].realnamelist.length;
						for(var j=0;j<leng;j++){
							str+="<li>";
							str+="<span>"+(data.userlist[i].realnamelist[j].realname!=null && data.userlist[i].realnamelist[j].realname!=undefined?data.userlist[i].realnamelist[j].realname:"未知")+"</span>";
							//
							str+="<i onclick=\"callphone('"+data.userlist[i].realnamelist[j].phone+"',this)\"><a href=\"tel://"+data.userlist[i].realnamelist[j].phone+"\"><img src=\"../appcssjs/images/public/ico_tel2.png\"></a></i>";
							str+="<i onclick=\"sendmess('"+data.userlist[i].realnamelist[j].userid+"')\"><img src=\"../appcssjs/images/public/ico_msg.png\"></i>";
							/*str+="<div name=\"people\"><div class=\"user\" style=\"padding:0px 30px;height: 32px;color: #999; font-size: 12px;\"><span style=\"float: left;line-height: 32px;\">"
								+(data.userlist[i].realnamelist[j].realname!=null && data.userlist[i].realnamelist[j].realname!=undefined?data.userlist[i].realnamelist[j].realname:"未知")+
								"</span><i onclick=\"sendmess('"+data.userlist[i].realnamelist[j].userid+
								"')\" style=\"display: block;float: right;width: 20px;height: 20px;border: 1px solid #e5e5e5;padding: 3px;border-radius: 16px;margin: 6px 0px;\">" +
								"<img src=\"../appcssjs/images/public/ico_msg.png\" style=\"float:left;max-width: 100%;vertical-align: middle;border: 0;\"></i><i onclick=\"callphone('"+
								data.userlist[i].realnamelist[j].phone+"',this)\"  style=\"display: block;float: right;width: 20px;height: 20px;border: 1px solid #e5e5e5;padding: 3px;border-radius: 16px;margin: 6px 20px;\">" +
										"<img style=\"float:left;max-width: 100%;vertical-align: middle;border: 0;\" src=\"../appcssjs/images/public/ico_tel2.png\"></a></i></div></div>";*/
							str+="</li>";
						}
						str+="</ul>";
					}
				}
				
				$(".address_list").append(str);
				$(".address_word span").css({
					"height":"15px"
				});
			}
		}
	})
}
function changerealname(obj,userid,realname){
	$('a[class=radio_ed]').attr("class","radio");
	$(obj).find('a').attr("class","radio_ed");
	$('#userid').val(userid);
	$('#realname').val(realname);
}

function sendmess(userid){
	logical(userid);
}

function callphone(phone,obj){
	/*swal({
		title : "",
		text : "确认拨打电话"+phone+"?",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#ff7922",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(){
		$(obj).find("a[name=phone]").click();
	});*/
	console.log(phone);
	location.href="tel:"+phone;
}
function searchuser(){
	var searchname=$("#organizetext").val()!=null && $("#organizetext").val()!=undefined?$("#organizetext").val():"";
	var companyid=userInfo.companyid;
	getorganizelist(companyid,searchname);
}