
!function(window) {
    "use strict";

    var doc = window.document
        , ydui = {};

    $(window).on('load', function() {
        typeof FastClick == 'function' && FastClick.attach(doc.body);
    });

    var util = ydui.util = {

        parseOptions: function(string) {
            if ($.isPlainObject(string)) {
                return string;
            }

            var start = (string ? string.indexOf('{') : -1)
                , options = {};

            if (start != -1) {
                try {
                    options = (new Function('','var json = ' + string.substr(start) + '; return JSON.parse(JSON.stringify(json));'))();
                } catch (e) {}
            }
            return options;
        },

    };

    if (typeof define === 'function') {
        define(ydui);
    } else {
        window.YDUI = ydui;
    }

}(window);

!function(window) {
    "use strict";

    var doc = window.document
        , $doc = $(doc)
        , $body = $(doc.body)
        , $mask = $('<div class="mask-black"></div>');

    function ActionSheet(element, closeElement) {
        this.$element = $(element);
        this.closeElement = closeElement;
        this.toggleClass = 'actionsheet-toggle';
    }

    ActionSheet.prototype.open = function() {

        YDUI.device.isIOS && $('.g-scrollview').addClass('g-fix-ios-overflow-scrolling-bug');

        var _this = this;
        $body.append($mask);

        $mask.on('click.ydui.actionsheet.mask', function() {
            _this.close();
        });

        if (_this.closeElement) {
            $doc.on('click.ydui.actionsheet', _this.closeElement, function() {
                _this.close();
            });
        }

        _this.$element.addClass(_this.toggleClass).trigger('open.ydui.actionsheet');
    }
    ;

    ActionSheet.prototype.close = function() {
        var _this = this;

        YDUI.device.isIOS && $('.g-scrollview').removeClass('g-fix-ios-overflow-scrolling-bug');

        $mask.off('click.ydui.actionsheet.mask').remove();
        _this.$element.removeClass(_this.toggleClass).trigger('close.ydui.actionsheet');
    }
    ;

    function Plugin(option) {
        var args = Array.prototype.slice.call(arguments, 1);

        return this.each(function() {
            var $this = $(this)
                , actionsheet = $this.data('ydui.actionsheet');

            if (!actionsheet) {
                $this.data('ydui.actionsheet', (actionsheet = new ActionSheet(this,option.closeElement)));
                if (!option || typeof option == 'object') {
                    actionsheet.open();
                }
            }

            if (typeof option == 'string') {
                actionsheet[option] && actionsheet[option].apply(actionsheet, args);
            }
        });
    }

    $doc.on('click.ydui.actionsheet.data-api', '[data-ydui-actionsheet]', function(e) {
        e.preventDefault();

        var options = window.YDUI.util.parseOptions($(this).data('ydui-actionsheet'))
            , $target = $(options.target)
            , option = $target.data('ydui.actionsheet') ? 'open' : options;

        Plugin.call($target, option);
    });

    $.fn.actionSheet = Plugin;

}(window);

!function(window) {
    window.document.addEventListener('touchstart', function(event) {/* Do Nothing */
    }, false);
}(window);

!function(window) {
    var doc = window.document
        , ydui = window.YDUI
        , ua = window.navigator && window.navigator.userAgent || '';

    var ipad = !!ua.match(/(iPad).*OS\s([\d_]+)/)
        , ipod = !!ua.match(/(iPod)(.*OS\s([\d_]+))?/)
        , iphone = !ipad && !!ua.match(/(iPhone\sOS)\s([\d_]+)/);

    ydui.device = {};
}(window);



//rule js

