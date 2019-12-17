$(document).ready(function () {
    $('#cachedObjectTable').DataTable();
})

function reloadMethod(cacheId) {
    clearAllMsg();
    $.get('/cached/reloadCache/'+cacheId, function (res) {
        showInfoMsg("reload cache success.");
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        showErrorMsg(responseError);
    })
}
