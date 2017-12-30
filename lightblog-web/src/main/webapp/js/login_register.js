// JavaScript Document
//配置参数
var length_min_leagal_password = 14 ;//注册、登录时，密码合法长度最小值
var length_max_leagal_password = 20 ;//注册、登录时，密码合法长度最大值

//全局变量
var is_not_null = true;//检验空
var is_ajax_check_success = true;//ajax检验
var is_email_check_success = true;//邮箱（userid）格式校验
var is_password_length_leagal = true;//密码长度合法校验
var is_match_password = true;//两次密码输入是否一致
var is_man = true;//人机验证

var id_input_error_help = "";//为提示模块字符串
var	number_id_input_error_help = 1;//使提示模块id唯一
var error_message = "";//报错提示
var str; //存放字符

$(function(){
		
//重置输入框的聚焦即还原性质
	$.init_on_form_reload();
 //点击'登录'标签，修改菜单面板内容为登录菜单
   $("#login_label").click(function(){
	   $("#login_label").removeClass("menu_label_slake");
	   $("#login_label").addClass("menu_label_active");
	   $("#register_label").removeClass("menu_label_active");
	   $("#register_label").addClass("menu_label_slake");
	   str = '<div class="menu_choosed_style"><form name="form_login" action="login" method="post"><div class="form-group"><label class="control-label sr-only" for="input_userid_login">UserId</label><input type="email" name="userId" placeholder="请输入邮箱..." class="form-control input-lg" id="input_userid_login"></div><div class="form-group"><label class="control-label sr-only" for="input_userpassword_login">Password</label><input type="password" name="userPassword" placeholder="请输入密码..." class="form-control input-lg" id="input_userpassword_login"/></div><button id="button_login" type="button" class="btn btn-success btn-lg btn-block">登    录</button>	</form></div>';
      	$("#menu_choosed").html(str);
	   //重置输入框的聚焦即还原性质
	   $.init_on_form_reload();
	   //重置输入合法指标
	   is_not_null = true;//检验空
	   is_ajax_check_success = true;//ajax检验
	   is_email_check_success = true;//邮箱（userid）格式校验
	   is_password_length_leagal = true;//密码长度合法校验
	   is_match_password = true;//两次密码输入是否一致
	   is_man = true;//人机验证
   });
//点击'注册'标签，修改菜单面板内容为注册菜单
   $("#register_label").click(function(){
	   $("#register_label").removeClass("menu_label_slake");
	   $("#register_label").addClass("menu_label_active");
	   $("#login_label").removeClass("menu_label_active");
	   $("#login_label").addClass("menu_label_slake");
	   str = '<div class="menu_choosed_style"><form name="form_register" action="register" method="post"><div class="form-group"><label class="control-label sr-only" for="input_username_register">UserName</label><input type="text" name="userName" placeholder="请输入昵称..." class="form-control" id="input_username_register"/></div><div class="form-group"><label class="control-label sr-only" for="input_userid_register">UserId(Email)</label><input type="email" name="userId" placeholder="请输入邮箱..." class="form-control" id="input_userid_register"/></div>		<div class="form-group"><label class="control-label sr-only" for="input_userpassword_register">Password</label><input type="password" name="userPassword" placeholder="请输入14-20位密码..." class="form-control" id="input_userpassword_register"/></div><div class="form-group"><label class="control-label sr-only" for="input_userpassword_register">Password</label><input type="password" name="userPassword" placeholder="请再次输入密码..." class="form-control" id="input_userpassword_register_check"/></div><button id="button_register" type="button" class="btn btn-success btn-lg btn-block">注    册</button><span class="declare text-center">点击「注册」按钮，即代表你同意<a href="login_register.html" class="text-success">《LightBlog协议》</a></span></form></div>';
       $("#menu_choosed").html(str);
	   //重置输入框的聚焦即还原性质
	   $.init_on_form_reload();
	   //重置输入合法指标
	   is_not_null = true;//检验空
	   is_ajax_check_success = true;//ajax检验
	   is_email_check_success = true;//邮箱（userid）格式校验
	   is_password_length_leagal = true;//密码长度合法校验
	   is_match_password = true;//两次密码输入是否一致
	   is_man = true;//人机验证
   });
	
	//邮箱校验
	$("#input_userid_register").live('blur',function(){
		$.check_leagal_style_email("input_userid_register");
	});
	$("#input_userid_login").live('blur',function(){
		$.check_leagal_style_email("input_userid_login");
	});
	
	//注册时，密码长度校验
	$("#input_userpassword_register").live('blur',function(){		
		$.check_length_input_password("input_userpassword_register");
		//二次密码一致校验		
		var value_to_check = $("#input_userpassword_register_check").val();
		if(value_to_check.length>0){
			var value_check = $(this).val();
			if(value_check === value_to_check){
				$.status_on_success($("#input_userpassword_register_check"));
			}else{
				$.status_on_error($("#input_userpassword_register_check"),"两次输入密码不一致！");
			}			
		}
	});
	
	
	//二次输入密码一致性校验
	$("#input_userpassword_register_check").live('blur',function(){
		var value_check = $(this).val();
		var value_to_check = $("#input_userpassword_register").val();
		
		if(value_check === value_to_check){
			$.status_on_success($(this));
		}else{
			is_match_password = false;
			$.status_on_error($(this),"两次输入密码不一致！");			
		}		
	});	
	
	$("#input_userpassword_register_check").live('keyup',function(){
		var value_check = $(this).val();
		var value_to_check = $("#input_userpassword_register").val();
		if($("#input_userpassword_register").siblings(".glyphicon-ok").length>0){
			if(value_check.length === value_to_check.length){
				if(value_check === value_to_check){
					$.status_on_success($(this));
				}else{
					is_match_password = false;
					$.status_on_error($(this),"两次输入密码不一致！");
				}
			}
		}
		
	});	
	$("#input_userpassword_register").live('keyup',function(){//重新编辑第一个密码时，两个密码相同就更改状态为成功
		var value_check = $(this).val();
		var value_to_check = $("#input_userpassword_register_check").val();
		if(value_to_check.length>0){
			if(value_check.length === value_to_check.length){
				if(value_check === value_to_check){
					$.status_on_success($("#input_userpassword_register_check"));
				}else{
					is_match_password = false;
					$.status_on_error($("#input_userpassword_register_check"),"两次输入密码不一致！");
				}
			}
		}
	});
	
	
	//点击'登录/注册'按钮，检查输入是否为空	
	
    $("#button_login").live('click',function(){		
		$.check_is_null();
	});
	
	$("#button_register").live('click',function(){		
		$.check_is_null();
	});
		

	//注册校验昵称是否已存在
	$("#input_username_register").live('blur',function(){
       $.ajax({
           type: "POST",
           url: "register/nameCheck",
           data: {
               userName: $("#input_username_register").val()
           },
           dataType: "json",
           success: function(data){

               if ("success" === data.outcome) {
                   $.status_on_success($("#input_username_register"));
               } else {
                   $.status_on_error($("#input_username_register"),data.msg);
               }
           },
           error: function(jqXHR){
             alert("发生错误：" + jqXHR.status);
           },
       });

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
	   			
				if(value_for_check_length.length<length_min_leagal_password) {					
					$.status_on_error($("#"+ id_input),"密码不能短于"+length_min_leagal_password);
					is_password_length_leagal = false;
				}else if(value_for_check_length.length>length_max_leagal_password){
					$.status_on_error($("#"+ id_input),"密码不能长于"+length_max_leagal_password);
					is_password_length_leagal = false;
				}else{
					$.status_on_success($("#"+ id_input));
				}
	}
});

//自定义函数 邮箱格式校验
$.extend({'check_leagal_style_email':function(id_input_email){		
				var value_for_check_email = $("#"+ id_input_email).val();
	   			
				if(!/^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/.test(value_for_check_email)) {
					is_email_check_success = false;
					$.status_on_error($("#"+ id_input_email),"无效的邮箱！");
				}
	}
});

//检验输入空函数
$.extend({'check_is_null':function(){
				
		var name_null_input;
		var map_input_key = {
			userId : "邮箱",
			userName : "昵称",
			userPassword : "密码"
		};		
		$("form input").each(function(){
			if($(this).val().length===0){
				
				is_not_null = false;				
				
				name_null_input = $(this).attr("name");
				error_message = map_input_key[name_null_input]+"不能为空！";
				$.status_on_error(this,error_message);
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

$.extend({'recover_on_focus':function(info_object){		
				
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
				
				$("form input").each(function(){
					$(this).live('focus',function(){
						$.recover_on_focus(this);
					});
				});
		}
});



