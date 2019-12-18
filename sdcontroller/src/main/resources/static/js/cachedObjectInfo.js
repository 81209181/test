$(function(){
    $.get('/cached/getCacheInfo/'+cacheId,function(res){
        $('.old_detail').text(res);
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        showErrorMsg(responseError);
    })
})

function getReloadCache(){
    clearAllMsg();
    $.get('/cached/getReloadedCacheInfo/'+cacheId,function(res){
        $('.new_detail').text(res);
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        showErrorMsg(responseError);
    })
}