$(function(){
    $.get('/cached/getCacheInfo/'+cacheName,function(res){
        $('.old_detail').text(res);
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        showErrorMsg(responseError);
    })
})

function getReloadCache(){
    clearAllMsg();
    $.get('/cached/reloadCache/'+cacheName,function(res){
        if(res == true){
            $.get('/cached/getCacheInfo/'+cacheName,function(res){
                $('.new_detail').text(res);
            }).fail(function(e){
                var responseError = e.responseText ? e.responseText : "Get failed.";
                showErrorMsg(responseError);
            })
        }
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        showErrorMsg(responseError);
    })
}