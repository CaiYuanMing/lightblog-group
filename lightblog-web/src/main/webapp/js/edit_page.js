// JavaScript Document
var edit_type =  "edit_new";
var is_not_null = false;
var error_message = "";
var str = "";
var worning_msg = "";
var id_input_error_help = "";
var number_id_input_error_help = 1;
$(function(){
    //tooltp初始化
    $("[data-toggle='tooltip']").tooltip();

    //----输入框
    //重置输入框的聚焦即还原性质
    $.init_on_form_reload();
    //输入框失焦，如果不是标签输入框且输入不为空，则更改输入框状态为校验成功，标签输入框需另外校验（标签输入不能有“&）
    $("form input").each(function(){
        $(this).blur(function(){
            if( $(this).attr("id")==="input_tag"){
                if(/&+/.test($(this).val())){
                    $.status_on_error(this,"&是系统保留字符，标签内不能包含&！");
                }else if($(this).val().length>0){
                    $.status_on_success(this);
                }
            }else if($(this).val().length>0){
                $.status_on_success(this);
            }
        });
    });
    //分类输入框，输入提示
    $('#input_category').typeahead({
        source: function (query, process) {
            console.log("正在匹配分类");
            console.log("query = "+query);
            $.ajax({
                url: 'editor/categoryTip',
                type: 'POST',
                dataType: 'JSON',
                data: {
                    query:query
                },
                success: function(data) {
                    process(data);
                }
            });
        },
        items:5, // 设置展示多少条 默认8条
    });
    //----标签

    //input_tag按回车添加标签
    $(document).on("keydown","#input_tag",function(event){
        if(event.which == "13"){
            console.log("input_tag : enter press ----");
            $.add_tag_to_container_tag($("#input_tag").val());
            $("#input_tag").val("");
        }

    });
    //标签输入框输入提示初始化
    $('#input_tag').typeahead({
        source: function (query, process) {
            console.log("正在匹配分类");
            console.log("query = "+query);
            $.ajax({
                url: 'editor/tagTip',
                type: 'POST',
                dataType: 'JSON',
                data: {
                    query:query
                },
                success: function(data) {
                    process(data);
                }
            });
        },
        items:8, // 设置展示多少条 默认8条
    });
    //点击相应标签去除标签

    $(document).on("click",".label-success",function(){
        $(this).remove();
    });
    //---返回按钮
    $(document).on("click","#btn_to_back",function(){
        window.history.back();
    });
    //---表单
    //点击保存按钮，校验输入是否为空，不为空即保存，并返回保存结果
    $(document).on("click","#button_save",function(){
        console.log("button_save : click ----");
        console.log("-- edit_type = "+edit_type);
        if (edit_type === "edit_new"||edit_type === "edit_re"){
            $.check_is_null();
            if (is_not_null){
                console.log("--合乎保存条件，进入保存处理");
                $("#button_save").attr("disabled","disabled");
                $("#button_save").text("正在保存...");
                var tag_string = "";
                var tag_count = 0;
                $("#container_tag").children(".label-success").each(function(){
                    tag_count += 1;
                    if(tag_count===1){
                        tag_string = tag_string + $(this).text();
                    }else{
                        tag_string = tag_string +"&"+ $(this).text();

                    }
                });
                console.log("tag_string = "+tag_string);
                console.log(tag_count+"个标签保存到tag_string");
                console.log("--ajax: 保存文章");
                $.ajax({
                    type: "POST",
                    url: "editor/saveWork",
                    data: {
                        workTitle: $("#input_title").val(),
                        workCategory: $("#input_category").val(),
                        tag_string : tag_string,
                        workContentMarkdown : $(".editormd-markdown-textarea").val(),
                        workContentHtml : $(".editormd-html-textarea").val()
                    },
                    dataType: "json",
                    success: function(data){
                        if ("success" === data.outcome) {
                            worning_msg = "<strong>文章保存成功！</strong>";
                            $.add_waring_after("#panel_info","alert-success",worning_msg);
                            $("input").val("");
                            $("#container_tag").children(".label-success").remove();
                            $.recover_on_focus( $("input"));
                            $(".editormd-markdown-textarea").val("");
                        } else {
                            worning_msg = "<strong>文章保存失败！</strong> "+data.msg;
                            $.add_waring_after("#panel_info","alert-success",worning_msg);
                        }
                    },
                    error: function(jqXHR){
                        worning_msg = "发生错误：" + jqXHR.status;
                        $.add_waring_after("#panel_info","alert-danger",worning_msg);
                    },
                });
                $("#button_save").removeAttr("disabled");
                $("#button_save").text("保存");
            }
        }else if (edit_type === "edit_about"){
            $("#button_save").attr("disabled","disabled");
            $("#button_save").text("正在保存...");
            console.log("--ajax: 保存关于页");
            $.ajax({
                type: "POST",
                url: "editor/saveAbout",
                data: {
                    workContentMarkdown : $(".editormd-markdown-textarea").val(),
                    workContentHtml : $(".editormd-html-textarea").val()
                },
                dataType: "json",
                success: function(data){
                    if ("success" === data.outcome) {
                        worning_msg = "<strong>关于页保存成功！</strong>";
                        $.add_waring_after("#panel_info","alert-success",worning_msg);
                        $(".editormd-markdown-textarea").val("");
                    } else {
                        worning_msg = "<strong>关于页保存失败！</strong> "+data.msg;
                        $.add_waring_after("#panel_info","alert-success",worning_msg);
                    }
                },
                error: function(jqXHR){
                    worning_msg = "发生错误：" + jqXHR.status;
                    $.add_waring_after("#panel_info","alert-danger",worning_msg);
                },
            });
            $("#button_save").removeAttr("disabled");
            $("#button_save").text("保存");
        }

    });

});

