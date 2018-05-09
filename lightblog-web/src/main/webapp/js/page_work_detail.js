// JavaScript Document
$(function(){
    var str;
    var worning_msg;
	var workId;
    var visitType;
	var ownerId = "";	
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
				$("#btn_back").attr("title","返回作者个人主页");
				$(document).on("click","#btn_back",function(){					
					window.location.href = "/lightblog/mainpage/jumpToMianPage?ownerId="+data.ownerId;
				});				
                console.log("确认是游客访问，设置visitType = visitor，隐藏所有编辑入口！");
                visitType = "visitor";				
                $("#btn_to_re_edit").hide();
                $("#val_ownerId").text(data.workTemp.workUserId);
                str = '<span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>  '+data.workTemp.workGeneratesTime+' |<span class="glyphicon glyphicon-book" aria-hidden="true"></span> 分类于 '+data.workTemp.workCategory+'  | <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> 浏览'+data.workTemp.workBrowseSum+'次  |<a href="javascript:jump_to_page_main();"><span class="glyphicon glyphicon-user" aria-hidden="true"></span> '+data.workTemp.authorName+'</a>';
                $(".info_article").html(str);
            }else{
				$("#btn_back").attr("title","返回个人主页");
				$(document).on("click","#btn_back",function(){					
					window.location.href = "/lightblog/mainpage/jumpToMianPage";
				});
			}
            $("#val_workId").text(data.workTemp.workId);
			workId = data.workTemp.workId;
            $(".title_article").text(data.workTemp.workTitle);
            str = '<span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>  '+data.workTemp.workGeneratesTime+' |<span class="glyphicon glyphicon-book" aria-hidden="true"></span> 分类于 '+data.workTemp.workCategory+'  | <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> 浏览'+data.workTemp.workBrowseSum+'次 ';
            $(".info_article").html(str);
            $("#content_article").html(data.workTemp.workContentHtml);
			$("div#content_article img").addClass("img-responsive");			
            $.each(data.workTemp.tagList,function(i,tag){
                str = "#"+tag+"  ";
                $(".tag_article").append(str);
            });
			//赞与分享按钮初始化
			if(data.isThumbUp){
				$("#block_thumbUp").addClass("button_active");
			}else{
				$("#block_thumbUp").addClass("button_sleep");
			}
			if(data.isShare){
				$("#block_share").addClass("button_active");
			}else{
				$("#block_share").addClass("button_sleep");
			}
			//返回主页按钮初始化
			if(data.pageNature==="social"){
				str = '<span class="glyphicon glyphicon-home" aria-hidden="true"></span>';
				$("#btn_back").html(str);
				$("#btn_back").attr("title","返回首页");				
				$(document).on("click","#btn_back",function(){					
					window.location.href = "social_main.html";
				});
			}else{
				str = '<span class="glyphicon glyphicon-user" aria-hidden="true"></span>';
				$("#btn_back").html(str);				
			}
			
			$.init_commit(data.commitList);
			
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
			$("#container_work").removeClass("col-md-10 col-md-offset-1");
			$("#container_work").addClass("col-md-8 col-md-offset-2");
			$("#container_commit").removeClass("col-md-10 col-md-offset-1");
			$("#container_commit").addClass("col-md-8 col-md-offset-2");
        }else{
            $("#content_main").removeClass("col-md-12");
            $("#content_main").addClass("col-md-9");
			$("#container_work").removeClass("col-md-8 col-md-offset-2");
			$("#container_work").addClass("col-md-10 col-md-offset-1");	
			$("#container_commit").removeClass("col-md-8 col-md-offset-2");
			$("#container_commit").addClass("col-md-10 col-md-offset-1");	
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

    //删除文章
    $(document).on("click","#btn_to_delete",function(){
        $.confirm({
            type: 'orange',
            title: '删除文章!',
            content: '这篇文章删除后，无法找回',
            buttons: {
                confirm:{
                    text: '确认',
                    btnClass: 'btn-orange',
                    action: function(){
                        $.confirm({
                            title: '正在删除',
                            content: function () {
                                var self = this;
                                return $.ajax({
                                    type: "POST",
                                    url: "workdetail/deleteWorkById",
                                    data: {
                                        workId: $("#val_workId").text()
                                    },
                                    dataType: "json"
                                }).done(function (response) {
                                    if ("success" === response.outcome) {
                                        self.setType('green');
                                        self.buttons.backAction.addClass('btn-green');
                                        self.setTitle('文章成功删除');
                                        self.setContent('<h4>结束，是新的开始</h4><br/>conclusion,is to new gambit');
                                    } else {
                                        self.setType('red');
                                        self.buttons.backAction.addClass('btn-red');
                                        self.setTitle('删除失败');
                                        self.setContent(response.msg);
                                    }
                                    self.setContentAppend('<br/><br/>即将返回主页');
                                }).fail(function(){
                                    self.setType('red');
                                    self.setTitle('删除失败');
                                    self.setContent('Opp,数据传送出错了..');
                                    self.setContentAppend('<br/><br/>即将返回主页');
                                });
                            },
                            autoClose: 'backAction|7000',
                            onDestroy: function () {
                                // when the modal is removed from DOM
                                window.location.href = "/lightblog/mainpage/jumpToMianPage";
                            },
                            buttons: {
                                backAction: {
                                    text: '返回主页',
                                    action:function () {
                                        window.location.href = "/lightblog/mainpage/jumpToMianPage";
                                    }
                                }
                            }
                        });
                    }
                },
                cancel: {
                    text: '放弃',
                    btnClass: 'btn-green',
                    action: function(){
                    }
                }
            }
        });
    });
    // $("#btn_delete_work").click(function(){
    //     str = '<button id="btn_delete_work" type="button" class="btn btn-warning btn-block" disabled="disabled">正在删除</button>';
    //     $("#foot_modal_delete").html(str);
    //     $.ajax({
    //         type: "POST",
    //         url: "workdetail/deleteWorkById",
    //         data: {
    //             workId: $("#val_workId").text()
    //         },
    //         dataType: "json",
    //         success: function(data){
    //             console.log(data);
    //             if ("success" === data.outcome) {
    //                 str = '<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>文章删除成功！';
    //                 $("#body_modal_delete").html(str);
    //                 str = '<button id="btn_to_home" type="button" class="btn btn-success btn-block"">回到主页</button>';
    //                 $("#foot_modal_delete").html(str);
    //             } else {
    //                 str = '<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>文章删除失败'+data.msg;
    //                 $("#body_modal_delete").html(str);
    //                 str = '<button id="btn_to_home" type="button" class="btn btn-success btn-block"">回到主页</button>';
    //                 $("#foot_modal_delete").html(str);
    //             }
    //         },
    //         error: function(jqXHR){
    //             worning_msg = "发生错误：" + jqXHR.status;
    //             alert(worning_msg);
    //         },
    //     });
    // });

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
	
	//配置tooltip
	$("[data-toggle='tooltip']").tooltip();
	
	//点赞按钮
	$(document).on("click","#block_thumbUp",function(){
		$.ajax({
        type: "POST",
        url: "workdetail/thumbUpToggle",
        data: {
            workId: workId
        },
        dataType: "json",
        success: function(data){
			if("success"===data.outcome){
				console.log(data.msg);
				if("点赞成功！"===data.msg){
					console.log("点亮赞");
					$("#block_thumbUp").removeClass("button_sleep");
					$("#block_thumbUp").addClass("button_active");
				}else{
					$("#block_thumbUp").removeClass("button_active");
					$("#block_thumbUp").addClass("button_sleep");
				}  
			}else{
				alert(data.msg);
			}
                    
        },
        error: function(jqXHR){
            worning_msg = "发生错误：" + jqXHR.status;
            console.log(worning_msg);
            alert(worning_msg);
        },
    });
	});
	//推荐按钮
	$(document).on("click","#block_share",function(){
		$.ajax({
        type: "POST",
        url: "workdetail/shareToggle",
        data: {
            workId: workId
        },
        dataType: "json",
        success: function(data){
			if("success"===data.outcome){
				console.log(data.msg);
				if("推荐成功！"===data.msg){
					$("#block_share").removeClass("button_sleep");
					$("#block_share").addClass("button_active");
				}else{
					$("#block_share").removeClass("button_active");
					$("#block_share").addClass("button_sleep");
				}  
			}else{
				alert(data.msg);
			}
                    
        },
        error: function(jqXHR){
            worning_msg = "发生错误：" + jqXHR.status;
            console.log(worning_msg);
            alert(worning_msg);
        },
    });
	});
	
	//提交评论
	$(document).on("keydown","#input_commit",function(event){
        if((event.which == "13")&&(event.ctrlKey)){
			if($("#input_commit").val().length>0){			
				console.log("--ajax 提交评论");            
				$.ajax({
					type: "POST",
					url: "workdetail/commitSubmit",
					data: {
						actType: "评论",
						commit: $("#input_commit").val(),						
						workId: workId
					},
					dataType: "json",		
					success: function(data){
						console.log(data);
						if("success"===data.outcome){
							console.log("评论提交成功！");
							var url_to_personal_page = '/lightblog/mainpage/jumpToMianPage?ownerId='+data.simpleCommitBean.actorId;
							str = '<li class="media"><div class="media-left"><a href="'+url_to_personal_page+'"><img class="media-object head_icon" src="'+data.simpleCommitBean.actorHeadIconPath+'"></a></div><div class="media-body"><div class="pull-right text-muted">'+data.simpleCommitBean.actGenerateTime+'</div><div><a href="'+url_to_personal_page+'"><strong>'+data.simpleCommitBean.actorName+'</strong></a></div><div id="text_'+data.simpleCommitBean.actId+'" class="text">'+data.simpleCommitBean.commitContent+'</div><div class="thin_grey_bottom_border"><a onclick="javascript:to_edit_commit('+data.simpleCommitBean.actId+');return false;" href="#">编辑</a><a onclick="javascript:delete_commit('+data.simpleCommitBean.actId+');return false;" href="#">删除</a></div>';
							$("#container_commit").prepend(str);
							$("#input_commit").val("");
						}else{
							alert("Opp,评论提交出错了!");
						}
					},
					error: function(jqXHR){
						var worning_msg = "发生错误：" + jqXHR.status;
						alert(worning_msg);
					},
				});
			}            
        }
    });
	
	//提交回复
	$(document).on("keydown",".textarea_recommit",function(event){
        if((event.which == "13")&&(event.ctrlKey)){
			if($(this).val().length>0){			
				console.log("--ajax 提交回复");  
				var this_object = $(this);
				$.ajax({
					type: "POST",
					url: "workdetail/reCommitSubmit",
					data: {
						toActId: $(this).attr("id"),
						commit: $(this).val()
					},
					dataType: "json",		
					success: function(data){
						console.log(data);
						if("success"===data.outcome){
							console.log("回复成功！");							
							var url_to_personal_page = '/lightblog/mainpage/jumpToMianPage?ownerId='+data.recomitBean.actorId;
							var url_to_to_actor_page = '/lightblog/mainpage/jumpToMianPage?ownerId='+data.recomitBean.toActorId;
							str = '<li class="media"><div class="media-left"><a href="'+url_to_personal_page+'"><img class="media-object head_icon" src="'+data.recomitBean.actorHeadIconPath+'"></a></div><div class="media-body"><div class="pull-right text-muted">'+data.recomitBean.actGenerateTime+'</div><div><a href="'+url_to_personal_page+'"><strong>'+data.recomitBean.actorName+'</strong></a> <span class="text-muted">回复</span> <a   href="'+url_to_to_actor_page+'">'+data.recomitBean.toActorName+'</a></div><div id="text_'+data.recomitBean.actId+'" class="text">'+data.recomitBean.commitContent+'<p class="bg-info">'+data.recomitBean.toActContent+'</p></div><div class="thin_grey_bottom_border"><a onclick="javascript:to_edit_commit('+data.recomitBean.actId+');return false;" href="#">编辑</a><a onclick="javascript:delete_commit('+data.recomitBean.actId+');return false;" href="#">删除</a></div></div></li>';
							if(this_object.parent().parent().parent().parent().attr("id")==="container_commit"){
								if(this_object.parent().siblings(".media-list").length===0){
									str = '<ul class="media-list">'+str+'</ul>';
									this_object.parent().after(str);
								}else{
									this_object.parent().siblings(".media-list").prepend(str);
								}
							}else{
								console.log("已经处于子评论列表");
								this_object.parent().parent().parent().parent().prepend(str);
							}
							
							
							this_object.parent().remove();
						}else{
							alert("Opp,回复失败!");
						}
					},
					error: function(jqXHR){
						var worning_msg = "发生错误：" + jqXHR.status;
						alert(worning_msg);
					},
				});
			}            
        }
    });
	
	//提交修改后的回复/评论
	$(document).on("keydown",".textarea_edit_commit",function(event){
        if((event.which == "13")&&(event.ctrlKey)){
			if($(this).val().length>0){			
				console.log("--ajax 提交修改后的回复/评论"); 
				var id = $(this).attr("id");
				$.ajax({
					type: "POST",
					url: "workdetail/editCommitSubmit",
					data: {
						actId: $(this).attr("id"),
						commit: $(this).val()
					},
					dataType: "json",		
					success: function(data){
						console.log(data);
						if("success"===data.outcome){
							console.log("修改成功！");							
							$("#"+id).remove();
							$("#text_"+id).text(data.commit);							
							
						}else{
							alert("Opp,回复失败!");
						}
					},
					error: function(jqXHR){
						var worning_msg = "发生错误：" + jqXHR.status;
						alert(worning_msg);
					},
				});
			}            
        }
    });
	
});


