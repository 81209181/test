function showInfoMsg(msg){
    showInfoMsg(msg, true);
}
function showInfoMsg(msg, scrollToTop){
    msg = "[INFO][" + getFormattedTimestamp() + "] " + msg;
    $("#infoMsg").html(msg);

    if(scrollToTop) {
        $("html, body").animate({scrollTop: 0}, "fast");
    }
    $("#infoMsg").slideDown(200);
}

function hideInfoMsg(){
    $("#infoMsg").html('');
    $("#infoMsg").hide();
}

function showWarningMsg(msg){
    msg = "[WARNING][" + getFormattedTimestamp() + "] " + msg;
    $("#warningMsg").html(msg);
    $("html, body").animate({ scrollTop: 0 }, "fast");
    $("#warningMsg").slideDown(200);
}

function hideWarningMsg(){
    $("#warningMsg").html('');
    $("#warningMsg").hide();
}

function showErrorMsg(msg){
    // redirect, if needed
    let inputJson = safeParseJson(msg);
    if(inputJson!=null) {
        let jsonResponse = inputJson.hasOwnProperty("responseJSON") ? inputJson.responseJSON : inputJson;
        let status = jsonResponse.status;
        let message = jsonResponse.message;
        let exceptionType = jsonResponse.error;

        let ctx = $("meta[name='_ctx']").attr("content");
        if (status === 403) {
            if (message === "Forbidden" || message === "TIMEOUT") {
                window.location.href = ctx + "/login?error=" + message;
                return;
            } else if (message === "INSUFFICIENT_AUTH" || message === "HELP") {
                showErrorMsg("Insufficient authority.");
                return;
            }
        } else if (status !== 200) {
            if (exceptionType !== null) {
                msg = exceptionType;
            }
        }
    }

    // display as message
    msg = "[ERROR][" + getFormattedTimestamp() + "] " + msg;
    $("#errorMsg").html(msg);
    $("html, body").animate({ scrollTop: 0 }, "fast");
    $("#errorMsg").slideDown(200);
}

function hideErrorMsg(){
    $("#errorMsg").html('');
    $("#errorMsg").hide();
}

function clearAllMsg() {
    hideInfoMsg();
    hideWarningMsg();
    hideErrorMsg();
}

function getFormattedTimestamp() {
    var NOW = new Date();
    var fullDate = NOW.getFullYear() + "/" + pad(NOW.getMonth()+1,2) + "/" + pad(NOW.getDate(),2);
    var time = pad(NOW.getHours(),2) + ":" + pad(NOW.getMinutes(),2) + ":" + pad(NOW.getSeconds(),2);
    return  fullDate + " " + time;
}

function pad(number, length) {
    var str = '' + number;
    while (str.length < length) {
        str = '0' + str;
    }
    return str;
}