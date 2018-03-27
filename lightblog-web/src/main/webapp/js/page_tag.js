// JavaScript Document
//为安全性，默认主页访问模式为游客模式，无法修改主页任何信息
var visitType = "visitor";
var str = "";
var worning_msg = "";
$(function(){
    //初始化页面
    $.ajax({
        type: "POST",
        url: "taglist/init",
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
            $("#sum_tag").text(" "+data.tagMap.tagCount+" ");
            // if(data.tagMap.tagSum === 0){
            //
            //     if("master" === data.visitType){
            //         str = '<p><span class="h4 text-left">你还没有文章</span><br/>点击右下角的编辑，开始迈出第一步吧</p>';
            //     }else{
            //         str = '<p class="h3">博主文章库存为零</p>';
            //     }
            //     $("#container_tag").append(str);
            // }else{
            if(data.tagMap.tagSum != 0){
                var font_size = 14;
                $.each(data.tagMap.tagList,function(i,tagListItemMap){
                    font_size += tagListItemMap.tagSum*0.5;
                    str = '	<div class="categpry_item" style="font-size:'+font_size+'px">'+tagListItemMap.tagName+'</div>';
                    $("#container_tag").append(str);
                });
            }

            // }
        },
        error: function(jqXHR){
            worning_msg = "发生错误：" + jqXHR.status;
            alert(worning_msg);
        },
    });
    //标签名称动效N
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
    //点击标签名称，获得相应文章列表
    $(document).on("click",".categpry_item",function(){
        var str;
        var ownerId = $(".val_ownerId").text();
        console.log("-Ajax:获取ownerId = "+ownerId+" tag= "+$(this).text()+" 的文章列表");
        $.ajax({
            type: "POST",
            url: "worklist/listByTag",
            data: {
                userId: ownerId,
                tag: $(this).text()
            },
            dataType: "json",
            success: function(data){
                console.log(data);
                $("#container_tag").html("");
                str = '<div class="h3 margin_botttom_20 text-justify">tag: '+data.tagName+'</div>';
                $("#container_tag").append(str);
                $.each(data.workList,function(i,workListItemMap){
                    str = '<div class="item_list_work text-justify"><span class="glyphicon glyphicon-leaf" aria-hidden="true"></span><a href="javascript:jump_to_page_workdetail('+workListItemMap.workId+');"> '+workListItemMap.workTitle+'	</a></div><hr/>';
                    $("#container_tag").append(str);
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