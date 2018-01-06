// JavaScript Document
//为安全性，默认主页访问模式为游客模式，无法修改主页任何信息
var str = "";
$(function(){
    $(document).on("mouseenter",".button_to_complie_article",function(){
        $(this).css({
            "display": "inline-block",
            "background-color": "black",
            "color":" white",
            "padding": "5px 10px"
        });
    });
    $(document).on("mouseleave",".button_to_complie_article",function(){
        $(this).css({
            "display": "inline-block",
            "border": "1px solid #000000",
            "border-width":" 2px",
            "background-color": "white",
            "color":"black",
            "padding": "5px 10px"
        });
    });
    //回到顶部按钮的显示与隐藏
    $(window).scroll(function () {
        if ($(window).scrollTop() >= 50) {
            $('#btn_to_head').fadeIn();
        }
        else {
            $('#btn_to_head').fadeOut();
        }
    });

    $('#btn_to_head').click(function () {
        $('html,body').animate({ scrollTop: 0 }, 500);
    });
    //点击写作,跳转至文章编辑页
    $("#btn_to_edit").click(function(){

        window.location.href = "";
    });
    $("[data-toggle='tooltip']").tooltip();
});
//显示某元素后添加可去警告框
$.extend({'add_waring_after':function(choose_object,waring_type,msg){
    $(choose_object).siblings().remove(".alert");
    str = '<div class="alert '+waring_type+' alert-dismissible fade in" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>'+msg+'</div>';
    $(choose_object).after(str);
}
});