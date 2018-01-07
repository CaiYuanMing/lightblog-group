// JavaScript Document
$(function(){
    //点击写作,跳转至文章编辑页
    $("#btn_to_edit").click(function(){

        window.location.href = "/lightblog/editPage.html";
    });
});
function jump_to_page_main(){
    var ownerId = $(".val_ownerId").text();
    if(ownerId === ""){
        window.location.href = "/lightblog/mainpage/jumpToMianPage";
    }else{
        window.location.href = "/lightblog/mainpage/jumpToMianPage?ownerId="+ownerId;
    }
}

function jump_to_page_worklist(){
    var ownerId = $(".val_ownerId").text();
    if(ownerId === ""){
        window.location.href = "/lightblog/worklist/jumpToWorkList";
    }else{
        window.location.href = "/lightblog/worklist/jumpToWorkList?ownerId="+ownerId;
    }
}

function jump_to_page_categorylist(){
    var ownerId = $(".val_ownerId").text();
    if(ownerId === ""){
        window.location.href = "/lightblog/categorylist/jumpToCategoryList";
    }else{
        window.location.href = "/lightblog/categorylist/jumpToCategoryList?ownerId="+ownerId;
    }
}


function jump_to_page_taglist(){
    var ownerId = $(".val_ownerId").text();
    if(ownerId === ""){
        window.location.href = "/lightblog/taglist/jumpToTagList";
    }else{
        window.location.href = "/lightblog/taglist/jumpToTagList?ownerId="+ownerId;
    }
}

function jump_to_page_about(){
    var ownerId = $(".val_ownerId").text();
    if(ownerId === ""){
        window.location.href = "/lightblog/about/jumpToAbout";
    }else{
        window.location.href = "/lightblog/about/jumpToAbout?ownerId="+ownerId;
    }
}

function jump_to_page_workdetail(workId){
    window.location.href = "/lightblog/workdetail/jumpToWorkDetail?workId="+workId;
}

function list_work_by_category(category,id_container_list){
    var ownerId = $(".val_ownerId").text();
    console.log("-Ajax:获取ownerId = "+ownerId+" category= "+category+" 的文章列表");
	$.ajax({
        type: "POST",
        url: "worklist/listByCategory",
        data: {
            userId: ownerId,
            category: category
        },
        dataType: "json",
        success: function(data){
            console.log(data);
        },
        error: function(jqXHR){
            var worning_msg = "发生错误：" + jqXHR.status;
            alert(worning_msg);
        },
    });
}