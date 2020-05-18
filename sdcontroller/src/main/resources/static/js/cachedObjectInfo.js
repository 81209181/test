var cacheName = $('#cacheName').val();

$(function(){
    $.get('/cached/getCacheInfo/'+cacheName,function(res){
        var currentObject = "";
        var newObject = "";
        if (typeof res[0] == 'string' && typeof res[1] == 'string') {
            try {
                currentObject = JSON.stringify(JSON.parse(res[0]), null, 2);
            } catch(e) {
                currentObject = res[0];
            }
            try{
                newObject = JSON.stringify(JSON.parse(res[1]), null, 2);
            } catch(e) {
                newObject = res[1];
            }
        } else {
            currentObject = res[0];
            newObject = res[1];
        }
        $('#current_object').text(currentObject);
        $('#new_object').text(newObject);
        leftBlockId = $('#current_object').attr('id');
        rightBlockId = $('#new_object').attr('id');
        $(document).jdd(leftBlockId, rightBlockId);
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
