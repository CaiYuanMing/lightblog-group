// JavaScript Document
//配置参数
var length_min_leagal_password = 14 ;//注册、登录时，密码合法长度最小值
var length_max_leagal_password = 20 ;//注册、登录时，密码合法长度最大值

//全局变量，用于最终操作前的条件判断依据，初始值的设定基于观点：未经检验直接用初始值作为条件，最终操作是不会进行操作的
var is_email_legal = false ; //邮箱合法
var is_email_legal_and_exist = false;//邮箱合法且存在
var is_name_exist = true; //昵称是否已注册
var is_password_length_leagal = false;//密码长度合法校验
var is_match_password = false;//两次密码输入是否一致
var is_not_null = false;//检验空


var id_input_error_help = "";//为提示模块字符串
var	number_id_input_error_help = 1;//使提示模块id唯一
var error_message = "";//报错提示
var str; //存放字符
var worning_msg;//存放警告信息

$(function(){

//重置输入框的聚焦即还原性质
    console.log("页面载入---");
    $.init_on_form_reload();
    //点击'登录'标签，修改菜单面板内容为登录菜单
    $("#login_label").click(function(){
        console.log("login_label: click------");
        console.log("--移除警告框");
        $(".menu_login_register").siblings().remove(".alert");
        console.log("--初始化菜单面板");
        $("#login_label").removeClass("menu_label_slake");
        $("#login_label").addClass("menu_label_active");
        $("#register_label").removeClass("menu_label_active");
        $("#register_label").addClass("menu_label_slake");

        str = '<div class="menu_choosed_style"><form id="form_login" action="login" method="post"><div class="form-group"><label class="control-label sr-only" for="input_userid_login">UserId</label><input type="email" name="userId" placeholder="请输入邮箱..." class="form-control input-lg" id="input_userid_login"></div><div class="form-group"><label class="control-label sr-only" for="input_userpassword_login">Password</label><input type="password" name="userPassword" placeholder="请输入密码..." class="form-control input-lg" id="input_userpassword_login"/></div><button id="button_login" type="button" class="btn btn-success btn-lg btn-block">登    录</button>	</form></div>';
        $("#menu_choosed").html(str);
        //重置输入框的聚焦即还原性质
        $.init_on_form_reload();
        //重置输入合法指标
        is_not_null = false;
        is_email_legal_and_exist = false;
    });
//点击'注册'标签，修改菜单面板内容为注册菜单
    $("#register_label").click(function(){
        console.log("login_label: click------");
        console.log("--移除警告框");
        $(".menu_login_register").siblings().remove(".alert");
        console.log("--初始化菜单面板");

        $("#register_label").removeClass("menu_label_slake");
        $("#register_label").addClass("menu_label_active");
        $("#login_label").removeClass("menu_label_active");
        $("#login_label").addClass("menu_label_slake");
        str = '<div class="menu_choosed_style"><form id="form_register" action="register/toRegister" method="post"><div class="form-group"><label class="control-label sr-only" for="input_username_register">UserName</label><input type="text" name="userName" placeholder="请输入昵称..." class="form-control" id="input_username_register"/></div><div class="form-group"><label class="control-label sr-only" for="input_userid_register">UserId(Email)</label><input type="email" name="userId" placeholder="请输入邮箱..." class="form-control" id="input_userid_register"/></div>		<div class="form-group"><label class="control-label sr-only" for="input_userpassword_register">Password</label><input type="password" name="userPassword" placeholder="请输入14-20位密码..." class="form-control" id="input_userpassword_register"/></div><div class="form-group"><label class="control-label sr-only" for="input_userpassword_register">Password</label><input type="password" name="userPasswordCheck" placeholder="请再次输入密码..." class="form-control" id="input_userpassword_register_check"/></div><button id="button_register" type="button" class="btn btn-success btn-lg btn-block">注    册</button><span class="declare text-center">点击「注册」按钮，即代表你同意<a href="login_register.html" class="text-success">《LightBlog协议》</a></span></form></div>';
        $("#menu_choosed").html(str);
        //重置输入框的聚焦即还原性质
        $.init_on_form_reload();
        //重置输入合法指标
        is_not_null = false;//检验空
        is_name_exist = true;
        is_password_length_leagal = false;
        is_match_password = false;
    });


    //------登录

    //邮箱输入框聚焦，重置相关全局变量
    $(document).on("focus","#input_userid_login",function(){
        is_email_legal = false ; //邮箱合法
        is_email_legal_and_exist = false;//邮箱合法且存在
        console.log("重置相关全局变量：is_email_legal = "+is_email_legal+" is_email_legal_and_exist="+is_email_legal_and_exist);
    });
    //邮箱输入框失去焦点，检验邮箱格式，合法则进一步检验是否已注册
    $(document).on("blur","#input_userid_login",function(){
        console.log("input_userid_login ： blur ---- ");
        //只在邮箱输入不为空时检查格式
        if($("#input_userid_login").val().length>0){
            $.check_leagal_style_email("input_userid_login");
        }
        //邮箱合法，进行邮箱登录可用性检验
        if(is_email_legal){
            console.log("--ajax : 校验邮箱是否已注册");
            $.ajax({
                type: "POST",
                url: "login/userIdCheck",
                data: {
                    userId: $("#input_userid_login").val()
                },
                dataType: "json",
                success: function(data){
                    console.log("data.outcome ="+data.outcome+" data.msg = "+data.msg);
                    if ("success" === data.outcome) {
                        is_email_legal_and_exist = true;
                        $.status_on_success($("#input_userid_login"));
                    } else {
                        is_email_legal_and_exist = false;
                        $.status_on_error($("#input_userid_login"),data.msg);
                    }
                },
                error: function(jqXHR){
                    worning_msg = "发生错误：" + jqXHR.status;
                    console.log(worning_msg);
                    $.add_waring_after(".menu_login_register","alert-danger",worning_msg);
                },
            });
        }
    });
    //点击登录按钮，邮箱格式正确且已注册，则进行邮箱、密码匹配校验，校验成功则提交表单，否则显示警告信息
    $(document).on("click","#button_login",function(){
        console.log("button_login : click ----");
        $.check_is_null();
        if (is_not_null && is_email_legal_and_exist){
            console.log("--进入登录处理");
            $("#button_login").attr("disabled","disabled");
            console.log( $("#button_login").attr("disabled"));
            $("#button_login").text("正在登录...");
            console.log( $("#button_login").attr("disabled"));
            console.log("--ajax : 校验邮箱、密码是否匹配");
            $.ajax({
                type: "POST",
                url: "login/check_match_userid_password",
                data: {
                    userId: $("#input_userid_login").val(),
                    password: $("#input_userpassword_login").val()
                },
                dataType: "json",
                success: function(data){
                    console.log("data.outcome = "+data.outcome+"data.msg = "+data.msg);
                    if ("success" === data.outcome) {
                        $.status_on_success($("#input_userpassword_login"));
                        $("#form_login").submit();
                    } else {
                        console.log("data.outcome = "+data.outcome+"data.msg = "+data.msg);

                        worning_msg = "<strong>登录失败!</strong> "+data.msg;
                        $.add_waring_after(".menu_login_register","alert-danger",worning_msg);
                    }
                },
                error: function(jqXHR){
                    worning_msg ="发生错误！"+jqXHR.status;
                    console.log(worning_msg);
                    $.add_waring_after(".menu_login_register","alert-danger",worning_msg);
                },
            });
            $("#button_login").removeAttr("disabled");
            $("#button_login").text("登    录");
        }});


    //------注册

    //昵称输入框聚焦，重置相关全局变量
    $(document).on("focus","#input_username_register",function(){
        is_name_exist = true;
        console.log("重置相关全局变量：is_name_exist = "+is_name_exist);
    });
    //昵称输入框失去焦点，校验昵称是否已注册
    $(document).on("blur","#input_username_register",function(){
        console.log("input_username_register ： blur ---- ");
        if($("#input_username_register").val().length>0){

            $.ajax({
                type: "POST",
                url: "register/nameCheck",
                data: {
                    userName: $("#input_username_register").val()
                },
                dataType: "json",
                success: function(data){
                    console.log("data.outcome ="+data.outcome+" data.msg = "+data.msg);
                    if ("success" === data.outcome) {
                        is_name_exist = false;
                        $.status_on_success($("#input_username_register"));
                    } else {
                        is_name_exist = true;
                        $.status_on_error($("#input_username_register"),data.msg);
                    }
                },
                error: function(jqXHR){
                    worning_msg = "发生错误：" + jqXHR.status;
                    console.log(worning_msg);
                    $.add_waring_after(".menu_login_register","alert-danger",worning_msg);
                },
            });
        }

    });
    //邮箱输入框聚焦，重置相关全局变量
    $(document).on("focus","#input_userid_register",function(){
        is_email_legal = false;
        is_email_legal_and_exist = false;
        console.log("重置相关全局变量：is_email_legal = "+is_email_legal+" is_email_legal_and_exist = "+is_email_legal_and_exist);
    });
    //邮箱输入框失去焦点，检验邮箱格式，合法则进一步检验是否已注册
    $(document).on("blur","#input_userid_register",function(){
        //只在邮箱输入不为空时检查格式
        if($("#input_userid_register").val().length>0){
            $.check_leagal_style_email("input_userid_register");
        }
        //邮箱合法，进行邮箱注册是否已经注册
        if(is_email_legal){
            console.log("--ajax : 校验邮箱是否已注册");
            $.ajax({
                type: "POST",
                url: "register/userIdCheck",
                data: {
                    userId: $("#input_userid_register").val()
                },
                dataType: "json",
                success: function(data){
                    console.log("data.outcome ="+data.outcome+" data.msg = "+data.msg);
                    if ("success" === data.outcome) {
                        is_email_legal_and_exist = false;
                        $.status_on_success($("#input_userid_register"));
                    } else {
                        is_email_legal_and_exist = true;
                        $.status_on_error($("#input_userid_register"),data.msg);
                    }
                },
                error: function(jqXHR){
                    worning_msg = "发生错误：" + jqXHR.status;
                    console.log(worning_msg);
                    $.add_waring_after(".menu_login_register","alert-danger",worning_msg);
                },
            });
        }
    });
    //密码输入框聚焦，重置相关全局变量
    $(document).on("focus","#input_userpassword_register",function(){
        is_password_length_leagal = false;
        is_match_password = false;
        console.log("重置相关全局变量：is_password_length_leagal = "+is_password_length_leagal+" is_match_password = "+is_match_password);
    });

    //密码输入框失去焦点，检验长度是否合法
    $(document).on("blur","#input_userpassword_register",function(){
        console.log("input_userpassword_register ： blur ---- ");
        if($(this).val().length>0){
            $.check_length_input_password("input_userpassword_register");
            console.log("--二次密码一致校验");
            var value_to_check = $("#input_userpassword_register_check").val();
            if(value_to_check.length>0){
                var value_check = $(this).val();
                if(value_check === value_to_check){
                    is_match_password = true;
                    $.status_on_success($("#input_userpassword_register_check"));
                }else{
                    is_match_password = false;
                    $.status_on_error($("#input_userpassword_register_check"),"两次输入密码不一致！");
                }
            }
        }
    });

    //二次输入密码一致性校验
    $(document).on("blur","#input_userpassword_register_check",function(){
        console.log("input_userpassword_register_check ： blur ---- ");
        if($(this).val().length>0){
            console.log("--二次密码一致校验");
            var value_check = $(this).val();
            var value_to_check = $("#input_userpassword_register").val();

            if(value_check === value_to_check){
                is_match_password = true;
                $.status_on_success($(this));
            }else{
                is_match_password = false;
                $.status_on_error($(this),"两次输入密码不一致！");
            }
        }
    });
    //如果第一个密码合法，第二个密码输入且与第一个密码长度相同时时，检验二者是否匹配
    $(document).on("keyup","#input_userpassword_register_check",function(){
        var value_check = $(this).val();
        var value_to_check = $("#input_userpassword_register").val();
        if($("#input_userpassword_register").siblings(".glyphicon-ok").length>0){
            if(value_check.length === value_to_check.length){
                console.log("第一次密码合法，第二次密码输入中，此时两者长度相同，判断是否匹配");
                if(value_check === value_to_check){
                    is_match_password =true;
                    $.status_on_success($(this));
                }else{
                    is_match_password = false;
                    $.status_on_error($(this),"两次输入密码不一致！");
                }
            }
        }

    });
    //重新编辑第一个密码时，两个密码相同就更改状态为成功
    $(document).on("keyup","#input_userpassword_register",function(){
        var value_check = $(this).val();
        var value_to_check = $("#input_userpassword_register_check").val();
        if(value_to_check.length>0){
            if(value_check.length === value_to_check.length){
                console.log("第一次密码输入中，此时两者长度相同，判断是否匹配");
                if(value_check === value_to_check){
                    is_match_password = true;
                    $.status_on_success($("#input_userpassword_register_check"));
                }else{
                    is_match_password = false;
                    $.status_on_error($("#input_userpassword_register_check"),"两次输入密码不一致！");
                }
            }
        }
    });

    //点击注册按钮
    $(document).on("click","#button_register",function(){
        console.log("button_login : click ----");
        $.check_is_null();
        if (is_not_null && !is_name_exist && is_email_legal && is_password_length_leagal && is_match_password  ){	console.log("--合乎注册条件，进入注册处理");
            $("#button_register").attr("disabled","disabled");
            $("#button_register").text("正在注册...");
            console.log("--ajax: 注册");
            $.ajax({
                type: "POST",
                url: "register/toRegister",
                data: {
                    userName: $("#input_username_register").val(),
                    userId: $("#input_userid_register").val(),
                    userPassword : $("#input_userpassword_register").val()
                },
                dataType: "json",
                success: function(data){
                    if ("success" === data.outcome) {
                        worning_msg = "<strong>注册成功！</strong>";
                        $.add_waring_after(".menu_login_register","alert-success",worning_msg);
                        $("input").val("");
                    } else {
                        worning_msg = "<strong>注册失败！</strong> "+data.msg;
                        $.add_waring_after(".menu_login_register","alert-success",worning_msg);
                    }
                },
                error: function(jqXHR){
                    worning_msg = "发生错误：" + jqXHR.status;
                    $.add_waring_after(".menu_login_register","alert-danger",worning_msg);
                },
            });
            $("#button_register").removeAttr("disabled");
            $("#button_register").text("注    册");
        }
    });


});
////luosimao人机校验
//$.extend({'response_check_man_set':function(result_is_man){
//				is_man = result_is_man;
//				alert("is man = "+is_man);
//	}
//});