$.extend({'init_commit':function(commitList){
	var str_comit_option_menu = '';	
	var str_sub_commit = '';
	var url_to_actor_page = '';
	var url_to_to_actor_page = '';
	var str = '';
	console.log("评论数据初始化");
	$.each(commitList,function(i,comitListItem){			
		console.log("定制子评论");
		$.each(comitListItem.recomitBeanList,function(i,recomit){
			str_sub_commit = '';
			console.log("定制评论操作菜单");
			url_to_to_actor_page = '/lightblog/mainpage/jumpToMianPage?ownerId='+recomit.toActorId;
			url_to_actor_page = '/lightblog/mainpage/jumpToMianPage?ownerId='+recomit.actorId;
			str_comit_option_menu = $.get_str_option_menu_commit_by_Authority(recomit.commitEditAuthority,
																	 recomit.actId);
			str_sub_commit = str_sub_commit+'<li class="media"><div class="media-left"><a href="'+url_to_actor_page+'"><img class="media-object head_icon" src="'+recomit.actorHeadIconPath+'"></a></div><div class="media-body"><div class="pull-right text-muted">'+recomit.actGenerateTime+'</div><div><a href="'+url_to_actor_page+'"><strong>'+recomit.actorName+'</strong></a> <span class="text-muted">回复</span> <a   href="'+url_to_to_actor_page+'">'+recomit.toActorName+'</a></div><div id="text_'+recomit.actId+'" class="text">'+recomit.commitContent+'<p class="bg-info">'+recomit.toActContent+'</p></div><div class="thin_grey_bottom_border">'+str_comit_option_menu+'</div></div></li>';		
		});	
		str_sub_commit = '<ul class="media-list">'+str_sub_commit+'</ul>';
		console.log("定制评论操作菜单");				
		str_comit_option_menu = $.get_str_option_menu_commit_by_Authority(comitListItem.simpleCommitBean.commitEditAuthority,
																	 comitListItem.simpleCommitBean.actId);		
		console.log("拼装评论单元");
		url_to_actor_page = '/lightblog/mainpage/jumpToMianPage?ownerId='+comitListItem.simpleCommitBean.actorId;
		str = '<li class="media"><div class="media-left"><a href="'+url_to_actor_page+'"><img class="media-object head_icon" src="'+comitListItem.simpleCommitBean.actorHeadIconPath+'"></a></div><div class="media-body"><div class="pull-right text-muted">'+comitListItem.simpleCommitBean.actGenerateTime+'</div><div><a href="'+url_to_actor_page+'"><strong>'+comitListItem.simpleCommitBean.actorName+'</strong></a></div><div id="text_'+comitListItem.simpleCommitBean.actId+'" class="text">'+comitListItem.simpleCommitBean.commitContent+'</div><div class="thin_grey_bottom_border">'+str_comit_option_menu+'</div>'+str_sub_commit+'</div></li>';
		$("#container_commit").append(str);
	});
}
});

