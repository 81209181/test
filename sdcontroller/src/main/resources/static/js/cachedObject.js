$(document).ready(function () {
    $('#cachedObjectTable').DataTable();
})

function reloadMethod(cacheName) {
    clearAllMsg();
    $.get('/cached/reloadCache/'+cacheName, function (res) {
        showInfoMsg("reload cache success.");
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        showErrorMsg(responseError);
    })
}
