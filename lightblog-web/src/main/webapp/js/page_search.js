// JavaScript Document
$(function(){
	var str;
	//分类输入框，输入提示
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