// JavaScript Document
//为安全性，默认主页访问模式为游客模式，无法修改主页任何信息
var visitType = "visitor";
var str = "";
var worning_msg = "";
$(function(){
    //初始化页面
    $.ajax({
        type: "POST",
        url: "categorylist/init",
        data: {
            purpose: "init"
        },
        dataType: "json",
        success: function(data){
            console.log(data);
            if ("visitor" === data.visitType) {
                console.log("确认是游客访问，设置visitType = visitor，隐藏所有编辑入口！");
                visitType = "visitor";
                $("#btn_to_edit").hide();
                //将ownerId放入.val_ownerId的dom,便于其他页面的跳转
                $(".val_ownerId").text(data.ownerId);
            }console.log("根据接收参数，初始化主页内容");
            $(".name_owner").text(data.userTemp.userName);
            $(".introduction_owner").text(data.userTemp.userIntroduction);
            $("#sum_category").text(" "+data.categoryMap.categorySum+" ");
            var font_size = 14;
            $.each(data.categoryMap.categoryList,function(i,categoryListItemMap){
                font_size += categoryListItemMap.sum*0.5;
                str = '	<div class="categpry_item" style="font-size:'+font_size+'px">'+categoryListItemMap.categoryName+'</div>';
                $("#container_category").append(str);
            });
        },
        error: function(jqXHR){
            worning_msg = "发生错误：" + jqXHR.status;
            alert(worning_msg);
        },
    });
    //分类名称动效
    $(document).on("mouseenter",".categpry_item",function(){
        $(this).css({
            "display": "inline-block",
            "background-color": "black",
            "color":" white",
            "padding": "5px 10px"
        });
    });
    $(document).on("mouseleave",".categpry_item",function(){
        $(this).css({
            "display": "inline-block",
            "border": "1px solid #000000",
            "border-width":" 1px",
            "background-color": "white",
            "color":"black",
            "padding": "5px 10px"
        });
    });
    //点击分类名称，获得相应文章列表
    $(document).on("click",".categpry_item",function(){
        var str;
        var ownerId = $(".val_ownerId").text();
        console.log("-Ajax:获取ownerId = "+ownerId+" category= "+$(this).text()+" 的文章列表");
        $.ajax({
            type: "POST",
            url: "worklist/listByCategory",
            data: {
                userId: ownerId,
                category: $(this).text()
            },
            dataType: "json",
            success: function(data){
                console.log(data);
                $("#container_category").html("");
                str = '<div class="h3 margin_botttom_20 text-justify">category: '+data.categoryName+'</div>';
                $("#container_category").append(str);
                $.each(data.workList,function(i,workListItemMap){
                    str = '<div class="item_list_work text-justify"><span class="glyphicon glyphicon-leaf" aria-hidden="true"></span><a href="javascript:jump_to_page_workdetail('+workListItemMap.workId+');"> '+workListItemMap.workTitle+'	</a></div><hr/>';
                    $("#container_category").append(str);
                });
            },
            error: function(jqXHR){
                var worning_msg = "发生错误：" + jqXHR.status;
                alert(worning_msg);
            },
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

    $("[data-toggle='tooltip']").tooltip();

});