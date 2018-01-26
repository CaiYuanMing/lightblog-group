// JavaScript Document
$(function(){
    var str;
    var worning_msg;
    var visitType;
    //初始化
    $.ajax({
        type: "POST",
        url: "workdetail/init",
        data: {
            purpose: "init"
        },
        dataType: "json",
        success: function(data){
            console.log(data);
            console.log("文章详情页初始化请求成功并接收到参数，开始初始化...");
            console.log("data.visitType ="+data.visitType);
            if ("visitor" === data.visitType) {
                console.log("确认是游客访问，设置visitType = visitor，隐藏所有编辑入口！");
                visitType = "visitor";
                $("#btn_to_re_edit").hide();
                $("#val_ownerId").text(data.workTemp.workUserId);
                str = '<span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>  '+data.workTemp.workGeneratesTime+' |<span class="glyphicon glyphicon-book" aria-hidden="true"></span> 分类于 '+data.workTemp.workCategory+'  | <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> 浏览'+data.workTemp.workBrowseSum+'次  |<a href="javascript:jump_to_page_main();"><span class="glyphicon glyphicon-user" aria-hidden="true"></span> '+data.workTemp.authorName+'</a>';
                $(".info_article").html(str);
            }
            $("#val_workId").text(data.workTemp.workId);
            $(".title_article").text(data.workTemp.workTitle);
            str = '<span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>  '+data.workTemp.workGeneratesTime+' |<span class="glyphicon glyphicon-book" aria-hidden="true"></span> 分类于 '+data.workTemp.workCategory+'  | <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> 浏览'+data.workTemp.workBrowseSum+'次 ';
            $(".info_article").html(str);
            $("#content_article").html(data.workTemp.workContentHtml);
			$("div#content_article img").addClass("img-responsive");			
            $.each(data.workTemp.tagList,function(i,tag){
                str = "#"+tag+"  ";
                $(".tag_article").append(str);
            });

        },
        error: function(jqXHR){
            worning_msg = "发生错误：" + jqXHR.status;
            console.log(worning_msg);
            alert(worning_msg);
        },
    });
    //toc切换
    $("#toc_togggle").click(function(){
        if($(this).attr("title")==="开启TOC"){
            $(this).html('<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>');
            $(this).attr("title","关闭TOC");
        }else{
            $(this).html('<span class="glyphicon glyphicon-menu-hamburger" aria-hidden="true"></span>');
            $(this).attr("title","开启TOC");
        }
        if($("#content_main").hasClass("col-md-9")){
            $("#content_main").removeClass("col-md-9");
            $("#content_main").addClass("col-md-12");
        }else{
            $("#content_main").removeClass("col-md-12");
            $("#content_main").addClass("col-md-9");
        }
        $("#container_toc").fadeToggle(100);

    });
    //删除后回到主页

    $(document).on("click","#btn_to_home",function(){
        window.location.href = "/lightblog/mainpage/jumpToMianPage";
    });

    //重新编辑文章
    $("#btn_to_re_edit").click(function(){
        var workId = $("#val_workId").text();
        window.location.href = "/lightblog/editor/reEdit?workId="+workId;
    });


    $("#btn_delete_work").click(function(){
        str = '<button id="btn_delete_work" type="button" class="btn btn-warning btn-block" disabled="disabled">正在删除</button>';
        $("#foot_modal_delete").html(str);
        $.ajax({
            type: "POST",
            url: "workdetail/deleteWorkById",
            data: {
                workId: $("#val_workId").text()
            },
            dataType: "json",
            success: function(data){
                console.log(data);
                if ("success" === data.outcome) {
                    str = '<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>文章删除成功！';
                    $("#body_modal_delete").html(str);
                    str = '<button id="btn_to_home" type="button" class="btn btn-success btn-block"">回到主页</button>';
                    $("#foot_modal_delete").html(str);
                } else {
                    str = '<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>文章删除失败'+data.msg;
                    $("#body_modal_delete").html(str);
                    str = '<button id="btn_to_home" type="button" class="btn btn-success btn-block"">回到主页</button>';
                    $("#foot_modal_delete").html(str);
                }
            },
            error: function(jqXHR){
                worning_msg = "发生错误：" + jqXHR.status;
                alert(worning_msg);
            },
        });
    });

    //回到顶部按钮的显示与隐藏
    $(window).scroll(function (){
        if ($(window).scrollTop() >= 50) {
            $('#btn_to_head_black').fadeIn();
        }
        else {
            $('#btn_to_head_black').fadeOut();
        }
    });
    //点击回到顶部，回到顶部
    $('#btn_to_head_black').click(function () {
        $('html,body').animate({ scrollTop: 0 }, 500);
    });
    //点击菜单，弹出菜单（编辑/删除）
    $('#btn_get_menu').click(function () {
        $('#btn_to_delete').fadeToggle(300);
        $('#btn_to_re_edit').fadeToggle(500);
    });
});