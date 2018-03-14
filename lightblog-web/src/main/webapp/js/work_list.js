// JavaScript Document
//为安全性，默认主页访问模式为游客模式，无法修改主页任何信息
var visitType = "visitor";
var str = "";
var worning_msg = "";
$(function(){
    //初始化页面
    $.ajax({
        type: "POST",
        url: "worklist/init",
        data: {
            purpose: "init"
        },
        dataType: "json",
        success: function(data){
            console.log(data);
            console.log("归档页初始化请求成功并接收到参数，开始初始化...");
            console.log("data.visitType ="+data.visitType);

            if ("visitor" === data.visitType) {
                console.log("确认是游客访问，设置visitType = visitor，隐藏所有编辑入口！");
                visitType = "visitor";
                $("#btn_to_edit").hide();
                //将ownerId放入.val_ownerId的dom,便于其他页面的跳转
                $(".val_ownerId").text(data.ownerId);
            }console.log("根据接收参数，初始化主页内容");
            $(".name_owner").text(data.userTemp.userName);
            $(".introduction_owner").text(data.userTemp.userIntroduction);

            str = '<div class="row"><div class="col-md-6 col-md-offset-3 col-xs-12"><p class="h4">共计 <span id="sum_work" class="text-success h3">'+data.workListMap.workSum+'</span> 篇 Blog</p><div class="block_ceil_list_work_year"></div></div></div>';
            $("#container_work").append(str);
            //
            // if(data.workListMap.workSum === 0){
            //
            //     if("master" === data.visitType){
            //         str = '<p><span class="h3 text-center">你还没有文章</span><br/>点击右下角的编辑，开始迈出第一步吧</p>';
            //     }else{
            //         str = '<p class="h3">博主文章库存为零</p>';
            //     }
            //     $("#container_work").append(str);
            // }else {
            if (data.workListMap.workSum != 0){
                $.each(data.workListMap.yearList,function(i,yearMap){
                    str = '<div class="div_year"><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span><span id="number_year" class="h2">  '+yearMap.yearNumber+'</span></div>';
                    $(".block_ceil_list_work_year").append(str);

                    $.each(yearMap.monthList,function(i,monthMap){
                        str = '<div class="div_month"><span class="glyphicon glyphicon-tree-conifer" aria-hidden="true"></span><span id="number_month" class="h3">  '+monthMap.monthNumber+'</span></div>';
                        $(".block_ceil_list_work_year").append(str);
                        $.each(monthMap.dayList,function(i,workListItem){
                            str = '<div class="item_list_work"><span class="glyphicon glyphicon-leaf" aria-hidden="true"></span><a href="javascript:jump_to_page_workdetail('+workListItem.workId+');">  '+workListItem.workGeneratesTime+' '+workListItem.workTitle+'	</a><span id="num_brow" class="text-muted">浏览 '+workListItem.workBrowseSum+'</span></div><hr/>';
                            $(".block_ceil_list_work_year").append(str);
                        });
                    });
                });
            }

            // }

        },
        error: function(jqXHR){
            worning_msg = "发生错误：" + jqXHR.status;
            console.log(worning_msg);
            $.add_waring_after(".menu_login_register","alert-danger",worning_msg);
        },
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
//显示某元素后添加可去警告框
$.extend({'add_waring_after':function(choose_object,waring_type,msg){
    $(choose_object).siblings().remove(".alert");
    str = '<div class="alert '+waring_type+' alert-dismissible fade in" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>'+msg+'</div>';
    $(choose_object).after(str);
}
});