//密码长度校验
$.extend({'check_length_input_password':function(id_input){

    var value_for_check_length = $("#"+ id_input).val();
    console.log("--密码长度校验 length = "+value_for_check_length.length);
    if(value_for_check_length.length<length_min_leagal_password) {
        $.status_on_error($("#"+ id_input),"密码不能短于"+length_min_leagal_password);
        is_password_length_leagal = false;
    }else if(value_for_check_length.length>length_max_leagal_password){
        $.status_on_error($("#"+ id_input),"密码不能长于"+length_max_leagal_password);
        is_password_length_leagal = false;
    }else{
        is_password_length_leagal = true;
        $.status_on_success($("#"+ id_input));
    }
}
});

//自定义函数 邮箱格式校验
$.extend({'check_leagal_style_email':function(id_input_email){
    console.log("--进入邮箱格式校验");
    var value_for_check_email = $("#"+ id_input_email).val();

    if(!/^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/.test(value_for_check_email)) {
        is_email_legal = false ;
        $.status_on_error($("#"+ id_input_email),"无效的邮箱！");
    }else{
        is_email_legal = true;
    }
    console.log("is_email_legal = "+is_email_legal);
}
});

//检验输入空函数
$.extend({'check_is_null':function(){
    var name_null_input;
    var map_input_key = {
        userId : "邮箱",
        userName : "昵称",
        userPassword : "密码",
        userPasswordCheck : "再次密码输入"
    };
    $("form input").each(function(){
        if($(this).val().length===0){
            is_not_null = false;
            name_null_input = $(this).attr("name");
            error_message = map_input_key[name_null_input]+"不能为空！";
            $.status_on_error(this,error_message);
        }else{
            is_not_null = true;
        }
    });
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


//显示某元素后添加可去警告框
$.extend({'add_waring_after':function(choose_object,waring_type,msg){
    $(choose_object).siblings().remove(".alert");
    str = '<div class="alert '+waring_type+' alert-dismissible fade in" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>'+msg+'</div>';
    $(choose_object).after(str);
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

$.extend({'init_on_form_reload':function(){
    console.log("--恢复当前表单输入框，为聚焦后进入无报错状态");
    $("form input").each(function(){
        $(this).on('focus',function(){
            $.recover_on_focus(this);
        });
    });
}
});



