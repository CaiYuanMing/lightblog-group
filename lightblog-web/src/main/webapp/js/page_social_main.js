// JavaScript Document
//为安全性，默认主页访问模式为游客模式，无法修改主页任何信息
var visitType = "visitor";
var str = "";
var worning_msg = "";
var pageNature = "social";
$(function(){
    //初始化页面
    $.ajax({
        type: "POST",
        url: "socialMain/init",
        data: {
            purpose: "init"
        },
        dataType: "json",
        success: function(data){
            console.log(data);
            console.log("主页初始化请求成功并接收到参数，开始初始化...");          

            if ("visitor" === data.visitType) {
				
                console.log("确认是游客访问，设置visitType = visitor");
                visitType = "visitor";                
				console.log("初始化主页为游客内容");
				
				console.log("初始化导航栏");				
				str = '<li><a href="javascript:jump_to_page_login_register();">登录 / 注册 </a></li>';
				$("#block_info_user").html(str);
				
				console.log("精选各分类热度文章，在瀑布流中展示");	
				
            }else{
				console.log("确认已登录，设置visitType = user，显示写作入口");
                visitType = "user";
                str = '<div id="btn_to_edit" class="hidden-xs" data-toggle="tooltip" title="开始写作"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></div>'
				$("#btn_to_head").before(str);
								
				console.log("初始化主页为用户内容");
				
				console.log("初始化导航栏");
				
				str = '<li><a href="javascript:#;">文章推荐 </a></li><li><a href="javascript:#;">博友推荐 </a></li>';
				$("#block_nav_tab").append(str);
				
				str = '<li><a href="javascript:#;"><span class="glyphicon glyphicon-bell" aria-hidden="true"></span>  通知 <span class="badge alert-success">'+data.toReadInterActSum+'</span></a></li><li><a href="javascript:#;"><span class="glyphicon glyphicon-envelope" aria-hidden="true"></span>  私信 <span class="badge alert-success">4</span></a></li><li><a href="/lightblog/mainpage/jumpToMianPage"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>  '+data.userName+' </a></li>';
				$("#block_info_user").html(str);
				
				console.log("订阅用户的文章，在瀑布流中展示");	
			}
				$.init_workflow(data.workTempList);
        },
        error: function(jqXHR){
            worning_msg = "Opp，发生错误：" + jqXHR.status;
            console.log(worning_msg);
            alert(worning_msg);
        },
    });

	//搜索输入框，输入提示
    $('#input_search').typeahead({
        source: function (query, process) {
            console.log("正在匹配搜索关键字");
            console.log("query = "+query);
            $.ajax({
                url: 'socialMain/searchTip',
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
	
	$(document).on("keydown","#input_search",function(event){
        if(event.which == "13"){
			if($("#input_search").val().length>0){
				console.log("input_tag : enter press ----");
				console.log("--ajax 搜索‘"+$("#input_search").val()+"’");
				$.ajax({
					type: "POST",
					url: "socialMain/searchWorkListByTitleKey",
					data: {
						titleKey: $("#input_search").val()
					},
					dataType: "json",
		//			async: false,    // 使用同步操作
		//            timeout : 50000, //超时时间：50秒
					success: function(data){
						console.log(data);
						$.init_workflow(data.workTempList);
					},
					error: function(jqXHR){
						var worning_msg = "发生错误：" + jqXHR.status;
						alert(worning_msg);
					},
				});
			}
            
        }

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
//文章数据渲染到页面
$.extend({'init_workflow':function(workTempList){
	console.log("进入文章数据渲染");
	$("#container_work").html("");
	var url_to_personal_page = '';
    $.each(workTempList,function(i,work){
		//标签处理
		var tag = "";
		$.each(work.tagList,function(i,tagName){
			if(i===0){
				tag = tagName;
			}else{
				tag = tag+","+tagName;
			}						
		});
		url_to_personal_page = '/lightblog/mainpage/jumpToMianPage?ownerId='+work.authorId;
		str = '<div id="'+work.workId+'" class="card_article text-justify"><div><div class="pull-left"><a href="'+url_to_personal_page+'"><img class="media-object head_icon" src="'+work.authorHeadIconPath+'"></a></div><div><div class="text-left"><a href="'+url_to_personal_page+'">'+work.authorName+'</a></div><br/><span class="text-left text-muted">'+work.workGenerateTime+'</span></div></div><div class="content_card_title h4"><a href="javascript:jump_to_page_workdetail('+work.workId+');">'+work.title+'</a></div><p>'+work.summary+'</p><p class="text-muted h6"><span class="glyphicon glyphicon-book" aria-hidden="true"></span> '+work.category+'</p><p class="text-muted h6"><span class="glyphicon glyphicon-tag" aria-hidden="true"></span> '+tag+'</p><div  class="inline_block float_none magin_top_15 text-muted"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>'+work.browserSum+' <span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span>'+work.praiseSum+' <span class="glyphicon glyphicon-comment" aria-hidden="true"></span>'+work.comitSum+' <span class="glyphicon glyphicon-send" aria-hidden="true"></span>'+work.shareSum+'</div></div>  ';

		$("#container_work").append(str);

	});
}
});