$.extend({'get_str_option_menu_commit_by_Authority':function(commitEditAuthority,commit_id){
	var comit_option_menu = '';
	if("不能回复,能编辑、删除"===commitEditAuthority){
			comit_option_menu = '<a onclick="javascript:to_edit_commit('+commit_id+');return false;" href="#">编辑</a>&nbsp;&nbsp;<a onclick="javascript:delete_commit('+commit_id+');return false;" href="#">删除</a>';
	} 
	if("能回复，不能编辑、删除"===commitEditAuthority){
			comit_option_menu = '<a onclick="javascript:to_re_commit('+commit_id+'); return false;" href="#">回复</a>';
	}
	return comit_option_menu;
}
});

function to_edit_commit(act_id_to_edit){
	console.log("to_edit_commit:begin");
	var commit = $("#text_"+act_id_to_edit).text();
	var str = '';
	str = '<textarea id="'+act_id_to_edit+'" class="form-control textarea_edit_commit" rows="1" maxlength="140"></textarea>';
	$("#text_"+act_id_to_edit).html(str);	
	$("#"+act_id_to_edit).val(commit);
}


function to_re_commit(act_id_to_re_commit){	
	var str = '';
	str = '<div><textarea id="'+act_id_to_re_commit+'"  class="form-control textarea_recommit" rows="1" maxlength="140" placeholder="输入你的回复，按Ctrl+Enter发布..."></textarea></div>';
	
	if($("#text_"+act_id_to_re_commit).siblings(".media-list").length===0){
		$("#text_"+act_id_to_re_commit).parent().append(str);
	}else{
		$("#text_"+act_id_to_re_commit).siblings(".media-list").before(str);
	}
							
	
	
}


function delete_commit(act_id_to_delete){
	$.ajax({
			type: "POST",
			url: "workdetail/deleteCommit",
			data: {
				actId: act_id_to_delete
			},
			dataType: "json",		
			success: function(data){
				console.log(data);
				if("success"===data.outcome){
					alert("评论删除成功！");
					$("#text_"+act_id_to_delete).parent().parent().remove();
				}else{
					alert("Opp,评论删除失败!");
				}
			},
			error: function(jqXHR){
				var worning_msg = "发生错误：" + jqXHR.status;
				alert(worning_msg);
			},
		});
}



