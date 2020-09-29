var commentList = [
	{
		tx:'http://img5.imgtn.bdimg.com/it/u=3526059149,524929962&fm=26&gp=0.jpg',
		name:'W DeGaio',commTime:'2020/07/12',xin:1,version:'5.3.2.101',
		contentTxt:'为什么更新了老卡屏，有时候还闪退，差评能不能不给星，烦死人，我TM.....',
		zan:0, nzhan:0
	},{
		tx:'http://img1.imgtn.bdimg.com/it/u=2579660820,1492564183&fm=11&gp=0.jpg',
		name:'小飞同学',commTime:'2020/07/05',xin:5,version:'5.3.2.101',
		contentTxt:'非常棒，买了一个九尾狐的雅雅,一天直接转出去趟赚7块差价哈哈',
		zan:1, nzhan:0
	},{
		tx:'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597324447562&di=a3c4483eb0ce72dae277fea51e6893b5&imgtype=0&src=http%3A%2F%2Fimg.hao661.com%2Fzt.hao661.com%2Fuploads%2Fallimg%2F141005%2F0H304IT-0.jpg',
		name:'zt.MRs',commTime:'2020/07/01',xin:3,version:'5.3.2.101',
		contentTxt:'在做的都是弟弟，我是说所有人，便宜的我都不买直奔漫威专区，一个号每天净赚三百差不多',
		zan:3, nzhan:0
	},{
		tx:'https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2034740944,4251903193&fm=26&gp=0.jpg',
		name:'ai芙兰',commTime:'2020/06/22',xin:4,version:'5.3.1.103',
		contentTxt:'有要赚奖励的朋友加我好友，做我的粉丝我保证我宠你。V : yys3563,快点来快点来等我升级到V10再来的话就不宠你了',
		zan:20, nzhan:0
	},{
		tx:'http://img5.imgtn.bdimg.com/it/u=183326193,1784969774&fm=26&gp=0.jpg',
		name:'爱玩呼啦圈的小喵喵',commTime:'2020/06/20',xin:3,version:'5.3.1.103',
		contentTxt:'感觉还不错，但这个手办也太TM难得买了吧，靠，靠，靠',
		zan:208, nzhan:5
	},{
		tx:'http://img2.imgtn.bdimg.com/it/u=903791629,504409189&fm=11&gp=0.jpg',
		name:'狼也',commTime:'2020/06/13',xin:3,version:'5.3.1.103',
		contentTxt:'我就想知道主页里广告的那个女猪脚是谁，三分钟之内我要她的去全部信息，否则锤死在做的各位，egm',
		zan:12, nzhan:0
	}
];
function dn(){
	// var invitecode = getQueryVariable("invitecode");
	if(is_weixn()){
		$(".apremak").hide();
		$(".wbOpen").show();
	}else{
		var u = navigator.userAgent;
		var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
		if(isiOS){
			jqalert({
				prompt:'<div style="text-align:center">'+
				'非常抱歉：于相关政策及其他综合因素暂时取消对IOS系统兼容。因此对广大IOS用户带来的不便，我们深表歉意。'+
				'您可在iso-Safari浏览器中点击"<b>添加到主屏</b>"以方便使用本应用</div>',
				yestext:'前往应用',
				yesfn:function () {
					window.location.href='index.html';
				}
			})
		}else{
			downloadUrlFile('https://www.goaragekit.cn/download/xgomv.apk','xgoMv.apk');
		}
	}
}
function downloadUrlFile(url, fileName) {
	// const url2 = url.replace(/\\/g, '/');
    // const xhr = new XMLHttpRequest();
    // xhr.open('GET', url2, true);
    // xhr.responseType = 'blob';
    // //xhr.setRequestHeader(‘Authorization‘, ‘Basic a2VybWl0Omtlcm1pdA==‘);
    // xhr.onload = () => {
		//     if (xhr.status === 200) {
			window.location.href=url;
			//         // 获取文件blob数据并保存
    //         saveAs(xhr.response, fileName);
    //     }
    // };
    // xhr.send();
}
$(function(){
	$(".wbOpen").click(function(){
		$(".apremak").show();
		$(".wbOpen").hide();
	})
	$(".dwOptionEl").click(function(){
		$(".dwOptionEl").removeClass("dwOptionActive");
		$(this).addClass("dwOptionActive");
		if($(this).attr("tp")=="1"){
			$("#appInfo").show()
			$("#appcomment").hide()
		}else{
			$("#appInfo").hide()
			$("#appcomment").show()
		}
	});
	
	var commHtml = "";
	for(var i=0;i<commentList.length;i++){
		var c = commentList[i];
		var xinhtml = "";
		for(var j=1;j<=5;j++){
			if(j<=c.xin){
				xinhtml +='<img src="img/sXin.png" class="xin" />';
			}else{
				xinhtml +='<img src="img/eXin.png" class="xin" />';
			}
		}
		var eHtml = 
			'<div class="dwcommentCont">'+
				'<div class="dwLeft"> <img src="'+c.tx+'"/> </div>'+
				'<div class="dwright">'+
					'<div style="color:#393838;font-size:14px;">'+c.name+'</div>'+
					'<div style="color:#939393;font-size:12px;margin-top: 2px;">'+
						''+c.commTime+''+
					'</div>'+
				'</div>'+
				'<div class="clear"></div>'+
				'<div class="dwCommx">'+xinhtml+
					'<span style="color:#939393;font-size:12px;margin-left:20px;">版本号：'+c.version+'</span>'+
				'</div>'+
				'<div style="font-size:13px;color:#4f4f4f;padding:10px 10px 10px 0;">'+
					''+c.contentTxt+''+
				'</div>'+
				'<div class="bwzhan">'+
					'<img src="img/notzany.png" onclick="zan(this);" />&nbsp;<span>'+c.zan+'</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
					'<img src="img/notzann.png"  onclick="nzan(this);" />&nbsp;<span>'+c.nzhan+'</span>'+
				'</div>'+
				'<div class="clear"></div>'+
			'</div>';
		commHtml += eHtml;
	}
	commHtml += '<div style="font-size:13px;color:#777;padding:10px 10px 20px 0;text-align:center">下拉刷新</div>';
	$("#appcomment").html(commHtml);
})
function zan(obj){
	var z = parseInt($(obj).next().html());
	if($(obj).attr("src").indexOf("not")>=0){
		$(obj).attr("src","img/zanys.png")
		$(obj).next().html(z+1);
	}else{
		$(obj).attr("src","img/notzany.png");
		$(obj).next().html(z-1);
	}
}
function nzan(obj){
	var z = parseInt($(obj).next().html());
	if($(obj).attr("src").indexOf("not")>=0){
		$(obj).attr("src","img/zanns.png")
		$(obj).next().html(z+1);
	}else{
		$(obj).attr("src","img/notzann.png");
		$(obj).next().html(z-1);
	}
}