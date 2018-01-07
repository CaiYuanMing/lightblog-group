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