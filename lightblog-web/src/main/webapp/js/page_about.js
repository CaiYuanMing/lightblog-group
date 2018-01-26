// JavaScript Document
$(function(){
    var str;
    var worning_msg;
    var visitType;
    //初始化
    $.ajax({
        type: "POST",
        url: "about/init",
        data: {
            purpose: "init"
        },
        dataType: "json",
        success: function(data){
            console.log(data);
            $(".name_owner").text(data.userTemp.userName);
            $(".introduction_owner").text(data.userTemp.userIntroduction);
            if ("visitor" === data.visitType) {
                console.log("确认是游客访问，设置visitType = visitor，隐藏所有编辑入口！");
                visitType = "visitor";
                $("#btn_to_edit_about").hide();
                $(".val_ownerId").text(data.ownerId);
            }

            if(data.aboutContentHtml===""||data.aboutContentHtml===null){
                if("master" === data.visitType){
                    str = '<p><span class="h3">你还没有关于页内容</span><br/>点击右下角的编辑，开始编写，让更多人认识你</p>';
                }else{
                    str = '<p class="h3">博主还没有关于页内容</p>';
                }

                $("#content_article").html(str);
            }else{
                $("#content_article").html(data.aboutContentHtml);
				$("div#content_article img").addClass("img-responsive");
            }

        },
        error: function(jqXHR){
            worning_msg = "发生错误：" + jqXHR.status;
            console.log(worning_msg);
            alert(worning_msg);
        },
    });

    $("#btn_to_edit_about").click(function(){
        window.location.href = "/lightblog/editor/aboutEdit";
    });
});