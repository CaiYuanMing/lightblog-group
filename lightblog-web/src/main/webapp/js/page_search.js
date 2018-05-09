// JavaScript Document
$(function(){
	var str;
    //初始化页面
    $.ajax({
        type: "POST",
        url: "search/init",
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
            $(".name_owner").text(data.ownerName);
            $(".introduction_owner").text(data.ownerIntroduction);
        },
        error: function(jqXHR){
            worning_msg = "发生错误：" + jqXHR.status;
            alert(worning_msg);
        },
    });
	//搜索输入框，输入提示
    $('#input_search').typeahead({
        source: function (query, process) {
            console.log("正在匹配搜索关键字");
            console.log("query = "+query);
            $.ajax({
                url: 'search/searchTip',
                type: 'POST',
                dataType: 'JSON',
                data: {
                    query:query,
					userId: $(".val_ownerId").text()
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
            console.log("input_tag : enter press ----");
            console.log("--ajax 搜索‘"+$("#input_search").val()+"’");
			$.ajax({
            type: "POST",
            url: "search/searchWorkListByTitle",
            data: {
                title: $("#input_search").val(),
				userId: $(".val_ownerId").text()
            },
            dataType: "json",
            success: function(data){
                console.log(data);
                $("#container_list_work").html("");                
                $.each(data.workListByTitle,function(i,workListItemMap){
                    str = '<div class="item_list_work text-justify"><span class="glyphicon glyphicon-leaf" aria-hidden="true"></span><a href="javascript:jump_to_page_workdetail('+workListItemMap.workId+');"> '+workListItemMap.workTitle+'	</a></div><hr/>';
                    $("#container_list_work").append(str);
                });
            },
            error: function(jqXHR){
                var worning_msg = "发生错误：" + jqXHR.status;
                alert(worning_msg);
            },
        });
        }

    });
});