// JavaScript Document
$(function(){
    //点击写作,跳转至文章编辑页  
        
	$(document).on("click","#btn_to_edit",function(){
		var pageNature = $("#pageNature").text();
		window.location.href = "/lightblog/editor/newEdit?pageNature="+pageNature;
    });
	
	//"阅读全文"动画效果
	$(document).on("mouseenter",".button_to_complie_article",function(){
        $(this).css({
            "display": "inline-block",
            "background-color": "black",
            "color":" white",
            "padding": "5px 10px"
        });
    });
    $(document).on("mouseleave",".button_to_complie_article",function(){
        $(this).css({
            "display": "inline-block",
            "border": "1px solid #000000",
            "border-width":" 2px",
            "background-color": "white",
            "color":"black",
            "padding": "5px 10px"
        });
    });
	
	$(document).on("mouseenter",".button_over_interact",function(){
        $(this).css({
            "display": "inline-block",
            "background-color": "black",
            "color":" white",
            "padding": "5px 10px"
        });
    });
    $(document).on("mouseleave",".button_over_interact",function(){
        $(this).css({
           	"display": "inline-block",
			"border": "1px solid #000000",
			"background-color": "white",
			"color":"black",
			"padding":"5px 10px",
			"margin-bottom": "20px"
        });
    });
});
function jump_to_page_login_register(){    
        window.location.href = "login_register.html";    
}

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
    var ownerId = $(".val_ownerId").text();
	var pageNature = $("#pageNature").text();
    if(ownerId === ""){
//        window.location.href = "/lightblog/workdetail/jumpToWorkDetail?workId="+workId+"&pageNature="+pageNature;
		window.open("/lightblog/workdetail/jumpToWorkDetail?workId="+workId+"&pageNature="+pageNature);
    }else{
//        window.location.href = "/lightblog/workdetail/jumpToWorkDetail?workId="+workId+"&ownerId="+ownerId+"&pageNature="+pageNature;
		window.open("/lightblog/workdetail/jumpToWorkDetail?workId="+workId+"&ownerId="+ownerId+"&pageNature="+pageNature);
    }

}

function jump_to_page_search() {
    var ownerId = $(".val_ownerId").text();
    if(ownerId === ""){
        window.location.href = "/lightblog/search/jumpToSearch";
    }else{
        window.location.href = "/lightblog/search/jumpToSearch?ownerId="+ownerId;
    }
}



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
