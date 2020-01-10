var cacheName = $('#cacheName').val();

$(function(){
    $.get('/cached/getCacheInfo/'+cacheName,function(res){
        $('#current_object').text(res[0]);
        $('#new_object').text(res[1]);
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        showErrorMsg(responseError);
    })
})

function refresh(){
    clearAllMsg();
    $.get('/cached/reloadCache/'+cacheName,function(res){
        if(res == true){
            location.reload();
        }
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        showErrorMsg(responseError);
    })
}