//----自定义函数
//检验输入空函数
$.extend({'check_is_null':function(){

    var id_null_input;
    var map_input_key = {
        input_title : "标题",
        input_category : "分类"
    };
    $("form input").each(function(){
        if($(this).val().length===0){
            is_not_null = false;
            id_null_input = $(this).attr("id");
            error_message = map_input_key[id_null_input]+"不能为空！";
            $.status_on_error(this,error_message);
        }else{
            is_not_null = true;
        }
    });
}
});

$.extend({'add_waring_after':function(choose_object,waring_type,msg){
    $(choose_object).siblings().remove(".alert");
    str = '<div class="alert '+waring_type+' alert-dismissible fade in" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>'+msg+'</div>';
    $(choose_object).after(str);
}
});

$.extend({'status_on_error':function(info_object_error,message_error){
    $.recover_on_focus(info_object_error);
    $(info_object_error).parent().addClass("has-error");
    $(info_object_error).parent().addClass("has-feedback");

    id_input_error_help = "input_error_Status_"+number_id_input_error_help;
    number_id_input_error_help += 1;

    $(info_object_error).attr("aria-describedby",id_input_error_help);

    str = '<span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span><span id="'+id_input_error_help+'" class="help-block">'+message_error+'</span>';

    $(info_object_error).after(str);


}
});
//更改输入框状态为成功
$.extend({'status_on_success':function(info_object_success){
    $.recover_on_focus(info_object_success);
    $(info_object_success).parent().addClass("has-success");
    $(info_object_success).parent().addClass("has-feedback");

    id_input_error_help = "input_success_Status_"+number_id_input_error_help;
    number_id_input_error_help += 1;

    $(info_object_success).attr("aria-describedby",id_input_error_help);

    str = '<span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span><span id="'+id_input_error_help+'" class="sr-only"></span>';

    $(info_object_success).after(str);
}
});
//输入框聚焦即还原
$.extend({'init_on_form_reload':function(){
    console.log("--恢复当前表单输入框，为聚焦后进入无报错状态");
    $("form input").each(function(){
        $(this).on('focus',function(){
            $.recover_on_focus(this);
        });
    });
}
});

$.extend({'recover_on_focus':function(info_object){
    $(".menu_login_register").siblings().remove(".alert");
    $(info_object).parent().removeClass("has-error");
    $(info_object).parent().removeClass("has-success");
    $(info_object).parent().removeClass("has-feedback");

    $(info_object).removeAttr("aria-describedby");

    $(info_object).siblings().remove(".form-control-feedback");
    $(info_object).siblings().remove(".help-block");

    $(info_object).val();

}
});

//在标签容器添加标签
//输入框聚焦即还原
$.extend({'add_tag_to_container_tag':function(tag_name){
    console.log("--标签"+tag_name+"将被添加到div:container_tag");
    str = '  <span class="label label-success inline_block">'+tag_name+' <span class="glyphicon glyphicon-remove" aria-hidden="true"></span></span>';
    $("#container_tag").append(str);
    console.log("--标签"+tag_name+"已被添加到div:container_tag");
}
});