$(function() {
    $('#ClickMe').click(function() {
        $('#code').center();
        $('#goodcover').show();
        $('#code').fadeIn();
    });
    $('#closebt').click(function() {
        $('#code').hide();
        $('#goodcover').hide();
    });
    $('#goodcover').click(function() {
        $('#code').hide();
        $('#goodcover').hide();
    });

    jQuery.fn.center = function(loaded) {
        var obj = this;
        body_width = parseInt($(window).width());
        body_height = parseInt($(window).height());
        block_width = parseInt(obj.width());
        block_height = parseInt(obj.height());

        left_position = parseInt((body_width / 2.6) - (block_width / 2.6) + $(window).scrollLeft());
        if (body_width < block_width) {
            left_position = 0 + $(window).scrollLeft();
        };

        top_position = parseInt((body_height / 2) - (block_height / 2) + $(window).scrollTop());
        if (body_height < block_height) {
            top_position = 0 + $(window).scrollTop();
        };

        if (!loaded) {

            obj.css({
                'position': 'absolute'
            });
            obj.css({
                'top': ($(window).height() - $('#code').height()) * 0.5,
                'left': left_position
            });
            $(window).bind('resize', function() {
                obj.center(!loaded);
            });
            $(window).bind('scroll', function() {
                obj.center(!loaded);
            });

        } else {
            obj.stop();
            obj.css({
                'position': 'absolute'
            });
            obj.animate({
                'top': top_position
            }, 20, 'linear');
        }
    }

})


function share(tp){
    if(tp==1){
        // alert('唤起微信失败，未检测到您打开微信应用程序！');
        window.location.href="weixin://";
    }else if(tp==2){
        window.location.href="tencent://message/?uin=1014167202&Site=在线QQ&Menu=yes";
        // alert('唤起QQ失败，未检测到您打开QQ应用程序！');
    }else{
        $(".mask-black").click();
        jqalert({
            prompt:'【享GO影视】如果您下载个人海报失败，您还可以截图分享给您的好友',
            yestext:'知道了',
            yesfn:function () {
            }
        })
        downloadIamge(shareUrl,'xgo.png');
    }
}

function downloadIamge(imgsrc, name){ //下载图片地址和图片名
    let image = new Image();
    // 解决跨域 Canvas 污染问题
    image.setAttribute("crossOrigin", "anonymous");
    image.onload = function() {
      let canvas = document.createElement("canvas");
      canvas.width = image.width;
      canvas.height = image.height;
      let context = canvas.getContext("2d");
      context.drawImage(image, 0, 0, image.width, image.height);
      let url = canvas.toDataURL("image/png"); //得到图片的base64编码数据
      let a = document.createElement("a"); // 生成一个a元素
      let event = new MouseEvent("click"); // 创建一个单击事件
      a.download = name || "photo"; // 设置图片名称
      a.href = url; // 将生成的URL设置为a.href属性
      a.dispatchEvent(event); // 触发a的单击事件
    };
    image.src = imgsrc;
  }
var shareUrl = "";
$(function(){
    jAjax("/appUserCenter/myInviteCode",{},function(data){ 
        shareUrl = reqPath+data.invitecodeqrcode;
        clipboard = new Clipboard('#cp');
        $("#cpLink").html("秋水一色共长天每天赚个早餐钱，看视频玩游戏都能领现金！\n"+data.invitecodehttpurl);
        $("#incd").attr("src",shareUrl);
    });
    $("#cp").click(function(){
        $(".mask-black").click();
        jqalert({
            prompt:'【享GO影视】邀请链接已经复制成功，是否打开微信去粘贴!',
            yestext:'前往微信粘贴',
            notext:'不用了',
            yesfn:function () {
                window.location.href="weixin://";
            },
            nofn:function () {
            }
        })
    });
    $("#cp0").click(function(){
        $(".mask-black").click();
        // $("#actionSheet").hide();
        jqalert({
            prompt:'【享GO影视】邀请链接已复制，前往微信分享给您的好友吧！',
            yestext:'前往微信粘贴给好友',
            yesfn:function () {
                window.location.href="weixin://";
            }
        })
    });
    $("#cp1").click(function(){
        $(".mask-black").click();
        jqalert({
            prompt:'【享GO影视】检测到您没有打开QQ，请手动打开QQ粘贴邀请链接给好友!',
            yestext:'知道了',
            yesfn:function () {
            }
        })
    });

});